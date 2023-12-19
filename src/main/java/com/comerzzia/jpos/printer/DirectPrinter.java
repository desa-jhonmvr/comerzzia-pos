/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.printer;

import static com.comerzzia.jpos.printer.DeviceTicket.getWhiteString;
import com.comerzzia.jpos.servicios.login.Sesion;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.UUID;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Sides;

/**
 *
 * @author MGRI
 */
public class DirectPrinter {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DirectPrinter.class.getName());

    /**
     * Imprime el array de bits
     *
     * @param streamOut
     * @throws PrintException
     */
    public void imprimirDocumento(ByteArrayOutputStream streamOut, String nombreImpresora) throws PrintException, FileNotFoundException {

        log.debug("imprimirDocumento() - Imprimiendo por la impresora: "+nombreImpresora);
        log.trace("file.encoding=" + System.getProperty("file.encoding"));
        log.trace("Default Charset=" + Charset.defaultCharset());

        String cadenaLeida = "";
        try {
            //IMprimimos la cadena leida
            cadenaLeida = streamOut.toString("Windows-1252");
            log.trace("CADENA LEIDA:");
            log.trace(cadenaLeida);
        }
        catch (UnsupportedEncodingException ex) {
            log.error("imprimirDocumento() - Encoding no válido");
        }

        ByteArrayInputStream streamIn = null;
        try {
            streamIn = new ByteArrayInputStream(cadenaLeida.getBytes("IBM850"));
        }
        catch (UnsupportedEncodingException ex) {
            log.error("imprimirDocumento() - Encoding no soportado por impresora, ex");
        }

        DocFlavor myFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;

        // Creamos el documento
        Doc myDoc = new SimpleDoc(streamIn, myFormat, null);

        // Preparamos los atributos
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(new Copies(1));
        //aset.add(MediaSize.ISO.A4); // Útil para impresión de informes
        aset.add(Sides.ONE_SIDED);
        // Localizamos la impresora por defecto
         
        PrintService service = null;
        if (nombreImpresora.equals("DEFECTO")){
             service = PrintServiceLookup.lookupDefaultPrintService();
        }
        else{
            //TODO: Si esta búsqueda es temporalmente costosa, habría que realizarla una sola vez
            PrintService[] printServices = PrinterJob.lookupPrintServices();
            for (PrintService printService : printServices) {
                if (printService.getName().equals(nombreImpresora)){
                    service = printService;
                }                
            }
            if (service == null){
                log.error("imprimirDocumento() - No se ha encontrado la impresora con nombre + "+nombreImpresora);
                throw new PrintException();
            }
        }
        
        //Lista las opciones de impresión disponibles
        //DocFlavor[] flavors = service.getSupportedDocFlavors();
        
        // DESCOMENTAR EN PRODUCCIÓN PARA COMPROBAR SI LA IMPRESORA IMPRIME EN EL FORMATO REQUERIDO
        //for (DocFlavor flav : flavors) {
        //    log.info(flav.toString());
        //}
        
        // Creamos el trabajo de impresión
        log.debug("imprimirDocumento() - Añadiendo trabajo a la cola de impresión ");
        Sesion.putTiempoEjecucion("inicioColaImpresion", System.currentTimeMillis());
        String impresoraTermica = Sesion.getDatosConfiguracion().getImpresoraTermica();
        log.info("impresoraTermica " + impresoraTermica);
        if(impresoraTermica == null || impresoraTermica.equals("2")){
            if (service != null) {
                DocPrintJob job = service.createPrintJob();
                job.print(myDoc, aset);
            }
        }else{
            //Para impresoras termicas puerto USB
            
            BufferedWriter bw = null;
            FileWriter fw = null;
            String nombreArchivoImpresion = Sesion.getDatosConfiguracion().getRutaArchivosImpresoraTermica() + UUID.randomUUID();
            File  f = null;
            try {
               f = new File(nombreArchivoImpresion);
               if(f.exists()){
                   f.delete();
               }
               fw = new FileWriter(nombreArchivoImpresion);
               bw = new BufferedWriter(fw);
               bw.write(cadenaLeida.toString());
            } catch (IOException e) {
               log.error("No se generar el archivo ", e);
             }finally {
                try {
                    if (bw != null)
                            bw.close();
                    if (fw != null)
                            fw.close();
                } catch (IOException ex) {
                    log.error("No se generar el archivo ", ex);
                }
            }

         try {
                Process process1 = Runtime.getRuntime().exec("iconv  -t IBM850 " + nombreArchivoImpresion + " -o " + nombreArchivoImpresion + ".txt");
                BufferedReader in = new BufferedReader(new InputStreamReader(process1.getInputStream()));
                process1.waitFor();
                in.close();
                Thread.sleep(500);
                Process process = Runtime.getRuntime().exec(" lp -d " + nombreImpresora + " " + nombreArchivoImpresion + ".txt");
                process.waitFor();
                log.info("Imprimiendo: " + Sesion.getDatosConfiguracion().getCadenaImpresoraTicket() + " / " +Sesion.getDatosConfiguracion().getTipoImpresionTicket() );
                /*if(f.exists()){
                   f.delete();
                }*/
             } catch (Exception ex) {
                log.error("No se pudo imprimir ", ex);
             }
        }
        Sesion.putTiempoEjecucion("finColaImpresion", System.currentTimeMillis());
        log.debug("imprimirDocumento() - Documento añadido a la cola de impresión");
        log.debug("imprimirDocumento() - En añadir a la cola de impresión ha tardado: " + Sesion.getDameTiempo("inicioColaImpresion", "finColaImpresion")+ " segundos");
    }

    /* FUNCIONES AUXILIARES */
    public static String alignLeft(String sLine, int iSize) {
        if (sLine.length() > iSize) {
            return sLine.substring(0, iSize);
        }
        else {
            return sLine + getWhiteString(iSize - sLine.length());
        }
    }

    public static String alignRight(String sLine, int iSize) {
        if (sLine.length() > iSize) {
            return sLine.substring(sLine.length() - iSize);
        }
        else {
            return getWhiteString(iSize - sLine.length()) + sLine;
        }
    }

    public static String alignCenter(String sLine, int iSize) {
        if (sLine.length() > iSize) {
            return alignRight(sLine.substring(0, (sLine.length() + iSize) / 2), iSize);
        }
        else {
            return alignRight(sLine + getWhiteString((iSize - sLine.length()) / 2), iSize);
        }
    }

    public static String alignCenter(String sLine) {
        return alignCenter(sLine, 42);
    }

}
