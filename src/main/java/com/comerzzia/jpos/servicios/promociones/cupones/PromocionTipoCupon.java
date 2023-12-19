/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones.cupones;

import com.comerzzia.jpos.entity.db.Cupon;
import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.persistencia.promociones.combos.ComboArticuloCantidadBean;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.LineasTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TotalesXML;
import com.comerzzia.jpos.servicios.promociones.Promocion;
import com.comerzzia.jpos.servicios.promociones.PromocionException;
import com.comerzzia.jpos.servicios.promociones.articulos.RangosPromocion;
import com.comerzzia.jpos.servicios.promociones.articulos.SeleccionArticuloBean;
import com.comerzzia.jpos.servicios.promociones.detalles.DetallePromocion;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.xml.XMLDocument;
import es.mpsistemas.util.xml.XMLDocumentException;
import es.mpsistemas.util.xml.XMLDocumentNode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amos
 */
public abstract class PromocionTipoCupon extends Promocion {

    protected String textoLegales;
    protected Integer diasCaducidad;
    protected ConfigEmisionCupones configEmision;

    public PromocionTipoCupon(PromocionBean promocion) throws PromocionException {
        super(promocion);
    }

    @Override
    public void aplicaLineaUnitaria(TicketS ticket, LineaTicket linea, DetallePromocion detallePromocion, Tarifas tarifa) {
        throw new UnsupportedOperationException("No soportado para este tipo de promoción");
    }

    @Override
    public BigDecimal calculaDtoLineaUnitaria(LineaTicket linea, DetallePromocion detalle, Tarifas tarifa) {
        throw new UnsupportedOperationException("No soportado para este tipo de promoción");
    }

    @Override
    public void aplicaLineasMultiple(LineasTicket lineas) {
        throw new UnsupportedOperationException("No soportado para este tipo de promoción");
    }

    @Override
    public void aplicaSubtotales(TotalesXML totales, LineasTicket lineas) {
        throw new UnsupportedOperationException("No soportado para este tipo de promoción");
    }

