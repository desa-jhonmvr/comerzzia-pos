/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.correo;
import com.comerzzia.jpos.servicios.login.DatosConfiguracion;
import com.comerzzia.jpos.servicios.login.Sesion;
import es.mpsistemas.util.criptografia.CriptoException;
import es.mpsistemas.util.criptografia.CriptoUtil;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EnvioCorreo {
//
//    // Replace sender@example.com with your "From" address.de
//    // This address must be verified.
//
//    static final String FROM = "sistemas@comohogar.com";
    static final String FROMNAME = "SUKASA";
//	
//    // Replace recipient@example.com with a "To" address. If your account 
//    // is still in the sandbox, this address must be verified.para
//    static final String TO = "";
//    
//    // Replace smtp_username with your Amazon SES SMTP user name.usuario amazon
//    static final String SMTP_USERNAME = "AKIAI5JGFSTYMBHBWAQA";
//    
//    // Replace smtp_password with your Amazon SES SMTP password.conraseña amazon
//    static final String SMTP_PASSWORD = "AiRUlx1/J3jLXURBe7lvuTXGH3CgbKy2NjBh7o0Im/1k";
//    
//    // The name of the Configuration Set to use for this message.
//    // If you comment out or remove this variable, you will also need to
//    // comment out or remove the header below.
////    static final String CONFIGSET = "ConfigSet";
//    
//    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
//    // See http://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html#region-endpoints
//    // for more information.
////    static final String HOST = "email-smtp.us-west-2.amazonaws.com";//host amazon
//    static final String HOST = "email-smtp.us-east-1.amazonaws.com";//host amazon
    
    // The port you will connect to on the Amazon SES SMTP endpoint. 
    static final int PORT = 587; //puerto
    
    static final String SUBJECT = "Coméntanos tu experiencia";
   
    public static void envio(DatosCorreo correo) {
        try
        {
            System.out.println("Iniciando envio de correo");
            
//            String local=correo.getNumeroLocal();
            String local = CriptoUtil.cifrar(CriptoUtil.ALGORITMO_MD5, (""+correo.getNumeroLocal()).getBytes());
            String BODY = String.join(
    	    System.getProperty("line.separator"),
    	    "<h1>Coméntanos tu experiencia.</h1>",
    	    "<p>Ingrese al siguiente link. ", 
    	    "<a href='http://sukasa.com/encuesta/?l="+local+"'>Evaluación Venta</a>"
    	    
    	);
            // Create a Properties object to contain connection configuration information.
            Properties props = System.getProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.port", PORT);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");
            
            // Create a Session object to represent a mail session with the specified properties.
            Session session = Session.getDefaultInstance(props);
            
            // Create a message with the specified information.
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(  Sesion.getDatosConfiguracion().getDATABASE_CORREO_CORREO(),FROMNAME));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(correo.getCorreoElectronico()));
            msg.setSubject(  Sesion.getDatosConfiguracion().getDATABASE_CORREO_ASUNTO());
            msg.setContent(BODY,"text/html");
            
            // Add a configuration set header. Comment or delete the
            // next line if you are not using a configuration set
//            msg.setHeader("X-SES-CONFIGURATION-SET", CONFIGSET);
            
            // Create a transport.
            Transport transport = session.getTransport();
            
            // Send the message.
            try
            {
                System.out.println("Enviado Correo a "+correo.getCorreoElectronico()+"....");
                
                // Connect to Amazon SES using the SMTP username and password you specified above.
                transport.connect(Sesion.getDatosConfiguracion().getDATABASE_CORREO_HOST(),  Sesion.getDatosConfiguracion().getDATABASE_CORREO_USUARIO(),  Sesion.getDatosConfiguracion().getDATABASE_CORREO_PASSWORD());
                
                // Send the email.
                transport.sendMessage(msg, msg.getAllRecipients());
                System.out.println("Correo Enviado!");
            }
            catch (Exception ex) {
                System.out.println("El correo no se envio .");
                System.out.println("Error message: " + ex.getMessage());
            }
            finally
            {
                // Close and terminate the connection.
                transport.close();
            }
        }
        catch (UnsupportedEncodingException ex) {
             System.out.println("Error En El Envio De Correo.");
            Logger.getLogger(EnvioCorreo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(EnvioCorreo.class.getName()).log(Level.SEVERE, null, ex);
       } catch (CriptoException ex) {
            Logger.getLogger(EnvioCorreo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
