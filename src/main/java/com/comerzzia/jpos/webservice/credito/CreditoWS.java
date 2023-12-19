package com.comerzzia.jpos.webservice.credito;

import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;

/**
 *
 * @author DAS
 */
public class CreditoWS {

    protected static Logger log = Logger.getMLogger(CreditoWS.class);

    private String urlWebservice;
    private BigDecimal totalPagar;
    private Long cicloFacturacion;

    /**
     * *
     *
     * @param urlWebservice
     */
    public CreditoWS() {
    }

    public CreditoWS(String urlWebservice) {
        this.urlWebservice = urlWebservice;
    }

    public String getUrlWebservice() {
        return urlWebservice;
    }

    public void setUrlWebservice(String urlWebservice) {
        this.urlWebservice = urlWebservice;
    }

    public Long getCicloFacturacion() {
        return cicloFacturacion;
    }

    public BigDecimal getTotalPagar() {
        return totalPagar;
    }

    /**
     * *
     * Antes de llamar a getTotalPagar() o getCicloFacturacion() debemos
     * ejecutar: getEstado(String cuenta)
     *
     * @param cuenta
     */
    public void getEstado(String cuenta) {
        try {
//            log.debug("getEstado() - Contectando con el webservice de crédito con la url: " + urlWebservice);
//            CreditoWSProxy ws = new CreditoWSProxy(urlWebservice);
            VwBalance[] balance;
            log.debug("getEstado() - Obteniendo el estado de la cuenta: " + cuenta);
//            balance = ws.getEstado(cuenta);
            balance = CreditoDao.getEstado(cuenta);

            if (balance.length > 0) {
                log.debug("getEstado() - Total a pagar recibido: " + balance[0].getTotalPagar());
                log.debug("getEstado() - Ciclo de facturación recibido: " + balance[0].getCicloFacturacion());

                this.totalPagar = balance[0].getTotalPagar();
                this.cicloFacturacion = balance[0].getCicloFacturacion();
            }
//        } catch (RemoteException e) {
//            log.error("getEstado() - Ha ocurrido un error al tratar de obtener el estado de la cuenta " + cuenta, e);
        } catch (Exception e) {
            log.error("getEstado() - Error inesperado al tratar de obtener el estado de la cuenta " + cuenta, e);
        } catch (Throwable e) {
            log.error("getEstado() - Error inesperado al tratar de obtener el estado de la cuenta " + cuenta, e);
        }
    }
}
