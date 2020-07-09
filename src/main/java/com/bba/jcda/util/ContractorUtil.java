package com.bba.jcda.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.bba.common.interceptor.EntityField;
import com.bba.util.ArrayUtils;
import com.bba.util.DateUtils;
import com.bba.util.StringUtils;



public class ContractorUtil {
	private static class LazyHolder {    
	       private static final ContractorUtil INSTANCE = new ContractorUtil();    
	    }   
	
	public static final ContractorUtil getInstance(){
		return LazyHolder.INSTANCE;
	}
	
	/**
	 *	生成业务地点参数
	 *
	 * 	@param kind
	 * 	@param locationCode
	 * 	@return List<Map<String, String>>
	 */
	public List<Map<String, String>> generalLocationParam(String kind , String contractor ,String ywlocation){
		List<Map<String, String>> list = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Map<String,String> map = new HashMap<>();
			if(i==0){
				map.put(Const.JCDA_GRID_KEY_FIELD, "a.within_code");
				map.put(Const.JCDA_GRID_KEY_OP, Const.options.get(Const.JCDA_GRID_VAL_OPTION_EQ));
				map.put(Const.JCDA_GRID_KEY_DATA, "b.within_code");
				list.add(map);
			} else if(i==1){
				map.put(Const.JCDA_GRID_KEY_FIELD, "a.sn");
				map.put(Const.JCDA_GRID_KEY_OP, Const.options.get(Const.JCDA_GRID_VAL_OPTION_EQ));
				map.put(Const.JCDA_GRID_KEY_DATA, "b.base_sn");
				list.add(map);
			} else if(i==2){
				map.put(Const.JCDA_GRID_KEY_FIELD, "b.kind");
				map.put(Const.JCDA_GRID_KEY_OP, Const.options.get(Const.JCDA_GRID_VAL_OPTION_EQ));
				map.put(Const.JCDA_GRID_KEY_DATA, "'"+kind+"'");
				list.add(map);
			} else if (i==3){
				map.put(Const.JCDA_GRID_KEY_FIELD, "a.contractor_code");
				map.put(Const.JCDA_GRID_KEY_OP, Const.options.get(Const.JCDA_GRID_VAL_OPTION_EQ));
				map.put(Const.JCDA_GRID_KEY_DATA, "'"+contractor+"'");
				list.add(map);
			} else if (i==4){
				map.put(Const.JCDA_GRID_KEY_FIELD, "b.location_code");
				map.put(Const.JCDA_GRID_KEY_OP, Const.options.get(Const.JCDA_GRID_VAL_OPTION_EQ));
				map.put(Const.JCDA_GRID_KEY_DATA, "'"+ywlocation+"'");
				list.add(map);
			}
		}
		return list;
	}

	public <T> List<T> importEntityExcel(HttpServletRequest request, Class<T> entityClass)
			throws IOException, InvalidFormatException, InstantiationException, IllegalAccessException {
		return importEntityExcel(request, entityClass, null);
	}

	private <T> List<T> importEntityExcel(HttpServletRequest request, Class<T> entityClass, String[] reqFiledArray)
			throws IOException, InvalidFormatException, InstantiationException, IllegalAccessException {
		List<List<List<Object>>> rootlist = importExcel(null, request);

		List<List<Object>> listobj = null;
		if (rootlist.size() > 0) {
			listobj = rootlist.get(0);
		}
		List<T> entityList = new ArrayList<T>();
		if (listobj == null || listobj.size() < 2) {
			// 空行或者只有一个标题行,直接返回
			return entityList;
		}

		// 得到所有有Log注解的字段
		Map<String, Field> filedMap = new LinkedHashMap<String, Field>();
		Field[] filedArray = entityClass.getDeclaredFields();
		for (Field filed : filedArray) {

			EntityField entityFiled = filed.getAnnotation(EntityField.class);
			if (entityFiled == null) {
				continue;
			}

			if (reqFiledArray != null) {

				if (ArrayUtils.contains(reqFiledArray, entityFiled.value())) {
					filedMap.put(entityFiled.value(), filed);
				}
			} else {
				filedMap.put(entityFiled.value(), filed);
			}
		}
		List<Object> titleList = listobj.get(0);
		Map<Integer, Field> mainFiledMap = new LinkedHashMap<Integer, Field>();
		for (int i = 0; i < titleList.size(); i++) {
			if (!filedMap.containsKey(titleList.get(i))) {
				continue;
			}
			mainFiledMap.put(i, filedMap.get(titleList.get(i)));
		}
		for (int i = 1; i < listobj.size(); i++) {

			T entity = entityClass.newInstance();
			for (int j = 0; j < listobj.get(i).size(); j++) {
				if (!mainFiledMap.containsKey(j)) {
					continue;
				}
				Field filed = mainFiledMap.get(j);
				// 修改private私有权限的访问级别
				filed.setAccessible(true);
				filed.set(entity, listobj.get(i).get(j).toString().trim());
			}
			entityList.add(entity);
		}
		return entityList;
	}

	private List<List<List<Object>>> importExcel(HttpServletResponse response, HttpServletRequest request)
			throws IOException {
		List<List<List<Object>>> root = new ArrayList<List<List<Object>>>();// 多个文件的集合
		List<List<Object>> listobj = new ArrayList<List<Object>>();// 集合
		Workbook wb = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile(iter.next());
				try (InputStream fis = file.getInputStream();){
					wb = WorkbookFactory.create(fis);
				} catch (Exception e) {
					throw new RuntimeException("导入文件有误!");
				}
				Sheet sheet = wb.getSheetAt(0);
				for (int i = 0; i <= sheet.getLastRowNum(); i++) {
					List<Object> obj = new ArrayList<Object>(); // 实体
					Row row = sheet.getRow(i);
					if (row == null) {
						throw new RuntimeException("表格不能存在空行!");
					}
					for (int j = 0; j < sheet.getRow(0).getLastCellNum(); j++) {
						Cell cell = row.getCell(j);
						obj.add(getCellValue(cell));
					}
					if (StringUtils.isBlank(StringUtils.join(obj, ""))) {
						break;
					}
					listobj.add(obj);
				}

				root.add(listobj);
			}
		}
		return root;
	}

	private String getCellValue(Cell cell) {
		if (cell == null || StringUtils.isBlank(cell.toString())) {
			return "";
		}
		String v = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BOOLEAN:
			v = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			v = "";
			break;
		case Cell.CELL_TYPE_FORMULA:
			Workbook wb = cell.getSheet().getWorkbook();
			CreationHelper crateHelper = wb.getCreationHelper();
			FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
			v = getCellValue(evaluator.evaluateInCell(cell));
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				// 日期
				short dataFormat = cell.getCellStyle().getDataFormat();
				if (dataFormat == 22) {
					v = DateUtils.dateFormat(cell.getDateCellValue(), DateUtils.NORMAL_DATE_FORMAT_NEW);
				} else {
					v = DateUtils.dateFormat(cell.getDateCellValue());
				}
			} else {
				DecimalFormat df = new DecimalFormat("#.##");
				v = df.format(cell.getNumericCellValue());
			}
			break;
		case HSSFCell.CELL_TYPE_STRING:
			if(cell.getStringCellValue().indexOf("编号")!=-1){
				v = String.valueOf("编号");
			} else if(StringUtils.equals(cell.getStringCellValue(), "是否启用")) {
				v = String.valueOf("固定发送");
			} else {
				v = String.valueOf(cell.getStringCellValue());
			}
			break;
		default:
			if(cell.getStringCellValue().indexOf("编号")!=-1){
				v = String.valueOf("编号");
			} else if(StringUtils.equals(cell.getStringCellValue(), "是否启用")) {
				v = String.valueOf("固定发送");
			} else {
				v = cell.getStringCellValue();
			}
			break;
		}
		return v;
	}
}
