package com.bba.bbgl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.bbgl.dao.Js_Analysis_SummaryDao;
import com.bba.bbgl.dao.Js_Analysis_Summary_DetailDao;
import com.bba.bbgl.dto.Js_Analysis_SummaryDTO;
import com.bba.bbgl.service.api.IJs_Analysis_SummaryService;
import com.bba.bbgl.vo.Js_Analysis_SummaryVO;
import com.bba.bbgl.vo.Js_Analysis_Summary_DetailVO;
import com.bba.common.common.Sys_sheetidUtil;
import com.bba.common.constant.*;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.dz.dao.Js_Dz_Sheet_DetailDao;
import com.bba.fpgl.dao.IReceivable_invoiceDao;
import com.bba.fpgl.vo.Receivable_invoiceVO;
import com.bba.jcda.vo.Tr_statistical_rulesVO;
import com.bba.jcda.vo.Zd_CarrierVO;
import com.bba.nosettlement.vo.Js_Non_VehicleVO;
import com.bba.settlement.dao.IJs_Down_PremiumDao;
import com.bba.settlement.dao.Js_Vin_AmountDao;
import com.bba.settlement.vo.Js_Vin_AmountVO;
import com.bba.settlement.vo.Js_Vin_Down_AmountVO;
import com.bba.settlement.vo.Js_Vin_Down_PremiumVO;
import com.bba.settlement.vo.VehicleTotalVO;
import com.bba.tzgl.dao.AccountsToLedgerDao;
import com.bba.tzgl.dao.Js_CompensationDao;
import com.bba.tzgl.dao.LedgerDao;
import com.bba.tzgl.dao.Ledger_DetailDao;
import com.bba.tzgl.dto.LedgerDTO;
import com.bba.tzgl.service.api.ILedgerService;
import com.bba.tzgl.vo.Js_CompensationVO;
import com.bba.tzgl.vo.LedgerVO;
import com.bba.tzgl.vo.Ledger_DetailVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.util.MyUtils;
import com.bba.util.StringUtils;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:26
 */
@Service
@Transactional
public class Js_Analysis_SummaryServiceimpl extends ServiceImpl<Js_Analysis_SummaryDao, Js_Analysis_SummaryVO> implements IJs_Analysis_SummaryService {

    @Resource
    private Js_Analysis_SummaryDao js_Analysis_SummaryDao;

    @Resource
    private Js_Analysis_Summary_DetailDao js_Analysis_Summary_DetailDao;

