package com.comerzzia.jpos.persistencia.listapda.detalle;

import java.util.ArrayList;
import java.util.List;

public class DetalleSesionPdaExample {
    public static final String ORDER_BY_UID_SESION_PDA = "UID_SESION_PDA";

    public static final String ORDER_BY_UID_SESION_PDA_DESC = "UID_SESION_PDA DESC";

    public static final String ORDER_BY_ID_LINEA = "ID_LINEA";

    public static final String ORDER_BY_ID_LINEA_DESC = "ID_LINEA DESC";

    public static final String ORDER_BY_CODART = "CODART";

    public static final String ORDER_BY_CODART_DESC = "CODART DESC";

    public static final String ORDER_BY_CODIGO_BARRAS = "CODIGO_BARRAS";

    public static final String ORDER_BY_CODIGO_BARRAS_DESC = "CODIGO_BARRAS DESC";

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DetalleSesionPdaExample() {
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

        public Criteria andUidSesionPdaIsNull() {
            addCriterion("UID_SESION_PDA is null");
            return (Criteria) this;
        }

        public Criteria andUidSesionPdaIsNotNull() {
            addCriterion("UID_SESION_PDA is not null");
            return (Criteria) this;
        }

        public Criteria andUidSesionPdaEqualTo(String value) {
            addCriterion("UID_SESION_PDA =", value, "uidSesionPda");
            return (Criteria) this;
        }

        public Criteria andUidSesionPdaNotEqualTo(String value) {
            addCriterion("UID_SESION_PDA <>", value, "uidSesionPda");
            return (Criteria) this;
        }

        public Criteria andUidSesionPdaGreaterThan(String value) {
            addCriterion("UID_SESION_PDA >", value, "uidSesionPda");
            return (Criteria) this;
        }

        public Criteria andUidSesionPdaGreaterThanOrEqualTo(String value) {
            addCriterion("UID_SESION_PDA >=", value, "uidSesionPda");
            return (Criteria) this;
        }

        public Criteria andUidSesionPdaLessThan(String value) {
            addCriterion("UID_SESION_PDA <", value, "uidSesionPda");
            return (Criteria) this;
        }

        public Criteria andUidSesionPdaLessThanOrEqualTo(String value) {
            addCriterion("UID_SESION_PDA <=", value, "uidSesionPda");
            return (Criteria) this;
        }

        public Criteria andUidSesionPdaLike(String value) {
            addCriterion("UID_SESION_PDA like", value, "uidSesionPda");
            return (Criteria) this;
        }

        public Criteria andUidSesionPdaNotLike(String value) {
            addCriterion("UID_SESION_PDA not like", value, "uidSesionPda");
            return (Criteria) this;
        }

        public Criteria andUidSesionPdaIn(List<String> values) {
            addCriterion("UID_SESION_PDA in", values, "uidSesionPda");
            return (Criteria) this;
        }

        public Criteria andUidSesionPdaNotIn(List<String> values) {
            addCriterion("UID_SESION_PDA not in", values, "uidSesionPda");
            return (Criteria) this;
        }

        public Criteria andUidSesionPdaBetween(String value1, String value2) {
            addCriterion("UID_SESION_PDA between", value1, value2, "uidSesionPda");
            return (Criteria) this;
        }

        public Criteria andUidSesionPdaNotBetween(String value1, String value2) {
            addCriterion("UID_SESION_PDA not between", value1, value2, "uidSesionPda");
            return (Criteria) this;
        }

        public Criteria andIdLineaIsNull() {
            addCriterion("ID_LINEA is null");
            return (Criteria) this;
        }

        public Criteria andIdLineaIsNotNull() {
            addCriterion("ID_LINEA is not null");
            return (Criteria) this;
        }

        public Criteria andIdLineaEqualTo(Short value) {
            addCriterion("ID_LINEA =", value, "idLinea");
            return (Criteria) this;
        }

        public Criteria andIdLineaNotEqualTo(Short value) {
            addCriterion("ID_LINEA <>", value, "idLinea");
            return (Criteria) this;
        }

        public Criteria andIdLineaGreaterThan(Short value) {
            addCriterion("ID_LINEA >", value, "idLinea");
            return (Criteria) this;
        }

        public Criteria andIdLineaGreaterThanOrEqualTo(Short value) {
            addCriterion("ID_LINEA >=", value, "idLinea");
            return (Criteria) this;
        }

        public Criteria andIdLineaLessThan(Short value) {
            addCriterion("ID_LINEA <", value, "idLinea");
            return (Criteria) this;
        }

        public Criteria andIdLineaLessThanOrEqualTo(Short value) {
            addCriterion("ID_LINEA <=", value, "idLinea");
            return (Criteria) this;
        }

        public Criteria andIdLineaIn(List<Short> values) {
            addCriterion("ID_LINEA in", values, "idLinea");
            return (Criteria) this;
        }

        public Criteria andIdLineaNotIn(List<Short> values) {
            addCriterion("ID_LINEA not in", values, "idLinea");
            return (Criteria) this;
        }

        public Criteria andIdLineaBetween(Short value1, Short value2) {
            addCriterion("ID_LINEA between", value1, value2, "idLinea");
            return (Criteria) this;
        }

        public Criteria andIdLineaNotBetween(Short value1, Short value2) {
            addCriterion("ID_LINEA not between", value1, value2, "idLinea");
            return (Criteria) this;
        }

        public Criteria andCodartIsNull() {
            addCriterion("CODART is null");
            return (Criteria) this;
        }

        public Criteria andCodartIsNotNull() {
            addCriterion("CODART is not null");
            return (Criteria) this;
        }

        public Criteria andCodartEqualTo(String value) {
            addCriterion("CODART =", value, "codart");
            return (Criteria) this;
        }

        public Criteria andCodartNotEqualTo(String value) {
            addCriterion("CODART <>", value, "codart");
            return (Criteria) this;
        }

        public Criteria andCodartGreaterThan(String value) {
            addCriterion("CODART >", value, "codart");
            return (Criteria) this;
        }

        public Criteria andCodartGreaterThanOrEqualTo(String value) {
            addCriterion("CODART >=", value, "codart");
            return (Criteria) this;
        }

        public Criteria andCodartLessThan(String value) {
            addCriterion("CODART <", value, "codart");
            return (Criteria) this;
        }

        public Criteria andCodartLessThanOrEqualTo(String value) {
            addCriterion("CODART <=", value, "codart");
            return (Criteria) this;
        }

        public Criteria andCodartLike(String value) {
            addCriterion("CODART like", value, "codart");
            return (Criteria) this;
        }

        public Criteria andCodartNotLike(String value) {
            addCriterion("CODART not like", value, "codart");
            return (Criteria) this;
        }

        public Criteria andCodartIn(List<String> values) {
            addCriterion("CODART in", values, "codart");
            return (Criteria) this;
        }

        public Criteria andCodartNotIn(List<String> values) {
            addCriterion("CODART not in", values, "codart");
            return (Criteria) this;
        }

        public Criteria andCodartBetween(String value1, String value2) {
            addCriterion("CODART between", value1, value2, "codart");
            return (Criteria) this;
        }

        public Criteria andCodartNotBetween(String value1, String value2) {
            addCriterion("CODART not between", value1, value2, "codart");
            return (Criteria) this;
        }

        public Criteria andCodigoBarrasIsNull() {
            addCriterion("CODIGO_BARRAS is null");
            return (Criteria) this;
        }

        public Criteria andCodigoBarrasIsNotNull() {
            addCriterion("CODIGO_BARRAS is not null");
            return (Criteria) this;
        }

        public Criteria andCodigoBarrasEqualTo(String value) {
            addCriterion("CODIGO_BARRAS =", value, "codigoBarras");
            return (Criteria) this;
        }

        public Criteria andCodigoBarrasNotEqualTo(String value) {
            addCriterion("CODIGO_BARRAS <>", value, "codigoBarras");
            return (Criteria) this;
        }

        public Criteria andCodigoBarrasGreaterThan(String value) {
            addCriterion("CODIGO_BARRAS >", value, "codigoBarras");
            return (Criteria) this;
        }

        public Criteria andCodigoBarrasGreaterThanOrEqualTo(String value) {
            addCriterion("CODIGO_BARRAS >=", value, "codigoBarras");
            return (Criteria) this;
        }

        public Criteria andCodigoBarrasLessThan(String value) {
            addCriterion("CODIGO_BARRAS <", value, "codigoBarras");
            return (Criteria) this;
        }

        public Criteria andCodigoBarrasLessThanOrEqualTo(String value) {
            addCriterion("CODIGO_BARRAS <=", value, "codigoBarras");
            return (Criteria) this;
        }

        public Criteria andCodigoBarrasLike(String value) {
            addCriterion("CODIGO_BARRAS like", value, "codigoBarras");
            return (Criteria) this;
        }

        public Criteria andCodigoBarrasNotLike(String value) {
            addCriterion("CODIGO_BARRAS not like", value, "codigoBarras");
            return (Criteria) this;
        }

        public Criteria andCodigoBarrasIn(List<String> values) {
            addCriterion("CODIGO_BARRAS in", values, "codigoBarras");
            return (Criteria) this;
        }

        public Criteria andCodigoBarrasNotIn(List<String> values) {
            addCriterion("CODIGO_BARRAS not in", values, "codigoBarras");
            return (Criteria) this;
        }

        public Criteria andCodigoBarrasBetween(String value1, String value2) {
            addCriterion("CODIGO_BARRAS between", value1, value2, "codigoBarras");
            return (Criteria) this;
        }

        public Criteria andCodigoBarrasNotBetween(String value1, String value2) {
            addCriterion("CODIGO_BARRAS not between", value1, value2, "codigoBarras");
            return (Criteria) this;
        }

        public Criteria andUidSesionPdaLikeInsensitive(String value) {
            addCriterion("upper(UID_SESION_PDA) like", value.toUpperCase(), "uidSesionPda");
            return (Criteria) this;
        }

        public Criteria andCodartLikeInsensitive(String value) {
            addCriterion("upper(CODART) like", value.toUpperCase(), "codart");
            return (Criteria) this;
        }

        public Criteria andCodigoBarrasLikeInsensitive(String value) {
            addCriterion("upper(CODIGO_BARRAS) like", value.toUpperCase(), "codigoBarras");
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