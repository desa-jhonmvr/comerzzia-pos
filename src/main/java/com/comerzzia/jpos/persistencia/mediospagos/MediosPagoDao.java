/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.persistencia.mediospagos;

import com.comerzzia.jpos.persistencia.clientes.tiposclientes.TipoClienteBean;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.util.base.MantenimientoDao;
import es.mpsistemas.util.db.PreparedStatement;
import es.mpsistemas.util.log.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author amos
 */
public class MediosPagoDao extends MantenimientoDao {

    private static final String TABLA = "D_MEDIOS_PAGO_TBL";
    private static final String TABLA_TIENDA = "X_MEDIO_PAGO_TIENDA_TBL";
    private static final String TABLA_RELACION_BANCOS = "X_BANCOS_MEDIOS_PAGO_TBL";
    private static final String TABLA_BANCOS = "D_BANCOS_TBL";
    private static final String TABLA_RELACION_TIPOS_CLIENTES = "X_MEDIO_PAGO_TIPO_CLIENTE_TBL";
    private static final String TABLA_TIPOS_CLIENTES = "X_TIPOS_CLIENTES_TBL";
    private static final String TABLA_VENCIMIENTOS = "D_MEDIOS_PAGO_VEN_TBL";
    private static final String TABLA_DESCUENTOS = "X_MEDIO_PAGO_VEN_TIPO_CLIE_TBL";
    private static final String TABLA_PLASTICO_POS = "D_PLASTICO_TBL";
    private static final String TABLA_ESTADO_POS = "D_CESTACTUAL_TBL";

    private static Logger log = Logger.getMLogger(MediosPagoDao.class);

    public static MedioPagoBean consultar(Connection conn, String codMedPag) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        MedioPagoBean medioPago = null;

