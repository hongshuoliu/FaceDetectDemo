package com.opencv.detect;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.opencv.detect.utils.FacePresenter;
import com.opencv.detect.widget.MyCvCameraView;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.Collections;
import java.util.List;


public class FaceDetectActivity extends CameraBaseActivity {


    private FrameLayout mCameraLayout;
    private ImageView mCameraSwitch;
    private MyCvCameraView mOpenCvCameraView;

    private FacePresenter facePresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        this.setTitle("人脸检测");
        initView();

        facePresenter = new FacePresenter(this);
    }

    @Override
    protected void loadOpenCVSuccess() {
        mOpenCvCameraView.enableView();
        mCameraSwitch = (ImageView) findViewById(R.id.camera_switch);
        mCameraSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOpenCvCameraView.switchCamare();
            }
        });
    }

    @Override
    protected void loadOpenCVFail() {

    }

    private void initView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mOpenCvCameraView = new MyCvCameraView(this);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.enableFpsMeter();
        // 前置摄像头开启预览
        mOpenCvCameraView.setFontCamare();

        mCameraLayout = (FrameLayout) findViewById(R.id.camera_layout);
        mCameraLayout.addView(mOpenCvCameraView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_face_detect, menu);
        return true;
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

        if (this.getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            if (mOpenCvCameraView.isFrontCamare()) {
                Core.flip(frame, frame, 1);
                Core.rotate(frame, frame, Core.ROTATE_90_CLOCKWISE);
            }
            else {
                Core.rotate(frame, frame, Core.ROTATE_90_CLOCKWISE);
            }
        }

        facePresenter.process(frame);
        return frame;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.face_trace_menu_id:
                facePresenter.updateActLevel(1);
                break;
            case R.id.eye_roi_menu_id:
                facePresenter.updateActLevel(2);
                break;
            case R.id.eye_location_menu_id:
                facePresenter.updateActLevel(3);
                break;
            case R.id.location_ball_menu_id:
                facePresenter.updateActLevel(4);
                break;
            case R.id.render_ball_menu_id:
                facePresenter.updateActLevel(5);
                break;
            default:
                facePresenter.updateActLevel(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
