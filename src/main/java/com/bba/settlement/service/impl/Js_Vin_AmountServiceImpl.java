package com.bba.settlement.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.common.Sys_sheetidUtil;
import com.bba.common.constant.*;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.dz.dao.Js_Dz_SheetDao;
import com.bba.dz.dao.Js_Dz_Sheet_DetailDao;
import com.bba.dz.vo.Js_Dz_SheetVO;
import com.bba.dz.vo.Js_Dz_Sheet_DetailVO;
import com.bba.ht.service.api.IHt_CusService;
import com.bba.ht.service.api.IHt_Cus_FreightService;
import com.bba.ht.vo.Ht_CusVO;
import com.bba.jcda.vo.Tr_statistical_rulesVO;
import com.bba.settlement.dao.Js_Vin_AmountDao;
import com.bba.settlement.dao.Js_Vin_LogDao;
import com.bba.settlement.dao.Js_Vin_Temp_AmountDao;
import com.bba.settlement.service.api.IJs_Vin_AmountService;
import com.bba.settlement.vo.Js_Vin_AmountVO;
import com.bba.settlement.vo.Js_Vin_Temp_AmountVO;
import com.bba.settlement.vo.VehicleTotalVO;
import com.bba.tzgl.dao.Js_CompensationDao;
import com.bba.tzgl.vo.Js_CompensationVO;
import com.bba.util.*;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
@Service
public class Js_Vin_AmountServiceImpl extends ServiceImpl<Js_Vin_AmountDao, Js_Vin_AmountVO>  implements IJs_Vin_AmountService {

