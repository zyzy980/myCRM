package com.bba.jcda.service.impl;

import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.ht.vo.Ht_CarrierVO;
import com.bba.jcda.dao.IZd_CarrierDao;
import com.bba.jcda.service.api.IZdCarrierService;
import com.bba.jcda.vo.Zd_CarrierVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.util.SessionUtils;
import com.bba.util.StringUtils;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
@Transactional
public class ZdCarrierService implements IZdCarrierService {
	@Resource
	private IZd_CarrierDao dao;

	@Override
	public PageVO findCarrierListPageVO(JqGridParamModel jqGridParamModel, String customSearchFilters) {
		String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		List<Zd_CarrierVO> list = dao.findListForGrid(jqGridParamModel);
		int count = dao.findListForGridCount(jqGridParamModel);
		return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
	}

	@Override
	public ResultVO getCarDetail(Zd_CarrierVO vo) {
		ResultVO resultVO = ResultVO.successResult();
		Zd_CarrierVO zd_CarrierVO = dao.getDetail(vo);
		if (null==zd_CarrierVO) {
			return ResultVO.failResult("未查询到任何数据");
		}
		resultVO.setResultDataFull(zd_CarrierVO);
		return resultVO;
	}

	@Override
	public ResultVO SaveCus(Zd_CarrierVO vo, HttpSession session) {
		try {
			SysUserVO sysUserVO = (SysUserVO)SessionUtils.currentSession();
			String userName = sysUserVO.getRealName();
			//判断客户表
			if("".equals(vo.getId())||"0".equals(vo.getId())){ //新增
				Zd_CarrierVO vos = dao.getDetail(vo);
				if (vos!= null) {
					return ResultVO.failResult("承运商编号已重复"+vos.getCode());
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("保存数据失败，请联系管理员");
		}
		return ResultVO.successResult("操作成功");
	}

	@Override
	public ResultVO batchDelete(List<Zd_CarrierVO> vos) {
		for (Zd_CarrierVO removeVo:vos) {
			//正常或者审核
			List<Ht_CarrierVO> ht_carrierVOList =  dao.selectHtList(removeVo);
			if (ht_carrierVOList.size()>0) {
				return ResultVO.failResult("承运商存在未注销的合同，无法注销，承运商编号："+removeVo.getCode());
			}
		}
		dao.batchDelete(vos);
		return ResultVO.successResult("注销成功");
	}

	@Override
	public ResultVO batchRecovery(List<Zd_CarrierVO> vos) {
		dao.batchUpdate(vos);
		return ResultVO.successResult("恢复成功");
	}
}
