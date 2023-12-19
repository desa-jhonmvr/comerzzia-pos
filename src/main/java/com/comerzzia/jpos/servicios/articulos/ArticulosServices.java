package com.comerzzia.jpos.servicios.articulos;

import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.persistencia.articulos.ArticulosDao;
import com.comerzzia.jpos.persistencia.kit.KitBean;
import com.comerzzia.jpos.servicios.articulos.tarifas.TarifasServices;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Vector;
import javax.persistence.NoResultException;

/**
 *
 * @author MGRI
 */
public class ArticulosServices {

    private ArticulosDao aDao = new ArticulosDao();
    private static TarifasServices ts = new TarifasServices();
    protected static Logger log = Logger.getMLogger(ArticulosServices.class);
    private static ArticulosServices instancia = null;

    private ArticulosServices() {
    }

    public static ArticulosServices getInstance() {
        if (instancia == null) {
            instancia = new ArticulosServices();
        }
        return instancia;
    }

    /**
     *  Lista los artículos que cumplen cierta condición
     * @param parametrosBuscar
     * @param nmax
     * @return
     * @throws ArticuloLimitExceededException
     * @throws ArticuloNotFoundException
     * @throws Exception 
     */
    public Vector getArticulosAsVector(ArticulosParamBuscar parametrosBuscar, String nmax) throws ArticuloLimitExceededException, ArticuloNotFoundException, Exception {
        Vector vres = new Vector();
        Vector resultados;
        int resultadosMaximos = Integer.parseInt(nmax);

        Integer codAlmInt = new Integer(Sesion.getTienda().getSriTienda().getCodalminterno());
        Integer codAlmCenttralInt = new Integer(Variables.getVariable(Variables.DATABASE_CODIGO_CENTRAL_LOCAL));
        Integer codAlmCenttral2Int = new Integer(Variables.getVariable(Variables.DATABASE_CODIGO_CENTRAL2_LOCAL));
        
        parametrosBuscar.setCodAlmLocal(codAlmInt.toString());
        parametrosBuscar.setCodAlmCentral(codAlmCenttralInt.toString());
        parametrosBuscar.setCodAlmCentral2(codAlmCenttral2Int.toString());

        if (resultadosMaximos >= getNumeroResultadosVistaBusqueda(parametrosBuscar)) {
            resultados = buscarArticulosVistaBusqueda(parametrosBuscar);
        }
        else {
            throw new ArticuloLimitExceededException();
        }
        
        return resultados;
    }

    /** 
     * Obtiene un artículo a partir de un código de barras
     * @param codigo
     * @return
     * @throws ArticuloNotFoundException 
     */
    public Articulos getArticuloCB(String codigo) throws ArticuloNotFoundException {
        codigo = Articulos.formateaCodigoBarras(codigo);
        log.debug("Consultando artículo con código de barras: " + codigo);

        // Buscamos el codigo de artículo en la tabla de codigos de barra
        String codart = aDao.getArticuloCB(codigo);
        if (codart == null) {
            throw new ArticuloNotFoundException("No se ha encontrado artículo con código de barras: " + codigo);
        }
        Articulos articulo = getArticuloCod(codart);
        if (articulo == null) {
            throw new ArticuloNotFoundException("No se ha encontrado artículo con código de barras: " + codigo);
        }
        return articulo;
    }

    /** Obtiene un artículo por código
     * 
     * @param codigo
     * @return 
     */
    public Articulos getArticuloCod(String codigo) {
        return aDao.getArticuloCod(codigo);
    }

    /**
     *  Obtiene un artículo por marca e item
     * @param marca
     * @param item
     * @return 
     */
    public Articulos getArticulo(String marca, Integer item) {
        return aDao.getArticuloKit(marca, item);
    }

