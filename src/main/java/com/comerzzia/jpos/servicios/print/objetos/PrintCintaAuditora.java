/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.print.objetos;


import com.comerzzia.jpos.entity.db.XCintaAuditoraItemTbl;
import com.comerzzia.jpos.entity.db.XCintaAuditoraTbl;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Sistemas
 */
public class PrintCintaAuditora extends PrintDocument
{
    public String titulo;
    public String caja;    
    public String fecha;
    public String almacen;
    public String usuario;
    public String autorizador;
    public String fechaInicio;
    public String fechaFin;
    public String totales;
    
    public List<ItemCintaAuditora> itemsCintaAuditora;
    
    public PrintCintaAuditora()
    {
    }
    
    public void construir(XCintaAuditoraTbl cintaAuditoraDB)
    {
        this.titulo = "REPORTE CINTA AUDITORA";
        this.caja = cintaAuditoraDB.getXCintaAuditoraTblPK().getCodcaja();
        this.almacen = cintaAuditoraDB.getXCintaAuditoraTblPK().getCodalm();
        this.autorizador = cintaAuditoraDB.getAutorizador();
        this.usuario = cintaAuditoraDB.getUsuario();
        Format formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.fecha = formatter.format(cintaAuditoraDB.getFecha());
        
        if (cintaAuditoraDB.getFechaInicio() != null) {
            this.fechaInicio = formatter.format(cintaAuditoraDB.getFechaInicio());
        }
        else
        {
            this.fechaInicio = "-";
        }
        if (cintaAuditoraDB.getFechaFin() != null) {
            this.fechaFin = formatter.format(cintaAuditoraDB.getFechaFin());
        }
        else
        {
            this.fechaFin = "-";
        }
        
        
        
        
        List<ItemCintaAuditora> respuestaItemCintaAuditora = new ArrayList<ItemCintaAuditora>();
        ItemCintaAuditora itemX = null;
        
        Iterator<XCintaAuditoraItemTbl> it = cintaAuditoraDB.getXCintaAuditoraItemTblCollection().iterator();
	Double suma = new Double(0);
        while(it.hasNext())
	{
            XCintaAuditoraItemTbl i = it.next();
            
            itemX = new ItemCintaAuditora();            
            itemX.setNombre(i.getNombre());
            itemX.setValor(i.getValor());
            double valorSumar = Double.parseDouble(itemX.getValor());
            suma=suma+valorSumar;
            respuestaItemCintaAuditora.add(itemX);
	}
        System.out.println(suma);
        totales=""+suma;
        /*
        for (int i = 0; i < cintaAuditoraDB.getXCintaAuditoraItemTblCollection().size(); i++)
        {
            itemX = new ItemCintaAuditora();
            itemX.setNombre(cintaAuditoraDB.getXCintaAuditoraItemTblCollection()..getNombre());
            itemX.setValor(cintaAuditoraDB.getXCintaAuditoraItemTblCollection()..getValor());
            
            respuestaItemCintaAuditora.add(itemX);
        }
        */
        
        itemsCintaAuditora = respuestaItemCintaAuditora;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCaja() {
        return caja;
    }

    public String getAlmacen() {
        return almacen;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getAutorizador() {
        return autorizador;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<ItemCintaAuditora> getItemsCintaAuditora() {
        return itemsCintaAuditora;
    }

    public void setItemsCintaAuditora(List<ItemCintaAuditora> itemsCintaAuditora) {
        this.itemsCintaAuditora = itemsCintaAuditora;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getTotales() {
        return totales;
    }

    public void setTotales(String totales) {
        this.totales = totales;
    }

   
    
}
