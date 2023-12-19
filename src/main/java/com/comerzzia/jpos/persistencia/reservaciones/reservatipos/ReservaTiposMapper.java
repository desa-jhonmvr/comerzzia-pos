package com.comerzzia.jpos.persistencia.reservaciones.reservatipos;

import com.comerzzia.jpos.persistencia.reservaciones.reservatipos.ReservaTiposBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservatipos.ReservaTiposExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ReservaTiposMapper {
    int countByExample(ReservaTiposExample example);

    int deleteByExample(ReservaTiposExample example);

    int deleteByPrimaryKey(String codTipo);

    int insert(ReservaTiposBean record);

    int insertSelective(ReservaTiposBean record);

    List<ReservaTiposBean> selectByExampleWithRowbounds(ReservaTiposExample example, RowBounds rowBounds);

    List<ReservaTiposBean> selectByExample(ReservaTiposExample example);

    ReservaTiposBean selectByPrimaryKey(String codTipo);

    int updateByExampleSelective(@Param("record") ReservaTiposBean record, @Param("example") ReservaTiposExample example);

    int updateByExample(@Param("record") ReservaTiposBean record, @Param("example") ReservaTiposExample example);

    int updateByPrimaryKeySelective(ReservaTiposBean record);

    int updateByPrimaryKey(ReservaTiposBean record);
}