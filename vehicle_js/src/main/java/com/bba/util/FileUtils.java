package com.bba.util;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
	
	
	/**
	 * 获取上传文件服务器路径
	 */
	public static String getSevletPath(HttpServletRequest request){
		String rootPath = request.getSession().getServletContext().getRealPath("");
        rootPath = rootPath.substring(0, rootPath.lastIndexOf("\\")); 
		return rootPath;
	}
	
	/**
	 * 将源文件上传到指定文件路径
	 * @param file
	 * @param targetFile
	 */
	public static void transferTo(MultipartFile file, File targetFile){
		if(!targetFile.exists()){  
            targetFile.mkdirs();  
        } 
        try {
			file.transferTo(targetFile);
		} catch (IllegalStateException | IOException e) {
			throw new IllegalStateException("文件上传异常",e);
		}  
	}

	/**
	 * 删除目标文件
	 * @param targetFile
	 * @return
	 */
	public static boolean deleteFile(File targetFile){
		if(targetFile != null && targetFile.exists()){
			return targetFile.delete();
		}
		return false;
	}

}
