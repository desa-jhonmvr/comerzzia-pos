package com.comerzzia.jpos.persistencia.letras.detalles;

import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LetraCuotaExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public LetraCuotaExample() {
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
        protected List<Criterion> fechaVencimientoCriteria;

        protected List<Criterion> fechaCobroCriteria;

        protected List<Criterion> procesadoCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
            fechaVencimientoCriteria = new ArrayList<Criterion>();
            fechaCobroCriteria = new ArrayList<Criterion>();
            procesadoCriteria = new ArrayList<Criterion>();
        }

        public List<Criterion> getFechaVencimientoCriteria() {
            return fechaVencimientoCriteria;
        }

        protected void addFechaVencimientoCriterion(String condition, Object value, String property) {
            if (value != null) {
                fechaVencimientoCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.FechaTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addFechaVencimientoCriterion(String condition, Fecha value1, Fecha value2, String property) {
            if (value1 != null && value2 != null) {
                fechaVencimientoCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.FechaTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getFechaCobroCriteria() {
            return fechaCobroCriteria;
        }

        protected void addFechaCobroCriterion(String condition, Object value, String property) {
            if (value != null) {
                fechaCobroCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addFechaCobroCriterion(String condition, Fecha value1, Fecha value2, String property) {
            if (value1 != null && value2 != null) {
                fechaCobroCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
                allCriteria = null;
            }
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
                || fechaVencimientoCriteria.size() > 0
                || fechaCobroCriteria.size() > 0
                || procesadoCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<Criterion>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(fechaVencimientoCriteria);
                allCriteria.addAll(fechaCobroCriteria);
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

        public Criteria andUidLetraIsNull() {
            addCriterion("UID_LETRA is null");
            return (Criteria) this;
        }

        public Criteria andUidLetraIsNotNull() {
            addCriterion("UID_LETRA is not null");
            return (Criteria) this;
        }

        public Criteria andUidLetraEqualTo(String value) {
            addCriterion("UID_LETRA =", value, "uidLetra");
            return (Criteria) this;
        }

        public Criteria andUidLetraNotEqualTo(String value) {
            addCriterion("UID_LETRA <>", value, "uidLetra");
            return (Criteria) this;
        }

        public Criteria andUidLetraGreaterThan(String value) {
            addCriterion("UID_LETRA >", value, "uidLetra");
            return (Criteria) this;
        }

        public Criteria andUidLetraGreaterThanOrEqualTo(String value) {
            addCriterion("UID_LETRA >=", value, "uidLetra");
            return (Criteria) this;
        }

        public Criteria andUidLetraLessThan(String value) {
            addCriterion("UID_LETRA <", value, "uidLetra");
            return (Criteria) this;
        }

        public Criteria andUidLetraLessThanOrEqualTo(String value) {
            addCriterion("UID_LETRA <=", value, "uidLetra");
            return (Criteria) this;
        }

        public Criteria andUidLetraLike(String value) {
            addCriterion("UID_LETRA like", value, "uidLetra");
            return (Criteria) this;
        }

        public Criteria andUidLetraNotLike(String value) {
            addCriterion("UID_LETRA not like", value, "uidLetra");
            return (Criteria) this;
        }

        public Criteria andUidLetraIn(List<String> values) {
            addCriterion("UID_LETRA in", values, "uidLetra");
            return (Criteria) this;
        }

        public Criteria andUidLetraNotIn(List<String> values) {
            addCriterion("UID_LETRA not in", values, "uidLetra");
            return (Criteria) this;
        }

        public Criteria andUidLetraBetween(String value1, String value2) {
            addCriterion("UID_LETRA between", value1, value2, "uidLetra");
            return (Criteria) this;
        }

        public Criteria andUidLetraNotBetween(String value1, String value2) {
            addCriterion("UID_LETRA not between", value1, value2, "uidLetra");
            return (Criteria) this;
        }

        public Criteria andCuotaIsNull() {
            addCriterion("CUOTA is null");
            return (Criteria) this;
        }

        public Criteria andCuotaIsNotNull() {
            addCriterion("CUOTA is not null");
            return (Criteria) this;
        }

        public Criteria andCuotaEqualTo(Short value) {
            addCriterion("CUOTA =", value, "cuota");
            return (Criteria) this;
        }

        public Criteria andCuotaNotEqualTo(Short value) {
            addCriterion("CUOTA <>", value, "cuota");
            return (Criteria) this;
        }

        public Criteria andCuotaGreaterThan(Short value) {
            addCriterion("CUOTA >", value, "cuota");
            return (Criteria) this;
        }

        public Criteria andCuotaGreaterThanOrEqualTo(Short value) {
            addCriterion("CUOTA >=", value, "cuota");
            return (Criteria) this;
        }

        public Criteria andCuotaLessThan(Short value) {
            addCriterion("CUOTA <", value, "cuota");
            return (Criteria) this;
        }

        public Criteria andCuotaLessThanOrEqualTo(Short value) {
            addCriterion("CUOTA <=", value, "cuota");
            return (Criteria) this;
        }

        public Criteria andCuotaIn(List<Short> values) {
            addCriterion("CUOTA in", values, "cuota");
            return (Criteria) this;
        }

        public Criteria andCuotaNotIn(List<Short> values) {
            addCriterion("CUOTA not in", values, "cuota");
            return (Criteria) this;
        }

        public Criteria andCuotaBetween(Short value1, Short value2) {
            addCriterion("CUOTA between", value1, value2, "cuota");
            return (Criteria) this;
        }

        public Criteria andCuotaNotBetween(Short value1, Short value2) {
            addCriterion("CUOTA not between", value1, value2, "cuota");
            return (Criteria) this;
        }

        public Criteria andValorIsNull() {
            addCriterion("VALOR is null");
            return (Criteria) this;
        }

        public Criteria andValorIsNotNull() {
            addCriterion("VALOR is not null");
            return (Criteria) this;
        }

        public Criteria andValorEqualTo(BigDecimal value) {
            addCriterion("VALOR =", value, "valor");
            return (Criteria) this;
        }

        public Criteria andValorNotEqualTo(BigDecimal value) {
            addCriterion("VALOR <>", value, "valor");
            return (Criteria) this;
        }

        public Criteria andValorGreaterThan(BigDecimal value) {
            addCriterion("VALOR >", value, "valor");
            return (Criteria) this;
        }

        public Criteria andValorGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("VALOR >=", value, "valor");
            return (Criteria) this;
        }

        public Criteria andValorLessThan(BigDecimal value) {
            addCriterion("VALOR <", value, "valor");
            return (Criteria) this;
        }

        public Criteria andValorLessThanOrEqualTo(BigDecimal value) {
            addCriterion("VALOR <=", value, "valor");
            return (Criteria) this;
        }

        public Criteria andValorIn(List<BigDecimal> values) {
            addCriterion("VALOR in", values, "valor");
            return (Criteria) this;
        }

        public Criteria andValorNotIn(List<BigDecimal> values) {
            addCriterion("VALOR not in", values, "valor");
            return (Criteria) this;
        }

        public Criteria andValorBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("VALOR between", value1, value2, "valor");
            return (Criteria) this;
        }

        public Criteria andValorNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("VALOR not between", value1, value2, "valor");
            return (Criteria) this;
        }

        public Criteria andFechaVencimientoIsNull() {
            addCriterion("FECHA_VENCIMIENTO is null");
            return (Criteria) this;
        }

        public Criteria andFechaVencimientoIsNotNull() {
            addCriterion("FECHA_VENCIMIENTO is not null");
            return (Criteria) this;
        }

        public Criteria andFechaVencimientoEqualTo(Fecha value) {
            addFechaVencimientoCriterion("FECHA_VENCIMIENTO =", value, "fechaVencimiento");
            return (Criteria) this;
        }

        public Criteria andFechaVencimientoNotEqualTo(Fecha value) {
            addFechaVencimientoCriterion("FECHA_VENCIMIENTO <>", value, "fechaVencimiento");
            return (Criteria) this;
        }

        public Criteria andFechaVencimientoGreaterThan(Fecha value) {
            addFechaVencimientoCriterion("FECHA_VENCIMIENTO >", value, "fechaVencimiento");
            return (Criteria) this;
        }

        public Criteria andFechaVencimientoGreaterThanOrEqualTo(Fecha value) {
            addFechaVencimientoCriterion("FECHA_VENCIMIENTO >=", value, "fechaVencimiento");
            return (Criteria) this;
        }

        public Criteria andFechaVencimientoLessThan(Fecha value) {
            addFechaVencimientoCriterion("FECHA_VENCIMIENTO <", value, "fechaVencimiento");
            return (Criteria) this;
        }

        public Criteria andFechaVencimientoLessThanOrEqualTo(Fecha value) {
            addFechaVencimientoCriterion("FECHA_VENCIMIENTO <=", value, "fechaVencimiento");
            return (Criteria) this;
        }

        public Criteria andFechaVencimientoIn(List<Fecha> values) {
            addFechaVencimientoCriterion("FECHA_VENCIMIENTO in", values, "fechaVencimiento");
            return (Criteria) this;
        }

        public Criteria andFechaVencimientoNotIn(List<Fecha> values) {
            addFechaVencimientoCriterion("FECHA_VENCIMIENTO not in", values, "fechaVencimiento");
            return (Criteria) this;
        }

        public Criteria andFechaVencimientoBetween(Fecha value1, Fecha value2) {
            addFechaVencimientoCriterion("FECHA_VENCIMIENTO between", value1, value2, "fechaVencimiento");
            return (Criteria) this;
        }

        public Criteria andFechaVencimientoNotBetween(Fecha value1, Fecha value2) {
            addFechaVencimientoCriterion("FECHA_VENCIMIENTO not between", value1, value2, "fechaVencimiento");
            return (Criteria) this;
        }

        public Criteria andFechaCobroIsNull() {
            addCriterion("FECHA_COBRO is null");
            return (Criteria) this;
        }

        public Criteria andFechaCobroIsNotNull() {
            addCriterion("FECHA_COBRO is not null");
            return (Criteria) this;
        }

        public Criteria andFechaCobroEqualTo(Fecha value) {
            addFechaCobroCriterion("FECHA_COBRO =", value, "fechaCobro");
            return (Criteria) this;
        }

        public Criteria andFechaCobroNotEqualTo(Fecha value) {
            addFechaCobroCriterion("FECHA_COBRO <>", value, "fechaCobro");
            return (Criteria) this;
        }

        public Criteria andFechaCobroGreaterThan(Fecha value) {
            addFechaCobroCriterion("FECHA_COBRO >", value, "fechaCobro");
            return (Criteria) this;
        }

        public Criteria andFechaCobroGreaterThanOrEqualTo(Fecha value) {
            addFechaCobroCriterion("FECHA_COBRO >=", value, "fechaCobro");
            return (Criteria) this;
        }

        public Criteria andFechaCobroLessThan(Fecha value) {
            addFechaCobroCriterion("FECHA_COBRO <", value, "fechaCobro");
            return (Criteria) this;
        }

        public Criteria andFechaCobroLessThanOrEqualTo(Fecha value) {
            addFechaCobroCriterion("FECHA_COBRO <=", value, "fechaCobro");
            return (Criteria) this;
        }

        public Criteria andFechaCobroIn(List<Fecha> values) {
            addFechaCobroCriterion("FECHA_COBRO in", values, "fechaCobro");
            return (Criteria) this;
        }

        public Criteria andFechaCobroNotIn(List<Fecha> values) {
            addFechaCobroCriterion("FECHA_COBRO not in", values, "fechaCobro");
            return (Criteria) this;
        }

        public Criteria andFechaCobroBetween(Fecha value1, Fecha value2) {
            addFechaCobroCriterion("FECHA_COBRO between", value1, value2, "fechaCobro");
            return (Criteria) this;
        }

        public Criteria andFechaCobroNotBetween(Fecha value1, Fecha value2) {
            addFechaCobroCriterion("FECHA_COBRO not between", value1, value2, "fechaCobro");
            return (Criteria) this;
        }

        public Criteria andEstadoIsNull() {
            addCriterion("ESTADO is null");
            return (Criteria) this;
        }

        public Criteria andEstadoIsNotNull() {
            addCriterion("ESTADO is not null");
            return (Criteria) this;
        }

        public Criteria andEstadoEqualTo(String value) {
            addCriterion("ESTADO =", value, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoNotEqualTo(String value) {
            addCriterion("ESTADO <>", value, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoGreaterThan(String value) {
            addCriterion("ESTADO >", value, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoGreaterThanOrEqualTo(String value) {
            addCriterion("ESTADO >=", value, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoLessThan(String value) {
            addCriterion("ESTADO <", value, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoLessThanOrEqualTo(String value) {
            addCriterion("ESTADO <=", value, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoLike(String value) {
            addCriterion("ESTADO like", value, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoNotLike(String value) {
            addCriterion("ESTADO not like", value, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoIn(List<String> values) {
            addCriterion("ESTADO in", values, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoNotIn(List<String> values) {
            addCriterion("ESTADO not in", values, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoBetween(String value1, String value2) {
            addCriterion("ESTADO between", value1, value2, "estado");
            return (Criteria) this;
        }

        public Criteria andEstadoNotBetween(String value1, String value2) {
            addCriterion("ESTADO not between", value1, value2, "estado");
            return (Criteria) this;
        }

        public Criteria andMoraIsNull() {
            addCriterion("MORA is null");
            return (Criteria) this;
        }

        public Criteria andMoraIsNotNull() {
            addCriterion("MORA is not null");
            return (Criteria) this;
        }

        public Criteria andMoraEqualTo(BigDecimal value) {
            addCriterion("MORA =", value, "mora");
            return (Criteria) this;
        }

        public Criteria andMoraNotEqualTo(BigDecimal value) {
            addCriterion("MORA <>", value, "mora");
            return (Criteria) this;
        }

        public Criteria andMoraGreaterThan(BigDecimal value) {
            addCriterion("MORA >", value, "mora");
            return (Criteria) this;
        }

        public Criteria andMoraGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("MORA >=", value, "mora");
            return (Criteria) this;
        }

        public Criteria andMoraLessThan(BigDecimal value) {
            addCriterion("MORA <", value, "mora");
            return (Criteria) this;
        }

        public Criteria andMoraLessThanOrEqualTo(BigDecimal value) {
            addCriterion("MORA <=", value, "mora");
            return (Criteria) this;
        }

        public Criteria andMoraIn(List<BigDecimal> values) {
            addCriterion("MORA in", values, "mora");
            return (Criteria) this;
        }

        public Criteria andMoraNotIn(List<BigDecimal> values) {
            addCriterion("MORA not in", values, "mora");
            return (Criteria) this;
        }

        public Criteria andMoraBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("MORA between", value1, value2, "mora");
            return (Criteria) this;
        }

        public Criteria andMoraNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("MORA not between", value1, value2, "mora");
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

        public Criteria andUidLetraLikeInsensitive(String value) {
            addCriterion("upper(UID_LETRA) like", value.toUpperCase(), "uidLetra");
            return (Criteria) this;
        }

        public Criteria andEstadoLikeInsensitive(String value) {
            addCriterion("upper(ESTADO) like", value.toUpperCase(), "estado");
            return (Criteria) this;
        }
        
        public Criteria andCodCajaAbonoEqualTo(String value) {
            addCriterion("CODCAJA_ABONO =", value, "codcajaAbono");
            return (Criteria) this;
        }
        
        public Criteria andIdAbonoEqualTo(Long value) {
            addCriterion("ID_ABONO =", value, "idAbono");
            return (Criteria) this;
        }        
        
        public Criteria andUsuarioAbonoEqualTo(String value) {
            addCriterion("USUARIO_ABONO =", value, "usuarioAbono");
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