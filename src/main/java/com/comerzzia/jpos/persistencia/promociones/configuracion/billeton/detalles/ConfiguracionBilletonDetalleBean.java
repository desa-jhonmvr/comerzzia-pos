package com.comerzzia.jpos.persistencia.promociones.configuracion.billeton.detalles;

import java.math.BigDecimal;
import com.comerzzia.util.base.MantenimientoBean;

public class ConfiguracionBilletonDetalleBean extends MantenimientoBean {

    private static final long serialVersionUID = 1L;
    private Long idConfBilletonDet;
    private Long idConfBilleton;
    private Boolean tipo;
    private BigDecimal desde;
    private BigDecimal hasta;
    private BigDecimal valor;
    private Long codFormato;
    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    private String desFormato;

    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------
    public Long getIdConfBilletonDet() {
        return idConfBilletonDet;
    }

    public void setIdConfBilletonDet(Long idConfBilletonDet) {
        this.idConfBilletonDet = idConfBilletonDet;
    }

    public Long getIdConfBilleton() {
        return idConfBilleton;
    }

    public void setIdConfBilleton(Long idConfBilleton) {
        this.idConfBilleton = idConfBilleton;
    }

    public Boolean getTipo() {
        return tipo;
    }

    public void setTipo(Boolean tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getDesde() {
        return desde;
    }

    public void setDesde(BigDecimal desde) {
        this.desde = desde;
    }

    public BigDecimal getHasta() {
        return hasta;
    }

    public void setHasta(BigDecimal hasta) {
        this.hasta = hasta;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Long getCodFormato() {
        return codFormato;
    }

    public void setCodFormato(Long codFormato) {
        this.codFormato = codFormato;
    }

    //INICIO METODOS PERSONALIZADOS--------------------------------------------
    public String getDesFormato() {
        return desFormato;
    }

    public void setDesFormato(String desFormato) {
        this.desFormato = desFormato;
    }

    public String getTipoTexto() {
        return tipo ? "Auspicante" : "No Auspicante";
    }

    @Override
    protected void initNuevoBean() {
    }

    public boolean isDetalleAplicable(Boolean auspiciante, BigDecimal valor) {
        return (tipo.equals(auspiciante)
                && getDesde().compareTo(valor) <= 0
                && getHasta().compareTo(valor) >= 0);
    }
    //FIN METODOS PERSONALIZADOS-----------------------------------------------
}