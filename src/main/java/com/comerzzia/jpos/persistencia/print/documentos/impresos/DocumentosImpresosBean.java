package com.comerzzia.jpos.persistencia.print.documentos.impresos;

public class DocumentosImpresosBean extends DocumentosImpresosKey {

    public static final String TIPO_FACTURA = "FACTURA";
    public static final String TIPO_PAGO = "FAC_PAGO";
    public static final String TIPO_EXTGAR = "FAC_EXTGAR";
    public static final String TIPO_CUPON = "FAC_CUPON";
    public static final String TIPO_TABLA= "FAC_TABLA";
    public static final String TIPO_BONO = "FAC_BONO";
    public static final String TIPO_COT = "COTIZACION";
    public static final String TIPO_GIFTCAR = "GIFTCARD";
    public static final String TIPO_GIFTCARD_P = "GIFTCARD_P";
    public static final String TIPO_LETRA = "LETR_ABONO";
    public static final String TIPO_CREDITO = "CRED_ABONO";
    public static final String TIPO_RESERVACION = "RESERVA";
    public static final String TIPO_PLAN_NOVIO = "PLAN_NOVIO";
    public static final String TIPO_ABONO_RESERVA = "RE_ABONO";
    public static final String TIPO_ABONO_PLAN_NOVIO = "PN_ABONO";
    public static final String TIPO_PLAN_NOVIO_LIQU = "PN_LIQUIDA";
    public static final String TIPO_RES_LIQUIDA = "RE_LIQUIDA";
    public static final String TIPO_RES_BONO = "RE_BONO";
    public static final String TIPO_NC = "NOTA_CRED";
    public static final String TIPO_CREDITO_DIRECTO = "C_DIRECTO";
    public static final String TIPO_ANULA_VOUCH = "ANUL_VOUC";

    //DAINOVY_CA
    public static final String TIPO_CINTA_AUDITORA = "C_AUDITORA";
    //DAINOVY_CA

    //ORIGEN
    public static final String ORIGEN_REIMPRESION = "1";

    //PLANTILLAS
    public static final String PLANTILLA_CREDITO_DIRECTO = "/credito_directo.xml";
    public static final String PLANTILLA_PENDIENTE_DESPACHO = "/ticket_pendiente_despacho.xml";

    private String tipoImpreso;

    private byte[] impreso;

    private String origen;

    //INICIO ATRIBUTOS PERSONALIZADOS--------------------------------------------
    //FIN ATRIBUTOS PERSONALIZADOS-----------------------------------------------
    public String getTipoImpreso() {
        return tipoImpreso;
    }

    public void setTipoImpreso(String tipoImpreso) {
        this.tipoImpreso = tipoImpreso == null ? null : tipoImpreso.trim();
    }

    public byte[] getImpreso() {
        return impreso;
    }

    public void setImpreso(byte[] impreso) {
        this.impreso = impreso;
    }

    //INICIO M�TODOS PERSONALIZADOS--------------------------------------------
    //FIN M�TODOS PERSONALIZADOS-----------------------------------------------
    public boolean isTipoDocumentoFactura() {
        return (tipoImpreso != null && tipoImpreso.trim().equals(TIPO_FACTURA));
    }

    public boolean isTipoFacCupon() {
        return (tipoImpreso != null && tipoImpreso.trim().equals(TIPO_CUPON));
    }

    public boolean isTipoFacPago() {
        return (tipoImpreso != null && tipoImpreso.trim().equals(TIPO_PAGO));
    }

    public boolean isExtensionGarantia() {
        return (tipoImpreso != null && tipoImpreso.trim().equals(TIPO_EXTGAR));
    }
    public boolean isTablaAmortizacion() {
        return (tipoImpreso != null && tipoImpreso.trim().equals(TIPO_TABLA));
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

}
