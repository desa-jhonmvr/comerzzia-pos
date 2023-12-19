/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.entity.ticket;

import com.comerzzia.util.numeros.bigdecimal.Numero;
import es.mpsistemas.util.log.Logger;

/**
 *
 * @author MGRI
 */
public class IdentificadorTicket {
    
    private String codalm;
    private String codcaja;
    private Long idDocumento;
    
    private static Logger log = Logger.getMLogger(IdentificadorTicket.class);

    public IdentificadorTicket(String documentoImpreso) throws IDTicketMalFormadoException {
        String codAlm = null;
        String codCaja = null;
        String idDocumento = null;
                
        if (documentoImpreso ==null){
           throw new IDTicketMalFormadoException("Ha de indicar un cocumento impreso válido con formato xxx-xx-xxxxxxxx");
        }
        // minimo de longitud
        String[] doc = documentoImpreso.split("-");
        if (doc.length<3){
            log.debug("documento invalido "+ codCaja);
            throw new IDTicketMalFormadoException("Ha de indicar un cocumento impreso válido con formato xxx-xx-xxxxxxxx");
        }
        codAlm = doc[0];
        if (codAlm.length()>=1 && codAlm.length()<=3){
           this.codalm=Numero.completaconCeros(codAlm, 3);
        }
        else{
            log.debug("almacén invalido "+ codCaja);
            throw new IDTicketMalFormadoException("almacén invalido");
        }
        codCaja = doc[1];
        if (codCaja.length()>=1 && codCaja.length()<=2){
           this.codcaja=Numero.completaconCeros(codCaja, 2);
        }
        else{
            log.debug("caja invalida "+ codCaja);
            throw new IDTicketMalFormadoException("caja invalida");
        }
        idDocumento = doc[2];
        if (idDocumento.length()>=1 && idDocumento.length()<=8){
           try{
                this.idDocumento= new Long(idDocumento);
           }
           catch (NumberFormatException e){
               log.debug("documento invalido "+ idDocumento);
               throw new IDTicketMalFormadoException("id de documento invalido");
           }
        }
        else{
            log.debug("documento invalido "+ idDocumento);
            throw new IDTicketMalFormadoException("id de documento invalido");
        }
                
    }   
       
    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

    public String getCodcaja() {
        return codcaja;
    }

    public void setCodcaja(String codcaja) {
        this.codcaja = codcaja;
    }

    public Long getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Long idDocumento) {
        this.idDocumento = idDocumento;
    }
    
    
}
