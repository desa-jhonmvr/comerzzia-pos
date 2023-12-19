/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.servicios.login.Sesion;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "D_ARTICULOS_TBL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Articulos.findAll", query = "SELECT a FROM Articulos a LEFT JOIN FETCH a.codpro WHERE a.desart LIKE :valor")
    ,
    @NamedQuery(name = "Articulos.findByCodart", query = "SELECT a FROM Articulos a WHERE a.codart = :codart")
    ,
    @NamedQuery(name = "Articulos.findByDesart", query = "SELECT a FROM Articulos a WHERE a.desart = :desart")
    ,
    @NamedQuery(name = "Articulos.findByFormato", query = "SELECT a FROM Articulos a WHERE a.formato = :formato")
    ,
    @NamedQuery(name = "Articulos.findByReferenciaProveedor", query = "SELECT a FROM Articulos a WHERE a.referenciaProveedor = :referenciaProveedor")
    ,
    @NamedQuery(name = "Articulos.findByDtoProveedor", query = "SELECT a FROM Articulos a WHERE a.dtoProveedor = :dtoProveedor")
    ,
    @NamedQuery(name = "Articulos.findByCodfab", query = "SELECT a FROM Articulos a WHERE a.codfab = :codfab")
    ,
    @NamedQuery(name = "Articulos.findByPmp", query = "SELECT a FROM Articulos a WHERE a.pmp = :pmp")
    ,
    @NamedQuery(name = "Articulos.findByActAutomaticaCosto", query = "SELECT a FROM Articulos a WHERE a.actAutomaticaCosto = :actAutomaticaCosto")
    ,
    @NamedQuery(name = "Articulos.findByCostoActualizado", query = "SELECT a FROM Articulos a WHERE a.costoActualizado = :costoActualizado")
    ,
    @NamedQuery(name = "Articulos.findByObservaciones", query = "SELECT a FROM Articulos a WHERE a.observaciones = :observaciones")
    ,
    @NamedQuery(name = "Articulos.findByActivo", query = "SELECT a FROM Articulos a WHERE a.activo = :activo")
    ,
    @NamedQuery(name = "Articulos.findByNumerosSerie", query = "SELECT a FROM Articulos a WHERE a.numerosSerie = :numerosSerie")
    ,
    @NamedQuery(name = "Articulos.findByDesglose1", query = "SELECT a FROM Articulos a WHERE a.desglose1 = :desglose1")
    ,
    @NamedQuery(name = "Articulos.findByDesglose2", query = "SELECT a FROM Articulos a WHERE a.desglose2 = :desglose2")
    ,
    @NamedQuery(name = "Articulos.findByGenerico", query = "SELECT a FROM Articulos a WHERE a.generico = :generico")
    ,
    @NamedQuery(name = "Articulos.findByEscaparate", query = "SELECT a FROM Articulos a WHERE a.escaparate = :escaparate")
    ,
    @NamedQuery(name = "Articulos.findByUnidadMedidaAlternativa", query = "SELECT a FROM Articulos a WHERE a.unidadMedidaAlternativa = :unidadMedidaAlternativa")
    ,
    @NamedQuery(name = "Articulos.findByCantidadUmEtiqueta", query = "SELECT a FROM Articulos a WHERE a.cantidadUmEtiqueta = :cantidadUmEtiqueta")
    ,
    @NamedQuery(name = "Articulos.findByFechaAlta", query = "SELECT a FROM Articulos a WHERE a.fechaAlta = :fechaAlta")
    ,
    @NamedQuery(name = "Articulos.findByVersion", query = "SELECT a FROM Articulos a WHERE a.version = :version")
    ,
    @NamedQuery(name = "Articulos.findByFechaVersion", query = "SELECT a FROM Articulos a WHERE a.fechaVersion = :fechaVersion")
    ,
    @NamedQuery(name = "Articulos.findByReferenciaInterna", query = "SELECT a FROM Articulos a WHERE a.referenciaInterna = :referenciaInterna")
    ,
    @NamedQuery(name = "Articulos.findByCodcategoria", query = "SELECT a FROM Articulos a WHERE a.codcategoria = :codcategoria")
    ,
    @NamedQuery(name = "Articulos.findByModelo", query = "SELECT a FROM Articulos a WHERE a.modelo = :modelo")
    ,
    @NamedQuery(name = "Articulos.findByTalla", query = "SELECT a FROM Articulos a WHERE a.talla = :talla")
    ,
    @NamedQuery(name = "Articulos.findByColor", query = "SELECT a FROM Articulos a WHERE a.color = :color")
    ,
    @NamedQuery(name = "Articulos.findByColeccion", query = "SELECT a FROM Articulos a WHERE a.coleccion = :coleccion")})
