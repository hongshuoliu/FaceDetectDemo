package com.opencv.detect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.opencv.detect.callback.ImageFileInterface;
import com.opencv.detect.utils.BeautySkinPresenter;
import com.opencv.detect.utils.ImageUtils;
import com.opencv.detect.utils.ToastUtils;

import java.io.File;


public class ImageActivity extends BaseActivity implements ImageFileInterface, View.OnClickListener {
    private static final String TAG = "ImageActivity";

    private BeautySkinPresenter beautySkinPresenter;

    private ImageView mImgSrc;
    private ImageView mImgDst;

    private Button mBtnFilter1;
    private Button mBtnFilter2;
    private Button mBtnFilter3;
    private Button mBtnBeautyJava;
    private Button mBtnBeautyNative;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        this.setTitle("图片处理");

        try {
            File externalDir = this.getExternalFilesDir(null);
            ImageUtils.doCopy(this, "images", externalDir.getPath());
            String path = externalDir.getPath() + File.separator + "lena.png";
            if (new File(path).exists()) {
                fileUri = Uri.parse(path);
            } else {
                ToastUtils.show(this, "复制文件失败，请先选择照片");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        initView();
    }

    private void initView() {

        mImgSrc = (ImageView) findViewById(R.id.img_src);
        mImgDst = (ImageView) findViewById(R.id.img_dst);

        if (null != fileUri) {
            Bitmap bm = BitmapFactory.decodeFile(fileUri.getPath());
            mImgSrc.setImageBitmap(bm);
        }

        mBtnFilter1 = (Button) findViewById(R.id.btn_filter1);
        mBtnFilter2 = (Button) findViewById(R.id.btn_filter2);
        mBtnFilter3 = (Button) findViewById(R.id.btn_filter3);
        mBtnBeautyJava = (Button) findViewById(R.id.btn_beauty_java);
        mBtnBeautyNative = (Button) findViewById(R.id.btn_beauty_native);

        mBtnFilter1.setOnClickListener(this);
        mBtnFilter2.setOnClickListener(this);
        mBtnFilter3.setOnClickListener(this);
        mBtnBeautyJava.setOnClickListener(this);
        mBtnBeautyNative.setOnClickListener(this);
    }

    @Override
    public void onImageFileChanged(String filePath) {
        Bitmap bm = BitmapFactory.decodeFile(filePath);
        mImgSrc.setImageBitmap(bm);
        mImgDst.setImageBitmap(null);
    }


    @Override
    protected void loadOpenCVSuccess() {
        beautySkinPresenter = new BeautySkinPresenter();
    }

    @Override
    protected void loadOpenCVFail() {
        ToastUtils.show(this, "OpenCV loaded fail");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_filter1:
                beautySkinPresenter.imageFilter(fileUri.getPath(), 1, mImgDst);
                break;
            case R.id.btn_filter2:
                beautySkinPresenter.imageFilter(fileUri.getPath(), 1, mImgDst);
                break;
            case R.id.btn_filter3:
                beautySkinPresenter.imageFilter(fileUri.getPath(), 1, mImgDst);
                break;
            case R.id.btn_beauty_java:
                beautySkinPresenter.beautySkin(fileUri.getPath(), mImgDst, false);
                break;
            case R.id.btn_beauty_native:
                beautySkinPresenter.beautySkin(fileUri.getPath(), mImgDst, true);
                break;
            default:
                break;
        }
    }
}
