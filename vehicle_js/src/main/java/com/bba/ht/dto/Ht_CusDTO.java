package com.bba.ht.dto;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bba.ht.vo.Ht_CusVO;
import com.bba.ht.vo.Ht_Cus_FreightVO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Ht_CusDTO {

    @TableId
    private Ht_CusVO ht_cusVO;

    @TableId
    private List<Ht_Cus_FreightVO> ht_cus_freightVOList;

}
