package com.opencv.detect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.opencv.detect.callback.ImageFileInterface;
import com.opencv.detect.ocr.OcrManager;
import com.opencv.detect.utils.ToastUtils;

/**
 * OCR识别
 */
public class OcrDemoActivity extends BaseActivity implements ImageFileInterface, View.OnClickListener {

    private int option;
    private RadioButton mBtnOcrCard;
    private RadioButton mBtnOcrText;
    private RadioButton mBtnOcrNum;

    private ImageView mImgSrc;
    private TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        initView();
    }

    private void initView() {

        mImgSrc = (ImageView) findViewById(R.id.img_src);
        mTvResult = (TextView) findViewById(R.id.tv_result);

        mBtnOcrCard = (RadioButton) findViewById(R.id.btn_ocr_card);
        mBtnOcrText = (RadioButton) findViewById(R.id.btn_ocr_text);
        mBtnOcrNum = (RadioButton) findViewById(R.id.btn_ocr_num);

        mBtnOcrCard.setOnClickListener(this);
        mBtnOcrText.setOnClickListener(this);
        mBtnOcrNum.setOnClickListener(this);

        mBtnOcrCard.setChecked(true);
        this.setTitle("OCR识别演示");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_ocr_card:
                option = 1;
                break;
            case R.id.btn_ocr_text:
                option = 2;
                break;
            case R.id.btn_ocr_num:
                option = 3;
                break;
            default:
                break;
        }

    }

    /**
     * 识别操作
     */
    private void recongnize() {

        if (null == fileUri) {
            ToastUtils.show(this, "请先选择图片");
            return;
        }

        if (option == 1) {
            String cardId = OcrManager.getInstance(this).recognizeCardId(fileUri.getPath());
            mTvResult.setText(cardId);
        } else if (option == 2) {
            String cardText = OcrManager.getInstance(this).recognizeTextImage(fileUri.getPath());
            mTvResult.setText(cardText);
        } else {

        }
    }

    @Override
    protected void loadOpenCVSuccess() {

    }

    @Override
    protected void loadOpenCVFail() {

    }

    @Override
    public void onImageFileChanged(String filePath) {
        Bitmap bm = BitmapFactory.decodeFile(filePath);
        mImgSrc.setImageBitmap(bm);

        recongnize();
    }


}