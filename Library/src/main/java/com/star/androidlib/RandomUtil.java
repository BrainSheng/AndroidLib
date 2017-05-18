package com.star.androidlib;

import java.util.Random;
import java.util.UUID;

public class RandomUtil
{
    /** 生成指定长度的随机数字+字母字符串 **/
    public static String getRandomString(int length)
    {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++)
        {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /** 获得Android的UUID **/
    public static String getUUID()
    {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
