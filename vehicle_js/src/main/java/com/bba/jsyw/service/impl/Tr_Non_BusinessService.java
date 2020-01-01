package com.bba.jsyw.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.constant.*;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.jcda.dao.Cr_CustomerDao;
import com.bba.jcda.dao.Zd_CarrierDao;
import com.bba.jcda.vo.Cr_CustomerVO;
import com.bba.jcda.vo.Zd_CarrierVO;
import com.bba.jsyw.dao.ITr_Non_BusinessDao;
import com.bba.jsyw.dao.ITr_Non_Business_CarrierDao;
import com.bba.jsyw.dto.Tr_Non_BusinessDTO;
import com.bba.jsyw.service.api.ITr_Non_BusinessService;
import com.bba.jsyw.vo.Tr_Non_BusinessVO;
import com.bba.jsyw.vo.Tr_Non_Business_CarrierVO;
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
public class Tr_Non_BusinessService extends ServiceImpl<ITr_Non_BusinessDao, Tr_Non_BusinessVO> implements ITr_Non_BusinessService {
	@Resource
	private ITr_Non_BusinessDao tr_non_BusinessDao;

	@Resource
	private ITr_Non_Business_CarrierDao tr_non_Business_CarrierDao;

	@Resource
	private Sys_Dictionary_DataDao sys_dictionary_dataDao;

	@Resource
	private Zd_CarrierDao zd_CarrierDao;

	@Resource
	private Cr_CustomerDao cr_CustomerDao;

	@Override
	public PageVO findBusinessPageVO(JqGridParamModel jqGridParamModel, String customSearchFilters) {
		String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		List<Tr_Non_BusinessVO> list = tr_non_BusinessDao.findListForGrid(jqGridParamModel);
		int count = tr_non_BusinessDao.findListForGridCount(jqGridParamModel);
		return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
	}

	@Override
	public ResultVO updateJsState(List<Tr_Non_BusinessVO> vos) {
		tr_non_BusinessDao.updateJsState(vos);
		return ResultVO.successResult("操作成功");
	}

	@Override
	public Tr_Non_BusinessDTO getDetail(Tr_Non_BusinessVO vo) {
		Tr_Non_BusinessVO paramTr_non_BusinessVO = new Tr_Non_BusinessVO();
		paramTr_non_BusinessVO.setId(vo.getId());
		Tr_Non_BusinessVO dataTr_Non_BusinessVO = tr_non_BusinessDao.selectOne(paramTr_non_BusinessVO);
		if(dataTr_Non_BusinessVO == null){
			return null;
		}
		EntityWrapper tr_Non_BusinessVOEntityWrapper = new EntityWrapper();
		Tr_Non_Business_CarrierVO paramTr_Non_Business_CarrierVO = new Tr_Non_Business_CarrierVO();
		paramTr_Non_Business_CarrierVO.setM_id(dataTr_Non_BusinessVO.getId());
		tr_Non_BusinessVOEntityWrapper.setEntity(paramTr_Non_Business_CarrierVO);
		tr_Non_BusinessVOEntityWrapper.orderBy("id", true);
		List<Tr_Non_Business_CarrierVO> dataTr_Non_Business_CarrierVOList = tr_non_Business_CarrierDao.selectList(tr_Non_BusinessVOEntityWrapper);
		Tr_Non_BusinessDTO returnTr_Non_BusinessDTO = new Tr_Non_BusinessDTO();
		returnTr_Non_BusinessDTO.setTr_non_businessVO(dataTr_Non_BusinessVO);
		returnTr_Non_BusinessDTO.setTr_non_Business_CarrierVOList(dataTr_Non_Business_CarrierVOList);
		return returnTr_Non_BusinessDTO;
	}

