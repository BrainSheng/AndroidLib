package com.star.androidlib;

import android.os.Process;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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

    private static int mLevelConsole = DEBUG;
    private static int mLevelFile = DEBUG;
    private static long mMaxFileSize = 10 * 1024 * 1024;
    private static int mMaxFileCount = 5;
    private static String mLogFilePath = "";
    private static String mLogFileName = "";
    private static boolean mIsConsoleEnabled = false;
    private static boolean mIsFileEnabled = false;

    private static BlockingQueue<String> mLogMsgQueue;
    private static PrintWriter mLogFileWriter;
    private static String currentLogDate;

    /** 设置控制台日志级别 **/
    public static void setLevelConsole(int level)
    {
        mLevelConsole = level;
    }

    /** 设置文件日志级别 **/
    public static void setLevelFile(int level)
    {
        mLevelFile = level;
    }

    /** 设置单个日志文件最大大小，单位：字节 **/
    public static void setMaxFileSize(long size)
    {
        mMaxFileSize = size;
    }

    /** 设置最大日志文件数量 **/
    public static void setMaxFileCount(int count)
    {
        mMaxFileCount = count;
    }

    /** 设置日志路径 **/
    public static void setLogPath(String path)
    {
        if (!StringUtil.isEmpty(path))
        {
            // 统一把目录分隔符替换成/
            mLogFilePath = path.replace("\\", "/");
            // 结尾加上/
            if (!mLogFilePath.endsWith("/"))
            {
                mLogFilePath = mLogFilePath + "/";
            }
            // 创建路径
            FileUtil.createAllDirectories(mLogFilePath);
        }
    }

    /** 允许输出日志到控制台 **/
    public static void enableOutputToConsole()
    {
        mIsConsoleEnabled = true;
    }

    /** 允许输出日志到文件 **/
    public static void enableOutputToFile()
    {
        mIsFileEnabled = true;
        mLogMsgQueue = new LinkedBlockingQueue<>();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    try
                    {
                        // 如果日志路径为空，则不输出日志到文件中，每隔1秒检查一次
                        if (StringUtil.isEmpty(mLogFilePath))
                        {
                            CommonUtil.sleep(1000);
                        }
                        // 否则写入日志到文件中
                        else
                        {
                            String content = mLogMsgQueue.take();
                            checkFileWriter();
                            mLogFileWriter.println(content);
                            mLogFileWriter.flush();
                        }
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
        }).start();
    }

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

    /** 判断控制台日志级别是否生效 **/
    private static boolean isConsoleLogLevelEnabled(int level)
    {
        if (mIsConsoleEnabled && level >= mLevelConsole)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /** 判断文件日志级别是否生效 **/
    private static boolean isFileLogLevelEnabled(int level)
    {
        if (mIsFileEnabled && level >= mLevelFile)
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
        // 输出日志到控制台
        if (isConsoleLogLevelEnabled(level))
        {
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
        }
        // 输出日志到文件
        if (isFileLogLevelEnabled(level))
        {
            addLogToQueue(buildLogContent(level, tag, msg));
        }
    }

    private static void log(int level, String tag, String msg, Throwable tr)
    {
        // 输出日志到控制台
        if (isConsoleLogLevelEnabled(level))
        {
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
            addLogToQueue(buildLogContent(level, tag, msg));
            addLogToQueue(buildLogContent(level, tag, Log.getStackTraceString(tr)));
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

    private static void addLogToQueue(String msg)
    {
        if (mLogMsgQueue != null)
        {
            try
            {
                mLogMsgQueue.put(msg);
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    /** 获取当前系统时间 **/
    private static String getCurrentTimeDesc()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        return simpleDateFormat.format(new Date());
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
            return "UNKNOWN";
        }
    }

    /** 检查日志的输出文件是否需要切换 **/
    private static void checkFileWriter()
    {
        try
        {
            // 日志输出Writer为null，则需要新建一个Writer
            if (mLogFileWriter == null)
            {
                String filePathName = "";
                // 如果日志目录下已经存在日志文件，则挑选最新的文件继续写入
                if (getCurrLogFileCount() > 0)
                {
                    mLogFileName = getNewestFileName();
                    filePathName = getCurrentLogFilePathName();
                }
                else
                {
                    filePathName = generateNewLogFilePathName();
                }
                mLogFileWriter = new PrintWriter(new FileOutputStream(filePathName, true));
            }
            // 否则需要继续使用当前的Writer
            else
            {
                // 文件大小超过上限则新建一个文件继续输出
                File file = new File(getCurrentLogFilePathName());
                if (file.length() >= mMaxFileSize)
                {
                    mLogFileWriter.close();
                    mLogFileWriter = new PrintWriter(new FileOutputStream(generateNewLogFilePathName(), true));
                    // 文件数量超过上限则删除最老的文件
                    if (getCurrLogFileCount() > mMaxFileCount)
                    {
                        new File(mLogFilePath + getOldestFileName()).delete();
                    }
                }
            }
        }
        catch (IOException e)
        {
        }
    }

    /** 获取当前文件全路径名称 **/
    private static String getCurrentLogFilePathName()
    {
        return mLogFilePath + mLogFileName;
    }

    /** 生成一个新的日志文件，并返回日志文件路径 **/
    private static String generateNewLogFilePathName()
    {
        mLogFileName = TimeUtil.getLocalTime("yyyyMMdd_HHmmss_SSS") + ".log";
        return mLogFilePath + mLogFileName;
    }

    /** 获取当前日志路径下的日志文件个数 **/
    private static int getCurrLogFileCount()
    {
        File fPath = new File(mLogFilePath);
        File[] fChildFiles = fPath.listFiles();
        List<String> fileNameList = new ArrayList<>();
        for (File f : fChildFiles)
        {
            if (f.getName().endsWith(".log"))
            {
                fileNameList.add(f.getName());
            }
        }
        return fileNameList.size();
    }

    /** 获取日志路径下最新的日志文件名称 **/
    private static String getNewestFileName()
    {
        File fPath = new File(mLogFilePath);
        File[] fChildFiles = fPath.listFiles();
        List<String> fileNameList = new ArrayList<>();
        for (File f : fChildFiles)
        {
            if (f.getName().endsWith(".log"))
            {
                fileNameList.add(f.getName());
            }
        }
        if (fileNameList.size() > 0)
        {
            Collections.sort(fileNameList, Collator.getInstance());
            return fileNameList.get(fileNameList.size() - 1);
        }
        else
        {
            return "";
        }
    }

    /** 获取日志路径下最老的日志文件名称 **/
    private static String getOldestFileName()
    {
        File fPath = new File(mLogFilePath);
        File[] fChildFiles = fPath.listFiles();
        List<String> fileNameList = new ArrayList<>();
        for (File f : fChildFiles)
        {
            if (f.getName().endsWith(".log"))
            {
                fileNameList.add(f.getName());
            }
        }
        if (fileNameList.size() > 0)
        {
            Collections.sort(fileNameList, Collator.getInstance());
            return fileNameList.get(0);
        }
        else
        {
            return "";
        }
    }

    /** 获取输出到文件中的日志内容 **/
    private static String buildLogContent(int level, String tag, String msg)
    {
        String timeDesc = getCurrentTimeDesc();
        String levelDesc = getLogLevelDesc(level);
        String pid = String.format("%05d", Process.myPid());
        String tid = String.format("%05d", Process.myTid());
        String content = String.format("%s | %s-%s | %s | %s | %s", timeDesc, pid, tid, levelDesc, tag, msg);
        return content;
    }
}
