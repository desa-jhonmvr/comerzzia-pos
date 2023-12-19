package com.comerzzia.jpos.persistencia.promociones.clientes;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface PromocionClienteMapper {
    int countByExample(PromocionClienteExample example);

    int deleteByExample(PromocionClienteExample example);

    int deleteByPrimaryKey(PromocionClienteKey key);

    int insert(PromocionClienteBean record);

    int insertSelective(PromocionClienteBean record);

    List<PromocionClienteBean> selectByExampleWithRowbounds(PromocionClienteExample example, RowBounds rowBounds);

    List<PromocionClienteBean> selectByExample(PromocionClienteExample example);

    PromocionClienteBean selectByPrimaryKey(PromocionClienteKey key);

    int updateByExampleSelective(@Param("record") PromocionClienteBean record, @Param("example") PromocionClienteExample example);

    int updateByExample(@Param("record") PromocionClienteBean record, @Param("example") PromocionClienteExample example);

    int updateByPrimaryKeySelective(PromocionClienteBean record);

    int updateByPrimaryKey(PromocionClienteBean record);

    Long selectMaximaVersion();    

    Integer selectTicketDiaSocio(String uidTicket);    
}