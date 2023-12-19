/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.print.lineas;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author MGRI
 */
public class LineaEnTicket {

    private List<String> lineas;
    private String textoOriginal;

    public LineaEnTicket() {
        super();
        lineas = new LinkedList<String>();
    }

    /**
     *  Contruye nuestras líneas teniendo en cuenta que no se parten palabras
     * @param texto 
     */
    public LineaEnTicket(String texto) {
        this(texto, false);
    }

    public LineaEnTicket(String texto, boolean partirLineas) {
        this(texto, partirLineas, false);
    }

    /**
     *  Contruye nuestras líneas partiendo palabras si lo indica el booleano
     * @param texto
     * @param partirLineas
     */
    public LineaEnTicket(String texto, boolean partirLineas, boolean justificar) {
        this();
        if (texto !=null && !texto.isEmpty()){
            String[] textos = texto.split("\\|");
            int bloque = 0;
            while (bloque<textos.length){
                costruyeLineas(textos[bloque],partirLineas,justificar);
                bloque++;
            }
        }
    }
    
    public void costruyeLineas(String textoCompleto, boolean partirLineas, boolean justificar) {
        
        this.textoOriginal = textoCompleto;
        if (textoCompleto != null) {
            String[] textos = textoCompleto.split("\n");
            for (String texto : textos) {
                int ini = 0, fin;
                while (ini < texto.length()) {
                    fin = ini + 40;
                    if (fin < texto.length()) {
                        int retroceso = 0;
                        if (!partirLineas) {
                            while (fin>=0 && texto.charAt(fin) != ' ') {
                                fin--;
                                retroceso++;
                            }
                        }
                        if(ini>=fin){
                            fin = ini + 40;
                        }
                        String linea = texto.substring(ini, fin).trim();
                        retroceso = 40 - linea.length();
                        if (justificar && retroceso > 0) {
                            int index = 0;
                            int index2 = 0;
                            String espacio = " ";
                            while (retroceso > 0) {
                                index2 = linea.indexOf(espacio, index);
                                if (index2 > 0) {
                                    linea = linea.substring(0, index2) + " " + linea.substring(index2);
                                    index = index2 + 2;
                                    retroceso--;
                                }
                                else if (index > 0) {
                                    index = 0;
                                    espacio += " ";
                                }
                                else {
                                    break;
                                }
                            }
                        }
                        lineas.add(linea);
                        ini = fin + 1;
                    }
                    else {
                        lineas.add(texto.substring(ini));
                        break;
                    }
                }
            }
        }
    }

    public List<String> getLineas() {
        return lineas;
    }

    public void setLineas(List<String> lineas) {
        this.lineas = lineas;
    }

    public String getTextoOriginal() {
        return textoOriginal;
    }
}