    @Resource
    private Js_Vin_AmountDao js_vin_amountDao;

    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Js_Analysis_SummaryVO> list = js_Analysis_SummaryDao.findListForGrid(jqGridParamModel);
        int count = js_Analysis_SummaryDao.findListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }

    @Override
    public ResultVO saveDetail(Js_Analysis_SummaryDTO requestJs_Analysis_SummaryDTO, SysUserVO sysUserVO) {
        return null;
    }


    @Override
    public Js_Analysis_SummaryDTO getDetail(Js_Analysis_SummaryVO vo) {
        Js_Analysis_SummaryVO paramJs_Analysis_SummaryVO = new Js_Analysis_SummaryVO();
        paramJs_Analysis_SummaryVO.setSheet_no(vo.getSheet_no());
        Js_Analysis_SummaryVO dataJs_Analysis_SummaryVO = js_Analysis_SummaryDao.selectOne(paramJs_Analysis_SummaryVO);
        if(dataJs_Analysis_SummaryVO == null){
            return null;
        }
        EntityWrapper detailEntityWrapper = new EntityWrapper();
        Js_Analysis_Summary_DetailVO paramJs_Analysis_Summary_DetailVO = new Js_Analysis_Summary_DetailVO();
        paramJs_Analysis_Summary_DetailVO.setSheet_no(vo.getSheet_no());
        detailEntityWrapper.setEntity(paramJs_Analysis_Summary_DetailVO);
        detailEntityWrapper.orderBy("id", true);
        List<Js_Analysis_Summary_DetailVO> dataJs_Analysis_Summary_DetailVOList = js_Analysis_Summary_DetailDao.selectList(detailEntityWrapper);
        Js_Analysis_SummaryDTO returnJs_Analysis_SummaryDTO = new Js_Analysis_SummaryDTO();
        returnJs_Analysis_SummaryDTO.setJs_Analysis_SummaryVO(dataJs_Analysis_SummaryVO);
        returnJs_Analysis_SummaryDTO.setJs_Analysis_Summary_DetailVOList(dataJs_Analysis_Summary_DetailVOList);
        return returnJs_Analysis_SummaryDTO;
    }

    @Override
    public ResultVO create_month_data(String month, String type) {
        /**
         * 结算分析汇总，按月生成数据逻辑
         * type:0新增、1更新数据
         * 1、拿到月份
         * 2、查询统计表，根据统计月份
         * 3、抓取数据，插入到分析汇总数据
         * */
        if (StringUtils.equals(type,"0")) {
            //新增数据
            //1、验证
            Js_Analysis_SummaryVO paramJs_Analysis_SummaryVO = new Js_Analysis_SummaryVO();
            paramJs_Analysis_SummaryVO.setStatistics_month(month);
            Js_Analysis_SummaryVO dataJs_Analysis_SummaryVO =js_Analysis_SummaryDao.selectOne(paramJs_Analysis_SummaryVO);
            if (dataJs_Analysis_SummaryVO!=null) {
                return ResultVO.failResult("当前月份"+month+"的结算分析汇总数据已经存在，请勿重新生成");
            }
            //2、查询统计表数据
            List<VehicleTotalVO> list = getDataByMonth(month);
            //3、循环list，获取总额数据
            for (VehicleTotalVO vo:list) {

            }

        } else {
            //更新已经存在的数据
        }
        return ResultVO.successResult("操作成功");
    }


    /**
     * 根据月份，获取统计表数据
     * @param month
     * @return
     */
    public List<VehicleTotalVO> getDataByMonth(String month) {
        JqGridParamModel jqGridParamModel = new JqGridParamModel();
        jqGridParamModel.put("statistical_month",month.replace("-",""));
        String filters = JqGridSearchParamHandler.processSql("", jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<VehicleTotalVO> list = js_vin_amountDao.getListForGridBaobiao(jqGridParamModel);
        // 1、视图中已匹配合同，
        // 2、如果没有匹配合同，代码再按统计规则计算数据
        if(null!=list && list.size()>0) {
            // 2、如果没有匹配合同，代码再按统计规则计算数据  - 查询数据
            List<Tr_statistical_rulesVO> ruleList =js_vin_amountDao.findTR_STATISTICAL_RULES();
            if(null!=ruleList && ruleList.size()>0) {
                for(VehicleTotalVO item : list) {
                    // 2、如果没有匹配合同，代码再按统计规则计算数据  - 匹配数据
                    for(Tr_statistical_rulesVO rvo:ruleList) {
                        if(MyUtils.matchingRules(rvo,item)) {
                            //对上预算费用
                            if (item.getPrice() == null && item.getNot_tax_freight() == null && item.getNot_tax_premium() == null && item.getNot_tax_other_amount() == null && item.getNot_tax_amount() == null) {
                                //对上单价就是 第一段*1里程+第二段*2里程+第三段*3里程
                                Double price=rvo.getUp_first_mileage() * rvo.getUp_first_price() + rvo.getUp_two_mileage() * rvo.getUp_two_price() + rvo.getUp_three_mileage()*rvo.getUp_three_price();
                                item.setPrice(price);
                                item.setNot_tax_freight(price);
                                item.setNot_tax_premium(rvo.getUp_premium());
                                item.setNot_tax_other_amount(rvo.getUp_cross_sea_amount());
                                item.setNot_tax_amount(item.getNot_tax_freight()+item.getNot_tax_premium()+item.getNot_tax_other_amount());
                            }
                            //对下预算费用
                            if (item.getNot_tax_price() == null && item.getNot_tax_other_amount_down() == null && item.getDown_ntax_premium() == null && item.getDown_ntax_amount() == null) {
                                //对下单价就是 第一段*1里程+第二段*2里程+第三段*3里程
                                Double not_tax_price=rvo.getDown_first_mileage() * rvo.getDown_first_price() + rvo.getDown_two_mileage()*rvo.getDown_two_price() + rvo.getDown_three_mileage() * rvo.getDown_three_price();
                                item.setNot_tax_price(not_tax_price);
                                item.setNot_tax_other_amount_down(rvo.getDown_cross_sea_amount());
                                item.setDown_ntax_premium(rvo.getDown_premium());
                                item.setDown_ntax_amount(not_tax_price+item.getNot_tax_other_amount_down()+item.getDown_ntax_premium());
                            }
                            break;
                        }
                    }
                }
            }
            for(VehicleTotalVO item : list) {
                item.setMil(null==item.getMil()?0:item.getMil());
                item.setPrice(null==item.getPrice()?0d:item.getPrice());
                item.setNot_tax_freight(null==item.getNot_tax_freight()?0d:item.getNot_tax_freight());
                item.setNot_tax_premium(null==item.getNot_tax_premium()?0d:item.getNot_tax_premium());
                item.setNot_tax_other_amount(null==item.getNot_tax_other_amount()?0d:item.getNot_tax_other_amount());
                item.setNot_tax_amount(null==item.getNot_tax_amount()?0d:item.getNot_tax_amount());
                item.setNot_tax_price(null==item.getNot_tax_price()?0d:item.getNot_tax_price());
                item.setNot_tax_other_amount_down(null==item.getNot_tax_other_amount_down()?0d:item.getNot_tax_other_amount_down());
                item.setDown_ntax_premium(null==item.getDown_ntax_premium()?0d:item.getDown_ntax_premium());
                item.setDown_ntax_amount(null==item.getDown_ntax_amount()?0d:item.getDown_ntax_amount());
                item.setReal_tax_amount(null==item.getReal_tax_amount()?0d:item.getReal_tax_amount());
                item.setReal_ntax_amount(null==item.getReal_ntax_amount()?0d:item.getReal_ntax_amount());
                item.setReal_cost(null==item.getReal_cost()?0d:item.getReal_cost());
                item.setTax_real_cost(null==item.getTax_real_cost()?0d:item.getTax_real_cost());
            }
        }
        return list;
    }
}
