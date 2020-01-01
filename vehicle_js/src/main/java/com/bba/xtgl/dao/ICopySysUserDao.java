package com.bba.xtgl.dao;

import com.bba.xtgl.vo.copyUser.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName ICopySysUserDao
 * @Description TODO
 * @Author lao li
 * @Date 2019/2/20 10:08
 * @Version 1.0
 */
public interface ICopySysUserDao {

    /**
     * 功能描述:  快速创建用户复制-----查出sys_module表
     * @param withinCode 内码，值为TMS，只复制TMS的数据，以下的方法就不对withinCode作解释
     * @return:
     * @auther: laoli
     * @date: 2019/2/20 10:55
     */
    List<SysModuleVO> querySysModule(String withinCode);

    /**
     * 功能描述:  快速创建用户复制------批量新增sys_module表
     * @param sysModuleVOS 查出来的module数据
     * @param withinCode 内码
     * @return:
     * @auther: laoli
     * @date: 2019/2/20 10:56
     */
    void insertSysModule(@Param(value="list")List<SysModuleVO> sysModuleVOS, @Param(value="withinCode")String withinCode);

    void insertSysModuleTwo(SysModuleVO sysModuleVO);

    /**
     * 功能描述:  快速创建用户复制-------新增sys_user表
     * @return:
     * @auther: laoli
     * @date: 2019/2/20 15:01
     */
    void insertSysUser(SysUsersVO sysUsersVO);

    /**
     * 功能描述: 快速创建用户复制-------新增zd_location表
     * @return:
     * @auther: laoli
     * @date: 2019/2/20 17:14
     */
    void insertZdLocation(List<ZdYwLocationVO> ywLocationVOS);

    /**
     * 功能描述: 快速创建用户复制-------查询zd_contractor表
     * @return:
     * @auther: laoli
     * @date: 2019/2/20 17:14
     */
    List<ZdContractorVO> queryZdContractor(String withinCode);

    /**
     * 功能描述:   快速创建用户复制-------新增zd_contractor表
     * @return:
     * @auther: laoli
     * @date: 2019/2/21 11:13
     */
    void insertZdContractor(ZdContractorVO contractorVO);

    /**
     * 功能描述: 快速创建用户复制-------查询zd_cus表
     * @return:
     * @auther: laoli
     * @date: 2019/2/21 11:59
     */
    List<ZdCusVO> queryZdCus(String withinCode);

    /**
     * 功能描述: 快速创建用户复制-------复制zd_cus表
     * @return:
     * @auther: laoli
     * @date: 2019/2/21 13:28
     */
    void insertZdCus(ZdCusVO cusVO);

    /**
     * 功能描述:  快速创建用户复制-------查询zd_fare表
     * @return:
     * @auther: laoli
     * @date: 2019/2/21 14:41
     */
    List<ZdFareVO> queryZdFare(String withinCode);

    /**
     * 功能描述:  快速创建用户复制-------复制zd_fare表
     * @return:
     * @auther: laoli
     * @date: 2019/2/21 14:42
     */
    void insertZdFare(@Param(value="list")List<ZdFareVO> list, @Param(value="withinCode")String withinCode);

    /**
     * 功能描述: 快速创建用户复制-------查询zd_currency表
     * @return:
     * @auther: laoli
     * @date: 2019/2/21 15:08
     */
    List<ZdCurrencyVO> queryZdCurrency(String withinCode);

    /**
     * 功能描述: 快速创建用户复制-------复制zd_currency表
     * @return:
     * @auther: laoli
     * @date: 2019/2/21 15:08
     */
    void insertZdCurrency(@Param(value = "list")List<ZdCurrencyVO> list, @Param(value = "withinCode")String withinCode);

    /**
     * 功能描述: 快速创建用户复制-------查询sys_rolebuttons表
     * @return:
     * @auther: lao li
     * @date: 2019/2/22 16:59
     */
    List<SysRoleButtonsVO> querySysButtons(String withinCode);

    /**
     * 功能描述: 快速创建用户复制-------创建sys_rolebuttons表
     * @return:
     * @auther: lao li
     * @date: 2019/2/22 16:59
     */
    void insertSysButtons(SysRoleButtonsVO buttonsVO);

