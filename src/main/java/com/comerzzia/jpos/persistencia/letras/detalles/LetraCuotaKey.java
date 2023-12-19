package com.comerzzia.jpos.persistencia.letras.detalles;

public class LetraCuotaKey {
    private String uidLetra;

    private Short cuota;

    public String getUidLetra() {
        return uidLetra;
    }

    public void setUidLetra(String uidLetra) {
        this.uidLetra = uidLetra == null ? null : uidLetra.trim();
    }

    public Short getCuota() {
        return cuota;
    }

    public void setCuota(Short cuota) {
        this.cuota = cuota;
    }
}