package com.comerzzia.jpos.printer.escpos;

import com.comerzzia.jpos.printer.DevicePrinter;
import com.comerzzia.jpos.printer.DirectPrinter;
import com.comerzzia.jpos.printer.TicketPrinterException;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.print.PrintServices;
import es.mpsistemas.util.log.Logger;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.print.PrintException;

import javax.swing.JComponent;

public class DevicePrinterESCPOSDriver implements DevicePrinter {

    private static final Logger log = Logger.getMLogger(DevicePrinterESCPOSDriver.class);

    String nombreImpresora;
    private String impresora1, impresora2;
    
    ByteArrayOutputStream streamOut;

    private Codes m_codes;
    private UnicodeTranslator m_trans;
    // private boolean m_bInline;
    private String m_sName;
    String impresoraTermica;

    // Creates new TicketPrinter
    public DevicePrinterESCPOSDriver(String impresora1, String impresora2, Codes codes,
            UnicodeTranslator trans) throws TicketPrinterException {
        impresoraTermica = Sesion.getDatosConfiguracion().getImpresoraTermica() == null ? "0": Sesion.getDatosConfiguracion().getImpresoraTermica();
        log.info("Debug Impresion - DevicePrinterESCPOSDriver()");

        m_sName = "PrinterSerial";

        m_codes = codes;
        m_trans = trans;

        if (nombreImpresora == null) {
            nombreImpresora = "DEFECTO";
        }

        streamOut = new ByteArrayOutputStream();

        if(impresoraTermica.equals("0")){
        // Inicializamos la impresora
            ejecutaComando(ESCPOS.INIT);
            // Encoding
            ejecutaComando("27;116;2");
            //ejecutaComando("27;82;11");

            ejecutaComando(ESCPOS.SELECT_PRINTER); // A la impresora
        }

        this.impresora1 = impresora1; 
        this.impresora2 = impresora2; 
    }

    public String getPrinterName() {
        return m_sName;
    }

    public String getPrinterDescription() {
        return null;
    }

    public JComponent getPrinterComponent() {
        return null;
    }

    public void reset() {
    }

    public void beginReceipt() {
        streamOut = new ByteArrayOutputStream();
        if(impresoraTermica.equals("0")){
            // Inicializamos la impresora
            ejecutaComando(ESCPOS.INIT);
            // Encoding
            ejecutaComando("27;116;2"); // 27;116;16 charset moderno
            //ejecutaComando("27;82;11");

            ejecutaComando(ESCPOS.SELECT_PRINTER); // A la impresora
        }
    }

    public void printImage(BufferedImage image) {
        //log.trace("Debug Impresion - printImage()");
       // ejecutaComando(ESCPOS.SELECT_PRINTER);
       // ejecutaComando(m_codes.transImage(image));
    }

    public void printBarCode(String type, String position, String code) {
        //log.trace("Debug Impresion - printBarCode()");               
        imprimirCodigoBarras(code, type, position, 0);
    }

    public void beginLine(int iTextSize) {
        //log.trace("Debug Impresion - beguinLine()");
        if(impresoraTermica.equals("0")){
          ejecutaComando(ESCPOS.SELECT_PRINTER);
        }
        if (!Sesion.isSukasa()) {
            ejecutaComando(new byte[]{0x1b, 0x33, 0x0F});
        }

        if (iTextSize == DevicePrinter.SIZE_0) {
            ejecutaComando(m_codes.getSize0());
        }
        else if (iTextSize == DevicePrinter.SIZE_1) {
            ejecutaComando(m_codes.getSize1());
        }
        else if (iTextSize == DevicePrinter.SIZE_2) {
            ejecutaComando(m_codes.getSize2());
        }
        else if (iTextSize == DevicePrinter.SIZE_3) {
            ejecutaComando(m_codes.getSize3());
        }
        else {
            ejecutaComando(m_codes.getSize0());
        }
    }

