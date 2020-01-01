package com.bba.common.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bba.basics.dao.BaseDao;
import com.bba.common.annotation.Column;
import com.bba.common.annotation.ID;
import com.bba.common.annotation.Table;
import com.bba.common.enums.BooleanEnum;
import com.bba.common.enums.TypeSerializerEnum;
import com.bba.common.service.api.IBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class BaseService implements IBaseService {

	private static MathContext mathContext = new MathContext(2, RoundingMode.HALF_DOWN);
	private static ObjectMapper objectMapper = new ObjectMapper();
	private static DateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private BaseDao BaseDao;

	public void BaseService(){
	}

	@Override
	public <T> int insert(T vo) {
		if(vo instanceof List) {
			throw new IllegalArgumentException("Wrong call parameter, should be batch operation");
		}
		List<T> list = new ArrayList<T>();
		list.add(vo);
		return this.insert(list);
	}

	public <T> int insert(List<T> list){
		if(list == null || list.size() == 0) {
			return 0;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();

		Class<?> class1 = list.get(0).getClass();

		Table table = class1.getAnnotation(Table.class);
		if(table == null || "".equals(table.value())) {
			throw new IllegalArgumentException("The entity class must define Table annotations and column annotations");
		}
		String tableName = table.value();
		List<Map<String, String>> fieldList = new ArrayList<Map<String, String>>();
		Field[] declaredFields = class1.getDeclaredFields();
		for (Field field : declaredFields){
			Column column = field.getAnnotation(Column.class);
			if(column == null) {
				continue;
			}
			Map<String, String> fieldMap = new HashMap<String, String>();
			String databaseField = null;
			if("".equals(column.value())) {
				databaseField = field.getName();
			}else {
				databaseField = column.value();
			}
			if(column.type() == TypeSerializerEnum.DATE) {
				fieldMap.put("DATE", "Y");
			}else if(column.type() == TypeSerializerEnum.DATETIME) {
				fieldMap.put("DATETIME", "Y");
			}
			databaseField = databaseField.toUpperCase();
			if (databaseField.equals("ID") || databaseField.equals("SN")) {
				continue;
			}
			fieldMap.put("databaseField", databaseField);
			fieldMap.put("columnField", field.getName());
			fieldList.add(fieldMap);
		}
		map.put("fieldList", fieldList);
		map.put("dataList", list);
		map.put("tableName", tableName);
		return BaseDao.batchInsert(map);
	}

	@Override
	public <T> int delete(T vo) {
		if(vo == null) {
			throw new IllegalArgumentException("The delete parameter cannot be empty");
		}
		if(vo instanceof List) {
			throw new IllegalArgumentException("Wrong call parameter, should be batch operation");
		}
		List<T> list = new ArrayList<T>(5);
		list.add(vo);
		return this.delete(list);
	}

	@Override
	public <T> int delete(List<T> list) {
		if(list == null || list.size() == 0) {
			throw new IllegalArgumentException("The delete parameter cannot be empty");
		}
		Class<T> class1 = (Class<T>)list.get(0).getClass();
		return this.batchDelete(list, class1);
	}

	@Override
	public <T> int deleteAll(Class<T> class1) {
		if(class1 == null) {
			throw new IllegalArgumentException("The parameter cannot be empty");
		}
		return this.batchDelete(null, class1);
	}

	private <T> int batchDelete(List<T> list, Class<T> class1) {
		Table table = class1.getAnnotation(Table.class);
		if(table == null || "".equals(table.value())) {
			throw new IllegalArgumentException("The entity class Table annotation is undefined");
		}
		Map<Object, Object> paramMap = new HashMap<Object, Object>();
		if(list != null) {
			if(list.size() == 0) {
				throw new IllegalArgumentException("The delete parameter cannot be empty");
			}

			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			Field[] declaredFields = class1.getDeclaredFields();
			List<Field> classFieldList = new ArrayList<Field>();

			for (Field field : declaredFields){
				if(field.getAnnotation(Column.class) == null && field.getAnnotation(ID.class) == null) {
					continue;
				}
				field.setAccessible(true);
				classFieldList.add(field);
			}
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> dataVOMap = new HashMap<String, Object>();
				List<Map<String, Object>> fieldList = new ArrayList<Map<String, Object>>();
				T vo = list.get(i);
				try {
					for (Field field : classFieldList){
						Object value = field.get(vo);
						if(value != null) {
							Map<String, Object> tempMap = new HashMap<String, Object>();
							tempMap.put("databaseField", getDatabaseField(field));
							tempMap.put("columnField", field.getName());
							fieldList.add(tempMap);
						}

					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				if(fieldList.size() == 0) {
					throw new IllegalArgumentException("The "+(i+1)+"th delete condition object has no parameters set");
				}
				dataVOMap.put("fieldList", fieldList);
				dataVOMap.put("data", list.get(i));
				dataList.add(dataVOMap);
			}



			paramMap.put("dataList", dataList);
		}

		paramMap.put("tableName", table.value());
		return BaseDao.batchDelete(paramMap);
	}


	@Override
	public <T> int update(T vo) {
		if(vo == null) {
			throw new IllegalArgumentException("The update parameter cannot be empty");
		}
		if(vo instanceof List) {
			throw new IllegalArgumentException("Wrong call parameter, should be batch operation");
		}
		List<T> list = new ArrayList<T>(5);
		list.add(vo);
		return this.update(list);
	}

	@Override
	public <T> int update(List<T> list) {
		if(list == null || list.size() == 0) {
			throw new IllegalArgumentException("The query parameter cannot be empty");
		}
		Class<T> class1 = (Class<T>)list.get(0).getClass();
		Table table = class1.getAnnotation(Table.class);
		if(table == null || "".equals(table.value())) {
			throw new IllegalArgumentException("The entity class Table annotation is undefined");
		}
		Field[] declaredFields = class1.getDeclaredFields();
		List<Field> classFieldList = new ArrayList<Field>();

		List<Field> columnKeyList = new ArrayList<Field>();
		for (Field field : declaredFields){
			field.setAccessible(true);
			if(field.getAnnotation(ID.class) != null) {
				columnKeyList.add(field);
			}else{
				Column column = field.getAnnotation(Column.class);
				if(column == null) {
					continue;
				}
				if(column.key() == BooleanEnum.YES) {
					columnKeyList.add(field);
				}else {
					classFieldList.add(field);
				}
			}
		}
		if(classFieldList.size() == 0) {
			throw new IllegalArgumentException("No ID or Col is defined");
		}
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		try {

			for (int i = 0; i < list.size(); i++) {
				T vo = list.get(i);

				List<Map<String, Object>> whereFieldList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> fieldList = new ArrayList<Map<String, Object>>();
				for (Field field : columnKeyList) {
					Object value = field.get(vo);
					if(value != null) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("databaseField", getDatabaseField(field));
						map.put("columnField", field.getName());
						whereFieldList.add(map);
					}
				}
				for (Field field : classFieldList) {
					Object value = field.get(vo);
					if(value != null) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("databaseField", getDatabaseField(field));
						map.put("columnField", field.getName());
						Column column = field.getAnnotation(Column.class);
						if(column != null) {
							if(column.type() == TypeSerializerEnum.DATE) {
								map.put("DATE", "Y");
							}else if(column.type() == TypeSerializerEnum.DATETIME) {
								map.put("DATETIME", "Y");
							}
						}
						fieldList.add(map);

					}
				}

				if(whereFieldList.size() == 0) {
					throw new IllegalArgumentException("Line "+(i+1)+" does not have any modification conditions");
				}
				if(fieldList.size() == 0) {
					throw new IllegalArgumentException("Line "+(i+1)+" has no data changes");

				}

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("whereFieldList", whereFieldList);
				map.put("fieldList", fieldList);
				map.put("whereVO", vo);
				map.put("dataVO", vo);
				dataList.add(map);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Map<Object, Object> paramMap = new HashMap<Object, Object>();
		paramMap.put("dataList", dataList);
		paramMap.put("tableName", table.value());
		return BaseDao.batchUpdate(paramMap);
	}

	private static String getDatabaseField(Field field) {
		ID id = field.getAnnotation(ID.class);
		Column column = field.getAnnotation(Column.class);
		if(id != null) {
			if("".equals(id.value())) {
				return field.getName();
			}else {
				return id.value();
			}
		}else {
			if("".equals(column.value())) {
				return field.getName();
			}else {
				return column.value();
			}
		}
	}

	@Override
	public <T> int update(T vo, T whereVO) {
		if(vo == null) {
			throw new IllegalArgumentException("The modified data content cannot be empty");
		}
		if(whereVO == null) {
			throw new IllegalArgumentException("The modified data match condition cannot be empty");
		}
		if(whereVO instanceof List) {
			throw new IllegalArgumentException("Wrong call parameter, should be batch operation");
		}
		List<T> whereVOList = new ArrayList<T>(5);
		whereVOList.add(whereVO);
		return this.update(vo, whereVOList);
	}

	@Override
	public <T> int update(T vo, List<T> whereVOList) {
		if(vo == null) {
			throw new IllegalArgumentException("The modified data content cannot be empty");
		}
		if(whereVOList == null || whereVOList.size() == 0) {
			throw new IllegalArgumentException("The modified data match condition cannot be empty");
		}

		Class<T> class1 = (Class<T>)vo.getClass();
		Table table = class1.getAnnotation(Table.class);
		if(table == null || "".equals(table.value())) {
			throw new IllegalArgumentException("The entity class Table annotation is undefined");
		}
		Field[] declaredFields = class1.getDeclaredFields();
		List<Field> classFieldList = new ArrayList<Field>();
		for (Field field : declaredFields){
			field.setAccessible(true);
			if(field.getAnnotation(Column.class) == null && field.getAnnotation(ID.class) == null) {
				continue;
			}
			classFieldList.add(field);
		}
		if(classFieldList.size() == 0) {
			throw new IllegalArgumentException("No ID or Col is defined");
		}
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		try {

			for (int i = 0; i < whereVOList.size(); i++) {
				T whereVO = whereVOList.get(i);

				List<Map<String, Object>> whereFieldList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> fieldList = new ArrayList<Map<String, Object>>();
				for (Field field : classFieldList) {
					Object value = field.get(whereVO);
					if(value != null) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("databaseField", getDatabaseField(field));
						map.put("columnField", field.getName());
						whereFieldList.add(map);
					}
				}
				for (Field field : classFieldList) {
					Object value = field.get(vo);
					if(value != null) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("databaseField", getDatabaseField(field));
						map.put("columnField", field.getName());
						Column column = field.getAnnotation(Column.class);
						if(column != null) {
							if(column.type() == TypeSerializerEnum.DATE) {
								map.put("DATE", "Y");
							}else if(column.type() == TypeSerializerEnum.DATETIME) {
								map.put("DATETIME", "Y");
							}
						}
						fieldList.add(map);

					}
				}
				if(whereFieldList.size() == 0) {
					throw new IllegalArgumentException("Line "+(i+1)+" does not have any modification conditions");
				}
				if(fieldList.size() == 0) {
					throw new IllegalArgumentException("Line "+(i+1)+" has no data changes");

				}

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("whereFieldList", whereFieldList);
				map.put("fieldList", fieldList);
				map.put("whereVO", whereVO);
				map.put("dataVO", vo);
				dataList.add(map);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Map<Object, Object> paramMap = new HashMap<Object, Object>();
		paramMap.put("dataList", dataList);
		paramMap.put("tableName", table.value());
		return BaseDao.batchUpdate(paramMap);
	}


	@Override
	public <T> Map<String, T> findMapAll(Class<T> class1) {
		return this.findMapByProperty(null, class1);
	}

	@Override
	public <T> Map<String, T> findMapByProperty(T vo) {
		if(vo == null) {
			throw new IllegalArgumentException("The query parameter cannot be empty");
		}
		if(vo instanceof List) {
			throw new IllegalArgumentException("Wrong call parameter, should be batch operation");
		}
		List<T> list = new ArrayList<T>(5);
		list.add(vo);
		return this.findMapByProperty(list);
	}

	@Override
	public <T> Map<String, T> findMapByProperty(List<T> list) {
		if(list == null || list.size() == 0) {
			throw new IllegalArgumentException("The query parameter cannot be empty");
		}
		Class<T> class1 = (Class<T>)list.get(0).getClass();
		return this.findMapByProperty(list, class1);
	}

	private <T> Map<String, T> findMapByProperty(List<T> list, Class<T> class1) {
		List<T> dataList = this.findListByProperty(list, class1);
		Field[] declaredFields = class1.getDeclaredFields();

		Field op_field = null;
		for (Field field : declaredFields){
			Column column = field.getAnnotation(Column.class);
			if(column != null) {
				if(column.key() == BooleanEnum.YES && !"within_code".equals(field.getName().toLowerCase())) {
					op_field = field;
					op_field.setAccessible(true);
					break;
				}
			}

		}
		if(op_field == null) {
			throw new IllegalArgumentException("The column key value must be defined as Yes in the entity class");
		}
		Map<String, T> map = new HashMap<String, T>(dataList.size() * 2);
		try {
			for (T t : dataList) {
				Object key = op_field.get(t);
				if(key == null) {
					throw new IllegalArgumentException("The value corresponding to the key field in the query data cannot be empty");

				}
				map.put(key.toString(), t);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return map;
	}

	@Override
	public <T> T getDetail(T vo) {
		if(vo == null) {
			throw new IllegalArgumentException("The query failed and the result set matched multiple");
		}
		if(vo instanceof List) {
			throw new IllegalArgumentException("Wrong call parameter, should be batch operation");
		}
		List<T> list = this.findListByProperty(vo);
		if(list.size() > 1) {
			throw new IllegalArgumentException("");
		}
		if(list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public <T> List<T> findListByProperty(T vo) {
		if(vo == null) {
			throw new IllegalArgumentException("The query parameter cannot be empty");
		}
		if(vo instanceof List) {
			throw new IllegalArgumentException("Wrong call parameter, should be batch operation");
		}
		List<T> list = new ArrayList<T>(5);
		list.add(vo);
		return this.findListByProperty(list);
	}

	@Override
	public <T>List<T> findListAll(Class<T> class1){
		return this.findListByProperty(null, class1);
	}

	@Override
	public <T> List<T> findListByProperty(List<T> list) {
		if(list == null || list.size() == 0) {
			throw new IllegalArgumentException("The query parameter cannot be empty");
		}
		Class<T> class1 = (Class<T>)list.get(0).getClass();
		return this.findListByProperty(list, class1);
	}

	private <T> List<T> findListByProperty(List<T> list, Class<T> class1) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		Table table = class1.getAnnotation(Table.class);
		if(table == null || "".equals(table.value())) {
			throw new IllegalArgumentException("The entity class Table annotation is undefined");
		}
		String tableName = table.value();
		List<Map<String, Object>> fieldList = new ArrayList<Map<String, Object>>();
		Field[] declaredFields = class1.getDeclaredFields();
		for (Field field : declaredFields){
			Column column = field.getAnnotation(Column.class);
			ID id = null;
			if(column == null) {
				id = field.getAnnotation(ID.class);
				if(id == null) {
					continue;
				}
			}
			field.setAccessible(true);
			String databaseField = null;
			if(id != null) {
				if("".equals(id.value())) {
					databaseField = field.getName();
				}else {
					databaseField = id.value();
				}
			}else {
				if("".equals(column.value())) {
					databaseField = field.getName();
				}else {
					databaseField = column.value();
				}
			}
			Map<String, Object> fieldMap = new HashMap<String, Object>();
			fieldMap.put("columnField", field.getName());
			fieldMap.put("databaseField", databaseField);
			fieldMap.put("field", field);
			fieldMap.put("column", column);

			fieldList.add(fieldMap);
		}
		if(list != null) {
			List<List<Map<String, Object>>> dataList = new ArrayList<List<Map<String, Object>>>();
			try {
				for (int i = 0; i < list.size(); i++) {
					List<Map<String,Object>> voList = new ArrayList<Map<String,Object>>();
					for (Map<String, Object> fieldMap : fieldList) {
						Field field = (Field)fieldMap.get("field");
						Object objectValue = field.get(list.get(i));
						if(objectValue != null) {
							Map<String, Object> vo = new HashMap<String, Object>();
							vo.put("databaseField", fieldMap.get("databaseField"));
							vo.put("value", objectValue);
							voList.add(vo);
						}
					}
					if(voList.size() == 0) {
						throw new IllegalArgumentException("Line "+(i+1)+" parameter entity class does not set any valid conditions");
					}
					dataList.add(voList);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			map.put("dataList", dataList);
		}


		map.put("fieldList", fieldList);
		map.put("tableName", tableName);
		List<T> objectList = BaseDao.findListByProperty(map);

		for (Map<String, Object> tempMap : (List<Map<String, Object>>)objectList) {

			for (Map<String, Object> filedMap : fieldList) {
				Column column = (Column)filedMap.get("column");
				String columnField = filedMap.get("columnField")+"";
				if(column != null) {
					if(column.type() == TypeSerializerEnum.DATE) {
						Timestamp timestamp = (Timestamp)tempMap.get(filedMap.get("columnField"));
						if(timestamp != null) {
							tempMap.put(columnField, yyyyMMdd.format(timestamp).toString());
						}
					}else if(column.type() == TypeSerializerEnum.DATETIME) {
						Timestamp timestamp = (Timestamp)tempMap.get(filedMap.get("columnField"));
						if(timestamp != null) {
							tempMap.put(columnField, yyyyMMddHHmmss.format(timestamp).toString());
						}
					}else {

						Object value = tempMap.get(columnField);
						if(column.decimalsize() > -1) {
							if(value == null || "".equals(value)) {
								value = new BigDecimal(0);
							}
							if(!(value instanceof BigDecimal)) {
								throw new IllegalArgumentException("The decimalsize field does not "
										+ "match the database return type");
							}
							BigDecimal b = ((BigDecimal)value).round(mathContext);
							tempMap.put(columnField,getFixedLengthStr(b, column.decimalsize()));

						}else {
							if(value != null && value instanceof BigDecimal) {
								tempMap.put(columnField, value.toString());
							}
						}

					}
				}
			}
		}

		objectList = (List)toJSONObjectList(class1,objectList);
		return objectList;
	}
	private static String getFixedLengthStr(BigDecimal b, int length) {
		String str = b.toString();
		int last = str.indexOf(".");
		length = length - (str.length() - last - 1);
		if(last < 0) {
			str += ".";
		}
		for (int i = 0; i < length; i++) {
			str += "0";
		}
		return str;

	}

	private static <T>List<T> toJSONObjectList(Class<T> class1, Object jsonObject){
		JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, class1);
		try {
			List<T> list = null;
			if(jsonObject instanceof String){
				list = objectMapper.readValue((String)jsonObject, javaType);
			}else{
				list = objectMapper.convertValue(jsonObject, javaType);
			}

			return list;
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}



}
