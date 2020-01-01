package com.bba.jcda.vo;

import com.bba.jcda.vo.contractor.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Auther: laoli
 * @Date: 2018/12/7 11:41
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ZdContractorVO extends BaseVO {
    private String sn;
    private String withinCode;
    private String code; //承运商标号
    private String shortName; //承运商简称
    private String name; //承运商名称
    private String linkman; //联系人
    private String linkMobile; //联系电话
    private String address; //地址
    private String remark; //说明
    private String createBy;//创建人
    private String createDate; //创建时间
    private String updateBy; //更新人
    private String updateDate;//更新时间
    private String State; //状态 0正常 1失效
    private String location_code;
}