    public void printText(int iStyle, String sText) {
        //log.trace("Debug Impresion - printText()");
        ejecutaComando(ESCPOS.SELECT_PRINTER);

        if ((iStyle & DevicePrinter.STYLE_BOLD) != 0) {
            ejecutaComando(m_codes.getBoldSet());
        }
        if ((iStyle & DevicePrinter.STYLE_UNDERLINE) != 0) {
            ejecutaComando(m_codes.getUnderlineSet());
        }
        try {
            //ejecutaComando(m_trans.transString(sText));
            ejecutaComando(sText.getBytes("Windows-1252"));
        }
        catch (UnsupportedEncodingException ex) {
           log.error("printText() - ERROR codificando cadena como Windows-1252 : "+ sText + " [MSG]:"+ex.getMessage(),ex);
        }
        
        
        if ((iStyle & DevicePrinter.STYLE_UNDERLINE) != 0) {
            ejecutaComando(m_codes.getUnderlineReset());
        }
        if ((iStyle & DevicePrinter.STYLE_BOLD) != 0) {
            ejecutaComando(m_codes.getBoldReset());
    }
    }

    public void endLine() {
        //log.trace("Debug Impresion - endLine()");
        if(impresoraTermica.equals("0")){
            ejecutaComando(ESCPOS.SELECT_PRINTER);
        }
        ejecutaComando(m_codes.getNewLine());
    }

    public void endReceipt(int impresora) {
        //log.trace("Debug Impresion - endReceipt()");
        ejecutaComando(ESCPOS.SELECT_PRINTER);

        ejecutaComando(m_codes.getNewLine());
        ejecutaComando(m_codes.getNewLine());
        ejecutaComando(m_codes.getNewLine());
        ejecutaComando(m_codes.getNewLine());
        ejecutaComando(m_codes.getNewLine());
        ejecutaComando(m_codes.getNewLine());
        ejecutaComando(m_codes.getCutReceipt());


        imprimirTicket(impresora);

    }

    public void openDrawer() {
        //log.trace("Debug Impresion - openDrawer()");
        ejecutaComandoUnitario("27;112;0;26;300"); // EPSON TM-T20. Funciona en TM-U950/U950P
        //ejecutaComandoUnitario("27;112;0;50;450");   // EPSON TM-U950/U950P
    }

    public void close() {
        //log.trace("Debug Impresion - close()");
        streamOut.reset();
    }

    /**
     * Al seleccionar impresora, escribimos en el ticket que imprimiremos, pero prepararemos la impresora también para la impresión en modo SLIP o para cambiar de modo
     * @param tipo 
     */
    public void seleccionarImpresora(String tipo) {
        //log.trace("Debug Impresion - SeleccionarImpresora()");
        if(impresoraTermica.equals("0")){
            if (tipo.equals(PrintServices.SLIP)) {
                int i1 = Integer.parseInt("1B", 16);
                char aux1 = (char) i1;
                int i2 = Integer.parseInt("4", 10);
                char aux2 = (char) i2;
                String slipOn = aux1 + "c0" + aux2;
                ejecutaComando(slipOn.getBytes());
                //ejecutaComandoUnitario(slipOn.getBytes());
            }
            else if (tipo.equals(PrintServices.RECEIPT)) {
                int i1 = Integer.parseInt("1B", 16);
                char aux1 = (char) i1;
                int i2 = Integer.parseInt("2", 10);
                char aux2 = (char) i2;
                String receiptOn = aux1 + "c0" + aux2;
                ejecutaComando(receiptOn.getBytes());
                //ejecutaComandoUnitario(receiptOn.getBytes());
            }
            else if (tipo.equals(PrintServices.JOURNAL)) {
                int i1 = Integer.parseInt("1B", 16);
                char aux1 = (char) i1;
                int i2 = Integer.parseInt("1", 10);
                char aux2 = (char) i2;
                String journalOn = aux1 + "c0" + aux2;
                ejecutaComando(journalOn.getBytes());
                //ejecutaComandoUnitario(journalOn.getBytes());
            }
            else {
                int i1 = Integer.parseInt("1B", 16);
                char aux1 = (char) i1;
                int i2 = Integer.parseInt("2", 10);
                char aux2 = (char) i2;
                String otherOn = aux1 + "c0" + aux2;
                ejecutaComando(otherOn.getBytes());
                //ejecutaComandoUnitario(otherOn.getBytes());
            }
        }
        //necesitamos imprimir el Buffer en la impresora     
        // Si usamos como i2 = 4, se imprimirá tanto en journal como en receipt
    }

