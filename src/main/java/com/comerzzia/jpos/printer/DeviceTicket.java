package com.comerzzia.jpos.printer;

import com.comerzzia.jpos.printer.escpos.CodesSureMark;
import com.comerzzia.jpos.printer.escpos.CodesEpson;
import com.comerzzia.jpos.printer.escpos.CodesTMU220;
import com.comerzzia.jpos.printer.escpos.CodesHybrid;
import com.comerzzia.jpos.printer.escpos.DevicePrinterESCPOS;
import com.comerzzia.jpos.printer.escpos.DevicePrinterESCPOSDriver;
import com.comerzzia.jpos.printer.escpos.DevicePrinterHybridESCPOS;
import com.comerzzia.jpos.printer.escpos.DevicePrinterPlain;
import com.comerzzia.jpos.printer.escpos.PrinterWritter;
import com.comerzzia.jpos.printer.escpos.PrinterWritterFile;
import com.comerzzia.jpos.printer.escpos.PrinterWritterRXTX;
import com.comerzzia.jpos.printer.escpos.PrinterWritterUsb;
import com.comerzzia.jpos.printer.escpos.UnicodeTranslatorInt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comerzzia.jpos.printer.javapos.DevicePrinterJavaPOS;
import com.comerzzia.jpos.printer.printer.DevicePrinterPrinter;
import com.comerzzia.jpos.printer.screen.DeviceDisplayPanel;
import com.comerzzia.jpos.printer.screen.DeviceDisplayWindow;
import com.comerzzia.jpos.printer.screen.DevicePrinterPanel;
import com.comerzzia.jpos.util.StringParser;
import es.mpsistemas.util.log.Logger;
import java.awt.Component;

public class DeviceTicket {

    private static final Logger log = Logger.getMLogger(DeviceTicket.class);
    private DeviceDisplay m_devicedisplay;
    private DevicePrinter m_nullprinter;
    private Map<String, DevicePrinter> m_deviceprinters;
    private List<DevicePrinter> m_deviceprinterslist;

    /** Creates a new instance of DeviceTicket */
    public DeviceTicket() {
        // Una impresora solo de pantalla.

        m_nullprinter = new DevicePrinterNull();
        m_deviceprinters = new HashMap<String, DevicePrinter>();
        m_deviceprinterslist = new ArrayList<DevicePrinter>();
        m_devicedisplay = new DeviceDisplayNull();
        DevicePrinter p = new DevicePrinterPanel();
        m_deviceprinters.put("1", p);
        m_deviceprinterslist.add(p);
    }

    public DeviceTicket(Component parent) {

        String sDisplayType = "window";

        try {
            if ("screen".equals(sDisplayType)) {
                m_devicedisplay = new DeviceDisplayPanel();
            } else if ("window".equals(sDisplayType)) {
                m_devicedisplay = new DeviceDisplayWindow();
            } else {
                m_devicedisplay = new DeviceDisplayNull();
            }
        } catch (Exception e) {
            m_devicedisplay = new DeviceDisplayNull(e.getMessage());
        }

        m_nullprinter = new DevicePrinterNull();
        m_deviceprinters = new HashMap<String, DevicePrinter>();
        m_deviceprinterslist = new ArrayList<DevicePrinter>();

        // Empezamos a iterar por las impresoras...
        int iPrinterIndex = 1;
        String sPrinterIndex = Integer.toString(iPrinterIndex);
        String sprinter = "window";

        while (sprinter != null && !"".equals(sprinter)) {

            StringParser sp = new StringParser(sprinter);
            String sPrinterType = sp.nextToken(':');
            String sPrinterParam1 = sp.nextToken(',');
            String sPrinterParam2 = sp.nextToken(',');

            try {
                if ("screen".equals(sPrinterType)) {
                    addPrinter(sPrinterIndex, new DevicePrinterPanel());
                } else if ("printer".equals(sPrinterType)) {
                    addPrinter(sPrinterIndex, new DevicePrinterPrinter(
                            sPrinterParam1, 10, 287, 15, 297, "A4"));
                }
            } catch (Exception e) {
            }
            // siguiente impresora...
            iPrinterIndex++;
            sPrinterIndex = Integer.toString(iPrinterIndex);
            sprinter = "";
        }
    }