    @Autowired
    private IHt_Cus_FreightService ht_cus_freightService;
    @Autowired
    private Js_Vin_AmountDao js_vin_amountDao;
    @Autowired
    private Js_Vin_Temp_AmountDao js_vin_temp_amountDao;
    @Autowired
    private Js_Vin_LogDao js_vin_logDao;
    @Autowired
    private IHt_CusService ht_cusService;
    @Autowired
    private Js_Dz_Sheet_DetailDao js_dz_sheet_detailDao;
    @Autowired
    private Js_Dz_SheetDao js_dz_sheetDao;
    @Autowired
    private Js_CompensationDao js_CompensationDao;



    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Js_Vin_AmountVO> list = js_vin_amountDao.getListForGrid(jqGridParamModel);
        int records = js_vin_amountDao.getListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
    }

    @Override
    public PageVO getListForGridBaobiao(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<VehicleTotalVO> list = js_vin_amountDao.getListForGridBaobiao(jqGridParamModel);
        //CREATE OR REPLACE VIEW   VIEW_VEHICLETOTAL     AS
        // 1、视图中已匹配合同，
        // 2、如果没有匹配合同，代码再按统计规则计算数据
        if(null!=list && list.size()>0)
        {
           /*  for(VehicleTotalVO item : list)
            {
              // 2、如果没有匹配合同，代码再按统计规则计算数据  - 组织查询条件
                if((item.getPrice()==null && item.getNot_tax_freight()==null && item.getNot_tax_premium()==null && item.getNot_tax_other_amount()==null && item.getNot_tax_amount()==null) ||
                        (item.getNot_tax_price()==null && item.getNot_tax_other_amount_down()==null && item.getDown_ntax_premium()==null && item.getDown_ntax_amount()==null)
                ){
                    Tr_statistical_rulesVO info=new Tr_statistical_rulesVO();
                    info.setTrans_mode(item.getTrans_mode());
                    info.setBegin_city(item.getBegin_city());
                    info.setEnd_city(item.getEnd_city());
                    info.setState("0");
                    ruleList.add(info);
                }
            }*/

            // 2、如果没有匹配合同，代码再按统计规则计算数据  - 查询数据
            List<Tr_statistical_rulesVO> ruleList =js_vin_amountDao.findTR_STATISTICAL_RULES();
            if(null!=ruleList && ruleList.size()>0)
            {
                for(VehicleTotalVO item : list) {
                    // 2、如果没有匹配合同，代码再按统计规则计算数据  - 匹配数据
                    for(Tr_statistical_rulesVO rvo:ruleList) {
                        if(MyUtils.matchingRules(rvo,item)) {
                            //对上预算费用
                            if (item.getPrice() == null && item.getNot_tax_freight() == null && item.getNot_tax_premium() == null && item.getNot_tax_other_amount() == null && item.getNot_tax_amount() == null) {
                                //对上单价就是 第一段*1里程+第二段*2里程+第三段*3里程
                                Double price=rvo.getUp_first_mileage() * rvo.getUp_first_price() + rvo.getUp_two_mileage() * rvo.getUp_two_price() + rvo.getUp_three_mileage()*rvo.getUp_three_price();
                                if (item.getTrans_mode().indexOf("公路")!=-1) {
                                    item.setPrice(rvo.getUp_first_price() + rvo.getUp_two_price() + rvo.getUp_three_price());
                                    item.setMil(rvo.getUp_first_mileage());
                                } else {
                                    item.setPrice(price);
                                }
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
                    //循环数据后，重损的只保留保费
                    if (StringUtils.isNotBlank(item.getZs_remark()) && item.getZs_remark().indexOf("重损")!=-1) {
                        item.setPrice(null);
                        item.setNot_tax_freight(null);
                        item.setNot_tax_other_amount(null);
                        item.setNot_tax_amount(null);
                        item.setNot_tax_price(null);
                        item.setNot_tax_other_amount_down(null);
                        item.setDown_ntax_amount(null);
                        item.setReal_ntax_amount(null);
                        item.setReal_cost(null);
                        item.setReal_tax_amount(null);
                        item.setTax_real_cost(null);
                    }
                }
            }

            for(VehicleTotalVO item : list)
            {
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
        int records = js_vin_amountDao.getListForGridBaobiaoCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
    }

    @Override
    @Transactional
    public ResultVO two_settlementDetail(List<String> list, SysUserVO sysUserVO) {
        if(list.size() == 0){
            return ResultVO.failResult("请至少选择一行数据进行操作");
        }
        List<Js_Vin_AmountVO> js_vin_amountVOList = js_vin_amountDao.selectBatchIds(list);
        List<String> msg_list = new ArrayList<String>();
        for(int i = 0, line = 1; i < js_vin_amountVOList.size(); i++, line ++){
            Js_Vin_AmountVO vo = js_vin_amountVOList.get(i);
            if(!StringUtils.equals(vo.getJs_state(), SETTLEMENT_StateEnum.INVOICED.getCode())){
                msg_list.add("第"+line+"行,不处于已开票状态,不能进行补差结算");
                continue;
            }
            //用constract_type判断
        /*    EntityWrapper ht_cusEntityWrapper = new EntityWrapper();
            Ht_CusVO paramHt_CusVO = new Ht_CusVO();
            paramHt_CusVO.setContract_no(vo.getContract_no());
            ht_cusEntityWrapper.setEntity(paramHt_CusVO);
            Ht_CusVO dataHt_CusVO = ht_cusService.selectOne(ht_cusEntityWrapper);*/
            if(!Contract_TypeEnum.TEMP.getCode().equals(vo.getContract_type())){
                msg_list.add("第"+line+"行,合同非暂定合同,不能进行补差结算");
                continue;
            }
        }
        if(msg_list.size() > 0){
            return ResultVO.failResult("补差结算失败", ArrayUtils.join(msg_list, "</br>"));
        }

        //先将历史数据存在历史结算list
        List<Js_Vin_Temp_AmountVO> insertTempList = new ArrayList<Js_Vin_Temp_AmountVO>();
        for(Js_Vin_AmountVO vo: js_vin_amountVOList) {
            Js_Vin_Temp_AmountVO tempVO = new Js_Vin_Temp_AmountVO();
            BeanUtils.copyProperties(vo, tempVO);
            tempVO.setVin_id(vo.getId());
            tempVO.setCreate_date(new Date());
            insertTempList.add(tempVO);
        }
        //计算正式费用
        msg_list = ht_cus_freightService.processFreightByBusiness(js_vin_amountVOList);
        if(msg_list.size() > 0){
            return ResultVO.failResult("补差结算失败", ArrayUtils.join(msg_list, "</br>"));
        }
        /**开始正式补差操作*/
        try {
            //1.更新正式数据
            for(Js_Vin_AmountVO vo: js_vin_amountVOList){
                vo.setJs_state(SETTLEMENT_StateEnum.SETTLEMENT.getCode()); //SETTLEMENT("1", "结算"),
                vo.setHis_flag("Y");
                js_vin_amountDao.updateById(vo);
            }
            //2.插入到历史
            for (Js_Vin_Temp_AmountVO tempInsertVO:insertTempList) {
                js_vin_temp_amountDao.insert(tempInsertVO);
            }
            //插入到补差表
            List<Js_CompensationVO> insertJs_CompensationVOList = new ArrayList<>();
            for (Js_Vin_Temp_AmountVO dataTempAmountVO:insertTempList) {
                for (Js_Vin_AmountVO vin_amountVO:js_vin_amountVOList) {
                    if (vin_amountVO.getId().equals(dataTempAmountVO.getVin_id())) {
                        //算出差异
                        if (vin_amountVO.getTax_amount()!=dataTempAmountVO.getTax_amount()) {
                            Js_CompensationVO requestJs_CompensationVO = new Js_CompensationVO();
                            BeanUtils.copyProperties(vin_amountVO, requestJs_CompensationVO);
                            requestJs_CompensationVO.setVin_id(vin_amountVO.getId());
                            requestJs_CompensationVO.setType("1");//对上补差
                            requestJs_CompensationVO.setCreate_by(sysUserVO.getRealName());
                            requestJs_CompensationVO.setCreate_date(new Date());
                            requestJs_CompensationVO.setOld_ntax_amount(dataTempAmountVO.getNot_tax_amount());
                            requestJs_CompensationVO.setOld_tax_amount(dataTempAmountVO.getTax_amount());
                            requestJs_CompensationVO.setNow_ntax_amount(vin_amountVO.getNot_tax_amount());
                            requestJs_CompensationVO.setNow_tax_amount(vin_amountVO.getTax_amount());
                            requestJs_CompensationVO.setAfter_contract_sheet_no(vin_amountVO.getContract_sheet_no());//现合同单号
                            requestJs_CompensationVO.setBefor_contract_no(dataTempAmountVO.getContract_no());//前合同
                            requestJs_CompensationVO.setBefor_contract_type(dataTempAmountVO.getContract_type());//前合同类型
                            requestJs_CompensationVO.setAfter_contract_no(vin_amountVO.getContract_no());//后合同
                            requestJs_CompensationVO.setAfter_contract_type(vin_amountVO.getContract_type());//后合同类型
                            requestJs_CompensationVO.setNot_tax_premium(vin_amountVO.getNot_tax_premium().subtract(dataTempAmountVO.getNot_tax_premium()));//对上保费补差
                            requestJs_CompensationVO.setNot_tax_other_amount(vin_amountVO.getNot_tax_other_amount().subtract(dataTempAmountVO.getNot_tax_other_amount()));//其他保费补差
                            requestJs_CompensationVO.setNot_tax_freight(vin_amountVO.getNot_tax_freight().subtract(dataTempAmountVO.getNot_tax_freight()));//费用保费补差
                            requestJs_CompensationVO.setTax_up_total(vin_amountVO.getTax_amount().subtract(dataTempAmountVO.getTax_amount()));//应收运费含税补差
                            requestJs_CompensationVO.setNtax_up_total(vin_amountVO.getNot_tax_amount().subtract(dataTempAmountVO.getNot_tax_amount()));//应收运费不含税补差
                            requestJs_CompensationVO.setTax_amount(requestJs_CompensationVO.getTax_up_total().subtract(requestJs_CompensationVO.getNtax_up_total()));//税额
                            insertJs_CompensationVOList.add(requestJs_CompensationVO);
                            break;
                        }
                    }
                }
            }
            //批量插入差异表
            List<Js_CompensationVO> newList = new ArrayList<Js_CompensationVO>();
            for(int i = 0; i < insertJs_CompensationVOList.size(); i++){
                newList.add(insertJs_CompensationVOList.get(i));
                if((i+1) % 50 == 0 || i + 1 == insertJs_CompensationVOList.size()){
                    //插入主表
                    js_CompensationDao.batchInsert(newList);
                    newList.clear();
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return ResultVO.successResult("补差结算成功,请在补差管理查看补差数据");
    }

    @Override
    public ResultVO un_settlement(List<String> list, SysUserVO sysUserVO) {
        if(list.size() == 0){
            return ResultVO.failResult("请至少选择一行数据进行操作");
        }
        List<Js_Vin_AmountVO> js_vin_amountVOList = js_vin_amountDao.selectBatchIds(list);

        List<String> msg_list = new ArrayList<String>();
        for(int i = 0; i < js_vin_amountVOList.size(); i++){
            Js_Vin_AmountVO vo = js_vin_amountVOList.get(i);
            if(!SETTLEMENT_StateEnum.SETTLEMENT.getCode().equals(vo.getJs_state())){
                msg_list.add("该数据非结算状态不能进行撤回"+vo.getVdr_no());
                continue;
            }
        }

        if(msg_list.size() > 0){
            return ResultVO.failResult(ArrayUtils.join(msg_list, "</br>"));
        }
        for(int i = 0; i < js_vin_amountVOList.size(); i++) {
            Js_Vin_AmountVO vo = js_vin_amountVOList.get(i);
            vo.setJs_state(SETTLEMENT_StateEnum.NORMAL.getCode());
            //vo.setJs_batch("");
            vo.setNot_tax_freight(new BigDecimal(0));
            vo.setNot_tax_other_amount(new BigDecimal(0));
            vo.setNot_tax_premium(new BigDecimal(0));
            vo.setNot_tax_amount(new BigDecimal(0));
            vo.setTax_rate(new BigDecimal(0));
            vo.setTax_amount(new BigDecimal(0));
            vo.setContract_no("");
            vo.setContract_type("");
            vo.setContract_sheet_no("");
            vo.setMil("");
            js_vin_amountDao.updateById(vo);
        }
        return ResultVO.successResult("撤回成功");
    }

    @Override
    public ResultVO create_bill(List<String> list, SysUserVO sysUserVO,String type) {
        //type=0 正常，=1补差
        if(list.size() == 0){
            return ResultVO.failResult("请至少选择一行数据进行操作");
        }
        /***
         * 判断
         */
        List<Js_Vin_AmountVO> js_vin_amountVOList = new ArrayList<Js_Vin_AmountVO>();
        if (StringUtils.equals("0",type)) {
            js_vin_amountVOList = js_vin_amountDao.selectBatchIds(list);
        } else {
            //补差
            js_vin_amountVOList = js_vin_amountDao.selectCompensationList(list);
            if (js_vin_amountVOList.size()==0) {
                return ResultVO.failResult("没有找到相应的补差数据，请联系管理员");
            }
        }

        List<String> msg_list = new ArrayList<String>();
        Set cus_set = new HashSet();
        for(int i = 0; i < js_vin_amountVOList.size(); i++) {
            Js_Vin_AmountVO vo = js_vin_amountVOList.get(i);
            if (!SETTLEMENT_StateEnum.SETTLEMENT.getCode().equals(vo.getJs_state())) {
                msg_list.add("该数据非结算状态不能生成对账单" + vo.getVin());
                continue;
            }
            //判断是否是同一客户
            cus_set.add(vo.getCus_no());
        }
        if(msg_list.size() > 0){
            return ResultVO.failResult("生成对账单失败", ArrayUtils.join(msg_list, "</br>"));
        }
        if(cus_set.size() > 1){
            return ResultVO.failResult("生成对账单失败","不同的客户无法生成对账单");
        }
        String js_dz_sheet = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.JS_DZ_SHEET);
        Js_Dz_SheetVO js_dz_sheetVO = new Js_Dz_SheetVO();
        js_dz_sheetVO.setDz_sheet(js_dz_sheet);
        js_dz_sheetVO.setDz_op_by(sysUserVO.getRealName());
        js_dz_sheetVO.setDz_op_datetime(new Date());
        js_dz_sheetVO.setState("0");
        js_dz_sheetVO.setCus_no(js_vin_amountVOList.get(0).getCus_no());
        js_dz_sheetVO.setCus_name(js_vin_amountVOList.get(0).getCus_name());

        BigDecimal not_tax_amount = new BigDecimal(0);
        BigDecimal tax_amount = new BigDecimal(0);
        for (Js_Vin_AmountVO vo: js_vin_amountVOList){
            Js_Dz_Sheet_DetailVO detailVO = new Js_Dz_Sheet_DetailVO();
            BeanUtils.copyProperties(vo, detailVO);
            detailVO.setDz_sheet(js_dz_sheet);
            detailVO.setTax_rate(vo.getTax_rate());
            detailVO.setVin_id(vo.getId());
            detailVO.setType(type);
            js_dz_sheet_detailDao.insert(detailVO);
            not_tax_amount = not_tax_amount.add(vo.getNot_tax_amount());
            tax_amount = tax_amount.add(vo.getTax_amount());
            Js_Vin_AmountVO updateVO = new Js_Vin_AmountVO();
            updateVO.setId(vo.getId());
            updateVO.setJs_state(SETTLEMENT_StateEnum.GENERATE_BILLS.getCode());
            js_vin_amountDao.updateById(updateVO);
        }
        js_dz_sheetVO.setType(type);
        js_dz_sheetVO.setNot_tax_amount(not_tax_amount);
        js_dz_sheetVO.setTax_amount(tax_amount);
        js_dz_sheetDao.insert(js_dz_sheetVO);

        return ResultVO.successResult("生成对账单成功");
    }

    @Override
    @Transactional
    public ResultVO settlementDetail(List<String> list, SysUserVO sysUserVO) {
        if(list.size() == 0){
            return ResultVO.failResult("请至少选择一行数据进行操作");
        }
        List<Js_Vin_AmountVO> js_vin_amountVOList = js_vin_amountDao.selectBatchIds(list);
        List<String> msg_list = new ArrayList<String>();
        for(int i = 0, line = 1; i < js_vin_amountVOList.size(); i++, line ++){
            Js_Vin_AmountVO vo = js_vin_amountVOList.get(i);
            if(!StringUtils.equals(vo.getJs_state(), SETTLEMENT_StateEnum.NORMAL.getCode())){
                msg_list.add("第"+line+"行,已结算请勿重复结算");
            }
          /*  if(StringUtils.equals(vo.getHis_flag(), "Y")){
                msg_list.add("第"+line+"行,已经结算过的数据请使用‘二次结算’进行操作");
            }*/
        }
        if(msg_list.size() > 0){
            return ResultVO.failResult("结算失败", ArrayUtils.join(msg_list, "</br>"));
        }
        //计算费用
        msg_list = ht_cus_freightService.processFreightByTempBusiness(js_vin_amountVOList);
        if(msg_list.size() > 0){
            return ResultVO.failResult("结算失败", ArrayUtils.join(msg_list, "</br>"));
        }
        for(Js_Vin_AmountVO vo: js_vin_amountVOList){
            vo.setJs_state(SETTLEMENT_StateEnum.SETTLEMENT.getCode());
            js_vin_amountDao.updateById(vo);
        }

        return ResultVO.successResult("结算成功");
    }


    @Override
    public ResultVO importData(MultipartFile multipartFile)
    {
        List<Js_Vin_AmountVO> list = null;
        try {
            list = ExcelImportUtil.importExcel(multipartFile.getInputStream(), Js_Vin_AmountVO.class, new ImportParams());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null==list || list.size()<=0){
            return ResultVO.failResult("导入失败，上传Excel文件中没有数据");
        }
        /**非‘结算’状态无法导入修改*/
        List<String> msg_list = new ArrayList<String>();
        for (int i = 0, line = 1; i < list.size(); i++, line ++) {
            Js_Vin_AmountVO vo = list.get(i);
            if (StringUtils.notEquals(vo.getJs_state(),"结算")) {
                msg_list.add("第"+line+"行,只允许“结算”状态进行此操作");
            }
        }
        if(msg_list.size() > 0){
            return ResultVO.failResult("导入失败", ArrayUtils.join(msg_list, "</br>"));
        }
        js_vin_amountDao.updateBill_Number(list);
        return ResultVO.successResult("导入成功！");
    }

    @Override
    public List<Ht_CusVO> findHt_cusVO(Ht_CusVO vo)
    {
        return js_vin_amountDao.findHt_cusVO(vo);
    }

}
