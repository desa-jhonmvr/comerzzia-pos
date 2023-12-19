/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MGRI
 */
//subir Cambio RD
@Entity
@Table(name = "D_TARIFAS_DET_TBL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tarifas.findAll", query = "SELECT t FROM Tarifas t"),
    @NamedQuery(name = "Tarifas.dameTarifa", query = "SELECT t FROM Tarifas t WHERE t.tarifasPK.codtar = :codtar AND t.tarifasPK.codart = :codart"),
    @NamedQuery(name = "Tarifas.findByCodtar", query = "SELECT t FROM Tarifas t WHERE t.tarifasPK.codtar = :codtar"),
    @NamedQuery(name = "Tarifas.findByCodart", query = "SELECT t FROM Tarifas t WHERE t.tarifasPK.codart = :codart"),
    @NamedQuery(name = "Tarifas.findByPrecioCosto", query = "SELECT t FROM Tarifas t WHERE t.precioCosto = :precioCosto"),
//    @NamedQuery(name = "Tarifas.findByCostoLanded", query = "SELECT t FROM Tarifas t WHERE t.costoLanded = :costoLanded"),
    @NamedQuery(name = "Tarifas.findByFactorMarcaje", query = "SELECT t FROM Tarifas t WHERE t.factorMarcaje = :factorMarcaje"),
    @NamedQuery(name = "Tarifas.findByPrecioVenta", query = "SELECT t FROM Tarifas t WHERE t.precioVenta = :precioVenta"),
    @NamedQuery(name = "Tarifas.findByVersion", query = "SELECT t FROM Tarifas t WHERE t.version = :version")})
public class Tarifas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TarifasPK tarifasPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
//    @Column(name = "COSTO_LANDED")
//    private BigDecimal costoLanded;
    @Column(name = "PRECIO_COSTO")
    private BigDecimal precioCosto;
    @Column(name = "FACTOR_MARCAJE")
    private BigDecimal factorMarcaje;
    @Basic(optional = false)
    @Column(name = "PRECIO_VENTA" ,scale = 2)
    private BigDecimal precioVenta;
    @Transient
    private BigDecimal precioTotal;
    @Column(name = "VERSION")
    private Long version;
    @JoinColumn(name = "CODTAR", referencedColumnName = "CODTAR", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TarifaId tarifaId;
    @JoinColumn(name = "CODART", referencedColumnName = "CODART", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Articulos articulos;

    @Transient
    private BigDecimal precioReal;
    
    public Tarifas() {
    }

    public Tarifas(TarifasPK tarifasPK) {
        this.tarifasPK = tarifasPK;
    }

    public Tarifas(TarifasPK tarifasPK, BigDecimal precioVenta, BigDecimal precioTotal) {
        this.tarifasPK = tarifasPK;
        this.precioVenta = precioVenta.setScale(2, RoundingMode.DOWN);
        this.precioTotal = precioTotal;
    }

    public Tarifas(String codtar, String codart) {
        this.tarifasPK = new TarifasPK(codtar, codart);
    }

    public TarifasPK getTarifasPK() {
        return tarifasPK;
    }

    public void setTarifasPK(TarifasPK tarifasPK) {
        this.tarifasPK = tarifasPK;
    }

//    public BigDecimal getCostoLanded() {
//        return costoLanded;
//    }
//
//    public void setCostoLanded(BigDecimal costoLanded) {
//        this.costoLanded = costoLanded;
//    }

    public BigDecimal getPrecioCosto() {
        return precioCosto;
    }

    public void setPrecioCosto(BigDecimal precioCosto) {
        this.precioCosto = precioCosto;
    }

  

 

    public void cambiarPrecioTotal(BigDecimal precioTotal, Articulos articulo) {
        this.precioTotal = precioTotal;
        if(articulo.isArticuloTieneIva()){
            BigDecimal porcentaje = Sesion.getEmpresa().getPorcentajeIva();
            precioVenta = Numero.getAntesDePorcentajeR(precioTotal, porcentaje);
        }else{
            precioVenta = precioTotal;
        }
    }
    
    public BigDecimal getFactorMarcaje() {
        return factorMarcaje;
    }

    public void setFactorMarcaje(BigDecimal factorMarcaje) {
        this.factorMarcaje = factorMarcaje;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = Numero.redondear(precioVenta);
    }
    
    public void setPrecioVentaSinRedondear(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public TarifaId getTarifaId() {
        return tarifaId;
    }

    public void setTarifaId(TarifaId tarifaId) {
        this.tarifaId = tarifaId;
    }

    public Articulos getArticulos() {
        return articulos;
    }

    public void setArticulos(Articulos articulos) {
        this.articulos = articulos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tarifasPK != null ? tarifasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tarifas)) {
            return false;
        }
        Tarifas other = (Tarifas) object;
        if ((this.tarifasPK == null && other.tarifasPK != null) || (this.tarifasPK != null && !this.tarifasPK.equals(other.tarifasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.Tarifas[ tarifasPK=" + tarifasPK + " ]";
    }

    public BigDecimal getPrecioTotal() {        
        if (precioTotal == null) {
            if (this.articulos != null) { // articulos es el artículo al que hace referencai la tarifa
                if (this.articulos.isArticuloNoAplicaIva()) {
                    precioTotal = precioVenta;
                }
                else {
                    precioTotal = Numero.masPorcentajeR(this.precioVenta, Sesion.getEmpresa().getPorcentajeIva());
                }
            }            
        }
        return this.precioTotal;
    }

    public BigDecimal getPrecioReal() {
        return precioReal;
    }

    public void setPrecioReal(BigDecimal precioReal) {
        this.precioReal = precioReal;
    }
    
}
