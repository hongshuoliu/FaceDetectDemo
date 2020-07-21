package com.opencv.detect;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.opencv.detect.utils.FacePresenter;
import com.opencv.detect.widget.MyCvCameraView;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.Collections;
import java.util.List;

/**
 * 功能描述：
 *
 * @author liuhongshuo
 * @date 2020-07-09
 */
public class FaceDetectActivity extends CameraBaseActivity {

    private final static String TAG = "FaceDetectActivity";

    private ImageView mCameraSwitch;

    private MyCvCameraView mOpenCvCameraView;

    private FacePresenter facePresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera);

        mOpenCvCameraView = new MyCvCameraView(this);
        mOpenCvCameraView.setCvCameraViewListener(this);
        ((ViewGroup) findViewById(R.id.camera_layout)).addView(mOpenCvCameraView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mCameraSwitch = (ImageView) findViewById(R.id.camera_switch);
        mCameraSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOpenCvCameraView.switchCamare();
            }
        });
    }

    @Override
    protected void loadOpenCVSuccess() {
        if (null != mOpenCvCameraView) {
//            mOpenCvCameraView.showFullPreview(true);
            mOpenCvCameraView.enableView();
        }
        facePresenter = new FacePresenter(FaceDetectActivity.this);
    }

    @Override
    protected void loadOpenCVFail() {

    }

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCvCameraView);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }

        if (null != facePresenter) {
            facePresenter.onRelease();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat frame = inputFrame.rgba();
        if (mOpenCvCameraView.isFrontCamare()) {
            Core.flip(frame, frame, 1);
        }
        if (null != facePresenter) {
            facePresenter.process(frame);
        }
        return frame;
    }

}