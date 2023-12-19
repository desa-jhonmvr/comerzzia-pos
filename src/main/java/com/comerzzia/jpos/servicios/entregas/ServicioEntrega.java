/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.entregas;

import com.comerzzia.jpos.dto.ItemDTO;
import com.comerzzia.jpos.entity.db.LineaTicketOrigen;
import com.comerzzia.jpos.entity.db.TicketsAlm;
import com.comerzzia.jpos.servicios.tickets.TicketException;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 *
 * @author Gabriel Simbania
 */
public interface ServicioEntrega {

    /**
     * Cambia el estado a entregado
     *
     * @author Gabriel Simbania
     * @param numeroDocumento
     * @param observacion
     * @throws TicketException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws TransformerException
     * @throws Exception
     */
    void cambiaEstadoEntregado(String numeroDocumento, String observacion) throws TicketException, IOException, ParserConfigurationException, SAXException, TransformerException, Exception;

    /**
     * Realiza el proceso de cambio de estado
     *
     * @author Gabriel Simbania
     * @param deNadaAPendienteEnvioDomicilio
     * @param dePendienteEnvioDomicilioARecogidaPosterior
     * @param deRecogidaPosteriorAEntregadoRecogidaPosterior
     * @param deEntregadoRecogidaPosteriorANada
     * @param codAlm
     * @param codCaja
     * @param idDocumento
     * @param aplicativo
     * @param itemDTOLista
     * @param observacion
     * @throws TicketException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    void procesaCambioEnvioDomicilio(List<LineaTicketOrigen> deNadaAPendienteEnvioDomicilio,
            List<LineaTicketOrigen> dePendienteEnvioDomicilioARecogidaPosterior,
            List<LineaTicketOrigen> deRecogidaPosteriorAEntregadoRecogidaPosterior,
            List<LineaTicketOrigen> deEntregadoRecogidaPosteriorANada,
            String codAlm, String codCaja, Long idDocumento, boolean aplicativo,
            List<ItemDTO> itemDTOLista, String observacion) throws TicketException, IOException, ParserConfigurationException, SAXException, TransformerConfigurationException, TransformerException;

    /**
     * Guarda el log de la auditoria
     *
     * @author Gabriel Simbania
     * @param observacion
     * @param referencia
     * @param itemDTOLista
     * @param lista
     * @param ticket
     */
    void guardarLog(String observacion, String referencia, List<ItemDTO> itemDTOLista,
            List<LineaTicketOrigen> lista, TicketsAlm ticket);

    /**
     *
     * @author Gabriel Simbania
     * @param deNadaAPendienteEnvioDomicilio
     * @param dePendienteEnvioDomicilioARecogidaPosterior
     * @param deRecogidaPosteriorAEntregadoRecogidaPosterior
     * @param deEntregadoRecogidaPosteriorANada
     * @param codAlm
     * @param codCaja
     * @param idDocumento
     * @throws Exception
     */
    void actualizarDocumentoImpreso(List<LineaTicketOrigen> deNadaAPendienteEnvioDomicilio,
            List<LineaTicketOrigen> dePendienteEnvioDomicilioARecogidaPosterior,
            List<LineaTicketOrigen> deRecogidaPosteriorAEntregadoRecogidaPosterior,
            List<LineaTicketOrigen> deEntregadoRecogidaPosteriorANada,
            String codAlm, String codCaja, Long idDocumento) throws Exception;

}