    /** Devuelve el primer código de barras asociado al artículo 
     * 
     * @param codArticulo
     * @return
     * @throws ArticuloNotFoundException
     * @throws ArticuloException 
     */
    public static String consultarCodigoBarras(String codArticulo) throws ArticuloNotFoundException, ArticuloException {
        Connection conn = new Connection();
        try {
            log.debug("consultarCodigoBarras() - Consultando código de barras para el artículo: " + codArticulo);
            conn.abrirConexion(Database.getConnection());
            String codBarras = ArticulosDao.consultarCodigoBarras(conn, codArticulo);
            if (codBarras == null) {
                throw new ArticuloNotFoundException("No se encontró código de barras para el artículo indicado.");
            }
            return codBarras;
        }
        catch (SQLException e) {
            String msg = "Error consultando código de  barras del artículo: " + codArticulo;
            log.error("consultarCodigoBarras() - " + msg, e);
            throw new ArticuloException(msg, e);
        }
        finally {
            conn.cerrarConexion();
        }
    }
    
    
     
     /** Busca los artículos que coincidan con los parámetros de búsqueda. La búsqueda se realiza 
     * indicando comodines al final de los filtros. El código sirve para filtrar tanto códigos de barra como
     * códigos de artículo. Se filtra por defecto artículos activos.
     * @param paramBuscar :: Parámetros de búsqueda.
     * @return :: Lista de artículos 
     */
    public static Vector buscarArticulosVistaBusqueda(ArticulosParamBuscar paramBuscar) throws ArticuloException{
        
        Connection conn = new Connection();
        Vector resultados = null;
        try{
            conn.abrirConexion(Database.getConnection());

            resultados = ArticulosDao.buscarArticulosVistaBusqueda(conn, paramBuscar);   
            // Consultamos la disponibilidad del artículo

            return resultados;
        }
        catch(Exception e){
            String mgs = "Se ha producido un error realizando búsqueda de artículos: "  + e.getMessage();
            log.error("buscarArticulosVistaBusqueda() - "+ mgs, e);
            throw new ArticuloException();
        }
        finally{
           conn.cerrarConexion();
        }
    }
    
    
     /** Busca los artículos que coincidan con los parámetros de búsqueda. La búsqueda se realiza 
     * indicando comodines al final de los filtros. El código sirve para filtrar tanto códigos de barra como
     * códigos de artículo. Se filtra por defecto artículos activos.
     * @param paramBuscar :: Parámetros de búsqueda.
     * @return :: Lista de artículos 
     * @throws com.comerzzia.jpos.servicios.articulos.ArticuloException 
     */
    public static int getNumeroResultadosVistaBusqueda(ArticulosParamBuscar paramBuscar) throws ArticuloException,NoResultException{
        
        Connection conn = new Connection();
        int resultados = 0;
        try{
            conn.abrirConexion(Database.getConnection());
            resultados = ArticulosDao.getNumeroResultadosVistaBusqueda(conn, paramBuscar);            
        }
        catch(NoResultException e){            
            throw e;
        }
        catch(Exception e){
            String mgs = "Se ha producido un error realizando búsqueda de artículos: "  + e.getMessage();
            log.error("getNumeroResultadosVistaBusqueda() - "+ mgs, e);
            throw new ArticuloException();
        }
        finally{
           conn.cerrarConexion();
        }
        return resultados;
    }
    
    public static String generarPedidoFacturado(String articulo, String almacen, Long idUsuario, String numFactura, String observacion, int cantidad) throws ArticuloException{
        
        Connection conn = new Connection();
        String respuesta = null;
        try{
            conn.abrirConexion(Database.getConnection());

            respuesta = ArticulosDao.generarPedidoFacturado(conn, articulo, almacen, idUsuario, numFactura, observacion, cantidad) ;

            return respuesta;
        }
        catch(Exception e){
            String mgs = "Se ha producido un error generando el pedido facturado: "  + e.getMessage();
            log.error("generarPedidoFacturado() - "+ mgs, e);
            throw new ArticuloException();
        }
        finally{
           conn.cerrarConexion();
        }
    }
    
    public static String anularPedidoFacturado(String articulo, String numFactura) throws ArticuloException{
        
        Connection conn = new Connection();
        String respuesta = null;
        try{
            conn.abrirConexion(Database.getConnection());

            respuesta = ArticulosDao.anularPedidoFacturado(conn, articulo, numFactura) ;

            return respuesta;
        }
        catch(Exception e){
            String mgs = "Se ha producido un error anulando el pedido facturado: "  + e.getMessage();
            log.error("anularPedidoFacturado() - "+ mgs, e);
            throw new ArticuloException();
        }
        finally{
           conn.cerrarConexion();
        }
    }
}