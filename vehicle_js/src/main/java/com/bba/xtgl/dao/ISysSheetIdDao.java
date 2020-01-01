package com.bba.xtgl.dao;

import com.bba.util.JqGridParamModel;
import com.bba.xtgl.vo.SysSheetIdVO;
import com.bba.xtgl.vo.Sys_SheetIdVO;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ISysSheetIdDao{


	public void USP_GET_SHEETID(SysSheetIdVO sysSheetIdVO);

	public List<Sys_SheetIdVO> findAllList(Sys_SheetIdVO vo);

	public void insert(List<Sys_SheetIdVO> list);

	/**
	 * 查询列表数据
	 * @Author bcmaidou
	 * @Date 17:07 2019/3/28
	 */
    List<SysSheetIdVO> searchList(JqGridParamModel jqGridParamModel);

    /**
     * 查询单号详情
     * @Author bcmaidou
     * @Date 18:58 2019/3/28
     */
	SysSheetIdVO searchDetail(@Param("sn") String sn);

	/**
	 * 新增单据号
	 * @Author bcmaidou
	 * @Date 9:35 2019/3/29
	 */
//	@Options(useGeneratedKeys = true, keyProperty = "item.sn")
	void insertDetail(SysSheetIdVO sysSheetIdVO);

	/**
	 * 修改单据号
	 * @Author bcmaidou
	 * @Date 9:35 2019/3/29
	 */
	void updateDetail(@Param("item") SysSheetIdVO sysSheetIdVO);

	/**
	 * 批量删除单据号
	 * @Author bcmaidou
	 * @Date 9:36 2019/3/29
	 */
	void delDetail(@Param("sn")String sn);

	void insertDetail2(@Param("item")Sys_SheetIdVO sys_sheetIdVO);

    String findCurId();

}
