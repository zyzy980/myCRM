/**
 * 邮件发送类
 * Author:LTJ
 * Date:2018-07-11
 * */
package com.bba.util;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import javax.activation.FileDataSource;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import com.sun.mail.util.MailSSLSocketFactory;

/**
 * 邮件类
 */
public class EmailUtils{
 
	/**
	 * 文件内容：mailConfig.properties
### Send Mail Account ###
mailHost=smtp.ym.163.com
mailPort=994
mailUsername=????
mailPassword=????
mailTimeout=25000
mailFrom=service1@weekeyan.com
mailPersonal=Server Manager Weekeyan
	 * */
	//private final String mailConfig="/mailConfig.properties";
	private final String mailEncoding="utf-8";
 
	private Properties properties = new Properties();
	public EmailUtils(Properties properties)
	{
		this.properties=properties;
	}
	public EmailUtils(String mailConfig)
	{
		try
		{
			InputStream inputStream=this.getClass().getResourceAsStream(mailConfig);
			properties.load(inputStream);
			inputStream.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			properties=null;
		}
		finally
		{
			
		}
	}
	
	private JavaMailSenderImpl GetSender() throws Exception
	{
		if(null==properties)
		{
			throw new Exception("读取配置失败");
		}
		JavaMailSenderImpl sender=new JavaMailSenderImpl();
		sender.setHost(properties.getProperty("mailHost"));
		sender.setPort(Integer.parseInt(properties.getProperty("mailPort")));
		sender.setUsername(properties.getProperty("mailUsername"));
		sender.setPassword(properties.getProperty("mailPassword"));
		sender.setProtocol("smtp");
		sender.setDefaultEncoding(mailEncoding);
		Properties ps=new Properties();
		ps.setProperty("mail.smtp.timeout", properties.getProperty("mailTimeout"));
		ps.setProperty("mail.smtp.auth", "true");
		
		//开启安全协议
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ps.put("mail.smtp.ssl.enable", "true");
        ps.put("mail.smtp.ssl.socketFactory", sf);
        
        
		sender.setJavaMailProperties(ps);
		return sender;
	}
	
	/**
	 * 发送邮件
	 * toUser=多个邮箱地址
	 * String[] toUser=new String[]{"526831403@qq.com","lituji@weekeyan.com"};
	 * */
	public boolean SendMail(String[] toUser, String subject, String html) throws Exception
	{
		return SendMail(toUser,  subject,  html,null,null);
	}
	
	/**
	 * toUser收件人
	 * subject邮件标题
	 * html邮件正文
	 * attachmentFilename附件名称
	 * attachmentFilePath附件文件路径（路径+文件名）
	 * */
	public boolean SendMail(String[] toUser, String subject, String html,String attachmentFilename,String attachmentFilePath) throws Exception
	{
		System.setProperty("mail.mime.splitlongparameters","false");
		JavaMailSenderImpl mailSender=GetSender();
		MimeMessage mineMessage=mailSender.createMimeMessage();
		MimeMessageHelper messageHelper=new MimeMessageHelper(mineMessage,true,mailEncoding);
		messageHelper.setFrom(properties.getProperty("mailFrom"),properties.getProperty("mailPersonal"));
		messageHelper.setTo(toUser);
		messageHelper.setSubject(subject);
		messageHelper.setText(html,true);
		
		//添加附件
		File file = null;
		if(null!=attachmentFilename)
		{
			file = new File(attachmentFilePath);
			messageHelper.addAttachment(MimeUtility.encodeText(attachmentFilename), file);
		}
		//发送邮件
		try
		{
			mailSender.send(mineMessage);
		}
		catch(Exception e)
		{
			return false;
		}
		finally
		{
			if(null!=file)
				file=null;
			mineMessage=null;
			mailSender=null;
		}
		return true;
	}
	
	
	/*
	public static void main(String[] args) throws Exception
	{
		try
		{
			MailUtils mu=new MailUtils();
			String[] toUser=new String[]{"526831403@qq.com","1147234772@qq.com","lituji@weekeyan.com"};
			mu.SendMail(toUser,"邮件标题2018-05-18","<div style='color:red'>邮件内容（红色）</div>");
		}
		catch(Exception e)
		{
		 
		}
		 
	}
	*/
	
}
