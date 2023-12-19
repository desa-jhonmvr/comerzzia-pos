package com.comerzzia.jpos.persistencia.reservaciones.reserva;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.persistencia.reservaciones.reservaabono.ReservaAbonoBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo.ReservaArticuloBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservainvitados.ReservaInvitadoBean;
import com.comerzzia.jpos.persistencia.reservaciones.reservatipos.ReservaTiposBean;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class ReservaBean {
    
    private String uidReservacion;

    private String codcli;

    private String codTipo;

    private Fecha caducidad;

    private Fecha fechaAlta;

    private Boolean liquidado;

    private String codalm;

    private Boolean cancelado;

    private Boolean procesado;

    private String codcaja;

    private BigDecimal idUsuario;

    private BigDecimal cuotaInicial;

    private Fecha fechaLiquidacion;

    private String codvendedor;

    private Boolean procesadoTienda;

    private BigDecimal codReservacion;

    private String nombreOrganizadora;

    private String apellidosOrganizadora;

    private String telefonoOrganizadora;

    private String direccionEvento;

    private Fecha fechaHoraEvento;

    private String reservacionFact;

    private String nombreOrg;

    private String apellidosOrg;

    private String codcliente;
    
    private String tipoDocu;

    private String nombrecliente;

    private String apellidoscliente;
    
    private String codTipoReserva;
    
    private String desTipoReserva;
    
    private String bono;
    
    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    
    private List<ReservaArticuloBean> reservaArticuloList;
    
    private List<ReservaInvitadoBean> reservaInvitadoList;
    
    private List<ReservaAbonoBean> reservaAbonoList;
    
    private FacturacionTicketBean datosFacturacion;
    
    private Cliente cliente;
    
    private ReservaTiposBean reservaTipo;
    
    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------

    public ReservaBean() {
       this.reservaArticuloList = new LinkedList<ReservaArticuloBean>(); 
       this.reservaInvitadoList = new LinkedList<ReservaInvitadoBean>();
       this.reservaAbonoList = new LinkedList<ReservaAbonoBean>();
       this.reservaTipo = new ReservaTiposBean();       
    }
    
    public String getUidReservacion() {
        return uidReservacion;
    }

    public void setUidReservacion(String uidReservacion) {
        this.uidReservacion = uidReservacion == null ? null : uidReservacion.trim();
    }

    public String getCodcli() {
        return codcli;
    }

    public void setCodcli(String codcli) {
        this.codcli = codcli == null ? null : codcli.trim();
    }

    public String getCodTipo() {
        return codTipo;
    }

    public void setCodTipo(String codTipo) {
        this.codTipo = codTipo == null ? null : codTipo.trim();
    }

    public Fecha getCaducidad() {
        return caducidad;
    }

    public void setCaducidad(Fecha caducidad) {
        this.caducidad = caducidad;
    }

    public Fecha getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Fecha fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Boolean getLiquidado() {
        return liquidado;
    }

    public void setLiquidado(Boolean liquidado) {
        this.liquidado = liquidado;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm == null ? null : codalm.trim();
    }

    public Boolean getCancelado() {
        return cancelado;
    }

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }

    public Boolean getProcesado() {
        return procesado;
    }

    public void setProcesado(Boolean procesado) {
        this.procesado = procesado;
    }

    public String getCodcaja() {
        return codcaja;
    }

    public void setCodcaja(String codcaja) {
        this.codcaja = codcaja == null ? null : codcaja.trim();
    }

    public BigDecimal getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(BigDecimal idUsuario) {
        this.idUsuario = idUsuario;
    }

    public BigDecimal getCuotaInicial() {
        return cuotaInicial;
    }

    public void setCuotaInicial(BigDecimal cuotaInicial) {
        this.cuotaInicial = cuotaInicial;
    }

    public Fecha getFechaLiquidacion() {
        return fechaLiquidacion;
    }

    public void setFechaLiquidacion(Fecha fechaLiquidacion) {
        this.fechaLiquidacion = fechaLiquidacion;
    }

    public String getCodvendedor() {
        return codvendedor;
    }

    public void setCodvendedor(String codvendedor) {
        this.codvendedor = codvendedor == null ? null : codvendedor.trim();
    }

    public Boolean getProcesadoTienda() {
        return procesadoTienda;
    }

    public void setProcesadoTienda(Boolean procesadoTienda) {
        this.procesadoTienda = procesadoTienda;
    }

    public BigDecimal getCodReservacion() {
        return codReservacion;
    }

    public void setCodReservacion(BigDecimal codReservacion) {
        this.codReservacion = codReservacion;
    }

    public String getNombreOrganizadora() {
        return nombreOrganizadora;
    }

    public void setNombreOrganizadora(String nombreOrganizadora) {
        this.nombreOrganizadora = nombreOrganizadora == null ? null : nombreOrganizadora.trim();
    }

    public String getApellidosOrganizadora() {
        return apellidosOrganizadora;
    }

    public void setApellidosOrganizadora(String apellidosOrganizadora) {
        this.apellidosOrganizadora = apellidosOrganizadora == null ? null : apellidosOrganizadora.trim();
    }

    public String getTelefonoOrganizadora() {
        return telefonoOrganizadora;
    }

    public void setTelefonoOrganizadora(String telefonoOrganizadora) {
        this.telefonoOrganizadora = telefonoOrganizadora == null ? null : telefonoOrganizadora.trim();
    }

    public String getDireccionEvento() {
        return direccionEvento;
    }

    public void setDireccionEvento(String direccionEvento) {
        this.direccionEvento = direccionEvento == null ? null : direccionEvento.trim();
    }

    public Fecha getFechaHoraEvento() {
        return fechaHoraEvento;
    }

    public void setFechaHoraEvento(Fecha fechaHoraEvento) {
        this.fechaHoraEvento = fechaHoraEvento;
    }

    public String getReservacionFact() {
        return reservacionFact;
    }

    public void setReservacionFact(String reservacionFact) {
        this.reservacionFact = reservacionFact == null ? null : reservacionFact.trim();
    }

    public String getNombreOrg() {
        return nombreOrg;
    }

    public void setNombreOrg(String nombreOrg) {
        this.nombreOrg = nombreOrg == null ? null : nombreOrg.trim();
    }

    public String getApellidosOrg() {
        return apellidosOrg;
    }

    public void setApellidosOrg(String apellidosOrg) {
        this.apellidosOrg = apellidosOrg == null ? null : apellidosOrg.trim();
    }

    public String getCodcliente() {
        return codcliente;
    }

    public void setCodcliente(String codcliente) {
        this.codcliente = codcliente == null ? null : codcliente.trim();
    }
    
    public String getTipoDocu() {
        return tipoDocu;
    }
    
    public void setTipoDocu(String tipoDocu) {
        this.tipoDocu = tipoDocu == null ? null : tipoDocu.trim();
    }

    public String getNombrecliente() {
        return nombrecliente;
    }

    public void setNombrecliente(String nombrecliente) {
        this.nombrecliente = nombrecliente == null ? null : nombrecliente.trim();
    }

    public String getApellidoscliente() {
        return apellidoscliente;
    }

    public void setApellidoscliente(String apellidoscliente) {
        this.apellidoscliente = apellidoscliente == null ? null : apellidoscliente.trim();
    }

    public String getCodTipoReserva() {
        return codTipoReserva;
    }

    public void setCodTipoReserva(String codTipoReserva) {
        this.codTipoReserva = codTipoReserva;
    }

    public String getDesTipoReserva() {
        return desTipoReserva;
    }

    public void setDesTipoReserva(String desTipoReserva) {
        this.desTipoReserva = desTipoReserva;
    } 

    //INICIO M�TODOS PERSONALIZADOS--------------------------------------------
    
    public List<ReservaArticuloBean> getReservaArticuloList() {
        return reservaArticuloList;
    }

    public void setReservaArticuloList(List<ReservaArticuloBean> reservaArticuloList) {
        this.reservaArticuloList = reservaArticuloList;
    }

    public List<ReservaInvitadoBean> getReservaInvitadoList() {
        return reservaInvitadoList;
    }

    public void setReservaInvitadoList(List<ReservaInvitadoBean> reservaInvitadoList) {
        this.reservaInvitadoList = reservaInvitadoList;
    }

    public List<ReservaAbonoBean> getReservaAbonoList() {
        return reservaAbonoList;
    }

    public void setReservaAbonoList(List<ReservaAbonoBean> reservaAbonoList) {
        this.reservaAbonoList = reservaAbonoList;
    }

    public FacturacionTicketBean getDatosFacturacion() {
        return datosFacturacion;
    }

    public void setDatosFacturacion(FacturacionTicketBean datosFacturacion) {
        this.datosFacturacion = datosFacturacion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public ReservaTiposBean getReservaTipo() {
        return reservaTipo;
    }

    public void setReservaTipo(ReservaTiposBean reservaTipo) {
        this.reservaTipo = reservaTipo;
    }    

    public boolean isTipoBabyShower(){
        return reservaTipo.getCodTipo().equals("03");
    }
    public boolean isTipoCanastillas(){
        return reservaTipo.getCodTipo().equals("02");
    }
    public boolean isTipoConAbonoInicial(){
        return reservaTipo.getCodTipo().equals("00");
    }
    public boolean isTipoSeparacion(){
        return reservaTipo.getCodTipo().equals("01");
    }

    public boolean isAbierta() {
        return !isLiquidado() && !isCancelada() && !isCaducada();
    }

    public boolean isCaducada() {
        return getCaducidad().antes(new Fecha());
    }

    public boolean isLiquidado() {
        return getLiquidado();
    }

    public boolean isCancelada() {
        return this.getCancelado();
    }

    public Boolean isBono() {
       if(bono == null){
           return false;
       }
       return bono.equals("S");
    }

    public String getBono() {
        return bono;
    }
    
    public void setBono(String bono) {
        this.bono = bono;
    }    
    
    public String getEstado() {
        if (isCancelada()) {
            return "Anulada";
        }
        if (isLiquidado()) {
            return "Liquidada";
        }
        if (isCaducada()) {
            return "Caducada";
        }
        else {
            return "Abierta";
        }
    }
    
    public String getFechaCaducidadAsString() {
        Fecha f = this.getCaducidad();
        return f.getString("dd-MMM-yyyy");
    }    

    public String getIdRes() {
        String res = codalm;
        String codres = Numero.completaconCeros(String.valueOf(codReservacion), 8);
        return res+"-"+codres;
    }
    
    public String getIdResSinCeros() {
        String res = codalm;
        String codres = String.valueOf(codReservacion);
        return res+"-"+codres;
    }
    
    //FIN M�TODOS PERSONALIZADOS-----------------------------------------------
    
}