package com.bba.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class JqGridSearchParamHandlers {
	public static final String EQ = "eq";
	public static final String BW = "bw";
	
	
	public static final String SQL_COLUMNS = "columns";
	public static final String SQL_FROM = "from";

	/**
	 *  1.参数名为 _cd_ 开始的字符会进行截取  name:_cd_create_date 截取后 name:create_date
	 *  
	 */
	private static Map<String, String> operator = new HashMap<String, String>();
	private static ObjectMapper mapper = new ObjectMapper();
	
	private static String REGEX_DATE = "^\\d{4}-\\d{1,2}-\\d{1,2}$";
	
	private static String REGEX_DATETIME = "^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$";
	
	static {
		operator = new HashMap<String, String>();
		// ['eq','ne','lt','le','gt','ge','bw','bn','in','ni','ew','en','cn','nc']
		operator.put("eq", " = ");
		operator.put("ne", " <> ");
		operator.put("lt", " < ");
		operator.put("le", " <= ");
		operator.put("gt", " > ");
		operator.put("ge", " >= ");
		operator.put("bw", " LIKE ");
		operator.put("bn", " NOT LIKE ");
		operator.put("in", " IN ");
		operator.put("ni", " NOT IN ");
		operator.put("ew", " LIKE ");
		operator.put("en", " NOT LIKE ");
		operator.put("cn", " LIKE ");
		operator.put("nc", " NOT LIKE ");
		operator.put("inn", " IS ");

	}
	/**
	 * @param myJqGridParam
	 *            :自己的参数
	 * @param jqGridParam
	 *            : jqGrid的参数
	 * @return sql条件语句
	 */
	public static String processSql(String customSearchFilters,
			JqGridParamModel jqGridParamModel) {
		return processSql(customSearchFilters,jqGridParamModel,null,true);
	}
	
	public static String processSql(String customSearchFilters,
			JqGridParamModel jqGridParamModel,List<Map<String, String>> fields) {
		return processSql(customSearchFilters,jqGridParamModel,fields,false);
	}
	
	public static String processSql(String customSearchFilters,
			JqGridParamModel jqGridParamModel,List<Map<String, String>> fields,boolean isModelField) {
		StringBuffer sqlBuf = new StringBuffer();
		
		sqlBuf.append(formatSql(jqGridParamModel.getFilters(),jqGridParamModel,fields,isModelField));
		if(fields!=null){
			sqlBuf.append(formatSql(customSearchFilters,null,fields,true));
		} else {
			sqlBuf.append(formatSql(customSearchFilters,null));
		}
		
		//处理排序
		String sidx = jqGridParamModel.getSidx();
		String sord = jqGridParamModel.getSord();
		if (StringUtils.isNotEmpty(sidx)) {
			if(sidx.startsWith("_cd_")){
				// 进行字段转化
				sidx = sidx.substring(4);
			}else if(sidx.startsWith("_d_")){
				sidx = sidx.substring(3);
			}
			if(StringUtils.isBlank(sord)){
				sord = "";
			}
			
			if(fields!=null){
				if(sidx.contains(".")){
					sqlBuf.append(" ORDER BY " + sidx + " " + sord );
				} else {
					sqlBuf.append(" ORDER BY " + ("a."+sidx) + " " + sord );
				}
			} else {
				sqlBuf.append(" ORDER BY " + sidx + " " + sord );
			}
			
		}
		String sql = sqlBuf.toString();
		if (sql.startsWith(" AND ")) {
			sql = sql.substring(4);
		}
		else if(sql.startsWith(" OR "))
		{
			sql = sql.substring(3);	
		}
		else if (sql.startsWith(" ORDER ")) {
			sql = "1=1 " + sql;
		}
		return sql;
	}

	

	public static String formatSql(String filters,JqGridParamModel jqGridParamModel){
		return formatSql(filters,jqGridParamModel,null,true);
	}
	
	public static String formatSql(String filters,JqGridParamModel jqGridParamModel ,List<Map<String, String>> fieldParams ,boolean isModelField){
		StringBuffer sqlBuf = new StringBuffer("");
		if(StringUtils.isEmpty(filters)){
			return sqlBuf.toString();
		}
		Map map = null; 
		try {
			map = mapper.readValue(filters, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(map!=null){
			String groupOp = (String)map.get("groupOp");
			if(!StringUtils.equals(groupOp, "AND") && !StringUtils.equals(groupOp, "OR") ){
				groupOp = "AND";
			}
			groupOp = " "+groupOp+" ";
			List<Map> list = (List<Map>) map.get("rules");
			if(list!=null){
				for(Map<String,String> param : list){
					String data = param.get("data");
					if(param.get("op").toString().equals("ni")&&data!=null&&!data.equals("")){
						String dataIn[] = data.split(",");
						data = MyUtils.shuzu(dataIn);
					}else{
						data = StringUtils.trim(data);
					}
					
					if(fieldParams==null){
						if(!isModelField){
							sqlBuf.append(groupOp).append(
									processOperater(param.get("field"),param.get("op"),data,"",isModelField));
						} else {
							sqlBuf.append(groupOp).append(
									processOperater(param.get("field"),param.get("op"),data,null,isModelField));
						}
					} else {
						sqlBuf.append(groupOp).append(
								processOperater(param.get("field"),param.get("op"),data,fieldParams==null?"":fieldParams.get(2).get("data"),true));	
					}
				}				
			}
			if(fieldParams!=null && !isModelField){
				for (int i = 0; i < fieldParams.size(); i++) {
					String data = fieldParams.get(i).get("data");
					if(fieldParams.get(i).get("op").toString().equals("ni")&&data!=null&&!data.equals("")){
						String dataIn[] = data.split(",");
						data = MyUtils.shuzu(dataIn);
					}else{
						data = StringUtils.trim(data);
					}
					sqlBuf.append(" AND "+processOperater(fieldParams.get(i).get("field"),fieldParams.get(i).get("op"),data,fieldParams.get(2).get("data"),false));
				}
			}
			
		}
		return sqlBuf.toString();
	}
	

	
	public static String processOperater(String field, String op, String data,String kind,boolean isTableField) {
		/*if(data!=null && data.matches("^'.+'$")){
			data = data.replaceAll(".'.", "''");//
		}else{
			data = data.replaceAll("'", "''");//
		}*/
		StringBuffer condition = new StringBuffer();
		
		if(data!=null && !data.contains(",") || data.contains("||") || data.split(",")[0].contains("'")){
			if(kind!=null){
				if(isTableField){
					if(field.contains(".")){
						condition.append(field).append(operator.get(op));
					} else {
						condition.append("a."+field).append(operator.get(op));
					}
				} else {
					condition.append(field).append(operator.get(op));
				}
			} else {
				condition.append(field).append(operator.get(op));
			}
			if (op.equals("in") || op.equals("ni")) {
				condition.append("(").append(data).append(")");
			} else if (op.equals("bw") || op.equals("bn")) {
				condition.append("'").append(data).append("%'");
			} else if (op.equals("ew") || op.equals("en")) {
				condition.append("'%").append(data).append("'");
			} else if (op.equals("cn") || op.equals("nc")) {
				// 单引号 替换为双引号
				if(data!=null){
					data = data.replace("'", "''");
				}
				condition.append("'%").append(data).append("%'");
			} else if (op.equals("lt") || op.equals("le") || op.equals("gt") || op.equals("ge") || op.equals("eq")){
				if(field.startsWith("_cd_")){
					// 处理数据库日期字段是char
					condition = new StringBuffer(field.substring(4)).append(operator.get(op)).append("'"+data+"'");
				}else if(field.startsWith("_d_")){
					condition = new StringBuffer(field.substring(3)).append(operator.get(op)).append("to_date('"+data+"','yyyy-MM-dd hh24:mi:ss')");
				}else if(data.matches(REGEX_DATETIME)){
					condition.append("to_date('").append(data).append("','yyyy-MM-dd hh24:mi:ss')");
				}else if(data.matches(REGEX_DATE)){
					// 处理数据库字段是日期类型
					condition.append("to_date('").append(data).append("','yyyy-MM-dd')");
					/*condition.append("'").append(data).append("'");*/
				}else if(kind!=null && !isTableField) {
					condition.append(data);
				}else{
					// 处理数字
					condition.append("'").append(data).append("'");
				}
			} else {
				if(field.startsWith("_cd_")){
					// 字段名称转换
					condition = new StringBuffer(field.substring(4)).append(operator.get(op)).append("'"+data+"'");
				}else if(field.startsWith("_d_")){
					condition = new StringBuffer(field.substring(3)).append(operator.get(op)).append("to_date('"+data+"','yyyy-MM-dd hh24:mi:ss')");
				} else{
					// 单引号 替换为双引号
					if(data!=null){
						data = data.replace("'", "''");
					}
					condition.append("'").append(data).append("'");
				}

			}
		} else {
			String[] datas = data.split(",");
			
			for (int i = 0; i < datas.length; i++) {
				
				if(i!=0){
					condition.append(" OR ");
				} else {
					condition.append("(");
				}
				if(kind!=null){
					if(isTableField){
						if(field.contains(".")){
							condition.append(field).append(operator.get(op));
						} else {
							condition.append("a."+field).append(operator.get(op));
						}
					} else {
						condition.append(field).append(operator.get(op));
					}
				} else {
					condition.append(field).append(operator.get(op));
				}
				if(kind!=null && !isTableField) {
					condition.append(datas[i]);
				}else{
					// 处理数字
					if(StringUtils.equals(op, "cn")){
						condition.append("'%").append(datas[i]).append("%'");
					} else {
						condition.append("'").append(datas[i]).append("'");
					}
				}

				if(i==datas.length-1){
					condition.append(")");
				}
			}
		}
		if(op.equals("inn")) {
			return condition.toString().replace("'","");
		} else {
			return condition.toString();
		}

	}

}
