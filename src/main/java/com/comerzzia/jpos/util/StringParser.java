package com.comerzzia.jpos.util;

import com.comerzzia.util.Constantes;
import org.apache.commons.lang.StringUtils;

public class StringParser {

    /**
     * metodo para parsear caracteres especiales string a xml.
     *
     * @param observaciones
     * @return
     */
    public static String parsearXML(String observaciones) {
        String rep = StringUtils.EMPTY;
        if (StringUtils.isNotEmpty(observaciones)) {
            rep = observaciones.replaceAll("&", "&amp;");
            rep = rep.replaceAll("<", "&lt;");
            rep = rep.replaceAll(">", "&gt;");
            rep = rep.replaceAll("'", "&apos;");
            rep = rep.replaceAll("\"", "&quot;");
            return rep;
        }
        return rep;
    }

    private int currentPosition;
    private int maxPosition;
    private String str;

    /**
     * Creates a new instance of StringParser
     */
    public StringParser(String str) {
        this.str = str;
        currentPosition = 0;
        maxPosition = str == null ? 0 : str.length();
    }

    public String nextToken(char c) {

        if (currentPosition < maxPosition) {

            int start = currentPosition;
            while (currentPosition < maxPosition && c != str.charAt(currentPosition)) {
                currentPosition++;
            }

            if (currentPosition < maxPosition) {
                return str.substring(start, currentPosition++);
            } else {
                return str.substring(start);
            }
        } else {
            return "";
        }
    }

    /**
     * @author Gabriel Simbania
     * @param texto
     * @return
     */
    public static String parseaXMLDocumentosImpresos(String texto) {
        texto = texto.replaceAll("”", "\"");
        return texto;
    }

    public static String convertCodigoITocodArt(String codigoI) {
        String[] codArray = codigoI.split("-");
        String codArt = null;
        if (codArray.length == 2) {
            codArt = String.format(Constantes.FORMAT_CODART_POS, Integer.parseInt(codArray[0])) + "." + String.format(Constantes.FORMAT_CODART_POS, Integer.parseInt(codArray[1]));
        }

        return codArt;
    }

    /**
     * Convierte el formato del item del POS al formtado del ERP
     *
     * @author Gabriel Simbania
     * @param codigoI
     * @return
     */
    public static String convertCodArtToCodigoI(String codigoI) {
        String[] codArray = codigoI.split("\\.");
        String codArt = null;
        if (codArray.length == 2) {
            codArt = Integer.parseInt(codArray[0]) + "-" + Integer.parseInt(codArray[1]);
        }

        return codArt;
    }
}
