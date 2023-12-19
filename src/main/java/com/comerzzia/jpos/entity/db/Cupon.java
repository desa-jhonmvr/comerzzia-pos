/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.print.lineas.LineaEnTicket;
import com.comerzzia.jpos.servicios.promociones.cupones.PromocionTipoCupon;
import com.comerzzia.jpos.servicios.promociones.cupones.PromocionTipoCuponDescuento;
import es.mpsistemas.util.fechas.Fecha;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author amos
 */
public class Cupon implements Comparable<Cupon> {

    public static final String REF_ORIGEN_FACTURA_VENTA = "FACTURA_VENTA";
    public static final String REF_USO_FACTURA_VENTA = "FACTURA_VENTA";

    private String logo;
    private Long idCupon;
    private String codAlmacen;
    private Fecha fechaExpedicion;
    private Fecha fechaValidez;
    private boolean utilizado;
    private String referenciaUso;
    private String tipoReferenciaUso;
    private String referenciaOrigen;
    private String tipoReferenciaOrigen;
    private PromocionTipoCupon promocion;
    private Long idPromocion;
    private Long idTipoPromocion;
    private String codCliente;
    private Cliente cliente;
    private String codBarras;
    private String idFactura;
    private String variable;
    private String codart;
    private Long cantidad;
    private String valor;
    private String utilizadoValor;
    private String saldo;
    private String textoAdicional;
    private Fecha fechaImpresion;

    public Cupon() {
    }

    public Cupon(ResultSet rs) throws SQLException {
        idCupon = rs.getLong("ID_CUPON");
        codAlmacen = rs.getString("CODALM");
        fechaExpedicion = new Fecha(rs.getDate("FECHA_EXPEDICION"));
        utilizado = rs.getString("UTILIZADO").equals("S");
        referenciaUso = rs.getString("REFERENCIA_USO");
        tipoReferenciaUso = rs.getString("TIPO_REFERENCIA_USO");
        referenciaOrigen = rs.getString("REFERENCIA_USO");
        tipoReferenciaOrigen = rs.getString("TIPO_REFERENCIA_USO");
        codCliente = rs.getString("CODCLI");
        idPromocion = rs.getLong("ID_PROMOCION");
        variable = rs.getString("VARIABLE");
        saldo = rs.getString("SALDO");
        fechaValidez = Fecha.getFecha(rs.getTimestamp("FECHA_VALIDEZ"));
    }

    public String getFechaForPrint() {
        return this.fechaExpedicion.getString("dd-MMM-yyyy HH:mm");
    }

    public String getCaducidadForPrint() {
        return this.getFechaValidez().getString("dd-MMM-yyyy HH:mm");
    }

    public String getFechaImpresionForPrint() {
        return this.getFechaImpresion().getString("dd-MMM-yyyy HH:mm");
    }

    public List<String> getLineasAvisoLegal() {
        LineaEnTicket lineas = new LineaEnTicket(this.promocion.getTextoLegales(), true);
        return lineas.getLineas();
    }

    public List<String> getLineasTextoPromocion() {
        LineaEnTicket lineas = new LineaEnTicket(this.promocion.getTextoPromocion(), true);
        return lineas.getLineas();
    }

    public List<String> getTextoAdicionalForPrint() {
        if (textoAdicional == null) {
            return null;
        }
        LineaEnTicket lineas = new LineaEnTicket(textoAdicional, true);
        return lineas.getLineas();
    }

    public Cupon(TicketS ticket, PromocionTipoCupon promocion) {
        codAlmacen = ticket.getTienda();
        fechaExpedicion = new Fecha();
        utilizado = false;
        tipoReferenciaOrigen = REF_ORIGEN_FACTURA_VENTA;
        referenciaOrigen = ticket.getUid_ticket();
        this.promocion = promocion;
        codCliente = ticket.getCliente().getCodcli();
        idFactura = ticket.getIdFactura();
        cliente = ticket.getCliente();
        fechaValidez = promocion.getFechaValidez();
        fechaImpresion = promocion.getFechaValidez();
    }

    public boolean isVigente() {
        Fecha hoy = new Fecha();
        return hoy.antesOrEquals(getFechaValidez());

    }

    public String getCodAlmacen() {
        return codAlmacen;
    }

    public void setCodAlmacen(String codAlmacen) {
        this.codAlmacen = codAlmacen;
    }

