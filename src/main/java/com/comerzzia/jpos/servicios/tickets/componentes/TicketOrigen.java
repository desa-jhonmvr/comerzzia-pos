/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.tickets.componentes;

import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.FacturacionTicket;
import com.comerzzia.jpos.persistencia.core.usuarios.UsuarioBean;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.tickets.xml.TicketXMLServices;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.pagos.Pago;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentNode;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Admin
 */
public class TicketOrigen {

    private List<LineaTicket> lineas;
    private Cliente cliente;
    private XMLDocumentNode pagos;
    private String uid_ticket;
    private long id_ticket;
    private Fecha fecha;
    private Fecha fechaFinDevolucion;
    private String tienda;
    private String codcaja;
    private String uid_diario_caja;
    private String uid_cajero_caja;
    private UsuarioBean cajero;
    private BigDecimal totalDtoPagos = null;
    private List<Pago> listaPagos;
    private TotalesXML totales;
    private FacturacionTicket facturacion;
    private XMLDocumentNode nodoFacturacion;
    // Puntos
    private Integer puntosAcumulados;
    private Integer puntosConsumidos;
    private String clienteAcumulacion;
    // Promociones
    private String uidTicketDiaSocio;
    // Compensacion
    private BigDecimal porcentajeCompensacion; 
    
    private static final Logger log = Logger.getMLogger(TicketOrigen.class);

    public TicketOrigen() {
        super();
    }

    public static TicketOrigen getTicketOrigen(TicketS ticket) throws TicketException {
        return getTicketOrigen(TicketXMLServices.getXMLDocumentTicket(ticket));
    }

    public static TicketOrigen getTicketOrigen(XMLDocument xml) throws TicketException {
        return TicketXMLServices.getTicketOrigen(xml);
    }

    /** Añade a la lista lineasContains pasada por parámetro todas las líneas del ticket
     * que contienen el artículo indicado por parámetro.
     * Devuelve el número de artículos (suma de cantidades de cada 
     * línea añadida) añadidos a la lista.
     * @param articulo
     * @param lineasContains
     * @return 
     */
    public int getContains(String codArticulo, List<LineaTicket> lineasContains) {
        int cant = 0;
        for (LineaTicket linea : lineas) {
            if (linea.getArticulo().getCodart().equals(codArticulo)) {
                lineasContains.add(linea);
                cant += linea.getCantidad();
                cant = cant + Sesion.getTicket().getLineas().getNumGarantiasExtendidas();
            }
        }
        Comparator<LineaTicket> comparator = new Comparator<LineaTicket>() {

            @Override
            public int compare(LineaTicket a, LineaTicket b) {
                if (!a.isPromocionAplicada() && !b.isPromocionAplicada()) {
                    return 0;
                }
                if (!a.isPromocionAplicada() && b.isPromocionAplicada()) {
                    return 1;
                }
                if (a.isPromocionAplicada() && !b.isPromocionAplicada()) {
                    return -1;
                }
                return a.getImporteTotalFinalPagado().compareTo(b.getImporteTotalFinalPagado());
            }
        };
        Collections.sort(lineasContains, comparator);
        return cant;
    }

    public String getIdFactura() {
        String a = new Long(id_ticket).toString();
        String b = new Long(id_ticket).toString();
        for (int i = a.length(); i < 9; i++) {
            b = "0" + b;
        }
        return tienda + "-" + codcaja + "-" + b;
    }
    
    public String getIdFacturaNotaCreditoElectronica() {
        String a = new Long(id_ticket).toString();
        String b = new Long(id_ticket).toString();
        for (int i = a.length(); i < 9; i++) {
            b = "0" + b;
        }
        return tienda + "-" + codcaja + "-" + b;
    }

    public UsuarioBean getCajero() {
        return cajero;
    }

    public void setCajero(UsuarioBean cajero) {
        this.cajero = cajero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getCodcaja() {
        return codcaja;
    }

    public void setCodcaja(String codcaja) {
        this.codcaja = codcaja;
    }

    public Fecha getFecha() {
        return fecha;
    }

    public void setFecha(Fecha fecha) {
        this.fecha = fecha;
    }

    public long getId_ticket() {
        return id_ticket;
    }

    public void setId_ticket(long id_ticket) {
        this.id_ticket = id_ticket;
    }

    public List<LineaTicket> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaTicket> lineas) {
        this.lineas = lineas;
    }

