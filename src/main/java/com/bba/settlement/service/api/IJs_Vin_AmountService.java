package com.bba.settlement.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.constant.Contract_TypeEnum;
import com.bba.common.service.api.IBaseService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.ht.dto.Ht_CusDTO;
import com.bba.ht.vo.Ht_CusVO;
import com.bba.settlement.vo.Js_Vin_AmountVO;
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
public interface IJs_Vin_AmountService extends IService<Js_Vin_AmountVO> {

    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO settlementDetail(List<String> list, SysUserVO sysUserVO);

    ResultVO two_settlementDetail(List<String> list, SysUserVO sysUserVO);

    ResultVO un_settlement(List<String> list, SysUserVO sysUserVO);

    ResultVO create_bill(List<String> list, SysUserVO sysUserVO,String type);

    public ResultVO importData(MultipartFile multipartFile);

    PageVO getListForGridBaobiao(JqGridParamModel jqGridParamModel, String customSearchFilters);


    List<Ht_CusVO> findHt_cusVO(Ht_CusVO vo);
}
