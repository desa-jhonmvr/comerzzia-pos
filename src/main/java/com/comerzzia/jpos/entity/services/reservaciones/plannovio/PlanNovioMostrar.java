/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.services.reservaciones.plannovio;

import com.comerzzia.jpos.entity.db.AbonoPlanNovio;
import com.comerzzia.jpos.entity.db.ArticuloPlanNovio;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.InvitadoPlanNovio;
import com.comerzzia.jpos.entity.db.PlanNovio;
import com.comerzzia.jpos.gui.components.form.JTextFieldForm;
import com.comerzzia.jpos.gui.reservaciones.plannovios.JMostrarPlanNovio;
import com.comerzzia.jpos.gui.validation.IViewerValidationFormError;
import com.comerzzia.jpos.gui.validation.ValidationFormException;
import com.comerzzia.jpos.persistencia.logs.logskdx.LogKardexBean;
import com.comerzzia.jpos.printer.TicketPrinterException;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.logs.logsacceso.ServicioLogAcceso;
import com.comerzzia.jpos.servicios.print.documentos.DocumentoException;
import com.comerzzia.jpos.servicios.reservaciones.plannovio.PlanNovioException;
import com.comerzzia.jpos.servicios.reservaciones.plannovio.PlanNovioServices;
import com.comerzzia.jpos.servicios.stock.ServicioStock;
import com.comerzzia.jpos.servicios.stock.StockException;
import com.comerzzia.jpos.servicios.tickets.componentes.DatosDeEnvio;
import es.mpsistemas.util.log.Logger;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author MGRI
 */
public class PlanNovioMostrar {

    //Log 
    private static Logger log = Logger.getMLogger(PlanNovioMostrar.class);
    protected static SimpleDateFormat formateadorFechaCorta = new SimpleDateFormat("dd-MMM-yyyy");
    private static SimpleDateFormat formateadorFechaLarga = new SimpleDateFormat("dd/MM/yyyy - HH:mm", new Locale("es", "ES"));
    private static int[] anchosColumnasTablaResultadoPlanes = {105, 280, 90, 175, 85};
    private static int[] anchosColumnasTablaAbonosResultadoPlanes = {110, 160, 170, 90, 140, 100};
    private static JTable tablaArticulos;
    // Pantalla de liquidación
    private BigDecimal seleccionados;
    private BigDecimal restante;
    //Pantalla de gestión de invitados
    private InvitadoPlanNovio invitadoGestionado;
    private JMostrarPlanNovio pantallaMostrarPlan;
    private BigDecimal descuentoCompraAbonos;
    private Boolean pertenecePlanTienda;
    private String autorizadorRemate;
    
    
    private PlanNovioMostrar(){
        
    }
    
    public static PlanNovioOBJ getPlanNovio() {
        return planNovio;
    }

    public static void setPlanNovio(PlanNovioOBJ aPlanNovio) {
        planNovio = aPlanNovio;
    }

    public static PlanNovioMostrar inicia() {
        log.debug("inicia() - Iniciando el proceso de gestión de Plan Novio");
        PlanNovioMostrar res = PlanNovioMostrar.getIstance();

        // Editamos los datos del plan novio referentes a los datos adicionales 
        // Consultamos el plan Novios
        setPlanNovio(PlanNovioBuscar.getPlanNovio());
        propietarioAutenticado = false; // propiedad para no pedir autenticación mas de una vez

        try {
            planNovio.consultarAbonoSinAnular();
            planNovio.consultarDetalle();

        }
        catch (PlanNovioException e) {
        }
        res.setPertenecePlanTienda(res.getPlanNovio().getPlan().getPlanNovioPK().getCodalm().equals(Sesion.getTienda().getAlmacen().getCodalm()));
        return res;
    }

    public static PlanNovioMostrar getIstance() {
        if (manejador == null) {
            manejador = new PlanNovioMostrar();
        }
        return manejador;
    }

