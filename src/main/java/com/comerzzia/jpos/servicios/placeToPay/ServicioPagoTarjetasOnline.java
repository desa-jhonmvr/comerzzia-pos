/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.placeToPay;

import com.comerzzia.jpos.dto.ventas.paginaweb.paymentez.CardDTO;
import com.comerzzia.jpos.dto.ventas.paginaweb.paymentez.DebitRequestDTO;
import com.comerzzia.jpos.dto.ventas.paginaweb.paymentez.OrderDTO;
import com.comerzzia.jpos.dto.ventas.paginaweb.paymentez.UserDTO;
import com.comerzzia.jpos.dto.ventas.paginaweb.placeToPay.Amount;
import com.comerzzia.jpos.dto.ventas.paginaweb.placeToPay.CollectRequest;
import com.comerzzia.jpos.dto.ventas.paginaweb.placeToPay.Credit;
import com.comerzzia.jpos.dto.ventas.paginaweb.placeToPay.Instrument;
import com.comerzzia.jpos.dto.ventas.paginaweb.placeToPay.PaymentRequest;
import com.comerzzia.jpos.dto.ventas.paginaweb.placeToPay.Person;
import com.comerzzia.jpos.dto.ventas.paginaweb.placeToPay.SimpleToken;
import com.comerzzia.jpos.dto.ventas.paginaweb.placeToPay.TaxDetail;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.servicios.core.variables.Variables;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.tickets.TicketS;
import com.comerzzia.jpos.util.JsonUtil;
import com.comerzzia.jpos.util.enums.catalogo.EnumDocumentType;
import com.google.gson.JsonObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel Simbania
 */
public class ServicioPagoTarjetasOnline {

    public ServicioPagoTarjetasOnline() {
    }

    /**
     * @author Gabriel Simbania
     * @param ticketS
     * @param tokenPlaceToPay
     * @param numCuotas
     * @param uidCabId
     * @return 
     */
    public CollectRequest crearCollectRequest(TicketS ticketS, String tokenPlaceToPay, Long numCuotas, String uidCabId) {

        CollectRequest collectRequest = new CollectRequest();

        //Payet
        Cliente cliente = ticketS.getCliente();
        Person payer = new Person();
        payer.setName(cliente.getNombre());
        payer.setSurname(cliente.getApellido());
        payer.setEmail(cliente.getEmail());
        payer.setDocument(cliente.getIdentificacion());
        EnumDocumentType documentType = EnumDocumentType.findDocumentTypeByCodigoPOS(cliente.getTipoIdentificacion());
        payer.setDocumentType(documentType.getCodigoPlaceToPay());
        
        TaxDetail taxDetail = new TaxDetail("valueAddedTax",ticketS.getTotales().getImpuestos());
        
        if(ticketS.getTotales().getSubtotal12().compareTo(BigDecimal.ZERO)!=0){
            taxDetail.setBase(ticketS.getTotales().getSubtotal12());
        }else{
            taxDetail.setBase(BigDecimal.ZERO);
        }
        List<TaxDetail> taxes=new ArrayList<>();
        taxes.add(taxDetail);
        
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(ticketS.getTotales().getTotalPagado());
        amount.setTaxes(taxes);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setReference(ticketS.getTienda()+"-"+ticketS.getCabPrefactura().getCabIdPedido());
        paymentRequest.setDescription("Pago para la factura " + ticketS.getTienda() + ticketS.getCodcaja() + ticketS.getId_ticket());
        paymentRequest.setAmount(amount);
        
        String tipoPago = "C";//G.S. Si es pago corriente
        if(numCuotas>1){
            tipoPago = Variables.getVariable(Variables.TIPO_PAGO_PLACE_PAY);
        }
        
        Instrument instrument = new Instrument(new SimpleToken(tokenPlaceToPay),
        new Credit("1", "02", tipoPago, String.valueOf(numCuotas)));
        
        collectRequest.setPayer(payer);
        collectRequest.setPayment(paymentRequest);
        collectRequest.setInstrument(instrument);

        return collectRequest;
    }
    
    /**
     * @author Gabriel Simbania
     * @param ticketS
     * @param tokenPaymentez
     * @param cabIdPedido
     * @param numCuotas
     * @param uidCabId
     * @return 
     */
    public DebitRequestDTO  crearDebitRequest(TicketS ticketS, String tokenPaymentez, String cabIdPedido, Long numCuotas,
            String uidCabId){
        
        DebitRequestDTO debitRequestDTO= new DebitRequestDTO();
        
        String id= JsonUtil.getElementJson(tokenPaymentez, "id");
        String email= JsonUtil.getElementJson(tokenPaymentez, "email");
        String token= JsonUtil.getElementJson(tokenPaymentez, "token");
        
        UserDTO userDTO= new UserDTO(id, email);
        
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setAmount(ticketS.getTotales().getTotalAPagar());
        orderDTO.setDescription("Pago para la factura " + uidCabId);
        orderDTO.setDev_reference(cabIdPedido);
        orderDTO.setVat(ticketS.getTotales().getImpuestos());
        orderDTO.setTax_percentage(BigDecimal.ZERO);
        
        if(ticketS.getTotales().getSubtotal12().compareTo(BigDecimal.ZERO)!=0){
            orderDTO.setTax_percentage(Sesion.getEmpresa().getPorcentajeIva());
            orderDTO.setTaxable_amount(ticketS.getTotales().getSubtotal12());
        }else{
            orderDTO.setTax_percentage(BigDecimal.ZERO);
            orderDTO.setTaxable_amount(BigDecimal.ZERO);
        }
        
        if(numCuotas>1){
            orderDTO.setInstallments(numCuotas.intValue());
            String tipoDiferido = Variables.getVariable(Variables.TIPO_PAGO_PLACE_PAYMENTEZ);
            orderDTO.setInstallments_type(Integer.parseInt(tipoDiferido));
        }
        
        CardDTO cardDTO = new CardDTO(token);
        
        debitRequestDTO.setUser(userDTO);
        debitRequestDTO.setOrder(orderDTO);
        debitRequestDTO.setCard(cardDTO);
        
        return debitRequestDTO;
    }
}