    /**
     * 功能描述:  快速创建用户复制-----复制zd_ywlocation_base 表
     * @return:
     * @auther:  lao li
     * @date: 2019/2/23 11:55
     */
    void insertZdYwLocationBase(ZdYwLocationBaseVO baseVO);

    /**
     * 功能描述:  快速创建用户复制------- 复制 zd_trucktype 表
     * @return:
     * @auther: lao li
     * @date: 2019/2/23 14:10
     */
    List<ZdTruckTypeVO> queryZdTruckType(String withinCode);

    /**
     * @return:
     * @auther: lao li
     * @date: 2019/2/23 14:11
     */
    void insertZdTruckType(ZdTruckTypeVO truckType);

    /**
     * 功能描述: 快速创建用户复制----- 查询 zd_truck_relate  表
     * @return:
     * @auther: laoli
     * @date: 2019/2/23 15:59
     */
    List<ZdTruckRelateVO> queryZdTruckRelate(String withinCode);

    /**
     * 功能描述: 快速创建用户复制----- 复制 zd_truck_relate  表
     * @return:
     * @auther: laoli
     * @date: 2019/2/23 15:59
     */
    void insertZdTruckRelate(ZdTruckRelateVO relateVO);

    /**
     * 功能描述: 快速创建用户复制----- 查询 zd_driver_relate  表
     * @return:
     * @auther: laoli
     * @date: 2019/2/23 16:01
     */
    List<ZdDriverRelateVO> queryZdDriverRelate(String withinCode);
    
    /**
     * 功能描述: 快速创建用户复制----- 复制 zd_driver_relate  表
     * @param:
     * @return: 
     * @auther: laoli
     * @date: 2019/2/23 16:26
     */
    void insertDriverRelate(ZdDriverRelateVO relateVO);

    /**
     * 功能描述: 快速创建用户复制----- 查询 sys_role  表
     * @return:
     * @auther: laoli
     * @date: 2019/2/23 17:13
     */
    List<SysRoleVO> querySysRole(SysRoleVO sysRoleVO);

    /**
     * 功能描述: 快速创建用户复制----- 复制 sys_role  表
     * @return:
     * @auther: laoli
     * @date: 2019/2/23 17:13
     */
    void insertSysRole(@Param(value = "list")List<SysRoleVO> roleVO, @Param(value = "withinCode")String withinCode);

    /**
     * 功能描述: 快速创建用户复制----- 查询 zd_errorkind  表
     * @return:
     * @auther: laoli
     * @date: 2019/2/23 17:13
     */
    List<ZdErrorkindVO> queryZdErrorKind(String withinCode);

    /**
     * 功能描述: 快速创建用户复制----- 复制 zd_errorkind  表
     * @return:
     * @auther: laoli
     * @date: 2019/2/23 17:13
     */
    void insertZdErrorKind(@Param(value = "list")List<ZdErrorkindVO> errorKind, @Param(value = "withinCode")String withinCode);

    List<SysSheetIdVO> querySysSheetId(String withinCode);

    void insertSysSheetId(@Param("list") List<SysSheetIdVO> list,@Param("withinCode")String withinCode,@Param("ywLocation")String ywLocation,@Param("tableName")String tableName,@Param("funcationName")String functionName);

    List<ZdOrderSaleStateVO> queryZdOrderSaleState(String withinCode);
    void insertZdOrderSaleState(@Param("list")List<ZdOrderSaleStateVO> list, @Param("withinCode")String withinCode);
    List<ZdBusinessStateVO> queryZdBusinessState(String withinCode);
    void insertZdBusinessState(@Param("list")List<ZdBusinessStateVO> list, @Param("withinCode")String withinCode);

    List<ZdDictionaryVO> queryZdDictionary(String withinCode);
    void insertZdDictionary(ZdDictionaryVO VO);

    List<ZdDictionaryDataVO> queryZdDictionaryData(String withinCode);
    void insertZdDictionaryData(@Param("list")List<ZdDictionaryDataVO> dataVO, @Param("withinCode")String withinCode);

    void insertSysWithinSet(String withinCode);

}
