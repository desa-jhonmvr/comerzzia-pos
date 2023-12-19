/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services.pagos;

import com.comerzzia.jpos.dto.ventas.BancoOnlineDTO;
import com.comerzzia.jpos.dto.ventas.MarcaTarjetaOnlineDTO;
import com.comerzzia.jpos.dto.ventas.MedioPagoDTO;
import com.comerzzia.jpos.dto.ventas.online.PeticionPlanFinanciamientoDTO;
import com.comerzzia.jpos.dto.ventas.paginaweb.PlanPagoDTO;
import com.comerzzia.jpos.gui.validation.ValidationException;
import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.servicios.clientes.ClienteException;
import com.comerzzia.jpos.servicios.core.contadores.ContadorException;
import com.comerzzia.jpos.servicios.mediospago.MedioPagoException;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Gabriel Simbania
 */
public interface FormaPagoServices {

    /**
     * Devuelve todos los medios de pago activos para un tipo de cliente
     *
     * @author Gabriel Simbania
     * @param tipoCliente tipo de cliente
     * @return
     * @throws MedioPagoException
     */
    public List<MedioPagoDTO> obtenerFormasPago(Long tipoCliente) throws MedioPagoException;

    /**
     * El m&eacute;todo devuelve los planes de financiamento con sus respectivos
     * valors para un medio de pago determinado
     *
     * @author Gabriel Simbania
     * @param medioPago medio de pago
     * @param totalAPagar valor total a pagar
     * @param totalGarantiaExtendida valor total de la garantia extendida
     * @param identificacionCliente identificaci&oacute;n del cliente
     * @param esAfiliadoSupermaxi bandera para saber si es afiliado
     * @return
     * @throws ValidationException
     * @throws ClienteException
     * @throws ContadorException
     * @throws MedioPagoException
     */
    public List<PlanPagoDTO> obtenerPlanPago(MedioPagoBean medioPago, BigDecimal totalAPagar, BigDecimal totalGarantiaExtendida, String identificacionCliente, boolean esAfiliadoSupermaxi) throws ValidationException, ClienteException, ContadorException, MedioPagoException;

    /**
     * Servicio que devuelve los medios de pago con sus respectivos planes de
     * financiamimento y en estos se encuentran los valores correspondientes a
     * los items enviados
     *
     * @author Gabriel Simbania
     * @param peticionPlanFinanciamientoDTO
     * @return
     * @throws Exception
     */
    @Deprecated
    List<MedioPagoDTO> obtenerMediosPago(PeticionPlanFinanciamientoDTO peticionPlanFinanciamientoDTO) throws Exception;

    /**
     * Servicio que devuelve los medios de pago con sus respectivos planes de
     * financiamimento y en estos se encuentran los valores correspondientes a
     * los items enviados
     *
     * @author Gabriel Simbania
     * @param peticionPlanFinanciamientoDTO
     * @return
     * @throws Exception
     */
    List<MedioPagoDTO> obtenerMediosPagoByMarcaTarjeta(PeticionPlanFinanciamientoDTO peticionPlanFinanciamientoDTO) throws Exception;

    /**
     * Devuelve todos los bancos y sus medios de pago
     *
     * @author Gabriel Simbania
     * @return
     * @throws MedioPagoException
     */
    List<BancoOnlineDTO> obtenerBancos() throws MedioPagoException;

    /**
     *
     * @return @throws MedioPagoException
     * @throws com.comerzzia.jpos.servicios.clientes.ClienteException
     */
    List<MarcaTarjetaOnlineDTO> obtenerMarcasTarjetasMedioPago() throws MedioPagoException, ClienteException;

}
