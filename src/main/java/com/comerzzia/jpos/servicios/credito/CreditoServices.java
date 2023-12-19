/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.credito;

import com.comerzzia.jpos.dto.ventas.TramaCreditoDTO;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.CupoVirtual;
import com.comerzzia.jpos.persistencia.credito.abonos.AbonoCreditoBean;
import com.comerzzia.jpos.persistencia.credito.abonos.AbonoCreditoExample;
import com.comerzzia.jpos.persistencia.credito.abonos.AbonoCreditoMapper;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.persistencia.credito.cuposvirtuales.CupoVirtualBean;
import com.comerzzia.jpos.persistencia.credito.cuposvirtuales.CupoVirtualDao;
import com.comerzzia.jpos.persistencia.credito.plasticos.CuentaEstadoBean;
import com.comerzzia.jpos.persistencia.credito.plasticos.PlasticoBean;
import com.comerzzia.jpos.persistencia.credito.plasticos.PlasticoEstadoBean;
import com.comerzzia.jpos.persistencia.credito.plasticos.PlasticosDao;
import com.comerzzia.jpos.persistencia.credito.tarjetahabiente.TarjetaHabienteDao;
import com.comerzzia.jpos.persistencia.mediospagos.MediosPagoDao;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.servicios.bonos.BonosServices;
import com.comerzzia.jpos.servicios.clientes.ClientesServices;
import com.comerzzia.jpos.servicios.core.contadores.caja.ServicioContadoresCaja;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.core.variables.VariablesAlm;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import com.comerzzia.jpos.servicios.tickets.ReferenciaTicket;
import com.comerzzia.jpos.servicios.tickets.TicketService;
import com.comerzzia.jpos.servicios.tickets.xml.TicketXMLServices;
import com.comerzzia.jpos.util.JsonUtil;
import com.comerzzia.jpos.util.thread.ProcesoEncolarThread;
import com.comerzzia.jpos.util.db.Connection;
import com.comerzzia.jpos.util.db.Database;
import com.comerzzia.jpos.util.mybatis.SessionFactory;
import com.comerzzia.jpos.webservice.credito.CreditoWS;
import com.comerzzia.util.Constantes;
import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.fechas.Fecha;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.mybatis.exception.PersistenceException;
import es.mpsistemas.util.mybatis.session.SqlSession;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

/**
 *
 * @author MGRI
 */
public class CreditoServices {

    protected static Logger log = Logger.getMLogger(CreditoServices.class);

