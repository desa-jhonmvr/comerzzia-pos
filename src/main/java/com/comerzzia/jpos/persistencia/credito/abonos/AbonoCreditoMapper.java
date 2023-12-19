package com.comerzzia.jpos.persistencia.credito.abonos;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface AbonoCreditoMapper {
    int countByExample(AbonoCreditoExample example);

    int deleteByExample(AbonoCreditoExample example);

    int deleteByPrimaryKey(String uidCreditoPago);

    int insert(AbonoCreditoBean record);

    int insertSelective(AbonoCreditoBean record);

    List<AbonoCreditoBean> selectByExampleWithBLOBsWithRowbounds(AbonoCreditoExample example, RowBounds rowBounds);

    List<AbonoCreditoBean> selectByExampleWithBLOBs(AbonoCreditoExample example);

    List<AbonoCreditoBean> selectByExampleWithRowbounds(AbonoCreditoExample example, RowBounds rowBounds);

    List<AbonoCreditoBean> selectByExample(AbonoCreditoExample example);

    AbonoCreditoBean selectByPrimaryKey(String uidCreditoPago);

    int updateByExampleSelective(@Param("record") AbonoCreditoBean record, @Param("example") AbonoCreditoExample example);

    int updateByExampleWithBLOBs(@Param("record") AbonoCreditoBean record, @Param("example") AbonoCreditoExample example);

    int updateByExample(@Param("record") AbonoCreditoBean record, @Param("example") AbonoCreditoExample example);

    int updateByPrimaryKeySelective(AbonoCreditoBean record);

    int updateByPrimaryKeyWithBLOBs(AbonoCreditoBean record);

    int updateByPrimaryKey(AbonoCreditoBean record);
}