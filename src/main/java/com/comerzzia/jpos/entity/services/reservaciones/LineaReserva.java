/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services.reservaciones;

import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.Tarifas;
import com.comerzzia.jpos.servicios.tickets.componentes.DatosAdicionalesLineaTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.DescuentoTicket;
import com.comerzzia.jpos.servicios.tickets.componentes.PromocionLineaTicket;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import java.math.BigDecimal;

/**
 *
 * @author MGRI
 */
public class LineaReserva implements Comparable<LineaReserva> {

    private static final BigDecimal CTE_100 = new BigDecimal(100);
    
    private Integer idlinea; 
    private Articulos articulo;
    private String codigoBarras;
    private String codImp;
    private Integer cantidad;
    private BigDecimal precioSinDto; // precio tarifa (o precio promocion)
    private BigDecimal precioTotalSinDto; // precioSinDto + impuesto
    private BigDecimal descuento; // % descuento manual o por promoción por descuento
    private BigDecimal descuentoPrecio; // cantidad descuento manual o por promoción por descuento sobre el precio
    private BigDecimal descuentoPrecioTotal; // cantidad descuento manual o por promoción por descuento sobre el precio total
    private BigDecimal precio; // precioSinDto + (dto aplicado o promoción por dto)
    private BigDecimal precioTotal; // precio + impuestos
    private BigDecimal importe; // precio * cantidad
    private BigDecimal importeTotal; // precioTotal * cantidad
    private BigDecimal impuestos; // importeTotal - impuestos
    
    private DescuentoTicket impresionLineaDescuento; // linea de descuento que se imprimirá junto a esta línea
    
    private PromocionLineaTicket promocionLinea;
    private boolean promoUnitariaAplicada; // promociones siempre afectan sólo a una línea (dto y de precio)
    private boolean promoMultipleAplicada; // promociones que afectan a un conjunto de líneas
    
    private String descripcionAdicional;
    private BigDecimal importePantalla;
    private BigDecimal importeTotalPantalla;
    private BigDecimal precioPantalla;
    private BigDecimal precioTotalPantalla;
    
    private DatosAdicionalesLineaTicket datosAdicionales;
    
    
    public LineaReserva(String codigo, Articulos art, Integer cantidad, Tarifas tarifa) {
        articulo = art;
        codigoBarras = codigo;
        codImp = art.getCodimp(); 
        this.cantidad = cantidad;
        precioSinDto = tarifa.getPrecioVenta();
        precioTotalSinDto = tarifa.getPrecioTotal();
        descuento = new BigDecimal(0);
        recalcularPrecios();
        redondear();
    }

    public void recalcularPrecios(){
        descuentoPrecio = precioSinDto.multiply(descuento.divide(CTE_100));
        descuentoPrecioTotal = precioTotalSinDto.multiply(descuento.divide(CTE_100));
        precio = precioSinDto.subtract(descuentoPrecio);
        precioTotal = precioTotalSinDto.subtract(descuentoPrecioTotal);
        recalcularImportes();
    }

    public void recalcularImportes(){
        importe = precio.multiply(new BigDecimal(cantidad));
        importeTotal = precioTotal.multiply(new BigDecimal(cantidad));
        impuestos = importeTotal.subtract(importe);
    }

    private void redondear(){
        precioTotal = precioTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
        importe = importe.setScale(2, BigDecimal.ROUND_HALF_UP);
        importeTotal = importeTotal.setScale(2, BigDecimal.ROUND_HALF_UP);
        precio = precio.setScale(4, BigDecimal.ROUND_HALF_UP);
    }
    
    public void recalcularAhorroPromocion() {
        BigDecimal ahorro = promocionLinea.getPrecioTarifa().multiply(new BigDecimal(cantidad)).subtract(importe);
        BigDecimal ahorroTotal = promocionLinea.getPrecioTarifaTotal().multiply(new BigDecimal(cantidad)).subtract(importeTotal);
        promocionLinea.setImportesAhorro(ahorro, ahorroTotal);
    }
    
    public void establecerDescuento(BigDecimal descuento) {
        if (Numero.isMayorACero(descuento)){
            this.descuento = descuento;
            setPreciosPantalla(precioSinDto, precioTotalSinDto);
            recalcularPrecios();
            setImpresionLineaDescuento("Descuento");
        }
    }
    
    public boolean isPromoUnitariaAplicada(){
        return promoUnitariaAplicada;
    }

