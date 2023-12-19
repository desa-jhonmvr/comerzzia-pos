package com.comerzzia.jpos.persistencia.puntos.acumulacion;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface AcumulacionMapper {
    int countByExample(AcumulacionExample example);

    int deleteByExample(AcumulacionExample example);

    int deleteByPrimaryKey(String uidTicket);

    int insert(AcumulacionBean record);

    int insertSelective(AcumulacionBean record);

    List<AcumulacionBean> selectByExampleWithRowbounds(AcumulacionExample example, RowBounds rowBounds);

    List<AcumulacionBean> selectByExample(AcumulacionExample example);

    AcumulacionBean selectByPrimaryKey(String uidTicket);

    int updateByExampleSelective(@Param("record") AcumulacionBean record, @Param("example") AcumulacionExample example);

    int updateByExample(@Param("record") AcumulacionBean record, @Param("example") AcumulacionExample example);

    int updateByPrimaryKeySelective(AcumulacionBean record);

    int updateByPrimaryKey(AcumulacionBean record);
    
    Integer selectSumByCodCliente(@Param("codCliente") String codCliente);
}