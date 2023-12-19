/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.clientes;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.LogOperaciones;
import com.comerzzia.jpos.servicios.clientes.ClienteException;
import com.comerzzia.jpos.servicios.clientes.ParamBuscarClientes;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author MGRI
 */
public class ClientesDao extends MantenimientoDao {

    private static final Logger log = Logger.getMLogger(ClientesDao.class);

    /**
     * Consulta un cliente por su identificación
     *
     * @param ced
     * @param tipo
     * @return
     * @throws ClienteException
     */
    public Cliente consultaCliente(String ced, String tipo) throws ClienteException, NoResultException {
        log.info("DAO: Consulta por documento");
        Cliente cli = null;

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            if (!ced.equals("00")) {
                Query consulta = em.createQuery("SELECT c FROM Cliente c WHERE c.identificacion = UPPER(:identificacion) AND c.tipoIdentificacion = :tipo AND C.activo ='S' ");
                consulta.setParameter("identificacion", ced);
                consulta.setParameter("tipo", tipo);
                cli = (Cliente) consulta.getSingleResult();
            } else {
                log.debug("No se facturar al Consumidor Final.");
                throw new NoResultException("No se puede facturar al Consumidor Final.");
            }
        } catch (NoResultException e) {
            log.debug("No hay clientes con los datos documento:" + ced + " tipo:" + tipo);
            throw new NoResultException("No se encontró nigún cliente con el número de documento seleccionado y No se puede facturar al Consumidor Final");
        } catch (Exception e) {
            log.error("Error en la consulta: " + e.getMessage(), e);
            throw new ClienteException("Error en la consulta a cliente");
        } finally {
            em.close();
        }
        return cli;
    }

    /**
     * Consulta un cliente por su identificación
     *
     * @param ced
     * @return
     * @throws ClienteException
     */
    public Cliente consultaClienteIdent(String ced) throws ClienteException, NoResultException {
        log.info("DAO: Consulta por documento");
        Cliente cli = null;

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            Query consulta = em.createQuery("SELECT c FROM Cliente c WHERE c.identificacion = UPPER(:identificacion)");
            consulta.setParameter("identificacion", ced);
            cli = (Cliente) consulta.getSingleResult();
        } catch (NoResultException e) {
            log.debug("No hay clientes con los datos documento:" + ced);
            throw new NoResultException("No se encontró nigún cliente con el número de documento seleccionado.");
        } catch (Exception e) {
            log.error("Error en la consulta: " + e.getMessage(), e);
            throw new ClienteException("Error en la consulta a cliente");
        } finally {
            em.close();
        }
        return cli;
    }

    /**
     * Consulta un cliente por su numero de afiliación
     *
     * @param nAfil
     * @return
     * @throws ClienteException
     */
    public Cliente consultaClienteNAfil(String nAfil) throws ClienteException {
        log.info("DAO: Consulta por número de afiliación");
        Cliente cli = null;

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            Query consulta = em.createQuery("SELECT c FROM Cliente c WHERE c.codigoTarjetaBabysClub = UPPER(:nafil)");
            consulta.setParameter("nafil", nAfil);
            cli = (Cliente) consulta.getSingleResult();
            em.refresh(cli);
        } catch (NoResultException e) {
            log.debug("No hay clientes con el Número de Afiliación " + nAfil);
            throw new NoResultException("No se encontró nigún cliente con el número de afiliación seleccionado.");
        } catch (Exception e) {
            log.error("Error en la consulta al cliente por Número de Afiliación ." + e.getMessage());
            throw new ClienteException("Error en la consulta a cliente");
        } finally {
            em.close();
        }
        return cli;

    }

    public void nuevoCliente(Cliente clienteConsultado) throws ClienteException {
        log.info("DAO: Nuevo Cliente");

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {

            em.getTransaction().begin();
            em.persist(clienteConsultado);
            em.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error en la creación del cliente: " + e.getMessage(), e);
            String msg = "Error en la creación cliente";
            if (e.getMessage() != null && e.getMessage().contains("SQLIntegrityConstraintViolationException")) {
                msg = "Error al crear el cliente. Ya existe otro cliente con la misma identificación.";
            }
            throw new ClienteException(msg);
        } finally {
            em.close();
        }

    }

    public void modificaCliente(Cliente clienteConsultado, LogOperaciones logOperaciones) throws ClienteException {

        log.info("DAO: Modifica Cliente");
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(clienteConsultado);
            if (logOperaciones != null) {
                em.persist(logOperaciones);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error en la creación del cliente: " + e.getMessage(), e);
            throw new ClienteException("Error en la modificacion de los datos del cliente");
        } finally {
            em.close();
        }

    }

    public List<Cliente> consultarClientes(Connection conn, ParamBuscarClientes param) throws SQLException {
        log.info("DAO: Consultar Clientes");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Cliente> lista = new ArrayList<Cliente>();
        String sql = "SELECT DISTINCT (c.CODCLI), c.NOMBRE_COMERCIAL, c.DESCLI, c.DOMICILIO, c.TELEFONO1, c.DOMICILIO_TRABAJO, c.TELEFONO2, c.FAX, c.EMAIL "
                + "FROM " + getNombreElementoEmpresa("D_CLIENTES_TBL") + " c "
                + "LEFT JOIN " + getNombreElementoEmpresa("X_DOCUMENTOS_TBL") + " docu "
                + "ON (c.codcli = docu.codcli) ";
        sql += getConsultaWhere(param);
        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarClientes() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setCodcli(rs.getString("CODCLI"));
                c.setNombre(rs.getString("NOMBRE_COMERCIAL"));
                c.setDescli(rs.getString("DESCLI"));
                c.setDireccion(rs.getString("DOMICILIO"));
                c.setTelefonoParticular(rs.getString("TELEFONO1"));
                c.setDireccionTrabajo(rs.getString("DOMICILIO_TRABAJO"));
                c.setTelefonoTrabajo(rs.getString("TELEFONO2"));
                c.setTelefonoMovil(rs.getString("FAX"));
                c.setEmail(rs.getString("EMAIL"));
                lista.add(c);
            }

            return lista;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {
            }
        }

    }

    public Integer consultarClientesCount(Connection conn, ParamBuscarClientes param) throws SQLException {
        log.info("DAO: Consultar Clientes");
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(DISTINCT(c.CODCLI)) "
                + "FROM " + getNombreElementoEmpresa("D_CLIENTES_TBL") + " c "
                + "LEFT JOIN " + getNombreElementoEmpresa("X_DOCUMENTOS_TBL") + " docu "
                + "ON (c.codcli = docu.codcli) ";
        sql += getConsultaWhere(param);
        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarClientesCount() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

            return null;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {
            }
        }

    }

    private static String getConsultaWhere(ParamBuscarClientes param) {
        String res = "";
        if (param.getCodcli() != null && !param.getCodcli().isEmpty()) {
            res += "UPPER(c.CODCLI) LIKE UPPER ('%" + param.getCodcli() + "%') ";
        }
        if (param.getDescli() != null && !param.getDescli().isEmpty()) {
            if (!res.isEmpty()) {
                res += "AND ";
            }
            res += "UPPER(c.DESCLI) LIKE UPPER ('%" + param.getDescli() + "%') ";
        }
        if (param.getNombre_com() != null && !param.getNombre_com().isEmpty()) {
            if (!res.isEmpty()) {
                res += "AND ";
            }
            res += "UPPER(c.NOMBRE_COMERCIAL) LIKE UPPER ('%" + param.getNombre_com() + "%') ";
        }
        if (param.getTipoDocumento() != null && !param.getTipoDocumento().isEmpty()) {
            if (!res.isEmpty()) {
                res += "AND ";
            }
            res += "docu.TIPO = '" + param.getTipoDocumento() + "' AND docu.NUM_TRANSACCION LIKE '%" + param.getNumTransaccion() + "%' ";
        }

        if (!res.isEmpty()) {
            res = "WHERE " + res;
        }
        return res;
    }
}
