package com.utils.framework.google.images;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CM on 11/20/2014.
 */
public class Params {
    private ImageColor imageColor;
    private ImageSize imageSize;
    private ImageType imageType;
    private int start = 0;
    private int count = -1;
    private boolean grayScale = false;

    public ImageColor getImageColor() {
        return imageColor;
    }

    public void setImageColor(ImageColor imageColor) {
        this.imageColor = imageColor;
    }

    public ImageSize getImageSize() {
        return imageSize;
    }

    public void setImageSize(ImageSize imageSize) {
        this.imageSize = imageSize;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;

    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isGrayScale() {
        return grayScale;
    }

    public void setGrayScale(boolean grayScale) {
        this.grayScale = grayScale;
    }

    public Map<String, Object> toQueryMap() {
        Map<String, Object> result = new HashMap<String, Object>();

        if (imageColor != null) {
            result.put("imgcolor", imageColor.name());
        }

        if (imageSize != null) {
            result.put("imgsz", imageSize.name());
        }

        if (imageType != null) {
            result.put("imgtype", imageType.name());
        }

        if (count >= 0) {
            result.put("imgtype", count);
        }

        if (start >= 0) {
            result.put("start", start);
        }

        if (grayScale) {
            result.put("imgc", "gray");
        }

        return result;
    }
}
