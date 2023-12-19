package com.comerzzia.jpos.persistencia.reservaciones.reserva;

import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaBean;
import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ReservaMapper {
    int countByExample(ReservaExample example);

    int deleteByExample(ReservaExample example);

    int deleteByPrimaryKey(String uidReservacion);

    int insert(ReservaBean record);

    int insertSelective(ReservaBean record);

    List<ReservaBean> selectByExampleWithRowbounds(ReservaExample example, RowBounds rowBounds);

    List<ReservaBean> selectByExample(ReservaExample example);

    ReservaBean selectByPrimaryKey(String uidReservacion);

    int updateByExampleSelective(@Param("record") ReservaBean record, @Param("example") ReservaExample example);

    int updateByExample(@Param("record") ReservaBean record, @Param("example") ReservaExample example);

    int updateByPrimaryKeySelective(ReservaBean record);

    int updateByPrimaryKey(ReservaBean record);

    ReservaBean selectFromViewByPrimaryKey(String uidReservacion);

    List<ReservaBean> selectFromViewByExampleWithRowbounds(ReservaExample example, RowBounds rowBounds);

    List<ReservaBean> selectFromViewByExample(ReservaExample example);
}