/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.core.variables;

import com.comerzzia.jpos.entity.db.VariableAlm;
import com.comerzzia.jpos.persistencia.core.variables.VariablesAlmDao;
import es.mpsistemas.util.cadenas.ToString;
import es.mpsistemas.util.log.Logger;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author MGRI
 */
public class VariablesAlm {

    private static final Logger log = Logger.getMLogger(VariablesAlm.class);
    public static final String COD_ALMACEN = "COD_ALMACEN_BRANCH";
    public static final String MAXIMA_REIMPRESION_BONO = "MAXIMA_REIMPRESION_BONO";
    public static final String URL_IMAGENES = "URL_IMAGENES";
    public static final String TICKET_AVISO_LEGAL_BONO = "TICKET.AVISO_LEGAL_BONO";
    public static final String TICKET_AVISO_LEGAL_VOUCHER = "TICKET.AVISO_LEGAL_VOUCHER";
    public static final String UI_MENSAJE_DIAS = "UI.MENSAJE.BUENOS_DIAS";
    public static final String UI_MENSAJE_TARDES = "UI.MENSAJE.BUENAS_TARDES";
    public static final String UI_MENSAJE_NOCHES = "UI.MENSAJE.BUENAS_NOCHES";
    
    public static final String BBDD_PROPIETARIA_URL = "BBDD.PROPIETARIA.URL";
    public static final String BBDD_PROPIETARIA_DRIVER = "BBDD.PROPIETARIA.DRIVER";
    public static final String BBDD_PROPIETARIA_ESQUEMA_SUKASA = "BBDD.PROPIETARIA.ESQUEMA.SUKASA";
    public static final String BBDD_PROPIETARIA_ESQUEMA_CREDITO = "BBDD.PROPIETARIA.ESQUEMA.CREDITO";
    public static final String BBDD_PROPIETARIA_ESQUEMA_VENTAS = "BBDD.PROPIETARIA.ESQUEMA.VENTAS";
    public static final String BBDD_PROPIETARIA_ESQUEMA_STOCK = "BBDD.PROPIETARIA.ESQUEMA.STOCK";
    public static final String BBDD_PROPIETARIA_SUKASA_PASSWORD = "BBDD.PROPIETARIA.SUKASA.PASSWORD";
    public static final String BBDD_PROPIETARIA_VENTAS_PASSWORD = "BBDD.PROPIETARIA.VENTAS.PASSWORD";
    public static final String BBDD_PROPIETARIA_CREDITO_PASSWORD = "BBDD.PROPIETARIA.CREDITO.PASSWORD";
    public static final String BBDD_PROPIETARIA_STOCK_PASSWORD = "BBDD.PROPIETARIA.STOCK.PASSWORD";
    
    public static final String POS_CONFIG_FUNC_PLAN_NOVIOS = "POS.CONFIG.FUNC.PLAN_NOVIOS";
    public static final String POS_CONFIG_TIPO_AUTORIZADOR_TARJETAS = "POS.CONFIG.TIPO.AUTORIZADOR.TARJETAS";
    public static final String POS_CONFIG_FUNC_IMPRESION_CHEQUES = "POS.CONFIG.FUNC.IMPRESION.CHEQUES"; 
    public static final String POS_BINES_PRIORIDAD_FORMATO_CREDITO_DIRECTO = "POS.BINES.PRIORIDAD.FORMATO.CREDITO.DIRECTO";
    public static final String POS_IMPORTE_MAXIMO_AUTORIZACION_CREDITO_DIRECTO = "POS.IMPORTE.MAXIMO.AUTORIZACION.CREDITO.DIRECTO";
    
    public static final BigDecimal IMPORTE_MAXIMO_SIN_FACTURAR = new BigDecimal(200);
    
    public static final String REALIZA_FACT_ELECTRONICA = "REALIZA.FACT_ELECTRONICA";
    public static final String FACT_ELECT_CONFIG_AMBIENTE = "FACT_ELECT.CONFIG.AMBIENTE";
    
    public static final String PORCENTAJE_COMPENSACION_GOBIERNO = "PORCENTAJE.COMPENSACION.GOBIERNO";
    
    public static final String PINPAD_MID_DATAFAST = "PINPAD.MID.DATAFAST";
    public static final String PINPAD_MID_MEDIANET = "PINPAD.MID.MEDIANET";
    public static final String PINPAD_TIMEOUT = "PINPAD.TIMEOUT";
    public static final String PINPAD_ESTADO_AUTOMATICO = "PINPAD.ESTADO.AUTOMATICO";
    //TODO: PENDIENTE PASAR A PRODUCCION POR LOCAL
    public static final String PINPAD_FASTTRACK_ACTIVO = "PINPAD.FASTTRACK.ACTIVO";
    
