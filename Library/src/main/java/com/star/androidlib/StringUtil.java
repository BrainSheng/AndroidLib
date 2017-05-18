package com.star.androidlib;

public class StringUtil
{
    /** 判断字符串是否为空串或null **/
    public static boolean isEmpty(String value)
    {
        if (null == value || "".equals(value))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
