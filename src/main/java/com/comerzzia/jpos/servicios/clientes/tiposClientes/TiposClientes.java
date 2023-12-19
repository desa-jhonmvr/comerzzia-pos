/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.clientes.tiposClientes;

import com.comerzzia.jpos.persistencia.clientes.tiposclientes.TipoClienteBean;
import com.comerzzia.jpos.persistencia.clientes.tiposclientes.TiposClientesDao;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import es.mpsistemas.util.log.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jmc
 */
public class TiposClientes {

    protected static Logger log = Logger.getMLogger(TiposClientes.class);
    private static TiposClientes instancia;
    private List<TipoClienteBean> listaTiposClientes;
    private Map<Long, TipoClienteBean> mapTiposClientes;

    private TiposClientes() {
        mapTiposClientes = new HashMap<Long, TipoClienteBean>();
    }

    public static TiposClientes getInstancia() {
        if (instancia == null) {
            throw new RuntimeException("Los tipos de clientes no han sido cargados");
        }
        return instancia;
    }

    public static void cargarTiposClientes() throws TiposClientesException {
        instancia = new TiposClientes();
        Connection conn = new Connection();
        try {
            log.debug("cargarTiposClientes() - Cargando los tipos de clientes");
            conn.abrirConexion(Database.getConnection());
            List<TipoClienteBean> tiposClientes = TiposClientesDao.consultar(conn);
            for (TipoClienteBean tipoCliente : tiposClientes) {
                instancia.getMapTiposClientes().put(tipoCliente.getCodTipoCliente(), tipoCliente);
            }
        }
        catch (SQLException ex) {
            log.error("cargarTiposClientes()- Error consultado los tipos de clientes" + ex.getMessage(), ex);
            throw new TiposClientesException("Error consultado los tipos de clientes", ex);
        }
        finally {
            conn.cerrarConexion();
        }
    }

    private Map<Long, TipoClienteBean> getMapTiposClientes() {
        return mapTiposClientes;
    }

    private List<TipoClienteBean> getListaTiposClientes() {
        return listaTiposClientes;
    }

    private void setListaTiposClientes(List<TipoClienteBean> listaTiposClientes) {
        this.listaTiposClientes = listaTiposClientes;
    }

    public TipoClienteBean getTipoCliente(Long codTipoCliente) {
        return getMapTiposClientes().get(codTipoCliente);
    }

    public List<TipoClienteBean> getTiposClientes() {
        if (getListaTiposClientes() == null) {
            setListaTiposClientes(new ArrayList<TipoClienteBean>(getMapTiposClientes().values()));
        }
        return getListaTiposClientes();
    }
}
