/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.gui;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 *
 * @author CONTABILIDAD
 */
public class Modulo11 {
    public String invertirCadena(String cadena) {
        String cadenaInvertida = "";
        for (int x = cadena.length() - 1; x >= 0; x--) {
            cadenaInvertida = cadenaInvertida + cadena.charAt(x);
        }
        return cadenaInvertida;
    }
 
    public int obtenerSumaPorDigitos(String cadena) {
        int pivote = 2;
        int longitudCadena = cadena.length();
        int cantidadTotal = 0;
        int b = 1;
        for (int i = 0; i < longitudCadena; i++) {
            if (pivote == 8) {
                pivote = 2;
            }
            int temporal = Integer.parseInt("" + cadena.substring(i, b));
            b++;
            temporal *= pivote;
            pivote++;
            cantidadTotal += temporal;
        }
        cantidadTotal = 11 - cantidadTotal % 11;
        return cantidadTotal;
    }
    
       public int generaDigitoModulo11(String cadena) {

        //indica en que elemento se reinicia el factor de multiplicacion
        int baseMultiplicador = 7;
        System.out.println("CADENA-->" + cadena);
        int[] aux = new int[cadena.length()];
        int multiplicador = 2;
        int total = 0;
        int verificador = 0;
        for (int i = aux.length - 1; i >= 0; i--) {
            aux[i] = Integer.parseInt("" + cadena.charAt(i));
            aux[i] = aux[i] * multiplicador;
            multiplicador++;
            if (multiplicador > baseMultiplicador) {
                multiplicador = 2;
            }
            total += aux[i];
        }

        if (total == 0 || total == 1) {
            verificador = 0;
        } else {
            verificador = (11 - (total % 11)) == 11 ? 0 : (11 - (total % 11));
        }
        
        if (verificador == 10){
            verificador = 1; 
        }
        
        return verificador;
    }
 
    public static void main(String [] args){
       /* Modulo11 a = new Modulo11();
        try {
            a.enviarTrama();
            //System.out.println(a.generaDigitoModulo11("121120180117907461190012020099000002077179074611"));
        } catch (IOException ex) {
            Logger.getLogger(Modulo11.class.getName()).log(Level.SEVERE, null, ex);
        }*/
       
       /*int a[][] = new int[1][];
       int b[][] = new int[][];
       int c[][] = {{1,2} , {1,2}, {2,3}, {2}};
       int d[][] = {1,2,2};
       int e[][] = new int[1][2];
       int f[][] = new int[][]  {{1,2} , {1,2}, {2,3}, {2}};
       int g[][] = new int[][]  {null , {1,2}, {2,3}, {2}};*/
    }
    
    
      public String enviarTrama() throws SocketTimeoutException, IOException {
        String mensajeRecibido = null;
        DataInputStream entrada;
        DataOutputStream salida;
        byte buffer[] = new byte[512];
    
        Socket socket = null;
       
            //El formato de la cadena es host:puerto
            //for(int i = 0; i<20; i++){
                 try {
                socket = new Socket("192.168.36.25", 6019);
           //      System.out.println("CADENA-->" + i);
                 } catch (Exception ex) {
                    ex.printStackTrace();
                  //  continue;
                 }finally{
                                  }
       //     }
            if (socket != null) {
                socket.setSoTimeout(100*1000); //Pasamos el TIMEOUT a milisegundos
              // String msg = "10`00000200004000212200011520476004=1219000000000000000000000000000010045033115481820190220012000000000200120012050000000962840                120100             00000000";
                //String msg = "10`00000200314000212200011520476004=2004000000000000000000000000000000000000016205020190415022000000000000000000000000000962840                120100             00000000";
                String msg = "10`00000200314000212200011399690008=2004000000000000000000000000000000000000011492320190417022000000000000000000000000000968840                120100             00000000";
                entrada = new DataInputStream(socket.getInputStream());
                salida = new DataOutputStream(socket.getOutputStream());
                salida.write(msg.getBytes());
                
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int s;
                do {
                    s = entrada.read(buffer); //Leemos la respuesta del servidor (entrada)
                    if (s != -1) {
                        baos.write(buffer, baos.size(), s);
                    }
                } while (s >= buffer.length);
                
                byte result[] = baos.toByteArray();
                mensajeRecibido = new String(result, "UTF-8");
                mensajeRecibido = mensajeRecibido.substring(7);
                
                socket.close();
            }

        
       
        return mensajeRecibido;
    }
}
