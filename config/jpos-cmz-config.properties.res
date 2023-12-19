comerzzia.jpos.datosCorporativos.caja=152
# Base de datos principal
comerzzia.jpos.database.esquema.config=SUKASA_TEMPORAL
comerzzia.jpos.database.esquema.empresa=SUKASA_TEMPORAL
comerzzia.jpos.database.url=jdbc:oracle:thin:@192.168.2.189:1521:xe
comerzzia.jpos.database.driver=oracle.jdbc.driver.OracleDriver
comerzzia.jpos.database.usuario=SUKASA_TEMPORAL
comerzzia.jpos.database.password=SUKASA_TEMPORAL
comerzzia.jpos.database.logger=DefaultLogger
comerzzia.jpos.database.logger.level=FINE

# Conexion a base de datos de respaldo
comerzzia.jpos.database.reserva.esquema.config=BODEGA_SUKASA
comerzzia.jpos.database.reserva.esquema.empresa=BODEGA_SUKASA
comerzzia.jpos.database.reserva.url=jdbc:oracle:thin:@192.168.47.187:1521:xe
comerzzia.jpos.database.reserva.driver=oracle.jdbc.driver.OracleDriver
comerzzia.jpos.database.reserva.usuario=BODEGA_SUKASA
comerzzia.jpos.database.reserva.password=BODEGA

# Configuracion de impresion del ticket
comerzzia.jpos.configuracion.tipo.impresion=tlpi54

comerzzia.jpos.configuracion.dispositivo.impresora.tickets=driver:tlpi54


# Configuracion de impresion adicional

comerzzia.jpos.configuracion.dispositivo.impresora.adicional=driver:tlpi54

# Otros parametros
comerzzia.jpos.configuracion.limiteRetiro=999999999
comerzzia.jpos.configuracion.tiempoBloqueo=3000
comerzzia.jpos.configuracion.validacion.tarjetas.ruta.lectura=C:\\SUKASA\\Validacion\\ValEntrada.txt
comerzzia.jpos.configuracion.validacion.tarjetas.ruta.escritura=C:\\SUKASA\\Validacion\\ValSalida.txt
comerzzia.jpos.configuracion.plannovios.invitados.excel=/opt/comerzzia/jpos/invitados.xls

# XML con plantilla para impresion del ticket
comerzzia.jpos.configuracion.esquema.factura=/ticket_factura.xml
comerzzia.jpos.configuracion.esquema.cotizacion=/ticket_cotizacion.xml
comerzzia.jpos.configuracion.esquema.recibo.devolucion=/ticket_devolucion.xml
comerzzia.jpos.configuracion.esquema.recibo.reservacion=/ticket_reservacion.xml
comerzzia.jpos.configuracion.esquema.recibo.comprobanteReservacion=/comprobanteAbono.xml
comerzzia.jpos.configuracion.esquema.listado.articulos.reserva=/listadoArticulosReserva.xml
comerzzia.jpos.configuracion.esquema.recibo.comprobante.giftcard=/comprobanteGiftCard.xml
comerzzia.jpos.configuracion.esquema.cupon=/ticket_cupon.xml
comerzzia.jpos.configuracion.esquema.recibo.comprobanteRetiro=/ticket_retiro.xml
comerzzia.jpos.configuracion.esquema.recibo.comprobanteBono=/ticket_bono.xml
comerzzia.jpos.configuracion.esquema.recibo.comprobante.voucher=/esquemaVoucher.xml
comerzzia.jpos.configuracion.esquema.recibo.comprobante.voucherAnulacion=/esquemaVoucherAnulacion.xml
comerzzia.jpos.configuracion.esquema.recibo.comprobante.voucher.pinpad= /esquemaVoucherPinPad.xml
comerzzia.jpos.configuracion.esquema.comprobante.pago.creditodirecto=/comprobantePagoCreditoDirecto.xml
comerzzia.jpos.configuracion.esquema.comprobante.pago.letra=/comprobantePagoLetra.xml
comerzzia.jpos.configuracion.esquema.listado.articulos.plan=/listadoArticulosPlan.xml
comerzzia.jpos.configuracion.esquema.contrato.plan=/contratoPlan.xml
comerzzia.jpos.configuracion.esquema.extension.garantia=/extensionGarantia.xml
comerzzia.jpos.configuracion.esquema.listado.cancelaciones.pendientes=/listadoCancelacionesPendientes.xml
comerzzia.jpos.configuracion.esquema.comprobante.anulacion=/comprobanteAnulacion.xml
comerzzia.jpos.configuracion.esquema.cheque.anverso=/cheque_anverso.xml
comerzzia.jpos.configuracion.esquema.cheque.reverso=/cheque_reverso.xml

# Configuracion de modo de desarrollo
comerzzia.jpos.configuracion.modo.desarrollo=NO
comerzzia.jpos.configuracion.modo.desarrollo.usuario=Administrador
comerzzia.jpos.configuracion.modo.desarrollo.password=cmz
# incorporar esta nueva clave al fichero properties. 
# posibles valores: AUTOMATICA (pinpad activo) MANUAL (pinpad no activo)
comerzzia.jpos.configuracion.modo.autorizacion=AUTOMATICA 


