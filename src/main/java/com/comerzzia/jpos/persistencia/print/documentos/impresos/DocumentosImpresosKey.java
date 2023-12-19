package com.comerzzia.jpos.persistencia.print.documentos.impresos;

public class DocumentosImpresosKey {
    private String uidDocumento;

    private Short idImpreso;

    public String getUidDocumento() {
        return uidDocumento;
    }

    public void setUidDocumento(String uidDocumento) {
        this.uidDocumento = uidDocumento == null ? null : uidDocumento.trim();
    }

    public Short getIdImpreso() {
        return idImpreso;
    }

    public void setIdImpreso(Short idImpreso) {
        this.idImpreso = idImpreso;
    }
}