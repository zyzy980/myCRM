package com.bba.jcda.vo;

import com.baomidou.mybatisplus.annotations.TableName;
import com.bba.common.interceptor.EntityField;
import lombok.Data;
import java.util.List;
@TableName("CR_CUSTOMER")
@Data
public class CrCustomerVO {
    @EntityField(value = "ID")
    private String id;
    @EntityField(value = "类型")
    private String type;
    @EntityField(value = "公司编号")
    private String code;
    @EntityField(value = "公司")
    private String name;
    @EntityField(value = "税号")
    private String tax;
    @EntityField(value = "发票地址")
    private String in_address;
    @EntityField(value = "开户银行")
    private String bank;
    @EntityField(value = "开户账号")
    private String bank_no;
    @EntityField(value = "发票电话")
    private String in_tel;
    @EntityField(value = "状态")
    private String status;
    @EntityField(value = "省")
    private String pri;
    @EntityField(value = "市")
    private String city;
    @EntityField(value = "地址")
    private String address;
    @EntityField(value = "备注")
    private String remark;
    @EntityField(value = "创建")
    private String create_by;
    @EntityField(value = "创建时间")
    private String create_date;

    private List<Cr_Customer_LinkVO> crCusLinks;

}
