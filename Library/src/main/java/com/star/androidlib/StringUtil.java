package com.star.androidlib;

public class StringUtil
{
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
