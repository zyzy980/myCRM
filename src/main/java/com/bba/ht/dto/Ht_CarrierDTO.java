package com.bba.ht.dto;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bba.ht.vo.Ht_CarrierVO;
import com.bba.ht.vo.Ht_Carrier_FreightVO;
import lombok.Data;

import java.util.List;

@Data
public class Ht_CarrierDTO {

    @TableId
    private Ht_CarrierVO ht_carrierVO;

    @TableId
    private List<Ht_Carrier_FreightVO> ht_carrier_freightVOList;

}
