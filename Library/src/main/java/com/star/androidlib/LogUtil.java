package com.star.androidlib;

import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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

    private static int mLogLevel = OFF;
    private static String mLogFileNamePrefix = "";
    private static String mLogFilePath = "";
    private static BlockingQueue<String> mLogQueue;
    private static PrintWriter mFileWriter;
    private static String mCurrentDate;

    /** 初始化Logger **/
    public static void init(int level, String filePath, String fileNamePrefix)
    {
        mLogLevel = level;
        mLogFilePath = filePath;
        mLogFileNamePrefix = fileNamePrefix;
        if (!StringUtil.isEmpty(filePath))
        {
            FileUtil.createAllDirectories(mLogFilePath);
            enableOutputToFile();
        }
    }

    public static void verbose(String tag, String msg)
    {
        log(VERBOSE, tag, msg);
    }

    public static void verbose(String tag, String msg, Throwable throwable)
    {
        log(VERBOSE, tag, msg, throwable);
    }

    public static void verbose(String msg)
    {
        log(VERBOSE, msg);
    }

    public static void verbose(String msg, Throwable throwable)
    {
        log(VERBOSE, msg, throwable);
    }

    public static void debug(String tag, String msg)
    {
        log(DEBUG, tag, msg);
    }

    public static void debug(String tag, String msg, Throwable throwable)
    {
        log(DEBUG, tag, msg, throwable);
    }

    public static void debug(String msg)
    {
        log(DEBUG, msg);
    }

    public static void debug(String msg, Throwable throwable)
    {
        log(DEBUG, msg, throwable);
    }

    public static void info(String tag, String msg)
    {
        log(INFO, tag, msg);
    }

    public static void info(String tag, String msg, Throwable throwable)
    {
        log(INFO, tag, msg, throwable);
    }

    public static void info(String msg)
    {
        log(INFO, msg);
    }

    public static void info(String msg, Throwable throwable)
    {
        log(INFO, msg, throwable);
    }

    public static void warn(String tag, String msg)
    {
        log(WARN, tag, msg);
    }

    public static void warn(String tag, String msg, Throwable throwable)
    {
        log(WARN, tag, msg, throwable);
    }

    public static void warn(String msg)
    {
        log(WARN, msg);
    }

    public static void warn(String msg, Throwable throwable)
    {
        log(WARN, msg, throwable);
    }

    public static void error(String tag, String msg)
    {
        log(ERROR, tag, msg);
    }

    public static void error(String tag, String msg, Throwable throwable)
    {
        log(ERROR, tag, msg, throwable);
    }

    public static void error(String msg)
    {
        log(ERROR, msg);
    }

    public static void error(String msg, Throwable throwable)
    {
        log(ERROR, msg, throwable);
    }

    /** 判断指定日志级别是否生效 **/
    private static boolean isLogLevelEnabled(int level)
    {
        if (level >= mLogLevel)
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

    private static void log(int level, String tag, String msg, Throwable throwable)
    {
        if (isLogLevelEnabled(level))
        {
            // 输出到Logcat中
            switch (level)
            {
            case VERBOSE:
                Log.v(tag, msg, throwable);
                break;
            case DEBUG:
                Log.d(tag, msg, throwable);
                break;
            case INFO:
                Log.i(tag, msg, throwable);
                break;
            case WARN:
                Log.w(tag, msg, throwable);
                break;
            case ERROR:
                Log.e(tag, msg, throwable);
                break;
            }
            // 输出到文件中
            String content = String.format("%s | %s | %s | %s", getCurrentTimeDesc(), getLogLevelDesc(level), tag, msg);
            String trace = String.format("%s | %s | %s | %s", getCurrentTimeDesc(), getLogLevelDesc(level), tag, Log.getStackTraceString(throwable));
            addLogToQueue(tag, content.trim());
            addLogToQueue(tag, trace.trim());
        }
    }

    private static void log(int level, String msg)
    {
        log(level, getTag(), msg);
    }

    private static void log(int level, String msg, Throwable throwable)
    {
        log(level, getTag(), msg, throwable);
    }

    private static void addLogToQueue(String tag, String msg)
    {
        if (mLogQueue != null)
        {
            try
            {
                mLogQueue.put(msg);
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    /** 获取当前系统时间 **/
    private static String getCurrentTimeDesc()
    {
        return TimeUtil.getLocalTime("yyyy-MM-dd HH:mm:ss.SSS");
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
        // 截取日志每一行的前10位字符串，去除-，形成20170101的格式，比较日期是否变化
        String date = content.substring(0, 10);
        date.replaceAll("-", "");
        // 如果日期变化了，需要切换日志文件
        if (!date.equals(mCurrentDate))
        {
            try
            {
                // 如果有当前正在写的文件，则关闭之
                if (mFileWriter != null)
                {
                    mFileWriter.close();
                }
                // 打开新的文件进行写入
                String filePath = mLogFilePath;
                String fileName = String.format("%s.log", date);
                // 判断文件名前缀是否为空，前缀为空，则文件名只包含日期时间
                if (StringUtil.isEmpty(mLogFileNamePrefix))
                {
                    fileName = mLogFileNamePrefix + "_" + fileName;
                }
                String filePathName = filePath + "/" + fileName;
                mCurrentDate = date;
                mFileWriter = new PrintWriter(new FileOutputStream(filePathName, true));
            }
            catch (IOException e)
            {
            }
        }
    }

    /** 启动写文件的线程 **/
    private static void enableOutputToFile()
    {
        mLogQueue = new LinkedBlockingQueue<String>();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    try
                    {
                        String content = mLogQueue.take();
                        checkFileWriter(content);
                        mFileWriter.println(content);
                        mFileWriter.flush();
                    }
                    catch (InterruptedException e)
                    {
                    }
                }
            }
        }).start();
    }
}
