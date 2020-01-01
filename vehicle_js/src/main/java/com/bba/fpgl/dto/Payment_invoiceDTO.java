package com.bba.fpgl.dto;

import com.baomidou.mybatisplus.annotations.TableId;
import com.bba.fpgl.vo.Payment_invoiceVO;
import com.bba.fpgl.vo.Payment_invoice_DetailVO;
import lombok.Data;

import java.util.List;

@Data
public class Payment_invoiceDTO {

    @TableId
    private Payment_invoiceVO payment_invoiceVO;

    @TableId
    private List<Payment_invoice_DetailVO> payment_invoice_detailVOList;

}
