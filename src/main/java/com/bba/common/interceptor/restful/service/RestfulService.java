/**
	此文件归微科创源有限公司所有
	
	创  建  人:caining
	创建时间:2018年9月10日

	文件描述:
*/
package com.bba.common.interceptor.restful.service;

import com.bba.common.interceptor.restful.BBAColumn;
import com.bba.common.interceptor.restful.BBAOption;
import com.bba.common.interceptor.restful.BBATable;
import com.bba.common.interceptor.restful.util.Const;
import com.bba.common.vo.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RestfulService{
	
	@Resource
	private JdbcTemplate jdbcTemplate;

	public Object execute(String modelName , String tableName , String option, Map<String, String> paramMap) throws ClassNotFoundException {
		
		Object result = null;
		
		if(StringUtils.equalsIgnoreCase(Const.RESTFUL_OPTION_FIND, option)){
			result = find(paramMap,buildClazz(modelName, tableName));
		} else if(StringUtils.equalsIgnoreCase(Const.RESTFUL_OPTION_ADD, option)){
			
		} else if(StringUtils.equalsIgnoreCase(Const.RESTFUL_OPTION_UPDATE, option)){
			
		} else if(StringUtils.equalsIgnoreCase(Const.RESTFUL_OPTION_DELETE, option)){
			
		}
		
		return result;
	}
	
	//查询
	private Object find(Map<String, String> paramMap,Class<?> clazz){
		
		StringBuffer sql = new StringBuffer("SELECT ");
		
		// 获取表名注解
		String tableName = clazz.getAnnotation(BBATable.class).value();
		
		// 获取所有私有字段
		Field[] fields = clazz.getDeclaredFields();
		
		// 记录排序字段
		List<String> sortFields = new ArrayList<String>();
		
		// 记录排序类型
		List<String> sortTypes = new ArrayList<String>();
		
		// 列出查询字段
		int flag = 0;
		for (Field field : fields) {
			BBAColumn col = field.getAnnotation(BBAColumn.class);
			if(col==null)
				continue;
			if(flag==0){
				sql.append(col.name()+" as "+ "\""+col.name()+"\"");
				flag++;
			} else {
				sql.append(","+col.name()+" as "+ "\""+col.name()+"\"");
			}
			
			if(col.isSort()){
				sortFields.add(col.name());
				sortTypes.add(col.sortType().toString());
			}
		}
		
		sql.append(" FROM "+tableName);

		flag = 0;
		
		// 构建查询条件
		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			
			for (Field field : fields) {
			
				BBAColumn col = field.getAnnotation(BBAColumn.class);
			
				if(col==null)
					continue;
				if(StringUtils.equalsIgnoreCase(entry.getKey(), col.name())){
					String option = col.option()==BBAOption.CN?"=" : " like ";
					sql.append(buildConditions(entry.getKey(),entry.getValue(),option,flag));
					flag++;
				}
			}
			
		}
		
		// 构建排序
		if(sortFields.size()>0){
			for (int i = 0; i < sortFields.size(); i++) {
				String sortField = sortFields.get(i);
				String sortType = sortTypes.get(i);
				
				if(i==0){
					sql.append(" ORDER BY "+sortField+" "+sortType);
				} else {
					sql.append(","+sortField+" "+sortType);
				}
			}
		}
		
		String page = paramMap.get("page");
		String rows = paramMap.get("rows");
		
		Object result = null;
		
		if(StringUtils.isNotEmpty(page) && StringUtils.isNotEmpty(rows)){
			
			int pageNum = Integer.parseInt(page);
			int rowsNum = Integer.parseInt(rows);
			
			String beginPage = "SELECT A.* FROM (SELECT ROWNUM RN, T.* FROM (";
			String thisSql = sql.toString();
			String endPage = ")T WHERE ROWNUM < "+(pageNum*rowsNum+1)+") A WHERE A.RN > "+((pageNum-1)*rowsNum);
			
			String coutSql = "select count(*) from ("+sql.toString()+") A";
			
			log.debug(this.getClass().getName()+":"+sql.toString());
			
			int count = jdbcTemplate.queryForObject(coutSql, Integer.class);
			Object tempObj = jdbcTemplate.queryForList(beginPage+thisSql+endPage);
			result = new PageVO(pageNum, rowsNum, tempObj, count);
		} else {
			log.debug(this.getClass().getName()+":"+sql.toString());
			result = jdbcTemplate.queryForList(sql.toString());
		}
		return result;
	}
	
	//新增
	private void add(Map<String, String> paramMap,Class<?> clazz){
		
	}
	
	//删除
	private void delete(Map<String, String> paramMap,Class<?> clazz){
		
	}
	
	//修改
	private void update(Map<String, String> paramMap,Class<?> clazz){
		
	}

	// 构建类型信息
	private Class<?> buildClazz(String modelName , String tableName) throws ClassNotFoundException{
		Class<?> clazz = null;
		try {
			clazz = Class.forName("com.bba."+modelName+".vo."+tableName);
		} catch (Exception e) {
			clazz = Class.forName("com.bba."+modelName+".vo.order."+tableName);
		}
		return clazz;
	}
	
	// 构建查询参数
	private String buildConditions(String key,String val , String option , int flag){
		String conditions = "";
		if(flag==0){
			conditions+=" WHERE ";
		} else {
			conditions+=" AND ";
		}
		if(val.contains(",")){
			String[] vals = val.split(",");
			for (int i = 0; i < vals.length; i++) {
				if(i==0){
					conditions+="("+key+option+  "'"+(StringUtils.equals(option, "=")?vals[i]:"%"+vals[i]+"%")+ "'";
				} else if(i==vals.length-1){
					conditions+=" or " + key+option+  "'"+(StringUtils.equals(option, "=")?vals[i]:"%"+vals[i]+"%")+ "')";
				} else {
					conditions+=" or "+key+option+  "'"+(StringUtils.equals(option, "=")?vals[i]:"%"+vals[i]+"%")+ "'";
				}
			}
		} else {
			conditions+=key+option+  "'"+(StringUtils.equals(option, "=")?val:"%"+val+"%")+ "'";
		}
		return conditions;
	}
	
}
