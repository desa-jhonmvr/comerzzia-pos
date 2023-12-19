package com.comerzzia.jpos.persistencia.clientes.tiposclientes;

public class TipoClienteBean {

    /**
     * 
     */
    private static final long serialVersionUID = 2096236033084278382L;
    private Long codTipoCliente;
    private String desTipoCliente;
    private Boolean postFecharVoucher= false;
    private Boolean requiereAutorizacionEnVenta = false;
    private Boolean solicitaTarjetaSupermaxi = false;

    public Long getCodTipoCliente() {
        return codTipoCliente;
    }

    public void setCodTipoCliente(Long codTipoCliente) {
        this.codTipoCliente = codTipoCliente;
    }

    public String getDesTipoCliente() {
        return desTipoCliente;
    }

    public void setDesTipoCliente(String desTipoCliente) {
        this.desTipoCliente = desTipoCliente;
    }
    
     public Boolean getPostFecharVoucher() {
    	return postFecharVoucher;
    }

	
    public void setPostFecharVoucher(Boolean postFecharVoucher) {
    	this.postFecharVoucher = postFecharVoucher;
    }

	
    public Boolean getRequiereAutorizacionEnVenta() {
    	return requiereAutorizacionEnVenta;
    }

	
    public void setRequiereAutorizacionEnVenta(Boolean requiereAutorizacionEnVenta) {
    	this.requiereAutorizacionEnVenta = requiereAutorizacionEnVenta;
    }

	
    public Boolean getSolicitaTarjetaSupermaxi() {
    	return solicitaTarjetaSupermaxi;
    }

	
    public void setSolicitaTarjetaSupermaxi(Boolean solicitaTarjetaSupermaxi) {
    	this.solicitaTarjetaSupermaxi = solicitaTarjetaSupermaxi;
    }
    

    @Override
    public boolean equals(Object o){
        if (o != null && o instanceof TipoClienteBean){
            return codTipoCliente.equals(((TipoClienteBean)o).getCodTipoCliente());
        }
        return false;
    }

}