package com.star.androidlib;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil
{
    private static Toast mToastInstance = null;

    /** 自定义toast显示函数，防止多次点击时toast重复显示 **/
    public static void showToast(Context context, String text)
    {
        // 尚未弹出过toast，则调用系统方法弹出
        if (mToastInstance == null)
        {
            mToastInstance = Toast.makeText(context, text, Toast.LENGTH_LONG);
        }
        // 已经弹出的情况下，只修改其内容，并重新设置一下时长
        else
        {
            mToastInstance.setText(text);
            mToastInstance.setDuration(Toast.LENGTH_LONG);
        }
        mToastInstance.show();
    }

    /** 隐藏toast **/
    public static void hideToast()
    {
        if (mToastInstance != null)
        {
            mToastInstance.cancel();
        }
    }

}