    public static void estableceDatosNovios(JLabel l_nombre_novia, JLabel l_apellidos_novia, JLabel l_cedula_novia, JLabel l_nombre_novio, JLabel l_apellidos_novio, JLabel l_cedula_novio, JLabel lb_boda) {
        log.debug(" estableceDatosNovios() ");
        Cliente novia = planNovio.getPlan().getNovia();
        Cliente novio = planNovio.getPlan().getNovio();

        l_nombre_novia.setText(novia.getNombre());
        l_apellidos_novia.setText(novia.getApellido());
        l_cedula_novia.setText(novia.getCodcli());
        l_nombre_novio.setText(novio.getNombre());
        l_apellidos_novio.setText(novio.getApellido());
        l_cedula_novio.setText(novio.getCodcli());

        lb_boda.setText(planNovio.getPlan().getTitulo());
    }

    public static void estableceDatosAdicionales(JTextFieldForm t_lugar_boda, JTextFieldForm t_fecha_hora_boda, JTextFieldForm t_fecha_contacto_invitados, JTextFieldForm t_telefono_contacto, JTextFieldForm t_emails) {       
        if (planNovio.getPlan().getFechaContacto()!=null){
            t_fecha_contacto_invitados.setText(formateadorFechaCorta.format(planNovio.getPlan().getFechaContacto()));
        }
        else{
            t_fecha_contacto_invitados.setText("");
        }
        t_fecha_hora_boda.setText(formateadorFechaLarga.format(planNovio.getPlan().getFechaHoraBoda()));
        t_lugar_boda.setText(planNovio.getPlan().getLugar());
        t_emails.setText(planNovio.getPlan().getEmail());
        t_telefono_contacto.setText(planNovio.getPlan().getTelefono());
   }

    public static void estableceDatosCabecera(JLabel lb_fecha_alta, JLabel lb_fecha_fin, JLabel lb_estado, JLabel lb_num_plan) {

        lb_num_plan.setText(planNovio.getPlan().getPlanPantalla());
        lb_fecha_alta.setText(formateadorFechaCorta.format(planNovio.getPlan().getFechaAlta()));
        lb_fecha_fin.setText(formateadorFechaCorta.format(planNovio.getPlan().getCaducidad()));
        lb_estado.setText(planNovio.getPlan().getEstado());
    }

    /**
     * 
     * @param t_total   Total reservado en el plan
     * @param t_comprado    Total comprado como articulos
     * @param t_total_abonado   Total abonado a la reserva
     * @param lb_abono_realizado    Total de abonos reales (precio final tras descuentos y promociones)
     * @param t_n_abonos    Número de abonos
     * @param t_restante_abonos     Abonos disponibles para comprar los artículos
     * @param t_por_abonar      Total por abonar para alcanzar el mínimo de compra para el plan (difiere del campo de reservaciones)
     */
    public static void estableceTotales(JTextField t_total, JTextField t_comprado, JTextField t_total_abonado, JLabel lb_abono_realizado, JTextField t_n_abonos, JTextField t_restante_abonos, JTextField t_por_abonar) {

        t_total.setText(planNovio.getPlan().getReservado().toString());
        t_comprado.setText(planNovio.getPlan().getComprado().toString());
        t_total_abonado.setText(planNovio.getPlan().getAbonadoConDto().toString());
        lb_abono_realizado.setText("($" + planNovio.getPlan().getAbonadoSinDto().toString() + " sin descuentos)");
        t_n_abonos.setText("" + planNovio.getPlan().getAbonoPlanNovioList().size());
        t_restante_abonos.setText(planNovio.getPlan().getAbonadoSinUtilizar().toString());
        t_por_abonar.setText(planNovio.getTotalPorAbonar().toString());
        t_total.setText(planNovio.getPlan().getReservado().toString());

    }

    public static void setTablaArticulos(JTable tb_articulos) {
        tablaArticulos = tb_articulos;
    }

    public BigDecimal getDescuentoCompraAbonos() {
        return descuentoCompraAbonos;
    }

    public void setDescuentoCompraAbonos(BigDecimal descuentoCompraAbonos) {
        this.descuentoCompraAbonos = descuentoCompraAbonos;
    }

    public static int[] getAnchosColumnasTablaResultadoPlanes() {
        return anchosColumnasTablaResultadoPlanes;
    }
    private static boolean propietarioAutenticado;

    public int[] getAnchosColumnasTablaAbonosResultadoPlanes() {
        return anchosColumnasTablaAbonosResultadoPlanes;
    }

