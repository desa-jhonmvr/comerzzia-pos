/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.general.vendedores;

import com.comerzzia.jpos.entity.db.Vendedor;
import com.comerzzia.jpos.persistencia.general.vendedores.VendedoresDao;
import java.util.List;

/**
 *
 * @author MGRI
 */
public class VendedoresServices {
    
    private static VendedoresDao vDao = new VendedoresDao();
    
    public List<Vendedor> consultarVendedores(String codAlmacen) throws Exception{
        
        List resultado = null;
        Vendedor ven;
    
        resultado = vDao.consultarVendedores(codAlmacen);
        // creamos el vendedor vacio
        ven= new Vendedor();
        ven.setNombreVendedor("");
        ven.setApellidosVendedor("");
        ven.setCodvendedor("");
        ven.setCodAlm("");
        
        resultado.add(0,ven);
        
        return resultado;
    }
    public Vendedor consultarVendedor(String codVendedor) throws Exception{
        return vDao.consultarVendedor(codVendedor);
    }
}
