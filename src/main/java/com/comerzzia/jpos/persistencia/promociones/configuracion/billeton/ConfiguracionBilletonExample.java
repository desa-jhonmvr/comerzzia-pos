package com.comerzzia.jpos.persistencia.promociones.configuracion.billeton;

import es.mpsistemas.util.fechas.Fecha;
import java.util.ArrayList;
import java.util.List;

public class ConfiguracionBilletonExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ConfiguracionBilletonExample() {
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
        protected List<Criterion> vigenteCriteria;

        protected List<Criterion> fechaAltaCriteria;

        protected List<Criterion> fechaCambioVigenciaCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
            vigenteCriteria = new ArrayList<Criterion>();
            fechaAltaCriteria = new ArrayList<Criterion>();
            fechaCambioVigenciaCriteria = new ArrayList<Criterion>();
        }

        public List<Criterion> getVigenteCriteria() {
            return vigenteCriteria;
        }

        protected void addVigenteCriterion(String condition, Object value, String property) {
            if (value != null) {
                vigenteCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addVigenteCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                vigenteCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getFechaAltaCriteria() {
            return fechaAltaCriteria;
        }

        protected void addFechaAltaCriterion(String condition, Object value, String property) {
            if (value != null) {
                fechaAltaCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addFechaAltaCriterion(String condition, Fecha value1, Fecha value2, String property) {
            if (value1 != null && value2 != null) {
                fechaAltaCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getFechaCambioVigenciaCriteria() {
            return fechaCambioVigenciaCriteria;
        }

        protected void addFechaCambioVigenciaCriterion(String condition, Object value, String property) {
            if (value != null) {
                fechaCambioVigenciaCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addFechaCambioVigenciaCriterion(String condition, Fecha value1, Fecha value2, String property) {
            if (value1 != null && value2 != null) {
                fechaCambioVigenciaCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
                allCriteria = null;
            }
        }

        public boolean isValid() {
            return criteria.size() > 0
                || vigenteCriteria.size() > 0
                || fechaAltaCriteria.size() > 0
                || fechaCambioVigenciaCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<Criterion>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(vigenteCriteria);
                allCriteria.addAll(fechaAltaCriteria);
                allCriteria.addAll(fechaCambioVigenciaCriteria);
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

        public Criteria andIdConfBilletonIsNull() {
            addCriterion("ID_CONF_BILLETON is null");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonIsNotNull() {
            addCriterion("ID_CONF_BILLETON is not null");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonEqualTo(Long value) {
            addCriterion("ID_CONF_BILLETON =", value, "idConfBilleton");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonNotEqualTo(Long value) {
            addCriterion("ID_CONF_BILLETON <>", value, "idConfBilleton");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonGreaterThan(Long value) {
            addCriterion("ID_CONF_BILLETON >", value, "idConfBilleton");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonGreaterThanOrEqualTo(Long value) {
            addCriterion("ID_CONF_BILLETON >=", value, "idConfBilleton");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonLessThan(Long value) {
            addCriterion("ID_CONF_BILLETON <", value, "idConfBilleton");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonLessThanOrEqualTo(Long value) {
            addCriterion("ID_CONF_BILLETON <=", value, "idConfBilleton");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonIn(List<Long> values) {
            addCriterion("ID_CONF_BILLETON in", values, "idConfBilleton");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonNotIn(List<Long> values) {
            addCriterion("ID_CONF_BILLETON not in", values, "idConfBilleton");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonBetween(Long value1, Long value2) {
            addCriterion("ID_CONF_BILLETON between", value1, value2, "idConfBilleton");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonNotBetween(Long value1, Long value2) {
            addCriterion("ID_CONF_BILLETON not between", value1, value2, "idConfBilleton");
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

        public Criteria andVigenteIsNull() {
            addCriterion("VIGENTE is null");
            return (Criteria) this;
        }

        public Criteria andVigenteIsNotNull() {
            addCriterion("VIGENTE is not null");
            return (Criteria) this;
        }

        public Criteria andVigenteEqualTo(Boolean value) {
            addVigenteCriterion("VIGENTE =", value, "vigente");
            return (Criteria) this;
        }

        public Criteria andVigenteNotEqualTo(Boolean value) {
            addVigenteCriterion("VIGENTE <>", value, "vigente");
            return (Criteria) this;
        }

        public Criteria andVigenteGreaterThan(Boolean value) {
            addVigenteCriterion("VIGENTE >", value, "vigente");
            return (Criteria) this;
        }

        public Criteria andVigenteGreaterThanOrEqualTo(Boolean value) {
            addVigenteCriterion("VIGENTE >=", value, "vigente");
            return (Criteria) this;
        }

        public Criteria andVigenteLessThan(Boolean value) {
            addVigenteCriterion("VIGENTE <", value, "vigente");
            return (Criteria) this;
        }

        public Criteria andVigenteLessThanOrEqualTo(Boolean value) {
            addVigenteCriterion("VIGENTE <=", value, "vigente");
            return (Criteria) this;
        }

        public Criteria andVigenteLike(Boolean value) {
            addVigenteCriterion("VIGENTE like", value, "vigente");
            return (Criteria) this;
        }

        public Criteria andVigenteNotLike(Boolean value) {
            addVigenteCriterion("VIGENTE not like", value, "vigente");
            return (Criteria) this;
        }

        public Criteria andVigenteIn(List<Boolean> values) {
            addVigenteCriterion("VIGENTE in", values, "vigente");
            return (Criteria) this;
        }

        public Criteria andVigenteNotIn(List<Boolean> values) {
            addVigenteCriterion("VIGENTE not in", values, "vigente");
            return (Criteria) this;
        }

        public Criteria andVigenteBetween(Boolean value1, Boolean value2) {
            addVigenteCriterion("VIGENTE between", value1, value2, "vigente");
            return (Criteria) this;
        }

        public Criteria andVigenteNotBetween(Boolean value1, Boolean value2) {
            addVigenteCriterion("VIGENTE not between", value1, value2, "vigente");
            return (Criteria) this;
        }

        public Criteria andDescripcionIsNull() {
            addCriterion("DESCRIPCION is null");
            return (Criteria) this;
        }

        public Criteria andDescripcionIsNotNull() {
            addCriterion("DESCRIPCION is not null");
            return (Criteria) this;
        }

        public Criteria andDescripcionEqualTo(String value) {
            addCriterion("DESCRIPCION =", value, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionNotEqualTo(String value) {
            addCriterion("DESCRIPCION <>", value, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionGreaterThan(String value) {
            addCriterion("DESCRIPCION >", value, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionGreaterThanOrEqualTo(String value) {
            addCriterion("DESCRIPCION >=", value, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionLessThan(String value) {
            addCriterion("DESCRIPCION <", value, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionLessThanOrEqualTo(String value) {
            addCriterion("DESCRIPCION <=", value, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionLike(String value) {
            addCriterion("DESCRIPCION like", value, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionNotLike(String value) {
            addCriterion("DESCRIPCION not like", value, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionIn(List<String> values) {
            addCriterion("DESCRIPCION in", values, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionNotIn(List<String> values) {
            addCriterion("DESCRIPCION not in", values, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionBetween(String value1, String value2) {
            addCriterion("DESCRIPCION between", value1, value2, "descripcion");
            return (Criteria) this;
        }

        public Criteria andDescripcionNotBetween(String value1, String value2) {
            addCriterion("DESCRIPCION not between", value1, value2, "descripcion");
            return (Criteria) this;
        }

        public Criteria andFechaAltaIsNull() {
            addCriterion("FECHA_ALTA is null");
            return (Criteria) this;
        }

        public Criteria andFechaAltaIsNotNull() {
            addCriterion("FECHA_ALTA is not null");
            return (Criteria) this;
        }

        public Criteria andFechaAltaEqualTo(Fecha value) {
            addFechaAltaCriterion("FECHA_ALTA =", value, "fechaAlta");
            return (Criteria) this;
        }

        public Criteria andFechaAltaNotEqualTo(Fecha value) {
            addFechaAltaCriterion("FECHA_ALTA <>", value, "fechaAlta");
            return (Criteria) this;
        }

        public Criteria andFechaAltaGreaterThan(Fecha value) {
            addFechaAltaCriterion("FECHA_ALTA >", value, "fechaAlta");
            return (Criteria) this;
        }

        public Criteria andFechaAltaGreaterThanOrEqualTo(Fecha value) {
            addFechaAltaCriterion("FECHA_ALTA >=", value, "fechaAlta");
            return (Criteria) this;
        }

        public Criteria andFechaAltaLessThan(Fecha value) {
            addFechaAltaCriterion("FECHA_ALTA <", value, "fechaAlta");
            return (Criteria) this;
        }

        public Criteria andFechaAltaLessThanOrEqualTo(Fecha value) {
            addFechaAltaCriterion("FECHA_ALTA <=", value, "fechaAlta");
            return (Criteria) this;
        }

        public Criteria andFechaAltaIn(List<Fecha> values) {
            addFechaAltaCriterion("FECHA_ALTA in", values, "fechaAlta");
            return (Criteria) this;
        }

        public Criteria andFechaAltaNotIn(List<Fecha> values) {
            addFechaAltaCriterion("FECHA_ALTA not in", values, "fechaAlta");
            return (Criteria) this;
        }

        public Criteria andFechaAltaBetween(Fecha value1, Fecha value2) {
            addFechaAltaCriterion("FECHA_ALTA between", value1, value2, "fechaAlta");
            return (Criteria) this;
        }

        public Criteria andFechaAltaNotBetween(Fecha value1, Fecha value2) {
            addFechaAltaCriterion("FECHA_ALTA not between", value1, value2, "fechaAlta");
            return (Criteria) this;
        }

        public Criteria andFechaCambioVigenciaIsNull() {
            addCriterion("FECHA_CAMBIO_VIGENCIA is null");
            return (Criteria) this;
        }

        public Criteria andFechaCambioVigenciaIsNotNull() {
            addCriterion("FECHA_CAMBIO_VIGENCIA is not null");
            return (Criteria) this;
        }

        public Criteria andFechaCambioVigenciaEqualTo(Fecha value) {
            addFechaCambioVigenciaCriterion("FECHA_CAMBIO_VIGENCIA =", value, "fechaCambioVigencia");
            return (Criteria) this;
        }

        public Criteria andFechaCambioVigenciaNotEqualTo(Fecha value) {
            addFechaCambioVigenciaCriterion("FECHA_CAMBIO_VIGENCIA <>", value, "fechaCambioVigencia");
            return (Criteria) this;
        }

        public Criteria andFechaCambioVigenciaGreaterThan(Fecha value) {
            addFechaCambioVigenciaCriterion("FECHA_CAMBIO_VIGENCIA >", value, "fechaCambioVigencia");
            return (Criteria) this;
        }

        public Criteria andFechaCambioVigenciaGreaterThanOrEqualTo(Fecha value) {
            addFechaCambioVigenciaCriterion("FECHA_CAMBIO_VIGENCIA >=", value, "fechaCambioVigencia");
            return (Criteria) this;
        }

        public Criteria andFechaCambioVigenciaLessThan(Fecha value) {
            addFechaCambioVigenciaCriterion("FECHA_CAMBIO_VIGENCIA <", value, "fechaCambioVigencia");
            return (Criteria) this;
        }

        public Criteria andFechaCambioVigenciaLessThanOrEqualTo(Fecha value) {
            addFechaCambioVigenciaCriterion("FECHA_CAMBIO_VIGENCIA <=", value, "fechaCambioVigencia");
            return (Criteria) this;
        }

        public Criteria andFechaCambioVigenciaIn(List<Fecha> values) {
            addFechaCambioVigenciaCriterion("FECHA_CAMBIO_VIGENCIA in", values, "fechaCambioVigencia");
            return (Criteria) this;
        }

        public Criteria andFechaCambioVigenciaNotIn(List<Fecha> values) {
            addFechaCambioVigenciaCriterion("FECHA_CAMBIO_VIGENCIA not in", values, "fechaCambioVigencia");
            return (Criteria) this;
        }

        public Criteria andFechaCambioVigenciaBetween(Fecha value1, Fecha value2) {
            addFechaCambioVigenciaCriterion("FECHA_CAMBIO_VIGENCIA between", value1, value2, "fechaCambioVigencia");
            return (Criteria) this;
        }

        public Criteria andFechaCambioVigenciaNotBetween(Fecha value1, Fecha value2) {
            addFechaCambioVigenciaCriterion("FECHA_CAMBIO_VIGENCIA not between", value1, value2, "fechaCambioVigencia");
            return (Criteria) this;
        }

        public Criteria andTipoLikeInsensitive(String value) {
            addCriterion("upper(TIPO) like", value.toUpperCase(), "tipo");
            return (Criteria) this;
        }

        public Criteria andDescripcionLikeInsensitive(String value) {
            addCriterion("upper(DESCRIPCION) like", value.toUpperCase(), "descripcion");
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