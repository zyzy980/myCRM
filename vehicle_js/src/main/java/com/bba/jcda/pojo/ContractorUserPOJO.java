package com.bba.jcda.pojo;

import com.bba.jcda.vo.contractor.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Auther: Administrator
 * @Date: 2018/12/7 19:16
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ContractorUserPOJO extends BaseVO {
    private String id;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 手机号码
     */
    private String mobileNo;
    /**
     * 性别
     */
    private String sex;
    /**
     * 身份证
     */
    private String idCard;
    /**
    * 是否承运商接收人
    */
    private String isRecipient;
    /**
     * 状态 0: 无效 1: 有效
     */
    private String status;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ContractorUserPOJO{");
        sb.append("id='").append(id).append('\'');
        sb.append(", realName='").append(realName).append('\'');
        sb.append(", mobileNo='").append(mobileNo).append('\'');
        sb.append(", sex='").append(sex).append('\'');
        sb.append(", idCard='").append(idCard).append('\'');
        sb.append(", isRecipient='").append(isRecipient).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
