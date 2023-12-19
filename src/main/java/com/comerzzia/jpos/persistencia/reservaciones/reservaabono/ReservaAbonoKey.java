package com.comerzzia.jpos.persistencia.reservaciones.reservaabono;

public class ReservaAbonoKey {
    private String uidReservacion;

    private Long idAbono;
    
    public String getUidReservacion() {
        return uidReservacion;
    }

    public void setUidReservacion(String uidReservacion) {
        this.uidReservacion = uidReservacion == null ? null : uidReservacion.trim();
    }

    public Long getIdAbono() {
        return idAbono;
    }

    public void setIdAbono(Long idAbono) {
        this.idAbono = idAbono;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uidReservacion != null ? uidReservacion.hashCode() : 0);
        hash += (idAbono != null ? idAbono.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ReservaAbonoKey)) {
            return false;
        }
        ReservaAbonoKey other = (ReservaAbonoKey) object;
        if ((this.uidReservacion == null && other.uidReservacion != null) || (this.uidReservacion != null && !this.uidReservacion.equals(other.uidReservacion))) {
            return false;
        }
        if ((this.idAbono == null && other.idAbono != null) || (this.idAbono != null && !this.idAbono.equals(other.idAbono))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.db.ReservaAbonoKey[ uidReservacion=" + uidReservacion + ", idAbono=" + idAbono + " ]";
    }    
}