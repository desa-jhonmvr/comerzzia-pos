package com.comerzzia.jpos.persistencia.listapda.detalle;

public class DetalleSesionPdaBean extends DetalleSesionPdaKey {
    private String codart;

    private String codigoBarras;
    
    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    
    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------


    public String getCodart() {
        return codart;
    }

    public void setCodart(String codart) {
        this.codart = codart == null ? null : codart.trim();
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras == null ? null : codigoBarras.trim();
    }
    
    //INICIO M�TODOS PERSONALIZADOS--------------------------------------------
    
    //FIN M�TODOS PERSONALIZADOS-----------------------------------------------

}