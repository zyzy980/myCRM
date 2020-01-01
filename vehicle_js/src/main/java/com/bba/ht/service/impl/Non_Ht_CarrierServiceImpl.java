package com.bba.ht.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.bba.common.common.Sys_sheetidUtil;
import com.bba.common.constant.Contract_StateEnum;
import com.bba.common.constant.Contract_TypeEnum;
import com.bba.common.constant.SysDictionaryDataConstant;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.ht.dao.Non_Ht_CarrierDao;
import com.bba.ht.dao.Non_Ht_Carrier_FreightDao;
import com.bba.ht.dto.Non_Ht_CarrierDTO;
import com.bba.ht.service.api.INon_Ht_CarrierService;
import com.bba.ht.vo.Non_Ht_CarrierVO;
import com.bba.ht.vo.Non_Ht_Carrier_FreightVO;
import com.bba.util.ArrayUtils;
import com.bba.util.JqGridParamModel;
import com.bba.util.JqGridSearchParamHandler;
import com.bba.util.StringUtils;
import com.bba.xtgl.dao.Sys_Dictionary_DataDao;
import com.bba.xtgl.vo.SysUserVO;
import com.bba.xtgl.vo.Sys_Dictionary_DataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
@Service
public class Non_Ht_CarrierServiceImpl extends ServiceImpl<Non_Ht_CarrierDao, Non_Ht_CarrierVO> implements INon_Ht_CarrierService {

