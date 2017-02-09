package com.star.androidlib;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil
{
	static Toast toast = null;

	/** 自定义toast显示函数，防止多次点击时toast重复显示 **/
	public static void ShowToast(Context context, String text)
	{
		// 根据字符长度，设置toast提示的持续时间
		int duration = Toast.LENGTH_LONG;
		if (text == null)
		{
			duration = Toast.LENGTH_SHORT;
		}
		else
		{
			duration = text.length() > 10 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
		}
		// 尚未弹出过toast，则调用系统方法弹出
		if (toast == null)
		{
			toast = Toast.makeText(context, text, duration);
		}
		// 已经弹出的情况下，只修改其内容，并重新设置一下时长
		else
		{
			toast.setText(text);
			toast.setDuration(duration);
		}
		toast.show();
	}

	/** 隐藏toast **/
	public static void HideToast()
	{
		if (toast != null)
		{
			toast.cancel();
		}
	}

}
