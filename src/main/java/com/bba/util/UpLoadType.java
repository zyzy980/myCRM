package com.bba.util;

import java.io.File;

public class UpLoadType {

    //WINDOWS
	public static String UPLOAD_PATH = "E:/VEHICLE_JS/UPLOAD";

    //linux
    String separator= File.separator;
    public final String LINUX_UPLOAD_PATH =System.getProperty("user.dir") + separator + "UPLOAD" + separator;


}
