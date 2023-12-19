package com.comerzzia.jpos.printer.escpos;

import com.comerzzia.jpos.printer.DevicePrinter;
import com.comerzzia.jpos.printer.DeviceTicket;
import java.awt.image.BufferedImage;

public class CodesHybrid extends Codes {

	
	private static final byte[] INITSEQUENCE = {};
	public static final byte[] CHAR_SIZE_0 = { 0x1B, 0x21, 0x01 }; 
	public static final byte[] CHAR_SIZE_1 = { 0x1B, 0x21, 0x11 };
	public static final byte[] CHAR_SIZE_2 = { 0x1B, 0x21, 0x21 };
	public static final byte[] CHAR_SIZE_3 = { 0x1B, 0x21, 0x31 };
	public static final byte[] BOLD_SET = { 0x1B, 0x45, 0x01 };
	public static final byte[] BOLD_RESET = { 0x1B, 0x45, 0x00 };
	public static final byte[] UNDERLINE_SET = { 0x1B, 0x2D, 0x01 };
	public static final byte[] UNDERLINE_RESET = { 0x1B, 0x2D, 0x00 };
	private static final byte[] OPEN_DRAWER = { 0x1B, 0x70, 0x00, 0x32, -0x06 };
	private static final byte[] PARTIAL_CUT_1 = { 0x1B, 0x69 };
	private static final byte[] IMAGE_HEADER = { 0x1D, 0x76, 0x30, 0x03 };
	private static final byte[] NEW_LINE = { 0x0D, 0x0A }; 

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
	} // Not used

	public int getImageWidth() {
		return 256;
	}
	
	
	public CodesHybrid() {
	}

	@Override
	public void printBarcode(PrinterWritter out, String type, String position,
			String code) {

		if (DevicePrinter.BARCODE_EAN13.equals(type)) {

			out.write(getNewLine());

			out.write(ESCPOS.BAR_HEIGHT);
                        
			if (DevicePrinter.POSITION_NONE.equals(position)) {
				out.write(ESCPOS.BAR_POSITIONNONE);
			} else {
				out.write(ESCPOS.BAR_POSITIONDOWN);
			}
			out.write(ESCPOS.BAR_HRIFONT1);
			out.write(ESCPOS.BAR_CODE02);
			out.write(DeviceTicket.transNumber(code));
			out.write(new byte[] { 0x00 });

			out.write(getNewLine());
		}
                
                else if (DevicePrinter.BARCODE_CODE128.equals(type)) {
                out.write(getNewLine());

			out.write(ESCPOS.BAR_HEIGHT);
                        //out.write(ESCPOS.BAR_WIDTH);
                        out.write(new byte[]{(byte)0x1d,(byte)0x77,(byte)(int)2});
			if (DevicePrinter.POSITION_NONE.equals(position)) {
				out.write(ESCPOS.BAR_POSITIONNONE);
			} else {
				out.write(ESCPOS.BAR_POSITIONDOWN);
			}
			out.write(ESCPOS.BAR_HRIFONT1);
			
			//byte[] code128 = new byte[]{(byte)0x7b,(byte)0x43};			
			//byte[] code128Data = code.getBytes();			
			//byte[] encoded = new byte[code128.length + code128Data.length];

			//System.arraycopy(code128,0,encoded,0,code128.length);
			//System.arraycopy(code128Data,0,encoded,code128.length,code128Data.length);
			String encoded = "{B"+code;		
			out.write(new byte[]{(byte)0x1d,(byte)(char)'k',(byte)(int)73,(byte)(int)encoded.getBytes().length});
			
			out.write(encoded.getBytes());

			out.write(getNewLine());
                }
	}
}
