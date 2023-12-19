/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util.db;

import javax.persistence.EntityManager;



/**
 *
 * @author Administrador
 */
public class Connection extends es.mpsistemas.util.db.Connection {

    public java.sql.Connection getSQLConnection(){
        return getConnection();
    }
    
    public static Connection getConnection(EntityManager em){
        java.sql.Connection connSQL = em.unwrap(java.sql.Connection.class);
        Connection conn = new Connection();
        conn.abrirConexion(connSQL);
        return conn;
    }
}
