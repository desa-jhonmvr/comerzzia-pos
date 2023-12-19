package com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact;

import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface FacturacionTicketMapper {
    int countByExample(FacturacionTicketExample example);

    int deleteByExample(FacturacionTicketExample example);

    int deleteByPrimaryKey(String uidReservacion);

    int insert(FacturacionTicketBean record);

    int insertSelective(FacturacionTicketBean record);

    List<FacturacionTicketBean> selectByExampleWithRowbounds(FacturacionTicketExample example, RowBounds rowBounds);

    List<FacturacionTicketBean> selectByExample(FacturacionTicketExample example);

    FacturacionTicketBean selectByPrimaryKey(String uidReservacion);

    int updateByExampleSelective(@Param("record") FacturacionTicketBean record, @Param("example") FacturacionTicketExample example);

    int updateByExample(@Param("record") FacturacionTicketBean record, @Param("example") FacturacionTicketExample example);

    int updateByPrimaryKeySelective(FacturacionTicketBean record);

    int updateByPrimaryKey(FacturacionTicketBean record);
}