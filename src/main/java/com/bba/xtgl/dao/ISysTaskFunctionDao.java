package com.bba.xtgl.dao;

import java.util.List;

import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysTaskFunctionVO;

public interface ISysTaskFunctionDao {
	
	public List<SysTaskFunctionVO> getListForGrid(JqGridParamModel jqGridParamModel);
	
	public int getListForGridCount(JqGridParamModel jqGridParamModel);
	

}
