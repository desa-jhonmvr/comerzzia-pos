/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;


import java.io.Serializable;
import java.math.BigDecimal;
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
import java.util.regex.Pattern;
//cambio subir RD
/**
 *
 * @author MGRI
 */
@Entity
@Table(name = "D_TICKETS_DET_TBL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LineaTicketOrigen.findAll", query = "SELECT l FROM LineaTicketOrigen l"),
    @NamedQuery(name = "LineaTicketOrigen.findByUidTicket", query = "SELECT l FROM LineaTicketOrigen l WHERE l.lineaTicketOrigenPK.uidTicket = :uidTicket"),
    @NamedQuery(name = "LineaTicketOrigen.findByIdCajero", query = "SELECT l FROM LineaTicketOrigen l WHERE l.idCajero = :idCajero"),
    @NamedQuery(name = "LineaTicketOrigen.findByCodVendedor", query = "SELECT l FROM LineaTicketOrigen l WHERE l.codVendedor = :codVendedor"),
    @NamedQuery(name = "LineaTicketOrigen.findByIdLinea", query = "SELECT l FROM LineaTicketOrigen l WHERE l.lineaTicketOrigenPK.idLinea = :idLinea"),
    @NamedQuery(name = "LineaTicketOrigen.findByCodigoBarras", query = "SELECT l FROM LineaTicketOrigen l WHERE l.codigoBarras = :codigoBarras"),
    @NamedQuery(name = "LineaTicketOrigen.findByCantidad", query = "SELECT l FROM LineaTicketOrigen l WHERE l.cantidad = :cantidad"),
    @NamedQuery(name = "LineaTicketOrigen.findByPrecioOrigen", query = "SELECT l FROM LineaTicketOrigen l WHERE l.precioOrigen = :precioOrigen"),
    @NamedQuery(name = "LineaTicketOrigen.findByCostoLanded", query = "SELECT l FROM LineaTicketOrigen l WHERE l.costoLanded = :costoLanded"),
    @NamedQuery(name = "LineaTicketOrigen.findByPrecioTotalOrigen", query = "SELECT l FROM LineaTicketOrigen l WHERE l.precioTotalOrigen = :precioTotalOrigen"),
    @NamedQuery(name = "LineaTicketOrigen.findByImporteFinal", query = "SELECT l FROM LineaTicketOrigen l WHERE l.importeFinal = :importeFinal"),
    @NamedQuery(name = "LineaTicketOrigen.findByImporteTotalFinal", query = "SELECT l FROM LineaTicketOrigen l WHERE l.importeTotalFinal = :importeTotalFinal"),
    @NamedQuery(name = "LineaTicketOrigen.findByEnvioDomicilio", query = "SELECT l FROM LineaTicketOrigen l WHERE l.envioDomicilio = :envioDomicilio"),
    @NamedQuery(name = "LineaTicketOrigen.findByRecogidaPosterior", query = "SELECT l FROM LineaTicketOrigen l WHERE l.recogidaPosterior = :recogidaPosterior"),
    @NamedQuery(name = "LineaTicketOrigen.findByCodCategoria", query = "SELECT a FROM LineaTicketOrigen a WHERE a.codCategoria = :codCategoria"),
    @NamedQuery(name = "LineaTicketOrigen.findByPrecioReal", query = "SELECT l FROM LineaTicketOrigen l WHERE l.precioReal = :precioReal"),})
    
