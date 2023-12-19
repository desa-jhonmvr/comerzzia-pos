package com.comerzzia.jpos.util.db;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class JDCConnection implements java.sql.Connection{

	private JDCConnectionPool pool;
	private java.sql.Connection conn;
	private boolean inuse;
	private long timestamp;

	public JDCConnection(java.sql.Connection conn, JDCConnectionPool pool) {
		this.conn = conn;
		this.pool = pool;
		this.inuse = false;
		this.timestamp = 0;
	}

	public synchronized boolean lease() {
		if (inuse) {
			return false;
		}
		else {
			inuse = true;
			timestamp = System.currentTimeMillis();
			return true;
		}
	}

	public boolean validate() {
		try {
			conn.getMetaData();
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean inUse() {
		return inuse;
	}

	public long getLastUse() {
		return timestamp;
	}
 
    @Override
	public void close() throws SQLException {
		pool.returnConnection(this);
	}

	protected void expireLease() {
		inuse = false;
	}

	protected java.sql.Connection getConnection() {
		return conn;
	}

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        conn.setTypeMap(map);
    }

    @Override
    public void setTransactionIsolation(int i) throws SQLException {
        conn.setTransactionIsolation(i);
    }

    @Override
    public Savepoint setSavepoint(String string) throws SQLException {
        return conn.setSavepoint(string);
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return conn.setSavepoint();
    }

    @Override
    public void setReadOnly(boolean bln) throws SQLException {
        conn.setReadOnly(bln);
    }

    @Override
    public void setHoldability(int i) throws SQLException {
        conn.setHoldability(i);
    }

    @Override
    public void setClientInfo(Properties prprts) throws SQLClientInfoException {
        conn.setClientInfo(prprts);
    }

    @Override
    public void setClientInfo(String string, String string1) throws SQLClientInfoException {
        conn.setClientInfo(string, string1);
    }

    @Override
    public void setCatalog(String string) throws SQLException {
        conn.setCatalog(string);
    }

    @Override
    public void setAutoCommit(boolean bln) throws SQLException {
        conn.setAutoCommit(bln);
    }

    @Override
    public void rollback(Savepoint svpnt) throws SQLException {
        conn.rollback(svpnt);
    }

    @Override
    public void rollback() throws SQLException {
        conn.rollback();
    }

    @Override
    public void releaseSavepoint(Savepoint svpnt) throws SQLException {
        conn.releaseSavepoint(svpnt);
    }

    @Override
    public PreparedStatement prepareStatement(String string, String[] strings) throws SQLException {
        return conn.prepareStatement(string, strings);
    }

    @Override
    public PreparedStatement prepareStatement(String string, int[] ints) throws SQLException {
        return conn.prepareStatement(string, ints);
    }

    @Override
    public PreparedStatement prepareStatement(String string, int i) throws SQLException {
        return conn.prepareStatement(string, i);
    }

    @Override
    public PreparedStatement prepareStatement(String string, int i, int i1, int i2) throws SQLException {
        return conn.prepareStatement(string, i, i1, i2);
    }

    @Override
    public PreparedStatement prepareStatement(String string, int i, int i1) throws SQLException {
        return conn.prepareStatement(string, i, i1);
    }

    @Override
    public PreparedStatement prepareStatement(String string) throws SQLException {
        return conn.prepareStatement(string);
    }

    @Override
    public CallableStatement prepareCall(String string, int i, int i1, int i2) throws SQLException {
        return conn.prepareCall(string, i, i1, i2);
    }

    @Override
    public CallableStatement prepareCall(String string, int i, int i1) throws SQLException {
        return conn.prepareCall(string, i, i1);
    }

    @Override
    public CallableStatement prepareCall(String string) throws SQLException {
        return conn.prepareCall(string);
    }

    @Override
    public String nativeSQL(String string) throws SQLException {
        return conn.nativeSQL(string);
    }

    @Override
    public boolean isValid(int i) throws SQLException {
        return conn.isValid(i);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return conn.isReadOnly();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return conn.isClosed();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return conn.getWarnings();
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return conn.getTypeMap();
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return conn.getTransactionIsolation();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return conn.getMetaData();
    }

    @Override
    public int getHoldability() throws SQLException {
        return conn.getHoldability();
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return conn.getClientInfo();
    }

    @Override
    public String getClientInfo(String string) throws SQLException {
        return conn.getClientInfo(string);
    }

    @Override
    public String getCatalog() throws SQLException {
        return conn.getCatalog();
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return conn.getAutoCommit();
    }

    @Override
    public Struct createStruct(String string, Object[] os) throws SQLException {
        return conn.createStruct(string, os);
    }

    @Override
    public Statement createStatement(int i, int i1, int i2) throws SQLException {
        return conn.createStatement(i, i1, i2);
    }

    @Override
    public Statement createStatement(int i, int i1) throws SQLException {
        return conn.createStatement(i, i1);
    }

    @Override
    public Statement createStatement() throws SQLException {
        return conn.createStatement();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return conn.createSQLXML();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return conn.createNClob();
    }

    @Override
    public Clob createClob() throws SQLException {
        return conn.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return conn.createBlob();
    }

    @Override
    public Array createArrayOf(String string, Object[] os) throws SQLException {
        return conn.createArrayOf(string, os);
    }

    @Override
    public void commit() throws SQLException {
        conn.commit();
    }

    @Override
    public void clearWarnings() throws SQLException {
        conn.clearWarnings();
    }

    @Override
    public <T> T unwrap(Class<T> type) throws SQLException {
        return conn.unwrap(type);
    }

    @Override
    public boolean isWrapperFor(Class<?> type) throws SQLException {
        return conn.isWrapperFor(type);
    }    

    @Override
    public void setSchema(String schema) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getSchema() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

}
