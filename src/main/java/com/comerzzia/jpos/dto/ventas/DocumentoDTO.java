/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas;

import com.comerzzia.jpos.dto.ClienteDTO;
import com.comerzzia.jpos.dto.ItemDTO;
import com.comerzzia.jpos.dto.UsuarioDTO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Gabriel Simbania
 */
public class DocumentoDTO implements Serializable {

    private static final long serialVersionUID = -6853452696584750555L;

    private String numeroDocumento;
    private String lugSRI;
    private String tipoDocumento;
    private String tipoMovimiento;
    private String observacion;
    private ClienteDTO clienteDTO;
    private List<ItemDTO> itemDtoLista;
    private List<MedioPagoDTO> medioPagoLista;
    private UsuarioDTO usuarioDTO;
    private transient BigDecimal totalFactura;
    private transient Long tipoDevolucion;
    private transient MotivoDevolucionDTO motivoDevolucionDTO;
    private transient String estadoMercaderia;
    private transient String observacionNC;
    private transient String usuarioAutoriza;
    private byte[] blob;

    public DocumentoDTO() {

    }

    public DocumentoDTO(String numeroDocumento, String lugSRI, String observacion, List<ItemDTO> itemDtoLista, UsuarioDTO usuarioDTO) {
        this.numeroDocumento = numeroDocumento;
        this.lugSRI = lugSRI;
        this.observacion = observacion;
        this.itemDtoLista = itemDtoLista;
        this.usuarioDTO = usuarioDTO;
    }

    public String getLugSRI() {
        return lugSRI;
    }

    public void setLugSRI(String lugSRI) {
        this.lugSRI = lugSRI;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<ItemDTO> getItemDtoLista() {
        return itemDtoLista;
    }

    public void setItemDtoLista(List<ItemDTO> itemDtoLista) {
        this.itemDtoLista = itemDtoLista;
    }

    public UsuarioDTO getUsuarioDTO() {
        return usuarioDTO;
    }

    public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
        this.usuarioDTO = usuarioDTO;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public List<MedioPagoDTO> getMedioPagoLista() {
        return medioPagoLista;
    }

    public void setMedioPagoLista(List<MedioPagoDTO> medioPagoLista) {
        this.medioPagoLista = medioPagoLista;
    }

    public ClienteDTO getClienteDTO() {
        return clienteDTO;
    }

    public void setClienteDTO(ClienteDTO clienteDTO) {
        this.clienteDTO = clienteDTO;
    }

    public BigDecimal getTotalFactura() {
        return totalFactura;
    }

    public void setTotalFactura(BigDecimal totalFactura) {
        this.totalFactura = totalFactura;
    }

    public Long getTipoDevolucion() {
        return tipoDevolucion;
    }

    public void setTipoDevolucion(Long tipoDevolucion) {
        this.tipoDevolucion = tipoDevolucion;
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

    public String getObservacionNC() {
        return observacionNC;
    }

    public void setObservacionNC(String observacionNC) {
        this.observacionNC = observacionNC;
    }

    public byte[] getBlob() {
        return blob;
    }

    public void setBlob(byte[] blob) {
        this.blob = blob;
    }

    public String getUsuarioAutoriza() {
        return usuarioAutoriza;
    }

    public void setUsuarioAutoriza(String usuarioAutoriza) {
        this.usuarioAutoriza = usuarioAutoriza;
    }
}