    @Override
    protected void parsearXMLDatosPromocion(XMLDocument xml) throws XMLDocumentException {
            XMLDocumentNode detalle = xml.getNodo("detalles").getNodo("detalle");

            /*********************************** IMPRESION **********************************/
            XMLDocumentNode impresion = detalle.getNodo("impresion", true) != null ? detalle.getNodo("impresion") : null;
            XMLDocumentNode parametrosImpresion = impresion.getNodo("parametros", true) != null ? impresion.getNodo("parametros") : null;

            // obtenemos el tipo
            String tipo = impresion.getNodo("tipo", false).getValue();
            configEmision = new ConfigEmisionCupones(this);
            configEmision.setTipoImpresion(tipo);
            
            try{
                Integer cadencia = Integer.valueOf(parametrosImpresion.getNodo("cadencia", false).getValue());
                configEmision.setCadencia(cadencia == null ? 0 : cadencia);
            }
            catch(Exception e){
                configEmision.setCadencia(0);
            }

            // MANUAL
            if (configEmision.isTipoManual()) {
                configEmision.setNumero(Integer.valueOf(parametrosImpresion.getNodo("numero", false).getValue()));
                configEmision.setCondicion(parametrosImpresion.getNodo("condicion", false).getValue());
            }
            // IMPRESION BASICA
            else if (configEmision.isTipoAutomaticaBasica()) {
                configEmision.setMontoMinimo(parametrosImpresion.getNodo("montoMinimo", false).getValueAsBigDecimal());
                configEmision.setCantidadMinima(Integer.valueOf(parametrosImpresion.getNodo("cantidadMinima", false).getValue()));
                configEmision.setCodMarca(parametrosImpresion.getNodo("codMarca", false).getValue());
                configEmision.setCodSeccion(parametrosImpresion.getNodo("codSeccion", false).getValue());
                configEmision.setCodSubseccion(parametrosImpresion.getNodo("codSubseccion", false).getValue());
                configEmision.setCodCategoria(parametrosImpresion.getNodo("codCategoria", false).getValue());
            }
            // IMPRESION BASICA SORTEO
            else if (configEmision.isTipoAutomaticaBasicaSorteo()) {
                configEmision.setMontoMinimo(parametrosImpresion.getNodo("montoMinimo", false).getValueAsBigDecimal());
                configEmision.setCantidadMinima(Integer.valueOf(parametrosImpresion.getNodo("cantidadMinima", false).getValue()));
                configEmision.setCodMarca(parametrosImpresion.getNodo("codMarca", false).getValue());
                configEmision.setCodSeccion(parametrosImpresion.getNodo("codSeccion", false).getValue());
                configEmision.setCodSubseccion(parametrosImpresion.getNodo("codSubseccion", false).getValue());
                configEmision.setCodCategoria(parametrosImpresion.getNodo("codCategoria", false).getValue());
                configEmision.setMontoMaximo(parametrosImpresion.getNodo("montoMaximo", false).getValueAsBigDecimal());
                configEmision.setMontoFraccion(parametrosImpresion.getNodo("cantidad", false).getValueAsBigDecimal());
            }
            // IMPRESION BASICA VOTOS
            else if (configEmision.isTipoAutomaticaBasicaVotos()) {
                configEmision.setMontoMinimo(parametrosImpresion.getNodo("montoMinimo", false).getValueAsBigDecimal());
                configEmision.setCantidadMinima(Integer.valueOf(parametrosImpresion.getNodo("cantidadMinima", false).getValue()));
                configEmision.setCodMarca(parametrosImpresion.getNodo("codMarca", false).getValue());
                configEmision.setCodSeccion(parametrosImpresion.getNodo("codSeccion", false).getValue());
                configEmision.setCodSubseccion(parametrosImpresion.getNodo("codSubseccion", false).getValue());
                configEmision.setCodCategoria(parametrosImpresion.getNodo("codCategoria", false).getValue());
                configEmision.setMontoMaximo(parametrosImpresion.getNodo("montoMaximo", false).getValueAsBigDecimal());
                configEmision.setMontoFraccion(parametrosImpresion.getNodo("cantidad", false).getValueAsBigDecimal());
                configEmision.setNumVotos(parametrosImpresion.getNodo("numeroVotos", false).getValueAsInteger());
            }
            // AUTOMATICA HISTORICO
            else if (configEmision.isTipoAutomaticaHistorico()) {
                configEmision.setNumComprasMinimo(Integer.valueOf(parametrosImpresion.getNodo("numComprasMinimo", false).getValue()));
                configEmision.setMontoComprasMinimo(parametrosImpresion.getNodo("montoComprasMinimo", false).getValueAsBigDecimal());
            }
            // AUTOMATICA CUMPLEAÑOS
            else if (configEmision.isTipoAutomaticaCumpleaños()) {
                configEmision.setDiasIntervalo(Integer.valueOf(parametrosImpresion.getNodo("diasIntervalo", false).getValue()));
                configEmision.setTipo(parametrosImpresion.getNodo("tipo", false).getValue());
            }
            // AUTOMATICA_ARTICULOS
            else if (configEmision.isTipoAutomaticaArticulos()) {
                XMLDocumentNode articulos = parametrosImpresion.getNodo("articulos");
                List<String> listaCodigosArticulos = new ArrayList<String>();
                for (XMLDocumentNode articulo : articulos.getHijos()) {
                    listaCodigosArticulos.add(articulo.getValue());
                }
                configEmision.setListaCodigosArticulos(listaCodigosArticulos);
            }
            // AUTOMATICA_COMBO_SECCION
            else if (configEmision.isTipoAutomaticaComboSeccion()) {
                XMLDocumentNode combos = parametrosImpresion.getNodo("combos");
                List<ComboArticuloCantidadBean> listaCombos = new ArrayList<ComboArticuloCantidadBean>();
                for (XMLDocumentNode combo : combos.getHijos()) {
                    ComboArticuloCantidadBean comboCuponBean = new ComboArticuloCantidadBean();
                    comboCuponBean.setCodigo(combo.getNodo("codSeccion", false).getValue());
                    comboCuponBean.setCantidad(Integer.valueOf(combo.getNodo("cantidad", false).getValue()));
                    listaCombos.add(comboCuponBean);
                }
                configEmision.setListaCombos(listaCombos);
            }
            // AUTOMATICA_BASICA_REGALO_PROVEEDOR_EXTERNO
            else if (configEmision.isTipoAutomaticaBasicaRegaloProveedorExterno()) {
                configEmision.setMontoMinimo(parametrosImpresion.getNodo("montoMinimo", false).getValueAsBigDecimal());
                configEmision.setCuponProveedorExterno(parametrosImpresion.getNodo("cuponProveedorExterno", false).getValueAsLong());
                
                // número de cupones por el total de la factura
                String numCupones = parametrosImpresion.getNodo("numCupones", true).getValue();
                if(numCupones != null && !numCupones.isEmpty()){
                    configEmision.setNumCupones(Integer.valueOf(numCupones));
                }
                // intervalo de dólares por los que daremos un cupón cada intervalo
                String cantidad = parametrosImpresion.getNodo("cantidad", true).getValue();
                if(cantidad != null && !cantidad.isEmpty()){
                    configEmision.setCantidad(Integer.valueOf(cantidad));
                }
                seleccion =  new SeleccionArticuloBean(detalle.getNodo("impresion").getNodo("parametros"));
                configEmision.setFechaValidez(new Fecha(parametrosImpresion.getNodo("fechaValidez").getValue(), Fecha.PATRON_FECHA_HORA));
            }
            // SORTEO SUKASA
            else if (configEmision.isTipoAutomaticaBasicaSorteoSukasa()) {
                configEmision.setMontoMinimo(parametrosImpresion.getNodo("montoMinimo", false).getValueAsBigDecimal());
                
                // número de cupones por el total de la factura
                String numCupones = parametrosImpresion.getNodo("numCupones", true).getValue();
                if(numCupones != null && !numCupones.isEmpty()){
                    configEmision.setNumCupones(Integer.valueOf(numCupones));
                }
                // intervalo de dólares por los que daremos un cupón cada intervalo
                String cantidad = parametrosImpresion.getNodo("cantidad", true).getValue();
                if(cantidad != null && !cantidad.isEmpty()){
                    configEmision.setCantidad(Integer.valueOf(cantidad));
                }
                seleccion =  new SeleccionArticuloBean(detalle.getNodo("impresion").getNodo("parametros"));
                configEmision.setListSeleccion(seleccion);
                configEmision.setFechaValidez(new Fecha(parametrosImpresion.getNodo("fechaValidez").getValue(), Fecha.PATRON_FECHA_HORA));
            }
             // AUTOMATICA_BASICA_REGALO_CURSO
            else if (configEmision.isTipoAutomaticaBasicaRegaloCurso()) {
                configEmision.setMontoMinimo(parametrosImpresion.getNodo("montoMinimo", false).getValueAsBigDecimal());
                String a = parametrosImpresion.toString();
                a.toCharArray();
                String conAcompanante = parametrosImpresion.getNodo("conAconpanante", true).getValue();
                if(conAcompanante != null && !conAcompanante.isEmpty() && conAcompanante.equals("S")){
                    configEmision.setConAcompanante(true);
                }
                else{
                    configEmision.setConAcompanante(false);
                }
                rangos = new RangosPromocion(parametrosImpresion);
                rangos.ajustaRangoMinimo(configEmision.getMontoMinimo());
                seleccion =  new SeleccionArticuloBean(detalle.getNodo("impresion").getNodo("parametros"));
                configEmision.setFechaValidez(new Fecha(parametrosImpresion.getNodo("fechaValidez").getValue(), Fecha.PATRON_FECHA_HORA));

            }
            else if (configEmision.isTipoAutomaticaBasicaBilleton()) {
                XMLDocumentNode precioMinimo = parametrosImpresion.getNodo("precioMinimo");
                configEmision.setMontoMinimo(precioMinimo.getValueAsBigDecimal());
                configEmision.setMontoComprasMinimo(parametrosImpresion.getNodo("montoMinimo", false).getValueAsBigDecimal());
                seleccion =  new SeleccionArticuloBean(detalle.getNodo("impresion").getNodo("parametros"));
                configEmision.setTipo(parametrosImpresion.getNodo("tipoConfiguracion").getValue());
                configEmision.setFechaValidez(new Fecha(parametrosImpresion.getNodo("fechaValidez").getValue(), Fecha.PATRON_FECHA_HORA));
            }

            /*********************************** DATOS GENERALES **********************************/
            textoLegales = detalle.getNodo("legales", false).getValue();
            setTextoPromocion(detalle.getNodo("textoPromocion", false).getValue());
            try{
                diasCaducidad = detalle.getNodo("caducidad", false).getValueAsInteger();
            }
            catch(Exception e){
                diasCaducidad = Variables.CUPON_CADUCIDAD_DEFAULT; // Ponemos variable por defecto
            }
            
    }

    public ConfigEmisionCupones getConfigEmision() {
        return configEmision;
    }

    public Integer getDiasCaducidad() {
        return diasCaducidad;
    }

    public String getTextoLegales() {
        return textoLegales;
    }

    public abstract List<Cupon> emiteCupones(TicketS ticket,ConfigEmisionCupones configEmision);
    
    public abstract void aplicaCupon(TicketS ticket, Cupon cupon) throws CuponNotValidException;
    
    public Fecha getFechaValidez(){
        Fecha fecha = new Fecha();
        fecha.sumaDias(diasCaducidad);
        return fecha;
    }
    
    public String getMensajeNoAplicableMontoMinimo(Promocion promocion){
        return "El monto mínimo no permite emitir cupones a la promoción: "+promocion.toString()+".";
    }
    
}
