package com.bba.xtgl.dao;

import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.Sys_within_setVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISys_within_setDao {

	public List<Sys_within_setVO> findDataList(Sys_within_setVO vo);

	public void batchInsert(List<Sys_within_setVO> list);

	Sys_within_setVO searchWithin(String withinCode);

    void updateWithinSet(Sys_within_setVO sys_within_setVO);
}
