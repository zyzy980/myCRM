package com.bba.dz.controller;

import com.bba.util.EmailUtils;
import java.util.Properties;

/**
 * 使用多线程发送邮件
 * */
public class SendEMailThread  implements Runnable {

    //发送邮件的参数
    private Properties properties;
    private String[] toUser;
    private String subject;
    private String html;
    private String attachmentFilename;
    private String attachmentFilePath;

    /**
     * 初始化参数
     * */
    public SendEMailThread(Properties properties,String[] toUser, String subject, String html,String attachmentFilename,String attachmentFilePath)
    {
        this.properties=properties;
        this.toUser=toUser;
        this.subject=subject;
        this.html=html;
        this.attachmentFilename=attachmentFilename;
        this.attachmentFilePath=attachmentFilePath;
    }

    @Override
    public void run()
    {
        EmailUtils emailUtils=new EmailUtils(properties);
        try {
            emailUtils.SendMail(toUser, subject, html, attachmentFilename, attachmentFilePath);
        }catch (Exception e)
        {
            System.out.println("发送邮件出错，原因："+e.getMessage());
        }
    }
}

