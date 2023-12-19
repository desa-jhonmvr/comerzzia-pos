    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.servicios.print.lineas.LineaEnTicket;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "D_TIENDAS_TBL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tienda.findAll", query = "SELECT t FROM Tienda t"),
    @NamedQuery(name = "Tienda.findByCodalm", query = "SELECT t FROM Tienda t WHERE t.codalm = :codalm"),
    @NamedQuery(name = "Tienda.findByVersionArticulos", query = "SELECT t FROM Tienda t WHERE t.versionArticulos = :versionArticulos"),
    @NamedQuery(name = "Tienda.findByVersionArticulosRev", query = "SELECT t FROM Tienda t WHERE t.versionArticulosRev = :versionArticulosRev"),
    @NamedQuery(name = "Tienda.findByActivo", query = "SELECT t FROM Tienda t WHERE t.activo = :activo"),
    @NamedQuery(name = "Tienda.findByFechaVersionArticulos", query = "SELECT t FROM Tienda t WHERE t.fechaVersionArticulos = :fechaVersionArticulos"),
    @NamedQuery(name = "Tienda.findByFechaVersionArticulosRev", query = "SELECT t FROM Tienda t WHERE t.fechaVersionArticulosRev = :fechaVersionArticulosRev"),
    @NamedQuery(name = "Tienda.findByCodconalmVentas", query = "SELECT t FROM Tienda t WHERE t.codconalmVentas = :codconalmVentas")})
