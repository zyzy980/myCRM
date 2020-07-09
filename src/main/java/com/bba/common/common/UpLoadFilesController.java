package com.bba.common.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


import com.bba.common.vo.ResultVO;
import com.bba.util.SessionUtils;
import com.bba.xtgl.service.api.ISysUploadfileConfigService;
import com.bba.xtgl.vo.SysUploadfileConfigVO;
import com.bba.xtgl.vo.SysUserVO;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

import com.bba.xtgl.vo.SysOperateLogVO;

@Controller
@RequestMapping("/Common/UpLoadFiles")
public class UpLoadFilesController
{
	
	@Autowired
	private ISysUploadfileConfigService iSysUploadfileConfigService;

	/**
	 * 网络路径保存
	 * */
	private final String UploadConfig_Kind_NET="NET";
 
	/**
	 * 只允许上传一个文件
	 * */
	@RequestMapping(value = "/FileToServers", method = { RequestMethod.POST ,RequestMethod.GET })
	@ResponseBody
	public ResultVO FileToServers(String CODE,HttpServletRequest request,HttpServletResponse response,HttpSession session)
	{
		//暂时不处理上传类型??
		if(null==CODE || CODE.equals(""))
		{
			return ResultVO.failResult("上传配置表编号不能为空");
		}
		
		ResultVO resultVO=new ResultVO();
		SysUserVO sysUserVO = SessionUtils.currentSession();
		//String within_code=sysUserVO.getWithin_code();
		//String whcenter=sysUserVO.getWhcenter();
		//String UserId=sysUserVO.getUserId();
		
		Date d=new Date();
		SimpleDateFormat Date=new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat DateAndTime=new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr=Date.format(d);
		String dateAndTimeStr=DateAndTime.format(d);
		 
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		if (!multipartResolver.isMultipart(request)) 
		{
			return ResultVO.failResult("请选择文件");
		}
		
		SysUploadfileConfigVO uploadfileConfigItem=GetSysUploadfileConfig(CODE,sysUserVO);
		if(null==uploadfileConfigItem)
			return ResultVO.failResult("上传配置表没有设置编号（"+CODE+"）");
		
		try
		{
			Random rand = new Random();
			String fileName=dateAndTimeStr+"_"+rand.nextInt(10000);
	 
			String filePath="";
			String urlPath="";
			if(uploadfileConfigItem.getKind().toUpperCase().equals(UploadConfig_Kind_NET))
			{
				//网络：分离服务器
				filePath=uploadfileConfigItem.getSave_path()+"/"+CODE+"/"+dateStr+"/";//保存路径
				urlPath=CODE+"/"+dateStr+"/";//保存后URL路径
			}
			else
			{
				//本地：应用程序为当前目录
				filePath=request.getSession().getServletContext().getRealPath("/");
				filePath=filePath+uploadfileConfigItem.getSave_path()+"\\"+CODE+"\\"+dateStr+"\\";
				urlPath="../../../"+request.getContextPath()+"/"+uploadfileConfigItem.getSave_path().replace("\\", "/")+"/"+CODE+"/"+dateStr+"/";//保存后URL路径
				urlPath=urlPath.replace("//", "/");
				
				filePath=filePath.replace("\\\\", "\\").replace("\\", File.separator);
				File dir = new File(filePath);
				if(!dir.isDirectory())
				{
					dir.mkdirs();
				}
			}
			
			
			 
			
			SysOperateLog log=new SysOperateLog();
			SysOperateLogVO logVO=new SysOperateLogVO();
			logVO.setOperateitem("上传文件");
			logVO.setPlatform("网页");
			logVO.setOperatemodule(CODE);
			
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext())
			{
				try
				{
					MultipartFile file = multiRequest.getFile(iter.next());
					String uploadfileName=file.getOriginalFilename();
					InputStream inputStream = file.getInputStream();
	
					if(uploadfileName.indexOf(".")!=-1)
						fileName = fileName+"."+uploadfileName.split("\\.")[1];
					else
						fileName = fileName+"."+uploadfileName;
					filePath = filePath+fileName;
					
					
					
					int index = 0;
					byte[] bytes = new byte[1024];
					if(uploadfileConfigItem.getKind().toUpperCase().equals(UploadConfig_Kind_NET))
					{
						//网络
						NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null,uploadfileConfigItem.getAccount(), uploadfileConfigItem.getPassword());
						CreateDirectory(filePath,auth);
						SmbFile smbFile = new SmbFile(filePath,auth);//"smb://guest:1234@192.168.3.56/share/a.txt");
						//SmbFile smbFile = new SmbFile("smb://192.168.1.160/share/JCDA1/123.png",auth);
						smbFile.connect();
						OutputStream downloadfile = new BufferedOutputStream(new SmbFileOutputStream(smbFile));
						while ((index = inputStream.read(bytes)) != -1)
						{
							downloadfile.write(bytes, 0, index);
						}
						downloadfile.flush();
						downloadfile.close();
						downloadfile = null;
						auth=null;
						smbFile=null;
						
						urlPath=uploadfileConfigItem.getAccess_ip()+urlPath+fileName;
					}
					else
					{
						//本地
						FileOutputStream downloadfile = new FileOutputStream(filePath);
						
						while ((index = inputStream.read(bytes)) != -1)
						{
							downloadfile.write(bytes, 0, index);
						}
						downloadfile.flush();
						downloadfile.close();
						downloadfile = null;
						
						urlPath = urlPath+fileName;
					}
	
				} catch (Exception exception)
				{
					
					logVO.setOperateresult("Fail");//Fail,Success
					logVO.setParams("保存失败，原因："+exception.getMessage());
					log.setSysOperateLog(logVO);
					log.insert();
					return ResultVO.failResult("保存失败，原因："+exception.getMessage());
				}
				break;
			}
			logVO.setOperateresult("Success");//Fail,Success
			logVO.setParams("保存成功");
			log.setSysOperateLog(logVO);
			log.insert();
	 
			resultVO.setResultCode("0");
			resultVO.setResultDataFull(urlPath);
		}catch(Exception eUploadfile)
		{
			
		}
		return resultVO;
	}
	
	
	/**
	 * 创建路径
	 * */
	private void CreateDirectory(String Path,NtlmPasswordAuthentication auth)
	{
		//SmbFile smbFile = new SmbFile(filePath,auth);//"smb://guest:1234@192.168.3.56/share/jcda/yyyymmdd/a.txt");
		if(null==Path || Path.equals(""))
			return;
		String[] Path_Array=Path.split("/");
		Path=Path_Array[0]+"/"+Path_Array[1];
		for(int i=2,ilen=Path_Array.length-1;i<ilen;i++)
		{
			try
			{
				Path+="/"+Path_Array[i];
				SmbFile smbFile = new SmbFile(Path,auth);
				smbFile.connect();
				if(!smbFile.isDirectory())
				{
					smbFile.mkdir();
				}
				smbFile=null;
			}
			catch(Exception e)
			{
				
			}
		}
	}
	
	
	private SysUploadfileConfigVO GetSysUploadfileConfig(String CODE,SysUserVO sysUserVO)
	{
		return iSysUploadfileConfigService.GetSysUploadfileConfig(CODE,sysUserVO);
	}
	
}
