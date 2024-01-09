/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.print.objetos;

import com.comerzzia.jpos.dto.credito.tabla.amortizacion.TablaAmortizacionCabDTO;
import com.comerzzia.jpos.dto.credito.tabla.amortizacion.TablaAmortizacionDetDTO;
import com.comerzzia.jpos.entity.db.TablaAmortizacionCab;
import com.comerzzia.jpos.entity.db.TablaAmortizacionDet;
import com.comerzzia.jpos.entity.db.Tienda;
import com.comerzzia.jpos.servicios.core.tiendas.TiendasException;
import com.comerzzia.jpos.servicios.core.tiendas.TiendasServices;
import com.comerzzia.jpos.servicios.pagos.credito.PagoCreditoSK;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import es.mpsistemas.util.log.Logger;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author CONTABILIDAD
 */
public class PrintTablaAmortizacionCreditoDirecto extends PrintDocument {

    private static Logger log = Logger.getMLogger(PrintTicket.class);

    private TicketS ticket;
    private String fechaVenta;
    private String facturaNumero;
    private String local;
    private String precioFinalVenta;
    private String plazo;
    private String tazaInteresFinanciamiento;
    private TablaAmortizacionCabDTO cabecera;
    private String totalPagarCliente;
    private String cedulaCliente;
    private double valorCreditoDirecto;
    private double valortotalProductos;
    private String pie;
    private List<ValorIndice> cuotas;

    public PrintTablaAmortizacionCreditoDirecto(TicketS ticket, TablaAmortizacionCabDTO cabecera) {
        boolean estado = Boolean.FALSE;
        //this.valortotalProductos = valortotalProductos;
        this.cabecera = cabecera;
        this.cuotas = new ArrayList<>();
            try {
                this.fechaVenta = ticket.getFecha().toString();
//                this.facturaNumero = ticket.getIdFactura().substring(8);
                this.facturaNumero = ticket.getIdFactura();
                Tienda tienda = TiendasServices.consultaTienda(ticket.getTienda());
                this.local = tienda.getSriTienda().getDesalm();
                
                this.plazo = ""+cabecera.getNumeroCuotas();
                if(this.plazo.equals("0")){
                    this.plazo="1";
                }
//////////////////////////////////////////////////
                valorCreditoDirecto = cabecera.getValorPrestamo().doubleValue();
                log.debug("valorCreditoDirecto " + valorCreditoDirecto);

                double valortotal = 0;
                DecimalFormat df = new DecimalFormat("#0.00");
                this.tazaInteresFinanciamiento = df.format(cabecera.getTasaInteres()) + "%";

                log.debug("valortotalProductos >= valorCreditoDirecto " + valorCreditoDirecto);
                this.precioFinalVenta = "" + valorCreditoDirecto;
                for(TablaAmortizacionDetDTO detalle : cabecera.getCuotas()){
                    ValorIndice valorind=new ValorIndice();
                    valorind.setIndice(""+detalle.getNumeroCuota());
                    valorind.setCapitalVivo(""+detalle.getCapitalVivo());
                    valorind.setCapitalAmortizado(""+detalle.getCapitalAmortizado());
                    valorind.setInteres(""+detalle.getInteres());
                    valorind.setCuotaAPagar(""+detalle.getCuotaAPagar());
                    valorind.setNumeroCuota(""+detalle.getNumeroCuota());
                    System.out.println("Detalle de Cuotas: " + "Cuota" + detalle.getNumeroCuota() );

                    this.cuotas.add(valorind);
                    valortotal = valortotal + detalle.getCuotaAPagar().doubleValue();
                }



                System.out.print("Total a pagar por el cliente al final del plazo: ");
//                 this.precioFinalVenta = df.format(valortotalProductos);
                this.totalPagarCliente = df.format(valortotal);
                this.cedulaCliente = ticket.getCliente().getCodcli();
                this.plazo=this.plazo+" Meses";
                this.pie = "COPIA";
            } catch (TiendasException ex) {
                java.util.logging.Logger.getLogger(PrintTablaAmortizacionCreditoDirecto.class.getName()).log(Level.SEVERE, null, ex);
            }
//        }
    }
    
