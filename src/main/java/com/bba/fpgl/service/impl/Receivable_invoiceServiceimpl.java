package com.bba.fpgl.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.common.Sys_sheetidUtil;
import com.bba.common.constant.BusinessData_projectEnum;
import com.bba.common.constant.InvoiceEnum;
import com.bba.common.constant.Js_StateEnum;
import com.bba.common.constant.LedgerEnum;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.fpgl.dao.IReceivable_invoiceDao;
import com.bba.fpgl.service.api.IReceivable_invoiceService;
import com.bba.fpgl.vo.Receivable_invoiceVO;
import com.bba.nosettlement.vo.Js_Non_VehicleVO;
import com.bba.settlement.dao.Js_Vin_AmountDao;
import com.bba.settlement.vo.Js_Vin_AmountVO;
import com.bba.tzgl.dao.AccountsToLedgerDao;
import com.bba.tzgl.dao.Js_CompensationDao;
import com.bba.tzgl.dao.LedgerDao;
import com.bba.tzgl.vo.Js_CompensationVO;
import com.bba.tzgl.vo.LedgerVO;
import com.bba.util.*;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:26
 */
@Service
@Transactional
public class Receivable_invoiceServiceimpl extends ServiceImpl<IReceivable_invoiceDao,Receivable_invoiceVO> implements IReceivable_invoiceService {

    @Resource
    private IReceivable_invoiceDao receivable_invoiceDao;

    @Resource
    private LedgerDao ledgerDao;

    @Resource
    private Js_Vin_AmountDao js_Vin_AmountDao;

    @Resource
    private AccountsToLedgerDao accountsToLedgerDao;

