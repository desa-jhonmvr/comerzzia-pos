package com.comerzzia.jpos.persistencia.promociones.configuracion.billeton.detalles;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ConfiguracionBilletonDetalleMapper {
	
    int countByExample(ConfiguracionBilletonDetalleExample example);

    int deleteByExample(ConfiguracionBilletonDetalleExample example);

    int deleteByPrimaryKey(Long idConfBilletonDet);

    int insert(ConfiguracionBilletonDetalleBean record);

    int insertSelective(ConfiguracionBilletonDetalleBean record);

    List<ConfiguracionBilletonDetalleBean> selectByExampleWithRowbounds(ConfiguracionBilletonDetalleExample example, RowBounds rowBounds);

    List<ConfiguracionBilletonDetalleBean> selectByExample(ConfiguracionBilletonDetalleExample example);

    ConfiguracionBilletonDetalleBean selectByPrimaryKey(Long idConfBilletonDet);

    int updateByExampleSelective(@Param("record") ConfiguracionBilletonDetalleBean record, @Param("example") ConfiguracionBilletonDetalleExample example);

    int updateByExample(@Param("record") ConfiguracionBilletonDetalleBean record, @Param("example") ConfiguracionBilletonDetalleExample example);

    int updateByPrimaryKeySelective(ConfiguracionBilletonDetalleBean record);

    int updateByPrimaryKey(ConfiguracionBilletonDetalleBean record);

    List<ConfiguracionBilletonDetalleBean> selectByExampleConDesFormato(ConfiguracionBilletonDetalleExample example);
    
}