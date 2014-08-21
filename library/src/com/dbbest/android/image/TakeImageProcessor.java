
package com.dbbest.android.image;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import com.dbbest.android.analytics.L;
import com.dbbest.android.image.listener.ImagePicked;
import com.dbbest.android.image.listener.ImageProcessResult;
import com.dbbest.android.image.task.ImageBitmapProcessAsyncTask;
import com.dbbest.android.image.task.ImageProcessAsyncTask;
import com.dbbest.android.image.task.ImageUriProcessAsyncTask;

import java.io.File;
import java.lang.ref.WeakReference;

public class TakeImageProcessor {
    private static final String TAG = TakeImageProcessor.class.getSimpleName();
    private static final int SINGLE_ITEM = 1;

    public static final int CAMERA_REQUEST_CODE = 501;
    public static final int GALLERY_REQUEST_CODE = 502;
    public static final int CROP_REQUEST_CODE = 503;

    private WeakReference<Context> mContextRef;
    private ImagePicked imageListener;
    private ImageProcessResult imageProcessResultListener;

    private Uri currentImageDestinationUri;
    private boolean isOutputUriSpecified;

    public TakeImageProcessor(Context context) {
        mContextRef = new WeakReference<Context>(context);
    }

    public void setImagePickedListener(ImagePicked listener) {
        this.imageListener = listener;
    }

    public void setImageProcessResultListener(ImageProcessResult listener) {
        this.imageProcessResultListener = listener;
    }

    public Intent createTakeFromCameraIntent(Uri outputUri) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        specifiedOutputUriForIntent(cameraIntent, outputUri);

        return cameraIntent;
    }

    public Intent createCropImageIntent(Uri inputImageUri, Uri outputImageUri) {
        final Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inputImageUri, "image/*");
//        intent.putExtra("outputX", 400);
//        intent.putExtra("outputY", 400);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);

        if (inputImageUri.equals(outputImageUri)) {
            specifiedOutputUriForIntent(intent, outputImageUri);
        }
        return intent;
    }

    public Intent createTakeFromGalleryIntent(Uri galleryUri) {
        final Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        if (galleryUri != null) {
            galleryIntent.setData(galleryUri);
        }

        return galleryIntent;
    }

    public void onHandleActivityResult(int requestCode, int resultCode, Intent intent) throws DeliveryResultException, NullPointerException {
        Context processContext = mContextRef.get();

        if (processContext == null) {
            L.e(new NullPointerException("Context is null"));

            return;
        }
        if (intent == null && (!isOutputUriSpecified)) {
            L.e(new NullPointerException("Result intent is null with no specified output image Uri"));

            return;
        }

        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                L.d(TAG, "CAMERA request");
                if (resultCode != Activity.RESULT_OK) {
                    throwDeliveryException("Activity abort");
                }

                if (isOutputUriSpecified) {
                    L.i(TAG, "Camera image picked with specified Uri");

                    imageListener.onImagePicked(requestCode, currentImageDestinationUri);
                } else {
                    L.i(TAG, "Camera image picked with Uri from native gallery");

                    Uri cameraImageUri = intent.getData();
                    if (cameraImageUri == null) {
                        L.d(TAG, "Image uri from gallery is null");

                        throw new NullPointerException("Image uri is null");
                    }

                    File imageFileInFileSystem = getImageFileFromNativeGallery(processContext, cameraImageUri);
                    if (cameraImageUri != null && imageListener != null) {
                        imageListener.onImagePicked(requestCode, Uri.fromFile(imageFileInFileSystem));
//                        imageListener.onImagePicked(requestCode, cameraImageUri);
                    }
                }

                break;
            case GALLERY_REQUEST_CODE:
                L.d(TAG, "GALLERY request");
                if (resultCode != Activity.RESULT_OK) {
                    throwDeliveryException("Activity abort");
                }

                Uri galleryUri = intent.getData();

                if (galleryUri == null) {
                    L.d(TAG, "Image uri from gallery is null");

                    throw new NullPointerException("Image uri is null");
                }

                File imageFileInFileSystem = getImageFileFromNativeGallery(processContext, galleryUri);
                if (imageListener != null) {
                    imageListener.onImagePicked(requestCode, Uri.fromFile(imageFileInFileSystem));
                }

                break;
            case CROP_REQUEST_CODE:
                L.d(TAG, "CROP request");
                if (resultCode != Activity.RESULT_OK) {
                    throwDeliveryException("Activity abort");
                }

                Bitmap resultImageBmp = intent.getExtras().getParcelable("data");
                if (!isOutputUriSpecified) {
                    throw new NullPointerException("Image destination uri - null, see setCurrentCropDestinationUri method");
                }
                savePickedImageTo(resultImageBmp, currentImageDestinationUri);

                break;
        }
    }

    public void savePickedImageTo(Uri sourceUri, Uri destinationUri) {
        Context contextToProcess = mContextRef.get();
        if (contextToProcess != null) {
            ImageProcessAsyncTask processImage = new ImageUriProcessAsyncTask(destinationUri);
            if (imageProcessResultListener != null) {
                processImage.setImageProcessResultListener(imageProcessResultListener);
            }

            processImage.execute(sourceUri);
        }
    }

    public void savePickedImageTo(Bitmap source, Uri destinationUri) {
        Context contextToProcess = mContextRef.get();
        if (contextToProcess != null) {
            ImageBitmapProcessAsyncTask processImage = new ImageBitmapProcessAsyncTask(destinationUri);
            if (imageProcessResultListener != null) {
                processImage.setImageProcessResultListener(imageProcessResultListener);
            }

            processImage.execute(source);
        }
    }

    private void throwDeliveryException(String message) throws DeliveryResultException {
        L.d(TAG, message);

        throw new DeliveryResultException(message);
    }

    private void specifiedOutputUriForIntent(Intent intent, Uri outputImageUri) {
        if (outputImageUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputImageUri);

            currentImageDestinationUri = outputImageUri;
            isOutputUriSpecified = true;
        }
    }

    private File getImageFileFromNativeGallery(Context context, Uri galleryContentUri) throws NullPointerException {
        ContentResolver resolver = context.getContentResolver();
        Cursor galleryImageCursor = resolver.query(galleryContentUri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);

        if (galleryImageCursor.getCount() == SINGLE_ITEM) {
            galleryImageCursor.moveToFirst();

            int dataColumnIndex = galleryImageCursor.getColumnIndex(MediaStore.MediaColumns.DATA);
            String imagePathFromGallery = galleryImageCursor.getString(dataColumnIndex);
            galleryImageCursor.close();

            File imageFile = new File(imagePathFromGallery);
            return imageFile;
        } else {
            throw new NullPointerException("No image found from native gallery");
        }
    }
}
