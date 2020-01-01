package com.bba.dz.vo;

import lombok.Data;

import java.util.List;


@Data
public class Js_Dz_SheetExportVO {
    private List<Js_Dz_Sheet_DetailAccountVO> masterList;
    private List<Js_Dz_Sheet_DetailAccountVO> detailList;
}
