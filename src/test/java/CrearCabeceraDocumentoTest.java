
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosBean;
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosBean;
import com.comerzzia.jpos.servicios.login.LecturaConfiguracion;
import com.comerzzia.jpos.servicios.login.Sesion;
import com.comerzzia.jpos.servicios.print.documentos.DocumentosService;
import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gabriel Simbania
 */
public class CrearCabeceraDocumentoTest {

    public static void main(String[] args) {

        String tipoDocumentoImpreso = DocumentosImpresosBean.TIPO_FACTURA;
                                      
        try {
            ClaseConfiguracionTest.leerConfiguracion();
            // Leer los datos de sesión de la base de datos
            LecturaConfiguracion.leerDatosSesion(true);

            String codCaja = "014";
            String codAlmacen = "021";
            String idDocumento = "63960";
            String codCliente = "1004677892001";
            BigDecimal monto = new BigDecimal("1085.39");
            String usuario = "80016";
            String observacion="";
            
            String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><output>    <ticket>		<line><cabecera align=\"center\" length=\"40\" bold=\"true\"></cabecera></line>                <line>                    <text align=\"center\" length=\"40\">COMOHOGAR S.A</text>		</line>		<line>			<text align=\"center\" length=\"40\">TODOHOGAR IBARRA</text>		</line>					<line><text align=\"center\" length=\"40\" >AV. MARIANO ACOSTA 2297 Y V. GOMEZ</text> </line>					<line><text align=\"center\" length=\"40\" >JURADO C.C. PLAZA SHOPING</text> </line>		                                    <line>                            <text align=\"center\" length=\"40\">Telefono:  062631028 - 062631055</text>                    </line>                		<line>			<text align=\"center\" length=\"40\">R.U.C: 1790746119001</text>		</line>                                                <line>                        <text align=\"center\" length=\"40\">000000811461 - 40228529 - CHIP</text>                </line>                <line>                        <text align=\"center\" length=\"40\" bold=\"true\">MASTERCARD</text>                </line>                                <line>                        <text algin=\"left\" length=\"40\">MAST GUAYAQUIL </text>                </line>	                                <line>                        <text align=\"left\" length=\"40\">TARJETA : 514440 XXXXXXX 009 </text>                </line>                                <line>                        <text align=\"left\" length=\"6\">LOTE: </text>                        <text align=\"left\" length=\"6\">001020</text>                        <text align=\"right\" length=\"22\">Ref: </text>                        <text align=\"left\" legth=\"6\">000014</text>                </line>                 <line>                        <text align=\"left\" length=\"13\">ADQUIRIENTE: </text>                        <text align=\"right\" length=\"27\">MEDIANET</text>                </line>                                                  <line>                        <text align=\"left\" length=\"16\">FECHA: 20/04/21</text>                        <text align=\"right\" length=\"24\">HORA: 17:58</text>                </line>                             		<line>			<text align=\"left\" length=\"14\">Transaccion : </text>			<text align=\"left\" length=\"26\">021-014-000063960</text>		</line>                		                    <line>                            <text align=\"center\" length=\"40\">APROBACION: 522689</text>                    </line>		                <line>                        <text align=\"left\" length=\"15\">BASE 12%  :US$ </text>                        <text align=\"right\" length=\"25\">969.10</text>                </line>                <line>                        <text align=\"left\" length=\"15\">BASE 0%   :US$ </text>                        <text align=\"right\" length=\"25\">0.00</text>                </line>                <line>                        <text align=\"left\" length=\"15\">SUBTOTAL  :US$ </text>                        <text align=\"right\" length=\"25\">969.10</text>                                    </line>                <line>                        <text align=\"left\" length=\"15\">IVA  12%  :US$ </text>                        <text align=\"right\" length=\"25\">116.29</text>                </line>                <line>                        <text align=\"left\" length=\"15\">Total     :US$ </text>                        <text align=\"right\" length=\"25\">1085.39</text>                </line>                <line>                        <text align=\"center\" length=\"40\"></text>                </line>                                                                                                                             <line><original align=\"center\" length=\"40\">CAP ELEC DATAFAST</original></line>                                                                                                                                                        <line><original align=\"left\" length=\"40\" >DEBO  Y  PAGARE  INCONDICIONALMENTE,   Y</original> </line>                    <line><original align=\"left\" length=\"40\" >SIN  PROTESTO  EL  TOTAL  DE ESTE PAGARE</original> </line>                    <line><original align=\"left\" length=\"40\" >MAS LOS INTERESES Y CARGOS POR SERVICIO,</original> </line>                    <line><original align=\"left\" length=\"40\" >EN CASO DE  MORA  PAGARE LA TASA  MAXIMA</original> </line>                    <line><original align=\"left\" length=\"40\" >AUTORIZADA PARA EL EMISOR.</original> </line>                    <line><original align=\"left\" length=\"40\" >DECLARO  QUE  EL  PRODUCTO  DE  LA TRAN-</original> </line>                    <line><original align=\"left\" length=\"40\" >SACCION NO SERA UTILIZADO EN ACTIVIDADES</original> </line>                    <line><original align=\"left\" length=\"40\" >DE LAVADO DE ACTIVOS, FINANCIAMIENTO DEL</original> </line>                    <line><original align=\"left\" length=\"40\" >TERRORISMO Y OTROS DELITOS.</original> </line>                                                                      <line>                    <text align=\"left\" length=\"40\">NOMBRE: MASTERCARD/DEBIT</text>                </line>                                        <line/>                    <line><original align=\"left\" length=\"40\" >___________________________________</original> </line>                    <line><original align=\"left\" length=\"40\">EL ESTABLECIMIENTO VERIFICA QUE LA </original></line>                    <line><original align=\"left\" length=\"40\">FIRMA DEL CLIENTE ES AUTENTICA</original></line>                       <line/>                                 <line><original align=\"left\" length=\"40\" >___________________________________</original> </line>                    <line><original align=\"left\" length=\"40\" >C.I.                               </original> </line>                    <line><original align=\"left\" length=\"40\" >___________________________________</original> </line>                    <line><original align=\"left\" length=\"40\" >Telefono                           </original> </line>                                                   <line/>                    <line/>                                                                                                  <line>                             <original align=\"left\" length=\"20\">DEBIT MASTERCARD    </original>                            </line>                                                                                                                  <line>                             <original align=\"left\" length=\"20\">AID : A0000000041010      </original>                            </line>                                                                                                                               <line/>                 <line><pie align=\"center\" length=\"40\" bold=\"true\"></pie></line>            		<line/>                                                                     <line><text align=\"left\" length=\"40\" ></text> </line>                                                <line><text align=\"left\" length=\"40\" >                                       </text> </line>                                        </ticket></output>";
            
            

            DocumentosBean documentoBean = new DocumentosBean();
            documentoBean.setUidDocumento(UUID.randomUUID().toString());
            documentoBean.setTipo(tipoDocumentoImpreso);
            documentoBean.setCodAlmacen(codAlmacen);
            documentoBean.setCodCaja(codCaja);
            documentoBean.setIdDocumento(idDocumento);
            documentoBean.setFecha(new Fecha());
            documentoBean.setCodCliente(codCliente);
            documentoBean.setMonto(monto);
            documentoBean.setEstado("V");
            documentoBean.setUsuario(usuario);
            documentoBean.setObservaciones(observacion);

            Fecha caducidad = documentoBean.getFecha();
            caducidad.sumaAños(1);
            
            List<DocumentosImpresosBean> documentoList = new ArrayList<DocumentosImpresosBean>();
            DocumentosImpresosBean documentosImpresosBean= new DocumentosImpresosBean();
            documentosImpresosBean.setImpreso(xml.getBytes());
            documentosImpresosBean.setUidDocumento(documentoBean.getUidDocumento());
            documentosImpresosBean.setTipoImpreso(tipoDocumentoImpreso);
            documentosImpresosBean.setIdImpreso((short)0);
            documentoList.add(documentosImpresosBean);
            
            documentoBean.setImpresos(documentoList);
            documentoBean.setNumTransaccion(documentoBean.getCodAlmacen() + "-" + documentoBean.getCodCaja() + "-" + documentoBean.getIdDocumento());
            documentoBean.setFechaCaducidad(caducidad);
            documentoBean.setCodCajaEmision(Sesion.getTienda().getCajaActiva().getCodcajaSri());
            DocumentosService.crearDocumentoTest(documentoBean, tipoDocumentoImpreso);

        } catch (Exception ex) {
            Logger.getLogger(CrearCabeceraDocumentoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }

}
