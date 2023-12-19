/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.cintaauditora;

import com.comerzzia.jpos.entity.db.XCintaAuditoraTbl;
import com.comerzzia.jpos.entity.db.XCintaAuditoraItemTbl;
import com.comerzzia.jpos.entity.db.LineaTicketOrigen;
import com.comerzzia.jpos.entity.db.TicketsAlm;
import com.comerzzia.jpos.servicios.core.contadores.caja.ServicioContadoresCaja;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.print.objetos.ItemCintaAuditora;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.tickets.TicketId;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;




import com.comerzzia.jpos.entity.db.LineaTicketOrigen;
import com.comerzzia.jpos.entity.db.TicketsAlm;
import com.comerzzia.jpos.persistencia.core.contadores.DefinicionContadorBean;
import com.comerzzia.jpos.servicios.cintaauditora.CintaAuditoraException;
import com.comerzzia.jpos.servicios.core.contadores.caja.ServicioContadoresCaja;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.print.objetos.ItemCintaAuditora;
import com.comerzzia.jpos.servicios.tickets.TicketId;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;



/**
 *
 * @author Sistemas
 */
public class CintaAuditoraDao extends MantenimientoDao{
    
    private static final Logger log = Logger.getMLogger(CintaAuditoraDao.class);
    
    
    public static void escribirCintaAuditora(EntityManager em, XCintaAuditoraTbl cintaAuditora) throws CintaAuditoraException {
        try {
            em.persist(cintaAuditora);
            em.flush(); // Para que el contador se incremente cuando debe
            ServicioContadoresCaja.incrementarContadorFactura(Connection.getConnection(em));
        }
        catch (Exception ex) {
            log.error("Error al salvar cinta auditora en base de datos: " + ex.getMessage(), ex);
            CintaAuditoraException e = new CintaAuditoraException();
            e.setCausa("BBDD");
            throw e;
        }        
    }
    
    
    public static void escribirLineaCintaAuditora(EntityManager em, ItemCintaAuditora linCA) throws CintaAuditoraException {
        try{
            em.persist(linCA);
        }
        catch (Exception ex) {           
            log.error("Error al salvar linea de cinta auditora en base de datos "+ ex.getMessage(), ex);
            CintaAuditoraException e = new CintaAuditoraException();
            e.setCausa("BBDD");
            throw e;
        }  
    }
    
