package com.comerzzia.jpos.persistencia.clientes.afiliados;

import com.comerzzia.util.base.MantenimientoBean;
import java.awt.Image;
import java.math.BigInteger;
import javax.swing.ImageIcon;

public class TipoAfiliadoBean extends MantenimientoBean implements Comparable<String> {

    /**
     * 
     */
    private static final long serialVersionUID = -4242526861553853965L;
    private String codTipoAfiliado;
    private String desTipoAfiliado;
    private BigInteger porcentajeAbonoInicial;
    private Long compraSiguienteNivel;
    private TipoAfiliadoBean siguienteNivel;
    private ImageIcon imagenTarjetaAfiliado;

    @Override
    protected void initNuevoBean() {
    }

    public String getCodTipoAfiliado() {
        return codTipoAfiliado;
    }

    public void setCodTipoAfiliado(String codTipoAfiliado) {
        this.codTipoAfiliado = codTipoAfiliado;
    }

    public String getDesTipoAfiliado() {
        return desTipoAfiliado;
    }

    public void setDesTipoAfiliado(String desTipoAfiliado) {
        this.desTipoAfiliado = desTipoAfiliado;
    }

    public Long getCompraSiguienteNivel() {
        return compraSiguienteNivel;
    }

    public void setCompraSiguienteNivel(Long compraSiguienteNivel) {
        this.compraSiguienteNivel = compraSiguienteNivel;
    }

    @Override
    public int compareTo(String arg0) {
        return codTipoAfiliado.compareTo(arg0);
    }

    public TipoAfiliadoBean getSiguienteNivel() {
        return siguienteNivel;
    }

    public void setSiguienteNivel(TipoAfiliadoBean siguienteNivel) {
        this.siguienteNivel = siguienteNivel;
    }

    public BigInteger getPorcentajeAbonoInicial() {
        return porcentajeAbonoInicial;
    }

    public void setPorcentajeAbonoInicial(BigInteger porcentajeAbonoInicial) {
        this.porcentajeAbonoInicial = porcentajeAbonoInicial;
    }

    public boolean isCompraSiguienteNivelDefinido(){
        return compraSiguienteNivel != null && compraSiguienteNivel.compareTo(0L) > 0;
    }

    /**
     * @return the imagenTarjetaAfiliado
     */
    public ImageIcon getImagenTarjetaAfiliado() {
        return imagenTarjetaAfiliado;
    }

    /**
     * @param imagenTarjetaAfiliado the imagenTarjetaAfiliado to set
     */
    public void setImagenTarjetaAfiliado(ImageIcon imagenTarjetaAfiliado) {
        this.imagenTarjetaAfiliado = imagenTarjetaAfiliado;
    }
    
    
    
}