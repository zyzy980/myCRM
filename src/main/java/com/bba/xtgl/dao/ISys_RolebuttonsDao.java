package com.bba.xtgl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.bba.xtgl.vo.RegisterVO;
import com.bba.xtgl.vo.Sys_RolebuttonsVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISys_RolebuttonsDao extends BaseMapper {


    /**
     * 查询所有该用户所有的模块权限
     * @param userid 用户userid
     * @return
     */
    public List<Sys_RolebuttonsVO> findListByUser(String userid);

}
