/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;


import com.comerzzia.util.numeros.bigdecimal.Numero;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.regex.Pattern;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "X_PLAN_NOVIOS_ART_TBL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ArticuloPlanNovio.findAll", query = "SELECT a FROM ArticuloPlanNovio a"),
    @NamedQuery(name = "ArticuloPlanNovio.findByIdPlan", query = "SELECT a FROM ArticuloPlanNovio a WHERE a.articuloPlanNovioPK.idPlan = :idPlan"),
    @NamedQuery(name = "ArticuloPlanNovio.findByIdLinea", query = "SELECT a FROM ArticuloPlanNovio a WHERE a.articuloPlanNovioPK.idLinea = :idLinea"),
    @NamedQuery(name = "ArticuloPlanNovio.findByCodArt", query = "SELECT a FROM ArticuloPlanNovio a WHERE a.codArt = :codArt"),
    @NamedQuery(name = "ArticuloPlanNovio.findByCodBarras", query = "SELECT a FROM ArticuloPlanNovio a WHERE a.codBarras = :codBarras"),
    @NamedQuery(name = "ArticuloPlanNovio.findByPrecio", query = "SELECT a FROM ArticuloPlanNovio a WHERE a.precio = :precio"),
    @NamedQuery(name = "ArticuloPlanNovio.findByPrecioTotal", query = "SELECT a FROM ArticuloPlanNovio a WHERE a.precioTotal = :precioTotal"),
    @NamedQuery(name = "ArticuloPlanNovio.findByComprado", query = "SELECT a FROM ArticuloPlanNovio a WHERE a.comprado = :comprado"),
    @NamedQuery(name = "ArticuloPlanNovio.findByEntregado", query = "SELECT a FROM ArticuloPlanNovio a WHERE a.entregado = :entregado"),
    @NamedQuery(name = "ArticuloPlanNovio.findByDevuelto", query = "SELECT a FROM ArticuloPlanNovio a WHERE a.devuelto = :devuelto"),
    @NamedQuery(name = "ArticuloPlanNovio.findByBorrado", query = "SELECT a FROM ArticuloPlanNovio a WHERE a.borrado = :borrado"),
    @NamedQuery(name = "ArticuloPlanNovio.findByFechaCompra", query = "SELECT a FROM ArticuloPlanNovio a WHERE a.fechaCompra = :fechaCompra"),
    @NamedQuery(name = "ArticuloPlanNovio.findByTotalPagadoConDscto", query = "SELECT a FROM ArticuloPlanNovio a WHERE a.totalPagadoConDscto = :totalPagadoConDscto"),
    @NamedQuery(name = "ArticuloPlanNovio.findByProcesado", query = "SELECT a FROM ArticuloPlanNovio a WHERE a.procesado = :procesado"),
    @NamedQuery(name = "ArticuloPlanNovio.findByUidTicket", query = "SELECT a FROM ArticuloPlanNovio a WHERE a.uidTicket = :uidTicket"),
    @NamedQuery(name = "ArticuloPlanNovio.findByIdLineaTicket", query = "SELECT a FROM ArticuloPlanNovio a WHERE a.idLineaTicket = :idLineaTicket"),
    @NamedQuery(name = "ArticuloPlanNovio.findByCodalm", query = "SELECT a FROM ArticuloPlanNovio a WHERE a.articuloPlanNovioPK.codalm = :codalm")})
