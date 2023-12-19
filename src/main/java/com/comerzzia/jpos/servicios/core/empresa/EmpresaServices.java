/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.core.empresa;

import com.comerzzia.jpos.entity.db.Empresa;
import com.comerzzia.jpos.persistencia.EmpresaDao;
import es.mpsistemas.util.log.Logger;

import javax.persistence.NoResultException;

/**
 *
 * @author MGRI
 */
public class EmpresaServices {
    
    private static final Logger log = Logger.getMLogger(EmpresaServices.class);
    
    public static Empresa consultarDatosEmpresa() throws EmpresaException{
        Empresa empresa = null;
        try {
            empresa = EmpresaDao.consultaEmpresa();
        }
        catch (EmpresaException ex) {
            log.error("Excepción leyendo configuración de empresa",ex);
            throw ex;
        }
        catch (NoResultException ex) {
            log.error("Excepción leyendo configuración de empresa",ex);
            throw new EmpresaException("Excepción leyendo configuración de empresa",ex);
        }
        return empresa;
    }
    
}