    public static void setAnchosColumnasTablaResultadoPlanes(int[] aAnchosColumnasTablaResultadoPlanes) {
        anchosColumnasTablaResultadoPlanes = aAnchosColumnasTablaResultadoPlanes;
    }
    // Manejador de errores de la pantalla
    private IViewerValidationFormError manejadorErrores;
    // Pantalla a la que asociamos esta clase
    private static PlanNovioMostrar manejador;
    // Objeto Plan Novios
    private static PlanNovioOBJ planNovio;

    public IViewerValidationFormError getManejadorErrores() {
        return manejadorErrores;
    }

    public void setManejadorErrores(IViewerValidationFormError manejadorErrores) {
        this.manejadorErrores = manejadorErrores;
    }

    public void establecerDatosAdicionales(JTextFieldForm t_direccion, JTextFieldForm t_telefono_contacto, JTextFieldForm t_emails, JTextFieldForm t_lugar_boda, JTextFieldForm t_fecha_hora_boda, JTextFieldForm t_numero_inviados, JTextFieldForm t_fecha_contacto_invitados) throws PlanNovioException {
        try {
            planNovio.getPlan().setDireccion(t_direccion.getText());
            planNovio.getPlan().setTelefono(t_telefono_contacto.getText());
            planNovio.getPlan().setEmail(t_emails.getText());
            planNovio.getPlan().setLugar(t_lugar_boda.getText());
            planNovio.getPlan().setNumInvitados(Integer.valueOf(t_numero_inviados.getText()));
            planNovio.getPlan().setFechaContacto(formateadorFechaCorta.parse(t_fecha_contacto_invitados.getText()));
            planNovio.getPlan().setFechaHoraBoda(formateadorFechaLarga.parse(t_fecha_hora_boda.getText()));
        }
        catch (Exception e) {
            log.debug("establecerDatosAdicionales() Error estableciendo Datos Adicionales", e);
            throw new PlanNovioException("Error en el ingreso de los datos Adicionales");
        }
    }

    public void crearPlan() throws PlanNovioException {
        planNovio.crear();
    }

    public List<ArticuloPlanNovio> getListaArticulos() {
        if (planNovio.getPlan().getListaArticulos() == null) {
            return ((List<ArticuloPlanNovio>) new LinkedList<ArticuloPlanNovio>());
        }
        return planNovio.getPlan().getListaArticulos();
    }

    public int getUltimaLinea() {
        return this.getListaArticulos().size() - 1;
    }

    public void removeArticulosReserva(int lineaSeleccionada) {
        ArticuloPlanNovio articuloReservado = this.getListaArticulos().get(lineaSeleccionada);
        if (!articuloReservado.isComprado() && !articuloReservado.isPendienteEnvio() && !articuloReservado.isPendienteRecoger()) {
            try {
                // LLamamos al servicio para que borr el artículo
                planNovio.removeArticulo(articuloReservado);
                LogKardexBean logKardex = new LogKardexBean();
                logKardex.setTipoAccion(LogKardexBean.tipoAccionPlanNovio);
                logKardex.setUsuarioAutorizacion(Sesion.getUsuario().getUsuario());    
                logKardex.setFactura(String.valueOf(planNovio.getPlan().getPlanNovioPK().getIdPlan()));
                log.debug("removeArticulosReserva() - Disminuyendo stock de Artículo con ID:" + articuloReservado.getIdItem() + " y marca." + articuloReservado.getCodMarca());
                ServicioStock.disminuyeStockReserva(articuloReservado.getCodMarca(), articuloReservado.getIdItem(), 1, logKardex);
                ServicioStock.actualizaKardex(articuloReservado.getCodArt(), ServicioStock.MOVIMIENTO_52, Sesion.getTienda().getCodalm(), 1L);
            }
            catch (PlanNovioException ex) {
                log.error("removeArticulosReserva() - Error eliminando articulo de la reserva", ex);
                manejadorErrores.addError(new ValidationFormException("Error eliminando articulo de la reserva"));
            }
            catch (StockException ex) {
                log.error("removeArticulosReserva() - STOCK: No fué posible disminuir el stock reservado para el artículo");
            }
        }
        else {
            manejadorErrores.addError(new ValidationFormException("El artículo no puede eliminarse por haber sido ya comprado"));
        }
    }

    public InvitadoPlanNovio getInvitadoSeleccionado() {
        return planNovio.getInvitado();
    }

    public void setInvitadoSeleccionado(int selectedRow) {
        planNovio.setInvitadoSeleccionado(selectedRow);
    }

