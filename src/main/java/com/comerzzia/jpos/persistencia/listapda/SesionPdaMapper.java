package com.comerzzia.jpos.persistencia.listapda;

import com.comerzzia.jpos.persistencia.listapda.SesionPdaBean;
import com.comerzzia.jpos.persistencia.listapda.SesionPdaExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SesionPdaMapper {
    int countByExample(SesionPdaExample example);

    int deleteByExample(SesionPdaExample example);

    int deleteByPrimaryKey(String uidSesionPda);

    int insert(SesionPdaBean record);

    int insertSelective(SesionPdaBean record);

    List<SesionPdaBean> selectByExampleWithRowbounds(SesionPdaExample example, RowBounds rowBounds);

    List<SesionPdaBean> selectByExample(SesionPdaExample example);

    SesionPdaBean selectByPrimaryKey(String uidSesionPda);

    int updateByExampleSelective(@Param("record") SesionPdaBean record, @Param("example") SesionPdaExample example);

    int updateByExample(@Param("record") SesionPdaBean record, @Param("example") SesionPdaExample example);

    int updateByPrimaryKeySelective(SesionPdaBean record);

    int updateByPrimaryKey(SesionPdaBean record);
}