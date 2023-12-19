/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.util.exception;

import com.comerzzia.jpos.dto.ResponseDTO;

/**
 *
 * @author Gabriel Simbania
 */
public class SocketTPVException extends com.comerzzia.util.base.Exception{
    
    private static final long serialVersionUID = 5331140724812828372L;

    private ResponseDTO responseDTO;
    
    public SocketTPVException() {
    }
    
    public SocketTPVException(ResponseDTO responseDTO) {
        this.responseDTO = responseDTO;
    }
    
    public SocketTPVException(String codigo, String descripcion) {
        this.responseDTO = new ResponseDTO(Boolean.FALSE,codigo,descripcion);
    }

    public SocketTPVException(String msg) {
        super(msg);
    }

    public SocketTPVException(String msg, Throwable e) {
        super(msg, e);
    }

    public SocketTPVException(String msg, String msgKey, Throwable e) {
        super(msg, msgKey, e);
    }

    public ResponseDTO getResponseDTO() {
        return responseDTO;
    }

    public void setResponseDTO(ResponseDTO responseDTO) {
        this.responseDTO = responseDTO;
    }
    
    
    
}
