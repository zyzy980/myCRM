package com.bba.nosettlement.dao;

import com.bba.ht.vo.Non_Ht_CusVO;
import com.bba.ht.vo.Non_Ht_Cus_FreightVO;
import com.bba.nosettlement.vo.Js_Non_VehicleVO;
import com.bba.nosettlement.vo.NonVehicleTotalVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

public interface IJs_Non_VehicleDao {


    public List<Js_Non_VehicleVO> getListForGrid(JqGridParamModel jqGridParamModel);

    public int getListForGridCount(JqGridParamModel jqGridParamModel);


    public void updateUn_Settlement(List<Js_Non_VehicleVO> list);


    public void insertDz_Non_Sheet_Detail(List<Js_Non_VehicleVO> list);

    public List<NonVehicleTotalVO> getListForGridBaobiao(JqGridParamModel jqGridParamModel);

    public void updateJs_Non_Down_Vehicle(List<Js_Non_VehicleVO> list);

    public int getListForGridBaobiaoCount(JqGridParamModel jqGridParamModel);


    //非商品车对上合同
    public Non_Ht_CusVO findContractVO(Js_Non_VehicleVO vo);
    public List<Non_Ht_Cus_FreightVO> findContractFreightVO(Js_Non_VehicleVO vo);

}