    public DeviceTicket(String type) {
        PrinterWritterPool pws = new PrinterWritterPool();
        m_nullprinter = new DevicePrinterNull();
        m_deviceprinters = new HashMap<String, DevicePrinter>();
        m_deviceprinterslist = new ArrayList<DevicePrinter>();

        // Empezamos a iterar por las impresoras...
        int iPrinterIndex = 1;
        String sPrinterIndex = Integer.toString(iPrinterIndex);
        String sprinter = type;

        StringParser sp = new StringParser(sprinter);
        String sPrinterType = sp.nextToken(':');
        String sPrinterParam1 = sp.nextToken(',');
        String sPrinterParam2 = sp.nextToken(',')+","+sp.nextToken(',');
        try {
            if ("screen".equals(sPrinterType)) {
                addPrinter(sPrinterIndex, new DevicePrinterPanel());
            } else if ("printer".equals(sPrinterType)) {

                // Mejora: añadir tamaños de pagina de forma dinámica
                addPrinter(sPrinterIndex, new DevicePrinterPrinter(
                        sPrinterParam1, 10, 10, 15, 297, "A4"));
            } else if ("epson".equals(sPrinterType)) {

                addPrinter(sPrinterIndex, new DevicePrinterESCPOS(pws.getPrinterWritter(sPrinterParam1, sPrinterParam2),
                        new CodesEpson(), new UnicodeTranslatorInt()));
                
            } else if ("suremark".equals(sPrinterType)) {

                addPrinter(sPrinterIndex, new DevicePrinterESCPOS(pws.getPrinterWritter(sPrinterParam1, sPrinterParam2),
                        new CodesSureMark(), new UnicodeTranslatorInt())); 
            
            }else if ("tmu220".equals(sPrinterType)) {
		PrinterWritter cpw = pws.getPrinterWritter(sPrinterParam1, sPrinterParam2);
                DevicePrinterESCPOS escposdevice = new DevicePrinterESCPOS(cpw,new CodesTMU220(), new UnicodeTranslatorInt());
                addPrinter(sPrinterIndex, escposdevice);

            }else if ("hybrid".equals(sPrinterType)) {
		System.out.println("############## add epson " + sPrinterType+'-'+sPrinterParam1+'-'+sPrinterParam2);
                
                PrinterWritter cpw = pws.getPrinterWritter(sPrinterParam1, sPrinterParam2);
                DevicePrinterHybridESCPOS escposdevice = new DevicePrinterHybridESCPOS(cpw,new CodesHybrid(), new UnicodeTranslatorInt());
                addPrinter(sPrinterIndex, escposdevice);

            }else if ("plain".equals(sPrinterType)) {

                addPrinter(sPrinterIndex, new DevicePrinterPlain(pws.getPrinterWritter(sPrinterParam1, sPrinterParam2)));

            } else if ("javapos".equals(sPrinterType)) {
                addPrinter(sPrinterIndex, new DevicePrinterJavaPOS(
                        sPrinterParam1, sPrinterParam2));

            }
            else if ("driver".equals(sPrinterType)) {
                // La impresora de impresión directa permite utilizar un campo impresora en el campo ticket. 
                //  Un valor de 1 será impresión por la primera impresora cuyo nombre configuramos y un 2 la segunda
                addPrinter(sPrinterIndex, new DevicePrinterESCPOSDriver(sPrinterParam1, sPrinterParam2.replace(",", ""),
                        new CodesEpson(), new UnicodeTranslatorInt()));
            }
        } catch (TicketPrinterException e) {
            // TODO: TICKET - Repasar tratamiento de errores lanzando excepción
            log.error(e);
        }
    }

