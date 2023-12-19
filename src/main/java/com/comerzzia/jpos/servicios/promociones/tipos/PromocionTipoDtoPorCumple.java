/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.tipos;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.tickets.componentes.DescuentoTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocion;
import com.comerzzia.jpos.servicios.promociones.totales.PromocionTotal;
import com.comerzzia.jpos.servicios.promociones.totales.TotalesEnPromocion;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;

import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author amos
 */
public class PromocionTipoDtoPorCumple extends PromocionTotal {

    private int intervalo;
    private String tipo;
    private BigDecimal descuento;
    private String textoDetallePromocion;
    private List<String> secciones;
    private List<String> articulos;

    public PromocionTipoDtoPorCumple(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
        // Obtenemos el nodo cabecera
        XMLDocumentNode cabecera = xml.getNodo("cabecera");

        // Obtenemos los valores 
        descuento = cabecera.getNodo("descuento").getValueAsBigDecimal();
        tipo = cabecera.getNodo("tipo").getValue();
        intervalo = cabecera.getNodo("intervalo").getValueAsInteger();
        textoDetallePromocion = cabecera.getNodo("textoPromocion").getValue();

        // Obtenemos el nodo detalles para obtener los hijos
        XMLDocumentNode detalles = xml.getNodo("detalles");
        XMLDocumentNode detalle = detalles.getNodo("detalle", true);

        // Obtenemos el nodo articulos para obtener los hijos
        XMLDocumentNode articulosXML = detalle.getNodo("articulos", true);
        // Obtenemos el nodo secciones para obtener los hijos
        XMLDocumentNode seccionesXML = detalle.getNodo("secciones", true);

        if (articulosXML != null && articulosXML.getHijos().size() > 0) {//se trata de articulos
            articulos = new ArrayList<String>(articulosXML.getHijos().size());
            for (XMLDocumentNode articulo : articulosXML.getHijos()) {
                articulos.add(articulo.getNodo("codArticulo").getValue());
            }
        }
        else if (seccionesXML != null && seccionesXML.getHijos().size() > 0) {
            secciones = new ArrayList<String>(seccionesXML.getHijos().size());
            for (XMLDocumentNode seccion : seccionesXML.getHijos()) {
                secciones.add(seccion.getNodo("codSeccion").getValue());
            }
        }
        // Configuramos que aplica a todas las líneas con cualquier importe
        setAplicaALineasConPromocion(true);
        TotalesEnPromocion.getInstance().addTotalEnPromocion(BigDecimal.ZERO, this);
    }

    @Override
    public boolean isAplicableACliente(Cliente cliente) {
        boolean aplicable = super.isAplicableACliente(cliente);
        if (!aplicable) {
            return false;
        }
        // tenemos que comprobar que el cumpleaños del cliente o del bebé está dentro del intervalo indicado
        Date nacimiento;
        if (tipo.equals("BEBE")) {
            nacimiento = cliente.getFechaNacimientoUltimoHijo();
        }
        else { // CUMPLEAÑOS CLIENTE
            nacimiento = cliente.getFechaNacimiento();
        }
        if (nacimiento == null) {
            return false;
        }
        Fecha fechaNacimiento = new Fecha(nacimiento);
        Fecha fechaHoy = new Fecha();
        int diaNacimiento = fechaNacimiento.getDia();
        int mesNacimiento = fechaNacimiento.getMes();
        int diaHoy = fechaHoy.getDia();
        int mesHoy = fechaHoy.getMes();
        if (mesNacimiento != mesHoy) {
            return false;
        }
        if (Math.abs(diaNacimiento - diaHoy) > intervalo) {
            return false;
        }
        return true;
    }

    @Override
    public void aplicaLineaUnitaria(TicketS ticket, LineaTicket linea, DetallePromocion detallePromocion, Tarifas tarifa) {
    }

    @Override
    public BigDecimal calculaDtoLineaUnitaria(LineaTicket linea, DetallePromocion detalle, Tarifas tarifa) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void aplicaLineasMultiple(LineasTicket lineas) {
    }

    @Override
    public void aplicaSubtotales(TotalesXML totales, LineasTicket lineas) {
        BigDecimal monto = null;
        // vamos a calcular sobre qué tenemos que aplicar el descuento en función de la configuración
        List<LineaTicket> lineasAplicables = new ArrayList<LineaTicket>();
        if (articulos != null) {
            for (String codArticulo : articulos) {
                lineas.getContains(false, codArticulo, lineasAplicables);
            }
        }
        else if (secciones != null) {
            for (String codSeccion : secciones) {
                lineas.getContainsSecciones(false, codSeccion, lineasAplicables);
            }
        }
        else { // aplica a todas las líneas
            monto = totales.getTotalAPagar();
        }

        // si no tenemos líneas sobre las que aplicar la promoción, no hacemos nada
        if (monto == null && lineasAplicables.isEmpty()) {
            return;
        }

        // Calculamos el monto de las líneas aplicables, a no ser que ya tengamos el monto total porque aplique a todo
        if (monto == null) {
            for (LineaTicket lineaTicket : lineasAplicables) {
                monto = monto.add(lineaTicket.getImporteTotal());
            }
        }

        PromocionLineaTicket promocionTicket = new PromocionLineaTicket(this, PromocionLineaTicket.PROMO_SUBTOTAL);
        promocionTicket.setTextoPromocion(getTextoDetallePromocion());
        promocionTicket.setCantidadPromocion(0);

        BigDecimal ahorro = Numero.porcentajeR(monto, descuento);
        promocionTicket.setImporteTotalPromocion(ahorro);
        promocionTicket.setImportesAhorro(ahorro, ahorro); // sólo se calcula con iva
        DescuentoTicket dto = new DescuentoTicket(getTextoDetallePromocion(), ahorro, ahorro);
        promocionTicket.setDescuentoTicket(dto);
        totales.addPromocionATotal(promocionTicket);
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public String getTextoDetallePromocion() {
        if (textoDetallePromocion == null || textoDetallePromocion.isEmpty()) {
            return "Descuento";
        }
        return textoDetallePromocion;
    }
}
