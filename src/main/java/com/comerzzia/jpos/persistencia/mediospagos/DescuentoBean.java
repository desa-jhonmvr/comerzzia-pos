package com.comerzzia.jpos.persistencia.mediospagos;

import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DescuentoBean {

    /**
     *
     */
    private Long codTipoCliente;
    private Long idMedioPagoVencimiento;
    private String codMedioPago;
    private BigDecimal dtoAfiliados = BigDecimal.ZERO;
    private BigDecimal dtoAfiliadosPromo = BigDecimal.ZERO;
    private BigDecimal dtoNoAfiliados = BigDecimal.ZERO;
    private BigDecimal dtoEspecialAfiliados = BigDecimal.ZERO;
    private BigDecimal dtoEspecialNoAfiliados = BigDecimal.ZERO;
    private Fecha dtoEspecialFechaInicio;
    private Fecha dtoEspecialFechaFin;
    private BigDecimal interesAfiliado = BigDecimal.ZERO;
    private BigDecimal interesNoAfiliado = BigDecimal.ZERO;
    private BigDecimal interesEspecialAfiliado = BigDecimal.ZERO;
    private BigDecimal interesEspecialNoAfiliado = BigDecimal.ZERO;
    private BigDecimal pisoMinimo = BigDecimal.ZERO;
    private boolean descuentoEspecial;
    private boolean activo;

    public DescuentoBean(ResultSet rs) throws SQLException {
        idMedioPagoVencimiento = rs.getLong("ID_MEDPAG_VEN");
        codTipoCliente = rs.getLong("COD_TIPO_CLIENTE");
        codMedioPago = rs.getString("CODMEDPAG");
        activo = rs.getString("ACTIVO") == null || rs.getString("ACTIVO").equals("S");
        pisoMinimo = rs.getBigDecimal("PISO_MINIMO");

        if (rs.getTimestamp("FECHA_HORA_DESDE") == null) { // no es un descuento especial
            dtoAfiliados = rs.getBigDecimal("DESCUENTO_AFILIADO");
            dtoAfiliadosPromo = rs.getBigDecimal("DESCUENTO_AF_PROMO");
            dtoNoAfiliados = rs.getBigDecimal("DESCUENTO_NO_AFILIADO");
            interesAfiliado = rs.getBigDecimal("INTERES_AFILIADO");
            interesNoAfiliado = rs.getBigDecimal("INTERES_NO_AFILIADO");
        } else { // es un descuento especial
            descuentoEspecial = true;
            dtoEspecialAfiliados = rs.getBigDecimal("DESCUENTO_AFILIADO");
            dtoEspecialNoAfiliados = rs.getBigDecimal("DESCUENTO_NO_AFILIADO");
            interesEspecialAfiliado = rs.getBigDecimal("INTERES_AFILIADO");
            interesEspecialNoAfiliado = rs.getBigDecimal("INTERES_NO_AFILIADO");
            dtoEspecialFechaInicio = new Fecha(rs.getTimestamp("FECHA_HORA_DESDE"));
            dtoEspecialFechaFin = new Fecha(rs.getTimestamp("FECHA_HORA_HASTA"));
        }
    }

    public void completarDescuento(DescuentoBean nuevoDescuento) {
        if (descuentoEspecial) { // si lo que teníamos es un descuento especial, el nuevo no lo es
            dtoAfiliados = nuevoDescuento.getDtoAfiliados();
            dtoAfiliadosPromo = nuevoDescuento.getDtoAfiliadosPromo();
            dtoNoAfiliados = nuevoDescuento.getDtoNoAfiliados();
            interesAfiliado = nuevoDescuento.getInteresAfiliado();
            interesNoAfiliado = nuevoDescuento.getInteresNoAfiliado();
        } else { // el nuevo será el descuento especial
            descuentoEspecial = true;
            dtoEspecialAfiliados = nuevoDescuento.getDtoEspecialAfiliados();
            dtoEspecialNoAfiliados = nuevoDescuento.getDtoEspecialNoAfiliados();
            interesEspecialAfiliado = nuevoDescuento.getInteresEspecialAfiliado();
            interesEspecialNoAfiliado = nuevoDescuento.getInteresEspecialNoAfiliado();
            dtoEspecialFechaInicio = nuevoDescuento.getDtoEspecialFechaInicio();
            dtoEspecialFechaFin = nuevoDescuento.getDtoEspecialFechaFin();
        }
    }

    public String getCodMedioPago() {
        return codMedioPago;
    }

    public void setCodMedioPago(String codMedioPago) {
        this.codMedioPago = codMedioPago;
    }

    public Long getCodTipoCliente() {
        return codTipoCliente;
    }

    public void setCodTipoCliente(Long codTipoCliente) {
        this.codTipoCliente = codTipoCliente;
    }

    public BigDecimal getDtoAfiliados() {
        return dtoAfiliados;
    }

    public void setDtoAfiliados(BigDecimal dtoAfiliados) {
        this.dtoAfiliados = dtoAfiliados;
    }

    public BigDecimal getDtoEspecialAfiliados() {
        return dtoEspecialAfiliados;
    }

    public void setDtoEspecialAfiliados(BigDecimal dtoEspecialAfiliados) {
        this.dtoEspecialAfiliados = dtoEspecialAfiliados;
    }

    public Fecha getDtoEspecialFechaFin() {
        return dtoEspecialFechaFin;
    }

    public void setDtoEspecialFechaFin(Fecha dtoEspecialFechaFin) {
        this.dtoEspecialFechaFin = dtoEspecialFechaFin;
    }

    public Fecha getDtoEspecialFechaInicio() {
        return dtoEspecialFechaInicio;
    }

    public void setDtoEspecialFechaInicio(Fecha dtoEspecialFechaInicio) {
        this.dtoEspecialFechaInicio = dtoEspecialFechaInicio;
    }

    public BigDecimal getDtoEspecialNoAfiliados() {
        return dtoEspecialNoAfiliados;
    }

    public void setDtoEspecialNoAfiliados(BigDecimal dtoEspecialNoAfiliados) {
        this.dtoEspecialNoAfiliados = dtoEspecialNoAfiliados;
    }

    public BigDecimal getDtoNoAfiliados() {
        return dtoNoAfiliados;
    }

    public void setDtoNoAfiliados(BigDecimal dtoNoAfiliados) {
        this.dtoNoAfiliados = dtoNoAfiliados;
    }

    public Long getIdMedioPagoVencimiento() {
        return idMedioPagoVencimiento;
    }

    public void setIdMedioPagoVencimiento(Long idMedioPagoVencimiento) {
        this.idMedioPagoVencimiento = idMedioPagoVencimiento;
    }

    public BigDecimal getInteresAfiliado() {
        return interesAfiliado;
    }

    public void setInteresAfiliado(BigDecimal interesAfiliado) {
        this.interesAfiliado = interesAfiliado;
    }

    public BigDecimal getInteresEspecialAfiliado() {
        return interesEspecialAfiliado;
    }

    public void setInteresEspecialAfiliado(BigDecimal interesEspecialAfiliado) {
        this.interesEspecialAfiliado = interesEspecialAfiliado;
    }

    public BigDecimal getInteresEspecialNoAfiliado() {
        return interesEspecialNoAfiliado;
    }

    public void setInteresEspecialNoAfiliado(BigDecimal interesEspecialNoAfiliado) {
        this.interesEspecialNoAfiliado = interesEspecialNoAfiliado;
    }

    public BigDecimal getInteresNoAfiliado() {
        return interesNoAfiliado;
    }

    public void setInteresNoAfiliado(BigDecimal interesNoAfiliado) {
        this.interesNoAfiliado = interesNoAfiliado;
    }

    public boolean isDescuentoEspecial() {
        return descuentoEspecial;
    }

    public BigDecimal getPisoMinimo() {
        return pisoMinimo;
    }

    public void setPisoMinimo(BigDecimal pisoMinimo) {
        this.pisoMinimo = pisoMinimo;
    }

    public BigDecimal getDescuento(boolean afiliado, boolean afiliadoPromo) {
        Fecha ahora = new Fecha();
        if (isDescuentoEspecial()) {
            if ((dtoEspecialFechaInicio == null || dtoEspecialFechaInicio.antesOrEquals(ahora))
                    && (dtoEspecialFechaFin == null || dtoEspecialFechaFin.despuesOrEquals(ahora))) {
                return getDescuento(true, afiliado, afiliadoPromo);
            }
        }
        return getDescuento(false, afiliado, afiliadoPromo);
    }

    public BigDecimal getInteres(boolean afiliado) {
        Fecha ahora = new Fecha();
        if (isDescuentoEspecial()) {
            if ((dtoEspecialFechaInicio == null || dtoEspecialFechaInicio.antesOrEquals(ahora))
                    && (dtoEspecialFechaFin == null || dtoEspecialFechaFin.despuesOrEquals(ahora))) {
                return getInteres(true, afiliado);
            }
        }
        return getInteres(false, afiliado);
    }

    private BigDecimal getDescuento(boolean especial, boolean afiliado, boolean afiliadoPromo) {
        if (especial && afiliado) {
            return dtoEspecialAfiliados;
        }
        if (especial && !afiliado) {
            return dtoEspecialNoAfiliados;
        }
        if (!especial && afiliado && !afiliadoPromo) {
            return dtoAfiliados;
        }
        if (!especial && afiliado && afiliadoPromo) {
            return dtoAfiliadosPromo;
        }
        if (!especial && !afiliado) {
            return dtoNoAfiliados;
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getInteres(boolean especial, boolean afiliado) {
        if (especial && afiliado) {
            return interesEspecialAfiliado;
        }
        if (especial && !afiliado) {
            return interesEspecialNoAfiliado;
        }
        if (!especial && afiliado) {
            return interesAfiliado;
        }
        if (!especial && !afiliado) {
            return interesNoAfiliado;
        }
        return BigDecimal.ZERO;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public BigDecimal getDtoAfiliadosPromo() {
        return dtoAfiliadosPromo;
    }

    public void setDtoAfiliadosPromo(BigDecimal dtoAfiliadosPromo) {
        this.dtoAfiliadosPromo = dtoAfiliadosPromo;
    }

}
