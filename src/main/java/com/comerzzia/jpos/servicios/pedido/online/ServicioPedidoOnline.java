/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.pedido.online;

import com.comerzzia.jpos.dto.ventas.online.DireccionEnvioOnlineDTO;
import com.comerzzia.jpos.dto.ventas.online.FormaPagoOnlineDTO;
import com.comerzzia.jpos.dto.ventas.online.ItemOnlineDTO;
import com.comerzzia.jpos.dto.ventas.online.VentaOnlineDTO;
import com.comerzzia.jpos.entity.db.PedidoOnlineDetalleTbl;
import com.comerzzia.jpos.entity.db.PedidoOnlineDireccionTbl;
import com.comerzzia.jpos.entity.db.PedidoOnlinePagoTbl;
import com.comerzzia.jpos.entity.db.PedidoOnlineTbl;
import com.comerzzia.jpos.persistencia.pedido.online.Pedido.PedidoOnlineDao;
import com.comerzzia.jpos.persistencia.pedido.online.Pedido.PedidoOnlineDetalleDao;
import com.comerzzia.jpos.persistencia.pedido.online.Pedido.PedidoOnlineDireccionDao;
import com.comerzzia.jpos.persistencia.pedido.online.Pedido.PedidoOnlinePagoDao;
import com.comerzzia.jpos.servicios.facturacion.tarjetas.FacturacionTarjetaException;
import com.comerzzia.jpos.util.enums.EnumEstado;
import com.comerzzia.jpos.util.enums.EnumTipoPagoPaginaWeb;
import com.comerzzia.util.fechas.Fechas;
import es.mpsistemas.util.log.Logger;
import java.util.UUID;
import javax.persistence.EntityManager;

/**
 *
 * @author Gabriel Simbania
 */
public class ServicioPedidoOnline {

    protected static Logger LOG_POS = Logger.getMLogger(ServicioPedidoOnline.class);

    /**
     * Inserta el pedido online en la base de datos
     *
     * @author Gabriel Simbania
     * @param ventaOnlineDTO
     * @param uidTicket
     * @param em
     * @return
     * @throws FacturacionTarjetaException
     * @throws Exception
     */
    public static PedidoOnlineTbl insertarPedidoOnline(VentaOnlineDTO ventaOnlineDTO, String uidTicket, EntityManager em) throws FacturacionTarjetaException, Exception {

        String uidPedido = UUID.randomUUID().toString();
        PedidoOnlineTbl pedidoOnline = new PedidoOnlineTbl(uidPedido,
                ventaOnlineDTO.getIdPedido(), ventaOnlineDTO.getReferenciaPedido(), Fechas.actualDateTime(), ventaOnlineDTO.getCliente().getIdentificacion(),
                EnumEstado.ACTIVO, null, uidTicket);

        PedidoOnlineDao.crear(pedidoOnline, em);

        for (ItemOnlineDTO itemOnlineDTO : ventaOnlineDTO.getItems()) {
            PedidoOnlineDetalleTbl pedidoDetalle = new PedidoOnlineDetalleTbl(pedidoOnline,
                    itemOnlineDTO.getIdLinea(), itemOnlineDTO.getItmCodigoI(),
                    itemOnlineDTO.getItmCantidad(), itemOnlineDTO.getItmCobraIva(),
                    itemOnlineDTO.getItmPvpUnitario(), itemOnlineDTO.getItmPrecioFinanciamiento(),
                    itemOnlineDTO.getItmPrecioTotal(), itemOnlineDTO.getPorcentajeDescuento(), itemOnlineDTO.getIdPromocion(),
                    itemOnlineDTO.getValorDescuento());
            PedidoOnlineDetalleDao.crear(pedidoDetalle, em);
        }

        for (FormaPagoOnlineDTO formaPagoOnlineDTO : ventaOnlineDTO.getFormasPago()) {
            String uidPedidoPago = UUID.randomUUID().toString();
            PedidoOnlinePagoTbl pedidoPago = new PedidoOnlinePagoTbl(uidPedidoPago, pedidoOnline,
                    EnumTipoPagoPaginaWeb.findEnumByOrdinal(formaPagoOnlineDTO.getTipoPago()),
                    formaPagoOnlineDTO.getNumeroTarjeta(), formaPagoOnlineDTO.getPorcentajeDescuento(),
                    formaPagoOnlineDTO.getImporteInteres(), formaPagoOnlineDTO.getaPagar(),
                    formaPagoOnlineDTO.getNumCuotas(), formaPagoOnlineDTO.getnReferenciaAutorizacion(),
                    formaPagoOnlineDTO.getPorcentajeInteres(), formaPagoOnlineDTO.getImporteInteres(),
                    formaPagoOnlineDTO.getLote(), formaPagoOnlineDTO.getReferencia());

            PedidoOnlinePagoDao.crear(pedidoPago, em);
        }

        DireccionEnvioOnlineDTO direccionEnvioOnlineDTO = ventaOnlineDTO.getDireccionEnvio();
        PedidoOnlineDireccionTbl pedidoDireccion = new PedidoOnlineDireccionTbl(pedidoOnline,
                direccionEnvioOnlineDTO.getProvincia(), direccionEnvioOnlineDTO.getLocalidad(),
                direccionEnvioOnlineDTO.getCallePrincipal(), direccionEnvioOnlineDTO.getNumeracion(),
                direccionEnvioOnlineDTO.getCalleSecundaria(), direccionEnvioOnlineDTO.getReferencia(),
                direccionEnvioOnlineDTO.getNombreContacto(), direccionEnvioOnlineDTO.getIdentificacionContacto(),
                direccionEnvioOnlineDTO.getTelefonoContacto(),direccionEnvioOnlineDTO.getUbigeo());

        PedidoOnlineDireccionDao.crear(pedidoDireccion, em);

        return pedidoOnline;
    }

}
