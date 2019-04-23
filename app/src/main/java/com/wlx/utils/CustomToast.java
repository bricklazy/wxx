package com.wlx.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class CustomToast {

	public volatile static Toast toast;
	
	private volatile static Handler handler = new Handler(Looper.getMainLooper());
	
	private volatile static Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//关闭Toast
			toast.cancel();
		}
	};
	
	/**
	 * 显示Toast
	 * @param context 上下文
	 * @param text 显示文本内容
	 * @param duration 显示时间 毫秒
	 */
	public static void showToast(Context context, String text, int duration){

		//移除
		handler.removeCallbacks(runnable);
		
		if(toast==null){
			//创建新的Toast
			toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		}else{
			toast.setText(text);
		}
		//duration 毫秒后执行
		handler.postDelayed(runnable, duration);

		//显示Toast
		toast.show();
	}

	/**
	 * 显示Toast
	 * @param context 上下文
	 * @param msg 显示文本内容
	 */
	public static void showToast(Context context, String msg){
		try {
			showToast(context, msg, 1000);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 显示Toast
	 * @param context 上下文
	 * @param resId 显示文本内容资源id
	 * @param duration 显示时间
	 */
	public static void showToast(Context context, int resId, int duration){
		try{
			showToast(context, context.getResources().getString(resId), duration);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
}
