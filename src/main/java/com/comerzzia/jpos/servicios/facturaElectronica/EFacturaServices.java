/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.facturaElectronica;

import com.comerzzia.jpos.entity.db.ConfigImpPorcentaje;
import com.comerzzia.jpos.entity.db.Impuestos;
import com.comerzzia.jpos.persistencia.facturaElectronica.CampoAdicionalBean;
import com.comerzzia.jpos.persistencia.facturaElectronica.DetalleBean;
import com.comerzzia.jpos.persistencia.facturaElectronica.ImpuestoBean;
import com.comerzzia.jpos.persistencia.facturaElectronica.InfoTributariaBean;
import com.comerzzia.jpos.persistencia.facturaElectronica.TotalImpuestoBean;
import com.comerzzia.jpos.persistencia.facturaElectronica.efactura.CompensacionBean;
import com.comerzzia.jpos.persistencia.facturaElectronica.efactura.FacturaElectronicaBean;
import com.comerzzia.jpos.persistencia.facturaElectronica.efactura.InfoFacturaBean;
import com.comerzzia.jpos.persistencia.facturaElectronica.efactura.PagoBean;
import com.comerzzia.jpos.servicios.core.impuestos.ImpuestosServices;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.util.marshall.MarshallUtil;
import com.comerzzia.util.marshall.MarshallUtilException;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.xml.XMLDocument;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.Pago;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.util.enums.EnumCodigoImpuestos;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SMLM
 */
public class EFacturaServices {

    private static final Logger log = Logger.getMLogger(EFacturaServices.class);
    private static BigDecimal precioIce = BigDecimal.ZERO;
    ImpuestosServices impuestosServices = ImpuestosServices.getInstance();

    public static XMLDocument generaFacturaElectronica(TicketS ticket) throws FacturaElectronicaException {
        XMLDocument xml = null;
        try {
            FacturaElectronicaBean factura = new FacturaElectronicaBean();
            generarFactura(factura, ticket);
            byte[] fac = MarshallUtil.crearXML(factura, FacturaElectronicaBean.class);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            xml = new XMLDocument(builder.parse(new ByteArrayInputStream(fac)));
            log.debug("XML Factura Electrónica generado: " + xml.toString());
        } catch (MarshallUtilException e) {
            log.error("generaFacturaElectronica() - Error generando la factura electrónica: " + e, e);
            throw new FacturaElectronicaException(e);
        } catch (SAXException e) {
            log.error("generaFacturaElectronica() - Error generando la factura electrónica: " + e, e);
            throw new FacturaElectronicaException(e);
        } catch (IOException e) {
            log.error("generaFacturaElectronica() - Error generando la factura electrónica: " + e, e);
            throw new FacturaElectronicaException(e);
        } catch (ParserConfigurationException e) {
            log.error("generaFacturaElectronica() - Error generando la factura electrónica: " + e, e);
            throw new FacturaElectronicaException(e);
        } catch (Exception e) {
            log.error("generaFacturaElectronica() - Error generando la factura electrónica: " + e, e);
            throw new FacturaElectronicaException(e);
        }
        return xml;
    }

    private static void generarFactura(FacturaElectronicaBean factura, TicketS ticket) throws FacturaElectronicaException {
        factura.setInfoTributaria(generarInfoTributaria(ticket.getId_ticket(), "FA"));
        factura.setInfoFactura(generarInfoFactura(factura, ticket));
        factura.setDetalles(getDetalles(ticket, "FA"));
        if (getInfoAdicionales(ticket).size() > 0) {
            factura.setInfoAdicional(getInfoAdicionales(ticket));
        }
    }

