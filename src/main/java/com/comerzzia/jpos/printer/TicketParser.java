package com.comerzzia.jpos.printer;

import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.print.PrintServices;
import com.comerzzia.jpos.util.StringParser;
import com.comerzzia.util.imagenes.Imagenes;
import es.mpsistemas.util.log.Logger;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TicketParser extends DefaultHandler {

    private SAXParser m_sp = null;
    private DeviceTicket m_printer;
    private StringBuffer text;
    private String bctype;
    private String bcposition;
    private int m_iTextAlign;
    private int m_iTextLength;
    private int m_iTextStyle;
    private StringBuffer m_sVisorLine;
    private int m_iVisorAnimation;
    private String m_sVisorLine1;
    private String m_sVisorLine2;
    private double m_dValue1;
    private double m_dValue2;
    private int attribute3;
    private int m_iOutputType;
    private static final int OUTPUT_NONE = 0;
    private static final int OUTPUT_DISPLAY = 1;
    private static final int OUTPUT_TICKET = 2;
    private static final int OUTPUT_FISCAL = 3;
    private DevicePrinter m_oOutputPrinter;
    private StringBuffer textoCabecera = new StringBuffer("");
    private StringBuffer textoPie = new StringBuffer("");
    private StringBuffer textoReimpresionCabecera = new StringBuffer("");
    private StringBuffer textoReimpresionDetalle = new StringBuffer("");
    private StringBuffer textoAnulada = new StringBuffer("");
    private boolean original = false;

    private String modoImpresora; //normal 0, SLIP 1
    private int impresoraDirecta; // Número de la impresora en la que se desea imprimir el comprobante

    private static Logger log = Logger.getMLogger(TicketParser.class);

    /**
     * Creates a new instance of TicketParser
     */
    public TicketParser(DeviceTicket printer) {
        m_printer = printer;

    }

    public void printTicket(String sIn) throws TicketPrinterException {
        sIn = StringParser.parseaXMLDocumentosImpresos(sIn);
        printTicket(new StringReader(sIn));
    }

    public void printTicket(Reader in) throws TicketPrinterException {

        try {
            if (m_sp == null) {
                SAXParserFactory spf = SAXParserFactory.newInstance();
                m_sp = spf.newSAXParser();
            }
            m_sp.parse(new InputSource(in), this);

        } catch (ParserConfigurationException ePC) {
            throw new TicketPrinterException("error parsing configuration", ePC);
        } catch (SAXException eSAX) {
            throw new TicketPrinterException("error sax", eSAX);
        } catch (IOException eIO) {
            throw new TicketPrinterException("error io", eIO);
        } catch (Exception ex) {
            throw new TicketPrinterException("Error no controlado", ex);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        // inicalizo las variables pertinentes
        text = null;
        bctype = null;
        bcposition = null;
        m_sVisorLine = null;
        // m_iVisorAnimation = DeviceDisplayBase.ANIMATION_NULL;
        m_sVisorLine1 = null;
        m_sVisorLine2 = null;
        m_iOutputType = OUTPUT_NONE;
        m_oOutputPrinter = null;
        modoImpresora = PrintServices.RECEIPT;
    }

    @Override
    public void endDocument() throws SAXException {
        log.debug("XML de documento se ha parseado por completo y enviado a la impresora.");
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {

        switch (m_iOutputType) {
            case OUTPUT_NONE:
                if ("opendrawer".equals(qName)) {
                    m_printer.getDevicePrinter(
                            readString(attributes.getValue("printer"), "1")).openDrawer();
                } else if ("play".equals(qName)) {
                    text = new StringBuffer();
                } else if ("ticket".equals(qName)) {
                    m_iOutputType = OUTPUT_TICKET;
                    m_oOutputPrinter = m_printer.getDevicePrinter(readString(
                            attributes.getValue("printer"), "1"));
                    m_oOutputPrinter.beginReceipt();

                    // Leemos del ticket el modo de la impresora (modoImpresora. Solo será necesario para imprimir cheques)
                    String modoStr = attributes.getValue("modoImpresora");
                    if (modoStr == null) {
                        modoStr = PrintServices.RECEIPT;
                    }
                    m_oOutputPrinter.seleccionarImpresora(modoStr);

                    String impresoraDirectaStr = attributes.getValue("impresora");
                    if (impresoraDirectaStr != null) {
                        try {
                            impresoraDirecta = Integer.valueOf(impresoraDirectaStr);
                        } catch (Exception e) {
                            log.error("startElement() -Número de impresora inválido " + impresoraDirectaStr);
                            impresoraDirecta = 1;
                        }
                    }
                } else if ("display".equals(qName)) {
                    m_iOutputType = OUTPUT_DISPLAY;
                    m_sVisorLine1 = null;
                    m_sVisorLine2 = null;
                    m_oOutputPrinter = null;

                }
                break;
            case OUTPUT_TICKET:
                if ("image".equals(qName)) {
                    text = new StringBuffer();
                } else if ("barcode".equals(qName)) {
                    text = new StringBuffer();
                    bctype = attributes.getValue("type");
                    bcposition = attributes.getValue("position");
                } else if ("line".equals(qName)) {
                    m_oOutputPrinter.beginLine(DevicePrinter.SIZE_0);
                } else if ("text".equals(qName)) {
                    text = new StringBuffer();
                    m_iTextStyle = ("true".equals(attributes.getValue("bold")) ? DevicePrinter.STYLE_BOLD
                            : DevicePrinter.STYLE_PLAIN)
                            | ("true".equals(attributes.getValue("underline")) ? DevicePrinter.STYLE_UNDERLINE
                            : DevicePrinter.STYLE_PLAIN);
                    String sAlign = attributes.getValue("align");
                    if ("right".equals(sAlign)) {
                        m_iTextAlign = DevicePrinter.ALIGN_RIGHT;
                    } else if ("center".equals(sAlign)) {
                        m_iTextAlign = DevicePrinter.ALIGN_CENTER;
                    } else {
                        m_iTextAlign = DevicePrinter.ALIGN_LEFT;
                    }
                    m_iTextLength = parseInt(attributes.getValue("length"), 0);
                } else if ("cabecera".equals(qName)) {
                    m_iTextStyle = ("true".equals(attributes.getValue("bold")) ? DevicePrinter.STYLE_BOLD
                            : DevicePrinter.STYLE_PLAIN)
                            | ("true".equals(attributes.getValue("underline")) ? DevicePrinter.STYLE_UNDERLINE
                            : DevicePrinter.STYLE_PLAIN);
                    m_iTextLength = parseInt(attributes.getValue("length"), 0);
                } else if ("anulada".equals(qName)) {
                    m_iTextStyle = ("true".equals(attributes.getValue("bold")) ? DevicePrinter.STYLE_BOLD
                            : DevicePrinter.STYLE_PLAIN)
                            | ("true".equals(attributes.getValue("underline")) ? DevicePrinter.STYLE_UNDERLINE
                            : DevicePrinter.STYLE_PLAIN);
                    m_iTextLength = parseInt(attributes.getValue("length"), 0);
                } else if ("pie".equals(qName)) {
                    m_iTextStyle = ("true".equals(attributes.getValue("bold")) ? DevicePrinter.STYLE_BOLD
                            : DevicePrinter.STYLE_PLAIN)
                            | ("true".equals(attributes.getValue("underline")) ? DevicePrinter.STYLE_UNDERLINE
                            : DevicePrinter.STYLE_PLAIN);
                    m_iTextLength = parseInt(attributes.getValue("length"), 0);
                } else if ("reimpresion".equals(qName)) {
                    m_iTextStyle = ("true".equals(attributes.getValue("bold")) ? DevicePrinter.STYLE_BOLD
                            : DevicePrinter.STYLE_PLAIN)
                            | ("true".equals(attributes.getValue("underline")) ? DevicePrinter.STYLE_UNDERLINE
                            : DevicePrinter.STYLE_PLAIN);
                    m_iTextLength = parseInt(attributes.getValue("length"), 0);
                } else if ("reimpresionDetalle".equals(qName)) {
                    m_iTextStyle = ("true".equals(attributes.getValue("bold")) ? DevicePrinter.STYLE_BOLD
                            : DevicePrinter.STYLE_PLAIN)
                            | ("true".equals(attributes.getValue("underline")) ? DevicePrinter.STYLE_UNDERLINE
                            : DevicePrinter.STYLE_PLAIN);
                    m_iTextLength = parseInt(attributes.getValue("length"), 0);
                } else if ("original".equals(qName)) {
                    if (original) {
                        text = new StringBuffer();
                        m_iTextStyle = ("true".equals(attributes.getValue("bold")) ? DevicePrinter.STYLE_BOLD
                                : DevicePrinter.STYLE_PLAIN)
                                | ("true".equals(attributes.getValue("underline")) ? DevicePrinter.STYLE_UNDERLINE
                                : DevicePrinter.STYLE_PLAIN);
                        String sAlign = attributes.getValue("align");
                        if ("right".equals(sAlign)) {
                            m_iTextAlign = DevicePrinter.ALIGN_RIGHT;
                        } else if ("center".equals(sAlign)) {
                            m_iTextAlign = DevicePrinter.ALIGN_CENTER;
                        } else {
                            m_iTextAlign = DevicePrinter.ALIGN_LEFT;
                        }
                        m_iTextLength = parseInt(attributes.getValue("length"), 0);
                    }
                }
                break;
            case OUTPUT_DISPLAY:
                if ("line".equals(qName)) { // line 1 or 2 of the display
                    m_sVisorLine = new StringBuffer();
                } else if ("line1".equals(qName)) { // linea 1 del visor
                    m_sVisorLine = new StringBuffer();
                } else if ("line2".equals(qName)) { // linea 2 del visor
                    m_sVisorLine = new StringBuffer();
                } else if ("text".equals(qName)) {
                    text = new StringBuffer();
                    String sAlign = attributes.getValue("align");
                    if ("right".equals(sAlign)) {
                        m_iTextAlign = DevicePrinter.ALIGN_RIGHT;
                    } else if ("center".equals(sAlign)) {
                        m_iTextAlign = DevicePrinter.ALIGN_CENTER;
                    } else {
                        m_iTextAlign = DevicePrinter.ALIGN_LEFT;
                    }
                    m_iTextLength = parseInt(attributes.getValue("length"));
                } else if ("original".equals(qName)) {
                    if (original) {
                        text = new StringBuffer();
                        String sAlign = attributes.getValue("align");
                        if ("right".equals(sAlign)) {
                            m_iTextAlign = DevicePrinter.ALIGN_RIGHT;
                        } else if ("center".equals(sAlign)) {
                            m_iTextAlign = DevicePrinter.ALIGN_CENTER;
                        } else {
                            m_iTextAlign = DevicePrinter.ALIGN_LEFT;
                        }
                        m_iTextLength = parseInt(attributes.getValue("length"));
                    }
                }
                break;
            case OUTPUT_FISCAL:
                if ("line".equals(qName)) {
                    text = new StringBuffer();
                    m_dValue1 = parseDouble(attributes.getValue("price"));
                    m_dValue2 = parseDouble(attributes.getValue("units"), 1.0);
                    attribute3 = parseInt(attributes.getValue("tax"));

                } else if ("message".equals(qName)) {
                    text = new StringBuffer();
                } else if ("total".equals(qName)) {
                    text = new StringBuffer();
                    m_dValue1 = parseDouble(attributes.getValue("paid"));
                }
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        switch (m_iOutputType) {
            case OUTPUT_NONE:
                if ("play".equals(qName)) {
                    try {
                        AudioClip oAudio = Applet.newAudioClip(getClass().getClassLoader().getResource(text.toString()));
                        oAudio.play();
                    } catch (Exception fnfe) {
                    }
                    text = null;
                }
                break;
            case OUTPUT_TICKET:

                if ("barcode".equals(qName)) {
                    m_oOutputPrinter.printBarCode(bctype, bcposition, text.toString());
                    text = null;
                } else if ("text".equals(qName)) {
                    if (m_iTextLength > 0) {
                        switch (m_iTextAlign) {
                            case DevicePrinter.ALIGN_RIGHT:
                                m_oOutputPrinter.printText(m_iTextStyle, DeviceTicket.alignRight(text.toString(), m_iTextLength));
                                break;
                            case DevicePrinter.ALIGN_CENTER:
                                m_oOutputPrinter.printText(m_iTextStyle, DeviceTicket.alignCenter(text.toString(), m_iTextLength));
                                break;
                            default:
                                m_oOutputPrinter.printText(m_iTextStyle, DeviceTicket.alignLeft(text.toString(), m_iTextLength));
                                break;
                        }
                        if (textoPie.toString().equals("COPIA SIN DERECHO A CRÉDITO TRIBUTARIO")
                                && (text.toString().contains("HORA") || text.toString().contains("Elaborado"))) {
                            m_oOutputPrinter.endLine();
                            m_oOutputPrinter.printText(m_iTextStyle, textoPie.toString());
                        }
                    } else {
                        m_oOutputPrinter.printText(m_iTextStyle, text.toString());
                    }
                    text = null;
                } else if ("cabecera".equals(qName)) {
                    if (textoCabecera.toString().length() > 0) {
                        String textos[] = textoCabecera.toString().split("\\|");
                        boolean primero = true;
                        for (String t : textos) {
                            if (!primero) {
                                m_oOutputPrinter.endLine();
                                m_oOutputPrinter.beginLine(DevicePrinter.SIZE_0);
                            } else {
                                primero = false;
                            }
                            textoCabecera = new StringBuffer(t);
                            m_oOutputPrinter.printText(m_iTextStyle, DeviceTicket.alignCenter(textoCabecera.toString(), m_iTextLength));

                        }
                    }
                } else if ("anulada".equals(qName)) {
                    if (textoAnulada.toString().length() > 0) {
                        String textos[] = textoAnulada.toString().split("\\|");
                        boolean primero = true;
                        for (String t : textos) {
                            if (!primero) {
                                m_oOutputPrinter.endLine();
                                m_oOutputPrinter.beginLine(DevicePrinter.SIZE_0);
                            } else {
                                primero = false;
                            }
                            textoAnulada = new StringBuffer(t);
                            m_oOutputPrinter.printText(m_iTextStyle, DeviceTicket.alignCenter(textoAnulada.toString(), m_iTextLength));
                        }
                    }
                } else if ("pie".equals(qName)) {
                    if (textoPie.toString().length() > 0) {
                        String textos[] = textoPie.toString().split("\\|");
                        boolean primero = true;
                        for (String t : textos) {
                            if (!primero) {
                                m_oOutputPrinter.endLine();
                                m_oOutputPrinter.beginLine(DevicePrinter.SIZE_0);
                            } else {
                                primero = false;
                            }
                            textoPie = new StringBuffer(t);
                            m_oOutputPrinter.printText(m_iTextStyle, DeviceTicket.alignCenter(textoPie.toString(), m_iTextLength));
                        }
                    }
                }else if ("reimpresion".equals(qName)) {
                    if (textoReimpresionCabecera.toString().length() > 0) {
                        String textos[] = textoReimpresionCabecera.toString().split("\\|");
                        boolean primero = true;
                        for (String t : textos) {
                            if (!primero) {
                                m_oOutputPrinter.endLine();
                                m_oOutputPrinter.beginLine(DevicePrinter.SIZE_0);
                            } else {
                                primero = false;
                            }
                            textoReimpresionCabecera = new StringBuffer(t);
                            m_oOutputPrinter.printText(m_iTextStyle, DeviceTicket.alignCenter(textoReimpresionCabecera.toString(), m_iTextLength));
                        }
                    }
                } else if ("reimpresionDetalle".equals(qName)) {
                    if (textoReimpresionDetalle.toString().length() > 0) {
                        String textos[] = textoReimpresionDetalle.toString().split("\\|");
                        boolean primero = true;
                        for (String t : textos) {
                            if (!primero) {
                                m_oOutputPrinter.endLine();
                                m_oOutputPrinter.beginLine(DevicePrinter.SIZE_0);
                            } else {
                                primero = false;
                            }
                            textoReimpresionDetalle = new StringBuffer(t);
                            m_oOutputPrinter.printText(m_iTextStyle, DeviceTicket.alignCenter(textoReimpresionDetalle.toString(), m_iTextLength));
                        }
                    }
                } else if ("line".equals(qName)) {
                    m_oOutputPrinter.endLine();
                } else if ("ticket".equals(qName)) {
                    m_oOutputPrinter.endReceipt(impresoraDirecta);
                    m_iOutputType = OUTPUT_NONE;
                    m_oOutputPrinter = null;
                } else if ("original".equals(qName)) {
                    if (original) {
                        if (m_iTextLength > 0) {
                            switch (m_iTextAlign) {
                                case DevicePrinter.ALIGN_RIGHT:
                                    m_oOutputPrinter.printText(m_iTextStyle, DeviceTicket.alignRight(text.toString(), m_iTextLength));
                                    break;
                                case DevicePrinter.ALIGN_CENTER:
                                    m_oOutputPrinter.printText(m_iTextStyle, DeviceTicket.alignCenter(text.toString(), m_iTextLength));
                                    break;
                                default:
                                    m_oOutputPrinter.printText(m_iTextStyle, DeviceTicket.alignLeft(text.toString(), m_iTextLength));
                                    break;
                            }
                        } else {
                            m_oOutputPrinter.printText(m_iTextStyle, text.toString());
                        }
                        text = null;
                    }
                } else if ("image".equals(qName)) {
                    BufferedImage img = null;
                    String codigoLogo = text.toString();      // attributes.getValue("codigo");
                    Map<String, Image> logosDisponibles = Sesion.getLogos();
                    for (String logo : logosDisponibles.keySet()) {
                        if (logo.equals(codigoLogo)) {
                            /*Podemos hacer casting de Image a BufferedImage sin riesgos
                             * ya que en com.comerzzia.util.Imagenes nos hemos asegurado
                             * al introducir un objeto en la sesión de que sea una instancia
                             * de BufferedImage.
                             */
                            img = (BufferedImage) logosDisponibles.get(logo);
                        }
                    }
                    if (img == null) {
                        img = Imagenes.obtenerLogo(codigoLogo, 257, 50);
                    }
                    //Si ha ocurrido algún error imprimimos el ticket sin la imagen.
                    if (img != null) {
                        m_oOutputPrinter.printImage(img);
                    }
                }
                break;
            case OUTPUT_DISPLAY:
                if ("line".equals(qName)) { // line 1 or 2 of the display
                    if (m_sVisorLine1 == null) {
                        m_sVisorLine1 = m_sVisorLine.toString();
                    } else {
                        m_sVisorLine2 = m_sVisorLine.toString();
                    }
                    m_sVisorLine = null;
                } else if ("line1".equals(qName)) { // linea 1 del visor
                    m_sVisorLine1 = m_sVisorLine.toString();
                    m_sVisorLine = null;
                } else if ("line2".equals(qName)) { // linea 2 del visor
                    m_sVisorLine2 = m_sVisorLine.toString();
                    m_sVisorLine = null;
                } else if ("text".equals(qName)) {
                    if (m_iTextLength > 0) {
                        switch (m_iTextAlign) {
                            case DevicePrinter.ALIGN_RIGHT:
                                m_sVisorLine.append(DeviceTicket.alignRight(text.toString(), m_iTextLength));
                                break;
                            case DevicePrinter.ALIGN_CENTER:
                                m_sVisorLine.append(DeviceTicket.alignCenter(text.toString(), m_iTextLength));
                                break;
                            default:
                                m_sVisorLine.append(DeviceTicket.alignLeft(text.toString(), m_iTextLength));
                                break;
                        }
                    } else {
                        m_sVisorLine.append(text);
                    }
                    text = null;
                } else if ("display".equals(qName)) {
                    m_printer.getDeviceDisplay().writeVisor(m_iVisorAnimation,
                            m_sVisorLine1, m_sVisorLine2);
                    m_sVisorLine1 = null;
                    m_sVisorLine2 = null;
                    m_iOutputType = OUTPUT_NONE;
                    m_oOutputPrinter = null;
                } else if ("cabecera".equals(qName)) {
                    if (textoCabecera.toString().length() > 0) {
                        String textos[] = textoCabecera.toString().split("\\|");
                        for (String t : textos) {
                            textoCabecera = new StringBuffer(t);
                            m_sVisorLine.append(DeviceTicket.alignCenter(textoCabecera.toString(), m_iTextLength));
                            m_sVisorLine = new StringBuffer();
                        }
                    }
                } else if ("anulada".equals(qName)) {
                    if (textoAnulada.toString().length() > 0) {
                        String textos[] = textoAnulada.toString().split("\\|");
                        for (String t : textos) {
                            textoAnulada = new StringBuffer(t);
                            m_sVisorLine.append(DeviceTicket.alignCenter(textoAnulada.toString(), m_iTextLength));
                            m_sVisorLine = new StringBuffer();
                        }
                    }
                } else if ("pie".equals(qName)) {
                    if (textoPie.toString().length() > 0) {
                        String textos[] = textoPie.toString().split("\\|");
                        for (String t : textos) {
                            textoPie = new StringBuffer(t);
                            m_sVisorLine.append(DeviceTicket.alignCenter(textoPie.toString(), m_iTextLength));
                            m_sVisorLine = new StringBuffer();
                        }
                    }
                } else if ("reimpresion".equals(qName)) {
                    if (textoReimpresionCabecera.toString().length() > 0) {
                        String textos[] = textoReimpresionCabecera.toString().split("\\|");
                        for (String t : textos) {
                            textoReimpresionCabecera = new StringBuffer(t);
                            m_sVisorLine.append(DeviceTicket.alignCenter(textoReimpresionCabecera.toString(), m_iTextLength));
                            m_sVisorLine = new StringBuffer();
                        }
                    }
                }else if ("reimpresionDetalle".equals(qName)) {
                    if (textoReimpresionDetalle.toString().length() > 0) {
                        String textos[] = textoReimpresionDetalle.toString().split("\\|");
                        for (String t : textos) {
                            textoReimpresionDetalle = new StringBuffer(t);
                            m_sVisorLine.append(DeviceTicket.alignCenter(textoReimpresionDetalle.toString(), m_iTextLength));
                            m_sVisorLine = new StringBuffer();
                        }
                    }
                }else if ("original".equals(qName)) {
                    if (original) {
                        if (m_iTextLength > 0) {
                            switch (m_iTextAlign) {
                                case DevicePrinter.ALIGN_RIGHT:
                                    m_sVisorLine.append(DeviceTicket.alignRight(text.toString(), m_iTextLength));
                                    break;
                                case DevicePrinter.ALIGN_CENTER:
                                    m_sVisorLine.append(DeviceTicket.alignCenter(text.toString(), m_iTextLength));
                                    break;
                                default:
                                    m_sVisorLine.append(DeviceTicket.alignLeft(text.toString(), m_iTextLength));
                                    break;
                            }
                        } else {
                            m_sVisorLine.append(text);
                        }
                        text = null;
                    }
                }
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        if (text != null) {
            text.append(ch, start, length);
        }
    }

    private int parseInt(String sValue, int iDefault) {
        try {
            return Integer.parseInt(sValue);
        } catch (NumberFormatException eNF) {
            return iDefault;
        }
    }

    private int parseInt(String sValue) {
        return parseInt(sValue, 0);
    }

    private double parseDouble(String sValue, double ddefault) {
        try {
            return Double.parseDouble(sValue);
        } catch (NumberFormatException eNF) {
            return ddefault;
        }
    }

    private double parseDouble(String sValue) {
        return parseDouble(sValue, 0.0);
    }

    private String readString(String sValue, String sDefault) {
        if (sValue == null || sValue.isEmpty()) {
            return sDefault;
        } else {
            return sValue;
        }
    }

    public void setTextoCabecera(String textoCabecera) {
        this.textoCabecera = new StringBuffer(textoCabecera);
    }

    public void setTextoPie(String textoPie) {
        this.textoPie = new StringBuffer(textoPie);
    }

    public void setTextoAnulada(String textoAnulada) {
        this.textoAnulada = new StringBuffer(textoAnulada);
    }

    public void setOriginal(boolean original) {
        this.original = original;
    }
     
     public void setTextoReimpresion(String textoReimpresionCabecera) {
        this.textoReimpresionCabecera = new StringBuffer(textoReimpresionCabecera);
    }
     
      public void setTextoReimpresionDetalle(String textoReimpresionDetalle) {
        this.textoReimpresionDetalle = new StringBuffer(textoReimpresionDetalle);
    } 

}