    public void seleccionaInterlineado(int pulgadas) {
        //log.trace("Debug Impresion - SeleccionarInterlineado()");
        int i = Integer.parseInt("1B", 16);
        char esc = (char) i;
        if (pulgadas == 0) {
            String defecto = esc + "2";
            ejecutaComando(defecto.getBytes());
        }
        else {
            char pulg = (char) pulgadas;
            String interLineado = esc + "3" + pulg;
            ejecutaComando(interLineado.getBytes());
        }
    }

    public void cleaning() {
        //log.trace("Debug Impresion - cleaning()");
        int i = Integer.parseInt("10", 16);
        char dle = (char) i;
        int j = Integer.parseInt("05", 16);
        char enq = (char) j;
        String cleaning = dle + enq + "3";
        ejecutaComando(cleaning.getBytes());
    }

    private void imprimirTicket(int impresora) {
         
        try {
            if (impresora == 2){
                DirectPrinter directPrinter = new DirectPrinter();
                directPrinter.imprimirDocumento(streamOut, impresora2);
            }
            else{
                 DirectPrinter directPrinter = new DirectPrinter();
                directPrinter.imprimirDocumento(streamOut, impresora1);
            }
        }
        catch (IOException ex) {
            log.error("printTicket() - Error escribiendo ticket para imprimir -" + streamOut);
        }
        catch (PrintException ex) {
            log.error("printTicket() - Error escribiendo ticket para imprimir -" + streamOut);
        }
    }
    
    /**
     * Manda a la impresora el comando recibido. Puesto que el comando llegará a la cola de impresión.
     * @param comando 
     */
     private void imprimirComando(ByteArrayOutputStream comando) {        
        try {
            DirectPrinter directPrinter = new DirectPrinter();
            directPrinter.imprimirDocumento(comando, impresora1);
        }
        catch (IOException ex) {
            log.error("printTicket() - Error escribiendo ticket para imprimir -" + streamOut);
        }
        catch (PrintException ex) {
            log.error("printTicket() - Error escribiendo ticket para imprimir -" + streamOut);
        }
    }

    private void ejecutaComando(String comando) {
        try {
            if (comando != null && !comando.isEmpty()) {
                String[] split = comando.split(";");
                for (String parteComando : split) {
                    Integer parteComandoInt = new Integer(parteComando);
                    streamOut.write((char) parteComandoInt.intValue());
                }
            }
        }
        catch (Exception e) {
            log.error("ejecutaComando()- Error ejecutando comando - " + comando);
        }
    }

    // Añade un elemento o comando a la salida del documento
    private void ejecutaComando(byte[] comando) {
        try {
            if (comando != null && comando.length > 0) {
                for (byte parteComando : comando) {
                    streamOut.write(parteComando);
                }
            }
        }
        catch (Exception e) {
            log.error("ejecutaComando()- Error ejecutando comando - " + comando);
        }
    }
    
    
    private void ejecutaComandoUnitario(String comando) {
        ByteArrayOutputStream streamComando = new ByteArrayOutputStream();
        try {
            if (comando != null && !comando.isEmpty()) {
                String[] split = comando.split(";");
                for (String parteComando : split) {
                    Integer parteComandoInt = new Integer(parteComando);
                    streamComando.write((char) parteComandoInt.intValue());
                }
                
                imprimirComando(streamComando);
            }
        }
        catch (Exception e) {
            log.error("ejecutaComando()- Error ejecutando comando - " + comando);
        }
    }
    
