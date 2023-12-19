
package com.comerzzia.jpos.servicios.print.objetos;

import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.print.lineas.LineaEnTicket;
import com.comerzzia.util.cadenas.Cadena;
import es.mpsistemas.util.fechas.Fecha;

/**
 * Clase Padre de Objetos para impresión de tickets
 * @author MGRI
 */
public class PrintDocument {
    
    
    private String nombreEmpresa ;            
    private String nombreTienda ; // Enviado por el módulo SRI 
    private String direccionLocal ; // Enviado por el módulo SRI
    private String ruc ; // Enviado por el módulo SRI  
    private String lugarYfecha ;// Region del local y fecha
    private String cajero;
    private String formato;
    private String porcentajeIvaEmpresa; //Porcentaje de iva actual
    
    public PrintDocument (){
        super();
    }
    
    /**
     * Carga datos base comunes a la mayoría de tickets
     * @param cargaDatosBase 
     */
    public PrintDocument (boolean cargaDatosBase, Fecha fecha){
        super();
        if (cargaDatosBase){
                this.nombreEmpresa = Sesion.getEmpresa().getNombreComercial();            
                this.nombreTienda = Sesion.getTienda().getSriTienda().getDesalm(); // Enviado por el módulo SRI                 
                this.direccionLocal = Sesion.getTienda().getSriTienda().getDomicilio(); // Enviado por el módulo SRI
                this.ruc = Sesion.getEmpresa().getCif() ; // Enviado por el módulo SRI
                this.lugarYfecha = Sesion.getTienda().getCodRegion().getDesregion() +", " +fecha.getString("dd 'de' MMMMM 'de' yyyy");
                this.cajero = Sesion.getUsuario().getDesUsuario();
                this.formato = Sesion.getTienda().getDesFormato();
                this.porcentajeIvaEmpresa = Sesion.getEmpresa().getPorcentajeIva().toString().trim();
        }
    }
    
    /**
     *  Método que oculta los números intermedios en una tarjeta de crédito
     * @param numeroTarjeta
     * @return 
     */
    public String ofuscarTarjeta(String numeroTarjeta){
        return Cadena.ofuscarTarjeta(numeroTarjeta);
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getNombreTienda() {
        return nombreTienda;
    }

    public void setNombreTienda(String nombreTienda) {
        this.nombreTienda = nombreTienda;
    }

    public String getDireccionLocal() {
        return direccionLocal;
    }

    public void setDireccionLocal(String direccionLocal) {
        this.direccionLocal = direccionLocal;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getLugarYfecha() {
        return lugarYfecha;
    }

    public void setLugarYfecha(String lugarYfecha) {
        this.lugarYfecha = lugarYfecha;
    }
    
    
    /**
     *  Nombre de empresa y local para la cabecera de los tickets
     * @return 
     */
    public LineaEnTicket getNombreEmpresaLocalAsLineas() {
        return  new LineaEnTicket(nombreEmpresa + "       "+nombreTienda);
    }
    
    public LineaEnTicket getDireccionLocalAsLineas() {
       return  new LineaEnTicket(direccionLocal);
    }
    
    public LineaEnTicket getLugarYfechaAsLineas() {
        return  new LineaEnTicket(lugarYfecha);
    }

    public String getCajero() {
        return cajero;
    }

    public void setCajero(String cajero) {
        this.cajero = cajero;
    }
    
   public LineaEnTicket getNombreCajeroAsLineas() {
        return  new LineaEnTicket("Elaborado por : "+cajero);
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getPorcentajeIvaEmpresa() {
        return porcentajeIvaEmpresa;
    }

    public void setPorcentajeIvaEmpresa(String porcentajeIvaEmpresa) {
        this.porcentajeIvaEmpresa = porcentajeIvaEmpresa;
    }    
 
}
