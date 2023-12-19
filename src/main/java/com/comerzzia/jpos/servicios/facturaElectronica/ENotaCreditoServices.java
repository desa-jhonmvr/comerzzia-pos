/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.facturaElectronica;

import com.comerzzia.jpos.entity.db.Impuestos;
import com.comerzzia.jpos.entity.db.NotasCredito;
import com.comerzzia.jpos.persistencia.facturaElectronica.DetalleBean;
import com.comerzzia.jpos.persistencia.facturaElectronica.ImpuestoBean;
import com.comerzzia.jpos.persistencia.facturaElectronica.TotalImpuestoBean;
import com.comerzzia.jpos.persistencia.facturaElectronica.efactura.CompensacionBean;
import com.comerzzia.jpos.persistencia.facturaElectronica.enotaCredito.InfoNotaCreditoBean;
import com.comerzzia.jpos.persistencia.facturaElectronica.enotaCredito.NotaCreditoElectronicaBean;
import com.comerzzia.jpos.servicios.core.impuestos.ImpuestosServices;
import com.comerzzia.jpos.servicios.devoluciones.Devolucion;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.TicketOrigen;
import com.comerzzia.jpos.util.enums.EnumCodigoImpuestos;
import com.comerzzia.util.marshall.MarshallUtil;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.xml.XMLDocument;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author SMLM
 */
public class ENotaCreditoServices {

    private static final Logger log = Logger.getMLogger(ENotaCreditoServices.class);
    private static BigDecimal precioIce = BigDecimal.ZERO;
    private static BigDecimal baseIce = BigDecimal.ZERO;

    public static XMLDocument generarNotaCreditoElectronica(NotasCredito nota, Devolucion devolucion) throws FacturaElectronicaException {
        XMLDocument xml = null;
        try {
            NotaCreditoElectronicaBean notaCredito = new NotaCreditoElectronicaBean();
            generarNotaCredito(notaCredito, nota, devolucion);
            byte[] nc = MarshallUtil.crearXML(notaCredito, NotaCreditoElectronicaBean.class);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            xml = new XMLDocument(builder.parse(new ByteArrayInputStream(nc)));
            log.debug("XML Nota Crédito Electrónica generado: " + xml.toString());
        } catch (SAXException e) {
            log.error("generarNotaCreditoElectronica() - Error generando la nota de crédito electrónica: " + e, e);
            throw new FacturaElectronicaException(e);
        } catch (IOException e) {
            log.error("generarNotaCreditoElectronica() - Error generando la nota de crédito electrónica: " + e, e);
            throw new FacturaElectronicaException(e);
        } catch (ParserConfigurationException e) {
            log.error("generarNotaCreditoElectronica() - Error generando la nota de crédito electrónica: " + e, e);
            throw new FacturaElectronicaException(e);
        } catch (Exception e) {
            log.error("generarNotaCreditoElectronica() - Error generando la nota de crédito electrónica: " + e, e);
            throw new FacturaElectronicaException(e);
        }
        return xml;
    }

    private static void generarNotaCredito(NotaCreditoElectronicaBean nota, NotasCredito notaCredito, Devolucion devolucion) throws FacturaElectronicaException {
        nota.setInfoTributaria(EFacturaServices.generarInfoTributaria(notaCredito.getIdNotaCredito(), "NC"));
        nota.setInfoNotaCredito(generarInfoNotaCredito(nota, notaCredito, devolucion));
        nota.setDetalles(getDetalles(devolucion.getTicketDevolucion(), devolucion.getTicketOriginal()));
    }

