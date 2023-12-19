/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.sms;

import com.comerzzia.jpos.entity.db.Formato;
import com.comerzzia.jpos.entity.db.Tienda;
import com.comerzzia.jpos.entity.sms.SsmsDatainfoRealtimeSms;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.util.base.MantenimientoDao;
import com.sun.org.apache.xpath.internal.operations.Variable;
import es.mpsistemas.util.log.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author CONTABILIDAD
 */
public class ServicioSms extends MantenimientoDao {

    private static final Logger log = Logger.getMLogger(ServicioSms.class);
//comentado hasta puesta en produccion
    public static void insertarDatosJdbc(SsmsDatainfoRealtimeSms objetoMensaje) throws SQLException {
        String connectionUrl = Sesion.getDatosConfiguracion().getDATABASE_SMS_URL()+ ";user=" + Sesion.getDatosConfiguracion().getDATABASE_SMS_USUARIO() + "; password=" + "" +Sesion.getDatosConfiguracion().getDATABASE_SMS_PASSWORD() + ";";
        Connection con = null;
        PreparedStatement pstmt = null;
        con = DriverManager.getConnection(connectionUrl);

        String sql = null;

        sql = "INSERT INTO " + "[dbo].[ssms_datainfo_realtime_sms]"
                + "(format,mask,local_code,datetime_purchase,price,cell_number,caja,factura,cedula)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            int i = 0;
            String caja = objetoMensaje.getCaja();
            while (caja.startsWith("0")) {
                i++;
                caja = objetoMensaje.getCaja().substring(i);
            }
            i = 0;
            String local = objetoMensaje.getLocalCode();
            while (local.startsWith("0")) {
                i++;
                local = objetoMensaje.getLocalCode().substring(i);

            }
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, objetoMensaje.getFormat());
            pstmt.setString(2, objetoMensaje.getMask());
            pstmt.setString(3, local);
            java.sql.Timestamp sqlDate = new java.sql.Timestamp(objetoMensaje.getDatetimePurchase().getTime());
            sqlDate.setNanos(0);
            pstmt.setTimestamp(4, sqlDate);
            pstmt.setBigDecimal(5, objetoMensaje.getPrice());
            pstmt.setString(6, objetoMensaje.getCellNumber());
            pstmt.setString(7, caja);
            pstmt.setString(8, objetoMensaje.getFactura());
            pstmt.setString(9, objetoMensaje.getCedula());
            pstmt.execute();
            log.debug("Insertado para envio sms ");
            log.debug("Sql insert "+sql);
            log.debug("Insertado correctamente en tabla sms_datainfo_realtime_sms ");
        } finally {
            try {
                pstmt.close();
            } catch (Exception ignore) {;
                log.debug("Sql insert "+sql);
                log.error("ERROR AL INSERTAR EN TABLA  ssms_datainfo_realtime_sms... " + ignore);
            }
        }
    }
//Comentado hasta Puesta en Prodducion
    public static boolean insertarDatos(SsmsDatainfoRealtimeSms objetoMensaje) {
        try {
            insertarDatosJdbc(objetoMensaje);
            return true;
        } catch (SQLException ex) {
             
            log.error("ERROR AL INSERTAR EN TABLA  ssms_datainfo_realtime_sms " + ex);
        }
        return true;
    }

    public static Integer obtenerUltimoRegistro() {
        EntityManagerFactory emf = Sesion.getEmfsms();
        EntityManager em = emf.createEntityManager();
        Query consulta = em.createQuery("SELECT MAX(s.id) FROM SsmsDatainfoRealtimeSms s  ");
        Integer resultado = (Integer) consulta.getSingleResult();
        return resultado;

    }

    public static Formato obtenerFormato(Long formato) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        Query consulta = em.createQuery("SELECT n FROM Formato n WHERE n.codformato = :formato");
        consulta.setHint("eclipselink.refresh", "true");
        consulta.setParameter("formato", formato);
        Formato form = (Formato) consulta.getSingleResult();
        return form;
    }

    public static Tienda obtenerIdFormato(String local) {
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        Query consulta = em.createQuery("SELECT n FROM Tienda n WHERE n.codalm = :local");
        consulta.setHint("eclipselink.refresh", "true");
        consulta.setParameter("local", local);
        Tienda form = (Tienda) consulta.getSingleResult();
        return form;
    }
}
