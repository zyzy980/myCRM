package com.bba.dz.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bba.common.constant.OperateConstant;
import com.bba.common.service.impl.BaseService;
import com.bba.common.vo.ResultVO;
import com.bba.dz.dao.Js_Dz_SheetAccountDao;
import com.bba.dz.dao.Js_Dz_SheetDao;
import com.bba.dz.dao.Js_Dz_Sheet_DetailDao;
import com.bba.dz.service.api.IJs_Dz_SheetAccountService;
import com.bba.dz.vo.Js_Dz_SheetAccountVO;
import com.bba.dz.vo.Js_Dz_SheetVO;
import com.bba.dz.vo.Js_Dz_Sheet_DetailAccountVO;
import com.bba.dz.vo.Js_Dz_Sheet_DetailVO;
import com.bba.settlement.dao.Js_Vin_Temp_AmountDao;
import com.bba.settlement.vo.Js_Vin_AmountVO;
import com.bba.settlement.vo.Js_Vin_Temp_AmountVO;
import com.bba.tzgl.dao.Js_CompensationDao;
import com.bba.tzgl.vo.Js_CompensationVO;
import com.bba.util.DateUtils;
import com.bba.util.ENDECodeUtils;
import com.bba.util.SessionUtils;
import com.bba.util.StringUtils;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Transactional
@Service
public class Js_Dz_SheetAccountService extends BaseService implements IJs_Dz_SheetAccountService {

    @Autowired
    private Js_Dz_SheetAccountDao js_Dz_SheetAccountDao;

    @Autowired
    private Js_Dz_Sheet_DetailDao js_Dz_Sheet_DetailDao;

    @Autowired
    private Js_Vin_Temp_AmountDao js_Vin_Temp_AmountDao;

    @Autowired
    private Js_CompensationDao js_CompensationDao;

    @Autowired
    private Js_Dz_SheetDao js_Dz_SheetDao;

