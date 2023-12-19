/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.util.cadenas.Cadena;
import es.mpsistemas.util.fechas.Fecha;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
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
@Table(name = "X_PLAN_NOVIOS_TBL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlanNovio.findAll", query = "SELECT p FROM PlanNovio p"),
    @NamedQuery(name = "PlanNovio.findByIdPlan", query = "SELECT p FROM PlanNovio p WHERE p.planNovioPK.idPlan = :idPlan"),
    @NamedQuery(name = "PlanNovio.findByCodalm", query = "SELECT p FROM PlanNovio p WHERE p.planNovioPK.codalm = :codalm"),
    @NamedQuery(name = "PlanNovio.findByFechaAlta", query = "SELECT p FROM PlanNovio p WHERE p.fechaAlta = :fechaAlta"),
    @NamedQuery(name = "PlanNovio.findByCodcaja", query = "SELECT p FROM PlanNovio p WHERE p.codcaja = :codcaja"),
    @NamedQuery(name = "PlanNovio.findByIdUsuario", query = "SELECT p FROM PlanNovio p WHERE p.idUsuario = :idUsuario"),
    @NamedQuery(name = "PlanNovio.findByCodVendedor", query = "SELECT p FROM PlanNovio p WHERE p.codVendedor = :codVendedor"),
    @NamedQuery(name = "PlanNovio.findByDireccion", query = "SELECT p FROM PlanNovio p WHERE p.direccion = :direccion"),
    @NamedQuery(name = "PlanNovio.findByTelefono", query = "SELECT p FROM PlanNovio p WHERE p.telefono = :telefono"),
    @NamedQuery(name = "PlanNovio.findByEmail", query = "SELECT p FROM PlanNovio p WHERE p.email = :email"),
    @NamedQuery(name = "PlanNovio.findByLugar", query = "SELECT p FROM PlanNovio p WHERE p.lugar = :lugar"),
    @NamedQuery(name = "PlanNovio.findByNumInvitados", query = "SELECT p FROM PlanNovio p WHERE p.numInvitados = :numInvitados"),
    @NamedQuery(name = "PlanNovio.findByFechaHoraBoda", query = "SELECT p FROM PlanNovio p WHERE p.fechaHoraBoda = :fechaHoraBoda"),
    @NamedQuery(name = "PlanNovio.findByFechaContacto", query = "SELECT p FROM PlanNovio p WHERE p.fechaContacto = :fechaContacto"),
    @NamedQuery(name = "PlanNovio.findBySolicitudLiquidacion", query = "SELECT p FROM PlanNovio p WHERE p.solicitudLiquidacion = :solicitudLiquidacion"),
    @NamedQuery(name = "PlanNovio.findByLiquidado", query = "SELECT p FROM PlanNovio p WHERE p.liquidado = :liquidado"),
    @NamedQuery(name = "PlanNovio.findByProcesado", query = "SELECT p FROM PlanNovio p WHERE p.procesado = :procesado")})