    private static InfoNotaCreditoBean generarInfoNotaCredito(NotaCreditoElectronicaBean nota, NotasCredito notaCredito, Devolucion devolucion) throws FacturaElectronicaException {
        InfoNotaCreditoBean infoNota = new InfoNotaCreditoBean();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        infoNota.setFechaEmision(df.format(notaCredito.getFecha()));
        infoNota.setDirEstablecimiento(Sesion.getTienda().getAlmacen().getDomicilio());
        infoNota.setContribuyenteEspecial("5368");
        infoNota.setObligadoContabilidad("SI");
        String identificacion = devolucion.getTicketOriginal().getFacturacion().getTipoIdent();
        String codigoTipoIdentificacion = null;
        String codigoIdentificacion = devolucion.getTicketOriginal().getFacturacion().getIdent();
        String razonSocial = devolucion.getTicketOriginal().getFacturacion().getNombre() + " " + devolucion.getTicketOriginal().getFacturacion().getApellidos();

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
            log.error("generarInfoNotaCredito() - La identificacion del cliente no es de ninguno de los tipos soportados");
            throw new FacturaElectronicaException("La identificacion del cliente no es de ninguno de los tipos soportados");
        }
        infoNota.setTipoIdentificacionComprador(codigoTipoIdentificacion);
        infoNota.setRazonSocialComprador(razonSocial);
        infoNota.setIdentificacionComprador(codigoIdentificacion);
        //TODO : mirar lo del contribuyente, obligado contabilidad y rise
        //infoNota.setRise("");
        infoNota.setCodDocModificado(ConstantesEFacturacion.COMPROBANTE_FACTURA);
        infoNota.setNumDocModificado(devolucion.getTicketOriginal().getIdFacturaNotaCreditoElectronica());
        infoNota.setFechaEmisionDocSustento(df.format(devolucion.getTicketOriginal().getFecha().getDate()));
        BigDecimal sinImp = devolucion.getTicketDevolucion().getTotales().getBase().subtract(devolucion.getTicketDevolucion().getTotales().getImpuestosIce());
//        infoFactura.setTotalSinImpuestos(ticket.getTotales().getBase().doubleValue());
        if (sinImp.compareTo(BigDecimal.ZERO) > 0) {
//            infoFactura.setTotalSinImpuestos(sinImp.doubleValue());
            infoNota.setTotalSinImpuestos(devolucion.getTicketDevolucion().getTotales().getBase().doubleValue());
        } else {
            infoNota.setTotalSinImpuestos(BigDecimal.ZERO.doubleValue());
        }
        BigDecimal valorModificacion = devolucion.getTicketDevolucion().getTotales().getTotalAPagar();
        if (Numero.isMayorACero(devolucion.getTicketDevolucion().getTotales().getCompensacionGobierno())) {
            valorModificacion = valorModificacion.subtract(devolucion.getTicketDevolucion().getTotales().getCompensacionGobierno());
        }
        infoNota.setValorModificacion(Numero.redondear(valorModificacion).doubleValue());
        if (Numero.isMayorACero(devolucion.getTicketDevolucion().getTotales().getCompensacionGobierno())) {
            infoNota.setCompensaciones(construirTagCompensaciones(devolucion));
        }
        infoNota.setMoneda(ConstantesEFacturacion.MONEDA_DOLAR);
        //Construir Tag con Impuestos
        infoNota.setTotalConImpuestos(construirTagConImpuestos(devolucion.getTicketDevolucion(), devolucion.getTicketOriginal()));
        infoNota.setMotivo(devolucion.getDevolucion().getMotivo().getDescripcionMotivo());

