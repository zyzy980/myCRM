package com.bba.basics.vo;

import com.bba.jcda.vo.contractor.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName SysNoticeUserVO
 * @Discription TODO
 * @Author lao li
 * @Date 2019-06-15 15:21
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysNoticeUserVO extends BaseVO {

    private String id;
    private String name;

    private String sn;
    private String withinCode;
    private String ywLocation;
    private String noticeGroup;
    private String noticeUser;
    private String remark;
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SysNoticeUserVO{");
        sb.append("id='").append(id).append('\'');
        sb.append("name='").append(name).append('\'');
        sb.append("sn='").append(sn).append('\'');
        sb.append(", withinCode='").append(withinCode).append('\'');
        sb.append(", ywLocation='").append(ywLocation).append('\'');
        sb.append(", noticeGroup='").append(noticeGroup).append('\'');
        sb.append(", noticeUser='").append(noticeUser).append('\'');
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", createBy='").append(createBy).append('\'');
        sb.append(", createDate='").append(createDate).append('\'');
        sb.append(", updateBy='").append(updateBy).append('\'');
        sb.append(", updateDate='").append(updateDate).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
