package com.bba.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JqGridSearchParamHandler {
	
	public static final String EQ = "eq";
	public static final String BW = "bw";

	/**
	 *  1.参数名为 _cd_ 开始的字符会进行截取  name:_cd_create_date 截取后 name:create_date
	 *  
	 */
	private static Map<String, String> operator = new HashMap<String, String>();
	private static ObjectMapper mapper = new ObjectMapper();
	static {
		operator = new HashMap<String, String>();
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
		StringBuffer sqlBuf = new StringBuffer();
		
		sqlBuf.append(formatSql(jqGridParamModel.getFilters(),jqGridParamModel));
		sqlBuf.append(formatSql(customSearchFilters,null));
		
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
			sqlBuf.append(" ORDER BY " + sidx + " " + sord );
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
			List<Map> list = (List<Map>) map.get("rules");
			if(list!=null){

				for(int i = 0; i < list.size(); i++){
					if(list.get(i) instanceof List) {
						List<Map<String, String>> detailList = (List) list.get(i);
						String groupOp = " OR ";
						for(int j = 0; j < detailList.size(); j++){
							Map<String, String> detailMap = detailList.get(j);
							if(j == 0){
								formatSqlDetail(sqlBuf, " AND (", detailMap);
							}else{
								formatSqlDetail(sqlBuf, groupOp, detailMap);
							}
							if(j + 1 == detailList.size()){
								sqlBuf.append(")");
							}
						}
					}else{
						String groupOp = " AND ";
						Map<String,String> param = (Map<String,String>)list.get(i);
						formatSqlDetail(sqlBuf, groupOp, param);
					}


				}				
			}
		}
		return sqlBuf.toString();
	}
	public static void formatSqlDetail(StringBuffer sqlBuf, String groupOp, Map<String,String> param){
		String data = param.get("data");
		if(param.get("op").toString().equals("ni")&&data!=null&&!data.equals("")){
			String dataIn[] = data.split(",");
			data = MyUtils.shuzu(dataIn);
		}else{
			data = StringUtils.trim(data);
		}
		sqlBuf.append(groupOp).append(processOperater(param.get("field"),param.get("op"),data));
	}

	private static String REGEX_DATE = "^\\d{4}-\\d{1,2}-\\d{1,2}$";
	public static String processOperater(String field, String op, String data) {
		/*if(data!=null && data.matches("^'.+'$")){
			data = data.replaceAll(".'.", "''");//
		}else{
			data = data.replaceAll("'", "''");//
		}*/
		StringBuffer condition = new StringBuffer();
		condition.append(field).append(operator.get(op));
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
		} else if (op.equals("lt") || op.equals("le") || op.equals("gt") || op.equals("ge")){
			if(field.startsWith("_cd_")){
				// 处理数据库日期字段是char
				condition = new StringBuffer(field.substring(4)).append(operator.get(op)).append("'"+data+"'");
			}else if(field.startsWith("_d_")){
				condition = new StringBuffer(field.substring(3)).append(operator.get(op)).append("to_date('"+data+"','yyyy-MM-dd')");
			}else if(data.matches(REGEX_DATE)){
				// 处理数据库字段是日期类型
				condition.append("to_date('").append(data).append("','yyyy-MM-dd')");
				//condition.append("'").append(data).append("'");
			}else{
				// 处理数字
				condition.append("'").append(data).append("'");
			}
		} else {
			if(field.startsWith("_cd_")){
				// 字段名称转换
				condition = new StringBuffer(field.substring(4)).append(operator.get(op)).append("'"+data+"'");
			}else if(field.startsWith("_d_")){
				condition = new StringBuffer(field.substring(3)).append(operator.get(op)).append("to_date('"+data+"','yyyy-MM-dd')");
			}else{
				// 单引号 替换为双引号
				if(data!=null){
					data = data.replace("'", "''");
				}
				condition.append("'").append(data).append("'");
			}
		}
		return condition.toString();
	}
}