    public static InfoTributariaBean generarInfoTributaria(Long idTicket, String tipo) {
        InfoTributariaBean infoTributaria = new InfoTributariaBean();
        //TODO : Ver como hacemos el ambiente y la emision configurable
        infoTributaria.setAmbiente(VariablesAlm.getVariableAsBoolean(VariablesAlm.FACT_ELECT_CONFIG_AMBIENTE) ? ConstantesEFacturacion.AMBIENTE_PRODUCCION : ConstantesEFacturacion.AMBIENTE_PRUEBA);
        infoTributaria.setTipoEmision(ConstantesEFacturacion.EMISION_NORMAL);
        infoTributaria.setRazonSocial(Sesion.getEmpresa().getDesemp());
        infoTributaria.setNombreComercial(Sesion.getEmpresa().getNombreComercial());
        infoTributaria.setRuc(Sesion.getEmpresa().getCif());
        infoTributaria.setClaveAcceso(Numero.completaconCeros("", 49));
        if (tipo.equals("FA")) {
            infoTributaria.setCodDoc(ConstantesEFacturacion.COMPROBANTE_FACTURA);
        } else if (tipo.equals("NC")) {
            infoTributaria.setCodDoc(ConstantesEFacturacion.COMPROBANTE_NOTACREDITO);
        }
        infoTributaria.setEstab(Sesion.getTienda().getCodalmSRI());
        infoTributaria.setPtoEmi(Sesion.getTienda().getSriTienda().getCajaActiva().getCodcajaSri());
        infoTributaria.setSecuencial(Numero.completaconCeros(String.valueOf(idTicket), 9));
        infoTributaria.setDirMatriz(Sesion.getEmpresa().getDomicilio());

        return infoTributaria;
    }

    private static InfoFacturaBean generarInfoFactura(FacturaElectronicaBean factura, TicketS ticket) throws FacturaElectronicaException {
        InfoFacturaBean infoFactura = new InfoFacturaBean();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        infoFactura.setFechaEmision(df.format(ticket.getFecha().getDate()));
        infoFactura.setDirEstablecimiento(Sesion.getTienda().getAlmacen().getDomicilio());
        infoFactura.setContribuyenteEspecial("5368");
        infoFactura.setObligadoContabilidad("SI");
        String identificacion = ticket.getFacturacion().getTipoDocumento();
        String codigoTipoIdentificacion = null;
        String codigoIdentificacion = ticket.getFacturacion().getDocumento();
        String razonSocial = ticket.getFacturacion().getNombre() + " " + ticket.getFacturacion().getApellidos();

        if (identificacion.equals("RUJ") || identificacion.equals("RUN") || identificacion.equals("RUC")) {
            codigoTipoIdentificacion = ConstantesEFacturacion.IDENTIFICACION_RUC;
        } else if (identificacion.equals("CED")) {
            if (codigoIdentificacion.equals("00")) {
                codigoTipoIdentificacion = ConstantesEFacturacion.IDENTIFICACION_CONSUMIDOR_FINAL;
                codigoIdentificacion = "9999999999999";
                razonSocial = "CONSUMIDOR FINAL";
            } else {
                codigoTipoIdentificacion = ConstantesEFacturacion.IDENTIFICACION_CEDULA;
            }
        } else if (identificacion.equals("PAS")) {
            codigoTipoIdentificacion = ConstantesEFacturacion.IDENTIFICACION_PASAPORTE;
        } else {
            log.error("generarInfoFactura() - La identificacion del cliente no es de ninguno de los tipos soportados");
            throw new FacturaElectronicaException("La identificacion del cliente no es de ninguno de los tipos soportados");
        }
        infoFactura.setTipoIdentificacionComprador(codigoTipoIdentificacion);
        infoFactura.setRazonSocialComprador(razonSocial);
        infoFactura.setIdentificacionComprador(codigoIdentificacion);
        infoFactura.setDireccionComprador(ticket.getFacturacion().getDireccion());
        BigDecimal sinImp = ticket.getTotales().getBase().subtract(ticket.getTotales().getImpuestosIce());
//        infoFactura.setTotalSinImpuestos(ticket.getTotales().getBase().doubleValue());
        if (sinImp.compareTo(BigDecimal.ZERO) > 0) {
//            infoFactura.setTotalSinImpuestos(sinImp.doubleValue());
            infoFactura.setTotalSinImpuestos(ticket.getTotales().getBase().doubleValue());
        } else {
            infoFactura.setTotalSinImpuestos(BigDecimal.ZERO.doubleValue());
        }
//        infoFactura.setTotalSinImpuestos(sinImp.doubleValue());
        infoFactura.setTotalDescuento(ticket.getTotales().getTotalDescuentoFinalElectronico().doubleValue());
        infoFactura.setTotalConImpuestos(construirTagConImpuestos(ticket));
        if (ticket.getPagos().isCompensacionAplicada()) {
            infoFactura.setCompensaciones(construirTagCompensaciones(ticket));
        }
        infoFactura.setPropina(ConstantesEFacturacion.PROPINA);
        infoFactura.setImporteTotal(ticket.getTotales().getTotalPagado().doubleValue());
        infoFactura.setMoneda(ConstantesEFacturacion.MONEDA_DOLAR);
        infoFactura.setPagos(construirTagPagos(ticket));

        return infoFactura;
    }