public class Tienda implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODALM")
    private String codalm;
    @Basic(optional = false)
    @Column(name = "VERSION_ARTICULOS")
    private long versionArticulos;
    @Basic(optional = false)
    @Column(name = "VERSION_ARTICULOS_REV")
    private long versionArticulosRev;
    @Lob
    @Column(name = "CONFIGURACION")
    private char[] configuracion;
    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private char activo;
    @Column(name = "FECHA_VERSION_ARTICULOS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVersionArticulos;
    @Column(name = "FECHA_VERSION_ARTICULOS_REV")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVersionArticulosRev;
    @Basic(optional = false)
    @Column(name = "CODCONALM_VENTAS")
    private String codconalmVentas;
    @Basic(optional = false)
    @Column(name="ACEPTAR_CEDULAS_DESC")
    private char aceptarCedulasDesc;
    @Basic(optional = false)
    @Column(name="ACEPTAR_TARJ_AFILIACION_DESC")
    private char aceptarTarjAfiliacionDesc;
    @JoinColumn(name = "CODALM", referencedColumnName = "CODALM", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Almacen almacen;
    @JoinColumn(name = "CODALM", referencedColumnName = "CODALMSRI", insertable = false, updatable = false)
    @OneToOne(optional = true, fetch = FetchType.LAZY)
    private SriTienda sriTienda;
    @JoinColumn(name = "CODREGION", referencedColumnName = "CODREGION")
    @ManyToOne(fetch = FetchType.LAZY)
    private Region codRegion;
    @JoinColumn(name = "CODFORMATO", referencedColumnName = "CODFORMATO")
    @ManyToOne(fetch = FetchType.LAZY)
    private Formato codformato;

    public Tienda() {
    }

    public Tienda(String codalm) {
        this.codalm = codalm;
    }

    public Tienda(String codalm, long versionArticulos, long versionArticulosRev, char activo, String codconalmVentas) {
        this.codalm = codalm;
        this.versionArticulos = versionArticulos;
        this.versionArticulosRev = versionArticulosRev;
        this.activo = activo;
        this.codconalmVentas = codconalmVentas;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

    public long getVersionArticulos() {
        return versionArticulos;
    }

    public void setVersionArticulos(long versionArticulos) {
        this.versionArticulos = versionArticulos;
    }

    public long getVersionArticulosRev() {
        return versionArticulosRev;
    }

    public void setVersionArticulosRev(long versionArticulosRev) {
        this.versionArticulosRev = versionArticulosRev;
    }

    public Serializable getConfiguracion() {
        return configuracion;
    }

    public char getActivo() {
        return activo;
    }

    public void setActivo(char activo) {
        this.activo = activo;
    }

    public Date getFechaVersionArticulos() {
        return fechaVersionArticulos;
    }

    public void setFechaVersionArticulos(Date fechaVersionArticulos) {
        this.fechaVersionArticulos = fechaVersionArticulos;
    }

    public Date getFechaVersionArticulosRev() {
        return fechaVersionArticulosRev;
    }

    public void setFechaVersionArticulosRev(Date fechaVersionArticulosRev) {
        this.fechaVersionArticulosRev = fechaVersionArticulosRev;
    }

    public String getCodconalmVentas() {
        return codconalmVentas;
    }

    public void setCodconalmVentas(String codconalmVentas) {
        this.codconalmVentas = codconalmVentas;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codalm != null ? codalm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Tienda)) {
            return false;
        }
        Tienda other = (Tienda) object;
        if ((this.codalm == null && other.codalm != null) || (this.codalm != null && !this.codalm.equals(other.codalm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.Tienda[ codalm=" + codalm + " ]";
    }

    public void setConfiguracion(char[] configuracion) {
        this.configuracion = configuracion;
    }

    public String getLocalForPrint() {
        String tienda = this.sriTienda.getDesalm();
        if (tienda != null && tienda.length() > 40) {
            tienda.substring(0, 40);
        }
        return tienda;
    }

    public String getDireccionForPrint() {
        String direccion = this.sriTienda.getDomicilio();
        if (direccion == null) {
            direccion = "[no establecida]";
        }
        if (direccion.length() > 40) {
            direccion.substring(0, 40);
        }

        return direccion;
    }

    public String getDireccionForPrint(int i) {
        String direccion = this.sriTienda.getDomicilio();
        if (direccion == null) {
            direccion = "[no establecida]";
        }
        if (direccion.length() > i) {
            direccion.substring(0, i);
        }

        return direccion;
    }

    public String getPoblacionForPrint() {
        String poblacion = this.getAlmacen().getPoblacion();
        if (poblacion == null) {
            poblacion = "[no establecida]";
        }
        if (poblacion.length() > 33) {
            poblacion.substring(0, 33);
        }
        return poblacion;
    }

    public String getPoblacionForPrint(int i) {
        String poblacion = this.getAlmacen().getPoblacion();
        if (poblacion == null) {
            poblacion = "[no establecida]";
        }
        if (poblacion.length() > i) {
            poblacion.substring(0, i);
        }
        return poblacion;
    }

    public String getDesalmForPrint(int i) {
        String desalm = this.sriTienda.getDesalm();
        if (desalm == null) {
            desalm = "[no establecida]";
        }
        if (desalm.length() > i) {
            desalm.substring(0, i);
        }
        return desalm;
    }

    public SriTienda getSriTienda() {
        return sriTienda;
    }

    public void setSriTienda(SriTienda sriTienda) {
        this.sriTienda = sriTienda;
    }

    public boolean isTiendaActiva() {
        return (this.getSriTienda().getEstado() == '1');
    }

    /** Métodos internos **/
    public String getCodalmSRI() {
        return this.sriTienda.getCodalmsri();
    }

    public char getEstadoSRI() {
        return this.sriTienda.getEstado();
    }

    public SriCaja getCajaActiva() {
        return this.sriTienda.getCajaActiva();
    }

    public Region getCodRegion() {
        return codRegion;
    }

    public void setCodRegion(Region codRegion) {
        this.codRegion = codRegion;
    }

    public Long getCodFormato() {
        return this.codformato.getCodformato();
    }
    
    public String getDesFormato() {
        return this.codformato.getDesformato();
    }

    public Formato getCodformato() {
        return codformato;
    }

    public void setCodformato(Formato codformato) {
        this.codformato = codformato;
    }

    public char getAceptarCedulasDesc() {
        return aceptarCedulasDesc;
    }

    public void setAceptarCedulasDesc(char aceptarCedulasDesc) {
        this.aceptarCedulasDesc = aceptarCedulasDesc;
    }

    public char getAceptarTarjAfiliacionDesc() {
        return aceptarTarjAfiliacionDesc;
    }

    public void setAceptarTarjAfiliacionDesc(char aceptarTarjAfiliacionDesc) {
        this.aceptarTarjAfiliacionDesc = aceptarTarjAfiliacionDesc;
    }
    
    public LineaEnTicket getDireccionLocalAsLineas() {
        return new LineaEnTicket("SUCURSAL : " + getSriTienda().getDomicilio());
    }
    
}
