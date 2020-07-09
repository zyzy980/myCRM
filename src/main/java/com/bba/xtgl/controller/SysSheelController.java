package com.bba.xtgl.controller;

import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.StringUtils;
import com.bba.xtgl.service.api.ISysSheetIdService;
import com.bba.xtgl.vo.SysSheetIdVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 单号管理
 * @Author bcmaidou
 * @Date 2019/3/28 16:56
 */
@RestController
@RequestMapping("/sysInfo/SheelNo")
public class SysSheelController {

    @Autowired
    private ISysSheetIdService iSysSheetIdServicel;


    @RequestMapping("getSys_sheetid")
    @ResponseBody
    public synchronized ResultVO getSys_sheetid(SysSheetIdVO sysSheetIdVO){
        String sheetid = iSysSheetIdServicel.USP_WS_SHEETID_GET(sysSheetIdVO);
        if("1".equals(sysSheetIdVO.getP_ok())){
            ResultVO resultVO = ResultVO.successResult();
            resultVO.setResultDataFull(sheetid);
            return resultVO;
        }else{
            ResultVO resultVO = ResultVO.failResult(sheetid);
            resultVO.setResultDataFull(sheetid);
            return resultVO;
        }

    }


    /**
     * 查询列表数据
     * @Author bcmaidou
     * @Date 16:58 2019/3/28
     */
    @Log("查询单号列表")
    @RequestMapping(value = "getList",method = {RequestMethod.POST,RequestMethod.GET})
    public PageVO getList(String filters, JqGridParamModel jqGridParamModel){
        return iSysSheetIdServicel.searchList(filters,jqGridParamModel);
    }

    /**
     * 查询单号详情
     * @Author bcmaidou
     * @Date 18:55 2019/3/28
     */
    @Log("查询单号详情")
    @RequestMapping(value = "searchDetail",method = {RequestMethod.POST,RequestMethod.GET})
    public ResultVO searchDetail(String sn){
        return iSysSheetIdServicel.searchDetail(sn);
    }

    /**
     * 新增或修改单据号
     * @Author bcmaidou
     * @Date 9:30 2019/3/29
     */
    @Log("新增或修改单据号")
    @RequestMapping(value = "save",method = RequestMethod.POST)
    public ResultVO saveDetail(@RequestBody SysSheetIdVO sysSheetIdVO){
        sysSheetIdVO.setP_yw_location("TMS");
        return iSysSheetIdServicel.saveDetail(sysSheetIdVO);
    }

    /**
     * 删除单据号
     * @Author bcmaidou
     * @Date 9:30 2019/3/29
     */
    @Log("删除单据号")
    @RequestMapping(value = "del",method = {RequestMethod.POST,RequestMethod.GET})
    public ResultVO saveDetail(String sn){
        return iSysSheetIdServicel.delDetail(sn);
    }

}
