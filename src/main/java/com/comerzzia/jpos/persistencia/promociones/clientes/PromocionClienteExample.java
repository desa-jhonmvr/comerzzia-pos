package com.comerzzia.jpos.persistencia.promociones.clientes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PromocionClienteExample {
    public static final String ORDER_BY_ID_PROMOCION = "ID_PROMOCION";

    public static final String ORDER_BY_ID_PROMOCION_DESC = "ID_PROMOCION DESC";

    public static final String ORDER_BY_CODCLI = "CODCLI";

    public static final String ORDER_BY_CODCLI_DESC = "CODCLI DESC";

    public static final String ORDER_BY_UID_TICKET = "UID_TICKET";

    public static final String ORDER_BY_UID_TICKET_DESC = "UID_TICKET DESC";

    public static final String ORDER_BY_CODALM = "CODALM";

    public static final String ORDER_BY_CODALM_DESC = "CODALM DESC";

    public static final String ORDER_BY_FECHA = "FECHA";

    public static final String ORDER_BY_FECHA_DESC = "FECHA DESC";

    public static final String ORDER_BY_VERSION = "VERSION";

    public static final String ORDER_BY_VERSION_DESC = "VERSION DESC";

    public static final String ORDER_BY_PROCESADO = "PROCESADO";

    public static final String ORDER_BY_PROCESADO_DESC = "PROCESADO DESC";

    public static final String ORDER_BY_ANULADA = "ANULADA";

    public static final String ORDER_BY_ANULADA_DESC = "ANULADA DESC";

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public PromocionClienteExample() {
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
        protected List<Criterion> procesadoCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
            procesadoCriteria = new ArrayList<Criterion>();
        }

        public List<Criterion> getProcesadoCriteria() {
            return procesadoCriteria;
        }

        protected void addProcesadoCriterion(String condition, Object value, String property) {
            if (value != null) {
                procesadoCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addProcesadoCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                procesadoCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public boolean isValid() {
            return criteria.size() > 0
                || procesadoCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<Criterion>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(procesadoCriteria);
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

        public Criteria andIdPromocionIsNull() {
            addCriterion("ID_PROMOCION is null");
            return (Criteria) this;
        }

        public Criteria andIdPromocionIsNotNull() {
            addCriterion("ID_PROMOCION is not null");
            return (Criteria) this;
        }

        public Criteria andIdPromocionEqualTo(Long value) {
            addCriterion("ID_PROMOCION =", value, "idPromocion");
            return (Criteria) this;
        }

        public Criteria andIdPromocionNotEqualTo(Long value) {
            addCriterion("ID_PROMOCION <>", value, "idPromocion");
            return (Criteria) this;
        }

        public Criteria andIdPromocionGreaterThan(Long value) {
            addCriterion("ID_PROMOCION >", value, "idPromocion");
            return (Criteria) this;
        }

        public Criteria andIdPromocionGreaterThanOrEqualTo(Long value) {
            addCriterion("ID_PROMOCION >=", value, "idPromocion");
            return (Criteria) this;
        }

        public Criteria andIdPromocionLessThan(Long value) {
            addCriterion("ID_PROMOCION <", value, "idPromocion");
            return (Criteria) this;
        }

        public Criteria andIdPromocionLessThanOrEqualTo(Long value) {
            addCriterion("ID_PROMOCION <=", value, "idPromocion");
            return (Criteria) this;
        }

        public Criteria andIdPromocionIn(List<Long> values) {
            addCriterion("ID_PROMOCION in", values, "idPromocion");
            return (Criteria) this;
        }

        public Criteria andIdPromocionNotIn(List<Long> values) {
            addCriterion("ID_PROMOCION not in", values, "idPromocion");
            return (Criteria) this;
        }

        public Criteria andIdPromocionBetween(Long value1, Long value2) {
            addCriterion("ID_PROMOCION between", value1, value2, "idPromocion");
            return (Criteria) this;
        }

        public Criteria andIdPromocionNotBetween(Long value1, Long value2) {
            addCriterion("ID_PROMOCION not between", value1, value2, "idPromocion");
            return (Criteria) this;
        }

        public Criteria andCodClienteIsNull() {
            addCriterion("CODCLI is null");
            return (Criteria) this;
        }

        public Criteria andCodClienteIsNotNull() {
            addCriterion("CODCLI is not null");
            return (Criteria) this;
        }

        public Criteria andCodClienteEqualTo(String value) {
            addCriterion("CODCLI =", value, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteNotEqualTo(String value) {
            addCriterion("CODCLI <>", value, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteGreaterThan(String value) {
            addCriterion("CODCLI >", value, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteGreaterThanOrEqualTo(String value) {
            addCriterion("CODCLI >=", value, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteLessThan(String value) {
            addCriterion("CODCLI <", value, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteLessThanOrEqualTo(String value) {
            addCriterion("CODCLI <=", value, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteLike(String value) {
            addCriterion("CODCLI like", value, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteNotLike(String value) {
            addCriterion("CODCLI not like", value, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteIn(List<String> values) {
            addCriterion("CODCLI in", values, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteNotIn(List<String> values) {
            addCriterion("CODCLI not in", values, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteBetween(String value1, String value2) {
            addCriterion("CODCLI between", value1, value2, "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodClienteNotBetween(String value1, String value2) {
            addCriterion("CODCLI not between", value1, value2, "codCliente");
            return (Criteria) this;
        }

        public Criteria andUidTicketIsNull() {
            addCriterion("UID_TICKET is null");
            return (Criteria) this;
        }

        public Criteria andUidTicketIsNotNull() {
            addCriterion("UID_TICKET is not null");
            return (Criteria) this;
        }

        public Criteria andUidTicketEqualTo(String value) {
            addCriterion("UID_TICKET =", value, "uidTicket");
            return (Criteria) this;
        }
        
        public Criteria andUidTicketDSEqualTo(String value) {
            addCriterion("UID_TICKET_DIA_SOCIO =", value, "uidTicketDSocio");
            return (Criteria) this;
        }
        
        public Criteria andUidTicketNotEqualTo(String value) {
            addCriterion("UID_TICKET <>", value, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketGreaterThan(String value) {
            addCriterion("UID_TICKET >", value, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketGreaterThanOrEqualTo(String value) {
            addCriterion("UID_TICKET >=", value, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketLessThan(String value) {
            addCriterion("UID_TICKET <", value, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketLessThanOrEqualTo(String value) {
            addCriterion("UID_TICKET <=", value, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketLike(String value) {
            addCriterion("UID_TICKET like", value, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotLike(String value) {
            addCriterion("UID_TICKET not like", value, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketIn(List<String> values) {
            addCriterion("UID_TICKET in", values, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotIn(List<String> values) {
            addCriterion("UID_TICKET not in", values, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketBetween(String value1, String value2) {
            addCriterion("UID_TICKET between", value1, value2, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andUidTicketNotBetween(String value1, String value2) {
            addCriterion("UID_TICKET not between", value1, value2, "uidTicket");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenIsNull() {
            addCriterion("CODALM is null");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenIsNotNull() {
            addCriterion("CODALM is not null");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenEqualTo(String value) {
            addCriterion("CODALM =", value, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenNotEqualTo(String value) {
            addCriterion("CODALM <>", value, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenGreaterThan(String value) {
            addCriterion("CODALM >", value, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenGreaterThanOrEqualTo(String value) {
            addCriterion("CODALM >=", value, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenLessThan(String value) {
            addCriterion("CODALM <", value, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenLessThanOrEqualTo(String value) {
            addCriterion("CODALM <=", value, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenLike(String value) {
            addCriterion("CODALM like", value, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenNotLike(String value) {
            addCriterion("CODALM not like", value, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenIn(List<String> values) {
            addCriterion("CODALM in", values, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenNotIn(List<String> values) {
            addCriterion("CODALM not in", values, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenBetween(String value1, String value2) {
            addCriterion("CODALM between", value1, value2, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenNotBetween(String value1, String value2) {
            addCriterion("CODALM not between", value1, value2, "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andFechaIsNull() {
            addCriterion("FECHA is null");
            return (Criteria) this;
        }

        public Criteria andFechaIsNotNull() {
            addCriterion("FECHA is not null");
            return (Criteria) this;
        }

        public Criteria andFechaEqualTo(Date value) {
            addCriterion("FECHA =", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaNotEqualTo(Date value) {
            addCriterion("FECHA <>", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaGreaterThan(Date value) {
            addCriterion("FECHA >", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaGreaterThanOrEqualTo(Date value) {
            addCriterion("FECHA >=", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaLessThan(Date value) {
            addCriterion("FECHA <", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaLessThanOrEqualTo(Date value) {
            addCriterion("FECHA <=", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaIn(List<Date> values) {
            addCriterion("FECHA in", values, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaNotIn(List<Date> values) {
            addCriterion("FECHA not in", values, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaBetween(Date value1, Date value2) {
            addCriterion("FECHA between", value1, value2, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaNotBetween(Date value1, Date value2) {
            addCriterion("FECHA not between", value1, value2, "fecha");
            return (Criteria) this;
        }

        public Criteria andVersionIsNull() {
            addCriterion("VERSION is null");
            return (Criteria) this;
        }

        public Criteria andVersionIsNotNull() {
            addCriterion("VERSION is not null");
            return (Criteria) this;
        }

        public Criteria andVersionEqualTo(Integer value) {
            addCriterion("VERSION =", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotEqualTo(Integer value) {
            addCriterion("VERSION <>", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThan(Integer value) {
            addCriterion("VERSION >", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThanOrEqualTo(Integer value) {
            addCriterion("VERSION >=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThan(Integer value) {
            addCriterion("VERSION <", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThanOrEqualTo(Integer value) {
            addCriterion("VERSION <=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionIn(List<Integer> values) {
            addCriterion("VERSION in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotIn(List<Integer> values) {
            addCriterion("VERSION not in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionBetween(Integer value1, Integer value2) {
            addCriterion("VERSION between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotBetween(Integer value1, Integer value2) {
            addCriterion("VERSION not between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andProcesadoIsNull() {
            addCriterion("PROCESADO is null");
            return (Criteria) this;
        }

        public Criteria andProcesadoIsNotNull() {
            addCriterion("PROCESADO is not null");
            return (Criteria) this;
        }

        public Criteria andProcesadoEqualTo(Boolean value) {
            addProcesadoCriterion("PROCESADO =", value, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoNotEqualTo(Boolean value) {
            addProcesadoCriterion("PROCESADO <>", value, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoGreaterThan(Boolean value) {
            addProcesadoCriterion("PROCESADO >", value, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoGreaterThanOrEqualTo(Boolean value) {
            addProcesadoCriterion("PROCESADO >=", value, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoLessThan(Boolean value) {
            addProcesadoCriterion("PROCESADO <", value, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoLessThanOrEqualTo(Boolean value) {
            addProcesadoCriterion("PROCESADO <=", value, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoLike(Boolean value) {
            addProcesadoCriterion("PROCESADO like", value, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoNotLike(Boolean value) {
            addProcesadoCriterion("PROCESADO not like", value, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoIn(List<Boolean> values) {
            addProcesadoCriterion("PROCESADO in", values, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoNotIn(List<Boolean> values) {
            addProcesadoCriterion("PROCESADO not in", values, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoBetween(Boolean value1, Boolean value2) {
            addProcesadoCriterion("PROCESADO between", value1, value2, "procesado");
            return (Criteria) this;
        }

        public Criteria andProcesadoNotBetween(Boolean value1, Boolean value2) {
            addProcesadoCriterion("PROCESADO not between", value1, value2, "procesado");
            return (Criteria) this;
        }

        public Criteria andAnuladaIsNull() {
            addCriterion("ANULADA is null");
            return (Criteria) this;
        }

        public Criteria andAnuladaIsNotNull() {
            addCriterion("ANULADA is not null");
            return (Criteria) this;
        }

        public Criteria andAnuladaEqualTo(String value) {
            addCriterion("ANULADA =", value, "anulada");
            return (Criteria) this;
        }

        public Criteria andAnuladaNotEqualTo(String value) {
            addCriterion("ANULADA <>", value, "anulada");
            return (Criteria) this;
        }

        public Criteria andAnuladaGreaterThan(String value) {
            addCriterion("ANULADA >", value, "anulada");
            return (Criteria) this;
        }

        public Criteria andAnuladaGreaterThanOrEqualTo(String value) {
            addCriterion("ANULADA >=", value, "anulada");
            return (Criteria) this;
        }

        public Criteria andAnuladaLessThan(String value) {
            addCriterion("ANULADA <", value, "anulada");
            return (Criteria) this;
        }

        public Criteria andAnuladaLessThanOrEqualTo(String value) {
            addCriterion("ANULADA <=", value, "anulada");
            return (Criteria) this;
        }

        public Criteria andAnuladaLike(String value) {
            addCriterion("ANULADA like", value, "anulada");
            return (Criteria) this;
        }

        public Criteria andAnuladaNotLike(String value) {
            addCriterion("ANULADA not like", value, "anulada");
            return (Criteria) this;
        }

        public Criteria andAnuladaIn(List<String> values) {
            addCriterion("ANULADA in", values, "anulada");
            return (Criteria) this;
        }

        public Criteria andAnuladaNotIn(List<String> values) {
            addCriterion("ANULADA not in", values, "anulada");
            return (Criteria) this;
        }

        public Criteria andAnuladaBetween(String value1, String value2) {
            addCriterion("ANULADA between", value1, value2, "anulada");
            return (Criteria) this;
        }

        public Criteria andAnuladaNotBetween(String value1, String value2) {
            addCriterion("ANULADA not between", value1, value2, "anulada");
            return (Criteria) this;
        }

        public Criteria andCodClienteLikeInsensitive(String value) {
            addCriterion("upper(CODCLI) like", value.toUpperCase(), "codCliente");
            return (Criteria) this;
        }

        public Criteria andUidTicketLikeInsensitive(String value) {
            addCriterion("upper(UID_TICKET) like", value.toUpperCase(), "uidTicket");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenLikeInsensitive(String value) {
            addCriterion("upper(CODALM) like", value.toUpperCase(), "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andAnuladaLikeInsensitive(String value) {
            addCriterion("upper(ANULADA) like", value.toUpperCase(), "anulada");
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