    public List<InvitadoPlanNovio> getListaInvitados() {
        return planNovio.getListaInvitados();
    }

    public void setInvitadoSeleccionado(InvitadoPlanNovio invitado) {
        planNovio.setInvitado(invitado);
    }

    public void addInvitadoToListaInvitados(InvitadoPlanNovio invitado) {
        PlanNovioMostrar.planNovio.getListaInvitados().add(invitado);
    }

    public BigDecimal getAbonoMinimo() {
        return planNovio.getAbonoMinimo();
    }

    public boolean isModoEditarDatosGenerales() {
        return planNovio.isModoEditarDatosGenerales();
    }

    public List<AbonoPlanNovio> getListaAbonos() {
        if (planNovio.getListaAbonos() == null) {
            return new LinkedList<AbonoPlanNovio>();
        }        
        return planNovio.getListaAbonos();
    }

    public void accionRematarReservacion(BigDecimal valorRematar, Boolean esLocalOrigen) {
        try {
            planNovio.accionRematarReservacion(autorizadorRemate, valorRematar, esLocalOrigen);

            try {
                LogKardexBean logKardex = new LogKardexBean();
                logKardex.setTipoAccion(LogKardexBean.tipoAccionPlanNovio);
                logKardex.setUsuarioAutorizacion(Sesion.getUsuario().getUsuario());    
                logKardex.setFactura(String.valueOf(planNovio.getPlan().getPlanNovioPK().getIdPlan()));
                for (ArticuloPlanNovio artpn : manejador.planNovio.getPlan().getArticuloPlanNovioList()) {
                    if (!artpn.isComprado() && !artpn.isPendienteEnvio() && !artpn.isPendienteRecoger()) {
                        log.debug(" -- Disminuyendo Stock de Reserva ");
                        ServicioStock.disminuyeStockReserva(artpn.getCodMarca(), artpn.getIdItem(), 1, logKardex);
                        ServicioStock.actualizaKardex(artpn.getCodArt(), ServicioStock.MOVIMIENTO_52, Sesion.getTienda().getCodalm(), 1L);
                    }
                }
            }
            catch (Exception e) {
                log.error ("accionRematarReservacion()- [Stock] Error modificando Stock de artículo");
            }
        }
        catch (TicketException ex) {
            log.error("Error imprimiendo ticket de listado de artículos: " + ex.getMessage(), ex);
            manejadorErrores.addError(new ValidationFormException("Error imprimiendo ticket de listado de artículos"));
        }
        catch (TicketPrinterException ex) {
            log.error("Error imprimiendo ticket de reservación: " + ex.getMessage(), ex);
            manejadorErrores.addError(new ValidationFormException("Error imprimiendo comprobante de remate"));
        }
        catch (PlanNovioException ex) {
            log.error("Error rematando reservación: " + ex.getMessage(), ex);
            manejadorErrores.addError(new ValidationFormException("Error rematando reservación"));
        }
        catch (DocumentoException ex) {
            log.error("Error rematando reservación: " + ex.getMessage(), ex);
            manejadorErrores.addError(new ValidationFormException("Error rematando reservación"));           
        }
    }

    public void accionComprarConAbonos(List<ArticuloPlanNovio> lista, DatosDeEnvio datosEnvio) {
        BigDecimal efectivoEnCajaAnt = Sesion.getCajaActual().getEfectivoEnCaja();
        try {
            planNovio.accionComprarConAbonos(lista, datosEnvio);
        }
        catch (TicketException ex) {
            log.error("Error imprimiendo ticket de listado de artículos: " + ex.getMessage(), ex);
            manejadorErrores.addError(new ValidationFormException("Error imprimiendo ticket de listado de artículos"));
        }
        catch (TicketPrinterException ex) {
            log.error("Error imprimiendo ticket de reservación: " + ex.getMessage(), ex);
            manejadorErrores.addError(new ValidationFormException("Error imprimiendo comprobante de remate"));
        }
        catch (PlanNovioException ex) {
            log.error("Error comprando con abonos: " + ex.getMessage(), ex);
            manejadorErrores.addError(new ValidationFormException("Error comprando con abonos"));
            Sesion.getCajaActual().setEfectivoEnCaja(efectivoEnCajaAnt);
        }
        catch (Exception ex) {
            log.error("Error comprando con abonos: " + ex.getMessage(), ex);
            manejadorErrores.addError(new ValidationFormException("Error comprando con abonos"));
            Sesion.getCajaActual().setEfectivoEnCaja(efectivoEnCajaAnt);
        }
        finally {
            try {
                manejador.estableceArticulosSeleccionadosComoNoComprados();
            }
            catch (Exception e) {
                log.error("accionComprarConAbonos(): Error reseteando Articulos como no comprados tras excepcion -" + e.getMessage(), e);
            }
        }
    }

