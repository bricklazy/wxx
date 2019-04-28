package com.wlx.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/23.
 */

public class DisplayUtil {

    private static Handler handler = new Handler();

    //缓存图片路径
    private static Map<String, SoftReference<Bitmap>> bitmapMap = new HashMap<String, SoftReference<Bitmap>>();

    public static void changeStatusBarTextColor(Activity ac, boolean isBlack) {
        if(ac==null){
            return;
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (isBlack) {
                ac.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
            }else {
                ac.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_VISIBLE);//恢复状态栏白色字体
            }
        }
    }

    /**
     * 获取通知栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取屏幕DPI
     * @param context
     * @return
     */
    public static int getScreenDPI(Context context){
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        dpi = displayMetrics.densityDpi;
        return dpi;
    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return int px
     */
    public static int getScreenWidth(Context context){
        int width = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        return width;
    }

    /**
     * 获取屏幕高度
     * @param context
     * @return int px
     */
    public static int getScreenHeight(Context context){
        int height = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        return height;
    }

    /**
     * 获取屏幕高度
     * @param context
     * @return
     */
    public static int getScreenHeightDP(Context context){
        return px2dip(context, getScreenHeight(context));
    }

    /**
     * 获取旋转角度
     */
    public static int getDisplayRotation(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        switch (configuration.orientation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    static SoftReference<Bitmap> bitmapSoftReference;

    /**
     * 图片去色,返回灰度图片
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap toGrayScale(Bitmap bmpOriginal) throws Exception {

        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayScale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayScale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(1);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayScale;
    }

    /**
     * @param path
     * @return Bitmap
     * 根据图片url获取图片对象
     */
    public static Bitmap getLocalBitmap(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }

    /**
     * @param urlpath
     * @return Bitmap
     * 根据图片url获取图片对象
     */
    public static Bitmap getBitMBitmap(String urlpath) {

        Bitmap bitmap = null;

        try {
            URL url = new URL(urlpath);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream in;
            in = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(in);
            bitmapSoftReference = new SoftReference<Bitmap>(bitmap);
            return bitmapSoftReference.get();
            // TODO Auto-generated catch block
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param urlpath
     * @return Bitmap
     * 根据url获取布局背景的对象
     */
    public static Drawable getDrawable(String urlpath){
        Drawable d = null;
        try {
            URL url = new URL(urlpath);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream in;
            in = conn.getInputStream();
            d = Drawable.createFromStream(in, "background.jpg");
            // TODO Auto-generated catch block
        } catch (IOException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void drawableToTextView(Context context, TextView tv, int resourceId, int local) {
        Drawable drawable= context.getResources().getDrawable(resourceId);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, (int)(drawable.getMinimumWidth()*0.8), (int)(drawable.getMinimumHeight()*0.8));
        if(local== Gravity.LEFT) {
            tv.setCompoundDrawables(drawable, null, null, null);
        }else if(local== Gravity.RIGHT){
            tv.setCompoundDrawables(null, null, drawable, null);
        }else if(local== Gravity.TOP){
            tv.setCompoundDrawables(null, drawable, null, null);
        }else if(local== Gravity.BOTTOM){
            tv.setCompoundDrawables(null, null, null, drawable);
        }
    }

}
