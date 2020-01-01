package com.bba.common.service.api;

import java.util.List;
import java.util.Map;


public interface IBaseService{

	<T> int insert(T vo);

	<T> int insert(List<T> list);

	<T> int delete(T vo);

	<T> int delete(List<T> list);

	<T> int deleteAll(Class<T> class1);

	<T> int update(T vo);

	<T> int update(List<T> list);

	<T> int update(T vo, T whereVO);

	<T> int update(T vo, List<T> whereVOList);


	<T> Map<String, T> findMapAll(Class<T> class1);

	<T> Map<String, T> findMapByProperty(T vo);

	<T> Map<String, T> findMapByProperty(List<T> list);


	<T> List<T> findListAll(Class<T> class1);

	<T> T getDetail(T vo);

	<T> List<T> findListByProperty(T vo);

	<T> List<T> findListByProperty(List<T> list);
}
