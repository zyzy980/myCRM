package com.bba.jcda.service.impl;

import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.ht.vo.Ht_CusVO;
import com.bba.jcda.dao.ICr_CustomerDao;
import com.bba.jcda.service.api.ICrCustomerService;
import com.bba.jcda.vo.CrCustomerVO;
import com.bba.jcda.vo.Cr_Customer_LinkVO;
import com.bba.util.*;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CrCustomerService implements ICrCustomerService {
	@Resource
	private ICr_CustomerDao dao;

	@Override
	public PageVO findCustomerListPageVO(JqGridParamModel jqGridParamModel, String customSearchFilters) {
		String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		List<CrCustomerVO> list = dao.findListForGrid(jqGridParamModel);
		int count = dao.findListForGridCount(jqGridParamModel);
		return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
	}

	@Override
	public ResultVO getCusDetail(CrCustomerVO vo) {
		ResultVO resultVO = ResultVO.successResult();
		CrCustomerVO cr_customerVO = dao.getDetail(vo);
		if (null==cr_customerVO) {
			return ResultVO.failResult("未查询到任何数据");
		}
		resultVO.setResultDataFull(cr_customerVO);
		return resultVO;
	}

	@Override
	public ResultVO SaveCus(CrCustomerVO vo, HttpSession session) {
		try {
			SysUserVO sysUserVO = (SysUserVO)SessionUtils.currentSession();
			String userName = sysUserVO.getRealName();
			//判断客户表
			if("".equals(vo.getId())||"0".equals(vo.getId())){ //新增
				CrCustomerVO vos = dao.getDetail(vo);
				if (vos!= null) {
					return ResultVO.failResult("客户编号已重复"+vos.getCode());
				}
				vo.setCreate_by(userName);
				vo.setStatus("0");
				dao.insertData(vo);
			}else{
				//保存，非正常状态无法保存
				if (StringUtils.notEquals(vo.getStatus(),"正常")) {
					return ResultVO.failResult("非‘正常’状态无法进行此操作");
				} else {
					vo.setStatus("0");
				}
				dao.updateData(vo);
			}
			//获取客户地址及联系人信息
			CrCustomerVO paramVO = dao.getDetail(vo);
			List<Cr_Customer_LinkVO> linklList = vo.getCrCusLinks();
			List<Cr_Customer_LinkVO> insert = new ArrayList<>();
			List<Cr_Customer_LinkVO> update = new ArrayList<>();
			for (Cr_Customer_LinkVO linkVO : linklList){
				linkVO.setCus_id(paramVO.getId());
				linkVO.setCus_code(vo.getCode());
				linkVO.setCreate_by(userName);
				//判断添加还是修改
				if(StringUtils.equals("add",linkVO.getOperatetype())){
					insert.add(linkVO);
				}else{
					update.add(linkVO);
				}
			}
			if(insert.size()>0){
				dao.batchInsertLinks(insert);
			}else if(update.size()>0){
				for (Cr_Customer_LinkVO l_vo:update) {
					dao.updateLink(l_vo);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("保存数据失败，请联系管理员");
		}
		return ResultVO.successResult("操作成功");
	}

	@Override
	public void deleteDetail(List<Cr_Customer_LinkVO> vos) {
		dao.deleteLinks(vos);
	}

	@Override
	public ResultVO batchRecovery(List<CrCustomerVO> customerVOs) {
		dao.batchUpdate(customerVOs);
		return ResultVO.successResult("恢复成功");
	}

	@Override
	public PageVO findzdCusLinkPageVO(JqGridParamModel jqGridParamModel, String customSearchFilters) {
		String filters = JqGridSearchParamHandlers.processSql(customSearchFilters, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		List<Cr_Customer_LinkVO> list = dao.findCusLinkListForGrid(jqGridParamModel);
		int count = dao.findCusLinkListForGridCount(jqGridParamModel);
		return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
	}

	@Override
	public ResultVO batchDelete(List<CrCustomerVO> customerVOs) {
		/**注销前提:客户没有合同存在*/
		for (CrCustomerVO removeVo:customerVOs) {
			//正常或者审核
			List<Ht_CusVO> ht_cusVOList =  dao.selectHtList(removeVo);
			if (ht_cusVOList.size()>0) {
				return ResultVO.failResult("客户存在未注销的合同，无法注销，客户编号："+removeVo.getCode());
			}
		}
		dao.batchDelete(customerVOs);
		return ResultVO.successResult("注销成功");
	}
}
