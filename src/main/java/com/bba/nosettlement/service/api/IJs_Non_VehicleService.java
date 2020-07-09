package com.bba.nosettlement.service.api;


import com.bba.common.service.api.IBaseService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.nosettlement.vo.Js_Non_VehicleVO;
import com.bba.util.JqGridParamModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/*
 * Author:LTJ
 * Date:2019-07-31
 * Description:3.3.3.非商品车对上结算操作
 * */
public interface IJs_Non_VehicleService extends IBaseService {


    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    public PageVO getListForGridBaobiao(JqGridParamModel jqGridParamModel, String customSearchFilters);

    /*
    * 结算
    * */
    public ResultVO settlement(List<Js_Non_VehicleVO> list);
    /*
     * 撤回
     * */
    public ResultVO un_settlement(List<Js_Non_VehicleVO> list);
    /*
     * 生成对账单
     * */
    public ResultVO create_bill(List<Js_Non_VehicleVO> list);

    /*
     * 导入数据 修改
     * */
    public ResultVO importData(MultipartFile file,String mtype);


    public ResultVO setcontract_no(List<Js_Non_VehicleVO> list);

}
