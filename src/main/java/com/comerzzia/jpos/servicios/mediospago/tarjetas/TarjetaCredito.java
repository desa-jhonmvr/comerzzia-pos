/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.mediospago.tarjetas;

import com.comerzzia.jpos.persistencia.mediospagos.MedioPagoBean;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.mediospago.MedioPagoException;
import com.comerzzia.jpos.servicios.mediospago.MediosPago;
import com.comerzzia.jpos.servicios.mediospago.MediosPagoServices;
import com.comerzzia.util.cadenas.Cadena;
import es.mpsistemas.util.log.Logger;
import es.mpsistemas.util.numeros.Numero;

/**
 *
 * @author MGRI
 */
public class TarjetaCredito {

    private static final Logger log = Logger.getMLogger(TarjetaCredito.class);
    private static final int LONGITUD_BIN_MAX = 10;
    private static final int LONGITUD_BIN_MAX_E = 19;
    //Caracter separador ingles ^, en español &
    public static String CARACTER_SEPARADOR = "^";
    public static String CARACTER_SEPARADOR_ALTERNATIVO = "¡"; // Este caracter separador se utilizará si no se detecta en la trama el primer caracter separador
    public static String CARACTER_SEPARADOR_ALTERNATIVO2 = "&";
    public static String CAB_PISTA1 = "";
    public static String FIN_PISTA1 = ";"; // Antes ""
    public static String SEP_PISTA1 = "_";
    public static String CAB_PISTA2 = "";
    public static String FIN_PISTA2 = "";
    public static String SEP_PISTA2 = "";
    public static String CARACTER_SEPARADOR2 = "¡"; //Usado en la pista 2
    public static final String TARJETA_DEFECTO = "XXXXXXXXXXXXXXXX";
    private String numero;
    private MedioPagoBean medioPago;
    private String bine; // n digitos
    private String autorizador;  // Si el autorizador puede dar de alta
    private String caducidad;
    private String cvv;
    private String banda;
    private String track1;
    private String track2;
    private boolean lecturaManual;
    private boolean errorLecturaTarjeta;

    private boolean lecturaDesdeCedula;
    private String numeroTarjetaPinPad;

    static {
        if (Sesion.isSukasa()) {
            CARACTER_SEPARADOR = "^";
            CARACTER_SEPARADOR_ALTERNATIVO = "¡"; // Este caracter separador se utilizará si no se detecta en la trama el primer caracter separador
            CAB_PISTA1 = "%";
            FIN_PISTA1 = "?"; // Antes ""
            SEP_PISTA1 = "";
            CAB_PISTA2 = "ñ";
            FIN_PISTA2 = "_";
            SEP_PISTA2 = "";
            CARACTER_SEPARADOR2 = "¿"; //Usado en la pista 2   
        }

    }

    private TarjetaCredito() {
    }

    public TarjetaCredito(String numero, String caducidad, String cvv) {
        super();
        this.numero = numero;
        this.cvv = cvv;
        this.caducidad = caducidad;
        this.banda = "";
        this.track1 = "";
        this.track2 = "";
        this.lecturaManual = true;
        lecturaDesdeCedula = false;
    }

    public TarjetaCredito(String numero, String caducidad, String cvv, boolean lecturaDesdeCedula) {
        super();
        this.numero = numero;
        this.cvv = cvv;
        this.caducidad = caducidad;
        this.banda = "";
        this.track1 = "";
        this.track2 = "";
        this.lecturaManual = true;
        this.lecturaDesdeCedula = lecturaDesdeCedula;
    }

    public TarjetaCredito(String numero, boolean lecturaDesdeCedula) {
        super();
        this.numero = numero;
        this.cvv = "";
        this.caducidad = "";
        this.banda = "";
        this.track1 = "";
        this.track2 = "";
        this.lecturaManual = false;
        this.lecturaDesdeCedula = lecturaDesdeCedula;
    }

    public TarjetaCredito(String bandaMagnetica) {
        super();
        this.banda = bandaMagnetica;
        decodificaBanda();
        this.lecturaManual = false;
        this.lecturaDesdeCedula = false;
    }

