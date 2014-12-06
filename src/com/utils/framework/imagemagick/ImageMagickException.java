package com.utils.framework.imagemagick;

/**
 * Created by CM on 12/6/2014.
 */
public class ImageMagickException extends RuntimeException {
    public ImageMagickException() {
    }

    public ImageMagickException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageMagickException(String message) {
        super(message);
    }

    public ImageMagickException(Throwable cause) {
        super(cause);
    }

    public ImageMagickException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