    //DAINOVY_CA
    public static List<ItemCintaAuditora> consultarItemsCintaAuditora(Connection conn,EntityManager em, String almacen, String caja) throws SQLException
    {
        int size=0;
        String fechaHoraInicio = "";
        String fechaHoraFin = "";
        PreparedStatement pstmt0 = null;        
    	ResultSet rs0 = null;
    	String sql0 = null;
        sql0 = "SELECT TO_CHAR(FECHA_HORA,'DD/MM/YYYY HH24:MI:SS') FECHA\n" +
                "FROM\n" +
                "(\n" +
                "    SELECT ROW_NUMBER() OVER (ORDER BY FECHA_HORA DESC) AS ORDEN,FECHA_HORA\n" +
                "    FROM X_LOG_OPERACIONES_TBL WHERE COD_OPERACION = 'CIERRE_PARCIAL_CAJA' AND COD_ALM = ? AND COD_CAJA = ? \n" +
                ")TABLA\n" +
                "WHERE ORDEN<3";
        try
        {
            pstmt0 = new PreparedStatement(conn, sql0);
            pstmt0.setString(1, almacen);
            pstmt0.setString(2, caja);
            rs0 = pstmt0.executeQuery();            
            while (rs0.next())
            {
                size++;
                if (size == 1)
                {
                    fechaHoraFin = rs0.getString("FECHA");
                }
                else if (size == 2)
                {
                    fechaHoraInicio = rs0.getString("FECHA");
                }
            }
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
        
        
        
        
        
        
        
        
        
        
        
        
        List<ItemCintaAuditora> resultado = new ArrayList<ItemCintaAuditora>();
        ItemCintaAuditora itemCA;
        
        PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	String sql = null;
        
                    
        if (size == 1)
        {
            //Primer cierre parcial de la caja, solo hay una fecha y es la de fin, hay que predefinir la fecha de inicio o cambiar el query para este caso
            sql = "(\n" +
                    "SELECT M.CODMEDPAG, M.DESMEDPAG,\n" +
                    "  (\n" +
                    "    (\n" +
                    "    SELECT NVL(SUM(CARGO),0)\n" +
                    "    \n" +
                    "    FROM D_CAJA_DET_TBL C\n" +
                    "    \n" +
                    "    JOIN D_CAJA_CAB_TBL CC ON C.UID_DIARIO_CAJA = CC.UID_DIARIO_CAJA\n" +
                    "    \n" +
                    "    WHERE C.CODMEDPAG= M.CODMEDPAG AND CC.CODALM = ? AND CC.CODCAJA = ? AND NOT(C.TIPO IN ('M','R')) AND TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') <= ?\n" +
                    "    )        \n" +
                    "  +\n" +
                    "    (\n" +
                    "    SELECT NVL(SUM(CARGO),0)\n" +
                    "  \n" +
                    "    FROM D_CAJA_DET_TBL C\n" +
                    "  \n" +
                    "    JOIN X_RESERVA_TBL R ON (R.UID_RESERVACION = C.ID_DOCUMENTO AND C.TIPO = 'R' AND TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') <= ?    )\n" +
                    "  \n" +
                    "    WHERE C.CODMEDPAG= M.CODMEDPAG AND R.CODALM = ? AND R.CODCAJA = ? \n" +
                    "    )        \n" +
                    "  +\n" +
                    "--------------------------PLAN NOVIOS--------------------------\n" +
                    "    (\n" +
                    "    SELECT NVL(SUM(CARGO),0)\n" +
                    "  \n" +
                    "    FROM D_CAJA_DET_TBL C\n" +
                    "  \n" +
                    "    JOIN X_PLAN_NOVIOS_TBL L ON (L.ID_PLAN  = SUBSTR(C.ID_DOCUMENTO,5,2) AND LENGTH(C.ID_DOCUMENTO) BETWEEN 5 AND 6 AND C.TIPO = 'R' AND TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') <= ?)\n" +
                    "  \n" +
                    "    WHERE C.CODMEDPAG= M.CODMEDPAG AND L.CODALM = ? AND L.CODCAJA = ?\n" +
                    "    )\n" +
                    "  +\n" +
                    "    (\n" +
                    "    SELECT NVL(SUM(ABONO),0)\n" +
                    "    \n" +
                    "    FROM D_CAJA_DET_TBL C\n" +
                    "    \n" +
                    "    JOIN D_CAJA_CAB_TBL CC ON C.UID_DIARIO_CAJA = CC.UID_DIARIO_CAJA \n" +
                    "  \n" +
                    "    WHERE CC.CODALM = ? AND CC.CODCAJA = ? AND C.CODMEDPAG= M.CODMEDPAG AND C.TIPO='A' AND TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') <= ?)\n" +
                    "    \n" +
                    "    )DIARIA\n" +
                    "\n" +
                    "FROM D_MEDIOS_PAGO_TBL M\n" +
                    "JOIN x_Medio_Pago_Tienda_Tbl MPT ON M.CODMEDPAG = MPT.CODMEDPAG JOIN D_TIENDAS_TBL T ON MPT.CODALM = T.CODALM\n" +
                    ")\n" +
                    "\n" +
                    "UNION ALL\n" +
                    "\n" +
                    "(\n" +
                    "SELECT '41','ABONOS A RESERVACIONES',\n" +
                    "\n" +
                    "       (       \n" +
                    "         (SELECT NVL(SUM(A.CANT_ABONO_SIN_DTO),0)\n" +
                    "  \n" +
                    "         FROM X_RESERVA_TBL C\n" +
                    "  \n" +
                    "         JOIN X_RESERVA_ABONOS_TBL A ON (A.UID_RESERVACION = C.UID_RESERVACION AND C.LIQUIDADO = 'S' AND C.CANCELADO = 'N' AND A.ANULADO = 'N' AND TO_CHAR(C.FECHA_LIQUIDACION,'DD/MM/YYYY HH24:MI:SS') <= ?)\n" +
                    "         \n" +
                    "         WHERE C.CODALM = ? AND C.CODCAJA = ?\n" +
                    "         )        \n" +
                    "         +\n" +
                    "         (\n" +
                    "         SELECT NVL(SUM(A.CANT_ABONO_SIN_DTO),0)\n" +
                    "  \n" +
                    "         FROM X_RESERVA_TBL C\n" +
                    "  \n" +
                    "         JOIN X_RESERVA_ABONOS_TBL A ON ( A.UID_RESERVACION = C.UID_RESERVACION AND C.LIQUIDADO = 'N' AND C.CANCELADO = 'S' AND A.ANULADO = 'N' AND TO_CHAR(C.FECHA_LIQUIDACION,'DD/MM/YYYY hh:mm:ss') <= ?)\n" +
                    "          \n" +
                    "         WHERE C.CODALM = ? AND C.CODCAJA = ?  \n" +
                    "         )\n" +
                    "       )DIARIA\n" +
                    "\n" +
                    "FROM DUAL\n" +
                    ")\n" +
                    "ORDER BY CODMEDPAG";
        }
        else
        {
            sql = "(\n" +
                "SELECT M.CODMEDPAG, M.DESMEDPAG,\n" +
                "  (\n" +
                "    (\n" +
                "    SELECT NVL(SUM(CARGO),0)\n" +
                "    \n" +
                "    FROM D_CAJA_DET_TBL C\n" +
                "    \n" +
                "    JOIN D_CAJA_CAB_TBL CC ON C.UID_DIARIO_CAJA = CC.UID_DIARIO_CAJA\n" +
                "    \n" +
                "    WHERE C.CODMEDPAG= M.CODMEDPAG AND CC.CODALM = ? AND CC.CODCAJA = ? AND NOT(C.TIPO IN ('M','R')) AND TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') > ? AND TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') <= ?\n" +
                "    )        \n" +
                "  +\n" +
                "    (\n" +
                "    SELECT NVL(SUM(CARGO),0)\n" +
                "  \n" +
                "    FROM D_CAJA_DET_TBL C\n" +
                "  \n" +
                "    JOIN X_RESERVA_TBL R ON (R.UID_RESERVACION = C.ID_DOCUMENTO AND C.TIPO = 'R' AND TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') > ? AND TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') <= ?    )\n" +
                "  \n" +
                "    WHERE C.CODMEDPAG= M.CODMEDPAG AND R.CODALM = ? AND R.CODCAJA = ? \n" +
                "    )        \n" +
                "  +\n" +
                "--------------------------PLAN NOVIOS--------------------------\n" +
                "    (\n" +
                "    SELECT NVL(SUM(CARGO),0)\n" +
                "  \n" +
                "    FROM D_CAJA_DET_TBL C\n" +
                "  \n" +
                "    JOIN X_PLAN_NOVIOS_TBL L ON (L.ID_PLAN  = SUBSTR(C.ID_DOCUMENTO,5,2) AND LENGTH(C.ID_DOCUMENTO) BETWEEN 5 AND 6 AND C.TIPO = 'R' AND TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') > ? AND TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') <= ?)\n" +
                "  \n" +
                "    WHERE C.CODMEDPAG= M.CODMEDPAG AND L.CODALM = ? AND L.CODCAJA = ?\n" +
                "    )\n" +
                "  +\n" +
                "    (\n" +
                "    SELECT NVL(SUM(ABONO),0)\n" +
                "    \n" +
                "    FROM D_CAJA_DET_TBL C\n" +
                "    \n" +
                "    JOIN D_CAJA_CAB_TBL CC ON C.UID_DIARIO_CAJA = CC.UID_DIARIO_CAJA \n" +
                "  \n" +
                "    WHERE CC.CODALM = ? AND CC.CODCAJA = ? AND C.CODMEDPAG= M.CODMEDPAG AND C.TIPO='A' AND TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') > ? AND TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') <= ?)\n" +
                "    \n" +
                "    )DIARIA\n" +
                "\n" +
                "FROM D_MEDIOS_PAGO_TBL M\n" +
                "JOIN x_Medio_Pago_Tienda_Tbl MPT ON M.CODMEDPAG = MPT.CODMEDPAG JOIN D_TIENDAS_TBL T ON MPT.CODALM = T.CODALM\n" +
                ")\n" +
                "\n" +
                "UNION ALL\n" +
                "\n" +
                "(\n" +
                "SELECT '41','ABONOS A RESERVACIONES',\n" +
                "\n" +
                "       (       \n" +
                "         (SELECT NVL(SUM(A.CANT_ABONO_SIN_DTO),0)\n" +
                "  \n" +
                "         FROM X_RESERVA_TBL C\n" +
                "  \n" +
                "         JOIN X_RESERVA_ABONOS_TBL A ON (A.UID_RESERVACION = C.UID_RESERVACION AND C.LIQUIDADO = 'S' AND C.CANCELADO = 'N' AND A.ANULADO = 'N' AND TO_CHAR(C.FECHA_LIQUIDACION,'DD/MM/YYYY HH24:MI:SS') > ? AND TO_CHAR(C.FECHA_LIQUIDACION,'DD/MM/YYYY HH24:MI:SS') <= ?)\n" +
                "         \n" +
                "         WHERE C.CODALM = ? AND C.CODCAJA = ?\n" +
                "         )        \n" +
                "         +\n" +
                "         (\n" +
                "         SELECT NVL(SUM(A.CANT_ABONO_SIN_DTO),0)\n" +
                "  \n" +
                "         FROM X_RESERVA_TBL C\n" +
                "  \n" +
                "         JOIN X_RESERVA_ABONOS_TBL A ON ( A.UID_RESERVACION = C.UID_RESERVACION AND C.LIQUIDADO = 'N' AND C.CANCELADO = 'S' AND A.ANULADO = 'N' AND TO_CHAR(C.FECHA_LIQUIDACION,'DD/MM/YYYY HH24:MI:SS') > ? AND TO_CHAR(C.FECHA_LIQUIDACION,'DD/MM/YYYY HH24:MI:SS') <= ?)\n" +
                "          \n" +
                "         WHERE C.CODALM = ? AND C.CODCAJA = ?  \n" +
                "         )\n" +
                "       )DIARIA\n" +
                "\n" +
                "FROM DUAL\n" +
                ")\n" +
                "ORDER BY CODMEDPAG";
        }
        
    	try
        {
            pstmt = new PreparedStatement(conn, sql);
            
            if (size == 1)
            {
                pstmt.setString(1, almacen);
                pstmt.setString(2, caja);
                pstmt.setString(3, fechaHoraFin);

                pstmt.setString(4, fechaHoraFin);
                pstmt.setString(5, almacen);
                pstmt.setString(6, caja);

                pstmt.setString(7, fechaHoraFin);
                pstmt.setString(8, almacen);
                pstmt.setString(9, caja);

                pstmt.setString(10, almacen);
                pstmt.setString(11, caja);
                pstmt.setString(12, fechaHoraFin);

                pstmt.setString(13, fechaHoraFin);
                pstmt.setString(14, almacen);
                pstmt.setString(15, caja);

                pstmt.setString(16, fechaHoraFin);
                pstmt.setString(17, almacen);
                pstmt.setString(18, caja);
            }
            else
            {
                pstmt.setString(1, almacen);
                pstmt.setString(2, caja);
                pstmt.setString(3, fechaHoraInicio);
                pstmt.setString(4, fechaHoraFin);

                pstmt.setString(5, fechaHoraInicio);
                pstmt.setString(6, fechaHoraFin);
                pstmt.setString(7, almacen);
                pstmt.setString(8, caja);

                pstmt.setString(9, fechaHoraInicio);
                pstmt.setString(10, fechaHoraFin);
                pstmt.setString(11, almacen);
                pstmt.setString(12, caja);

                pstmt.setString(13, almacen);
                pstmt.setString(14, caja);
                pstmt.setString(15, fechaHoraInicio);
                pstmt.setString(16, fechaHoraFin);


                pstmt.setString(17, fechaHoraInicio);
                pstmt.setString(18, fechaHoraFin);
                pstmt.setString(19, almacen);
                pstmt.setString(20, caja);

                pstmt.setString(21, fechaHoraInicio);
                pstmt.setString(22, fechaHoraFin);
                pstmt.setString(23, almacen);
                pstmt.setString(24, caja);
            }
            
            

            


            rs = pstmt.executeQuery();

            while (rs.next())
            {

                    //BigDecimal a = ((BigDecimal) rs.getString("DIARIA")).setScale(2, BigDecimal.ROUND_HALF_UP);

                    itemCA = new ItemCintaAuditora();
                    itemCA.setNombre(rs.getString("DESMEDPAG")); 
                    itemCA.setValor( rs.getString("DIARIA"));

                    resultado.add(itemCA);  
            }

            return resultado;
    	}
    	finally
        {
            try{
                    rs.close();
            }
            catch(Exception ignore) {;}
            try{
                    pstmt.close();
            }
            catch(Exception ignore) {;}
    	}
    }
    //DAINOVY_CA
    
    
    
    //DAINOVY_CA
    public static List<XCintaAuditoraItemTbl> consultarItemsCintaAuditora(Connection conn,EntityManager em, String almacen, String caja, int size, String fechaHoraInicio, String fechaHoraFin) throws SQLException,Exception
    {        
        List<XCintaAuditoraItemTbl> resultado = new ArrayList<XCintaAuditoraItemTbl>();
        XCintaAuditoraItemTbl itemCA;
        
        PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	String sql = null;
        
                    
        if (size == 1)
        {
            //Primer cierre parcial de la caja, solo hay una fecha y es la de fin, hay que predefinir la fecha de inicio o cambiar el query para este caso
            sql = "(\n" +
                    "SELECT M.CODMEDPAG, M.DESMEDPAG,\n" +
                    "  (\n" +
                    "    (\n" +
                    "    SELECT NVL(SUM(CARGO),0)\n" +
                    "    \n" +
                    "    FROM D_CAJA_DET_TBL C\n" +
                    "    \n" +
                    "    JOIN D_CAJA_CAB_TBL CC ON C.UID_DIARIO_CAJA = CC.UID_DIARIO_CAJA\n" +
                    "    \n" +
                    "    WHERE C.CODMEDPAG= M.CODMEDPAG AND CC.CODALM = ? AND CC.CODCAJA = ? AND NOT(C.TIPO IN ('M','R')) AND to_timestamp( TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') , 'DD/MM/YYYY HH24:MI:SS') <= to_timestamp (? , 'DD/MM/YYYY HH24:MI:SS') \n" +
                    "    )        \n" +
                    "  +\n" +
                    "    (\n" +
                    "    SELECT NVL(SUM(CARGO),0)\n" +
                    "  \n" +
                    "    FROM D_CAJA_DET_TBL C\n" +
                    "  \n" +
                    "    JOIN X_RESERVA_TBL R ON (R.UID_RESERVACION = C.ID_DOCUMENTO AND C.TIPO = 'R' AND to_timestamp( TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') , 'DD/MM/YYYY HH24:MI:SS') <= to_timestamp (? , 'DD/MM/YYYY HH24:MI:SS')    )\n" +
                    "  \n" +
                    "    WHERE C.CODMEDPAG= M.CODMEDPAG AND R.CODALM = ? AND R.CODCAJA = ? \n" +
                    "    )        \n" +
                    "  +\n" +
                    "--------------------------PLAN NOVIOS--------------------------\n" +
                    "    (\n" +
                    "    SELECT NVL(SUM(CARGO),0)\n" +
                    "  \n" +
                    "    FROM D_CAJA_DET_TBL C\n" +
                    "  \n" +
                    "    JOIN X_PLAN_NOVIOS_TBL L ON (L.ID_PLAN  = SUBSTR(C.ID_DOCUMENTO,5,2) AND LENGTH(C.ID_DOCUMENTO) BETWEEN 5 AND 6 AND C.TIPO = 'R' AND to_timestamp( TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS'), 'DD/MM/YYYY HH24:MI:SS') <= to_timestamp (? , 'DD/MM/YYYY HH24:MI:SS') )\n" +
                    "  \n" +
                    "    WHERE C.CODMEDPAG= M.CODMEDPAG AND L.CODALM = ? AND L.CODCAJA = ?\n" +
                    "    )\n" +
                    "  +\n" +
                    "    (\n" +
                    "    SELECT NVL(SUM(ABONO),0)\n" +
                    "    \n" +
                    "    FROM D_CAJA_DET_TBL C\n" +
                    "    \n" +
                    "    JOIN D_CAJA_CAB_TBL CC ON C.UID_DIARIO_CAJA = CC.UID_DIARIO_CAJA \n" +
                    "  \n" +
                    "    WHERE CC.CODALM = ? AND CC.CODCAJA = ? AND C.CODMEDPAG= M.CODMEDPAG AND C.TIPO='A' AND to_timestamp( TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') , 'DD/MM/YYYY HH24:MI:SS') <= to_timestamp (? , 'DD/MM/YYYY HH24:MI:SS') )\n" +
                    "    \n" +
                    "    )DIARIA\n" +
                    "\n" +
                    "FROM D_MEDIOS_PAGO_TBL M\n" +
                    "JOIN x_Medio_Pago_Tienda_Tbl MPT ON M.CODMEDPAG = MPT.CODMEDPAG JOIN D_TIENDAS_TBL T ON MPT.CODALM = T.CODALM\n" +
                    ")\n" +
                    "\n" +
                    "UNION ALL\n" +
                    "\n" +
                    "(\n" +
                    "SELECT '41','ABONOS A RESERVACIONES',\n" +
                    "\n" +
                    "       (       \n" +
                    "         (SELECT NVL(SUM(A.CANT_ABONO_SIN_DTO),0)\n" +
                    "  \n" +
                    "         FROM X_RESERVA_TBL C\n" +
                    "  \n" +
                    "         JOIN X_RESERVA_ABONOS_TBL A ON (A.UID_RESERVACION = C.UID_RESERVACION AND C.LIQUIDADO = 'S' AND C.CANCELADO = 'N' AND A.ANULADO = 'N' AND to_timestamp( TO_CHAR(C.FECHA_LIQUIDACION,'DD/MM/YYYY HH24:MI:SS') , 'DD/MM/YYYY HH24:MI:SS') <= to_timestamp (? , 'DD/MM/YYYY HH24:MI:SS') )\n" +
                    "         \n" +
                    "         WHERE C.CODALM = ? AND C.CODCAJA = ?\n" +
                    "         )        \n" +
                    "         +\n" +
                    "         (\n" +
                    "         SELECT NVL(SUM(A.CANT_ABONO_SIN_DTO),0)\n" +
                    "  \n" +
                    "         FROM X_RESERVA_TBL C\n" +
                    "  \n" +
                    "         JOIN X_RESERVA_ABONOS_TBL A ON ( A.UID_RESERVACION = C.UID_RESERVACION AND C.LIQUIDADO = 'N' AND C.CANCELADO = 'S' AND A.ANULADO = 'N' AND to_timestamp( TO_CHAR(C.FECHA_LIQUIDACION,'DD/MM/YYYY hh:mm:ss') , 'DD/MM/YYYY HH24:MI:SS') <= to_timestamp (? , 'DD/MM/YYYY HH24:MI:SS') )\n" +
                    "          \n" +
                    "         WHERE C.CODALM = ? AND C.CODCAJA = ?  \n" +
                    "         )\n" +
                    "       )DIARIA\n" +
                    "\n" +
                    "FROM DUAL\n" +
                    ")\n" +
                    "ORDER BY CODMEDPAG";
        }
        else
        {
            sql = "(\n" +
                "SELECT M.CODMEDPAG, M.DESMEDPAG,\n" +
                "  (\n" +
                "    (\n" +
                "    SELECT NVL(SUM(CARGO),0)\n" +
                "    \n" +
                "    FROM D_CAJA_DET_TBL C\n" +
                "    \n" +
                "    JOIN D_CAJA_CAB_TBL CC ON C.UID_DIARIO_CAJA = CC.UID_DIARIO_CAJA\n" +
                "    \n" +
                "    WHERE C.CODMEDPAG= M.CODMEDPAG AND  NOT exists(select null from D_FACTURACION_TARJETAS_TBL\n " +
                "    where id_documento = id_referencia\n" +
                "    and estatus_transaccion = 'A') AND CC.CODALM = ? AND CC.CODCAJA = ? AND NOT(C.TIPO IN ('M','R')) AND to_timestamp( TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') , 'DD/MM/YYYY HH24:MI:SS' ) > to_timestamp (? , 'DD/MM/YYYY HH24:MI:SS') AND to_timestamp( TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS')  , 'DD/MM/YYYY HH24:MI:SS' ) <= to_timestamp (? , 'DD/MM/YYYY HH24:MI:SS') \n" +
                "    )        \n" +
                "  +\n" +
                "    (\n" +
                "    SELECT NVL(SUM(CARGO),0)\n" +
                "  \n" +
                "    FROM D_CAJA_DET_TBL C\n" +
                "  \n" +
                "    JOIN X_RESERVA_TBL R ON (R.UID_RESERVACION = C.ID_DOCUMENTO AND C.TIPO = 'R' AND to_timestamp( TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') , 'DD/MM/YYYY HH24:MI:SS' ) > to_timestamp (? , 'DD/MM/YYYY HH24:MI:SS') AND to_timestamp(  TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') , 'DD/MM/YYYY HH24:MI:SS' ) <= to_timestamp (? , 'DD/MM/YYYY HH24:MI:SS')    )\n" +
                "  \n" +
                "    WHERE C.CODMEDPAG= M.CODMEDPAG AND R.CODALM = ? AND R.CODCAJA = ? \n" +
                "    )        \n" +
                "  +\n" +
                "--------------------------PLAN NOVIOS--------------------------\n" +
                "    (\n" +
                "    SELECT NVL(SUM(CARGO),0)\n" +
                "  \n" +
                "    FROM D_CAJA_DET_TBL C\n" +
                "  \n" +
                "    JOIN X_PLAN_NOVIOS_TBL L ON (L.ID_PLAN  = SUBSTR(C.ID_DOCUMENTO,5,2) AND LENGTH(C.ID_DOCUMENTO) BETWEEN 5 AND 6 AND C.TIPO = 'R' AND to_timestamp( TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') , 'DD/MM/YYYY HH24:MI:SS' ) > to_timestamp (? , 'DD/MM/YYYY HH24:MI:SS') AND to_timestamp( TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') , 'DD/MM/YYYY HH24:MI:SS' ) <= to_timestamp (? , 'DD/MM/YYYY HH24:MI:SS') )\n" +
                "  \n" +
                "    WHERE C.CODMEDPAG= M.CODMEDPAG AND L.CODALM = ? AND L.CODCAJA = ?\n" +
                "    )\n" +
                "  +\n" +
                "    (\n" +
                "    SELECT NVL(SUM(ABONO),0)\n" +
                "    \n" +
                "    FROM D_CAJA_DET_TBL C\n" +
                "    \n" +
                "    JOIN D_CAJA_CAB_TBL CC ON C.UID_DIARIO_CAJA = CC.UID_DIARIO_CAJA \n" +
                "  \n" +
                "    WHERE CC.CODALM = ? AND CC.CODCAJA = ? AND C.CODMEDPAG= M.CODMEDPAG AND C.TIPO='A' AND to_timestamp( TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') , 'DD/MM/YYYY HH24:MI:SS' ) > to_timestamp (? , 'DD/MM/YYYY HH24:MI:SS') AND to_timestamp ( TO_CHAR(C.FECHA,'DD/MM/YYYY HH24:MI:SS') , 'DD/MM/YYYY HH24:MI:SS' ) <= to_timestamp (? , 'DD/MM/YYYY HH24:MI:SS'))\n" +
                "    \n" +
                "    )DIARIA\n" +
                "\n" +
                "FROM D_MEDIOS_PAGO_TBL M\n" +
                "JOIN x_Medio_Pago_Tienda_Tbl MPT ON M.CODMEDPAG = MPT.CODMEDPAG JOIN D_TIENDAS_TBL T ON MPT.CODALM = T.CODALM\n" +
                ")\n" +
                "\n" +
                "UNION ALL\n" +
                "\n" +
                "(\n" +
                "SELECT '41','ABONOS A RESERVACIONES',\n" +
                "\n" +
                "       (       \n" +
                "         (SELECT NVL(SUM(A.CANT_ABONO_SIN_DTO),0)\n" +
                "  \n" +
                "         FROM X_RESERVA_TBL C\n" +
                "  \n" +
                "         JOIN X_RESERVA_ABONOS_TBL A ON (A.UID_RESERVACION = C.UID_RESERVACION AND C.LIQUIDADO = 'S' AND C.CANCELADO = 'N' AND A.ANULADO = 'N' AND to_timestamp ( TO_CHAR(C.FECHA_LIQUIDACION,'DD/MM/YYYY HH24:MI:SS') , 'DD/MM/YYYY HH24:MI:SS' ) > to_timestamp (? , 'DD/MM/YYYY HH24:MI:SS') AND to_timestamp ( TO_CHAR(C.FECHA_LIQUIDACION,'DD/MM/YYYY HH24:MI:SS') , 'DD/MM/YYYY HH24:MI:SS' ) <= to_timestamp (? , 'DD/MM/YYYY HH24:MI:SS') )\n" +
                "         \n" +
                "         WHERE C.CODALM = ? AND C.CODCAJA = ?\n" +
                "         )        \n" +
                "         +\n" +
                "         (\n" +
                "         SELECT NVL(SUM(A.CANT_ABONO_SIN_DTO),0)\n" +
                "  \n" +
                "         FROM X_RESERVA_TBL C\n" +
                "  \n" +
                "         JOIN X_RESERVA_ABONOS_TBL A ON ( A.UID_RESERVACION = C.UID_RESERVACION AND C.LIQUIDADO = 'N' AND C.CANCELADO = 'S' AND A.ANULADO = 'N' AND to_timestamp ( TO_CHAR(C.FECHA_LIQUIDACION,'DD/MM/YYYY HH24:MI:SS') , 'DD/MM/YYYY HH24:MI:SS' ) > to_timestamp (? , 'DD/MM/YYYY HH24:MI:SS') AND to_timestamp ( TO_CHAR(C.FECHA_LIQUIDACION,'DD/MM/YYYY HH24:MI:SS') , 'DD/MM/YYYY HH24:MI:SS' ) <= to_timestamp (? , 'DD/MM/YYYY HH24:MI:SS') )\n" +
                "          \n" +
                "         WHERE C.CODALM = ? AND C.CODCAJA = ?  \n" +
                "         )\n" +
                "       )DIARIA\n" +
                "\n" +
                "FROM DUAL\n" +
                ")\n" +
                "ORDER BY CODMEDPAG";
        }
        
    	try
        {
            pstmt = new PreparedStatement(conn, sql);
            
            if (size == 1)
            {
                pstmt.setString(1, almacen);
                pstmt.setString(2, caja);
                pstmt.setString(3, fechaHoraFin);

                pstmt.setString(4, fechaHoraFin);
                pstmt.setString(5, almacen);
                pstmt.setString(6, caja);

                pstmt.setString(7, fechaHoraFin);
                pstmt.setString(8, almacen);
                pstmt.setString(9, caja);

                pstmt.setString(10, almacen);
                pstmt.setString(11, caja);
                pstmt.setString(12, fechaHoraFin);

                pstmt.setString(13, fechaHoraFin);
                pstmt.setString(14, almacen);
                pstmt.setString(15, caja);

                pstmt.setString(16, fechaHoraFin);
                pstmt.setString(17, almacen);
                pstmt.setString(18, caja);
            }
            else
            {
                pstmt.setString(1, almacen);
                pstmt.setString(2, caja);
                pstmt.setString(3, fechaHoraInicio);
                pstmt.setString(4, fechaHoraFin);

                pstmt.setString(5, fechaHoraInicio);
                pstmt.setString(6, fechaHoraFin);
                pstmt.setString(7, almacen);
                pstmt.setString(8, caja);

                pstmt.setString(9, fechaHoraInicio);
                pstmt.setString(10, fechaHoraFin);
                pstmt.setString(11, almacen);
                pstmt.setString(12, caja);

                pstmt.setString(13, almacen);
                pstmt.setString(14, caja);
                pstmt.setString(15, fechaHoraInicio);
                pstmt.setString(16, fechaHoraFin);


                pstmt.setString(17, fechaHoraInicio);
                pstmt.setString(18, fechaHoraFin);
                pstmt.setString(19, almacen);
                pstmt.setString(20, caja);

                pstmt.setString(21, fechaHoraInicio);
                pstmt.setString(22, fechaHoraFin);
                pstmt.setString(23, almacen);
                pstmt.setString(24, caja);
            }
            rs = pstmt.executeQuery();

            while (rs.next())
            {
                itemCA = new XCintaAuditoraItemTbl();
                itemCA.setNombre(rs.getString("DESMEDPAG")); 
                itemCA.setValor( rs.getString("DIARIA"));

                resultado.add(itemCA);  
            }

            return resultado;
    	}
        catch (SQLException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw e;
        }
    	finally
        {
            try
            {
                rs.close();
            }
            catch(Exception ignore) {;}
            
            try
            {
                pstmt.close();
            }
            catch(Exception ignore) {;}
    	}
    }
    //DAINOVY_CA
    
    
}