    @Autowired
    private Non_Ht_CarrierDao non_ht_carrierDao;
    @Autowired
    private Non_Ht_Carrier_FreightDao non_ht_carrier_freightDao;
    @Autowired
    private Sys_Dictionary_DataDao sys_dictionary_dataDao;

    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Non_Ht_CarrierVO> list = non_ht_carrierDao.getListForGrid(jqGridParamModel);
        int records = non_ht_carrierDao.getListForGridCount(jqGridParamModel);
        // 获取用户所有角色
        return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
    }

    @Override
    @Transactional
    public ResultVO saveDetail(Non_Ht_CarrierDTO requestHt_CarrierDTO, SysUserVO sysUserVO, Contract_TypeEnum contract_typeEnum) {

        Non_Ht_CarrierVO requestHt_carrierVO = requestHt_CarrierDTO.getNon_ht_carrierVO();
        List<Non_Ht_Carrier_FreightVO> requestHt_Carrier_FreightVOList = requestHt_CarrierDTO.getNon_ht_carrier_freightVOList();


        if(StringUtils.isBlank(requestHt_carrierVO.getCarrier_no())){
            return ResultVO.failResult("承运商不能为空");
        }

        if(StringUtils.isBlank(requestHt_carrierVO.getContract_no())){
            return ResultVO.failResult("合同编号不能为空");
        }

        if(StringUtils.isBlank(requestHt_carrierVO.getContract_name())){
            return ResultVO.failResult("合同名称不能为空");
        }

      /*  if(StringUtils.isBlank(requestHt_carrierVO.getCus_contract_no())){
            return ResultVO.failResult("客户合同号不能为空");
        }*/
       /* if(requestHt_carrierVO.getTax_rate() == null || requestHt_carrierVO.getTax_rate().doubleValue() <= 0){
            return ResultVO.failResult("税率不能为空&值不能少于等于0");
        }*/

        if(requestHt_carrierVO.getBegin_date() == null || requestHt_carrierVO.getEnd_date() == null){
            return ResultVO.failResult("合同有效期起,止不能为空");
        }else{
            if(!(requestHt_carrierVO.getBegin_date().getTime() < requestHt_carrierVO.getEnd_date().getTime())){
                return ResultVO.failResult("合同有效期起必须小于止");
            }
        }

        if(requestHt_Carrier_FreightVOList.size() == 0){
            return ResultVO.failResult("必须录入一笔合同规则明细");
        }


        List<String> msgList = verificationDetail(requestHt_Carrier_FreightVOList, contract_typeEnum);
        if(msgList.size() > 0){
            return ResultVO.failResult(ArrayUtils.join(msgList, "<br/>"));
        }


        requestHt_carrierVO.setState(Contract_StateEnum.NORMAL.getCode());

        if(StringUtils.isBlank(requestHt_carrierVO.getSheet_no())){
            //新增合同，验证合同编号是否唯一

          /*   Non_Ht_CarrierVO paramHt_CarrierVO = new Non_Ht_CarrierVO();
            paramHt_CarrierVO.setContract_no(requestHt_carrierVO.getContract_no());
           Non_Ht_CarrierVO dataHt_CarrierVO = non_ht_carrierDao.selectOne(paramHt_CarrierVO);
            if(dataHt_CarrierVO != null){
                return ResultVO.failResult("合同编号已存在,请勿重复录入"+requestHt_carrierVO.getContract_no());
            }*/
            String sheet_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.HT_CARRIER);
            requestHt_carrierVO.setSheet_no(sheet_no);
            requestHt_carrierVO.setCreate_by(sysUserVO.getRealName());
            non_ht_carrierDao.insert(requestHt_carrierVO);
        }else{
            Non_Ht_CarrierVO paramHt_CarrierVO = new Non_Ht_CarrierVO();
            paramHt_CarrierVO.setSheet_no(requestHt_carrierVO.getSheet_no());
            Non_Ht_CarrierVO dataHt_CarrierVO = non_ht_carrierDao.selectOne(paramHt_CarrierVO);
            if(dataHt_CarrierVO == null || !dataHt_CarrierVO.getSheet_no().equals(requestHt_carrierVO.getSheet_no())){
                return ResultVO.failResult("该合同编号数据异常,请联系管理员"+requestHt_carrierVO.getContract_no());
            }
            if(!Contract_StateEnum.NORMAL.getCode().equals(dataHt_CarrierVO.getState())){
                return ResultVO.failResult("只允许正常状态下的合同进行修改操作");
            }
            requestHt_carrierVO.setSheet_no(dataHt_CarrierVO.getSheet_no());
            requestHt_carrierVO.setId(dataHt_CarrierVO.getId());
            requestHt_carrierVO.setCreate_by(null);
            non_ht_carrierDao.updateById(requestHt_carrierVO);

            EntityWrapper ht_carrier_freightEntityWrapper = new EntityWrapper();
            Non_Ht_Carrier_FreightVO paramHt_Carrier_FreightVO = new Non_Ht_Carrier_FreightVO();
            paramHt_Carrier_FreightVO.setSheet_no(requestHt_carrierVO.getSheet_no());
            ht_carrier_freightEntityWrapper.setEntity(paramHt_Carrier_FreightVO);
            List<Non_Ht_Carrier_FreightVO> dataHt_Carrier_FreightVOList =
                    non_ht_carrier_freightDao.selectList(ht_carrier_freightEntityWrapper);

            //如果老数据里不存在新的数据List，就删除，存在就新增
            List<Non_Ht_Carrier_FreightVO> deleteHt_Carrier_FreightVOList = new ArrayList<Non_Ht_Carrier_FreightVO>();
            List<Non_Ht_Carrier_FreightVO> updateHt_Carrier_FreightVOList = new ArrayList<Non_Ht_Carrier_FreightVO>();

            for(Non_Ht_Carrier_FreightVO dataHt_Carrier_FreightVO: dataHt_Carrier_FreightVOList){
                boolean bool = false;
                for(Non_Ht_Carrier_FreightVO requestHt_Carrier_FreightVO: requestHt_Carrier_FreightVOList){
                    if(dataHt_Carrier_FreightVO.getSn().equals(requestHt_Carrier_FreightVO.getSn())){
                        updateHt_Carrier_FreightVOList.add(requestHt_Carrier_FreightVO);
                        bool = true;
                        break;
                    }
                }
                if(!bool){
                    deleteHt_Carrier_FreightVOList.add(dataHt_Carrier_FreightVO);
                }
            }

            for(Non_Ht_Carrier_FreightVO deleteVO: deleteHt_Carrier_FreightVOList){
                non_ht_carrier_freightDao.deleteById(deleteVO.getSn());
            }
            for(Non_Ht_Carrier_FreightVO updateVO: updateHt_Carrier_FreightVOList){
                non_ht_carrier_freightDao.updateById(updateVO);
            }

        }


        for(Non_Ht_Carrier_FreightVO vo: requestHt_Carrier_FreightVOList){
            if(vo.getSn() == null || vo.getSn() == 0){
                //初始化规则明细表一些外键关系
                vo.setSheet_no(requestHt_carrierVO.getSheet_no());
                non_ht_carrier_freightDao.insert(vo);
            }
        }
        ResultVO resultVO = ResultVO.successResult();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("sheet_no", requestHt_carrierVO.getSheet_no());
        returnMap.put("msg", "保存成功");
        resultVO.setResultDataFull(returnMap);
        return resultVO;
    }

    @Override
    public Non_Ht_CarrierDTO getDetail(Non_Ht_CarrierVO ht_carrierVO) {
        Non_Ht_CarrierVO paramHt_CarrierVO = new Non_Ht_CarrierVO();
        paramHt_CarrierVO.setSheet_no(ht_carrierVO.getSheet_no());
        Non_Ht_CarrierVO dataHt_CarrierVO = non_ht_carrierDao.selectOne(paramHt_CarrierVO);
        if(dataHt_CarrierVO == null){
            return null;
        }
        EntityWrapper ht_carrier_freightEntityWrapper = new EntityWrapper();
        Non_Ht_Carrier_FreightVO paramHt_Carrier_FreightVO = new Non_Ht_Carrier_FreightVO();
        paramHt_Carrier_FreightVO.setSheet_no(ht_carrierVO.getSheet_no());
        ht_carrier_freightEntityWrapper.setEntity(paramHt_Carrier_FreightVO);
        ht_carrier_freightEntityWrapper.orderBy("sn", true);
        List<Non_Ht_Carrier_FreightVO> dataHt_Carrier_FreightVOList = non_ht_carrier_freightDao.selectList(ht_carrier_freightEntityWrapper);
        Non_Ht_CarrierDTO returnHt_carrierDTO = new Non_Ht_CarrierDTO();
        returnHt_carrierDTO.setNon_ht_carrierVO(dataHt_CarrierVO);
        returnHt_carrierDTO.setNon_ht_carrier_freightVOList(dataHt_Carrier_FreightVOList);

        return returnHt_carrierDTO;
    }


    private List<String> verificationDetail(List<Non_Ht_Carrier_FreightVO> requestHt_Carrier_FreightVOList, Contract_TypeEnum contract_TypeEnum){
        EntityWrapper param = new EntityWrapper();
        Sys_Dictionary_DataVO sys_dictionary_dataVO = new Sys_Dictionary_DataVO();
        sys_dictionary_dataVO.setTypecode(SysDictionaryDataConstant.TRANS_MODE);
        param.setEntity(sys_dictionary_dataVO);
        List<Sys_Dictionary_DataVO> trans_modeList = sys_dictionary_dataDao.selectList(param);
        Set<String> trans_modeSet = new HashSet<String>();
        for(Sys_Dictionary_DataVO vo: trans_modeList){
            trans_modeSet.add(vo.getDictext());
        }

        List<String> msgList = new ArrayList<String>();
        for(int i = 0, line = 1; i < requestHt_Carrier_FreightVOList.size(); i++, line++){
            Non_Ht_Carrier_FreightVO requestHt_Carrier_FreightVO = requestHt_Carrier_FreightVOList.get(i);
            if(StringUtils.isBlank(requestHt_Carrier_FreightVO.getTrans_mode())){
                msgList.add("第"+line+"行规则明细,运输方式不能为空");
            }else if(!trans_modeSet.contains(requestHt_Carrier_FreightVO.getTrans_mode())){
                msgList.add("第"+line+"行规则明细,运输方式不存在");
            }
            if(requestHt_Carrier_FreightVO.getFirst_mileage() == null){
                msgList.add("第"+line+"行规则明细,第一段里程不能为空");
            }
            if(requestHt_Carrier_FreightVO.getFirst_price() == null){
                msgList.add("第"+line+"行规则明细,第一段单价不能为空");
            }
            if(requestHt_Carrier_FreightVO.getTwo_mileage() == null){
                msgList.add("第"+line+"行规则明细,第二段里程不能为空");
            }
            if(requestHt_Carrier_FreightVO.getTwo_price() == null){
                msgList.add("第"+line+"行规则明细,第二段单价不能为空");
            }
            if(requestHt_Carrier_FreightVO.getThree_mileage() == null){
                msgList.add("第"+line+"行规则明细,第三段里程不能为空");
            }
            if(requestHt_Carrier_FreightVO.getThree_price() == null){
                msgList.add("第"+line+"行规则明细,第三段单价不能为空");
            }
            if(StringUtils.isBlank(requestHt_Carrier_FreightVO.getBegin_city())){
                msgList.add("第"+line+"行规则明细,起运地不能为空");
            }
            if(StringUtils.isBlank(requestHt_Carrier_FreightVO.getEnd_city())){
                msgList.add("第"+line+"行规则明细,目的地不能为空");
            }
            /*if(requestHt_Carrier_FreightVO.getCross_sea_amount() == null){
                msgList.add("第"+line+"行规则明细,过海费不能为空");
            }*/

            /*if(contract_TypeEnum == Contract_TypeEnum.FORMAL){
                //正式合同，要检验保费
                if(requestHt_Carrier_FreightVO.getPremium() == null || requestHt_Carrier_FreightVO.getPremium().doubleValue() < 0){
                    msgList.add("第"+line+"行规则明细,保费不能为空&少于0");
                }
            }*/
        }
        return msgList;
    }

    @Override
    public ResultVO importData(MultipartFile multipartFile, Contract_TypeEnum contract_typeEnum) {

        List<Non_Ht_Carrier_FreightVO> requestHt_Carrier_FreightVOList = null;
        try {
            requestHt_Carrier_FreightVOList = ExcelImportUtil.importExcel(multipartFile.getInputStream(), Non_Ht_Carrier_FreightVO.class, new ImportParams());
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> msgList = verificationDetail(requestHt_Carrier_FreightVOList, contract_typeEnum);
        if(msgList.size() > 0){
            return ResultVO.failResult(ArrayUtils.join(msgList, "<br/>"));
        }
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(requestHt_Carrier_FreightVOList);
        return resultVO;
    }

    @Override
    public ResultVO check(Non_Ht_CarrierVO ht_carrierVO, SysUserVO sysUserVO) {
        if(StringUtils.isBlank(ht_carrierVO.getSheet_no())){
            return ResultVO.failResult("合同单号不能为空");
        }
        Non_Ht_CarrierVO paramHt_CarrierVO = new Non_Ht_CarrierVO();
        paramHt_CarrierVO.setSheet_no(ht_carrierVO.getSheet_no());
        Non_Ht_CarrierVO dataHt_CarrierVO = non_ht_carrierDao.selectOne(paramHt_CarrierVO);
        if(dataHt_CarrierVO == null){
            return ResultVO.failResult("合同不存在");
        }else if(!Contract_StateEnum.NORMAL.getCode().equals(dataHt_CarrierVO.getState())){
            return ResultVO.failResult("只允许正常状态下的合同进行审核操作");
        }
        dataHt_CarrierVO.setState(Contract_StateEnum.CHECK.getCode());
        dataHt_CarrierVO.setCheck_by(sysUserVO.getRealName());
        dataHt_CarrierVO.setCheck_date(new Date());
        non_ht_carrierDao.updateById(dataHt_CarrierVO);
        return ResultVO.successResult("审核成功");
    }

    @Override
    public ResultVO uncheck(Non_Ht_CarrierVO ht_carrierVO, SysUserVO sysUserVO) {
        if(StringUtils.isBlank(ht_carrierVO.getSheet_no())){
            return ResultVO.failResult("合同单号不能为空");
        }
        Non_Ht_CarrierVO paramHt_CarrierVO = new Non_Ht_CarrierVO();
        paramHt_CarrierVO.setSheet_no(ht_carrierVO.getSheet_no());
        Non_Ht_CarrierVO dataHt_CarrierVO = non_ht_carrierDao.selectOne(paramHt_CarrierVO);
        if(dataHt_CarrierVO == null){
            return ResultVO.failResult("合同不存在");
        }else if(!Contract_StateEnum.CHECK.getCode().equals(dataHt_CarrierVO.getState())){
            return ResultVO.failResult("只允许审核状态下的合同进行反审核操作");
        }
        dataHt_CarrierVO.setState(Contract_StateEnum.NORMAL.getCode());
        dataHt_CarrierVO.setCheck_by(sysUserVO.getRealName());
        dataHt_CarrierVO.setCheck_date(new Date());
        non_ht_carrierDao.updateById(dataHt_CarrierVO);
        return ResultVO.successResult("反审核成功");
    }

    @Override
    public ResultVO cancel(Non_Ht_CarrierVO ht_carrierVO, SysUserVO sysUserVO) {
        if(StringUtils.isBlank(ht_carrierVO.getSheet_no())){
            return ResultVO.failResult("合同单号不能为空");
        }
        Non_Ht_CarrierVO paramHt_CarrierVO = new Non_Ht_CarrierVO();
        paramHt_CarrierVO.setSheet_no(ht_carrierVO.getSheet_no());
        Non_Ht_CarrierVO dataHt_CarrierVO = non_ht_carrierDao.selectOne(paramHt_CarrierVO);
        if(dataHt_CarrierVO == null){
            return ResultVO.failResult("合同不存在");
        }else if(!Contract_StateEnum.NORMAL.getCode().equals(dataHt_CarrierVO.getState())){
            return ResultVO.failResult("只允许正常状态下的合同进行注销操作");
        }
        dataHt_CarrierVO.setState(Contract_StateEnum.CANCEL.getCode());
        dataHt_CarrierVO.setCheck_by(sysUserVO.getRealName());
        dataHt_CarrierVO.setCheck_date(new Date());
        non_ht_carrierDao.updateById(dataHt_CarrierVO);
        return ResultVO.successResult("注销");
    }
}
