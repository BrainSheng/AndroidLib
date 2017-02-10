package com.star.androidlib;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Environment;
import android.util.DisplayMetrics;

import java.io.File;
import java.util.Locale;

public class CommonUtil
{
	/** 获取SD卡根目录路径 **/
	public static String getSDCardPath()
	{
		File sdCardDir = null;
		// 判断SD卡是否存在
		boolean isSDCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		// SD卡存在，返回SD卡目录
		if (isSDCardExist)
		{
			sdCardDir = Environment.getExternalStorageDirectory();
			return sdCardDir.toString();
		}
		// SD卡不存在，返回空串
		else
		{
			return "";
		}
	}

	/** 切换应用的语言和国家，该函数需要在主Activity的setContentView方法之前调用 **/
	public static void switchLocale(Context context, Locale locale)
	{
		// 获得设置对象
		Configuration config = context.getResources().getConfiguration();
		// 获得res资源对象
		Resources resources = context.getResources();
		// 获得屏幕参数：主要是分辨率，像素等。
		DisplayMetrics displayMetrics = resources.getDisplayMetrics();
		// 语言和地区设置
		config.locale = locale;
		// 更新设置
		resources.updateConfiguration(config, displayMetrics);
		Locale.setDefault(locale);
	}
}