	/***
	 * 导入校验规则
	 * @param requestTr_Non_BusinessVOList
	 * @param businessData_projectEnum
	 * @return
	 */
	private ResultVO verificationDetail(List<Tr_Non_BusinessVO> requestTr_Non_BusinessVOList, BusinessData_projectEnum businessData_projectEnum) {
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
		//客户
		Cr_CustomerVO paramCr_CustomerVO = new Cr_CustomerVO();
		paramCr_CustomerVO.setStatus("0");//正式客户
		EntityWrapper cr_customerWrapper = new EntityWrapper();
		cr_customerWrapper.setEntity(paramCr_CustomerVO);
		List<Cr_CustomerVO> cusVOList = cr_CustomerDao.selectList(cr_customerWrapper);
		Map<String,String> cus_map = new HashMap<>();
		for(Cr_CustomerVO cus_vo: cusVOList){
			cus_map.put(cus_vo.getCode(),cus_vo.getName());
		}
		List<String> msgList = new ArrayList<String>();
		for(int i = 0, line = 1; i < requestTr_Non_BusinessVOList.size(); i++, line++) {
			Tr_Non_BusinessVO requestTr_Non_BusinessVO = requestTr_Non_BusinessVOList.get(i);
			//承运商 付款单位判断,付款单位可以为空(VIP)
			if (StringUtils.isBlank(requestTr_Non_BusinessVO.getCarrier_no())) {
				msgList.add("第" + line + "行业务数据,承运商不能为空");
			} else if (!carrier_map.containsKey(requestTr_Non_BusinessVO.getCarrier_no())) {
				msgList.add("第" + line + "行业务数据,承运商不存在");
			} else {
				requestTr_Non_BusinessVO.setCarrier_name(carrier_map.get(requestTr_Non_BusinessVO.getCarrier_no()));
			}
			//客户
			if (StringUtils.isNotBlank(requestTr_Non_BusinessVO.getCus_no())) {
				if (!cus_map.containsKey(requestTr_Non_BusinessVO.getCus_no())) {
					msgList.add("第" + line + "行业务数据,付款单位不存在");
				} else {
					requestTr_Non_BusinessVO.setCus_name(cus_map.get(requestTr_Non_BusinessVO.getCus_no()));
				}

			}
			if(StringUtils.isBlank(requestTr_Non_BusinessVO.getTrans_mode())){
				msgList.add("第"+line+"行业务数据,运输方式不能为空");
			}else if(!trans_modeSet.contains(requestTr_Non_BusinessVO.getTrans_mode())){
				msgList.add("第"+line+"行业务数据,运输方式不存在");
			}

			if (requestTr_Non_BusinessVO.getDemand_sector() == null) {
				msgList.add("第" + line + "行业务数据,需求部门不能为空");
			}
			if (requestTr_Non_BusinessVO.getApplicant() == null) {
				msgList.add("第" + line + "行业务数据,申请人（发运指令人）不能为空");
			}
			if (requestTr_Non_BusinessVO.getPeizai_info() == null) {
				msgList.add("第" + line + "行业务数据,配载说明不能为空");
			}
			if (requestTr_Non_BusinessVO.getSecrecy_flag() == null) {
				msgList.add("第" + line + "行业务数据,是否保密不能为空");
			} else if (StringUtils.notEquals(requestTr_Non_BusinessVO.getSecrecy_flag(), "Y") && StringUtils.notEquals(requestTr_Non_BusinessVO.getSecrecy_flag(), "N")) {
				msgList.add("第" + line + "行业务数据,是否保密只能录入“N”或“Y”");
			}
			if (requestTr_Non_BusinessVO.getBegin_city() == null) {
				msgList.add("第" + line + "行业务数据,启运城市不能为空");
			}
			if (requestTr_Non_BusinessVO.getEnd_city() == null) {
				msgList.add("第" + line + "行业务数据,目的城市不能为空");
			}
			if (requestTr_Non_BusinessVO.getHandover_no() == null) {
				msgList.add("第" + line + "行业务数据,交接单号不能为空");
			}
			if (requestTr_Non_BusinessVO.getBegin_date() == null || requestTr_Non_BusinessVO.getReceipt_date() == null) {
				msgList.add("第" + line + "行业务数据,发运时间或收车时间不能为空");
			} else if (!(requestTr_Non_BusinessVO.getBegin_date().getTime() < requestTr_Non_BusinessVO.getReceipt_date().getTime())) {
				msgList.add("第" + line + "行业务数据,发运时间必须小于收车时间");
			}
			if (requestTr_Non_BusinessVO.getShipment_qty() == null) {
				msgList.add("第" + line + "行业务数据,承运数量不能为空");
			}
			if (requestTr_Non_BusinessVO.getUp_js_qty() == null) {
				msgList.add("第" + line + "行业务数据,对上结费数量不能为空");
			}
			if (requestTr_Non_BusinessVO.getDown_js_qty() == null) {
				msgList.add("第" + line + "行业务数据,对下结费数量不能为空");
			}
			if (requestTr_Non_BusinessVO.getJs_jiage() == null) {
				msgList.add("第" + line + "行业务数据,结算价格不能为空");
			}
			if (requestTr_Non_BusinessVO.getVin() == null) {
				msgList.add("第" + line + "行业务数据,VIN不能为空");
			}
			if (requestTr_Non_BusinessVO.getCar_type() == null) {
				msgList.add("第" + line + "行业务数据,小车车型不能为空");
			}
		}
		ResultVO resultVO = new ResultVO();
		if (msgList.size()>0) {
			resultVO.setResultCode("3");//错误
			resultVO.setResultDataFull(msgList);
		} else {
			resultVO.setResultCode("0");//正确
			resultVO.setResultDataFull(requestTr_Non_BusinessVOList);
		}
		return resultVO;
	}

