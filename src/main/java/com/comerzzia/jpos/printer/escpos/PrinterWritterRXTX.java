package com.comerzzia.jpos.printer.escpos;

import com.comerzzia.jpos.printer.TicketPrinterException;
import com.comerzzia.jpos.servicios.login.Sesion;
import es.mpsistemas.util.log.Logger;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

public class PrinterWritterRXTX extends PrinterWritter /*
 * implements
 * SerialPortEventListener
 */ {
    private static Logger log = Logger.getMLogger(PrinterWritterRXTX.class);

    private CommPortIdentifier m_PortIdPrinter;
    private CommPort m_CommPortPrinter;
    private String m_sPortPrinter;
    Integer numero;
    private OutputStream m_out;

    /** Creates a new instance of PrinterWritterComm */
    public PrinterWritterRXTX(String sPortPrinter)
            throws TicketPrinterException {
        String[] cadena = sPortPrinter.split(",");
        m_sPortPrinter = cadena[0];
        numero = Integer.parseInt(cadena[1]);
        m_out = null;
    }

    protected void internalWrite(byte[] data) {
        try {
            log.debug("PRINT:: Escribiendo en puerto: " + new String(data));
            if (m_out == null) {
                m_PortIdPrinter = CommPortIdentifier.getPortIdentifier(m_sPortPrinter); // Tomamos el puerto

                m_CommPortPrinter = m_PortIdPrinter.open("PORTID", 2000); // Abrimos el puerto

                m_out = m_CommPortPrinter.getOutputStream(); // Tomamos el chorro de escritura

                if (m_PortIdPrinter.getPortType() == CommPortIdentifier.PORT_SERIAL && numero == 1) {
                    ((SerialPort) m_CommPortPrinter).setSerialPortParams(Sesion.getDatosConfiguracion().getVelocidadTickets(), 
                            Sesion.getDatosConfiguracion().getBitstTickets(), Sesion.getDatosConfiguracion().getStopTickets(),
                            Sesion.getDatosConfiguracion().getParidadTickets()); // Configuramos el puerto

                    // this line prevents the printer tmu220 to stop printing
                    // after +-18 lines printed. Bug 8324
                    // But if added a regression error appears. Bug 9417, Better
                    // to keep it commented.
                    // ((SerialPort)m_CommPortPrinter).setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
                    // Not needed to set parallel properties
                    // } else if (m_PortIdPrinter.getPortType() ==
                    // CommPortIdentifier.PORT_PARALLEL) {
                    // ((ParallelPort)m_CommPortPrinter).setMode(1);
                }
                else if (m_PortIdPrinter.getPortType() == CommPortIdentifier.PORT_SERIAL && numero == 2) {
                    ((SerialPort) m_CommPortPrinter).setSerialPortParams(Sesion.getDatosConfiguracion().getVelocidadTicketAdicional(), 
                            Sesion.getDatosConfiguracion().getBitstTicketAdicional(), Sesion.getDatosConfiguracion().getStopTicketAdicional(),
                            Sesion.getDatosConfiguracion().getParidadTicketAdicional()); // Configuramos el puerto

                    // this line prevents the printer tmu220 to stop printing
                    // after +-18 lines printed. Bug 8324
                    // But if added a regression error appears. Bug 9417, Better
                    // to keep it commented.
                    // ((SerialPort)m_CommPortPrinter).setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
                    // Not needed to set parallel properties
                    // } else if (m_PortIdPrinter.getPortType() ==
                    // CommPortIdentifier.PORT_PARALLEL) {
                    // ((ParallelPort)m_CommPortPrinter).setMode(1);
                }
            }
            m_out.write(data);
        }
        catch (NoSuchPortException e) {
            log.error("internalWrite() - Error escribiendo en puerto: Puerto no encontrado: " + e.getMessage(), e);
        }
        catch (PortInUseException e) {
            log.error("internalWrite() - Error escribiendo en puerto: Puerto en uso: " + e.getMessage(), e);
        }
        catch (UnsupportedCommOperationException e) {
            log.error("internalWrite() - Error escribiendo en puerto: Operación no soportada:  " + e.getMessage(), e);
        }
        catch (IOException e) {
            log.error("internalWrite() - Error escribiendo en puerto: Error de entrada salida: " + e.getMessage(), e);
        }
        catch(Exception e){
            log.error("internalWrite() - Error escribiendo en puerto: Error inesperado: " + e.getMessage(), e);
        }
    }

    protected void internalFlush() {
        try {
            log.debug("PRINT:: FLUSH");
            if (m_out != null) {
                m_out.flush();
            }
        }
        catch (IOException e) {
            log.error("internalFlush() - Error realizando flush y puerto: " + e.getMessage(), e);
        }
    }

    protected void internalClose() {
        try {
            log.debug("PRINT:: CLOSE");
            if (m_out != null) {
                m_out.flush();
                m_out.close();
                m_out = null;
                m_CommPortPrinter.close();
                m_CommPortPrinter = null;
                m_PortIdPrinter = null;
            }
        }
        catch (IOException e) {
            log.error("internalClose() - Error realizando cierre de flujo y puerto: " + e.getMessage(), e);
        }
    }

    public static void logAvailableSerialPorts() {
        try{
            log.debug("PRINT:: Listando puertos disponibles...");
            Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
            while (portEnum.hasMoreElements()) {
                CommPortIdentifier portIdentifier = portEnum.nextElement();
                log.debug(" > " + portIdentifier.getName() + " - " + getPortTypeName(portIdentifier.getPortType()));
            }
        } catch (java.lang.UnsatisfiedLinkError e){
            log.debug("PRINT:: No ha sido posible listar los puertos Serie/Paralelo disponibles...");            
        } catch (Exception e){
            log.debug("PRINT:: No ha sido posible listar los puertos Serie/Paralelo disponibles...");
        }
    }

    private static String getPortTypeName(int portType) {
        switch (portType) {
            case CommPortIdentifier.PORT_I2C:
                return "I2C";
            case CommPortIdentifier.PORT_PARALLEL:
                return "Parallel";
            case CommPortIdentifier.PORT_RAW:
                return "Raw";
            case CommPortIdentifier.PORT_RS485:
                return "RS485";
            case CommPortIdentifier.PORT_SERIAL:
                return "Serial";
            default:
                return "unknown type";
        }
    }
}