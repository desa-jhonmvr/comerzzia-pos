package com.comerzzia.jpos.persistencia.articulos;

//import com.comerzzia.jpos.entity.Articulos;
import com.comerzzia.jpos.entity.db.Articulos;
import com.comerzzia.jpos.entity.db.ArticulosCodbar;
import com.comerzzia.jpos.servicios.articulos.ArticulosParamBuscar;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author MGRI
 */
public class ArticulosDao extends MantenimientoDao {

    private static Logger log = Logger.getMLogger(ArticulosDao.class);

    private static final String VISTA_BUSQUEDA_ARTICULOS = "X_ARTICULOS";
    public static final String CODIGO_DESCONTINUADO = "0226000H";

    public int getNumeroResultados(String descripcion, String proveedor, String marca, String codArt, String codBarra, int nmax) {
        log.info("DAO: Consulta de número de resultados");
        Number res;

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            String strConsultaNativa = "SELECT COUNT(a.codart)"
                    + " FROM d_articulos_tbl a left join d_proveedores_tbl p on (a.codpro = p.codpro)"
                    + " LEFT JOIN x_marcas_tbl m on (a.codmarca = m.codmarca) "
                    + " inner join d_articulos_codbar_tbl c on (a.codart = c.codart and c.dun14 = 'N')"
                    + " inner join d_tarifas_det_tbl t on (a.codart = t.codart and t.codtar = 'GENERAL')"
                    + " WHERE UPPER(a.desart) LIKE UPPER('%" + descripcion + "%') AND UPPER(p.despro) LIKE UPPER('%" + proveedor + "%')"
                    + " AND UPPER(m.desmarca) LIKE UPPER('%" + marca + "%') AND UPPER(c.codart) LIKE UPPER('" + codArt + "%')";

            String strConsulta = "SELECT a ,c FROM Articulos a LEFT JOIN a.codpro p LEFT JOIN a.codmarca m INNER JOIN  c.articulosCodbarPK.codart c  "; // WHERE UPPER(a.desart) LIKE UPPER(:desc) AND UPPER(p.despro) LIKE UPPER(:despro) AND UPPER(m.desmarca) LIKE UPPER(:marca)  AND UPPER(c.codart) LIKE UPPER(:codart) ";
            if (!codBarra.isEmpty()) {
                strConsultaNativa = strConsultaNativa + " AND c.codigo_barras LIKE '" + codBarra + "%'";
            }

            Query consulta = em.createNativeQuery(strConsultaNativa);
            res = (Number) consulta.getSingleResult();
        } finally {
            em.close();
        }
        return res.intValue();
    }

    /**
     * Devuelve artículos que cumplen los parametros de búsqueda
     *
     * @param descripcion
     * @param proveedor
     * @param marca
     * @return Articulos segun los parametros de Busqueda
     */
    public Vector getArticulos(String descripcion, String proveedor, String marca, String codArt, String codBarra, int nmax) throws Exception {
        log.info("DAO: Consulta de artículos");
        List lres = null;

        Vector vres = new Vector();  // Vector de resultados

        Articulos art = new Articulos();

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {

            String strConsultaNativa = "SELECT a.codart, a.desart, p.despro, m.desmarca, c.codigo_barras, t.precio_venta, a.codimp"
                    + " FROM d_articulos_tbl a left join d_proveedores_tbl p on (a.codpro = p.codpro)"
                    + " LEFT JOIN x_marcas_tbl m on (a.codmarca = m.codmarca) "
                    + " inner join d_articulos_codbar_tbl c on (a.codart = c.codart and c.dun14 = 'N')"
                    + " inner join d_tarifas_det_tbl t on (a.codart = t.codart and t.codtar = 'GENERAL')"
                    + " WHERE UPPER(a.desart) LIKE UPPER('%" + descripcion + "%') AND UPPER(p.despro) LIKE UPPER('%" + proveedor + "%')"
                    + " AND UPPER(m.desmarca) LIKE UPPER('%" + marca + "%') AND UPPER(c.codart) LIKE UPPER('" + codArt + "%')";

            String strConsulta = "SELECT a ,c FROM Articulos a LEFT JOIN a.codpro p LEFT JOIN a.codmarca m INNER JOIN  c.articulosCodbarPK.codart c  "; // WHERE UPPER(a.desart) LIKE UPPER(:desc) AND UPPER(p.despro) LIKE UPPER(:despro) AND UPPER(m.desmarca) LIKE UPPER(:marca)  AND UPPER(c.codart) LIKE UPPER(:codart) ";
            if (!codBarra.isEmpty()) {
                strConsultaNativa = strConsultaNativa + " AND c.codigo_barras LIKE '" + codBarra + "%'";
            }

            Query consulta = em.createNativeQuery(strConsultaNativa);
            consulta.setMaxResults(nmax);

            lres = consulta.getResultList();
            Iterator filasResultado = lres.iterator();
            while (filasResultado.hasNext()) {
                Object[] par = (Object[]) filasResultado.next();
                Vector fila = new Vector();
                for (Object artbus : par) {
                    fila.add(artbus);
                }

                // El filtro de ordenación necesita un bigdecimal 
                BigDecimal precioventatotal = null;
                // Cogemos el CODIMP                

                if (fila.get(5) != null) {
                    try {
                        String varCodImp = (String) fila.get(6);
                        if (varCodImp.equals("0")) {
                            precioventatotal = (BigDecimal) fila.get(5);
                        } else {
                            precioventatotal = Numero.masPorcentaje((BigDecimal) fila.get(5), Sesion.getEmpresa().getPorcentajeIva());
                        }
                    } catch (Exception e) {
                        log.warn("Articulo no tarificado: " + fila.get(0));
                        precioventatotal = BigDecimal.ZERO;
                    }
                } else {
                    log.warn("Articulo no tarificado: " + fila.get(0));
                    precioventatotal = BigDecimal.ZERO;
                }
                fila.set(5, precioventatotal.setScale(2, BigDecimal.ROUND_HALF_UP));
                vres.add(fila);
            }

        } catch (NoResultException e) {
            log.info("DAO: No se encontraron artículos en la Base de Datos");
        } catch (Exception e) {
            log.error("DAO: Error al consultar artículos en la Base de Datos", e);
            throw e;
        } finally {
            em.close();
        }
        return vres;
    }

    public Articulos getArticuloCod(String codigo) {
        log.info("DAO: Consulta de código de un artículo");
        Articulos resultado = null;
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            Query consulta = em.createQuery("SELECT a FROM Articulos a LEFT JOIN FETCH a.codmarca "
                    + " WHERE a.codart = :codart "
                    + " and a.activo = :activo ");
            consulta.setParameter("codart", codigo);
            consulta.setParameter("activo", 'S');
            consulta.setMaxResults(1);
            consulta.setHint("eclipselink.refresh", "true");
            consulta.setHint("eclipselink.left-join-fetch", "a.codmarca");
            resultado = (Articulos) consulta.getSingleResult();
            //em.refresh(resultado);
        } catch (Exception e) {
            log.error("Error al realizar la consulta de primer con CB " + codigo + " Error: " + e.getMessage(), e);
        } finally {
            em.close();
        }

        return resultado;
    }

    public String getArticuloCB(String codigo) {
        log.info("DAO: Consulta de artículo por CB");
        String resultado = null;
        ArticulosCodbar artcb;

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            Query consulta = em.createNamedQuery("ArticulosCodbar.findByCodigoBarras");
            consulta.setParameter("codigoBarras", codigo);
            consulta.setMaxResults(1);
            consulta.setHint("eclipselink.refresh", "true");
            artcb = (ArticulosCodbar) consulta.getSingleResult();
            if (artcb != null) {
                resultado = artcb.getArticulosCodbarPK().getCodart();
            }
        } catch (NoResultException e) {
            log.debug("No se encontró Artículo con CB " + codigo);
        } catch (Exception e) {
            log.error("Error al consultar artículos con CB " + codigo + " Error: " + e.getMessage(), e);
        } finally {
            em.close();
        }
        return resultado;
    }

    public Articulos getArticuloKit(String marca, Integer idItem) {
        log.info("DAO: Consulta Artículos de un KIT");
        Articulos resultado = null;

        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();

        try {
            Query consulta = em.createQuery("SELECT a FROM Articulos a LEFT JOIN FETCH a.codmarca WHERE a.codmarca.codmarca = :codmarca AND a.idItem = :idItem ");
            consulta.setParameter("codmarca", Numero.completaconCeros(marca, 4));
            consulta.setParameter("idItem", idItem);
            consulta.setMaxResults(1);
            consulta.setHint("eclipselink.refresh", "true");
            consulta.setHint("eclipselink.left-join-fetch", "a.codmarca");
            resultado = (Articulos) consulta.getSingleResult();
        } catch (NoResultException e) {
            log.debug("No se encontró Artículo con idItem: " + idItem + " marca: " + marca);
        } catch (Exception e) {
            log.error("Error al consultar artículos con  idItem: " + idItem + " marca: " + marca + " Error: " + e.getMessage(), e);
        } finally {
            em.close();
        }
        return resultado;
    }

    public static String consultarCodigoBarras(Connection conn, String codArticulo) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        sql = "Select * FROM " + getNombreElementoEmpresa("D_ARTICULOS_CODBAR_TBL") + "WHERE CODART = ? ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, codArticulo);
            log.debug("consultarCodigoBarras() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("CODIGO_BARRAS");
            }
            return null;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {
            }
        }
    }

    public static Vector buscarArticulosVistaBusqueda(Connection conn, ArticulosParamBuscar param) throws SQLException {
        log.debug("buscarArticulosVistaBusqueda()");
        PreparedStatement pstmt = null;
        PreparedStatement pstmt_disponible = null;
        ResultSet rs = null;
        String sql = null;
        ResultSet rs_disponible = null;
        String sqlStock = null;
        ResultSet rs_stock = null;
        PreparedStatement pstmt_stock = null;

        Vector vArticulos = new Vector();  // Vector de resultados

        sql = "SELECT a.codart,a.modelo,a.cod_item AS codigo_interno,a.desart,p.despro,m.desmarca,c.codigo_barras,t.precio_venta,a.codimp,a.garantia_original, "
                + "a.garantia_extendida,sec.desseccion,fam.desfam AS dessubseccion,";
        if (Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK_ERP)) {
            sql += "0 AS stock_local,0 AS codalm_local,";
        } else {
            sql += "klocal.KDX_CANTIDAD AS stock_local,0 AS codalm_local,";
        }
        sql += "0 AS stock_central,"
                + "a.codmarca,a.cod_item, "
                + "0 AS stock_central2, 0 AS stock_disponible, a.coleccion, a.cantidad_um_etiqueta "
                + "FROM " + getNombreElementoEmpresa("d_articulos_tbl") + " a"
                + " LEFT JOIN " + getNombreElementoEmpresa("d_proveedores_tbl") + " p ON (a.codpro = p.codpro)"
                + " LEFT JOIN " + getNombreElementoEmpresa("x_marcas_tbl") + " m ON (a.codmarca = m.codmarca)"
                + " INNER JOIN " + getNombreElementoEmpresa("d_articulos_codbar_tbl") + " c ON (a.codart = c.codart AND c.dun14 = 'N')"
                + " INNER JOIN " + getNombreElementoEmpresa("d_tarifas_det_tbl") + " t ON (a.codart = t.codart AND t.codtar = 'GENERAL')"
                + " LEFT JOIN " + getNombreElementoEmpresa("d_secciones_tbl") + " sec ON (a.codseccion = sec.codseccion)"
                + " LEFT JOIN " + getNombreElementoEmpresa("d_familias_tbl") + " fam ON (a.codfam = fam.codfam)";
        if (!Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK_ERP)) {
            sql += " LEFT JOIN " + getNombreElementoEmpresa("BDG_KARDEX") + " klocal "
                    + " ON (klocal.CODART = a.CODART AND klocal.MOV_ID = 99 AND klocal.LUG_ID = ?)";
        }
