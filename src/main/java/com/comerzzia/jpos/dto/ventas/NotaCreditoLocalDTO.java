/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas;

import com.comerzzia.jpos.dto.UsuarioDTO;
import java.io.Serializable;

/**
 *
 * @author Gabriel Simbania
 */
public class NotaCreditoLocalDTO implements Serializable {

    private static final long serialVersionUID = -6845572521115091241L;

    private String lugSRIOriginal;
    private String lugSRIGenera;
    private String codCajaGenera;
    private DocumentoDTO documentoDTO;
    private MotivoDevolucionDTO motivoDevolucionDTO;
    private String estadoMercaderia;
    private String observaciones;
    private UsuarioDTO usuarioDTOGenera;
    private Long tipoDevolucion;
    private String usuarioAutoriza;

    public String getLugSRIOriginal() {
        return lugSRIOriginal;
    }

    public void setLugSRIOriginal(String lugSRIOriginal) {
        this.lugSRIOriginal = lugSRIOriginal;
    }

    public String getLugSRIGenera() {
        return lugSRIGenera;
    }

    public void setLugSRIGenera(String lugSRIGenera) {
        this.lugSRIGenera = lugSRIGenera;
    }

    public String getCodCajaGenera() {
        return codCajaGenera;
    }

    public void setCodCajaGenera(String codCajaGenera) {
        this.codCajaGenera = codCajaGenera;
    }

    public DocumentoDTO getDocumentoDTO() {
        return documentoDTO;
    }

    public void setDocumentoDTO(DocumentoDTO documentoDTO) {
        this.documentoDTO = documentoDTO;
    }

    public UsuarioDTO getUsuarioDTOGenera() {
        return usuarioDTOGenera;
    }

    public void setUsuarioDTOGenera(UsuarioDTO usuarioDTOGenera) {
        this.usuarioDTOGenera = usuarioDTOGenera;
    }

    public MotivoDevolucionDTO getMotivoDevolucionDTO() {
        return motivoDevolucionDTO;
    }

    public void setMotivoDevolucionDTO(MotivoDevolucionDTO motivoDevolucionDTO) {
        this.motivoDevolucionDTO = motivoDevolucionDTO;
    }

    public String getEstadoMercaderia() {
        return estadoMercaderia;
    }

    public void setEstadoMercaderia(String estadoMercaderia) {
        this.estadoMercaderia = estadoMercaderia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Long getTipoDevolucion() {
        return tipoDevolucion;
    }

    public void setTipoDevolucion(Long tipoDevolucion) {
        this.tipoDevolucion = tipoDevolucion;
    }

    public String getUsuarioAutoriza() {
        return usuarioAutoriza;
    }

    public void setUsuarioAutoriza(String usuarioAutoriza) {
        this.usuarioAutoriza = usuarioAutoriza;
    }

}
