package com.comerzzia.jpos.persistencia.promociones.configuracion.billeton.detalles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ConfiguracionBilletonDetalleExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ConfiguracionBilletonDetalleExample() {
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
        protected List<Criterion> tipoCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
            tipoCriteria = new ArrayList<Criterion>();
        }

        public List<Criterion> getTipoCriteria() {
            return tipoCriteria;
        }

        protected void addTipoCriterion(String condition, Object value, String property) {
            if (value != null) {
                tipoCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addTipoCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                tipoCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public boolean isValid() {
            return criteria.size() > 0
                || tipoCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<Criterion>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(tipoCriteria);
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

        public Criteria andIdConfBilletonDetIsNull() {
            addCriterion("ID_CONF_BILLETON_DET is null");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonDetIsNotNull() {
            addCriterion("ID_CONF_BILLETON_DET is not null");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonDetEqualTo(Long value) {
            addCriterion("ID_CONF_BILLETON_DET =", value, "idConfBilletonDet");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonDetNotEqualTo(Long value) {
            addCriterion("ID_CONF_BILLETON_DET <>", value, "idConfBilletonDet");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonDetGreaterThan(Long value) {
            addCriterion("ID_CONF_BILLETON_DET >", value, "idConfBilletonDet");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonDetGreaterThanOrEqualTo(Long value) {
            addCriterion("ID_CONF_BILLETON_DET >=", value, "idConfBilletonDet");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonDetLessThan(Long value) {
            addCriterion("ID_CONF_BILLETON_DET <", value, "idConfBilletonDet");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonDetLessThanOrEqualTo(Long value) {
            addCriterion("ID_CONF_BILLETON_DET <=", value, "idConfBilletonDet");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonDetIn(List<Long> values) {
            addCriterion("ID_CONF_BILLETON_DET in", values, "idConfBilletonDet");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonDetNotIn(List<Long> values) {
            addCriterion("ID_CONF_BILLETON_DET not in", values, "idConfBilletonDet");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonDetBetween(Long value1, Long value2) {
            addCriterion("ID_CONF_BILLETON_DET between", value1, value2, "idConfBilletonDet");
            return (Criteria) this;
        }

        public Criteria andIdConfBilletonDetNotBetween(Long value1, Long value2) {
            addCriterion("ID_CONF_BILLETON_DET not between", value1, value2, "idConfBilletonDet");
            return (Criteria) this;
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

        public Criteria andTipoEqualTo(Boolean value) {
            addTipoCriterion("TIPO =", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoNotEqualTo(Boolean value) {
            addTipoCriterion("TIPO <>", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoGreaterThan(Boolean value) {
            addTipoCriterion("TIPO >", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoGreaterThanOrEqualTo(Boolean value) {
            addTipoCriterion("TIPO >=", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoLessThan(Boolean value) {
            addTipoCriterion("TIPO <", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoLessThanOrEqualTo(Boolean value) {
            addTipoCriterion("TIPO <=", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoLike(Boolean value) {
            addTipoCriterion("TIPO like", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoNotLike(Boolean value) {
            addTipoCriterion("TIPO not like", value, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoIn(List<Boolean> values) {
            addTipoCriterion("TIPO in", values, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoNotIn(List<Boolean> values) {
            addTipoCriterion("TIPO not in", values, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoBetween(Boolean value1, Boolean value2) {
            addTipoCriterion("TIPO between", value1, value2, "tipo");
            return (Criteria) this;
        }

        public Criteria andTipoNotBetween(Boolean value1, Boolean value2) {
            addTipoCriterion("TIPO not between", value1, value2, "tipo");
            return (Criteria) this;
        }

        public Criteria andDesdeIsNull() {
            addCriterion("DESDE is null");
            return (Criteria) this;
        }

        public Criteria andDesdeIsNotNull() {
            addCriterion("DESDE is not null");
            return (Criteria) this;
        }

        public Criteria andDesdeEqualTo(BigDecimal value) {
            addCriterion("DESDE =", value, "desde");
            return (Criteria) this;
        }

        public Criteria andDesdeNotEqualTo(BigDecimal value) {
            addCriterion("DESDE <>", value, "desde");
            return (Criteria) this;
        }

        public Criteria andDesdeGreaterThan(BigDecimal value) {
            addCriterion("DESDE >", value, "desde");
            return (Criteria) this;
        }

        public Criteria andDesdeGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("DESDE >=", value, "desde");
            return (Criteria) this;
        }

        public Criteria andDesdeLessThan(BigDecimal value) {
            addCriterion("DESDE <", value, "desde");
            return (Criteria) this;
        }

        public Criteria andDesdeLessThanOrEqualTo(BigDecimal value) {
            addCriterion("DESDE <=", value, "desde");
            return (Criteria) this;
        }

        public Criteria andDesdeIn(List<BigDecimal> values) {
            addCriterion("DESDE in", values, "desde");
            return (Criteria) this;
        }

        public Criteria andDesdeNotIn(List<BigDecimal> values) {
            addCriterion("DESDE not in", values, "desde");
            return (Criteria) this;
        }

        public Criteria andDesdeBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("DESDE between", value1, value2, "desde");
            return (Criteria) this;
        }

        public Criteria andDesdeNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("DESDE not between", value1, value2, "desde");
            return (Criteria) this;
        }

        public Criteria andHastaIsNull() {
            addCriterion("HASTA is null");
            return (Criteria) this;
        }

        public Criteria andHastaIsNotNull() {
            addCriterion("HASTA is not null");
            return (Criteria) this;
        }

        public Criteria andHastaEqualTo(BigDecimal value) {
            addCriterion("HASTA =", value, "hasta");
            return (Criteria) this;
        }

        public Criteria andHastaNotEqualTo(BigDecimal value) {
            addCriterion("HASTA <>", value, "hasta");
            return (Criteria) this;
        }

        public Criteria andHastaGreaterThan(BigDecimal value) {
            addCriterion("HASTA >", value, "hasta");
            return (Criteria) this;
        }

        public Criteria andHastaGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("HASTA >=", value, "hasta");
            return (Criteria) this;
        }

        public Criteria andHastaLessThan(BigDecimal value) {
            addCriterion("HASTA <", value, "hasta");
            return (Criteria) this;
        }

        public Criteria andHastaLessThanOrEqualTo(BigDecimal value) {
            addCriterion("HASTA <=", value, "hasta");
            return (Criteria) this;
        }

        public Criteria andHastaIn(List<BigDecimal> values) {
            addCriterion("HASTA in", values, "hasta");
            return (Criteria) this;
        }

        public Criteria andHastaNotIn(List<BigDecimal> values) {
            addCriterion("HASTA not in", values, "hasta");
            return (Criteria) this;
        }

        public Criteria andHastaBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("HASTA between", value1, value2, "hasta");
            return (Criteria) this;
        }

        public Criteria andHastaNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("HASTA not between", value1, value2, "hasta");
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

        public Criteria andCodFormatoIsNull() {
            addCriterion("COD_FORMATO is null");
            return (Criteria) this;
        }

        public Criteria andCodFormatoIsNotNull() {
            addCriterion("COD_FORMATO is not null");
            return (Criteria) this;
        }

        public Criteria andCodFormatoEqualTo(Long value) {
            addCriterion("COD_FORMATO =", value, "codFormato");
            return (Criteria) this;
        }

        public Criteria andCodFormatoNotEqualTo(Long value) {
            addCriterion("COD_FORMATO <>", value, "codFormato");
            return (Criteria) this;
        }

        public Criteria andCodFormatoGreaterThan(Long value) {
            addCriterion("COD_FORMATO >", value, "codFormato");
            return (Criteria) this;
        }

        public Criteria andCodFormatoGreaterThanOrEqualTo(Long value) {
            addCriterion("COD_FORMATO >=", value, "codFormato");
            return (Criteria) this;
        }

        public Criteria andCodFormatoLessThan(Long value) {
            addCriterion("COD_FORMATO <", value, "codFormato");
            return (Criteria) this;
        }

        public Criteria andCodFormatoLessThanOrEqualTo(Long value) {
            addCriterion("COD_FORMATO <=", value, "codFormato");
            return (Criteria) this;
        }

        public Criteria andCodFormatoIn(List<Long> values) {
            addCriterion("COD_FORMATO in", values, "codFormato");
            return (Criteria) this;
        }

        public Criteria andCodFormatoNotIn(List<Long> values) {
            addCriterion("COD_FORMATO not in", values, "codFormato");
            return (Criteria) this;
        }

        public Criteria andCodFormatoBetween(Long value1, Long value2) {
            addCriterion("COD_FORMATO between", value1, value2, "codFormato");
            return (Criteria) this;
        }

        public Criteria andCodFormatoNotBetween(Long value1, Long value2) {
            addCriterion("COD_FORMATO not between", value1, value2, "codFormato");
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