package com.bba.basics.dao;

import java.util.List;
import java.util.Map;



public interface BaseDao {

    int batchInsert(Map<Object, Object> map);

    int batchUpdate(Map<Object, Object> map);

    int batchDelete(Map<Object, Object> map);

    <T> List<T> findListByProperty(Map<Object, Object> map);

}
