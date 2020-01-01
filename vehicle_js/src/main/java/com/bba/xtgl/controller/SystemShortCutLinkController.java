package com.bba.xtgl.controller;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bba.common.interceptor.Log;
import com.bba.common.vo.ResultVO;
import com.bba.util.ResultDataFullMore;
import com.bba.util.SessionUtils;
import com.bba.xtgl.service.api.ISysShortCutLinkService;
import com.bba.xtgl.vo.SysShortCutLinkVO;
import com.bba.xtgl.vo.SysUserVO;

@Controller
@RequestMapping("/sysInfo/shortCutLink")
public class SystemShortCutLinkController {

	@Autowired
	private ISysShortCutLinkService sysShortCutLinkService;

 

	
	@Log(value="查询用户所有快捷键",log = false)
	@ResponseBody
	@RequestMapping(value="/getShortCutListBySystemUser",method=RequestMethod.POST)
	public ResultVO getShortCutListBySystemUser(HttpServletRequest request) {
		SysUserVO sysUserVO = (SysUserVO) request.getSession().getAttribute(SessionUtils.SESSION_KEY);
		// 用的是用户自增id 不是编号
		List<SysShortCutLinkVO> list = sysShortCutLinkService.findSysShortCutLinkByUserId(String.valueOf(sysUserVO.getId())); 
		if(list.size() > 0){
			ResultVO resultVO = ResultVO.successResult();
			resultVO.setResultDataFull(list);
			return resultVO;
		}else{
			ResultVO resultVO = new ResultVO();
			resultVO.setResultCode("3");
			resultVO.setResultDataFull(new ResultDataFullMore("该用户无设置任何快捷", "", false,true));
			return resultVO;
		}
	}
	
	@Log(value="查询用户快捷键明细")
	@ResponseBody
	@RequestMapping(value="/getDetail",method=RequestMethod.POST)
	public ResultVO getDetail(SysShortCutLinkVO sysShortCutLinkVO, HttpServletRequest request) {
		SysShortCutLinkVO vo =  sysShortCutLinkService.getSysShortCutLinkById(sysShortCutLinkVO.getId());
		if(vo != null){
			ResultVO resultVO = ResultVO.successResult();
			resultVO.setResultDataFull(vo);
			return resultVO;
		}else{
			return ResultVO.successResult("该用户无设置任何快捷");
		}
	}
	
	@Log(value="id,保存用户快捷键,修改用户快捷键")
	@ResponseBody
	@RequestMapping(value="/saveDetail",method=RequestMethod.POST)
	public ResultVO saveDetail(SysShortCutLinkVO sysShortCutLinkVO, HttpServletRequest request) {
		SysUserVO sysUserVO = (SysUserVO)request.getSession().getAttribute(SessionUtils.SESSION_KEY);
		if(StringUtils.isEmpty(sysShortCutLinkVO.getId()) || sysShortCutLinkVO.getId() == 0){
			sysShortCutLinkVO.setUserId(String.valueOf(sysUserVO.getId())); //这里保存的是自增id 不是编号
			
			String newIco = sysShortCutLinkVO.getIco().replaceFirst("../../|../", "");// 去掉前面路径
			sysShortCutLinkVO.setIco(newIco);
			sysShortCutLinkService.saveSysShortCutLink(sysShortCutLinkVO);
			return ResultVO.successResult("保存成功");
		}else{
			String ico = sysShortCutLinkVO.getIco();
			if(ico != null){
				String newIco = sysShortCutLinkVO.getIco().replaceFirst("../../|../", "");// 去掉前面路径
				sysShortCutLinkVO.setIco(newIco);
			}
			List<SysShortCutLinkVO> list = new ArrayList<SysShortCutLinkVO>(3);
			list.add(sysShortCutLinkVO);
			sysShortCutLinkService.updateSysShortCutLink(list);
			return ResultVO.successResult("修改成功");
		}
	}
	@Log(value="id,保存用户快捷键,修改用户快捷键")
	@ResponseBody
	@RequestMapping(value="/saveDetail1",method=RequestMethod.POST)
	public ResultVO saveDetail1(@RequestParam(value = "file", required = false) MultipartFile file,SysShortCutLinkVO sysShortCutLinkVO, HttpServletRequest request) {
		SysUserVO sysUserVO = (SysUserVO)request.getSession().getAttribute(SessionUtils.SESSION_KEY);
			if(file!=null&&file.getSize()>0){
				String endWith = null;
		         if("image/gif".equals(file.getContentType())){
		        	 endWith = "gif";
		         }else if("image/jpeg".equals(file.getContentType())){
		        	 endWith = "png";
		         }else if("image/png".equals(file.getContentType())){
		        	 endWith = "jpg";
		         }else{
		        	 return ResultVO.failResult("上传文件格式如：jpg,png,gif");
		         }
		         String rootPath = request.getSession().getServletContext().getRealPath("");
		         rootPath = rootPath.substring(0, rootPath.lastIndexOf("\\"));
		         String savaPath = "/WMS_Image/userImg/"+System.currentTimeMillis()+"."+endWith;
		         sysShortCutLinkVO.setIco(savaPath);
		         File file1 =  new File(rootPath+savaPath);
		         if (!file1.getParentFile().exists()){
		        	 file1.getParentFile().mkdirs();
		         }
		         try {
					file.transferTo(file1);
		         } catch (Exception e) {
					throw new RuntimeException("文件上传发生异常!", e);
		         }
			}
		if(StringUtils.isEmpty(sysShortCutLinkVO.getId()) || sysShortCutLinkVO.getId() == 0){
			sysShortCutLinkVO.setUserId(String.valueOf(sysUserVO.getId()));
			sysShortCutLinkService.saveSysShortCutLink(sysShortCutLinkVO);
			return ResultVO.successResult("保存成功");
		}else{
			List<SysShortCutLinkVO> list = new ArrayList<SysShortCutLinkVO>(3);
			list.add(sysShortCutLinkVO);
			sysShortCutLinkService.updateSysShortCutLink(list);
			return ResultVO.successResult("修改成功");
		}
		
	}
	
	@Log(value="更新快捷键顺序")
	@ResponseBody
	@RequestMapping(value="/editOrder",method=RequestMethod.POST)
	public ResultVO editOrder(@RequestBody List<SysShortCutLinkVO> sysShortCutLinkList,HttpServletRequest request,HttpServletResponse response) {
		if(sysShortCutLinkList.isEmpty()){
			return ResultVO.failResult("空数据快捷键更新!");
		}
		
		sysShortCutLinkService.updateSysShortCutLink(sysShortCutLinkList);
		return ResultVO.successResult("更新成功");
	}
	
	@Log(value="删除用户快捷键")
	@ResponseBody
	@RequestMapping(value="/deleteByCodes",method=RequestMethod.POST)
	public ResultVO deleteByCodes(SysShortCutLinkVO sysShortCutLinkVO) {

		SysShortCutLinkVO oldData = sysShortCutLinkService.getSysShortCutLinkById(sysShortCutLinkVO.getId());
		if(oldData == null){
			return ResultVO.failResult("快捷键不存在!");
		}
		sysShortCutLinkService.deleteSysShortCutLink(oldData);
		
		
		return ResultVO.successResult("删除成功");
	}

	
}