public class LineaTicketOrigen implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LineaTicketOrigenPK lineaTicketOrigenPK;
    @Basic(optional = false)
    @Column(name = "ID_CAJERO")
    private long idCajero;
    @Column(name = "COD_VENDEDOR")
    private String codVendedor;   
    @Column(name = "CODIGO_BARRAS")
    private String codigoBarras;
    @Basic(optional = false)
    @Column(name = "CANTIDAD")
    private int cantidad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "PRECIO_ORIGEN")
    private BigDecimal precioOrigen;
    @Basic(optional = false)
    @Column(name = "PRECIO_TOTAL_ORIGEN")
    private BigDecimal precioTotalOrigen;
    @Basic(optional = false)
    @Column(name = "IMPORTE_FINAL")
    private BigDecimal importeFinal;
     @Column(name = "COSTO_LANDED")
    private BigDecimal costoLanded;
    @Basic(optional = false)
    @Column(name = "IMPORTE_TOTAL_FINAL")
    private BigDecimal importeTotalFinal;
    @Basic(optional = false)
    @Column(name = "ENVIO_DOMICILIO")
    private char envioDomicilio;
    @Basic(optional = false)
    @Column(name = "RECOGIDA_POSTERIOR")
    private char recogidaPosterior;
    @Column(name = "CODIMP")
    private String codImp;
    @Column(name = "PORCENTAJE")
    private BigDecimal porcentaje;
    @JoinColumn(name = "CODART", referencedColumnName = "CODART")
    @ManyToOne(fetch = FetchType.LAZY)
    private Articulos codart;
    @Column(name = "COD_CATEGORIA")
    private String codCategoria;
     @Column(name = "PRECIO_REAL")
    private BigDecimal precioReal;
    @Column(name = "PEDIDO_FACTURADO")
    private char pedidoFacturado;
    
    //@Basic(optional = false)
    //@Column(name = "CODART")
    //private String codart;
    
    @Transient
    private String codMarca;
    @Transient
    private Integer idItem;
    
    @Transient
    private TicketsAlm ticket;
    @Transient 
    private char envioDomicilioOriginal;
    
    @Transient 
    public char recogidaPosteriorOriginal;
    
    @Transient 
    public BigDecimal importeIce;

    public LineaTicketOrigen() {
    }

    public LineaTicketOrigen(LineaTicketOrigenPK lineaTicketOrigenPK) {
        this.lineaTicketOrigenPK = lineaTicketOrigenPK;
    }

    public LineaTicketOrigen(LineaTicketOrigenPK lineaTicketOrigenPK, long idCajero, Articulos codart, int cantidad, BigDecimal precioOrigen, BigDecimal precioTotalOrigen, BigDecimal importeFinal, BigDecimal importeTotalFinal, char envioDomicilio, char recogidaPosterior, String codImp, BigDecimal porcentaje) {
        this.lineaTicketOrigenPK = lineaTicketOrigenPK;
        this.idCajero = idCajero;
        this.codart = codart;
        this.cantidad = cantidad;
        this.precioOrigen = precioOrigen;
        this.precioTotalOrigen = precioTotalOrigen;
        this.importeFinal = importeFinal;
        this.importeTotalFinal = importeTotalFinal;
        this.envioDomicilio = envioDomicilio;
        this.recogidaPosterior = recogidaPosterior;
        this.codImp = codImp;
        this.porcentaje = porcentaje;
    }

    public LineaTicketOrigen(String uidTicket, short idLinea) {
        this.lineaTicketOrigenPK = new LineaTicketOrigenPK(uidTicket, idLinea);
    }

    public LineaTicketOrigenPK getLineaTicketOrigenPK() {
        return lineaTicketOrigenPK;
    }

    public void setLineaTicketOrigenPK(LineaTicketOrigenPK lineaTicketOrigenPK) {
        this.lineaTicketOrigenPK = lineaTicketOrigenPK;
    }

    public long getIdCajero() {
        return idCajero;
    }

    public void setIdCajero(long idCajero) {
        this.idCajero = idCajero;
    }

    public String getCodVendedor() {
        return codVendedor;
    }

    public void setCodVendedor(String codVendedor) {
        this.codVendedor = codVendedor;
    }

    public Articulos getCodart() {
        return codart;
    }

    public void setCodart(Articulos codart) {
        this.codart = codart;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioOrigen() {
        return precioOrigen;
    }

    public void setPrecioOrigen(BigDecimal precioOrigen) {
        this.precioOrigen = precioOrigen;
    }

    public BigDecimal getPrecioTotalOrigen() {
        return precioTotalOrigen;
    }

    public void setPrecioTotalOrigen(BigDecimal precioTotalOrigen) {
        this.precioTotalOrigen = precioTotalOrigen;
    }

    public BigDecimal getImporteFinal() {
        return importeFinal;
    }

    public void setImporteFinal(BigDecimal importeFinal) {
        this.importeFinal = importeFinal;
    }

    public BigDecimal getImporteTotalFinal() {
        return importeTotalFinal;
    }

    public void setImporteTotalFinal(BigDecimal importeTotalFinal) {
        this.importeTotalFinal = importeTotalFinal;
    }

    public char getEnvioDomicilio() {
        return envioDomicilio;
    }

    public void setEnvioDomicilio(char envioDomicilio) {
        this.envioDomicilio = envioDomicilio;
    }

    public char getRecogidaPosterior() {
        return recogidaPosterior;
    }

    public void setRecogidaPosterior(char recogidaPosterior) {
        this.recogidaPosterior = recogidaPosterior;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lineaTicketOrigenPK != null ? lineaTicketOrigenPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof LineaTicketOrigen)) {
            return false;
        }
        LineaTicketOrigen other = (LineaTicketOrigen) object;
        if ((this.lineaTicketOrigenPK == null && other.lineaTicketOrigenPK != null) || (this.lineaTicketOrigenPK != null && !this.lineaTicketOrigenPK.equals(other.lineaTicketOrigenPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.LineaTicketOrigen[ lineaTicketOrigenPK=" + lineaTicketOrigenPK + " ]";
    }
        
    public boolean isEnvioDomicilio(){
        return (this.envioDomicilio!='N');
    }
    
    public boolean isEnvioDomicilioEntregado(){
        return (this.envioDomicilio == 'E');
    }
    
    public boolean isRecogidaPosteriorEntregado(){
        return (this.recogidaPosterior == 'E');
    } 
    
    public boolean isEnvioDomicilioPendiente(){
        return (this.envioDomicilio=='P');
    } 

    public void setEnvioDomicilioEntregado(){
        this.envioDomicilio = 'E';
    }
    
    public void setEnvioDomicilioPendiente(){
        this.envioDomicilio = 'P';
    }

    public boolean isRecogidaPosterior(){
        return (this.recogidaPosterior!='N');
    }
    
    public boolean isRecogidaPosteriorRecogido(){
        return (this.recogidaPosterior=='R');
    }
        
    public boolean isRecogidaPosteriorPendiente(){
        return (this.recogidaPosterior=='P');
    }

    public void setRecogidaPosteriorRecogido(){
        this.recogidaPosterior = 'R';
    }
    public void setRecogidaPosteriorPendiente(){
        this.recogidaPosterior = 'P';
    }

    public void setEnvioDomicilio(boolean envioDomicilio) {
        if (envioDomicilio){
            setEnvioDomicilioPendiente();
        }
        else{
            this.envioDomicilio = 'N';
        }
    }
    
    public void setRecogidaPosterior(boolean envioDomicilio) {
        if (envioDomicilio){
            setRecogidaPosteriorPendiente();
        }
        else{
            this.recogidaPosterior = 'N';
        }
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
        String[] split = this.codart.getCodart().split(Pattern.quote("."));    
            this.codMarca = split[0];
            this.idItem = new Integer(split[1]);
    }

    public TicketsAlm getTicket() {
        return ticket;
    }

    public void setTicket(TicketsAlm ticket) {
        this.ticket = ticket;
    }

    public char getEnvioDomicilioOriginal() {
        return envioDomicilioOriginal;
    }

    public void setEnvioDomicilioOriginal(char envioDomicilioOriginal) {
        this.envioDomicilioOriginal = envioDomicilioOriginal;
    }

    public String getCodImp() {
        return codImp;
    }

    public void setCodImp(String codImp) {
        this.codImp = codImp;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    public char getRecogidaPosteriorOriginal() {
        return recogidaPosteriorOriginal;
    }

    public void setRecogidaPosteriorOriginal(char recogidaPosteriorOriginal) {
        this.recogidaPosteriorOriginal = recogidaPosteriorOriginal;
    }

    public BigDecimal getCostoLanded() {
        return costoLanded;
    }

    public void setCostoLanded(BigDecimal costoLanded) {
        this.costoLanded = costoLanded;
    }

    public String getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(String codCategoria) {
        this.codCategoria = codCategoria;
    }

    public BigDecimal getPrecioReal() {
        return precioReal;
    }

    public void setPrecioReal(BigDecimal precioReal) {
        this.precioReal = precioReal;
    }    

    public char getPedidoFacturado() {
        return pedidoFacturado;
    }

    public void setPedidoFacturado(boolean pedidoFacturado) {
        
        if (pedidoFacturado){
            this.pedidoFacturado='S';
        }
        else{
            this.pedidoFacturado = 'N';
        }
    }

    public BigDecimal getImporteIce() {
        return importeIce;
    }

    public void setImporteIce(BigDecimal importeIce) {
        this.importeIce = importeIce;
    }
    
    
}
