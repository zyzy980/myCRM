package com.bba.tzgl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.common.Sys_sheetidUtil;
import com.bba.common.constant.*;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.dz.dao.Js_Dz_Sheet_DetailDao;
import com.bba.dz.vo.Js_Dz_Sheet_DetailVO;
import com.bba.fpgl.dao.IReceivable_invoiceDao;
import com.bba.fpgl.vo.Receivable_invoiceVO;
import com.bba.jcda.vo.Zd_CarrierVO;
import com.bba.nosettlement.vo.Js_Non_VehicleVO;
import com.bba.settlement.dao.IJs_Down_PremiumDao;
import com.bba.settlement.dao.Js_Vin_AmountDao;
import com.bba.settlement.vo.Js_Vin_AmountVO;
import com.bba.settlement.vo.Js_Vin_Down_AmountVO;
import com.bba.settlement.vo.Js_Vin_Down_PremiumVO;
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
public class LedgerServiceimpl extends ServiceImpl<LedgerDao, LedgerVO> implements ILedgerService {

    @Resource
    private LedgerDao ledgerDao;

    @Resource
    private Ledger_DetailDao ledger_DetailDao;

    @Resource
    private IJs_Down_PremiumDao js_down_premiumDao;

    @Resource
    private Js_Dz_Sheet_DetailDao js_dz_sheet_detailDao;

    @Resource
    private Js_Vin_AmountDao js_Vin_AmountDao;

    @Resource
    private AccountsToLedgerDao accountsToLedgerDao;

    @Resource
    private IReceivable_invoiceDao receivable_invoiceDao;

