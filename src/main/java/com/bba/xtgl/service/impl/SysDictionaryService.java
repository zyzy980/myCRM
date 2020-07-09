package com.bba.xtgl.service.impl;

import com.bba.common.vo.PageVO;
import com.bba.common.vo.ResultVO;
import com.bba.util.JqGridParamModel;
import com.bba.util.SessionUtils;
import com.bba.util.StringUtils;
import com.bba.xtgl.dao.SysDictionaryDao;
import com.bba.xtgl.service.api.ISysDictionaryService;
import com.bba.xtgl.vo.DictionaryVO;
import com.bba.xtgl.vo.SysDictionaryDataVO;
import com.bba.xtgl.vo.SysDictionaryVO;
import com.bba.xtgl.vo.SysUserVO;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
@Transactional
public class SysDictionaryService implements ISysDictionaryService {
	
	@Resource
	private SysDictionaryDao sysDictionaryDao;


	private Map<String,List<SysDictionaryDataVO>> dictionaryDataCacheMap = new HashMap<String,List<SysDictionaryDataVO>>();
	
	
	
	
	@Override
	public String saveDictionaryData(String typeCode, String dicText) {
		SysUserVO sysUserVO=SessionUtils.currentSession();
		List<SysDictionaryDataVO> dictionaryList = this.findDictionaryDataList(typeCode);
		Integer dicorder = 0;
		Integer dicValue = 0;
		for (SysDictionaryDataVO sysDictionaryData : dictionaryList) {
			if(sysDictionaryData.getDicValue()!=null && NumberUtils.isNumber(sysDictionaryData.getDicValue()) ){
				if(Integer.valueOf(sysDictionaryData.getDicValue()) >= dicValue){
					dicValue = Integer.valueOf(sysDictionaryData.getDicValue());
					
				}
			}
			if(sysDictionaryData.getDicorder() != null && sysDictionaryData.getDicorder() >= dicorder){
				dicorder = sysDictionaryData.getDicorder();
			}
		}
		// 自增
		dicValue++;
		dicorder++;
		
		
		DictionaryVO dictionaryVO = new DictionaryVO();
		
		dictionaryVO.setDictext(dicText);
		dictionaryVO.setIsDefault("0");
		dictionaryVO.setDicvalue(dicValue + "");
		dictionaryVO.setTypeCode(typeCode);
		dictionaryVO.setDicorder(""+dicorder);
		
		List<DictionaryVO> list = new ArrayList<DictionaryVO>();
		list.add(dictionaryVO);
		sysDictionaryDao.insertDictionary(list);
		
		//重新读取缓存
		//这一组数据的typeCode是同一个
		// 覆盖老的缓存数据
		List<SysDictionaryDataVO> caches = sysDictionaryDao.findDictionaryDataList(sysUserVO.getWithin_code(),typeCode);
		dictionaryDataCacheMap.put(typeCode, caches);
		
		
		return dicValue + "";
	}

	@Override
	public ResultVO getDictionaryDataListCache(String dicTypeCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DictionaryVO> getListForGrid(JqGridParamModel jqGridParamModel) {
		List<DictionaryVO> dictionaryVo = sysDictionaryDao.findList(jqGridParamModel);
		return dictionaryVo;
	}

	@Override
	public PageVO getListForGridSubordinate(
			JqGridParamModel jqGridParamModel) {

		List<DictionaryVO> list = this.sysDictionaryDao.getListForGridSubordinate(jqGridParamModel);
		int records = sysDictionaryDao.getListForGridSubordinateCount(jqGridParamModel);
		return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,records);
	}

	@Override
	public void add(SysDictionaryVO sysvo) {
		this.sysDictionaryDao.add(sysvo);
	}

