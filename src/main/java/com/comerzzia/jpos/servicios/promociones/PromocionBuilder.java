/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.servicios.promociones;

import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoPrecio;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoComboSubSecciones;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoDto;
import com.comerzzia.jpos.servicios.promociones.puntos.PromocionTipoPuntosAcumula;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoCombo;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoComboCategorias;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoDtoPorCumple;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoNxM;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoNxMSimple;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoDtoPorCant;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoDtoPorImporte;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoComboSecciones;
import com.comerzzia.jpos.persistencia.promociones.PromocionBean;
import com.comerzzia.jpos.servicios.promociones.cupones.PromocionTipoCuponDescuento;
import com.comerzzia.jpos.servicios.promociones.cupones.PromocionTipoCuponRegaloProveedorExterno;
import com.comerzzia.jpos.servicios.promociones.cupones.PromocionTipoCuponSorteo;
import com.comerzzia.jpos.servicios.promociones.cupones.PromocionTipoCuponVotos;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoDiaSocio;
import com.comerzzia.jpos.servicios.promociones.cuotas.PromocionTipoMesesGracia;
import com.comerzzia.jpos.servicios.promociones.cuotas.PromocionTipoNCuotasGratis;
import com.comerzzia.jpos.servicios.promociones.cupones.PromocionTipoCuponRegaloCurso;
import com.comerzzia.jpos.servicios.promociones.cupones.PromocionTipoCuponSorteoSukasa;
import com.comerzzia.jpos.servicios.promociones.puntos.PromocionTipoPuntosCanjeo;
import com.comerzzia.jpos.servicios.promociones.cupones.PromocionTipoBilleton;
import com.comerzzia.jpos.servicios.promociones.cupones.PromocionTipoCuponDescuentoAzar;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoDescuentoCombinado;
import com.comerzzia.jpos.servicios.promociones.tipos.PromocionTipoRegaloCompra;

/**
 *
 * @author amos
 */
public class PromocionBuilder {
    public static Promocion buildPromocion(PromocionBean promocionBean) throws PromocionException{
        if(promocionBean.getTipoPromocion().isPromocionTipoPrecio()){
                return new PromocionTipoPrecio(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoDescuento()){
                return new PromocionTipoDto(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoDescuentoPorCantidad()){
                return new PromocionTipoDtoPorCant(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoNxM()){
                return new PromocionTipoNxM(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoNxMSimple()){
                return new PromocionTipoNxMSimple(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoCombo()){
                return new PromocionTipoCombo(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoDescuentoPorImporte()){
                return new PromocionTipoDtoPorImporte(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoComboCategorias()){
                return new PromocionTipoComboCategorias(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoComboSecciones()){
                return new PromocionTipoComboSecciones(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoComboSubSecciones()){
                return new PromocionTipoComboSubSecciones(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoCuponDescuento()){
                return new PromocionTipoCuponDescuento(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoCuponSorteo()){
                return new PromocionTipoCuponSorteo(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoCuponVotos()){
                return new PromocionTipoCuponVotos(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoDescuentoCumple()){
                return new PromocionTipoDtoPorCumple(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoPuntosAcumula()){
            return new PromocionTipoPuntosAcumula(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoPuntosCanjea()){
            return new PromocionTipoPuntosCanjeo(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoDiaSocio()){
            return new PromocionTipoDiaSocio(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoNCuotasGratis()){
            return new PromocionTipoNCuotasGratis(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoMesesGracia()){
            return new PromocionTipoMesesGracia(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoCuponRegaloProveedorExterno()){
                return new PromocionTipoCuponRegaloProveedorExterno(promocionBean);
        }
        //no entra
        else if(promocionBean.getTipoPromocion().isPromocionTipoCuponSorteoSukasa()){
                return new PromocionTipoCuponSorteoSukasa(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoCuponRegaloCurso()){
                return new PromocionTipoCuponRegaloCurso(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoRegaloCompra()){
                return new PromocionTipoRegaloCompra(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoBilleton()){
                return new PromocionTipoBilleton(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoDescuentoCombinado()){
                return new PromocionTipoDescuentoCombinado(promocionBean);
        }
        else if(promocionBean.getTipoPromocion().isPromocionTipoCuponDescuentoAzar()){
                return new PromocionTipoCuponDescuentoAzar(promocionBean);
        }        
        return null;
    }
    
    
}
