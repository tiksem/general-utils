package com.utils.framework.imagemagick;

import com.utils.framework.strings.Strings;
import com.utils.framework.system.SystemUtilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CM on 12/6/2014.
 */
public class ImageMagickExecutor {
    private String imageMagickExecutablePath;

    public ImageMagickExecutor(String imageMagickExecutablePath) {
        if (!Strings.isEmpty(imageMagickExecutablePath)) {
            imageMagickExecutablePath += "/";
        }

        this.imageMagickExecutablePath = imageMagickExecutablePath;
    }

    private void checkFilePath(String imagePath) throws FileNotFoundException {
        if (!new File(imagePath).exists()) {
            throw new FileNotFoundException(imagePath);
        }
    }

    public Size getImageSize(String imagePath) throws IOException {
        checkFilePath(imagePath);
        String command = imageMagickExecutablePath + "identify " + imagePath;
        String output = SystemUtilities.execCmd(command);

        Size size = null;
        try {
            String[] dimensions = output.split(" +")[2].split("x", 2);
            size = new Size();
            size.width = Integer.parseInt(dimensions[0]);
            size.height = Integer.parseInt(dimensions[1]);
        } catch (Exception e) {
            throw new ImageMagickException(output, e);
        }

        return size;
    }

    private String execute(String destination, String command) throws IOException {
        String output = SystemUtilities.execCmd(command);
        if (!new File(destination).exists()) {
            throw new ImageMagickException(output);
        }

        return output;
    }

    public class Image {
        private String source;
        private Size size;

        public Image(String source) throws IOException {
            this.source = source;
            size = getImageSize(source);
        }

        public Size getSize() {
            return size;
        }

        public String createSquareThumbnail(String destination, int dimension) throws IOException {
            checkFilePath(source);
            Size size = getImageSize(source);
            int min = Math.min(size.width, size.height);
            double k = (double) dimension / (double) min;

            int resultWidth = (int) Math.round(size.width * k);
            int resultHeight = (int) Math.round(size.height * k);

            String dimensions = resultWidth + "x" + resultHeight + "^";
            String thumbnailDimensions = dimension + "x" + dimension;
            String gravity = size.width >= size.height ? "center" : "north";
            String command = imageMagickExecutablePath
                    + "convert " + source +
                    " -resize " + dimensions +
                    " -gravity " + gravity +
                    " -extent " + thumbnailDimensions + " " +
                    destination;

            return execute(destination, command);
        }

        public String resizeProportionallyFitMax(Size max, String destination) throws IOException {
            if (size.width <= max.width && size.height <= max.height) {
                return source;
            }

            double k = (double) size.width / size.height;

            double widthDiff = (size.width - max.width) * k;
            double heightDiff = (double) (size.height - max.height) / k;

            boolean byHeight = Math.max(heightDiff, widthDiff) == heightDiff;
            String sizeArg;
            if (byHeight) {
                sizeArg = "x" + max.height;
            } else {
                sizeArg = max.width + "x";
            }

            String command = imageMagickExecutablePath
                    + "convert " + source +
                    " -resize " +
                    sizeArg +
                    " " + destination;

            execute(destination, command);
            return destination;
        }
    }

    public Image getImage(String path) throws IOException {
        return new Image(path);
    }
}
