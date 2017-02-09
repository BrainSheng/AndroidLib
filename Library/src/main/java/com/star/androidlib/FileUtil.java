package com.star.androidlib;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class FileUtil
{
	public static final String FILE_ENCODING_GBK = "GBK";
	public static final String FILE_ENCODING_UTF8 = "UTF-8";
	
	/** 判断文件是否存在 **/
	public static boolean isFileExists(String strFilePath)
	{
		try
		{
			File file = new File(strFilePath);
			if (file.exists())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/** 拷贝文件 **/
	public static void copyFile(String srcFile, String dstFile)
	{
		File fileSrc = new File(srcFile);
		File fileDst = new File(dstFile);
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try
		{
			inputStream = new BufferedInputStream(new FileInputStream(fileSrc));
			outputStream = new BufferedOutputStream(new FileOutputStream(fileDst));
			byte[] buffer = new byte[2048];
			int i;
			while ((i = inputStream.read(buffer)) != -1)
			{
				outputStream.write(buffer, 0, i);

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				inputStream.close();
				outputStream.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/** 删除文件 **/
	public static void deleteFile(String filePath)
	{
		// 路径为文件且不为空则进行删除
		File file = new File(filePath);
		if (file.isFile() && file.exists())
		{
			file.delete();
		}
	}

	/** 删除某个目录 **/
	public static void deletePath(String strPath)
	{
		File file = new File(strPath);
		if (file.exists())
		{
			if (file.isFile())
			{
				file.delete();
				return;
			}
			if (file.isDirectory())
			{
				File[] childFile = file.listFiles();
				if (childFile == null || childFile.length == 0)
				{
					file.delete();
					return;
				}
				for (File f : childFile)
				{
					deletePath(f.getPath());
				}
				file.delete();
			}
		}
	}

	/** 创建文件夹 **/
	public static boolean createAllDirectories(String strPath)
	{
		File filePath = new File(strPath);
		// 目录已存在
		if (filePath.exists())
		{
			return true;
		}
		// 目录不存在
		else
		{
			return filePath.mkdirs();
		}
	}
	
	/** 将文本写入文件中 **/
	public static void WriteContentToFile(String strContent, String strFilePath, boolean isOverWrite, String strEncoding)
	{
		try
		{
			File file = new File(strFilePath);
			// 文件不存在，则新建文件
			if (!file.exists())
			{
				file.createNewFile();
			}
			// 文件存在
			else
			{
				// 判断是否需要覆盖
				if (isOverWrite)
				{
					file.delete();
					file.createNewFile();
				}
			}
			// 定位到文件末尾写入
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			raf.seek(file.length());
			raf.write(strContent.getBytes(strEncoding));
			raf.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/** 获取文件大小，单位：字节 **/
	public static long getFileSize(String fileName)
	{
		long size = -1;
		File f = new File(fileName);
		if (f.exists())
		{
			size = f.length();
		}
		return size;
	}
}
