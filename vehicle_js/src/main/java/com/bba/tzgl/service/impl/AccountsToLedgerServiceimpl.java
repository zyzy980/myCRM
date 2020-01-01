package com.bba.tzgl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.common.Sys_sheetidUtil;
import com.bba.common.constant.BusinessData_projectEnum;
import com.bba.common.constant.CompensationEnum;
import com.bba.common.constant.Js_StateEnum;
import com.bba.common.constant.LedgerEnum;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.dz.dao.Js_Dz_Sheet_DetailDao;
import com.bba.dz.vo.Js_Dz_Sheet_DetailVO;
import com.bba.nosettlement.vo.Js_Dz_Non_Sheet_DetailVO;
import com.bba.nosettlement.vo.Js_Non_Down_VehicleVO;
import com.bba.nosettlement.vo.Js_Non_VehicleVO;
import com.bba.settlement.dao.Js_Vin_AmountDao;
import com.bba.settlement.vo.Js_Vin_AmountVO;
import com.bba.settlement.vo.Js_Vin_Down_AmountVO;
import com.bba.tzgl.dao.AccountsToLedgerDao;
import com.bba.tzgl.dao.Js_CompensationDao;
import com.bba.tzgl.dao.LedgerDao;
import com.bba.tzgl.dao.Ledger_DetailDao;
import com.bba.tzgl.service.api.IAccountsToLedgerService;
import com.bba.tzgl.vo.Js_CompensationVO;
import com.bba.tzgl.vo.LedgerVO;
import com.bba.tzgl.vo.Ledger_DetailVO;
import com.bba.tzgl.vo.Ledger_Dz_SheetVO;
import com.bba.util.DateUtils;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.util.StringUtils;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/24 13:55
 */
@Service
@Transactional
public class AccountsToLedgerServiceimpl extends ServiceImpl<AccountsToLedgerDao, Ledger_Dz_SheetVO> implements IAccountsToLedgerService {
    @Resource
    private AccountsToLedgerDao accountsToLedgerDao;

    @Resource
    private LedgerDao ledgerDao;

    @Resource
    private Ledger_DetailDao ledger_detaildDao;

    @Resource
    private Js_Dz_Sheet_DetailDao js_dz_sheet_detailDao;

    @Resource
    private Js_Vin_AmountDao js_Vin_AmountDao;

