package com.bba.jsyw.service.impl;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.constant.BusinessData_FromEnum;
import com.bba.common.constant.BusinessData_StateEnum;
import com.bba.common.constant.BusinessData_projectEnum;
import com.bba.common.constant.SysDictionaryDataConstant;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.jcda.dao.Zd_CarrierDao;
import com.bba.jcda.vo.Zd_CarrierVO;
import com.bba.jsyw.dao.ITr_Business_CarrierDao;
import com.bba.jsyw.dao.ITr_BusinessDao;
import com.bba.jsyw.dto.Tr_BusinessDTO;
import com.bba.jsyw.service.api.ITr_BusinessService;
import com.bba.jsyw.vo.Tr_BusinessVO;
import com.bba.jsyw.vo.Tr_Business_CarrierVO;
import com.bba.jsyw.vo.Tr_ImportMoreCarrierVO;
import com.bba.util.*;
import com.bba.xtgl.dao.Sys_Dictionary_DataDao;
import com.bba.xtgl.vo.SysUserVO;
import com.bba.xtgl.vo.Sys_Dictionary_DataVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class Tr_BusinessService extends ServiceImpl<ITr_BusinessDao, Tr_BusinessVO> implements ITr_BusinessService {
	@Resource
	private ITr_BusinessDao tr_BusinessDao;

	@Resource
	private ITr_Business_CarrierDao tr_Business_CarrierDao;

	@Resource
	private Sys_Dictionary_DataDao sys_dictionary_dataDao;

	@Resource
	private Zd_CarrierDao zd_CarrierDao;
	@Override
	public PageVO findBusinessPageVO(JqGridParamModel jqGridParamModel, String customSearchFilters) {
		String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		List<Tr_BusinessVO> list = tr_BusinessDao.findListForGrid(jqGridParamModel);
		int count = tr_BusinessDao.findListForGridCount(jqGridParamModel);
		return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
	}

	@Override
	public ResultVO updateJsState(List<Tr_BusinessVO> vos) {
		tr_BusinessDao.updateJsState(vos);
		return ResultVO.successResult("操作成功");
	}

	@Override
	public Tr_BusinessDTO getDetail(Tr_BusinessVO vo) {
		Tr_BusinessVO paramTr_BusinessVO = new Tr_BusinessVO();
		paramTr_BusinessVO.setId(vo.getId());
		Tr_BusinessVO dataTr_BusinessVO = tr_BusinessDao.selectOne(paramTr_BusinessVO);
		if(dataTr_BusinessVO == null){
			return null;
		}
		EntityWrapper tr_BusinessVOEntityWrapper = new EntityWrapper();
		Tr_Business_CarrierVO paramTr_Business_CarrierVO = new Tr_Business_CarrierVO();
		paramTr_Business_CarrierVO.setVin(dataTr_BusinessVO.getVin());
		tr_BusinessVOEntityWrapper.setEntity(paramTr_Business_CarrierVO);
		tr_BusinessVOEntityWrapper.orderBy("id", true);
		List<Tr_Business_CarrierVO> dataTr_Business_CarrierVOList = tr_Business_CarrierDao.selectList(tr_BusinessVOEntityWrapper);
		Tr_BusinessDTO returnTr_BusinessDTO = new Tr_BusinessDTO();
		returnTr_BusinessDTO.setTr_businessVO(dataTr_BusinessVO);
		returnTr_BusinessDTO.setTr_Business_CarrierVOList(dataTr_Business_CarrierVOList);
		return returnTr_BusinessDTO;
	}

	/***
	 * 导入校验规则
	 * @param requestTr_BusinessVOList
	 * @param businessData_projectEnum
	 * @return
	 */
	private List<String> verificationDetail(List<Tr_BusinessVO> requestTr_BusinessVOList, BusinessData_projectEnum businessData_projectEnum) {
		return null;
	}

	@Override
	public ResultVO importData(MultipartFile multipartFile, BusinessData_projectEnum businessData_projectEnum) {
		SysUserVO sysUserVO = SessionUtils.currentSession();
		List<Tr_BusinessVO> requestTr_BusinessVOList = null;
		try {
			requestTr_BusinessVOList = ExcelImportUtil.importExcel(multipartFile.getInputStream(), Tr_BusinessVO.class, new ImportParams());
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*List<String> msgList = verificationDetail(requestTr_BusinessVOList, businessData_projectEnum);
		if(msgList.size() > 0){
			return ResultVO.failResult(ArrayUtils.join(msgList, "<br/>"));
		}*/
		for (Tr_BusinessVO requestTr_BusinessVO: requestTr_BusinessVOList) {
			//插入到主表
			requestTr_BusinessVO.setCreate_by(sysUserVO.getRealName());
			requestTr_BusinessVO.setVehicle_project(BusinessData_projectEnum.getCodeByName(requestTr_BusinessVO.getVehicle_project()));
			requestTr_BusinessVO.setData_from(BusinessData_FromEnum.FROM_MANUAL.getCode());
			tr_BusinessDao.insert(requestTr_BusinessVO);
			//插入到明细
			Tr_Business_CarrierVO tr_business_carrierVO = new Tr_Business_CarrierVO();
			tr_business_carrierVO.setCreate_by(sysUserVO.getRealName());
			tr_business_carrierVO.setVin(requestTr_BusinessVO.getVin());
			tr_business_carrierVO.setTrans_mode(requestTr_BusinessVO.getTrans_mode());
			tr_business_carrierVO.setBusiness_order("1");
			tr_business_carrierVO.setCarrier_no(requestTr_BusinessVO.getCarrier_no());
			tr_business_carrierVO.setCarrier_name(requestTr_BusinessVO.getCarrier_name());
			tr_Business_CarrierDao.insert(tr_business_carrierVO);
		}
		return ResultVO.successResult("导入成功！");
	}

	@Override
	public ResultVO saveDetail(Tr_BusinessDTO requestTr_businessDTO, SysUserVO sysUserVO, MultipartFile commonsMultipartFile) {
		Tr_BusinessVO requestTr_BusinessVO = requestTr_businessDTO.getTr_businessVO();
		List<Tr_Business_CarrierVO> requestTr_Business_CarrierVOList = requestTr_businessDTO.getTr_Business_CarrierVOList();

		if(requestTr_BusinessVO.getBegin_datetime() == null || requestTr_BusinessVO.getReceipt_datetime() == null){
			return ResultVO.failResult("业务起运、收车时间不能为空");
		}else{
			if(!(requestTr_BusinessVO.getBegin_datetime().getTime() <= requestTr_BusinessVO.getReceipt_datetime().getTime())){
				return ResultVO.failResult("起运时间必须小于收车时间");
			}
		}

		if(requestTr_Business_CarrierVOList.size() == 0){
			return ResultVO.failResult("必须保留一个承运商");
		}
		//上传文件
		String oldFile = null;
		String rootPath= null;
		if(commonsMultipartFile != null) {
			com.bba.bean.Resource resource = (com.bba.bean.Resource) SpringUtil.getBean(com.bba.bean.Resource.class);
			rootPath= resource.getUploadPath();
			String fileName = DateUtils.dateFormat(new Date(), "yyyyMMdd_HHmmddsss");
			String file_end_with = commonsMultipartFile.getOriginalFilename().substring(commonsMultipartFile.getOriginalFilename().lastIndexOf("."));
			String newPath = "/static/upload/"+DateUtils.dateFormat(new Date(), "yyyyMM")+"/" + fileName + file_end_with;
			File newFile = new File(rootPath, newPath);
			if (!newFile.getParentFile().exists()){
				newFile.getParentFile().mkdirs();
			}
			try {
				commonsMultipartFile.transferTo(newFile);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			requestTr_BusinessVO.setUpload_files(newPath);
		}else {
			oldFile = null;
		}

		/**校验*/
		/*	List<String> msgList = verificationDetail(requestTr_BusinessVO);
		if(msgList.size() > 0){
			return ResultVO.failResult(ArrayUtils.join(msgList, "<br/>"));
		}*/

		if("".equals(requestTr_BusinessVO.getId())|| requestTr_BusinessVO.getId() ==0){
			//新增，验证vin、运单是否唯一
			requestTr_BusinessVO.setData_from(BusinessData_FromEnum.FROM_MANUAL.getCode());
			Tr_BusinessVO paramTr_BusinessVO = new Tr_BusinessVO();
			paramTr_BusinessVO.setVin(requestTr_BusinessVO.getVin());
			Tr_BusinessVO dataTr_BusinessVO = tr_BusinessDao.selectOne(paramTr_BusinessVO);
			if(dataTr_BusinessVO != null){
				return ResultVO.failResult("VIN码已存在,请勿重复录入"+requestTr_BusinessVO.getVin());
			}
		/*	paramTr_BusinessVO = new Tr_BusinessVO();
			paramTr_BusinessVO.setVdr_no(requestTr_BusinessVO.getVdr_no());
			dataTr_BusinessVO = tr_BusinessDao.selectOne(paramTr_BusinessVO);
			if(dataTr_BusinessVO != null){
				return ResultVO.failResult("运单号已存在,请勿重复录入"+requestTr_BusinessVO.getVin());
			}*/
			requestTr_BusinessVO.setCreate_by(sysUserVO.getRealName());
			tr_BusinessDao.insert(requestTr_BusinessVO);
		}else{
			Tr_BusinessVO paramTr_BusinessVO = new Tr_BusinessVO();
			paramTr_BusinessVO.setVin(requestTr_BusinessVO.getVin());
			Tr_BusinessVO dataTr_BusinessVO = tr_BusinessDao.selectOne(paramTr_BusinessVO);
			if(dataTr_BusinessVO == null || !dataTr_BusinessVO.getVin().equals(requestTr_BusinessVO.getVin())){
				return ResultVO.failResult("该VIN数据异常,请联系管理员"+requestTr_BusinessVO.getVin());
			}
			if(!BusinessData_StateEnum.NORMAL.getCode().equals(dataTr_BusinessVO.getData_state())){
				return ResultVO.failResult("只允许正常状态下的数据进行修改操作");
			}
			if (commonsMultipartFile != null) {
				oldFile = dataTr_BusinessVO.getUpload_files();
				oldFile = rootPath + oldFile;
			}
			tr_BusinessDao.updateById(requestTr_BusinessVO);

			EntityWrapper tr_business_carrierEntityWrapper = new EntityWrapper();
			Tr_Business_CarrierVO paramTr_Business_CarrierVO = new Tr_Business_CarrierVO();
			paramTr_Business_CarrierVO.setVin(requestTr_BusinessVO.getVin());
			tr_business_carrierEntityWrapper.setEntity(paramTr_Business_CarrierVO);
			List<Tr_Business_CarrierVO> dataTr_Business_CarrierVOList = tr_Business_CarrierDao.selectList(tr_business_carrierEntityWrapper);

			//如果老数据里不存在新的数据List，就删除，存在就新增
			List<Tr_Business_CarrierVO> deleteTr_Business_CarrierVOList = new ArrayList<Tr_Business_CarrierVO>();
			List<Tr_Business_CarrierVO> updateTr_Business_CarrierVOList = new ArrayList<Tr_Business_CarrierVO>();

			for(Tr_Business_CarrierVO dataTr_Business_CarrierVO: dataTr_Business_CarrierVOList){
				boolean bool = false;
				for(Tr_Business_CarrierVO requestTr_Business_CarrierVO: requestTr_Business_CarrierVOList){
					if(dataTr_Business_CarrierVO.getId().equals(requestTr_Business_CarrierVO.getId())){
						updateTr_Business_CarrierVOList.add(requestTr_Business_CarrierVO);
						bool = true;
						break;
					}
				}
				if(!bool){
					deleteTr_Business_CarrierVOList.add(dataTr_Business_CarrierVO);
				}
			}

			for(Tr_Business_CarrierVO deleteVO: deleteTr_Business_CarrierVOList){
				tr_Business_CarrierDao.deleteById(deleteVO.getId());
			}
			for(Tr_Business_CarrierVO updateVO: updateTr_Business_CarrierVOList){
				tr_Business_CarrierDao.updateById(updateVO);
			}

		}
		for(Tr_Business_CarrierVO vo: requestTr_Business_CarrierVOList){
			if(vo.getId() == null || vo.getId() == 0){
				vo.setCreate_by(sysUserVO.getRealName());
				vo.setCreate_date(new Date());
				vo.setVin(requestTr_BusinessVO.getVin());
				tr_Business_CarrierDao.insert(vo);
			}
		}

		if(oldFile != null) {
			File f = new File(oldFile);
			if(f.exists()) {
				f.delete();
			}
		}

		ResultVO resultVO = ResultVO.successResult();
		resultVO.setResultDataFull("保存成功!");
		return resultVO;
	}

	@Override
	public ResultVO batchCancel(List<Tr_BusinessVO> tr_businessVOs,SysUserVO sysUserVO) {
		for (Tr_BusinessVO requestTr_BusinessVO: tr_businessVOs) {
			Tr_BusinessVO paramTr_BusinessVO = new Tr_BusinessVO();
			paramTr_BusinessVO.setVin(requestTr_BusinessVO.getVin());
			Tr_BusinessVO dataTr_BusinessVO = tr_BusinessDao.selectOne(paramTr_BusinessVO);
			if(dataTr_BusinessVO == null){
				return ResultVO.failResult("VIN不存在:"+requestTr_BusinessVO.getVin());
			}else if(!BusinessData_StateEnum.NORMAL.getCode().equals(dataTr_BusinessVO.getData_state())){
				return ResultVO.failResult("只允许正常状态下的业务数据进行注销操作:"+requestTr_BusinessVO.getVin());
			}
			Tr_BusinessVO updateTr_BusinessVO = new Tr_BusinessVO();
			updateTr_BusinessVO.setId(dataTr_BusinessVO.getId());
			updateTr_BusinessVO.setData_state(BusinessData_StateEnum.CANCEL.getCode());
			updateTr_BusinessVO.setCheck_by(sysUserVO.getRealName());
			updateTr_BusinessVO.setCheck_date(new Date());
			tr_BusinessDao.updateById(updateTr_BusinessVO);
		}
		return ResultVO.successResult("注销成功！");
	}

	@Override
	public ResultVO check(List<Tr_BusinessVO> vos,SysUserVO sysUserVO) {
		/**
		 * 1、正常的数据才可以审核
		 * 2、重损质损不可以审核
		 * 3
		 * 4、改变数据状态
		 * */
		try {
			for (Tr_BusinessVO requestVO: vos) {
				//插入到对上结算表
				tr_BusinessDao.insertJs_vin_amount(requestVO.getId(),sysUserVO.getRealName());
				//插入到对下结算表
				tr_BusinessDao.insertJs_vin_down_amount(requestVO.getId(),sysUserVO.getRealName());
				//插入到对下保费结算表
				//tr_BusinessDao.insertJs_vin_down_premium(requestVO.getId(),sysUserVO.getRealName());
				//修改审核状态
				Tr_BusinessVO updateTr_BusinessVO = new Tr_BusinessVO();
				updateTr_BusinessVO.setId(requestVO.getId());
				updateTr_BusinessVO.setCheck_by(sysUserVO.getRealName());
				updateTr_BusinessVO.setCheck_date(new Date());
				updateTr_BusinessVO.setData_state(BusinessData_StateEnum.CHECK.getCode());
				tr_BusinessDao.updateById(updateTr_BusinessVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return ResultVO.successResult("审核成功！");
	}

	/***
	 * 2019-9-30CMSCL方面要求暂停开发，等确定再开发
	 * @param file
	 * @return
	 */
	public ResultVO importMoreCarrier(MultipartFile file) {
		List<Tr_ImportMoreCarrierVO> requestTr_ImportMoreCarrierVOList = null;
		try {
			requestTr_ImportMoreCarrierVOList = ExcelImportUtil.importExcel(file.getInputStream(), Tr_ImportMoreCarrierVO.class, new ImportParams());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ResultVO resultVO = verificationDetail(requestTr_ImportMoreCarrierVOList);
		List<String> msgList = new ArrayList<String>();
		if (resultVO.getResultCode().equals("3")) {
			msgList = (List<String>) resultVO.getResultDataFull();
		} else {
			requestTr_ImportMoreCarrierVOList = (List<Tr_ImportMoreCarrierVO>) resultVO.getResultDataFull();
		}
		if(msgList.size() > 0){
			return ResultVO.failResult(ArrayUtils.join(msgList, "<br/>"));
		}
		return ResultVO.successResult("导入成功！");
	}

	/***
	 * 导入校验规则
	 * @param requestTr_ImportMoreCarrierVOList
	 * @return
	 */
	private ResultVO verificationDetail(List<Tr_ImportMoreCarrierVO> requestTr_ImportMoreCarrierVOList) {
		SysUserVO sysUserVO = SessionUtils.currentSession();
		//运输方式
		EntityWrapper param = new EntityWrapper();
		Sys_Dictionary_DataVO sys_dictionary_dataVO = new Sys_Dictionary_DataVO();
		sys_dictionary_dataVO.setTypecode(SysDictionaryDataConstant.TRANS_MODE);
		param.setEntity(sys_dictionary_dataVO);
		List<Sys_Dictionary_DataVO> trans_modeList = sys_dictionary_dataDao.selectList(param);
		Set<String> trans_modeSet = new HashSet<String>();
		for(Sys_Dictionary_DataVO vo: trans_modeList){
			trans_modeSet.add(vo.getDictext());
		}
		//承运商
		Zd_CarrierVO paramZd_CarrierVO = new Zd_CarrierVO();
		paramZd_CarrierVO.setStatus("0");//正式客户
		EntityWrapper zd_CarrierWrapper = new EntityWrapper();
		zd_CarrierWrapper.setEntity(paramZd_CarrierVO);
		List<Zd_CarrierVO> carrierVOList = zd_CarrierDao.selectList(zd_CarrierWrapper);
		Map<String,String> carrier_map = new HashMap<>();
		for(Zd_CarrierVO carrier_vo: carrierVOList){
			carrier_map.put(carrier_vo.getCode(),carrier_vo.getName());
		}
		//业务承运商明细集合
		List<Tr_Business_CarrierVO> insertCarrierList = new ArrayList<Tr_Business_CarrierVO>();

		List<String> msgList = new ArrayList<String>();
		for(int i = 0, line = 1; i < requestTr_ImportMoreCarrierVOList.size(); i++, line++) {
			Tr_Business_CarrierVO insertVO = new Tr_Business_CarrierVO();
			Tr_ImportMoreCarrierVO requestTr_ImportMoreCarrierVO = requestTr_ImportMoreCarrierVOList.get(i);
			//VIN
			if (StringUtils.isBlank(requestTr_ImportMoreCarrierVO.getVin())) {
				msgList.add("第" + line + "行数据,VIN不能为空");
			}
			//承运商一判断
			if (StringUtils.isBlank(requestTr_ImportMoreCarrierVO.getCarrier_no_one())) {
				msgList.add("第" + line + "行数据,承运商编号一不能为空");
			} else if (!carrier_map.containsKey(requestTr_ImportMoreCarrierVO.getCarrier_no_one())) {
				msgList.add("第" + line + "行数据,承运商编号一不存在");
			} else {
				requestTr_ImportMoreCarrierVO.setCarrier_name_one(carrier_map.get(requestTr_ImportMoreCarrierVO.getCarrier_no_one()));
			}
			//运输方式一
			if(StringUtils.isBlank(requestTr_ImportMoreCarrierVO.getTrans_mode_one())){
				msgList.add("第"+line+"行数据,运输方式一不能为空");
			}else if(!trans_modeSet.contains(requestTr_ImportMoreCarrierVO.getTrans_mode_one())){
				msgList.add("第"+line+"行数据,运输方式一不存在");
			}
			//起运地目的地
			if (StringUtils.isBlank(requestTr_ImportMoreCarrierVO.getBegon_city_one())) {
				msgList.add("第" + line + "行数据,起运地一不能为空");
			}
			if (StringUtils.isBlank(requestTr_ImportMoreCarrierVO.getEnd_city_one())) {
				msgList.add("第" + line + "行数据,目的地一不能为空");
			}
			//第一段承运商
			insertVO.setVin(requestTr_ImportMoreCarrierVO.getVin());
			insertVO.setCarrier_no(requestTr_ImportMoreCarrierVO.getCarrier_no_one());
			insertVO.setCarrier_name(requestTr_ImportMoreCarrierVO.getCarrier_name_one());
			insertVO.setTrans_mode(requestTr_ImportMoreCarrierVO.getTrans_mode_one());
			insertVO.setBegin_city(requestTr_ImportMoreCarrierVO.getBegon_city_one());
			insertVO.setEnd_city(requestTr_ImportMoreCarrierVO.getEnd_city_one());
			insertVO.setCreate_by(sysUserVO.getRealName());
			insertCarrierList.add(insertVO);
			/**所有字段必然不能为空**/
			if (StringUtils.isNotBlank(requestTr_ImportMoreCarrierVO.getCarrier_no_two()) && StringUtils.isNotBlank(requestTr_ImportMoreCarrierVO.getTrans_mode_two())
			&& StringUtils.isNotBlank(requestTr_ImportMoreCarrierVO.getBegon_city_two()) && StringUtils.isNotBlank(requestTr_ImportMoreCarrierVO.getEnd_city_two())) {

			}

		}
		ResultVO resultVO = new ResultVO();
		if (msgList.size()>0) {
			resultVO.setResultCode("3");//错误
			resultVO.setResultDataFull(msgList);
		} else {
			resultVO.setResultCode("0");//正确
			resultVO.setResultDataFull(requestTr_ImportMoreCarrierVOList);
		}
		return resultVO;
	}
}
