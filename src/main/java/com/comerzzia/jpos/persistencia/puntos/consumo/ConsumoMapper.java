package com.comerzzia.jpos.persistencia.puntos.consumo;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ConsumoMapper {
    int countByExample(ConsumoExample example);

    int deleteByExample(ConsumoExample example);

    int deleteByPrimaryKey(String uidTicket);

    int insert(ConsumoBean record);

    int insertSelective(ConsumoBean record);

    List<ConsumoBean> selectByExampleWithRowbounds(ConsumoExample example, RowBounds rowBounds);

    List<ConsumoBean> selectByExample(ConsumoExample example);

    ConsumoBean selectByPrimaryKey(String uidTicket);

    int updateByExampleSelective(@Param("record") ConsumoBean record, @Param("example") ConsumoExample example);

    int updateByExample(@Param("record") ConsumoBean record, @Param("example") ConsumoExample example);

    int updateByPrimaryKeySelective(ConsumoBean record);

    int updateByPrimaryKey(ConsumoBean record);
    
    Integer selectSumByCodCliente(@Param("codCliente") String codCliente);
    
}