    protected void decodificaBanda() {
        log.debug("decodificaBanda() - Decodificando banda magnética: " + this.banda);
        // Para tarjetas
        int inicioTarjeta = 0;

        // Si la codificación de la tarjeta tiene comienzo de Bnada 1, se lo quitamos
        if (!CAB_PISTA1.isEmpty()) {
            if (banda.startsWith(CAB_PISTA1)) {
                // Tratamiento de cabecera de pista 1 (genérico)
                banda = banda.substring(CAB_PISTA1.length(), banda.length());
            }
        }

        // La tarjeta supermaxi comienza por 000, si es supermaxi, ignoraremos los 3 primeros caracteres.
        if (this.banda.startsWith("B")) {
            inicioTarjeta = this.banda.indexOf("B");
        } else if (this.banda.startsWith("b")) {
            inicioTarjeta = this.banda.indexOf("b");
        } else {
            inicioTarjeta = 2; // esto hace que no consideremos los 3 primeros caracteres a la hora de calcular
        }

        // Establecemos el caracter separador de la trama
        String caracterSeparador = null;
        if (this.banda.indexOf(CARACTER_SEPARADOR) >= 0) {
            caracterSeparador = CARACTER_SEPARADOR;
        } else if (this.banda.indexOf(CARACTER_SEPARADOR_ALTERNATIVO) >= 0) {
            caracterSeparador = CARACTER_SEPARADOR_ALTERNATIVO;
        } else if (this.banda.indexOf(CARACTER_SEPARADOR_ALTERNATIVO2) >= 0) {
            caracterSeparador = CARACTER_SEPARADOR_ALTERNATIVO2;
        } else {
            setErrorLecturaTarjeta(true);
            log.error("Error: No se ha encontrado el caracter de separación de la trama1 para la tarjeta");
            log.error("Muestra: " + this.banda.substring(14, 18)); // Se imprime una muestra donde debe aparecer el separador real de la tarjeta
            return;
        }

        // Fin del número de tarjeta
        int finTarjeta = this.banda.indexOf(caracterSeparador);

        // Leemos el número de tarjeta
        log.debug("- (PagoCredito - DecodificarBanda) - Decodificando número de tarjeta ");
        this.numero = this.banda.substring(inicioTarjeta + 1, finTarjeta);
        log.debug(" Numero de tarjeta leído: " + numero);
        // Comprobamos que tiene una longitud adecuada
        if (this.numero.length() < 14) {
            log.debug("- (PagoCredito - DecodificarBanda) - Error leyendo  número de tarjeta desde la banda. ");
            this.numero = "";
            setErrorLecturaTarjeta(true);
        } else if (this.numero.length() > 16) {
            log.debug("- (PagoCredito - DecodificarBanda) - Error leyendo  número de tarjeta desde la banda. Nos quedamos con los 16 primeros dígitos. ");
            this.numero = this.numero.substring(0, 16);
            //setErrorLecturaTarjeta(true);
        }
        try {
            // Fecha de caducidad AAMM
            log.debug("- (PagoCredito - DecodificarBanda) - Decodificando fecha de caducidad ");
            int inicioFechaCaducidad = this.banda.lastIndexOf(caracterSeparador);
            this.caducidad = this.banda.substring(inicioFechaCaducidad + 1, inicioFechaCaducidad + 5);

            // TrackI y II         
            int finTrack1 = this.banda.indexOf(FIN_PISTA1);
            if (finTrack1 > 0) {
                this.track1 = this.banda.substring(0, finTrack1);
            }
            // this.track2 = this.banda.substring(finTrack1 + 2, banda.length());  // El track 2 comienza tras el fin del track1 y tras el separador de track1      
            this.track2 = "";

            // Tratamiento si hay cabecera de pista 2
            if (!CAB_PISTA2.isEmpty() && track2.startsWith(CAB_PISTA2)) {
                track2 = track2.substring(CAB_PISTA2.length(), track2.length());
            }

            // Tratamiento para despreciar Pista 3 si esta existe.
            if (!CAB_PISTA2.isEmpty() && track2.indexOf(CAB_PISTA2) > 0) {
                // Existe una tercera pista y la borramos
                track2 = track2.substring(0, track2.indexOf(CAB_PISTA2));
            }

            // Tratamiento para eliminar el caracter de fin de pista 2
            if (!FIN_PISTA2.isEmpty() && track2.endsWith(FIN_PISTA2)) {
                track2 = track2.substring(0, track2.length() - FIN_PISTA2.length());
            }
            log.debug("decodificaBanda() - La caducidad es: " + caducidad + ", track1: " + track1 + ", track2: " + track2);
        } catch (Exception e) {
            log.warn("Error parseando la caducidad, track 1 o track 2", e);
            this.caducidad = "";
            this.track1 = "";
            this.track2 = "";
        }
    }

