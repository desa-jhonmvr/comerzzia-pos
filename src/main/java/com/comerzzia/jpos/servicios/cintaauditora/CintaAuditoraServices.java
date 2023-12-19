/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.cintaauditora;


import com.comerzzia.jpos.entity.db.XCintaAuditoraTbl;

import com.comerzzia.jpos.entity.db.XCintaAuditoraItemTbl;

import com.comerzzia.jpos.persistencia.cintaauditora.CintaAuditoraDao;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


/**
 *
 * @author Sistemas
 */
public class CintaAuditoraServices {
    
    private static Logger log = Logger.getMLogger(CintaAuditoraServices.class);
    
    public static void crearCintaAuditora(XCintaAuditoraTbl ca) throws CintaAuditoraException,Exception {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            crearCintaAuditora(em, ca);
            em.getTransaction().commit();
        }
        catch (Exception e) {
            log.error("Error en la creación de la cinta auditora: " + e.getMessage(), e);
            throw new CintaAuditoraException("Error en la creación de la cinta auditora");
        }
        finally {
            em.close();
        }
    }
     
    private static void crearCintaAuditora(EntityManager em, XCintaAuditoraTbl ca) throws CintaAuditoraException,Exception {
        
        try 
        {
            em.persist(ca);
        }
        catch (Exception e)
        {
            log.error("Error en la creación de la cinta auditora: " + e.getMessage(), e);
            throw new CintaAuditoraException("Error en la creación de la cinta auditora");
        }
    }
        
    //DAINOVY_CA
    public static List<XCintaAuditoraItemTbl> obtenerItemsCintaAuditora(EntityManager em, String almacen, String caja, int size ,String fechaInicio, String fechaFin) throws Exception
    {
        List<XCintaAuditoraItemTbl> respuestaItemsCintaAuditora = new ArrayList<XCintaAuditoraItemTbl>();
        try
        {
            Connection conn = new Connection();
            conn.abrirConexion(Database.getConnection());            
            respuestaItemsCintaAuditora = CintaAuditoraDao.consultarItemsCintaAuditora(conn,em,almacen,caja,size ,fechaInicio, fechaFin);
            return respuestaItemsCintaAuditora;
        }
        catch (SQLException e)
        {
            log.error("Error consultando acumulado por forma de pago para la cinta auditora",e);
            throw e;
        }
        catch (Exception e)
        {
            log.error("Error consultando acumulado por forma de pago para la cinta auditora",e);
            throw e;
        }
    }
    //DAINOVY_CA
    
    
    
    
    public static String getFecha(String codAlm, String codCaja, String posicion) throws SQLException
    {
        
        Connection conn = new Connection();
        conn.abrirConexion(Database.getConnection());
        
        String fecha = "";
        PreparedStatement pstmt0 = null;        
    	ResultSet rs0 = null;
    	String sql0 = null;
        sql0 = "SELECT TO_CHAR(FECHA_HORA,'DD/MM/YYYY HH24:MI:SS') FECHA\n" +
                "FROM\n" +
                "(\n" +
                "    SELECT ROW_NUMBER() OVER (ORDER BY FECHA_HORA DESC) AS ORDEN,FECHA_HORA\n" +
                "    FROM X_LOG_OPERACIONES_TBL WHERE COD_OPERACION = 'CIERRE_PARCIAL_CAJA' AND COD_ALM = ? AND COD_CAJA = ? \n" +
                ")TABLA\n" +
                "WHERE ORDEN=?";
        try
        {
            pstmt0 = new PreparedStatement(conn, sql0);
            pstmt0.setString(1, codAlm);
            pstmt0.setString(2, codCaja);
            pstmt0.setString(3, posicion);
            rs0 = pstmt0.executeQuery();            
            while (rs0.next())
            {
                fecha = rs0.getString("FECHA");
            }
            return fecha;
            
    	}
    	finally
        {
            try
            {
                rs0.close();
            }
            catch(Exception ignore) {;}
            try{
                    pstmt0.close();
            }
            catch(Exception ignore) {;}
    	}
    }
    
    public static void crearCintaAuditoraItem(XCintaAuditoraItemTbl cai) throws CintaAuditoraException, Exception {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            crearCintaAuditoraItem(em, cai);            
            em.getTransaction().commit();
        }
        catch (Exception e) {
            log.error("Error en la creación del item de la cinta auditora: " + e.getMessage(), e);
            throw new CintaAuditoraException("Error en la creación del item de la cinta auditora");
        }
        finally {
            em.close();
        }
    }
    private static void crearCintaAuditoraItem(EntityManager em, XCintaAuditoraItemTbl cai) throws CintaAuditoraException,Exception {
        
        try 
        {
            em.persist(cai);
        }
        catch (Exception e)
        {
            log.error("Error en la creación del item de la cinta auditora: " + e.getMessage(), e);
            throw new CintaAuditoraException("Error en la creación del item de la cinta auditora: ");
        }
    }
    
    
}
