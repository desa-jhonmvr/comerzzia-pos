/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.print.objetos;

import com.comerzzia.jpos.entity.db.Caja;
import com.comerzzia.jpos.entity.db.LineaTicketOrigen;
import com.comerzzia.jpos.entity.db.TicketsAlm;
import com.comerzzia.jpos.entity.db.Usuarios;
import com.comerzzia.jpos.persistencia.core.usuarios.UsuariosDao;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author CONTABILIDAD
 */
public class PrintPendiente extends PrintDocument {
    private String cabecera;
    private boolean datosNodatos;
    private String pie;
    private String comprador;
    private String cedula;
    private String direccion;
    private String telefono;
    private String elaborado;
    private String autorizado;
    private String cajaNumero;
    private String fecha;
    private String cajaSecuencial;
    private List<LineaTicketOrigen> articulos;
    private List<PrintPendiente> pendientes;
    private static Logger log = Logger.getMLogger(PrintTicket.class);

//    public List<PrintPendiente> obtenerImpresiones() {
//            return pendientes;
//    }
    public PrintPendiente(String cabecera){
        this.cabecera=cabecera;
    }
 
    public List<PrintPendiente> obtenerImpresiones(Caja cajaActual) {
       
        pendientes=new ArrayList<PrintPendiente>();
        datosNodatos = Boolean.FALSE;
      
        this.pie = "COPIA";
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        //Relizar el Proceso De Reimpresion De Comprobantes Pendientes
        Fecha fechaac = new Fecha(new Date());
//        Date fechaActual=;
        Date fechacero = new Date();
        fechacero.setHours(0);
        fechacero.setMinutes(0);
        fechacero.setSeconds(0);
        fechaac.sumaDias(1);
        Fecha fechaDiaEnCero = new Fecha(fechacero);
        DateFormat fechaHora = new SimpleDateFormat("dd/MM/yyyy");
        this.fecha = fechaHora.format(fechaDiaEnCero.getDate());
        List<LineaTicketOrigen> respuestaPendienes = TicketService.obtenerPendientes(em);
        for (int i = 0; i < respuestaPendienes.size(); i++) {
            String valorCabecera="FACTURA PENDIENTE DE DESPACHO";
//          if(respuestaPendienes.get(i).getEnvioDomicilio()=='P'){
//              valorCabecera="FACTURA PENDIENTE ENTREGA A DOMICILIO";
//          }else{
//              valorCabecera="FACTURA PENDIENTE DE DESPACHO";
//          }
          PrintPendiente pendi=new PrintPendiente(valorCabecera); 
            try {
                TicketsAlm respuesta = TicketService.consultarTicketUid(em, respuestaPendienes.get(i).getLineaTicketOrigenPK().getUidTicket(), cajaActual.getCodcaja(), cajaActual.getCodalm(), fechaDiaEnCero.getDate(), fechaac.getDate());
                if (respuesta != null) { 
                     articulos = new ArrayList<LineaTicketOrigen>();
                     boolean agregar=false;
                    for(int numero =0;numero<pendientes.size();numero++){
                         if(pendientes.get(numero).getCajaSecuencial().equals(cajaActual.getCodcaja()+ " " + respuesta.getIdTicket())){
                          articulos.add(respuestaPendienes.get(i));
                            pendientes.get(numero).getArticulos().add(respuestaPendienes.get(i));
                            agregar=true;
                         }
                         }
                             
                    if(!agregar){
                    this.cajaNumero = cajaActual.getCodcaja();
                    this.cajaSecuencial = cajaNumero + " " + respuesta.getIdTicket();
                    datosNodatos = Boolean.TRUE;
                    System.out.println("Comprador: " + respuesta.getFactNombre());
                    comprador = respuesta.getFactNombre();
                    System.out.println("Cedula: " + respuesta.getCodCli());
                    cedula = respuesta.getCodCli();
                    System.out.println("Dirección: " + respuesta.getFactDireccion());
                    direccion = respuesta.getFactDireccion();
                    System.out.println("Telefono: " + respuesta.getFactTelefonon());
                    if(respuesta.getFactTelefonon()!=null){
                    telefono = respuesta.getFactTelefonon();
                    }else{
                    telefono = "";    
                    }
                    System.out.println("Elaborado Por: " + respuesta.getUsuario());
                    UsuariosDao usuariodao = new UsuariosDao();
                    Usuarios usuario = usuariodao.obtenerUsuarioporNumero(respuesta.getUsuario());
                    elaborado = respuesta.getUsuario() + "-" + usuario.getDesusuario();
                    pendi.setDatosNodatos(datosNodatos);
                    pendi.setComprador(this.comprador);
                    pendi.setCedula(this.cedula);
                    pendi.setDireccion(this.direccion);
                    pendi.setTelefono(this.telefono);
                    pendi.setPie(this.pie);
                    pendi.setFecha(this.fecha);
                    pendi.setCajaNumero(this.cajaNumero);
                    pendi.setCajaSecuencial(this.cajaSecuencial);
                    articulos.add(respuestaPendienes.get(i));
                    pendi.setArticulos(articulos);
                    pendi.setAutorizado(this.autorizado);
                    pendi.setElaborado(this.elaborado);
                    pendientes.add(pendi);                
                             
                            
                         }
                       
                }
            } catch (NoResultException ex) {
                java.util.logging.Logger.getLogger(PrintPendiente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TicketException ex) {
                java.util.logging.Logger.getLogger(PrintPendiente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        
        
//        List<LineaTicketOrigen> respuestaPendienes = TicketService.obtenerPendientes(em);
//        for (int i = 0; i < respuestaPendienes.size(); i++) {
//        
//             PrintPendiente pendi=new PrintPendiente();
//            try {
//                int contador=0;
//                TicketsAlm respuesta = TicketService.consultarTicketUid(em, respuestaPendienes.get(i).getLineaTicketOrigenPK().getUidTicket(), cajaActual.getCodcaja(), cajaActual.getCodalm(), fechaDiaEnCero.getDate(), fechaac.getDate());
//                if (respuesta != null) { 
//                     for(int numero =0;numero<pendientes.size();numero++){
//                         if(pendientes.get(numero).getCajaSecuencial().equals(cajaActual.getCodcaja()+ " " + respuesta.getIdTicket())){
//                             contador ++;
//                         }else{
//                             contador=0;
//                         }
//                     }
//                    
//                    articulos = new ArrayList<LineaTicketOrigen>();
////                    articulos.add(respuestaPendienes.get(i));
////                    System.out.println("Codigo: " + respuestaPendienes.get(i).getCodigoBarras() + " Cantidad: " + respuestaPendienes.get(i).getCantidad() + " descripción: " + respuestaPendienes.get(i).getCodart().getDescForPrint());
//                    LineaTicketOrigen tikectseleccionado = respuestaPendienes.get(i);
//                    for (int j = 0; j < respuestaPendienes.size(); j++) {
//                    if (tikectseleccionado.getLineaTicketOrigenPK().getUidTicket().equals(respuestaPendienes.get(j).getLineaTicketOrigenPK().getUidTicket())) {
//                            articulos.add(respuestaPendienes.get(j));
//                            System.out.println("Codigo: " + respuestaPendienes.get(i).getCodigoBarras() + " Cantidad: " + respuestaPendienes.get(i).getCantidad() + " descripción: " + respuestaPendienes.get(i).getCodart().getDescForPrint());
////                            respuestaPendienes.remove(i);
////                            j--;
//
//                     }
//                    }
//                    if(contador==0){
//                    this.cajaNumero = cajaActual.getCodcaja();
//                    this.cajaSecuencial = cajaNumero + " " + respuesta.getIdTicket();
//                    datosNodatos = Boolean.TRUE;
//                    System.out.println("Comprador: " + respuesta.getFactNombre());
//                    comprador = respuesta.getFactNombre();
//                    System.out.println("Cedula: " + respuesta.getCodCli());
//                    cedula = respuesta.getCodCli();
//                    System.out.println("Dirección: " + respuesta.getFactDireccion());
//                    direccion = respuesta.getFactDireccion();
//                    System.out.println("Telefono: " + respuesta.getFactTelefonon());
//                    if(respuesta.getFactTelefonon()!=null){
//                    telefono = respuesta.getFactTelefonon();
//                    }else{
//                    telefono = "";    
//                    }
//                    System.out.println("Elaborado Por: " + respuesta.getUsuario());
//                    UsuariosDao usuariodao = new UsuariosDao();
//                    Usuarios usuario = usuariodao.obtenerUsuarioporNumero(respuesta.getUsuario());
//                    elaborado = respuesta.getUsuario() + "-" + usuario.getDesusuario();
//                    pendi.setDatosNodatos(datosNodatos);
//                    pendi.setComprador(this.comprador);
//                    pendi.setCedula(this.cedula);
//                    pendi.setDireccion(this.direccion);
//                    pendi.setTelefono(this.telefono);
//                    pendi.setPie(this.pie);
//                    pendi.setFecha(this.fecha);
//                    pendi.setCajaNumero(this.cajaNumero);
//                    pendi.setCajaSecuencial(this.cajaSecuencial);
//                    pendi.setArticulos(articulos);
//                    pendi.setAutorizado(this.autorizado);
//                    pendi.setElaborado(this.elaborado);
//                    pendientes.add(pendi);
//                    }
//               
//                   
//
//               
//               
//                }
//            } catch (NoResultException ex) {
//                 log.info("No se pudo imprimir Error al obtener pendientes ");
//                java.util.logging.Logger.getLogger(PrintPendiente.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (TicketException ex) {
//                 log.info("No se pudo imprimir Error al obtener Ticket ");
//                java.util.logging.Logger.getLogger(PrintPendiente.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
        return pendientes;
    }

public List<PrintPendiente> obtenerImpresionesEnvioDomicilio(Caja cajaActual) {
       
        pendientes=new ArrayList<PrintPendiente>();
        datosNodatos = Boolean.FALSE;
      
        this.pie = "COPIA";
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        //Relizar el Proceso De Reimpresion De Comprobantes Pendientes
        Fecha fechaac = new Fecha(new Date());
//        Date fechaActual=;
        Date fechacero = new Date();
        fechacero.setHours(0);
        fechacero.setMinutes(0);
        fechacero.setSeconds(0);
        fechaac.sumaDias(1);
        Fecha fechaDiaEnCero = new Fecha(fechacero);
        DateFormat fechaHora = new SimpleDateFormat("dd/MM/yyyy");
        this.fecha = fechaHora.format(fechaDiaEnCero.getDate());
        List<LineaTicketOrigen> respuestaPendienes = TicketService.obtenerDomicilio(em);
        for (int i = 0; i < respuestaPendienes.size(); i++) {
            String valorCabecera="FACTURA PENDIENTE ENTREGA A DOMICILIO";
          PrintPendiente pendi=new PrintPendiente(valorCabecera); 
            try {
                TicketsAlm respuesta = TicketService.consultarTicketUid(em, respuestaPendienes.get(i).getLineaTicketOrigenPK().getUidTicket(), cajaActual.getCodcaja(), cajaActual.getCodalm(), fechaDiaEnCero.getDate(), fechaac.getDate());
                if (respuesta != null) { 
                     articulos = new ArrayList<LineaTicketOrigen>();
                     boolean agregar=false;
                    for(int numero =0;numero<pendientes.size();numero++){
                         if(pendientes.get(numero).getCajaSecuencial().equals(cajaActual.getCodcaja()+ " " + respuesta.getIdTicket())){
                          articulos.add(respuestaPendienes.get(i));
                            pendientes.get(numero).getArticulos().add(respuestaPendienes.get(i));
                            agregar=true;
                         }
                         }
                             
                    if(!agregar){
                    this.cajaNumero = cajaActual.getCodcaja();
                    this.cajaSecuencial = cajaNumero + " " + respuesta.getIdTicket();
                    datosNodatos = Boolean.TRUE;
                    System.out.println("Comprador: " + respuesta.getFactNombre());
                    comprador = respuesta.getFactNombre();
                    System.out.println("Cedula: " + respuesta.getCodCli());
                    cedula = respuesta.getCodCli();
                    System.out.println("Dirección: " + respuesta.getFactDireccion());
                    direccion = respuesta.getFactDireccion();
                    System.out.println("Telefono: " + respuesta.getFactTelefonon());
                    if(respuesta.getFactTelefonon()!=null){
                    telefono = respuesta.getFactTelefonon();
                    }else{
                    telefono = "";    
                    }
                    System.out.println("Elaborado Por: " + respuesta.getUsuario());
                    UsuariosDao usuariodao = new UsuariosDao();
                    Usuarios usuario = usuariodao.obtenerUsuarioporNumero(respuesta.getUsuario());
                    elaborado = respuesta.getUsuario() + "-" + usuario.getDesusuario();
                    pendi.setDatosNodatos(datosNodatos);
                    pendi.setComprador(this.comprador);
                    pendi.setCedula(this.cedula);
                    pendi.setDireccion(this.direccion);
                    pendi.setTelefono(this.telefono);
                    pendi.setPie(this.pie);
                    pendi.setFecha(this.fecha);
                    pendi.setCajaNumero(this.cajaNumero);
                    pendi.setCajaSecuencial(this.cajaSecuencial);
                    articulos.add(respuestaPendienes.get(i));
                    pendi.setArticulos(articulos);
                    pendi.setAutorizado(this.autorizado);
                    pendi.setElaborado(this.elaborado);
                    pendientes.add(pendi);                
                             
                            
                         }
                       
                }
            } catch (NoResultException ex) {
                java.util.logging.Logger.getLogger(PrintPendiente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TicketException ex) {
                java.util.logging.Logger.getLogger(PrintPendiente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        
        
//        List<LineaTicketOrigen> respuestaPendienes = TicketService.obtenerPendientes(em);
//        for (int i = 0; i < respuestaPendienes.size(); i++) {
//        
//             PrintPendiente pendi=new PrintPendiente();
//            try {
//                int contador=0;
//                TicketsAlm respuesta = TicketService.consultarTicketUid(em, respuestaPendienes.get(i).getLineaTicketOrigenPK().getUidTicket(), cajaActual.getCodcaja(), cajaActual.getCodalm(), fechaDiaEnCero.getDate(), fechaac.getDate());
//                if (respuesta != null) { 
//                     for(int numero =0;numero<pendientes.size();numero++){
//                         if(pendientes.get(numero).getCajaSecuencial().equals(cajaActual.getCodcaja()+ " " + respuesta.getIdTicket())){
//                             contador ++;
//                         }else{
//                             contador=0;
//                         }
//                     }
//                    
//                    articulos = new ArrayList<LineaTicketOrigen>();
////                    articulos.add(respuestaPendienes.get(i));
////                    System.out.println("Codigo: " + respuestaPendienes.get(i).getCodigoBarras() + " Cantidad: " + respuestaPendienes.get(i).getCantidad() + " descripción: " + respuestaPendienes.get(i).getCodart().getDescForPrint());
//                    LineaTicketOrigen tikectseleccionado = respuestaPendienes.get(i);
//                    for (int j = 0; j < respuestaPendienes.size(); j++) {
//                    if (tikectseleccionado.getLineaTicketOrigenPK().getUidTicket().equals(respuestaPendienes.get(j).getLineaTicketOrigenPK().getUidTicket())) {
//                            articulos.add(respuestaPendienes.get(j));
//                            System.out.println("Codigo: " + respuestaPendienes.get(i).getCodigoBarras() + " Cantidad: " + respuestaPendienes.get(i).getCantidad() + " descripción: " + respuestaPendienes.get(i).getCodart().getDescForPrint());
////                            respuestaPendienes.remove(i);
////                            j--;
//
//                     }
//                    }
//                    if(contador==0){
//                    this.cajaNumero = cajaActual.getCodcaja();
//                    this.cajaSecuencial = cajaNumero + " " + respuesta.getIdTicket();
//                    datosNodatos = Boolean.TRUE;
//                    System.out.println("Comprador: " + respuesta.getFactNombre());
//                    comprador = respuesta.getFactNombre();
//                    System.out.println("Cedula: " + respuesta.getCodCli());
//                    cedula = respuesta.getCodCli();
//                    System.out.println("Dirección: " + respuesta.getFactDireccion());
//                    direccion = respuesta.getFactDireccion();
//                    System.out.println("Telefono: " + respuesta.getFactTelefonon());
//                    if(respuesta.getFactTelefonon()!=null){
//                    telefono = respuesta.getFactTelefonon();
//                    }else{
//                    telefono = "";    
//                    }
//                    System.out.println("Elaborado Por: " + respuesta.getUsuario());
//                    UsuariosDao usuariodao = new UsuariosDao();
//                    Usuarios usuario = usuariodao.obtenerUsuarioporNumero(respuesta.getUsuario());
//                    elaborado = respuesta.getUsuario() + "-" + usuario.getDesusuario();
//                    pendi.setDatosNodatos(datosNodatos);
//                    pendi.setComprador(this.comprador);
//                    pendi.setCedula(this.cedula);
//                    pendi.setDireccion(this.direccion);
//                    pendi.setTelefono(this.telefono);
//                    pendi.setPie(this.pie);
//                    pendi.setFecha(this.fecha);
//                    pendi.setCajaNumero(this.cajaNumero);
//                    pendi.setCajaSecuencial(this.cajaSecuencial);
//                    pendi.setArticulos(articulos);
//                    pendi.setAutorizado(this.autorizado);
//                    pendi.setElaborado(this.elaborado);
//                    pendientes.add(pendi);
//                    }
//               
//                   
//
//               
//               
//                }
//            } catch (NoResultException ex) {
//                 log.info("No se pudo imprimir Error al obtener pendientes ");
//                java.util.logging.Logger.getLogger(PrintPendiente.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (TicketException ex) {
//                 log.info("No se pudo imprimir Error al obtener Ticket ");
//                java.util.logging.Logger.getLogger(PrintPendiente.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
        return pendientes;
    }
    public String getPie() {
        return pie;
    }

    public void setPie(String pie) {
        this.pie = pie;
    }

    public String getComprador() {
        return comprador;
    }

    public void setComprador(String comprador) {
        this.comprador = comprador;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<LineaTicketOrigen> getArticulos() {
        return articulos;
    }

    public void setArticulos(List<LineaTicketOrigen> articulos) {
        this.articulos = articulos;
    }

    public String getElaborado() {
        return elaborado;
    }

    public void setElaborado(String elaborado) {
        this.elaborado = elaborado;
    }

    public String getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(String autorizado) {
        this.autorizado = autorizado;
    }

    public boolean isDatosNodatos() {
        return datosNodatos;
    }

    public void setDatosNodatos(boolean datosNodatos) {
        this.datosNodatos = datosNodatos;
    }

    public String getCajaNumero() {
        return cajaNumero;
    }

    public void setCajaNumero(String cajaNumero) {
        this.cajaNumero = cajaNumero;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCajaSecuencial() {
        return cajaSecuencial;
    }

    public void setCajaSecuencial(String cajaSecuencial) {
        this.cajaSecuencial = cajaSecuencial;
    }

    public String getCabecera() {
        return cabecera;
    }

    public void setCabecera(String cabecera) {
        this.cabecera = cabecera;
    }

}
