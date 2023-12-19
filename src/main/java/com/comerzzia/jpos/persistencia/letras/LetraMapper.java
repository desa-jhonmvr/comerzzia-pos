package com.comerzzia.jpos.persistencia.letras;


import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface LetraMapper {
    int countByExample(LetraExample example);

    int deleteByExample(LetraExample example);

    int deleteByPrimaryKey(String uidLetra);

    int insert(LetraBean record);

    int insertSelective(LetraBean record);

    List<LetraBean> selectByExampleWithRowbounds(LetraExample example, RowBounds rowBounds);

    List<LetraBean> selectByExample(LetraExample example);

    LetraBean selectByPrimaryKey(String uidLetra);

    int updateByExampleSelective(@Param("record") LetraBean record, @Param("example") LetraExample example);

    int updateByExample(@Param("record") LetraBean record, @Param("example") LetraExample example);

    int updateByPrimaryKeySelective(LetraBean record);

    int updateByPrimaryKey(LetraBean record);

    LetraBean selectFromViewByPrimaryKey(String uidLetra);

    List<LetraBean> selectFromViewByExampleWithRowbounds(LetraExample example, RowBounds rowBounds);

    List<LetraBean> selectFromViewByExample(LetraExample example);
}