package com.bba.jcda.controller;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.jcda.service.api.ITr_statistical_rulesService;
import com.bba.jcda.vo.Tr_statistical_rulesVO;
import com.bba.util.*;
import com.bba.xtgl.vo.SysUserVO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TR_STATISTICAL_RULES 统计规则
 * */
@RestController
@RequestMapping("/base/tr_statistical_rules")
public class Tr_statistical_rulesController {
	@Autowired
	private ITr_statistical_rulesService iTr_statistical_rulesService;

	@RequestMapping("getListForGrid")
	public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters, String contract_type) {
		SysUserVO sysUserVO = SessionUtils.currentSession();
		customSearchFilters = MyUtils.decode(customSearchFilters);
		return iTr_statistical_rulesService.getListForGrid(jqGridParamModel,customSearchFilters);
	}


	@Log(value = "基础档案-统计规则-注销/恢复数据")
	@RequestMapping("DelOrResume")
	public ResultVO DelOrResume(@RequestBody Tr_statistical_rulesVO vo)
	{
		if(null==vo || StringUtils.isBlank(vo.getSn()))
		{
			return ResultVO.failResult("没有选择任何数据。");
		}

		String sn=ENDECodeUtils.URLDecode(vo.getSn());
		String[] sn_array=sn.split(",");
		List<Tr_statistical_rulesVO> list= new ArrayList<Tr_statistical_rulesVO>();
		for(String item:sn_array)
		{
			Tr_statistical_rulesVO info=new Tr_statistical_rulesVO();
			info.setSn(item);
			info.setState(vo.getState());
			list.add(info);
		}
		iTr_statistical_rulesService.update(list);
		return ResultVO.successResult("OK");
	}



	//@Log(value = "基础档案-统计规则-注销/恢复数据")
	@RequestMapping("getDetail")
	public ResultVO getDetail(@RequestBody Tr_statistical_rulesVO vo) {
		ResultVO resultVO = new ResultVO();
		resultVO.setResultCode("0");
		if (null == vo || StringUtils.isBlank(vo.getSn())) {
			resultVO.setResultDataFull(new Tr_statistical_rulesVO());
			return resultVO;
		}

		List<Tr_statistical_rulesVO> list = iTr_statistical_rulesService.findListByProperty(vo);
		if (null != list && list.size() > 0) {
			resultVO.setResultDataFull(list.get(0));
		} else {
			resultVO.setResultDataFull(new Tr_statistical_rulesVO());
		}
		return resultVO;
	}


	@Log(value = "基础档案-统计规则-保存数据")
	@RequestMapping("SaveDetail")
	public ResultVO SaveDetail(@RequestBody Tr_statistical_rulesVO vo) {
		if(null==vo)
		{
			return ResultVO.failResult("保存失败，没有录入任何数据。");
		}
		if(vo.getBegin_date() == null || vo.getEnd_date() == null){
			return ResultVO.failResult("有效期起,止不能为空");
		}else{
			if(!(vo.getBegin_date().getTime() < vo.getEnd_date().getTime())){
				return ResultVO.failResult("有效期起必须小于止");
			}
		}
		SysUserVO sysUserVO=SessionUtils.currentSession();
		vo.setCreate_by(sysUserVO.getUserId());
		vo.setCreate_date(DateUtils.nowDate());

		vo.setUp_first_mileage(vo.getUp_first_mileage()==null ? 0:vo.getUp_first_mileage());
		vo.setUp_first_price(vo.getUp_first_price()==null? 0d:vo.getUp_first_price());
		vo.setUp_two_mileage(vo.getUp_two_mileage()==null ? 0:vo.getUp_two_mileage());
		vo.setUp_two_price(vo.getUp_two_price()==null? 0d:vo.getUp_two_price());
		vo.setUp_three_mileage(vo.getUp_three_mileage()==null ?0:vo.getUp_three_mileage());
		vo.setUp_three_price(vo.getUp_three_price()==null? 0d:vo.getUp_three_price());
		vo.setUp_premium(vo.getUp_premium()==null? 0d:vo.getUp_premium());
		vo.setUp_total(vo.getUp_total()==null? 0d:vo.getUp_total());
		vo.setDown_first_mileage(vo.getDown_first_mileage()==null ?0:vo.getDown_first_mileage());
		vo.setDown_first_price(vo.getDown_first_price()==null? 0d:vo.getDown_first_price());
		vo.setDown_two_mileage(vo.getDown_two_mileage()==null ? 0:vo.getDown_two_mileage());
		vo.setDown_two_price(vo.getDown_two_price()==null? 0d:vo.getDown_two_price());
		vo.setDown_three_mileage(vo.getDown_three_mileage()==null ? 0:vo.getDown_three_mileage());
		vo.setDown_three_price(vo.getDown_three_price()==null? 0d:vo.getDown_three_price());
		vo.setDown_cross_sea_amount(vo.getDown_cross_sea_amount()==null? 0d:vo.getDown_cross_sea_amount());
		vo.setDown_premium(vo.getDown_premium()==null? 0d:vo.getDown_premium());
		vo.setDown_total(vo.getDown_total()==null? 0d:vo.getDown_total());
		vo.setUp_cross_sea_amount(vo.getUp_cross_sea_amount()==null? 0d:vo.getUp_cross_sea_amount());


		if(StringUtils.isBlank(vo.getSn())) {
			vo.setState("0");
			iTr_statistical_rulesService.insert(vo);
		}else {
			vo.setState(null);
			iTr_statistical_rulesService.update(vo);
		}
		return ResultVO.successResult("保存成功。");
	}





    @RequestMapping("/exportTemplate")
    @ResponseBody
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException, InvalidFormatException {
        POIUtils.exportTemplate(request, response, "统计规则导入模板","Resource/excel/jcda/统计规则导入模板.xlsx");
    }


    @Log(value = "基础档案-统计规则-导入数据")
    @RequestMapping("/importData")
    @ResponseBody
    public ResultVO importData(@RequestParam("fileList") MultipartFile file) throws Exception {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        List<Tr_statistical_rulesVO> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), Tr_statistical_rulesVO.class, new ImportParams());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null==list || list.size()<=0)
		{
			return ResultVO.failResult("导入失败，导入Excel文件没有数据。");
		}
        //运输方式+起运地+目的地 构成唯一性 （需要验证）
		String error="";
		List<String> cols=new ArrayList<String>();
		int Rows=2;
        for(Tr_statistical_rulesVO item:list) {
			String ColStr = item.getTrans_mode() + item.getBegin_city() + item.getEnd_city();
			if (!cols.contains(ColStr)) {
				cols.add(ColStr);
			} else {
				error=Rows+",";
			}
			Rows++;
		}
        if(StringUtils.isNotBlank(error))
		{
			return ResultVO.failResult("导入失败，导入数据有重复（运输方式+起运地+目的地），重复行号："+error);
		}
        String CurrentDate=DateUtils.nowDate();
        //判断更新，或新增
		List<Tr_statistical_rulesVO> insertData=new ArrayList<Tr_statistical_rulesVO>();
		List<Tr_statistical_rulesVO> updateData=new ArrayList<Tr_statistical_rulesVO>();
		for(Tr_statistical_rulesVO item:list)
		{
			item.setCreate_by(sysUserVO.getUserId());
			item.setCreate_date(CurrentDate);
			if(UniqueValideData(item))
			{
				//存在数据
				item.setState("0");
				updateData.add(item);
			}
			else
			{
				//不存在数据
				item.setState("0");
				insertData.add(item);
			}
		}
		if(insertData.size()>0)
			iTr_statistical_rulesService.insert(insertData);
		if(updateData.size()>0)
			iTr_statistical_rulesService.update(updateData);
        return ResultVO.successResult("导入成功！");
    }

    /**
	 * 验证数据唯一
	 * 运输方式+起运地+目的地 构成唯一性 （需要验证）
	 * false=不存在，true=存在
	 * */
    private boolean UniqueValideData(Tr_statistical_rulesVO vo) {
		vo.setUp_first_mileage(vo.getUp_first_mileage()==null ? 0:vo.getUp_first_mileage());
		vo.setUp_first_price(vo.getUp_first_price()==null? 0d:vo.getUp_first_price());
		vo.setUp_two_mileage(vo.getUp_two_mileage()==null ? 0:vo.getUp_two_mileage());
		vo.setUp_two_price(vo.getUp_two_price()==null? 0d:vo.getUp_two_price());
		vo.setUp_three_mileage(vo.getUp_three_mileage()==null ?0:vo.getUp_three_mileage());
		vo.setUp_three_price(vo.getUp_three_price()==null? 0d:vo.getUp_three_price());
		vo.setUp_premium(vo.getUp_premium()==null? 0d:vo.getUp_premium());
		vo.setUp_total(vo.getUp_total()==null? 0d:vo.getUp_total());
		vo.setDown_first_mileage(vo.getDown_first_mileage()==null ?0:vo.getDown_first_mileage());
		vo.setDown_first_price(vo.getDown_first_price()==null? 0d:vo.getDown_first_price());
		vo.setDown_two_mileage(vo.getDown_two_mileage()==null ? 0:vo.getDown_two_mileage());
		vo.setDown_two_price(vo.getDown_two_price()==null? 0d:vo.getDown_two_price());
		vo.setDown_three_mileage(vo.getDown_three_mileage()==null ? 0:vo.getDown_three_mileage());
		vo.setDown_three_price(vo.getDown_three_price()==null? 0d:vo.getDown_three_price());
		vo.setDown_cross_sea_amount(vo.getDown_cross_sea_amount()==null? 0d:vo.getDown_cross_sea_amount());
		vo.setDown_premium(vo.getDown_premium()==null? 0d:vo.getDown_premium());
		vo.setDown_total(vo.getDown_total()==null? 0d:vo.getDown_total());
		vo.setUp_cross_sea_amount(vo.getUp_cross_sea_amount()==null? 0d:vo.getUp_cross_sea_amount());

		Tr_statistical_rulesVO item = new Tr_statistical_rulesVO();
		item.setTrans_mode(vo.getTrans_mode());
		item.setBegin_city(vo.getBegin_city());
		item.setEnd_city(vo.getEnd_city());
		item.setBegin_date(vo.getBegin_date());
		item.setEnd_date(vo.getEnd_date());
		List<Tr_statistical_rulesVO> list = iTr_statistical_rulesService.findListByProperty(item);
		if (null == list || list.size() <= 0) {
			return false;
		}
		else {
			vo.setSn(list.get(0).getSn());
			return true;
		}
	}
}