        sql = "SELECT PAG.CODMEDPAG AS CODMEDPAG, DESMEDPAG, CONTADO, EFECTIVO, TARJETA_CREDITO, CREDITO_DIRECTO, "
                + "OTROS, VISIBLE_VENTA, VISIBLE_TIENDA_VIRTUAL, VISIBLE_COMPRA, ACTIVO, ADMITE_VUELTO, "
                + "ADMITE_ABONO_RESERVA_CORRIENTE, ADMITE_ABONO_RESERVA_DIFERIDO, REQUIERE_AUTORIZACION, "
                + "TIENE_INFOEXTRA_1, TIENE_INFOEXTRA_2, TIENE_INFOEXTRA_3, LABEL_INFOEXTRA_1, "
                + "LABEL_INFOEXTRA_2, LABEL_INFOEXTRA_3, BINES, ABRIR_CAJON, GIFTCARD, PERMITE_PAGAR_GIFTC_CORRIENTE, "
                + "PERMITE_PAGAR_GIFTC_DIFERIDO, ADMITE_PAGO_CREDITO_TEMPORAL, ADMITE_ABONO_TARJETA_PROPIA, "
                + "CORRIENTE, DIFERIDO_SIN_INTERES, DIFERIDO_CON_INTERES, TARJETA_SUKASA, TIPO_PAGO_CORRIENTE, TIPO_PAGO_DIFERIDO, "
                + "ADMITE_AUTOMATICA_DIFERIDO, ADMITE_AUTOMATICA_CORRIENTE, ADMITE_MANUAL_DIFERIDO, ADMITE_MANUAL_CORRIENTE, "
                + "REQUIERE_LECTURA_MANUAL, MONTO_MAX_AUTORIZA, CODMEDPAG_ELEC "
                + "FROM " + getNombreElementoEmpresa(TABLA) + " PAG "
                + "INNER JOIN " + getNombreElementoEmpresa(TABLA_TIENDA) + " TIE ON (PAG.CODMEDPAG = TIE.CODMEDPAG) "
                + "WHERE ACTIVO = 'S' "
                + "AND PAG.CODMEDPAG = '" + codMedPag + "' "
                + "AND TIE.CODALM = '" + VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN) + "' "
                + "AND (TIE.CAJAS LIKE '%" + Sesion.getDatosConfiguracion().getCodcaja() + "%' OR TIE.CAJAS IS NULL)";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultar() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                medioPago = new MedioPagoBean();
                medioPago.setCodMedioPago(rs.getString("CODMEDPAG"));
                medioPago.setDesMedioPago(rs.getString("DESMEDPAG"));
                medioPago.setContado(rs.getString("CONTADO"));
                medioPago.setOtros(rs.getString("OTROS"));
                medioPago.setTarjetaCredito(rs.getString("TARJETA_CREDITO"));
                medioPago.setTarjetaSukasa(rs.getString("TARJETA_SUKASA") == null ? "N" : rs.getString("TARJETA_SUKASA"));
                medioPago.setVisibleVenta(rs.getString("VISIBLE_VENTA"));
                medioPago.setVisibleTiendaVirtual(rs.getString("VISIBLE_TIENDA_VIRTUAL"));
                medioPago.setVisibleCompra(rs.getString("VISIBLE_COMPRA"));
                medioPago.setActivo(rs.getString("ACTIVO"));
                medioPago.setAbrirCajon(rs.getString("ABRIR_CAJON"));
                medioPago.setGiftCard(rs.getString("GIFTCARD"));
                medioPago.setCreditoDirecto(rs.getString("CREDITO_DIRECTO"));
                medioPago.setMontoMaximoAutorizacion(rs.getBigDecimal("MONTO_MAX_AUTORIZA"));
                medioPago.setCodMedPagElec(rs.getString("CODMEDPAG_ELEC"));

                if (medioPago.isTarjetaCredito()) {
                    medioPago.setAdmiteAutorizacionAutomaticaDiferido(rs.getString("ADMITE_AUTOMATICA_DIFERIDO"));
                    medioPago.setAdmiteAutorizacionAutomaticaCorriente(rs.getString("ADMITE_AUTOMATICA_CORRIENTE"));
                    medioPago.setAdmiteAutorizacionManualCorriente(rs.getString("ADMITE_MANUAL_CORRIENTE"));
                    medioPago.setAdmiteAutorizacionManualDiferido(rs.getString("ADMITE_MANUAL_DIFERIDO"));
                    medioPago.setRequiereAutorizacionLecturaManual(rs.getString("REQUIERE_LECTURA_MANUAL"));
                }
                if (medioPago.isTarjetaCredito() && !medioPago.isTarjetaSukasa()) {
                    medioPago.setTipoPagoCorriente(rs.getString("TIPO_PAGO_CORRIENTE"));
                    medioPago.setTipoPagoDiferido(rs.getString("TIPO_PAGO_DIFERIDO"));
                    medioPago.setCodEstCorriente(rs.getString("CORRIENTE"));
                    medioPago.setCodEstDiferidoConIntereses(rs.getString("DIFERIDO_CON_INTERES"));
                    medioPago.setCodEstDiferidoSinIntereses(rs.getString("DIFERIDO_SIN_INTERES"));

                }
                if (rs.getString("BINES") == null) {
                    medioPago.setBines(null);
                } else {
                    medioPago.setBines(rs.getString("BINES").split(";"));
                }
                if (rs.getString("ADMITE_VUELTO") == null) {
                    medioPago.setAdmiteVuelto("N");
                } else {
                    medioPago.setAdmiteVuelto(rs.getString("ADMITE_VUELTO"));
                }
                if (rs.getString("PERMITE_PAGAR_GIFTC_CORRIENTE") == null) {
                    medioPago.setPermitePagarGiftCardCorriente("N");
                } else {
                    medioPago.setPermitePagarGiftCardCorriente(rs.getString("PERMITE_PAGAR_GIFTC_CORRIENTE"));
                }
                if (rs.getString("PERMITE_PAGAR_GIFTC_DIFERIDO") == null) {
                    medioPago.setPermitePagarGiftCardDiferido("N");
                } else {
                    medioPago.setPermitePagarGiftCardDiferido(rs.getString("PERMITE_PAGAR_GIFTC_DIFERIDO"));
                }
                if (rs.getString("ADMITE_ABONO_RESERVA_CORRIENTE") == null) {
                    medioPago.setAdmiteAbonoReservacionCorriente("N");
                } else {
                    medioPago.setAdmiteAbonoReservacionCorriente(rs.getString("ADMITE_ABONO_RESERVA_CORRIENTE"));
                }
                if (rs.getString("ADMITE_ABONO_RESERVA_DIFERIDO") == null) {
                    medioPago.setAdmiteAbonoReservacionDiferido("N");
                } else {
                    medioPago.setAdmiteAbonoReservacionDiferido(rs.getString("ADMITE_ABONO_RESERVA_DIFERIDO"));
                }
                if (rs.getString("ADMITE_PAGO_CREDITO_TEMPORAL") == null) {
                    medioPago.setAdmitePagoCreditoTemporal("N");
                } else {
                    medioPago.setAdmitePagoCreditoTemporal(rs.getString("ADMITE_PAGO_CREDITO_TEMPORAL"));
                }
                if (rs.getString("ADMITE_ABONO_TARJETA_PROPIA") == null) {
                    medioPago.setAdmiteAbonoTarjetaPropia("N");
                } else {
                    medioPago.setAdmiteAbonoTarjetaPropia(rs.getString("ADMITE_ABONO_TARJETA_PROPIA"));
                }
                if (rs.getString("REQUIERE_AUTORIZACION") == null) {
                    medioPago.setRequiereAutorizacion("N");
                } else {
                    medioPago.setRequiereAutorizacion(rs.getString("REQUIERE_AUTORIZACION"));
                }
                if (rs.getString("TIENE_INFOEXTRA_1") == null) {
                    medioPago.setTieneInfoExtra1("N");
                } else {
                    medioPago.setTieneInfoExtra1("N");
                    medioPago.setInfoExtra1(rs.getString("LABEL_INFOEXTRA_1"));
                }
                if (rs.getString("TIENE_INFOEXTRA_2") == null) {
                    medioPago.setTieneInfoExtra2("N");
                } else {
                    medioPago.setTieneInfoExtra2("N");
                    medioPago.setInfoExtra2(rs.getString("LABEL_INFOEXTRA_2"));
                }
                if (rs.getString("TIENE_INFOEXTRA_3") == null) {
                    medioPago.setTieneInfoExtra3("N");
                } else {
                    medioPago.setTieneInfoExtra3("N");
                    medioPago.setInfoExtra3(rs.getString("LABEL_INFOEXTRA_3"));
                }
            }
            return medioPago;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {;
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

    public static void consultarMediosPago(Connection conn, MediosPago mediosPago) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        sql = "SELECT PAG.CODMEDPAG AS CODMEDPAG, DESMEDPAG, CONTADO, EFECTIVO, TARJETA_CREDITO, CREDITO_DIRECTO, "
                + "OTROS, VISIBLE_VENTA, VISIBLE_TIENDA_VIRTUAL, VISIBLE_COMPRA, ACTIVO, ADMITE_VUELTO, "
                + "ADMITE_ABONO_RESERVA_CORRIENTE, ADMITE_ABONO_RESERVA_DIFERIDO, REQUIERE_AUTORIZACION, "
                + "TIENE_INFOEXTRA_1, TIENE_INFOEXTRA_2, TIENE_INFOEXTRA_3, LABEL_INFOEXTRA_1, "
                + "LABEL_INFOEXTRA_2, LABEL_INFOEXTRA_3, BINES, ABRIR_CAJON, GIFTCARD, PERMITE_PAGAR_GIFTC_CORRIENTE, "
                + "PERMITE_PAGAR_GIFTC_DIFERIDO, ADMITE_PAGO_CREDITO_TEMPORAL, ADMITE_ABONO_TARJETA_PROPIA, "
                + "CORRIENTE, DIFERIDO_SIN_INTERES, DIFERIDO_CON_INTERES, TARJETA_SUKASA, TIPO_PAGO_CORRIENTE, TIPO_PAGO_DIFERIDO, "
                + "ADMITE_AUTOMATICA_DIFERIDO, ADMITE_AUTOMATICA_CORRIENTE, ADMITE_MANUAL_DIFERIDO, ADMITE_MANUAL_CORRIENTE, "
                + "REQUIERE_LECTURA_MANUAL, MONTO_MAX_AUTORIZA, CODMEDPAG_ELEC, PAG.CODBAN  "
                + "FROM " + getNombreElementoEmpresa(TABLA) + " PAG "
                + "INNER JOIN " + getNombreElementoEmpresa(TABLA_TIENDA) + " TIE ON (PAG.CODMEDPAG = TIE.CODMEDPAG) "
                + "WHERE ACTIVO = 'S' "
                + "AND VISIBLE_VENTA = 'S' "
                + "AND TIE.CODALM = '" + VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN) + "' "
                + "AND (TIE.CAJAS LIKE '%" + Sesion.getDatosConfiguracion().getCodcaja() + "%' OR TIE.CAJAS IS NULL)";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultarMediosPago() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                MedioPagoBean medioPago = new MedioPagoBean();
                medioPago.setCodMedioPago(rs.getString("CODMEDPAG"));
                medioPago.setDesMedioPago(rs.getString("DESMEDPAG"));
                medioPago.setContado(rs.getString("CONTADO"));
                medioPago.setOtros(rs.getString("OTROS"));
                medioPago.setTarjetaCredito(rs.getString("TARJETA_CREDITO"));
                medioPago.setTarjetaSukasa(rs.getString("TARJETA_SUKASA") == null ? "N" : rs.getString("TARJETA_SUKASA"));
                medioPago.setVisibleVenta(rs.getString("VISIBLE_VENTA"));
                medioPago.setVisibleTiendaVirtual(rs.getString("VISIBLE_TIENDA_VIRTUAL"));
                medioPago.setVisibleCompra(rs.getString("VISIBLE_COMPRA"));
                medioPago.setActivo(rs.getString("ACTIVO"));
                medioPago.setAbrirCajon(rs.getString("ABRIR_CAJON"));
                medioPago.setGiftCard(rs.getString("GIFTCARD"));
                medioPago.setCreditoDirecto(rs.getString("CREDITO_DIRECTO"));
                medioPago.setMontoMaximoAutorizacion(rs.getBigDecimal("MONTO_MAX_AUTORIZA"));
                medioPago.setCodMedPagElec(rs.getString("CODMEDPAG_ELEC"));
                medioPago.setCodBan(rs.getString("CODBAN"));

                if (medioPago.isTarjetaCredito()) {
                    medioPago.setAdmiteAutorizacionAutomaticaDiferido(rs.getString("ADMITE_AUTOMATICA_DIFERIDO"));
                    medioPago.setAdmiteAutorizacionAutomaticaCorriente(rs.getString("ADMITE_AUTOMATICA_CORRIENTE"));
                    medioPago.setAdmiteAutorizacionManualCorriente(rs.getString("ADMITE_MANUAL_CORRIENTE"));
                    medioPago.setAdmiteAutorizacionManualDiferido(rs.getString("ADMITE_MANUAL_DIFERIDO"));
                    medioPago.setRequiereAutorizacionLecturaManual(rs.getString("REQUIERE_LECTURA_MANUAL"));
                }
                if ((medioPago.isTarjetaCredito() || medioPago.isOtros()) && !medioPago.isTarjetaSukasa()) {
                    if (rs.getString("TIPO_PAGO_CORRIENTE") != null) {
                        medioPago.setTipoPagoCorriente(rs.getString("TIPO_PAGO_CORRIENTE"));
                    }
                    if (rs.getString("TIPO_PAGO_DIFERIDO") != null) {
                        medioPago.setTipoPagoDiferido(rs.getString("TIPO_PAGO_DIFERIDO"));
                    }
                    if (rs.getString("CORRIENTE") != null) {
                        medioPago.setCodEstCorriente(rs.getString("CORRIENTE"));
                    }
                    if (rs.getString("DIFERIDO_CON_INTERES") != null) {
                        medioPago.setCodEstDiferidoConIntereses(rs.getString("DIFERIDO_CON_INTERES"));
                    }
                    if (rs.getString("DIFERIDO_SIN_INTERES") != null) {
                        medioPago.setCodEstDiferidoSinIntereses(rs.getString("DIFERIDO_SIN_INTERES"));
                    }

                }
                if (rs.getString("BINES") == null) {
                    medioPago.setBines(null);
                } else {
                    medioPago.setBines(rs.getString("BINES").split(";"));
                }
                if (rs.getString("ADMITE_VUELTO") == null) {
                    medioPago.setAdmiteVuelto("N");
                } else {
                    medioPago.setAdmiteVuelto(rs.getString("ADMITE_VUELTO"));
                }
                if (rs.getString("PERMITE_PAGAR_GIFTC_CORRIENTE") == null) {
                    medioPago.setPermitePagarGiftCardCorriente("N");
                } else {
                    medioPago.setPermitePagarGiftCardCorriente(rs.getString("PERMITE_PAGAR_GIFTC_CORRIENTE"));
                }
                if (rs.getString("PERMITE_PAGAR_GIFTC_DIFERIDO") == null) {
                    medioPago.setPermitePagarGiftCardDiferido("N");
                } else {
                    medioPago.setPermitePagarGiftCardDiferido(rs.getString("PERMITE_PAGAR_GIFTC_DIFERIDO"));
                }
                if (rs.getString("ADMITE_ABONO_RESERVA_CORRIENTE") == null) {
                    medioPago.setAdmiteAbonoReservacionCorriente("N");
                } else {
                    medioPago.setAdmiteAbonoReservacionCorriente(rs.getString("ADMITE_ABONO_RESERVA_CORRIENTE"));
                }
                if (rs.getString("ADMITE_ABONO_RESERVA_DIFERIDO") == null) {
                    medioPago.setAdmiteAbonoReservacionDiferido("N");
                } else {
                    medioPago.setAdmiteAbonoReservacionDiferido(rs.getString("ADMITE_ABONO_RESERVA_DIFERIDO"));
                }
                if (rs.getString("ADMITE_PAGO_CREDITO_TEMPORAL") == null) {
                    medioPago.setAdmitePagoCreditoTemporal("N");
                } else {
                    medioPago.setAdmitePagoCreditoTemporal(rs.getString("ADMITE_PAGO_CREDITO_TEMPORAL"));
                }
                if (rs.getString("ADMITE_ABONO_TARJETA_PROPIA") == null) {
                    medioPago.setAdmiteAbonoTarjetaPropia("N");
                } else {
                    medioPago.setAdmiteAbonoTarjetaPropia(rs.getString("ADMITE_ABONO_TARJETA_PROPIA"));
                }
                if (rs.getString("REQUIERE_AUTORIZACION") == null) {
                    medioPago.setRequiereAutorizacion("N");
                } else {
                    medioPago.setRequiereAutorizacion(rs.getString("REQUIERE_AUTORIZACION"));
                }
                if (rs.getString("TIENE_INFOEXTRA_1") == null) {
                    medioPago.setTieneInfoExtra1("N");
                } else {
                    medioPago.setTieneInfoExtra1("N");
                    medioPago.setInfoExtra1(rs.getString("LABEL_INFOEXTRA_1"));
                }
                if (rs.getString("TIENE_INFOEXTRA_2") == null) {
                    medioPago.setTieneInfoExtra2("N");
                } else {
                    medioPago.setTieneInfoExtra2("N");
                    medioPago.setInfoExtra2(rs.getString("LABEL_INFOEXTRA_2"));
                }
                if (rs.getString("TIENE_INFOEXTRA_3") == null) {
                    medioPago.setTieneInfoExtra3("N");
                } else {
                    medioPago.setTieneInfoExtra3("N");
                    medioPago.setInfoExtra3(rs.getString("LABEL_INFOEXTRA_3"));
                }

                mediosPago.addMedioPago(medioPago);
            }
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {;
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

    public static void consultarBancos(Connection conn, MediosPago mediosPago) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT BANCO.CODBAN, DESBAN, DOMICILIO, POBLACION, PROVINCIA, TELEFONO1, TELEFONO2, FAX, CCC, "
                + "CIF, CP, OBSERVACIONES, ACTIVO, CODMEDPAG "
                + "FROM " + getNombreElementoEmpresa(TABLA_BANCOS) + " BANCO "
                + "INNER JOIN " + getNombreElementoEmpresa(TABLA_RELACION_BANCOS) + " PAGO "
                + "ON (BANCO.CODBAN = PAGO.CODBAN)"
                + "WHERE ACTIVO = 'S' ";

        try {
            log.debug("consultarBancos() - " + sql);
            pstmt = new PreparedStatement(conn, sql);
            rs = pstmt.executeQuery();

            BancoBean banco = null;
            while (rs.next()) {
                banco = new BancoBean();
                banco.setCodBan(rs.getString("CODBAN"));
                banco.setDesBan(rs.getString("DESBAN"));
                banco.setDomicilio(rs.getString("DOMICILIO"));
                banco.setPoblacion(rs.getString("POBLACION"));
                banco.setProvincia(rs.getString("PROVINCIA"));
                banco.setTelefono1(rs.getString("TELEFONO1"));
                banco.setTelefono2(rs.getString("TELEFONO2"));
                banco.setFax(rs.getString("FAX"));
                banco.setCcc(rs.getString("CCC"));
                banco.setCif(rs.getString("CIF"));
                banco.setCp(rs.getString("CP"));
                banco.setObservaciones(rs.getString("OBSERVACIONES"));
                banco.setActivo(rs.getString("ACTIVO"));
                mediosPago.addBanco(banco, rs.getString("CODMEDPAG"));
            }
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {;
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

    public static void consultarTiposClientes(Connection conn, MediosPago mediosPago) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT TIP.COD_TIPO_CLIENTE, DES_TIPO_CLIENTE, CODMEDPAG "
                + "FROM " + getNombreElementoEmpresa(TABLA_RELACION_TIPOS_CLIENTES) + " PAG "
                + "INNER JOIN " + getNombreElementoEmpresa(TABLA_TIPOS_CLIENTES) + " TIP "
                + "ON (TIP.COD_TIPO_CLIENTE = PAG.COD_TIPO_CLIENTE)";

        try {
            log.debug("consultarTiposClientes() - " + sql);
            pstmt = new PreparedStatement(conn, sql);
            rs = pstmt.executeQuery();

            TipoClienteBean tipoCliente = null;
            while (rs.next()) {
                tipoCliente = new TipoClienteBean();
                tipoCliente.setCodTipoCliente(rs.getLong("COD_TIPO_CLIENTE"));
                tipoCliente.setDesTipoCliente(rs.getString("DES_TIPO_CLIENTE"));
                mediosPago.addTipoCliente(tipoCliente, rs.getString("CODMEDPAG"));
            }
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {;
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

    public static void consultarVencimientos(Connection conn, MediosPago mediosPago) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;

        sql = "SELECT ID_MEDPAG_VEN, DESMEDPAG_VEN, NUMERO_VENCIMIENTOS, PISO_MAXIMO, TIPO_CREDITO, CODMEDPAG, PLACE_TO_PAY "
                + " FROM " + getNombreElementoEmpresa(TABLA_VENCIMIENTOS)
                + " WHERE ACTIVO = 'S' "
                + " ORDER BY TO_NUMBER(CODMEDPAG) DESC,NUMERO_VENCIMIENTOS DESC";

        try {
            pstmt = new PreparedStatement(conn, sql);

            log.debug("consultarVencimientos() - " + sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                VencimientoBean vencimiento = new VencimientoBean(mediosPago);
                vencimiento.setCodMedioPago(rs.getString("CODMEDPAG"));
                vencimiento.setDesVencimiento(rs.getString("DESMEDPAG_VEN"));
                vencimiento.setNumeroVencimientos(rs.getInt("NUMERO_VENCIMIENTOS"));
                vencimiento.setIdMedioPagoVencimiento(rs.getLong("ID_MEDPAG_VEN"));
                vencimiento.setPisoMaximo(rs.getBigDecimal("PISO_MAXIMO"));
                vencimiento.setTipoCredito(rs.getString("TIPO_CREDITO"));
                vencimiento.setPlaceToPay("S".equals(rs.getString("PLACE_TO_PAY")));

                mediosPago.addVencimiento(vencimiento);
            }
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
                ;
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {
                ;
            }
        }
    }

    public static void consultarDescuentos(Connection conn, MediosPago mediosPago) throws SQLException {
        consultarDescuentos(conn, mediosPago, VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
    }

    public static void consultarDescuentos(Connection conn, MediosPago mediosPago, String codAlm) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT ID_MEDPAG_VEN, CODALM, COD_TIPO_CLIENTE, DESCUENTO_AFILIADO, "
                + "DESCUENTO_NO_AFILIADO, CODALM, FECHA_HORA_DESDE, ACTIVO, "
                + "FECHA_HORA_HASTA, INTERES_AFILIADO, INTERES_NO_AFILIADO, PISO_MINIMO, CODMEDPAG, DESCUENTO_AF_PROMO "
                + "FROM " + getNombreElementoEmpresa(TABLA_DESCUENTOS)
                + "WHERE CODALM = '" + codAlm + "'";

        try {
            log.debug("consultarDescuentos() - " + sql);
            pstmt = new PreparedStatement(conn, sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                DescuentoBean descuento = new DescuentoBean(rs);
                mediosPago.addDescuento(descuento);
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
    }

    public static boolean consultarEnListaNegra(EntityManager em, String tarjeta) throws Exception {
        try {
            Query consulta = em.createQuery("SELECT t FROM TarjetaEnListaNegra t WHERE t.numeroTarjeta = :numeroTarjeta AND t.activo = :activo");
            consulta.setParameter("numeroTarjeta", Long.valueOf(tarjeta));
            consulta.setParameter("activo", 'S');
            if (!consulta.getResultList().isEmpty()) {
                return true;
            }
            return false;
        } catch (NoResultException e) {
            return false;
        }
    }

    public static String consultarNumeroPorCedula(Connection conn, String cedula, Long formato) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        String numeroTarjeta = null;

        String nombreTabla = " CESTACTUAL@SUKASA.SUKASA.COM ";
        String nombreTablaPlastico = " PLASTICO@SUKASA.SUKASA.COM ";
        if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")) {
            //Esta utilizando las tablas de COMMERZIA
            nombreTabla = TABLA_ESTADO_POS;
            nombreTablaPlastico = TABLA_PLASTICO_POS;
        }

        sql = "SELECT MAX (p_num), MAX (p_credito) "
                + "FROM(SELECT p.p_num, p.p_credito, p.P_SEC, PRIORIDAD, "
                + "rank() over (partition BY p.p_id order by vmp.PRIORIDAD ASC) rnk "
                + "FROM " + nombreTablaPlastico + " p, ( "
                + "SELECT DISTINCT b1.CODMEDPAG, b1.PRIORIDAD, b1.BINES, f1.FORMATO FROM( "
                + "SELECT CODMEDPAG, PRIORIDAD, regexp_substr(BINES, '([^;]*)[;]{0,1}', 1, level, 'i', 1 ) BINES "
                + "FROM (SELECT mp.CODMEDPAG, mp.LABEL_INFOEXTRA_1 AS PRIORIDAD, mp.BINES "
                + "FROM " + getNombreElementoEmpresa(TABLA) + " mp "
                + "WHERE mp.TARJETA_SUKASA = 'S' "
                + "AND mp.ACTIVO = 'S' "
                + "AND mp.LABEL_INFOEXTRA_1 IS NOT NULL) "
                + "CONNECT BY LEVEL <= regexp_count(BINES, '[;]' ) + 1 "
                + ")b1, ( "
                + "SELECT DISTINCT CODMEDPAG,FORMATO "
                + "FROM( "
                + "SELECT CODMEDPAG, regexp_substr(FORMATO, '([^;]*)[;]{0,1}', 1, level, 'i', 1 ) FORMATO "
                + "FROM (SELECT mp.CODMEDPAG, mp.LABEL_INFOEXTRA_2 AS FORMATO "
                + "FROM " + getNombreElementoEmpresa(TABLA) + " mp "
                + "WHERE mp.TARJETA_SUKASA = 'S' "
                + "AND mp.ACTIVO = 'S' "
                + "AND mp.LABEL_INFOEXTRA_1 IS NOT NULL) "
                + "CONNECT BY LEVEL <= regexp_count(FORMATO, '[;]' ) + 1 "
                + ")WHERE FORMATO IS NOT NULL "
                + ")f1 WHERE b1.CODMEDPAG = f1.CODMEDPAG AND  BINES IS NOT NULL)vmp "
                + "WHERE SUBSTR(p.P_NUM,0,6) = vmp.BINES "
                + "AND p_id = ? ";
        if (formato != null) {
            sql += "AND vmp.FORMATO = ? ";
        }

        sql += "AND p.p_num NOT IN ( "
                + "SELECT c.a_plastico "
                + "FROM " + nombreTabla + " c "
                + "WHERE c.a_tipo = 'P' "
                + "AND c.a_estatus = 'C' "
                + "AND c.a_fechault IS NULL "
                + "AND c.a_plastico = p.p_num) "
                + "AND p.p_credito NOT IN ( "
                + "SELECT c.a_credito "
                + "FROM " + nombreTabla + " c "
                + "WHERE c.a_tipo = 'C' "
                + "AND c.a_estatus = 'C' "
                + "AND c.a_fechault IS NULL "
                + "AND c.a_credito = p.p_credito) "
                + ")WHERE rnk = 1";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, cedula);
            if (formato != null) {
                pstmt.setLong(2, formato);
            }
            log.debug("consultarNumeroPorCedula() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                numeroTarjeta = rs.getString(1);
                log.debug("consultarNumeroPorCedula() - numeroTarjeta " + numeroTarjeta);
                return numeroTarjeta;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
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
        return null;
    }

    public static String consultarNumeroCanceladoPorCedula(Connection conn, String cedula, Long formato) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        String numeroTarjeta = null;

        String nombreTabla = " CESTACTUAL@SUKASA.SUKASA.COM ";
        String nombreTablaPlastico = " PLASTICO@SUKASA.SUKASA.COM ";
        if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")) {
            //Esta utilizando las tablas de COMMERZIA
            nombreTabla = TABLA_ESTADO_POS;
            nombreTablaPlastico = TABLA_PLASTICO_POS;
        }

        sql = "SELECT MAX (p_num), MAX (p_credito) "
                + "FROM(SELECT p.p_num, p.p_credito, p.P_SEC, PRIORIDAD, "
                + "rank() over (partition BY p.p_id order by vmp.PRIORIDAD ASC) rnk "
                + "FROM " + nombreTablaPlastico + " p, ( "
                + "SELECT DISTINCT b1.CODMEDPAG, b1.PRIORIDAD, b1.BINES, f1.FORMATO FROM( "
                + "SELECT CODMEDPAG, PRIORIDAD, regexp_substr(BINES, '([^;]*)[;]{0,1}', 1, level, 'i', 1 ) BINES "
                + "FROM (SELECT mp.CODMEDPAG, mp.LABEL_INFOEXTRA_1 AS PRIORIDAD, mp.BINES "
                + "FROM " + getNombreElementoEmpresa(TABLA) + " mp "
                + "WHERE mp.TARJETA_SUKASA = 'S' "
                + "AND mp.ACTIVO = 'S' "
                + "AND mp.LABEL_INFOEXTRA_1 IS NOT NULL) "
                + "CONNECT BY LEVEL <= regexp_count(BINES, '[;]' ) + 1 "
                + ")b1, ( "
                + "SELECT DISTINCT CODMEDPAG,FORMATO "
                + "FROM( "
                + "SELECT CODMEDPAG, regexp_substr(FORMATO, '([^;]*)[;]{0,1}', 1, level, 'i', 1 ) FORMATO "
                + "FROM (SELECT mp.CODMEDPAG, mp.LABEL_INFOEXTRA_2 AS FORMATO "
                + "FROM " + getNombreElementoEmpresa(TABLA) + " mp "
                + "WHERE mp.TARJETA_SUKASA = 'S' "
                + "AND mp.ACTIVO = 'S' "
                + "AND mp.LABEL_INFOEXTRA_1 IS NOT NULL) "
                + "CONNECT BY LEVEL <= regexp_count(FORMATO, '[;]' ) + 1 "
                + ")WHERE FORMATO IS NOT NULL "
                + ")f1 WHERE b1.CODMEDPAG = f1.CODMEDPAG AND  BINES IS NOT NULL)vmp "
                + "WHERE SUBSTR(p.P_NUM,0,6) = vmp.BINES "
                + "AND p_id = ? ";
        if (formato != null) {
            sql += "AND vmp.FORMATO = ? ";
        }
        sql += ")WHERE rnk = 1";

        try {
            pstmt = new PreparedStatement(conn, sql);
            pstmt.setString(1, cedula);
            if (formato != null) {
                pstmt.setLong(2, formato);
            }
            log.debug("consultarNumeroPorCedula() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                numeroTarjeta = rs.getString(1);
                return numeroTarjeta;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
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
        return null;
    }

    public static MedioPagoBean getCodigoEstablecimiento(Connection conn, String codMedPag) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        MedioPagoBean medioPago = null;

        sql = "SELECT CORRIENTE "
                + "FROM " + getNombreElementoEmpresa(TABLA_TIENDA)
                + "WHERE CODMEDPAG = '" + codMedPag + "' ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultar() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                medioPago = new MedioPagoBean();
                medioPago.setCodEstCorriente(rs.getString("CORRIENTE"));
            }
            return medioPago;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {;
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

    public static MedioPagoBean getEmpleadosAfiliado(Connection conn, String codMedPag, String tarjeta) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        MedioPagoBean medioPago = null;

        sql = "select * from ( "
                + " SELECT CODMEDPAG, PRIORIDAD,  "
                + " regexp_substr(BINES, '([^;]*)[;]{0,1}', 1, level, 'i', 1 ) BINES FROM (SELECT mp.CODMEDPAG, mp.LABEL_INFOEXTRA_1 AS PRIORIDAD,mp.BINES  "
                + " FROM " + getNombreElementoEmpresa(TABLA) + " mp "
                + " WHERE mp.ACTIVO = 'S' AND mp.LABEL_INFOEXTRA_1 IS NOT NULL"
                + " AND CODMEDPAG = '" + codMedPag + "' ) "
                + " CONNECT BY LEVEL <= regexp_count(BINES, '[;]' ) + 1 ) "
                + " where bines in (2481, "
                + " 2900,2901,2902,2903,2904,2905,2906,2907,"
                + " 2908,2909,2910,2911,2912,2914,2916,2923,2960,2964,2965,2050)"
                + " and bines = substr('" + tarjeta + "',1,4)";
//                + " and bines = '" + bin + "'";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultar() - " + pstmt);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                medioPago = new MedioPagoBean();
                medioPago.setCodMedioPago(rs.getString("CODMEDPAG"));
            }
            return medioPago;
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {;
            }
            try {
                pstmt.close();
            } catch (Exception ignore) {;
            }
        }
    }

    /**
     * Devuelve todos los bancos activos
     *
     * author Gabriel Simbania
     *
     * @param conn
     * @return
     * @throws SQLException
     */
    public static List<BancoBean> consultarBancos(Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BancoBean> bancos = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT BANCO.CODBAN, DESBAN, DOMICILIO, POBLACION, PROVINCIA, TELEFONO1, TELEFONO2, FAX, CCC, ");
        sql.append("CIF, CP, OBSERVACIONES, ACTIVO  ");
        sql.append("FROM ").append(getNombreElementoEmpresa(TABLA_BANCOS)).append(" BANCO ");
        sql.append("WHERE ACTIVO = 'S' ");

        try {
            log.debug("consultarBancos() - " + sql);
            pstmt = new PreparedStatement(conn, sql.toString());
            rs = pstmt.executeQuery();

            BancoBean banco = null;
            while (rs.next()) {
                banco = new BancoBean();
                banco.setCodBan(rs.getString("CODBAN"));
                banco.setDesBan(rs.getString("DESBAN"));
                banco.setDomicilio(rs.getString("DOMICILIO"));
                banco.setPoblacion(rs.getString("POBLACION"));
                banco.setProvincia(rs.getString("PROVINCIA"));
                banco.setTelefono1(rs.getString("TELEFONO1"));
                banco.setTelefono2(rs.getString("TELEFONO2"));
                banco.setFax(rs.getString("FAX"));
                banco.setCcc(rs.getString("CCC"));
                banco.setCif(rs.getString("CIF"));
                banco.setCp(rs.getString("CP"));
                banco.setObservaciones(rs.getString("OBSERVACIONES"));
                banco.setActivo(rs.getString("ACTIVO"));
                bancos.add(banco);
            }

            return bancos;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }

        }
    }

    /**
     *
     * @param conn
     * @param id
     * @return
     * @throws SQLException
     */
    public static String consultarCodMedByIdPlan(Connection conn, Long id) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = null;
        String codMedPag = null;

        sql = "SELECT VEN.CODMEDPAG CODMEDPAG"
                + " FROM " + getNombreElementoEmpresa(TABLA_VENCIMIENTOS) + " VEN "
                + " WHERE ACTIVO = 'S' "
                + " AND VEN.ID_MEDPAG_VEN = " + id + " ";

        try {
            pstmt = new PreparedStatement(conn, sql);
            log.debug("consultar() - " + pstmt);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                codMedPag = (rs.getString("CODMEDPAG"));
            }
            return codMedPag;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

}