    private void addPrinter(String sPrinterIndex, DevicePrinter p) {
        m_deviceprinters.put(sPrinterIndex, p);
        m_deviceprinterslist.add(p);
    }

    // Display
    public DeviceDisplay getDeviceDisplay() {
        return m_devicedisplay;
    }

    // Receipt printers
    public DevicePrinter getDevicePrinter(String key) {
        DevicePrinter printer = m_deviceprinters.get(key);
        return printer == null ? m_nullprinter : printer;
    }

    public List<DevicePrinter> getDevicePrinterAll() {
        return m_deviceprinterslist;
    }

    // Utilidades
    public static String getWhiteString(int iSize, char cWhiteChar) {

        char[] cFill = new char[iSize];
        for (int i = 0; i < iSize; i++) {
            cFill[i] = cWhiteChar;
        }
        return new String(cFill);
    }

    public static String getWhiteString(int iSize) {

        return getWhiteString(iSize, ' ');
    }

    public static String alignBarCode(String sLine, int iSize) {

        if (sLine.length() > iSize) {
            return sLine.substring(sLine.length() - iSize);
        } else {
            return getWhiteString(iSize - sLine.length(), '0') + sLine;
        }
    }

    public static String alignLeft(String sLine, int iSize) {

        if (sLine.length() > iSize) {
            return sLine.substring(0, iSize);
        } else {
            return sLine + getWhiteString(iSize - sLine.length());
        }
    }

    public static String alignRight(String sLine, int iSize) {

        if (sLine.length() > iSize) {
            return sLine.substring(sLine.length() - iSize);
        } else {
            return getWhiteString(iSize - sLine.length()) + sLine;
        }
    }

    public static String alignCenter(String sLine, int iSize) {

        if (sLine.length() > iSize) {
            return alignRight(sLine.substring(0, (sLine.length() + iSize) / 2),
                    iSize);
        } else {
            return alignRight(sLine
                    + getWhiteString((iSize - sLine.length()) / 2), iSize);
        }
    }

    public static String alignCenter(String sLine) {
        return alignCenter(sLine, 42);
    }

    public static final byte[] transNumber(String sCad) {

        if (sCad == null) {
            return null;
        } else {
            byte bAux[] = new byte[sCad.length()];
            for (int i = 0; i < sCad.length(); i++) {
                bAux[i] = transNumberChar(sCad.charAt(i));
            }
            return bAux;
        }
    }

    public static byte transNumberChar(char sChar) {
        switch (sChar) {
            case '0':
                return 0x30;
            case '1':
                return 0x31;
            case '2':
                return 0x32;
            case '3':
                return 0x33;
            case '4':
                return 0x34;
            case '5':
                return 0x35;
            case '6':
                return 0x36;
            case '7':
                return 0x37;
            case '8':
                return 0x38;
            case '9':
                return 0x39;
            default:
                return 0x30;
        }
    }

    private static class PrinterWritterPool {

        private Map<String, PrinterWritter> m_apool = new HashMap<String, PrinterWritter>();

        public PrinterWritter getPrinterWritter(String con, String port)
                throws TicketPrinterException {

            String skey = con + "-->" + port;
            
            System.out.println("#################          Creando Impresora:"+con+"-"+port);
            
            
            PrinterWritter pw = (PrinterWritter) m_apool.get(skey);
            if (pw == null) {
                if ("serial".equals(con) || "rxtx".equals(con)) {
                    pw = new PrinterWritterRXTX(port);
                    m_apool.put(skey, pw);
                } 
                else if ("usb".equals(con)) {
                    System.out.println("Creando Impresora Usb:"+port);
                    try{
                    pw = new PrinterWritterUsb(port);
                    }catch(Exception ex){
		      log.error("getPrinterWritter() - " + ex,ex);
                    }
                    m_apool.put(skey, pw);
                }                
                else if ("file".equals(con)) {
                    pw = new PrinterWritterFile(port);
                    m_apool.put(skey, pw);
                } else {
                    throw new TicketPrinterException();
                }
            }
            return pw;
        }
    }
}
