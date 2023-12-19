/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.util;

/**
 *
 * @author Admin
 */
public class ValidadorCedula {

    public static String[] codigosProvincia = {"02", "03", "04", "05", "06", "07"};
    public static final String PASAPORTE = "PASAPORTE";
    public static final String CEDULA = "CÉDULA";
    public static final String RUC = "RUC";
    public static final String JURIDICA = "Persona Jurídica";
    public static final String NATURAL = "Persona Natural";
    public static final int NUMERO_DE_PROVINCIAS = 24;

    /**
     * Verifica si el número de identificacion enviado cumple con las reglas
     * Ecuatorianas
     *
     * @param type Tipo de persona: Jurídica, Natural
     * @param idType Tipo de identificacion: Pasaporte, CI, RUC
     * @param id Número de identificacion
     * @return Verdadero si cumple con las reglas ecuatorianas
     */
    public static boolean verificarIdEcuador(String idType, String id) {
        String tipo = idType;
        if (idType.equals("RUC Natural") || idType.equals("RUC Jurídico")) {
            idType = "RUC";
        }
        try {
            String type = NATURAL;
            if (idType.equals("RUC") && id.substring(2, 3).equals("9") || idType.equals("RUC") && id.substring(2, 3).equals("6")) {
                if (tipo.equals("RUC Natural")) {
                    return false;
                }
                type = JURIDICA;
            }
            if (!idType.equals(PASAPORTE)) {

                //Verifica que contenga solo dígitos
                if (!id.matches("^[0-9]*$")) {
                    return false;
                }

                //Si es CI no puede ser Jurídica
                if (idType.equals(CEDULA) && type.equals(JURIDICA)) {
                    return false;
                }

                //Verifica la longitud
                if ((idType.equals(RUC) && id.length() != 13) || (idType.equals(CEDULA) && id.length() != 10)) {
                    return false;
                }

                //Verifica que los dos primeros dígitos correspondan a un valor entre 1 y NUMERO_DE_PROVINCIAS
                int prov = Integer.parseInt(id.substring(0, 2));

                if (!((prov > 0) && (prov <= NUMERO_DE_PROVINCIAS))) {
                    return false;
                }
            } else {
                //Si es Pasaporte no puede ser Jurídica
                if (type.equals(JURIDICA)) {
                    return false;
                }
                return true;
            }

            //Validacion de dígitos
            if (type.equals(NATURAL)) {

                //Si es Natural y es RUC los últimos tres dígitos solo pueden ser 001
                if (idType.equals(RUC)) {

                    if (!id.substring(10).equals("001") || tipo.equals("RUC Jurídico")) {
                        return false;
                    }
                    return true;
                    //Se comenta porque el SRI ya no existe el dígito verificador para el RUC https://www.sri.gob.ec/RUC
                    //return digitoVerificadorPersonaNatural(id.substring(0, 10));
                }
                return digitoVerificadorPersonaNatural(id);
            } else if (type.equals(JURIDICA)) {
                return true;
                //Se comenta porque el SRI ya no existe el dígito verificador para el RUC https://www.sri.gob.ec/RUC
                //return digitoVerificadorPersonaJuridica(id);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si el número de identificacion cumple con el algoritmo dígito
     * verificador para personas naturales
     *
     * @param id Número de identificacion
     * @return Verdadero si cumple con el algoritmo
     */
    public static boolean digitoVerificadorPersonaNatural(String id) {

        int[] d = new int[10];

        //Asignamos el string a un array
        for (int i = 0; i < d.length; i++) {
            d[i] = Integer.parseInt(id.charAt(i) + "");
        }

        int imp = 0;
        int par = 0;

        //Sumamos los duplos de posicion impar
        for (int i = 0; i < d.length; i += 2) {
            d[i] = ((d[i] * 2) > 9) ? ((d[i] * 2) - 9) : (d[i] * 2);
            imp += d[i];
        }

        //Sumamos los digitos de posicion par
        for (int i = 1; i < (d.length - 1); i += 2) {
            par += d[i];
        }

        //Sumamos los dos resultados
        int suma = imp + par;

        //Restamos de la decena superior
        int d10 = Integer.parseInt(String.valueOf(suma + 10).substring(0, 1) + "0") - suma;

        //Si es diez el decimo digito es cero
        d10 = (d10 == 10) ? 0 : d10;

        //si el decimo dígito calculado es igual al digitado la cedula es correcta
        return d10 == d[9];
    }

    /**
     * Verifica si el número de identificacion cumple con el algoritmo dígito
     * verificador para personas jurídicas
     *
     * @param id Número de identificacion
     * @return Verdadero si cumple con el algoritmo
     */
    private static boolean digitoVerificadorPersonaJuridica(String id) {
        int[] d = new int[13];
        int sum = 0;
        int digito;

        //Asignamos el string a un array
        for (int i = 0; i < d.length; i++) {
            d[i] = Integer.parseInt(id.charAt(i) + "");
        }

        //Si el tercer dìgito es igual a 9, se trata de una empresa Privada con coeficientes 4,3,2,7,6,5,4,3,2
        if (id.substring(2, 3).equals("9")) {

            int[] c1 = new int[9];

            c1[0] = 4;
            c1[1] = 3;
            c1[2] = 2;
            c1[3] = 7;
            c1[4] = 6;
            c1[5] = 5;
            c1[6] = 4;
            c1[7] = 3;
            c1[8] = 2;

            for (int i = 0; i < 9; i++) {
                sum += d[i] * c1[i];
            }

            digito = (sum % 11) == 0 ? 0 : 11 - (sum % 11);

            return d[9] == digito;
        } //Si el tercer dìgito es igual a 6, se trata de una empresa Pública con coeficientes 3,2,7,6,5,4,3,2
        else if (id.substring(2, 3).equals("6")) {

            int[] c2 = new int[8];

            c2[0] = 3;
            c2[1] = 2;
            c2[2] = 7;
            c2[3] = 6;
            c2[4] = 5;
            c2[5] = 4;
            c2[6] = 3;
            c2[7] = 2;

            for (int i = 0; i < 8; i++) {
                sum += d[i] * c2[i];
            }

            digito = (sum % 11) == 0 ? 0 : 11 - (sum % 11);

            return d[8] == digito;
        }

        return false;
    }

}
