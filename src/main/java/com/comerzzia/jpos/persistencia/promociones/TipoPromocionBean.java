package com.comerzzia.jpos.persistencia.promociones;

public class TipoPromocionBean {

    //Constantes que definen los distintos tipos de promoción
    public static Long TIPO_PROMOCION_PRECIO = 1L;
    public static Long TIPO_PROMOCION_DESCUENTO = 2L;
    public static Long TIPO_PROMOCION_NXM = 3L;
    public static Long TIPO_PROMOCION_DESCUENTO_POR_CANTIDAD = 4L;
    public static Long TIPO_PROMOCION_COMBO = 5L;
    public static Long TIPO_PROMOCION_DESCUENTO_POR_IMPORTE = 6L;
    public static Long TIPO_PROMOCION_COMBO_CATEGORIAS = 7L;
    public static Long TIPO_PROMOCION_COMBO_SUBSECCIONES = 8L;
    public static Long TIPO_PROMOCION_COMBO_SECCIONES = 9L;
    public static Long TIPO_PROMOCION_NXM_SIMPLE = 10L;
    public static Long TIPO_PROMOCION_CUPON_DESCUENTO = 11L;
    public static Long TIPO_PROMOCION_CUPON_VOTOS = 12L;
    public static Long TIPO_PROMOCION_CUPON_SORTEO = 13L;
    public static Long TIPO_PROMOCION_DESCUENTO_CUMPLE = 14L;
    public static Long TIPO_PROMOCION_PUNTOS_CANJEO = 21L;
    public static Long TIPO_PROMOCION_PUNTOS_ACUMULA = 22L;
    public static Long TIPO_PROMOCION_DIA_SOCIO = 23L;
    public static Long TIPO_PROMOCION_N_CUOTAS_GRATIS = 17L;
    public static Long TIPO_PROMOCION_MESES_GRACIA = 18L;
    public static Long TIPO_PROMOCION_CUPON_REGALO_PROVEEDOR_EXTERNO = 15L;
    public static Long TIPO_PROMOCION_CUPON_REGALO_CURSO = 16L;
    public static Long TIPO_PROMOCION_REGALO_COMPRA = 19L;
    public static Long TIPO_PROMOCION_BILLETON = 20L;
    public static Long TIPO_PROMOCION_CUPON_SORTEO_SUKASA = 24L;
    public static Long TIPO_PROMOCION_DESCUENTO_COMBINADO = 25L;
    public static Long TIPO_PROMOCION_CUPON_DESCUENTO_AZAR = 26L;
    
    public TipoPromocionBean(Long id, String descripcion){
        idTipoPromocion = id;
        desTipoPromocion = descripcion;
    }
    
    
    private Long idTipoPromocion;
    private String desTipoPromocion;

    public String getDesTipoPromocion() {
        return desTipoPromocion;
    }

    public void setDesTipoPromocion(String desTipoPromocion) {
        this.desTipoPromocion = desTipoPromocion;
    }

    public Long getIdTipoPromocion() {
        return idTipoPromocion;
    }

    public void setIdTipoPromocion(Long idTipoPromocion) {
        this.idTipoPromocion = idTipoPromocion;
    }
    

    public boolean isPromocionTipoPrecio() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_PRECIO));
    }

    public boolean isPromocionTipoDescuento() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_DESCUENTO));
    }

    public boolean isPromocionTipoDescuentoPorCantidad() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_DESCUENTO_POR_CANTIDAD));
    }

    public boolean isPromocionTipoNxM() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_NXM));
    }

    public boolean isPromocionTipoNxMSimple() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_NXM_SIMPLE));
    }

    public boolean isPromocionTipoCombo() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_COMBO));
    }

    public boolean isPromocionTipoDescuentoPorImporte() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_DESCUENTO_POR_IMPORTE));
    }

    public boolean isPromocionTipoComboCategorias() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_COMBO_CATEGORIAS));
    }

    public boolean isPromocionTipoComboSecciones() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_COMBO_SECCIONES));
    }

    public boolean isPromocionTipoComboSubSecciones() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_COMBO_SUBSECCIONES));
    }

    public boolean isPromocionTipoCuponDescuento() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_CUPON_DESCUENTO));
    }

    public boolean isPromocionTipoCuponVotos() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_CUPON_VOTOS));
    }

    public boolean isPromocionTipoCuponSorteo() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_CUPON_SORTEO));
    }

    public boolean isPromocionTipoCuponSorteoSukasa() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_CUPON_SORTEO_SUKASA));
    }

    public boolean isPromocionTipoDescuentoCumple() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_DESCUENTO_CUMPLE));
    }

    public boolean isPromocionTipoPuntosAcumula() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_PUNTOS_ACUMULA));
    }

    public boolean isPromocionTipoPuntosCanjea() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_PUNTOS_CANJEO));
    }
    
    public boolean isPromocionTipoDescuentoCombinado() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_DESCUENTO_COMBINADO));
    }
    
    public boolean isPromocionTipoCuponDescuentoAzar() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_CUPON_DESCUENTO_AZAR));
    }    

    public boolean isPromocionTipoCupon() {
        return (isPromocionTipoCuponDescuento()
                || isPromocionTipoCuponSorteo()
                || isPromocionTipoCuponVotos()
                || isPromocionTipoCuponRegaloProveedorExterno()
                || isPromocionTipoCuponRegaloCurso()
                || isPromocionTipoCuponSorteoSukasa()
                || isPromocionTipoBilleton()
                || isPromocionTipoCuponDescuentoAzar());                
    }

    public boolean isPromocionTipoCuotas() {
        return (isPromocionTipoNCuotasGratis() || isPromocionTipoMesesGracia());
    }

    public boolean isPromocionTipoDiaSocio() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_DIA_SOCIO));
    }

    public boolean isPromocionTipoNCuotasGratis() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_N_CUOTAS_GRATIS));
    }

    public boolean isPromocionTipoMesesGracia() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_MESES_GRACIA));
    }

    public boolean isPromocionTipoCuponRegaloProveedorExterno() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_CUPON_REGALO_PROVEEDOR_EXTERNO));
    }

    public boolean isPromocionTipoCuponRegaloCurso() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_CUPON_REGALO_CURSO));
    }

    public boolean isPromocionTipoRegaloCompra() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_REGALO_COMPRA));
    }

    public boolean isPromocionTipoBilleton() {
        return (this.getIdTipoPromocion().equals(TipoPromocionBean.TIPO_PROMOCION_BILLETON));
    }    
        
}
