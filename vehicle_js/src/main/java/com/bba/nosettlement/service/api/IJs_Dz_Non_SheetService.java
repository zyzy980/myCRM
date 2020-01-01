package com.bba.nosettlement.service.api;

import com.baomidou.mybatisplus.service.IService;
import com.bba.common.service.api.IBaseService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.dz.vo.Js_Dz_SheetAccountVO;
import com.bba.dz.vo.Js_Dz_SheetVO;
import com.bba.nosettlement.vo.Js_Dz_Non_SheetVO;
import com.bba.nosettlement.vo.Js_Dz_Non_Sheet_DetailVO;
import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysUserVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface IJs_Dz_Non_SheetService extends IBaseService {

    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    PageVO getListForGridDetail(JqGridParamModel jqGridParamModel, String customSearchFilters);

    public void UpdateJs_Dz_Sheet_state(List<Js_Dz_Non_SheetVO> list);


    public void replayUpdateData(List<Js_Dz_Non_Sheet_DetailVO> list);
    public void deleteJs_Dz_Sheet(Js_Dz_Non_Sheet_DetailVO vo);
    public void updateJs_Dz_Sheet(Js_Dz_Non_Sheet_DetailVO vo);


    public ResultVO reback(List<Js_Dz_Non_SheetVO> list);

    public ResultVO save(Js_Dz_Non_SheetVO vo);

    public ResultVO  buildSheet(Js_Dz_Non_Sheet_DetailVO vo);

    Integer findNullBillNumber(Js_Dz_Non_SheetVO item);
}
