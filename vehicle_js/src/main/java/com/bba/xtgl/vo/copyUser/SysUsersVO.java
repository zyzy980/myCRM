package com.bba.xtgl.vo.copyUser;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName SysModuleVO
 * @Description TODO
 * @Author lao li
 * @Date 2019/2/20 10:26
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUsersVO {
    private Long id;
    private String withinCode;
    private String code;
    private String userId;
    private String realName;
    private String password;
    private String deptId;
    private String position;
    private String status;
    private String mobileNo;
    private String sex;
    private String idCard;
    private String tel;
    private String mail;
    private String fax;
    private String isCus;
    private String isContractor;
    private String isThDriver;
    private String isPsDriver;
    private String isOP;
    private String cusCode;
    private String contractorCode;
    private String userLoginIP;
    private String userLoginTime;
    private String address;
    private String birthday;
    private String headImg;
    private String userLoginBrowser;
    private String userLoginPlatform;
    private String validating;
    private String pushFlag;
    private String pushBaiDuKey;
    private String baiDuUserId;
    private String province;
    private String companyId;
    private String city;
    private String remark;
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;
    private String openId;
    private String wxOpenId;
    private String key;
    private String weChat;
    private String sensitiveAuthority;
    private String weChar;
    private String partner;
    private String isRecipient;
    private String validDate;

}