    // Función que ejecuta un comando pero no lo imprime en el streamOut que usaremos para imprimir el documento
    private ByteArrayOutputStream ejecutaComandoUnitario(byte[] comando) { 
        ByteArrayOutputStream streamComando = new ByteArrayOutputStream();
        addToStream(comando, streamComando);
           
        imprimirComando(streamComando);
        return streamComando;
    }
    
    private ByteArrayOutputStream addToStream(byte[] comando, ByteArrayOutputStream streamComando){        
        try {
            if (comando != null && comando.length > 0) {
                for (byte parteComando : comando) {
                    streamComando.write(parteComando);
                }
            }
        }
        catch (Exception e) {
            log.error("ejecutaComando()- Error ejecutando comando - " + comando);
        } 
        return streamComando;
    }
    
    public void imprimirCodigoBarras(String codigoBarras, String tipo, String alineacion, int tipoLeyendaNumericaCodBar) {
        try {
            // Alineación del código de barras
            if (alineacion != null && !alineacion.isEmpty()) {
                if (alineacion.equals("center")) {
                    ejecutaComando("27;97;1"); //codigosImpresora.getBarCodeAlignCenter());
                }
                else if (alineacion.equals("left")){
                    ejecutaComando("27;97;0");//ejecutaComando(codigosImpresora.getBarCodeAlignLeft());
                }
                else if (alineacion.equals("right")){
                    ejecutaComando("27;97;2");//ejecutaComando(codigosImpresora.getBarCodeAlignRight());
                    
                }else{
                    ejecutaComando("27;97;0"); //ejecutaComando(codigosImpresora.getBarCodeAlignLeft());                       
                }
            }


                String posicion = DevicePrinter.POSITION_NONE; // Sacar a xml
                String tipoCodigoBarras = DevicePrinter.BARCODE_EAN13; // 128 es el otro

                ejecutaComando("13;10"); //codigosImpresora.getNewLine());

                // Imprimimos la secuencia de selección de código de barras
                //TODO: Sacamos a xml de configuración
                ejecutaComando("27;61;1"); //codigosImpresora.getSelectPrinter());

                ejecutaComando("29;104;96"); //codigosImpresora.getBarHeight());

                ejecutaComando("29;119;2"); //codigosImpresora.getBarWidth());

                if (DevicePrinter.POSITION_NONE.equals(posicion)) {
                    ejecutaComando("29;72;0"); //codigosImpresora.getBarPositionNone());
                }
                else {
                    ejecutaComando("29;72;2"); //codigosImpresora.getBarPositionDown());
                }
                ejecutaComando("29;102;1"); //codigosImpresora.getBarHRFont());
                streamOut.write(ESCPOS.BAR_CODE07);

                String encoded = "{B" + codigoBarras;
              
                // Configuramos el tipo de ´numeración que aparecerá en el código de barras
                ejecutaComando("29;72"+";"+ tipoLeyendaNumericaCodBar);                
                
                ejecutaComando("29;107;73"); //codigosImpresora.getBarCodeDataLength());
                streamOut.write((byte) encoded.getBytes().length);

                // Imprimimos el código de barras
                streamOut.write(encoded.getBytes());

                ejecutaComando("13;10"); //codigosImpresora.getNewLine());            

            // Quitamos la alineación a la derecha del ticket
            // "27;97;0" codigosImpresora.getBarCodeAlignLeft();
        }
        catch (IOException ex) {
            log.error("imprimirCodigoBarras() - Error imprimiendo código de barras :" + ex.getMessage(), ex);
        }

    }
}
