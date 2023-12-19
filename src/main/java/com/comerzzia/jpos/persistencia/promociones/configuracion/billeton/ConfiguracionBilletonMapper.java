package com.comerzzia.jpos.persistencia.promociones.configuracion.billeton;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ConfiguracionBilletonMapper {
    int countByExample(ConfiguracionBilletonExample example);

    int deleteByExample(ConfiguracionBilletonExample example);

    int deleteByPrimaryKey(Long idConfBilleton);

    int insert(ConfiguracionBilletonBean record);

    int insertSelective(ConfiguracionBilletonBean record);

    List<ConfiguracionBilletonBean> selectByExampleWithRowbounds(ConfiguracionBilletonExample example, RowBounds rowBounds);

    List<ConfiguracionBilletonBean> selectByExample(ConfiguracionBilletonExample example);

    ConfiguracionBilletonBean selectByPrimaryKey(Long idConfBilleton);

    int updateByExampleSelective(@Param("record") ConfiguracionBilletonBean record, @Param("example") ConfiguracionBilletonExample example);

    int updateByExample(@Param("record") ConfiguracionBilletonBean record, @Param("example") ConfiguracionBilletonExample example);

    int updateByPrimaryKeySelective(ConfiguracionBilletonBean record);

    int updateByPrimaryKey(ConfiguracionBilletonBean record);
}