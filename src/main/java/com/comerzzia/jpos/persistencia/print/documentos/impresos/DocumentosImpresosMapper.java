package com.comerzzia.jpos.persistencia.print.documentos.impresos;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface DocumentosImpresosMapper {
    int countByExample(DocumentosImpresosExample example);

    int deleteByExample(DocumentosImpresosExample example);

    int deleteByPrimaryKey(DocumentosImpresosKey key);

    int insert(DocumentosImpresosBean record);

    int insertSelective(DocumentosImpresosBean record);

    List<DocumentosImpresosBean> selectByExampleWithBLOBsWithRowbounds(DocumentosImpresosExample example, RowBounds rowBounds);

    List<DocumentosImpresosBean> selectByExampleWithBLOBs(DocumentosImpresosExample example);

    List<DocumentosImpresosBean> selectByExampleWithRowbounds(DocumentosImpresosExample example, RowBounds rowBounds);

    List<DocumentosImpresosBean> selectByExample(DocumentosImpresosExample example);

    DocumentosImpresosBean selectByPrimaryKey(DocumentosImpresosKey key);

    int updateByExampleSelective(@Param("record") DocumentosImpresosBean record, @Param("example") DocumentosImpresosExample example);

    int updateByExampleWithBLOBs(@Param("record") DocumentosImpresosBean record, @Param("example") DocumentosImpresosExample example);

    int updateByExample(@Param("record") DocumentosImpresosBean record, @Param("example") DocumentosImpresosExample example);

    int updateByPrimaryKeySelective(DocumentosImpresosBean record);

    int updateByPrimaryKeyWithBLOBs(DocumentosImpresosBean record);

    int updateByPrimaryKey(DocumentosImpresosBean record);
}