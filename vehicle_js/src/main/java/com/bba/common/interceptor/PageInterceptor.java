package com.bba.common.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class PageInterceptor implements Interceptor {  
	
	private ObjectMapper objectMapper = new ObjectMapper();

	private PageInterceptor() {
		super();
		//不进行修改的字段
		notUpdateFieldArray.add("CEATE_DATE");
		notUpdateFieldArray.add("CREATE_DATE");
		notUpdateFieldArray.add("CREATE_BY");
		notUpdateFieldArray.add("CREATE_DATE");
		notUpdateFieldArray.add("CREATEBY");
		notUpdateFieldArray.add("CREATEDATE");
		notUpdateFieldArray.add("UPDATE_BY");
		notUpdateFieldArray.add("UPDATE_DATE");
		notUpdateFieldArray.add("UPDATEBY");
		notUpdateFieldArray.add("UPDATEDATE");
		notUpdateFieldArray.add("PASSWORD");
	}
    private Map<String,String> sys_Baseoperator_TabMap = null;
    private Map<String,String> main_columnMap = null;

    @Override
	public Object intercept(Invocation invocation) throws Throwable {  
    	if(invocation.getTarget() instanceof RoutingStatementHandler){    
            RoutingStatementHandler statementHandler = (RoutingStatementHandler)invocation.getTarget();    
            StatementHandler delegate = (StatementHandler) getFieldValue(statementHandler, "delegate");    
            BoundSql boundSql = delegate.getBoundSql();  
            Object obj = boundSql.getParameterObject();  
            String exceSql = boundSql.getSql().toUpperCase();
            
            int exist = 0;
            
            if(StringUtils.startsWithIgnoreCase(exceSql, "UPDATE")){
            	exist = 1;
            }else if(StringUtils.startsWithIgnoreCase(exceSql, "BEGIN")){
            	
            	exceSql = exceSql.substring(6, exceSql.length()-4).trim();
	            if(StringUtils.startsWithIgnoreCase(exceSql, "UPDATE")){
	            	exist = 2;
            	};
            }
            Field propertyField = null;
        	Map<String,String> oldPropertyField = null;

            if (exist != 0) {
            	int tempIndex = exceSql.indexOf(" SET");
            	if(tempIndex == -1) {
            		tempIndex = exceSql.indexOf("\tSET");
            	}
            	
            	String tableNameAndAs = exceSql.substring(6,tempIndex+1).trim().toUpperCase();
            	
            	String main_tableName = tableNameAndAs.split("\\s+")[0];
            	MappedStatement mappedStatement = (MappedStatement)getFieldValue(delegate, "mappedStatement");    
                Connection connection = (Connection)invocation.getArgs()[0];   
                
                //数据库
            	String databaseId = mappedStatement.getConfiguration().getDatabaseId();

            	if("Microsoft SQL Server".equals(databaseId)){
            		tableNameAndAs = tableNameAndAs.split("\\s+")[0];
            	}
            	
            	
                if(sys_Baseoperator_TabMap == null){
                	String sys_Baseoperator_TabSql = "select upper(t.table_code)table_code,upper(t.primary_field)primary_field from SYS_BASEOPERATOR_TAB t";
                	getSys_Baseoperator_TabMapList(sys_Baseoperator_TabSql,
                			mappedStatement,connection);
                }
                
                String primary_fieldTab = main_tableName;
                int startIndex = main_tableName.indexOf(".");
                if(startIndex > -1){
                	primary_fieldTab = main_tableName.substring(startIndex+1);
                }
            	
                String primary_field = sys_Baseoperator_TabMap.get(primary_fieldTab);
                if(primary_field == null){
                	//没有找到说明不需要记录该基础表数据
                	return invocation.proceed();
                }
                
                
                if(main_columnMap == null){
                	String columnSql = null;
                	if("Microsoft SQL Server".equals(databaseId)){
                		columnSql = "select t.* from (SELECT UPPER(A.name) AS table_name,UPPER(B.name) AS column_name,C.value AS comments FROM sys.tables A INNER JOIN sys.columns B ON B.object_id = A.object_id LEFT JOIN sys.extended_properties C ON C.major_id = B.object_id AND C.minor_id = B.column_id union all select upper(t.name) AS table_name,upper(t.name) AS column_name,r.value AS comments from sys.tables t left join sys.extended_properties r on t.object_id=r.major_id and r.minor_id='0') t inner join SYS_BASEOPERATOR_TAB tab on upper(t.table_name)=upper(tab.table_code)";
                    }else{
                    	//默认oracle
                    	columnSql = "select t.* from (select t.table_name,t.column_name,t.comments from user_col_comments t union all select t.table_name,t.table_name column_name,t.comments column_name from user_tab_comments t) t inner join SYS_BASEOPERATOR_TAB tab on t.table_name=tab.table_code";
                    }
 
                	getCommentsList(columnSql,mappedStatement,connection);
                }
                String json = main_columnMap.get(primary_fieldTab);
                if(json == null){
                	return invocation.proceed();
                }
                Map<String,String> columnMap = toJSONObject(Map.class, json);
                
            	List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            	List<ParameterMapping> newParameterMappings = new ArrayList<ParameterMapping>();

            	String exce_sql = null;
            	
            	StringBuffer columnSqlBuf = new StringBuffer();
            	for (String key : columnMap.keySet()) {
					if(StringUtils.equals(key, primary_fieldTab)){
						continue;
					}
					columnSqlBuf.append(","+key);
				}
            	String columnSql = columnSqlBuf.substring(1);
            	if(exist == 1){
            		String old_sql = exceSql.substring(0,exceSql.indexOf("WHERE"));
            		String where_sql = exceSql.substring(exceSql.indexOf("WHERE"));
            		exce_sql = "select "+columnSql+" from "+tableNameAndAs+" "+where_sql;
            		int index = old_sql.split("\\?").length - 1;
            		
            		if(obj instanceof Map){
                		propertyField = getDeclaredField(ParameterMapping.class,"property",true);

            			oldPropertyField = new HashMap<String,String>();
                		Map<String,Object> map = new HashMap<String,Object>();

            			Map<String,Object> paramMap = (Map<String,Object>)obj;
            			List<?> paramDataList = (List<?>)paramMap.get("list");
            			if(paramDataList != null){
                			Map<String,Field> fieldMap = new HashMap<String,Field>();

                    		for (int i = index; i < parameterMappings.size(); i++) {
                    			
                				String property = parameterMappings.get(i).getProperty();
                				
                				String newProperty = null;
                				int tempForIndex = property.indexOf(".");
                				/*if(tempForIndex == -1) {
                					///obj = paramMap.get("list");
                					break;
                				}*/
            					String fieldName = property.substring(tempForIndex+1);
            					String list_str = (tempForIndex == -1)?property : property.substring(0,tempForIndex);
            					int list_index = Integer.valueOf(list_str.substring(list_str.lastIndexOf("_")+1));

            					newProperty = property.replaceAll("\\.", "_");
            					
            					Object objVO = paramDataList.get(list_index);
                				
            					Object value = null;
            					if(tempForIndex == -1) {

            						value = objVO;
            					}else {
            						Field field = fieldMap.get(fieldName);
                					if(field == null){
                    					field = getDeclaredField(objVO.getClass(), fieldName, true);
                    					fieldMap.put(fieldName, field);	
                    				}
            						value = readField(field, objVO, true);
            						writeField(propertyField,parameterMappings.get(i),newProperty);
            					}

                				map.put(newProperty, value);
                				oldPropertyField.put(newProperty, property);
                    		}
                    		if(map.size() > 0){
                    			obj = map;
                    		}
                    		
            			}
            		}
            		for (int i = index; i < parameterMappings.size(); i++) {
            			newParameterMappings.add(parameterMappings.get(i));
            		}
            	}else if(exist == 2){
            		oldPropertyField = new HashMap<String,String>();
            		String[] stringArray = exceSql.split(";");
            		int index = 0;
            		List<String> sqlSet = new ArrayList<String>();
            		Map<String,Object> map = new HashMap<String,Object>();

            		List<?> paramDataList = (List<?>)((Map<String,Object>)boundSql.getParameterObject()).get("list");
            		propertyField = getDeclaredField(ParameterMapping.class,"property",true);
        			Map<String,Field> fieldMap = new HashMap<String,Field>();
            		for (int i = 0; i < stringArray.length; i++) {
            			int lastIndex = stringArray[i].toUpperCase().indexOf("WHERE");
            			String old_sql = exceSql.substring(0, lastIndex);
            			index += old_sql.split("\\?").length - 1;
            			
            			int count = stringArray[i].substring(lastIndex).trim().split("\\?").length;
            			Object objVO = paramDataList.get(i);
            			
            			for (int j = index; (j < index + count) && j < parameterMappings.size(); j++) {
            				
            				String property = parameterMappings.get(j).getProperty();
            				String key = property.substring(property.indexOf(".")+1);
            				
            				Field field = fieldMap.get(key);
            				if(field == null){
            					field = getDeclaredField(objVO.getClass(), key, true);
            					fieldMap.put(key, field);	
            				}
            				Object value = readField(field, objVO, true);
            				
            				String newProperty = property.replaceAll("\\.", "_");
            				writeField(propertyField,parameterMappings.get(j),newProperty);
            				map.put(newProperty, value);
	            			newParameterMappings.add(parameterMappings.get(j));
	            			oldPropertyField.put(newProperty, property);
	            		}
            			index += count;
            			
            			sqlSet.add("("+stringArray[i].substring(lastIndex+5)+")");
					}
            		obj = map;
            		
            		
            		exce_sql = "select "+columnSql+" from "+tableNameAndAs+" where "+StringUtils.join(sqlSet," or ");
            	}
                List<Map<String,Object>> pref_data_list = this.getDataList(exce_sql,obj,mappedStatement,connection,newParameterMappings);
                ServletRequestAttributes servletContainer = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = servletContainer.getRequest();
                if(oldPropertyField != null && oldPropertyField.size() > 0){
                	for (ParameterMapping parameterMappingVO : newParameterMappings) {
                		String property = parameterMappingVO.getProperty();
        				
                		String oldproperty = oldPropertyField.get(property);
                		if(!StringUtils.equals(oldproperty, property)){
                			writeField(propertyField,parameterMappingVO,oldproperty);
                		}
					}
                }
               
                if(pref_data_list.size() > 0){
                    request.setAttribute("pref_data_list",pref_data_list);
                	String separator = " or ";	
                	StringBuffer after_exce_sql = new StringBuffer(); 
                	for (Map<String,Object> prev_dataVO : pref_data_list) {
                		after_exce_sql.append(separator).append(primary_field).append("=").append(prev_dataVO.get(primary_field));
					}
                	exce_sql = "select "+columnSql+" from "+main_tableName+" where "+after_exce_sql.substring(separator.length());
	                request.setAttribute("after_data_list_sql",exce_sql);
	                Map<String,Field> fieldMap = new HashMap<String,Field>();
	                for (Field field : obj.getClass().getDeclaredFields()) {
	                	fieldMap.put(field.getName().toUpperCase(), field);
					}
	                request.setAttribute("columnMap", columnMap);
	                request.setAttribute("primary_field", primary_field);
	                request.setAttribute("table_code", primary_fieldTab);
	                request.setAttribute("databaseId", databaseId);
                }
                
                return invocation.proceed();
            }   
        }    
    	return invocation.proceed();
    }
    
    private static Set<String> notUpdateFieldArray = new HashSet<String>();
    
    private List<Map<String,Object>> getDataList(String excel_sql,Object obj,  MappedStatement mappedStatement, Connection connection,
    		List<ParameterMapping> parameterMappings) {    
       BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), excel_sql, parameterMappings, obj);    
       ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, obj, countBoundSql);    
       PreparedStatement pstmt = null;    
       ResultSet rs = null;    
       List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
       try {    
           pstmt = connection.prepareStatement(excel_sql);    
           parameterHandler.setParameters(pstmt);    
           rs = pstmt.executeQuery(); 
           ResultSetMetaData rsmd = rs.getMetaData();
           while (rs.next()) {    
        	   Map<String,Object> resultMap = new HashMap<String,Object>();
        	   for (int i = 0; i < rsmd.getColumnCount(); i++) {
        		   String columnName = rsmd.getColumnName(i+1).toUpperCase();
        		   if(!notUpdateFieldArray.contains(columnName)){
        			   resultMap.put(columnName, rs.getObject(i+1));
        		   }
        	   }
        	   dataList.add(resultMap);
           }    
       } catch (SQLException e) {    
           e.printStackTrace();    
       } finally {    
           try {    
              if (rs != null)    
                  rs.close();    
               if (pstmt != null)    
                  pstmt.close();    
           } catch (SQLException e) {    
              e.printStackTrace();    
           }    
       }    
       return dataList;
    } 
    private String getComment(String value){
	  if(value == null){
		  return "";
	  }
      int index = value.indexOf(",");
      if(index == -1){
    	  index = value.indexOf("，");
    	  if(index == -1){
    		  index = value.indexOf(".");
    		  if(index == -1){
    			  index = value.indexOf("。");
    		  }
    	  }
      }
      if(index <= 0){
    	  return value;
      }
      return value.substring(0, index);
    }
    /**  
     * 给当前的参数对象page设置总记录数  
     *  
     * @param page Mapper映射语句对应的参数对象  
     * @param mappedStatement Mapper映射语句  
     * @param connection 当前的数据库连接  
     */    
    private synchronized Map<String,String> getCommentsList(String excel_sql, MappedStatement mappedStatement, Connection connection) {
       if(main_columnMap != null){
    	   return main_columnMap;
       }
       Object obj = new Object();
       BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), excel_sql, new ArrayList<ParameterMapping>(), obj);    
       ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, obj, countBoundSql);    
       PreparedStatement pstmt = null;    
       ResultSet rs = null;    
       Map<String,Map<String,String>> main_columnMap = new HashMap<String, Map<String,String>>();
       Map<String,String> main_columnStrMap = new HashMap<String,String>();

       try {    
           pstmt = connection.prepareStatement(excel_sql);    
           parameterHandler.setParameters(pstmt);    
           rs = pstmt.executeQuery(); 
           while (rs.next()) { 	
        	   String table_name = rs.getString(1);
        	   String column_name = rs.getString(2);
        	   String comments = rs.getString(3);
        	   
        	   Map<String,String> columnMap = main_columnMap.get(table_name);
        	   if(columnMap == null){
        		   columnMap = new HashMap<String, String>();
        		   main_columnMap.put(table_name, columnMap);
        	   }
        	   columnMap.put(column_name, getComment(comments));
           }    
       } catch (SQLException e) {    
           e.printStackTrace();    
       } finally {    
           try {    
              if (rs != null)    
                  rs.close();    
               if (pstmt != null)    
                  pstmt.close();    
           } catch (SQLException e) {    
              e.printStackTrace();    
           }    
       }    
       for (String key : main_columnMap.keySet()) {
    	   main_columnStrMap.put(key, toJSONString(main_columnMap.get(key)));
       }
       this.main_columnMap = main_columnStrMap;
       return main_columnStrMap;
    } 
    public <T>T toJSONObject(Class<T> class1, Object jsonObject){
		try {
			return objectMapper.readValue(""+jsonObject, class1);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
    private synchronized Map<String,String> getSys_Baseoperator_TabMapList(String excel_sql, MappedStatement mappedStatement, Connection connection) {
       if(sys_Baseoperator_TabMap != null){
    	   return sys_Baseoperator_TabMap;
       }
       Object obj = new Object();
       BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), excel_sql, new ArrayList<ParameterMapping>(), obj);    
       ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, obj, countBoundSql);    
       PreparedStatement pstmt = null;    
       ResultSet rs = null;    
       
       Map<String,String> columnMap = new HashMap<String,String>();
       try {    
           pstmt = connection.prepareStatement(excel_sql);    
           parameterHandler.setParameters(pstmt);    
           rs = pstmt.executeQuery(); 
           while (rs.next()) {   
        	   
        	   String c1 = rs.getString(1);
        	   String c2 = rs.getString(2);
        	   if(c1 != null){
        		   c1 = c1.toUpperCase();
        	   }
        	   if(c2 != null){
        		   c2 = c2.toUpperCase();
        	   }
        	   columnMap.put(c1, c2);
           }    
       } catch (SQLException e) {    
           e.printStackTrace();    
       } finally {    
           try {    
              if (rs != null)    
                  rs.close();    
               if (pstmt != null)    
                  pstmt.close();    
           } catch (SQLException e) {    
              e.printStackTrace();    
           }    
       }    
       sys_Baseoperator_TabMap = columnMap;
       return columnMap;
    } 
	        

	public String toJSONString(Object result) {
        try {
			return objectMapper.writeValueAsString(result);
		} catch (IOException e) {
//			e.printStackTrace();
			throw new RuntimeException("解析json格式数据错误",e);
		}
	}
    /**  
     * 拦截器对应的封装原始对象的方法  
     */          
    @Override
	public Object plugin(Object arg0) {    
        if (arg0 instanceof StatementHandler) {    
            Object obj = Plugin.wrap(arg0, this);    
        
            return obj;
        } else {    
            return arg0;    
        }   
    }    
    
    /**  
     * 设置注册拦截器时设定的属性  
     */   
    @Override
	public void setProperties(Properties p) {  
    }  
    public Object getFieldValue(Object obj , String fieldName ){  
        if(obj == null){  
            return null ;  
        }  
          
        Field targetField = getTargetField(obj.getClass(), fieldName);  
        return readField(targetField, obj, true ) ;   
    }  
	
	public Object readField(Field targetField, Object obj, boolean bool){
		try {
			targetField.setAccessible(bool);
			return targetField.get(obj);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
      
    public Field getTargetField(Class<?> targetClass, String fieldName) {  
        Field field = null;  
  
        try {  
            if (targetClass == null) {  
                return field;  
            }  
  
            if (Object.class.equals(targetClass)) {  
                return field;  
            }  
  
            field = getDeclaredField(targetClass, fieldName, true);  
            if (field == null) {  
                field = getTargetField(targetClass.getSuperclass(), fieldName);  
            }  
        } catch (Exception e) {  
        }  
  
        return field;  
    }  
    public Field getDeclaredField(Class<?> targetClass, String fieldName, boolean bool){
		try {
			
			for (Field dataField : targetClass.getDeclaredFields()) {
				if(dataField.getName().equals(fieldName)){
					return dataField;
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}
      
    public void writeField(Field targetField , Object obj , Object value ){
    	
    	try {
    		targetField.setAccessible(true);
			targetField.set(obj, value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	      

}