//            + " INNER JOIN " + getNombreElementoEmpresa("kdx") + " klocal "
//                    + " ON (a.cod_item = klocal.kitem AND klocal.kmarca = TO_NUMBER (a.codmarca) and klocal.ktipo = 99 AND KLOCAL.KLUGAR = ?)"
        /*+ " LEFT JOIN " + getNombreElementoEmpresa("kdx") + " kcentral "
                    + " ON (a.cod_item = kcentral.kitem AND kcentral.kmarca = TO_NUMBER (a.codmarca) and kcentral.ktipo = 99 AND KCENTRAL.KLUGAR = ?)"
            + " LEFT JOIN " + getNombreElementoEmpresa("kdx") + " kcentral2 "
                    + " ON (a.cod_item = kcentral2.kitem AND kcentral2.kmarca = TO_NUMBER (a.codmarca) and kcentral2.ktipo = 99 AND KCENTRAL2.KLUGAR = ?)"*/
        //+ "INNER JOIN " + ("vw_cd_stock_actual_disponible") + " stck  ON (a.cod_item = stck.item AND stck.marca = TO_NUMBER (a.codmarca)) "
        sql += " WHERE (1=1) ";
        if (param.getCodigo() != null && !param.getCodigo().isEmpty()) {
            sql = sql + "AND UPPER(A.CODART) LIKE UPPER(?) "; // Código interno
        }
        if (param.getDescripcion() != null && !param.getDescripcion().isEmpty()) {
            sql = sql + "AND UPPER(A.DESART) LIKE UPPER(?) "; // Descripción de artículo
        }
        if (param.getMarca() != null && !param.getMarca().isEmpty()) {
            sql = sql + "AND UPPER(M.DESMARCA)LIKE UPPER(?) "; // Marca
        }
        if (param.getProveedor() != null && !param.getProveedor().isEmpty()) {
            sql = sql + "AND UPPER(A.DESPRO)LIKE UPPER(?) "; // Proveedor
        }
        if (param.getSeccion() != null && !param.getSeccion().isEmpty()) {
            sql = sql + "AND UPPER(SEC.DESSECCION)LIKE UPPER(?) "; // Seccion
        }
        if (param.getSubseccion() != null && !param.getSubseccion().isEmpty()) {
            sql = sql + "AND UPPER(FAM.DESFAM)LIKE UPPER(?) "; // Subsección
        }
        if (param.getModelo() != null && !param.getModelo().isEmpty()) {
            sql = sql + "AND UPPER(A.MODELO)LIKE UPPER(?) "; // Modelo-Referencia, Codigo de artículo
        }
        if (param.getCodigoBarras() != null && !param.getCodigoBarras().isEmpty()) {
            sql = sql + "AND UPPER(CODIGO_BARRAS) LIKE UPPER(?) "; // Código de barras
        }

        sql = sql + "ORDER BY CODART";
        try {
            int contador = 1;
            pstmt = new PreparedStatement(conn, sql);
            if (!Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK_ERP)) {
                contador = 2;
                pstmt.setInt(1, Integer.parseInt(param.getCodAlmLocal()));
            } else {
                contador = 1;
            }
            //      pstmt.setString(2, param.getCodAlmCentral());
            //     pstmt.setString(3, param.getCodAlmCentral2());

            if (param.getCodigo() != null && !param.getCodigo().isEmpty()) {
                pstmt.setString(contador, "%" + param.getCodigo() + "%");
                contador++;
            }
            if (param.getDescripcion() != null && !param.getDescripcion().isEmpty()) {
                pstmt.setString(contador, "%" + param.getDescripcion() + "%");
                contador++;
            }
            if (param.getMarca() != null && !param.getMarca().isEmpty()) {
                pstmt.setString(contador, "%" + param.getMarca() + "%");
                contador++;
            }
            if (param.getProveedor() != null && !param.getProveedor().isEmpty()) {
                pstmt.setString(contador, "%" + param.getProveedor() + "%");
                contador++;
            }
            if (param.getSeccion() != null && !param.getSeccion().isEmpty()) {
                pstmt.setString(contador, "%" + param.getSeccion() + "%");
                contador++;
            }
            if (param.getSubseccion() != null && !param.getSubseccion().isEmpty()) {
                pstmt.setString(contador, "%" + param.getSubseccion() + "%");
                contador++;
            }
            if (param.getModelo() != null && !param.getModelo().isEmpty()) {
                pstmt.setString(contador, "%" + param.getModelo() + "%");
                contador++;
            }
            if (param.getCodigoBarras() != null && !param.getCodigoBarras().isEmpty()) {
                pstmt.setString(contador, "%" + param.getCodigoBarras() + "%");
                contador++;
            }

            log.debug("buscarArticulosVistaBusqueda() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Vector vFila = new Vector();

                ArticuloBuscarBean articuloBuscar = new ArticuloBuscarBean();

                //Buscar el disponible en el ERP
                sql = " SELECT stck.stock AS stock_central, stck.stock_disponible as stock_disponible "
                        + " FROM vw_cd_stock_actual_disponible stck  "
                        + " WHERE stck.item = ? AND stck.marca = ? ";

                pstmt_disponible = new PreparedStatement(conn, sql);
                pstmt_disponible.setInt(1, rs.getInt("CODIGO_INTERNO"));
                pstmt_disponible.setInt(2, Integer.valueOf(rs.getString("CODMARCA")));

                log.debug("buscarArticulosVistaBusqueda() - vw_cd_stock_actual_disponible " + pstmt_disponible);
                rs_disponible = pstmt_disponible.executeQuery();

                if (rs_disponible.next()) {
                    articuloBuscar.setStockCentral(rs_disponible.getInt("STOCK_CENTRAL"));
                    articuloBuscar.setStockDisponible(rs_disponible.getInt("STOCK_DISPONIBLE"));
                } else {
                    articuloBuscar.setStockCentral(rs.getInt("STOCK_CENTRAL") + rs.getInt("STOCK_CENTRAL2"));
                    articuloBuscar.setStockDisponible(rs.getInt("STOCK_DISPONIBLE"));
                }

                if (Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK_ERP)) {
                    sqlStock = "SELECT kd.KDX_CANTIDAD AS stock_local"
                            + " FROM BDG_KARDEX@erp kd, CPS_ITEM@erp it"
                            + " WHERE kd.ITM_ID = it.ITM_ID"
                            + " AND kd.MOV_ID = 99"
                            + " AND it.MRC_ID = ?"
                            + " AND it.ITM_SEQ_MARCA = ?"
                            + " AND kd.LUG_ID = ?";

                    pstmt_stock = new PreparedStatement(conn, sqlStock);
                    pstmt_stock.setInt(1, Integer.valueOf(rs.getString("CODMARCA")));
                    pstmt_stock.setInt(2, rs.getInt("CODIGO_INTERNO"));
                    pstmt_stock.setInt(3, Integer.parseInt(param.getCodAlmLocal()));

                    log.debug("buscarArticulosVistaBusqueda() - Stock desde erp " + pstmt_stock);
                    rs_stock = pstmt_stock.executeQuery();

                    if (rs_stock.next()) {
                        articuloBuscar.setStockLocal(rs_stock.getInt("STOCK_LOCAL"));
                    } else {
                        articuloBuscar.setStockLocal(rs.getInt("STOCK_LOCAL"));
                    }
                } else {
                    articuloBuscar.setStockLocal(rs.getInt("STOCK_LOCAL"));
                }
                articuloBuscar.setCodArticulo(rs.getString("CODART"));
                articuloBuscar.setDesArticulo(rs.getString("DESART"));
                articuloBuscar.setMarca(rs.getString("DESMARCA"));
                articuloBuscar.setProveedor(rs.getString("DESPRO"));
                articuloBuscar.setSeccion(rs.getString("DESSECCION"));
                articuloBuscar.setSubseccion(rs.getString("DESSUBSECCION"));
                articuloBuscar.setModelo(rs.getString("MODELO"));
                articuloBuscar.setCodigoBarras(rs.getString("CODIGO_BARRAS"));
                articuloBuscar.setTiempoGarantia(rs.getInt("GARANTIA_ORIGINAL"));
                String garantiaExtendida = rs.getString("GARANTIA_EXTENDIDA");
                if (garantiaExtendida == null || !garantiaExtendida.equals("S")) {
                    articuloBuscar.setGarantiaExtendida("N");
                } else {
                    articuloBuscar.setGarantiaExtendida("S");
                }
                String descontinuado = rs.getString("COLECCION");
                if (descontinuado == null || !descontinuado.equals(CODIGO_DESCONTINUADO)) {
                    articuloBuscar.setDescontinuado("N");
                } else {
                    articuloBuscar.setDescontinuado("S");
                }
