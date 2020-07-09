package com.bba.xtgl.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName RegisterVO
 * @Description TODO
 * @Author lao li
 * @Date 2019/2/13 16:54
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RegisterVO {

    private String userId;
    private String userName; // 用户登录名
    private String password; // 用户密码
    private String withinCode; // 用户内码
    private String withinCodeName; // 客户名称
    private String ywLocationOne; // 业务地点1
    private String ywLocationOneCode; // 业务地点代码
    private String ywLocationTwo; // 业务地点2
    private String ywLocationTwoCode; // 业务地点代码
    private String ywLocationThree; // 业务地点3
    private String ywLocationThreeCode; // 业务地点代码
    private String mobile; // 客户电话号码

    private String ywdata; //是否需要业务数据 0=不写业务数据表，=1写业务数据表
    private String within_code_src; //复制数据来源
}
