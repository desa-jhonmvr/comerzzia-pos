package com.comerzzia.jpos.servicios.promociones.cupones;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.entity.db.Cupon;
import com.comerzzia.jpos.persistencia.promociones.combos.ComboArticuloCantidadBean;
import com.comerzzia.jpos.servicios.promociones.articulos.SeleccionArticuloBean;
import com.comerzzia.jpos.servicios.tickets.componentes.LineaTicket;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfigEmisionCupones {

    //tipos de impresion
    public static String TIPO_MANUAL = "MANUAL";
    public static String TIPO_AUTOMATICA_BASICA = "AUTOMATICA_BASICA";
    public static String TIPO_AUTOMATICA_BASICA_VOTOS = "AUTOMATICA_BASICA_VOTOS";
    public static String TIPO_AUTOMATICA_BASICA_SORTEO = "AUTOMATICA_BASICA_SORTEO";
    public static String TIPO_AUTOMATICA_HISTORICO = "AUTOMATICA_HISTORICO";
    public static String TIPO_AUTOMATICA_CUMPLEAÑOS = "AUTOMATICA_CUMPLEAÑOS";
    public static String TIPO_AUTOMATICA_ARTICULOS = "AUTOMATICA_ARTICULOS";
    public static String TIPO_AUTOMATICA_COMBO_SECCION = "AUTOMATICA_COMBO_SECCION";
    public static String TIPO_AUTOMATICA_BASICA_REGALO_PROVEEDOR_EXTERNO = "AUTOMATICA_BASICA_REGALO_PROVEEDOR_EXTERNO";
    public static String TIPO_AUTOMATICA_BASICA_REGALO_CURSO = "AUTOMATICA_BASICA_REGALO_CURSO";
    public static String TIPO_AUTOMATICA_BASICA_SORTEO_SUKASA = "SORTEO_SUKASA";
    public static String TIPO_AUTOMATICA_BASICA_BILLETON = "AUTOMATICA_BASICA_BILLETON";
    public static String TIPO_AUTOMATICA_BASICA_AZAR = "AUTOMATICA_BASICA_AZAR";

    //tipos de cupon cumpleaños
    public static String TIPO_CUMPLEAÑOS_SOCIA = "SOCIA";
    public static String TIPO_CUMPLEAÑOS_BEBE = "BEBE";
    // general
    private String tipoImpresion;
    private PromocionTipoCupon promocion;
    //MANUAL
    private Integer numero;//numero de cupones que se imprimen para cada cliente
    private Integer cadencia;//Intervalo de tiempo necesario para la impresión entre un conjunto de cupones y otro para el mismo cliente
    private String condicion;//Descripción de la condición que debe cumplir el cliente para que se le pueda otorgar el cupón
    //AUTOMATICA BASICA
    private BigDecimal montoMinimo;//Monto mínimo de compra (si es cero no habrá monto mínimo)
    private Integer cantidadMinima;//Cantidad mínima de artículos (si es cero no habrá monto mínimo)
    private String codMarca;//Marca (si es vacío se aplicará a todos los artículos)
    private String desMarca;//no se incluira en el xml, pero para mostrar la ayuda es necesario
    //Sección, subsección o categoría (si es vacío se aplicará a todos los artículos)
    private String codSeccion;
    private String codSubseccion;
    private String codCategoria;
    private SeleccionArticuloBean listSeleccion;
    //AUTOMATICA HISTORICO
    private Integer numComprasMinimo;//Número de compras históricas mínima para emitir el cupón. (Si es cero, no habrá mínimo)
    private BigDecimal montoComprasMinimo;//Monto de compras histórico mínimo para emitir cupón. (Si es cero, no habrá mínimo)
    //AUTOMATICA CUMPLEAÑOS
    private Integer diasIntervalo; //Tamaño del intervalo en días en torno al cumpleaños.
    private String tipo;//Cumpleaños de (dos opciones fijas: SOCIA o BEBÉ) 
    //AUTOMATICA ARTICULOS
    List<String> listaCodigosArticulos;//lista de artículos
    //AUTOMATICA COMBO SECCION
    List<ComboArticuloCantidadBean> listaCombos;//lista de combos cantidad / sección
    // SORTEO
    private BigDecimal montoMaximo;
    private BigDecimal montoFraccion;
    // VOTOS
    private Integer numVotos;
    // REGALO PROVEEDOR EXTERNO
    private Long cuponProveedorExterno;
    private Integer numCupones;
    private Integer cantidad;
    private Fecha fechaValidez;

    // REGALO CURSO
    private Boolean conAcompanante;

    public ConfigEmisionCupones(PromocionTipoCupon promocion) {
        this.promocion = promocion;
    }

    /**
     * Comprueba si se deben emitir cupones en función de esta configuración
     * emisión y del ticket Devolverá NULL si no se pueden emitir. En caso
     * contrario devolverá una lista con los cupones que se deben emitir.
     *
     * @param ticket
     * @return
     */
    public List<Cupon> emitirCupones(TicketS ticket, ConfigEmisionCupones configEmision) {
        if (isTipoAutomaticaArticulos()) {
            // tenemos que comprobar que existe al menos un artículo de cada uno de los indicados
            for (String codArticulo : listaCodigosArticulos) {
                int numArticulos = ticket.getLineas().getContains(false, codArticulo, null);
                if (numArticulos == 0) {
                    return null;
                }
            }
        } else if (isTipoAutomaticaComboSeccion()) {
            // tenemos que comprobar que existen tantos articulos de cada sección como se indica en el combo
            for (ComboArticuloCantidadBean combo : listaCombos) {
                int numArticulos = ticket.getLineas().getContainsSecciones(false, combo.getCodigo(), null);
                if (numArticulos < combo.getCantidad()) {
                    return null;
                }
            }
        } else if (isTipoAutomaticaCumpleaños()) {
            // tenemos que comprobar que el cumpleaños del cliente o del bebé está dentro del intervalo indicado
            Date nacimiento;
            if (tipo.equals(TIPO_CUMPLEAÑOS_BEBE)) {
                nacimiento = ticket.getCliente().getFechaNacimientoUltimoHijo();
            } else { // CUMPLEAÑOS CLIENTE
                nacimiento = ticket.getCliente().getFechaNacimiento();
            }
            if (nacimiento == null) {
                return null;
            }
            Fecha fechaNacimiento = new Fecha(nacimiento);
            Fecha fechaHoy = new Fecha();
            int diaNacimiento = fechaNacimiento.getDia();
            int mesNacimiento = fechaNacimiento.getMesNumero();
            int diaHoy = fechaHoy.getDia();
            int mesHoy = fechaHoy.getMesNumero();
            if (mesNacimiento != mesHoy) {
                return null;
            }
            if (Math.abs(diaNacimiento - diaHoy) > diasIntervalo) {
                return null;
            }
        } else if (isTipoAutomaticaHistorico()) {
            Integer numComprasCliente = ticket.getCliente().getNumCompras();
            BigDecimal importeComprasCliente = ticket.getCliente().getImporteCompras();

            // si no tenemos definido ni numComprasMínimo ni montoComprasMinimo, no se puede emitir.
            if ((numComprasMinimo == null || numComprasMinimo.equals(0))
                    && (montoComprasMinimo == null || montoComprasMinimo.compareTo(BigDecimal.ZERO) == 0)) {
                return null;
            }
            // si tenemos definido numComprasMínimo, comprobamos
            if (numComprasMinimo != null && numComprasMinimo > 0) {
                if (numComprasCliente == null
                        || numComprasMinimo.compareTo(numComprasCliente) < 0) {
                    return null;
                }
            }

            // si tenemos definido montoComprasMínimo, comprobamos
            if ((montoComprasMinimo != null) && (montoComprasMinimo.compareTo(BigDecimal.ZERO) > 0)) {
                if (importeComprasCliente == null
                        || montoComprasMinimo.compareTo(importeComprasCliente) < 0) {
                    return null;
                }
            }
        } else if (isTipoAutomaticaBasica()) {
            List<LineaTicket> lineas = new ArrayList<LineaTicket>();
            int cant = calcularLineasEmision(ticket, lineas);
            // comprobamos cantidad mínima
            if (cantidadMinima > 0 && cantidadMinima > cant) {
                return null;
            }
            // comprobamos monto mínimo
            BigDecimal montoTotal = calcularMontoEmision(ticket, lineas, cant);
            if (montoMinimo.compareTo(BigDecimal.ZERO) > 0 && montoMinimo.compareTo(montoTotal) > 0) {
                return null;
            }
        } else if (isTipoAutomaticaBasicaSorteo()) {
            List<LineaTicket> lineas = new ArrayList<LineaTicket>();
            int cant = calcularLineasEmision(ticket, lineas);
            // comprobamos cantidad mínima
            if (cantidadMinima > 0 && cantidadMinima > cant) {
                return null;
            }
            // comprobamos monto mínimo
            BigDecimal montoTotal = calcularMontoEmision(ticket, lineas, cant);
            if (montoMinimo.compareTo(BigDecimal.ZERO) > 0 && montoMinimo.compareTo(montoTotal) > 0) {
                return null;
            }
            // comprobamos monto máximo
            if (montoMaximo.compareTo(BigDecimal.ZERO) > 0 && montoTotal.compareTo(montoMaximo) > 0) {
                montoTotal = montoMaximo;
            }
            // calculamos número de cupones a generar
            int numCupones = montoTotal.divide(montoFraccion, RoundingMode.DOWN).intValue();
            if (numCupones == 0) {
                return null;
            }
            List<Cupon> resultado = new ArrayList<Cupon>();
            for (int i = 0; i < numCupones; i++) {
                resultado.add(new Cupon(ticket, promocion));
            }
            return resultado;
        } else if (isTipoAutomaticaBasicaVotos()) {
            List<LineaTicket> lineas = new ArrayList<LineaTicket>();
            int cant = calcularLineasEmision(ticket, lineas);
            // comprobamos cantidad mínima
            if (cantidadMinima > 0 && cantidadMinima > cant) {
                return null;
            }
            // comprobamos monto mínimo
            BigDecimal montoTotal = calcularMontoEmision(ticket, lineas, cant);
            if (montoMinimo.compareTo(BigDecimal.ZERO) > 0 && montoMinimo.compareTo(montoTotal) > 0) {
                return null;
            }
            // comprobamos monto máximo
            if (montoMaximo.compareTo(BigDecimal.ZERO) > 0 && montoTotal.compareTo(montoMaximo) > 0) {
                montoTotal = montoMaximo;
            }
            // calculamos número de votos que tendrá el cupón
            int numVotosCupon = montoTotal.divide(montoFraccion, RoundingMode.DOWN).intValue() * numVotos;
            if (numVotosCupon == 0) {
                return null;
            }
            List<Cupon> resultado = new ArrayList<Cupon>();
            Cupon cupon = new Cupon(ticket, promocion);
            cupon.setVariable(numVotosCupon);
            resultado.add(cupon);
            return resultado;
        } else if (isTipoAutomaticaBasicaRegaloProveedorExterno()) {
            return promocion.emiteCupones(ticket, configEmision);
        } else if (isTipoAutomaticaBasicaRegaloCurso()) {
            return promocion.emiteCupones(ticket, configEmision);
        } else if (isTipoAutomaticaBasicaSorteoSukasa()) {
            return promocion.emiteCupones(ticket, configEmision);
        } else if (isTipoAutomaticaBasicaBilleton()) {
            return promocion.emiteCupones(ticket, configEmision);
        } else if (isTipoAutomaticaBasicaAzar()) {
            return promocion.emiteCupones(ticket, configEmision);
        }

        // si hemos llegado aquí es que hay que generar un cupon normal
        // (para todos los casos excepto VOTOS y SORTEO)
        List<Cupon> resultado = new ArrayList<Cupon>();
        resultado.add(new Cupon(ticket, promocion));
        return resultado;
    }

    public String vaAEmitirCupones(TicketS ticket) {
        //En este método comprobamos si hay algún artículo de alguna promoción de cupones pero no va llega al monto mínimo
        if (isTipoAutomaticaBasicaRegaloProveedorExterno()) {
            return ((PromocionTipoCuponRegaloProveedorExterno) promocion).isAplicableMontoMinimo(ticket);
        } else if (isTipoAutomaticaBasicaRegaloCurso()) {
            return ((PromocionTipoCuponRegaloCurso) promocion).isAplicableMontoMinimo(ticket);

        } else if (isTipoAutomaticaBasicaSorteoSukasa()) {
            return ((PromocionTipoCuponSorteoSukasa) promocion).isAplicableMontoMinimo(ticket);
        }
        return null;
    }

    public boolean permiteImprimirPorCadencia(Cliente cliente) throws CuponException {
        if (cliente.isClienteGenerico()) {
            return true;
        }
        if (cadencia > 0) {
            return !ServicioCupones.existenCuponesCliente(cliente.getCodcli(), promocion.getIdPromocion(), cadencia);
        }
        return true;

    }

    private BigDecimal calcularMontoEmision(TicketS ticket, List<LineaTicket> lineas, int numLineas) {
        BigDecimal montoTotal = BigDecimal.ZERO;
        // calculamos monto total
        if (numLineas == ticket.getLineas().getNumLineas()) { // si estamos trabajando con todas las líneas
            montoTotal = ticket.getTotales().getTotalAPagar();
        } else { // si no, calculamos
            montoTotal = BigDecimal.ZERO;
            for (LineaTicket linea : lineas) {
                montoTotal = montoTotal.add(linea.getImporteTotal());
            }
        }
        return montoTotal;
    }

    private int calcularLineasEmision(TicketS ticket, List<LineaTicket> lineas) {
        int cant;

        if (codMarca != null) {
            cant = ticket.getLineas().getContainsMarcas(false, codMarca, lineas);
        } else if (codCategoria != null) {
            cant = ticket.getLineas().getContainsCategoria(false, codCategoria, lineas);
        } else if (codSubseccion != null) {
            cant = ticket.getLineas().getContainsSubSecciones(false, codSubseccion, lineas);
        } else if (codSeccion != null) {
            cant = ticket.getLineas().getContainsSecciones(false, codSeccion, lineas);
        } else { // todas son null, tenemos en cuenta todos los artículos
            cant = ticket.getLineas().getNumLineas(); // TODO: PROMO - esta cantidad supone un artículo por línea, sólo funciona para BM, no para SK.
            lineas = ticket.getLineas().getLineas();
        }

        // ahora tratamos el caso combinado, es decir, que se haya indicado una marca y también una sección, subsección o categoría
        List<LineaTicket> lineasAux = new ArrayList<LineaTicket>();
        if (codMarca != null && codCategoria != null) {
            cant = 0;
            for (LineaTicket linea : lineas) {
                if (linea.getArticulo().getCodcategoria().equals(codCategoria)) {
                    lineasAux.add(linea);
                    cant = cant + linea.getCantidad();
                }
            }
            lineas = lineasAux;
        } else if (codMarca != null && codSubseccion != null) {
            cant = 0;
            for (LineaTicket linea : lineas) {
                if (linea.getArticulo().getCodsubseccion().equals(codSubseccion)) {
                    lineasAux.add(linea);
                    cant = cant + linea.getCantidad();
                }
            }
            lineas = lineasAux;
        } else if (codMarca != null && codSeccion != null) {
            cant = 0;
            for (LineaTicket linea : lineas) {
                if (linea.getArticulo().getCodseccion().equals(codSeccion)) {
                    lineasAux.add(linea);
                    cant = cant + linea.getCantidad();
                }
            }
            lineas = lineasAux;
        }
        return cant;
    }

    public String getTipoImpresion() {
        return tipoImpresion;
    }

    public void setTipoImpresion(String tipoImpresion) {
        this.tipoImpresion = tipoImpresion;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getCadencia() {
        return cadencia;
    }

    public void setCadencia(Integer cadencia) {
        this.cadencia = cadencia;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public BigDecimal getMontoMinimo() {
        return montoMinimo;
    }

    public void setMontoMinimo(BigDecimal montoMinimo) {
        this.montoMinimo = montoMinimo;
    }

    public Integer getCantidadMinima() {
        return cantidadMinima;
    }

    public void setCantidadMinima(Integer cantidadMinima) {
        this.cantidadMinima = cantidadMinima;
    }

    public String getCodMarca() {
        return codMarca;
    }

    public void setCodMarca(String codMarca) {
        this.codMarca = (codMarca != null && codMarca.isEmpty() ? null : codMarca);
    }

    public String getDesMarca() {
        return desMarca;
    }

    public void setDesMarca(String desMarca) {
        this.desMarca = desMarca;
    }

    public String getCodSeccion() {
        return codSeccion;
    }

    public void setCodSeccion(String codSeccion) {
        this.codSeccion = (codSeccion != null && codSeccion.isEmpty() ? null : codSeccion);
    }

    public String getCodSubseccion() {
        return codSubseccion;
    }

    public void setCodSubseccion(String codSubseccion) {
        this.codSubseccion = (codSubseccion != null && codSubseccion.isEmpty() ? null : codSubseccion);
    }

    public String getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(String codCategoria) {
        this.codCategoria = (codCategoria != null && codCategoria.isEmpty() ? null : codCategoria);
    }

    public Integer getNumComprasMinimo() {
        return numComprasMinimo;
    }

    public void setNumComprasMinimo(Integer numComprasMinimo) {
        this.numComprasMinimo = numComprasMinimo;
    }

    public BigDecimal getMontoComprasMinimo() {
        return montoComprasMinimo;
    }

    public void setMontoComprasMinimo(BigDecimal montoComprasMinimo) {
        this.montoComprasMinimo = montoComprasMinimo;
    }

    public Integer getDiasIntervalo() {
        return diasIntervalo;
    }

    public void setDiasIntervalo(Integer diasIntervalo) {
        this.diasIntervalo = diasIntervalo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<String> getListaCodigosArticulos() {
        return listaCodigosArticulos;
    }

    public void setListaCodigosArticulos(List<String> listaCodigosArticulos) {
        this.listaCodigosArticulos = listaCodigosArticulos;
    }

    public List<ComboArticuloCantidadBean> getListaCombos() {
        return listaCombos;
    }

    public void setListaCombos(List<ComboArticuloCantidadBean> listaCombos) {
        this.listaCombos = listaCombos;
    }

    public BigDecimal getMontoFraccion() {
        return montoFraccion;
    }

    public void setMontoFraccion(BigDecimal montoFraccion) {
        this.montoFraccion = montoFraccion;
    }

    public BigDecimal getMontoMaximo() {
        return montoMaximo;
    }

    public void setMontoMaximo(BigDecimal montoMaximo) {
        this.montoMaximo = montoMaximo;
    }

    public Integer getNumVotos() {
        return numVotos;
    }

    public void setNumVotos(Integer numVotos) {
        this.numVotos = numVotos;
    }

    public Long getCuponProveedorExterno() {
        return cuponProveedorExterno;
    }

    public void setCuponProveedorExterno(Long cuponProveedorExterno) {
        this.cuponProveedorExterno = cuponProveedorExterno;
    }

    public Integer getNumCupones() {
        return numCupones;
    }

    public void setNumCupones(Integer numCupones) {
        this.numCupones = numCupones;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Boolean getConAcompanante() {
        return conAcompanante;
    }

    public void setConAcompanante(Boolean conAcompanante) {
        this.conAcompanante = conAcompanante;
    }

    public boolean isTipoManual() {
        return tipoImpresion.equals(TIPO_MANUAL);
    }

    public boolean isTipoAutomaticaBasica() {
        return tipoImpresion.equals(TIPO_AUTOMATICA_BASICA);
    }

    public boolean isTipoAutomaticaBasicaVotos() {
        return tipoImpresion.equals(TIPO_AUTOMATICA_BASICA_VOTOS);
    }

    public boolean isTipoAutomaticaBasicaSorteo() {
        return tipoImpresion.equals(TIPO_AUTOMATICA_BASICA_SORTEO);
    }

    public boolean isTipoAutomaticaBasicaSorteoSukasa() {
        return tipoImpresion.equals(TIPO_AUTOMATICA_BASICA_SORTEO_SUKASA);
    }

    public boolean isTipoAutomaticaHistorico() {
        return tipoImpresion.equals(TIPO_AUTOMATICA_HISTORICO);
    }

    public boolean isTipoAutomaticaCumpleaños() {
        return tipoImpresion.equals(TIPO_AUTOMATICA_CUMPLEAÑOS);
    }

    public boolean isTipoAutomaticaArticulos() {
        return tipoImpresion.equals(TIPO_AUTOMATICA_ARTICULOS);
    }

    public boolean isTipoAutomaticaComboSeccion() {
        return tipoImpresion.equals(TIPO_AUTOMATICA_COMBO_SECCION);
    }

    public boolean isTipoAutomaticaBasicaRegaloProveedorExterno() {
        return tipoImpresion.equals(TIPO_AUTOMATICA_BASICA_REGALO_PROVEEDOR_EXTERNO);
    }

    public boolean isTipoAutomaticaBasicaRegaloCurso() {
        return tipoImpresion.equals(TIPO_AUTOMATICA_BASICA_REGALO_CURSO);
    }

    public boolean isTipoAutomaticaBasicaBilleton() {
        return tipoImpresion.equals(TIPO_AUTOMATICA_BASICA_BILLETON);
    }

    public boolean isTipoAutomaticaBasicaAzar() {
        return tipoImpresion.equals(TIPO_AUTOMATICA_BASICA_AZAR);
    }

    public Fecha getFechaValidez() {
        return fechaValidez;
    }

    public void setFechaValidez(Fecha fechaValidez) {
        this.fechaValidez = fechaValidez;
    }

    public SeleccionArticuloBean getListSeleccion() {
        return listSeleccion;
    }

    public void setListSeleccion(SeleccionArticuloBean listSeleccion) {
        this.listSeleccion = listSeleccion;
    }

}
