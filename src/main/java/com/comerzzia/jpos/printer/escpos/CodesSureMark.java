/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.printer.escpos;

import com.comerzzia.jpos.printer.DevicePrinter;
import com.comerzzia.jpos.printer.DeviceTicket;
import com.comerzzia.jpos.printer.escpos.PrinterWritter;

/**
 *
 * @author Admin
 */
public class CodesSureMark extends com.comerzzia.jpos.printer.escpos.Codes{
    public static final byte[] INIT = { 0x1B, 0x40 };

	public static final byte[] SELECT_PRINTER = { 0x1B, 0x3D, 0x01 };
	public static final byte[] SELECT_DISPLAY = { 0x1B, 0x3D, 0x02 };

	public static final byte[] HT = { 0x09 }; // Horizontal Tab
	// public static final byte[] LF = {0x0A}; // Print and line feed
	public static final byte[] FF = { 0x0C }; // 
	// public static final byte[] CR = {0x0D}; // Print and carriage return

	public static final byte[] CHAR_FONT_0 = { 0x1B, 0x4D, 0x00 };
	public static final byte[] CHAR_FONT_1 = { 0x1B, 0x4D, 0x01 };
	public static final byte[] CHAR_FONT_2 = { 0x1B, 0x4D, 0x30 };
	public static final byte[] CHAR_FONT_3 = { 0x1B, 0x4D, 0x31 };

	public static final byte[] BAR_HEIGHT = { 0x1D, 0x68, 0x60 };
        public static final byte[] BAR_WIDTH = { 0x1D, 0x77, 0x02 };
	public static final byte[] BAR_POSITIONDOWN = { 0x1D, 0x48, 0x02 };
	public static final byte[] BAR_POSITIONNONE = { 0x1D, 0x48, 0x00 };
	public static final byte[] BAR_HRIFONT1 = { 0x1D, 0x66, 0x01 };

	public static final byte[] BAR_CODE02 = { 0x1D, 0x6B, 0x02 }; // 12 numeros
	public static final byte[] BAR_CODE07 = { 0x1D, 0x6B, 0x07};

	public static final byte[] VISOR_HIDE_CURSOR = { 0x1F, 0x43, 0x00 };
	public static final byte[] VISOR_SHOW_CURSOR = { 0x1F, 0x43, 0x01 };
	public static final byte[] VISOR_HOME = { 0x0B };
	public static final byte[] VISOR_CLEAR = { 0x0C };

	public static final byte[] CODE_TABLE_00 = { 0x1B, 0x74, 0x00 };
	public static final byte[] CODE_TABLE_13 = { 0x1B, 0x74, 0x13 };

        private static final byte[] INITSEQUENCE = {};

	private static final byte[] CHAR_SIZE_0 = { 0x1D, 0x21, 0x00 };
	private static final byte[] CHAR_SIZE_1 = { 0x1D, 0x21, 0x01 };
	private static final byte[] CHAR_SIZE_2 = { 0x1D, 0x21, 0x30 };
	private static final byte[] CHAR_SIZE_3 = { 0x1D, 0x21, 0x31 };

	public static final byte[] BOLD_SET = { 0x1B, 0x47, 0x01 };
	public static final byte[] BOLD_RESET = { 0x1B, 0x47, 0x00 };
	public static final byte[] UNDERLINE_SET = { 0x1B, 0x2D, 0x01 };
	public static final byte[] UNDERLINE_RESET = { 0x1B, 0x2D, 0x00 };

	private static final byte[] OPEN_DRAWER = { 0x1B, 0x70, 0x00, 0x32, -0x06 };
	private static final byte[] PARTIAL_CUT_1 = { 0x1B, 0x69 };
	private static final byte[] IMAGE_HEADER = { 0x1D, 0x76, 0x30, 0x03 };
	private static final byte[] NEW_LINE = { 0x0D, 0x0A }; // Print and carriage
											
	public CodesSureMark() {
	}

    public byte[] getInitSequence() {
		return INITSEQUENCE;
	}

	public byte[] getSize0() {
		return CHAR_SIZE_0;
	}

	public byte[] getSize1() {
		return CHAR_SIZE_1;
	}

	public byte[] getSize2() {
		return CHAR_SIZE_2;
	}

	public byte[] getSize3() {
		return CHAR_SIZE_3;
	}

	public byte[] getBoldSet() {
		return BOLD_SET;
	}

	public byte[] getBoldReset() {
		return BOLD_RESET;
	}

	public byte[] getUnderlineSet() {
		return UNDERLINE_SET;
	}

	public byte[] getUnderlineReset() {
		return UNDERLINE_RESET;
	}

	public byte[] getOpenDrawer() {
		return OPEN_DRAWER;
	}

	public byte[] getCutReceipt() {
		return PARTIAL_CUT_1;
	}

	public byte[] getNewLine() {
		return NEW_LINE;
	}

	public byte[] getImageHeader() {
		return IMAGE_HEADER;
	}

	public int getImageWidth() {
		return 256;
	}
        
        public void printBarcode(PrinterWritter out, String type, String position,
			String code) {

		if (DevicePrinter.BARCODE_EAN13.equals(type)) {

			out.write(getNewLine());

			out.write(BAR_HEIGHT);
                        
			if (DevicePrinter.POSITION_NONE.equals(position)) {
				out.write(BAR_POSITIONNONE);
			} else {
				out.write(BAR_POSITIONDOWN);
			}
			out.write(BAR_HRIFONT1);
			out.write(BAR_CODE02);
			out.write(DeviceTicket.transNumber(code));
			out.write(new byte[] { 0x00 });

			out.write(getNewLine());
		}
                
                else if (DevicePrinter.BARCODE_CODE128.equals(type)) {
                out.write(getNewLine());

			out.write(BAR_HEIGHT);
                        out.write(BAR_WIDTH);
			if (DevicePrinter.POSITION_NONE.equals(position)) {
				out.write(BAR_POSITIONNONE);
			} else {
				out.write(BAR_POSITIONDOWN);
			}
			out.write(BAR_HRIFONT1);
			out.write(BAR_CODE07);
                        out.write(code.getBytes());
			out.write(new byte[]{0x00});

			out.write(getNewLine());
                }
	}
    
}