//                articuloBuscar.setStockLocal(rs.getInt("STOCK_LOCAL"));
                //articuloBuscar.setStockCentral(rs.getInt("STOCK_CENTRAL")+rs.getInt("STOCK_CENTRAL2"));
                articuloBuscar.setIdItem(rs.getInt("COD_ITEM"));
                articuloBuscar.setCodMarca(rs.getString("CODMARCA"));
                //articuloBuscar.setStockDisponible(rs.getInt("STOCK_DISPONIBLE"));
                // Cálculo del precio del artículo
                String codimp = rs.getString("CODIMP");
                BigDecimal precio = rs.getBigDecimal("PRECIO_VENTA");
                if (precio != null) {
                    try {
                        if (codimp != null && !codimp.equals("0")) {
                            precio = Numero.masPorcentaje(precio, Sesion.getEmpresa().getPorcentajeIva());
                        }
                    } catch (Exception e) {
                        log.warn("Articulo no tarificado: " + articuloBuscar.getCodArticulo());
                        precio = BigDecimal.ZERO;
                    }
                } else {
                    log.warn("Articulo no tarificado: " + articuloBuscar.getCodArticulo());
                    precio = BigDecimal.ZERO;
                }
                articuloBuscar.setPrecio(precio.setScale(2, BigDecimal.ROUND_HALF_UP));
                articuloBuscar.setCantidadUnidadManejo(rs.getInt("CANTIDAD_UM_ETIQUETA"));
                // Creamos el vector con los datos guardados en el bean
                vFila.add(articuloBuscar.getCodArticulo()); //0
                vFila.add(articuloBuscar.getDesArticulo()); //1
                vFila.add(articuloBuscar.getMarca()); //2
                vFila.add(articuloBuscar.getCodigoBarras()); //3
                vFila.add(articuloBuscar.getPrecio()); //4
                vFila.add(articuloBuscar.getDescontinuado()); //5
                vFila.add(articuloBuscar.getStockDisponible()); //6
                vFila.add(articuloBuscar.getStockLocal()); //7
                vFila.add(articuloBuscar.getTiempoGarantia()); //8
                vFila.add(articuloBuscar.getGarantiaExtendida()); //9
                vFila.add(articuloBuscar.getSeccion()); //10
                vFila.add(articuloBuscar.getSubseccion()); //11
                vFila.add(articuloBuscar.getStockCentral()); //12     
                vFila.add(articuloBuscar.getModelo()); //13
                vFila.add(articuloBuscar.getCantidadUnidadManejo()); //14
                vFila.add(articuloBuscar);    // Nota. ponemos el artículo a buscar como último elemento del vector y fuera de visualización de columna

                vArticulos.add(vFila);

                rs_disponible.close();
                pstmt_disponible.close();
                rs_stock.close();
                pstmt_stock.close();
            }
            if (vArticulos != null && vArticulos.size() > 0) {
                return vArticulos;
            } else {
                return buscarArticulosVistaBusquedaSinKardex(conn, param);
            }
        } finally {
            try {
                rs.close();
                if (rs_disponible != null) {
                    rs_disponible.close();
                }
                if (rs_stock != null) {
                    rs_stock.close();
                }
            } catch (Exception ignore) {
                if (rs_disponible != null) {
                    rs_disponible.close();
                }
            }
            try {
                pstmt.close();
                if (pstmt_disponible != null) {
                    pstmt_disponible.close();
                }
                if (pstmt_stock != null) {
                    pstmt_stock.close();
                }
            } catch (Exception ignore) {
            }
        }
    }

    private static Vector buscarArticulosVistaBusquedaSinKardex(Connection conn, ArticulosParamBuscar param) throws SQLException {
        log.debug("buscarArticulosVistaBusqueda()");
        PreparedStatement pstmt = null;
        PreparedStatement pstmt_disponible = null;
        ResultSet rs = null;
        ResultSet rs_disponible = null;
        String sql = null;

        Vector vArticulos = new Vector();  // Vector de resultados

        sql = "SELECT a.codart,a.modelo,a.cod_item AS codigo_interno,a.desart,p.despro,m.desmarca,c.codigo_barras,t.precio_venta,a.codimp,a.garantia_original, "
                + "a.garantia_extendida,sec.desseccion,fam.desfam AS dessubseccion,"
                //   + "klocal.kcant AS stock_local,klocal.klugar AS codalm_local,"
                //+ "stck.stock AS stock_central,"
                //  + "a.codmarca,a.cod_item, stck.stock AS stock_central2, stck.stock_disponible,"
                //+ "stck.stock AS stock_central2, stck.stock_disponible, "
                + "0 AS stock_local,0 AS codalm_local,"
                + "0 AS stock_central,"
                + "a.codmarca,a.cod_item, 0 AS stock_central2, 0 as stock_disponible,"
                + "0 AS stock_central2,  "
                + "a.coleccion, a.cantidad_um_etiqueta  "
                + "FROM " + getNombreElementoEmpresa("d_articulos_tbl") + " a"
                + " LEFT JOIN " + getNombreElementoEmpresa("d_proveedores_tbl") + " p ON (a.codpro = p.codpro)"
                + " LEFT JOIN " + getNombreElementoEmpresa("x_marcas_tbl") + " m ON (a.codmarca = m.codmarca)"
                + " INNER JOIN " + getNombreElementoEmpresa("d_articulos_codbar_tbl") + " c ON (a.codart = c.codart AND c.dun14 = 'N')"
                + " INNER JOIN " + getNombreElementoEmpresa("d_tarifas_det_tbl") + " t ON (a.codart = t.codart AND t.codtar = 'GENERAL')"
                + " LEFT JOIN " + getNombreElementoEmpresa("d_secciones_tbl") + " sec ON (a.codseccion = sec.codseccion)"
                + " LEFT JOIN " + getNombreElementoEmpresa("d_familias_tbl") + " fam ON (a.codfam = fam.codfam)"
                /*+ " LEFT  JOIN " + getNombreElementoEmpresa("kdx") + " klocal "
                    + " ON (a.cod_item = klocal.kitem AND klocal.kmarca = TO_NUMBER (a.codmarca) and klocal.ktipo = 99 AND KLOCAL.KLUGAR = ?)"
            + " LEFT JOIN " + getNombreElementoEmpresa("kdx") + " kcentral "
                    + " ON (a.cod_item = kcentral.kitem AND kcentral.kmarca = TO_NUMBER (a.codmarca) and kcentral.ktipo = 99 AND KCENTRAL.KLUGAR = ?)"
            + " LEFT JOIN " + getNombreElementoEmpresa("kdx") + " kcentral2 "
                    + " ON (a.cod_item = kcentral2.kitem AND kcentral2.kmarca = TO_NUMBER (a.codmarca) and kcentral2.ktipo = 99 AND KCENTRAL2.KLUGAR = ?)"
            + "INNER JOIN " + ("vw_cd_stock_actual_disponible") + " stck  ON (a.cod_item = stck.item AND stck.marca = TO_NUMBER (a.codmarca)) "*/
                + " WHERE (1=1) ";
        if (param.getCodigo() != null && !param.getCodigo().isEmpty()) {
            sql = sql + "AND UPPER(A.CODART) LIKE UPPER(?) "; // Código interno
        }
        if (param.getDescripcion() != null && !param.getDescripcion().isEmpty()) {
            sql = sql + "AND UPPER(A.DESART) LIKE UPPER(?) "; // Descripción de artículo
        }
        if (param.getMarca() != null && !param.getMarca().isEmpty()) {
            sql = sql + "AND UPPER(M.DESMARCA)LIKE UPPER(?) "; // Marca
        }
        if (param.getProveedor() != null && !param.getProveedor().isEmpty()) {
            sql = sql + "AND UPPER(A.DESPRO)LIKE UPPER(?) "; // Proveedor
        }
        if (param.getSeccion() != null && !param.getSeccion().isEmpty()) {
            sql = sql + "AND UPPER(SEC.DESSECCION)LIKE UPPER(?) "; // Seccion
        }
        if (param.getSubseccion() != null && !param.getSubseccion().isEmpty()) {
            sql = sql + "AND UPPER(FAM.DESFAM)LIKE UPPER(?) "; // Subsección
        }
        if (param.getModelo() != null && !param.getModelo().isEmpty()) {
            sql = sql + "AND UPPER(A.MODELO)LIKE UPPER(?) "; // Modelo-Referencia, Codigo de artículo
        }
        if (param.getCodigoBarras() != null && !param.getCodigoBarras().isEmpty()) {
            sql = sql + "AND UPPER(CODIGO_BARRAS) LIKE UPPER(?) "; // Código de barras
        }

        sql = sql + "ORDER BY CODART";
        try {
            int contador = 1;
            pstmt = new PreparedStatement(conn, sql);
            //pstmt.setString(1, param.getCodAlmLocal());
            //pstmt.setString(2, param.getCodAlmCentral());
            //pstmt.setString(3, param.getCodAlmCentral2());

            if (param.getCodigo() != null && !param.getCodigo().isEmpty()) {
                pstmt.setString(contador, "%" + param.getCodigo() + "%");
                contador++;
            }
            if (param.getDescripcion() != null && !param.getDescripcion().isEmpty()) {
                pstmt.setString(contador, "%" + param.getDescripcion() + "%");
                contador++;
            }
            if (param.getMarca() != null && !param.getMarca().isEmpty()) {
                pstmt.setString(contador, "%" + param.getMarca() + "%");
                contador++;
            }
            if (param.getProveedor() != null && !param.getProveedor().isEmpty()) {
                pstmt.setString(contador, "%" + param.getProveedor() + "%");
                contador++;
            }
            if (param.getSeccion() != null && !param.getSeccion().isEmpty()) {
                pstmt.setString(contador, "%" + param.getSeccion() + "%");
                contador++;
            }
            if (param.getSubseccion() != null && !param.getSubseccion().isEmpty()) {
                pstmt.setString(contador, "%" + param.getSubseccion() + "%");
                contador++;
            }
            if (param.getModelo() != null && !param.getModelo().isEmpty()) {
                pstmt.setString(contador, "%" + param.getModelo() + "%");
                contador++;
            }
            if (param.getCodigoBarras() != null && !param.getCodigoBarras().isEmpty()) {
                pstmt.setString(contador, "%" + param.getCodigoBarras() + "%");
                contador++;
            }

            log.debug("buscarArticulosVistaBusqueda() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Vector vFila = new Vector();

                ArticuloBuscarBean articuloBuscar = new ArticuloBuscarBean();

                //Buscar el disponible en el ERP
                sql = " SELECT stck.stock AS stock_central, stck.stock_disponible as stock_disponible "
                        + " FROM vw_cd_stock_actual_disponible stck  "
                        + " WHERE stck.item = ? AND stck.marca = ? ";

                pstmt_disponible = new PreparedStatement(conn, sql);
                pstmt_disponible.setInt(1, rs.getInt("CODIGO_INTERNO"));
                pstmt_disponible.setInt(2, Integer.valueOf(rs.getString("CODMARCA")));

                log.debug("buscarArticulosVistaBusqueda() - vw_cd_stock_actual_disponible " + pstmt_disponible);

                rs_disponible = pstmt_disponible.executeQuery();

                if (rs_disponible.next()) {
                    articuloBuscar.setStockCentral(rs_disponible.getInt("STOCK_CENTRAL"));
                    articuloBuscar.setStockDisponible(rs_disponible.getInt("STOCK_DISPONIBLE"));
                } else {
                    articuloBuscar.setStockCentral(rs.getInt("STOCK_CENTRAL") + rs.getInt("STOCK_CENTRAL2"));
                    articuloBuscar.setStockDisponible(rs.getInt("STOCK_DISPONIBLE"));
                }

                articuloBuscar.setCodArticulo(rs.getString("CODART"));
                articuloBuscar.setDesArticulo(rs.getString("DESART"));
                articuloBuscar.setMarca(rs.getString("DESMARCA"));
                articuloBuscar.setProveedor(rs.getString("DESPRO"));
                articuloBuscar.setSeccion(rs.getString("DESSECCION"));
                articuloBuscar.setSubseccion(rs.getString("DESSUBSECCION"));
                articuloBuscar.setModelo(rs.getString("MODELO"));
                articuloBuscar.setCodigoBarras(rs.getString("CODIGO_BARRAS"));
                articuloBuscar.setTiempoGarantia(rs.getInt("GARANTIA_ORIGINAL"));
                String garantiaExtendida = rs.getString("GARANTIA_EXTENDIDA");
                if (garantiaExtendida == null || !garantiaExtendida.equals("S")) {
                    articuloBuscar.setGarantiaExtendida("N");
                } else {
                    articuloBuscar.setGarantiaExtendida("S");
                }
                String descontinuado = rs.getString("COLECCION");
                if (descontinuado == null || !descontinuado.equals(CODIGO_DESCONTINUADO)) {
                    articuloBuscar.setDescontinuado("N");
                } else {
                    articuloBuscar.setDescontinuado("S");
                }
                articuloBuscar.setStockLocal(rs.getInt("STOCK_LOCAL"));
                //articuloBuscar.setStockCentral(rs.getInt("STOCK_CENTRAL")+rs.getInt("STOCK_CENTRAL2"));
                articuloBuscar.setIdItem(rs.getInt("COD_ITEM"));
                articuloBuscar.setCodMarca(rs.getString("CODMARCA"));
                //articuloBuscar.setStockDisponible(rs.getInt("STOCK_DISPONIBLE"));
                // Cálculo del precio del artículo
                String codimp = rs.getString("CODIMP");
                articuloBuscar.setCantidadUnidadManejo(rs.getInt("CANTIDAD_UM_ETIQUETA"));
                BigDecimal precio = rs.getBigDecimal("PRECIO_VENTA");
                if (precio != null) {
                    try {
                        if (codimp != null && !codimp.equals("0")) {
                            precio = Numero.masPorcentaje(precio, Sesion.getEmpresa().getPorcentajeIva());
                        }
                    } catch (Exception e) {
                        log.warn("Articulo no tarificado: " + articuloBuscar.getCodArticulo());
                        precio = BigDecimal.ZERO;
                    }
                } else {
                    log.warn("Articulo no tarificado: " + articuloBuscar.getCodArticulo());
                    precio = BigDecimal.ZERO;
                }
                articuloBuscar.setPrecio(precio.setScale(2, BigDecimal.ROUND_HALF_UP));

                // Creamos el vector con los datos guardados en el bean
                vFila.add(articuloBuscar.getCodArticulo()); //0
                vFila.add(articuloBuscar.getDesArticulo()); //1
                vFila.add(articuloBuscar.getMarca()); //2
                vFila.add(articuloBuscar.getCodigoBarras()); //3
                vFila.add(articuloBuscar.getPrecio()); //4
                vFila.add(articuloBuscar.getDescontinuado()); //5
                vFila.add(articuloBuscar.getStockDisponible()); //6
                vFila.add(articuloBuscar.getStockLocal()); //7
                vFila.add(articuloBuscar.getTiempoGarantia()); //8
                vFila.add(articuloBuscar.getGarantiaExtendida()); //9
                vFila.add(articuloBuscar.getSeccion()); //10
                vFila.add(articuloBuscar.getSubseccion()); //11
                vFila.add(articuloBuscar.getStockCentral()); //12     
                vFila.add(articuloBuscar.getModelo()); //13
                vFila.add(articuloBuscar.getCantidadUnidadManejo()); //14
                vFila.add(articuloBuscar);   // Nota. ponemos el artículo a buscar como último elemento del vector y fuera de visualización de columna

                vArticulos.add(vFila);
            }
            return vArticulos;
        } finally {
            try {
                rs.close();
                if (rs_disponible != null) {
                    rs_disponible.close();
                }
            } catch (Exception ignore) {
            }
            try {
                pstmt.close();
                if (pstmt_disponible != null) {
                    pstmt_disponible.close();
                }
            } catch (Exception ignore) {
            }
        }
    }

    /**
     * Búsqueda de artículos según parámetros de búsqueda (sin desglose)
     *
     * @param conn
     * @param param
     * @return
     * @throws SQLException
     */