    public void refrescaArticulosNoComprados() {
        planNovio.refrescaArticulosNoComprados();
    }

    public void crearLogAccesoRematarPlan(String compruebaAutorizacion) {
        autorizadorRemate = compruebaAutorizacion;
        ServicioLogAcceso.crearAccesoLogLiquidarPlan(compruebaAutorizacion, planNovio.getCodPlanAsString());
    }

    public void accionSeleccionaArticulo(int selectedRow) throws PlanNovioException {
        ArticuloPlanNovio articulo = planNovio.getArticulosNoComprados().get(selectedRow);
        if (articulo.getComprado() == 'N') {
            articulo.establecerDescuento(descuentoCompraAbonos);
            if (getRestante().compareTo(articulo.getPrecioTotal()) >= 0) {
                planNovio.getArticulosSeleccionados().add(articulo);
                articulo.setComprado('S');
                articulo.setEnvio('N');
                articulo.setRecogida('N');
                setSeleccionados(getSeleccionados().add(articulo.getPrecioTotal()));
                setRestante(planNovio.getPlan().getAbonadoSinUtilizar().subtract(getSeleccionados()));

                log.debug("Artículo marcado como comprado.");

            }
            else {
                articulo.establecerDescuento(null);
                log.debug("No tiene suficientes abonos disponibles para pagar ese artículo.");
                throw new PlanNovioException("No tiene suficientes abonos disponibles para pagar ese artículo.");
            }
        }
        else if (articulo.isComprado() && !articulo.isPendienteEnvio() && !articulo.isPendienteRecoger()) {
            articulo.setEnvio('P');
            articulo.setRecogida('N');
            log.debug("Articulo marcado como envío a domicilio.");
        }
        else if (articulo.isPendienteEnvio()) {
            articulo.setEnvio('N');
            articulo.setRecogida('P');
            log.debug("Articulo marcado como pendiente de recoger");
        }
        else {
            planNovio.getArticulosSeleccionados().remove(articulo);
            setRestante(getRestante().add(articulo.getPrecioTotal()));
            setSeleccionados(getSeleccionados().subtract(articulo.getPrecioTotal()));
            articulo.establecerDescuento(null);
            articulo.setEnvio('N');
            articulo.setRecogida('N');
            articulo.setComprado('N');
            log.debug("Artículo marcado como no comprado.");
        }
    }

    public BigDecimal getSeleccionados() {
        return seleccionados;
    }

    public void setSeleccionados(BigDecimal seleccionados) {
        this.seleccionados = seleccionados;
    }

    public BigDecimal getRestante() {
        return restante;
    }

    public void setRestante(BigDecimal restante) {
        this.restante = restante;
    }

    public void inicializaLiquidacion() {
        planNovio.refrescaArticulosNoComprados();
        restante = planNovio.getPlan().getAbonadoSinUtilizar();
        seleccionados = BigDecimal.ZERO;
        planNovio.setArticulosSeleccionados(new LinkedList<ArticuloPlanNovio>());
    }

    public void estableceArticulosSeleccionadosComoNoComprados() {
        for (ArticuloPlanNovio art : planNovio.getArticulosSeleccionados()) {
            art.setComprado('N');
            art.establecerDescuento(null);
        }
    }

    public InvitadoPlanNovio getInvitadoGestionado() {
        return invitadoGestionado;
    }

    public void setInvitadoGestionado(InvitadoPlanNovio invitadoGestionado) {
        this.invitadoGestionado = invitadoGestionado;
    }

    public boolean autenticaPropietario(Cliente cliente) {
        boolean res = cliente.equals(planNovio.getPlan().getNovia()) || cliente.equals(planNovio.getPlan().getNovio());
        if (res) {
            PlanNovioMostrar.propietarioAutenticado = true;
            planNovio.setClienteLogueado(cliente);
        }
        return res;
    }

