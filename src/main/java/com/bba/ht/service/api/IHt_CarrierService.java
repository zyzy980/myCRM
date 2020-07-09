package com.bba.ht.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.constant.Contract_TypeEnum;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.ht.dto.Ht_CarrierDTO;
import com.bba.ht.vo.Ht_CarrierVO;
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
public interface IHt_CarrierService extends IService<Ht_CarrierVO> {

    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO saveDetail(Ht_CarrierDTO requestHt_cusDTO, SysUserVO sysUserVO, Contract_TypeEnum contract_typeEnum);

    Ht_CarrierDTO getDetail(Ht_CarrierVO ht_cusVO);

    ResultVO importData(MultipartFile multipartFile, Contract_TypeEnum contract_typeEnum);

    ResultVO check(Ht_CarrierVO ht_cusVO, SysUserVO sysUserVO);

    ResultVO uncheck(Ht_CarrierVO ht_cusVO, SysUserVO sysUserVO);

    ResultVO cancel(Ht_CarrierVO ht_cusVO, SysUserVO sysUserVO);
}
