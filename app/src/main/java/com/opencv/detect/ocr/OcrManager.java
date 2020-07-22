package com.opencv.detect.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.opencv.detect.R;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * 功能描述：
 *
 * @author liuhongshuo
 * @date 2020-06-24
 */
public class OcrManager {

    private final static String TAG = "OcrManager";

    private TessBaseAPI baseApi;
    private Context context;

    private static OcrManager instance;

    private OcrManager(Context context) {
        this.context = context;
        try {
            initTessBaseAPI(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static OcrManager getInstance(Context context) {
        if (null == instance) {
            synchronized (OcrManager.class) {
                if (null == instance) {
                    instance = new OcrManager(context);
                }
            }

        }
        return instance;
    }

    /**
     * 初始化tesseract API
     *
     * @param context
     * @throws IOException
     */
    private void initTessBaseAPI(Context context) throws IOException {

        String datapath = Environment.getExternalStorageDirectory() + "/tesseract/";
        File dir = new File(datapath + "tessdata/");
        if (!dir.exists()) {
            dir.mkdirs();
            InputStream input = context.getResources().openRawResource(R.raw.nums);
            File file = new File(dir, "nums.traineddata");
            FileOutputStream output = new FileOutputStream(file);
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = input.read(buff)) != -1) {
                output.write(buff, 0, len);
            }
            input.close();
            output.close();
        }
        baseApi = new TessBaseAPI();
        boolean success = baseApi.init(datapath, "nums");
        if (success) {
            Log.i(TAG, "load Tesseract OCR Engine successfully...");
        } else {
            Log.i(TAG, "WARNING:could not initialize Tesseract data...");
        }
    }

    /**
     * 识别身份证号
     *
     * @param imgPath
     * @return
     */
    public String recognizeCardId(String imgPath) {
        Bitmap template = BitmapFactory.decodeResource(context.getResources(), R.mipmap.card_template);
        Bitmap cardImage = BitmapFactory.decodeFile(imgPath);
        Bitmap input = cardImage.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap temp = CardNumberROIFinder.extractNumberROI(input, template);
        baseApi.setImage(temp);
        return baseApi.getUTF8Text();

    }

    /**
     * 识别文本
     *
     * @param imgPath
     * @return
     */
    public String recognizeTextImage(String imgPath) {
        if (TextUtils.isEmpty(imgPath)) {
            return "";
        }
        if (null == baseApi) {
            return "";
        }

        Bitmap bmp = BitmapFactory.decodeFile(imgPath);
        baseApi.setImage(bmp);
        return baseApi.getUTF8Text();
    }


    /**
     * 偏斜纠正
     *
     * @param imgPath
     * @param imageView
     */
    public static void deSkewTextImage(String imgPath, ImageView imageView) {
        Mat src = Imgcodecs.imread(imgPath);
        if (src.empty()) {
            return;
        }
        Mat dst = new Mat();
        CardNumberROIFinder.deSkewText(src, dst);

        // 转换为Bitmap，显示
        Bitmap bm = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, bm);

        // show
        imageView.setImageBitmap(bm);

        // 释放内存
        dst.release();
        src.release();
    }

}
