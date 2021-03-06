package com.utils.framework.system;

/**
 * Created by Tikhonenko.S on 04.10.13.
 */
public class SystemUtilities {
    private static final int GC_WORKING_INTERVAL = 2000;

    public static void runOutOfMemoryErrorCriticalCode(Runnable runnable,
                                                       String outOfMemoryMessage)
            throws OutOfMemoryException {
        try {
            runnable.run();
        } catch (OutOfMemoryError e) {
            System.gc();
            try {
                //wait for gc processing
                Thread.sleep(GC_WORKING_INTERVAL);
            } catch (InterruptedException e1) {
                throw new RuntimeException(e1);
            }
            // try again
            try {
                runnable.run();
            } catch (OutOfMemoryError secondE) {
                throw new OutOfMemoryException(outOfMemoryMessage, secondE);
            }
        }
    }

    public static String execCmd(String cmd) throws java.io.IOException {
        Process proc = Runtime.getRuntime().exec(cmd);
        java.io.InputStream is = proc.getInputStream();
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        String val = "";
        if (s.hasNext()) {
            val = s.next();
        } else {
            val = "";
        }
        return val;
    }
}