    public static final String INTERLINEADO_CHEQUE_ANVERSO = "INTERLINEADO.CHEQUE.ANVERSO";
    public static final String INTERLINEADO_CHEQUE_REVERSO = "INTERLINEADO.CHEQUE.REVERSO";
    
    public static final String SMS_IDCLIENTE = "SMS.IDCLIENTE"; 
    public static final String SMS_IDMENSAJE ="SMS.IDMENSAJE";
    public static final String SMS_TOKEN ="SMS.TOKEN";
    
    //TODO: PENDIENTE PASAR A PRODUCCION. DEBE CAMBIARSE EN TODOS LOS LOCALES AL MISMO TIEMPO
    public static final String PARAMETRO_CREDITO_ACTIVO_CRT = "CREDITO.ACTIVO.CRT";
    
    public static final String FACTURA_VENTA_MANUAL = "FACTURA.VENTA.MANUAL";
    public static final String CONSULTA_REGISTRO_CIVIL ="CONSULTA.REGISTRO.CIVIL";
    
    private static Map<String, String> variables = new HashMap<String, String>();  
    
    public static void obtieneVariables() {
        try {
            variables.clear();


            List<VariableAlm> lAux = VariablesAlmDao.consultaVariables();
            for (VariableAlm vAux : lAux) {
                variables.put(vAux.getIdVariable(), vAux.getValor());
            }
            log.debug("Listando valor de VARIABLES LOCAL configurado:::: ");
            log.debug("\n" + ToString.toStringMap(variables));
        }
        catch (Exception ex) {
            log.error("VariablesAlmServices: Error al Consultar las variables de almacen", ex);
        }
    }

    public static String getVariable(String var) {

        if (variables.isEmpty()) {
            obtieneVariables();
        }
        return variables.get(var);
    }

    public static Integer getVariableAsInt(String var) {
        try {
            return Integer.valueOf(getVariable(var));
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    public static Long getVariableAsLong(String var) {
        try {
            return Long.valueOf(getVariable(var));
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    public static BigDecimal getVariableAsBigDecimal(String var) {
        try {
            return new BigDecimal(getVariable(var));
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    public static Boolean getVariableAsBoolean(String var) {

        String boolVar = getVariable(var);
        if (boolVar != null && boolVar.equals("S")) {
            return true;
        }
        else if (boolVar != null && boolVar.equals("N")) {
            return false;
        }
        else {
            return null;
        }
    }
    
    public static Boolean getVariableEstadoPinpadAsBoolean(String variableAlm) {

        String boolVar = null;
        try {
            VariableAlm pinpad = VariablesAlmDao.consultaVariables(variableAlm);
            if(pinpad != null){
                boolVar = VariablesAlmDao.consultaVariables(variableAlm).getValor();
                 if (boolVar != null && boolVar.equals("S")) {
                    return true;
                }
                else if (boolVar != null && boolVar.equals("N")) {
                    return false;
                }
                else {
                    return false;
                }
            }else{
                return false;
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(VariablesAlm.class.getName()).log(Level.SEVERE, null, ex);
        }
       return null;
    }
    
    /**
     * @author Gabriel Simbania
     * @description Metodo que cosulta de la base de datos en ese momento el valor del variables
     * @param variableAlm
     * @return 
     */
    public static Boolean getVariableAsBooleanActual(String variableAlm) {

        String boolVar = null;
        try {
            VariableAlm var = VariablesAlmDao.consultaVariables(variableAlm);
            if(var != null){
                boolVar = VariablesAlmDao.consultaVariables(variableAlm).getValor();
                 if (boolVar != null && boolVar.equals("S")) {
                    return true;
                }
                else if (boolVar != null && boolVar.equals("N")) {
                    return false;
                }
                else {
                    return false;
                }
            }else{
                return false;
            }
        } catch (Exception ex) {
            log.error("Error al consultar la variable "+variableAlm,ex);
        }
       return null;
    }
    
    
      public void modificaVariable(VariableAlm variableAlm) throws Exception {
        VariablesAlmDao variablesAlmDao = new VariablesAlmDao();
        variablesAlmDao.modificaVariable(variableAlm);
    }
}
