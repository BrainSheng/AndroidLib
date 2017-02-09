package com.star.androidlib;

import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LogUtil
{
	public static final int VERBOSE = 0;
	public static final int DEBUG = 1;
	public static final int INFO = 2;
	public static final int WARN = 3;
	public static final int ERROR = 4;
	public static final int OFF = 5;
	
	private static final String LOG_FILE_NAME_PREFIX = "sdkdemo";

	private static int logLevel = OFF;
	private static String logPath = "";
	private static BlockingQueue<String> msgQueue;
	private static PrintWriter currentWriter;
	private static String currentLogDate;
	
	/** 初始化Logger **/
	public static void init(int level, String path)
	{
		logLevel = level;
		logPath = path;
		FileUtil.createAllDirectories(logPath);
		enableOutputToFile();
	}

	/** VERBOSE **/
	public static void verbose(String tag, String msg)
	{
		log(VERBOSE, tag, msg);
	}

	public static void verbose(String tag, String msg, Throwable tr)
	{
		log(VERBOSE, tag, msg, tr);
	}

	public static void verbose(String msg)
	{
		log(VERBOSE, msg);
	}

	public static void verbose(String msg, Throwable tr)
	{
		log(VERBOSE, msg, tr);
	}

	/** DEBUG **/
	public static void debug(String tag, String msg)
	{
		log(DEBUG, tag, msg);
	}

	public static void debug(String tag, String msg, Throwable tr)
	{
		log(DEBUG, tag, msg, tr);
	}

	public static void debug(String msg)
	{
		log(DEBUG, msg);
	}

	public static void debug(String msg, Throwable tr)
	{
		log(DEBUG, msg, tr);
	}

	/** INFO **/
	public static void info(String tag, String msg)
	{
		log(INFO, tag, msg);
	}

	public static void info(String tag, String msg, Throwable tr)
	{
		log(INFO, tag, msg, tr);
	}

	public static void info(String msg)
	{
		log(INFO, msg);
	}

	public static void info(String msg, Throwable tr)
	{
		log(INFO, msg, tr);
	}

	/** WARN **/
	public static void warn(String tag, String msg)
	{
		log(WARN, tag, msg);
	}

	public static void warn(String tag, String msg, Throwable tr)
	{
		log(WARN, tag, msg, tr);
	}

	public static void warn(String msg)
	{
		log(WARN, msg);
	}

	public static void warn(String msg, Throwable tr)
	{
		log(WARN, msg, tr);
	}

	/** ERROR **/
	public static void error(String tag, String msg)
	{
		log(ERROR, tag, msg);
	}

	public static void error(String tag, String msg, Throwable tr)
	{
		log(ERROR, tag, msg, tr);
	}

	public static void error(String msg)
	{
		log(ERROR, msg);
	}

	public static void error(String msg, Throwable tr)
	{
		log(ERROR, msg, tr);
	}
	
	/** 判断指定日志级别是否生效 **/
	private static boolean isLogLevelEnabled(int level)
	{
		if (level >= logLevel)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/** 获取日志的tag，返回调用者的类名 **/
	private static String getTag()
	{
		StackTraceElement[] stack = new Throwable().getStackTrace();
		for (int i = 0; i < stack.length; i++)
		{
			// 获取当前的类名
			String thisClassName = LogUtil.class.getName();
			// 获取堆栈中类名
			String stackClassName = stack[i].getClassName();
			// 根据栈的结构，当遍历到堆栈中的类名不是当前类名时，才是调用者的类名
			if (thisClassName.equals(stackClassName))
			{
				continue;
			}
			else
			{
				return stackClassName.substring(stackClassName.lastIndexOf(".") + 1, stackClassName.length());
			}
		}
		return "";
	}

	/** 输出日志 **/
	private static void log(int level, String tag, String msg)
	{
		if (isLogLevelEnabled(level))
		{
			// 输出到Logcat中
			switch (level)
			{
			case VERBOSE:
				Log.v(tag, msg);
				break;
			case DEBUG:
				Log.d(tag, msg);
				break;
			case INFO:
				Log.i(tag, msg);
				break;
			case WARN:
				Log.w(tag, msg);
				break;
			case ERROR:
				Log.e(tag, msg);
				break;
			}
			// 输出到文件中
			String content = String.format("%s | %s | %s | %s", getCurrentTimeDesc(), getLogLevelDesc(level), tag, msg);
			addLogToQueue(tag, content.trim());
		}
	}

	private static void log(int level, String tag, String msg, Throwable tr)
	{
		if (isLogLevelEnabled(level))
		{
			// 输出到Logcat中
			switch (level)
			{
			case VERBOSE:
				Log.v(tag, msg, tr);
				break;
			case DEBUG:
				Log.d(tag, msg, tr);
				break;
			case INFO:
				Log.i(tag, msg, tr);
				break;
			case WARN:
				Log.w(tag, msg, tr);
				break;
			case ERROR:
				Log.e(tag, msg, tr);
				break;
			}
			// 输出到文件中
			String content = String.format("%s | %s | %s | %s", getCurrentTimeDesc(), getLogLevelDesc(level), tag, msg);
			String trace = String.format("%s | %s | %s | %s", getCurrentTimeDesc(), getLogLevelDesc(level), tag, Log.getStackTraceString(tr));
			addLogToQueue(tag, content.trim());
			addLogToQueue(tag, trace.trim());
		}
	}

	private static void log(int level, String msg)
	{
		log(level, getTag(), msg);
	}

	private static void log(int level, String msg, Throwable tr)
	{
		log(level, getTag(), msg, tr);
	}

	private static void addLogToQueue(String tag, String msg)
	{
		if (msgQueue != null)
		{
			try
			{
				msgQueue.put(msg);
			}
			catch (InterruptedException e)
			{
				Log.e(tag, "add log to queue failed!", e);
			}
		}
	}

	/** 获取当前系统时间 **/
	private static String getCurrentTimeDesc()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
		return df.format(new Date());
	}

	/** 获取日志级别的描述 **/
	private static String getLogLevelDesc(int level)
	{
		switch (level)
		{
		case VERBOSE:
			return "VERBOSE";
		case DEBUG:
			return "DEBUG";
		case INFO:
			return "INFO";
		case WARN:
			return "WARN";
		case ERROR:
			return "ERROR";
		default:
			return "";
		}
	}

	/** 检查日志的输出文件是否需要切换 **/
	private static void checkFileWriter(String content)
	{
		// 截取日志每一行的前10位字符串，比较日期是否变化
		String date = content.substring(0, 10);
		// 如果日期变化了，需要切换日志文件
		if (!date.equals(currentLogDate))
		{
			try
			{
				if (currentWriter != null)
				{
					currentWriter.close();
				}
				String fileName = logPath + "/" + String.format("%s_%s.log", LOG_FILE_NAME_PREFIX, date);
				currentLogDate = date;
				currentWriter = new PrintWriter(new FileOutputStream(fileName, true));
			}
			catch (IOException e)
			{
			}
		}
	}
	
	/** 启动写文件的线程 **/
	private static void enableOutputToFile()
	{
		msgQueue = new LinkedBlockingQueue<String>();
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					try
					{
						String content = msgQueue.take();
						checkFileWriter(content);
						currentWriter.println(content);
						currentWriter.flush();
					}
					catch (Exception e)
					{
					}
				}
			}
		}).start();
	}
}
