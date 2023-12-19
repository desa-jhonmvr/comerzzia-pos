
import com.comerzzia.jpos.entity.db.Usuarios;
import com.comerzzia.jpos.persistencia.core.usuarios.UsuariosDao;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.pagos.credito.ServicioCodigoOTP;
import com.comerzzia.jpos.webservice.credito.CreditoDao;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gabriel Simbania
 */
public class ValidacionOTTest {

    public static void main(String[] args) {

        EntityManager em = null;
        try {
            ClaseConfiguracionTest.leerConfiguracion();

            EntityManagerFactory emf = Sesion.getEmf();
            em = emf.createEntityManager();
            em.getTransaction().begin();
            
            String uidTicket = UUID.randomUUID().toString();
            Usuarios usuario = UsuariosDao.obtenerUsuarioporNumero("70739");
            //ServicioCodigoOTP.enviarOTP(numeroTelefono, 181643L, usuario, uidTicket, em);
            
            ServicioCodigoOTP.validarOTP(850708L, "25c5e246-95fe-4615-888d-2b5cd5efb5d7", em);
            
            
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        System.exit(0);
    }

}
