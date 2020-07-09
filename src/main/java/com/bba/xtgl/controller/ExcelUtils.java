package com.bba.xtgl.controller;

import com.bba.util.JSONUtils;
import com.bba.util.POIUtils;
import com.bba.util.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * @ClassName ExcelUtils
 * @Discription TODO
 * @Author lao li
 * @Date 2019/3/14 0014 10:14
 * @Version 1.0
 */
public class ExcelUtils {

    public static void main(String[] args) {


//        List<List<Object>> listobj = new ArrayList<List<Object>>();//集合
//        Workbook wb = null;
//        try (
//                InputStream fis = new FileInputStream("C:\\Users\\Administrator.PC-201902282131\\Desktop\\整车导入.xlsx");) {
//            wb = WorkbookFactory.create(fis);
//        } catch (Exception e) {
//            throw new RuntimeException("导入文件有误!");
//        }
//        Sheet sheet = wb.getSheetAt(0);
//        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
//            List<Object> obj = new ArrayList<Object>();    //实体
//            Row row = sheet.getRow(i);
//            if (row == null) {
//                // throw new RuntimeException("表格不能存在空行!");
//                continue;
//            }
//            for (int j = 0; j < sheet.getRow(0).getLastCellNum(); j++) {
//                Cell cell = row.getCell(j);
//                obj.add(POIUtils.getCellValue(cell));
//            }
//            if (StringUtils.isBlank(StringUtils.join(obj, ""))) {
//               continue;
//            }
//            listobj.add(obj);
//        }
//
//        List<Map<String, Object>> countList = new ArrayList<Map<String,Object>>(); // 所有的对象放到这个list中
//        Map<String,Object> vehicleVO = null; // 存放车次的信息
//        Map<String,Object> mostlyVO = null;
//        Map<String,Object> detailVO = null;
//
//        List<Map<String,Object>> mostlyVOList = null; //  定义运单集合
//        List<Map<String,Object>> detailVOList = null; // 定义运单明细集合
//
//        System.out.println(JSONUtils.toJSONString(listobj));
//
//        for(int i = 2; i < listobj.size(); i++){
//            List<Object> detailList = listobj.get(i);
//
//            // 如果第一行不为空，就是车次的信息
//            if(!"".equals(detailList.get(0))) {
//                if(vehicleVO != null) {
//                    countList.add(vehicleVO);
//                }
//                mostlyVOList = new ArrayList<Map<String,Object>>(); // 每次车次信息部分不为空的时候，就重置当前list
//                vehicleVO = new HashMap<String,Object>();
//                vehicleVO.put("vehicleCusNo", detailList.get(0)); // 取出整车的客户编号
//                vehicleVO.put("vehiclePlanBeginDate", detailList.get(1)); // 取出整车的计划起运时间
//                vehicleVO.put("vehicleTransportType", detailList.get(2)); // 整车的运输方式
//                vehicleVO.put("vehicleBeginAddressCode", detailList.get(3)); //整车发货地编号
//                vehicleVO.put("vehicleYwLocationCode", detailList.get(4)); // 整车业务地点编号
//                vehicleVO.put("vehicleEndAddressCode", detailList.get(5)); // 整车收货地编号
//            }
//
//            if(!"".equals(detailList.get(6))) {
//                detailVOList = new ArrayList<Map<String,Object>>(); // 每次运单信息不为空的时候，就重置运单明细list
//                mostlyVO = new HashMap<String,Object> ();
//                mostlyVO.put("zeroLoadCusNo", detailList.get(6)); // 零担客户编号
//                mostlyVO.put("zeroLoadWeiTuoDate", detailList.get(7)); // 零担客户委托时间
//                mostlyVO.put("zeroBusinessYwId", detailList.get(8)); //  零担客户业务号
//                mostlyVO.put("zeroBeginDate", detailList.get(9)); // 零担发货日期
//                mostlyVO.put("zeroBeginAddressCode", detailList.get(10)); // 零担发货地编号
//                mostlyVO.put("zeroEndAddressCode", detailList.get(11)); // 零担收货地编号
//                mostlyVO.put("zeroYwLocationCode", detailList.get(12)); // 零担业务地点编号
//                mostlyVO.put("zeroPlanEndDate", detailList.get(13)); // 零担计划收货时间
//                mostlyVOList.add(mostlyVO);
//            }
//
//            if(!"".equals(detailList.get(14))) {
//                detailVO = new HashMap<String,Object>();
//                detailVO.put("goodsName", detailList.get(14)); // 货物名称
//                detailVO.put("unit", detailList.get(15)); // 单位
//                detailVO.put("vol", detailList.get(16)); // 体积
//                detailVO.put("gwt", detailList.get(17)); // 重量
//                detailVO.put("pcs", detailList.get(18)); // 件数
//                detailVOList.add(detailVO);
//
//                mostlyVO.put("detailVO", detailVOList);
//                vehicleVO.put("mostlyVO", mostlyVOList);
//            }
//
//        }
//        countList.add(vehicleVO);
    }
}
