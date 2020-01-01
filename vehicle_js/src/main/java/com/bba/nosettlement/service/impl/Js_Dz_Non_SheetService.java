package com.bba.nosettlement.service.impl;

import com.bba.common.constant.OperateConstant;
import com.bba.common.service.impl.BaseService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.nosettlement.dao.IJs_Dz_Non_SheetDao;
import com.bba.nosettlement.service.api.IJs_Dz_Non_SheetService;
import com.bba.nosettlement.vo.Js_Dz_Non_SheetVO;
import com.bba.nosettlement.vo.Js_Dz_Non_Sheet_DetailVO;
import com.bba.nosettlement.vo.Js_Non_VehicleVO;
import com.bba.util.*;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 */
@Transactional
@Service
public class Js_Dz_Non_SheetService extends BaseService implements IJs_Dz_Non_SheetService {

    @Autowired
    private IJs_Dz_Non_SheetDao iJs_Dz_Non_SheetDao;


    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Js_Dz_Non_SheetVO> list = iJs_Dz_Non_SheetDao.getListForGrid(jqGridParamModel);
        int records = iJs_Dz_Non_SheetDao.getListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
    }

    @Override
    public PageVO getListForGridDetail(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Js_Dz_Non_Sheet_DetailVO> list = iJs_Dz_Non_SheetDao.getListForGridDetail(jqGridParamModel);
        int records = iJs_Dz_Non_SheetDao.getListForGridDetailCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
    }

    @Transactional
    @Override
    public void UpdateJs_Dz_Sheet_state(List<Js_Dz_Non_SheetVO> list)
    {
        iJs_Dz_Non_SheetDao.UpdateJs_Dz_Sheet_state(list);
    }




    @Transactional
    @Override
    public void replayUpdateData(List<Js_Dz_Non_Sheet_DetailVO> list)
    {
        iJs_Dz_Non_SheetDao.replayUpdateData(list);
    }


    @Override
    public void deleteJs_Dz_Sheet(Js_Dz_Non_Sheet_DetailVO vo)
    {
        iJs_Dz_Non_SheetDao.deleteJs_Dz_Sheet(vo);
    }

    @Override
    public void updateJs_Dz_Sheet(Js_Dz_Non_Sheet_DetailVO vo){
        iJs_Dz_Non_SheetDao.updateJs_Dz_Sheet(vo);
    }


    @Override
    public ResultVO reback(List<Js_Dz_Non_SheetVO> list)
    {
        List<Js_Dz_Non_SheetVO> datalist = this.findListByProperty(list);
        if (null == datalist || datalist.size() <= 0) {
            return ResultVO.failResult("没有查询到选中数据，原因：数据已被其他用户删除或处理，请查询数据后再操作。");
        }
        List<Js_Dz_Non_SheetVO> deletelist=new ArrayList<Js_Dz_Non_SheetVO>();
        for(Js_Dz_Non_SheetVO item:datalist)
        {
            if("0".equals(item.getState()))
            {
                Js_Dz_Non_SheetVO vo=new Js_Dz_Non_SheetVO();
                vo.setDz_sheet(item.getDz_sheet());
                deletelist.add(vo);
            }
        }
        iJs_Dz_Non_SheetDao.reback(deletelist);
        return ResultVO.successResult("撤消成功。");
    }

    @Override
    public ResultVO save(Js_Dz_Non_SheetVO vo)
    {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        String id = ENDECodeUtils.URLDecode(vo.getRemark());//特殊，因为ID=Long类型，所以使用remark代替
        String[] id_array = id.split(",");
        List<Js_Non_VehicleVO> list = new ArrayList<Js_Non_VehicleVO>();
        for (String item : id_array) {
            Js_Non_VehicleVO info = new Js_Non_VehicleVO();
            info.setId(item);
            list.add(info);
        }
        List<Js_Non_VehicleVO> dataList=this.findListByProperty(list);
        for(Js_Non_VehicleVO item:dataList) {
            if (!"1".equals(item.getJs_state())) {
                return ResultVO.failResult("选中数据不是“结算”状态，交车单:" + item.getHandover_no());
            }
        }

        Double tax_amount=0d;
        Double not_tax_amount=0d;
        //JS_DZ_NON_SHEET_DETAIL
        List<Js_Dz_Non_Sheet_DetailVO> detailList=new ArrayList<Js_Dz_Non_Sheet_DetailVO>();
        for(Js_Non_VehicleVO item:dataList) {
            Js_Dz_Non_Sheet_DetailVO target=new Js_Dz_Non_Sheet_DetailVO();
            BeanUtils.copyProperties(item,target);
            target.setId(null);
            target.setJs_state("0");
            target.setVin_id(String.valueOf(item.getId()));
            target.setBegin_date(item.getBegin_date());
            target.setReceipt_date(item.getReceipt_date());
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
                tax_amount=tax_amount+Double.valueOf(item.getTax_amount());
            if(null!=item.getNot_tax_amount())
                not_tax_amount=not_tax_amount+Double.valueOf(item.getNot_tax_amount());
        }

        if(detailList.size()>0) {
            //1、写入明细表
            this.insert(detailList);
            //2、更新 Js_Non_Vehicle
            for (Js_Non_VehicleVO item : list) {
                item.setJs_state("2"); //生成账单
            }
            this.update(list);
            //3、修改对账单主表的金额数据
            Js_Dz_Non_SheetVO masterVO=new Js_Dz_Non_SheetVO();
            masterVO.setDz_sheet(vo.getDz_sheet());
            List<Js_Dz_Non_SheetVO> masterList=this.findListByProperty(masterVO);
            if(null!=masterList && masterList.size()>0)
            {
                tax_amount=tax_amount+Double.valueOf(masterList.get(0).getTax_amount());
                not_tax_amount=not_tax_amount+Double.valueOf(masterList.get(0).getNot_tax_amount());
                masterVO.setTax_amount(String.valueOf(tax_amount));
                masterVO.setNot_tax_amount(String.valueOf(not_tax_amount));

                masterVO.setDz_sheet(null);
                masterVO.setId(masterList.get(0).getId());
            }
            this.update(masterVO);
        }
        return ResultVO.successResult("保存数据成功。");
    }



    /**
     * 一健账单号
     * */
    @Override
    public ResultVO  buildSheet(Js_Dz_Non_Sheet_DetailVO vo)
    {
        //1、将该对账单中的数据，根据：发运日年份+合同号+税率，将数据区分，按年份排序
        /*
        方法：groupbyDataList
         select distinct dz_sheet,begin_date,contract_no,tax_rate from (
            select dz_sheet,to_char(begin_date,'yyyy') begin_date,contract_no,tax_rate  from js_dz_sheet_detail
            where dz_sheet=#{dz_sheet,jdbcType=VARCHAR}
        ) a order by begin_date asc,contract_no asc
        * */
        List<Js_Dz_Non_Sheet_DetailVO> groupbyDataList=iJs_Dz_Non_SheetDao.groupbyDataList(vo);
        if(null==groupbyDataList || groupbyDataList.size()<=0)
        {
            return ResultVO.failResult("数据已被其它用户处理，请再查询数据后操作。");
        }

        for(Js_Dz_Non_Sheet_DetailVO item:groupbyDataList)
        {
            Integer maxIndex=0;
            item.setBill_number(OperateConstant.Global_Bill_Number_Pre);
            Js_Non_VehicleVO vinVO=iJs_Dz_Non_SheetDao.GetMaxBill_NumberByYear_JS_VIN_AMOUNT(item);
            if(null!=vinVO && StringUtils.isNotBlank(vinVO.getBill_number()))
            {
                maxIndex=Integer.valueOf(vinVO.getBill_number().substring(vinVO.getBill_number().length()-3));
            }
            maxIndex+=1;
            item.setBill_number(OperateConstant.Global_Bill_Number_Pre+item.getBegin_date()+String.format("%03d",maxIndex));
            iJs_Dz_Non_SheetDao.updateBill_Number(item);
        }

        return ResultVO.successResult("数据处理成功。");
    }

    @Override
    public Integer findNullBillNumber(Js_Dz_Non_SheetVO item) {
        return iJs_Dz_Non_SheetDao.findNullBillNumber(item);
    }
}