	@Override
	public ResultVO importData(MultipartFile multipartFile, BusinessData_projectEnum businessData_projectEnum) {
		SysUserVO sysUserVO = SessionUtils.currentSession();
		List<Tr_Non_BusinessVO> requestTr_Non_BusinessVOList = null;
		try {
			requestTr_Non_BusinessVOList = ExcelImportUtil.importExcel(multipartFile.getInputStream(), Tr_Non_BusinessVO.class, new ImportParams());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResultVO resultVO = verificationDetail(requestTr_Non_BusinessVOList, businessData_projectEnum);
		List<String> msgList = new ArrayList<String>();
		if (resultVO.getResultCode().equals("3")) {
			msgList = (List<String>) resultVO.getResultDataFull();
		} else {
			requestTr_Non_BusinessVOList = (List<Tr_Non_BusinessVO>) resultVO.getResultDataFull();
		}
		if(msgList.size() > 0){
			return ResultVO.failResult(ArrayUtils.join(msgList, "<br/>"));
		}
		for (Tr_Non_BusinessVO requestTr_Non_BusinessVO: requestTr_Non_BusinessVOList) {
			//非商品车插入到主表
			requestTr_Non_BusinessVO.setCreate_by(sysUserVO.getRealName());
			requestTr_Non_BusinessVO.setAttribute(BusinessNon_CommonEnum.ATT_IN_SYSTEM.getCode());
			//设置分类
			/***
			 * 分类逻辑：1、首先判断付款单位是否为空，为空则为VIP
			 * 			 2、判断结算价格是否为“救援车”，如是，则为救援车
			 * 			 3、判断是否保密，如是，则分为保密
			 * 			 4、其他为合同价
			 */

			if (StringUtils.isBlank(requestTr_Non_BusinessVO.getCus_no())) {
				requestTr_Non_BusinessVO.setType(BusinessNon_CommonEnum.TYPE_VIP.getCode());
				requestTr_Non_BusinessVO.setCus_no("对上不收费");
				requestTr_Non_BusinessVO.setCus_name("对上不收费");
			} else if (requestTr_Non_BusinessVO.getJs_jiage().indexOf("救援车")!=-1) {
				requestTr_Non_BusinessVO.setType(BusinessNon_CommonEnum.TYPE_JYC.getCode());
			} else if (requestTr_Non_BusinessVO.getSecrecy_flag().equals("Y")) {
				requestTr_Non_BusinessVO.setType(BusinessNon_CommonEnum.TYPE_SECRET.getCode());
			} else {
				requestTr_Non_BusinessVO.setType(BusinessNon_CommonEnum.TYPE_HTJ.getCode());
			}
			requestTr_Non_BusinessVO.setVehicle_project(BusinessData_projectEnum.NON_COMMODITY_CAR.getCode());
			requestTr_Non_BusinessVO.setData_from(BusinessData_FromEnum.FROM_MANUAL.getCode());
			tr_non_BusinessDao.insert(requestTr_Non_BusinessVO);
			//插入到明细
			Tr_Non_Business_CarrierVO tr_non_business_carrierVO = new Tr_Non_Business_CarrierVO();
			tr_non_business_carrierVO.setCreate_by(sysUserVO.getRealName());
			tr_non_business_carrierVO.setM_id(requestTr_Non_BusinessVO.getId());
			tr_non_business_carrierVO.setTrans_mode(requestTr_Non_BusinessVO.getTrans_mode());
			tr_non_business_carrierVO.setBusiness_order("1");
			tr_non_business_carrierVO.setCarrier_no(requestTr_Non_BusinessVO.getCarrier_no());
			tr_non_business_carrierVO.setCarrier_name(requestTr_Non_BusinessVO.getCarrier_name());
			tr_non_business_carrierVO.setBegin_city(requestTr_Non_BusinessVO.getBegin_city());
			tr_non_business_carrierVO.setEnd_city(requestTr_Non_BusinessVO.getEnd_city());
			tr_non_business_carrierVO.setDown_js_qty(requestTr_Non_BusinessVO.getDown_js_qty());
			tr_non_Business_CarrierDao.insert(tr_non_business_carrierVO);
		}
		return ResultVO.successResult("导入成功！");
	}

	@Override
	public ResultVO saveDetail(Tr_Non_BusinessDTO requestTr_Non_BusinessDTO, SysUserVO sysUserVO, MultipartFile commonsMultipartFile) {
		Tr_Non_BusinessVO requestTr_Non_BusinessVO = requestTr_Non_BusinessDTO.getTr_non_businessVO();
		List<Tr_Non_Business_CarrierVO> requestTr_Non_Business_CarrierVOList = requestTr_Non_BusinessDTO.getTr_non_Business_CarrierVOList();

		if(requestTr_Non_BusinessVO.getBegin_date() == null || requestTr_Non_BusinessVO.getReceipt_date() == null){
			return ResultVO.failResult("业务起运、收车时间不能为空");
		}else if(!(requestTr_Non_BusinessVO.getBegin_date().getTime() <= requestTr_Non_BusinessVO.getReceipt_date().getTime())){
			return ResultVO.failResult("起运时间必须小于收车时间");
		}

		if(requestTr_Non_Business_CarrierVOList.size() == 0){
			return ResultVO.failResult("必须保留一个承运商");
		}
		//上传文件
		String oldFile = null;
		String rootPath= null;
		if(commonsMultipartFile != null) {
			//String separator= File.separator;
			rootPath= (System.getProperty("user.dir")+"/Upload/business/").replace("\\","/");
			//String fileName = DateUtils.dateFormat(new Date(), "yyyyMMdd_HHmmddsss");
			//String file_end_with = commonsMultipartFile.getOriginalFilename().substring(commonsMultipartFile.getOriginalFilename().lastIndexOf("."));
			String newPath = commonsMultipartFile.getOriginalFilename();
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
			String path = rootPath+newPath;
			requestTr_Non_BusinessVO.setUpload_files(path);
		}else {
			oldFile = null;
		}

		/**校验*/
		/*	List<String> msgList = verificationDetail(requestTr_BusinessVO);
		if(msgList.size() > 0){
			return ResultVO.failResult(ArrayUtils.join(msgList, "<br/>"));
		}*/

		if("".equals(requestTr_Non_BusinessVO.getId())|| requestTr_Non_BusinessVO.getId() ==0){
			//新增，验证vin、运单是否唯一
			requestTr_Non_BusinessVO.setData_from(BusinessData_FromEnum.FROM_MANUAL.getCode());
			Tr_Non_BusinessVO paramTr_Non_BusinessVO = new Tr_Non_BusinessVO();
			paramTr_Non_BusinessVO.setVin(requestTr_Non_BusinessVO.getVin());
			Tr_Non_BusinessVO dataTr_Non_BusinessVO = tr_non_BusinessDao.selectOne(paramTr_Non_BusinessVO);
			if(dataTr_Non_BusinessVO != null){
				return ResultVO.failResult("VIN码已存在,请勿重复录入"+requestTr_Non_BusinessVO.getVin());
			}
			requestTr_Non_BusinessVO.setCreate_by(sysUserVO.getRealName());
			tr_non_BusinessDao.insert(requestTr_Non_BusinessVO);
		}else{
			Tr_Non_BusinessVO paramTr_Non_BusinessVO = new Tr_Non_BusinessVO();
			paramTr_Non_BusinessVO.setId(requestTr_Non_BusinessVO.getId());
			Tr_Non_BusinessVO dataTr_Non_BusinessVO = tr_non_BusinessDao.selectOne(paramTr_Non_BusinessVO);
			if(!BusinessData_StateEnum.NORMAL.getCode().equals(dataTr_Non_BusinessVO.getData_state())){
				return ResultVO.failResult("只允许正常状态下的数据进行修改操作");
			}
			if (commonsMultipartFile != null) {
				oldFile = dataTr_Non_BusinessVO.getUpload_files();
				//requestTr_Non_BusinessVO.setUpload_files(oldFile);
			}
			tr_non_BusinessDao.updateById(requestTr_Non_BusinessVO);

			EntityWrapper tr_non_business_carrierEntityWrapper = new EntityWrapper();
			Tr_Non_Business_CarrierVO paramTr_Non_Business_CarrierVO = new Tr_Non_Business_CarrierVO();
			paramTr_Non_Business_CarrierVO.setM_id(requestTr_Non_BusinessVO.getId());
			tr_non_business_carrierEntityWrapper.setEntity(paramTr_Non_Business_CarrierVO);
			List<Tr_Non_Business_CarrierVO> dataTr_Non_Business_CarrierVOList = tr_non_Business_CarrierDao.selectList(tr_non_business_carrierEntityWrapper);

			//如果老数据里不存在新的数据List，就删除，存在就新增
			List<Tr_Non_Business_CarrierVO> deleteTr_Non_Business_CarrierVOVOList = new ArrayList<Tr_Non_Business_CarrierVO>();
			List<Tr_Non_Business_CarrierVO> updateTr_Non_Business_CarrierVOList = new ArrayList<Tr_Non_Business_CarrierVO>();

			for(Tr_Non_Business_CarrierVO dataTr_Non_Business_CarrierVO: dataTr_Non_Business_CarrierVOList){
				boolean bool = false;
				for(Tr_Non_Business_CarrierVO requestTr_Non_Business_CarrierVO: requestTr_Non_Business_CarrierVOList){
					if(dataTr_Non_Business_CarrierVO.getId().equals(requestTr_Non_Business_CarrierVO.getId())){
						updateTr_Non_Business_CarrierVOList.add(requestTr_Non_Business_CarrierVO);
						bool = true;
						break;
					}
				}
				if(!bool){
					deleteTr_Non_Business_CarrierVOVOList.add(dataTr_Non_Business_CarrierVO);
				}
			}

			for(Tr_Non_Business_CarrierVO deleteVO: deleteTr_Non_Business_CarrierVOVOList){
				tr_non_Business_CarrierDao.deleteById(deleteVO.getId());
			}
			for(Tr_Non_Business_CarrierVO updateVO: updateTr_Non_Business_CarrierVOList){
				tr_non_Business_CarrierDao.updateById(updateVO);
			}

		}
		for(Tr_Non_Business_CarrierVO vo: requestTr_Non_Business_CarrierVOList){
			if(vo.getId() == null || vo.getId() == 0){
				vo.setM_id(requestTr_Non_BusinessVO.getId());
				vo.setCreate_by(sysUserVO.getRealName());
				vo.setCreate_date(new Date());
				tr_non_Business_CarrierDao.insert(vo);
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
	public ResultVO batchCancel(List<Tr_Non_BusinessVO> tr_non_businessVOs,SysUserVO sysUserVO) {
		for (Tr_Non_BusinessVO requestTr_Non_BusinessVO: tr_non_businessVOs) {
			Tr_Non_BusinessVO paramTr_Non_BusinessVO = new Tr_Non_BusinessVO();
			paramTr_Non_BusinessVO.setId(requestTr_Non_BusinessVO.getId());
			Tr_Non_BusinessVO dataTr_Non_BusinessVO = tr_non_BusinessDao.selectOne(paramTr_Non_BusinessVO);
			if(dataTr_Non_BusinessVO == null){
				return ResultVO.failResult("业务数据不存在:"+requestTr_Non_BusinessVO.getVin());
			}else if(!BusinessData_StateEnum.NORMAL.getCode().equals(dataTr_Non_BusinessVO.getData_state())){
				return ResultVO.failResult("只允许正常状态下的业务数据进行注销操作:"+requestTr_Non_BusinessVO.getVin());
			}
			Tr_Non_BusinessVO updateTr_Non_BusinessVO = new Tr_Non_BusinessVO();
			updateTr_Non_BusinessVO.setId(dataTr_Non_BusinessVO.getId());
			updateTr_Non_BusinessVO.setData_state(BusinessData_StateEnum.CANCEL.getCode());
			updateTr_Non_BusinessVO.setCheck_by(sysUserVO.getRealName());
			updateTr_Non_BusinessVO.setCheck_date(new Date());
			tr_non_BusinessDao.updateById(updateTr_Non_BusinessVO);
		}
		return ResultVO.successResult("注销成功！");
	}

	@Override
	public ResultVO check(List<Tr_Non_BusinessVO> vos,SysUserVO sysUserVO) {
		/**
		 * 1、正常的数据才可以审核
		 * 2、重损质损不可以审核
		 * 3、插入数据到对上对下结算表、对下保费表
		 * 4、改变数据状态
		 * */
		try {
			for (Tr_Non_BusinessVO requestVO: vos) {
				//插入到对上结算表
				tr_non_BusinessDao.insertJs_non_vehicle(requestVO.getId(),sysUserVO.getRealName());
				//插入到对下结算表
				tr_non_BusinessDao.insertJs_non_down_vehicle(requestVO.getId(),sysUserVO.getRealName());
				//插入到对下保费结算表
				tr_non_BusinessDao.insertJs_vin_down_premium(requestVO.getId(),sysUserVO.getRealName());
				//修改审核状态
				Tr_Non_BusinessVO updateTr_Non_BusinessVO = new Tr_Non_BusinessVO();
				updateTr_Non_BusinessVO.setId(requestVO.getId());
				updateTr_Non_BusinessVO.setCheck_by(sysUserVO.getRealName());
				updateTr_Non_BusinessVO.setCheck_date(new Date());
				updateTr_Non_BusinessVO.setData_state(BusinessData_StateEnum.CHECK.getCode());
				tr_non_BusinessDao.updateById(updateTr_Non_BusinessVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return ResultVO.successResult("审核成功！");
	}
}
