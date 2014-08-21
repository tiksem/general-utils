package com.dbbest.android.media;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.*;
import android.net.Uri;
import android.os.Build;
import com.dbbest.android.threading.*;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * User: Tikhonenko.S
 * Date: 24.12.13
 * Time: 18:20
 */
public class MediaUtils {
    private static final int MAX_SAMPLE_SIZE = 256 * 1024;
    private static final int MINIMUM_MUXER_SUPPORTED_SDK = 18;

    public interface DurationResult{
        void onResult(boolean success, int duration);
    }

    public static Cancelable tryGetMediaFileDuration(
            final DurationResult onFinish,
            final Context context,
            final String filePath,
            long maxWaitTime){
        final BackgroundLoopEvent backgroundLoopEvent = Tasks.waitForResult(new ResultLoop<Integer>() {
            private MediaPlayer mediaPlayer;

            @Override
            public boolean resultIsReady() {
                mediaPlayer = MediaPlayer.create(context, Uri.fromFile(new File(filePath)));
                return mediaPlayer != null;
            }

            @Override
            public Integer getResult() {
                return mediaPlayer.getDuration();
            }

            @Override
            public void handleResult(Integer result) {
                mediaPlayer.release();
                onFinish.onResult(true, result);
            }

            @Override
            public void onTimeIsUp() {
                onFinish.onResult(false, -1);
            }
        });

        backgroundLoopEvent.setMaxRunningTime(maxWaitTime);

        return new Cancelable() {
            @Override
            public void cancel() {
                backgroundLoopEvent.setOnStop(null);
                backgroundLoopEvent.stop();
            }
        };
    }

    private static int[] generateMuxerIndexMap(MediaMuxer muxer, MediaExtractor extractor){
        int trackCount = extractor.getTrackCount();

        // Set up the tracks.
        int[] indexMap = new int[trackCount];
        for (int i = 0; i < trackCount; i++) {
            extractor.selectTrack(i);
            MediaFormat format = extractor.getTrackFormat(i);
            int dstIndex = muxer.addTrack(format);
            indexMap[i] =  dstIndex;
        }

        return indexMap;
    }

    private static void writeSampleData(MediaMuxer muxer, MediaExtractor extractor, int[] indexMap){
        // Copy the samples from MediaExtractor to MediaMuxer.
        boolean sawEOS = false;
        int bufferSize = MAX_SAMPLE_SIZE;
        int frameCount = 0;
        int offset = 100;

        ByteBuffer dstBuf = ByteBuffer.allocate(bufferSize);
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

        while (!sawEOS) {
            bufferInfo.offset = offset;
            bufferInfo.size = extractor.readSampleData(dstBuf, offset);

            if (bufferInfo.size < 0) {
                sawEOS = true;
                bufferInfo.size = 0;
            } else {
                bufferInfo.presentationTimeUs = extractor.getSampleTime();
                bufferInfo.flags = extractor.getSampleFlags();
                int trackIndex = extractor.getSampleTrackIndex();

                muxer.writeSampleData(indexMap[trackIndex], dstBuf,
                        bufferInfo);
                extractor.advance();

                frameCount++;
            }
        }
    }

    // Works on Android 4.3 anf higher
    public static void concatVideoAndAudio(String audioSource, String videoSource, String destinationMedia) throws IOException {
        if(Build.VERSION.SDK_INT < MINIMUM_MUXER_SUPPORTED_SDK){
            throw new UnsupportedOperationException("concatVideoAndAudio is supported on Android 4.3 and higher");
        }

        // Set up MediaMuxer for the destination.
        MediaMuxer muxer = null;
        try {
            muxer = new MediaMuxer(destinationMedia, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

            MediaExtractor audioExtractor = new MediaExtractor();
            audioExtractor.setDataSource(audioSource);
            MediaExtractor videoExtractor = new MediaExtractor();
            videoExtractor.setDataSource(videoSource);

            int[] audioIndexMap = generateMuxerIndexMap(muxer, audioExtractor);
            int[] videoIndexMap = generateMuxerIndexMap(muxer, videoExtractor);

            muxer.start();

            writeSampleData(muxer, audioExtractor, audioIndexMap);
            writeSampleData(muxer, videoExtractor, videoIndexMap);
        } finally {
            if (muxer != null) {
                muxer.stop();
                muxer.release();
            }
        }
    }
}
