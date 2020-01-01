package com.bba.jcda.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.ht.vo.Ht_Carrier_FreightVO;
import com.bba.ht.vo.Ht_Cus_FreightVO;
import com.bba.jcda.dao.IJs_Trans_Mode_ChangeDao;
import com.bba.jcda.dto.Ht_FreightDTO;
import com.bba.jcda.service.api.IJs_Trans_Mode_ChangeService;
import com.bba.jcda.vo.Js_Trans_Mode_ChangeVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.util.StringUtils;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:26
 */
@Service
@Transactional
public class Js_Trans_Mode_ChangeServiceimpl extends ServiceImpl<IJs_Trans_Mode_ChangeDao, Js_Trans_Mode_ChangeVO> implements IJs_Trans_Mode_ChangeService {

    @Resource
    private IJs_Trans_Mode_ChangeDao js_Trans_Mode_ChangeDao;


    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Js_Trans_Mode_ChangeVO> list = js_Trans_Mode_ChangeDao.findListForGrid(jqGridParamModel);
        int count = js_Trans_Mode_ChangeDao.findListForGridCount(jqGridParamModel);
        return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,count);
    }

    @Override
    public ResultVO saveDetail(Js_Trans_Mode_ChangeVO requestJs_trans_mode_changeVO, SysUserVO sysUserVO) {

        if(StringUtils.isBlank(requestJs_trans_mode_changeVO.getTrans_mode())){
            return ResultVO.failResult("运输方式不能为空");
        }

        if(StringUtils.isBlank(requestJs_trans_mode_changeVO.getFirst_route())){
            return ResultVO.failResult("第一段路线不能为空");
        }

        if(StringUtils.isBlank(requestJs_trans_mode_changeVO.getTwo_route())){
            return ResultVO.failResult("第二段路线不能为空");
        }

        if(StringUtils.isBlank(requestJs_trans_mode_changeVO.getThree_route())){
            return ResultVO.failResult("第三段路线不能为空");
        }

        if(requestJs_trans_mode_changeVO.getId()==0){
            //新增合同，验证合同编号是否唯一
            requestJs_trans_mode_changeVO.setCreate_date(new Date());
            requestJs_trans_mode_changeVO.setCreate_by(sysUserVO.getRealName());
            Js_Trans_Mode_ChangeVO dataJs_Trans_Mode_ChangeVO = js_Trans_Mode_ChangeDao.selectOne(requestJs_trans_mode_changeVO);
            if (dataJs_Trans_Mode_ChangeVO!=null) {
                return ResultVO.failResult("录入的内容已存在系统"+dataJs_Trans_Mode_ChangeVO.getTrans_mode()+"+"+dataJs_Trans_Mode_ChangeVO.getFirst_route()
                        +"+"+dataJs_Trans_Mode_ChangeVO.getTwo_route()+"+"+dataJs_Trans_Mode_ChangeVO.getThree_route());
            }
            js_Trans_Mode_ChangeDao.insert(requestJs_trans_mode_changeVO);
        }else {
            Js_Trans_Mode_ChangeVO paramJs_Trans_Mode_ChangeVO = new Js_Trans_Mode_ChangeVO();
            paramJs_Trans_Mode_ChangeVO.setId(requestJs_trans_mode_changeVO.getId());
            Js_Trans_Mode_ChangeVO dataJs_Trans_Mode_ChangeVO = js_Trans_Mode_ChangeDao.selectOne(paramJs_Trans_Mode_ChangeVO);
            if (dataJs_Trans_Mode_ChangeVO == null) {
                return ResultVO.failResult("该条数据不存在，请核实数据");
            }
            String newstr= dataJs_Trans_Mode_ChangeVO.getTrans_mode()+dataJs_Trans_Mode_ChangeVO.getFirst_route()+dataJs_Trans_Mode_ChangeVO.getTwo_route()+dataJs_Trans_Mode_ChangeVO.getThree_route();
            String oldstr = requestJs_trans_mode_changeVO.getTrans_mode()+requestJs_trans_mode_changeVO.getFirst_route()+requestJs_trans_mode_changeVO.getTwo_route()+requestJs_trans_mode_changeVO.getThree_route();
            //
            if (StringUtils.notEquals(newstr,oldstr)) {
                dataJs_Trans_Mode_ChangeVO = js_Trans_Mode_ChangeDao.selectOne(requestJs_trans_mode_changeVO);
                if (dataJs_Trans_Mode_ChangeVO != null) {
                    return ResultVO.failResult("录入的内容已存在系统"+dataJs_Trans_Mode_ChangeVO.getTrans_mode()+"+"+dataJs_Trans_Mode_ChangeVO.getFirst_route()
                            +"+"+dataJs_Trans_Mode_ChangeVO.getTwo_route()+"+"+dataJs_Trans_Mode_ChangeVO.getThree_route());
                }
            }
            requestJs_trans_mode_changeVO.setCreate_by(null);
            requestJs_trans_mode_changeVO.setCreate_date(null);
            js_Trans_Mode_ChangeDao.updateById(requestJs_trans_mode_changeVO);
        }
        return ResultVO.successResult("保存成功");
    }

    @Override
    public Js_Trans_Mode_ChangeVO getDetail(Js_Trans_Mode_ChangeVO js_trans_mode_changeVO) {
        Js_Trans_Mode_ChangeVO paramJs_Trans_Mode_ChangeVO = new Js_Trans_Mode_ChangeVO();
        paramJs_Trans_Mode_ChangeVO.setId(js_trans_mode_changeVO.getId());
        Js_Trans_Mode_ChangeVO dataTax_rateVO = js_Trans_Mode_ChangeDao.selectOne(paramJs_Trans_Mode_ChangeVO);
        if(dataTax_rateVO == null){
            return null;
        }
        return dataTax_rateVO;
    }

    @Override
    public ResultVO batchDelete(List<Js_Trans_Mode_ChangeVO> modeChangeLists) {
        List<String> idList = new ArrayList<String>();
        for (Js_Trans_Mode_ChangeVO vo:modeChangeLists) {
            idList.add(vo.getId().toString());
        }
        js_Trans_Mode_ChangeDao.deleteBatchIds(idList);
        return ResultVO.successResult("删除成功");
    }

    /***
     * 转换合同运输方式通用方法
     * @param dto
     * @param type
     * @return
     */
    @Override
    public Ht_FreightDTO exchangeTransMode(Ht_FreightDTO dto, String type) {
        //type=1,对上合同，type=2对下
        if (StringUtils.equals(type,"1")) {
            List<Ht_Cus_FreightVO> new_list = new ArrayList<Ht_Cus_FreightVO>();
            List<Ht_Cus_FreightVO> list =dto.getHt_Cus_Freight_List();
            for (Ht_Cus_FreightVO cus_vo:list) {
                if (StringUtils.isBlank(cus_vo.getTrans_mode())) {
                    if (StringUtils.isNotBlank(cus_vo.getFirst_route()) && StringUtils.isNotBlank(cus_vo.getTwo_route()) && StringUtils.isNotBlank(cus_vo.getThree_route())) {
                        Js_Trans_Mode_ChangeVO paramChangeVO = new Js_Trans_Mode_ChangeVO();
                        paramChangeVO.setFirst_route(cus_vo.getFirst_route());
                        paramChangeVO.setTwo_route(cus_vo.getTwo_route());
                        paramChangeVO.setThree_route(cus_vo.getThree_route());
                        EntityWrapper changeWrapper = new EntityWrapper(paramChangeVO);
                        List<Js_Trans_Mode_ChangeVO> changeList = js_Trans_Mode_ChangeDao.selectList(changeWrapper);
                        if (changeList.size()>0) {
                            for (Js_Trans_Mode_ChangeVO data_change_vo:changeList ) {
                                Ht_Cus_FreightVO new_cus_vo = new Ht_Cus_FreightVO();
                                BeanUtils.copyProperties(cus_vo, new_cus_vo);
                                new_cus_vo.setTrans_mode(data_change_vo.getTrans_mode());
                                new_list.add(new_cus_vo);
                            }
                        } else {
                            new_list.add(cus_vo);
                        }

                    } else {
                        new_list.add(cus_vo);
                    }
                } else {
                    new_list.add(cus_vo);
                }
            }
            dto.setHt_Cus_Freight_List(new_list);
        } else {
            List<Ht_Carrier_FreightVO> list =dto.getHt_Carrier_Freight_List();
            List<Ht_Carrier_FreightVO> new_list = new ArrayList<Ht_Carrier_FreightVO>();
            for (Ht_Carrier_FreightVO carrier_vo:list) {
                if (StringUtils.isBlank(carrier_vo.getTrans_mode())) {
                    if (StringUtils.isNotBlank(carrier_vo.getFirst_route()) && StringUtils.isNotBlank(carrier_vo.getTwo_route()) && StringUtils.isNotBlank(carrier_vo.getThree_route())) {
                        Js_Trans_Mode_ChangeVO paramChangeVO = new Js_Trans_Mode_ChangeVO();
                        paramChangeVO.setFirst_route(carrier_vo.getFirst_route());
                        paramChangeVO.setTwo_route(carrier_vo.getTwo_route());
                        paramChangeVO.setThree_route(carrier_vo.getThree_route());
                        EntityWrapper changeWrapper = new EntityWrapper(paramChangeVO);
                        List<Js_Trans_Mode_ChangeVO> changeList = js_Trans_Mode_ChangeDao.selectList(changeWrapper);
                        if (changeList.size()>0) {
                            for (Js_Trans_Mode_ChangeVO data_change_vo:changeList ) {
                                Ht_Carrier_FreightVO new_carrier_vo = new Ht_Carrier_FreightVO();
                                BeanUtils.copyProperties(carrier_vo, new_carrier_vo);
                                new_carrier_vo.setTrans_mode(data_change_vo.getTrans_mode());
                                new_list.add(new_carrier_vo);
                            }
                        } else {
                            new_list.add(carrier_vo);
                        }

                    } else {
                        new_list.add(carrier_vo);
                    }
                } else {
                    new_list.add(carrier_vo);
                }
            }
            dto.setHt_Carrier_Freight_List(new_list);
        }
        return dto;
    }
}
