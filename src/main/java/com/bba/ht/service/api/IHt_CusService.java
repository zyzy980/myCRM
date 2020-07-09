package com.bba.ht.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.constant.Contract_TypeEnum;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.ht.dto.Ht_CusDTO;
import com.bba.ht.vo.Ht_CusVO;
import com.bba.ht.vo.Ht_Cus_FreightVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysUserVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
public interface IHt_CusService extends IService<Ht_CusVO> {

    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO saveDetail(Ht_CusDTO requestHt_cusDTO, SysUserVO sysUserVO, Contract_TypeEnum contract_typeEnum);

    Ht_CusDTO getDetail(Ht_CusVO ht_cusVO);

    ResultVO importData(MultipartFile multipartFile, Contract_TypeEnum contract_typeEnum);

    ResultVO check(Ht_CusVO ht_cusVO, SysUserVO sysUserVO);

    ResultVO uncheck(Ht_CusVO ht_cusVO, SysUserVO sysUserVO);

    ResultVO cancel(Ht_CusVO ht_cusVO, SysUserVO sysUserVO);


}