//    public static Vector buscarArticulosVistaBusqueda(Connection conn, ArticulosParamBuscar param) throws SQLException {
//        log.debug("buscarArticulosVistaBusqueda()");
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        String sql = null;
//
//        Vector vArticulos = new Vector();  // Vector de resultados
//
//        sql = "SELECT a.codart,a.modelo,a.cod_item AS codigo_interno,a.desart,p.despro,m.desmarca,c.codigo_barras,t.precio_venta,a.codimp,a.garantia_original, "
//            + "a.garantia_extendida,sec.desseccion,fam.desfam AS dessubseccion,klocal.kcant AS stock_local,klocal.klugar AS codalm_local,kcentral.klugar AS codalm_central,"
//            + "kcentral.kcant AS stock_central,a.codmarca,a.cod_item,kcentral2.klugar AS codalm_central2,kcentral2.kcant AS stock_central2, stck.stock_disponible "
//            + "FROM " + getNombreElementoEmpresa("d_articulos_tbl") + " a"
//            + " LEFT JOIN " + getNombreElementoEmpresa("d_proveedores_tbl") + " p ON (a.codpro = p.codpro)"
//            + " LEFT JOIN " + getNombreElementoEmpresa("x_marcas_tbl") + " m ON (a.codmarca = m.codmarca)"
//            + " INNER JOIN " + getNombreElementoEmpresa("d_articulos_codbar_tbl") + " c ON (a.codart = c.codart AND c.dun14 = 'N')"
//            + " INNER JOIN " + getNombreElementoEmpresa("d_tarifas_det_tbl") + " t ON (a.codart = t.codart AND t.codtar = 'GENERAL')"
//            + " LEFT JOIN " + getNombreElementoEmpresa("d_secciones_tbl") + " sec ON (a.codseccion = sec.codseccion)"
//            + " LEFT JOIN " + getNombreElementoEmpresa("d_familias_tbl") + " fam ON (a.codfam = fam.codfam)"
//            + " INNER JOIN " + getNombreElementoEmpresa("kdx") + " klocal "
//                    + " ON (a.cod_item = klocal.kitem AND klocal.kmarca = TO_NUMBER (a.codmarca) and klocal.ktipo = 99 AND KLOCAL.KLUGAR = ?)"
//            + " LEFT JOIN " + getNombreElementoEmpresa("kdx") + " kcentral "
//                    + " ON (a.cod_item = kcentral.kitem AND kcentral.kmarca = TO_NUMBER (a.codmarca) and kcentral.ktipo = 99 AND KCENTRAL.KLUGAR = ?)"
//            + " LEFT JOIN " + getNombreElementoEmpresa("kdx") + " kcentral2 "
//                    + " ON (a.cod_item = kcentral2.kitem AND kcentral2.kmarca = TO_NUMBER (a.codmarca) and kcentral2.ktipo = 99 AND KCENTRAL2.KLUGAR = ?)"
//            + "INNER JOIN " + ("vw_cd_stock_actual_disponible") + " stck  ON (a.cod_item = stck.item AND stck.marca = TO_NUMBER (a.codmarca)) "
//            + " WHERE (1=1) ";
//        if (param.getCodigo() != null && !param.getCodigo().isEmpty()) {
//            sql = sql + "AND UPPER(A.CODART) LIKE UPPER(?) "; // Código interno
//        }
//        if (param.getDescripcion() != null && !param.getDescripcion().isEmpty()) {
//            sql = sql + "AND UPPER(A.DESART) LIKE UPPER(?) "; // Descripción de artículo
//        }
//        if (param.getMarca() != null && !param.getMarca().isEmpty()) {
//            sql = sql + "AND UPPER(M.DESMARCA)LIKE UPPER(?) "; // Marca
//        }
//        if (param.getProveedor() != null && !param.getProveedor().isEmpty()) {
//            sql = sql + "AND UPPER(A.DESPRO)LIKE UPPER(?) "; // Proveedor
//        }
//        if (param.getSeccion() != null && !param.getSeccion().isEmpty()) {
//            sql = sql + "AND UPPER(SEC.DESSECCION)LIKE UPPER(?) "; // Seccion
//        }
//        if (param.getSubseccion() != null && !param.getSubseccion().isEmpty()) {
//            sql = sql + "AND UPPER(FAM.DESFAM)LIKE UPPER(?) "; // Subsección
//        }
//        if (param.getModelo() != null && !param.getModelo().isEmpty()) {
//            sql = sql + "AND UPPER(A.MODELO)LIKE UPPER(?) "; // Modelo-Referencia, Codigo de artículo
//        }
//        if (param.getCodigoBarras() != null && !param.getCodigoBarras().isEmpty()) {
//            sql = sql + "AND UPPER(CODIGO_BARRAS) LIKE UPPER(?) "; // Código de barras
//        }
//
//        sql = sql + "ORDER BY CODART";
//        try {            
//            int contador = 4;            
//            pstmt = new PreparedStatement(conn, sql);
//            pstmt.setString(1, param.getCodAlmLocal());
//            pstmt.setString(2, param.getCodAlmCentral());
//            pstmt.setString(3, param.getCodAlmCentral2());
//            
//            
//        if (param.getCodigo() != null && !param.getCodigo().isEmpty()) {
//           pstmt.setString(contador, "%" + param.getCodigo() + "%");
//           contador++;
//        }
//        if (param.getDescripcion() != null && !param.getDescripcion().isEmpty()) {
//             pstmt.setString(contador, "%" + param.getDescripcion() + "%");
//           contador++;
//        }
//        if (param.getMarca() != null && !param.getMarca().isEmpty()) {
//            pstmt.setString(contador, "%" + param.getMarca() + "%");
//           contador++;
//        }
//        if (param.getProveedor() != null && !param.getProveedor().isEmpty()) {
//             pstmt.setString(contador, "%" + param.getProveedor() + "%");
//           contador++;
//        }
//        if (param.getSeccion() != null && !param.getSeccion().isEmpty()) {
//            pstmt.setString(contador, "%" + param.getSeccion() + "%");
//           contador++;
//        }
//        if (param.getSubseccion() != null && !param.getSubseccion().isEmpty()) {
//             pstmt.setString(contador, "%" + param.getSubseccion() + "%");
//           contador++;
//        }
//        if (param.getModelo() != null && !param.getModelo().isEmpty()) {
//             pstmt.setString(contador, "%" + param.getModelo() + "%");
//           contador++;
//        }
//        if (param.getCodigoBarras() != null && !param.getCodigoBarras().isEmpty()) {
//             pstmt.setString(contador, "%" + param.getCodigoBarras()+ "%");
//           contador++;
//        }           
//            
//            log.debug("buscarArticulosVistaBusqueda() - " + pstmt);
//
//            rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//                Vector vFila = new Vector();
//
//                ArticuloBuscarBean articuloBuscar = new ArticuloBuscarBean();
//                articuloBuscar.setCodArticulo(rs.getString("CODART"));
//                articuloBuscar.setDesArticulo(rs.getString("DESART"));
//                articuloBuscar.setMarca(rs.getString("DESMARCA"));
//                articuloBuscar.setProveedor(rs.getString("DESPRO"));
//                articuloBuscar.setSeccion(rs.getString("DESSECCION"));
//                articuloBuscar.setSubseccion(rs.getString("DESSUBSECCION"));
//                articuloBuscar.setModelo(rs.getString("MODELO"));
//                articuloBuscar.setCodigoBarras(rs.getString("CODIGO_BARRAS"));
//                articuloBuscar.setTiempoGarantia(rs.getInt("GARANTIA_ORIGINAL"));
//                String garantiaExtendida = rs.getString("GARANTIA_EXTENDIDA");
//                if (garantiaExtendida == null || !garantiaExtendida.equals("S")){
//                    articuloBuscar.setGarantiaExtendida("N");
//                }
//                else{
//                    articuloBuscar.setGarantiaExtendida("S");
//                }
//                articuloBuscar.setStockLocal(rs.getInt("STOCK_LOCAL"));
//                articuloBuscar.setStockCentral(rs.getInt("STOCK_CENTRAL")+rs.getInt("STOCK_CENTRAL2"));
//                articuloBuscar.setIdItem(rs.getInt("COD_ITEM"));
//                articuloBuscar.setCodMarca(rs.getString("CODMARCA"));
//                articuloBuscar.setStockDisponible(rs.getInt("STOCK_DISPONIBLE"));
//                // Cálculo del precio del artículo
//                String codimp = rs.getString("CODIMP");
//                BigDecimal precio = rs.getBigDecimal("PRECIO_VENTA");
//                if (precio != null) {
//                    try {
//                        if (codimp != null && !codimp.equals("0")) {
//                            precio = Numero.masPorcentaje(precio, Sesion.getEmpresa().getPorcentajeIva());
//                        }
//                    }
//                    catch (Exception e) {
//                        log.warn("Articulo no tarificado: " + articuloBuscar.getCodArticulo());
//                        precio = BigDecimal.ZERO;
//                    }
//                }
//                else {
//                    log.warn("Articulo no tarificado: " + articuloBuscar.getCodArticulo());
//                    precio = BigDecimal.ZERO;
//                }
//                articuloBuscar.setPrecio(precio.setScale(2, BigDecimal.ROUND_HALF_UP));
//
//                // Creamos el vector con los datos guardados en el bean
//                vFila.add(articuloBuscar.getCodArticulo()); //0
//                vFila.add(articuloBuscar.getDesArticulo()); //1
//                vFila.add(articuloBuscar.getMarca()); //2
//                vFila.add(articuloBuscar.getCodigoBarras()); //3
//                vFila.add(articuloBuscar.getPrecio()); //4
//                vFila.add(articuloBuscar.getModelo()); //5
//                vFila.add(articuloBuscar.getStockLocal()); //6
//                vFila.add(articuloBuscar.getStockCentral()); //7              
//                vFila.add(articuloBuscar.getTiempoGarantia()); //8
//                vFila.add(articuloBuscar.getGarantiaExtendida()); //9
//                vFila.add(articuloBuscar.getSeccion()); //10
//                vFila.add(articuloBuscar.getSubseccion()); //11
//                vFila.add(articuloBuscar.getStockDisponible()); //12
//                vFila.add(articuloBuscar);   // Nota. ponemos el artículo a buscar como último elemento del vector y fuera de visualización de columna
//
//                vArticulos.add(vFila);
//            }
//            return vArticulos;
//        }
//        finally {
//            try {
//                rs.close();
//            }
//            catch (Exception ignore) {
//            }
//            try {
//                pstmt.close();
//            }
//            catch (Exception ignore) {
//            }
//        }
//    }
    public static int getNumeroResultadosVistaBusqueda(Connection conn, ArticulosParamBuscar param) throws SQLException, NoResultException {
        log.debug("getNumeroResultadosVistaBusqueda()");

        int resultados = 0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        Vector vArticulos = new Vector();  // Vector de resultados

        //  sql = "SELECT COUNT(a.codart) FROM " + getNombreElementoEmpresa(VISTA_BUSQUEDA_ARTICULOS) + " A "
        sql = "SELECT count(DISTINCT a.CODART) "
                + "FROM " + getNombreElementoEmpresa("d_articulos_tbl") + " a"
                + " LEFT JOIN " + getNombreElementoEmpresa("d_proveedores_tbl") + " p ON (a.codpro = p.codpro)"
                + " LEFT JOIN " + getNombreElementoEmpresa("x_marcas_tbl") + " m ON (a.codmarca = m.codmarca)"
                + " INNER JOIN " + getNombreElementoEmpresa("d_articulos_codbar_tbl") + " c ON (a.codart = c.codart AND c.dun14 = 'N')"
                + " INNER JOIN " + getNombreElementoEmpresa("d_tarifas_det_tbl") + " t ON (a.codart = t.codart AND t.codtar = 'GENERAL')"
                + " LEFT JOIN " + getNombreElementoEmpresa("d_secciones_tbl") + " sec ON (a.codseccion = sec.codseccion)"
                + " LEFT JOIN " + getNombreElementoEmpresa("d_familias_tbl") + " fam ON (a.codfam = fam.codfam)";
//        if (Variables.getVariableAsBoolean(Variables.FUNCIONALIDAD_CONSULTA_STOCK_ERP)) {
//            sql += " LEFT JOIN CPS_ITEM@erp item ON (item.MRC_ID = a.CODMARCA AND item.ITM_SEQ_MARCA = a.COD_ITEM)"
//                    + " LEFT JOIN BDG_KARDEX@erp klocal ON (klocal.ITM_ID = item.ITM_ID AND klocal.MOV_ID = 99 AND klocal.LUG_ID IN (?,?,?)) ";
//        } else {
//            sql += " LEFT JOIN " + getNombreElementoEmpresa("BDG_KARDEX") + " klocal "
//                    + " ON (klocal.CODART = a.CODART AND klocal.MOV_ID = 99 AND klocal.LUG_ID = ?)"
//                    + " LEFT JOIN " + getNombreElementoCentral("BDG_KARDEX") + " kcentral "
//                    + " ON (kcentral.CODART = a.CODART AND kcentral.MOV_ID = 99 AND kcentral.LUG_ID IN (?,?)) ";
//        }
//        + " LEFT JOIN " + getNombreElementoEmpresa("kdx") + " klocal "
//                + " ON (a.cod_item = klocal.kitem AND klocal.kmarca = TO_NUMBER (a.codmarca) and klocal.ktipo = 99 AND KLOCAL.KLUGAR = ?)"
//        + " LEFT JOIN " + getNombreElementoEmpresa("kdx") + " kcentral "
//                + " ON (a.cod_item = kcentral.kitem AND kcentral.kmarca = TO_NUMBER (a.codmarca) and kcentral.ktipo = 99 AND KCENTRAL.KLUGAR = ?)"
//        + " LEFT JOIN " + getNombreElementoEmpresa("kdx") + " kcentral2 "
//                + " ON (a.cod_item = kcentral2.kitem AND kcentral2.kmarca = TO_NUMBER (a.codmarca) and kcentral2.ktipo = 99 AND KCENTRAL2.KLUGAR = ?)"
        sql += " WHERE (1=1) ";
        if (param.getCodigo() != null && !param.getCodigo().isEmpty()) {
            sql = sql + "AND UPPER(A.CODART) LIKE UPPER(?) "; // Código interno
        }
        if (param.getDescripcion() != null && !param.getDescripcion().isEmpty()) {
            sql = sql + "AND UPPER(A.DESART) LIKE UPPER(?) "; // Descripción de artículo
        }
        if (param.getMarca() != null && !param.getMarca().isEmpty()) {
            sql = sql + "AND UPPER(M.DESMARCA)LIKE UPPER(?) "; // Marca
        }
        if (param.getProveedor() != null && !param.getProveedor().isEmpty()) {
            sql = sql + "AND UPPER(A.DESPRO)LIKE UPPER(?) "; // Proveedor
        }
        if (param.getSeccion() != null && !param.getSeccion().isEmpty()) {
            sql = sql + "AND UPPER(SEC.DESSECCION)LIKE UPPER(?) "; // Seccion
        }
        if (param.getSubseccion() != null && !param.getSubseccion().isEmpty()) {
            sql = sql + "AND UPPER(FAM.DESFAM)LIKE UPPER(?) "; // Subsección
        }
        if (param.getModelo() != null && !param.getModelo().isEmpty()) {
            sql = sql + "AND UPPER(A.MODELO)LIKE UPPER(?) "; // Modelo-Referencia, Codigo de artículo
        }
        if (param.getCodigoBarras() != null && !param.getCodigoBarras().isEmpty()) {
            sql = sql + "AND UPPER(CODIGO_BARRAS) LIKE UPPER(?) "; // Código de barras
        }
        sql = sql + " ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            int contador = 1;
            pstmt = new PreparedStatement(conn, sql);
//            pstmt.setString(1, param.getCodAlmLocal());
//            pstmt.setString(2, param.getCodAlmCentral());
//            pstmt.setString(3, param.getCodAlmCentral2());

            if (param.getCodigo() != null && !param.getCodigo().isEmpty()) {
                pstmt.setString(contador, "%" + param.getCodigo() + "%");
                contador++;
            }
            if (param.getDescripcion() != null && !param.getDescripcion().isEmpty()) {
                pstmt.setString(contador, "%" + param.getDescripcion() + "%");
                contador++;
            }
            if (param.getMarca() != null && !param.getMarca().isEmpty()) {
                pstmt.setString(contador, "%" + param.getMarca() + "%");
                contador++;
            }
            if (param.getProveedor() != null && !param.getProveedor().isEmpty()) {
                pstmt.setString(contador, "%" + param.getProveedor() + "%");
                contador++;
            }
            if (param.getSeccion() != null && !param.getSeccion().isEmpty()) {
                pstmt.setString(contador, "%" + param.getSeccion() + "%");
                contador++;
            }
            if (param.getSubseccion() != null && !param.getSubseccion().isEmpty()) {
                pstmt.setString(contador, "%" + param.getSubseccion() + "%");
                contador++;
            }
            if (param.getModelo() != null && !param.getModelo().isEmpty()) {
                pstmt.setString(contador, "%" + param.getModelo() + "%");
                contador++;
            }
            if (param.getCodigoBarras() != null && !param.getCodigoBarras().isEmpty()) {
                pstmt.setString(contador, "%" + param.getCodigoBarras() + "%");
                contador++;
            }
            log.debug("getNumeroResultadosVistaBusqueda() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                resultados = rs.getInt(1); // Establecemos el número de resultados
            }

        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {
            }
        }
        // Lanzamos excepción si no hay resultados
