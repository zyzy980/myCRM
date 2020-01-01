package com.bba.nosettlement.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.bba.common.constant.NOSETTLEMENT_STATE_Enum;
import com.bba.common.constant.NOSETTLEMENT_TAB_Enum;
import com.bba.common.service.impl.BaseService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.ht.service.api.INon_Ht_CusService;
import com.bba.ht.service.api.INon_Ht_Cus_FreightService;
import com.bba.ht.vo.Non_Ht_CusVO;
import com.bba.ht.vo.Non_Ht_Cus_FreightVO;
import com.bba.jcda.service.api.IJs_Tax_RateService;
import com.bba.jcda.vo.Js_Tax_RateVO;
import com.bba.jcda.vo.Tr_statistical_rulesVO;
import com.bba.nosettlement.dao.IJs_Non_VehicleDao;
import com.bba.nosettlement.service.api.IJs_Non_VehicleService;
import com.bba.nosettlement.vo.Js_Dz_Non_SheetVO;
import com.bba.nosettlement.vo.Js_Non_VehicleVO;
import com.bba.nosettlement.vo.NonVehicleTotalVO;
import com.bba.settlement.dao.Js_Vin_AmountDao;
import com.bba.util.*;
import com.bba.xtgl.service.api.ISysSheetIdService;
import com.bba.xtgl.vo.SysSheetIdVO;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/*
 * Author:LTJ
 * Date:2019-07-31
 * Description:3.3.3.非商品车对上结算操作
 * */
@Service
public class Js_Non_VehicleService extends BaseService implements IJs_Non_VehicleService {

    @Resource
    private IJs_Non_VehicleDao iJs_Non_VehicleDao;
    @Autowired
    private ISysSheetIdService iSysSheetIdService;
    @Autowired
    private Js_Vin_AmountDao Js_Vin_AmountDao;

    @Autowired
    private INon_Ht_CusService ht_cusService;
    @Autowired
    private INon_Ht_Cus_FreightService ht_cus_freightService;

    @Autowired
    private IJs_Tax_RateService iJs_Tax_RateService;

