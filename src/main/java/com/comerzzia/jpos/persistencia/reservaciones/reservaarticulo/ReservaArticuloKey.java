package com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo;

public class ReservaArticuloKey {
    private String uidReservacion;

    private Long idLinea;

    public String getUidReservacion() {
        return uidReservacion;
    }

    public void setUidReservacion(String uidReservacion) {
        this.uidReservacion = uidReservacion == null ? null : uidReservacion.trim();
    }

    public Long getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(Long idLinea) {
        this.idLinea = idLinea;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uidReservacion != null ? uidReservacion.hashCode() : 0);
        hash += (idLinea != null ? idLinea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ReservaArticuloKey)) {
            return false;
        }
        ReservaArticuloKey other = (ReservaArticuloKey) object;
        if ((this.uidReservacion == null && other.uidReservacion != null) || (this.uidReservacion != null && !this.uidReservacion.equals(other.uidReservacion))) {
            return false;
        }
        if ((this.idLinea == null && other.idLinea != null) || (this.idLinea != null && !this.idLinea.equals(other.idLinea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.ReservaArticuloPK[ uidReservacion=" + uidReservacion + ", idLinea=" + idLinea + " ]";
    }
    
    
}