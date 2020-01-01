package com.bba.jcda.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.bba.common.interceptor.Log;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.jcda.service.api.IJs_Trans_Mode_ChangeService;
import com.bba.jcda.vo.Js_Trans_Mode_ChangeVO;
import com.bba.util.*;
import com.bba.xtgl.vo.SysUserVO;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 账单编号
 * @Author:Suwendaidi
 * @Date: 2019/8/1
 */

@RestController
@RequestMapping("/jcda/transModeChange")
public class Js_Trans_Mode_ChangeController {

    @Autowired
    private IJs_Trans_Mode_ChangeService js_Trans_Mode_ChangeService;

    @Log(value = "运输方式转换-清单查询")
    @RequestMapping("getListForGrid")
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        customSearchFilters = MyUtils.decode(customSearchFilters);
        PageVO pageVO = js_Trans_Mode_ChangeService.getListForGrid(jqGridParamModel,customSearchFilters);
        return pageVO;
    }

    @Log(value = "运输方式转换-保存")
    @RequestMapping("saveDetail")
    public ResultVO saveDetail(@RequestBody Js_Trans_Mode_ChangeVO requestJs_Trans_Mode_ChangeVO) {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        ResultVO resultVO = js_Trans_Mode_ChangeService.saveDetail(requestJs_Trans_Mode_ChangeVO, sysUserVO);
        return resultVO;
    }

    @Log(value = "运输方式转换-明细查询")
    @RequestMapping("getDetail")
    public ResultVO getDetail(Js_Trans_Mode_ChangeVO js_Trans_Mode_ChangeVO) {
        if(js_Trans_Mode_ChangeVO.getId()==0){
            return ResultVO.failResult("未查询到明细");
        }
        Js_Trans_Mode_ChangeVO returnJs_Trans_Mode_ChangeVO = js_Trans_Mode_ChangeService.getDetail(js_Trans_Mode_ChangeVO);
        if(returnJs_Trans_Mode_ChangeVO == null){
            return ResultVO.failResult("未查询到任何数据");
        }
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(returnJs_Trans_Mode_ChangeVO);
        return resultVO;
    }

    @Log(value = "运输方式转换-删除")
    @ResponseBody
    @RequestMapping("/batchDelete")
    public ResultVO batchDelete(String jsonData) {
        if (jsonData == null || jsonData.length() == 0) {
            return ResultVO.failResult("要删除的数据为空");
        }
        List<Js_Trans_Mode_ChangeVO> modeChangeLists = JSONUtils.toJSONObjectList(Js_Trans_Mode_ChangeVO.class, jsonData);
        return js_Trans_Mode_ChangeService.batchDelete(modeChangeLists);
    }

    @Log(value = "运输方式转换-导入数据")
    @RequestMapping("/importData")
    @ResponseBody
    public ResultVO importData(@RequestParam("fileList") MultipartFile file) throws Exception {
        SysUserVO sysUserVO = SessionUtils.currentSession();
        List<Js_Trans_Mode_ChangeVO> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), Js_Trans_Mode_ChangeVO.class, new ImportParams());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null==list || list.size()<=0)
        {
            return ResultVO.failResult("导入失败，导入Excel文件没有数据。");
        }
        String error="";
        List<String> cols=new ArrayList<String>();
        int Rows=2;
        //第一段+第二段+第三段+运输方式
        for(Js_Trans_Mode_ChangeVO item:list) {
            if (StringUtils.isBlank(item.getFirst_route()) || StringUtils.isBlank(item.getTwo_route()) || StringUtils.isBlank(item.getThree_route())) {
                return ResultVO.failResult("导入失败，第一段路线、第二段路线和第三段路不能为空，行号："+error);
            }
            String ColStr = item.getTrans_mode() + item.getFirst_route() + item.getTwo_route()+item.getThree_route();
            if (!cols.contains(ColStr)) {
                cols.add(ColStr);
            } else {
                error=Rows+",";
            }
            Rows++;
        }
        if(StringUtils.isNotBlank(error))
        {
            return ResultVO.failResult("导入失败，导入数据有重复（运输方式+第一段路线+第二段路线+第三段路线），重复行号："+error);
        }
        Date CurrentDate= new Date();
        //判断更新，或新增
        List<Js_Trans_Mode_ChangeVO> insertData=new ArrayList<Js_Trans_Mode_ChangeVO>();
        List<Js_Trans_Mode_ChangeVO> updateData=new ArrayList<Js_Trans_Mode_ChangeVO>();
        for(Js_Trans_Mode_ChangeVO item:list)
        {
            item.setCreate_by(sysUserVO.getRealName());
            item.setCreate_date(CurrentDate);
            if(UniqueValideData(item)) {
                //存在数据
                updateData.add(item);
            } else {
                //不存在数据
                insertData.add(item);
            }
        }
        if(insertData.size()>0)
            for (Js_Trans_Mode_ChangeVO insertVO:insertData) {
                js_Trans_Mode_ChangeService.insert(insertVO);
            }
        return ResultVO.successResult("导入成功！");
    }

    /**
     * 验证数据唯一
     * 运输方式+第一段路线+第二段路线+第三段路线 构成唯一性 （需要验证）
     * false=不存在，true=存在
     * */
    private boolean UniqueValideData(Js_Trans_Mode_ChangeVO vo) {
        Js_Trans_Mode_ChangeVO item = new Js_Trans_Mode_ChangeVO();
        item.setTrans_mode(vo.getTrans_mode());
        item.setFirst_route(vo.getFirst_route());
        item.setTwo_route(vo.getTwo_route());
        item.setThree_route(vo.getThree_route());
        EntityWrapper changeWrapper = new EntityWrapper();
        changeWrapper.setEntity(item);
        List<Js_Trans_Mode_ChangeVO> list = js_Trans_Mode_ChangeService.selectList(changeWrapper);
        if (null == list || list.size() <= 0) {
            return false;
        }
        else {
            vo.setId(list.get(0).getId());
            return true;
        }
    }

    @Log(value = "运输方式转换--下载导入模板")
    @RequestMapping("exportTemplate")
    @ResponseBody
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response)
            throws IOException, InvalidFormatException {
        POIUtils.exportTemplate(request, response, "合同运输方式转换",
                "Resource/excel/jcda/trans_mode_change.xlsx");
    }

}
