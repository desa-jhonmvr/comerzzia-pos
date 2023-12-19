/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas;

import com.comerzzia.jpos.entity.db.NotasCredito;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosBean;
import java.util.List;

/**
 *
 * @author Gabriel Simbania
 */
public class NotaCreditoResponseDTO {

    private NotasCredito notasCredito;
    private DocumentosBean documentosBean;
    private List<DocumentosImpresosBean> documentosImpresos;

    public NotasCredito getNotasCredito() {
        return notasCredito;
    }

    public void setNotasCredito(NotasCredito notasCredito) {
        this.notasCredito = notasCredito;
    }

    public List<DocumentosImpresosBean> getDocumentosImpresos() {
        return documentosImpresos;
    }

    public void setDocumentosImpresos(List<DocumentosImpresosBean> documentosImpresos) {
        this.documentosImpresos = documentosImpresos;
    }

    public DocumentosBean getDocumentosBean() {
        return documentosBean;
    }

    public void setDocumentosBean(DocumentosBean documentosBean) {
        this.documentosBean = documentosBean;
    }

}
