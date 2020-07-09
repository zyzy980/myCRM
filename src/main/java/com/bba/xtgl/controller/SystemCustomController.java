package com.bba.xtgl.controller;

import com.bba.common.constant.OperateConstant;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.ResultVO;
import com.bba.util.SessionUtils;
import com.bba.xtgl.service.api.ISysCustomService;
import com.bba.xtgl.service.api.ISysUserService;
import com.bba.xtgl.vo.SysCustomVO;
import com.bba.xtgl.vo.SysUserVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/sysInfo/custom")
public class SystemCustomController {
	
	@Autowired
	private ISysCustomService sysCustomService;
	@Autowired
	private ISysUserService sysUserService;
	
	
	@RequestMapping("/getUserCustom")
	@ResponseBody
	public ResultVO getUserCustom(HttpServletRequest request) {

		SysUserVO sysUserVO = SessionUtils.currentSession();
		SysCustomVO sysCustomVO = sysCustomService.getCustomDetail(sysUserVO.getId());
		
		ResultVO resultVO = new ResultVO();
		resultVO.setResultCode("0");
		
		if (sysCustomVO != null) {			
			SysUserVO dataSysUserVO = new SysUserVO();
			dataSysUserVO.setUserId(sysUserVO.getUserId());
			dataSysUserVO.setWithin_code(sysUserVO.getWithin_code());
			
			List<SysUserVO> list = sysUserService.findSysUserList(dataSysUserVO);
			if(list.isEmpty()){
				sysCustomVO.setWithin_code("");
			}else{
				sysCustomVO.setWithin_code(list.get(0).getWithin_code());
			}

			resultVO.setResultDataFull(sysCustomVO);
		} else {
			resultVO = ResultVO.failResult("该用户没有任何个性化设置");
		}
		return resultVO;
	}
	
	
	@Log(value = "上传用户头像")
	@RequestMapping(value = "/saveUserHeaderPic", method = RequestMethod.POST)
	public @ResponseBody ResultVO saveUserHeaderPic(@RequestParam("userHeaderPic") MultipartFile userHeaderPic, HttpServletRequest request) {
         if(userHeaderPic.isEmpty()){
        	 return ResultVO.failResult("上传文件为空");
         }
         
         
         SysUserVO sysUserVO = SessionUtils.currentSession();
         SysCustomVO oldSysCustomVO = sysCustomService.getCustomDetail(sysUserVO.getId());
         if(oldSysCustomVO == null){
        	 return ResultVO.failResult("请先保存用户个性化设置");
         }
         String endWith = null;
         if(OperateConstant.image_GIF.equals(userHeaderPic.getContentType())){
        	 endWith = "gif";
         }else if(OperateConstant.image_PNG.equals(userHeaderPic.getContentType())){
        	 endWith = "png";
         }else if(OperateConstant.image_JPG.equals(userHeaderPic.getContentType())){
        	 endWith = "jpg";
         }else{
        	 return ResultVO.failResult("上传文件格式如：jpg,png,gif");
         }
         
         
         String rootPath = request.getSession().getServletContext().getRealPath("");
         rootPath = rootPath.substring(0, rootPath.lastIndexOf("\\")); 
         
         String savaPath = "/WMS_Image/userHeaderPic/" + System.currentTimeMillis()+"."+endWith;
         File file =  new File(rootPath+savaPath);
         if (!file.getParentFile().exists()){
        	 file.getParentFile().mkdirs();
         }
         try {
			userHeaderPic.transferTo(file);
         } catch (Exception e) {
			throw new RuntimeException("文件上传发生异常!", e);
         }
         
         
         SysCustomVO newSysCustomVO = new SysCustomVO();
         newSysCustomVO.setUserId(oldSysCustomVO.getUserId());
         newSysCustomVO.setUserHeadPic(savaPath);
         sysCustomService.updateUserCustom(newSysCustomVO);
         // 删除老的文件
         if(!StringUtils.isBlank(oldSysCustomVO.getUserHeadPic())){
        	 File f = new File(rootPath + oldSysCustomVO.getUserHeadPic());
        	 if(f.isFile()){
        		 f.delete();
        	 }
         }
         return ResultVO.successResult("上传成功", savaPath);
	}
	@Log(value = "修改个性化设置")
	@RequestMapping(value = "/saveCustomDetail", method = RequestMethod.POST)
	public @ResponseBody ResultVO saveCustomDetail(SysCustomVO sysCustomVO, HttpServletRequest request) {
		
		SysUserVO sysUserVO = SessionUtils.currentSession();
		sysCustomVO.setUserId(String.valueOf(sysUserVO.getId())); // 这里的用户id 指的是主键ID
		SysCustomVO oldSysCustomVO = sysCustomService.getCustomDetail(sysUserVO.getId());
		
		
		if(StringUtils.isNotBlank(sysCustomVO.getWithin_code())){
			SysUserVO updateSysUserVO = new SysUserVO();
	        
	        updateSysUserVO.setWithin_code(sysUserVO.getWithin_code());
	        updateSysUserVO.setId(sysUserVO.getId());
	        updateSysUserVO.setUserId(sysUserVO.getUserId());
	        
	        updateSysUserVO.setWhcenter(sysCustomVO.getWithin_code());
	        sysUserService.updateSysUserDetail(updateSysUserVO);

	        //重设session值
	        sysUserVO.setWhcenter(sysCustomVO.getWithin_code());
		}
		 
		if(oldSysCustomVO == null){
			sysCustomService.addUserCustom(sysCustomVO);
		}else{
			sysCustomService.updateUserCustom(sysCustomVO);
		}
		return ResultVO.successResult("修改成功");
	}
	
	
	@Log(value = "更新缓存")
	@RequestMapping(value = "/reflushCache", method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody ResultVO reflushCache(HttpServletRequest request,HttpServletResponse response) 
	{
		//sysCustomService.reflushCache(request, response);
		return ResultVO.successResult("更新成功");
	}
}