    public static List<TotalImpuestoBean> construirTagConImpuestos(TicketS ticket) {
        List<TotalImpuestoBean> listaImpuestos = new ArrayList<TotalImpuestoBean>();

        if (ticket.getTotales().getSubtotal0().compareTo(BigDecimal.ZERO) > 0) {
            TotalImpuestoBean totalImpuesto = new TotalImpuestoBean();
            totalImpuesto.setCodigo(ConstantesEFacturacion.TIPO_IVA);
            totalImpuesto.setCodigoPorcentaje(ConstantesEFacturacion.PORCENTAJE_0);
            totalImpuesto.setBaseImponible(String.valueOf(ticket.getTotales().getSubtotal0().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
            totalImpuesto.setValor(ConstantesEFacturacion.VALOR_EXENTO);
            listaImpuestos.add(totalImpuesto);
        }
        if (ticket.getTotales().getSubtotal12().compareTo(BigDecimal.ZERO) > 0) {
            TotalImpuestoBean totalImpuesto = new TotalImpuestoBean();
            totalImpuesto.setCodigo(ConstantesEFacturacion.TIPO_IVA);
            if (Sesion.getEmpresa().getPorcentajeIva().setScale(2, RoundingMode.HALF_UP).equals(new BigDecimal(ConstantesEFacturacion.TARIFA_IMP_14).setScale(2, RoundingMode.HALF_UP))) {
                totalImpuesto.setCodigoPorcentaje(ConstantesEFacturacion.PORCENTAJE_14);
            } else {
                totalImpuesto.setCodigoPorcentaje(ConstantesEFacturacion.PORCENTAJE_12);
            }
            totalImpuesto.setBaseImponible(String.valueOf(ticket.getTotales().getSubtotal12().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
            totalImpuesto.setValor(ticket.getTotales().getSubtotalesImpuestos().get(ConfigImpPorcentaje.COD_IMPUESTO_NORMAL).getImpuestos().doubleValue());
            listaImpuestos.add(totalImpuesto);
        }
        if (ticket.getTotales().getBaseImponibleIce() != null) {
            TotalImpuestoBean totalImpuesto = new TotalImpuestoBean();
            totalImpuesto.setCodigo(ConstantesEFacturacion.TIPO_ICE);
            totalImpuesto.setCodigoPorcentaje(EnumCodigoImpuestos.IMPUESTO_ICE.getCodigo().intValue());
//            BigDecimal sinImp = ticket.getTotales().getBase().subtract(ticket.getTotales().getImpuestosIce());
            totalImpuesto.setBaseImponible(String.valueOf(ticket.getTotales().getBaseImponibleIce()));
            totalImpuesto.setValor(ticket.getTotales().getImpuestosIce().doubleValue());
            listaImpuestos.add(totalImpuesto);
        }
        return listaImpuestos;
    }

    public static List<DetalleBean> getDetalles(TicketS ticket, String tipo) throws FacturaElectronicaException {
        List<DetalleBean> detalles = new ArrayList<DetalleBean>();
        BigDecimal valorNegativo = BigDecimal.ZERO;

        for (LineaTicket linea : ticket.getLineas().getLineas()) {
            int i3 = linea.getImporteFinalPagado().signum();
            if (i3 < 0) {
                linea.setImporteFinalPagado(valorNegativo);
            }
            DetalleBean detalle = new DetalleBean();
            if (tipo.equals("FA")) {

                //cambios en descripcion 
                String desMarca = " Marca: ";
                String desModelo = " Modelo: ";
                String garantia = " GARANTIA: ";
                String meses = " meses ";
                if (linea.getArticulo().getCodmarca().getCodmarca().equals("1533")) {
                    if (linea.getArticulo().getPmp() == null) {
                        detalle.setCodigoPrincipal("ICE-FPE-03");
                        detalle.setCodigoAuxiliar(linea.getCodigoBarras());
                        detalle.setDescripcion("FUNDA/BOLSA PLÁSTICA EXCENTA");
                    } else {
                        detalle.setCodigoPrincipal("ICE-FPN-01");
                        detalle.setCodigoAuxiliar(linea.getCodigoBarras());
                        detalle.setDescripcion("FUNDA/BOLSA PLÁSTICA");
                    }
                } else {
                    detalle.setCodigoPrincipal(linea.getArticulo().getCodart());
                    detalle.setCodigoAuxiliar(linea.getCodigoBarras());
                    if (linea.getArticulo().getGarantiaOriginal() != null) {
                        String detalleArtiulo = linea.getArticulo().getDesart().concat(desMarca).concat(linea.getArticulo().getCodmarca().getDesmarca()).concat(desModelo).concat(linea.getArticulo().getModelo().concat(garantia).concat(String.valueOf(linea.getArticulo().getGarantiaOriginal())).concat(meses));
                        ////            String detalleArticuloResplazoAmp=detalleArtiulo.replace("&","&amp;"); 
//            detalle.setDescripcion(linea.getArticulo().getDesart());
                        detalle.setDescripcion(detalleArtiulo);
////            detalle.setDescripcion(detalleArticuloResplazoAmp);
                    } else {
                        String detalleArtiulo = linea.getArticulo().getDesart().concat(desMarca).concat(linea.getArticulo().getCodmarca().getDesmarca()).concat(desModelo).concat(linea.getArticulo().getModelo());
                        detalle.setDescripcion(detalleArtiulo);
                    }

                }
            } else if (tipo.equals("NC")) {
                detalle.setCodigoInterno(linea.getArticulo().getCodart());
                detalle.setCodigoAdicional(linea.getCodigoBarras());
            } else {
                log.error("getDetalles() - El tipo no es de los soportados por la facturación electrónica.");
                throw new FacturaElectronicaException("El tipo no es de los soportados por la facturación electrónica");
            }

            detalle.setCantidad(linea.getCantidad());
            if (linea.getArticulo().getPmp() == null) {
                detalle.setPrecioUnitario(Numero.redondear(linea.getPrecioTarifaOrigen()).doubleValue());
            } else {
                detalle.setPrecioUnitario(precioIce.doubleValue());
            }

//            if (linea.getArticulo().getPmp() == null) {
            if (!linea.getArticulo().getCodmarca().getCodmarca().equals("1533")) {
                if (Numero.isMayorACero(linea.getImporteDescuentoFinal())) {
                    detalle.setDescuento(linea.getImporteDescuentoFinal().abs().doubleValue());
                } else {
                    detalle.setDescuento(0d);
                }
                detalle.setPrecioTotalSinImpuesto(Numero.redondear(linea.getImporteFinalPagado()).doubleValue());
                detalle.setImpuestos(getImpuestos(ticket, linea));
            } else {

                BigDecimal precioSinImpuestos = BigDecimal.ZERO;
                if (linea.getArticulo().getPmp() != null) {
                    detalle.setDescuento(0d);
                    detalle.setPrecioTotalSinImpuesto(precioSinImpuestos.doubleValue());
                    detalle.setImpuestos(getImpuestosIce(ticket, linea));
                } else {
                    detalle.setDescuento(linea.getImporteDescuentoFinal().abs().doubleValue());
                    detalle.setPrecioTotalSinImpuesto(Numero.redondear(linea.getImporteFinalPagado()).doubleValue());
                    detalle.setImpuestos(getImpuestosIce(ticket, linea));
                }

            }

            detalles.add(detalle);
        }
        return detalles;
    }

    public static List<CampoAdicionalBean> getInfoAdicionales(TicketS ticket) {
        //CAMBIO SE REALIZA PARA YA NO ENVIAR ARCHIVO DE ACTUALIZACION DE CORREO A SERES
        //   List<CampoAdicionalBean> camposAdicionales = null;
        /*if(ticket.getPagos().isCompensacionAplicada()){
            camposAdicionales = new ArrayList<CampoAdicionalBean>();
            CampoAdicionalBean campoCompensacion = new CampoAdicionalBean();
            campoCompensacion.setAtributo(ConstantesEFacturacion.ATRIBUTO_COMP+VariablesAlm.getVariable(VariablesAlm.PORCENTAJE_COMPENSACION_GOBIERNO)+"%:");
            campoCompensacion.setCampoAdicional(ticket.getTotales().getCompensacionGobierno().toString());
            camposAdicionales.add(campoCompensacion);

            CampoAdicionalBean campoTotalPagar = new CampoAdicionalBean();
            campoTotalPagar.setAtributo(ConstantesEFacturacion.ATRIBUTO_TOTAL_PAGAR);
            campoTotalPagar.setCampoAdicional(ticket.getTotales().getTotalPagado().subtract(ticket.getTotales().getCompensacionGobierno()).toString());
            camposAdicionales.add(campoTotalPagar);
        }*/
        //detalle.añadirHijo("textoPromocion", curso.getCurPromocion();
        List<CampoAdicionalBean> camposAdicionales = null;
        camposAdicionales = new ArrayList<CampoAdicionalBean>();
        if (ticket.getCliente().getEmail() != null) {
            CampoAdicionalBean campoMail = new CampoAdicionalBean();
            campoMail.setCampoAdicional(ticket.getCliente().getEmail());
            campoMail.setAtributo("emailCliente");
            camposAdicionales.add(campoMail);
        }

        if (ticket.getObservaciones() != null && !ticket.getObservaciones().equals("")) {
            CampoAdicionalBean campoObservacion = new CampoAdicionalBean();
            campoObservacion.setCampoAdicional(ticket.getObservaciones().replaceAll("\n", " "));
            campoObservacion.setAtributo("OBSERVACIONES");
            int cont = campoObservacion.getCampoAdicional().length();
            if (cont < 300) {
                campoObservacion.setCampoAdicional(campoObservacion.getCampoAdicional());
            } else {
                String campoObservacionTemp = campoObservacion.getCampoAdicional().substring(0, 300);
                campoObservacion.setCampoAdicional(campoObservacionTemp);
            }
            camposAdicionales.add(campoObservacion);
        }

        return camposAdicionales;
    }

    private static List<ImpuestoBean> getImpuestos(TicketS ticket, LineaTicket linea) throws FacturaElectronicaException {
        List<ImpuestoBean> impuestos = new ArrayList<ImpuestoBean>();
        ImpuestoBean impuesto = new ImpuestoBean();
        impuesto.setCodigo(ConstantesEFacturacion.TIPO_IVA);
        String codImp = linea.getArticulo().getCodimp();
        int valorCodPorcentaje;
        Integer tarifaImpuesto = null;
        BigDecimal impuestosPrecioFinal = linea.getImporteFinalPagado();

        if (codImp.equals(ConstantesEFacturacion.COD_IMP_EXCENTO)) {
            valorCodPorcentaje = ConstantesEFacturacion.PORCENTAJE_0;
            tarifaImpuesto = ConstantesEFacturacion.TARIFA_IMP_0;
            impuestosPrecioFinal = BigDecimal.ZERO;
        } else if (codImp.equals(ConstantesEFacturacion.COD_IMP_NORMAL)) {
            if (Sesion.getEmpresa().getPorcentajeIva().setScale(2, RoundingMode.HALF_UP).equals(new BigDecimal(ConstantesEFacturacion.TARIFA_IMP_14).setScale(2, RoundingMode.HALF_UP))) {
                valorCodPorcentaje = ConstantesEFacturacion.PORCENTAJE_14;
                tarifaImpuesto = ConstantesEFacturacion.TARIFA_IMP_14;
            } else {
                valorCodPorcentaje = ConstantesEFacturacion.PORCENTAJE_12;
                tarifaImpuesto = ConstantesEFacturacion.TARIFA_IMP_12;
            }

            impuestosPrecioFinal = Numero.porcentajeR(linea.getImporteFinalPagado().setScale(2, BigDecimal.ROUND_HALF_UP), Sesion.getEmpresa().getPorcentajeIva());
        } else {
            log.error("getImpuestos() - El impuesto del detalle no es de los soportados");
            throw new FacturaElectronicaException("El impuesto del detalle no es de los soportados");
        }
        impuesto.setCodigoPorcentaje(valorCodPorcentaje);
        impuesto.setTarifa(String.valueOf(tarifaImpuesto));
        impuesto.setBaseImponible(String.valueOf(linea.getImporteFinalPagado().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
        impuesto.setValor(impuestosPrecioFinal.doubleValue());
        impuestos.add(impuesto);

        return impuestos;
    }

    private static List<ImpuestoBean> getImpuestosIce(TicketS ticket, LineaTicket linea) throws FacturaElectronicaException {
        List<ImpuestoBean> impuestos = new ArrayList<ImpuestoBean>();
        BigDecimal impuestoIce = BigDecimal.ZERO;
        Impuestos imp = null;

        String codImp = linea.getArticulo().getCodimp();
        int valorCodPorcentaje;
        Integer tarifaImpuesto = null;
//        BigDecimal impuestosPrecioFinal = linea.getImporteFinalPagado();
        BigDecimal impuestosPrecioFinal = Numero.porcentajeR(linea.getImporteFinalPagado().setScale(2, BigDecimal.ROUND_HALF_UP), Sesion.getEmpresa().getPorcentajeIva());
        if (ticket.getTotales().getImpuestosIce() != null) {
            ImpuestoBean impuesto = new ImpuestoBean();
            impuesto.setCodigo(ConstantesEFacturacion.TIPO_IVA);
            if (codImp.equals(ConstantesEFacturacion.COD_IMP_EXCENTO)) {
                valorCodPorcentaje = ConstantesEFacturacion.PORCENTAJE_0;
                tarifaImpuesto = ConstantesEFacturacion.TARIFA_IMP_0;
//                impuestosPrecioFinal = BigDecimal.ZERO;
                impuestosPrecioFinal = Numero.porcentajeR(linea.getImporteFinalPagado().setScale(2, BigDecimal.ROUND_HALF_UP), Sesion.getEmpresa().getPorcentajeIva());
            } else if (codImp.equals(ConstantesEFacturacion.COD_IMP_NORMAL)) {
                if (Sesion.getEmpresa().getPorcentajeIva().setScale(2, RoundingMode.HALF_UP).equals(new BigDecimal(ConstantesEFacturacion.TARIFA_IMP_14).setScale(2, RoundingMode.HALF_UP))) {
                    valorCodPorcentaje = ConstantesEFacturacion.PORCENTAJE_14;
                    tarifaImpuesto = ConstantesEFacturacion.TARIFA_IMP_14;
                } else {
                    valorCodPorcentaje = ConstantesEFacturacion.PORCENTAJE_12;
                    tarifaImpuesto = ConstantesEFacturacion.TARIFA_IMP_12;
                }

                impuestosPrecioFinal = Numero.porcentajeR(linea.getImporteFinalPagado().setScale(2, BigDecimal.ROUND_HALF_UP), Sesion.getEmpresa().getPorcentajeIva());
            } else {
                log.error("getImpuestos() - El impuesto del detalle no es de los soportados");
                throw new FacturaElectronicaException("El impuesto del detalle no es de los soportados");
            }
//            impuesto.setCodigoPorcentaje(valorCodPorcentaje);
//            impuesto.setTarifa(String.valueOf(tarifaImpuesto));
//            impuesto.setBaseImponible(String.valueOf(precioIce));
//            impuesto.setValor(impuestosPrecioFinal.doubleValue());
//            impuestos.add(impuesto);
            impuesto.setCodigoPorcentaje(valorCodPorcentaje);
            impuesto.setTarifa(String.valueOf(tarifaImpuesto));
            impuesto.setBaseImponible(String.valueOf(linea.getImporteFinalPagado().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
            impuesto.setValor(impuestosPrecioFinal.doubleValue());
            impuestos.add(impuesto);
        }
        if (linea.getArticulo().getCodmarca().getCodmarca().equals("1533")) {
            if (linea.getArticulo().getPmp() != null) {
                if (ticket.getTotales().getImpuestosIce() != null) {
                    ImpuestoBean impuesto = new ImpuestoBean();
                    impuesto.setCodigo(ConstantesEFacturacion.TIPO_ICE);
                    impuesto.setCodigoPorcentaje(EnumCodigoImpuestos.IMPUESTO_ICE.getCodigo().intValue());
                    BigDecimal tarifa = BigDecimal.ZERO;
//            impuesto.setTarifa(String.valueOf(ticket.getTotales().getTarifaIce()));
                    impuesto.setTarifa(String.valueOf(tarifa));
//            impuesto.setBaseImponible(String.valueOf(linea.getCantidad()));
                    impuesto.setBaseImponible(String.valueOf(tarifa));
                    imp = ImpuestosServices.consultar(EnumCodigoImpuestos.IMPUESTO_ICE.getCodigo());
                    impuestoIce = impuestoIce.add(imp.getTarifaEspecifica().multiply(new BigDecimal(linea.getCantidad())).setScale(2, BigDecimal.ROUND_HALF_UP));
                    impuesto.setValor(impuestoIce.doubleValue());
                    impuestos.add(impuesto);
                }
            } else {
                ImpuestoBean impuesto = new ImpuestoBean();
                impuesto.setCodigo(ConstantesEFacturacion.TIPO_ICE);
                impuesto.setCodigoPorcentaje(EnumCodigoImpuestos.IMPUESTO_ICE.getCodigo().intValue());
                BigDecimal tarifa = BigDecimal.ZERO;
                impuesto.setTarifa(String.valueOf(tarifa));
                impuesto.setBaseImponible(String.valueOf(tarifa));
                impuesto.setValor(tarifa.doubleValue());
                impuestos.add(impuesto);
            }
        }

        return impuestos;
    }

    private static List<CompensacionBean> construirTagCompensaciones(TicketS ticket) {
        List<CompensacionBean> compensaciones = new ArrayList<CompensacionBean>();
        CompensacionBean compensacion = new CompensacionBean();
        compensacion.setCodigo(ConstantesEFacturacion.CODIGO_COMP);
        compensacion.setTarifa(VariablesAlm.getVariable(VariablesAlm.PORCENTAJE_COMPENSACION_GOBIERNO));
        compensacion.setValor(Numero.redondear(ticket.getTotales().getCompensacionGobierno()).doubleValue());
        compensaciones.add(compensacion);
        return compensaciones;
    }

    private static List<PagoBean> construirTagPagos(TicketS ticket) {
        List<PagoBean> pagos = new ArrayList<PagoBean>();
        for (Pago pago : ticket.getPagos().getPagos()) {
            PagoBean pagoBean = new PagoBean();
            pagoBean.setFormaPago(pago.getMedioPagoActivo().getCodMedPagElec());
            pagoBean.setTotal(pago.getUstedPaga().doubleValue());
            pagos.add(pagoBean);
        }
        return pagos;
    }
}
