package com.bba.wdgl.service.api;
import com.baomidou.mybatisplus.service.IService;
import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.wdgl.vo.QusetionsVO;
import com.bba.xtgl.vo.SysUserVO;
import java.util.List;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/30 13:24
 */
public interface IQuestionsService extends IService<QusetionsVO> {
    PageVO getListForGrid(JqGridParamModel jqGridParamModel, String customSearchFilters);

    ResultVO saveDetail(QusetionsVO vo, SysUserVO sysUserVO);

    ResultVO cancel(QusetionsVO vo, SysUserVO sysUserVO);

    QusetionsVO getDetail(QusetionsVO vo);

    ResultVO check(List<QusetionsVO> vos, SysUserVO sysUserVO);
}
