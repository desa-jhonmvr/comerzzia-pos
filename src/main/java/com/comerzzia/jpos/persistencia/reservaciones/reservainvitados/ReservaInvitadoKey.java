package com.comerzzia.jpos.persistencia.reservaciones.reservainvitados;

public class ReservaInvitadoKey {
    private String uidReservacion;

    private Long idInvitado;

    public String getUidReservacion() {
        return uidReservacion;
    }

    public void setUidReservacion(String uidReservacion) {
        this.uidReservacion = uidReservacion == null ? null : uidReservacion.trim();
    }

    public Long getIdInvitado() {
        return idInvitado;
    }

    public void setIdInvitado(Long idInvitado) {
        this.idInvitado = idInvitado;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uidReservacion != null ? uidReservacion.hashCode() : 0);
        hash += (idInvitado != null ? idInvitado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ReservaInvitadoKey)) {
            return false;
        }
        ReservaInvitadoKey other = (ReservaInvitadoKey) object;
        if ((this.uidReservacion == null && other.uidReservacion != null) || (this.uidReservacion != null && !this.uidReservacion.equals(other.uidReservacion))) {
            return false;
        }
        if ((this.idInvitado == null && other.idInvitado != null) || (this.idInvitado != null && !this.idInvitado.equals(other.idInvitado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.comerzzia.jpos.entity.db.ReservaInvitadoKey[ uidReservacion=" + uidReservacion + ", idInvitado=" + idInvitado + " ]";
    }
    
}