package com.opencv.detect;

import android.os.Bundle;
import android.util.Log;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraGLSurfaceView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 功能描述：
 *
 * @author liuhongshuo
 * @date 2020-07-09
 */
public class FaceDetectActivity extends AppCompatActivity {

    private final static String TAG = "FaceDetectActivity";

    private CameraGLSurfaceView mOpenCvCameraView;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");

                    if (null != mOpenCvCameraView) {
                        mOpenCvCameraView.enableView();
                    }

                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detect);

        mOpenCvCameraView = (CameraGLSurfaceView) findViewById(R.id.cv_camera);
        mOpenCvCameraView.setCameraTextureListener(new CameraGLSurfaceView.CameraTextureListener() {
            @Override
            public void onCameraViewStarted(int width, int height) {
                Log.i(TAG, "onCameraViewStarted----------------------------");
                mOpenCvCameraView.enableView();
            }

            @Override
            public void onCameraViewStopped() {

            }

            @Override
            public boolean onCameraTexture(int texIn, int texOut, int width, int height) {
                return false;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
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
    }

}
