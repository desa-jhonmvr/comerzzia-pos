package com.comerzzia.jpos.persistencia.letras.detalles;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface LetraCuotaMapper {
    int countByExample(LetraCuotaExample example);

    int deleteByExample(LetraCuotaExample example);

    int deleteByPrimaryKey(LetraCuotaKey key);

    int insert(LetraCuotaBean record);

    int insertSelective(LetraCuotaBean record);

    List<LetraCuotaBean> selectByExampleWithBLOBsWithRowbounds(LetraCuotaExample example, RowBounds rowBounds);

    List<LetraCuotaBean> selectByExampleWithBLOBs(LetraCuotaExample example);

    List<LetraCuotaBean> selectByExampleWithRowbounds(LetraCuotaExample example, RowBounds rowBounds);

    List<LetraCuotaBean> selectByExample(LetraCuotaExample example);

    LetraCuotaBean selectByPrimaryKey(LetraCuotaKey key);

    int updateByExampleSelective(@Param("record") LetraCuotaBean record, @Param("example") LetraCuotaExample example);

    int updateByExampleWithBLOBs(@Param("record") LetraCuotaBean record, @Param("example") LetraCuotaExample example);

    int updateByExample(@Param("record") LetraCuotaBean record, @Param("example") LetraCuotaExample example);

    int updateByPrimaryKeySelective(LetraCuotaBean record);

    int updateByPrimaryKeyWithBLOBs(LetraCuotaBean record);

    int updateByPrimaryKey(LetraCuotaBean record);
}