    @Resource
    private Js_CompensationDao js_CompensationDao;

    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Ledger_Dz_SheetVO> list = accountsToLedgerDao.findListForGrid(jqGridParamModel);
        int count = accountsToLedgerDao.findListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }

    @Override
    public PageVO getNonListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Ledger_Dz_SheetVO> list = accountsToLedgerDao.findNonListForGrid(jqGridParamModel);
        int count = accountsToLedgerDao.findNonListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }

    @Override
    public PageVO getStatisticsListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Ledger_Dz_SheetVO> list = accountsToLedgerDao.getStatisticsListForGrid(jqGridParamModel);
        int count = accountsToLedgerDao.getStatisticsListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }

    @Override
    public PageVO getNonStatisticsListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Ledger_Dz_SheetVO> list = accountsToLedgerDao.getNonStatisticsListForGrid(jqGridParamModel);
        int count = accountsToLedgerDao.getNonStatisticsListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }

    @Override
    public PageVO getCompensationListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Js_CompensationVO> list = accountsToLedgerDao.getCompensationListForGrid(jqGridParamModel);
        int count = accountsToLedgerDao.getCompensationListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }


    @Override
    public PageVO getDownCompensationListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Js_CompensationVO> list = accountsToLedgerDao.getDownCompensationListForGrid(jqGridParamModel);
        int count = accountsToLedgerDao.getDownCompensationListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }


    /***
     * 对上补差台账生成
     * @param vos
     * @param sysUserVO
     * @return
     */
    @Override
    public ResultVO createUpCompensationLedger(List<Js_CompensationVO> vos, SysUserVO sysUserVO) {
        /**对上补差，只需要往台账写入对上补差的金额即可，结算号为之前的结算号，对下则为0
         * */
        try {
            for (Js_CompensationVO requestJs_CompensationVO:vos) {
                /**1、获取数据*/
                JqGridParamModel jqGridParamModel = new JqGridParamModel();
                jqGridParamModel.put("js_no",requestJs_CompensationVO.getJs_no());
                jqGridParamModel.put("js_batch",requestJs_CompensationVO.getJs_batch());
                jqGridParamModel.put("cus_no",requestJs_CompensationVO.getCus_no());
                jqGridParamModel.put("vehicle_project",requestJs_CompensationVO.getVehicle_project());
                String filters = JqGridSearchParamHandler.processSql("", jqGridParamModel);
                jqGridParamModel.setFilters(filters);
                List<Js_CompensationVO> list = accountsToLedgerDao.getCompensationListForGrid(jqGridParamModel);
                //如果长度不等于1，说明数据有误，需要抛出异常
                if (list.size()!=1) {
                    throw new RuntimeException("该补差数据存在异常，请联系管理员，结算号："+requestJs_CompensationVO.getJs_no());
                } else if (!StringUtils.equals(list.get(0).getState(),CompensationEnum.COMPENSATION_NORMAL.getCode())) {
                    throw new RuntimeException("该补差数据已生成台账，结算号："+requestJs_CompensationVO.getJs_no());
                }
                Js_CompensationVO dataJs_CompensationVO = list.get(0);
                /**2、插入到主表*/
                //插入到主表信息
                LedgerVO requestLedgerVO = new LedgerVO();
                //生成结算号/结算批次/台账号/类型
                BeanUtils.copyProperties(dataJs_CompensationVO, requestLedgerVO);
                String sheet_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.JS_LEDGER);
                String ledger_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.JS_LEDGER);
                requestLedgerVO.setSheet_no(sheet_no);
                requestLedgerVO.setLedger_no(ledger_no);
                requestLedgerVO.setJs_no(requestJs_CompensationVO.getJs_no());
                requestLedgerVO.setJs_project(requestJs_CompensationVO.getJs_batch());
                requestLedgerVO.setProject(requestJs_CompensationVO.getVehicle_project());
                requestLedgerVO.setState(LedgerEnum.NORMAL.getCode());
                requestLedgerVO.setLedger_type(LedgerEnum.UP_COMPENSATION.getCode());//对上补差
                requestLedgerVO.setCreate_by(sysUserVO.getRealName());
                /***设置台账的开票单位
                 * 单位逻辑:1、CMA统一全部换成CMAS
                 *          2、其余的不做变动
                 * */
                if (StringUtils.equals(requestLedgerVO.getCus_no(),"CMA")) {
                    requestLedgerVO.setInvoice_company("CMAS");
                }
                requestLedgerVO.setContract_no(dataJs_CompensationVO.getAfter_contract_no());
               /* //设置对上金额
                requestLedgerVO.setNtax_up_total(dataJs_CompensationVO.getNtax_up_tatal());
                requestLedgerVO.setTax_up_total(dataJs_CompensationVO.getTax_up_total());
                requestLedgerVO.setTax_amount(dataJs_CompensationVO.getTax_amount());
                requestLedgerVO.setTax_rate(dataJs_CompensationVO.getTax_rate());*/
                //利润
                requestLedgerVO.setNot_tax_profit(requestLedgerVO.getNtax_up_total());
                requestLedgerVO.setInvoice_no("");//需要把发票写成空
                ledgerDao.insert(requestLedgerVO);

                /**3、生成台账后，回写补差表数据*/
                Js_CompensationVO paramJs_CompensationVO = new Js_CompensationVO();
                paramJs_CompensationVO.setJs_no(requestJs_CompensationVO.getJs_no());
                paramJs_CompensationVO.setJs_batch(requestJs_CompensationVO.getJs_batch());
                paramJs_CompensationVO.setCus_no(paramJs_CompensationVO.getCus_no());
                EntityWrapper Js_CompensationEntityWrapper = new EntityWrapper();
                Js_CompensationEntityWrapper.setEntity(paramJs_CompensationVO);
                Js_CompensationVO updateJs_CompensationVO = new Js_CompensationVO();
                updateJs_CompensationVO.setState(CompensationEnum.COMPENSATION_LEDGER.getCode());
                js_CompensationDao.update(updateJs_CompensationVO,Js_CompensationEntityWrapper);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return ResultVO.successResult("生成台账成功");
    }

    /***
     * 对下补差生成台账
     * @param vos
     * @param sysUserVO
     * @return
     */
    @Override
    public ResultVO createDownCompensationLedger(List<Js_CompensationVO> vos, SysUserVO sysUserVO) {
        /**对下补差，分为【对下补差】和【预估补差】
         * */
        try {
            for (Js_CompensationVO requestJs_CompensationVO:vos) {
                /**1、获取数据*/
                JqGridParamModel jqGridParamModel = new JqGridParamModel();
                jqGridParamModel.put("js_no",requestJs_CompensationVO.getJs_no());
                jqGridParamModel.put("type",CompensationEnum.getCodeByName(requestJs_CompensationVO.getType()));
                jqGridParamModel.put("js_batch",requestJs_CompensationVO.getJs_batch());
                jqGridParamModel.put("carrier_no",requestJs_CompensationVO.getCarrier_no());
                jqGridParamModel.put("vehicle_project",requestJs_CompensationVO.getVehicle_project());
                String filters = JqGridSearchParamHandler.processSql("", jqGridParamModel);
                jqGridParamModel.setFilters(filters);
                List<Js_CompensationVO> list = accountsToLedgerDao.getDownCompensationListForGrid(jqGridParamModel);
                //如果长度不等于1，说明数据有误，需要抛出异常
                if (list.size()!=1) {
                    throw new RuntimeException("该补差数据存在异常，请联系管理员，结算号："+requestJs_CompensationVO.getJs_no());
                } else if (!StringUtils.equals(list.get(0).getState(),CompensationEnum.COMPENSATION_NORMAL.getCode())) {
                    throw new RuntimeException("该补差数据已生成台账，结算号："+requestJs_CompensationVO.getJs_no());
                }
                Js_CompensationVO dataJs_CompensationVO = list.get(0);
                /**2、插入到主表*/
                LedgerVO requestLedgerVO = new LedgerVO();
                BeanUtils.copyProperties(dataJs_CompensationVO, requestLedgerVO);
                //生成结算号/结算批次/台账号/类型
                String sheet_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.JS_LEDGER);
                String ledger_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.JS_LEDGER);
                requestLedgerVO.setSheet_no(sheet_no);
                requestLedgerVO.setLedger_no(ledger_no);
                requestLedgerVO.setJs_no(requestJs_CompensationVO.getJs_no());
                requestLedgerVO.setJs_project(requestJs_CompensationVO.getJs_batch());
                requestLedgerVO.setProject(requestJs_CompensationVO.getVehicle_project());
                requestLedgerVO.setState(LedgerEnum.NORMAL.getCode());
                if (StringUtils.equals(dataJs_CompensationVO.getType(),"2")) {
                    //对下补差
                    requestLedgerVO.setLedger_type(LedgerEnum.DOWN_COMPENSATION.getCode());
                } else {
                    //预估补差
                    requestLedgerVO.setLedger_type(LedgerEnum.DOWN_YUGU_COMPENSATION.getCode());
                }
                //获取对下费用总额
                /*requestLedgerVO.setTax_down_total(dataJs_CompensationVO.getTax_down_total());
                requestLedgerVO.setNtax_down_total(dataJs_CompensationVO.getNtax_down_total());*/
                //获取不含税利润
                requestLedgerVO.setNtax_up_total(new BigDecimal(0));
                requestLedgerVO.setNot_tax_profit(requestLedgerVO.getNtax_up_total().subtract(requestLedgerVO.getNtax_down_total()));
                requestLedgerVO.setCreate_by(sysUserVO.getRealName());
                requestLedgerVO.setInvoice_no("");
                ledgerDao.insert(requestLedgerVO);
                /**3、插入到明细表,目前由于是按承运商汇总，所以只有一条数据*/
                Ledger_DetailVO requestLedger_DetailVO = new Ledger_DetailVO();
                requestLedger_DetailVO.setSheet_no(sheet_no);
                requestLedger_DetailVO.setJs_no(requestJs_CompensationVO.getJs_no());
                requestLedger_DetailVO.setJs_project(requestJs_CompensationVO.getJs_batch());
                requestLedger_DetailVO.setCarrier_no(dataJs_CompensationVO.getCarrier_no());
                requestLedger_DetailVO.setCarrier_name(dataJs_CompensationVO.getCarrier_name());
                requestLedger_DetailVO.setNtax_down_total(dataJs_CompensationVO.getNtax_down_total());
                requestLedger_DetailVO.setTax_rate(dataJs_CompensationVO.getTax_rate());
                requestLedger_DetailVO.setTax_down_total(dataJs_CompensationVO.getTax_down_total());
                ledger_detaildDao.insert(requestLedger_DetailVO);

                /**3、生成台账后，回写补差表数据*/
                Js_CompensationVO paramJs_CompensationVO = new Js_CompensationVO();
                paramJs_CompensationVO.setJs_no(requestJs_CompensationVO.getJs_no());
                paramJs_CompensationVO.setJs_batch(requestJs_CompensationVO.getJs_batch());
                paramJs_CompensationVO.setCus_no(paramJs_CompensationVO.getCus_no());
                EntityWrapper Js_CompensationEntityWrapper = new EntityWrapper();
                Js_CompensationEntityWrapper.setEntity(paramJs_CompensationVO);
                Js_CompensationVO updateJs_CompensationVO = new Js_CompensationVO();
                updateJs_CompensationVO.setState(CompensationEnum.COMPENSATION_LEDGER.getCode());
                js_CompensationDao.update(updateJs_CompensationVO,Js_CompensationEntityWrapper);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return ResultVO.successResult("生成台账成功");
    }

    @Override
    public ResultVO createStatisticsLedger(List<Ledger_Dz_SheetVO> vos, SysUserVO sysUserVO) {
        /**
         * 商品车统计表生成台账逻辑：
         * 1、拿到传过来的账单号+对账单号集合，循环
         * 2、根据账单编号，生成一个台账，并且生成结算号+结算批次（2019-05-24结算批次）
         * */
        try {
            for (Ledger_Dz_SheetVO requestLedger_Dz_SheetVO:vos) {
                /**1、插入到主表*/
                //查询账单编号+对账单号查询对上的费用
                LedgerVO statisticsUpLedgerVO = accountsToLedgerDao.selectStatisticsUpLedger(requestLedger_Dz_SheetVO);
                if (statisticsUpLedgerVO==null) {
                    throw new RuntimeException("没有查询到该账单编号统计表对上的费用数据，请联系管理员");
                }
                //查询该BILL_NUMBER 是否存在台账
                LedgerVO paramLedgerVO = new LedgerVO();
                paramLedgerVO.setBill_number(requestLedger_Dz_SheetVO.getBill_number());
                LedgerVO dataLedgerVO = ledgerDao.selectOne(paramLedgerVO);
                if (dataLedgerVO !=null) {
                    throw new RuntimeException("账单编号已经生成台账,请勿重复生成"+requestLedger_Dz_SheetVO.getBill_number());
                }
                //直接查询统计表对下
                List<Ledger_DetailVO> ledger_detailVOList = accountsToLedgerDao.selectStatisticsLedgerDetailByBillNo(requestLedger_Dz_SheetVO);
                if (ledger_detailVOList.size()==0) {
                    throw new RuntimeException("没有查询到该账单编号统计表对下的费用数据，请联系管理员");
                }
                //插入到主表信息
                LedgerVO requestLedgerVO = statisticsUpLedgerVO;
                //算出对上税额
                requestLedgerVO.setTax_amount(requestLedgerVO.getTax_up_total().subtract(requestLedgerVO.getNtax_up_total()));
                //算出对上税率  税率=税额/不含税金额
                BigDecimal num = new BigDecimal(100);
                BigDecimal tax_rate = new BigDecimal(0);
                tax_rate = requestLedgerVO.getTax_amount().divide(requestLedgerVO.getNtax_up_total());
                requestLedgerVO.setTax_rate(tax_rate.multiply(num));
                //生成结算号/结算批次/台账号/类型
                Map<String,String> map = getJsno();
                String js_no = map.get("js_no");
                String js_project = map.get("js_project");
                String sheet_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.JS_LEDGER);
                String ledger_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.JS_LEDGER);
                //requestLedgerVO.setDz_sheet(requestLedger_Dz_SheetVO.getDz_sheet());
                requestLedgerVO.setBill_number(requestLedger_Dz_SheetVO.getBill_number());
                requestLedgerVO.setSheet_no(sheet_no);
                requestLedgerVO.setLedger_no(ledger_no);
                requestLedgerVO.setJs_no(js_no);
                requestLedgerVO.setJs_project(js_project);
                requestLedgerVO.setState(LedgerEnum.NORMAL.getCode());
                requestLedgerVO.setLedger_type(requestLedger_Dz_SheetVO.getType());
                /***设置台账的开票单位
                 * 单位逻辑:1、CMA统一全部换成CMAS
                 *          2、其余的不做变动
                 * */
                if (StringUtils.equals(requestLedgerVO.getCus_no(),"CMA")) {
                    requestLedgerVO.setInvoice_company("CMAS");
                }
                //获取对下费用总额
                BigDecimal tax_down_total = new BigDecimal(0);//含税应付总
                BigDecimal ntax_down_total = new BigDecimal(0);//不含税应付总
                for (Ledger_DetailVO ledger_detailVO:ledger_detailVOList) {
                    tax_down_total = tax_down_total.add(ledger_detailVO.getTax_down_total());
                    ntax_down_total = ntax_down_total.add(ledger_detailVO.getNtax_down_total());
                }
                requestLedgerVO.setTax_down_total(tax_down_total);
                requestLedgerVO.setNtax_down_total(ntax_down_total);
                //获取不含税利润
                requestLedgerVO.setNot_tax_profit(requestLedgerVO.getNtax_up_total().subtract(requestLedgerVO.getNtax_down_total()));
                requestLedgerVO.setProject(BusinessData_projectEnum.COMMODITY_CAR.getCode());
                requestLedgerVO.setCreate_by(sysUserVO.getRealName());
                ledgerDao.insert(requestLedgerVO);
                /**2、插入到明细表*/
                for (Ledger_DetailVO requestLedger_DetailVO:ledger_detailVOList) {
                    requestLedger_DetailVO.setSheet_no(requestLedgerVO.getSheet_no());
                    requestLedger_DetailVO.setJs_no(js_no);
                    requestLedger_DetailVO.setJs_project(js_project);
                    //算出对上税率  税率=税额/不含税金额
                    BigDecimal num2 = new BigDecimal(100);
                    BigDecimal down_tax_rate = new BigDecimal(0);
                    BigDecimal down_tax_amount = new BigDecimal(0);
                    down_tax_amount = requestLedger_DetailVO.getTax_down_total().subtract(requestLedger_DetailVO.getNtax_down_total());
                    down_tax_rate = down_tax_amount.divide(requestLedger_DetailVO.getNtax_down_total());
                    requestLedger_DetailVO.setTax_rate(down_tax_rate.multiply(num2));
                    requestLedgerVO.setTax_rate(tax_rate.multiply(num));
                    ledger_detaildDao.insert(requestLedger_DetailVO);
                }
                /**2、回写数据，更新统计表状态为“台账”*/
            /*    Js_Dz_Sheet_DetailVO updateJs_Dz_Sheet_DetailVO = new Js_Dz_Sheet_DetailVO();
                EntityWrapper js_dz_sheet_detailWrapper = new EntityWrapper();
                Js_Dz_Sheet_DetailVO requestJs_Dz_Sheet_DetailVO = new Js_Dz_Sheet_DetailVO();
                requestJs_Dz_Sheet_DetailVO.setBill_number(requestLedger_Dz_SheetVO.getBill_number());
                updateJs_Dz_Sheet_DetailVO.setJs_no(js_no);
                updateJs_Dz_Sheet_DetailVO.setData_state(LedgerEnum.DZ_CREATE_LEDGER.getCode());
                updateJs_Dz_Sheet_DetailVO.setJs_batch(js_project);
                js_dz_sheet_detailWrapper.setEntity(requestJs_Dz_Sheet_DetailVO);
                js_dz_sheet_detailDao.update(updateJs_Dz_Sheet_DetailVO,js_dz_sheet_detailWrapper);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return ResultVO.successResult("生成台账成功");
    }

    @Override
    public ResultVO createNonStatisticsLedger(List<Ledger_Dz_SheetVO> vos, SysUserVO sysUserVO) {
        /**
         * 非商品车统计表生成台账逻辑：
         * 1、拿到传过来的账单号+对账单号集合，循环
         * 2、根据账单编号，生成一个台账，并且生成结算号+结算批次（2019-05-24结算批次）
         * */
        try {
            for (Ledger_Dz_SheetVO requestLedger_Dz_SheetVO:vos) {
                /**1、插入到主表*/
                //查询账单编号+对账单号查询对上的费用
                LedgerVO statisticsUpLedgerVO = accountsToLedgerDao.selectNonStatisticsUpLedger(requestLedger_Dz_SheetVO);
                if (statisticsUpLedgerVO==null) {
                    throw new RuntimeException("没有查询到该账单编号统计表对上的费用数据，请联系管理员");
                }
                //查询该BILL_NUMBER 是否存在台账
                LedgerVO paramLedgerVO = new LedgerVO();
                paramLedgerVO.setBill_number(requestLedger_Dz_SheetVO.getBill_number());
                LedgerVO dataLedgerVO = ledgerDao.selectOne(paramLedgerVO);
                if (dataLedgerVO !=null) {
                    throw new RuntimeException("账单编号已经生成台账,请勿重复生成"+requestLedger_Dz_SheetVO.getBill_number());
                }
                //直接查询统计表对下
                List<Ledger_DetailVO> ledger_detailVOList = accountsToLedgerDao.selectNonStatisticsLedgerDetailByBillNo(requestLedger_Dz_SheetVO);
                if (ledger_detailVOList.size()==0) {
                    throw new RuntimeException("没有查询到该账单编号统计表对下的费用数据，请联系管理员");
                }
                //插入到主表信息
                LedgerVO requestLedgerVO = statisticsUpLedgerVO;
                //算出对上税额
                requestLedgerVO.setTax_amount(requestLedgerVO.getTax_up_total().subtract(requestLedgerVO.getNtax_up_total()));
                //算出对上税率  税率=税额/不含税金额
                BigDecimal num = new BigDecimal(100);
                BigDecimal tax_rate = new BigDecimal(0);
                tax_rate = requestLedgerVO.getTax_amount().divide(requestLedgerVO.getNtax_up_total());
                requestLedgerVO.setTax_rate(tax_rate.multiply(num));
                //生成结算号/结算批次/台账号/类型
                Map<String,String> map = getJsno();
                String js_no = map.get("js_no");
                String js_project = map.get("js_project");
                String sheet_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.JS_LEDGER);
                String ledger_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.JS_LEDGER);
                //requestLedgerVO.setDz_sheet(requestLedger_Dz_SheetVO.getDz_sheet());
                requestLedgerVO.setBill_number(requestLedger_Dz_SheetVO.getBill_number());
                requestLedgerVO.setSheet_no(sheet_no);
                requestLedgerVO.setLedger_no(ledger_no);
                requestLedgerVO.setJs_no(js_no);
                requestLedgerVO.setJs_project(js_project);
                requestLedgerVO.setState(LedgerEnum.NORMAL.getCode());
                requestLedgerVO.setLedger_type(requestLedger_Dz_SheetVO.getType());
                /***设置台账的开票单位
                 * 单位逻辑:1、CMA统一全部换成CMAS
                 *          2、其余的不做变动
                 * */
                if (StringUtils.equals(requestLedgerVO.getCus_no(),"CMA")) {
                    requestLedgerVO.setInvoice_company("CMAS");
                }
                //获取对下费用总额
                BigDecimal tax_down_total = new BigDecimal(0);//含税应付总
                BigDecimal ntax_down_total = new BigDecimal(0);//不含税应付总
                for (Ledger_DetailVO ledger_detailVO:ledger_detailVOList) {
                    tax_down_total = tax_down_total.add(ledger_detailVO.getTax_down_total());
                    ntax_down_total = ntax_down_total.add(ledger_detailVO.getNtax_down_total());
                }
                requestLedgerVO.setTax_down_total(tax_down_total);
                requestLedgerVO.setNtax_down_total(ntax_down_total);
                //获取不含税利润
                requestLedgerVO.setNot_tax_profit(requestLedgerVO.getNtax_up_total().subtract(requestLedgerVO.getNtax_down_total()));
                requestLedgerVO.setCreate_by(sysUserVO.getRealName());
                requestLedgerVO.setProject(BusinessData_projectEnum.NON_COMMODITY_CAR.getCode());
                ledgerDao.insert(requestLedgerVO);
                /**2、插入到明细表*/
                for (Ledger_DetailVO requestLedger_DetailVO:ledger_detailVOList) {
                    requestLedger_DetailVO.setSheet_no(requestLedgerVO.getSheet_no());
                    requestLedger_DetailVO.setJs_no(js_no);
                    requestLedger_DetailVO.setJs_project(js_project);
                    //算出对上税率  税率=税额/不含税金额
                    BigDecimal num2 = new BigDecimal(100);
                    BigDecimal down_tax_rate = new BigDecimal(0);
                    BigDecimal down_tax_amount = new BigDecimal(0);
                    down_tax_amount = requestLedger_DetailVO.getTax_down_total().subtract(requestLedger_DetailVO.getNtax_down_total());
                    down_tax_rate = down_tax_amount.divide(requestLedger_DetailVO.getNtax_down_total());
                    requestLedger_DetailVO.setTax_rate(down_tax_rate.multiply(num2));
                    requestLedgerVO.setTax_rate(tax_rate.multiply(num));
                    ledger_detaildDao.insert(requestLedger_DetailVO);
                }
                /**2、回写数据，更新统计表状态为“台账”*/
            /*    Js_Dz_Sheet_DetailVO updateJs_Dz_Sheet_DetailVO = new Js_Dz_Sheet_DetailVO();
                EntityWrapper js_dz_sheet_detailWrapper = new EntityWrapper();
                Js_Dz_Sheet_DetailVO requestJs_Dz_Sheet_DetailVO = new Js_Dz_Sheet_DetailVO();
                requestJs_Dz_Sheet_DetailVO.setBill_number(requestLedger_Dz_SheetVO.getBill_number());
                updateJs_Dz_Sheet_DetailVO.setJs_no(js_no);
                updateJs_Dz_Sheet_DetailVO.setData_state(LedgerEnum.DZ_CREATE_LEDGER.getCode());
                updateJs_Dz_Sheet_DetailVO.setJs_batch(js_project);
                js_dz_sheet_detailWrapper.setEntity(requestJs_Dz_Sheet_DetailVO);
                js_dz_sheet_detailDao.update(updateJs_Dz_Sheet_DetailVO,js_dz_sheet_detailWrapper);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return ResultVO.successResult("生成台账成功");
    }

    @Override
    public ResultVO createLedger(List<Ledger_Dz_SheetVO> vos, SysUserVO sysUserVO) {
        /**
         * 生成台账逻辑：
         * 1、拿到传过来的账单号+对账单号集合，循环
         * 2、根据账单编号，生成一个台账，并且生成结算号+结算批次（2019-05-24结算批次），更新业务数据的结算号和结算批次
         * 3、考虑数据的状态，如：非商品车的VIP/对下不付
         * 4、主表的税率是客户税率，明细税率为承运商税率
         * 5、台账生成的属性：VIP/对下不付/正常
         * */
        try {
            for (Ledger_Dz_SheetVO requestLedger_Dz_SheetVO:vos) {
                /**1、插入到主表*/
                //查询账单编号+对账单号查询对上的费用
                LedgerVO upLedgerVO = accountsToLedgerDao.selectUpLedger(requestLedger_Dz_SheetVO);
                if (upLedgerVO==null) {
                    throw new RuntimeException("没有查询到该账单编号对上的费用数据，请联系管理员");
                }
                //查询该DZ_SHEET+BILL_NUMBER 是否存在台账
                LedgerVO paramLedgerVO = new LedgerVO();
                paramLedgerVO.setDz_sheet(requestLedger_Dz_SheetVO.getDz_sheet());
                paramLedgerVO.setBill_number(requestLedger_Dz_SheetVO.getBill_number());
                LedgerVO dataLedgerVO = ledgerDao.selectOne(paramLedgerVO);
                if (dataLedgerVO !=null) {
                    throw new RuntimeException("账单编号已经生成台账,请勿重复生成"+requestLedger_Dz_SheetVO.getBill_number());
                }
                //直接查询该账单编号下的VIN金额，对下结算表，拿到对下金额。目前逻辑，必须对上对下都数量一致（因为存在预估合同了）
                List<Ledger_DetailVO> ledger_detailVOList = accountsToLedgerDao.selectLedgerDetailByBillNo(requestLedger_Dz_SheetVO);
                if (ledger_detailVOList.size()==0) {
                    throw new RuntimeException("没有查询到该账单编号对下的费用数据，请确认财务或者承运商是否审核？账单编号:"+requestLedger_Dz_SheetVO.getBill_number());
                }
                //插入到主表信息
                LedgerVO requestLedgerVO = upLedgerVO;
                //算出对上税额
                requestLedgerVO.setTax_amount(requestLedgerVO.getTax_up_total().subtract(requestLedgerVO.getNtax_up_total()));
                //生成结算号/结算批次/台账号/类型
                Map<String,String> map = getJsno();
                String js_no = map.get("js_no");
                String js_project = map.get("js_project");
                String sheet_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.JS_LEDGER);
                String ledger_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.JS_LEDGER);
                requestLedgerVO.setDz_sheet(requestLedger_Dz_SheetVO.getDz_sheet());
                requestLedgerVO.setBill_number(requestLedger_Dz_SheetVO.getBill_number());
                requestLedgerVO.setSheet_no(sheet_no);
                requestLedgerVO.setLedger_no(ledger_no);
                requestLedgerVO.setJs_no(js_no);
                requestLedgerVO.setJs_project(js_project);
                requestLedgerVO.setProject(BusinessData_projectEnum.COMMODITY_CAR.getCode());
                requestLedgerVO.setState(LedgerEnum.NORMAL.getCode());
                requestLedgerVO.setLedger_type(LedgerEnum.NORMAL_LEDGER.getCode());//台账类型
                /***设置台账的开票单位
                 * 单位逻辑:1、CMA统一全部换成CMAS
                 *          2、其余的不做变动
                 * */
                if (StringUtils.equals(requestLedgerVO.getCus_no(),"CMA")) {
                    requestLedgerVO.setInvoice_company("CMAS");
                }
                //获取对下费用总额
                BigDecimal tax_down_total = new BigDecimal(0);//含税应付总
                BigDecimal ntax_down_total = new BigDecimal(0);//不含税应付总
                Integer counts=0;//对下的条数
                for (Ledger_DetailVO ledger_detailVO:ledger_detailVOList) {
                    tax_down_total = tax_down_total.add(ledger_detailVO.getTax_down_total());
                    ntax_down_total = ntax_down_total.add(ledger_detailVO.getNtax_down_total());
                    counts+=ledger_detailVO.getCounts();
                }
                //判断对上对下数量是否一致，不一致不允许生成台账,对下多承运商，对下可能比对上多
                if (upLedgerVO.getCounts()>counts) {
                    throw new RuntimeException("对上的结算数量不能大于对下结算数量，请核实数据后再生成台账，账单编号:"+requestLedger_Dz_SheetVO.getBill_number());
                }
                requestLedgerVO.setTax_down_total(tax_down_total);
                requestLedgerVO.setNtax_down_total(ntax_down_total);
                //获取不含税利润
                requestLedgerVO.setNot_tax_profit(requestLedgerVO.getNtax_up_total().subtract(requestLedgerVO.getNtax_down_total()));
                requestLedgerVO.setCreate_by(sysUserVO.getRealName());
                ledgerDao.insert(requestLedgerVO);
                /**2、插入到明细表*/
                for (Ledger_DetailVO requestLedger_DetailVO:ledger_detailVOList) {
                    requestLedger_DetailVO.setSheet_no(requestLedgerVO.getSheet_no());
                    requestLedger_DetailVO.setJs_no(js_no);
                    requestLedger_DetailVO.setJs_project(js_project);
                    ledger_detaildDao.insert(requestLedger_DetailVO);
                }
                /**2、回写数据，更新数据来源的结算号，对账单明细状态变为“台账”*/
                //更新对账单明细状态
                Js_Dz_Sheet_DetailVO updateJs_Dz_Sheet_DetailVO = new Js_Dz_Sheet_DetailVO();
                EntityWrapper js_dz_sheet_detailWrapper = new EntityWrapper();
                Js_Dz_Sheet_DetailVO requestJs_Dz_Sheet_DetailVO = new Js_Dz_Sheet_DetailVO();
                requestJs_Dz_Sheet_DetailVO.setBill_number(requestLedger_Dz_SheetVO.getBill_number());
                updateJs_Dz_Sheet_DetailVO.setJs_no(js_no);
                updateJs_Dz_Sheet_DetailVO.setData_state(LedgerEnum.DZ_CREATE_LEDGER.getCode());
                updateJs_Dz_Sheet_DetailVO.setJs_batch(js_project);
                js_dz_sheet_detailWrapper.setEntity(requestJs_Dz_Sheet_DetailVO);
                js_dz_sheet_detailDao.update(updateJs_Dz_Sheet_DetailVO,js_dz_sheet_detailWrapper);
                //更新对上结算表状态
                Js_Vin_AmountVO updateJs_Vin_AmountVO = new Js_Vin_AmountVO();
                Js_Vin_AmountVO requestJs_Vin_AmountVO = new Js_Vin_AmountVO();
                EntityWrapper js_Vin_AmountWrapper = new EntityWrapper();
                requestJs_Vin_AmountVO.setBill_number(requestLedger_Dz_SheetVO.getBill_number());
                updateJs_Vin_AmountVO.setJs_no(js_no);
                updateJs_Vin_AmountVO.setJs_batch(js_project);
                updateJs_Vin_AmountVO.setJs_state(Js_StateEnum.CREATE_TZ.getCode());
                js_Vin_AmountWrapper.setEntity(requestJs_Vin_AmountVO);
                js_Vin_AmountDao.update(updateJs_Vin_AmountVO,js_Vin_AmountWrapper);
                //更新对下结算表状态
                Js_Vin_Down_AmountVO updateJs_Vin_Down_AmountVO = new Js_Vin_Down_AmountVO();
                updateJs_Vin_Down_AmountVO.setBill_number(requestLedger_Dz_SheetVO.getBill_number());
                updateJs_Vin_Down_AmountVO.setJs_no(js_no);
                updateJs_Vin_Down_AmountVO.setJs_batch(js_project);
                updateJs_Vin_Down_AmountVO.setJs_state(Js_StateEnum.DOWN_CREATE_TZ.getCode());
                accountsToLedgerDao.updateDownData(updateJs_Vin_Down_AmountVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return ResultVO.successResult("生成台账成功");
    }

    @Override
    public ResultVO createNonLedger(List<Ledger_Dz_SheetVO> vos, SysUserVO sysUserVO) {
        /**
         * 非商品车生成台账逻辑：
         * 1、拿到传过来的账单号+对账单号集合，循环
         * 2、根据账单编号，生成一个台账，并且生成结算号+结算批次（2019-05-24结算批次），更新业务数据的结算号和结算批次
         * 3、考虑数据的状态，如：非商品车的VIP/对下不付
         * 4、主表的税率是客户税率，明细税率为承运商税率
         * 5、台账生成的属性：VIP/对下不付/正常
         * */
        try {
            for (Ledger_Dz_SheetVO requestLedger_Dz_SheetVO:vos) {
                /**1、插入到主表*/
                //查询账单编号+对账单号查询对上的费用
                LedgerVO upLedgerVO = accountsToLedgerDao.selectNonUpLedger(requestLedger_Dz_SheetVO);
                if (upLedgerVO==null) {
                    throw new RuntimeException("没有查询到该账单编号对上的费用数据，请联系管理员");
                }
                //查询该DZ_SHEET+BILL_NUMBER 是否存在台账
                LedgerVO paramLedgerVO = new LedgerVO();
                paramLedgerVO.setDz_sheet(requestLedger_Dz_SheetVO.getDz_sheet());
                paramLedgerVO.setBill_number(requestLedger_Dz_SheetVO.getBill_number());
                LedgerVO dataLedgerVO = ledgerDao.selectOne(paramLedgerVO);
                if (dataLedgerVO !=null) {
                    throw new RuntimeException("账单编号已经生成台账,请勿重复生成"+requestLedger_Dz_SheetVO.getBill_number());
                }
                //直接查询该账单编号下的VIN金额，left join 对下结算表，拿到对下金额
                List<Ledger_DetailVO> ledger_detailVOList = accountsToLedgerDao.selectNonLedgerDetailByBillNo(requestLedger_Dz_SheetVO);
                if (ledger_detailVOList.size()==0) {
                    throw new RuntimeException("没有查询到该账单编号对下的费用数据，请确认财务或者承运商是否审核？账单编号:"+requestLedger_Dz_SheetVO.getBill_number());
                }
                //插入到主表信息
                LedgerVO requestLedgerVO = upLedgerVO;
                //算出对上税额
                requestLedgerVO.setTax_amount(requestLedgerVO.getTax_up_total().subtract(requestLedgerVO.getNtax_up_total()));
                //生成结算号/结算批次/台账号/类型
                Map<String,String> map = getJsno();
                String js_no = map.get("js_no");
                String js_project = map.get("js_project");
                String sheet_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.JS_LEDGER);
                String ledger_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.JS_LEDGER);
                requestLedgerVO.setDz_sheet(requestLedger_Dz_SheetVO.getDz_sheet());
                requestLedgerVO.setBill_number(requestLedger_Dz_SheetVO.getBill_number());
                requestLedgerVO.setSheet_no(sheet_no);
                requestLedgerVO.setLedger_no(ledger_no);
                requestLedgerVO.setJs_no(js_no);
                requestLedgerVO.setJs_project(js_project);
                requestLedgerVO.setProject(BusinessData_projectEnum.NON_COMMODITY_CAR.getCode());
                requestLedgerVO.setState(LedgerEnum.NORMAL.getCode());
                requestLedgerVO.setLedger_type(LedgerEnum.NORMAL_LEDGER.getCode());
                /***设置台账的开票单位
                 * 单位逻辑:1、CMA统一全部换成CMAS
                 *          2、其余的不做变动
                 * */
                if (StringUtils.equals(requestLedgerVO.getCus_no(),"CMA")) {
                    requestLedgerVO.setInvoice_company("CMAS");
                }
                //获取对下费用总额
                BigDecimal tax_down_total = new BigDecimal(0);//含税应付总
                BigDecimal ntax_down_total = new BigDecimal(0);//不含税应付总
                for (Ledger_DetailVO ledger_detailVO:ledger_detailVOList) {
                    tax_down_total = tax_down_total.add(ledger_detailVO.getTax_down_total());
                    ntax_down_total = ntax_down_total.add(ledger_detailVO.getNtax_down_total());
                }
                requestLedgerVO.setTax_down_total(tax_down_total);
                requestLedgerVO.setNtax_down_total(ntax_down_total);
                //获取不含税利润
                requestLedgerVO.setNot_tax_profit(requestLedgerVO.getNtax_up_total().subtract(requestLedgerVO.getNtax_down_total()));
                requestLedgerVO.setCreate_by(sysUserVO.getRealName());
                ledgerDao.insert(requestLedgerVO);

                /**2、插入到明细表*/
                for (Ledger_DetailVO requestLedger_DetailVO:ledger_detailVOList) {
                    requestLedger_DetailVO.setSheet_no(requestLedgerVO.getSheet_no());
                    requestLedger_DetailVO.setJs_no(js_no);
                    requestLedger_DetailVO.setJs_project(js_project);
                    ledger_detaildDao.insert(requestLedger_DetailVO);
                }

                /**3、回写数据，更新数据来源的结算号，对账单明细状态变为“台账”*/
                //更新非商品车对账单明细状态
                Js_Dz_Non_Sheet_DetailVO updateJs_Dz_Sheet_DetailVO = new Js_Dz_Non_Sheet_DetailVO();
                updateJs_Dz_Sheet_DetailVO.setBill_number(requestLedger_Dz_SheetVO.getBill_number());
                updateJs_Dz_Sheet_DetailVO.setJs_no(js_no);
                updateJs_Dz_Sheet_DetailVO.setJs_state(LedgerEnum.DZ_CREATE_LEDGER.getCode());
                updateJs_Dz_Sheet_DetailVO.setJs_batch(js_project);
                accountsToLedgerDao.updateDzNonDetail(updateJs_Dz_Sheet_DetailVO);
                //更新对上结算表状态
                Js_Non_VehicleVO updateJs_Non_VehicleVO = new Js_Non_VehicleVO();
                updateJs_Non_VehicleVO.setBill_number(requestLedger_Dz_SheetVO.getBill_number());
                updateJs_Non_VehicleVO.setJs_no(js_no);
                updateJs_Non_VehicleVO.setJs_batch(js_project);
                updateJs_Non_VehicleVO.setJs_state(Js_StateEnum.CREATE_TZ.getCode());
                accountsToLedgerDao.updateJsNonVehicle(updateJs_Non_VehicleVO);
                //更新对下结算表状态
                Js_Non_Down_VehicleVO updateJs_Non_Down_VehicleVO = new Js_Non_Down_VehicleVO();
                updateJs_Non_Down_VehicleVO.setBill_number(requestLedger_Dz_SheetVO.getBill_number());
                updateJs_Non_Down_VehicleVO.setJs_no(js_no);
                updateJs_Non_Down_VehicleVO.setJs_batch(js_project);
                updateJs_Non_Down_VehicleVO.setJs_state(Js_StateEnum.DOWN_CREATE_TZ.getCode());
                accountsToLedgerDao.updateNonDownData(updateJs_Non_Down_VehicleVO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return ResultVO.successResult("生成台账成功");
    }

    /***
     * 获取结算号
     * 结算号规则
     * ZCYS19010406  19年份 01 没有跨年  04月份 06序号
     * @return
     */
    public Map<String,String> getJsno () {
        String js_no=ledgerDao.getToponeJsno();
        String first = "ZCYS";
        String index = "01";
        String data_format = DateUtils.getDate("yyyyMMdd");
        String now_year = data_format.substring(2,4);
        String now_month = data_format.substring(4,6);
        String now_day = data_format.substring(6,8);
        String now_last = "01";
        //第一种情况：系统一个结算号都没有
        if (StringUtils.isBlank(js_no)) {
            js_no = first+now_year+index+now_month+now_last;
        } else if (StringUtils.notEquals(js_no.substring(4,6),now_year)){//第二种情况：不是同一年的
            js_no = first+now_year+index+now_month+now_last;
        } else if (StringUtils.notEquals(js_no.substring(4,6),now_year)) {//第三种情况：不是同月的
            js_no = first+now_year+index+now_month+now_last;
        } else {//第四种情况，同年同月的，序号往后叠加
            String last = js_no.substring(10,12);
            Integer i_last = Integer.valueOf(last)+1;
            last = String.format("%02d",i_last);
            js_no = first+now_year+index+now_month+last;
        }
        //结算批次
        String js_project =now_year+now_month+now_day + "结算批次";
        Map<String,String> map = new HashMap<>();
        map.put("js_no",js_no);
        map.put("js_project",js_project);
        return map;
    }
}
