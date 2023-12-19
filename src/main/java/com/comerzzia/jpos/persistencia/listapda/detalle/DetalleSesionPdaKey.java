package com.comerzzia.jpos.persistencia.listapda.detalle;

public class DetalleSesionPdaKey {
    private String uidSesionPda;

    private Short idLinea;

    public String getUidSesionPda() {
        return uidSesionPda;
    }

    public void setUidSesionPda(String uidSesionPda) {
        this.uidSesionPda = uidSesionPda == null ? null : uidSesionPda.trim();
    }

    public Short getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(Short idLinea) {
        this.idLinea = idLinea;
    }
}