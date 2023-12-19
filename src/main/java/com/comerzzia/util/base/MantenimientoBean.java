package com.comerzzia.util.base;

import java.io.Serializable;

/**
 * Mantenimiento básico de los datos de un registro
 *
 */
public abstract class MantenimientoBean implements Serializable {

    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 5341599003382464139L;
    /**
     * Definición de constante booleana verdadera como cadena
     */
    protected static final String TRUE = "S";
    /**
     * Definición de constante booleana falsa como cadena
     */
    protected static final String FALSE = "N";
    /**
     * Indica si el registro está activo o no
     */
    private boolean activo = true;
    /**
     * Estado del registro
     */
    private int estadoBean = Estado.SIN_MODIFICAR;
    /**
     * Indica si el registro está en edición o no
     */
    private boolean enEdicion = false;

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo.equals(TRUE);
    }

    public String getActivo() {
        return (activo) ? TRUE : FALSE;
    }

    public void setEstadoBean(int estadoBean) {
        this.estadoBean = estadoBean;

        if (estadoBean == Estado.NUEVO) {
            initNuevoBean();
        }
    }

    public int getEstadoBean() {
        return estadoBean;
    }

    public boolean isEstadoBorrado() {
        return estadoBean == Estado.BORRADO;
    }

    public boolean isEstadoNuevo() {
        return estadoBean == Estado.NUEVO;
    }

    public void setEnEdicion(boolean enEdicion) {
        this.enEdicion = enEdicion;
    }

    public boolean isEnEdicion() {
        return enEdicion;
    }

    protected abstract void initNuevoBean();
}
