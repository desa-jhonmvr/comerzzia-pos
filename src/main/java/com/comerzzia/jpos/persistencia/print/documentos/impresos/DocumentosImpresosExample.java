package com.comerzzia.jpos.persistencia.print.documentos.impresos;

import java.util.ArrayList;
import java.util.List;

public class DocumentosImpresosExample {
    public static final String ORDER_BY_UID_DOCUMENTO = "UID_DOCUMENTO";

    public static final String ORDER_BY_UID_DOCUMENTO_DESC = "UID_DOCUMENTO DESC";

    public static final String ORDER_BY_ID_IMPRESO = "ID_IMPRESO";

    public static final String ORDER_BY_ID_IMPRESO_DESC = "ID_IMPRESO DESC";

    public static final String ORDER_BY_TIPO_IMPRESO = "TIPO_IMPRESO";

    public static final String ORDER_BY_TIPO_IMPRESO_DESC = "TIPO_IMPRESO DESC";

    public static final String ORDER_BY_IMPRESO = "IMPRESO";

    public static final String ORDER_BY_IMPRESO_DESC = "IMPRESO DESC";

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DocumentosImpresosExample() {
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
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition != null) {
                criteria.add(new Criterion(condition));
            }
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value != null) {
                criteria.add(new Criterion(condition, value));
            }
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 != null && value2 != null) {
                criteria.add(new Criterion(condition, value1, value2));
            }
        }

        public Criteria andUidDocumentoIsNull() {
            addCriterion("UID_DOCUMENTO is null");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoIsNotNull() {
            addCriterion("UID_DOCUMENTO is not null");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoEqualTo(String value) {
            addCriterion("UID_DOCUMENTO =", value, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoNotEqualTo(String value) {
            addCriterion("UID_DOCUMENTO <>", value, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoGreaterThan(String value) {
            addCriterion("UID_DOCUMENTO >", value, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoGreaterThanOrEqualTo(String value) {
            addCriterion("UID_DOCUMENTO >=", value, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoLessThan(String value) {
            addCriterion("UID_DOCUMENTO <", value, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoLessThanOrEqualTo(String value) {
            addCriterion("UID_DOCUMENTO <=", value, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoLike(String value) {
            addCriterion("UID_DOCUMENTO like", value, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoNotLike(String value) {
            addCriterion("UID_DOCUMENTO not like", value, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoIn(List<String> values) {
            addCriterion("UID_DOCUMENTO in", values, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoNotIn(List<String> values) {
            addCriterion("UID_DOCUMENTO not in", values, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoBetween(String value1, String value2) {
            addCriterion("UID_DOCUMENTO between", value1, value2, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoNotBetween(String value1, String value2) {
            addCriterion("UID_DOCUMENTO not between", value1, value2, "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andIdImpresoIsNull() {
            addCriterion("ID_IMPRESO is null");
            return (Criteria) this;
        }

        public Criteria andIdImpresoIsNotNull() {
            addCriterion("ID_IMPRESO is not null");
            return (Criteria) this;
        }

        public Criteria andIdImpresoEqualTo(Short value) {
            addCriterion("ID_IMPRESO =", value, "idImpreso");
            return (Criteria) this;
        }

        public Criteria andIdImpresoNotEqualTo(Short value) {
            addCriterion("ID_IMPRESO <>", value, "idImpreso");
            return (Criteria) this;
        }

        public Criteria andIdImpresoGreaterThan(Short value) {
            addCriterion("ID_IMPRESO >", value, "idImpreso");
            return (Criteria) this;
        }

        public Criteria andIdImpresoGreaterThanOrEqualTo(Short value) {
            addCriterion("ID_IMPRESO >=", value, "idImpreso");
            return (Criteria) this;
        }

        public Criteria andIdImpresoLessThan(Short value) {
            addCriterion("ID_IMPRESO <", value, "idImpreso");
            return (Criteria) this;
        }

        public Criteria andIdImpresoLessThanOrEqualTo(Short value) {
            addCriterion("ID_IMPRESO <=", value, "idImpreso");
            return (Criteria) this;
        }

        public Criteria andIdImpresoIn(List<Short> values) {
            addCriterion("ID_IMPRESO in", values, "idImpreso");
            return (Criteria) this;
        }

        public Criteria andIdImpresoNotIn(List<Short> values) {
            addCriterion("ID_IMPRESO not in", values, "idImpreso");
            return (Criteria) this;
        }

        public Criteria andIdImpresoBetween(Short value1, Short value2) {
            addCriterion("ID_IMPRESO between", value1, value2, "idImpreso");
            return (Criteria) this;
        }

        public Criteria andIdImpresoNotBetween(Short value1, Short value2) {
            addCriterion("ID_IMPRESO not between", value1, value2, "idImpreso");
            return (Criteria) this;
        }

        public Criteria andTipoImpresoIsNull() {
            addCriterion("TIPO_IMPRESO is null");
            return (Criteria) this;
        }

        public Criteria andTipoImpresoIsNotNull() {
            addCriterion("TIPO_IMPRESO is not null");
            return (Criteria) this;
        }

        public Criteria andTipoImpresoEqualTo(String value) {
            addCriterion("TIPO_IMPRESO =", value, "tipoImpreso");
            return (Criteria) this;
        }

        public Criteria andTipoImpresoNotEqualTo(String value) {
            addCriterion("TIPO_IMPRESO <>", value, "tipoImpreso");
            return (Criteria) this;
        }

        public Criteria andTipoImpresoGreaterThan(String value) {
            addCriterion("TIPO_IMPRESO >", value, "tipoImpreso");
            return (Criteria) this;
        }

        public Criteria andTipoImpresoGreaterThanOrEqualTo(String value) {
            addCriterion("TIPO_IMPRESO >=", value, "tipoImpreso");
            return (Criteria) this;
        }

        public Criteria andTipoImpresoLessThan(String value) {
            addCriterion("TIPO_IMPRESO <", value, "tipoImpreso");
            return (Criteria) this;
        }

        public Criteria andTipoImpresoLessThanOrEqualTo(String value) {
            addCriterion("TIPO_IMPRESO <=", value, "tipoImpreso");
            return (Criteria) this;
        }

        public Criteria andTipoImpresoLike(String value) {
            addCriterion("TIPO_IMPRESO like", value, "tipoImpreso");
            return (Criteria) this;
        }

        public Criteria andTipoImpresoNotLike(String value) {
            addCriterion("TIPO_IMPRESO not like", value, "tipoImpreso");
            return (Criteria) this;
        }

        public Criteria andTipoImpresoIn(List<String> values) {
            addCriterion("TIPO_IMPRESO in", values, "tipoImpreso");
            return (Criteria) this;
        }

        public Criteria andTipoImpresoNotIn(List<String> values) {
            addCriterion("TIPO_IMPRESO not in", values, "tipoImpreso");
            return (Criteria) this;
        }

        public Criteria andTipoImpresoBetween(String value1, String value2) {
            addCriterion("TIPO_IMPRESO between", value1, value2, "tipoImpreso");
            return (Criteria) this;
        }

        public Criteria andTipoImpresoNotBetween(String value1, String value2) {
            addCriterion("TIPO_IMPRESO not between", value1, value2, "tipoImpreso");
            return (Criteria) this;
        }

        public Criteria andUidDocumentoLikeInsensitive(String value) {
            addCriterion("upper(UID_DOCUMENTO) like", value.toUpperCase(), "uidDocumento");
            return (Criteria) this;
        }

        public Criteria andTipoImpresoLikeInsensitive(String value) {
            addCriterion("upper(TIPO_IMPRESO) like", value.toUpperCase(), "tipoImpreso");
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