	@Override
	public void delete(Integer[] array) {
		if(array.length==0){return;}
		this.sysDictionaryDao.delete(array);
	}
	@Override
	public void deleteData(Integer[] sn) {
		if(sn.length==0){return;}
		this.sysDictionaryDao.deleteData(sn);
		// 这一组数据的 typeCode 会是同一个
		for (int i = 0; i < sn.length; i++) {
			Collection<List<SysDictionaryDataVO>> dictionaryDataList = dictionaryDataCacheMap.values();
			boolean isExist = false;
			for (List<SysDictionaryDataVO> list : dictionaryDataList) {
				for (SysDictionaryDataVO vo : list) {
					if(vo.getSn().equals(sn[i])){
						isExist = true;
						list.remove(vo); 
						break;
					}
				}
				if(isExist){
					break;
				}
			}
		}

	}
	@Override
	public void update(SysDictionaryVO sysvo) {
		// 编码不能被修改， 因为有些地方使用了常量, 包括删除也是
		this.sysDictionaryDao.update(sysvo);
	}

	@Override
	public SysDictionaryVO getDictionary(Integer sn) {
		return this.sysDictionaryDao.getDictionary(sn);
	}

	@Override
	public void insertDictionaryVO(List<DictionaryVO> dictionaryVOlist,
		HttpServletRequest request, HttpServletResponse response) {
		List<DictionaryVO> dictionaryVOAdd=new ArrayList<DictionaryVO>();
		List<DictionaryVO> dictionaryVOAddUpdate=new ArrayList<DictionaryVO>();
		SysUserVO sysUserVO=SessionUtils.currentSession();
		String typeCode = null;
		for(DictionaryVO dictionaryVO : dictionaryVOlist) {
			dictionaryVO.setWithin_code(sysUserVO.getWithin_code());
			dictionaryVO.setWhcenter(sysUserVO.getWhcenter());
			System.out.println(dictionaryVO.getOperateType());
			if("add".equals(dictionaryVO.getOperateType())){
				dictionaryVOAdd.add(dictionaryVO);
			}else{
				dictionaryVO.setSn(dictionaryVO.getSn());
				dictionaryVOAddUpdate.add(dictionaryVO);
			}
			if(typeCode==null){
				typeCode = dictionaryVO.getTypeCode();
			}
		}
		if(dictionaryVOAdd.size()>0){
			this.sysDictionaryDao.insertDictionary(dictionaryVOAdd);
		}
		if(dictionaryVOAddUpdate.size()>0){
			this.sysDictionaryDao.updateDictionary(dictionaryVOAddUpdate);
		}
		
		if(dictionaryVOAdd.size() > 0 || dictionaryVOAddUpdate.size() > 0){
			//这一组数据的typeCode是同一个
			// 覆盖老的缓存数据
			List<SysDictionaryDataVO> caches = sysDictionaryDao.findDictionaryDataList(sysUserVO.getWithin_code(),typeCode);
			dictionaryDataCacheMap.put(typeCode, caches);
		}

	}

	@Override
	public List<SysDictionaryDataVO> findDictionaryDataList(String typeCode) {
		SysUserVO sysUserVO=SessionUtils.currentSession();
		List<SysDictionaryDataVO> caches = sysDictionaryDao.findDictionaryDataList(sysUserVO.getWithin_code(),typeCode);
		return caches;
		/*
		List<SysDictionaryDataVO> caches = dictionaryDataCacheMap.get(typeCode);
		if(caches == null){
			// 查询数据库
			synchronized (dictionaryDataCacheMap) {
				if(caches == null){
					caches = sysDictionaryDao.findDictionaryDataList(typeCode);
					if(caches!=null){
						dictionaryDataCacheMap.put(typeCode, caches);
					}
				}
			}
		}
		return caches;
		*/
	}
	
	public List<DictionaryVO> findDictionaryList(){
		return sysDictionaryDao.findDictionaryList();
	}

	@Override
	public List<SysDictionaryDataVO> findDictionaryLists(SysDictionaryDataVO sysDictionaryDataVO) {
		return sysDictionaryDao.findDictionaryLists(sysDictionaryDataVO);
	}

	@Override
	public List<SysDictionaryDataVO> getState(String tableName) {
		Map<String,Object> params = new HashMap<String,Object>();
		SysUserVO sysUserVO = SessionUtils.currentSession();
		params.put("lang", SessionUtils.getLang());
		params.put("tableName", tableName);
		params.put("withinCode", sysUserVO.getWithin_code());
		params.put("showType", 0);
		System.out.println(params);
		return sysDictionaryDao.getDicInfo(params);
	}

