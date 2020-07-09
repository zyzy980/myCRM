package com.bba.jsyw.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.constant.BusinessData_projectEnum;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.jsyw.dto.Tr_BusinessDTO;
import com.bba.jsyw.vo.Tr_BusinessVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;

/**
 * 业务数据管理服务层
 *@Author:Suwendaidi
 *2019/7/11
 */
public interface ITr_BusinessService extends IService<Tr_BusinessVO> {

    PageVO findBusinessPageVO(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO updateJsState(List<Tr_BusinessVO> vos);

    Tr_BusinessDTO getDetail(Tr_BusinessVO vo);

    ResultVO importData(MultipartFile file, BusinessData_projectEnum businessData_projectEnum);

    ResultVO saveDetail(Tr_BusinessDTO requestTr_businessDTO, SysUserVO sysUserVO, MultipartFile commonsMultipartFile);

    ResultVO batchCancel(List<Tr_BusinessVO> tr_businessVOs,SysUserVO sysUserVO);

    ResultVO check(List<Tr_BusinessVO> vos,SysUserVO sysUserVO);

    ResultVO importMoreCarrier(MultipartFile file);
}
