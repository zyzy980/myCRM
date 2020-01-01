package com.bba.bbgl.dto;

import com.bba.bbgl.vo.Js_Analysis_SummaryVO;
import com.bba.bbgl.vo.Js_Analysis_Summary_DetailVO;
import lombok.Data;

import java.util.List;

/**
 * @Author:Suwendaidi
 * @Date: 2019/10/30 17:56
 */
@Data
public class Js_Analysis_SummaryDTO {

    private Js_Analysis_SummaryVO js_Analysis_SummaryVO;

    private List<Js_Analysis_Summary_DetailVO> js_Analysis_Summary_DetailVOList;
}
