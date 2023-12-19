/**
 * Copyright 2009-2011 RED.ES - Desarrollado por MP Sistemas
 * 
 * Licencia con arreglo a la EUPL, versión 1.1 o -en cuanto 
 * sean aprobadas por la comisión Europea- versiones 
 * posteriores de la EUPL (la "Licencia").
 * Solo podrá usarse esta obra si se respeta la Licencia.
 * 
 * http://ec.europa.eu/idabc/eupl.html
 * 
 * Salvo cuando lo exija la legislación aplicable o se acuerde
 * por escrito, el programa distribuido con arreglo a la
 * Licencia se distribuye "TAL CUAL",
 * SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, 
 * ni expresas ni implícitas.
 * Véase la Licencia en el idioma concreto que rige
 * los permisos y limitaciones que establece la Licencia.
 */
package com.comerzzia.util.base;

import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public abstract class MantenimientoDao {

    protected static String getNombreElemento(String esquema, String elemento) {
        return esquema + "." + elemento + " ";
    }

    protected static String getNombreElementoConfiguracion(String elemento) {
        return Sesion.datosDatabase.getEsquemaConfig() + "." + elemento + " ";
    }

    protected static String getNombreElementoEmpresa(String elemento) {
        return Sesion.datosDatabase.getEsquemaEmpresa() + "." + elemento + " ";
    }
    
    protected static String getNombreElementoCentral(String elemento) {
        return Variables.getVariable(Variables.DATABASE_CENTRAL_ESQUEMA_EMPRESA) + "." + elemento + " ";
    }
    protected static String getNombreElementoCentralBMSK(String elemento) {
        return Variables.getVariable(Variables.DATABASE_CENTRAL_ESQUEMA_BMSK) + "." + elemento + " ";
    }
    
    protected static String getNombreElementoCredito(String elemento) {
        return VariablesAlm.getVariable(VariablesAlm.BBDD_PROPIETARIA_ESQUEMA_CREDITO) + "." + elemento + " ";
    }
    protected static String getNombreElementoSukasa(String elemento) {
        return VariablesAlm.getVariable(VariablesAlm.BBDD_PROPIETARIA_ESQUEMA_SUKASA) + "." + elemento + " ";
    }
    protected static String getNombreElementoVentas(String elemento) {
        return VariablesAlm.getVariable(VariablesAlm.BBDD_PROPIETARIA_ESQUEMA_VENTAS) + "." + elemento + " ";
    }
    protected static String getNombreElementoStock(String elemento) {
        return VariablesAlm.getVariable(VariablesAlm.BBDD_PROPIETARIA_ESQUEMA_STOCK) + "." + elemento + " ";
    }

    protected static SQLException getDaoException(SQLException e) {
        SQLException s = e;

        if (e instanceof SQLIntegrityConstraintViolationException) {
            switch (e.getErrorCode()) {
                // PK y UNQ
                case 1:     // ORACLE
                case 1062:  // MYSQL
                    s = new KeyConstraintViolationException(e.getMessage(),
                            e.getSQLState(), e.getErrorCode(), e);
                    break;

                // NOT NULL
                case 1400:  // ORACLE
                case 1048:  // MYSQL
                    s = new NotNullConstraintViolationException(e.getMessage(),
                            e.getSQLState(), e.getErrorCode(), e);
                    break;

                // FK
                case 2292:  // ORACLE
                case 1451:  // MYSQL
                    s = new ForeingKeyConstraintViolationException(e.getMessage(),
                            e.getSQLState(), e.getErrorCode(), e);
                    break;

                default:
                    s = new KeyConstraintViolationException(e.getMessage(), e.getSQLState(), e.getErrorCode(), e);
            }
        }

        return s;
    }
}