    public XMLDocumentNode getPagos() {
        return pagos;
    }

    public void setPagos(XMLDocumentNode pagos) {
        this.pagos = pagos;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    public String getUid_cajero_caja() {
        return uid_cajero_caja;
    }

    public void setUid_cajero_caja(String uid_cajero_caja) {
        this.uid_cajero_caja = uid_cajero_caja;
    }

    public String getUid_diario_caja() {
        return uid_diario_caja;
    }

    public void setUid_diario_caja(String uid_diario_caja) {
        this.uid_diario_caja = uid_diario_caja;
    }

    public String getUid_ticket() {
        return uid_ticket;
    }

    public void setUid_ticket(String uid_ticket) {
        this.uid_ticket = uid_ticket;
    }

    public List<Pago> getListaPagos() {
        return listaPagos;
    }

    public void setListaPagos(List<Pago> listaPagos) {
        this.listaPagos = listaPagos;
    }

    public Fecha getFechaFinDevolucion() {
        return fechaFinDevolucion;
    }

    public boolean isFechaFinDevolucionCaducada(){
        Fecha fechaFin = new Fecha(fechaFinDevolucion.getString());
        fechaFin.sumaDias(1);
        return fechaFin.antes(new Fecha());
    }
    
    public void setFechaFinDevolucion(Fecha fechaFinDevolucion) {
        this.fechaFinDevolucion = fechaFinDevolucion;
    }

    public BigDecimal getTotalDtoPagos() {
        return totalDtoPagos;
    }

    public void setTotalDtoPagos(BigDecimal totalDtoPagos) {
        this.totalDtoPagos = totalDtoPagos;
    }

    public TotalesXML getTotales() {
        return this.totales;
    }
    
    public void setTotales(TotalesXML totales){
        this.totales = totales;
    }

    public FacturacionTicket getFacturacion() {
        return facturacion;
    }

    public void setFacturacion(FacturacionTicket facturacion) {
        this.facturacion = facturacion;
    }

    public XMLDocumentNode getNodoFacturacion() {
        return nodoFacturacion;
    }

    public void setNodoFacturacion(XMLDocumentNode nodoFacturacion) {
        this.nodoFacturacion = nodoFacturacion;
    }

    public boolean isFacturaAcumulaPuntos() {
        return clienteAcumulacion != null;
    }

    public Integer getPuntosAcumulados() {
        return puntosAcumulados;
    }

    public void setPuntosAcumulados(Integer puntosAcumulados) {
        this.puntosAcumulados = puntosAcumulados;
    }

    public Integer getPuntosConsumidos() {
        return puntosConsumidos;
    }

    public void setPuntosConsumidos(Integer puntosConsumidos) {
        this.puntosConsumidos = puntosConsumidos;
    }

    public String getClienteAcumulacion() {
        return clienteAcumulacion;
    }

    public void setClienteAcumulacion(String clienteAcumulacion) {
        this.clienteAcumulacion = clienteAcumulacion;
    }

    public String getUidTicketDiaSocio() {
        return uidTicketDiaSocio;
    }
    
    public void setUidTicketDiaSocio(String uidTicketDiaSocio) {
        this.uidTicketDiaSocio = uidTicketDiaSocio;
    }

    public LineaTicket getLinea(int lineaOriginal) {
        for (LineaTicket l : getLineas()){
            if (l.getIdlinea() == lineaOriginal){
                return l;
            }
        }
        return null;
    }
    
    public BigDecimal getCompensacion(){
        BigDecimal res = BigDecimal.ZERO;
        if(listaPagos != null){
            for(Pago p:listaPagos){
                if(p.getMedioPagoActivo() != null && p.getMedioPagoActivo().isCompensacion()){
                    return p.getTotal();
                }
            }
        }
        return res;
    }   

    public BigDecimal getPorcentajeCompensacion() {
        return porcentajeCompensacion;
    }

    public void setPorcentajeCompensacion(BigDecimal porcentajeCompensacion) {
        this.porcentajeCompensacion = porcentajeCompensacion;
    }
    
}
