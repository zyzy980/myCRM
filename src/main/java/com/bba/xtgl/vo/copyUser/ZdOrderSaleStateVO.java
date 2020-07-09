package com.bba.xtgl.vo.copyUser;

import com.bba.jcda.vo.contractor.BaseLocationVO;
import com.bba.jcda.vo.contractor.BaseVO;
import lombok.Data;

/**
 * @ClassName ZdOrderSaleStateVO
 * @Discription TODO
 * @Author lao li
 * @Date 2019-04-02 18:44
 * @Version 1.0
 */
@Data
public class ZdOrderSaleStateVO extends BaseVO {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String sn;
    private String withinCode;
    private String orderState;
    private String saleState;
    private String routeType;
    private String createBy;
    private String createDate;
    private String updateBy;
    private String updateDate;
}
