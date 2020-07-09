package com.bba.settlement.vo;

import lombok.Data;
import java.util.List;


@Data
public class Js_Down_ExportVO {
    private List<Js_Vin_Down_AmountVO> masterList;
    private List<Js_Vin_Down_AmountVO> detailList;
}
