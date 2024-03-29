package com.bba.nosettlement.service.api;


import com.bba.common.service.api.IBaseService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.nosettlement.vo.Js_Non_Down_VehicleVO;
import com.bba.util.JqGridParamModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/*
 * Author:LTJ
 * Date:2019-07-31
 * Description:3.3.3.非商品车对下结算操作
 * */
public interface IJs_Non_Down_VehicleService extends IBaseService {


    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);


    public void UpdateDataList(List<Js_Non_Down_VehicleVO> list);

    public ResultVO settlement(List<Js_Non_Down_VehicleVO> list);

    public ResultVO setcontract_no(List<Js_Non_Down_VehicleVO> list);

    public ResultVO  un_settlement(List<Js_Non_Down_VehicleVO> list);

    public ResultVO  data_check(Js_Non_Down_VehicleVO vo,List<Js_Non_Down_VehicleVO> list,List<Js_Non_Down_VehicleVO> datalist);

}
