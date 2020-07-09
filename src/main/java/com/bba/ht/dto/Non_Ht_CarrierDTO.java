package com.bba.ht.dto;

import com.baomidou.mybatisplus.annotations.TableId;
import com.bba.ht.vo.Non_Ht_CarrierVO;
import com.bba.ht.vo.Non_Ht_Carrier_FreightVO;
import lombok.Data;

import java.util.List;

@Data
public class Non_Ht_CarrierDTO {

    @TableId
    private Non_Ht_CarrierVO non_ht_carrierVO;

    @TableId
    private List<Non_Ht_Carrier_FreightVO> non_ht_carrier_freightVOList;

}