    @Transactional
    @Override
    public ResultVO UpdateJs_Dz_Sheet_state(List<Js_Dz_SheetAccountVO> list,Js_Dz_SheetAccountVO vo)
    {

        try {
            js_Dz_SheetAccountDao.UpdateJs_Dz_Sheet_state(list);

/*            //对账单客户确认后，如果对账单里数据存在是二次结算的，则需要往补差表插入数据
            if("5".equals(vo.getState())) {
                for (Js_Dz_SheetAccountVO dz_sheetAccountVO:list) {
                    //1、查询对账单中需要补差的数据
                    //2、算出差额，插入到表
                    EntityWrapper dz_detailEntityWrapper = new EntityWrapper();
                    Js_Dz_Sheet_DetailVO paramJs_Dz_Sheet_DetailVO = new Js_Dz_Sheet_DetailVO();
                    paramJs_Dz_Sheet_DetailVO.setDz_sheet(dz_sheetAccountVO.getDz_sheet());
                    paramJs_Dz_Sheet_DetailVO.setType("1");//补差
                    paramJs_Dz_Sheet_DetailVO.setHis_flag("Y");//二次结算
                    dz_detailEntityWrapper.setEntity(paramJs_Dz_Sheet_DetailVO);
                    List<Js_Dz_Sheet_DetailVO> dz_detail_list = js_Dz_Sheet_DetailDao.selectList(dz_detailEntityWrapper);
                    List<String> idList = new ArrayList<>();
                    for (Js_Dz_Sheet_DetailVO dataVO:dz_detail_list) {
                        idList.add(dataVO.getVin_id().toString());
                    }
                    //查询历史结算数据
                    List<Js_Vin_Temp_AmountVO> temp_amountVOList = new ArrayList<>();
                    if (idList.size()>0) {
                        //temp_amountVOList = js_Vin_Temp_AmountDao.selectBatchIds(idList);
                        temp_amountVOList = js_Vin_Temp_AmountDao.selectByVinids(idList);
                    }
                    if (dz_detail_list.size()!=temp_amountVOList.size()) {
                        throw new RuntimeException("历史数据与补差数据不一致，请联系管理员");
                    }
                    //获取差额
                    List<Js_CompensationVO> insertJs_CompensationVOList = new ArrayList<>();
                    for (Js_Vin_Temp_AmountVO dataTempAmountVO:temp_amountVOList) {
                        for (Js_Dz_Sheet_DetailVO dataDz_Sheet_DetailVO:dz_detail_list) {
                            if (dataDz_Sheet_DetailVO.getVin_id().equals(dataTempAmountVO.getVin_id())) {
                                //算出差异
                                if (dataDz_Sheet_DetailVO.getTax_amount()!=dataTempAmountVO.getTax_amount()) {
                                    Js_CompensationVO requestJs_CompensationVO = new Js_CompensationVO();
                                    BeanUtils.copyProperties(dataDz_Sheet_DetailVO, requestJs_CompensationVO);
                                    requestJs_CompensationVO.setType("1");//对上补差
                                    requestJs_CompensationVO.setBefor_contract_no(dataTempAmountVO.getContract_no());//前合同
                                    requestJs_CompensationVO.setBefor_contract_type(dataTempAmountVO.getContract_type());//前合同类型
                                    requestJs_CompensationVO.setAfter_contract_no(dataDz_Sheet_DetailVO.getContract_no());//后合同
                                    requestJs_CompensationVO.setAfter_contract_type(dataDz_Sheet_DetailVO.getContract_type());//后合同类型
                                    requestJs_CompensationVO.setTax_up_total(dataDz_Sheet_DetailVO.getTax_amount().subtract(dataTempAmountVO.getTax_amount()));//应收运费含税补差
                                    requestJs_CompensationVO.setNtax_up_total(dataDz_Sheet_DetailVO.getNot_tax_amount().subtract(dataTempAmountVO.getNot_tax_amount()));//应收运费不含税补差
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

                }
            }*/
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return ResultVO.successResult("OK");
    }

    @Transactional
    @Override
    public void replayUpdateData(List<Js_Dz_Sheet_DetailAccountVO> list)
    {
        js_Dz_SheetAccountDao.replayUpdateData(list);
    }


    @Override
    public void deleteJs_Dz_Sheet(Js_Dz_Sheet_DetailAccountVO vo)
    {
        js_Dz_SheetAccountDao.deleteJs_Dz_Sheet(vo);
    }

    @Override
    public void updateJs_Dz_Sheet(Js_Dz_Sheet_DetailAccountVO vo){
        js_Dz_SheetAccountDao.updateJs_Dz_Sheet(vo);
    }

    @Override
    public void UpdateJs_Vin_Amount(List<Js_Dz_SheetAccountVO> list)
    {
        js_Dz_SheetAccountDao.UpdateJs_Vin_Amount(list);
    }


    @Override
    public List<Js_Dz_Sheet_DetailAccountVO> GetJs_Dz_Sheet_DetailVO(List<Js_Dz_Sheet_DetailAccountVO> list)
    {
        return js_Dz_SheetAccountDao.GetJs_Dz_Sheet_DetailVO(list);
    }

    @Override
    public ResultVO reback(List<Js_Dz_SheetAccountVO> list)
    {
        List<Js_Dz_SheetAccountVO> datalist = this.findListByProperty(list);
        if (null == datalist || datalist.size() <= 0) {
            return ResultVO.failResult("没有查询到选中数据，原因：数据已被其他用户删除或处理，请查询数据后再操作。");
        }
        List<Js_Dz_SheetAccountVO> deletelist=new ArrayList<Js_Dz_SheetAccountVO>();
        for(Js_Dz_SheetAccountVO item:datalist)
        {
            if("0".equals(item.getState()))
            {
                Js_Dz_SheetAccountVO vo=new Js_Dz_SheetAccountVO();
                vo.setDz_sheet(item.getDz_sheet());
                deletelist.add(vo);
            }
        }
        js_Dz_SheetAccountDao.reback(deletelist);
        return ResultVO.successResult("撤消成功。");
    }



    @Override
    public ResultVO save(Js_Dz_SheetAccountVO vo) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        String id = ENDECodeUtils.URLDecode(vo.getRemark());//特殊，因为ID=Long类型，所以使用remark代替
        String[] id_array = id.split(",");
        List<Js_Vin_AmountVO> list = new ArrayList<Js_Vin_AmountVO>();
        for (String item : id_array) {
            Js_Vin_AmountVO info = new Js_Vin_AmountVO();
            info.setId(Long.valueOf(item));
            list.add(info);
        }
        List<Js_Vin_AmountVO> dataList=this.findListByProperty(list);
        for(Js_Vin_AmountVO item:dataList) {
            if (!"1".equals(item.getJs_state())) {
                return ResultVO.failResult("选中数据不是“结算”状态，VIN:" + item.getVin());
            }
        }

        BigDecimal tax_amount=new BigDecimal("0");
        BigDecimal not_tax_amount=new BigDecimal("0");
        List<Js_Dz_Sheet_DetailAccountVO> detailList=new ArrayList<Js_Dz_Sheet_DetailAccountVO>();
        for(Js_Vin_AmountVO item:dataList) {
            Js_Dz_Sheet_DetailAccountVO target=new Js_Dz_Sheet_DetailAccountVO();
            BeanUtils.copyProperties(item,target);
            target.setId(null);
            target.setData_state("0");
            target.setVin_id(String.valueOf(item.getId()));
            target.setBegin_date(DateUtils.dateFormat(item.getBegin_date(),"yyyy-MM-dd HH:mm:ss"));
            target.setReceipt_date(DateUtils.dateFormat(item.getReceipt_date(),"yyyy-MM-dd HH:mm:ss"));
            target.setNot_tax_freight(null==item.getNot_tax_freight()?"0":item.getNot_tax_freight().toString());
            target.setNot_tax_other_amount(null==item.getNot_tax_other_amount()?"0":item.getNot_tax_other_amount().toString());
            target.setShipment_qty(null==item.getShipment_qty()?"0":item.getShipment_qty().toString());
            target.setJs_qty(null==item.getJs_qty()?"0":item.getJs_qty().toString());
            target.setNot_tax_price(null==item.getNot_tax_price()?"0":item.getNot_tax_price().toString());
            target.setJs_no(item.getJs_no());
            target.setNot_tax_premium(null==item.getNot_tax_premium()?"0":item.getNot_tax_premium().toString());
            target.setTax_amount(null==item.getTax_amount()?"0":item.getTax_amount().toString());
            target.setNot_tax_amount(null==item.getNot_tax_amount()?"0":item.getNot_tax_amount().toString());
            target.setCreate_by(sysUserVO.getUserId());
            target.setCreate_date(DateUtils.nowDate());
            target.setJs_batch(item.getJs_batch());
            target.setPrice(null==item.getPrice()?"0":item.getPrice().toString());
            target.setTax_rate(null==item.getTax_rate()?"0":item.getTax_rate().toString());
            target.setBill_number(item.getBill_number());
            target.setType("0");


            target.setDz_sheet(vo.getDz_sheet());
            detailList.add(target);

            //统计金额 - 累加
            if(null!=item.getTax_amount())
                tax_amount=tax_amount.add(item.getTax_amount());
            if(null!=item.getNot_tax_amount())
                not_tax_amount=not_tax_amount.add(item.getNot_tax_amount());
        }

        if(detailList.size()>0) {
            //1、写入明细表
            this.insert(detailList);
            //2、更新 JS_VIN_AMOUNT
            for (Js_Vin_AmountVO item : list) {
                item.setJs_state("2"); //生成账单
            }
            this.update(list);
            //3、修改对账单主表的金额数据
            Js_Dz_SheetAccountVO masterVO=new Js_Dz_SheetAccountVO();
            masterVO.setDz_sheet(vo.getDz_sheet());
            List<Js_Dz_SheetAccountVO> masterList=this.findListByProperty(masterVO);
            if(null!=masterList && masterList.size()>0)
            {
                tax_amount=tax_amount.add(masterList.get(0).getTax_amount());
                not_tax_amount=not_tax_amount.add(masterList.get(0).getNot_tax_amount());
                masterVO.setTax_amount(tax_amount);
                masterVO.setNot_tax_amount(not_tax_amount);
            }
            this.update(masterVO);
        }
        return ResultVO.successResult("保存数据成功。");
    }


