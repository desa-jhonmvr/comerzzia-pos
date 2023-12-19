package com.comerzzia.jpos.printer.escpos;


import com.comerzzia.jpos.printer.TicketPrinterException;
import ch.ntb.usb.Device;
import ch.ntb.usb.USB;
import ch.ntb.usb.USBException;
import es.mpsistemas.util.log.Logger;




public class PrinterWritterUsb extends PrinterWritter {

        private static final Logger log = Logger.getMLogger(PrinterWritterUsb.class);
	
	private String m_sPortPrinter;
	
	private short vendorId;
	private short productId;
	private String bus;
	private String file;
	private int writingZone;
        Integer numero;
	
	Device dev = null;
	
	public PrinterWritterUsb(String sPortPrinter)
			throws TicketPrinterException {
                log.info("PrinterWritterUsb()");
                String[] cadena=sPortPrinter.split(",");
		m_sPortPrinter = cadena[0];
                String[] portData = m_sPortPrinter.split("-");
                vendorId = Short.parseShort(portData[0],16);
                productId = Short.parseShort(portData[1],16);
                bus = portData[2];
                file = portData[3];
                if(bus.equals("null")){
                    bus = null;
                }
                if(file.equals("null")){
                    file = null;
                }
                writingZone = Integer.parseInt(portData[4],16);
                numero= Integer.parseInt(cadena[1]);
                System.out.println("Inciando dispositivo usb: vendorId["+vendorId+"] productId["+productId+"] bus["+bus+"] archivo["+file+"]");
		dev = null;
	}
        
        public PrinterWritterUsb(String vendorId, String productId, String bus, String file, String writingZone, String numero){
	    log.info("PrinterWritterUsb()");
            this.m_sPortPrinter = vendorId+"-"+productId+"-"+bus+"-"+file+"-"+writingZone;
                this.vendorId = Short.parseShort(vendorId,16);
                this.productId = Short.parseShort(productId,16);
                if(!bus.equals("null")){
                    this.bus = bus;
                }
                if(!file.equals("null")){
                    this.file = file;
                }
                this.writingZone = Integer.parseInt(writingZone,16);
                this.numero= Integer.parseInt(numero);
                System.out.println("Inciando dispositivo usb: vendorId["+vendorId+"] productId["+productId+"] bus["+bus+"] archivo["+file+"]");
		dev = null;  
        }

	protected void internalWrite(byte[] data) {
		try {
		    if(dev == null){
		        dev = USB.getDevice(vendorId, productId, bus, file);
		        dev.open(1, 0, -1);
		    }
		    dev.writeBulk(writingZone, data, data.length, 2000, false);					
		} catch (USBException e) {
			System.err.println("Error dispositivo usb: bus["+bus+"] archivo["+file+"], escribiendo["+data.length+"] bytes ");
			System.err.println(e);
		}
	}

	protected void internalFlush() {
                // Nothing TO DO explicit ESC/POS Flushing is required.
	}

	protected void internalClose() {
            log.debug("internalClose()");
		try {
			if (dev != null) {
			    dev.close();
			    dev = null;
			}
		} catch (USBException e) {
                        log.error("Error cerrando impresora usb");
			System.err.println(e);
		}
	}
}