    @Resource
    private Js_CompensationDao js_CompensationDao;

    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<LedgerVO> list = ledgerDao.findListForGrid(jqGridParamModel);
        int count = ledgerDao.findListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }

    @Override
    public ResultVO saveDetail(LedgerDTO requestLedgerDTO, SysUserVO sysUserVO) {
        /**
         * 》》》台账目前只有保存功能，手动添加看后期需求
         * */
        LedgerVO requestLedgerVO = requestLedgerDTO.getLedgerVO();
        List<Ledger_DetailVO> requestLedger_DetailVOList = requestLedgerDTO.getLedger_DetailVOList();

        if(StringUtils.isBlank(requestLedgerVO.getCus_no())){
            return ResultVO.failResult("客户不能为空");
        }
        requestLedgerVO.setState(LedgerEnum.NORMAL.getCode());
        if(StringUtils.isBlank(requestLedgerVO.getSheet_no())){
            /***新增台账，目前没有这个需求*/
            LedgerVO paramLedgerVO = new LedgerVO();
            paramLedgerVO.setLedger_no(requestLedgerVO.getLedger_no());
            LedgerVO dataLedgerVO = ledgerDao.selectOne(paramLedgerVO);
            if(dataLedgerVO != null){
                return ResultVO.failResult("台账编号已存在,请勿重复录入"+requestLedgerVO.getLedger_no());
            }
            String sheet_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.JS_LEDGER);
            requestLedgerVO.setSheet_no(sheet_no);
            requestLedgerVO.setCreate_by(sysUserVO.getRealName());
            ledgerDao.insert(requestLedgerVO);
            UpdateJsDataByBillNumber(requestLedgerVO,"1");
        }else{
            /***台账保存
             *1、保存时，结算号可以重复，但是结算批次不能
             */
            boolean flag = true;
            LedgerVO paramLedgerVO = new LedgerVO();
            paramLedgerVO.setLedger_no(requestLedgerVO.getLedger_no());
            LedgerVO dataLedgerVO = ledgerDao.selectOne(paramLedgerVO);
            if(dataLedgerVO == null || !dataLedgerVO.getSheet_no().equals(requestLedgerVO.getSheet_no())){
                return ResultVO.failResult("该台账编号数据异常,请联系管理员"+requestLedgerVO.getLedger_no());
            }
            if(!LedgerEnum.NORMAL.getCode().equals(dataLedgerVO.getState())){
                return ResultVO.failResult("只允许正常状态下的台账进行修改操作");
            }
            //如果结算号或者结算批次发生改变，需要重新修改,并且是正常台账，补差台账不允许修改
            if (StringUtils.notEquals(requestLedgerVO.getJs_no(),dataLedgerVO.getJs_no()) || StringUtils.notEquals(requestLedgerVO.getJs_project(),dataLedgerVO.getJs_project())) {
                if (StringUtils.notEquals(dataLedgerVO.getLedger_type(),"0")) {
                    throw new RuntimeException("补差台账不能修改结算号或结算批次");
                } else {
                    //正常的台账
                    //1、首先判断修改的结算批次和结算号在系统中是否存在，不存在才可以修改，否则会修改其他数据,注销的条件
                    LedgerVO newLedgerVO = new LedgerVO();
                    newLedgerVO.setJs_no(requestLedgerVO.getJs_no());
                    newLedgerVO.setJs_project(requestLedgerVO.getJs_project());
                    LedgerVO returnLedgerVO = ledgerDao.selectOneByConditions(newLedgerVO);
                    if(returnLedgerVO != null){
                        throw new RuntimeException("需要修改的结算号和结算批次在系统中不允许同时存在，请核实数据");
                    }
                    flag = true;
                }
            }
            requestLedgerVO.setId(dataLedgerVO.getId());
            requestLedgerVO.setCreate_by(null);
            requestLedgerVO.setCheck_by(null);
            requestLedgerVO.setCheck_date(null);
            requestLedgerVO.setLedger_type(null);
            requestLedgerVO.setProject(null);
            ledgerDao.updateById(requestLedgerVO);
            EntityWrapper ledger_detailEntityWrapper = new EntityWrapper();
            Ledger_DetailVO paramLedger_DetailVO = new Ledger_DetailVO();
            paramLedger_DetailVO.setSheet_no(requestLedgerVO.getSheet_no());
            ledger_detailEntityWrapper.setEntity(paramLedger_DetailVO);
            List<Ledger_DetailVO> dataLedger_DetailVOList = ledger_DetailDao.selectList(ledger_detailEntityWrapper);
            //如果老数据里不存在新的数据List，就删除，存在就新增
            List<Ledger_DetailVO> deleteLedger_DetailVOList = new ArrayList<Ledger_DetailVO>();
            List<Ledger_DetailVO> updateLedger_DetailVOList = new ArrayList<Ledger_DetailVO>();
            for(Ledger_DetailVO dataLedger_DetailVO: dataLedger_DetailVOList){
                boolean bool = false;
                for(Ledger_DetailVO requestLedger_DetailVO: requestLedger_DetailVOList){
                    if(dataLedger_DetailVO.getId().equals(requestLedger_DetailVO.getId())){
                        updateLedger_DetailVOList.add(requestLedger_DetailVO);
                        bool = true;
                        break;
                    }
                }
                if(!bool){
                    deleteLedger_DetailVOList.add(dataLedger_DetailVO);
                }
            }
            for(Ledger_DetailVO deleteVO: deleteLedger_DetailVOList){
                ledger_DetailDao.deleteById(deleteVO.getId());
            }
            for(Ledger_DetailVO updateVO: updateLedger_DetailVOList){
                ledger_DetailDao.updateById(updateVO);
            }
            if (flag) {
                UpdateJsDataByBillNumber(requestLedgerVO,"1");
            }

        }
        for(Ledger_DetailVO vo: requestLedger_DetailVOList){
            if(vo.getId() == null || vo.getId() == 0){
                //初始化规则明细表一些外键关系
                vo.setSheet_no(requestLedgerVO.getSheet_no());
                ledger_DetailDao.insert(vo);
            }
        }
        ResultVO resultVO = ResultVO.successResult();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("sheet_no", requestLedgerVO.getSheet_no());
        returnMap.put("msg", "保存成功");
        resultVO.setResultDataFull(returnMap);
        return resultVO;
    }

    @Override
    public ResultVO check(LedgerVO ledgerVO, SysUserVO sysUserVO) {
        if(StringUtils.isBlank(ledgerVO.getSheet_no())){
            return ResultVO.failResult("台账单号不能为空");
        }
        LedgerVO paramLedgerVO = new LedgerVO();
        paramLedgerVO.setSheet_no(ledgerVO.getSheet_no());
        LedgerVO dataLedgerVO = ledgerDao.selectOne(paramLedgerVO);
        if(dataLedgerVO == null){
            return ResultVO.failResult("台账不存在");
        }else if(!LedgerEnum.NORMAL.getCode().equals(dataLedgerVO.getState())){
            return ResultVO.failResult("只允许正常状态下的台账进行审核操作");
        }
        dataLedgerVO.setState(Contract_StateEnum.CHECK.getCode());
        dataLedgerVO.setCheck_by(sysUserVO.getRealName());
        dataLedgerVO.setCheck_date(new Date());
        ledgerDao.updateById(dataLedgerVO);
        return ResultVO.successResult("审核成功");
    }

    @Override
    public ResultVO uncheck(LedgerVO ledgerVO, SysUserVO sysUserVO) {
        if(StringUtils.isBlank(ledgerVO.getSheet_no())){
            return ResultVO.failResult("台账单号不能为空");
        }
        LedgerVO paramLedgerVO = new LedgerVO();
        paramLedgerVO.setSheet_no(ledgerVO.getSheet_no());
        LedgerVO dataLedgerVO = ledgerDao.selectOne(paramLedgerVO);
        if(dataLedgerVO == null){
            return ResultVO.failResult("台账不存在");
        }else if(!LedgerEnum.CHECK.getCode().equals(dataLedgerVO.getState())){
            return ResultVO.failResult("只允许审核状态下的台账进行反审核操作");
        }else if(StringUtils.isNotBlank(dataLedgerVO.getInvoice_no())) {
            return ResultVO.failResult("该台账已开具发票，无法反审核");
        }
        dataLedgerVO.setState(LedgerEnum.NORMAL.getCode());
        dataLedgerVO.setCheck_by(null);
        dataLedgerVO.setCheck_date(null);
        ledgerDao.updateById(dataLedgerVO);
        return ResultVO.successResult("反审核成功");
    }

    @Override
    public ResultVO cancel(LedgerVO ledgerVO, SysUserVO sysUserVO) {
        if(StringUtils.isBlank(ledgerVO.getSheet_no())){
            return ResultVO.failResult("台账单号不能为空");
        }
        LedgerVO paramLedgerVO = new LedgerVO();
        paramLedgerVO.setSheet_no(ledgerVO.getSheet_no());
        LedgerVO dataLedgerVO = ledgerDao.selectOne(paramLedgerVO);
        if(dataLedgerVO == null){
            return ResultVO.failResult("台账不存在");
        }else if(!LedgerEnum.NORMAL.getCode().equals(dataLedgerVO.getState())){
            return ResultVO.failResult("只允许正常状态下的台账进行注销操作");
        } else if(StringUtils.isNotBlank(dataLedgerVO.getInvoice_no())) {
            return ResultVO.failResult("该台账已开具发票，无法注销");
        }
        //如果存在保费，需要修改对下保费状态
        List<Ledger_DetailVO> premiumLedger_DetailVOList = new ArrayList<Ledger_DetailVO>();
        EntityWrapper ledger_detailtEntityWrapper = new EntityWrapper();
        Ledger_DetailVO paramLedger_DetailVO = new Ledger_DetailVO();
        paramLedger_DetailVO.setSheet_no(ledgerVO.getSheet_no());
        ledger_detailtEntityWrapper.setEntity(paramLedger_DetailVO);
        ledger_detailtEntityWrapper.orderBy("id", true);
        List<Ledger_DetailVO> dataLedger_DetailVOList = ledger_DetailDao.selectList(ledger_detailtEntityWrapper);
        for (Ledger_DetailVO dataLedger_DetailVO:dataLedger_DetailVOList) {
            //台账与对下开票无关
         /*   if(StringUtils.isNotBlank(dataLedger_DetailVO.getInvoice_no())) {
                return ResultVO.failResult("该台账存在对下已开发票的承运商，无法注销");
            }*/
            if(StringUtils.equals(dataLedger_DetailVO.getPremium_flag(),"Y")) {
                premiumLedger_DetailVOList.add(dataLedger_DetailVO);
            }
        }
        //修改保费状态
        for (Ledger_DetailVO updateVO: premiumLedger_DetailVOList) {
            Js_Vin_Down_PremiumVO updateVin_Down_PremiumVO = new Js_Vin_Down_PremiumVO();
            updateVin_Down_PremiumVO.setData_state(LedgerEnum.PREMIUM_NORMAL.getCode());//已生成台账
            updateVin_Down_PremiumVO.setBegin_date_month(updateVO.getPremium_month());
            js_down_premiumDao.updateDataState(updateVin_Down_PremiumVO);
        }
        //注销，注意，注销是否需要修改结算表对上对下的值
        LedgerVO updateLedgerVO = new LedgerVO();
        updateLedgerVO.setState(LedgerEnum.CANCEL.getCode());
        updateLedgerVO.setBill_number("");
        EntityWrapper ledgerEntityWrapper = new EntityWrapper();
        ledgerEntityWrapper.setEntity(paramLedgerVO);
        ledgerDao.update(updateLedgerVO,ledgerEntityWrapper);
        //注销后，需要修改该台账下结算数据的状态，对上返回到客户确认，对下如果是预估返回到财务审核，否则返回到承运商审核，另外补差的只改变补差表状态
        UpdateJsDataByBillNumber(dataLedgerVO,"2");
        return ResultVO.successResult("注销成功");
    }

    @Override
    public LedgerDTO getDetail(LedgerVO ledgerVO) {
        LedgerVO paramLedgerVO = new LedgerVO();
        paramLedgerVO.setSheet_no(ledgerVO.getSheet_no());
        LedgerVO dataLedgerVO = ledgerDao.selectOne(paramLedgerVO);
        if(dataLedgerVO == null){
            return null;
        }
        EntityWrapper ledger_detailEntityWrapper = new EntityWrapper();
        Ledger_DetailVO paramLedger_DetailVO = new Ledger_DetailVO();
        paramLedger_DetailVO.setSheet_no(ledgerVO.getSheet_no());
        ledger_detailEntityWrapper.setEntity(paramLedger_DetailVO);
        ledger_detailEntityWrapper.orderBy("id", true);
        List<Ledger_DetailVO> dataLedger_DetailVOList = ledger_DetailDao.selectList(ledger_detailEntityWrapper);
        LedgerDTO returnLedgerDTO = new LedgerDTO();
        returnLedgerDTO.setLedgerVO(dataLedgerVO);
        returnLedgerDTO.setLedger_DetailVOList(dataLedger_DetailVOList);
        return returnLedgerDTO;
    }

    @Override
    public List<LedgerVO> findCusJsProject(LedgerVO vo) {
        vo.setCus_invoice(LedgerEnum.INVOICE_FLAG_N.getCode());//做通用，在前台控制
        return ledgerDao.findCusJsProject(vo);
    }

    @Override
    public ResultVO addPremium(List<Js_Vin_Down_PremiumVO> vos) {
        /**台账添加月保费逻辑
         * 1、判断该月份数据是否已经插入到台账，判断补差台账是否可以添加保费
         * 2、insert 数据到台账明细，根据月份
         * 3、修改对下保费表数据状态
         * 4、修改主表数据
         * */
        String sheet_no = vos.get(0).getSheet_no();
        //查询保险公司，获取税率
        Zd_CarrierVO requestZd_CarrierVO = new Zd_CarrierVO();
        requestZd_CarrierVO.setType(BaseDataKeyEnum.ZD_CARRIER_PREMIUM.getCode());
        List<Zd_CarrierVO> zd_carrierVOList = js_down_premiumDao.findzd_carrierVOListByProperty(requestZd_CarrierVO);
        if (zd_carrierVOList.size()==0) {
            return ResultVO.failResult("系统没有保险公司档案设置，请到承运商档案设置");
        }
        Zd_CarrierVO dataZd_CarrierVO = zd_carrierVOList.get(0);
        //根据单号查询台账
        LedgerVO paramLedgerVO = new LedgerVO();
        paramLedgerVO.setSheet_no(sheet_no);
        LedgerVO dataLedgerVO = ledgerDao.selectOne(paramLedgerVO);
        if (dataLedgerVO==null) {
            return ResultVO.failResult("该台账存在异常，请联系管理员");
        } else if (!dataLedgerVO.getState().equals(LedgerEnum.NORMAL.getCode())) {
            return ResultVO.failResult("只有正常状态的台账才可以进行此操作");
        }

        try {
            for (Js_Vin_Down_PremiumVO requestJs_Vin_Down_PremiumVO:vos) {
                //1、查询月份保费数据
                JqGridParamModel jqGridParamModel = new JqGridParamModel();
                jqGridParamModel.put("begin_date_month","eq",requestJs_Vin_Down_PremiumVO.getBegin_date_month());
                jqGridParamModel.put("data_state","eq","0");
                jqGridParamModel.put("js_state","eq","1");
                String filters = JqGridSearchParamHandler.processSql("", jqGridParamModel);
                jqGridParamModel.setFilters(filters);
                List<Js_Vin_Down_PremiumVO> dataJs_Vin_Down_PremiumVOList = js_down_premiumDao.selectPremiumGroupByMonth(jqGridParamModel);

                if (dataJs_Vin_Down_PremiumVOList.size()==0) {
                    throw new RuntimeException("该月份保费数据已添加至台账，请核实数据或联系管理员");
                }
                //集合里面只有一条数据
                Ledger_DetailVO paramLedger_DetailVO = new Ledger_DetailVO();
                paramLedger_DetailVO.setSheet_no(requestJs_Vin_Down_PremiumVO.getSheet_no());
                paramLedger_DetailVO.setJs_no(dataLedgerVO.getJs_no());
                paramLedger_DetailVO.setJs_project(dataLedgerVO.getJs_project());
                paramLedger_DetailVO.setTax_down_total(dataJs_Vin_Down_PremiumVOList.get(0).getTax_amount());
                paramLedger_DetailVO.setNtax_down_total(dataJs_Vin_Down_PremiumVOList.get(0).getNot_tax_amount());
                paramLedger_DetailVO.setTax_rate(dataJs_Vin_Down_PremiumVOList.get(0).getTax_rate());
                paramLedger_DetailVO.setPremium_flag("Y");
                paramLedger_DetailVO.setCarrier_no(dataZd_CarrierVO.getCode());
                paramLedger_DetailVO.setCarrier_name(dataZd_CarrierVO.getName());
                paramLedger_DetailVO.setPremium_month(requestJs_Vin_Down_PremiumVO.getBegin_date_month());
                ledger_DetailDao.insert(paramLedger_DetailVO);
                //修改保费状态
                Js_Vin_Down_PremiumVO updateVin_Down_PremiumVO = new Js_Vin_Down_PremiumVO();
                updateVin_Down_PremiumVO.setData_state(LedgerEnum.PREMIUM_LEDGER.getCode());//已生成台账
                updateVin_Down_PremiumVO.setBegin_date_month(requestJs_Vin_Down_PremiumVO.getBegin_date_month());
                js_down_premiumDao.updateDataState(updateVin_Down_PremiumVO);
            }
            /**修改台账主表数据,只需要修含税改应付总、不含税应付总、不含税利润
             * 1、先查询台账明细下所有的明细
             * 2、循环得到值，更新
             * */
            ModiflyLedgerAmount(sheet_no,dataLedgerVO);
        } catch (Exception e) {
            throw new RuntimeException("新增数据失败，请联系管理员"+e.getMessage());
        }
        ResultVO resultVO = ResultVO.successResult();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("sheet_no", sheet_no);
        returnMap.put("msg", "新增成功");
        resultVO.setResultDataFull(returnMap);
        return resultVO;
    }

    @Override
    public ResultVO batchRrmovePremium(List<Ledger_DetailVO> ledger_detailVOs) {
        String sheet_no = ledger_detailVOs.get(0).getSheet_no();
        try {
            //根据单号查询台账
            LedgerVO paramLedgerVO = new LedgerVO();
            paramLedgerVO.setSheet_no(sheet_no);
            LedgerVO dataLedgerVO = ledgerDao.selectOne(paramLedgerVO);
            if (dataLedgerVO==null) {
                return ResultVO.failResult("该台账存在异常，请联系管理员");
            } else if (!dataLedgerVO.getState().equals(LedgerEnum.NORMAL.getCode())) {
                return ResultVO.failResult("只有正常状态的台账才可以进行此操作");
            }
            for (Ledger_DetailVO requestLedger_DetailVO: ledger_detailVOs) {
                Ledger_DetailVO paramLedger_DetailVO = new Ledger_DetailVO();
                paramLedger_DetailVO.setId(requestLedger_DetailVO.getId());
                Ledger_DetailVO dataLedger_DetailVO = ledger_DetailDao.selectOne(paramLedger_DetailVO);
                if(dataLedger_DetailVO==null) {
                    throw new RuntimeException("删除的数据不存在，请联系管理员");
                } else if (StringUtils.equals(dataLedger_DetailVO.getPremium_flag(),"N")) {
                    throw new RuntimeException("删除的数据不是保险费用，无法删除");
                }else if(StringUtils.isNotBlank(dataLedger_DetailVO.getInvoice_no())) {
                    throw new RuntimeException("删除的数据已经开具发票，无法删除");
                }
                ledger_DetailDao.deleteById(dataLedger_DetailVO.getId());
                //删除数据后，修改保费表状态
                Js_Vin_Down_PremiumVO updateVin_Down_PremiumVO = new Js_Vin_Down_PremiumVO();
                updateVin_Down_PremiumVO.setData_state(LedgerEnum.PREMIUM_NORMAL.getCode());//已生成台账
                updateVin_Down_PremiumVO.setBegin_date_month(dataLedger_DetailVO.getPremium_month());
                js_down_premiumDao.updateDataState(updateVin_Down_PremiumVO);
            }
            /**修改台账主表数据,只需要修含税改应付总、不含税应付总、不含税利润
             * 1、先查询台账明细下所有的明细
             * 2、循环得到值，更新
             * */
            ModiflyLedgerAmount(sheet_no,dataLedgerVO);
        } catch (Exception e) {
            throw new RuntimeException("删除数据失败，请联系管理员"+e.getMessage());
        }
        ResultVO resultVO = ResultVO.successResult();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("sheet_no", sheet_no);
        returnMap.put("msg", "删除成功");
        resultVO.setResultDataFull(returnMap);
        return resultVO;
    }

    /**
     * 增加或删除保费后调整主表金额统一方法
     * @param sheet_no
     * @param dataLedgerVO
     */
    public void ModiflyLedgerAmount (String sheet_no,LedgerVO dataLedgerVO) {
        /**修改台账主表数据,只需要修含税改应付总、不含税应付总、不含税利润
         * 1、先查询台账明细下所有的明细
         * 2、循环得到值，更新
         * */
        try {
            EntityWrapper ledger_detailEntityWrapper = new EntityWrapper();
            Ledger_DetailVO paramLedger_DetailVO = new Ledger_DetailVO();
            paramLedger_DetailVO.setSheet_no(sheet_no);
            ledger_detailEntityWrapper.setEntity(paramLedger_DetailVO);
            List<Ledger_DetailVO> dataLedger_DetailVOList = ledger_DetailDao.selectList(ledger_detailEntityWrapper);
            BigDecimal tax_down_total = new BigDecimal(0);
            BigDecimal ntax_down_total = new BigDecimal(0);
            for (Ledger_DetailVO dataLedger_DetailVO:dataLedger_DetailVOList) {
                tax_down_total=tax_down_total.add(dataLedger_DetailVO.getTax_down_total());
                ntax_down_total=ntax_down_total.add(dataLedger_DetailVO.getNtax_down_total());
            }
            LedgerVO paramLedgerVO = new LedgerVO();
            paramLedgerVO.setSheet_no(sheet_no);
            EntityWrapper ledgerWrapper = new EntityWrapper();
            ledgerWrapper.setEntity(paramLedgerVO);
            LedgerVO updateLedgerVO = new LedgerVO();
            updateLedgerVO.setTax_down_total(tax_down_total);
            updateLedgerVO.setNtax_down_total(ntax_down_total);
            updateLedgerVO.setNot_tax_profit(dataLedgerVO.getNtax_up_total().subtract(ntax_down_total));
            ledgerDao.update(updateLedgerVO,ledgerWrapper);
        } catch (Exception e) {
            throw new RuntimeException("删除数据失败，请联系管理员"+e.getMessage());
        }
    }


    public void UpdateJsDataByBillNumber(LedgerVO requestLedgerVO,String type) {
        //台账保存完之后，要修改该账单编号下的数据的结算号和结算批次
        /**type=1,是保存，需要将数据回写到表中,已做了判断，只有正常台账才可以修改
         * type=2,注销，需要将表中的数据清空并返回上一级状态
         * 注销后，需要修改该台账下结算数据的状态，对上返回到客户确认，对下如果是预估返回到财务审核，否则返回到承运商审核，另外补差的只改变补差表状态
         * */
        String ledger_type = requestLedgerVO.getLedger_type();//台账类型
        Boolean t = type.equals("1")?true:false;
        String dz_sheet_state = t?LedgerEnum.DZ_CREATE_LEDGER.getCode():LedgerEnum.DZ_CUS_CONFIRM.getCode();//对账单状态
        String js_no = t?requestLedgerVO.getJs_no():"";//结算号
        String js_batch = t?requestLedgerVO.getJs_project():"";//结算批次
        String up_js_state= t?Js_StateEnum.CREATE_TZ.getCode():Js_StateEnum.CUS_OK.getCode();//对上结算状态
        String down_js_state= t?Js_StateEnum.DOWN_CREATE_TZ.getCode():Js_StateEnum.DOWN_CARRIER_OK.getCode();//对下结算状态
        /**正常状态台账才去修改*/
        if (StringUtils.equals(LedgerEnum.NORMAL_LEDGER.getCode(),ledger_type)) {
            //更新对账单明细状态
        /*    Js_Dz_Sheet_DetailVO updateJs_Dz_Sheet_DetailVO = new Js_Dz_Sheet_DetailVO();
            EntityWrapper js_dz_sheet_detailWrapper = new EntityWrapper();
            Js_Dz_Sheet_DetailVO requestJs_Dz_Sheet_DetailVO = new Js_Dz_Sheet_DetailVO();
            requestJs_Dz_Sheet_DetailVO.setBill_number(requestLedgerVO.getBill_number());
            updateJs_Dz_Sheet_DetailVO.setData_state(dz_sheet_state);
            updateJs_Dz_Sheet_DetailVO.setJs_no(js_no);
            updateJs_Dz_Sheet_DetailVO.setJs_batch(js_batch);
            js_dz_sheet_detailWrapper.setEntity(requestJs_Dz_Sheet_DetailVO);
            js_dz_sheet_detailDao.update(updateJs_Dz_Sheet_DetailVO,js_dz_sheet_detailWrapper);*/

            //更新对上结算表状态
            Js_Vin_AmountVO updateJs_Vin_AmountVO = new Js_Vin_AmountVO();
            Js_Vin_AmountVO requestJs_Vin_AmountVO = new Js_Vin_AmountVO();
            EntityWrapper js_Vin_AmountWrapper = new EntityWrapper();
            requestJs_Vin_AmountVO.setBill_number(requestLedgerVO.getBill_number());
            updateJs_Vin_AmountVO.setJs_no(js_no);
            updateJs_Vin_AmountVO.setJs_batch(js_batch);
            updateJs_Vin_AmountVO.setJs_state(up_js_state);
            js_Vin_AmountWrapper.setEntity(requestJs_Vin_AmountVO);
            js_Vin_AmountDao.update(updateJs_Vin_AmountVO,js_Vin_AmountWrapper);

            //更新对下结算表状态
            Js_Vin_Down_AmountVO updateJs_Vin_Down_AmountVO = new Js_Vin_Down_AmountVO();
            updateJs_Vin_Down_AmountVO.setBill_number(requestLedgerVO.getBill_number());
            updateJs_Vin_Down_AmountVO.setJs_no(js_no);
            updateJs_Vin_Down_AmountVO.setJs_batch(js_batch);
            updateJs_Vin_Down_AmountVO.setJs_state(down_js_state);
            accountsToLedgerDao.updateDownData(updateJs_Vin_Down_AmountVO);
        } else {
            /**其他补差状态，只需要修改补差表状态*/
            Js_CompensationVO requestJs_CompensationVO = new Js_CompensationVO();
            Js_CompensationVO updateJs_CompensationVO = new Js_CompensationVO();
            EntityWrapper js_CompensationEntityWrapper = new EntityWrapper();
            requestJs_CompensationVO.setJs_no(requestLedgerVO.getJs_no());
            requestJs_CompensationVO.setJs_batch(requestLedgerVO.getJs_project());
            updateJs_CompensationVO.setState(CompensationEnum.COMPENSATION_NORMAL.getCode());
            js_CompensationEntityWrapper.setEntity(requestJs_CompensationVO);
            js_CompensationDao.update(updateJs_CompensationVO,js_CompensationEntityWrapper);
        }

    }

    @Override
    public ResultVO createInvoice(List<LedgerVO> vos, SysUserVO sysUserVO) {
        /***
         * 生成收款发票的逻辑
         * 1、拿到循环数据，判断数据是否存在异常
         * 2、正常台账，正常生成收款发票，补差台账，生成补差数据
         */
        try {
            for (LedgerVO requestLedgerVO: vos ) {
                //1、判断该结算号是否生成过发票_台账
                LedgerVO paramLedgerVO = new LedgerVO();
                paramLedgerVO.setJs_no(requestLedgerVO.getJs_no());
                paramLedgerVO.setLedger_no(requestLedgerVO.getLedger_no());
                LedgerVO dataLedgerVO = ledgerDao.selectOne(paramLedgerVO);
                if (dataLedgerVO==null) {
                    throw new RuntimeException("该台账存在异常，请联系管理员，结算号："+requestLedgerVO.getJs_no());
                } else if (StringUtils.notEquals(LedgerEnum.CHECK.getCode(),dataLedgerVO.getState())) {
                    throw new RuntimeException("非审核状态，无法生成发票，结算号："+requestLedgerVO.getJs_no());
                } else if (StringUtils.equals(LedgerEnum.INVOICE_FLAG_Y.getCode(),dataLedgerVO.getCus_invoice())
                || StringUtils.isNotBlank(dataLedgerVO.getInvoice_no())) {
                    throw new RuntimeException("该台账已开票或存在异常，请联系管理员，结算号："+requestLedgerVO.getJs_no());
                } else if (StringUtils.equals(dataLedgerVO.getLedger_type(),LedgerEnum.DOWN_COMPENSATION.getCode())) {
                    throw new RuntimeException("对下补差台账由承运商生成付款发票，结算号："+requestLedgerVO.getJs_no());
                } else if (StringUtils.equals(dataLedgerVO.getLedger_type(),LedgerEnum.DOWN_YUGU_COMPENSATION.getCode())) {
                    throw new RuntimeException("预估补差台账不生成发票，结算号："+requestLedgerVO.getJs_no());
                }
                //2、判断该结算批次是否生成过发票_发票，***作废，因为一个结算号或者批次可以多次生成台账
       /*         Receivable_invoiceVO paramReceivable_invoiceVO = new Receivable_invoiceVO();
                paramReceivable_invoiceVO.setJs_project(requestLedgerVO.getJs_project());
                paramReceivable_invoiceVO.setJs_project(requestLedgerVO.getJs_no());
                paramReceivable_invoiceVO.setLedger_no(requestLedgerVO.getLedger_no());
                paramReceivable_invoiceVO.setState(InvoiceEnum.NORMAL.getCode());
                Receivable_invoiceVO dataReceivable_invoiceVO =receivable_invoiceDao.selectOne(paramReceivable_invoiceVO);
                if (dataReceivable_invoiceVO!=null) {
                    throw new RuntimeException("该结算号已生成发票，结算号："+requestLedgerVO.getJs_no());
                }*/
                //3、插入到数据
                String ledger_type = dataLedgerVO.getLedger_type();//台账类型,上面已经判断，只能是0或1
                Receivable_invoiceVO requestReceivable_invoiceVO = new Receivable_invoiceVO();
                String sheet_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.JS_RECEIVABLE_INVOICE);
                requestReceivable_invoiceVO.setSheet_no(sheet_no);
                requestReceivable_invoiceVO.setJs_project(dataLedgerVO.getJs_project());
                requestReceivable_invoiceVO.setJs_no(dataLedgerVO.getJs_no());
                requestReceivable_invoiceVO.setLedger_no(dataLedgerVO.getLedger_no());
                if (StringUtils.equals(dataLedgerVO.getInvoice_company(),"CMAS")) {
                    requestReceivable_invoiceVO.setCus_no(dataLedgerVO.getInvoice_company());
                    requestReceivable_invoiceVO.setCus_name("长安马自达汽车销售分公司");
                } else {
                    requestReceivable_invoiceVO.setCus_no(dataLedgerVO.getCus_no());
                    requestReceivable_invoiceVO.setCus_name(dataLedgerVO.getCus_name());
                }
                requestReceivable_invoiceVO.setProject(dataLedgerVO.getProject());
                requestReceivable_invoiceVO.setGoods_name("运输票");
                requestReceivable_invoiceVO.setUnit("批");
                requestReceivable_invoiceVO.setAmount(dataLedgerVO.getTax_up_total());//金额--含税金额
                requestReceivable_invoiceVO.setTax_rate(dataLedgerVO.getTax_rate());//税率
                requestReceivable_invoiceVO.setCreate_by(sysUserVO.getRealName());
                requestReceivable_invoiceVO.setState(InvoiceEnum.NORMAL.getCode());
                requestReceivable_invoiceVO.setType(ledger_type);
                receivable_invoiceDao.insert(requestReceivable_invoiceVO);

                //4、生成发票后，需要回写台账数据
                //新增完后，需要回写台账的承运商开票字段及发票号
                LedgerVO paramWrapperLedgerVO = new LedgerVO();
                paramWrapperLedgerVO.setLedger_no(requestReceivable_invoiceVO.getLedger_no());
                LedgerVO updateLedgerVO = new LedgerVO();
                updateLedgerVO.setInvoice_no(requestReceivable_invoiceVO.getInvoice_no());
                updateLedgerVO.setCus_invoice(LedgerEnum.INVOICE_FLAG_Y.getCode());
                EntityWrapper LedgerWrapper = new EntityWrapper();
                LedgerWrapper.setEntity(paramWrapperLedgerVO);
                ledgerDao.update(updateLedgerVO,LedgerWrapper);

                if (StringUtils.equals(dataLedgerVO.getProject(),BusinessData_projectEnum.COMMODITY_CAR.getCode()) && StringUtils.equals(ledger_type,LedgerEnum.NORMAL_LEDGER.getCode())) {
                    //正常商品车台账
                    //发票完成后，回写结算数据，根据结算批次+结算号
                    Js_Vin_AmountVO updateJs_Vin_AmountVO = new Js_Vin_AmountVO();
                    Js_Vin_AmountVO requestJs_Vin_AmountVO = new Js_Vin_AmountVO();
                    EntityWrapper js_Vin_AmountWrapper = new EntityWrapper();
                    requestJs_Vin_AmountVO.setJs_batch(requestReceivable_invoiceVO.getJs_project());
                    requestJs_Vin_AmountVO.setJs_no(requestReceivable_invoiceVO.getJs_no());
                    updateJs_Vin_AmountVO.setInvoice_no(requestReceivable_invoiceVO.getInvoice_no());
                    updateJs_Vin_AmountVO.setInvoice_date(requestReceivable_invoiceVO.getCreate_date());
                    updateJs_Vin_AmountVO.setJs_state(Js_StateEnum.CREATE_FP.getCode());
                    js_Vin_AmountWrapper.setEntity(requestJs_Vin_AmountVO);
                    js_Vin_AmountDao.update(updateJs_Vin_AmountVO,js_Vin_AmountWrapper);
                } else if(StringUtils.equals(dataLedgerVO.getProject(),BusinessData_projectEnum.NON_COMMODITY_CAR.getCode()) && StringUtils.equals(ledger_type,LedgerEnum.NORMAL_LEDGER.getCode())){//正常非商品车
                    //正常非商品车台账
                    Js_Non_VehicleVO updateJs_Non_VehicleVO = new Js_Non_VehicleVO();
                    updateJs_Non_VehicleVO.setJs_batch(requestReceivable_invoiceVO.getJs_project());
                    updateJs_Non_VehicleVO.setJs_no(requestReceivable_invoiceVO.getJs_no());
                    updateJs_Non_VehicleVO.setJs_state(Js_StateEnum.CREATE_FP.getCode());
                    accountsToLedgerDao.updateUpNonJsVehicle(updateJs_Non_VehicleVO);
                } else {
                    //补差台账，前面已做判断，只能是对上补差，修改补差数据
                    Js_CompensationVO requestJs_CompensationVO = new Js_CompensationVO();
                    requestJs_CompensationVO.setJs_no(dataLedgerVO.getJs_no());
                    requestJs_CompensationVO.setJs_batch(dataLedgerVO.getJs_project());
                    requestJs_CompensationVO.setCus_no(dataLedgerVO.getCus_no());
                    EntityWrapper js_CompensationEntityWrapper = new EntityWrapper();
                    js_CompensationEntityWrapper.setEntity(requestJs_CompensationVO);
                    Js_CompensationVO updateJs_CompensationVO = new Js_CompensationVO();
                    updateJs_CompensationVO.setInvoice_flag(LedgerEnum.INVOICE_FLAG_Y.getCode());
                    js_CompensationDao.update(updateJs_CompensationVO,js_CompensationEntityWrapper);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("生成发票失败，请联系管理员，"+e.getMessage());
        }
        return ResultVO.successResult("生成发票成功");
    }

}
