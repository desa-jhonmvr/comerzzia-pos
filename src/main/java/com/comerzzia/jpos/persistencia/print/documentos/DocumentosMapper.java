package com.comerzzia.jpos.persistencia.print.documentos;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface DocumentosMapper {
    
    int countByExample(DocumentosExample example);

    int deleteByExample(DocumentosExample example);

    int deleteByPrimaryKey(String uidDocumento);

    int insert(DocumentosBean record);

    int insertSelective(DocumentosBean record);

    List<DocumentosBean> selectByExampleWithRowbounds(DocumentosExample example, RowBounds rowBounds);
    
    List<DocumentosBean> selectByExampleWithBono(DocumentosExample example);
    
    List<DocumentosBean> selectByExampleWithGiftCard(DocumentosExample example);
    
    List<DocumentosBean> selectByExampleWithNotaCredito(DocumentosExample example);
    
    List<DocumentosBean> selectByExampleWithReserva(DocumentosExample example);

    List<DocumentosBean> selectByExample(DocumentosExample example);

    DocumentosBean selectByPrimaryKey(String uidDocumento);

    int updateByExampleSelective(@Param("record") DocumentosBean record, @Param("example") DocumentosExample example);

    int updateByExample(@Param("record") DocumentosBean record, @Param("example") DocumentosExample example);

    int updateByPrimaryKeySelective(DocumentosBean record);

    int updateByPrimaryKey(DocumentosBean record);
    
    int updateByEstado(DocumentosBean record);
}