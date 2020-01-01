package com.bba.util;

import com.bba.bean.Resource;
import com.bba.common.interceptor.EntityField;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.*;
public class POIUtils {

	private static final String TEXT_NUMBER_REGEXP = "^0[0-9]{1,}$";
	
	public static String getCellValue(Cell cell){
		if(cell == null || StringUtils.isBlank(cell.toString())){
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
	    	 if(DateUtil.isCellDateFormatted(cell)){
				// 日期
	    		short dataFormat = cell.getCellStyle().getDataFormat();
	    		if(dataFormat == 22){
	      			v = DateUtils.dateFormat(cell.getDateCellValue(),DateUtils.NORMAL_DATE_FORMAT_NEW);
	       		}else{
	    			v = DateUtils.dateFormat(cell.getDateCellValue());
	    		}
	    	 }else{
	    		 DecimalFormat df = new DecimalFormat("#.##");
	    		 v = df.format(cell.getNumericCellValue());
	    	 }
	    	 break;
		 case HSSFCell.CELL_TYPE_STRING:
			 v = String.valueOf(cell.getStringCellValue());
			 break;
	     default:
			 v = cell.getStringCellValue();
			 break;
		}
		return v;
	}


	public static List<List<Object>> importExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<List<Object>> listObj = new ArrayList<List<Object>>();
		Workbook wb = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if(multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> iter = multiRequest.getFileNames();
			while(iter.hasNext()) {
				MultipartFile file = multiRequest.getFile(iter.next());
				try (InputStream fils = file.getInputStream();) {
					wb = WorkbookFactory.create(fils);
				} catch(Exception e) {
					throw new RuntimeException(("导入文件有误！"));
				}
				Sheet sheet = wb.getSheetAt(0);
				for (int i = 0; i <= sheet.getLastRowNum(); i++) {
					List<Object> obj = new ArrayList<Object>();    //实体
					Row row = sheet.getRow(i);
					if (row == null) {
						// throw new RuntimeException("表格不能存在空行!");
						continue;
					}
					for (int j = 0; j < sheet.getRow(0).getLastCellNum(); j++) {
						Cell cell = row.getCell(j);
						obj.add(POIUtils.getCellValue(cell));
					}
					if (StringUtils.isBlank(StringUtils.join(obj, ""))) {
						continue;
					}
					listObj.add(obj);
				}
			}
		}
		return listObj;

	}


	  public static List<List<List<Object>>> importExcel(HttpServletResponse response,HttpServletRequest request) throws IOException {
		List<List<List<Object>>> root = new ArrayList<List<List<Object>>>();//多个文件的集合
		List<List<Object>> listobj = new ArrayList<List<Object>>();//集合
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
					wb =  WorkbookFactory.create(fis);
				} catch (Exception e) {
					throw new RuntimeException("导入文件有误!");
				}
				Sheet sheet = wb.getSheetAt(0);
				for (int i = 0; i <= sheet.getLastRowNum(); i++) {
					List<Object> obj = new ArrayList<Object>();	//实体
					Row row = sheet.getRow(i);
					if(row == null){
						// throw new RuntimeException("表格不能存在空行!");
						continue;
					}
					for (int j = 0; j < sheet.getRow(0).getLastCellNum(); j++) {
						Cell cell = row.getCell(j);
						obj.add(getCellValue(cell));
					}
					if(StringUtils.isBlank(StringUtils.join(obj,""))){
						break;
					}
					listobj.add(obj);
				}
				
				root.add(listobj);
			}
		}
		return root;
	}
  
	  public static <T>List<T> importEntityExcel(HttpServletRequest request, Class<T> entityClass)
			  throws IOException, InvalidFormatException, InstantiationException, IllegalAccessException {
		  return importEntityExcel(request, entityClass, null);
	  }
	  
	  public static <T>List<T> importEntityExcel(HttpServletRequest request, Class<T> entityClass, String[] reqFiledArray)
		  throws IOException, InvalidFormatException, InstantiationException, IllegalAccessException {
		  List<List<List<Object>>> rootlist = importExcel(null, request);
		  
		  List<List<Object>> listobj = null;
		  if(rootlist.size() > 0){
			  listobj = rootlist.get(0);
		  }
		  List<T> entityList = new ArrayList<T>();
			  if(listobj == null || listobj.size() < 2){
				  //空行或者只有一个标题行,直接返回
				  return entityList;
			  }
		  
		  List<Object> titleList = listobj.get(0);
		  //得到所有有Log注解的字段
		  Map<String, Field> filedMap = new LinkedHashMap<String, Field>();
		  Field[] filedArray = entityClass.getDeclaredFields();
		  for (Field filed : filedArray){
			  
			 EntityField entityFiled = filed.getAnnotation(EntityField.class);
			 if(entityFiled == null){
				 continue;
			 }
			 
			 if(reqFiledArray != null){
				
				 if(ArrayUtils.contains(reqFiledArray, entityFiled.value())){
					 filedMap.put(entityFiled.value(), filed);
				 }
			 }else{
				 boolean flag = false;
				 for (Object title : titleList) {
					if(StringUtils.equals(title!=null?title.toString():"",entityFiled.value() )){
						flag = true;
						break;
					}
				 }
				 if(flag){
					 filedMap.put(entityFiled.value(), filed);
				 } else {
					 filedMap.put(entityFiled.value_en(), filed);
				 }
			 }
		  }
		  
		  Map<Integer, Field> mainFiledMap = new LinkedHashMap<Integer, Field>();
		  for (int i = 0; i < titleList.size(); i++) {
			  if(!filedMap.containsKey(titleList.get(i))){
				  continue;
			  }
			  mainFiledMap.put(i, filedMap.get(titleList.get(i)));
		  }
		  for (int i = 1; i < listobj.size(); i++) {
			  
			  T entity = entityClass.newInstance();
			  for (int j = 0; j < listobj.get(i).size(); j++) {
				 if(!mainFiledMap.containsKey(j)){
					 continue;
				 }
				 Field filed = mainFiledMap.get(j);
				 //修改private私有权限的访问级别
				 filed.setAccessible(true);
				 filed.set(entity, listobj.get(i).get(j).toString().trim());
			  }
			  entityList.add(entity);
		  }
		  return entityList;
	  }

	public static <T> void commonClassExport(CommonExportVO commonExportVO,Class<T> entityClass,List<T> dataList, HttpServletResponse response) throws Exception{

		String[] columnName = null;
		String[] columnAlign =null;
		String[] columnWidth =null;
		String[] columnCode = null;

		if( commonExportVO.getExport_ColumnCode()[0].indexOf(",")!=-1) {
			columnCode = commonExportVO.getExport_ColumnCode()[0].split(",");
			columnName = commonExportVO.getExport_ColumnName()[0].split(",");
			columnWidth = commonExportVO.getExport_ColumnWidth()[0].split(",");
			columnAlign = commonExportVO.getExport_ColumnAlign()[0].split(",");
		}
		else {
			columnCode = commonExportVO.getExport_ColumnCode();
			columnName = commonExportVO.getExport_ColumnName();
			columnWidth = commonExportVO.getExport_ColumnWidth();
			columnAlign = commonExportVO.getExport_ColumnAlign();

		}

		List<List<Object>> objectList = new ArrayList<List<Object>>();

		List<Object> titleList = new ArrayList<Object>();

		//得到所有有Log注解的字段
		Map<String, Field> filedMap = new LinkedHashMap<String, Field>();
		Field[] filedArray = entityClass.getDeclaredFields();
		String[] reqFiledArray = columnName;
		String[] reqFiledCodeArray = columnCode;
		for (int i = 0; i < reqFiledArray.length; i++) {
			titleList.add(reqFiledArray[i]);
		}
		for (Field filed : filedArray){
			EntityField entityFiled = filed.getAnnotation(EntityField.class);
			if(entityFiled == null){
				continue;
			}
			int index = ArrayUtils.indexOf(reqFiledCodeArray, filed.getName());
			if(index > -1){
				filedMap.put(filed.getName(), filed);
			}
		}
		objectList.add(titleList);
		for (T entity : dataList) {
			List<Object> objList = new ArrayList<Object>();
			for (int i = 0; i < reqFiledCodeArray.length; i++) {
				Field field = filedMap.get(reqFiledCodeArray[i]);
				if(field != null){
					field.setAccessible(true);
					Object value = field.get(entity);
					objList.add(value);
				}else{
					objList.add("");
				}
			}
			objectList.add(objList);
		}
//		for (int i = 0; i < dataList.size(); i++) {
//			  List<Object> objList = new ArrayList<Object>();
//			  for (int j = 0; j < reqFiledCodeArray.length; j++) {
//				  objList.add(reqFiledArray[j]);
//			  }
//			  objectList.add(objList);
//		}
		String title = commonExportVO.getExport_title();
		title = new String(title.getBytes("GBK"), "ISO8859_1");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + title + ".xlsx");
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		CellStyle textStyle = wb.createCellStyle();

		textStyle.setBorderBottom((short)1);
		textStyle.setBorderTop((short)1);
		textStyle.setBorderLeft((short)1);
		textStyle.setBorderRight((short)1);

		textStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		textStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式



		Font font = wb.createFont();
		font.setFontName("微软雅黑");
		font.setFontHeightInPoints((short)9);
		textStyle.setFont(font);


		CellStyle leftTextStyle = wb.createCellStyle();

		leftTextStyle.setBorderBottom((short)1);
		leftTextStyle.setBorderTop((short)1);
		leftTextStyle.setBorderLeft((short)1);
		leftTextStyle.setBorderRight((short)1);
		leftTextStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		leftTextStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		leftTextStyle.setFont(font);


		CellStyle rightTextStyle = wb.createCellStyle();

		rightTextStyle.setBorderBottom((short)1);
		rightTextStyle.setBorderTop((short)1);
		rightTextStyle.setBorderLeft((short)1);
		rightTextStyle.setBorderRight((short)1);
		rightTextStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		rightTextStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 创建一个居中格式
		rightTextStyle.setFont(font);


		for (int i = 0; i < objectList.size(); i++) {
			Row row = sheet.createRow(i);
			for (int j = 0; j < objectList.get(i).size(); j++) {

				Object value = objectList.get(i).get(j);
				Cell cell = row.createCell(j);

				if(i == 0) {
					cell.setCellStyle(textStyle);
				}else {
					if(StringUtils.equals(columnAlign[j], "left")){
						cell.setCellStyle(leftTextStyle);
					}else if(StringUtils.equals(columnAlign[j],  "center")){
						cell.setCellStyle(textStyle);
					}else{
						cell.setCellStyle(rightTextStyle);
					}
				}

				if(value == null){
					value = "";
					cell.setCellValue(value.toString());

					//文本格式

				}else if(StringUtils.isNumberspace(value+"")){
					//数字格式
					if(value.toString().length()>21){
						cell.setCellValue(value.toString());
					} else {
						cell.setCellValue(Double.parseDouble(value.toString()));
					}
				}else{
					//文本格式
					cell.setCellValue(value.toString());
				}

			}
		}
		for (int i = 0; i < columnWidth.length; i++) {
			sheet.setColumnWidth(i,(int)(Integer.valueOf(columnWidth[i]) * 41.5));
		}


		try (OutputStream out = response.getOutputStream()) {
			wb.write(out);
		}catch (Exception e){
			e.printStackTrace();
		}

	}
	
	  public static <T> void commonClassExport(CommonExportVO<T> commonExportVO, List<Map> dataList,
			  HttpServletResponse response) throws Exception{

		  String[] columnName = null;
		  String[] columnAlign =null;
		  String[] columnWidth =null;
		  String[] columnCode = null;
		  String[] columnNeed = null;
		  if( commonExportVO.getExport_ColumnCode()[0].indexOf(",")!=-1) {
			  columnCode = commonExportVO.getExport_ColumnCode()[0].split(",");
			  columnName = commonExportVO.getExport_ColumnName()[0].split(",");
			  columnWidth = commonExportVO.getExport_ColumnWidth()[0].split(",");
			  columnAlign = commonExportVO.getExport_ColumnAlign()[0].split(",");
		  }
		  else {
			  columnCode = commonExportVO.getExport_ColumnCode();
			  columnName = commonExportVO.getExport_ColumnName();
			  columnWidth = commonExportVO.getExport_ColumnWidth();
			  columnAlign = commonExportVO.getExport_ColumnAlign();

		  }

		  if(null!=commonExportVO.getExport_ColumnNeed() && commonExportVO.getExport_ColumnNeed().length>0 && StringUtils.isNotBlank(commonExportVO.getExport_ColumnNeed()[0]))
		  {
			  columnNeed = commonExportVO.getExport_ColumnNeed()[0].split(",");
		  }

		  Map<String, Object> titleMap = new HashMap<String, Object>();
		  for (int i = 0; i < columnCode.length; i++) {
			  titleMap.put(columnCode[i], columnName[i]);
		  }
		  dataList.add(0, titleMap);
		  
	    String title = commonExportVO.getExport_title();
	    title = new String(title.getBytes("GBK"), "ISO8859_1");
		response.setCharacterEncoding("UTF-8");
    	response.setHeader("Content-Disposition", "attachment;filename=" + title + ".xlsx");
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		CellStyle textStyle = wb.createCellStyle();
		


		Font font = wb.createFont();
		font.setFontName("微软雅黑");
		font.setFontHeightInPoints((short)9);
		font.setColor(Font.COLOR_NORMAL);
		textStyle.setFont(font);


		CellStyle redStyle = wb.createCellStyle();
		Font font_red = wb.createFont();
		font_red.setFontName("微软雅黑");
		font_red.setFontHeightInPoints((short)9);
		font_red.setColor(Font.COLOR_RED);
		redStyle.setFont(font_red);
		  redStyle.setBorderBottom((short)1);
		  redStyle.setBorderTop((short)1);
		  redStyle.setBorderLeft((short)1);
		  redStyle.setBorderRight((short)1);

		  redStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		  redStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		
		textStyle.setBorderBottom((short)1);
		textStyle.setBorderTop((short)1);
		textStyle.setBorderLeft((short)1);
		textStyle.setBorderRight((short)1);
		
		textStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		textStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		
		CellStyle leftTextStyle = wb.createCellStyle();
		
		leftTextStyle.setBorderBottom((short)1);
		leftTextStyle.setBorderTop((short)1);
		leftTextStyle.setBorderLeft((short)1);
		leftTextStyle.setBorderRight((short)1);
		leftTextStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		leftTextStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		
		leftTextStyle.setFont(font);

		CellStyle rightTextStyle = wb.createCellStyle();
		
		rightTextStyle.setBorderBottom((short)1);
		rightTextStyle.setBorderTop((short)1);
		rightTextStyle.setBorderLeft((short)1);
		rightTextStyle.setBorderRight((short)1);
		rightTextStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		rightTextStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 创建一个居中格式
		rightTextStyle.setFont(font);
		
		DataFormat format = wb.createDataFormat();
		
		CellStyle textStyleTextnumber = wb.createCellStyle();
		CellStyle leftTextStyleTextnumber = wb.createCellStyle();
		CellStyle rightTextStyleTextnumber = wb.createCellStyle();
		textStyleTextnumber.cloneStyleFrom(textStyle);
		leftTextStyleTextnumber.cloneStyleFrom(leftTextStyle);
		rightTextStyleTextnumber.cloneStyleFrom(rightTextStyle);
		textStyleTextnumber.setDataFormat(format.getFormat("@"));
		leftTextStyleTextnumber.setDataFormat(format.getFormat("@"));
		rightTextStyleTextnumber.setDataFormat(format.getFormat("@"));
		
		for (int i = 0; i < dataList.size(); i++) {
			Row row = sheet.createRow(i);
			
			Map<String, Object> vo = dataList.get(i);
			for (int j = 0; j < columnCode.length; j++) {
				String value = StringUtils.nvl(vo.get(columnCode[j]), "");
				Cell cell = row.createCell(j);

				if(i == 0){
					//设置红色字体 必填项
					if(null!=columnNeed)
					{
						Boolean bNeed=false;
						for(String cNeed:columnNeed)
						{
							if(columnCode[j].equalsIgnoreCase(cNeed))
							{
								bNeed=true;
								break;
							}
						}
						cell.setCellStyle(bNeed?redStyle:textStyle);
					}else{
						cell.setCellStyle(textStyle);
					}
					cell.setCellValue(value);
					continue;
				}
				if(StringUtils.isNumberspace(value)){
					if(value.matches(TEXT_NUMBER_REGEXP)){
						//如0000501这种格式数值,以文本格式输出
						if(StringUtils.equals(columnAlign[j], "left")){
							cell.setCellStyle(leftTextStyleTextnumber);
						}else if(StringUtils.equals(columnAlign[j],  "center")){
							cell.setCellStyle(textStyleTextnumber);
						}else{
							cell.setCellStyle(rightTextStyleTextnumber);
						}
						cell.setCellValue(value);
					}else{
						if(StringUtils.equals(columnAlign[j], "left")){
							cell.setCellStyle(leftTextStyle);
						}else if(StringUtils.equals(columnAlign[j],  "center")){
							cell.setCellStyle(textStyle);
						}else{
							cell.setCellStyle(rightTextStyle);
						}
						//数字格式
						cell.setCellValue(Double.parseDouble(value));
					}
				}else{
					if(StringUtils.equals(columnAlign[j], "left")){
						cell.setCellStyle(leftTextStyle);
					}else if(StringUtils.equals(columnAlign[j],  "center")){
						cell.setCellStyle(textStyle);
					}else{
						cell.setCellStyle(rightTextStyle);
					}
					//文本格式
		    		cell.setCellValue(value);
				}
					
			}
		}
		for (int i = 0; i < columnWidth.length; i++) {
	        sheet.setColumnWidth(i,(int)(Integer.valueOf(columnWidth[i]) * 41.5));
		} 

		
		try(OutputStream out = response.getOutputStream();){
		    wb.write(out);
		}catch (Exception e){
		    e.printStackTrace();
		}

	  }

	  /**
	   * @Description 导出模板新
	   * @param
	   * @Author lao li
	   * @Date
	   * @return
	  */
	public static void commonExportTemplate(HttpServletRequest request, HttpServletResponse response, String fileName, String url){
		try {
			response.setCharacterEncoding("UTF-8");
			fileName = new String(fileName.getBytes("GBK"), "ISO8859_1");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
			response.setDateHeader("Expires", 0);
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			InputStream fis = ClassUtils.class.getClassLoader().getResourceAsStream(url);
			Workbook wb = WorkbookFactory.create(fis);
			OutputStream out = response.getOutputStream();
			wb.write(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} finally {
		}
	}

	/**
	 * 导出excel模板
	 * @param fileName  文件名
	 * @param url 		文件相对路径
	 * 
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 */
/*	public static void exportTemplate(HttpServletRequest request, HttpServletResponse response, String fileName, String url)
				throws IOException, InvalidFormatException {

		ClassPathResource resource = new ClassPathResource("static/excel/userTemplate.xlsx");
		resource.getInputStream();
//		url = "/root/niutms/BOOT-INF/classes/static/" + url;
		fileName = new String(fileName.getBytes("GBK"), "ISO8859_1");
		response.setCharacterEncoding("UTF-8");
		
		String fileWith = url.substring(url.lastIndexOf(".")+1);
		System.out.printf("-------------------------------------" + fileWith + "------------------------------------");
		if(fileWith.equals("txt")){
			response.setContentType("application/octet-stream");

			InputStream ins = ClassUtils.class.getClassLoader().getResourceAsStream("static/"+url);
			byte[] buffer = new byte[ins.available()];
			ins.read(buffer);
			ins.close();
			
			File file = new File(url);
			response.setContentType("text/plain");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ".txt");
			response.addHeader("Content-Length", "" + file.length());

			System.out.println("------------------------------" + url + "------------------------------");
			try (OutputStream ous = new BufferedOutputStream(response.getOutputStream());){
				ous.write(buffer);
			} catch (Exception e) {
				throw e;
			}
		}else{
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName + "." + fileWith);
			response.setDateHeader("Expires", 0);
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			String serverURL = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/" + url;
			FileInputStream fis = new FileInputStream(serverURL);
			Workbook wb = WorkbookFactory.create(fis);
			try (OutputStream out = response.getOutputStream();){
				wb.write(out);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}*/

	public static void exportTemplate(HttpServletRequest request, HttpServletResponse response, String fileName, String url)
			throws IOException, InvalidFormatException {
		fileName = new String(fileName.getBytes("GBK"), "ISO8859_1");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		//String serverURL = request.getSession().getServletContext().getRealPath("/") + url;
		InputStream fis = ClassUtils.class.getClassLoader().getResourceAsStream("static/"+url);
		Workbook wb = WorkbookFactory.create(fis);
		OutputStream out = response.getOutputStream();
		wb.write(out);
		out.flush();
		out.close();
	}


	public static List<List<String>> importTxtFile(HttpServletResponse response, HttpServletRequest request) throws IOException {
		InputStream inputStream = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				MultipartFile file = multiRequest.getFile(iter.next());
				inputStream = file.getInputStream();
				break;
			}
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		List<List<String>> objectList = new ArrayList<List<String>>();
		try {
			String s = null;
			while ((s = br.readLine()) != null) {
				String[] strArray = StringUtils.trim(s).replace("\t", "  ").split("\\s{2,}");
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < strArray.length; i++) {
					list.add(strArray[i]);
				}
				objectList.add(list);
			}
		} catch (IOException e) {
			throw e;
		}finally{
			if(inputStream != null){
				inputStream.close();
			}
		}
		return objectList;
	}

	/**
	 * 功能描述: 下载文件
	 * @param dataPath 文件路径
	 * @param fileName 文件名称
	 * @return:
	 * @auther: laoli
	 * @date: 2019/1/7 14:17
	 */
	public static void downloadFile(HttpServletRequest request, HttpServletResponse response, String fileName, String dataPath) throws IOException, InvalidFormatException{
		fileName = new String(fileName.getBytes("GBK"), "ISO8859_1");
		response.setCharacterEncoding("UTF-8");

		String fileWith = dataPath.substring(dataPath.lastIndexOf(".")+1);
		if(fileWith.equals("txt")){
			response.setContentType("application/octet-stream");

			InputStream ins = ClassUtils.class.getClassLoader().getResourceAsStream(dataPath);
			byte[] buffer = new byte[ins.available()];
			ins.read(buffer);
			ins.close();

			File file = new File(dataPath);
			response.setContentType("text/plain");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName + ".txt");
			response.addHeader("Content-Length", "" + file.length());

			try (OutputStream ous = new BufferedOutputStream(response.getOutputStream());){
				ous.write(buffer);
			} catch (Exception e) {
				throw e;
			}
		}else if(fileWith.equals("xls") || fileWith.equals("xlsx")){
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName + "." + fileWith);
			response.setDateHeader("Expires", 0);
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			String serverURL = ClassUtils.getDefaultClassLoader().getResource("").getPath()  + dataPath;
			FileInputStream fis = new FileInputStream(serverURL);
			Workbook wb = WorkbookFactory.create(fis);
			try (OutputStream out = response.getOutputStream();){
				wb.write(out);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// dataPath 文件路径
			// fileName 文件名称
			Resource resource = SpringUtil.getBean(Resource.class);
			String serverURL = resource.getUploadPath()  + dataPath;
			response.setContentType("application/x-download");
			response.addHeader("Content-Disposition","attachment;filename=" + fileName + "." + fileWith);
			OutputStream outp = response.getOutputStream();
			FileInputStream in =  new FileInputStream(serverURL);
			byte[] b = new byte[1024];
			int i = 0;
			while((i = in.read(b)) > 0)  {
				outp.write(b, 0, i);
			}
			outp.flush();
			in.close();
			outp.close();
		}
	}

