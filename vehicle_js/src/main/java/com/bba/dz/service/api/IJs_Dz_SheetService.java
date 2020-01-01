package com.bba.dz.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.dz.vo.Js_Dz_SheetVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysUserVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baozha
 * @since 2019-07-02
 */
public interface IJs_Dz_SheetService extends IService<Js_Dz_SheetVO> {

    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);


    ResultVO business_check(Js_Dz_SheetVO vo, SysUserVO sysUserVO);

    ResultVO finance_check(Js_Dz_SheetVO vo, SysUserVO sysUserVO);

    ResultVO send_mail(Js_Dz_SheetVO vo, SysUserVO sysUserVO);

    ResultVO cus_check(Js_Dz_SheetVO vo, SysUserVO sysUserVO);

    ResultVO uncheck(List<String> list, SysUserVO sysUserVO);
}
