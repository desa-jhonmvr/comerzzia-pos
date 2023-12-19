package com.comerzzia.jpos.printer.escpos;

import com.comerzzia.jpos.printer.DevicePrinter;
import com.comerzzia.jpos.printer.TicketPrinterException;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.print.PrintServices;
import es.mpsistemas.util.log.Logger;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class DevicePrinterESCPOS implements DevicePrinter {
    
    private static final Logger log = Logger.getMLogger(DevicePrinterESCPOS.class);

    private PrinterWritter m_CommOutputPrinter;
    private Codes m_codes;
    private UnicodeTranslator m_trans;
    // private boolean m_bInline;
    private String m_sName;

    // Creates new TicketPrinter
    public DevicePrinterESCPOS(PrinterWritter CommOutputPrinter, Codes codes,
            UnicodeTranslator trans) throws TicketPrinterException {
        
        log.info("Debug Impresion - DevicePrinterESCPOS()");
        
        m_sName = "PrinterSerial";
        m_CommOutputPrinter = CommOutputPrinter;
        m_codes = codes;
        m_trans = trans;

        // Inicializamos la impresora
        m_CommOutputPrinter.init(ESCPOS.INIT);

        m_CommOutputPrinter.write(ESCPOS.SELECT_PRINTER); // A la impresora
        m_CommOutputPrinter.init(m_codes.getInitSequence());
        m_CommOutputPrinter.write(m_trans.getCodeTable());

        m_CommOutputPrinter.flush();
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
    }

    public void printImage(BufferedImage image) {
        log.info("Debug Impresion - printImage()");
        m_CommOutputPrinter.write(ESCPOS.SELECT_PRINTER);
        m_CommOutputPrinter.write(m_codes.transImage(image));
    }

    public void printBarCode(String type, String position, String code) {
        log.info("Debug Impresion - printBarCode()");
        m_CommOutputPrinter.write(ESCPOS.SELECT_PRINTER);
        m_codes.printBarcode(m_CommOutputPrinter, type, position, code);
    }

    public void beginLine(int iTextSize) {
        log.info("Debug Impresion - beguinLine()");
        m_CommOutputPrinter.write(ESCPOS.SELECT_PRINTER);
        if (!Sesion.isSukasa()) {
            m_CommOutputPrinter.write(new byte[]{0x1b, 0x33, 0x0F});
        }

        if (iTextSize == DevicePrinter.SIZE_0) {
            m_CommOutputPrinter.write(m_codes.getSize0());
        }
        else if (iTextSize == DevicePrinter.SIZE_1) {
            m_CommOutputPrinter.write(m_codes.getSize1());
        }
        else if (iTextSize == DevicePrinter.SIZE_2) {
            m_CommOutputPrinter.write(m_codes.getSize2());
        }
        else if (iTextSize == DevicePrinter.SIZE_3) {
            m_CommOutputPrinter.write(m_codes.getSize3());
        }
        else {
            m_CommOutputPrinter.write(m_codes.getSize0());
        }
    }

    public void printText(int iStyle, String sText) {
        log.info("Debug Impresion - printText()");
        m_CommOutputPrinter.write(ESCPOS.SELECT_PRINTER);

        if ((iStyle & DevicePrinter.STYLE_BOLD) != 0) {
            m_CommOutputPrinter.write(m_codes.getBoldSet());
        }
        if ((iStyle & DevicePrinter.STYLE_UNDERLINE) != 0) {
            m_CommOutputPrinter.write(m_codes.getUnderlineSet());
        }
        m_CommOutputPrinter.write(m_trans.transString(sText));
        if ((iStyle & DevicePrinter.STYLE_UNDERLINE) != 0) {
            m_CommOutputPrinter.write(m_codes.getUnderlineReset());
        }
        if ((iStyle & DevicePrinter.STYLE_BOLD) != 0) {
            m_CommOutputPrinter.write(m_codes.getBoldReset());
        }
    }

    public void endLine() {
        log.info("Debug Impresion - endLine()");
        m_CommOutputPrinter.write(ESCPOS.SELECT_PRINTER);
        m_CommOutputPrinter.write(m_codes.getNewLine());
    }

    public void endReceipt(int impresora) {
        log.info("Debug Impresion - endReceipt()");
        m_CommOutputPrinter.write(ESCPOS.SELECT_PRINTER);

        m_CommOutputPrinter.write(m_codes.getNewLine());
        m_CommOutputPrinter.write(m_codes.getNewLine());
        m_CommOutputPrinter.write(m_codes.getNewLine());
        m_CommOutputPrinter.write(m_codes.getNewLine());
        m_CommOutputPrinter.write(m_codes.getNewLine());

        m_CommOutputPrinter.write(m_codes.getCutReceipt());
        m_CommOutputPrinter.flush();
        m_CommOutputPrinter.close();
    }

    public void openDrawer() {
        log.info("Debug Impresion - openDrawer()");
        m_CommOutputPrinter.write(ESCPOS.SELECT_PRINTER);
        m_CommOutputPrinter.write(m_codes.getOpenDrawer());
        m_CommOutputPrinter.flush();
        m_CommOutputPrinter.close();
    }

    public void close() {
        log.info("Debug Impresion - close()");
        m_CommOutputPrinter.internalClose();
    }
    
    public void seleccionarImpresora(String tipo) {
        log.info("Debug Impresion - SeleccionarImpresora()");
        if(tipo.equals(PrintServices.SLIP)){
            int i1=Integer.parseInt("1B",16);
            char aux1=(char)i1;
            int i2=Integer.parseInt("4",10);
            char aux2=(char)i2;
            String slipOn = aux1+"c0"+aux2;        
            m_CommOutputPrinter.write(slipOn.getBytes());            
        } else if(tipo.equals(PrintServices.RECEIPT)){
            int i1=Integer.parseInt("1B",16);
            char aux1=(char)i1;
            int i2=Integer.parseInt("2",10);
            char aux2=(char)i2;
            String receiptOn = aux1+"c0"+aux2;        
            m_CommOutputPrinter.write(receiptOn.getBytes());               
        } else if(tipo.equals(PrintServices.JOURNAL)){
            int i1=Integer.parseInt("1B",16);
            char aux1=(char)i1;
            int i2=Integer.parseInt("1",10);
            char aux2=(char)i2;
            String journalOn = aux1+"c0"+aux2;        
            m_CommOutputPrinter.write(journalOn.getBytes());               
        } else{
            int i1=Integer.parseInt("1B",16);
            char aux1=(char)i1;
            int i2=Integer.parseInt("2",10);
            char aux2=(char)i2;
            String otherOn = aux1+"c0"+aux2;        
            m_CommOutputPrinter.write(otherOn.getBytes());               
        }
        
        // Si usamos como i2 = 4, se imprimirá tanto en journal como en receipt
    }
    
    public void seleccionaInterlineado(int pulgadas) {
        log.info("Debug Impresion - SeleccionarInterlineado()");
        int i=Integer.parseInt("1B",16);
        char esc=(char)i;        
        if(pulgadas==0){
            String defecto = esc+"2";
            m_CommOutputPrinter.write(defecto.getBytes());   
        } else {
            char pulg =(char)pulgadas;
            String interLineado=esc+"3"+pulg;
            m_CommOutputPrinter.write(interLineado.getBytes());   
        }
    }
    
    public void cleaning() {
        log.info("Debug Impresion - cleaning()");
        int i=Integer.parseInt("10",16);
        char dle=(char)i;         
        int j=Integer.parseInt("05",16);
        char enq=(char)j;
        String cleaning = dle+enq+"3";
        m_CommOutputPrinter.write(cleaning.getBytes());  
    }
}