    public void validaTarjetaCredito() throws TarjetaInvalidaException {
        try {
            boolean enListaNegra = MediosPagoServices.consultarEnListaNegra(numero);
            if (enListaNegra) {
                throw new TarjetaInvalidaException("Tarjeta en lista negra. Requiere autorización.", true);
            }
        } catch (MedioPagoException ex) {
            // Si tenemos error de validación daremos la tarjeta como inválida
            throw new TarjetaInvalidaException("Erro intentando comprobar en el sistema si la tarjeta está en lista negra", ex, true);
        }

    }
    
    public static MedioPagoBean getBINMedioPagoBanda(String banda) {
        return getBINMedioPagoBanda(banda,null);
    }

    public static MedioPagoBean getBINMedioPagoBanda(String banda,MediosPago medioPago) {
//        banda="002473710000248003^ANDRES QUINTANA^0802967703?;0002473710000248003=9912080296?y";
//           banda=banda.substring(2);
        MediosPago mediosPago =null;
        if(medioPago==null){
            medioPago = MediosPago.getInstancia();
        }
        log.debug("getBINMedioPagoBanda() - Banda leída: "+banda);
        String cadenaBineLeido = "";
        if (!CAB_PISTA1.isEmpty()) {
            if (banda.startsWith(CAB_PISTA1)) {
                // Tratamiento de cabecera de pista 1 (genérico)
                banda = banda.substring(CAB_PISTA1.length(), banda.length());
            }
        }
        if (banda.length() > 1) {
            int inicio = 0;
            if (banda.startsWith("B")) {
                inicio = 1;
            }
            if (banda.startsWith("0")) {
                inicio = 3;
            }
            int i = LONGITUD_BIN_MAX;
            while (i > inicio) {
                cadenaBineLeido = new String(banda.substring(inicio, i));
                if (medioPago.getBinesMedioPagos().containsKey(cadenaBineLeido)) {
                    MedioPagoBean medioPagoBean = medioPago.getBinesMedioPagos().get(cadenaBineLeido);
                    log.debug("getBINMedioPagoBanda() - Medio de pago encontrado: "+medioPagoBean.toString());
                    return medioPagoBean;
                }
                i--;
            }
        }
        log.debug("getBINMedioPagoBanda() - NO se ha encontrado ningún medio de pago asociado a los bines");
        return null;
    }

    public static String getBINPagoBanda(String banda) {
        log.debug("getBINMedioPagoBanda() - Banda leída: " + banda);
        String cadenaBineLeido = "";
        if (!CAB_PISTA1.isEmpty()) {
            if (banda.startsWith(CAB_PISTA1)) {
                // Tratamiento de cabecera de pista 1 (genérico)
                banda = banda.substring(CAB_PISTA1.length(), banda.length());
            }
        }
        if (banda.length() > 1) {
            int inicio = 0;
            if (banda.startsWith("B")) {
                inicio = 1;
            }
            if (banda.startsWith("0")) {
                inicio = 3;
            }
            int i = LONGITUD_BIN_MAX_E;
            cadenaBineLeido = new String(banda.substring(inicio, i));
            if (cadenaBineLeido.length() < 16) {
                return null;
            }
           
        }
         return cadenaBineLeido;
    }

