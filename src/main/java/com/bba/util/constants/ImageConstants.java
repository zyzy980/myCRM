package com.bba.util.constants;

import com.bba.util.FtpUtil;

import java.io.File;

/**
 * @ClassName ImageConstants
 * @Discription TODO
 * @Author lao li
 * @Date 2019-05-11 11:07
 * @Version 1.0
 */
public class ImageConstants {

    private static FtpUtil FTP_UTIL = new FtpUtil();
    private static final String URL_PATH = "http://139.224.44.115";
    private static String IMAGE_PATH =  + File.separatorChar + "dtod_files" + File.separatorChar;
}
