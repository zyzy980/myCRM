package com.bba.xtgl.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bba.common.vo.PageVO;
import com.bba.xtgl.dao.IPngDao;
import com.bba.xtgl.service.api.IPngService;
import com.bba.xtgl.vo.ImageVO;
@Service
@Transactional
public class PngService implements IPngService{
	@Resource
	private IPngDao iPngDao;
	
	@Override
	public PageVO findImage(ImageVO imageVO) {
		imageVO.setMaxPage((Integer.parseInt(imageVO.getPage()))*Integer.parseInt(imageVO.getRows()+"")+"");
		imageVO.setMinPage((Integer.parseInt(imageVO.getPage())-1)*Integer.parseInt(imageVO.getRows()+"")+"");
		List<ImageVO> imageList=iPngDao.findImage(imageVO);
		PageVO page=new PageVO();
		imageVO.setRecords(""+iPngDao.findImageCount(imageVO).get(0).getTotal());
		page.setPage(imageVO.getPage());
		page.setPageRows(imageVO.getPageRows());
		page.setRecords(imageVO.getRecords());
		page.setRows(imageList);
		return page;
	}

}