        return infoNota;
    }

    public static List<TotalImpuestoBean> construirTagConImpuestos(TicketS ticket, TicketOrigen ticketOrigen) {
        List<TotalImpuestoBean> listaImpuestos = new ArrayList<TotalImpuestoBean>();
        if (ticket.getTotales().getSubtotal0().compareTo(BigDecimal.ZERO) > 0) {
            TotalImpuestoBean totalImpuesto = new TotalImpuestoBean();
            totalImpuesto.setCodigo(ConstantesEFacturacion.TIPO_IVA);
            totalImpuesto.setCodigoPorcentaje(ConstantesEFacturacion.PORCENTAJE_0);
            totalImpuesto.setBaseImponible(String.valueOf(ticket.getTotales().getSubtotal0().setScale(2, BigDecimal.ROUND_HALF_UP).abs().doubleValue()));
            totalImpuesto.setValor(ConstantesEFacturacion.VALOR_EXENTO);
            listaImpuestos.add(totalImpuesto);
        }
        if (ticket.getTotales().getSubtotal12().compareTo(BigDecimal.ZERO) > 0) {
            TotalImpuestoBean totalImpuesto = new TotalImpuestoBean();
            totalImpuesto.setCodigo(ConstantesEFacturacion.TIPO_IVA);
            if (ticketOrigen.getFecha().antes(ConstantesEFacturacion.JUNIO_2016)) {
                totalImpuesto.setCodigoPorcentaje(ConstantesEFacturacion.PORCENTAJE_12);
            } else {
                if (ticketOrigen.getFecha().antes(ConstantesEFacturacion.JUNIO_2017)) {
                    totalImpuesto.setCodigoPorcentaje(ConstantesEFacturacion.PORCENTAJE_14);
                } else {
                    if (Sesion.getEmpresa().getPorcentajeIva().setScale(2, RoundingMode.HALF_UP).equals(new BigDecimal(ConstantesEFacturacion.TARIFA_IMP_14).setScale(2, RoundingMode.HALF_UP))) {
                        totalImpuesto.setCodigoPorcentaje(ConstantesEFacturacion.PORCENTAJE_14);
                    } else {
                        totalImpuesto.setCodigoPorcentaje(ConstantesEFacturacion.PORCENTAJE_12);
                    }
                }
            }
            totalImpuesto.setBaseImponible(String.valueOf(ticket.getTotales().getSubtotal12().setScale(2, BigDecimal.ROUND_HALF_UP).abs().doubleValue()));
            totalImpuesto.setValor(ticket.getTotales().getImpuestos().setScale(2, BigDecimal.ROUND_HALF_UP).abs().doubleValue());
            listaImpuestos.add(totalImpuesto);
        }
        if (ticket.getTotales().getBaseImponibleIce() != null) {
            TotalImpuestoBean totalImpuesto = new TotalImpuestoBean();
            totalImpuesto.setCodigo(ConstantesEFacturacion.TIPO_ICE);
            totalImpuesto.setCodigoPorcentaje(EnumCodigoImpuestos.IMPUESTO_ICE.getCodigo().intValue());
//            if (ticket.getTotales().getBaseImponibleIce() != null) {
            totalImpuesto.setBaseImponible(String.valueOf(ticket.getTotales().getBaseImponibleIce()));
//            } else {
//                baseIce = new BigDecimal(00.00);
//                totalImpuesto.setBaseImponible(String.valueOf(baseIce.doubleValue()));
//            }
            totalImpuesto.setValor(ticket.getTotales().getImpuestosIce().abs().doubleValue());
            listaImpuestos.add(totalImpuesto);
        }
        return listaImpuestos;
    }

    public static List<DetalleBean> getDetalles(TicketS ticket, TicketOrigen ticketOrigen) throws FacturaElectronicaException {
        List<DetalleBean> detalles = new ArrayList<DetalleBean>();
        for (LineaTicket linea : ticket.getLineas().getLineas()) {
            DetalleBean detalle = new DetalleBean();
            detalle.setCodigoInterno(linea.getArticulo().getCodart());
            detalle.setCodigoAdicional(linea.getCodigoBarras());
            detalle.setDescripcion(linea.getArticulo().getDesart());
            detalle.setCantidad(linea.getCantidad());
            if (linea.getArticulo().getPmp() == null) {
                detalle.setPrecioUnitario(Numero.redondear(linea.getPrecioTarifaOrigen()).doubleValue());
            } else {
                detalle.setPrecioUnitario(precioIce.doubleValue());
            }
            if (linea.getArticulo().getPmp() == null) {
                detalle.setDescuento(linea.getImporteDescuentoFinal().abs().doubleValue());
                detalle.setPrecioTotalSinImpuesto(Numero.redondear(linea.getImporteFinalPagado()).abs().doubleValue());
                detalle.setImpuestos(getImpuestos(ticket, linea, ticketOrigen));
            } else {
                detalle.setDescuento(0d);
                BigDecimal precioSinImpuestos = BigDecimal.ZERO;
                detalle.setPrecioTotalSinImpuesto(precioSinImpuestos.abs().doubleValue());
                detalle.setImpuestos(getImpuestosIce(ticket, linea));
            }

            detalles.add(detalle);
        }
        return detalles;
    }

    private static List<ImpuestoBean> getImpuestos(TicketS ticket, LineaTicket linea, TicketOrigen ticketOrigen) throws FacturaElectronicaException {
        List<ImpuestoBean> impuestos = new ArrayList<ImpuestoBean>();
        ImpuestoBean impuesto = new ImpuestoBean();
        impuesto.setCodigo(ConstantesEFacturacion.TIPO_IVA);
        String codImp = linea.getCodimp();
        int valorCodPorcentaje;
        Integer tarifaImpuesto = null;
        BigDecimal impuestosPrecioFinal = linea.getImporteFinalPagado();

        if (codImp.equals(ConstantesEFacturacion.COD_IMP_EXCENTO)) {
            valorCodPorcentaje = ConstantesEFacturacion.PORCENTAJE_0;
            tarifaImpuesto = ConstantesEFacturacion.TARIFA_IMP_0;
            impuestosPrecioFinal = BigDecimal.ZERO;
        } else if (codImp.equals(ConstantesEFacturacion.COD_IMP_NORMAL)) {
            if (ticketOrigen.getFecha().antes(ConstantesEFacturacion.JUNIO_2016)) {
                valorCodPorcentaje = ConstantesEFacturacion.PORCENTAJE_12;
                tarifaImpuesto = ConstantesEFacturacion.TARIFA_IMP_12;
            } else {
                if (ticketOrigen.getFecha().antes(ConstantesEFacturacion.JUNIO_2017)) {
                    valorCodPorcentaje = ConstantesEFacturacion.PORCENTAJE_14;
                    tarifaImpuesto = ConstantesEFacturacion.TARIFA_IMP_14;
                } else {
                    if (Sesion.getEmpresa().getPorcentajeIva().setScale(2, RoundingMode.HALF_UP).equals(new BigDecimal(ConstantesEFacturacion.TARIFA_IMP_14).setScale(2, RoundingMode.HALF_UP))) {
                        valorCodPorcentaje = ConstantesEFacturacion.PORCENTAJE_14;
                        tarifaImpuesto = ConstantesEFacturacion.TARIFA_IMP_14;
                    } else {
                        valorCodPorcentaje = ConstantesEFacturacion.PORCENTAJE_12;
                        tarifaImpuesto = ConstantesEFacturacion.TARIFA_IMP_12;
                    }
                }
            }
            impuestosPrecioFinal = linea.getImpuestosFinalR();
        } else {
            log.error("getImpuestos() - El impuesto del detalle no es de los soportados");
            throw new FacturaElectronicaException("El impuesto del detalle no es de los soportados");
        }
        impuesto.setCodigoPorcentaje(valorCodPorcentaje);
        impuesto.setTarifa(String.valueOf(tarifaImpuesto));
        impuesto.setBaseImponible(String.valueOf(linea.getImporteFinalPagado().setScale(2, BigDecimal.ROUND_HALF_UP).abs().doubleValue()));
        impuesto.setValor(impuestosPrecioFinal.abs().doubleValue());
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
        BigDecimal impuestosPrecioFinal = linea.getImporteFinalPagado();
        if (ticket.getTotales().getImpuestosIce() != null) {
            ImpuestoBean impuesto = new ImpuestoBean();
            impuesto.setCodigo(ConstantesEFacturacion.TIPO_IVA);
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
//            impuesto.setBaseImponible(String.valueOf(precioIce));
            impuesto.setBaseImponible(String.valueOf(linea.getImporteFinalPagado().setScale(2, BigDecimal.ROUND_HALF_UP).abs().doubleValue()));
            impuesto.setValor(impuestosPrecioFinal.abs().doubleValue());
            impuestos.add(impuesto);
        }
        if (ticket.getTotales().getImpuestosIce() != null) {
            ImpuestoBean impuesto = new ImpuestoBean();
            impuesto.setCodigo(ConstantesEFacturacion.TIPO_ICE);
            impuesto.setCodigoPorcentaje(EnumCodigoImpuestos.IMPUESTO_ICE.getCodigo().intValue());
            imp = ImpuestosServices.consultar(EnumCodigoImpuestos.IMPUESTO_ICE.getCodigo());
            impuesto.setTarifa(String.valueOf(imp.getTarifaEspecifica()));
            impuesto.setBaseImponible(String.valueOf(linea.getCantidad()));
            impuestoIce = impuestoIce.add(imp.getTarifaEspecifica().multiply(new BigDecimal(linea.getCantidad())));
            impuesto.setValor(impuestoIce.abs().doubleValue());
            impuestos.add(impuesto);
        }
        return impuestos;
    }

    private static List<CompensacionBean> construirTagCompensaciones(Devolucion devolucion) {
        List<CompensacionBean> compensaciones = new ArrayList<CompensacionBean>();
        CompensacionBean compensacion = new CompensacionBean();
        compensacion.setCodigo(ConstantesEFacturacion.CODIGO_COMP);
        compensacion.setTarifa(String.valueOf(devolucion.getTicketOriginal().getPorcentajeCompensacion()));
        compensacion.setValor(Numero.redondear(devolucion.getTicketDevolucion().getTotales().getCompensacionGobierno()).abs().doubleValue());
        compensaciones.add(compensacion);
        return compensaciones;
    }
}