    @Override
    public ResultVO validPassword(String password,String userName) {
        ResultVO resultVO = ResultVO.successResult();
        // 定义密码为 weekeyan@tms 用户名是administrator
		// 用户名和密码都相同，才修改
		if(!StringUtils.equals("administrator", userName)) {
			return ResultVO.failResult("用户名错误，用户名应为administrator");
		}
		if(!StringUtils.equals("weekeyan@tms",password)) {
			return ResultVO.failResult("密码错误，不能修改");
		}
		return resultVO;

    }

	@Override
	public List<SysDictionaryVO> getZdDic(JqGridParamModel jqGridParamModel) {
		List<SysDictionaryVO> list = this.sysDictionaryDao.getZdDic(jqGridParamModel);
		return list;
	}

	@Override
	public void insertDicData(List<DictionaryVO> dictionaryVOlist,
								   HttpServletRequest request, HttpServletResponse response) {
		List<DictionaryVO> dictionaryVOAdd=new ArrayList<DictionaryVO>();
		List<DictionaryVO> dictionaryVOAddUpdate=new ArrayList<DictionaryVO>();
		SysUserVO sysUserVO=SessionUtils.currentSession();
		String typeCode = null;
		for(DictionaryVO dictionaryVO : dictionaryVOlist) {
			dictionaryVO.setWithin_code(sysUserVO.getWithin_code());
			dictionaryVO.setWhcenter(sysUserVO.getWhcenter());
			System.out.println(dictionaryVO.getOperateType());
			if("add".equals(dictionaryVO.getOperateType())){
				dictionaryVOAdd.add(dictionaryVO);
			}else{
				dictionaryVO.setSn(dictionaryVO.getSn());
				dictionaryVOAddUpdate.add(dictionaryVO);
			}
			if(typeCode==null){
				typeCode = dictionaryVO.getTypeCode();
			}
		}
		if(dictionaryVOAdd.size()>0){
			this.sysDictionaryDao.insertZdDicData(dictionaryVOAdd);
		}
		if(dictionaryVOAddUpdate.size()>0){
			this.sysDictionaryDao.updateZdDicData(dictionaryVOAddUpdate);
		}

		if(dictionaryVOAdd.size() > 0 || dictionaryVOAddUpdate.size() > 0){
			//这一组数据的typeCode是同一个
			// 覆盖老的缓存数据
			List<SysDictionaryDataVO> caches = sysDictionaryDao.queryZdDicData(sysUserVO.getWithin_code(),typeCode);
			dictionaryDataCacheMap.put(typeCode, caches);
		}

	}
	@Override
	public void deleteDicData(Integer[] sn) {
		if(sn.length==0){return;}
		this.sysDictionaryDao.deleteDicData(sn);
		// 这一组数据的 typeCode 会是同一个
		for (int i = 0; i < sn.length; i++) {
			Collection<List<SysDictionaryDataVO>> dictionaryDataList = dictionaryDataCacheMap.values();
			boolean isExist = false;
			for (List<SysDictionaryDataVO> list : dictionaryDataList) {
				for (SysDictionaryDataVO vo : list) {
					if(vo.getSn().equals(sn[i])){
						isExist = true;
						list.remove(vo);
						break;
					}
				}
				if(isExist){
					break;
				}
			}
		}

	}

	@Override
	public void deleteDic(Integer[] array) {
		if(array.length==0){return;}
		this.sysDictionaryDao.deleteDic(array);
	}

	@Override
	public PageVO getDicData(JqGridParamModel jqGridParamModel) {

		List<DictionaryVO> list = this.sysDictionaryDao.getDicData(jqGridParamModel);
		return new PageVO(jqGridParamModel.getPage(),jqGridParamModel.getRows(),list,list.size());
	}

	@Override
	public SysDictionaryVO getCusDictionary(Integer sn) {
		return this.sysDictionaryDao.getCusDictionary(sn);
	}

	@Override
	public void updateDic(SysDictionaryVO sysvo) {
		// 编码不能被修改， 因为有些地方使用了常量, 包括删除也是
		this.sysDictionaryDao.updateDic(sysvo);
	}

	@Override
	public void addDic(SysDictionaryVO sysvo) {
		this.sysDictionaryDao.addDic(sysvo);
	}
}
