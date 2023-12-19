package com.comerzzia.jpos.persistencia.reservaciones.reservainvitados;

import com.comerzzia.jpos.persistencia.reservaciones.reservainvitados.ReservaInvitadoBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservainvitados.ReservaInvitadoExample;
import com.comerzzia.jpos.persistencia.reservaciones.reservainvitados.ReservaInvitadoKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ReservaInvitadoMapper {
    int countByExample(ReservaInvitadoExample example);

    int deleteByExample(ReservaInvitadoExample example);

    int deleteByPrimaryKey(ReservaInvitadoKey key);

    int insert(ReservaInvitadoBean record);

    int insertSelective(ReservaInvitadoBean record);

    List<ReservaInvitadoBean> selectByExampleWithRowbounds(ReservaInvitadoExample example, RowBounds rowBounds);

    List<ReservaInvitadoBean> selectByExample(ReservaInvitadoExample example);

    ReservaInvitadoBean selectByPrimaryKey(ReservaInvitadoKey key);

    int updateByExampleSelective(@Param("record") ReservaInvitadoBean record, @Param("example") ReservaInvitadoExample example);

    int updateByExample(@Param("record") ReservaInvitadoBean record, @Param("example") ReservaInvitadoExample example);

    int updateByPrimaryKeySelective(ReservaInvitadoBean record);

    int updateByPrimaryKey(ReservaInvitadoBean record);
    
    Long consultarSiguienteIdInvitado(String uidReservacion);
}