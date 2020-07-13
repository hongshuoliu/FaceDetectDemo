package com.opencv.detect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "MainActivity";
    private static final int REQUEST_CAMERA = 101;
    private static final int REQUEST_STORAGE = 102;

    private Button mBtnObjectdetect;
    private Button mBtnFacedetect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnObjectdetect = (Button) findViewById(R.id.btn_objectdetect);
        mBtnObjectdetect.setOnClickListener(this);
        mBtnFacedetect = (Button) findViewById(R.id.btn_facedetect);
        mBtnFacedetect.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermission();
    }

    private void requestPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (Build.VERSION.SDK_INT >= 23 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, REQUEST_CAMERA);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_objectdetect:
                startActivity(FdActivity.class);
                break;
            case R.id.btn_facedetect:
                startActivity(FaceDetectActivity.class);
                break;
            case R.id.btn_test:
                startActivity(TestActivity.class);
                break;
            default:
                break;
        }
    }

    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
    }
}
