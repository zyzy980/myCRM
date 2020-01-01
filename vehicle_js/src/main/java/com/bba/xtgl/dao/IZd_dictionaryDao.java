package com.bba.xtgl.dao;

import com.bba.xtgl.vo.RegisterVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author bcmaidou
 * @Date 2019/5/14 13:43
 */
@Repository
public interface IZd_dictionaryDao {

    void copyDic(@Param("item") RegisterVO vo,@Param("sn")String sn);

    void copyDicData(RegisterVO vo);

    void copyParent(RegisterVO vo);

    String parentSn(RegisterVO vo);

//    List<Map> seldemo(RegisterVO vo);

}
