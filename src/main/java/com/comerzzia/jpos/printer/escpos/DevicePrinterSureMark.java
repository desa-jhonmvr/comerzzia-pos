package com.comerzzia.jpos.printer.escpos;

import com.comerzzia.jpos.printer.DevicePrinter;
import com.comerzzia.jpos.printer.TicketPrinterException;
import com.comerzzia.jpos.printer.escpos.Codes;
import com.comerzzia.jpos.printer.escpos.PrinterWritter;
import com.comerzzia.jpos.printer.escpos.UnicodeTranslator;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;



public class DevicePrinterSureMark implements DevicePrinter {

	private PrinterWritter m_CommOutputPrinter;
	private Codes m_codes;
	private UnicodeTranslator m_trans;

	// private boolean m_bInline;
	private String m_sName;

	// Creates new TicketPrinter
	public DevicePrinterSureMark(PrinterWritter CommOutputPrinter, Codes codes,
			UnicodeTranslator trans) throws TicketPrinterException {

		m_sName = "PrinterSerial";
		m_CommOutputPrinter = CommOutputPrinter;
		m_codes = codes;
		m_trans = trans;

		// Inicializamos la impresora
		m_CommOutputPrinter.init(CodesSureMark.INIT);

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

		m_CommOutputPrinter.write(m_codes.transImage(image));
	}

	public void printBarCode(String type, String position, String code) {

		m_codes.printBarcode(m_CommOutputPrinter, type, position, code);
	}

	public void beginLine(int iTextSize) {

                m_CommOutputPrinter.write(new byte[]{0x1b,0x31});
		if (iTextSize == DevicePrinter.SIZE_0) {
			m_CommOutputPrinter.write(m_codes.getSize0());
		} else if (iTextSize == DevicePrinter.SIZE_1) {
			m_CommOutputPrinter.write(m_codes.getSize1());
		} else if (iTextSize == DevicePrinter.SIZE_2) {
			m_CommOutputPrinter.write(m_codes.getSize2());
		} else if (iTextSize == DevicePrinter.SIZE_3) {
			m_CommOutputPrinter.write(m_codes.getSize3());
		} else {
			m_CommOutputPrinter.write(m_codes.getSize0());
		}
	}

	public void printText(int iStyle, String sText) {


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
		m_CommOutputPrinter.write(m_codes.getNewLine());
	}

	public void endReceipt(int impresora) {

		m_CommOutputPrinter.write(m_codes.getNewLine());
		m_CommOutputPrinter.write(m_codes.getNewLine());
		m_CommOutputPrinter.write(m_codes.getNewLine());
		m_CommOutputPrinter.write(m_codes.getNewLine());
		m_CommOutputPrinter.write(m_codes.getNewLine());

		m_CommOutputPrinter.write(m_codes.getCutReceipt());
		m_CommOutputPrinter.flush();

	}

	public void openDrawer() {

		m_CommOutputPrinter.write(m_codes.getOpenDrawer());
		m_CommOutputPrinter.flush();
	}

	public void close() {

		m_CommOutputPrinter.internalClose();
	}

    @Override
    public void seleccionarImpresora(String tipo) {
      
    }
}