    public static void consultarCupoTarjeta(PlasticoBean plastico)
            throws CreditoException, CreditoNotFoundException {
        Connection connSukasa = new Connection();
        try {
            log.debug("consultarCupoTarjeta() - Consultando crédito para tarjeta: " + plastico.getNumeroTarjeta());
            if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                    && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")) {
                connSukasa.abrirConexion(Database.getConnection());
            } else {
                connSukasa.abrirConexion(Database.getConnectionSukasa());
            }
            consultarCupo(connSukasa, plastico);
        } catch (CreditoNotFoundException e) {
            throw e;
        } catch (SQLException e) {
            log.error("consultarCupoTarjeta() - " + e.getMessage(), e);
            String mensaje = "Error al consultar datos de crédito: " + e.getMessage();
            throw new CreditoException(mensaje, e);
        } catch (Exception e) {
            log.error("consultarCupoTarjeta() - " + e.getMessage(), e);
            String mensaje = "Error inesperado al consultar datos de crédito: " + e.getMessage();
            throw new CreditoException(mensaje, e);
        } finally {
            connSukasa.cerrarConexion();
        }
    }

    public static PlasticoBean consultarPlasticoPorNumero(String numeroTarjeta)
            throws CreditoException, CreditoNotFoundException {
        Connection connCredito = new Connection();
        try {
            log.debug("consultarPlasticoPorNumero() - Consultando plástico para tarjeta: " + numeroTarjeta);
            connCredito.abrirConexion(Database.getConnectionCredito());
            PlasticoBean tarjeta = PlasticosDao.consultarPorNumTarjeta(connCredito, numeroTarjeta);
            if (tarjeta == null) {
                throw new CreditoNotFoundException("No se ha encontrado en el sistema la tarjeta con número: " + numeroTarjeta);
            }
            CuentaEstadoBean cuentaEstadoBean = PlasticosDao.consultarEstadoCuenta(connCredito, tarjeta.getNumeroCredito());
            if (cuentaEstadoBean == null || !cuentaEstadoBean.isValida()) {
                throw new CreditoException("El crédito " + tarjeta.getNumeroCredito() + " no está activo ");
            }
            PlasticoEstadoBean estadoTarjeta = PlasticosDao.consultarEstado(connCredito, numeroTarjeta);
            tarjeta.setEstado(estadoTarjeta);
            return tarjeta;
        } catch (CreditoNotFoundException e) {
            throw e;
        } catch (SQLException e) {
            log.error("consultarPlasticoPorNumero() - " + e.getMessage());
            String mensaje = "Error al consultar datos de tarjetas: " + e.getMessage();
            throw new CreditoException(mensaje, e);
        } catch (Exception e) {
            log.error("consultarPlasticoPorNumero() - " + e.getMessage());
            String mensaje = "Error inesperado al consultar datos de tarjetas: " + e.getMessage();
            throw new CreditoException(mensaje, e);
        } finally {
            connCredito.cerrarConexion();
        }
    }

    public static PlasticoBean consultarPlasticoPorCedula(String cedula) throws CreditoException, CreditoNotFoundException {
        Connection connCredito = new Connection();
        Connection conn = new Connection();
        PlasticoBean plasticoElegido = null;

        try {
            log.debug("consultarPlasticoPorCedula() - Consultando plástico para cédula: " + cedula);
            connCredito.abrirConexion(Database.getConnectionCredito());
            conn.abrirConexion(Database.getConnection());
            String numeroTarjeta = null;
            numeroTarjeta = MediosPagoDao.consultarNumeroPorCedula(conn, cedula, Sesion.getTienda().getCodFormato());
            if (numeroTarjeta == null) {
                numeroTarjeta = MediosPagoDao.consultarNumeroPorCedula(conn, cedula, null);
            }
            if (numeroTarjeta != null) {
                PlasticoBean plastic = PlasticosDao.consultarPorNumTarjeta(connCredito, numeroTarjeta);
                if (plastic != null && plastic.getNumeroTarjeta() != null) {
                    plastic.setEstado(PlasticosDao.consultarEstado(connCredito, plastic.getNumeroTarjeta()));
                    if (plastic.getEstado() != null && !plastic.getEstado().isAnulada()) {
                        return plastic;
                    }
                }
            }
            List<PlasticoBean> consultarTodasPorCedulaCliente = PlasticosDao.consultarTodasPorCedulaCliente(connCredito, cedula);

            // Si no encontramos tarjetas, devolvemos una excepción
            if (consultarTodasPorCedulaCliente.isEmpty()) {
                throw new CreditoNotFoundException("No se encontraron tarjetas asociadas válidas");
            }
            // Si solo hay una tarjeta, la devolvemos
            if (consultarTodasPorCedulaCliente.size() == 1) {
                PlasticoEstadoBean estadotarjeta = PlasticosDao.consultarEstado(connCredito, consultarTodasPorCedulaCliente.get(0).getNumeroTarjeta());
                // Es la prioritaria y la seleccionamos
                if (estadotarjeta != null && (estadotarjeta.isValida() || estadotarjeta.isRequiereAutorizacion()) && !estadotarjeta.isAnulada()) {
                    plasticoElegido = consultarTodasPorCedulaCliente.get(0);
                    plasticoElegido.setEstado(estadotarjeta);
                    return plasticoElegido; //RETURN
                } else {
                    throw new CreditoNotFoundException("No se encontraron tarjetas asociadas válidas");
                }
            }
            // Si hay mas de una hay que decidir cual devolver por prioridad
            String prioridades = VariablesAlm.getVariable(VariablesAlm.POS_BINES_PRIORIDAD_FORMATO_CREDITO_DIRECTO);
            String[] prioridadesArray = prioridades.split(";");

            // TODO: MGR - Sacar a una función aparte
            for (String binePrior : prioridadesArray) {
                // Si la tarjeta no está anulada y su fecha es válida y es prioritaria, seleccionaremos dicha tarjeta
                for (PlasticoBean tarjetaConsultada : consultarTodasPorCedulaCliente) {
                    // Comprobamos que no está caducada 
                    if (!tarjetaConsultada.isCaducada()) {
                        if (tarjetaConsultada.getNumeroTarjeta().startsWith(binePrior)) {
                            PlasticoEstadoBean estadotarjeta = PlasticosDao.consultarEstado(connCredito, tarjetaConsultada.getNumeroTarjeta());
                            // Comprobamos estado (valida o requiere autorizacion) y que no esté anulada
                            if (estadotarjeta != null && (estadotarjeta.isValida() || estadotarjeta.isRequiereAutorizacion()) && !estadotarjeta.isAnulada()) {
                                plasticoElegido = tarjetaConsultada;
                                plasticoElegido.setEstado(estadotarjeta);
                                return plasticoElegido; //RETURN
                            }
                        }
                    } else {
                        log.debug("No se ha seleccionado la tarjeta: " + tarjetaConsultada.getNumeroTarjeta() + " porqué está caducada");
                    }
                }
            }
            log.debug("Ningún plástico cumple las condiciones de bines de prioridad, devolvemos el primer plástico que no esté anulado");

            // Si el algoritmo de seleccionar por prioridad no tiene exito. Elegimos la primera tarjeta que nos devuelve la consulta
            // ¿caducada? y ¿cancelada? -> propongo no incluirlas desde la consulta
            for (PlasticoBean tarjetaConsultada : consultarTodasPorCedulaCliente) {
                PlasticoEstadoBean estadotarjeta = PlasticosDao.consultarEstado(connCredito, tarjetaConsultada.getNumeroTarjeta());
                // Es la prioritaria y la seleccionamos
                if (estadotarjeta != null && (estadotarjeta.isValida() || estadotarjeta.isRequiereAutorizacion()) && !estadotarjeta.isAnulada()) {
                    plasticoElegido = tarjetaConsultada;
                    plasticoElegido.setEstado(estadotarjeta);
                    return plasticoElegido; //RETURN
                }
            }
            // Si llegamos aquí y no se ha devuelto una tarjeta porque todas las tarjetas consultadas tienen un estado caducado o no son válidas
            throw new CreditoNotFoundException("No se encontraron tarjetas asociadas válidas");
        } catch (CreditoNotFoundException e) {
            throw e;
        } catch (SQLException e) {
            log.error("consultarPlasticoPorCedula() - " + e.getMessage());
            String mensaje = "Error al consultar datos de tarjetas por cédula : " + e.getMessage();
            throw new CreditoException(mensaje, e);
        } catch (Exception e) {
            log.error("consultarPlasticoPorCedula() - " + e.getMessage());
            String mensaje = "Error inesperado al consultar datos de tarjetas por cédula : " + e.getMessage();
            throw new CreditoException(mensaje, e);
        } finally {
            conn.cerrarConexion();
            connCredito.cerrarConexion();
        }
    }

    public static PlasticoBean consultarPlasticoValidoPorCedula(String cedula)
            throws CreditoException, CreditoNotFoundException {
        Connection connCredito = new Connection();
        PlasticoBean plastico = null;
        try {
            log.debug("consultarPlasticoValidoPorCedula() - Consultando plástico para cédula: " + cedula);
            connCredito.abrirConexion(Database.getConnectionCredito());
            String numeroTarjeta = PlasticosDao.consultarNumeroPorCedula(connCredito, cedula);
            if (numeroTarjeta != null) {
                PlasticoBean plastic = PlasticosDao.consultarPorNumTarjeta(connCredito, numeroTarjeta);
                if (plastic != null) {
                    plastic.setEstado(PlasticosDao.consultarEstado(connCredito, plastic.getNumeroTarjeta()));
                    if (plastic.getEstado() != null && !plastic.getEstado().isAnulada()) {
                        return plastic;
                    }
                }
            }
            List<PlasticoBean> tarjetas = PlasticosDao.consultarTodasPorCedulaCliente(connCredito, cedula);
            if (tarjetas.isEmpty()) {
                throw new CreditoNotFoundException("No se ha encontrado en el sistema la tarjeta con cédula: " + cedula);
            }
            boolean anulada = true;
            int i = 0;
            while (anulada && i < tarjetas.size()) {
                plastico = tarjetas.get(i);
                PlasticoEstadoBean estadoTarjeta = PlasticosDao.consultarEstado(connCredito, plastico.getNumeroTarjeta());
                plastico.setEstado(estadoTarjeta);
                if (estadoTarjeta != null && !plastico.getEstado().isAnulada()) {
                    anulada = false;
                }
                i++;
            }
            return plastico;
        } catch (CreditoNotFoundException e) {
            throw e;
        } catch (SQLException e) {
            log.error("consultarPlasticoValidoPorCedula() - " + e.getMessage());
            String mensaje = "Error al consultar datos de tarjetas por cédula : " + e.getMessage();
            throw new CreditoException(mensaje, e);
        } catch (Exception e) {
            log.error("consultarPlasticoValidoPorCedula() - " + e.getMessage());
            String mensaje = "Error inesperado al consultar datos de tarjetas por cédula : " + e.getMessage();
            throw new CreditoException(mensaje, e);
        } finally {
            connCredito.cerrarConexion();
        }
    }

    public static CreditoDirectoBean consultarCreditoDirecto(String numeroTarjeta, String cedulaCliente)
            throws CreditoException, CreditoNotFoundException {
        Connection connSukasa = new Connection();
        Connection connCredito = new Connection();
        Connection conn = new Connection();
        PlasticoBean tarjeta = null;
        String estado = null;
        Sesion.setPlasticoBean(tarjeta);

        try {
            if (numeroTarjeta == null && cedulaCliente == null) {
                throw new IllegalArgumentException("Los parámetros de búsqueda (numeroTarjeta y cedulaCliente) son ambos NULL");
            }

            if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                    && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")) {
                connSukasa.abrirConexion(Database.getConnection());
                connCredito.abrirConexion(Database.getConnection());
            } else {
                connCredito.abrirConexion(Database.getConnectionCredito());
                connSukasa.abrirConexion(Database.getConnectionSukasa());
            }

            conn.abrirConexion(Database.getConnection());

            // consultamos la tarjeta a partir del cliente o del número
            if (numeroTarjeta != null) {
                log.debug("consultarCupoYDatosTarjeta() - Consultando credito para tarjeta: " + numeroTarjeta);
                tarjeta = PlasticosDao.consultarPorNumTarjeta(connCredito, numeroTarjeta);
                if (tarjeta == null) {
                    throw new CreditoNotFoundException("No se ha encontrado en el sistema la tarjeta con número: " + numeroTarjeta);
                }
                PlasticoEstadoBean estadoTarjeta = PlasticosDao.consultarEstado(connCredito, numeroTarjeta);
                if (estadoTarjeta != null) {
                    if (estadoTarjeta.getEstadoCta() == null) {
                        if (numeroTarjeta == null) {
                            estado = PlasticosDao.consultarEstadoPorTarjeta(conn, tarjeta.getNumeroTarjeta());
                        } else {
                            estado = PlasticosDao.consultarEstadoPorTarjeta(conn, numeroTarjeta);
                        }
                        estadoTarjeta = new PlasticoEstadoBean();
                        if (estado == null) {
                            if (numeroTarjeta == null) {
                                estado = PlasticosDao.consultarEstadoPorTarjetaStatusAnt(conn, tarjeta.getNumeroTarjeta());
                            } else {
                                estado = PlasticosDao.consultarEstadoPorTarjetaStatusAnt(conn, numeroTarjeta);
                            }
                        }
                        estadoTarjeta.setEstadoCta(estado);
                    }
                } else {
                    if (numeroTarjeta == null) {
                        estado = PlasticosDao.consultarEstadoPorTarjeta(conn, tarjeta.getNumeroTarjeta());
                    } else {
                        estado = PlasticosDao.consultarEstadoPorTarjeta(conn, numeroTarjeta);
                    }
                    estadoTarjeta = new PlasticoEstadoBean();
                    if (estado == null) {
                        if (numeroTarjeta == null) {
                            estado = PlasticosDao.consultarEstadoPorTarjetaStatusAnt(conn, tarjeta.getNumeroTarjeta());
                        } else {
                            estado = PlasticosDao.consultarEstadoPorTarjetaStatusAnt(conn, numeroTarjeta);
                        }
                    }
                    estadoTarjeta.setEstadoCta(estado);
                }
                tarjeta.setEstado(estadoTarjeta);
            } else if (cedulaCliente != null) {
                log.debug("consultarCupoYDatosTarjeta() - Consultando credito para tarjeta de cliente con cédula: " + cedulaCliente);
                String numTarjeta = null;
                numTarjeta = MediosPagoDao.consultarNumeroPorCedula(conn, cedulaCliente, null);
                if (numTarjeta == null) {
                    numTarjeta = MediosPagoDao.consultarNumeroCanceladoPorCedula(conn, cedulaCliente, null);
                }
                if (numTarjeta != null) {
                    tarjeta = PlasticosDao.consultarPorNumTarjeta(connCredito, numTarjeta);
                } else {
                    throw new CreditoNotFoundException("No se ha encontrado en el sistema la tarjeta de cliente con cédula: " + cedulaCliente);
                }
                PlasticoEstadoBean estadoTarjeta = PlasticosDao.consultarEstado(connCredito, tarjeta.getNumeroTarjeta());
                if (estadoTarjeta != null) {
                    if (estadoTarjeta.getEstadoCta() == null) {
                        if (numeroTarjeta == null) {
                            estado = PlasticosDao.consultarEstadoPorTarjeta(conn, tarjeta.getNumeroTarjeta());
                        } else {
                            estado = PlasticosDao.consultarEstadoPorTarjeta(conn, numeroTarjeta);
                        }
                        estadoTarjeta = new PlasticoEstadoBean();
                        if (estado == null) {
                            if (numeroTarjeta == null) {
                                estado = PlasticosDao.consultarEstadoPorTarjetaStatusAnt(conn, tarjeta.getNumeroTarjeta());
                            } else {
                                estado = PlasticosDao.consultarEstadoPorTarjetaStatusAnt(conn, numeroTarjeta);
                            }
                        }
                        estadoTarjeta.setEstadoCta(estado);
                    }

                } else {
                    if (numeroTarjeta == null) {
                        estado = PlasticosDao.consultarEstadoPorTarjeta(conn, tarjeta.getNumeroTarjeta());
                    } else {
                        estado = PlasticosDao.consultarEstadoPorTarjeta(conn, numeroTarjeta);
                    }
                    estadoTarjeta = new PlasticoEstadoBean();
                    if (estado == null) {
                        if (numeroTarjeta == null) {
                            estado = PlasticosDao.consultarEstadoPorTarjetaStatusAnt(conn, tarjeta.getNumeroTarjeta());
                        } else {
                            estado = PlasticosDao.consultarEstadoPorTarjetaStatusAnt(conn, numeroTarjeta);
                        }
                    }
                    estadoTarjeta.setEstadoCta(estado);
                }

                tarjeta.setEstado(estadoTarjeta);
            }

            // consultamos cupo de la tarjeta
            consultarCupo(connSukasa, tarjeta);

            // consultamos datos básicos del cliente
            Cliente cliente = ClientesServices.getInstance().consultaClienteIdenti(tarjeta.getCedulaCliente());
            tarjeta.setCliente(cliente);
            Sesion.setPlasticoBean(tarjeta);

            // construimos crédito directo en la central
            CreditoDirectoBean creditoDirecto = new CreditoDirectoBean();
            creditoDirecto.setPlastico(tarjeta);

            try {
//                CreditoWS creditoWS = new CreditoWS(Variables.getVariable(Variables.POS_CONFIG_WEBSERVICE_CREDITO));
//                if(creditoWS.getUrlWebservice() == null){
//                    log.error("No se configurado la URL para el webservice de Credito");
//                    throw new Exception();
//                }
//                else{
                CreditoWS creditoWS = new CreditoWS();
                creditoWS.getEstado(tarjeta.getNumeroCredito().toString());
                creditoDirecto.setTotalAPagar(creditoWS.getTotalPagar().setScale(2, BigDecimal.ROUND_HALF_UP));
                creditoDirecto.setFechaDeCorte(creditoWS.getCicloFacturacion().intValue());
//                }
            } catch (Exception e) {
                String mensaje = "Error al consultar datos de tarjetas y crédito: " + e.getMessage();
                log.error(mensaje);
            }
            return creditoDirecto;
        } catch (CreditoNotFoundException e) {
            throw e;
        } catch (NoResultException e) {
            throw new CreditoNotFoundException("No se encontraron los datos del cliente asociado a la tarjeta.");
        } catch (SQLException e) {
            log.error("consultarCreditoDirecto() - " + e.getMessage());
            String mensaje = "Error al consultar datos de tarjetas y crédito: " + e.getMessage();
            throw new CreditoException(mensaje, e);
        } catch (Exception e) {
            log.error("consultarCreditoDirecto() - " + e.getMessage());
            String mensaje = "Error inesperado al consultar datos de tarjetas y crédito: " + e.getMessage();
            throw new CreditoException(mensaje, e);
        } finally {
            connCredito.cerrarConexion();
            connSukasa.cerrarConexion();
        }
    }

    private static BigDecimal obtenerTotalAPagar(Connection connCentral, Integer numCredito) throws SQLException {
        log.debug("obtenerTotalAPagar() - Consultamos totales para obtener el total a pagar por el cliente con número de crédito: " + numCredito);
        BigDecimal pagosVencidos = TarjetaHabienteDao.consultarPagosVencidos(connCentral, numCredito);
        BigDecimal mora = TarjetaHabienteDao.consultarMora(connCentral, numCredito);
        BigDecimal facturado = TarjetaHabienteDao.consultarFacturado(connCentral, numCredito);
        BigDecimal notasDebito = TarjetaHabienteDao.consultarNotasDebito(connCentral, numCredito);
        BigDecimal saldoAFavor = TarjetaHabienteDao.consultarSaldoAFavor(connCentral, numCredito);
        BigDecimal suma = pagosVencidos.add(mora).add(facturado).add(notasDebito).subtract(saldoAFavor);
        return Numero.redondear(suma);
    }

    private static int obtenerFechaCorte(Connection conn, String cedula) throws SQLException {
        log.debug("obtenerFechaCorte() - Consultando ciclo para cliente con cédula: " + cedula);
        String ciclo = TarjetaHabienteDao.consultarCiclo(conn, cedula);
        if (ciclo.equals("4")) {
            return 15;
        }
        if (ciclo.equals("3")) {
            return 30;
        }
        log.warn("obtenerFechaCorte() - El ciclo del cliente no tiene datos. Devolvemos día 30 de cada mes por defecto. Cédula: " + cedula);
        return 30;
    }

    public static PlasticoBean consultarCupoTarjetaCliente(String cedulaCliente)
            throws CreditoException, CreditoNotFoundException {
        Connection connSukasa = new Connection();
        Connection connCredito = new Connection();
        PlasticoBean tarjeta = null;
        try {
            log.debug("consultarCupoTarjetaCliente() - Consultando credito para tarjeta de cliente con cédula: " + cedulaCliente);
            connCredito.abrirConexion(Database.getConnectionCredito());
            connSukasa.abrirConexion(Database.getConnectionSukasa());
            String numeroTarjeta = PlasticosDao.consultarNumeroPorCedula(connCredito, cedulaCliente);
            if (numeroTarjeta != null) {
                tarjeta = PlasticosDao.consultarPorNumTarjeta(connCredito, numeroTarjeta);
            }
            if (tarjeta == null) {
                throw new CreditoNotFoundException("No se ha encontrado en el sistema la tarjeta para cliente con cédula: " + cedulaCliente);
            }
            PlasticoEstadoBean estadoTarjeta = PlasticosDao.consultarEstado(connCredito, tarjeta.getNumeroTarjeta());
            tarjeta.setEstado(estadoTarjeta);
            consultarCupo(connSukasa, tarjeta);
            return tarjeta;
        } catch (CreditoNotFoundException e) {
            throw e;
        } catch (SQLException e) {
            log.error("consultarCupoTarjetaCliente() - " + e.getMessage());
            String mensaje = "Error al consultar datos de tarjetas y crédito: " + e.getMessage();
            throw new CreditoException(mensaje, e);
        } catch (Exception e) {
            log.error("consultarCupoTarjetaCliente() - " + e.getMessage());
            String mensaje = "Error inesperado al consultar datos de tarjetas y crédito: " + e.getMessage();
            throw new CreditoException(mensaje, e);
        } finally {
            connCredito.cerrarConexion();
            connSukasa.cerrarConexion();
        }

    }

    private static void consultarCupo(Connection connSukasa, PlasticoBean plastico) throws SQLException, CreditoNotFoundException {
        log.debug("consultarCupoTarjeta() - Consultando cupo para tarjeta: " + plastico.getNumeroTarjeta());
        CupoVirtualBean cupo = CupoVirtualDao.consultar(connSukasa, plastico.getNumeroCredito());
        if (cupo == null) {
            throw new CreditoNotFoundException("No se ha encontrado en el sistema cupo virtual para el número de crédito: " + plastico.getNumeroTarjeta());
        }
        plastico.setCupo(cupo);
    }

    public static void realizarPago(TicketS ticket, CreditoDirectoBean creditoDirecto) throws CreditoException {
        Connection connSukasa = new Connection();
        EntityManagerFactory emf = Sesion.getEmf();
        EntityManager em = emf.createEntityManager();
        try {
            log.debug("realizarPago() - Realizando pago de abono a crédito...");
            em.getTransaction().begin();

            // instanciamos la nueva letra
            AbonoCreditoBean abonoCredito = new AbonoCreditoBean();
            abonoCredito.setCodAlmacen(VariablesAlm.getVariable(VariablesAlm.COD_ALMACEN));
            abonoCredito.setCodCaja(Sesion.config.getCodcaja());
            abonoCredito.setUidCreditoPago(UUID.randomUUID().toString());
            abonoCredito.setCodCliente(creditoDirecto.getPlastico().getCliente().getCodcli());
            abonoCredito.setTotalSinDto(ticket.getTotales().getTotalAPagar());
            abonoCredito.setTotalConDto(ticket.getTotales().getTotalPagado());
            abonoCredito.setProcesado(false);
            abonoCredito.setCodVendedor(Sesion.getUsuario().getUsuario());
            abonoCredito.setNumCredito(creditoDirecto.getPlastico().getNumeroCredito());
            abonoCredito.setIdentificador(ServicioContadoresCaja.obtenerContadorAbonoCredito().intValue());
            abonoCredito.setObservaciones(ticket.getObservaciones());
            abonoCredito.setPagos(TicketXMLServices.getXMLPagos(ticket.getPagos()));
            abonoCredito.setFecha(new Fecha());
            abonoCredito.setNumeroTarjeta(creditoDirecto.getPlastico().getNumeroTarjeta());
            abonoCredito.setAnulado(false);

            ReferenciaTicket referencia = ReferenciaTicket.getReferenciaCredito(abonoCredito);
            String documento = abonoCredito.getCodAlmacen() + "-" + abonoCredito.getCodCaja() + "-" + String.format("%09d", abonoCredito.getIdentificador());
            TicketService.procesarMediosPagos(em, ticket.getPagos().getPagos(), referencia, "CRE", documento);

            java.sql.Connection conn = em.unwrap(java.sql.Connection.class);
            SqlSession session = new SqlSession();
            session.openSession(SessionFactory.openSession(conn));

            AbonoCreditoMapper mapper = session.getMapper(AbonoCreditoMapper.class);
            mapper.insert(abonoCredito);

            //incrementamos el contador del abono a Crédito
            Connection connMP = Connection.getConnection(em);
            ServicioContadoresCaja.incrementarContadorAbonoCredito(connMP);

            // incrementamos cupo
            if (VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT) != null
                    && VariablesAlm.getVariable(VariablesAlm.PARAMETRO_CREDITO_ACTIVO_CRT).equals("0")) {
                CupoVirtual cupoVirtual = CupoVirtualDao.consultarCupoVirtualByCredito(em, creditoDirecto.getPlastico().getNumeroCredito());
                if (cupoVirtual != null) {
                    cupoVirtual.setCupo(cupoVirtual.getCupo().add(ticket.getTotales().getTotalAPagar()));
                    cupoVirtual.setProcesado("N");
                    em.merge(cupoVirtual);
                    Integer numeroCredito = creditoDirecto.getPlastico().getNumeroCredito();
                    TramaCreditoDTO creditoDTO = new TramaCreditoDTO(numeroCredito, ticket.getTotales().getTotalAPagar(), Sesion.getTienda().getCodalm());
                    String tramaCredito = JsonUtil.objectToJson(creditoDTO);
                    log.info("Trama credito " + tramaCredito);
                    ProcesoEncolarThread envioDomicilioThread = new ProcesoEncolarThread(Variables.getVariable(Variables.URL_SERVIDOR_ACTIVEMQ), tramaCredito, Variables.getVariable(Variables.QUEUE_CREDITO_CUPO), Constantes.PROCESO_CREDITO_CUPO, UUID.randomUUID().toString());
                    envioDomicilioThread.start();
                }
            } else {
                connSukasa.abrirConexion(Database.getConnectionSukasa());
                CupoVirtualDao.restarCupo(connSukasa, ticket.getTotales().getTotalAPagar().negate(), creditoDirecto.getPlastico().getNumeroCredito());
            }

            em.getTransaction().commit();

            PrintServices.getInstance().limpiarListaDocumentos();
            PrintServices.getInstance().imprimirComprobantePagoCreditoDirecto(ticket, creditoDirecto, abonoCredito);
            BonosServices.crearBonosPagos(ticket.getPagos(), abonoCredito.getIdAbonoCredito(), BonosServices.LLAMADO_DESDE_CREDITO_DIRECTO, creditoDirecto.getPlastico().getCliente());

            DocumentosService.crearDocumentoCreditoAbono(ticket, PrintServices.getInstance().getDocumentosImpresos(), abonoCredito, DocumentosBean.CREDITO_ABONO);
            PrintServices.getInstance().limpiarListaDocumentos();
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            String msg = "Error registrando nuevo abono a crédito en base de datos: " + e.getMessage();
            log.error("crearLetra() - " + msg, e);
            throw new CreditoException(msg, e);
        } catch (Exception e) {
            em.getTransaction().rollback();
            String msg = "Error inesperado registrando abono a crédito: " + e.getMessage();
            log.error("realizarPago() - " + msg, e);
            throw new CreditoException(msg, e);
        } finally {
            connSukasa.cerrarConexion();
        }
    }

    public static AbonoCreditoBean consultarAbonoCredito(String codAlm, String codCaja, Long identificador) throws CreditoException {
        SqlSession sql = new SqlSession();
        try {
            sql.openSession(SessionFactory.openSession());
            AbonoCreditoMapper mapper = sql.getMapper(AbonoCreditoMapper.class);
            AbonoCreditoExample example = new AbonoCreditoExample();
            example.or().andCodAlmacenEqualTo(codAlm).andCodCajaEqualTo(codCaja).andIdentificadorEqualTo(identificador.intValue());
            List<AbonoCreditoBean> resultado = mapper.selectByExampleWithBLOBs(example);
            if (!resultado.isEmpty()) {
                return resultado.get(0);
            }
            return null;
        } catch (NoResultException e) {
            log.debug("El pago a crédito directo no existe.");
            throw new CreditoException("El pago a crédito directo no existe");
        } catch (Exception e) {
            log.error("consultarAbonoCredito() - Error inesperado consultado el pago a crédito directo.", e);
            throw new CreditoException("Error inesperado consultado el pago a crédito directo.", e);
        } finally {
            sql.close();
        }
    }

    public static void updateAbonoCredito(AbonoCreditoBean abonoCredito, SqlSession sql) throws CreditoException {
        try {
            AbonoCreditoMapper mapper = sql.getMapper(AbonoCreditoMapper.class);
            mapper.updateByPrimaryKeyWithBLOBs(abonoCredito);
        } catch (Exception e) {
            log.error("updateAbonoCredito() - Error inesperado anulando el pago a crédito directo.", e);
            throw new CreditoException("Error inesperado anulando el pago a crédito directo.", e);
        }
    }

    /**
     *
     * @param cedula
     * @param binInicio
     * @param binFin
     * @return
     * @throws CreditoException
     * @throws CreditoNotFoundException
     */
    public static PlasticoBean consultarPorBinTarjetaIdentificacion(String cedula, String binInicio, String binFin)
            throws CreditoException, CreditoNotFoundException {
        Connection connCredito = new Connection();

        try {
            log.debug("consultarPorBinTarjetaIdentificacion() - Consultando plástico para cédula: " + cedula);
            connCredito.abrirConexion(Database.getConnectionCredito());

            List<PlasticoBean> tarjetas = PlasticosDao.consultarTodasPorCedulaCliente(connCredito, cedula);
            if (tarjetas.isEmpty()) {
                throw new CreditoNotFoundException("No se ha encontrado en el sistema la tarjeta con cédula: " + cedula);
            }

            for (PlasticoBean plasticoBean : tarjetas) {
                if (plasticoBean.getNumeroTarjeta().contains(binInicio) && plasticoBean.getNumeroTarjeta().contains(binFin)) {
                    return plasticoBean;
                }
            }

            throw new CreditoNotFoundException("No se ha encontrado en el sistema la tarjeta " + binInicio + "XXX" + binFin + " para la identificación " + cedula);

        } catch (CreditoNotFoundException e) {
            throw e;
        } catch (SQLException e) {
            log.error("consultarPorBinTarjetaIdentificacion() - " + e.getMessage());
            String mensaje = "Error al consultar datos de tarjetas por cédula : " + e.getMessage();
            throw new CreditoException(mensaje, e);
        } catch (Exception e) {
            log.error("consultarPorBinTarjetaIdentificacion() - " + e.getMessage());
            String mensaje = "Error inesperado al consultar datos de tarjetas por cédula : " + e.getMessage();
            throw new CreditoException(mensaje, e);
        } finally {
            connCredito.cerrarConexion();
        }
    }
}
