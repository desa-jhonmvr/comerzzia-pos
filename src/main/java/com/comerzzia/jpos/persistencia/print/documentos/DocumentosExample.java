package com.comerzzia.jpos.persistencia.print.documentos;

import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DocumentosExample {
    public static final String ORDER_BY_UID_DOCUMENTO = "UID_DOCUMENTO";

    public static final String ORDER_BY_UID_DOCUMENTO_DESC = "UID_DOCUMENTO DESC";

    public static final String ORDER_BY_TIPO = "TIPO";

    public static final String ORDER_BY_TIPO_DESC = "TIPO DESC";

    public static final String ORDER_BY_CODALM = "CODALM";

    public static final String ORDER_BY_CODALM_DESC = "CODALM DESC";

    public static final String ORDER_BY_CODCAJA = "CODCAJA";

    public static final String ORDER_BY_CODCAJA_DESC = "CODCAJA DESC";
    
    public static final String ORDER_BY_CODCAJA_EMISION = "CODCAJA_EMISION";
    
    public static final String ORDER_BY_CODCAJA_EMISION_DESC = "CODCAJA_EMISION DESC";

    public static final String ORDER_BY_ID_DOCUMENTO = "ID_DOCUMENTO";

    public static final String ORDER_BY_ID_DOCUMENTO_DESC = "ID_DOCUMENTO DESC";

    public static final String ORDER_BY_FECHA = "FECHA";

    public static final String ORDER_BY_FECHA_DESC = "FECHA DESC";

    public static final String ORDER_BY_CODCLI = "CODCLI";

    public static final String ORDER_BY_CODCLI_DESC = "CODCLI DESC";

    public static final String ORDER_BY_MONTO = "MONTO";

    public static final String ORDER_BY_MONTO_DESC = "MONTO DESC";

    public static final String ORDER_BY_ESTADO = "ESTADO";

    public static final String ORDER_BY_ESTADO_DESC = "ESTADO DESC";

    public static final String ORDER_BY_USUARIO = "USUARIO";

    public static final String ORDER_BY_USUARIO_DESC = "USUARIO DESC";

    public static final String ORDER_BY_OBSERVACIONES = "OBSERVACIONES";

    public static final String ORDER_BY_OBSERVACIONES_DESC = "OBSERVACIONES DESC";
    
    public static final String ORDER_BY_REFERENCIA = "REFERENCIA";
    
    public static final String ORDER_BY_REFERENCIA_DESC = "REFERENCIA DESC";
    
    public static final String ORDER_BY_NUMTRANSACCION = "NUM_TRANSACCION";
    
    public static final String ORDER_BY_NUMTRANSACCION_DESC = "NUM_TRANSACCION DESC";
    
    public static final String ORDER_BY_FECHACADUCIDAD = "FECHA_CADUCIDAD";
    
    public static final String ORDER_BY_FECHACADUCIDAD_DESC = "FECHA_CADUCIDAD DESC";

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DocumentosExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> fechaCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
            fechaCriteria = new ArrayList<Criterion>();
        }

        public List<Criterion> getFechaCriteria() {
            return fechaCriteria;
        }

        protected void addFechaCriterion(String condition, Object value, String property) {
            if (value != null) {
                fechaCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addFechaCriterion(String condition, Fecha value1, Fecha value2, String property) {
            if (value1 != null && value2 != null) {
                fechaCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
                allCriteria = null;
            }
        }

        public boolean isValid() {
            return criteria.size() > 0
                || fechaCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<Criterion>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(fechaCriteria);
            }
            return allCriteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition != null) {
                criteria.add(new Criterion(condition));
                allCriteria = null;
            }
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value != null) {
                criteria.add(new Criterion(condition, value));
                allCriteria = null;
            }
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 != null && value2 != null) {
                criteria.add(new Criterion(condition, value1, value2));
                allCriteria = null;
            }
        }

        public Criteria andUidDocumentoIsNull() {
            addCriterion("DOCU.UID_DOCUMENTO is null");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoIsNotNull() {
            addCriterion("DOCU.UID_DOCUMENTO is not null");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoEqualTo(String value) {
            addCriterion("DOCU.UID_DOCUMENTO =", value, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoNotEqualTo(String value) {
            addCriterion("DOCU.UID_DOCUMENTO <>", value, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoGreaterThan(String value) {
            addCriterion("DOCU.UID_DOCUMENTO >", value, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoGreaterThanOrEqualTo(String value) {
            addCriterion("DOCU.UID_DOCUMENTO >=", value, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoLessThan(String value) {
            addCriterion("DOCU.UID_DOCUMENTO <", value, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoLessThanOrEqualTo(String value) {
            addCriterion("DOCU.UID_DOCUMENTO <=", value, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoLike(String value) {
            addCriterion("DOCU.UID_DOCUMENTO like", value, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoNotLike(String value) {
            addCriterion("DOCU.UID_DOCUMENTO not like", value, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoIn(List<String> values) {
            addCriterion("DOCU.UID_DOCUMENTO in", values, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoNotIn(List<String> values) {
            addCriterion("DOCU.UID_DOCUMENTO not in", values, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoBetween(String value1, String value2) {
            addCriterion("DOCU.UID_DOCUMENTO between", value1, value2, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoNotBetween(String value1, String value2) {
            addCriterion("DOCU.UID_DOCUMENTO not between", value1, value2, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andTipoIsNull() {
            addCriterion("DOCU.TIPO is null");
            return (Criteria) this;
        }

        public Criteria andTipoIsNotNull() {
            addCriterion("DOCU.TIPO is not null");
            return (Criteria) this;
        }

        public Criteria andTipoEqualTo(String value) {
            addCriterion("DOCU.TIPO =", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoNotEqualTo(String value) {
            addCriterion("DOCU.TIPO <>", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoGreaterThan(String value) {
            addCriterion("DOCU.TIPO >", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoGreaterThanOrEqualTo(String value) {
            addCriterion("DOCU.TIPO >=", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoLessThan(String value) {
            addCriterion("DOCU.TIPO <", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoLessThanOrEqualTo(String value) {
            addCriterion("DOCU.TIPO <=", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoLike(String value) {
            addCriterion("DOCU.TIPO like", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoNotLike(String value) {
            addCriterion("DOCU.TIPO not like", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoIn(List<String> values) {
            addCriterion("DOCU.TIPO in", values, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoNotIn(List<String> values) {
            addCriterion("DOCU.TIPO not in", values, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoBetween(String value1, String value2) {
            addCriterion("DOCU.TIPO between", value1, value2, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoNotBetween(String value1, String value2) {
            addCriterion("DOCU.TIPO not between", value1, value2, "tipo");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenIsNull() {
            addCriterion("DOCU.CODALM is null");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenIsNotNull() {
            addCriterion("DOCU.CODALM is not null");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenEqualTo(String value) {
            addCriterion("DOCU.CODALM =", value, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenNotEqualTo(String value) {
            addCriterion("DOCU.CODALM <>", value, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenGreaterThan(String value) {
            addCriterion("DOCU.CODALM >", value, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenGreaterThanOrEqualTo(String value) {
            addCriterion("DOCU.CODALM >=", value, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenLessThan(String value) {
            addCriterion("DOCU.CODALM <", value, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenLessThanOrEqualTo(String value) {
            addCriterion("DOCU.CODALM <=", value, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenLike(String value) {
            addCriterion("DOCU.CODALM like", value, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenNotLike(String value) {
            addCriterion("DOCU.CODALM not like", value, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenIn(List<String> values) {
            addCriterion("DOCU.CODALM in", values, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenNotIn(List<String> values) {
            addCriterion("DOCU.CODALM not in", values, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenBetween(String value1, String value2) {
            addCriterion("DOCU.CODALM between", value1, value2, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenNotBetween(String value1, String value2) {
            addCriterion("DOCU.CODALM not between", value1, value2, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodCajaIsNull() {
            addCriterion("DOCU.CODCAJA is null");
            return (Criteria) this;
        }

        public Criteria andCodCajaIsNotNull() {
            addCriterion("DOCU.CODCAJA is not null");
            return (Criteria) this;
        }

        public Criteria andCodCajaEqualTo(String value) {
            addCriterion("DOCU.CODCAJA =", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaNotEqualTo(String value) {
            addCriterion("DOCU.CODCAJA <>", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaGreaterThan(String value) {
            addCriterion("DOCU.CODCAJA >", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaGreaterThanOrEqualTo(String value) {
            addCriterion("DOCU.CODCAJA >=", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaLessThan(String value) {
            addCriterion("DOCU.CODCAJA <", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaLessThanOrEqualTo(String value) {
            addCriterion("DOCU.CODCAJA <=", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaLike(String value) {
            addCriterion("DOCU.CODCAJA like", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaNotLike(String value) {
            addCriterion("DOCU.CODCAJA not like", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaIn(List<String> values) {
            addCriterion("DOCU.CODCAJA in", values, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaNotIn(List<String> values) {
            addCriterion("DOCU.CODCAJA not in", values, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaBetween(String value1, String value2) {
            addCriterion("DOCU.CODCAJA between", value1, value2, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaNotBetween(String value1, String value2) {
            addCriterion("DOCU.CODCAJA not between", value1, value2, "codCaja");
            return (Criteria) this;
        }
        
        public Criteria andCodCajaEmisionIsNull() {
            addCriterion("DOCU.CODCAJA_EMISION is null");
            return (Criteria) this;
        }

        public Criteria andCodCajaEmisionIsNotNull() {
            addCriterion("DOCU.CODCAJA_EMISION is not null");
            return (Criteria) this;
        }

        public Criteria andCodCajaEmisionEqualTo(String value) {
            addCriterion("DOCU.CODCAJA_EMISION =", value, "codCajaEmision");
            return (Criteria) this;
        }

        public Criteria andCodCajaEmisionNotEqualTo(String value) {
            addCriterion("DOCU.CODCAJA_EMISION <>", value, "codCajaEmision");
            return (Criteria) this;
        }

        public Criteria andCodCajaEmisionGreaterThan(String value) {
            addCriterion("DOCU.CODCAJA_EMISION >", value, "codCajaEmision");
            return (Criteria) this;
        }

        public Criteria andCodCajaEmisionGreaterThanOrEqualTo(String value) {
            addCriterion("DOCU.CODCAJA_EMISION >=", value, "codCajaEmision");
            return (Criteria) this;
        }

        public Criteria andCodCajaEmisionLessThan(String value) {
            addCriterion("DOCU.CODCAJA_EMISION <", value, "codCajaEmision");
            return (Criteria) this;
        }

        public Criteria andCodCajaEmisionLessThanOrEqualTo(String value) {
            addCriterion("DOCU.CODCAJA_EMISION <=", value, "codCajaEmision");
            return (Criteria) this;
        }

        public Criteria andCodCajaEmisionLike(String value) {
            addCriterion("DOCU.CODCAJA_EMISION like", value, "codCajaEmision");
            return (Criteria) this;
        }

        public Criteria andCodCajaEmisionNotLike(String value) {
            addCriterion("DOCU.CODCAJA_EMISION not like", value, "codCajaEmision");
            return (Criteria) this;
        }

        public Criteria andCodCajaEmisionIn(List<String> values) {
            addCriterion("DOCU.CODCAJA_EMISION in", values, "codCajaEmision");
            return (Criteria) this;
        }

        public Criteria andCodCajaEmisionNotIn(List<String> values) {
            addCriterion("DOCU.CODCAJA_EMISION not in", values, "codCajaEmision");
            return (Criteria) this;
        }

        public Criteria andCodCajaEmisionBetween(String value1, String value2) {
            addCriterion("DOCU.CODCAJA_EMISION between", value1, value2, "codCajaEmision");
            return (Criteria) this;
        }

        public Criteria andCodCajaEmisionNotBetween(String value1, String value2) {
            addCriterion("DOCU.CODCAJA_EMISION not between", value1, value2, "codCajaEmision");
            return (Criteria) this;
        }        

        public Criteria andIdDocumentoIsNull() {
            addCriterion("DOCU.ID_DOCUMENTO is null");
            return (Criteria) this;
        }

        public Criteria andIdDocumentoIsNotNull() {
            addCriterion("DOCU.ID_DOCUMENTO is not null");
            return (Criteria) this;
        }

        public Criteria andIdDocumentoEqualTo(String value) {
            addCriterion("DOCU.ID_DOCUMENTO =", value, "idDocumento");
            return (Criteria) this;
        }

        public Criteria andIdDocumentoNotEqualTo(String value) {
            addCriterion("DOCU.ID_DOCUMENTO <>", value, "idDocumento");
            return (Criteria) this;
        }

        public Criteria andIdDocumentoGreaterThan(String value) {
            addCriterion("DOCU.ID_DOCUMENTO >", value, "idDocumento");
            return (Criteria) this;
        }

        public Criteria andIdDocumentoGreaterThanOrEqualTo(String value) {
            addCriterion("DOCU.ID_DOCUMENTO >=", value, "idDocumento");
            return (Criteria) this;
        }

        public Criteria andIdDocumentoLessThan(String value) {
            addCriterion("DOCU.ID_DOCUMENTO <", value, "idDocumento");
            return (Criteria) this;
        }

        public Criteria andIdDocumentoLessThanOrEqualTo(String value) {
            addCriterion("DOCU.ID_DOCUMENTO <=", value, "idDocumento");
            return (Criteria) this;
        }

        public Criteria andIdDocumentoIn(List<String> values) {
            addCriterion("DOCU.ID_DOCUMENTO in", values, "idDocumento");
            return (Criteria) this;
        }

        public Criteria andIdDocumentoNotIn(List<String> values) {
            addCriterion("DOCU.ID_DOCUMENTO not in", values, "idDocumento");
            return (Criteria) this;
        }

        public Criteria andIdDocumentoBetween(String value1, String value2) {
            addCriterion("DOCU.ID_DOCUMENTO between", value1, value2, "idDocumento");
            return (Criteria) this;
        }

        public Criteria andIdDocumentoNotBetween(String value1, String value2) {
            addCriterion("DOCU.ID_DOCUMENTO not between", value1, value2, "idDocumento");
            return (Criteria) this;
        }

        public Criteria andFechaIsNull() {
            addCriterion("DOCU.FECHA is null");
            return (Criteria) this;
        }

        public Criteria andFechaIsNotNull() {
            addCriterion("DOCU.FECHA is not null");
            return (Criteria) this;
        }

        public Criteria andFechaEqualTo(Fecha value) {
            addFechaCriterion("DOCU.FECHA =", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaNotEqualTo(Fecha value) {
            addFechaCriterion("DOCU.FECHA <>", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaGreaterThan(Fecha value) {
            addFechaCriterion("DOCU.FECHA >", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaGreaterThanOrEqualTo(Fecha value) {
            addFechaCriterion("DOCU.FECHA >=", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaLessThan(Fecha value) {
            addFechaCriterion("DOCU.FECHA <", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaLessThanOrEqualTo(Fecha value) {
            addFechaCriterion("DOCU.FECHA <=", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaIn(List<Fecha> values) {
            addFechaCriterion("DOCU.FECHA in", values, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaNotIn(List<Fecha> values) {
            addFechaCriterion("DOCU.FECHA not in", values, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaBetween(Fecha value1, Fecha value2) {
            addFechaCriterion("DOCU.FECHA between", value1, value2, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaNotBetween(Fecha value1, Fecha value2) {
            addFechaCriterion("DOCU.FECHA not between", value1, value2, "fecha");
            return (Criteria) this;
        }

        public Criteria andCodClienteIsNull() {
            addCriterion("DOCU.CODCLI is null");
            return (Criteria) this;
        }

        public Criteria andCodClienteIsNotNull() {
            addCriterion("DOCU.CODCLI is not null");
            return (Criteria) this;
        }

        public Criteria andCodClienteEqualTo(String value) {
            addCriterion("DOCU.CODCLI =", value, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteNotEqualTo(String value) {
            addCriterion("DOCU.CODCLI <>", value, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteGreaterThan(String value) {
            addCriterion("DOCU.CODCLI >", value, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteGreaterThanOrEqualTo(String value) {
            addCriterion("DOCU.CODCLI >=", value, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteLessThan(String value) {
            addCriterion("DOCU.CODCLI <", value, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteLessThanOrEqualTo(String value) {
            addCriterion("DOCU.CODCLI <=", value, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteLike(String value) {
            addCriterion("DOCU.CODCLI like", value, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteNotLike(String value) {
            addCriterion("DOCU.CODCLI not like", value, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteIn(List<String> values) {
            addCriterion("DOCU.CODCLI in", values, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteNotIn(List<String> values) {
            addCriterion("DOCU.CODCLI not in", values, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteBetween(String value1, String value2) {
            addCriterion("DOCU.CODCLI between", value1, value2, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteNotBetween(String value1, String value2) {
            addCriterion("DOCU.CODCLI not between", value1, value2, "codCliente");
            return (Criteria) this;
        }

        public Criteria andMontoIsNull() {
            addCriterion("DOCU.MONTO is null");
            return (Criteria) this;
        }

        public Criteria andMontoIsNotNull() {
            addCriterion("DOCU.MONTO is not null");
            return (Criteria) this;
        }

        public Criteria andMontoEqualTo(BigDecimal value) {
            addCriterion("DOCU.MONTO =", value, "monto");
            return (Criteria) this;
        }

        public Criteria andMontoNotEqualTo(BigDecimal value) {
            addCriterion("DOCU.MONTO <>", value, "monto");
            return (Criteria) this;
        }

        public Criteria andMontoGreaterThan(BigDecimal value) {
            addCriterion("DOCU.MONTO >", value, "monto");
            return (Criteria) this;
        }

        public Criteria andMontoGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("DOCU.MONTO >=", value, "monto");
            return (Criteria) this;
        }

        public Criteria andMontoLessThan(BigDecimal value) {
            addCriterion("DOCU.MONTO <", value, "monto");
            return (Criteria) this;
        }

        public Criteria andMontoLessThanOrEqualTo(BigDecimal value) {
            addCriterion("DOCU.MONTO <=", value, "monto");
            return (Criteria) this;
        }

        public Criteria andMontoIn(List<BigDecimal> values) {
            addCriterion("DOCU.MONTO in", values, "monto");
            return (Criteria) this;
        }

        public Criteria andMontoNotIn(List<BigDecimal> values) {
            addCriterion("DOCU.MONTO not in", values, "monto");
            return (Criteria) this;
        }

        public Criteria andMontoBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("DOCU.MONTO between", value1, value2, "monto");
            return (Criteria) this;
        }

        public Criteria andMontoNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("DOCU.MONTO not between", value1, value2, "monto");
            return (Criteria) this;
        }

        public Criteria andMontoLike(BigDecimal value) {
            addCriterion("DOCU.MONTO like", value, "monto");
            return (Criteria) this;
        }
        
        public Criteria andMontoLikeToString(BigDecimal value) {
            if(value != null){
                addCriterion("DOCU.MONTO like", "%"+value.toString().replace('.', ',') +"%", "monto");
            }
            return (Criteria) this;           
        }
        
        public Criteria andEstadoIsNull() {
            addCriterion("DOCU.ESTADO is null");
            return (Criteria) this;
        }

        public Criteria andEstadoIsNotNull() {
            addCriterion("DOCU.ESTADO is not null");
            return (Criteria) this;
        }

        public Criteria andEstadoEqualTo(String value) {
            addCriterion("DOCU.ESTADO =", value, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoNotEqualTo(String value) {
            addCriterion("DOCU.ESTADO <>", value, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoGreaterThan(String value) {
            addCriterion("DOCU.ESTADO >", value, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoGreaterThanOrEqualTo(String value) {
            addCriterion("DOCU.ESTADO >=", value, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoLessThan(String value) {
            addCriterion("DOCU.ESTADO <", value, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoLessThanOrEqualTo(String value) {
            addCriterion("DOCU.ESTADO <=", value, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoLike(String value) {
            addCriterion("DOCU.ESTADO like", value, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoNotLike(String value) {
            addCriterion("DOCU.ESTADO not like", value, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoIn(List<String> values) {
            addCriterion("DOCU.ESTADO in", values, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoNotIn(List<String> values) {
            addCriterion("DOCU.ESTADO not in", values, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoBetween(String value1, String value2) {
            addCriterion("DOCU.ESTADO between", value1, value2, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoNotBetween(String value1, String value2) {
            addCriterion("DOCU.ESTADO not between", value1, value2, "estado");
            return (Criteria) this;
        }

        public Criteria andUsuarioIsNull() {
            addCriterion("DOCU.USUARIO is null");
            return (Criteria) this;
        }

        public Criteria andUsuarioIsNotNull() {
            addCriterion("DOCU.USUARIO is not null");
            return (Criteria) this;
        }

        public Criteria andUsuarioEqualTo(String value) {
            addCriterion("DOCU.USUARIO =", value, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioNotEqualTo(String value) {
            addCriterion("DOCU.USUARIO <>", value, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioGreaterThan(String value) {
            addCriterion("DOCU.USUARIO >", value, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioGreaterThanOrEqualTo(String value) {
            addCriterion("DOCU.USUARIO >=", value, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioLessThan(String value) {
            addCriterion("DOCU.USUARIO <", value, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioLessThanOrEqualTo(String value) {
            addCriterion("DOCU.USUARIO <=", value, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioLike(String value) {
            addCriterion("DOCU.USUARIO like", value, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioNotLike(String value) {
            addCriterion("DOCU.USUARIO not like", value, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioIn(List<String> values) {
            addCriterion("DOCU.USUARIO in", values, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioNotIn(List<String> values) {
            addCriterion("DOCU.USUARIO not in", values, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioBetween(String value1, String value2) {
            addCriterion("DOCU.USUARIO between", value1, value2, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioNotBetween(String value1, String value2) {
            addCriterion("DOCU.USUARIO not between", value1, value2, "usuario");
            return (Criteria) this;
        }

        public Criteria andObservacionesIsNull() {
            addCriterion("DOCU.OBSERVACIONES is null");
            return (Criteria) this;
        }

        public Criteria andObservacionesIsNotNull() {
            addCriterion("DOCU.OBSERVACIONES is not null");
            return (Criteria) this;
        }

        public Criteria andObservacionesEqualTo(String value) {
            addCriterion("DOCU.OBSERVACIONES =", value, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesNotEqualTo(String value) {
            addCriterion("DOCU.OBSERVACIONES <>", value, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesGreaterThan(String value) {
            addCriterion("DOCU.OBSERVACIONES >", value, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesGreaterThanOrEqualTo(String value) {
            addCriterion("DOCU.OBSERVACIONES >=", value, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesLessThan(String value) {
            addCriterion("DOCU.OBSERVACIONES <", value, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesLessThanOrEqualTo(String value) {
            addCriterion("DOCU.OBSERVACIONES <=", value, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesLike(String value) {
            addCriterion("DOCU.OBSERVACIONES like", value, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesNotLike(String value) {
            addCriterion("DOCU.OBSERVACIONES not like", value, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesIn(List<String> values) {
            addCriterion("DOCU.OBSERVACIONES in", values, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesNotIn(List<String> values) {
            addCriterion("DOCU.OBSERVACIONES not in", values, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesBetween(String value1, String value2) {
            addCriterion("DOCU.OBSERVACIONES between", value1, value2, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesNotBetween(String value1, String value2) {
            addCriterion("DOCU.OBSERVACIONES not between", value1, value2, "observaciones");
            return (Criteria) this;
        }
        
        public Criteria andReferenciaIsNull() {
            addCriterion("DOCU.REFERENCIA is null");
            return (Criteria) this;
        }

        public Criteria andReferenciaIsNotNull() {
            addCriterion("DOCU.REFERENCIA is not null");
            return (Criteria) this;
        }

        public Criteria andReferenciaEqualTo(String value) {
            addCriterion("DOCU.REFERENCIA =", value, "REFERENCIA");
            return (Criteria) this;
        }

        public Criteria andReferenciaNotEqualTo(String value) {
            addCriterion("DOCU.REFERENCIA <>", value, "REFERENCIA");
            return (Criteria) this;
        }

        public Criteria andReferenciaGreaterThan(String value) {
            addCriterion("DOCU.REFERENCIA >", value, "REFERENCIA");
            return (Criteria) this;
        }

        public Criteria andReferenciaGreaterThanOrEqualTo(String value) {
            addCriterion("DOCU.REFERENCIA >=", value, "REFERENCIA");
            return (Criteria) this;
        }

        public Criteria andReferenciaLessThan(String value) {
            addCriterion("DOCU.REFERENCIA <", value, "REFERENCIA");
            return (Criteria) this;
        }

        public Criteria andReferenciaLessThanOrEqualTo(String value) {
            addCriterion("DOCU.REFERENCIA <=", value, "REFERENCIA");
            return (Criteria) this;
        }

        public Criteria andReferenciaLike(String value) {
            addCriterion("DOCU.REFERENCIA like", value, "REFERENCIA");
            return (Criteria) this;
        }

        public Criteria andReferenciaNotLike(String value) {
            addCriterion("DOCU.REFERENCIA not like", value, "REFERENCIA");
            return (Criteria) this;
        }

        public Criteria andReferenciaIn(List<String> values) {
            addCriterion("DOCU.REFERENCIA in", values, "REFERENCIA");
            return (Criteria) this;
        }

        public Criteria andReferenciaNotIn(List<String> values) {
            addCriterion("DOCU.REFERENCIA not in", values, "REFERENCIA");
            return (Criteria) this;
        }

        public Criteria andReferenciaBetween(String value1, String value2) {
            addCriterion("DOCU.REFERENCIA between", value1, value2, "REFERENCIA");
            return (Criteria) this;
        }

        public Criteria andReferenciaNotBetween(String value1, String value2) {
            addCriterion("DOCU.REFERENCIA not between", value1, value2, "REFERENCIA");
            return (Criteria) this;
        }
        
        public Criteria andNumTransaccionIsNull() {
            addCriterion("DOCU.NUM_TRANSACCION is null");
            return (Criteria) this;
        }

        public Criteria andNumTransaccionIsNotNull() {
            addCriterion("DOCU.NUM_TRANSACCION is not null");
            return (Criteria) this;
        }

        public Criteria andNumTransaccionEqualTo(String value) {
            addCriterion("DOCU.NUM_TRANSACCION =", value, "NUM_TRANSACCION");
            return (Criteria) this;
        }

        public Criteria andNumTransaccionNotEqualTo(String value) {
            addCriterion("DOCU.NUM_TRANSACCION <>", value, "NUM_TRANSACCION");
            return (Criteria) this;
        }

        public Criteria andNumTransaccionGreaterThan(String value) {
            addCriterion("DOCU.NUM_TRANSACCION >", value, "NUM_TRANSACCION");
            return (Criteria) this;
        }

        public Criteria andNumTransaccionGreaterThanOrEqualTo(String value) {
            addCriterion("DOCU.NUM_TRANSACCION >=", value, "NUM_TRANSACCION");
            return (Criteria) this;
        }

        public Criteria andNumTransaccionLessThan(String value) {
            addCriterion("DOCU.NUM_TRANSACCION <", value, "NUM_TRANSACCION");
            return (Criteria) this;
        }

        public Criteria andNumTransaccionLessThanOrEqualTo(String value) {
            addCriterion("DOCU.NUM_TRANSACCION <=", value, "NUM_TRANSACCION");
            return (Criteria) this;
        }

        public Criteria andNumTransaccionLike(String value) {
            addCriterion("DOCU.NUM_TRANSACCION like", value, "NUM_TRANSACCION");
            return (Criteria) this;
        }

        public Criteria andNumTransaccionNotLike(String value) {
            addCriterion("DOCU.NUM_TRANSACCION not like", value, "NUM_TRANSACCION");
            return (Criteria) this;
        }

        public Criteria andNumTransaccionIn(List<String> values) {
            addCriterion("DOCU.NUM_TRANSACCION in", values, "NUM_TRANSACCION");
            return (Criteria) this;
        }

        public Criteria andNumTransaccionNotIn(List<String> values) {
            addCriterion("DOCU.NUM_TRANSACCION not in", values, "NUM_TRANSACCION");
            return (Criteria) this;
        }

        public Criteria andNumTransaccionBetween(String value1, String value2) {
            addCriterion("DOCU.NUM_TRANSACCION between", value1, value2, "NUM_TRANSACCION");
            return (Criteria) this;
        }

        public Criteria andNumTransaccionNotBetween(String value1, String value2) {
            addCriterion("DOCU.NUM_TRANSACCION not between", value1, value2, "NUM_TRANSACCION");
            return (Criteria) this;
        }        

        public Criteria andUidDocumentoLikeInsensitive(String value) {
            addCriterion("upper(DOCU.UID_DOCUMENTO) like", value.toUpperCase(), "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andTipoLikeInsensitive(String value) {
            addCriterion("upper(DOCU.TIPO) like", value.toUpperCase(), "tipo");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenLikeInsensitive(String value) {
            addCriterion("upper(DOCU.CODALM) like", value.toUpperCase(), "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodCajaLikeInsensitive(String value) {
            addCriterion("upper(DOCU.CODCAJA) like", value.toUpperCase(), "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodClienteLikeInsensitive(String value) {
            addCriterion("upper(DOCU.CODCLI) like", value.toUpperCase(), "codCliente");
            return (Criteria) this;
        }

        public Criteria andEstadoLikeInsensitive(String value) {
            addCriterion("upper(DOCU.ESTADO) like", value.toUpperCase(), "estado");
            return (Criteria) this;
        }

        public Criteria andUsuarioLikeInsensitive(String value) {
            addCriterion("upper(DOCU.USUARIO) like", value.toUpperCase(), "usuario");
            return (Criteria) this;
        }

        public Criteria andObservacionesLikeInsensitive(String value) {
            addCriterion("upper(DOCU.OBSERVACIONES) like", value.toUpperCase(), "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesLikeInsensitiveWithoutNull(String value) {
            if(value != null && !value.isEmpty()){
                addCriterion("upper(DOCU.OBSERVACIONES) like", value.toUpperCase(), "observaciones");
            }
            return (Criteria) this;
        }
     
        public Criteria andFechaCaducidadIsNull() {
            addCriterion("DOCU.FECHA_CADUCIDAD is null");
            return (Criteria) this;
        }

        public Criteria andFechaCaducidadIsNotNull() {
            addCriterion("DOCU.FECHA_CADUCIDAD is not null");
            return (Criteria) this;
        }

        public Criteria andFechaCaducidadaEqualTo(Fecha value) {
            addFechaCriterion("DOCU.FECHA_CADUCIDAD =", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaCaducidadNotEqualTo(Fecha value) {
            addFechaCriterion("DOCU.FECHA_CADUCIDAD <>", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaCaducidadGreaterThan(Fecha value) {
            addFechaCriterion("DOCU.FECHA_CADUCIDAD >", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaCaducidadGreaterThanOrEqualTo(Fecha value) {
            addFechaCriterion("DOCU.FECHA_CADUCIDAD >=", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaCaducidadLessThan(Fecha value) {
            addFechaCriterion("DOCU.FECHA_CADUCIDAD <", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaCaducidadLessThanOrEqualTo(Fecha value) {
            addFechaCriterion("DOCU.FECHA_CADUCIDAD <=", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaCaducidadIn(List<Fecha> values) {
            addFechaCriterion("DOCU.FECHA_CADUCIDAD in", values, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaCaducidadNotIn(List<Fecha> values) {
            addFechaCriterion("DOCU.FECHA_CADUCIDAD not in", values, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaCaducidadBetween(Fecha value1, Fecha value2) {
            addFechaCriterion("DOCU.FECHA_CADUCIDAD between", value1, value2, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaCaducidadNotBetween(Fecha value1, Fecha value2) {
            addFechaCriterion("DOCU.FECHA_CADUCIDAD not between", value1, value2, "fecha");
            return (Criteria) this;
        }        
        
        public Criteria andOperationsWithMonto(String value, BigDecimal big) {
            if(big == null || value == null || value.isEmpty()){
                return (Criteria) this;
            }
            if(value.equals("=")){
                return this.andMontoEqualTo(big);
            }
            if(value.equals("<")){
                return this.andMontoLessThan(big);
            }
            if(value.equals("<=")){
                return this.andMontoLessThanOrEqualTo(big);
            }
            if(value.equals("%")){
                return this.andMontoLikeToString(big);
            }
            if(value.equals(">")){
                return this.andMontoGreaterThan(big);
            }
            if(value.equals(">=")){
                return this.andMontoGreaterThanOrEqualTo(big);
            }   
            return (Criteria) this;
        }
        public Criteria andEstadoEqualToWithUsed(String value) {

            return (Criteria) this;
        }
    
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}