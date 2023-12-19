/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.correo;

import es.mpsistemas.util.criptografia.CriptoException;
import es.mpsistemas.util.criptografia.CriptoUtil;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author CONTABILIDAD
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        DatosCorreo a=new DatosCorreo();
//        a.setCorreoElectronico("rdelgado@sukasa.com");
//        a.setNumeroLocal("020");
//        EnvioCorreo.envio(a);
       String locales[]={"Portoviejo-026","Manta-014","Recreo-024","Machala-030","Chillos-020","Eloy Alfaro-023","Condado-022","CityMall-028","Ibarra-021","Loja-015","Esmeraldas-025","Quicentro Sur-027","Riobamba-032","Ambato-033"};
       String locales2[]={"SK CUENCA-002","SK  SCALA-031","SK BOSQUE-001","SK JARDIN-003","SK GUAYAQUIL-010"};
       System.out.println("TODO HOGAR");
       for(int i=0;i<locales.length;i++){
           try {
               String[] todoholar = locales[i].split("-");
               String local = CriptoUtil.cifrar(CriptoUtil.ALGORITMO_MD5, (todoholar[1]).getBytes());
               System.out.println(todoholar[0]+"-"+todoholar[1]+"-md5-"+local);
           } catch (CriptoException ex) {
               Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
           }
           
       }
        System.out.println("SUKASA");
          for(int i=0;i<locales2.length;i++){
           try {
               String[] todoholar2 = locales2[i].split("-");
               String local = CriptoUtil.cifrar(CriptoUtil.ALGORITMO_MD5, (todoholar2[1]).getBytes());
               System.out.println(todoholar2[0]+"-"+todoholar2[1]+"-md5-"+local);
           } catch (CriptoException ex) {
               Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
           }
           
       }
    
    }
    
}
