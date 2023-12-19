package com.comerzzia.util.marshall;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

public class MarshallUtil {
	private static final Logger log = Logger.getLogger(MarshallUtil.class.getName());
	 
    /** Serializa un objeto en un XML
     * @param obj Objeto que se va a serializar
     * @param clase Clase del objeto del que se va a serializar
     * @return byte[]
     * @throws com.comerzzia.util.marshall.MarshallUtilException
     */
    public static byte[] crearXML(Object obj,Class clase) throws MarshallUtilException {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JAXBContext jaxbContext = JAXBContext.newInstance(clase);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(obj, out);
            return out.toByteArray();
        } 
        catch (JAXBException e) {
            log.error("crearXML() - Error creando XML: " + e.getMessage(), e);
            throw new MarshallUtilException(e.getMessage());
        }
    }
 
    /**
     * Desserializa un objeto desde una cadena
     * @param cadenaXML
     * @param clase
     * @return 
     */
    public static Object leerXML(byte[] cadenaXML, Class clase) {
        Object res = null;
        try {
            String stringXML = new String(cadenaXML, "UTF-8");
            JAXBContext jaxbContext = JAXBContext.newInstance(clase);           
            res =  jaxbContext.createUnmarshaller().unmarshal(new StringReader(stringXML));            
        } catch (JAXBException e) {
            log.error("crearXML() - Error creando XML"+e.getMessage(), e);
        }
        catch (UnsupportedEncodingException ex) {
            log.error("crearXML() - Problema con el encoding al leer cadena. Se esperaba UTF-8", ex);
        }
        return res;
    }

	
}
