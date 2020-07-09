package com.bba.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Font;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;

/**
 * @ClassName ExcelUtil
 * @Discription TODO
 * @Author lao li
 * @Date 2019-05-11 20:01
 * @Version 1.0
 */
public class ExcelUtil {

    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param values 内容
     * @param wb HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName,String []title,String [][]values, HSSFWorkbook wb){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for(int i=0;i<values.length;i++){
            row = sheet.createRow(i + 1);
            for(int j=0;j<values[i].length;j++){
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return wb;
    }

    //发送响应流方法
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
       * 
       * @param response
       *   请求
       * @param fileName
       *   文件名 如："学生表"
       * @param excelHeader
       *   excel表头数组，存放"姓名#name"格式字符串，"姓名"为excel标题行， "name"为对象字段名
       * @param dataList
       *   数据集合，需与表头数组中的字段名一致，并且符合javabean规范
       * @return 返回一个HSSFWorkbook
       * @throws Exception
   */
    public static <T> HSSFWorkbook export(HttpServletResponse response, String fileName, String[] excelHeader,
        Collection<T> dataList) throws Exception {
        // 设置请求
        response.setContentType("application/application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
        // 创建一个Workbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 设置标题样式
        HSSFCellStyle titleStyle = wb.createCellStyle();
        // 设置单元格边框样式
        titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框 细边线
        titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);// 下边框 细边线
        titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框 细边线
        titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框 细边线
        // 设置单元格对齐方式
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
        // 设置字体样式
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 15); // 字体高度
        titleFont.setFontName("黑体"); // 字体样式
        titleStyle.setFont(titleFont);
        // 在Workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(fileName);
        // 标题数组
        String[] titleArray = new String[excelHeader.length];
        // 字段名数组
        String[] fieldArray = new String[excelHeader.length];
        for (int i = 0; i < excelHeader.length; i++) {
            String[] tempArray = excelHeader[i].split("#");// 临时数组 分割#
            titleArray[i] = tempArray[0];
            fieldArray[i] = tempArray[1];
        }
        // 在sheet中添加标题行
        HSSFRow row = sheet.createRow((int) 0);// 行数从0开始
        HSSFCell sequenceCell = row.createCell(0);// cell列 从0开始 第一列添加序号
        sequenceCell.setCellValue("序号");
        sequenceCell.setCellStyle(titleStyle);
        sheet.autoSizeColumn(0);// 自动设置宽度
        // 为标题行赋值
        for (int i = 0; i < titleArray.length; i++) {
            HSSFCell titleCell = row.createCell(i + 1);// 0号位被序号占用，所以需+1
            titleCell.setCellValue(titleArray[i]);
            titleCell.setCellStyle(titleStyle);
            sheet.autoSizeColumn(i + 1);// 0号位被序号占用，所以需+1
        }
        // 数据样式 因为标题和数据样式不同 需要分开设置 不然会覆盖
        HSSFCellStyle dataStyle = wb.createCellStyle();
        // 设置数据边框
        dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置居中样式
        dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平居中
        dataStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直居中
        // 设置数据字体
        Font dataFont = wb.createFont();
        dataFont.setFontHeightInPoints((short) 12); // 字体高度
        dataFont.setFontName("宋体"); // 字体
        dataStyle.setFont(dataFont);
        // 遍历集合数据，产生数据行
        Iterator<T> it = dataList.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;// 0号位被占用 所以+1
            row = sheet.createRow(index);
            // 为序号赋值
            HSSFCell sequenceCellValue = row.createCell(0);// 序号值永远是第0列
            sequenceCellValue.setCellValue(index);
            sequenceCellValue.setCellStyle(dataStyle);
            sheet.autoSizeColumn(0);
            T t = (T) it.next();
        // 利用反射，根据传过来的字段名数组，动态调用对应的getXxx()方法得到属性值
            for (int i = 0; i < fieldArray.length; i++) {
                HSSFCell dataCell = row.createCell(i + 1);
                dataCell.setCellStyle(dataStyle);
                sheet.autoSizeColumn(i + 1);
                String fieldName = fieldArray[i];
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);// 取得对应getXxx()方法
                Class<? extends Object> tCls = t.getClass();// 泛型为Object以及所有Object的子类
                Method getMethod = tCls.getMethod(getMethodName, new Class[] {});// 通过方法名得到对应的方法
                Object value = getMethod.invoke(t, new Object[] {});// 动态调用方,得到属性值
                if (value != null) {
                    dataCell.setCellValue(value.toString());// 为当前列赋值
                }
            }
        }

        OutputStream outputStream = response.getOutputStream();// 打开流
        wb.write(outputStream);// HSSFWorkbook写入流
//        wb.close();// HSSFWorkbook关闭
        outputStream.flush();// 刷新流
        outputStream.close();// 关闭流
        return wb;
    }
}