public class PlanNovio implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
   
    @EmbeddedId
    protected PlanNovioPK planNovioPK;
    @Basic(optional = false)
    @Column(name = "FECHA_ALTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
    @Basic(optional = false)
    @Column(name = "CODCAJA")
    private String codcaja;
    @Basic(optional = false)
    @Column(name = "ID_USUARIO")
    private String idUsuario;
    @Column(name = "ID_PLAN_SUKASA")
    private BigInteger idPlanSukasa;
    @Column(name = "COD_VENDEDOR")
    private String codVendedor;
    @JoinColumn(name = "NOVIO_CODCLI", referencedColumnName = "CODCLI")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)    
    private Cliente novio;
    @JoinColumn(name = "NOVIA_CODCLI", referencedColumnName = "CODCLI")
    @ManyToOne(optional = false, fetch = FetchType.LAZY) 
    private Cliente novia;    
    @Column(name = "DIRECCION")
    private String direccion;
    @Column(name = "TELEFONO")
    private String telefono;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "LUGAR")
    private String lugar;
    @Column(name = "NUM_INVITADOS")
    private Integer numInvitados;
    @Column(name = "FECHA_HORA_BODA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBoda;
    @Column(name = "FECHA_CONTACTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaContacto;
    @Column(name = "RESERVADO")
    private BigDecimal reservado;
    @Column(name = "ABONADO_CON_DTO")
    private BigDecimal abonadoConDto;
    @Column(name = "ABONADO_SIN_DTO")
    private BigDecimal abonadoSinDto;
    @Column(name = "ABONADO_UTILIZADO")
    private BigDecimal abonadoUtilizado;
    @Column(name = "COMPRADO")
    private BigDecimal comprado;
    @Column(name = "SOLICITUD_LIQUIDACION")
    private Character solicitudLiquidacion;
    @Column(name = "LIQUIDADO")
    private Character liquidado;
    @Column(name = "CADUCIDAD")
    @Temporal(TemporalType.TIMESTAMP)
    private Date caducidad;
    @Column(name = "FECHA_LIQUIDACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaLiquidacion;
    @Column(name = "PROCESADO")
    private Character procesado;
    @Column(name = "PROCESADO_TIENDA")
    private Character procesadoTienda;    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planNovio", fetch = FetchType.LAZY)
    @OrderBy("nombre")    
    private List<InvitadoPlanNovio> listaInvitados;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planNovio", fetch = FetchType.LAZY)
    @OrderBy("abonoPlanNovioPK.idAbono")
    private List<AbonoPlanNovio> listaAbonos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planNovio", fetch = FetchType.LAZY)
    @OrderBy("codArt")
    private List<ArticuloPlanNovio> listaArticulos;
    
    public PlanNovio() {  
        listaInvitados = new LinkedList<InvitadoPlanNovio>();
        listaAbonos =  new LinkedList<AbonoPlanNovio>();
        listaArticulos = new ArrayList<ArticuloPlanNovio>();
    }

    public PlanNovio(PlanNovioPK planNovioPK) {
        this.planNovioPK = planNovioPK;
    }

    public PlanNovio(PlanNovioPK planNovioPK, Date fechaAlta, String codcaja, String idUsuario) {
        this.planNovioPK = planNovioPK;
        this.fechaAlta = fechaAlta;
        this.codcaja = codcaja;
        this.idUsuario = idUsuario;
    }

    public PlanNovio(BigInteger idPlan, String codalm) {
        this.planNovioPK = new PlanNovioPK(idPlan, codalm);
    }

    public PlanNovioPK getPlanNovioPK() {
        return planNovioPK;
    }

    public void setPlanNovioPK(PlanNovioPK planNovioPK) {
        this.planNovioPK = planNovioPK;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getCodcaja() {
        return codcaja;
    }

    public void setCodcaja(String codcaja) {
        this.codcaja = codcaja;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCodVendedor() {
        return codVendedor;
    }

    public void setCodVendedor(String codVendedor) {
        this.codVendedor = codVendedor;
    }


    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Integer getNumInvitados() {
        return numInvitados;
    }

    public void setNumInvitados(Integer numInvitados) {
        this.numInvitados = numInvitados;
    }

    public Date getFechaHoraBoda() {
        return fechaHoraBoda;
    }

    public void setFechaHoraBoda(Date fechaHoraBoda) {
        this.fechaHoraBoda = fechaHoraBoda;
    }

    public Date getFechaContacto() {
        return fechaContacto;
    }

    public void setFechaContacto(Date fechaContacto) {
        this.fechaContacto = fechaContacto;
    }

    public BigDecimal getReservado() {
        return reservado;
    }

    public void setReservado(BigDecimal reservado) {
        this.reservado = reservado;
    }

    public BigDecimal getAbonadoUtilizado() {
        return abonadoUtilizado;
    }

    public void setAbonadoUtilizado(BigDecimal abonadoSinUsar) {
        this.abonadoUtilizado = abonadoSinUsar;
    }

    public BigDecimal getComprado() {
        return comprado;
    }

    public void setComprado(BigDecimal comprado) {
        this.comprado = comprado;
    }

    public Character getSolicitudLiquidacion() {
        return solicitudLiquidacion;
    }

    public void setSolicitudLiquidacion(Character solicitudLiquidacion) {
        this.solicitudLiquidacion = solicitudLiquidacion;
    }

    public Character getLiquidado() {
        return liquidado;
    }

    public void setLiquidado(Character liquidado) {
        this.liquidado = liquidado;
    }

    public Character getProcesado() {
        return procesado;
    }

    public void setProcesado(Character procesado) {
        this.procesado = procesado;
    }

    public Character getProcesadoTienda() {
        return procesadoTienda;
    }

    public void setProcesadoTienda(Character procesadoTienda) {
        this.procesadoTienda = procesadoTienda;
    }

    @XmlTransient
    public List<InvitadoPlanNovio> getInvitadoPlanNovioList() {
        return getListaInvitados();
    }

    public void setInvitadoPlanNovioList(List<InvitadoPlanNovio> invitadoPlanNovioList) {
        this.setListaInvitados(invitadoPlanNovioList);
    }

    @XmlTransient
    public List<AbonoPlanNovio> getAbonoPlanNovioList() {
        return getListaAbonos();
    }

    public void setAbonoPlanNovioList(List<AbonoPlanNovio> abonoPlanNovioList) {
        this.setListaAbonos(abonoPlanNovioList);
    }

    @XmlTransient
    public List<ArticuloPlanNovio> getArticuloPlanNovioList() {
        return getListaArticulos();
    }

    public void setArticuloPlanNovioList(List<ArticuloPlanNovio> articuloPlanNovioList) {
        this.setListaArticulos(articuloPlanNovioList);
    }

     @Override
    public int hashCode() {
        int hash = 0;
        hash += (planNovioPK != null ? planNovioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PlanNovio)) {
            return false;
        }
        PlanNovio other = (PlanNovio) object;
        if ((this.planNovioPK == null && other.planNovioPK != null) || (this.planNovioPK != null && !this.planNovioPK.equals(other.planNovioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.PlanNovio[ planNovioPK=" + planNovioPK + " ]";
    }

    public String getTitulo() {
        return "BODA:"+getMatrimonio();
    }
    
    public String getMatrimonio() {
        return Cadena.primerApellido(this.getNovio().getApellido())+"-"+Cadena.primerApellido(this.getNovia().getApellido());
    }
    
    public String getEstado() {
        if (this.isLiquidado()){
            return "LIQUIDADO";
        }
        else if (this.isCaducado()){
            return "CADUCADO";
        }
        else{
            return "ABIERTO";
        }               
     }

    public boolean isLiquidado() {
        return (this.liquidado !=null && this.liquidado=='S');
    }

    public boolean isCaducado() {
        ;
        Fecha fechaCaducidad = new Fecha(getCaducidad());
        Fecha fechaHoy = new Fecha();
        
        if (fechaHoy.despues(fechaCaducidad)){
            return true;
        }
        return false;     
    }
   
    public Cliente getNovio() {
        return novio;
    }

    public void setNovio(Cliente novio) {
        this.novio = novio;
    }

    public Cliente getNovia() {
        return novia;
    }

    public void setNovia(Cliente novia) {
        this.novia = novia;
    }

    public void addReservado(BigDecimal totalAPagar) {
        this.reservado.add(totalAPagar);
    }

    public void subtractReservado(BigDecimal totalAPagar) {
        this.reservado.subtract(totalAPagar);
    }

    public BigDecimal getAbonadoConDto() {
        return abonadoConDto;
    }

    public void setAbonadoConDto(BigDecimal abonadoConDto) {
        this.abonadoConDto = abonadoConDto;
    }

    public BigDecimal getAbonadoSinDto() {
        return abonadoSinDto;
    }

    public void setAbonadoSinDto(BigDecimal abonadoSinDto) {
        this.abonadoSinDto = abonadoSinDto;
    }

    public BigDecimal getAbonadoSinUtilizar() {
        //Calcular el valor que puede utilizar de ese local
        BigDecimal totalAbonoLocal = BigDecimal.ZERO;
        Boolean esPropietario = false;
        for(AbonoPlanNovio abonoPlanNovio : getAbonoPlanNovioList()){
            if(abonoPlanNovio.getAbonoPlanNovioPK().getCodAlmAbono().equals(Sesion.getTienda().getCodalm()) &&
                    (abonoPlanNovio.getEstadoLiquidacion() == null || !abonoPlanNovio.getEstadoLiquidacion().equals("L"))){
                totalAbonoLocal = totalAbonoLocal.add(abonoPlanNovio.getCantidadConDcto());
                if(Sesion.getTienda().getCodalm().equals(abonoPlanNovio.getAbonoPlanNovioPK().getCodalm())){
                   esPropietario = true;
                }
            }
        }
        if(esPropietario){
            return totalAbonoLocal.subtract(this.abonadoUtilizado);
        }else{
            return totalAbonoLocal;
        }
    }

    public List<InvitadoPlanNovio> getListaInvitados() {
        return listaInvitados;
    }

    public void setListaInvitados(List<InvitadoPlanNovio> listaInvitados) {
        this.listaInvitados = listaInvitados;
    }

    public List<AbonoPlanNovio> getListaAbonos() {
        return listaAbonos;
    }

    public void setListaAbonos(List<AbonoPlanNovio> listaAbonos) {
        this.listaAbonos = listaAbonos;
    }

    public List<ArticuloPlanNovio> getListaArticulos() {
        return listaArticulos;
    }

    public void setListaArticulos(List<ArticuloPlanNovio> listaArticulos) {
        this.listaArticulos = listaArticulos;
    }

    public String getCodPlanAsString() {
        return planNovioPK.getCodalm()+"-"+planNovioPK.getIdPlan();
    }

    public BigInteger getCodPlanAsNumber() {
        return new BigInteger(""+planNovioPK.getCodalm()+planNovioPK.getIdPlan());
    }

    public void refrescaTotales() {
        this.reservado = BigDecimal.ZERO;
        this.comprado = BigDecimal.ZERO;
        this.abonadoConDto = BigDecimal.ZERO;
        this.abonadoSinDto = BigDecimal.ZERO;
        this.abonadoUtilizado = BigDecimal.ZERO;
        
        // Listamos Artículos
        for (ArticuloPlanNovio art: getListaArticulos()){
            this.reservado = this.reservado.add(art.getPrecioTotal());
            // COMPRADO
            if (art.isComprado() || art.isPendienteEnvio() || art.isPendienteRecoger()){
                this.comprado = this.comprado.add(art.getTotalPagadoConDscto());
            }
            // ABONADO
            if ((art.isComprado() || art.isPendienteEnvio() || art.isPendienteRecoger()) && art.getInvitadoPlanNovio()==null){//               
                 this.abonadoUtilizado = this.abonadoUtilizado.add(art.getTotalPagadoConDscto());//                
            }            
        }
        // Listamos Abonos
        for (AbonoPlanNovio abn: listaAbonos){
            if(abn.getAnulado() == 'N'){
                this.abonadoConDto = this.abonadoConDto.add(abn.getCantidadConDcto());
                this.abonadoSinDto = this.abonadoSinDto.add(abn.getCantidadSinDcto());
            }
        }
        
    }

    public Date getCaducidad() {
        return caducidad;
    }

    public void setCaducidad(Date caducidad) {
        this.caducidad = caducidad;
    }

    public Date getFechaLiquidacion() {
        return fechaLiquidacion;
    }

    public void setFechaLiquidacion(Date fechaLiquidacion) {
        this.fechaLiquidacion = fechaLiquidacion;
    }

    public BigInteger getIdPlanSukasa() {
        return idPlanSukasa;
    }

    public void setIdPlanSukasa(BigInteger idPlanSukasa) {
        this.idPlanSukasa = idPlanSukasa;
    }

    public BigDecimal getImporteAlcanzado() {
        return this.getComprado().add(this.getAbonadoSinUtilizar());
    }

    public void removeTemporalArticulos(ArticuloPlanNovio articuloReservado) {
        getListaArticulos().remove(articuloReservado);
    }

    public void addTemporalListaArticulos(List<ArticuloPlanNovio> listaArt) {
        for (ArticuloPlanNovio art : listaArt){
           this.getListaArticulos().add(art);
       }
    }

    public void addTemporalAbono(AbonoPlanNovio nuevoAbono) {
        this.getAbonoPlanNovioList().add(nuevoAbono);
    }
    
    
    public boolean existeInvitado(InvitadoPlanNovio invitado){
        for (InvitadoPlanNovio invitadoPlanNovio : getInvitadoPlanNovioList()) {
            if (invitado.getNombre().equals(invitadoPlanNovio.getNombre())
                    && invitado.getApellido().equals(invitadoPlanNovio.getApellido())){
                return true;
            }
        }
        return false;
    }

    public String getPlanPantalla() {
        return planNovioPK.getCodalm() + " " + planNovioPK.getIdPlan();
    }
}