    /**
     * 一健账单号
     * */
    @Override
    public ResultVO  buildSheet(Js_Dz_Sheet_DetailAccountVO vo)
    {
        //1、将该对账单中的数据，根据：发运日年份+合同号+税率，将数据区分，按年份排序
        /*
        方法：groupbyDataList
         select distinct dz_sheet,begin_date,contract_no,tax_rate from (
            select dz_sheet,to_char(begin_date,'yyyy') begin_date,contract_no,tax_rate  from js_dz_sheet_detail
            where dz_sheet=#{dz_sheet,jdbcType=VARCHAR}
        ) a order by begin_date asc,contract_no asc
        * */
        //1、首先判断状态
        Js_Dz_SheetVO paramJs_Dz_SheetVO = new Js_Dz_SheetVO();
        paramJs_Dz_SheetVO.setDz_sheet(vo.getDz_sheet());
        Js_Dz_SheetVO dataJs_Dz_SheetVO = js_Dz_SheetDao.selectOne(paramJs_Dz_SheetVO);
        if (dataJs_Dz_SheetVO==null) {
            return ResultVO.failResult("操作的对账单不存在，请核实数据");
        } else if (StringUtils.notEquals(dataJs_Dz_SheetVO.getState(),"0")) {
            return ResultVO.failResult("非‘正常’状态，无法进行此操作");
        } else if (StringUtils.notEquals(dataJs_Dz_SheetVO.getType(),"0")) {
            return ResultVO.failResult("补差台账无法进行此操作");
        }
        //2、清空当前对账单里面所有数据的账单编号,包括结算数据
        vo.setBill_number("");
        js_Dz_SheetAccountDao.updateBill_Number(vo);

        List<Js_Dz_Sheet_DetailAccountVO> groupbyDataList=js_Dz_SheetAccountDao.groupbyDataList(vo);
        if(null==groupbyDataList || groupbyDataList.size()<=0)
        {
            return ResultVO.failResult("数据已被其它用户处理，请再查询数据后操作。");
        }

        for(Js_Dz_Sheet_DetailAccountVO item:groupbyDataList)
        {
            Integer maxIndex=0;
            item.setBill_number(OperateConstant.Global_Bill_Number_Pre);
            Js_Vin_AmountVO vinVO=js_Dz_SheetAccountDao.GetMaxBill_NumberByYear_JS_VIN_AMOUNT(item);
            if(null!=vinVO && StringUtils.isNotBlank(vinVO.getBill_number()))
            {
                maxIndex=Integer.valueOf(vinVO.getBill_number().substring(vinVO.getBill_number().length()-3));
            }
            maxIndex+=1;
            item.setBill_number(OperateConstant.Global_Bill_Number_Pre+item.getBegin_date()+String.format("%03d",maxIndex));
            js_Dz_SheetAccountDao.updateBill_Number(item);
        }

        return ResultVO.successResult("数据处理成功。");
    }

    @Override
    public Integer findNullBillNumber(Js_Dz_SheetAccountVO item) {
        return js_Dz_SheetAccountDao.findNullBillNumber(item);
    }

    @Override
    public List<Js_Dz_Sheet_DetailAccountVO> findBillNumber(List<Js_Dz_Sheet_DetailAccountVO> detailList) {
        return js_Dz_SheetAccountDao.findBillNumber(detailList);
    }

    @Override
    public String transModeSelect(String contract_no) {
        String flag = null;
        Integer count = js_Dz_SheetAccountDao.transModeSelect(contract_no);
        if (count>0) {
            flag = "GL";
        }
        return flag;
    }

    @Override
    public String getCusContractNo(String contract_sheet_no) {
        return js_Dz_SheetAccountDao.getCusContractNo(contract_sheet_no);
    }

}