public class ArticuloPlanNovio implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ArticuloPlanNovioPK articuloPlanNovioPK;
    @Basic(optional = false)
    @Column(name = "COD_ART")
    private String codArt;
    @Basic(optional = false)
    @Column(name = "COD_BARRAS")
    private String codBarras;
    @Column(name = "DESART")
    private String desArt;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "PRECIO")
    private BigDecimal precio;
    @Basic(optional = false)
    @Column(name = "PRECIO_TOTAL")
    private BigDecimal precioTotal;
    @Basic(optional = false)
    @Column(name = "COMPRADO")
    private char comprado;
    @Column(name = "ENTREGADO")
    private Character entregado;
    @Column(name = "DEVUELTO")
    private Character devuelto;
    @Column(name = "BORRADO")
    private Character borrado;
    @Column(name = "FECHA_COMPRA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCompra;
    @Column(name = "TOTAL_PAGADO_CON_DSCTO")
    private BigDecimal totalPagadoConDscto;
    @Column(name = "PRECIO_TARIFA_ORIGEN")
    private BigDecimal precioTarifaOrigen;
    @Column(name = "PRECIO_TOTAL_TARIFA_ORIGEN")
    private BigDecimal precioTotalTarifaOrigen;
    @Column(name = "ID_TICKET")
    private Long id_ticket;
    @Column(name = "PROCESADO")
    private Character procesado;
    @Column(name = "UID_TICKET")
    private String uidTicket;
    @Column(name = "ID_LINEA_TICKET")
    private Long idLineaTicket;    
    @Column(name = "COD_VENDEDOR")
    private String codVendedor;
    @Column(name = "CODALM_INVITADO")
    private String codAlmInvitado;
    @JoinColumns({
        @JoinColumn(name = "ID_PLAN", referencedColumnName = "ID_PLAN", insertable = false, updatable = false),
        @JoinColumn(name = "CODALM", referencedColumnName = "CODALM", insertable = false, updatable = false)})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PlanNovio planNovio;
    @JoinColumns({
        @JoinColumn(name = "ID_PLAN", referencedColumnName = "ID_PLAN", insertable = false, updatable = false),
        @JoinColumn(name = "ID_INVITADO", referencedColumnName = "ID_INVITADO"),
        @JoinColumn(name = "CODALM", referencedColumnName = "CODALM", insertable = false, updatable = false),
        @JoinColumn(name = "CODALM_INVITADO", referencedColumnName = "CODALM_INVITADO", insertable = false, updatable = false)})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private InvitadoPlanNovio invitadoPlanNovio;
    @Transient
    private BigDecimal descuento;
    
    
        
    @Transient
    private String codMarca;
    @Transient
    private Integer idItem;
    @Transient 
    private char envio;
    @Transient
    private char recogida;
    
    public ArticuloPlanNovio() {
    }

    public ArticuloPlanNovio(ArticuloPlanNovioPK articuloPlanNovioPK) {
        this.articuloPlanNovioPK = articuloPlanNovioPK;
    }

    public ArticuloPlanNovio(ArticuloPlanNovioPK articuloPlanNovioPK, String codArt, String codBarras, BigDecimal precio, BigDecimal precioTotal, char comprado) {
        this.articuloPlanNovioPK = articuloPlanNovioPK;
        this.codArt = codArt;
        this.codBarras = codBarras;
        this.precio = precio;
        this.precioTotal = precioTotal;
        this.comprado = comprado;
    }

    public ArticuloPlanNovio(BigInteger idPlan, long idLinea, String codAlm) {
        this.articuloPlanNovioPK = new ArticuloPlanNovioPK(idPlan, idLinea, codAlm);
    }

    public ArticuloPlanNovioPK getArticuloPlanNovioPK() {
        return articuloPlanNovioPK;
    }

    public void setArticuloPlanNovioPK(ArticuloPlanNovioPK articuloPlanNovioPK) {
        this.articuloPlanNovioPK = articuloPlanNovioPK;
    }

    public String getCodArt() {
        return codArt;
    }

    public void setCodArt(String codArt) {
        this.codArt = codArt;
    }

    public String getCodBarras() {
        return codBarras;
    }

    public void setCodBarras(String codBarras) {
        this.codBarras = codBarras;
    }

    public BigDecimal getPrecio() {
        if (descuento != null && Numero.isMayorACero(descuento)){
            return  Numero.menosPorcentajeR(precio, descuento);
        }
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getPrecioTotal() {
        if (descuento != null && Numero.isMayorACero(descuento)){
            return  Numero.menosPorcentajeR(precioTotal, descuento);
        }
        return precioTotal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void establecerDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public char getComprado() {
        return comprado;
    }

    public void setComprado(char comprado) {
        this.comprado = comprado;
    }

    public Character getEntregado() {
        return entregado;
    }

    public void setEntregado(Character entregado) {
        this.entregado = entregado;
    }

    public Character getDevuelto() {
        return devuelto;
    }

    public void setDevuelto(Character devuelto) {
        this.devuelto = devuelto;
    }

    public Character getBorrado() {
        return borrado;
    }

    public void setBorrado(Character borrado) {
        this.borrado = borrado;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public BigDecimal getTotalPagadoConDscto() {
        return totalPagadoConDscto;
    }

    public void setTotalPagadoConDscto(BigDecimal totalPagadoConDscto) {
        this.totalPagadoConDscto = totalPagadoConDscto;
    }

    public Character getProcesado() {
        return procesado;
    }

    public void setProcesado(Character procesado) {
        this.procesado = procesado;
    }

    public PlanNovio getPlanNovio() {
        return planNovio;
    }

    public void setPlanNovio(PlanNovio planNovio) {
        this.planNovio = planNovio;
    }

    public InvitadoPlanNovio getInvitadoPlanNovio() {
        return invitadoPlanNovio;
    }

    public void setInvitadoPlanNovio(InvitadoPlanNovio invitadoPlanNovio) {
        this.invitadoPlanNovio = invitadoPlanNovio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (articuloPlanNovioPK != null ? articuloPlanNovioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ArticuloPlanNovio)) {
            return false;
        }
        ArticuloPlanNovio other = (ArticuloPlanNovio) object;
        if ((this.articuloPlanNovioPK == null && other.articuloPlanNovioPK != null) || (this.articuloPlanNovioPK != null && !this.articuloPlanNovioPK.equals(other.articuloPlanNovioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.ArticuloPlanNovio[ articuloPlanNovioPK=" + articuloPlanNovioPK + " ]";
    }

    public void setComprado(boolean b) {
        if (b) {
            this.comprado = 'S';
        }
        else {
            this.comprado = 'N';
        }

    }

    public void setProcesado(boolean b) {
        if (b) {
            this.procesado = 'S';
        }
        else {
            this.procesado = 'N';
        }
    }

    public void setBorrado(boolean b) {
        if (b) {
            this.borrado = 'S';
        }
        else {
            this.borrado = 'N';
        }
    }

    public void setDevuelto(boolean b) {
        if (b) {
            this.devuelto = 'S';
        }
        else {
            this.devuelto = 'N';
        }
    }

    public void setEntregado(boolean b) {
        if (b) {
            this.entregado = 'S';
        }
        else {
            this.entregado = 'N';
        }
    }

    public String getDesArt() {
        return parseaXML(desArt);
    }

    public void setDesArt(String desArt) {
        this.desArt = desArt;
    }

    public boolean isComprado(){
        return (this.comprado =='S');
    }
    
    public boolean isPendienteEnvio(){
        return (this.envio =='P');
    }
    
    public boolean isPendienteRecoger(){
        return (this.recogida == 'P');
    }
    
    public boolean isCompradoOReservado(){
        return isComprado() || isPendienteEnvio() || isPendienteRecoger();
    }
    
    public boolean isPuedeSerBorrado() {
        return(this.comprado =='N' && this.devuelto!='S');            
    }

    public BigDecimal getPrecioTarifaOrigen() {
        return precioTarifaOrigen;
    }

    public void setPrecioTarifaOrigen(BigDecimal precioTarifaOrigen) {
        this.precioTarifaOrigen = precioTarifaOrigen;
    }

    public BigDecimal getPrecioTotalTarifaOrigen() {
        return precioTotalTarifaOrigen;
    }

    public void setPrecioTotalTarifaOrigen(BigDecimal precioTotalTarifaOrigen) {
        this.precioTotalTarifaOrigen = precioTotalTarifaOrigen;
    }

    public Long getId_ticket() {
        return id_ticket;
    }

    public void setId_ticket(Long id_ticket) {
        this.id_ticket = id_ticket;
    }

    public String getCodVendedor() {
        return codVendedor;
    }

    public void setCodVendedor(String codVendedor) {
        this.codVendedor = codVendedor;
    }

    public String getCodMarca() {
        if (codMarca==null){
            generaCampos();
        }
        return codMarca;
    }

    public void setCodMarca(String codMarca) {
        this.codMarca = codMarca;
    }

    public Integer getIdItem() {
        if (idItem==null){
            generaCampos();
        }
        return idItem;
    }

    public void setIdItem(Integer idItem) {
        this.idItem = idItem;
    }

    private void generaCampos() {
        String[] split = this.codArt.split(Pattern.quote("."));
        
            this.codMarca = split[0];
            this.idItem = new Integer(split[1]);
    }
    
    public String getUidTicket() {
        return uidTicket;
    }
    
    public void setUidTicket(String uidTicket) {
        this.uidTicket = uidTicket;
    }

    public Long getIdLineaTicket() {
        return idLineaTicket;
    }

    public void setIdLineaTicket(Long idLineaTicket) {
        this.idLineaTicket = idLineaTicket;
    }

    public char getEnvio() {
        return envio;
    }

    public void setEnvio(char envio) {
        this.envio = envio;
    }

    public char getRecogida() {
        return recogida;
    }

    public void setRecogida(char recogida) {
        this.recogida = recogida;
    }

    public String getCodAlmInvitado() {
        return codAlmInvitado;
    }

    public void setCodAlmInvitado(String codAlmInvitado) {
        this.codAlmInvitado = codAlmInvitado;
    }
   
    private String parseaXML(String sIn){
        String res = sIn;
        res = res.replaceAll("&", "&amp;");
        res = res.replaceAll("<","&lt;");
        res = res.replaceAll(">","&gt;");
        return res;
    }  
}
