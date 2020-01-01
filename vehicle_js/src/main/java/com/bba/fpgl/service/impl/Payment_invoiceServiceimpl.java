package com.bba.fpgl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.common.Sys_sheetidUtil;
import com.bba.common.constant.*;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.fpgl.dao.IPayment_invoiceDao;
import com.bba.fpgl.dao.IPayment_invoice_DetailDao;
import com.bba.fpgl.dao.ITr_Payment_PlanDao;
import com.bba.fpgl.dto.Payment_invoiceDTO;
import com.bba.fpgl.service.api.IPayment_invoiceService;
import com.bba.fpgl.vo.Payment_invoiceVO;
import com.bba.fpgl.vo.Payment_invoice_DetailVO;
import com.bba.fpgl.vo.Tr_Payment_PlanVO;
import com.bba.nosettlement.dao.IJs_Non_Down_VehicleDao;
import com.bba.nosettlement.vo.Js_Non_Down_VehicleVO;
import com.bba.settlement.dao.IJs_Vin_Down_AmountDao;
import com.bba.settlement.vo.Js_Vin_Down_AmountVO;
import com.bba.tzgl.dao.Js_CompensationDao;
import com.bba.tzgl.dao.Ledger_DetailDao;
import com.bba.tzgl.vo.Js_CompensationVO;
import com.bba.tzgl.vo.Ledger_DetailVO;
import com.bba.util.DateUtils;
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
public class Payment_invoiceServiceimpl extends ServiceImpl<IPayment_invoiceDao, Payment_invoiceVO> implements IPayment_invoiceService {

    @Resource
    private IPayment_invoiceDao payment_invoiceDao;

    @Resource
    private IPayment_invoice_DetailDao payment_invoice_DetailDao;

    @Resource
    private Js_CompensationDao js_CompensationDao;

    @Resource
    private Ledger_DetailDao ledger_DetailDao;

    @Resource
    private IJs_Vin_Down_AmountDao js_Vin_Down_AmountDao;

    @Resource
    private IJs_Non_Down_VehicleDao js_Non_Down_VehicleDao;

