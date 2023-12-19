package com.comerzzia.jpos.persistencia.cotizaciones;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface CotizacionMapper {
    int countByExample(CotizacionExample example);

    int deleteByExample(CotizacionExample example);

    int deleteByPrimaryKey(String uidCotizacion);

    int insert(CotizacionBean record);

    int insertSelective(CotizacionBean record);

    List<CotizacionBean> selectByExampleWithRowbounds(CotizacionExample example, RowBounds rowBounds);

    List<CotizacionBean> selectByExample(CotizacionExample example);
    
    List<CotizacionBean> selectByExampleWithCliente(CotizacionExample example);

    CotizacionBean selectByPrimaryKey(String uidCotizacion);

    int updateByExampleSelective(@Param("record") CotizacionBean record, @Param("example") CotizacionExample example);

    int updateByExample(@Param("record") CotizacionBean record, @Param("example") CotizacionExample example);

    int updateByPrimaryKeySelective(CotizacionBean record);

    int updateByPrimaryKey(CotizacionBean record);
}