    /**
     * 查询
     * */
    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters)
    {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Js_Non_VehicleVO> list = iJs_Non_VehicleDao.getListForGrid(jqGridParamModel);
        int records = iJs_Non_VehicleDao.getListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
    }





    /**
     * 非商品车 - 结算
     * */
    @Transactional
    @Override
    public ResultVO settlement(List<Js_Non_VehicleVO> list)
    {
        // 属性	ATTRIBUTE	0 体系内，1 体系外		varchar2(40)		FALSE	FALSE
        // TYPE	TYPE	0 VIP, 1 保密，2 合同价	3 救援车	varchar2(40)		FALSE	FALSE
        if(null==list || list.size()<=0)
        {
            return ResultVO.failResult("结算失败，没有选择列表数据。");
        }

        for(Js_Non_VehicleVO item:list)
        {
            if("体系内".equals(item.getAttribute()))
                item.setAttribute("0");
            else if("体系外".equals(item.getAttribute()))
                item.setAttribute("1");
        }
        //数据状态是否正常
        List<Js_Non_VehicleVO> dataList=this.findListByProperty(list);
        if(null==dataList || dataList.size()<=0)
        {
            return ResultVO.failResult("结算失败，选择数据已被其他用户处理，请查询数据后再操作。");
        }
        if(list.size()!=dataList.size())
        {
            return ResultVO.failResult("结算失败，选择数据有部份已被其他用户处理，请查询数据后再操作。");
        }

        List<String> msgList=new ArrayList<String>();
        for(Js_Non_VehicleVO item:dataList)
        {
            if(!NOSETTLEMENT_STATE_Enum.NORMAL.getCode().equals(item.getJs_state()))
            {
                msgList.add(item.getHandover_no());
            }
        }
        if(msgList.size()>0)
        {
            return ResultVO.failResult("结算失败","如下数据不是“"+NOSETTLEMENT_STATE_Enum.NORMAL.getName()+"”状态，交车单号："+ ArrayUtils.join(msgList,";"));
        }
        Integer iRows=0;
        Integer isucc=0;
        Integer ierror=0;
        String errorString="";
        for(Js_Non_VehicleVO item:dataList) {
            //前端有4个界面分别使用 ATTRIBUTE=0 且 type=0,1,2,3 区分
            if (NOSETTLEMENT_TAB_Enum.VIP.getCode().equals(item.getType())) {
                //VIP界面
                //主机厂的VIP需要对上结算，民生VIP对上不收费，不可以结算。
                //区分字段：【客户】固定为“对上不收费”的为民生VIP，则不可以结算，或者结算后费用为0。
                if(!item.getCus_no().equals("对上不收费") && !item.getCus_name().equals("对上不收费")) {
                    ResultVO res=updateVehicleContractData_settlement(item);
                    if(!"0".equals(res.getResultCode()))
                    {
                        errorString+="交车单号 "+item.getHandover_no()+" "+((ResultDataFullMore)res.getResultDataFull()).getSimpleMessage()+"；";
                        ierror++;
                    }
                    else {
                        isucc++;
                    }
                }
            } else if (NOSETTLEMENT_TAB_Enum.SEC.getCode().equals(item.getType())) {
                //保密车
                //保密车，不走合同规则，人工结算后导入系统。
            } else if (NOSETTLEMENT_TAB_Enum.CON.getCode().equals(item.getType())) {
                //合同车
                ResultVO res=updateVehicleContractData_settlement(item);
                if(!"0".equals(res.getResultCode()))
                {
                    errorString+="交车单号 "+item.getHandover_no()+" "+((ResultDataFullMore)res.getResultDataFull()).getSimpleMessage()+"；";
                    ierror++;
                }
                else {
                    isucc++;
                }
            } else if (NOSETTLEMENT_TAB_Enum.SAV.getCode().equals(item.getType())) {
                //救援车
                ResultVO res=updateVehicleContractData_settlement(item);
                if(!"0".equals(res.getResultCode()))
                {
                    errorString+="交车单号 "+item.getHandover_no()+" "+((ResultDataFullMore)res.getResultDataFull()).getSimpleMessage()+"；";
                    ierror++;
                }
                else {
                    isucc++;
                }
            }
        }
        return ResultVO.successResult("结算完成；成功："+isucc.toString()+"条；失败："+ierror.toString()+"条，原因："+errorString);
    }

    private ResultVO updateVehicleContractData_contract(Js_Non_VehicleVO item)
    {
        //匹配合同
        return updateVehicleContractData(item,0);
    }
    private ResultVO updateVehicleContractData_settlement(Js_Non_VehicleVO item)
    {
        //结算
        return updateVehicleContractData(item,1);
    }

    private ResultVO updateVehicleContractData(Js_Non_VehicleVO item,int iKind)
    {
        Js_Non_VehicleVO updateJs_Non_VehicleVO=new Js_Non_VehicleVO();
        updateJs_Non_VehicleVO.setId(item.getId());
        //查找合同号
        //查找合同号对应运输合同规则
        List<Non_Ht_Cus_FreightVO> freightList=iJs_Non_VehicleDao.findContractFreightVO(item);
        if(freightList.size()==0) {
            return ResultVO.failResult("没有找到合同号运费规则");
        } else if (freightList.size()>1){
            return ResultVO.failResult("匹配到多条合同");
        }
        Non_Ht_Cus_FreightVO freightVO = freightList.get(0);
        updateJs_Non_VehicleVO.setContract_no(freightVO.getContract_no());
        //updateJs_Non_VehicleVO.setContract_type(freightVO.getContract_type());
        //取值 - js_tax_rate // LTJ:2019-09-24
        Js_Tax_RateVO rateVO=new Js_Tax_RateVO();
        rateVO.setTax_month(DateUtils.dateFormat(DateUtils.parseDate(item.getBegin_date(),"yyyy-MM-dd"),"yyyy-MM"));
        List<Js_Tax_RateVO> rateList=iJs_Tax_RateService.GetJs_Tax_RateList(rateVO);
        if(null==rateList || rateList.size()<=0)
        {
            return ResultVO.failResult("没有找到税率（"+rateVO.getTax_month()+"）。");
        }
        updateJs_Non_VehicleVO.setTax_rate(rateList.get(0).getTax_rate().toString());
        //updateJs_Non_VehicleVO.setTax_rate(contractVO.getTax_rate().toString());
        if(iKind==0)
        {
            //匹配合同
        }else {
            //结算  -  计算费用
            updateJs_Non_VehicleVO.setNot_tax_premium(freightVO.getPremium().toString()); //不含税保费
            Float not_tax_freight = freightVO.getFirst_mileage().floatValue() * freightVO.getFirst_price().floatValue() + freightVO.getTwo_mileage().floatValue() * freightVO.getTwo_price().floatValue() + freightVO.getThree_mileage().floatValue() * freightVO.getThree_price().floatValue();
            updateJs_Non_VehicleVO.setNot_tax_freight(not_tax_freight.toString());//运费
            updateJs_Non_VehicleVO.setNot_tax_other_amount(freightVO.getCross_sea_amount().toString());//其它费用
            Float not_tax_amount=not_tax_freight+freightVO.getCross_sea_amount().floatValue()+freightVO.getPremium().floatValue();
            Float tax_amount=not_tax_amount+not_tax_amount*Float.valueOf(updateJs_Non_VehicleVO.getTax_rate());
            updateJs_Non_VehicleVO.setNot_tax_amount(not_tax_amount.toString());//不含税合计
            updateJs_Non_VehicleVO.setTax_amount(tax_amount.toString());//含税合计
            Float mil = freightVO.getFirst_mileage().floatValue()+freightVO.getTwo_mileage().floatValue()+freightVO.getThree_mileage().floatValue();
            updateJs_Non_VehicleVO.setMil(mil.toString());
            updateJs_Non_VehicleVO.setJs_state("1");
        }
        this.update(updateJs_Non_VehicleVO);
        return ResultVO.successResult("OK");
    }

    /**
     * 匹配合同
     * */
    @Override
    public ResultVO setcontract_no(List<Js_Non_VehicleVO> dataList)
    {
        Integer iRows=0;
        Integer isucc=0;
        Integer ierror=0;
        String errorString="";
        for(Js_Non_VehicleVO item:dataList) {
            //前端有4个界面分别使用 ATTRIBUTE=0 且 type=0,1,2,3 区分
            if (NOSETTLEMENT_TAB_Enum.VIP.getCode().equals(item.getType())) {
                //VIP界面
                //主机厂的VIP需要对上结算，民生VIP对上不收费，不可以结算。
                //区分字段：【客户】固定为“对上不收费”的为民生VIP，则不可以结算，或者结算后费用为0。
                if(!item.getCus_no().equals("对上不收费") && !item.getCus_name().equals("对上不收费")) {
                    ResultVO res=updateVehicleContractData_contract(item);
                    if(!"0".equals(res.getResultCode()))
                    {
                        errorString+="交车单号 "+item.getHandover_no()+" "+((ResultDataFullMore)res.getResultDataFull()).getSimpleMessage()+"；";
                        ierror++;
                    }
                    else {
                        isucc++;
                    }
                }
            } else if (NOSETTLEMENT_TAB_Enum.SEC.getCode().equals(item.getType())) {
                //保密车
                //问题描述：不匹配，因为保密车是手工结算。
            } else if (NOSETTLEMENT_TAB_Enum.CON.getCode().equals(item.getType())) {
                //合同车
                ResultVO res=updateVehicleContractData_contract(item);
                if(!"0".equals(res.getResultCode()))
                {
                    errorString+="交车单号 "+item.getHandover_no()+" "+((ResultDataFullMore)res.getResultDataFull()).getSimpleMessage()+"；";
                    ierror++;
                }
                else {
                    isucc++;
                }
            } else if (NOSETTLEMENT_TAB_Enum.SAV.getCode().equals(item.getType())) {
                //救援车
                ResultVO res=updateVehicleContractData_contract(item);
                if(!"0".equals(res.getResultCode()))
                {
                    errorString+="交车单号 "+item.getHandover_no()+" "+((ResultDataFullMore)res.getResultDataFull()).getSimpleMessage()+"；";
                    ierror++;
                }
                else {
                    isucc++;
                }
            }
        }
        return ResultVO.successResult("匹配合同完成；成功："+isucc.toString()+"条；失败："+ierror.toString()+"条，原因："+errorString);
    }


    /**
     * 撤回
     * */
    @Transactional
    @Override
    public ResultVO un_settlement(List<Js_Non_VehicleVO> list)
    {
        // 属性	ATTRIBUTE	0 体系内，1 体系外		varchar2(40)		FALSE	FALSE
        // TYPE	TYPE	0 VIP, 1 保密，2 合同价	3 救援车	varchar2(40)		FALSE	FALSE
        if(null==list || list.size()<=0)
        {
            return ResultVO.failResult("撤回失败，没有选择列表数据。");
        }

        //数据状态是否正常
        List<Js_Non_VehicleVO> dataList=this.findListByProperty(list);
        if(null==dataList || dataList.size()<=0)
        {
            return ResultVO.failResult("撤回失败，选择数据已被其他用户处理，请查询数据后再操作。");
        }
        if(list.size()!=dataList.size())
        {
            return ResultVO.failResult("撤回失败，选择数据有部份已被其他用户处理，请查询数据后再操作。");
        }

        List<String> msgList=new ArrayList<String>();
        for(Js_Non_VehicleVO item:dataList)
        {
            if(!NOSETTLEMENT_STATE_Enum.SETTLEMENT.getCode().equals(item.getJs_state()))
            {
                msgList.add(item.getVin());
            }
        }
        if(msgList.size()>0)
        {
            return ResultVO.failResult("撤回失败","如下数据不是“"+NOSETTLEMENT_STATE_Enum.SETTLEMENT.getName()+"”状态，车架号："+ ArrayUtils.join(msgList,";"));
        }

        iJs_Non_VehicleDao.updateUn_Settlement(list);

        return ResultVO.successResult("撤回成功。");
    }

    /*
     * 生成对账单
     * */
    @Transactional
    @Override
    public ResultVO create_bill(List<Js_Non_VehicleVO> list)
    {
        // 属性	ATTRIBUTE	0 体系内，1 体系外		varchar2(40)		FALSE	FALSE
        // TYPE	TYPE	0 VIP, 1 保密，2 合同价	3 救援车	varchar2(40)		FALSE	FALSE
        if(null==list || list.size()<=0)
        {
            return ResultVO.failResult("生成对账单失败，没有选择列表数据。");
        }
    /*    for(Js_Non_VehicleVO item:list)
        {
            if(item.getAttribute().equals("体系内"))
            {
                item.setAttribute("0");
            }
            else if(item.getAttribute().equals("体系外"))
            {
                item.setAttribute("1");
            }
        }*/
        //数据状态是否正常
        List<Js_Non_VehicleVO> dataList=this.findListByProperty(list);
        if(null==dataList || dataList.size()<=0)
        {
            return ResultVO.failResult("生成对账单失败，选择数据已被其他用户处理，请查询数据后再操作。");
        }
        if(list.size()!=dataList.size())
        {
            return ResultVO.failResult("生成对账单失败，选择数据有部份已被其他用户处理，请查询数据后再操作。");
        }

        List<String> msgList=new ArrayList<String>();
        Set cus_set = new HashSet();
        for(Js_Non_VehicleVO item:dataList)
        {
            if(!NOSETTLEMENT_STATE_Enum.SETTLEMENT.getCode().equals(item.getJs_state()))
            {
                msgList.add(item.getVin());
                continue;
            }
            //判断是否是同一客户
            cus_set.add(item.getCus_no());
        }
        if(msgList.size()>0)
        {
            return ResultVO.failResult("生成对账单失败","如下数据不是“"+NOSETTLEMENT_STATE_Enum.SETTLEMENT.getName()+"”状态，车架号："+ ArrayUtils.join(msgList,";"));
        }
        if(cus_set.size() > 1){
            return ResultVO.failResult("生成对账单失败","不同的客户无法生成对账单");
        }
        SysUserVO sysUserVO= SessionUtils.currentSession();

        String dz_sheet=GetSheetId("TMS","1","JS_DZ_NON_SHEET");
        //String js_batch=GetSheetId("TMS","2","JS_DZ_NON_SHEET");
        //String js_no=GetSheetId("TMS","3","JS_DZ_NON_SHEET");
        Float not_tax_amount=0f;
        Float tax_amount=0f;
        String CurrentDate= DateUtils.nowDate();
        //写入主表（JS_DZ_NON_SHEET）
        Js_Dz_Non_SheetVO sheetVO=new Js_Dz_Non_SheetVO();
        sheetVO.setDz_sheet(dz_sheet);
        sheetVO.setDz_op_by(sysUserVO.getUserId());
        sheetVO.setDz_op_datetime(CurrentDate);
        sheetVO.setState(NOSETTLEMENT_STATE_Enum.NORMAL.getCode());
        sheetVO.setCus_no(dataList.get(0).getCus_no());
        sheetVO.setCus_name(dataList.get(0).getCus_name());
        //sheetVO.setJs_batch(js_batch);
        //sheetVO.setJs_no(js_no);
        for(Js_Non_VehicleVO item:dataList) {
            if (StringUtils.isNotBlank(item.getNot_tax_amount()))
                not_tax_amount = not_tax_amount + Float.valueOf(item.getNot_tax_amount());
            if (StringUtils.isNotBlank(item.getTax_amount()))
                tax_amount = tax_amount + Float.valueOf(item.getTax_amount());
        }
        //写入主表（JS_DZ_NON_SHEET）
        sheetVO.setNot_tax_amount(not_tax_amount.toString());
        sheetVO.setTax_amount(tax_amount.toString());
        this.insert(sheetVO);


        // 写入明细表（JS_DZ_NON_SHEET_DETAIL）且更新 JS_NON_VEHICLE 状态
        for(Js_Non_VehicleVO item:list){
            item.setJs_state(NOSETTLEMENT_STATE_Enum.BILL.getCode());
            item.setDz_sheet(dz_sheet);
            item.setCreate_by(sysUserVO.getUserId());
            item.setCreate_date(CurrentDate);
        }
        iJs_Non_VehicleDao.insertDz_Non_Sheet_Detail(list);

        return ResultVO.successResult("生成对账单成功");
    }


    private String GetSheetId(String p_within_code,String p_whcenter,String p_tablename)
    {
        SysSheetIdVO sysSheetIdVO=new SysSheetIdVO();
        sysSheetIdVO.setP_within_code(p_within_code);
        sysSheetIdVO.setP_whcenter(p_whcenter);
        sysSheetIdVO.setP_tablename(p_tablename);
        return iSysSheetIdService.USP_WS_SHEETID_GET(sysSheetIdVO);
    }



    /*
     * 导入数据 修改
     * */
    @Override
    public ResultVO importData(MultipartFile file,String mtype) {
        List<Js_Non_VehicleVO> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), Js_Non_VehicleVO.class, new ImportParams());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == list || list.size() <= 0) {
            return ResultVO.failResult("导入失败，上传Excel文件中没有数据");
        }
        SysUserVO sysUserVO = SessionUtils.currentSession();
        String create_date = DateUtils.nowDate();
        if (NOSETTLEMENT_TAB_Enum.SEC.getCode().equals(mtype)) {
            //保密车，不走合同规则，人工结算后导入系统。
            //状态 = 1 结算
            List<Js_Non_VehicleVO> insertList = new ArrayList<Js_Non_VehicleVO>();
            List<Js_Non_VehicleVO> updateList = new ArrayList<Js_Non_VehicleVO>();
            List<Js_Non_VehicleVO> queryList = new ArrayList<Js_Non_VehicleVO>();
            for (Js_Non_VehicleVO item : list) {
                Js_Non_VehicleVO queryvo = new Js_Non_VehicleVO();
                queryvo.setHandover_no(item.getHandover_no());
                queryList.add(queryvo);
            }
            Integer iRow = 1;
            String errorRow = "";
            queryList = this.findListByProperty(queryList);
            for (Js_Non_VehicleVO Info : list) {
                iRow++;
                Boolean bExists = false;
                for (Js_Non_VehicleVO queryInfo : list) {
                    if (Info.getHandover_no().equals(queryInfo.getHandover_no())) {
                        if (!queryInfo.getJs_state().equals(NOSETTLEMENT_STATE_Enum.NORMAL.getCode()) && !queryInfo.getJs_state().equals(NOSETTLEMENT_STATE_Enum.SETTLEMENT.getCode()))
                            errorRow += iRow.toString() + ",";
                        Info.setId(queryInfo.getId());
                        bExists = true;
                        break;
                    }
                }
                //客户编号
                Non_Ht_CusVO cusvo = new Non_Ht_CusVO();
                cusvo.setCus_name(Info.getCus_name());
                List<Non_Ht_CusVO> cusList = this.findListByProperty(cusvo);
                if (null != cusList && cusList.size() > 0) {
                    Info.setCus_no(cusList.get(0).getCus_no());
                }
                if (bExists) {
                    updateList.add(Info);
                } else {
                    Info.setJs_state(NOSETTLEMENT_STATE_Enum.SETTLEMENT.getCode());
                    Info.setCreate_by(sysUserVO.getUserId());
                    Info.setCreate_date(create_date);
                    insertList.add(Info);
                }
            }
            if (StringUtils.isNotBlank(errorRow)) {
                return ResultVO.failResult("导入失败，数据不是“" + NOSETTLEMENT_STATE_Enum.NORMAL.getName() + "”或“" + NOSETTLEMENT_STATE_Enum.SETTLEMENT.getName() + "”状态，导入文件行号：" + errorRow);
            }
            if (insertList.size() > 0)
                this.insert(insertList);
            if (updateList.size() > 0)
                this.update(updateList);
            return ResultVO.successResult("导入成功！新增 " + insertList.size() + "笔；更新 " + updateList.size() + "笔。");
        }

        //下面是   VIP,合同车，救援车    程序代码
        List<Js_Non_VehicleVO> dataList = new ArrayList<Js_Non_VehicleVO>();
        for (Js_Non_VehicleVO item : list) {
            Js_Non_VehicleVO vo = new Js_Non_VehicleVO();
            vo.setId(item.getId());
            vo.setJs_state(NOSETTLEMENT_STATE_Enum.NORMAL.getCode());
            dataList.add(vo);
        }
        dataList = this.findListByProperty(dataList);
        if (null == dataList || dataList.size() <= 0) {
            return ResultVO.failResult("导入失败，上传Excel文件中的数据已不是“" + NOSETTLEMENT_STATE_Enum.NORMAL.getName() + "”状态。");
        }
        //过滤状态非
        for (int i = list.size() - 1; i >= 0; i--) {
            boolean bExists = false;
            for (Js_Non_VehicleVO item : dataList) {
                if (list.get(i).getId().equals(item.getId())) {
                    list.get(i).setBus_id(item.getBus_id());
                    list.get(i).setBill_number(item.getBill_number());
                    bExists = true;
                }
            }
            if (!bExists) {
                list.remove(i);
            }
        }
        if (null != list && list.size() > 0) {
            this.update(list);
            //3.3.5预付 - 需求
            iJs_Non_VehicleDao.updateJs_Non_Down_Vehicle(list);
        }
        String noUpdate = "";
        if (dataList.size() != list.size()) {
            noUpdate = "；忽略更新" + (list.size() - dataList.size()) + "笔数据(因为不是“" + NOSETTLEMENT_STATE_Enum.NORMAL.getName() + "”状态)";
        }

        return ResultVO.successResult("导入成功！更新" + list.size() + "笔数据" + noUpdate);
    }





    /**
     * 报表功能
     * */
    @Override
    public PageVO getListForGridBaobiao(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<NonVehicleTotalVO> list = iJs_Non_VehicleDao.getListForGridBaobiao(jqGridParamModel);
        //CREATE OR REPLACE VIEW   VIEW_NONVEHICLETOTAL     AS
        // 1、视图中已匹配合同，
        // 2、如果没有匹配合同，代码再按统计规则计算数据

        if(null!=list && list.size()>0)
        {
      /*      for(NonVehicleTotalVO item : list)
            {
                // 2、如果没有匹配合同，代码再按统计规则计算数据  - 组织查询条件
                if((item.getNot_tax_freight()==null && item.getNot_tax_premium()==null && item.getNot_tax_other_amount()==null && item.getNot_tax_amount()==null) ||
                        (item.getNot_tax_down_freight()==null && item.getDown_premium_price()==null && item.getNot_tax_other_down_amount()==null && item.getDown_amount()==null)
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
            List<Tr_statistical_rulesVO> ruleList=Js_Vin_AmountDao.findTR_STATISTICAL_RULES();
            if(null!=ruleList && ruleList.size()>0)
            {
                for(NonVehicleTotalVO item : list) {
                    // 2、如果没有匹配合同，代码再按统计规则计算数据  - 匹配数据
                    for(Tr_statistical_rulesVO rvo:ruleList) {
                        if(matchingRules(rvo,item)) {
                            //对上预算费用
                            if ( item.getNot_tax_freight() == null && item.getNot_tax_premium() == null && item.getNot_tax_other_amount() == null && item.getNot_tax_amount() == null) {
                                //对上单价就是 第一段*1里程+第二段*2里程+第三段*3里程
                                Double price=rvo.getUp_first_mileage() * rvo.getUp_first_price() + rvo.getUp_two_mileage() * rvo.getUp_two_price() + rvo.getUp_three_mileage()*rvo.getUp_three_price();
                                //item.setPrice(price);
                                item.setNot_tax_freight(price);
                                item.setNot_tax_premium(rvo.getUp_premium());
                                item.setNot_tax_other_amount(rvo.getUp_cross_sea_amount());
                                item.setNot_tax_amount(/*item.getPrice()+*/item.getNot_tax_freight()+item.getNot_tax_premium()+item.getNot_tax_other_amount());
                            }
                            //对下预算费用
                            if (item.getNot_tax_down_freight() == null && item.getDown_premium_price() == null && item.getDown_premium_total() == null && item.getNot_tax_other_down_amount() == null && item.getDown_amount() == null) {
                                //对下单价就是 第一段*1里程+第二段*2里程+第三段*3里程
                                Double not_tax_price=rvo.getDown_first_mileage() * rvo.getDown_first_price() + rvo.getDown_two_mileage()*rvo.getDown_two_price() + rvo.getDown_three_mileage() * rvo.getDown_three_price();
                                //item.setNot_tax_price(not_tax_price);

                                item.setNot_tax_other_down_amount(Double.valueOf(rvo.getDown_cross_sea_amount()));
                                item.setNot_tax_down_freight(not_tax_price);
                                item.setDown_premium_price(rvo.getDown_premium());
                                item.setDown_premium_total(rvo.getDown_premium()* Integer.valueOf(item.getDown_jifei_qty()));
                                item.setNot_tax_other_down_amount(rvo.getDown_cross_sea_amount());
                            }
                            break;
                        }
                    }
                }
            }

            for(NonVehicleTotalVO item : list)
            {
                item.setMil(null==item.getMil()?0:item.getMil());
                item.setHt_price(null==item.getHt_price()?0d:item.getHt_price());
                item.setNot_tax_freight(null==item.getNot_tax_freight()?0d:item.getNot_tax_freight());
                item.setNot_tax_premium_price(null==item.getNot_tax_premium_price()?0d:item.getNot_tax_premium_price());
                item.setNot_tax_premium(null==item.getNot_tax_premium()?0d:item.getNot_tax_premium());
                item.setNot_tax_other_amount(null==item.getNot_tax_other_amount()?0d:item.getNot_tax_other_amount());
                item.setNot_tax_amount(null==item.getNot_tax_amount()?0d:item.getNot_tax_amount());
                item.setFreight_zucheng(null==item.getFreight_zucheng()?0d:item.getFreight_zucheng());
                item.setHt_jifei_biaozhun(null==item.getHt_jifei_biaozhun()?0d:item.getHt_jifei_biaozhun());
                item.setNot_tax_down_freight(null==item.getNot_tax_down_freight()?0d:item.getNot_tax_down_freight());
                item.setDown_premium_price(null==item.getDown_premium_price()?0d:item.getDown_premium_price());
                item.setDown_premium_total(null==item.getDown_premium_total()?0d:item.getDown_premium_total());
                item.setNot_tax_other_down_amount(null==item.getNot_tax_other_down_amount()?0d:item.getNot_tax_other_down_amount());
                item.setDown_amount(null==item.getDown_amount()?0d:item.getDown_amount());
                item.setTax__real_cost(null==item.getTax__real_cost()?0d:item.getTax__real_cost());
            }
        }

        int records = iJs_Non_VehicleDao.getListForGridBaobiaoCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
    }

    //验证匹配规则
    public boolean matchingRules (Tr_statistical_rulesVO rulesVO,NonVehicleTotalVO vehicletotalVO) {
        boolean flag = false;
        if (vehicletotalVO.getTrans_mode().equalsIgnoreCase(rulesVO.getTrans_mode())
                && vehicletotalVO.getBegin_city().equalsIgnoreCase(rulesVO.getBegin_city())
                && vehicletotalVO.getEnd_city().equalsIgnoreCase(rulesVO.getEnd_city())
                && vehicletotalVO.getBegin_date().getTime()>rulesVO.getBegin_date().getTime()
                && vehicletotalVO.getReceipt_date().getTime()<rulesVO.getEnd_date().getTime()) {
            flag=true;
        }
        return flag;
    }

}