//        if (resultados<=0){
//            throw new NoResultException();
//        }

        return resultados;
    }

    public static String generarPedidoFacturado(Connection conn, String articulo, String almacen, Long idUsuario, String numFactura, String observacion, int cantidad) throws SQLException {
        log.debug("generarPedidoFacturado()");

        String respuesta = "";
        CallableStatement cstmt = null;
        String sql = "{call " + getNombreElementoEmpresa("pkg_erp_pedidos_facturados") + ".SP_ERP_GEN_PEDIDO_FACTURADO(?, ?, ?, ?, ?, ?, ?)}";

        try {
            cstmt = conn.prepareCall(sql);

            cstmt.setString(1, articulo);
            cstmt.setString(2, almacen);
            cstmt.setLong(3, idUsuario);
            cstmt.setString(4, numFactura);
            cstmt.setString(5, observacion);
            cstmt.setInt(6, cantidad);
            cstmt.registerOutParameter(7, Types.VARCHAR); // respuesta

            cstmt.execute();

            respuesta = cstmt.getString(7);
            log.debug("generarPedidoFacturado() - Respuesta recibida: " + respuesta);

        } finally {
            try {
                cstmt.close();
            } catch (Exception ignore) {
            }
        }

        return respuesta;
    }

    public static String anularPedidoFacturado(Connection conn, String articulo, String numFactura) throws SQLException {
        log.debug("anulaPedidoFacturado()");

        String respuesta = "";
        CallableStatement cstmt = null;
        String sql = "{call " + getNombreElementoEmpresa("pkg_erp_pedidos_facturados") + ".SP_ERP_ANULA_REQ_ITEM(?, ?, ?)}";

        try {
            cstmt = conn.prepareCall(sql);

            cstmt.setString(1, articulo);
            cstmt.setString(2, numFactura);
            cstmt.registerOutParameter(3, Types.VARCHAR); // respuesta

            cstmt.execute();

            respuesta = cstmt.getString(3);
            log.debug("anulaPedidoFacturado() - Respuesta recibida: " + respuesta);

        } finally {
            try {
                cstmt.close();
            } catch (Exception ignore) {
            }
        }

        return respuesta;
    }
}
