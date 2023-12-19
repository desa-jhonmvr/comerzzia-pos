package com.comerzzia.jpos.util.db;

import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import es.mpsistemas.util.log.Logger;

public class Database  {
    
    private static final Logger log = Logger.getMLogger(Database.class);
    
    // Instanciamos la clase de forma estática
    static {
        try{ 
             new JDCConnectionDriver(
                     Sesion.datosDatabase.getDriver(), 
                     Sesion.datosDatabase.getUrlConexion(), 
                     Sesion.datosDatabase.getUsuario(), 
                     Sesion.datosDatabase.getPassword(),
                     
                     Variables.getVariable(Variables.DATABASE_CENTRAL_URL), 
                     Variables.getVariable(Variables.DATABASE_CENTRAL_USUARIO), 
                     Variables.getVariable(Variables.DATABASE_CENTRAL_PASSWORD),
                     
                     VariablesAlm.getVariable(VariablesAlm.BBDD_PROPIETARIA_URL), 
                     
                     VariablesAlm.getVariable(VariablesAlm.BBDD_PROPIETARIA_ESQUEMA_SUKASA), 
                     VariablesAlm.getVariable(VariablesAlm.BBDD_PROPIETARIA_SUKASA_PASSWORD),
                     
                     VariablesAlm.getVariable(VariablesAlm.BBDD_PROPIETARIA_ESQUEMA_VENTAS), 
                     VariablesAlm.getVariable(VariablesAlm.BBDD_PROPIETARIA_VENTAS_PASSWORD),
                     
                     VariablesAlm.getVariable(VariablesAlm.BBDD_PROPIETARIA_ESQUEMA_CREDITO), 
                     VariablesAlm.getVariable(VariablesAlm.BBDD_PROPIETARIA_CREDITO_PASSWORD),
                     
                     VariablesAlm.getVariable(VariablesAlm.BBDD_PROPIETARIA_ESQUEMA_STOCK), 
                     VariablesAlm.getVariable(VariablesAlm.BBDD_PROPIETARIA_STOCK_PASSWORD),

                     Variables.getVariable(Variables.DATABASE_CENTRAL_URL_BMSK), 
                     Variables.getVariable(Variables.DATABASE_CENTRAL_USUARIO_BMSK), 
                     Variables.getVariable(Variables.DATABASE_CENTRAL_PASSWORD_BMSK),
                      
                     Variables.getVariable(Variables.DATABASE_PASARELA_URL), 
                     Variables.getVariable(Variables.DATABASE_PASARELA_USUARIO), 
                     Variables.getVariable(Variables.DATABASE_PASARELA_PASSWORD)
                     
                     ); 
        } 
        catch(Exception e){
            log.error(e);
        }
    }
    
    public Database() {
        
    }
    
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:jdc:local");
        
    }
    
    public static Connection getConnectionCentral() throws SQLException {
        return DriverManager.getConnection("jdbc:jdc:central");
    }
    
    public static Connection getConnectionPasarela() throws SQLException {
        return DriverManager.getConnection("jdbc:jdc:pasarela");
    }

    public static Connection getConnectionCentralBMSK() throws SQLException {
        return DriverManager.getConnection("jdbc:jdc:centralBMSK");
    }
    
    public static Connection getConnectionSukasa() throws SQLException {
        return DriverManager.getConnection("jdbc:jdc:sukasa");
        
    }
    
    public static Connection getConnectionVentas() throws SQLException {
        return DriverManager.getConnection("jdbc:jdc:ventas");
        
    }
    
    public static Connection getConnectionCredito() throws SQLException {
        if(VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")){
            return DriverManager.getConnection("jdbc:jdc:local");
        }else{
            return DriverManager.getConnection("jdbc:jdc:credito");
        }
        
    }
    

    

    
}