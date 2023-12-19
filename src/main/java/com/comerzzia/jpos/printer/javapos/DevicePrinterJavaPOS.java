
package com.comerzzia.jpos.printer.javapos;

import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import jpos.CashDrawer;
import jpos.JposException;
import jpos.POSPrinter;
import jpos.POSPrinterConst;

import com.comerzzia.jpos.printer.DevicePrinter;

public class DevicePrinterJavaPOS implements DevicePrinter {

	private static final String JPOS_SIZE0 = "\u001b|1C";
	private static final String JPOS_SIZE1 = "\u001b|2C";
	private static final String JPOS_SIZE2 = "\u001b|3C";
	private static final String JPOS_SIZE3 = "\u001b|4C";
	private static final String JPOS_LF = "\n";
	private static final String JPOS_BOLD = "\u001b|bC";
	private static final String JPOS_UNDERLINE = "\u001b|uC";
	private static final String JPOS_CUT = "\u001b|100fP";

	private String m_sName;

	private POSPrinter m_printer = null;
	private CashDrawer m_drawer = null;

	private StringBuffer m_sline;

	/** Creates a new instance of DevicePrinterJavaPOS */
	public DevicePrinterJavaPOS(String sDevicePrinterName,
			String sDeviceDrawerName) {

		m_sName = sDevicePrinterName;
		if (sDeviceDrawerName != null && !sDeviceDrawerName.equals("")) {
			m_sName += " - " + sDeviceDrawerName;
		}

		try {
			m_printer = new POSPrinter();
			m_printer.open(sDevicePrinterName);
			m_printer.claim(10000);
			m_printer.setDeviceEnabled(true);
			m_printer.setMapMode(POSPrinterConst.PTR_MM_METRIC); // unit = 1/100
																	// mm - i.e.
																	// 1 cm = 10
																	// mm = 10 *
																	// 100 units
		} catch (JposException e) {
			// cannot live without the printer.

		}

		try {
			m_drawer = new CashDrawer();
			m_drawer.open(sDeviceDrawerName);
			m_drawer.claim(10000);
			m_drawer.setDeviceEnabled(true);
		} catch (JposException e) {
			// can live without the drawer;
			m_drawer = null;
		}
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
		System.out.println("Imprimiendo recibo\n");
		try {
			m_printer.transactionPrint(POSPrinterConst.PTR_S_RECEIPT,
					POSPrinterConst.PTR_TP_TRANSACTION);
		} catch (JposException e) {
		}
	}

	public void beginLine(int iTextSize) {
		System.out.println("Comienzo de l�nea\n");
		m_sline = new StringBuffer();
		if (iTextSize == 0) {
			m_sline.append(JPOS_SIZE0);
		} else if (iTextSize == 1) {
			m_sline.append(JPOS_SIZE1);
		} else if (iTextSize == 2) {
			m_sline.append(JPOS_SIZE2);
		} else if (iTextSize == 3) {
			m_sline.append(JPOS_SIZE3);
		} else {
			m_sline.append(JPOS_SIZE0);
		}
	}

	public void printText(int iStyle, String sText) {
		System.out.println("Imprimiendo texto\n");

		if ((iStyle & 1) != 0) {
			m_sline.append(JPOS_BOLD);
		}
		if ((iStyle & 2) != 0) {
			m_sline.append(JPOS_UNDERLINE);
		}
		m_sline.append(sText);
	}

	public void endLine() {
		System.out.println("imprimiendo final de linea\n");

		m_sline.append(JPOS_LF);
		try {
			m_printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, m_sline
					.toString());
		} catch (JposException e) {
		}
		m_sline = null;
	}

	public void endReceipt(int impresora) {
		System.out.println("finalizando recibo\n");
		try {
			// cut the receipt
			m_printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, JPOS_CUT);

			// end of the transaction
			m_printer.transactionPrint(POSPrinterConst.PTR_S_RECEIPT,
					POSPrinterConst.PTR_TP_NORMAL);
		} catch (JposException e) {
		}
	}

	public void openDrawer() {

		if (m_drawer != null) {
			try {
				m_drawer.openDrawer();
			} catch (JposException e) {
			}
		}
	}

	@Override
	public void finalize() throws Throwable {

		m_printer.setDeviceEnabled(false);
		m_printer.release();
		m_printer.close();

		if (m_drawer != null) {
			m_drawer.setDeviceEnabled(false);
			m_drawer.release();
			m_drawer.close();
		}

		super.finalize();
	}

	@Override
	public void printBarCode(String type, String position, String code) {
		
	}

	@Override
	public void printImage(BufferedImage image) {
		
	}

    @Override
    public void seleccionarImpresora(String tipo) {
        
    }
}
