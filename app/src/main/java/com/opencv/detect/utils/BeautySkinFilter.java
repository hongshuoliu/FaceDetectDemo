package com.opencv.detect.utils;

/**
 * 功能描述：
 *
 * @author liuhongshuo
 * @date 2020-07-22
 */
public class BeautySkinFilter {


    public BeautySkinFilter() {
        System.loadLibrary("detection_based_tracker");
    }

    public native void initLoad(String haarFilePath);

    public native void faceDetection(long frameAddress);

    public native void beautySkinFilter(long srcAddress, long dstAddress, float sigma, boolean blur);

}