    public Fecha getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(Fecha fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public Long getIdCupon() {
        return idCupon;
    }

    public void setIdCupon(Long idCupon) {
        this.idCupon = idCupon;
    }

    public String getReferenciaOrigen() {
        return referenciaOrigen;
    }

    public void setReferenciaOrigen(String referenciaOrigen) {
        this.referenciaOrigen = referenciaOrigen;
    }

    public String getReferenciaUso() {
        return referenciaUso;
    }

    public void setReferenciaUso(String referenciaUso) {
        this.referenciaUso = referenciaUso;
    }

    public String getTipoReferenciaOrigen() {
        return tipoReferenciaOrigen;
    }

    public void setTipoReferenciaOrigen(String tipoReferenciaOrigen) {
        this.tipoReferenciaOrigen = tipoReferenciaOrigen;
    }

    public String getTipoReferenciaUso() {
        return tipoReferenciaUso;
    }

    public void setTipoReferenciaUso(String tipoReferenciaUso) {
        this.tipoReferenciaUso = tipoReferenciaUso;
    }

    public boolean isUtilizado() {
        return utilizado;
    }

    public void setUtilizado(boolean utilizado) {
        this.utilizado = utilizado;
    }

    public PromocionTipoCupon getPromocion() {
        return promocion;
    }

    public void setPromocion(PromocionTipoCupon promocion) {
        this.promocion = promocion;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public Long getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion(Long idPromocion) {
        this.idPromocion = idPromocion;
    }

    public String getCodBarras() {
        return codBarras;
    }

    public void setCodBarras(String codBarras) {
        this.codBarras = codBarras;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getIdFactura() {
        return idFactura;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public void setVariable(Integer variable) {
        this.variable = variable.toString();
    }

    // TODO: PROMOCION: Este texto debería ser el texto adicional incluido para los cupones de sukasa.
    public String getTextoCuponForPrint() {
        if (getPromocion().getTipoPromocion().isPromocionTipoCuponDescuento()) {
            return "DESCUENTO DEL " + ((PromocionTipoCuponDescuento) getPromocion()).getDescuento().intValue() + "%";
        }
        if (getPromocion().getTipoPromocion().isPromocionTipoCuponVotos()) {
            return "NÚMERO DE VOTOS: " + getVariable();
        }
        return "";

    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Cupon) {
            Cupon c = (Cupon) o;
            if (c.getIdCupon().equals(getIdCupon()) && c.getCodAlmacen().equals(getCodAlmacen())) {
                return true;
            }
        }
        return false;
    }

    public String getTextoAdicional() {
        return textoAdicional;
    }

    public void setTextoAdicional(String textoAdicional) {
        this.textoAdicional = textoAdicional;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Fecha getFechaValidez() {
        if (fechaValidez == null && promocion != null) {
            fechaValidez = promocion.getFechaValidez();
        }
        return fechaValidez;
    }

    public void setFechaValidez(Fecha fechaValidez) {
        this.fechaValidez = fechaValidez;
    }

    public Fecha getFechaImpresion() {
        return fechaImpresion;
    }

    public void setFechaImpresion(Fecha fechaImpresion) {
        this.fechaImpresion = fechaImpresion;
    }

    @Override
    public int compareTo(Cupon c) {
        try {
            if (Sesion.isSukasa() && getPromocion().getTipoPromocion().isPromocionTipoBilleton() && c.getPromocion().getTipoPromocion().isPromocionTipoBilleton()) {
                return 0;
            } else if (Sesion.isSukasa() && getPromocion().getTipoPromocion().isPromocionTipoBilleton() && !c.getPromocion().getTipoPromocion().isPromocionTipoBilleton()) {
                return -1;
            } else {
                return 1;
            }
        } catch (Exception e) {
            return 1;
        }
    }

    public String getUtilizadoValor() {
        return utilizadoValor;
    }

    public void setUtilizadoValor(String utilizadoValor) {
        this.utilizadoValor = utilizadoValor;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getCodart() {
        return codart;
    }

    public void setCodart(String codart) {
        this.codart = codart;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Long getIdTipoPromocion() {
        return idTipoPromocion;
    }

    public void setIdTipoPromocion(Long idTipoPromocion) {
        this.idTipoPromocion = idTipoPromocion;
    }
    
    

}
