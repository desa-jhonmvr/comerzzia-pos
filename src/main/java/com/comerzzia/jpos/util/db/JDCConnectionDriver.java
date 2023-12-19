package com.comerzzia.jpos.util.db;
 
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class JDCConnectionDriver implements Driver {
 
    public static final String URL_PREFIX           = "jdbc:jdc:";
    public static final String URL_SUFIX_LOCAL      = "local";
    public static final String URL_SUFIX_CENTRAL    = "central";
    public static final String URL_SUFIX_SUKASA     = "sukasa";
    public static final String URL_SUFIX_VENTAS     = "ventas";
    public static final String URL_SUFIX_CREDITO    = "credito";
    public static final String URL_SUFIX_CENTRAL_BMSK = "centralBMSK";
    public static final String URL_SUFIX_PASARELA    = "pasarela";
    
    private static final int MAJOR_VERSION = 1;
    private static final int MINOR_VERSION = 1;

    private JDCConnectionPool pool;
    private JDCConnectionPool poolCentral;
    private JDCConnectionPool poolSukasa;
    private JDCConnectionPool poolVentas;
    private JDCConnectionPool poolCredito;
    private JDCConnectionPool poolCentralBmsk;
    private JDCConnectionPool poolPasarela;
 
    private static Logger log = Logger.getLogger("JDCConnectionDriver");
    
    public JDCConnectionDriver(
            String driver, 
            String urlLocal, 
            String userLocal, 
            String passwordLocal, 
            String urlCentral, 
            String userCentral, 
            String passwordCentral,
            String urlOtros, 
            String userSukasa, 
            String passwordSukasa,
            String userVentas, 
            String passwordVentas,
            String userCredito, 
            String passwordCredito,
            String userStock, 
            String passwordStock,
            String urlCentralBmsk,
            String usuarioCentralBmsk,
            String passwordCentralBmsk,
            String urlPasarela,
            String usuarioPasarela,
            String passwordPasarela
         ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
 
        DriverManager.registerDriver(this);
        Class.forName(driver).newInstance();
        pool = new JDCConnectionPool(urlLocal, userLocal, passwordLocal);
        poolCentral = new JDCConnectionPool(urlCentral, userCentral, passwordCentral);
        poolSukasa = new JDCConnectionPool(urlOtros, userSukasa, passwordSukasa);
        poolVentas = new JDCConnectionPool(urlOtros, userVentas, passwordVentas);
        poolCredito = new JDCConnectionPool(urlOtros, userCredito, passwordCredito);
        poolCentralBmsk = new JDCConnectionPool(urlCentralBmsk, usuarioCentralBmsk, passwordCentralBmsk);
        poolPasarela = new JDCConnectionPool(urlPasarela, usuarioPasarela, passwordPasarela);
    }
 
    public java.sql.Connection connect(String url, Properties props) throws SQLException {
        if (url.startsWith(URL_PREFIX)) {
            if (url.endsWith(URL_SUFIX_LOCAL)) {
                return pool.getConnection();
            }
            else if (url.endsWith(URL_SUFIX_CENTRAL)) {
                return poolCentral.getConnection();
            }
            else if (url.endsWith(URL_SUFIX_SUKASA)) {
                return poolSukasa.getConnection();
            }
            else if (url.endsWith(URL_SUFIX_VENTAS)) {
                return poolVentas.getConnection();
            }
            else if (url.endsWith(URL_SUFIX_CREDITO)) {
                return poolCredito.getConnection();
            }
            else if (url.endsWith(URL_SUFIX_CENTRAL_BMSK)) {
                return poolCentralBmsk.getConnection();
            }
            else if (url.endsWith(URL_SUFIX_PASARELA)) {
                return poolPasarela.getConnection();
            }
        }
        return null;
 

    }
 
    public boolean acceptsURL(String url) {
        return url.startsWith(URL_PREFIX);
    }
 
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }
 
    public int getMinorVersion() {
        return MINOR_VERSION;
    }
 
    public DriverPropertyInfo[] getPropertyInfo(String str, Properties props) {
        return new DriverPropertyInfo[0];
    }
 
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
  
}