    //resplado cambios 
//       public PrintTablaAmortizacion(TicketS ticket, double valortotalProductos) {
//        boolean estado = Boolean.FALSE;
//        this.valortotalProductos = valortotalProductos;
////        String[] cateorias = Sesion.getDatosConfiguracion().getDATOS_TABLA_AMORTIZACION();
////        if (ticket.getPagos().getMediosPagoCreditoDirecto().size() > 0) {
//            try {
//                //     for(int i=0;i<ticket.getLineas().getLineas().size();i++){
////       for(int j=0;j<cateorias.length;j++){
////         if(ticket.getLineas().getLineas().get(i).getArticulo().getCodcategoria().equals(cateorias[j])){
////            estado=Boolean.TRUE;  
//////            System.out.println(ticket.getLineas().getLinea(i).getPrecioTotal());
////            valortotalProductos=valortotalProductos+ticket.getLineas().getLinea(i).getPrecioTotal().doubleValue();
////            
////         }
////       }
////     }
////               if(!estado){
////                   return;
////               }
//                this.fechaVenta = ticket.getFecha().toString();
//                this.facturaNumero = ticket.getIdFactura().substring(8);
//                Tienda tienda = TiendasServices.consultaTienda(ticket.getTienda());
//                this.local = tienda.getSriTienda().getDesalm();
//
////               this.precioFinalVenta=ticket.getPagos().getTotal().toString();
//                int indice = 0;
//                for (int i = 0; i < ticket.getPagos().getPagos().size(); i++) {
//
//                    if (ticket.getPagos().getPagos().get(i) instanceof PagoCredito) {
//                        indice = i;
//                    } else {
//
//                    }
//                }
//                this.plazo = ticket.getPagos().getPagos().get(indice).getPlanSeleccionado().getMeses() + " Meses";
////////////////////////////////////////////////////
//                valorCreditoDirecto = ticket.getPagos().getPagos().get(indice).getPlanSeleccionado().getaPagar().doubleValue();
//                this.tazaInteresFinanciamiento = ticket.getPagos().getPagos().get(indice).getPlanSeleccionado().getPorcentajeInteres() + "%";
//                double valortotal = 0;
//                DecimalFormat df = new DecimalFormat("#.00");
//                this.cuotas = new ArrayList<ValorIndice>();
//                valortotalProductos = (Double) (Math.round(valortotalProductos * 100.0) / 100.0);
//                if (valortotalProductos >= valorCreditoDirecto) {
//                    this.precioFinalVenta = "" + valorCreditoDirecto;
//                    for (int i = 1; i <= ticket.getPagos().getPagos().get(indice).getPlanSeleccionado().getMeses(); i++) {
//                        double valor = valorCreditoDirecto / (ticket.getPagos().getPagos().get(indice).getPlanSeleccionado().getMeses());
//                        ValorIndice valorind=new ValorIndice();
//                        valorind.setValor(df.format(valor));
//                        valorind.setIndice(""+i);
//                        this.cuotas.add(valorind);
//                        System.out.println("Detalle de Cuotas: " + "Cuota" + i + " Cuota De Capital: " + df.format(valor));
//                        valortotal = valortotal + valor;
//                    }
//                } else {
//                    this.precioFinalVenta = "" + valortotalProductos;
//                    for (int i = 1; i <= ticket.getPagos().getPagos().get(indice).getPlanSeleccionado().getMeses(); i++) {
//                        double valor = valortotalProductos / (ticket.getPagos().getPagos().get(indice).getPlanSeleccionado().getMeses());
//                         ValorIndice valorind=new ValorIndice();
//                        valorind.setValor(df.format(valor));
//                        valorind.setIndice(""+i);
//                        this.cuotas.add(valorind);
////                        this.cuotas.add(df.format(valor));
//                        System.out.println("Detalle de Cuotas: " + "Cuota" + i + " Cuota De Capital: " + df.format(valor));
//                        valortotal = valortotal + valor;
//                    }
//                }
//                System.out.print("Total a pagar por el cliente al final del plazo: ");
//                this.totalPagarCliente = df.format(valortotal);
//                this.cedulaCliente = ticket.getCliente().getCodcli();
//                this.pie = "COPIA";
//            } catch (TiendasException ex) {
//                java.util.logging.Logger.getLogger(PrintTablaAmortizacion.class.getName()).log(Level.SEVERE, null, ex);
//            }
////        }
//    }