    @Resource
    private ITr_Payment_PlanDao tr_Payment_PlanDao;

    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Payment_invoiceVO> list = payment_invoiceDao.findListForGrid(jqGridParamModel);
        int count = payment_invoiceDao.findListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }

    @Override
    public PageVO getBeforListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Payment_invoiceVO> list = payment_invoiceDao.findBeforListForGrid(jqGridParamModel);
        int count = payment_invoiceDao.findBeforListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }

    @Override
    public PageVO getBeforCompensationListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Payment_invoiceVO> list = payment_invoiceDao.findBeforCompensationListForGrid(jqGridParamModel);
        int count = payment_invoiceDao.findBeforCompensationListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }

    @Override
    public ResultVO saveDetail(Payment_invoiceDTO requestPayment_invoiceDTO, SysUserVO sysUserVO) {
        /**
         * 发票保存逻辑：
         * 1、结算项目来自于台账的结算批次
         * 2、保存后，回写台账结算号状态
         * */
        Payment_invoiceVO requestPayment_invoiceVO = requestPayment_invoiceDTO.getPayment_invoiceVO();
        List<Payment_invoice_DetailVO> requestPayment_invoice_detailVOList = requestPayment_invoiceDTO.getPayment_invoice_detailVOList();
        if(StringUtils.isBlank(requestPayment_invoiceVO.getSheet_no())){
            return ResultVO.failResult("单号不能为空");
        }
        if(StringUtils.isBlank(requestPayment_invoiceVO.getJs_no())){
            return ResultVO.failResult("结算号不能为空");
        }
        if(StringUtils.isBlank(requestPayment_invoiceVO.getJs_project())){
            return ResultVO.failResult("结算批次不能为空");
        }
        if(requestPayment_invoiceVO.getId()==0){
            //新增发票
            //判断结算批次是否被占用
            Ledger_DetailVO paramLedger_DetailVO = new Ledger_DetailVO();
            paramLedger_DetailVO.setJs_project(requestPayment_invoiceVO.getJs_project());
            paramLedger_DetailVO.setJs_no(requestPayment_invoiceVO.getJs_no());
            paramLedger_DetailVO.setCarrier_invoice(LedgerEnum.INVOICE_FLAG_N.getCode());
            Ledger_DetailVO dataLedger_DetailVO = ledger_DetailDao.selectOne(paramLedger_DetailVO);
            if (dataLedger_DetailVO==null) {
                return ResultVO.failResult("该结算号已开票或不存在，请核实数据");
            }
            //目前允许发票重复
        /*    if (StringUtils.isNotBlank(requestPayment_invoiceVO.getInvoice_no())) {
                Payment_invoiceVO paramPayment_invoiceVO = new Payment_invoiceVO();
                paramPayment_invoiceVO.setInvoice_no(requestPayment_invoiceVO.getInvoice_no());
                paramPayment_invoiceVO.setState(InvoiceEnum.NORMAL.getCode());
                Payment_invoiceVO dataPayment_invoiceVO = payment_invoiceDao.selectOne(paramPayment_invoiceVO);
                if(dataPayment_invoiceVO != null){
                    return ResultVO.failResult("发票号已存在,请勿重复录入"+requestPayment_invoiceVO.getInvoice_no());
                }
            }*/
            requestPayment_invoiceVO.setCreate_by(sysUserVO.getRealName());
            requestPayment_invoiceVO.setState(InvoiceEnum.NORMAL.getCode());
            payment_invoiceDao.insert(requestPayment_invoiceVO);
            //新增完后，需要回写台账的承运商开票字段及发票号
           /* Ledger_DetailVO paramWrapperLedger_DetailVO = new Ledger_DetailVO();
            paramWrapperLedger_DetailVO.setJs_project(requestPayment_invoiceVO.getJs_project());
            paramWrapperLedger_DetailVO.setJs_no(requestPayment_invoiceVO.getJs_no());
            Ledger_DetailVO updateLedger_DetailVO = new Ledger_DetailVO();
            updateLedger_DetailVO.setInvoice_no(requestPayment_invoiceVO.getInvoice_no());
            updateLedger_DetailVO.setCarrier_invoice(LedgerEnum.INVOICE_FLAG_Y.getCode());
            EntityWrapper LedgerWrapper = new EntityWrapper();
            LedgerWrapper.setEntity(paramWrapperLedger_DetailVO);
            ledger_DetailDao.update(updateLedger_DetailVO,LedgerWrapper);*/
        }else {
            Payment_invoiceVO paramPayment_invoiceVO = new Payment_invoiceVO();
            paramPayment_invoiceVO.setSheet_no(requestPayment_invoiceVO.getSheet_no());
            Payment_invoiceVO dataPayment_invoiceVO = payment_invoiceDao.selectOne(paramPayment_invoiceVO);
            if (dataPayment_invoiceVO == null) {
                return ResultVO.failResult("该发票号数据异常,请联系管理员" + requestPayment_invoiceVO.getInvoice_no());
            } else if (!InvoiceEnum.NORMAL.getCode().equals(dataPayment_invoiceVO.getState())) {
                return ResultVO.failResult("只允许正常状态下的发票进行修改操作");
            }
            requestPayment_invoiceVO.setId(dataPayment_invoiceVO.getId());
            requestPayment_invoiceVO.setState(null);
            requestPayment_invoiceVO.setCreate_by(null);
            requestPayment_invoiceVO.setCreate_date(null);
            payment_invoiceDao.updateById(requestPayment_invoiceVO);
            requestPayment_invoiceVO.setType(dataPayment_invoiceVO.getType());
            requestPayment_invoiceVO.setVehicle_project(dataPayment_invoiceVO.getVehicle_project());
            requestPayment_invoiceVO.setCreate_date(dataPayment_invoiceVO.getCreate_date());
            /**
             * 注意：明细总金额不能超过主表金额
             * */
            BigDecimal fp_tax_total = new BigDecimal(0);
            for (Payment_invoice_DetailVO vo:requestPayment_invoice_detailVOList) {
                if (StringUtils.isBlank(vo.getInvoice_no())) {
                    return ResultVO.failResult("明细发票号不能为空");
                }
                if (vo.getTax_total()==null) {
                    return ResultVO.failResult("明细含税金额不能为空");
                }
                fp_tax_total = fp_tax_total.add(vo.getTax_total());
            }
            if (requestPayment_invoiceVO.getTax_total().compareTo(fp_tax_total) == -1) {
                return ResultVO.failResult("明细总额不能大于发票实际含税金额");
            }
            EntityWrapper payment_invoice_detailEntityWrapper = new EntityWrapper();
            Payment_invoice_DetailVO payment_invoice_DetailVO = new Payment_invoice_DetailVO();
            payment_invoice_DetailVO.setSheet_no(requestPayment_invoiceVO.getSheet_no());
            payment_invoice_detailEntityWrapper.setEntity(payment_invoice_DetailVO);
            List<Payment_invoice_DetailVO> dataPayment_invoice_DetailVOList = payment_invoice_DetailDao.selectList(payment_invoice_detailEntityWrapper);
            //如果老数据里不存在新的数据List，就删除，存在就新增
            List<Payment_invoice_DetailVO> deletePayment_invoice_DetailVOList = new ArrayList<Payment_invoice_DetailVO>();
            List<Payment_invoice_DetailVO> updatePayment_invoice_DetailVOList = new ArrayList<Payment_invoice_DetailVO>();
            for(Payment_invoice_DetailVO dataPayment_invoice_DetailVO: dataPayment_invoice_DetailVOList){
                boolean bool = false;
                for(Payment_invoice_DetailVO requestPayment_invoice_DetailVO: requestPayment_invoice_detailVOList){
                    if(dataPayment_invoice_DetailVO.getId().equals(requestPayment_invoice_DetailVO.getId())){
                        updatePayment_invoice_DetailVOList.add(requestPayment_invoice_DetailVO);
                        bool = true;
                        break;
                    }
                }
                if(!bool){
                    deletePayment_invoice_DetailVOList.add(dataPayment_invoice_DetailVO);
                }
            }
            for(Payment_invoice_DetailVO deleteVO: deletePayment_invoice_DetailVOList){
                payment_invoice_DetailDao.deleteById(deleteVO.getId());
            }
            for(Payment_invoice_DetailVO updateVO: updatePayment_invoice_DetailVOList){
                //计算不含税金额和税额
                updateVO.setNtax_total(updateVO.getTax_total().divide(updateVO.getTax_rate().add(new BigDecimal(1)),2,BigDecimal.ROUND_HALF_UP));
                updateVO.setTax_amount(updateVO.getTax_total().subtract(updateVO.getNtax_total()));
                payment_invoice_DetailDao.updateById(updateVO);
            }
        }
        for(Payment_invoice_DetailVO vo: requestPayment_invoice_detailVOList){
            if(vo.getId() == null || vo.getId() == 0){
                //计算不含税金额和税额
                vo.setNtax_total(vo.getTax_total().divide(vo.getTax_rate().add(new BigDecimal(1)),2,BigDecimal.ROUND_HALF_UP));
                vo.setTax_amount(vo.getTax_total().subtract(vo.getNtax_total()));
                vo.setSheet_no(requestPayment_invoiceVO.getSheet_no());
                payment_invoice_DetailDao.insert(vo);
            }
        }
        /**回写对下结算数据*/
        updateJsState(requestPayment_invoiceVO,"create",requestPayment_invoiceVO.getType());
        return ResultVO.successResult("保存成功");
    }

    @Override
    public ResultVO cancel(Payment_invoiceVO payment_invoiceVO, SysUserVO sysUserVO) {
        if(StringUtils.isBlank(payment_invoiceVO.getSheet_no())){
            return ResultVO.failResult("单号不能为空");
        }
        Payment_invoiceVO paramPayment_invoiceVO = new Payment_invoiceVO();
        paramPayment_invoiceVO.setSheet_no(payment_invoiceVO.getSheet_no());
        Payment_invoiceVO dataPayment_invoiceVO = payment_invoiceDao.selectOne(paramPayment_invoiceVO);
        if(dataPayment_invoiceVO == null){
            return ResultVO.failResult("发票不存在");
        }else if(!InvoiceEnum.NORMAL.getCode().equals(dataPayment_invoiceVO.getState())){
            return ResultVO.failResult("只允许正常状态下的发票进行注销操作");
        }
        payment_invoiceVO.setState(InvoiceEnum.CANCEL.getCode());
        payment_invoiceVO.setJs_no("");
        payment_invoiceVO.setJs_project("");
        payment_invoiceVO.setId(dataPayment_invoiceVO.getId());
        payment_invoiceDao.updateById(payment_invoiceVO);

        //注销完后，需要回写台账的承运商开票字段
    /*    Ledger_DetailVO paramWrapperLedger_DetailVO = new Ledger_DetailVO();
        paramWrapperLedger_DetailVO.setJs_project(payment_invoiceVO.getJs_project());
        Ledger_DetailVO updateLedger_DetailVO = new Ledger_DetailVO();
        updateLedger_DetailVO.setCarrier_invoice(LedgerEnum.INVOICE_FLAG_N.getCode());
        updateLedger_DetailVO.setInvoice_no("");
        EntityWrapper LedgerWrapper = new EntityWrapper();
        LedgerWrapper.setEntity(paramWrapperLedger_DetailVO);
        ledger_DetailDao.update(updateLedger_DetailVO,LedgerWrapper);*/
        //回写结算表数据
        updateJsState(dataPayment_invoiceVO,"cancel",dataPayment_invoiceVO.getType());
        return ResultVO.successResult("注销成功");
    }

    /***
     *
     * @param payment_invoiceVO
     * @param kind --区分是新增/保存or注销
     * @param type --区分是正常or补差
     */
    private void updateJsState(Payment_invoiceVO payment_invoiceVO,String kind,String type) {
        /**更改结算数据*/
        String project = payment_invoiceVO.getVehicle_project();//车辆项目
        String js_state = kind.equals("create")?Js_StateEnum.DOWN_CREATE_FP.getCode():Js_StateEnum.DOWN_CREATE_TZ.getCode();
        String bc_state = kind.equals("create")?"2":"1";
        String invoice_no=kind.equals("create")?payment_invoiceVO.getInvoice_no():"";
        Date invoice_date = kind.equals("create")?payment_invoiceVO.getCreate_date():null;
        if (type.equals("0")) {
            /***回写对下结算表,判断商品车和非商品车*/
            if (StringUtils.equals(BusinessData_projectEnum.COMMODITY_CAR.getCode(),project)) {
                //商品车
                Js_Vin_Down_AmountVO updateJs_Vin_Down_AmountVO = new Js_Vin_Down_AmountVO();
                updateJs_Vin_Down_AmountVO.setJs_no(payment_invoiceVO.getJs_no());
                updateJs_Vin_Down_AmountVO.setJs_batch(payment_invoiceVO.getJs_project());
                updateJs_Vin_Down_AmountVO.setCarrier_no(payment_invoiceVO.getCarrier_no());
                updateJs_Vin_Down_AmountVO.setJs_state(js_state);
                updateJs_Vin_Down_AmountVO.setInvoice_no(invoice_no);
                updateJs_Vin_Down_AmountVO.setInvoice_date(invoice_date);
                js_Vin_Down_AmountDao.invoice_update(updateJs_Vin_Down_AmountVO);
            } else {
                //非商品车
                Js_Non_Down_VehicleVO paramJs_Non_Down_VehicleVO = new Js_Non_Down_VehicleVO();
                paramJs_Non_Down_VehicleVO.setJs_no(payment_invoiceVO.getJs_no());
                paramJs_Non_Down_VehicleVO.setJs_batch(payment_invoiceVO.getJs_project());
                paramJs_Non_Down_VehicleVO.setCarrier_no(payment_invoiceVO.getCarrier_no());
                paramJs_Non_Down_VehicleVO.setJs_state(js_state);
                paramJs_Non_Down_VehicleVO.setInvoice_no(invoice_no);
                paramJs_Non_Down_VehicleVO.setInvoice_date(invoice_date);
                js_Non_Down_VehicleDao.update(paramJs_Non_Down_VehicleVO);
            }
        } else {
            //回写补差表
            Js_CompensationVO updateJs_CompensationVO = new Js_CompensationVO();
            updateJs_CompensationVO.setJs_no(payment_invoiceVO.getJs_no());
            updateJs_CompensationVO.setState(CompensationEnum.COMPENSATION_INVOICE.getCode());
            updateJs_CompensationVO.setJs_batch(payment_invoiceVO.getJs_project());
            updateJs_CompensationVO.setCarrier_no(payment_invoiceVO.getCarrier_no());
            updateJs_CompensationVO.setInvoice_flag(kind.equals("CREATE")?"Y":"N");
            updateJs_CompensationVO.setInvoice_no(invoice_no);
            updateJs_CompensationVO.setInvoice_date(invoice_date);
            js_CompensationDao.invoice_update(updateJs_CompensationVO);
        }
    }

    @Override
    public Payment_invoiceDTO getDetail(Payment_invoiceVO payment_invoiceVO) {

        Payment_invoiceVO paramPayment_invoiceVO = new Payment_invoiceVO();
        paramPayment_invoiceVO.setSheet_no(payment_invoiceVO.getSheet_no());
        Payment_invoiceVO dataPayment_invoiceVO = payment_invoiceDao.selectOne(paramPayment_invoiceVO);
        if(dataPayment_invoiceVO == null){
            return null;
        }
        EntityWrapper payment_invoice_detailEntityWrapper = new EntityWrapper();
        Payment_invoice_DetailVO paramPayment_invoice_DetailVO = new Payment_invoice_DetailVO();
        paramPayment_invoice_DetailVO.setSheet_no(payment_invoiceVO.getSheet_no());
        payment_invoice_detailEntityWrapper.setEntity(paramPayment_invoice_DetailVO);
        payment_invoice_detailEntityWrapper.orderBy("id", true);
        List<Payment_invoice_DetailVO> dataPayment_invoice_DetailVOList = payment_invoice_DetailDao.selectList(payment_invoice_detailEntityWrapper);
        Payment_invoiceDTO returnPayment_invoiceDTO = new Payment_invoiceDTO();
        returnPayment_invoiceDTO.setPayment_invoiceVO(dataPayment_invoiceVO);
        returnPayment_invoiceDTO.setPayment_invoice_detailVOList(dataPayment_invoice_DetailVOList);
        return returnPayment_invoiceDTO;
    }

    /***
     * 生成付款发票
     * @param vos
     * @param sysUserVO
     * @param type
     * @return
     */
    @Override
    public ResultVO createInvoice(List<Payment_invoiceVO> vos, SysUserVO sysUserVO, String type) {
        /***
         * 生成付款发票，以对下结算数据为准
         * type=0,正常对下结算发票
         * type=1,补差的发票
         * */
        try {
            for (Payment_invoiceVO requestPayment_invoiceVO:vos) {
                /**1、根据结算号、承运商和类型type,抓取数据*/
                //判断是否已经开票，一般来说，js_no+js_project+type唯一
                Payment_invoiceVO paramPayment_invoiceVO = new Payment_invoiceVO();
                paramPayment_invoiceVO.setJs_no(requestPayment_invoiceVO.getJs_no());
                paramPayment_invoiceVO.setJs_project(requestPayment_invoiceVO.getJs_project());
                paramPayment_invoiceVO.setCarrier_no(requestPayment_invoiceVO.getCarrier_no());
                paramPayment_invoiceVO.setType(type);
                //一个结算号理论上最多可以生成两次发票，因为存在补差情况，所以加上类型判断
                Payment_invoiceVO dataPayment_invoiceVO = payment_invoiceDao.selectOne(paramPayment_invoiceVO);
                if (dataPayment_invoiceVO!=null) {
                    throw new RuntimeException("该结算号已经生成付款发票，结算号："+requestPayment_invoiceVO.getJs_no()+"类型："+type);
                }
                JqGridParamModel jqGridParamModel = new JqGridParamModel();
                jqGridParamModel.put("js_no",requestPayment_invoiceVO.getJs_no());
                jqGridParamModel.put("js_project",requestPayment_invoiceVO.getJs_project());
                jqGridParamModel.put("carrier_no",requestPayment_invoiceVO.getCarrier_no());
                jqGridParamModel.put("vehicle_project",requestPayment_invoiceVO.getVehicle_project());
                String filters = JqGridSearchParamHandler.processSql("", jqGridParamModel);
                jqGridParamModel.setFilters(filters);
                List<Payment_invoiceVO> list = new ArrayList<>();
                /**获取数据*/
                if (type.equals("0")) { //正常
                    list  = payment_invoiceDao.findBeforListForGridByReceiptDate(jqGridParamModel);
                } else {//补差
                    list  = payment_invoiceDao.findBeforCompensationListForGrid(jqGridParamModel);
                }
                /**2、拿到数据，插入到付款发票表*/
                if (list.size()==0) {
                    throw new RuntimeException("该数据存在异常，请联系管理员，结算号："+requestPayment_invoiceVO.getJs_no());
                }
                for (Payment_invoiceVO insertPayment_invoiceVO:list) {
                    String sheet_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.JS_PAYMENT_INVOICE);
                    insertPayment_invoiceVO.setState(InvoiceEnum.NORMAL.getCode());
                    insertPayment_invoiceVO.setSheet_no(sheet_no);
                    insertPayment_invoiceVO.setType(type);
                    insertPayment_invoiceVO.setCreate_by(sysUserVO.getRealName());
                    insertPayment_invoiceVO.setTax_amount(insertPayment_invoiceVO.getTax_total().subtract(insertPayment_invoiceVO.getNtax_total()));
                    payment_invoiceDao.insert(insertPayment_invoiceVO);
                }
                /**3、插入数据后，回写数据*/
                //回写结算表数据
                updateJsState(requestPayment_invoiceVO,"create",type);
            }
        }
        catch (Exception e) {
            throw new RuntimeException("生成发票失败，请联系管理员，"+e.getMessage());
        }
        return ResultVO.successResult("生成发票成功");
    }

    @Override
    public ResultVO check(List<Payment_invoiceVO> vos, SysUserVO sysUserVO) {
        /**付款发票审核
         * 1、修改发票状态
         * 2、数据推送至付款计划表
         * */
        try {
            for (Payment_invoiceVO requestPayment_invoiceVO:vos) {
                //1 验证
                Payment_invoiceVO paramPayment_invoiceVO = new Payment_invoiceVO();
                paramPayment_invoiceVO.setSheet_no(requestPayment_invoiceVO.getSheet_no());
                Payment_invoiceVO dataPayment_invoiceVO = payment_invoiceDao.selectOne(paramPayment_invoiceVO);
                if (dataPayment_invoiceVO==null) {
                    throw new RuntimeException("该发票数据存在异常，单号："+requestPayment_invoiceVO.getSheet_no());
                } else if (StringUtils.isBlank(dataPayment_invoiceVO.getInvoice_no())) {
                    throw new RuntimeException("该数据发票号不存在，单号："+requestPayment_invoiceVO.getSheet_no());
                } else if (StringUtils.notEquals(InvoiceEnum.NORMAL.getCode(),dataPayment_invoiceVO.getState())) {
                    throw new RuntimeException("非‘正常’状态无法进行此操作，单号："+requestPayment_invoiceVO.getSheet_no());
                }
                //2 修改状态
                Payment_invoiceVO updatePayment_invoiceVO = new Payment_invoiceVO();
                updatePayment_invoiceVO.setState(InvoiceEnum.PAY_CHECK.getCode());
                updatePayment_invoiceVO.setCheck_by(sysUserVO.getRealName());
                updatePayment_invoiceVO.setCheck_date(new Date());
                EntityWrapper payEntityWrapper = new EntityWrapper();
                payEntityWrapper.setEntity(paramPayment_invoiceVO);
                payment_invoiceDao.update(updatePayment_invoiceVO,payEntityWrapper);

                //3插入到付款计划
                //三种情况：商品车-非商品车-补差
                List<Tr_Payment_PlanVO> paymentList = new ArrayList<>();
                if (StringUtils.equals(dataPayment_invoiceVO.getType(),"1")) { //补差，不需要判断VEHICLE_PROJECT
                    paymentList = payment_invoiceDao.selectCompensationPayMentList(dataPayment_invoiceVO);
                } else if (StringUtils.equals(dataPayment_invoiceVO.getVehicle_project(),"1")) { //商品车
                    paymentList = payment_invoiceDao.selectPayMentList(dataPayment_invoiceVO);
                } else if (StringUtils.equals(dataPayment_invoiceVO.getVehicle_project(),"2")) { //非商品车
                    paymentList = payment_invoiceDao.selectNonPayMentList(dataPayment_invoiceVO);
                } else {

                }
                for (Tr_Payment_PlanVO insertVO:paymentList) {
                    insertVO.setPay_apply("N");
                    tr_Payment_PlanDao.insert(insertVO);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("审核失败，请联系管理员，"+e.getMessage());
        }
        return ResultVO.successResult("审核发票成功");
    }

}
