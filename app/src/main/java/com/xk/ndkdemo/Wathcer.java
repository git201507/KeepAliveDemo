package com.xk.ndkdemo;

/**
 * Created by aruba on 2020/5/20.
 */
public class Wathcer {
    public native void startDaemon(int userId);
    public static native void stopDuplicateService(int userId);
}
