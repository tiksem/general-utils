package com.dbbest.android.image.listener;

public interface ImageProcessResult<Success, Error> {
    void onImageProcessedStart();

    void onImageProcessedSuccess(Success result);

    void onImageProcessedWithError(Error source);
}
