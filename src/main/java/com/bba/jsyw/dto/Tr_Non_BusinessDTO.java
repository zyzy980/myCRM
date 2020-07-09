package com.bba.jsyw.dto;

import com.bba.jsyw.vo.Tr_Non_BusinessVO;
import com.bba.jsyw.vo.Tr_Non_Business_CarrierVO;
import lombok.Data;

import java.util.List;

/**
 * @Author:Suwendaidi
 * @Date: 2019/7/13 10:19
 */
@Data
public class Tr_Non_BusinessDTO {

    private Tr_Non_BusinessVO tr_non_businessVO;

    private List<Tr_Non_Business_CarrierVO> tr_non_Business_CarrierVOList;

}