public class Articulos implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Definición de constante booleana verdadera como cadena
     */
    protected static final char TRUE = 'S';
    /**
     * Definición de constante booleana falsa como cadena
     */
    protected static final char FALSE = 'N';
    @Id
    @Basic(optional = false)
    @Column(name = "CODART")
    private String codart;
    @Basic(optional = false)
    @Column(name = "DESART")
    private String desart;
    @Column(name = "FORMATO")
    private String formato;
    @Column(name = "REFERENCIA_PROVEEDOR")
    private String referenciaProveedor;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "DTO_PROVEEDOR")
    private BigDecimal dtoProveedor;
    @Column(name = "CODFAB")
    private String codfab;
    @Column(name = "PMP")
    private BigDecimal pmp;
    @Column(name = "ACT_AUTOMATICA_COSTO")
    private Character actAutomaticaCosto;
    @Column(name = "COSTO_ACTUALIZADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date costoActualizado;
    @Column(name = "OBSERVACIONES")
    private String observaciones;
    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private char activo;
    @Column(name = "NUMEROS_SERIE")
    private Character numerosSerie;
    @Basic(optional = false)
    @Column(name = "DESGLOSE1")
    private char desglose1;
    @Basic(optional = false)
    @Column(name = "DESGLOSE2")
    private char desglose2;
    @Basic(optional = false)
    @Column(name = "GENERICO")
    private char generico;
    @Basic(optional = false)
    @Column(name = "ESCAPARATE")
    private char escaparate;
    @Column(name = "UNIDAD_MEDIDA_ALTERNATIVA")
    private String unidadMedidaAlternativa;
    @Column(name = "CANTIDAD_UM_ETIQUETA")
    private BigDecimal cantidadUmEtiqueta;
    @Column(name = "FECHA_ALTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
    @Basic(optional = false)
    @Column(name = "VERSION")
    private long version;
    @Column(name = "FECHA_VERSION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVersion;
    @Column(name = "REFERENCIA_INTERNA")
    private String referenciaInterna;
    @Column(name = "CODCAT")
    private String codcategoria;
    @Column(name = "CODSECCION")
    private String codseccion;
    @Column(name = "CODFAM")
    private String codsubseccion;
    @Column(name = "MODELO")
    private String modelo;
    @Column(name = "TALLA")
    private String talla;
    @Column(name = "COLOR")
    private String color;
    @Column(name = "COLECCION")
    private String coleccion;
    @JoinColumn(name = "CODPRO", referencedColumnName = "CODPRO")
    @ManyToOne(fetch = FetchType.LAZY)
    private Proveedores codpro;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "articulos", fetch = FetchType.LAZY)
    private Collection<Tarifas> tarifasCollection;
    @JoinColumn(name = "CODMARCA", referencedColumnName = "CODMARCA")
    @ManyToOne(fetch = FetchType.LAZY)
    private Marcas codmarca;
    @Column(name = "CODIMP")
    private String codimp;
    @Column(name = "VENTA_OTRO_LOCAL")
    private Character ventaOtroLocal;
    @Column(name = "COD_ITEM")
    private Integer idItem;
    @Column(name = "KIT_INSTALACION")
    private char kitInstalacion;
    @Column(name = "GARANTIA_ORIGINAL")
    private Integer garantiaOriginal;
    @Column(name = "GARANTIA_EXTENDIDA")
    private char garantiaExtendida;
    @Column(name = "DEDUCIBLE")
    private Character deducible;
    @Column(name = "ITM_ID")
    private Long itmId;

    public Articulos() {
    }

    public Articulos(String codart) {
        this.codart = codart;
    }

    public Articulos(String codart, String desart, BigDecimal dtoProveedor, char activo, char desglose1, char desglose2, char generico, char escaparate, long version) {
        this.codart = codart;
        this.desart = desart;
        this.dtoProveedor = dtoProveedor;
        this.activo = activo;
        this.desglose1 = desglose1;
        this.desglose2 = desglose2;
        this.generico = generico;
        this.escaparate = escaparate;
        this.version = version;
    }

    public boolean isVentaOtroLocal() {
        return ventaOtroLocal != null && ventaOtroLocal.equals('S');
    }

    public String getCodart() {
        return codart;
    }

    public void setCodart(String codart) {
        this.codart = codart;
    }

    public String getDesart() {
        return desart;
    }

    public void setDesart(String desart) {
        this.desart = desart;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getReferenciaProveedor() {
        return referenciaProveedor;
    }

    public void setReferenciaProveedor(String referenciaProveedor) {
        this.referenciaProveedor = referenciaProveedor;
    }

    public BigDecimal getDtoProveedor() {
        return dtoProveedor;
    }

    public void setDtoProveedor(BigDecimal dtoProveedor) {
        this.dtoProveedor = dtoProveedor;
    }

    public String getCodfab() {
        return codfab;
    }

    public void setCodfab(String codfab) {
        this.codfab = codfab;
    }

    public BigDecimal getPmp() {
        return pmp;
    }

    public void setPmp(BigDecimal pmp) {
        this.pmp = pmp;
    }

    public Character getActAutomaticaCosto() {
        return actAutomaticaCosto;
    }

    public void setActAutomaticaCosto(Character actAutomaticaCosto) {
        this.actAutomaticaCosto = actAutomaticaCosto;
    }

    public Date getCostoActualizado() {
        return costoActualizado;
    }

    public void setCostoActualizado(Date costoActualizado) {
        this.costoActualizado = costoActualizado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public char getActivo() {
        return activo;
    }

    public void setActivo(char activo) {
        this.activo = activo;
    }

    public Character getNumerosSerie() {
        return numerosSerie;
    }

    public void setNumerosSerie(Character numerosSerie) {
        this.numerosSerie = numerosSerie;
    }

    public char getDesglose1() {
        return desglose1;
    }

    public void setDesglose1(char desglose1) {
        this.desglose1 = desglose1;
    }

    public char getDesglose2() {
        return desglose2;
    }

    public void setDesglose2(char desglose2) {
        this.desglose2 = desglose2;
    }

    public char getGenerico() {
        return generico;
    }

    public void setGenerico(char generico) {
        this.generico = generico;
    }

    public char getEscaparate() {
        return escaparate;
    }

    public void setEscaparate(char escaparate) {
        this.escaparate = escaparate;
    }

    public String getUnidadMedidaAlternativa() {
        return unidadMedidaAlternativa;
    }

    public void setUnidadMedidaAlternativa(String unidadMedidaAlternativa) {
        this.unidadMedidaAlternativa = unidadMedidaAlternativa;
    }

    public BigDecimal getCantidadUmEtiqueta() {
        return cantidadUmEtiqueta;
    }

    public void setCantidadUmEtiqueta(BigDecimal cantidadUmEtiqueta) {
        this.cantidadUmEtiqueta = cantidadUmEtiqueta;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Date getFechaVersion() {
        return fechaVersion;
    }

    public void setFechaVersion(Date fechaVersion) {
        this.fechaVersion = fechaVersion;
    }

    public String getReferenciaInterna() {
        return referenciaInterna;
    }

    public void setReferenciaInterna(String referenciaInterna) {
        this.referenciaInterna = referenciaInterna;
    }

    public String getCodcategoria() {
        return codcategoria;
    }

    public void setCodcategoria(String codcategoria) {
        this.codcategoria = codcategoria;
    }

    public String getCodseccion() {
        return codseccion;
    }

    public void setCodseccion(String codseccion) {
        this.codseccion = codseccion;
    }

    public String getCodsubseccion() {
        return codsubseccion;
    }

    public void setCodsubseccion(String codsubseccion) {
        this.codsubseccion = codsubseccion;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColeccion() {
        return coleccion;
    }

    public void setColeccion(String coleccion) {
        this.coleccion = coleccion;
    }

    public Proveedores getCodpro() {
        return codpro;
    }

    public void setCodpro(Proveedores codpro) {
        this.codpro = codpro;
    }

    public Marcas getCodmarca() {
        return codmarca;
    }

    public void setCodmarca(Marcas codmarca) {
        this.codmarca = codmarca;
    }

    @XmlTransient
    public Collection<Tarifas> getTarifasCollection() {
        return tarifasCollection;
    }

    public void setTarifasCollection(Collection<Tarifas> tarifasCollection) {
        this.tarifasCollection = tarifasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codart != null ? codart.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Articulos)) {
            return false;
        }
        Articulos other = (Articulos) object;
        if ((this.codart == null && other.codart != null) || (this.codart != null && !this.codart.equals(other.codart))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.Articulos[ codart=" + codart + " ]";
    }

    public String getCodimp() {
        return codimp;
    }

    public void setCodimp(String codimp) {
        this.codimp = codimp;
    }

    public String getDescForPrint() {
        String res = this.getDesart().replaceAll("&", "&amp;");
        res.replaceAll("\"", "&quot;");
        res.replaceAll("'", "&apos;");
        res.replaceAll(">", "&gt;");
        res.replaceAll("<", "&lt;");
        return res;
    }

    public String getModeloForPrint() {
        String res = "";
        if (this.getModelo() != null) {
            res = this.getModelo().replaceAll("&", "&amp;");
            res = res.replaceAll("\"", "&quot;");
            res = res.replaceAll("'", "&apos;");
            res = res.replaceAll(">", "&gt;");
            res = res.replaceAll("<", "&lt;");
        }
        return res;
    }

    public static String formateaCodigoBarras(String codigo) {
        String codigoFormateado = codigo;
        if (Sesion.isSukasa()) {
            return codigoFormateado;
        }
        if (codigo == null) {
            return null;
        }
        if (codigo.length() == 8 || codigo.length() == 13) {
            return codigoFormateado;
        }
        if (codigo.length() < 8) {
            while (codigoFormateado.length() < 8) {
                codigoFormateado = "0" + codigoFormateado;
            }
        } else if (codigo.length() < 13) {
            while (codigoFormateado.length() < 13) {
                codigoFormateado = "0" + codigoFormateado;
            }
        } else if (codigo.length() > 13) {
            codigoFormateado = codigoFormateado.substring(codigo.length() - 13, codigo.length());
        }
        return codigoFormateado;
    }

    public Integer getIdItem() {
        return idItem;
    }

    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }

    public char getKitInstalacion() {
        return kitInstalacion;
    }

    public void setKitInstalacion(char kitInstalacion) {
        this.kitInstalacion = kitInstalacion;
    }

    public boolean isKitInstalacion() {
        return this.kitInstalacion == TRUE;
    }

    public void setKitInstalacion(boolean iskitInstalacion) {
        if (iskitInstalacion) {
            this.kitInstalacion = TRUE;
        } else {
            this.kitInstalacion = FALSE;
        }
    }

    public Integer getGarantiaOriginal() {
        return garantiaOriginal;
    }

    public void setGarantiaOriginal(Integer garantiaOriginal) {
        this.garantiaOriginal = garantiaOriginal;
    }

    public boolean isGarantiaOriginal() {
        return this.garantiaOriginal != null;
    }

    public char getGarantiaExtendida() {
        return garantiaExtendida;
    }

    public void setGarantiaExtendida(char garantiaExtendida) {
        this.garantiaExtendida = garantiaExtendida;
    }

    public boolean isGarantiaExtendida() {
        return this.garantiaExtendida == TRUE;
    }

    public void setGarantiaExtendida(boolean isGarantiaExtendida) {
        if (isGarantiaExtendida) {
            this.garantiaExtendida = TRUE;
        } else {
            this.garantiaExtendida = FALSE;
        }
    }

    public boolean isArticuloTieneIva() {
        return this.codimp.equals("1");
    }

    public boolean isArticuloNoAplicaIva() {
        return this.codimp.equals("0");
    }

    public Character getDeducible() {
        return deducible;
    }

    public void setDeducible(Character deducible) {
        this.deducible = deducible;
    }

    public boolean isDeduclibleAlimentacion() {
        return deducible != null && deducible.charValue() == 'A';
    }

    public boolean isDeduclibleMedicinas() {
        return (deducible != null && deducible.charValue() == 'M');
    }

    public boolean isDeduclibleEducacion() {
        return (deducible != null && deducible.charValue() == 'E');
    }

    public boolean isDeduclibleRopa() {
        return (deducible != null && deducible.charValue() == 'R');
    }

    public boolean isDeduclibleVivienda() {
        return (deducible != null && deducible.charValue() == 'V');
    }

    public boolean isNoDeduclible() {
        return (deducible == null);
    }

    public Long getItmId() {
        return itmId;
    }

    public void setItmId(Long itmId) {
        this.itmId = itmId;
    }

}
