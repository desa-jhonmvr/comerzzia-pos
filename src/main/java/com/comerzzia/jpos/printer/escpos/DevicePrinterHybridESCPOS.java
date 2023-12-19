package com.comerzzia.jpos.printer.escpos;

import com.comerzzia.jpos.printer.DevicePrinter;
import com.comerzzia.jpos.printer.TicketPrinterException;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;



public class DevicePrinterHybridESCPOS implements DevicePrinter {

	private PrinterWritter printer;
	private CodesHybrid codes;
	private UnicodeTranslator translator;

	private String printerName;

	public DevicePrinterHybridESCPOS(PrinterWritter CommOutputPrinter, Codes codes,
			UnicodeTranslator trans) throws TicketPrinterException {

		printerName = "HybridPrinter";
		printer = CommOutputPrinter;
		this.codes = (CodesHybrid)codes;
		translator = trans;

		printer.init(ESCPOS.INIT);
		printer.write(ESCPOS.SELECT_PRINTER);
		printer.write(new byte[]{(byte)0x1b,(byte)(char)'c',(byte)(char)'0',(byte)(int)2});
		//
		printer.write(translator.getCodeTable());
		
	}	
	
	public String getPrinterName() {
		return printerName;
	}

	public String getPrinterDescription() {
		return "Hybrid Receipt Slip printer";
	}

	public JComponent getPrinterComponent() {
		return null;
	}

	public void reset() {
	}

	public void beginReceipt() {
	}

	public void printImage(BufferedImage image) {

		printer.write(ESCPOS.SELECT_PRINTER);
		printer.write(codes.transImage(image));
	}

	public void printBarCode(String type, String position, String code) {

		printer.write(ESCPOS.SELECT_PRINTER);
		codes.printBarcode(printer, type, position, code);
	}

	public void beginLine(int iTextSize) {

		printer.write(ESCPOS.SELECT_PRINTER);
		
		if (iTextSize == DevicePrinter.SIZE_0) {
			printer.write(codes.getSize0());
		} else if (iTextSize == DevicePrinter.SIZE_1) {
			printer.write(codes.getSize1());
		} else if (iTextSize == DevicePrinter.SIZE_2) {
			printer.write(codes.getSize2());
		} else if (iTextSize == DevicePrinter.SIZE_3) {
			printer.write(codes.getSize3());
		} else {
			printer.write(codes.getSize0());
		}
	}

	public void printText(int iStyle, String sText) {

		printer.write(ESCPOS.SELECT_PRINTER);

		if ((iStyle & DevicePrinter.STYLE_BOLD) != 0) {
			printer.write(codes.getBoldSet());
		}
		if ((iStyle & DevicePrinter.STYLE_UNDERLINE) != 0) {
			printer.write(codes.getUnderlineSet());
		}
		printer.write(translator.transString(sText));
		if ((iStyle & DevicePrinter.STYLE_UNDERLINE) != 0) {
			printer.write(codes.getUnderlineReset());
		}
		if ((iStyle & DevicePrinter.STYLE_BOLD) != 0) {
			printer.write(codes.getBoldReset());
		}
	}

	public void endLine() {
		printer.write(ESCPOS.SELECT_PRINTER);
		printer.write(new byte[]{(byte)0x1b,(byte)(char)'3',(byte)(int)25});
		printer.write(new byte[]{(byte)0x0A});
	}

	public void endReceipt(int impresora) {
		printer.write(ESCPOS.SELECT_PRINTER);
		printer.write(codes.getCutReceipt());
		
	}

	public void openDrawer() {

		printer.write(ESCPOS.SELECT_PRINTER);
		printer.write(codes.getOpenDrawer());
		
	}

	public void close() {

		printer.internalClose();
	}

    @Override
    public void seleccionarImpresora(String tipo) {
        
    }
}
