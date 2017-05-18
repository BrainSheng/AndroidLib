package com.star.androidlib;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeUtil
{
    /** 获取UTC时间，按照指定格式输出，例如：yyyy-MM-dd HH:mm:ss.SSS **/
    public static String getUTCTime(String format)
    {
        // 取得本地时间
        Calendar calendar = Calendar.getInstance();
        // 取得时间偏移量
        int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
        // 取得夏令时差
        int dstOffset = calendar.get(Calendar.DST_OFFSET);
        // 从本地时间里扣除这些差量，即可以取得UTC时间
        calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        // 格式化时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

    /** 获取本地时间，按照指定格式输出，例如：yyyy-MM-dd HH:mm:ss.SSS **/
    public static String getLocalTime(String format)
    {
        // 取得本地时间
        Calendar calendar = Calendar.getInstance();
        // 格式化时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }
}