    public static Logger getLog() {
        return log;
    }

    public static void setLog(Logger log) {
        PrintTablaAmortizacionCreditoDirecto.log = log;
    }

    public TicketS getTicket() {
        return ticket;
    }

    public void setTicket(TicketS ticket) {
        this.ticket = ticket;
    }

    public String getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public String getFacturaNumero() {
        return facturaNumero;
    }

    public void setFacturaNumero(String facturaNumero) {
        this.facturaNumero = facturaNumero;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getPrecioFinalVenta() {
        return precioFinalVenta;
    }

    public void setPrecioFinalVenta(String precioFinalVenta) {
        this.precioFinalVenta = precioFinalVenta;
    }

    public String getPlazo() {
        return plazo;
    }

    public void setPlazo(String plazo) {
        this.plazo = plazo;
    }

    public String getTazaInteresFinanciamiento() {
        return tazaInteresFinanciamiento;
    }

    public void setTazaInteresFinanciamiento(String tazaInteresFinanciamiento) {
        this.tazaInteresFinanciamiento = tazaInteresFinanciamiento;
    }

    public List<ValorIndice> getCuotas() {
        return cuotas;
    }

    public void setCuotas(List<ValorIndice> cuotas) {
        this.cuotas = cuotas;
    }

    public String getTotalPagarCliente() {
        return totalPagarCliente;
    }

    public void setTotalPagarCliente(String totalPagarCliente) {
        this.totalPagarCliente = totalPagarCliente;
    }

    public String getCedulaCliente() {
        return cedulaCliente;
    }

    public void setCedulaCliente(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    public String getPie() {
        return pie;
    }

    public void setPie(String pie) {
        this.pie = pie;
    }

    public TablaAmortizacionCabDTO getCabecera() {
        return cabecera;
    }

    public void setCabecera(TablaAmortizacionCabDTO cabecera) {
        this.cabecera = cabecera;
    }
}

//                //obtencion devalores para la impresion 
//                System.out.print("Fecha De Venta: ");     -------
//                System.out.println(ticket.getFecha());
//                //////////////////////////////////////////
//                System.out.print("Factura Numero: ");     -------
//                System.out.println(ticket.getIdFactura().substring(8));
//                //////////////////////////////////////////
//                System.out.print("Local: ");              -------
//                System.out.println(ticket.getTienda());
//                ////////////////////////////////////////////////
//                System.out.print("Precio Final Venta: "); --------
//                System.out.println(ticket.getPagos().getTotal());
//                ////////////////////////////////////////////////
//                int indice=0;
//                for (int i=0;i<ticket.getPagos().getPagos().size();i++) {
//
//                    if (ticket.getPagos().getPagos().get(i) instanceof PagoCredito) {
//                        indice=i;
//                    }else{
//                        
//                    }
//                  
//                }
//                System.out.print("Plazo: ");------
//                System.out.println(ticket.getPagos().getPagos().get(indice).getPlanSeleccionado().getMeses() + " Meses");
//                //////////////////////////////////////////////////
//                System.out.print("Taza de Interés de financiamiento: ");------
//                System.out.println(ticket.getPagos().getPagos().get(indice).getPlanSeleccionado().getPorcentajeInteres()+"%");
////                System.out.println(ticket.getPagos().get);
//                double valortotal=0;
//                DecimalFormat df = new DecimalFormat("#.00");----
//                for (int i = 1; i <=ticket.getPagos().getPagos().get(indice).getPlanSeleccionado().getMeses();  i++) {
//                    double valor = (Double.parseDouble(ticket.getPagos().getPagos().get(indice).getTotalImprimir()))/(ticket.getPagos().getPagos().get(indice).getPlanSeleccionado().getMeses());
//                    System.out.println("Detalle de Cuotas: " + "Cuota" + i + " Cuota De Capital: " + df.format(valor));
//                    valortotal=valortotal+valor;
//                }
//                 System.out.print("Total a pagar por el cliente al final del plazo: ");-----
//                 System.out.println(df.format(valortotal));
//                 System.out.print("Cedula Cliente: ");
//                 System.out.println(ticket.getCliente().getCodcli());  
