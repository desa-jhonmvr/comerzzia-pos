package com.comerzzia.jpos.persistencia.reservaciones.reservaabono;

import com.comerzzia.jpos.persistencia.reservaciones.reservaabono.ReservaAbonoBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservaabono.ReservaAbonoExample;
import com.comerzzia.jpos.persistencia.reservaciones.reservaabono.ReservaAbonoKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ReservaAbonoMapper {
    int countByExample(ReservaAbonoExample example);

    int deleteByExample(ReservaAbonoExample example);

    int deleteByPrimaryKey(ReservaAbonoKey key);

    int insert(ReservaAbonoBean record);

    int insertSelective(ReservaAbonoBean record);

    List<ReservaAbonoBean> selectByExampleWithBLOBsWithRowbounds(ReservaAbonoExample example, RowBounds rowBounds);

    List<ReservaAbonoBean> selectByExampleWithBLOBs(ReservaAbonoExample example);

    List<ReservaAbonoBean> selectByExampleWithRowbounds(ReservaAbonoExample example, RowBounds rowBounds);

    List<ReservaAbonoBean> selectByExample(ReservaAbonoExample example);

    ReservaAbonoBean selectByPrimaryKey(ReservaAbonoKey key);

    int updateByExampleSelective(@Param("record") ReservaAbonoBean record, @Param("example") ReservaAbonoExample example);

    int updateByExampleWithBLOBs(@Param("record") ReservaAbonoBean record, @Param("example") ReservaAbonoExample example);

    int updateByExample(@Param("record") ReservaAbonoBean record, @Param("example") ReservaAbonoExample example);

    int updateByPrimaryKeySelective(ReservaAbonoBean record);

    int updateByPrimaryKeyWithBLOBs(ReservaAbonoBean record);

    int updateByPrimaryKey(ReservaAbonoBean record);
    
    Long consultarSiguienteIdAbono(String uidReservacion);      
}