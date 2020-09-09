package com.bba.cpgl.dto;

import com.bba.cpgl.vo.OpenRecordVO;
import lombok.Data;

import java.util.List;

/**
 * @progrem: com.bba.cpgl.dto
 * @author: ZhouSuwen'sdadi
 * @date: 2020-09-01 14:37
 **/
@Data
public class BjKanbanDTO {

    private List<OpenRecordVO> goodsTypeList;
    private List<OpenRecordVO> valueList;
    private List<OpenRecordVO> sellModeList;
    private List<OpenRecordVO> salesTrendList;
    private List<OpenRecordVO> salesRankingList;
    private List<OpenRecordVO> rollList;
}