    // MÉTODOS PARA GESTIÓN DE INVITADOS
    public void crearInvitadoGestionado(String nombre, String apellido, String telefono, String titulo) throws PlanNovioException {
        PlanNovio plan = planNovio.getPlan();
        BigInteger idInvitado = new BigInteger(PlanNovioServices.consultaSiguienteIdInvitado(plan).toString());
        InvitadoPlanNovio invitado = new InvitadoPlanNovio(plan.getPlanNovioPK().getIdPlan(), idInvitado, plan.getPlanNovioPK().getCodalm(), Sesion.getTienda().getCodalm());
        invitado.setProcesado('N');
        invitado.setNombre(nombre);
        invitado.setApellido(apellido);
        invitado.setTelefono(telefono);
        invitado.setTitulo(titulo);
        invitadoGestionado = invitado;
    }

    public void setInvitadoGestionado(int selectedRow) {
        invitadoGestionado = planNovio.getInvitado(selectedRow);
    }

    public void crearInvitado() throws PlanNovioException {
        PlanNovioServices.crearInvitado(invitadoGestionado, planNovio.getPlan());
        PlanNovioServices.refrescaInvitadosPlan(planNovio.getPlan());
        //addInvitadoToListaInvitados(invitadoGestionado);
    }

    public void setInvitadoGestionadoAsSeleccionado() {
        planNovio.setInvitado(invitadoGestionado);
    }

    public void salvarInvitado(String nombre, String apellidos, String telefono, String titulo) throws PlanNovioException {
        invitadoGestionado.setNombre(nombre);
        invitadoGestionado.setApellido(apellidos);
        invitadoGestionado.setTelefono(telefono);
        invitadoGestionado.setTitulo(titulo);

        PlanNovioServices.modificarInvitado(invitadoGestionado, planNovio.getPlan());
        PlanNovioServices.refrescaInvitadosPlan(planNovio.getPlan());
    }

    public void accionImportarInvitados(File archivoInvitados) throws PlanNovioException {
        PlanNovioServices.importarInvitados(planNovio.getPlan(), archivoInvitados);
    }

    public boolean isModoLiquidado() {
        return planNovio.getPlan().isLiquidado();
    }

    public void accionEliminaInvitado(int lineaSelecionada) throws PlanNovioException {
        PlanNovioServices.accionEliminaInvitado(planNovio.getPlan(), lineaSelecionada);
        PlanNovioServices.refrescaInvitadosPlan(planNovio.getPlan());
    }

    public boolean isPropietarioAutenticado() {
        return propietarioAutenticado;

    }

    public JMostrarPlanNovio getPantallaMostrarPlan() {
        return pantallaMostrarPlan;
    }

    public void setPantallaMostrarPlan(JMostrarPlanNovio pantallaMostrarPlan) {
        this.pantallaMostrarPlan = pantallaMostrarPlan;
    }

    public void accionPantallaPlanComprarConAbonos() {
        this.pantallaMostrarPlan.accionComprarConAbonos();
    }

    public boolean isMinimoPlanAlcanzado() {
        return (this.planNovio.isMinimoAlcanzado());
    }

    public boolean isModoCaducado() {
        return (planNovio.getPlan().isCaducado());

    }

    public void accionAmpliarReserva(int diasAmpliacion) throws PlanNovioException {
        planNovio.accionAmpliarReserva(diasAmpliacion);
    }

    public void refrescaInvitados() throws PlanNovioException {
        PlanNovioServices.refrescaInvitadosPlan(planNovio.getPlan());
    }

    public String getAutorizadorRemate() {
        return autorizadorRemate;
    }

    public void setAutorizadorRemate(String autorizadorRemate) {
        this.autorizadorRemate = autorizadorRemate;
    }

    public void accionSeleccionarComoPropietario() {
         PlanNovioMostrar.propietarioAutenticado = true;
         planNovio.setClienteLogueado(planNovio.getPlan().getNovia());
    }

    public Boolean getPertenecePlanTienda() {
        return pertenecePlanTienda;
    }

    public void setPertenecePlanTienda(Boolean pertenecePlanTienda) {
        this.pertenecePlanTienda = pertenecePlanTienda;
    }            
}