    public boolean isPromoMultipleAplicada(){
        return promoMultipleAplicada;
    }
    
    public boolean isPromocionAplicada(){
        return isPromoUnitariaAplicada() || isPromoMultipleAplicada();
    }

    public boolean tieneImpresionDescuento(){
        return impresionLineaDescuento != null;
    }

    /** Ordena de por precio descendente */
    @Override
    public int compareTo(LineaReserva linea) {
        return linea.getPrecioTotal().compareTo(getPrecioTotal());
    }

    public void setPreciosPantalla(BigDecimal precioPantalla, BigDecimal precioTotalPantalla) {
        this.precioPantalla = precioPantalla;
        this.precioTotalPantalla = precioTotalPantalla;
        importePantalla = precioPantalla.multiply(new BigDecimal(cantidad));
        importeTotalPantalla = precioTotalPantalla.multiply(new BigDecimal(cantidad));
    }

    public String getImportePantalla() {
        if (importePantalla != null){
            return "$ " + importePantalla.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
        return "$ " + importe.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public String getImporteTotalPantalla() {
        if (importeTotalPantalla != null){
            return "$ " + importeTotalPantalla.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
        return "$ " + importeTotal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public String getPrecioPantalla() {
        if (precioPantalla != null){
            return "$ " + precioPantalla.setScale(4, BigDecimal.ROUND_HALF_UP).toString();
        }
        return "$ " + precio.setScale(4, BigDecimal.ROUND_HALF_UP).toString();
    }

    public String getPrecioTotalPantalla() {
        if (precioTotalPantalla != null){
            return "$ " + precioTotalPantalla.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
        return "$ " + precioTotal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public BigDecimal getImportePromocion(){
        if (isPromoMultipleAplicada()){
            return getPromocionLinea().getImportePromocion();
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getImporteTotalPromocion(){
        if (isPromoMultipleAplicada()){
            return getPromocionLinea().getImporteTotalPromocion();
        }
        return BigDecimal.ZERO;
    }
    
    public Integer getCantidad() {
        return cantidad;
    }

    public String getCodImp() {
        return codImp;
    }

    public Articulos getArticulo() {
        return articulo;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public Integer getIdlinea() {
        return idlinea;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public BigDecimal getPrecioSinDto() {
        return precioSinDto;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public BigDecimal getPrecioTotalSinDto() {
        return precioTotalSinDto;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setCodImp(String codImp) {
        this.codImp = codImp;
    }

    public void setArticulo(Articulos articulo) {
        this.articulo = articulo;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public void setIdlinea(Integer idlinea) {
        this.idlinea = idlinea;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public void setImporteTotal(BigDecimal importeTotal) {
        this.importeTotal = importeTotal;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public void setPrecioSinDto(BigDecimal precioSinDto) {
        this.precioSinDto = precioSinDto;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public void setPrecioTotalSinDto(BigDecimal precioTotalSinDto) {
        this.precioTotalSinDto = precioTotalSinDto;
    }

    public BigDecimal getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos = impuestos;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public PromocionLineaTicket getPromocionLinea() {
        return promocionLinea;
    }

    public void setPromocionLinea(PromocionLineaTicket promocionLinea) {
        this.promocionLinea = promocionLinea;
    }

    public void setPromoUnitariaAplicada(boolean promoUnitariaAplicada) {
        this.promoUnitariaAplicada = promoUnitariaAplicada;
    }

    public void setPromoMultipleAplicada(boolean promoMultipleAplicada) {
        this.promoMultipleAplicada = promoMultipleAplicada;
    }

    public DatosAdicionalesLineaTicket getDatosAdicionales() {
        return datosAdicionales;
    }

    protected void setDatosAdicionales(DatosAdicionalesLineaTicket datosAdicionales) {
        this.datosAdicionales = datosAdicionales;
    }

    public DescuentoTicket getImpresionLineaDescuento() {
        return impresionLineaDescuento;
    }

    public void setImpresionLineaDescuento(String impresionLineaDescuento) {
        this.impresionLineaDescuento = new DescuentoTicket(impresionLineaDescuento, descuentoPrecioTotal, descuentoPrecio);
    }

    public String getDescripcionAdicional() {
        return descripcionAdicional;
    }

    public void setDescripcionAdicional(String descripcionAdicional) {
        this.descripcionAdicional = descripcionAdicional;
    }





}
