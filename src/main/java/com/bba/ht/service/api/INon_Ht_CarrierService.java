package com.bba.ht.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.constant.Contract_TypeEnum;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.ht.dto.Non_Ht_CarrierDTO;
import com.bba.ht.vo.Non_Ht_CarrierVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
public interface INon_Ht_CarrierService extends IService<Non_Ht_CarrierVO> {

    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO saveDetail(Non_Ht_CarrierDTO requestHt_cusDTO, SysUserVO sysUserVO, Contract_TypeEnum contract_typeEnum);

    Non_Ht_CarrierDTO getDetail(Non_Ht_CarrierVO ht_cusVO);

    ResultVO importData(MultipartFile multipartFile, Contract_TypeEnum contract_typeEnum);

    ResultVO check(Non_Ht_CarrierVO ht_cusVO, SysUserVO sysUserVO);

    ResultVO uncheck(Non_Ht_CarrierVO ht_cusVO, SysUserVO sysUserVO);

    ResultVO cancel(Non_Ht_CarrierVO ht_cusVO, SysUserVO sysUserVO);
}
