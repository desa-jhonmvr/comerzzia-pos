package com.comerzzia.jpos.persistencia.listapda;

import es.mpsistemas.util.fechas.Fecha;
import java.util.ArrayList;
import java.util.List;

public class SesionPdaExample {
    public static final String ORDER_BY_UID_SESION_PDA = "UID_SESION_PDA";

    public static final String ORDER_BY_UID_SESION_PDA_DESC = "UID_SESION_PDA DESC";

    public static final String ORDER_BY_TIPO = "TIPO";

    public static final String ORDER_BY_TIPO_DESC = "TIPO DESC";

    public static final String ORDER_BY_FECHA_HORA = "FECHA_HORA";

    public static final String ORDER_BY_FECHA_HORA_DESC = "FECHA_HORA DESC";

    public static final String ORDER_BY_CODIGO = "CODIGO";

    public static final String ORDER_BY_CODIGO_DESC = "CODIGO DESC";

    public static final String ORDER_BY_UTILIZADO = "UTILIZADO";

    public static final String ORDER_BY_UTILIZADO_DESC = "UTILIZADO DESC";

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SesionPdaExample() {
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
        protected List<Criterion> fechaHoraCriteria;

        protected List<Criterion> utilizadoCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
            fechaHoraCriteria = new ArrayList<Criterion>();
            utilizadoCriteria = new ArrayList<Criterion>();
        }

        public List<Criterion> getFechaHoraCriteria() {
            return fechaHoraCriteria;
        }

