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
import com.bba.ht.dao.Non_Ht_CusDao;
import com.bba.ht.dao.Non_Ht_Cus_FreightDao;
import com.bba.ht.dto.Non_Ht_CusDTO;
import com.bba.ht.service.api.INon_Ht_CusService;
import com.bba.ht.vo.Non_Ht_CusVO;
import com.bba.ht.vo.Non_Ht_Cus_FreightVO;
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
public class Non_Ht_CusServiceImpl extends ServiceImpl<Non_Ht_CusDao, Non_Ht_CusVO> implements INon_Ht_CusService {

    @Autowired
    private Non_Ht_CusDao ht_cusDao;
    @Autowired
    private Non_Ht_Cus_FreightDao ht_cus_freightDao;
    @Autowired
    private Sys_Dictionary_DataDao sys_dictionary_dataDao;

    @Override
    public PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters) {
        String filters = JqGridSearchParamHandler.processSql(customSearchFilters, jqGridParamModel);
        jqGridParamModel.setFilters(filters);
        List<Non_Ht_CusVO> list = ht_cusDao.getListForGrid(jqGridParamModel);
        int records = ht_cusDao.getListForGridCount(jqGridParamModel);
        // 获取用户所有角色
        return new PageVO(jqGridParamModel.getPage(), jqGridParamModel.getRows(), list, records);
    }

    @Override
    @Transactional
    public ResultVO saveDetail(Non_Ht_CusDTO requestHt_CusDTO, SysUserVO sysUserVO, Contract_TypeEnum contract_typeEnum) {

        Non_Ht_CusVO requestHt_cusVO = requestHt_CusDTO.getNon_ht_cusVO();
        List<Non_Ht_Cus_FreightVO> requestHt_Cus_FreightVOList = requestHt_CusDTO.getNon_ht_cus_freightVOList();


        if(StringUtils.isBlank(requestHt_cusVO.getCus_no())){
            return ResultVO.failResult("客户不能为空");
        }

        if(StringUtils.isBlank(requestHt_cusVO.getContract_no())){
            return ResultVO.failResult("合同编号不能为空");
        }

        if(StringUtils.isBlank(requestHt_cusVO.getContract_name())){
            return ResultVO.failResult("合同名称不能为空");
        }

        if(StringUtils.isBlank(requestHt_cusVO.getCus_contract_no())){
            return ResultVO.failResult("客户合同号不能为空");
        }
        /*if(requestHt_cusVO.getTax_rate() == null || requestHt_cusVO.getTax_rate().doubleValue() <= 0){
            return ResultVO.failResult("税率不能为空&值不能少于等于0");
        }*/

        if(requestHt_cusVO.getBegin_date() == null || requestHt_cusVO.getEnd_date() == null){
            return ResultVO.failResult("合同有效期起,止不能为空");
        }else{
            if(!(requestHt_cusVO.getBegin_date().getTime() < requestHt_cusVO.getEnd_date().getTime())){
                return ResultVO.failResult("合同有效期起必须小于止");
            }
        }

        if(requestHt_Cus_FreightVOList.size() == 0){
            return ResultVO.failResult("必须录入一笔合同规则明细");
        }


        List<String> msgList = verificationDetail(requestHt_Cus_FreightVOList, contract_typeEnum);
        if(msgList.size() > 0){
            return ResultVO.failResult(ArrayUtils.join(msgList, "<br/>"));
        }


        requestHt_cusVO.setState(Contract_StateEnum.NORMAL.getCode());

        if(StringUtils.isBlank(requestHt_cusVO.getSheet_no())){
            //新增合同，验证合同编号是否唯一

           /*  Non_Ht_CusVO paramHt_CusVO = new Non_Ht_CusVO();
            paramHt_CusVO.setContract_no(requestHt_cusVO.getContract_no());
           Non_Ht_CusVO dataHt_CusVO = ht_cusDao.selectOne(paramHt_CusVO);
            if(dataHt_CusVO != null){
                return ResultVO.failResult("合同编号已存在,请勿重复录入"+requestHt_cusVO.getContract_no());
            }*/
            String sheet_no = Sys_sheetidUtil.getSys_sheetid(Sys_sheetidUtil.HT_CUS);
            requestHt_cusVO.setSheet_no(sheet_no);
            requestHt_cusVO.setCreate_by(sysUserVO.getRealName());
            ht_cusDao.insert(requestHt_cusVO);
        }else{
            Non_Ht_CusVO paramHt_CusVO = new Non_Ht_CusVO();
            paramHt_CusVO.setSheet_no(requestHt_cusVO.getSheet_no());
            Non_Ht_CusVO dataHt_CusVO = ht_cusDao.selectOne(paramHt_CusVO);
            if(dataHt_CusVO == null || !dataHt_CusVO.getSheet_no().equals(requestHt_cusVO.getSheet_no())){
                return ResultVO.failResult("该合同编号数据异常,请联系管理员"+requestHt_cusVO.getContract_no());
            }
            if(!Contract_StateEnum.NORMAL.getCode().equals(dataHt_CusVO.getState())){
                return ResultVO.failResult("只允许正常状态下的合同进行修改操作");
            }
            requestHt_cusVO.setSheet_no(dataHt_CusVO.getSheet_no());
            requestHt_cusVO.setId(dataHt_CusVO.getId());
            requestHt_cusVO.setCreate_by(null);
            ht_cusDao.updateById(requestHt_cusVO);

            EntityWrapper ht_cus_freightEntityWrapper = new EntityWrapper();
            Non_Ht_Cus_FreightVO paramHt_Cus_FreightVO = new Non_Ht_Cus_FreightVO();
            paramHt_Cus_FreightVO.setSheet_no(requestHt_cusVO.getSheet_no());
            ht_cus_freightEntityWrapper.setEntity(paramHt_Cus_FreightVO);
            List<Non_Ht_Cus_FreightVO> dataHt_Cus_FreightVOList = ht_cus_freightDao.selectList(ht_cus_freightEntityWrapper);

            //如果老数据里不存在新的数据List，就删除，存在就新增
            List<Non_Ht_Cus_FreightVO> deleteHt_Cus_FreightVOList = new ArrayList<Non_Ht_Cus_FreightVO>();
            List<Non_Ht_Cus_FreightVO> updateHt_Cus_FreightVOList = new ArrayList<Non_Ht_Cus_FreightVO>();

            for(Non_Ht_Cus_FreightVO dataHt_Cus_FreightVO: dataHt_Cus_FreightVOList){
                boolean bool = false;
                for(Non_Ht_Cus_FreightVO requestHt_Cus_FreightVO: requestHt_Cus_FreightVOList){
                    if(dataHt_Cus_FreightVO.getSn().equals(requestHt_Cus_FreightVO.getSn())){
                        updateHt_Cus_FreightVOList.add(requestHt_Cus_FreightVO);
                        bool = true;
                        break;
                    }
                }
                if(!bool){
                    deleteHt_Cus_FreightVOList.add(dataHt_Cus_FreightVO);
                }
            }

            for(Non_Ht_Cus_FreightVO deleteVO: deleteHt_Cus_FreightVOList){
                ht_cus_freightDao.deleteById(deleteVO.getSn());
            }
            for(Non_Ht_Cus_FreightVO updateVO: updateHt_Cus_FreightVOList){
                ht_cus_freightDao.updateById(updateVO);
            }

        }


        for(Non_Ht_Cus_FreightVO vo: requestHt_Cus_FreightVOList){
            if(vo.getSn() == null || vo.getSn() == 0){
                //初始化规则明细表一些外键关系
                vo.setSheet_no(requestHt_cusVO.getSheet_no());
                ht_cus_freightDao.insert(vo);
            }
        }
        ResultVO resultVO = ResultVO.successResult();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("sheet_no", requestHt_cusVO.getSheet_no());
        returnMap.put("msg", "保存成功");
        resultVO.setResultDataFull(returnMap);
        return resultVO;
    }

    @Override
    public Non_Ht_CusDTO getDetail(Non_Ht_CusVO ht_cusVO) {
        Non_Ht_CusVO paramHt_CusVO = new Non_Ht_CusVO();
        paramHt_CusVO.setSheet_no(ht_cusVO.getSheet_no());
        Non_Ht_CusVO dataHt_CusVO = ht_cusDao.selectOne(paramHt_CusVO);
        if(dataHt_CusVO == null){
            return null;
        }
        EntityWrapper ht_cus_freightEntityWrapper = new EntityWrapper();
        Non_Ht_Cus_FreightVO paramHt_Cus_FreightVO = new Non_Ht_Cus_FreightVO();
        paramHt_Cus_FreightVO.setSheet_no(ht_cusVO.getSheet_no());
        ht_cus_freightEntityWrapper.setEntity(paramHt_Cus_FreightVO);
        ht_cus_freightEntityWrapper.orderBy("sn", true);
        List<Non_Ht_Cus_FreightVO> dataHt_Cus_FreightVOList = ht_cus_freightDao.selectList(ht_cus_freightEntityWrapper);
        Non_Ht_CusDTO returnHt_cusDTO = new Non_Ht_CusDTO();
        returnHt_cusDTO.setNon_ht_cusVO(dataHt_CusVO);
        returnHt_cusDTO.setNon_ht_cus_freightVOList(dataHt_Cus_FreightVOList);

        return returnHt_cusDTO;
    }


    private List<String> verificationDetail(List<Non_Ht_Cus_FreightVO> requestHt_Cus_FreightVOList, Contract_TypeEnum contract_TypeEnum){
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
        for(int i = 0, line = 1; i < requestHt_Cus_FreightVOList.size(); i++, line++){
            Non_Ht_Cus_FreightVO requestHt_Cus_FreightVO = requestHt_Cus_FreightVOList.get(i);
            if(StringUtils.isBlank(requestHt_Cus_FreightVO.getTrans_mode())){
                msgList.add("第"+line+"行规则明细,运输方式不能为空");
            }else if(!trans_modeSet.contains(requestHt_Cus_FreightVO.getTrans_mode())){
                msgList.add("第"+line+"行规则明细,运输方式不存在");
            }
            if(requestHt_Cus_FreightVO.getFirst_mileage() == null){
                msgList.add("第"+line+"行规则明细,第一段里程不能为空");
            }
            if(requestHt_Cus_FreightVO.getFirst_price() == null){
                msgList.add("第"+line+"行规则明细,第一段单价不能为空");
            }
            if(requestHt_Cus_FreightVO.getTwo_mileage() == null){
                msgList.add("第"+line+"行规则明细,第二段里程不能为空");
            }
            if(requestHt_Cus_FreightVO.getTwo_price() == null){
                msgList.add("第"+line+"行规则明细,第二段单价不能为空");
            }
            if(requestHt_Cus_FreightVO.getThree_mileage() == null){
                msgList.add("第"+line+"行规则明细,第三段里程不能为空");
            }
            if(requestHt_Cus_FreightVO.getThree_price() == null){
                msgList.add("第"+line+"行规则明细,第三段单价不能为空");
            }
            if(StringUtils.isBlank(requestHt_Cus_FreightVO.getBegin_city())){
                msgList.add("第"+line+"行规则明细,起运地不能为空");
            }
            if(StringUtils.isBlank(requestHt_Cus_FreightVO.getEnd_city())){
                msgList.add("第"+line+"行规则明细,目的地不能为空");
            }
           /* if(requestHt_Cus_FreightVO.getCross_sea_amount() == null){
                msgList.add("第"+line+"行规则明细,过海费不能为空");
            }*/

            if(contract_TypeEnum == Contract_TypeEnum.FORMAL){
                //正式合同，要检验保费
                if(requestHt_Cus_FreightVO.getPremium() == null || requestHt_Cus_FreightVO.getPremium().doubleValue() < 0){
                    msgList.add("第"+line+"行规则明细,保费不能为空&少于0");
                }
            }
        }
        return msgList;
    }

    @Override
    public ResultVO importData(MultipartFile multipartFile, Contract_TypeEnum contract_typeEnum) {

        List<Non_Ht_Cus_FreightVO> requestHt_Cus_FreightVOList = null;
        try {
            requestHt_Cus_FreightVOList = ExcelImportUtil.importExcel(multipartFile.getInputStream(), Non_Ht_Cus_FreightVO.class, new ImportParams());
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> msgList = verificationDetail(requestHt_Cus_FreightVOList, contract_typeEnum);
        if(msgList.size() > 0){
            return ResultVO.failResult(ArrayUtils.join(msgList, "<br/>"));
        }
        ResultVO resultVO = ResultVO.successResult();
        resultVO.setResultDataFull(requestHt_Cus_FreightVOList);
        return resultVO;
    }

    @Override
    public ResultVO check(Non_Ht_CusVO ht_cusVO, SysUserVO sysUserVO) {
        if(StringUtils.isBlank(ht_cusVO.getSheet_no())){
            return ResultVO.failResult("合同单号不能为空");
        }
        Non_Ht_CusVO paramHt_CusVO = new Non_Ht_CusVO();
        paramHt_CusVO.setSheet_no(ht_cusVO.getSheet_no());
        Non_Ht_CusVO dataHt_CusVO = ht_cusDao.selectOne(paramHt_CusVO);
        if(dataHt_CusVO == null){
            return ResultVO.failResult("合同不存在");
        }else if(!Contract_StateEnum.NORMAL.getCode().equals(dataHt_CusVO.getState())){
            return ResultVO.failResult("只允许正常状态下的合同进行审核操作");
        }
        dataHt_CusVO.setState(Contract_StateEnum.CHECK.getCode());
        dataHt_CusVO.setCheck_by(sysUserVO.getRealName());
        dataHt_CusVO.setCheck_date(new Date());
        ht_cusDao.updateById(dataHt_CusVO);
        return ResultVO.successResult("审核成功");
    }

    @Override
    public ResultVO uncheck(Non_Ht_CusVO ht_cusVO, SysUserVO sysUserVO) {
        if(StringUtils.isBlank(ht_cusVO.getSheet_no())){
            return ResultVO.failResult("合同单号不能为空");
        }
        Non_Ht_CusVO paramHt_CusVO = new Non_Ht_CusVO();
        paramHt_CusVO.setSheet_no(ht_cusVO.getSheet_no());
        Non_Ht_CusVO dataHt_CusVO = ht_cusDao.selectOne(paramHt_CusVO);
        if(dataHt_CusVO == null){
            return ResultVO.failResult("合同不存在");
        }else if(!Contract_StateEnum.CHECK.getCode().equals(dataHt_CusVO.getState())){
            return ResultVO.failResult("只允许审核状态下的合同进行反审核操作");
        }
        dataHt_CusVO.setState(Contract_StateEnum.NORMAL.getCode());
        dataHt_CusVO.setCheck_by(sysUserVO.getRealName());
        dataHt_CusVO.setCheck_date(new Date());
        ht_cusDao.updateById(dataHt_CusVO);
        return ResultVO.successResult("反审核成功");
    }

    @Override
    public ResultVO cancel(Non_Ht_CusVO ht_cusVO, SysUserVO sysUserVO) {
        if(StringUtils.isBlank(ht_cusVO.getSheet_no())){
            return ResultVO.failResult("合同单号不能为空");
        }
        Non_Ht_CusVO paramHt_CusVO = new Non_Ht_CusVO();
        paramHt_CusVO.setSheet_no(ht_cusVO.getSheet_no());
        Non_Ht_CusVO dataHt_CusVO = ht_cusDao.selectOne(paramHt_CusVO);
        if(dataHt_CusVO == null){
            return ResultVO.failResult("合同不存在");
        }else if(!Contract_StateEnum.NORMAL.getCode().equals(dataHt_CusVO.getState())){
            return ResultVO.failResult("只允许正常状态下的合同进行注销操作");
        }
        dataHt_CusVO.setState(Contract_StateEnum.CANCEL.getCode());
        dataHt_CusVO.setCheck_by(sysUserVO.getRealName());
        dataHt_CusVO.setCheck_date(new Date());
        ht_cusDao.updateById(dataHt_CusVO);
        return ResultVO.successResult("注销");
    }
}
