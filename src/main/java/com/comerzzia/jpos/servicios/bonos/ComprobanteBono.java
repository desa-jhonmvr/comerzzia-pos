/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.bonos;

import com.comerzzia.jpos.entity.codigosBarra.CodigoBarrasBonoEfectivo;
import com.comerzzia.jpos.entity.db.Bono;
import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.persistencia.core.usuarios.UsuarioBean;
import com.comerzzia.jpos.servicios.login.Sesion;

/**
 *
 * @author MGRI
 */
public class ComprobanteBono {

    private Bono bono;
    private CodigoBarrasBonoEfectivo codigoBarras;
    private Cliente cliente;
    private UsuarioBean cajero;
    private String tienda;

    public ComprobanteBono(Bono bono, CodigoBarrasBonoEfectivo codigoBarras, Cliente cliente) {
        this.bono = bono;
        this.codigoBarras = codigoBarras;
        this.cliente = cliente;        
        this.cajero=Sesion.getUsuario();
        String tiendaS = Sesion.getTienda().getAlmacen().getDesalm();
        if (tiendaS.length()>40){
            tiendaS.substring(0, 40);
        }
        this.tienda=tiendaS;
    }

    public Bono getBono() {
        return bono;
    }

    public void setBono(Bono bono) {
        this.bono = bono;
    }

    public CodigoBarrasBonoEfectivo getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(CodigoBarrasBonoEfectivo codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public UsuarioBean getCajero() {
        return cajero;
    }

    public void setCajero(UsuarioBean cajero) {
        this.cajero = cajero;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }
}