/*	public static void copySheet( XSSFWorkbook wb, XSSFSheet fromsheet,  XSSFSheet newSheet, int firstrow, int lasttrow) {
		// 复制一个单元格样式到新建单元格
		if ((firstrow == -1) || (lasttrow == -1) || lasttrow < firstrow) {
			return;
		}
		// 复制合并的单元格
		for (int i = 0; i < fromsheet.getNumMergedRegions(); i++) {
			CellRangeAddress cellR = null;
			cellR = fromsheet.getMergedRegion(i);
			newSheet.addMergedRegion(cellR);
		}
		XSSFRow fromRow = null;
		XSSFRow newRow = null;
		XSSFCell newCell = null;
		XSSFCell fromCell = null;
		// 设置列宽
		for (int i = firstrow; i < lasttrow; i++) {
			fromRow = fromsheet.getRow(i);
			if (fromRow != null) {
				for (int j = fromRow.getLastCellNum(); j >= fromRow.getFirstCellNum(); j--) {
					int colnum = fromsheet.getColumnWidth((short) j);
					if (colnum > 100) {
						newSheet.setColumnWidth((short) j, (short) colnum);
					}
					if (colnum == 0) {
						newSheet.setColumnHidden((short) j, true);
					} else {
						newSheet.setColumnHidden((short) j, false);
					}
				}
				break;
			}
		}
		// 复制行并填充数据
		for (int i = 0; i < lasttrow; i++) {
			fromRow = fromsheet.getRow(i);
			if (fromRow == null) {
				continue;
			}
			newRow = newSheet.createRow(i - firstrow);
			newRow.setHeight(fromRow.getHeight());
			for (int j = fromRow.getFirstCellNum(); j < fromRow.getLastCellNum(); j++) {
				fromCell = fromRow.getCell((short) j);
				if (fromCell == null) {
					continue;
				}
				newCell = newRow.createCell((short) j);
				newCell.getCellStyle().cloneStyleFrom(fromCell.getCellStyle());
				int cType = fromCell.getCellType();
				newCell.setCellType(cType);
				switch (cType) {
					case XSSFCell.CELL_TYPE_STRING:
						newCell.setCellValue(fromCell.getRichStringCellValue());
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						newCell.setCellValue(fromCell.getNumericCellValue());
						break;
					case XSSFCell.CELL_TYPE_FORMULA:
						newCell.setCellValue(fromCell.getCellFormula());
						break;
					case XSSFCell.CELL_TYPE_BOOLEAN:
						newCell.setCellValue(fromCell.getBooleanCellValue());
						break;
					case XSSFCell.CELL_TYPE_ERROR:
						newCell.setCellValue(fromCell.getErrorCellValue());
						break;
					default:
						newCell.setCellValue(fromCell.getRichStringCellValue());
						break;
				}
			}
		}
	}*/

	public static void copyCellStyle(XSSFCellStyle fromStyle, XSSFCellStyle toStyle) {
		toStyle.cloneStyleFrom(fromStyle);// 此一行代码搞定
	}

	/**
	 * 合并单元格
	 * @param fromSheet
	 * @param toSheet
	 */
	public static void mergeSheetAllRegion(XSSFSheet fromSheet, XSSFSheet toSheet) {
		int num = fromSheet.getNumMergedRegions();
		CellRangeAddress cellR = null;
		for (int i = 0; i < num; i++) {
			cellR = fromSheet.getMergedRegion(i);
			toSheet.addMergedRegion(cellR);
		}
	}
	/**
	 * 复制单元格
	 * @param wb
	 * @param fromCell
	 * @param toCell
	 */
	public static void copyCell(XSSFWorkbook wb, XSSFCell fromCell, XSSFCell toCell) {
		XSSFCellStyle newstyle = wb.createCellStyle();
		copyCellStyle(fromCell.getCellStyle(), newstyle);
		//  toCell.setEncoding(fromCell.getStringCelllValue());
		// 样式
		toCell.setCellStyle(newstyle);
		if (fromCell.getCellComment() != null) {
			toCell.setCellComment(fromCell.getCellComment());
		}
		// 不同数据类型处理
		int fromCellType = fromCell.getCellType();
		toCell.setCellType(fromCellType);
		if (fromCellType == XSSFCell.CELL_TYPE_NUMERIC) {
			if (XSSFDateUtil.isCellDateFormatted(fromCell)) {
				toCell.setCellValue(fromCell.getDateCellValue());
			} else {
				toCell.setCellValue(fromCell.getNumericCellValue());
			}
		} else if (fromCellType == XSSFCell.CELL_TYPE_STRING) {
			toCell.setCellValue(fromCell.getRichStringCellValue());
		} else if (fromCellType == XSSFCell.CELL_TYPE_BLANK) {
			// nothing21
		} else if (fromCellType == XSSFCell.CELL_TYPE_BOOLEAN) {
			toCell.setCellValue(fromCell.getBooleanCellValue());
		} else if (fromCellType == XSSFCell.CELL_TYPE_ERROR) {
			toCell.setCellErrorValue(fromCell.getErrorCellValue());
		} else if (fromCellType == XSSFCell.CELL_TYPE_FORMULA) {
			toCell.setCellFormula(fromCell.getCellFormula());
		} else { // nothing29
		}

	}

	/**
	 * 行复制功能
	 * @param wb
	 * @param oldRow
	 * @param toRow
	 */
	public static void copyRow(XSSFWorkbook wb, XSSFRow oldRow, XSSFRow toRow) {
		toRow.setHeight(oldRow.getHeight());
		for (Iterator cellIt = oldRow.cellIterator(); cellIt.hasNext();) {
			XSSFCell tmpCell = (XSSFCell) cellIt.next();
			XSSFCell newCell = toRow.createCell(tmpCell.getColumnIndex());
			copyCell(wb, tmpCell, newCell);
		}
	}

	/**
	 * Sheet复制
	 * @param wb
	 * @param fromSheet
	 * @param toSheet
	 */
	public static void copySheet(XSSFWorkbook wb, XSSFSheet fromSheet, XSSFSheet toSheet) {
		mergeSheetAllRegion(fromSheet, toSheet);
		// 设置列宽
		int length = fromSheet.getRow(fromSheet.getFirstRowNum()).getLastCellNum();
		for (int i = 0; i <= length; i++) {
			toSheet.setColumnWidth(i, fromSheet.getColumnWidth(i));
		}
		for (Iterator rowIt = fromSheet.rowIterator(); rowIt.hasNext();) {
			XSSFRow oldRow = (XSSFRow) rowIt.next();
			XSSFRow newRow = toSheet.createRow(oldRow.getRowNum());
			copyRow(wb, oldRow, newRow);
		}
	}

	public class XSSFDateUtil extends DateUtil {

	}

	// 合并日期占两行(4个参数，分别为起始行，结束行，起始列，结束列)
	public static void mergeCell (Sheet sheet,int firstRow,int lastRow,int firstCol,int lastCol) {
		CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
		sheet.addMergedRegion(region);
	}

	/**
	 * 文本
	 * @param wb
	 * @return
	 */
	public static CellStyle getTextStype (XSSFWorkbook wb) {
		CellStyle xlsxStyle = wb.createCellStyle();
		xlsxStyle.setBorderLeft(BorderStyle.THIN);
		xlsxStyle.setBorderTop(BorderStyle.THIN);
		xlsxStyle.setBorderRight(BorderStyle.THIN);
		xlsxStyle.setBorderBottom(BorderStyle.THIN);
		xlsxStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		xlsxStyle.setAlignment(HorizontalAlignment.CENTER);
		return xlsxStyle;
	}

	/**
	 * 数值，整数
	 * @param wb
	 * @return
	 */
	public static CellStyle getNumIntegerStype (XSSFWorkbook wb) {
		CellStyle numxlsxStyle = wb.createCellStyle();
		numxlsxStyle.setBorderLeft(BorderStyle.THIN);
		numxlsxStyle.setBorderTop(BorderStyle.THIN);
		numxlsxStyle.setBorderRight(BorderStyle.THIN);
		numxlsxStyle.setBorderBottom(BorderStyle.THIN);
		numxlsxStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		numxlsxStyle.setAlignment(HorizontalAlignment.CENTER);
		XSSFDataFormat format = wb.createDataFormat();
		numxlsxStyle.setDataFormat(format.getFormat("0"));
		return numxlsxStyle;
	}

	/**
	 * 数值，保留两位
	 * @param wb
	 * @return
	 */
	public static CellStyle getNumDoubleStype (XSSFWorkbook wb) {
		CellStyle numxlsxStyle = wb.createCellStyle();
		numxlsxStyle.setBorderLeft(BorderStyle.THIN);
		numxlsxStyle.setBorderTop(BorderStyle.THIN);
		numxlsxStyle.setBorderRight(BorderStyle.THIN);
		numxlsxStyle.setBorderBottom(BorderStyle.THIN);
		numxlsxStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		numxlsxStyle.setAlignment(HorizontalAlignment.CENTER);
		XSSFDataFormat format = wb.createDataFormat();
		numxlsxStyle.setDataFormat(format.getFormat("0.00"));
		return numxlsxStyle;
	}


	public static void CreateCell(Row row,CellStyle xlsxStyle,Integer ColumnNumber,String value)
	{
		Cell cell_begin_city = row.createCell(ColumnNumber);
		if(null!=xlsxStyle)
			cell_begin_city.setCellStyle(xlsxStyle);
		cell_begin_city.setCellValue(value);
	}

	public static void CreateNumCell(Row row,CellStyle xlsxStyle,Integer ColumnNumber,Double value)
	{
		Cell cell_begin_city = row.createCell(ColumnNumber);
		if(null!=xlsxStyle)
			cell_begin_city.setCellStyle(xlsxStyle);
		cell_begin_city.setCellValue(value);
	}
}
