package com.comerzzia.jpos.persistencia.logs.transaccioneserradas;

import java.util.LinkedList;
import java.util.List;

import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import com.comerzzia.util.fechas.Fechas;
import es.mpsistemas.util.db.PreparedStatement;
import java.sql.ResultSet;
import es.mpsistemas.util.log.Logger;
import java.sql.SQLException;

import es.mpsistemas.util.fechas.Fecha;


public class TransaccionesErradasDao extends MantenimientoDao {

	private static String TABLA_TRANSACCIONES_ERRADAS = "X_TRANSACCION_ERRADA_TBL";
	private static Logger log = Logger.getMLogger(TransaccionesErradasDao.class);
	
	/**
	 * Consultar las transacciones erradas pendientes
	 */
	public static List<TransaccionErradaBean> consultarTransaccionesErradasPendientes(Connection conn) throws SQLException{
		List<TransaccionErradaBean> resultado = new LinkedList<TransaccionErradaBean>();
		PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	
    	String sql = "SELECT * FROM "+TABLA_TRANSACCIONES_ERRADAS+ " WHERE PROCESADO ='N' ";
    	
    	try {
        	pstmt = new PreparedStatement(conn, sql); 
        	log.debug("consultarTransaccionesErradasPendientes() - " + sql);
            rs = pstmt.executeQuery();
            
            while(rs.next()){
            	TransaccionErradaBean tranErr= new TransaccionErradaBean();
            	tranErr.setCodAlm(rs.getString("CODALM"));
            	tranErr.setCodCaja(rs.getString("CODCAJA"));
            	tranErr.setIdTransaccion(rs.getLong("ID_TRANSACCION"));
            	tranErr.setTipotransaccion(rs.getString("TIPO_TRANSACCION"));
            	tranErr.setUsaurio(rs.getString("USUARIO"));
            	tranErr.setFechaHora(new Fecha(rs.getTimestamp("FECHA_HORA")));
            	tranErr.setError(rs.getString("ERROR"));
            	tranErr.setProcesado(rs.getString("PROCESADO"));
            	
            	resultado.add(tranErr);
            }
    	}
        finally {
    		try {
    			rs.close();
    		}
    		catch(Exception ignore) {;}
    		try {
    			pstmt.close();
    		}
    		catch(Exception ignore) {;}
    	}
		return resultado;		
	}
	
	
	public static void insert(Connection conn, TransaccionErradaBean tranErr)throws SQLException {
		PreparedStatement pstmt = null;
		String sql = null;
		
		sql = "INSERT INTO " + TABLA_TRANSACCIONES_ERRADAS + 
		      "( CODALM , CODCAJA ,ID_TRANSACCION ,  TIPO_TRANSACCION, USUARIO , FECHA_HORA , ERROR,  PROCESADO) " + 
		      " VALUES ( ?,?,?,?,?,?,?,? )";
		
		try {			
			pstmt = new PreparedStatement(conn, sql);
			pstmt.setString(1, tranErr.getCodAlm());
			pstmt.setString(2, tranErr.getCodCaja());
			pstmt.setLong(3, tranErr.getIdTransaccion());
			pstmt.setString(4, tranErr.getTipotransaccion());
			pstmt.setString(5, tranErr.getUsaurio());
			pstmt.setTimestamp(6, Fechas.toSqlTimestamp(tranErr.getFechaHora().getDate()));   
			pstmt.setString(7, tranErr.getError());
			pstmt.setString(8, tranErr.getProcesado());
        	
        	log.debug("insert() - " + pstmt);
        	
        	pstmt.execute();
		}
		catch (SQLException e) {
			throw getDaoException(e);
		}
		finally {
    		try {
    			pstmt.close();
    		}
    		catch(Exception ignore) {;}
    	}
	}

	
	public static void updateProcesado(Connection conn, TransaccionErradaBean tranErr)throws SQLException {
		PreparedStatement pstmt = null;
		String sql = null;
			
		sql = "UPDATE " + TABLA_TRANSACCIONES_ERRADAS + 
		 "SET PROCESADO = 'S' WHERE CODALM = ? AND CODCAJA = ? AND ID_TRANSACCION = ? AND TIPO_TRANSACCION = ? ";
		 
		try {
			pstmt = new PreparedStatement(conn, sql);
        	pstmt.setString(1, tranErr.getCodAlm());
        	pstmt.setString(2, tranErr.getCodCaja());
        	pstmt.setLong(3, tranErr.getIdTransaccion());
        	pstmt.setString(4, tranErr.getTipotransaccion());
        	
        	log.debug("updateProcesado() - " + pstmt);
        	
        	pstmt.execute();
		}
		catch (SQLException e) {
			throw getDaoException(e);
		}
		finally {
    		try {
    			pstmt.close();
    		}
    		catch(Exception ignore) {;}
    	}
	}
}