    @Resource
    private Js_CompensationDao js_CompensationDao;

    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Receivable_invoiceVO> list = receivable_invoiceDao.findListForGrid(jqGridParamModel);
        int count = receivable_invoiceDao.findListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }

    @Override
    public ResultVO saveDetail(Receivable_invoiceVO requestReceivable_invoiceVO, SysUserVO sysUserVO) {
        /*if(StringUtils.isBlank(requestReceivable_invoiceVO.getCus_tax_no())){
            return ResultVO.failResult("购方税号不能为空");
        }*/
        if(StringUtils.isBlank(requestReceivable_invoiceVO.getJs_project())){
            return ResultVO.failResult("结算批次不能为空");
        }
        requestReceivable_invoiceVO.setState(InvoiceEnum.NORMAL.getCode());
        if(requestReceivable_invoiceVO.getId()==0){
            //新增发票功能关闭，只允许从台账列表生成
            //判断结算批次是否被占用
           /* LedgerVO paramLedgerVO = new LedgerVO();
            paramLedgerVO.setJs_project(requestReceivable_invoiceVO.getJs_project());
            paramLedgerVO.setJs_no(requestReceivable_invoiceVO.getJs_no());
            paramLedgerVO.setCus_invoice(LedgerEnum.INVOICE_FLAG_N.getCode());
            LedgerVO dataLedgerVO = ledgerDao.selectOne(paramLedgerVO);
            if (dataLedgerVO==null) {
                return ResultVO.failResult("该结算号已开票或不存在，请核实数据");
            }*/
            if (StringUtils.isNotBlank(requestReceivable_invoiceVO.getInvoice_no())) {
                Receivable_invoiceVO paramReceivable_invoiceVO = new Receivable_invoiceVO();
                paramReceivable_invoiceVO.setInvoice_no(requestReceivable_invoiceVO.getInvoice_no());
                paramReceivable_invoiceVO.setState(InvoiceEnum.NORMAL.getCode());
                Receivable_invoiceVO dataReceivable_invoiceVO = receivable_invoiceDao.selectOne(paramReceivable_invoiceVO);
                if(dataReceivable_invoiceVO != null){
                    return ResultVO.failResult("发票号已存在,请勿重复录入"+requestReceivable_invoiceVO.getSheet_no());
                }
            }
            String sheet_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.JS_RECEIVABLE_INVOICE);
            requestReceivable_invoiceVO.setSheet_no(sheet_no);
            requestReceivable_invoiceVO.setCreate_by(sysUserVO.getRealName());
            requestReceivable_invoiceVO.setState(InvoiceEnum.NORMAL.getCode());
            requestReceivable_invoiceVO.setProject(requestReceivable_invoiceVO.getProject());
            receivable_invoiceDao.insert(requestReceivable_invoiceVO);
        } else {
            Receivable_invoiceVO paramReceivable_invoiceVO = new Receivable_invoiceVO();
            paramReceivable_invoiceVO.setSheet_no(requestReceivable_invoiceVO.getSheet_no());
            Receivable_invoiceVO dataReceivable_invoiceVO = receivable_invoiceDao.selectOne(paramReceivable_invoiceVO);
            if (dataReceivable_invoiceVO == null) {
                return ResultVO.failResult("该单号数据异常,请联系管理员" + requestReceivable_invoiceVO.getSheet_no());
            }
            if (!InvoiceEnum.NORMAL.getCode().equals(dataReceivable_invoiceVO.getState())) {
                return ResultVO.failResult("只允许正常状态下的发票进行修改操作");
            }
            requestReceivable_invoiceVO.setId(dataReceivable_invoiceVO.getId());
            requestReceivable_invoiceVO.setCreate_by(null);
            receivable_invoiceDao.updateById(requestReceivable_invoiceVO);
            requestReceivable_invoiceVO.setType(dataReceivable_invoiceVO.getType());
        }
        //发票修改后回写数据
        invoice_update(requestReceivable_invoiceVO,"CREATE");
        return ResultVO.successResult("保存成功");
    }

    @Override
    public ResultVO cancel(Receivable_invoiceVO requestReceivable_invoiceVO, SysUserVO sysUserVO) {
        if(StringUtils.isBlank(requestReceivable_invoiceVO.getSheet_no())){
            return ResultVO.failResult("单号不能为空");
        }
        Receivable_invoiceVO paramReceivable_invoiceVO = new Receivable_invoiceVO();
        paramReceivable_invoiceVO.setSheet_no(requestReceivable_invoiceVO.getSheet_no());
        Receivable_invoiceVO dataReceivable_invoiceVO = receivable_invoiceDao.selectOne(paramReceivable_invoiceVO);
        if(dataReceivable_invoiceVO == null){
            return ResultVO.failResult("单号不存在");
        }else if(!InvoiceEnum.NORMAL.getCode().equals(dataReceivable_invoiceVO.getState())){
            return ResultVO.failResult("只允许正常状态下的发票进行注销操作");
        }
        dataReceivable_invoiceVO.setState(InvoiceEnum.CANCEL.getCode());
        receivable_invoiceDao.updateById(dataReceivable_invoiceVO);
     /*   LedgerVO paramWrapperLedgerVO = new LedgerVO();
        paramWrapperLedgerVO.setJs_project(dataReceivable_invoiceVO.getJs_project());
        paramWrapperLedgerVO.setJs_no(dataReceivable_invoiceVO.getJs_no());
        LedgerVO updateLedgerVO = new LedgerVO();
        updateLedgerVO.setCus_invoice(LedgerEnum.INVOICE_FLAG_N.getCode());
        updateLedgerVO.setInvoice_no("");
        EntityWrapper LedgerWrapper = new EntityWrapper();
        LedgerWrapper.setEntity(paramWrapperLedgerVO);
        ledgerDao.update(updateLedgerVO,LedgerWrapper);*/
        //回写台账、对上结算数据
        invoice_update(dataReceivable_invoiceVO,"CANCEL");
        return ResultVO.successResult("注销成功");
    }

    @Override
    public Receivable_invoiceVO getDetail(Receivable_invoiceVO receivable_invoiceVO) {
        Receivable_invoiceVO paramReceivable_invoiceVO = new Receivable_invoiceVO();
        paramReceivable_invoiceVO.setSheet_no(receivable_invoiceVO.getSheet_no());
        Receivable_invoiceVO dataReceivable_invoiceVO = receivable_invoiceDao.selectOne(paramReceivable_invoiceVO);
        if(dataReceivable_invoiceVO == null){
            return null;
        }
        return dataReceivable_invoiceVO;
    }

    @Override
    public ResultVO importData(MultipartFile file) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        List<Receivable_invoiceVO> requestReceivable_invoiceVOList = null;
        try {
            requestReceivable_invoiceVOList = ExcelImportUtil.importExcel(file.getInputStream(), Receivable_invoiceVO.class, new ImportParams());
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> msgList = verificationDetail(requestReceivable_invoiceVOList);
        if(msgList.size() > 0){
            return ResultVO.failResult(ArrayUtils.join(msgList, "<br/>"));
        }
        for (Receivable_invoiceVO requestReceivable_invoiceVO: requestReceivable_invoiceVOList) {
            //导入修改
            Receivable_invoiceVO paramReceivable_invoiceVO = new Receivable_invoiceVO();
            paramReceivable_invoiceVO.setSheet_no(requestReceivable_invoiceVO.getSheet_no());
            Receivable_invoiceVO dataReceivable_invoiceVO = receivable_invoiceDao.selectOne(paramReceivable_invoiceVO);
            if (dataReceivable_invoiceVO==null) {
                throw new RuntimeException("发票单号不存在，请联系管理员，单号："+requestReceivable_invoiceVO.getSheet_no());
            }
            EntityWrapper Receivable_invoiceWrapper = new EntityWrapper();
            Receivable_invoiceWrapper.setEntity(paramReceivable_invoiceVO);
            Receivable_invoiceVO updateReceivable_invoiceVO = requestReceivable_invoiceVO;
            updateReceivable_invoiceVO.setSheet_no(null);
            receivable_invoiceDao.update(updateReceivable_invoiceVO,Receivable_invoiceWrapper);
            //发票修改后回写数据
            requestReceivable_invoiceVO.setJs_no(dataReceivable_invoiceVO.getJs_no());
            requestReceivable_invoiceVO.setJs_project(dataReceivable_invoiceVO.getJs_project());
            requestReceivable_invoiceVO.setType(dataReceivable_invoiceVO.getType());
            invoice_update(requestReceivable_invoiceVO,"CREATE");
        }
        return ResultVO.successResult("导入修改成功");
    }

    private List<String> verificationDetail(List<Receivable_invoiceVO> requestReceivable_invoiceVOList) {
        List<String> msgList = new ArrayList<String>();
        for (int i = 0, line = 1; i < requestReceivable_invoiceVOList.size(); i++, line++) {
            Receivable_invoiceVO requestReceivable_invoiceVO = requestReceivable_invoiceVOList.get(i);
            if (StringUtils.isBlank(requestReceivable_invoiceVO.getDoc_number())) {
                msgList.add("第" + line + "行,单据号不能为空");
            }
            if (StringUtils.isBlank(requestReceivable_invoiceVO.getInvoice_no())) {
                msgList.add("第" + line + "行,发票号不能为空");
            }
            if (requestReceivable_invoiceVO.getInvoice_date()==null) {
                msgList.add("第" + line + "行,开票日期不能为空");
            }
        }
        return msgList;
    }

    private void invoice_update (Receivable_invoiceVO requestReceivable_invoiceVO,String kind) {
        /**
         * kind:create 新增/保存 ;cancel注销
         * */
        //发票完生成后修改台账状态
        LedgerVO paramWrapperLedgerVO = new LedgerVO();
        paramWrapperLedgerVO.setJs_project(requestReceivable_invoiceVO.getJs_project());
        paramWrapperLedgerVO.setJs_no(requestReceivable_invoiceVO.getJs_no());
        LedgerVO updateLedgerVO = new LedgerVO();
        updateLedgerVO.setInvoice_no(kind.equals("CREATE")?requestReceivable_invoiceVO.getInvoice_no():"");//发票号
        updateLedgerVO.setCus_invoice(kind.equals("CREATE")?LedgerEnum.INVOICE_FLAG_Y.getCode():LedgerEnum.INVOICE_FLAG_N.getCode());
        EntityWrapper LedgerWrapper = new EntityWrapper();
        LedgerWrapper.setEntity(paramWrapperLedgerVO);
        ledgerDao.update(updateLedgerVO,LedgerWrapper);
        String ledger_type = requestReceivable_invoiceVO.getType();//类型0正常 1 补差
        if (StringUtils.equals(requestReceivable_invoiceVO.getProject(), BusinessData_projectEnum.COMMODITY_CAR.getCode()) && StringUtils.equals(ledger_type,LedgerEnum.NORMAL_LEDGER.getCode())) {
            ////商品车台账，发票完成后，回写结算数据，根据结算批次（结算号）
            Js_Vin_AmountVO updateJs_Vin_AmountVO = new Js_Vin_AmountVO();
            updateJs_Vin_AmountVO.setJs_batch(requestReceivable_invoiceVO.getJs_project());
            updateJs_Vin_AmountVO.setJs_no(requestReceivable_invoiceVO.getJs_no());
            //由于开票单位是CMAS,回写时要替换成CMA,否则无法回写到数据
            if (StringUtils.equals("CMAS",requestReceivable_invoiceVO.getCus_no())) {
                updateJs_Vin_AmountVO.setCus_no("CMA");
            }
            updateJs_Vin_AmountVO.setInvoice_no(kind.equals("CREATE")?requestReceivable_invoiceVO.getInvoice_no():"");
            updateJs_Vin_AmountVO.setInvoice_date(kind.equals("CREATE")?requestReceivable_invoiceVO.getInvoice_date():null);
            updateJs_Vin_AmountVO.setJs_state(kind.equals("CREATE")?Js_StateEnum.CREATE_FP.getCode():Js_StateEnum.CREATE_TZ.getCode());
            js_Vin_AmountDao.invoice_update(updateJs_Vin_AmountVO);
        } else if(StringUtils.equals(requestReceivable_invoiceVO.getProject(),BusinessData_projectEnum.NON_COMMODITY_CAR.getCode()) && StringUtils.equals(ledger_type,LedgerEnum.NORMAL_LEDGER.getCode())){//非商品车
            //非商品车
            Js_Non_VehicleVO updateJs_Non_VehicleVO = new Js_Non_VehicleVO();
            updateJs_Non_VehicleVO.setJs_no(requestReceivable_invoiceVO.getJs_no());
            updateJs_Non_VehicleVO.setJs_batch(requestReceivable_invoiceVO.getJs_project());
            updateJs_Non_VehicleVO.setCus_no(requestReceivable_invoiceVO.getCus_no());
            updateJs_Non_VehicleVO.setInvoice_no(kind.equals("CREATE")?requestReceivable_invoiceVO.getInvoice_no():"");
            updateJs_Non_VehicleVO.setInvoice_date(kind.equals("CREATE")?requestReceivable_invoiceVO.getInvoice_date():null);
            updateJs_Non_VehicleVO.setJs_state(kind.equals("CREATE")?Js_StateEnum.CREATE_FP.getCode():Js_StateEnum.CREATE_TZ.getCode());
            accountsToLedgerDao.updateUpNonJsVehicle(updateJs_Non_VehicleVO);
        } else {
            //补差台账，前面已做判断，只能是对上补差，修改补差数据
            Js_CompensationVO updateJs_CompensationVO = new Js_CompensationVO();
            updateJs_CompensationVO.setJs_no(requestReceivable_invoiceVO.getJs_no());
            updateJs_CompensationVO.setJs_batch(requestReceivable_invoiceVO.getJs_project());
            updateJs_CompensationVO.setCus_no(requestReceivable_invoiceVO.getCus_no());
            updateJs_CompensationVO.setInvoice_flag(kind.equals("CREATE")?"Y":"N");
            updateJs_CompensationVO.setInvoice_no(kind.equals("CREATE")?requestReceivable_invoiceVO.getInvoice_no():"");
            updateJs_CompensationVO.setInvoice_date(kind.equals("CREATE")?requestReceivable_invoiceVO.getInvoice_date():null);
            js_CompensationDao.invoice_update(updateJs_CompensationVO);
        }
    }
}
