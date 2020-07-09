package com.bba.settlement.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.constant.BaseDataKeyEnum;
import com.bba.common.constant.SETTLEMENT_StateEnum;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.jcda.vo.Zd_CarrierVO;
import com.bba.settlement.dao.IJs_Down_PremiumDao;
import com.bba.settlement.service.api.IJs_Down_PremiumService;
import com.bba.settlement.vo.Js_Vin_Down_PremiumVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.util.StringUtils;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class Js_Down_PremiumServiceimpl extends ServiceImpl<IJs_Down_PremiumDao, Js_Vin_Down_PremiumVO> implements IJs_Down_PremiumService {
	@Resource
	private IJs_Down_PremiumDao js_down_premiumDao;

	@Override
	public PageVO findPremiumPageVO(JqGridParamModel jqGridParamModel, String customSearchFilters) {
		String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		List<Js_Vin_Down_PremiumVO> list = js_down_premiumDao.findListForGrid(jqGridParamModel);
		int count = js_down_premiumDao.findListForGridCount(jqGridParamModel);
		return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
	}

	@Override
	public PageVO findPremiumGroupPageVO(JqGridParamModel jqGridParamModel, String customSearchFilters) {
		String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		List<Js_Vin_Down_PremiumVO> list = js_down_premiumDao.findListGroupForGrid(jqGridParamModel);
		int count = js_down_premiumDao.findListGroupForGridCount(jqGridParamModel);
		return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
	}

	@Override
	public ResultVO settlement(List<Js_Vin_Down_PremiumVO> vos, SysUserVO sysUserVO) {
		/**结算步骤
		 * 1、判断是否有保险公司承运商，拿到保险公司的税率（计算含税）
		 * 2、拿到集合后，首先根据选择的数据查询所有需要结算的明细
		 * 3、拿到车型相对应的保费(单价)
		 * 4、开始计算，更新数据的费用+计算人+税率+状态
		 * */
		//查询保险公司，获取税率
		Zd_CarrierVO requestZd_CarrierVO = new Zd_CarrierVO();
		requestZd_CarrierVO.setType(BaseDataKeyEnum.ZD_CARRIER_PREMIUM.getCode());
		List<Zd_CarrierVO> zd_carrierVOList = js_down_premiumDao.findzd_carrierVOListByProperty(requestZd_CarrierVO);
		if (zd_carrierVOList.size()==0) {
			return ResultVO.failResult("系统没有保险公司档案设置，请到承运商档案设置");
		}
		requestZd_CarrierVO.setTax_rate(zd_carrierVOList.get(0).getTax_rate());
		//查询字典，获取车型及保费集合
		List<Js_Vin_Down_PremiumVO> carPremiumList = js_down_premiumDao.getCarPremiumList();
		Map<String,BigDecimal> carPremiumMap = new HashMap();
		for (Js_Vin_Down_PremiumVO mapvo:carPremiumList) {
			if(StringUtils.isBlank(mapvo.getCar_type())) {
				return ResultVO.failResult("系统字典-校车车型 异常，请联系管理员");
			}
			if(!StringUtils.isNumberspace(mapvo.getRemark())) {
				return ResultVO.failResult("系统字典-校车车型 保费异常，请联系管理员");
			}
			carPremiumMap.put(mapvo.getCar_type(),new BigDecimal(mapvo.getRemark()));
		}
		//一层循环
		try {
			for (Js_Vin_Down_PremiumVO vo:vos) {
				if (!carPremiumMap.containsKey(vo.getCar_type())) {
					throw new RuntimeException("该车型不存在系统，请联系管理员");
				}
				Js_Vin_Down_PremiumVO requestJs_Vin_Down_PremiumVO = new Js_Vin_Down_PremiumVO();
				requestJs_Vin_Down_PremiumVO.setBegin_date(vo.getBegin_date());
				requestJs_Vin_Down_PremiumVO.setCar_type(vo.getCar_type());
				EntityWrapper Js_Vin_Down_PremiumVOEntityWrapper = new EntityWrapper();
				Js_Vin_Down_PremiumVOEntityWrapper.setEntity(requestJs_Vin_Down_PremiumVO);
				List<Js_Vin_Down_PremiumVO> js_vin_down_premiumVOList = js_down_premiumDao.selectList(Js_Vin_Down_PremiumVOEntityWrapper);
				//二层循环-开始结算
				for (Js_Vin_Down_PremiumVO js_vin_down_premiumVO:js_vin_down_premiumVOList) {
					Js_Vin_Down_PremiumVO updateJs_vin_down_premiumVO = new Js_Vin_Down_PremiumVO();
					updateJs_vin_down_premiumVO.setId(js_vin_down_premiumVO.getId());
					updateJs_vin_down_premiumVO.setJs_by(sysUserVO.getRealName());
					updateJs_vin_down_premiumVO.setJs_date(new Date());
					updateJs_vin_down_premiumVO.setTax_rate(requestZd_CarrierVO.getTax_rate());
					//单价即保费，根据车型;不含税合计，单台价，也是保费价格
					updateJs_vin_down_premiumVO.setNot_tax_price(carPremiumMap.get(js_vin_down_premiumVO.getCar_type()));
					updateJs_vin_down_premiumVO.setNot_tax_amount(carPremiumMap.get(js_vin_down_premiumVO.getCar_type()));
					//含税合计=不含税金额*(1+税率)
					updateJs_vin_down_premiumVO.setTax_amount(updateJs_vin_down_premiumVO.getNot_tax_amount().multiply(requestZd_CarrierVO.getTax_rate().add(new BigDecimal("1"))));
					updateJs_vin_down_premiumVO.setJs_state(SETTLEMENT_StateEnum.SETTLEMENT.getCode());
					js_down_premiumDao.updateById(updateJs_vin_down_premiumVO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return ResultVO.successResult("结算成功");
	}

	@Override
	public PageVO selectPremiumGroupByMonth(JqGridParamModel jqGridParamModel, String customSearchFilters) {
		String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
		jqGridParamModel.setFilters(filters);
		List<Js_Vin_Down_PremiumVO> list = js_down_premiumDao.selectPremiumGroupByMonth(jqGridParamModel);
		int count = js_down_premiumDao.selectPremiumGroupByMonthCount(jqGridParamModel);
		return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
	}


}
