package com.bba.jsyw.dto;

import com.baomidou.mybatisplus.annotations.TableId;
import com.bba.jsyw.vo.Tr_BusinessVO;
import com.bba.jsyw.vo.Tr_Business_CarrierVO;
import lombok.Data;

import java.util.List;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/13 10:19
 */
@Data
public class Tr_BusinessDTO {

    private Tr_BusinessVO tr_businessVO;

    private List<Tr_Business_CarrierVO> tr_Business_CarrierVOList;

}