        protected void addFechaHoraCriterion(String condition, Object value, String property) {
            if (value != null) {
                fechaHoraCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addFechaHoraCriterion(String condition, Fecha value1, Fecha value2, String property) {
            if (value1 != null && value2 != null) {
                fechaHoraCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getUtilizadoCriteria() {
            return utilizadoCriteria;
        }

        protected void addUtilizadoCriterion(String condition, Object value, String property) {
            if (value != null) {
                utilizadoCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addUtilizadoCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                utilizadoCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public boolean isValid() {
            return criteria.size() > 0
                || fechaHoraCriteria.size() > 0
                || utilizadoCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<Criterion>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(fechaHoraCriteria);
                allCriteria.addAll(utilizadoCriteria);
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

        public Criteria andTipoIsNull() {
            addCriterion("TIPO is null");
            return (Criteria) this;
        }

        public Criteria andTipoIsNotNull() {
            addCriterion("TIPO is not null");
            return (Criteria) this;
        }

        public Criteria andTipoEqualTo(String value) {
            addCriterion("TIPO =", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoNotEqualTo(String value) {
            addCriterion("TIPO <>", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoGreaterThan(String value) {
            addCriterion("TIPO >", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoGreaterThanOrEqualTo(String value) {
            addCriterion("TIPO >=", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoLessThan(String value) {
            addCriterion("TIPO <", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoLessThanOrEqualTo(String value) {
            addCriterion("TIPO <=", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoLike(String value) {
            addCriterion("TIPO like", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoNotLike(String value) {
            addCriterion("TIPO not like", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoIn(List<String> values) {
            addCriterion("TIPO in", values, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoNotIn(List<String> values) {
            addCriterion("TIPO not in", values, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoBetween(String value1, String value2) {
            addCriterion("TIPO between", value1, value2, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoNotBetween(String value1, String value2) {
            addCriterion("TIPO not between", value1, value2, "tipo");
            return (Criteria) this;
        }

        public Criteria andFechaHoraIsNull() {
            addCriterion("FECHA_HORA is null");
            return (Criteria) this;
        }

        public Criteria andFechaHoraIsNotNull() {
            addCriterion("FECHA_HORA is not null");
            return (Criteria) this;
        }

        public Criteria andFechaHoraEqualTo(Fecha value) {
            addFechaHoraCriterion("FECHA_HORA =", value, "fechaHora");
            return (Criteria) this;
        }

        public Criteria andFechaHoraNotEqualTo(Fecha value) {
            addFechaHoraCriterion("FECHA_HORA <>", value, "fechaHora");
            return (Criteria) this;
        }

        public Criteria andFechaHoraGreaterThan(Fecha value) {
            addFechaHoraCriterion("FECHA_HORA >", value, "fechaHora");
            return (Criteria) this;
        }

        public Criteria andFechaHoraGreaterThanOrEqualTo(Fecha value) {
            addFechaHoraCriterion("FECHA_HORA >=", value, "fechaHora");
            return (Criteria) this;
        }

        public Criteria andFechaHoraLessThan(Fecha value) {
            addFechaHoraCriterion("FECHA_HORA <", value, "fechaHora");
            return (Criteria) this;
        }

        public Criteria andFechaHoraLessThanOrEqualTo(Fecha value) {
            addFechaHoraCriterion("FECHA_HORA <=", value, "fechaHora");
            return (Criteria) this;
        }

        public Criteria andFechaHoraIn(List<Fecha> values) {
            addFechaHoraCriterion("FECHA_HORA in", values, "fechaHora");
            return (Criteria) this;
        }

        public Criteria andFechaHoraNotIn(List<Fecha> values) {
            addFechaHoraCriterion("FECHA_HORA not in", values, "fechaHora");
            return (Criteria) this;
        }

        public Criteria andFechaHoraBetween(Fecha value1, Fecha value2) {
            addFechaHoraCriterion("FECHA_HORA between", value1, value2, "fechaHora");
            return (Criteria) this;
        }

        public Criteria andFechaHoraNotBetween(Fecha value1, Fecha value2) {
            addFechaHoraCriterion("FECHA_HORA not between", value1, value2, "fechaHora");
            return (Criteria) this;
        }

        public Criteria andCodigoIsNull() {
            addCriterion("CODIGO is null");
            return (Criteria) this;
        }

        public Criteria andCodigoIsNotNull() {
            addCriterion("CODIGO is not null");
            return (Criteria) this;
        }

        public Criteria andCodigoEqualTo(String value) {
            addCriterion("CODIGO =", value, "codigo");
            return (Criteria) this;
        }

        public Criteria andCodigoNotEqualTo(String value) {
            addCriterion("CODIGO <>", value, "codigo");
            return (Criteria) this;
        }

        public Criteria andCodigoGreaterThan(String value) {
            addCriterion("CODIGO >", value, "codigo");
            return (Criteria) this;
        }

        public Criteria andCodigoGreaterThanOrEqualTo(String value) {
            addCriterion("CODIGO >=", value, "codigo");
            return (Criteria) this;
        }

        public Criteria andCodigoLessThan(String value) {
            addCriterion("CODIGO <", value, "codigo");
            return (Criteria) this;
        }

        public Criteria andCodigoLessThanOrEqualTo(String value) {
            addCriterion("CODIGO <=", value, "codigo");
            return (Criteria) this;
        }

        public Criteria andCodigoLike(String value) {
            addCriterion("CODIGO like", value, "codigo");
            return (Criteria) this;
        }

        public Criteria andCodigoNotLike(String value) {
            addCriterion("CODIGO not like", value, "codigo");
            return (Criteria) this;
        }

        public Criteria andCodigoIn(List<String> values) {
            addCriterion("CODIGO in", values, "codigo");
            return (Criteria) this;
        }

        public Criteria andCodigoNotIn(List<String> values) {
            addCriterion("CODIGO not in", values, "codigo");
            return (Criteria) this;
        }

        public Criteria andCodigoBetween(String value1, String value2) {
            addCriterion("CODIGO between", value1, value2, "codigo");
            return (Criteria) this;
        }

        public Criteria andCodigoNotBetween(String value1, String value2) {
            addCriterion("CODIGO not between", value1, value2, "codigo");
            return (Criteria) this;
        }

        public Criteria andUtilizadoIsNull() {
            addCriterion("UTILIZADO is null");
            return (Criteria) this;
        }

        public Criteria andUtilizadoIsNotNull() {
            addCriterion("UTILIZADO is not null");
            return (Criteria) this;
        }

        public Criteria andUtilizadoEqualTo(Boolean value) {
            addUtilizadoCriterion("UTILIZADO =", value, "utilizado");
            return (Criteria) this;
        }

        public Criteria andUtilizadoNotEqualTo(Boolean value) {
            addUtilizadoCriterion("UTILIZADO <>", value, "utilizado");
            return (Criteria) this;
        }

        public Criteria andUtilizadoGreaterThan(Boolean value) {
            addUtilizadoCriterion("UTILIZADO >", value, "utilizado");
            return (Criteria) this;
        }

        public Criteria andUtilizadoGreaterThanOrEqualTo(Boolean value) {
            addUtilizadoCriterion("UTILIZADO >=", value, "utilizado");
            return (Criteria) this;
        }

        public Criteria andUtilizadoLessThan(Boolean value) {
            addUtilizadoCriterion("UTILIZADO <", value, "utilizado");
            return (Criteria) this;
        }

        public Criteria andUtilizadoLessThanOrEqualTo(Boolean value) {
            addUtilizadoCriterion("UTILIZADO <=", value, "utilizado");
            return (Criteria) this;
        }

        public Criteria andUtilizadoLike(Boolean value) {
            addUtilizadoCriterion("UTILIZADO like", value, "utilizado");
            return (Criteria) this;
        }

        public Criteria andUtilizadoNotLike(Boolean value) {
            addUtilizadoCriterion("UTILIZADO not like", value, "utilizado");
            return (Criteria) this;
        }

        public Criteria andUtilizadoIn(List<Boolean> values) {
            addUtilizadoCriterion("UTILIZADO in", values, "utilizado");
            return (Criteria) this;
        }

        public Criteria andUtilizadoNotIn(List<Boolean> values) {
            addUtilizadoCriterion("UTILIZADO not in", values, "utilizado");
            return (Criteria) this;
        }

        public Criteria andUtilizadoBetween(Boolean value1, Boolean value2) {
            addUtilizadoCriterion("UTILIZADO between", value1, value2, "utilizado");
            return (Criteria) this;
        }

        public Criteria andUtilizadoNotBetween(Boolean value1, Boolean value2) {
            addUtilizadoCriterion("UTILIZADO not between", value1, value2, "utilizado");
            return (Criteria) this;
        }

        public Criteria andUidSesionPdaLikeInsensitive(String value) {
            addCriterion("upper(UID_SESION_PDA) like", value.toUpperCase(), "uidSesionPda");
            return (Criteria) this;
        }

        public Criteria andTipoLikeInsensitive(String value) {
            addCriterion("upper(TIPO) like", value.toUpperCase(), "tipo");
            return (Criteria) this;
        }

        public Criteria andCodigoLikeInsensitive(String value) {
            addCriterion("upper(CODIGO) like", value.toUpperCase(), "codigo");
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