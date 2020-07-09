package com.bba.xtgl.vo;

import com.bba.jcda.vo.contractor.BaseVO;
import lombok.Data;

/**
 * @ClassName SysWithinCodeVO
 * @Discription TODO
 * @Author lao li
 * @Date 2019-03-25 11:04
 * @Version 1.0
 */
@Data
public class SysWithinCodeVO extends BaseVO {
    private String code;
    private String name;
    private String nameEng;
    private String province;
    private String city;
    private String address;
    private String web;
    private String linkMan;
    private String linkEmail;
    private String state;
    private String weiXin;
    private String linkMobile;
    private String linkQQ;
    private String createDate;
    private String createBy;
    private String updateDate;
    private String updateBy;
    private String logoPath;
    private String remark;
    private Long sn;
    private String birthday;
    private String guiMo;
    private String shortName;
}
