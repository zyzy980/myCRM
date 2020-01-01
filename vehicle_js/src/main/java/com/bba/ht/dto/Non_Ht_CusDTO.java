package com.bba.ht.dto;

import com.baomidou.mybatisplus.annotations.TableId;
import com.bba.ht.vo.Non_Ht_CusVO;
import com.bba.ht.vo.Non_Ht_Cus_FreightVO;
import lombok.Data;

import java.util.List;

@Data
public class Non_Ht_CusDTO {

    @TableId
    private Non_Ht_CusVO non_ht_cusVO;

    @TableId
    private List<Non_Ht_Cus_FreightVO> non_ht_cus_freightVOList;

}
