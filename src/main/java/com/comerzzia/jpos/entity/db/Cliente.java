/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.db;

import com.comerzzia.jpos.servicios.afiliacion.ITarjetaAfiliacion;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.promociones.puntos.ServicioPuntos;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Cacheable(false)
@Table(name = "D_CLIENTES_TBL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c"),
    @NamedQuery(name = "Cliente.findByPoblacion", query = "SELECT c FROM Cliente c WHERE c.poblacion = :poblacion"),
    @NamedQuery(name = "Cliente.findByProvincia", query = "SELECT c FROM Cliente c WHERE c.provincia = :provincia"),
    @NamedQuery(name = "Cliente.findByCp", query = "SELECT c FROM Cliente c WHERE c.cp = :cp"),
    @NamedQuery(name = "Cliente.findByEmail", query = "SELECT c FROM Cliente c WHERE c.email = :email"),
    @NamedQuery(name = "Cliente.findByCif", query = "SELECT c FROM Cliente c WHERE c.cif = :cif"),
    @NamedQuery(name = "Cliente.findByBanco", query = "SELECT c FROM Cliente c WHERE c.banco = :banco"),
    @NamedQuery(name = "Cliente.findByBancoDomicilio", query = "SELECT c FROM Cliente c WHERE c.bancoDomicilio = :bancoDomicilio"),
    @NamedQuery(name = "Cliente.findByBancoPoblacion", query = "SELECT c FROM Cliente c WHERE c.bancoPoblacion = :bancoPoblacion"),
    @NamedQuery(name = "Cliente.findByCcc", query = "SELECT c FROM Cliente c WHERE c.ccc = :ccc"),
    @NamedQuery(name = "Cliente.findByActivo", query = "SELECT c FROM Cliente c WHERE c.activo = :activo"),
    @NamedQuery(name = "Cliente.findByFechaAlta", query = "SELECT c FROM Cliente c WHERE c.fechaAlta = :fechaAlta"),
    @NamedQuery(name = "Cliente.findByFechaBaja", query = "SELECT c FROM Cliente c WHERE c.fechaBaja = :fechaBaja"),
    @NamedQuery(name = "Cliente.findByRiesgoConcedido", query = "SELECT c FROM Cliente c WHERE c.riesgoConcedido = :riesgoConcedido"),
    @NamedQuery(name = "Cliente.findByListaNegra", query = "SELECT c FROM Cliente c WHERE c.listaNegra = :listaNegra"),
    @NamedQuery(name = "Cliente.findByDireccionIncorrecta", query = "SELECT c FROM Cliente c WHERE c.direccionIncorrecta = :direccionIncorrecta"),
    @NamedQuery(name = "Cliente.findByEmailIncorrecto", query = "SELECT c FROM Cliente c WHERE c.emailIncorrecto = :emailIncorrecto"),
    @NamedQuery(name = "Cliente.findByTelefono1Incorrecto", query = "SELECT c FROM Cliente c WHERE c.telefono1Incorrecto = :telefono1Incorrecto"),
    @NamedQuery(name = "Cliente.findByTelefono2Incorrecto", query = "SELECT c FROM Cliente c WHERE c.telefono2Incorrecto = :telefono2Incorrecto"),
    @NamedQuery(name = "Cliente.findByTelefonoMovilIncorrecto", query = "SELECT c FROM Cliente c WHERE c.telefonoMovilIncorrecto = :telefonoMovilIncorrecto"),
    @NamedQuery(name = "Cliente.findByRecibirLlamadas", query = "SELECT c FROM Cliente c WHERE c.recibirLlamadas = :recibirLlamadas"),
    @NamedQuery(name = "Cliente.findByRecibirEmails", query = "SELECT c FROM Cliente c WHERE c.recibirEmails = :recibirEmails"),
    @NamedQuery(name = "Cliente.findByPreafiliado", query = "SELECT c FROM Cliente c WHERE c.preafiliado = :preafiliado"),
    @NamedQuery(name = "Cliente.findBySocio", query = "SELECT c FROM Cliente c WHERE c.socio = :socio"),
    @NamedQuery(name = "Cliente.findByOperadorTlfnoMovil", query = "SELECT c FROM Cliente c WHERE c.operadorTlfnoMovil = :operadorTlfnoMovil"),
    @NamedQuery(name = "Cliente.findByInfoExtra2", query = "SELECT c FROM Cliente c WHERE c.infoExtra2 = :infoExtra2"),
    @NamedQuery(name = "Cliente.findByInfoExtra3", query = "SELECT c FROM Cliente c WHERE c.infoExtra3 = :infoExtra3"),
    @NamedQuery(name = "Cliente.findByInfoExtra4", query = "SELECT c FROM Cliente c WHERE c.infoExtra4 = :infoExtra4"),
    @NamedQuery(name = "Cliente.findByTipoIdentificacion", query = "SELECT c FROM Cliente c WHERE c.tipoIdentificacion = :tipoIdentificacion"),
    @NamedQuery(name = "Cliente.findByFechaNacimiento", query = "SELECT c FROM Cliente c WHERE c.fechaNacimiento = :fechaNacimiento"),
    @NamedQuery(name = "Cliente.findBySemanaEmbarazo", query = "SELECT c FROM Cliente c WHERE c.semanaEmbarazo = :semanaEmbarazo"),
    @NamedQuery(name = "Cliente.findByFechaNacimientoUltimoHijo", query = "SELECT c FROM Cliente c WHERE c.fechaNacimientoUltimoHijo = :fechaNacimientoUltimoHijo"),
    @NamedQuery(name = "Cliente.findByEstadoCivil", query = "SELECT c FROM Cliente c WHERE c.estadoCivil = :estadoCivil"),
    @NamedQuery(name = "Cliente.findByGenero", query = "SELECT c FROM Cliente c WHERE c.genero = :genero")})
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODCLI")
    private String identificacion;
    @Basic(optional = false)
    @Column(name = "DESCLI")
    private String apellido; // modificado respecto al estandar de Comerzzia
    @Column(name = "NOMBRE_COMERCIAL")
    private String nombre;  // modificado respecto al estandar de Comerzzia
    @Column(name = "DOMICILIO")
    private String direccion;  // modificado respecto al estandar de Comerzzia
    @Column(name="DOMICILIO_TRABAJO")
    private String direccionTrabajo; 
    @Column(name = "POBLACION")
    private String poblacion;
    @Column(name = "PROVINCIA")
    private String provincia;
    @Column(name = "CP")
    private String cp;
    @Column(name = "TELEFONO1")
    private String telefonoParticular;  // modificado respecto al estandar de Comerzzia
    @Column(name = "TELEFONO2")
    private String telefonoTrabajo;  // modificado respecto al estandar de Comerzzia
    @Column(name = "FAX")
    private String telefonoMovil;  // modificado respecto al estandar de Comerzzia
    @Column(name = "PERSONA_CONTACTO")
    private String codigoTarjetaBabysClub;  // modificado respecto al estandar de Comerzzia
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "CIF")
    private String cif;
    @Column(name = "BANCO")
    private String banco;
    @Column(name = "BANCO_DOMICILIO")
    private String bancoDomicilio;
    @Column(name = "BANCO_POBLACION")
    private String bancoPoblacion;
    @Column(name = "CCC")
    private String ccc;
    @Column(name = "OBSERVACIONES")
    private String infoExtra1;  // modificado respecto al estandar de Comerzzia
    @Basic(optional = false)
    @Column(name = "ACTIVO")
    private Character activo;
    @Column(name = "FECHA_ALTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
    @Column(name = "FECHA_BAJA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaBaja;
    @Column(name = "RIESGO_CONCEDIDO")
    private Integer riesgoConcedido;
    @Column(name = "ID_TRAT_IMPUESTOS")
    private Integer idTratImpuestos;
    @Column(name = "LISTA_NEGRA")
    private Character listaNegra;
    @Column(name = "TIPO_CLIENTE")
    private Long tipoCliente;
    @Column(name = "DIRECCION_INCORRECTA")
    private Character direccionIncorrecta;
    @Column(name = "EMAIL_INCORRECTO")
    private Character emailIncorrecto;
    @Column(name = "TELEFONO_1_INCORRECTO")
    private Character telefono1Incorrecto;
    @Column(name = "TELEFONO_2_INCORRECTO")
    private Character telefono2Incorrecto;
    @Column(name = "TELEFONO_MOVIL_INCORRECTO")
    private Character telefonoMovilIncorrecto;
    @Column(name = "RECIBIR_LLAMADAS")
    private Character recibirLlamadas;
    @Column(name = "RECIBIR_EMAILS")
    private Character recibirEmails;
    @Column(name = "PREAFILIADO")
    private Character preafiliado;
    @Column(name = "SOCIO")
    private Character socio;
    @Column(name = "PROCESADO")
    private Character procesado;
    @Column(name = "OPERADOR_TLFNO_MOVIL")
    private String operadorTlfnoMovil;
    @Column(name = "INFO_EXTRA_2")
    private String infoExtra2;
    @Column(name = "INFO_EXTRA_3")
    private String infoExtra3;
    @Column(name = "INFO_EXTRA_4")
    private String infoExtra4;
    @Column(name = "TIPO_IDENTIFICACION")
    private String tipoIdentificacion;
    @Column(name = "FECHA_NACIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNacimiento;
    @Column(name = "SEMANA_EMBARAZO")
    private BigInteger semanaEmbarazo;
    @Column(name = "FECHA_NACIMIENTO_ULTIMO_HIJO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNacimientoUltimoHijo;
    @Column(name = "ESTADO_CIVIL")
    private String estadoCivil;
    @Column(name = "GENERO")
    private Character genero;
    @Column(name = "COD_TIPO_AFILIADO")
    private String tipoAfiliado;
    @Column(name = "NUM_COMPRAS")
    private Integer numCompras;
    @Column(name = "IMPORTE_COMPRAS")
    private BigDecimal importeCompras;
    @Transient
    private Integer puntos;
    @Transient
    private Boolean aplicaTarjetaAfiliado;
    @Transient
    private ITarjetaAfiliacion tarjetaAfiliacion;
    @Transient
    private Integer numeroFacturas;

    public Cliente() {
        this.procesado = 'N';
    }

    public Cliente(String codcli) {
        this.identificacion = codcli;
        this.procesado = 'N';
    }

    public Cliente(String codcli, String tipo) {
        this.identificacion = codcli;
        this.tipoIdentificacion = tipo;
        this.setGenero(' ');
        this.setEstadoCivil("CASADO");
        this.setPreafiliado('N');
        this.socio = 'N';
        this.setOperadorTlfnoMovil("");
        this.setRecibirEmails('S');
        this.setRecibirLlamadas('S');
        this.setSemanaEmbarazo(null);
        this.setActivo('N');
        this.setIdTratImpuestos(1);
        this.procesado = 'N';
        this.direccionIncorrecta = 'N';
        this.emailIncorrecto = 'N';
        this.telefono1Incorrecto = 'N';
        this.telefonoMovilIncorrecto = 'N';
        this.apellido = "";
        this.tipoCliente = Variables.getVariableAsLong(Variables.TIPO_CLIENTE_DEFECTO);
    }

    public Cliente(String codcli, String descli, char activo) {
        this.identificacion = codcli;
        this.apellido = descli;
        this.activo = activo;
        this.procesado = 'N';
    }

    public String getCodcli() {
        return getIdentificacion();
    }

    public void setCodcli(String codcli) {
        this.setIdentificacion(codcli);
    }

    public String getDescli() {
        return getApellido();
    }

    public void setDescli(String descli) {
        this.setApellido(descli);
    }

    public String getNombreComercial() {
        return getNombre();
    }

    public void setNombreComercial(String nombreComercial) {
        this.setNombre(nombreComercial);
    }

    public String getDomicilio() {
        return getDireccion();
    }

    public String getDomicilioImpresion() {
        String rep;
        rep = direccion.replaceAll("&", "&amp;");
        rep = rep.replaceAll("<", "&lt;");
        rep = rep.replaceAll(">", "&gt;");
        rep = rep.replaceAll("'", "&apos;");
        rep = rep.replaceAll("\"", "&quot;");
        return rep;
    }

    public void setDomicilio(String domicilio) {
        this.setDireccion(domicilio);
    }

    public String getDireccionTrabajo() {
        return direccionTrabajo;
    }
    
    public String getDireccionTrabajoImpresion() {
        String rep;
        rep = direccionTrabajo.replaceAll("&", "&amp;");
        rep = rep.replaceAll("<", "&lt;");
        rep = rep.replaceAll(">", "&gt;");
        rep = rep.replaceAll("'", "&apos;");
        rep = rep.replaceAll("\"", "&quot;");
        return rep;
    }
    
    public void setDireccionTrabajo(String direccionTrabajo) {
        this.direccionTrabajo = direccionTrabajo;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getTelefono1() {
        return getTelefonoParticular();
    }

    public void setTelefono1(String telefono1) {
        this.setTelefonoParticular(telefono1);
    }

    public String getTelefono2() {
        return getTelefonoTrabajo();
    }

    public void setTelefono2(String telefono2) {
        this.setTelefonoTrabajo(telefono2);
    }
    
    public String getTelefonoFacturacion(){
        if (getTelefono1() !=null && getTelefono1().length()>2){
             return getTelefono1();         
        }
        else{
             if (getTelefonoMovil() !=null){
                return getTelefonoMovil();
            }
            else{
                return "Tlf No Enc";
            } 
        }
    }

    public String getFax() {
        return getTelefonoMovil();
    }

    public void setFax(String fax) {
        this.setTelefonoMovil(fax);
    }

    public String getPersonaContacto() {
        return getCodigoTarjetaBabysClub();
    }

    public void setPersonaContacto(String personaContacto) {
        this.setCodigoTarjetaBabysClub(personaContacto);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getBancoDomicilio() {
        return bancoDomicilio;
    }

    public void setBancoDomicilio(String bancoDomicilio) {
        this.bancoDomicilio = bancoDomicilio;
    }

    public String getBancoPoblacion() {
        return bancoPoblacion;
    }

    public void setBancoPoblacion(String bancoPoblacion) {
        this.bancoPoblacion = bancoPoblacion;
    }

    public String getCcc() {
        return ccc;
    }

    public void setCcc(String ccc) {
        this.ccc = ccc;
    }

    public String getObservaciones() {
        return getInfoExtra1();
    }

    public void setObservaciones(String observaciones) {
        this.setInfoExtra1(observaciones);
    }

    public Character getActivo() {
        return activo;
    }

    public void setActivo(char activo) {
        this.activo = activo;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Integer getRiesgoConcedido() {
        return riesgoConcedido;
    }

    public void setRiesgoConcedido(Integer riesgoConcedido) {
        this.riesgoConcedido = riesgoConcedido;
    }

    public Character getListaNegra() {
        if (listaNegra == null) {
            return 'N';
        }
        return listaNegra;
    }

    public void setListaNegra(Character listaNegra) {
        this.listaNegra = listaNegra;
    }

    public Long getTipoCliente() {
        
        // Si estamos en bebemundo y hay tarjeta supermaxi seleccionada, devolvemos tipo de cliente supermaxi.
        if (Sesion.isBebemundo() && isAplicaTarjetaAfiliado()){
            return Variables.getVariableAsLong(Variables.POS_CONFIG_TIPO_CLIENTE_SUPERMAXI);
        }
        // En otros casos.
        return tipoCliente;
        
        
    }

    public void setTipoCliente(Long tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public Character getDireccionIncorrecta() {
        if (direccionIncorrecta == null) {
            return 'N';
        }
        return direccionIncorrecta;
    }

    public void setDireccionIncorrecta(Character direccionIncorrecta) {
        this.direccionIncorrecta = direccionIncorrecta;
    }

    public Character getEmailIncorrecto() {
        if (emailIncorrecto == null) {
            return 'N';
        }
        return emailIncorrecto;
    }

    public void setEmailIncorrecto(Character emailIncorrecto) {
        this.emailIncorrecto = emailIncorrecto;
    }

    public Character getTelefono1Incorrecto() {
        if (telefono1Incorrecto == null) {
            return 'N';
        }
        return telefono1Incorrecto;
    }

    public void setTelefono1Incorrecto(Character telefono1Incorrecto) {
        this.telefono1Incorrecto = telefono1Incorrecto;
    }

    public Character getTelefono2Incorrecto() {
        if (telefono2Incorrecto == null) {
            return 'N';
        }
        return telefono2Incorrecto;
    }

    public void setTelefono2Incorrecto(Character telefono2Incorrecto) {
        this.telefono2Incorrecto = telefono2Incorrecto;
    }

    public Character getTelefonoMovilIncorrecto() {
        if (telefonoMovilIncorrecto == null) {
            return 'N';
        }
        return telefonoMovilIncorrecto;
    }

    public void setTelefonoMovilIncorrecto(Character telefonoMovilIncorrecto) {
        this.telefonoMovilIncorrecto = telefonoMovilIncorrecto;
    }

    public Character getRecibirLlamadas() {
        if (recibirLlamadas == null) {
            return 'N';
        }
        return recibirLlamadas;
    }

    public void setRecibirLlamadas(Character recibirLlamadas) {
        this.recibirLlamadas = recibirLlamadas;
    }

    public Character getRecibirEmails() {
        if (recibirEmails == null) {
            return 'N';
        }
        return recibirEmails;
    }

    public void setRecibirEmails(Character recibirEmails) {
        this.recibirEmails = recibirEmails;
    }

    public Character getPreafiliado() {
        if (preafiliado == null) {
            return 'N';
        }
        return preafiliado;
    }

    public void setPreafiliado(Character preafiliado) {
        this.preafiliado = preafiliado;
    }

    public Character getSocio() {
        if (socio == null) {
            return 'N';
        }
        return socio;
    }

    public String getOperadorTlfnoMovil() {
        return operadorTlfnoMovil;
    }

    public void setOperadorTlfnoMovil(String operadorTlfnoMovil) {
        this.operadorTlfnoMovil = operadorTlfnoMovil;
    }

    public String getInfoExtra2() {
        return infoExtra2;
    }

    public void setInfoExtra2(String infoExtra2) {
        this.infoExtra2 = infoExtra2;
    }

    public String getInfoExtra3() {
        return infoExtra3;
    }

    public void setInfoExtra3(String infoExtra3) {
        this.infoExtra3 = infoExtra3;
    }

    public String getInfoExtra4() {
        return infoExtra4;
    }

    public void setInfoExtra4(String infoExtra4) {
        this.infoExtra4 = infoExtra4;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public BigInteger getSemanaEmbarazo() {
        return semanaEmbarazo;
    }

    public void setSemanaEmbarazo(BigInteger semanaEmbarazo) {
        this.semanaEmbarazo = semanaEmbarazo;
    }

    public Date getFechaNacimientoUltimoHijo() {
        return fechaNacimientoUltimoHijo;
    }

    public void setFechaNacimientoUltimoHijo(Date fechaNacimientoUltimoHijo) {
        this.fechaNacimientoUltimoHijo = fechaNacimientoUltimoHijo;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Character getGenero() {
        return genero;
    }

    public void setGenero(Character genero) {
        this.genero = genero;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getIdentificacion() != null ? getIdentificacion().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.getIdentificacion() == null && other.getIdentificacion() != null) || (this.getIdentificacion() != null && !this.identificacion.equals(other.identificacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CODCLI: " + getIdentificacion();
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getApellido() {
        return apellido;
    }

    public String getApellidoImpresion() {
        String rep;
        rep = apellido.replaceAll("&", "&amp;");
        rep = rep.replaceAll("<", "&lt;");
        rep = rep.replaceAll(">", "&gt;");
        rep = rep.replaceAll("'", "&apos;");
        rep = rep.replaceAll("\"", "&quot;");
        return rep;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreImpresion() {
        String rep;
        rep = nombre.replaceAll("&", "&amp;");
        rep = rep.replaceAll("<", "&lt;");
        rep = rep.replaceAll(">", "&gt;");
        rep = rep.replaceAll("'", "&apos;");
        rep = rep.replaceAll("\"", "&quot;");
        return rep;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefonoParticular() {
        return telefonoParticular;
    }

    public void setTelefonoParticular(String telefonoParticular) {
        this.telefonoParticular = telefonoParticular;
    }

    public String getTelefonoTrabajo() {
        return telefonoTrabajo;
    }

    public void setTelefonoTrabajo(String telefonoTrabajo) {
        this.telefonoTrabajo = telefonoTrabajo;
    }

    public String getTelefonoMovil() {
        return telefonoMovil;
    }

    public void setTelefonoMovil(String telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    public String getInfoExtra1() {
        return infoExtra1;
    }

    public void setInfoExtra1(String infoExtra1) {
        this.infoExtra1 = infoExtra1;
    }

    public String getCodigoTarjetaBabysClub() {
        return codigoTarjetaBabysClub;
    }

    public void setCodigoTarjetaBabysClub(String codigoTarjetaBabysClub) {
        this.codigoTarjetaBabysClub = codigoTarjetaBabysClub;
    }

    public Integer getIdTratImpuestos() {
        return idTratImpuestos;
    }

    public void setIdTratImpuestos(Integer idTratImpuestos) {
        this.idTratImpuestos = idTratImpuestos;
    }

    public boolean isSocio() {        
        
        if (Sesion.isSukasa()){
            return isAplicaTarjetaAfiliado();
        }
        else{
            // Si es bebemundo, devolvemos el tipo de socio
            return getSocio() != null && getSocio().equals('S');
        }
    }
    
    public boolean isSocioOrigen() {
      return getSocio() != null && getSocio().equals('S');  
    }

    public boolean isListaNegra() {
        return getListaNegra() != null && getListaNegra().equals('S');
    }

    public boolean isActivo() {
        return getActivo() != null && getActivo().equals('S');
    }

    public String getTipoAfiliado() {
        return tipoAfiliado;
    }

    public Character getProcesado() {
        return procesado;
    }

    public void setProcesado(Character procesado) {
        this.procesado = procesado;
    }

    public boolean isProcesado() {
        return getProcesado() != null && getProcesado().equals('S');
    }

    public String getNombreVenta() {
        String nombreVenta = this.getNombre();
        String bienvenido = "Bienvenido";
        Date fechaActual = new Date();
        int hora = fechaActual.getHours();
        if (hora >= 6 && hora < 12) {
            if (VariablesAlm.getVariable(VariablesAlm.UI_MENSAJE_DIAS) != null) {
                bienvenido = VariablesAlm.getVariable(VariablesAlm.UI_MENSAJE_DIAS);
            }
            else {
                bienvenido = "Buenos días";
            }
        }
        else if (hora >= 12 && hora < 19) {
            if (VariablesAlm.getVariable(VariablesAlm.UI_MENSAJE_TARDES) != null) {
                bienvenido = VariablesAlm.getVariable(VariablesAlm.UI_MENSAJE_TARDES);
            }
            else {
                bienvenido = "Buenas tardes";
            }
        }
        else {
            if (VariablesAlm.getVariable(VariablesAlm.UI_MENSAJE_NOCHES) != null) {
                bienvenido = VariablesAlm.getVariable(VariablesAlm.UI_MENSAJE_NOCHES);
            }
            else {
                bienvenido = "Buenas noches";
            }
        }


        if (!this.getNombre().equals("CLIENTE")) {
            if (nombreVenta.length() <= 25) {
                nombreVenta = "<html> <span style='font-size:80%;color: rgb(51, 153, 255);'> " + bienvenido + "</span>  <B>" + nombreVenta + "</B></html>";
            }
            else {
                nombreVenta = "<html><B>" + nombreVenta + "</B></html>";
            }
        }
        else {
            nombreVenta = "<html><span style='font-size:80%;color:rgb(51, 153, 255);'>" + bienvenido + "</span> </html>";
        }

        return nombreVenta;
    }

    public boolean isTelefonoIncorrecto() {
        return getTelefono1Incorrecto() != null && getTelefono1Incorrecto().equals('S');
    }

    public boolean isEmailIncorrecto() {
        return getEmailIncorrecto() != null && getEmailIncorrecto().equals('S');
    }

    public boolean isCelularIncorrecto() {
        return getTelefonoMovilIncorrecto() != null && getTelefonoMovilIncorrecto().equals('S');
    }

    public boolean isDireccionIncorrecta() {
        return getDireccionIncorrecta() != null && getDireccionIncorrecta().equals('S');
    }

    public boolean isClienteGenerico() {
        return getCodcli().equals(Variables.getVariable(Variables.POS_CONFIG_ID_CLIENTE_GENERICO));
    }

    public Integer getNumCompras() {
        return numCompras;
    }

    public void setNumCompras(Integer numCompras) {
        this.numCompras = numCompras;
    }

    public BigDecimal getImporteCompras() {
        return importeCompras;
    }

    public void setImporteCompras(BigDecimal importeCompras) {
        this.importeCompras = importeCompras;
    }

    public String getNombreYApellidos() {
        return this.nombre + " " + this.apellido;
    }

    public Integer obtenerPuntos() {
        if (puntos == null) {
            puntos = ServicioPuntos.consultarPuntosCliente(identificacion);
        }
        return puntos;
    }

    public boolean isAplicaTarjetaAfiliado() {
        return aplicaTarjetaAfiliado!= null && aplicaTarjetaAfiliado == true;
    }
    
    public boolean isSeleccionadoTarjetaAfiliado() {
        return aplicaTarjetaAfiliado!= null;
    }

    public void setAplicaTarjetaAfiliado(Boolean aplicaTarjetaSupermaxi) {
        this.aplicaTarjetaAfiliado = aplicaTarjetaSupermaxi;
    }

    public ITarjetaAfiliacion getTarjetaAfiliacion() {
        return tarjetaAfiliacion;
    }

    public void setTarjetaAfiliacion(ITarjetaAfiliacion tarjetaAfiliacion) {
        this.tarjetaAfiliacion = tarjetaAfiliacion;
    }


    public boolean isGarantiaExtendidaGratis(){
        if (getTipoAfiliado() == null){
            return false;
        }
        if (!isSocio()){
            return false;
        }
        return getTipoAfiliado().equalsIgnoreCase(Variables.getVariable(Variables.GARANTIA_EXTENDIDA_TIPO_AFILIADO_GRATIS));
    }

    public boolean isEmpleado() {
        return getTipoCliente().equals(Variables.getVariableAsLong(Variables.POS_CONFIG_TIPO_CLIENTE_EMPLEADO));
    }

    public Integer getNumeroFacturas() {
        return numeroFacturas;
    }

    public void setNumeroFacturas(Integer numeroFacturas) {
        this.numeroFacturas = numeroFacturas;
    }
    
    public boolean isTipoRuc() {
        return tipoIdentificacion.equals("RUC") || tipoIdentificacion.equals("RUN") || tipoIdentificacion.equals("RUJ");
    }
}
