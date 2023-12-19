/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.core.impuestos;

import com.comerzzia.jpos.entity.db.Impuestos;
import com.comerzzia.jpos.entity.db.ImpuestosFact;
import com.comerzzia.jpos.persistencia.ImpuestosDao;
import com.comerzzia.jpos.persistencia.logs.logacceso.LogException;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import com.comerzzia.jpos.util.enums.EnumCodigoImpuestos;
import es.mpsistemas.util.log.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author ME
 */
public class ImpuestosServices {

    protected static Logger log = Logger.getMLogger(ImpuestosServices.class);
    private ImpuestosDao iDao = new ImpuestosDao();

    private static ImpuestosServices instancia = null;

    private ImpuestosServices() {
    }

    public static ImpuestosServices getInstance() {
        if (instancia == null) {
            instancia = new ImpuestosServices();
        }
        return instancia;
    }

    public static ImpuestosFact consultaImpuestos(String uidTicket) {
        Connection conn = new Connection();
        ImpuestosFact imp = null;

        try {
            log.debug("consultar() - Consultando datos del usuario: " + uidTicket);
            conn.abrirConexion(Database.getConnection());
            imp = ImpuestosDao.consultarImpuestoFactura(conn, uidTicket);

        } catch (Exception e) {
            log.error("consultar() - " + e.getMessage());
            String mensaje = "Error al consultar datos de un usuario: " + e.getMessage();

        } finally {
            conn.cerrarConexion();
        }
        return imp;
    }

    public static Impuestos consultar(Long codigo) {
        Connection conn = new Connection();
        Impuestos imp = null;

        try {
            log.debug("consultar() - Consultando datos del usuario: " + codigo);
            conn.abrirConexion(Database.getConnection());
            imp = ImpuestosDao.consultar(conn, codigo);

        } catch (Exception e) {
            log.error("consultar() - " + e.getMessage());
            String mensaje = "Error al consultar datos de un usuario: " + e.getMessage();

        } finally {
            conn.cerrarConexion();
        }
        return imp;
    }

    public static void crearImpuestos(ImpuestosFact imp, EntityManager em) throws LogException {
        try {
            ImpuestosDao.crearImpuestos(imp, em);
        } catch (Exception ex) {
            log.error("Error guardando el Log detalles de operación  ", ex);
            throw new LogException();
        } finally {
        }
    }

    public static void guardarImpuestos(String uidTicket,TicketS ticket,EntityManager em) throws LogException {

        ImpuestosFact impFactura = new ImpuestosFact();
        impFactura = new ImpuestosFact(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN), Sesion.getDatosConfiguracion().getCodcaja());
        Impuestos imp = ImpuestosServices.consultar(EnumCodigoImpuestos.IMPUESTO_ICE.getCodigo());
        impFactura.setUidTicket(uidTicket);
        impFactura.setIdImpuestos(imp.getIdImpuestos());
        impFactura.setBaseImponible(ticket.getTotales().getBaseImponibleIce());
        impFactura.setPorcentaje(null);
        impFactura.setTarifa(imp.getTarifaEspecifica());
        impFactura.setValor(ticket.getTotales().getImpuestosIce());
        insertarImpuestos(impFactura,em);

    }

    public static void insertarImpuestos(ImpuestosFact impFactura,EntityManager em) throws LogException {
        ImpuestosServices.crearImpuestos(impFactura, em);
        
    }

}