    /**
     * Devuelve el medio de pago asociado al BIN contenido en el número de
     * tarjeta.
     */
    public MedioPagoBean getBINMedioPago() {
        return getBINMedioPagoNumero(numero);
    }

    /**
     * Devuelve el medio de pago asociado al BIN del número tarjeta indicado.
     */
    public static MedioPagoBean getBINMedioPagoNumero(String numeroTarjeta) {
        String cadenaBineLeido = "";
        if (!numeroTarjeta.isEmpty()) {
            /*
            int i = 1;
            while (i <= LONGITUD_BIN_MAX + 1 && i <= numeroTarjeta.length()) {
                cadenaBineLeido = new String(numeroTarjeta.substring(0, i));
                if (MediosPago.getInstancia().getBinesMedioPagos().containsKey(cadenaBineLeido)) {
                    return MediosPago.getInstancia().getBinesMedioPagos().get(cadenaBineLeido);
                }
                i++;
            }
             */
            int i = LONGITUD_BIN_MAX;
            while (i > 0) {
                cadenaBineLeido = new String(numeroTarjeta.substring(0, i));
                if (MediosPago.getInstancia().getBinesMedioPagos().containsKey(cadenaBineLeido)) {
                    return MediosPago.getInstancia().getBinesMedioPagos().get(cadenaBineLeido);
                }
                i--;
            }
        }
        return null;
    }

    public Boolean isLecturaBandaManual() {
        return lecturaManual;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        if (!CAB_PISTA1.isEmpty()) {
            if (numero.startsWith(CAB_PISTA1)) {
                // Tratamiento de cabecera de pista 1 (genérico)
                this.numero = numero.substring(CAB_PISTA1.length(), banda.length());
            } else {
                this.numero = numero;
            }
        }
    }

    public String getBine() {
        return bine;
    }

    public void setBine(String bine) {
        this.bine = bine;
    }

    public String getAutorizador() {
        return autorizador;
    }

    public void setAutorizador(String autorizador) {
        this.autorizador = autorizador;
    }

    public Integer getCaducidad() {
        if (this.caducidad != null && !this.caducidad.isEmpty()) {
            log.debug(" Validación normal -> caducidad " + caducidad);
            return Numero.getEntero(this.caducidad, null);
        } else {
            log.debug(" Validación manual -> caducidad 0");
            return 0;
        }
    }

    public void setCaducidad(String caducidad) {
        this.caducidad = caducidad;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getTrackI() {
        if (banda == null) {
            return "";
        }
        return track1;
    }

    public String getTrackII() {
        if (banda == null) {
            return "";
        }
        return track2;

    }

    public String getBanda() {
        return banda;
    }

    public void setBanda(String banda) {
        this.banda = banda;
    }

    public String getNumeroOculto() {
        if (getNumeroTarjetaPinPad() != null && !getNumeroTarjetaPinPad().isEmpty()) {
            return getNumeroTarjetaPinPad();
        }
        return Cadena.ofuscarTarjeta(this.getNumero());
    }

    public MedioPagoBean getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(MedioPagoBean medioPago) {
        this.medioPago = medioPago;
    }

    public boolean isErrorLecturaTarjeta() {
        return errorLecturaTarjeta;
    }

    public void setErrorLecturaTarjeta(boolean errorLecturaTarjeta) {
        this.errorLecturaTarjeta = errorLecturaTarjeta;
    }

    /**
     * @return the lecturaDesdeCedula
     */
    public boolean isLecturaDesdeCedula() {
        return lecturaDesdeCedula;
    }

    public String getNumeroTarjetaPinPad() {
        return numeroTarjetaPinPad;
    }

    public void setNumeroTarjetaPinPad(String numeroTarjetaPinPad) {
        this.numeroTarjetaPinPad = numeroTarjetaPinPad;
    }

    public boolean validarNumeroTarjeta() {
        if (numeroTarjetaPinPad == null || numeroTarjetaPinPad.isEmpty()) {
            return true;
        }
        String binPinPad = numeroTarjetaPinPad.substring(0, 6);
        String binBanda = numero.substring(0, 6);
        return binPinPad.equals(binBanda);
    }

}
