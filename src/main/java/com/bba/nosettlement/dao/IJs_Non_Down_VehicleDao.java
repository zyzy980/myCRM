package com.bba.nosettlement.dao;

import com.bba.ht.vo.Non_Ht_CarrierVO;
import com.bba.ht.vo.Non_Ht_Carrier_FreightVO;
import com.bba.nosettlement.vo.Js_Non_Down_VehicleVO;
import com.bba.settlement.vo.Js_Non_DownTemp_VehicleVO;
import com.bba.util.JqGridParamModel;

import java.util.List;

public interface IJs_Non_Down_VehicleDao {


    public List<Js_Non_Down_VehicleVO> getListForGrid(JqGridParamModel jqGridParamModel);

    public int getListForGridCount(JqGridParamModel jqGridParamModel);


    /**
     * 驳回
     * */
    public void UpdateDataList(List<Js_Non_Down_VehicleVO> list);

    void update(Js_Non_Down_VehicleVO paramJs_non_down_vehicleVO);


    //结算
    public Non_Ht_CarrierVO findContractVO(Js_Non_Down_VehicleVO vo);
    public Non_Ht_Carrier_FreightVO findContractFreightVO(Js_Non_Down_VehicleVO vo);



    public void updateJs_Non_DownTemp_Vehicle(List<Js_Non_DownTemp_VehicleVO> list);
}
