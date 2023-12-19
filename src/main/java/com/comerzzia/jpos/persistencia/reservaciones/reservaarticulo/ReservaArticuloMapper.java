package com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo;

import com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo.ReservaArticuloBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo.ReservaArticuloExample;
import com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo.ReservaArticuloKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ReservaArticuloMapper {
    int countByExample(ReservaArticuloExample example);

    int deleteByExample(ReservaArticuloExample example);

    int deleteByPrimaryKey(ReservaArticuloKey key);

    int insert(ReservaArticuloBean record);

    int insertSelective(ReservaArticuloBean record);

    List<ReservaArticuloBean> selectByExampleWithRowbounds(ReservaArticuloExample example, RowBounds rowBounds);

    List<ReservaArticuloBean> selectByExample(ReservaArticuloExample example);

    ReservaArticuloBean selectByPrimaryKey(ReservaArticuloKey key);

    int updateByExampleSelective(@Param("record") ReservaArticuloBean record, @Param("example") ReservaArticuloExample example);

    int updateByExample(@Param("record") ReservaArticuloBean record, @Param("example") ReservaArticuloExample example);

    int updateByPrimaryKeySelective(ReservaArticuloBean record);

    int updateByPrimaryKey(ReservaArticuloBean record);
    
    Long consultarSiguienteIdArticulo(String uidReservacion);    
}