package com.comerzzia.jpos.persistencia.promociones;

import com.comerzzia.util.base.MantenimientoBean;
import es.mpsistemas.util.fechas.Fecha;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PromocionBean extends MantenimientoBean {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1039810621142136368L;
    private Long idPromocion;
    private String codTar;
    private String desTar;
    private String descripcion;
    private Fecha fechaInicio;
    private Fecha fechaFin;
    private TipoPromocionBean tipoPromocion;
    private Long versionTarifa;
    private String textoPromocion;
    private byte[] datosPromocion;

    public PromocionBean() {
    }

    public PromocionBean(ResultSet rs) throws SQLException {
        setIdPromocion(rs.getLong("ID_PROMOCION"));
        setDescripcion(rs.getString("DESCRIPCION"));
        setCodTar(rs.getString("CODTAR"));
        setDesTar(rs.getString("DESTAR"));
        setFechaInicio(new Fecha(rs.getTimestamp("FECHA_INICIO")));
        setFechaFin(new Fecha(rs.getTimestamp("FECHA_FIN")));
        setTipoPromocion(new TipoPromocionBean(rs.getLong("ID_TIPO_PROMOCION"), rs.getString("DESTIPOPROMOCION")));
        setVersionTarifa((rs.getString("VERSION_TARIFA") != null) ? rs.getLong("VERSION_TARIFA") : null);
        setTextoPromocion(rs.getString("TEXTO_PROMOCION"));
        setDatosPromocion(rs.getBytes("DATOS_PROMOCION"));

    }

    @Override
    protected void initNuevoBean() {
    }

    public Long getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion(Long idPromocion) {
        this.idPromocion = idPromocion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Fecha getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Fecha fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Fecha getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Fecha fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getCodTar() {
        return codTar;
    }

    public void setCodTar(String codTar) {
        this.codTar = codTar;
    }

    public String getDesTar() {
        return desTar;
    }

    public void setDesTar(String desTar) {
        this.desTar = desTar;
    }

    public Long getIdTipoPromocion() {
        return tipoPromocion.getIdTipoPromocion();
    }

    public Long getVersionTarifa() {
        return versionTarifa;
    }

    public void setVersionTarifa(Long versionTarifa) {
        this.versionTarifa = versionTarifa;
    }

    public boolean isActiva() {
        return versionTarifa != null;
    }

    public boolean isFinalizada() {
        if (fechaFin == null) {
            return false;
        }
        return new Fecha().despues(fechaFin);
    }

    public String getDesTipoPromocion() {
        return tipoPromocion.getDesTipoPromocion();
    }

    public void setTipoPromocion(TipoPromocionBean tipoPromocion) {
        this.tipoPromocion = tipoPromocion;
    }

    public String getTextoPromocion() {
        return textoPromocion;
    }

    public void setTextoPromocion(String textoPromocion) {
        this.textoPromocion = textoPromocion;
    }

    public byte[] getDatosPromocion() {
        return datosPromocion;
    }

    public void setDatosPromocion(byte[] datosPromocion) {
        this.datosPromocion = datosPromocion;
    }

    public TipoPromocionBean getTipoPromocion() {
        return tipoPromocion;
    }

    

}
