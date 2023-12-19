package com.comerzzia.jpos.servicios.promociones;

import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.persistencia.mediospagos.VencimientoBean;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.persistencia.promociones.TipoPromocionBean;
import com.comerzzia.jpos.servicios.afiliacion.ITarjetaAfiliacion;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.promociones.articulos.ItemSeleccionArticuloBean;
import com.comerzzia.jpos.servicios.promociones.articulos.RangosPromocion;
import com.comerzzia.jpos.servicios.promociones.articulos.SeleccionArticuloBean;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocion;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoDescuentoCombinado;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.util.cadenas.Cadena;

import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Promocion implements Comparable<Promocion> {

    /**
     * Logger
     */
    protected static Logger log = Logger.getMLogger(Promocion.class);
    private Long idPromocion;
    private String codTar;
    private String descripcion;
    private Fecha fechaInicio;
    private Fecha fechaFin;
    private boolean soloFidelizacion;
    private TipoPromocionBean tipoPromocion;
    private String textoPromocion;
    private List<String> afiliados;
    private Integer prioridad;
    private String afiliadoIdentificacion; //La promoción se aplica a los clientes que se hayan identificado mediante dicho método (Por defecto CUALQUIER)
    protected SeleccionArticuloBean seleccion;
    protected RangosPromocion rangos;
    protected List<Long> vencimientos;
    protected boolean tieneFiltroPagos;
    protected boolean tieneFiltroPagosTarjetaSukasa;
    protected String filtroPagosDescripcion;
    private boolean soloPromoPermanente;

    protected Promocion() {
    }

    protected Promocion(PromocionBean promocion) throws PromocionException {
        if (promocion != null) {
            idPromocion = new Long(promocion.getIdPromocion());
            tipoPromocion = new TipoPromocionBean(promocion.getIdTipoPromocion(), promocion.getDesTipoPromocion());
            codTar = promocion.getCodTar();
            descripcion = promocion.getDescripcion();
            if (promocion.getFechaInicio() != null) {
                fechaInicio = new Fecha(promocion.getFechaInicio().getDate());
                fechaFin = new Fecha(promocion.getFechaFin().getDate());
                textoPromocion = promocion.getTextoPromocion();
                log.debug("Cargando promoción : " + toString());
                parsearXMLDatosPromocion(promocion.getDatosPromocion());
            }
        }

    }

    @Override
    public String toString() {
        return idPromocion + " / " + descripcion;
    }

    private void parsearXMLDatosPromocion(byte[] datosPromocion) throws PromocionException {
        try {
            XMLDocument xml = new XMLDocument(datosPromocion);
            XMLDocumentNode cabecera = xml.getNodo("cabecera");
            try {
                prioridad = cabecera.getNodo("prioridad").getValueAsInteger();
            } catch (Exception e) {
                prioridad = 0;
            }
            try {
                afiliadoIdentificacion = cabecera.getNodo("afiliadoIdentificacion").getValue();
            } catch (Exception e) {
                afiliadoIdentificacion = "";
            }
            parsearXMLDatosPromocion(xml);
        } catch (XMLDocumentException e) {
            String msg = "Se ha producido un error parseando el xml de los datos de promocion: " + e.getMessage();
            log.error("parsearXMLDatosPromocion() - " + msg);
            throw new PromocionException(msg);
        }
    }

    protected abstract void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException;

    // aplicación de la promoción a una línea de forma unitaria
    public abstract void aplicaLineaUnitaria(TicketS ticket, LineaTicket linea, DetallePromocion detalle, Tarifas tarifa);

    // realiza el cálculo de dto de aplicar la promoción a una línea unitaria (sin aplicarla)
    public abstract BigDecimal calculaDtoLineaUnitaria(LineaTicket linea, DetallePromocion detalle, Tarifas tarifa);

    // aplicación de la promoción a un conjunto múltiple de líneas
    public abstract void aplicaLineasMultiple(LineasTicket lineas);

    // aplicación de la promoción a los subtotales del ticket
    public abstract void aplicaSubtotales(TotalesXML totales, LineasTicket lineas);

    public boolean isAplicableAFecha() {
        Fecha ahora = new Fecha();
        if (fechaInicio.despues(ahora) || fechaFin.antes(ahora)) {
            log.debug(toString() + ":: La fecha y hora de inicio / fin de la promoción no permite su aplicación. Fecha actual: " + ahora.getStringHoraSeg());
            log.debug(toString() + ":: Fecha inicio promoción: " + fechaInicio.getStringHora());
            log.debug(toString() + ":: Fecha fin promoción: " + fechaFin.getStringHora());
            return false;
        }
        return true;
    }

    public boolean isAplicableACliente(Cliente cliente) {
        if (!isAplicableAfiliado(cliente)) {
            return false;
        }
        // si el cliente no es socio, vemos si la promoción es aplicable para no socios
        if (!cliente.isSocio() || cliente.getTipoAfiliado() == null) {
            return isAplicableNoSocios();
        } // si el cliente es socio y la promoción aplica a todos, entonces aplica al cliente
        else if (afiliados.isEmpty()) {
            return true;
        } else { // si el cliente es socio y la promoción aplica a socios, comprobamos el tipo de socio
            if (afiliados.contains(cliente.getTipoAfiliado())) {
                return true;
            } else {
                log.debug(toString() + ":: La promoción no es aplicable para el tipo de afiliación del cliente: " + cliente.getTipoAfiliado());
                return false;
            }
        }
    }

    public boolean isAplicableAfiliado(Cliente cliente) {
        //Si la promoción es aplicable para cualquiera, no comprobamos nada
        if (isIdentificacionCualquiera()) {
            return true;
        } else if (cliente.isSocio()) {
            //Si la promoción aplica sobre los afiliados, comprobamos que se trate de un socio
            if (isIdentificacionAfiliado()) {
                return true;
            } //TarjetaDescuento (bines)
            else if (isIdentificacionTarjetaDescuento(cliente)) {
                return true;
            } else if (isIdentificacionCreditoPropio(cliente)) {
                return true;
            } else if (isIdentificacionCedula(cliente)) {
                return true;
            } else if (isIdentificacionCodigoMaestro(cliente)) {
                return true;
            }
        }
        log.debug(toString() + ":: La promoción no es aplicable para el tipo de identificación del afiliado: " + cliente.getTipoAfiliado());
        return false;
    }

    private boolean isIdentificacionCualquiera() {
        return (afiliadoIdentificacion == null || afiliadoIdentificacion.equals("") || afiliadoIdentificacion.equals("CUALQUIER"));
    }

    private boolean isIdentificacionAfiliado() {
        return isIdentificacionCualquiera() || afiliadoIdentificacion.equals("AFILIADO");
    }

    private boolean isIdentificacionTarjetaDescuento(Cliente cliente) {
        //Si se trata de Tarjeta descuento, comprobamos que la tarjeta de afiliación no sea nula y que el tipo de afiliación sea Descuento
        return cliente.getTarjetaAfiliacion() != null && cliente.getTarjetaAfiliacion().getTipoAfiliacion().equals(ITarjetaAfiliacion.TIPO_AFILIACION_TARJETA_BINES) && afiliadoIdentificacion.equals("TARJETA_DESCUENTO");
    }

    private boolean isIdentificacionCreditoPropio(Cliente cliente) {
        return cliente.getTarjetaAfiliacion() != null && cliente.getTarjetaAfiliacion().getTipoAfiliacion().equals(ITarjetaAfiliacion.TIPO_AFILIACION_CREDITO_SK) && afiliadoIdentificacion.equals("CREDITO_PROPIO");
    }

    private boolean isIdentificacionCedula(Cliente cliente) {
        return cliente.getTarjetaAfiliacion() != null && cliente.getTarjetaAfiliacion().getTipoAfiliacion().equals(ITarjetaAfiliacion.TIPO_AFILIACION_CEDULA) && afiliadoIdentificacion.equals("CEDULA");
    }

    private boolean isIdentificacionCodigoMaestro(Cliente cliente) {
        return cliente.getTarjetaAfiliacion() != null && cliente.getTarjetaAfiliacion().getTipoAfiliacion().equals(ITarjetaAfiliacion.TIPO_AFILIACION_TARJETA_TIENDA) && afiliadoIdentificacion.equals("MAESTRO");
    }

    public BigDecimal getImporteAplicableAPagos(List<Pago> pagos) {
        if (vencimientos == null) {
            return null;
        }
        BigDecimal acumulado = BigDecimal.ZERO;
        for (Pago pago : pagos) {
            if (vencimientos.contains(pago.getVencimiento().getIdMedioPagoVencimiento())) {
                acumulado = acumulado.add(pago.getTotal());
            }
        }
        return acumulado;
    }

    public boolean isAplicableAPagos(List<Pago> pagos) {
        if (vencimientos == null) {
            return true;
        }
        for (Pago pago : pagos) {
            if (!pago.getMedioPagoActivo().isCompensacion() && vencimientos.contains(pago.getVencimiento().getIdMedioPagoVencimiento())) {
                return true;
            }
        }
        log.debug(toString() + ":: Los pagos no permiten aplicar la promoción.");
        return false;
    }

    public boolean isAplicableAPagosMixtos(List<Pago> pagos, Long pagoAceptados) {
        if (vencimientos == null) {
            return true;
        }
        for (Pago pago : pagos) {
            if (!pago.getMedioPagoActivo().isCompensacion() && vencimientos.contains(pago.getVencimiento().getIdMedioPagoVencimiento())) {
                pagoAceptados = pagoAceptados + 0L;
            } else {
                pagoAceptados = pagoAceptados + 1L;
            }
        }
        if (pagoAceptados == 0L) {
            return true;
        } else {
            log.debug(toString() + ":: Los pagos no permiten aplicar la promoción.");
            return false;
        }

    }

    public boolean isAplicableAPago(Pago pago) {
        if (vencimientos == null) {
            return true;
        }
        if (vencimientos.contains(pago.getVencimiento().getIdMedioPagoVencimiento())) {
            return true;
        }
        return false;
    }

    public boolean isAplicableALineas(LineasTicket lineas) {
        if (seleccion == null) {
            return true;
        }
        boolean res;
        if (this instanceof PromocionTipoDescuentoCombinado) {
            res = seleccion.isAplicable(true, lineas);
        } else {
            res = seleccion.isAplicable(false, lineas);
        }
        if (res) {
            log.debug(toString() + ":: La selección de artículos permite aplicar la promoción.");
            return true;
        } else {
            // log.debug(toString() + ":: La selección de artículos NO permite aplicar la promoción.");
            return false;
        }
    }

    public boolean isAplicableArticulo(Articulos articulo) {
        if (seleccion == null) {
            return true;
        }
        boolean resArticulo = false;
        for (ItemSeleccionArticuloBean item : seleccion.getItemsArticulo()) {
            if (articulo.getCodart().equals(item.getCodArticulo())) {
                resArticulo = true;
                break;
            }
        }

        if (resArticulo && seleccion.isHayTipoArticulo()) {
            return true;
        }

        boolean resMarca = false;
        for (ItemSeleccionArticuloBean item : seleccion.getItemsMarca()) {
            if (articulo != null && articulo.getCodmarca() != null && articulo.getCodmarca().getCodmarca() != null && articulo.getCodmarca().getCodmarca().equals(item.getCodMarca())) {
                resMarca = true;
                break;
            }
        }
        if (!resMarca && !resArticulo) {

        }

        boolean resSeccion = false;
        for (ItemSeleccionArticuloBean item : seleccion.getItemsSeccion()) {
            if (articulo != null && articulo.getCodseccion() != null && articulo.getCodseccion().equals(item.getCodSeccion())) {
                resSeccion = true;
                break;
            }
        }
        if (!resSeccion && !resArticulo) {

        }

        boolean resColeccion = false;
        for (ItemSeleccionArticuloBean item : seleccion.getItemsColeccion()) {
            if (articulo != null && articulo.getColeccion() != null && articulo.getColeccion().equals(item.getCodColeccion())) {
                resColeccion = true;
                break;
            }
        }
        if (!resColeccion && !resArticulo) {

        }

        boolean resExcluido = true;
        for (ItemSeleccionArticuloBean item : seleccion.getItemsExcluidos()) {
            if (articulo != null && articulo.getCodart() != null && articulo.getCodart().equals(item.getCodArtExcluido())) {
                resExcluido = false;
                break;
            }
        }
        if (!resExcluido) {

        }

        boolean resSubSeccion = false;
        for (ItemSeleccionArticuloBean item : seleccion.getItemsSubSeccion()) {
            if (articulo != null && articulo.getCodsubseccion() != null && articulo.getCodsubseccion().equals(item.getCodSubseccion())) {
                resSubSeccion = true;
                break;
            }
        }
        if (!resSubSeccion && !resArticulo) {

        }

        boolean resCategoria = false;
        for (ItemSeleccionArticuloBean item : seleccion.getItemsCategoria()) {
            if (articulo != null && articulo.getCodcategoria() != null && articulo.getCodcategoria().equals(item.getCodCategoria())) {
                resCategoria = true;
                break;
            }
        }
        if (!resCategoria && !resArticulo) {

        }

        boolean resDescripcion = false;
        for (ItemSeleccionArticuloBean item : seleccion.getItemsDescripcion()) {
            if (articulo != null && articulo.getDesart().startsWith(item.getFiltroDescripcion())) {
                resDescripcion = true;
                break;
            }
        }
        if (!resDescripcion && !resArticulo) {

        }

        if (resMarca && resSeccion && resColeccion && resSubSeccion && resCategoria && resDescripcion && resExcluido) {
            return true;
        }

        return false;
    }

    public String getMensajeNoAplicablePagos() {
        return "No se ha seleccionado la forma de pago necesaria para aplicar promoción.";
    }

    public String getMensajeNoAplicableMontoMinimo() {
        return "El monto mínimo no permite aplicar la promoción: " + descripcion + ".";
    }

    private boolean isAplicableNoSocios() {
        if (afiliados == null || afiliados.isEmpty()) {
            return true;
        } else {
            log.debug(toString() + ":: La promoción no es aplicable para clientes NO SOCIOS.");
            return false;
        }
    }

    public String getCodTar() {
        return codTar;
    }

    public String getDesTipoPromocion() {
        return tipoPromocion.getDesTipoPromocion();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDescripcionImpresion() {
        String rep;
        rep = descripcion.replaceAll("&", "&amp;");
        rep = rep.replaceAll("<", "&lt;");
        rep = rep.replaceAll(">", "&gt;");
        rep = rep.replaceAll("'", "&apos;");
        rep = rep.replaceAll("\"", "&quot;");
        return rep;
    }

    public Fecha getFechaFin() {
        return fechaFin;
    }

    public Fecha getFechaInicio() {
        return fechaInicio;
    }

    public Long getIdPromocion() {
        return idPromocion;
    }

    public Long getIdTipoPromocion() {
        return tipoPromocion.getIdTipoPromocion();
    }

    public TipoPromocionBean getTipoPromocion() {
        return tipoPromocion;
    }

    public boolean isSoloFidelizacion() {
        return soloFidelizacion;
    }

    public String getTextoPromocion() {
        return textoPromocion;
    }

    public void setTextoPromocion(String textoPromocion) {
        this.textoPromocion = textoPromocion;
    }

    public void setAfiliados(List<String> afiliados) {
        this.afiliados = afiliados;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public String getAfiliadoIdentificacion() {
        return afiliadoIdentificacion;
    }

    public void setAfiliadoIdentificacion(String afiliadoIdentificacion) {
        this.afiliadoIdentificacion = afiliadoIdentificacion;
    }

    @Override
    public int compareTo(Promocion p) {
        int r = p.getPrioridad().compareTo(prioridad);
        if (r == 0) {
            return p.getFechaInicio().compareTo(fechaInicio);
        }
        return r;
    }

    public SeleccionArticuloBean getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(SeleccionArticuloBean seleccion) {
        this.seleccion = seleccion;
    }

    public List<Long> getVencimientos() {
        return vencimientos;
    }

    public void setVencimientos(List<Long> vencimientos) {
        this.vencimientos = vencimientos;
    }

    public boolean tieneFiltroPagos() {
        return vencimientos != null && !vencimientos.isEmpty();
    }

    public boolean isReaplicable(TicketS ticket) {
        return true;
    }

    public boolean isTieneFiltroPagos() {
        return tieneFiltroPagos;
    }

    public void setTieneFiltroPagos(boolean tieneFiltroPagos) {
        this.tieneFiltroPagos = tieneFiltroPagos;
    }

    public boolean isTieneFiltroPagosTarjetaSukasa() {
        return tieneFiltroPagosTarjetaSukasa;
    }

    public void setTieneFiltroPagosTarjetaSukasa(boolean tieneFiltroPagosTarjetaSukasa) {
        this.tieneFiltroPagosTarjetaSukasa = tieneFiltroPagosTarjetaSukasa;
    }

    protected void borrarAplicacion(List<LineaTicket> lineasAplicadas) {
        for (LineaTicket linea : lineasAplicadas) {
            linea.setPromocionLinea(null);
        }
    }

    public String getFiltroPagosDescripcion() {
        if (filtroPagosDescripcion == null) {
            filtroPagosDescripcion = "";
            Map<MedioPagoBean, List<VencimientoBean>> filtroPago = new HashMap<MedioPagoBean, List<VencimientoBean>>();
            for (Long idVencimiento : vencimientos) {
                VencimientoBean vencimiento = MediosPago.getInstancia().getVencimiento(idVencimiento);
                MedioPagoBean medioPago = MediosPago.getInstancia().getMedioPago(vencimiento.getCodMedioPago());
                List<VencimientoBean> filtroVencimientos = filtroPago.get(medioPago);
                if (filtroVencimientos == null) {
                    filtroVencimientos = new ArrayList<VencimientoBean>();
                    filtroPago.put(medioPago, filtroVencimientos);
                }
                filtroVencimientos.add(vencimiento);
            }
            for (MedioPagoBean medioPagoFiltro : filtroPago.keySet()) {
                filtroPagosDescripcion += "- " + medioPagoFiltro.getDesMedioPago() + "";
                int numPlanes = medioPagoFiltro.getPlanes().size();
                List<VencimientoBean> filtroVencimientos = filtroPago.get(medioPagoFiltro);
                if (numPlanes > 1 && numPlanes > filtroVencimientos.size()) { // si el medio de pago tiene cuotas y no están todas seleccionadas, las pintamos
                    Collections.sort(filtroVencimientos);
                    filtroPagosDescripcion += "      (";
                    for (int i = 0; i < filtroVencimientos.size(); i++) {
                        VencimientoBean vencimientoFiltro = filtroVencimientos.get(i);
                        if (i > 0 && i < filtroVencimientos.size()) {
                            filtroPagosDescripcion += ", ";
                        }
                        filtroPagosDescripcion += vencimientoFiltro.getDesVencimiento();
                    }
                    filtroPagosDescripcion += ")\n";
                }

            }
        }
        return filtroPagosDescripcion;
    }

    public int getSaltosLineaFiltrosPagosDescrpcion() {
        if (filtroPagosDescripcion == null) {
            return 0;
        }
        return Cadena.contarCaracter(filtroPagosDescripcion, '\n');
    }
    
    public boolean isSoloPromoPermanente() {
        return soloPromoPermanente;
    }

    public void setSoloPromoPermanente(boolean soloPromoPermanente) {
        this.soloPromoPermanente = soloPromoPermanente;
    }

}
