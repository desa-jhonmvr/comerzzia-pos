/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.clientes;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.LogOperaciones;
import com.comerzzia.jpos.persistencia.clientes.ClientesDao;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.servicios.core.permisos.Operaciones;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.login.Sesion;
import static com.comerzzia.jpos.servicios.logs.logsacceso.ServicioLogAcceso.MODIFICAR_CELULAR_CLIENTE;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import es.mpsistemas.util.log.Logger;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.persistence.NoResultException;

/**
 *
 * @author MGRI
 */
public class ClientesServices {

    protected static Logger log = Logger.getMLogger(ClientesServices.class);
    private static ClientesDao cldao = new ClientesDao();
    private static ClientesServices instance;

    @Deprecated
    /** Utilizar ClientesServices.getInstance() */
    public ClientesServices() {
    }

    public static ClientesServices getInstance() {
        if (instance == null) {
            instance = new ClientesServices();
        }
        return instance;
    }

    /**
     *  Consulta los datos de un cliente en función de su documentacion
     * @param identificacion
     * @param tipo
     * @return
     * @throws ClienteException 
     */
    public Cliente consultaClienteDoc(String identificacion, String tipo) throws ClienteException, NoResultException {
        log.debug("consultaClienteDoc() - Consultando cliente con tipo documento: " + tipo + " y documento: " + identificacion);
        try {
            return cldao.consultaCliente(identificacion, tipo);
        }
        catch (NoResultException e) {
            throw e;
        }
        catch (ClienteException e) {
            log.error("consultaClienteDoc() - Error consultando cliente por número documento: " + e.getMessage(), e);
            throw e;
        }
        catch (Exception e) {
            log.error("consultaClienteDoc() - Error consultando cliente por número documento: " + e.getMessage(), e);
            throw new ClienteException("Error consultando cliente por número documento.", e);
        }
    }

    public Cliente consultaClienteIdenti(String identificacion) throws ClienteException, NoResultException {
        log.debug("consultaClienteIdenti() - Consultando cliente con documento: " + identificacion);
        try {
            return cldao.consultaClienteIdent(identificacion);
        }
        catch (NoResultException e) {
            throw e;
        }
        catch (ClienteException e) {
            log.error("consultaClienteDoc() - Error consultando cliente por número documento: " + e.getMessage(), e);
            throw e;
        }
        catch (Exception e) {
            log.error("consultaClienteDoc() - Error consultando cliente por número documento: " + e.getMessage(), e);
            throw new ClienteException("Error consultando cliente por número documento.", e);
        }
    }

    /**
     *  Consulta los datos de un cliente en función de su numero de afiliacion
     * @param identificacion
     * @param tipo
     * @return
     * @throws ClienteException 
     */
    public Cliente consultaClienteNAfil(String nAfil) throws ClienteException {
        log.debug("consultaClienteNAfil() - Consultando cliente con número afiliación: " + nAfil);
        try {
            return cldao.consultaClienteNAfil(nAfil);
        }
        catch (NoResultException e) {
            throw e;
        }
        catch (ClienteException e) {
            log.error("consultaClienteNAfil() - Error consultando cliente por número afiliación: " + e.getMessage(), e);
            throw e;
        }
        catch (Exception e) {
            log.error("consultaClienteNAfil() - Error consultando cliente por número afiliación: " + e.getMessage(), e);
            throw new ClienteException("Error consultando cliente por número afiliación.", e);
        }
    }

    public List<Cliente> consultar(ParamBuscarClientes param, String nmax) throws ClienteException, ClienteLimitExceededException, SQLException {
        log.debug("consultar() - Consultando clientes con CC/RUC/Pasaporte: " + param.getCodcli() + ", nombre comercial:" + param.getNombre_com() + ", descripción: " + param.getDescli());
        int resultadosMaximos = Integer.parseInt(nmax);
        Connection conn = new Connection();
        try {
            conn.abrirConexion(Database.getConnection());
            if (resultadosMaximos >= cldao.consultarClientesCount(conn,param)) {
                List<Cliente> lista =  cldao.consultarClientes(conn, param);
                for(Cliente c:lista){
                    c.setNumeroFacturas(DocumentosService.consultarNumFacturas(c.getCodcli(), DocumentosBean.FACTURA));
                }
                return lista;
            }
            else {
                throw new ClienteLimitExceededException();
            }
        }
        catch (ClienteLimitExceededException e) {
            throw e;
        }
        catch (SQLException e) {
            log.error("consultar() - Error consultando clientes con CC/RUC/Pasaporte: " + param.getCodcli() + ", nombre comercial:" + param.getNombre_com() + ", descripción: " + param.getDescli());
            throw e;
        }
        catch (Exception e) {
            log.error("consultar() - Error consultando clientes con CC/RUC/Pasaporte: " + param.getCodcli() + ", nombre comercial:" + param.getNombre_com() + ", descripción: " + param.getDescli());
            throw new ClienteException("Error consultando clientes con CC/RUC/Pasaporte: " + param.getCodcli() + ", nombre comercial:" + param.getNombre_com() + ", descripción: " + param.getDescli());
        }
        finally{
            conn.cerrarConexion();
        }
    }

    public void nuevoCliente(Cliente clienteConsultado) throws ClienteException {
        clienteConsultado.setActivo('S'); // Se activa al cliente en su creación
        clienteConsultado.setProcesado('N'); // por si acaso, pero se setea en el constructor
        clienteConsultado.setFechaAlta(new Date());
        clienteConsultado.setCodcli(clienteConsultado.getCodcli().toUpperCase());
        cldao.nuevoCliente(clienteConsultado);

    }

    public void modificaCliente(Cliente clienteConsultado, String usuario, String celularAnterior) throws ClienteException {
        //Log operaciones
        LogOperaciones logOp = null;
        if(celularAnterior != null && !celularAnterior.isEmpty() && !celularAnterior.equals(clienteConsultado.getTelefonoMovil())){
            logOp = new LogOperaciones(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
            logOp.setCodOperacion(MODIFICAR_CELULAR_CLIENTE);
            logOp.setFechaHora(new Date());
            logOp.setAutorizador(usuario);
            logOp.setUsuario(Sesion.getUsuario().getUsuario());
            logOp.setReferencia(clienteConsultado.getCodcli()); // Uid caja
            logOp.setProcesado('N');
            logOp.setEnvioCorreo('S');
            logOp.setIdAccion(Operaciones.ID_ACCION_POSS);
            logOp.setIdOperacion(Operaciones.MODIFICAR_NUMERO_CELULAR);
            logOp.setObservaciones("Cel. Anterior: " + celularAnterior + "; Cel. Nuevo: " + clienteConsultado.getTelefonoMovil());
        }
        clienteConsultado.setDireccionIncorrecta('N');
        clienteConsultado.setEmailIncorrecto('N');
        clienteConsultado.setTelefono1Incorrecto('N');
        clienteConsultado.setTelefonoMovilIncorrecto('N');
        clienteConsultado.setProcesado('N');
        cldao.modificaCliente(clienteConsultado, logOp);
    }
}
