package com.comerzzia.jpos.persistencia.listapda.detalle;

import com.comerzzia.jpos.persistencia.listapda.detalle.DetalleSesionPdaBean;
import com.comerzzia.jpos.persistencia.listapda.detalle.DetalleSesionPdaExample;
import com.comerzzia.jpos.persistencia.listapda.detalle.DetalleSesionPdaKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface DetalleSesionPdaMapper {
    int countByExample(DetalleSesionPdaExample example);

    int deleteByExample(DetalleSesionPdaExample example);

    int deleteByPrimaryKey(DetalleSesionPdaKey key);

    int insert(DetalleSesionPdaBean record);

    int insertSelective(DetalleSesionPdaBean record);

    List<DetalleSesionPdaBean> selectByExampleWithRowbounds(DetalleSesionPdaExample example, RowBounds rowBounds);

    List<DetalleSesionPdaBean> selectByExample(DetalleSesionPdaExample example);

    DetalleSesionPdaBean selectByPrimaryKey(DetalleSesionPdaKey key);

    int updateByExampleSelective(@Param("record") DetalleSesionPdaBean record, @Param("example") DetalleSesionPdaExample example);

    int updateByExample(@Param("record") DetalleSesionPdaBean record, @Param("example") DetalleSesionPdaExample example);

    int updateByPrimaryKeySelective(DetalleSesionPdaBean record);

    int updateByPrimaryKey(DetalleSesionPdaBean record);
}