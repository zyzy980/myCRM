package com.bba.jcda.dto;

import com.baomidou.mybatisplus.annotations.TableId;
import com.bba.ht.vo.Ht_Carrier_FreightVO;
import com.bba.ht.vo.Ht_Cus_FreightVO;
import lombok.Data;
import java.util.List;

/**
 * @Author:Suwendaidi
 * @Date: 2019/10/18 9:31
 */
@Data
public class Ht_FreightDTO {
    @TableId
    private List<Ht_Carrier_FreightVO> Ht_Carrier_Freight_List;

    @TableId
    private List<Ht_Cus_FreightVO> Ht_Cus_Freight_List;
}
