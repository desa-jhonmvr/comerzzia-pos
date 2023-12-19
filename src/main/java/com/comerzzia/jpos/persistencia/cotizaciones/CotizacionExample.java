package com.comerzzia.jpos.persistencia.cotizaciones;

import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CotizacionExample {
    public static final String ORDER_BY_UID_COTIZACION = "UID_COTIZACION";

    public static final String ORDER_BY_UID_COTIZACION_DESC = "UID_COTIZACION DESC";

    public static final String ORDER_BY_CODALM = "CODALM";

    public static final String ORDER_BY_CODALM_DESC = "CODALM DESC";

    public static final String ORDER_BY_CODCAJA = "CODCAJA";

    public static final String ORDER_BY_CODCAJA_DESC = "CODCAJA DESC";

    public static final String ORDER_BY_ID_COTIZACION = "ID_COTIZACION";

    public static final String ORDER_BY_ID_COTIZACION_DESC = "ID_COTIZACION DESC";

    public static final String ORDER_BY_FECHA = "FECHA";

    public static final String ORDER_BY_FECHA_DESC = "FECHA DESC";

    public static final String ORDER_BY_USUARIO = "USUARIO";

    public static final String ORDER_BY_USUARIO_DESC = "USUARIO DESC";

    public static final String ORDER_BY_CODCLI = "CODCLI";

    public static final String ORDER_BY_CODCLI_DESC = "CODCLI DESC";

    public static final String ORDER_BY_FECHA_VIGENCIA = "FECHA_VIGENCIA";

    public static final String ORDER_BY_FECHA_VIGENCIA_DESC = "FECHA_VIGENCIA DESC";

    public static final String ORDER_BY_TOTAL = "TOTAL";

    public static final String ORDER_BY_TOTAL_DESC = "TOTAL DESC";

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CotizacionExample() {
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

        protected List<Criterion> fechaVigenciaCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
            fechaCriteria = new ArrayList<Criterion>();
            fechaVigenciaCriteria = new ArrayList<Criterion>();
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

        public List<Criterion> getFechaVigenciaCriteria() {
            return fechaVigenciaCriteria;
        }

        protected void addFechaVigenciaCriterion(String condition, Object value, String property) {
            if (value != null) {
                fechaVigenciaCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addFechaVigenciaCriterion(String condition, Fecha value1, Fecha value2, String property) {
            if (value1 != null && value2 != null) {
                fechaVigenciaCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
                allCriteria = null;
            }
        }

        public boolean isValid() {
            return criteria.size() > 0
                || fechaCriteria.size() > 0
                || fechaVigenciaCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<Criterion>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(fechaCriteria);
                allCriteria.addAll(fechaVigenciaCriteria);
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

        public Criteria andUidCotizacionIsNull() {
            addCriterion("UID_COTIZACION is null");
            return (Criteria) this;
        }

        public Criteria andUidCotizacionIsNotNull() {
            addCriterion("UID_COTIZACION is not null");
            return (Criteria) this;
        }

        public Criteria andUidCotizacionEqualTo(String value) {
            addCriterion("UID_COTIZACION =", value, "uidCotizacion");
            return (Criteria) this;
        }

        public Criteria andUidCotizacionNotEqualTo(String value) {
            addCriterion("UID_COTIZACION <>", value, "uidCotizacion");
            return (Criteria) this;
        }

        public Criteria andUidCotizacionGreaterThan(String value) {
            addCriterion("UID_COTIZACION >", value, "uidCotizacion");
            return (Criteria) this;
        }

        public Criteria andUidCotizacionGreaterThanOrEqualTo(String value) {
            addCriterion("UID_COTIZACION >=", value, "uidCotizacion");
            return (Criteria) this;
        }

        public Criteria andUidCotizacionLessThan(String value) {
            addCriterion("UID_COTIZACION <", value, "uidCotizacion");
            return (Criteria) this;
        }

        public Criteria andUidCotizacionLessThanOrEqualTo(String value) {
            addCriterion("UID_COTIZACION <=", value, "uidCotizacion");
            return (Criteria) this;
        }

        public Criteria andUidCotizacionLike(String value) {
            addCriterion("UID_COTIZACION like", value, "uidCotizacion");
            return (Criteria) this;
        }

        public Criteria andUidCotizacionNotLike(String value) {
            addCriterion("UID_COTIZACION not like", value, "uidCotizacion");
            return (Criteria) this;
        }

        public Criteria andUidCotizacionIn(List<String> values) {
            addCriterion("UID_COTIZACION in", values, "uidCotizacion");
            return (Criteria) this;
        }

        public Criteria andUidCotizacionNotIn(List<String> values) {
            addCriterion("UID_COTIZACION not in", values, "uidCotizacion");
            return (Criteria) this;
        }

        public Criteria andUidCotizacionBetween(String value1, String value2) {
            addCriterion("UID_COTIZACION between", value1, value2, "uidCotizacion");
            return (Criteria) this;
        }

        public Criteria andUidCotizacionNotBetween(String value1, String value2) {
            addCriterion("UID_COTIZACION not between", value1, value2, "uidCotizacion");
            return (Criteria) this;
        }

        public Criteria andCodalmIsNull() {
            addCriterion("CODALM is null");
            return (Criteria) this;
        }

        public Criteria andCodalmIsNotNull() {
            addCriterion("CODALM is not null");
            return (Criteria) this;
        }

        public Criteria andCodalmEqualTo(String value) {
            addCriterion("CODALM =", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmNotEqualTo(String value) {
            addCriterion("CODALM <>", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmGreaterThan(String value) {
            addCriterion("CODALM >", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmGreaterThanOrEqualTo(String value) {
            addCriterion("CODALM >=", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmLessThan(String value) {
            addCriterion("CODALM <", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmLessThanOrEqualTo(String value) {
            addCriterion("CODALM <=", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmLike(String value) {
            addCriterion("CODALM like", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmNotLike(String value) {
            addCriterion("CODALM not like", value, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmIn(List<String> values) {
            addCriterion("CODALM in", values, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmNotIn(List<String> values) {
            addCriterion("CODALM not in", values, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmBetween(String value1, String value2) {
            addCriterion("CODALM between", value1, value2, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodalmNotBetween(String value1, String value2) {
            addCriterion("CODALM not between", value1, value2, "codalm");
            return (Criteria) this;
        }

        public Criteria andCodcajaIsNull() {
            addCriterion("CODCAJA is null");
            return (Criteria) this;
        }

        public Criteria andCodcajaIsNotNull() {
            addCriterion("CODCAJA is not null");
            return (Criteria) this;
        }

        public Criteria andCodcajaEqualTo(String value) {
            addCriterion("CODCAJA =", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaNotEqualTo(String value) {
            addCriterion("CODCAJA <>", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaGreaterThan(String value) {
            addCriterion("CODCAJA >", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaGreaterThanOrEqualTo(String value) {
            addCriterion("CODCAJA >=", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaLessThan(String value) {
            addCriterion("CODCAJA <", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaLessThanOrEqualTo(String value) {
            addCriterion("CODCAJA <=", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaLike(String value) {
            addCriterion("CODCAJA like", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaNotLike(String value) {
            addCriterion("CODCAJA not like", value, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaIn(List<String> values) {
            addCriterion("CODCAJA in", values, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaNotIn(List<String> values) {
            addCriterion("CODCAJA not in", values, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaBetween(String value1, String value2) {
            addCriterion("CODCAJA between", value1, value2, "codcaja");
            return (Criteria) this;
        }

        public Criteria andCodcajaNotBetween(String value1, String value2) {
            addCriterion("CODCAJA not between", value1, value2, "codcaja");
            return (Criteria) this;
        }

        public Criteria andIdCotizacionIsNull() {
            addCriterion("ID_COTIZACION is null");
            return (Criteria) this;
        }

        public Criteria andIdCotizacionIsNotNull() {
            addCriterion("ID_COTIZACION is not null");
            return (Criteria) this;
        }

        public Criteria andIdCotizacionEqualTo(Long value) {
            addCriterion("ID_COTIZACION =", value, "idCotizacion");
            return (Criteria) this;
        }

        public Criteria andIdCotizacionNotEqualTo(Long value) {
            addCriterion("ID_COTIZACION <>", value, "idCotizacion");
            return (Criteria) this;
        }

        public Criteria andIdCotizacionGreaterThan(Long value) {
            addCriterion("ID_COTIZACION >", value, "idCotizacion");
            return (Criteria) this;
        }

        public Criteria andIdCotizacionGreaterThanOrEqualTo(Long value) {
            addCriterion("ID_COTIZACION >=", value, "idCotizacion");
            return (Criteria) this;
        }

        public Criteria andIdCotizacionLessThan(Long value) {
            addCriterion("ID_COTIZACION <", value, "idCotizacion");
            return (Criteria) this;
        }

        public Criteria andIdCotizacionLessThanOrEqualTo(Long value) {
            addCriterion("ID_COTIZACION <=", value, "idCotizacion");
            return (Criteria) this;
        }

        public Criteria andIdCotizacionIn(List<Long> values) {
            addCriterion("ID_COTIZACION in", values, "idCotizacion");
            return (Criteria) this;
        }

        public Criteria andIdCotizacionNotIn(List<Long> values) {
            addCriterion("ID_COTIZACION not in", values, "idCotizacion");
            return (Criteria) this;
        }

        public Criteria andIdCotizacionBetween(Long value1, Long value2) {
            addCriterion("ID_COTIZACION between", value1, value2, "idCotizacion");
            return (Criteria) this;
        }

        public Criteria andIdCotizacionNotBetween(Long value1, Long value2) {
            addCriterion("ID_COTIZACION not between", value1, value2, "idCotizacion");
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

        public Criteria andFechaEqualTo(Fecha value) {
            addFechaCriterion("FECHA =", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaNotEqualTo(Fecha value) {
            addFechaCriterion("FECHA <>", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaGreaterThan(Fecha value) {
            addFechaCriterion("FECHA >", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaGreaterThanOrEqualTo(Fecha value) {
            addFechaCriterion("FECHA >=", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaLessThan(Fecha value) {
            addFechaCriterion("FECHA <", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaLessThanOrEqualTo(Fecha value) {
            addFechaCriterion("FECHA <=", value, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaIn(List<Fecha> values) {
            addFechaCriterion("FECHA in", values, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaNotIn(List<Fecha> values) {
            addFechaCriterion("FECHA not in", values, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaBetween(Fecha value1, Fecha value2) {
            addFechaCriterion("FECHA between", value1, value2, "fecha");
            return (Criteria) this;
        }

        public Criteria andFechaNotBetween(Fecha value1, Fecha value2) {
            addFechaCriterion("FECHA not between", value1, value2, "fecha");
            return (Criteria) this;
        }

        public Criteria andUsuarioIsNull() {
            addCriterion("USUARIO is null");
            return (Criteria) this;
        }

        public Criteria andUsuarioIsNotNull() {
            addCriterion("USUARIO is not null");
            return (Criteria) this;
        }

        public Criteria andUsuarioEqualTo(String value) {
            addCriterion("USUARIO =", value, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioNotEqualTo(String value) {
            addCriterion("USUARIO <>", value, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioGreaterThan(String value) {
            addCriterion("USUARIO >", value, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioGreaterThanOrEqualTo(String value) {
            addCriterion("USUARIO >=", value, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioLessThan(String value) {
            addCriterion("USUARIO <", value, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioLessThanOrEqualTo(String value) {
            addCriterion("USUARIO <=", value, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioLike(String value) {
            addCriterion("USUARIO like", value, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioNotLike(String value) {
            addCriterion("USUARIO not like", value, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioIn(List<String> values) {
            addCriterion("USUARIO in", values, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioNotIn(List<String> values) {
            addCriterion("USUARIO not in", values, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioBetween(String value1, String value2) {
            addCriterion("USUARIO between", value1, value2, "usuario");
            return (Criteria) this;
        }

        public Criteria andUsuarioNotBetween(String value1, String value2) {
            addCriterion("USUARIO not between", value1, value2, "usuario");
            return (Criteria) this;
        }

        public Criteria andCodcliIsNull() {
            addCriterion("CODCLI is null");
            return (Criteria) this;
        }

        public Criteria andCodcliIsNotNull() {
            addCriterion("CODCLI is not null");
            return (Criteria) this;
        }

        public Criteria andCodcliEqualTo(String value) {
            addCriterion("CODCLI =", value, "codcli");
            return (Criteria) this;
        }

        public Criteria andCodcliNotEqualTo(String value) {
            addCriterion("CODCLI <>", value, "codcli");
            return (Criteria) this;
        }

        public Criteria andCodcliGreaterThan(String value) {
            addCriterion("CODCLI >", value, "codcli");
            return (Criteria) this;
        }

        public Criteria andCodcliGreaterThanOrEqualTo(String value) {
            addCriterion("CODCLI >=", value, "codcli");
            return (Criteria) this;
        }

        public Criteria andCodcliLessThan(String value) {
            addCriterion("CODCLI <", value, "codcli");
            return (Criteria) this;
        }

        public Criteria andCodcliLessThanOrEqualTo(String value) {
            addCriterion("CODCLI <=", value, "codcli");
            return (Criteria) this;
        }

        public Criteria andCodcliLike(String value) {
            addCriterion("CODCLI like", value, "codcli");
            return (Criteria) this;
        }

        public Criteria andCodcliNotLike(String value) {
            addCriterion("CODCLI not like", value, "codcli");
            return (Criteria) this;
        }

        public Criteria andCodcliIn(List<String> values) {
            addCriterion("CODCLI in", values, "codcli");
            return (Criteria) this;
        }

        public Criteria andCodcliNotIn(List<String> values) {
            addCriterion("CODCLI not in", values, "codcli");
            return (Criteria) this;
        }

        public Criteria andCodcliBetween(String value1, String value2) {
            addCriterion("CODCLI between", value1, value2, "codcli");
            return (Criteria) this;
        }

        public Criteria andCodcliNotBetween(String value1, String value2) {
            addCriterion("CODCLI not between", value1, value2, "codcli");
            return (Criteria) this;
        }

        public Criteria andFechaVigenciaIsNull() {
            addCriterion("FECHA_VIGENCIA is null");
            return (Criteria) this;
        }

        public Criteria andFechaVigenciaIsNotNull() {
            addCriterion("FECHA_VIGENCIA is not null");
            return (Criteria) this;
        }

        public Criteria andFechaVigenciaEqualTo(Fecha value) {
            addFechaVigenciaCriterion("FECHA_VIGENCIA =", value, "fechaVigencia");
            return (Criteria) this;
        }

        public Criteria andFechaVigenciaNotEqualTo(Fecha value) {
            addFechaVigenciaCriterion("FECHA_VIGENCIA <>", value, "fechaVigencia");
            return (Criteria) this;
        }

        public Criteria andFechaVigenciaGreaterThan(Fecha value) {
            addFechaVigenciaCriterion("FECHA_VIGENCIA >", value, "fechaVigencia");
            return (Criteria) this;
        }

        public Criteria andFechaVigenciaGreaterThanOrEqualTo(Fecha value) {
            addFechaVigenciaCriterion("FECHA_VIGENCIA >=", value, "fechaVigencia");
            return (Criteria) this;
        }

        public Criteria andFechaVigenciaLessThan(Fecha value) {
            addFechaVigenciaCriterion("FECHA_VIGENCIA <", value, "fechaVigencia");
            return (Criteria) this;
        }

        public Criteria andFechaVigenciaLessThanOrEqualTo(Fecha value) {
            addFechaVigenciaCriterion("FECHA_VIGENCIA <=", value, "fechaVigencia");
            return (Criteria) this;
        }

        public Criteria andFechaVigenciaIn(List<Fecha> values) {
            addFechaVigenciaCriterion("FECHA_VIGENCIA in", values, "fechaVigencia");
            return (Criteria) this;
        }

        public Criteria andFechaVigenciaNotIn(List<Fecha> values) {
            addFechaVigenciaCriterion("FECHA_VIGENCIA not in", values, "fechaVigencia");
            return (Criteria) this;
        }

        public Criteria andFechaVigenciaBetween(Fecha value1, Fecha value2) {
            addFechaVigenciaCriterion("FECHA_VIGENCIA between", value1, value2, "fechaVigencia");
            return (Criteria) this;
        }

        public Criteria andFechaVigenciaNotBetween(Fecha value1, Fecha value2) {
            addFechaVigenciaCriterion("FECHA_VIGENCIA not between", value1, value2, "fechaVigencia");
            return (Criteria) this;
        }

        public Criteria andTotalIsNull() {
            addCriterion("TOTAL is null");
            return (Criteria) this;
        }

        public Criteria andTotalIsNotNull() {
            addCriterion("TOTAL is not null");
            return (Criteria) this;
        }

        public Criteria andTotalEqualTo(BigDecimal value) {
            addCriterion("TOTAL =", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotEqualTo(BigDecimal value) {
            addCriterion("TOTAL <>", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalGreaterThan(BigDecimal value) {
            addCriterion("TOTAL >", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("TOTAL >=", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalLessThan(BigDecimal value) {
            addCriterion("TOTAL <", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalLessThanOrEqualTo(BigDecimal value) {
            addCriterion("TOTAL <=", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalIn(List<BigDecimal> values) {
            addCriterion("TOTAL in", values, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotIn(List<BigDecimal> values) {
            addCriterion("TOTAL not in", values, "total");
            return (Criteria) this;
        }

        public Criteria andTotalBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("TOTAL between", value1, value2, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("TOTAL not between", value1, value2, "total");
            return (Criteria) this;
        }

        public Criteria andUidCotizacionLikeInsensitive(String value) {
            addCriterion("upper(UID_COTIZACION) like", value.toUpperCase(), "uidCotizacion");
            return (Criteria) this;
        }

        public Criteria andCodalmLikeInsensitive(String value) {
            addCriterion("upper(CODALM) like", value.toUpperCase(), "codalm");
            return (Criteria) this;
        }

        public Criteria andCodcajaLikeInsensitive(String value) {
            addCriterion("upper(CODCAJA) like", value.toUpperCase(), "codcaja");
            return (Criteria) this;
        }

        public Criteria andUsuarioLikeInsensitive(String value) {
            addCriterion("upper(USUARIO) like", value.toUpperCase(), "usuario");
            return (Criteria) this;
        }

        public Criteria andCodcliLikeInsensitive(String value) {
            addCriterion("upper(CODCLI) like", value.toUpperCase(), "codcli");
            return (Criteria) this;
        }
        
        public Criteria andFechaVigenciaIsCaducado(Fecha fecha, char estado){
            if(estado == 'V'){
                addFechaVigenciaCriterion("FECHA_VIGENCIA >=", fecha, "fechaVigencia");
            }
            else if(estado == 'C'){
                addFechaVigenciaCriterion("FECHA_VIGENCIA <=", fecha, "fechaVigencia");
            }
            return (Criteria) this;
        }
        
        public Criteria andCodCliEqualWithoutNull(String value) {
            if(value != null && !value.isEmpty()){
               addCriterion("X.CODCLI =", value, "codcli");
            }
            return (Criteria) this;
        }
        
        public Criteria andUsuarioEqualWithoutNull(String value) {
            if(value != null && !value.isEmpty()){
               addCriterion("USUARIO =", value, "codcli");
            }
            return (Criteria) this;            
        }
        
        public Criteria andFechaMayorWithoutNull(Fecha fecha) {
            if(fecha != null && fecha.toString() != null && !fecha.toString().isEmpty()){
               addFechaCriterion("FECHA >=", fecha, "fecha");
            }
            return (Criteria) this;  
        }
        
        public Criteria andFechaMenorWithoutNull(Fecha fecha) {
            if(fecha != null && fecha.toString() != null && !fecha.toString().isEmpty()){
               addFechaCriterion("FECHA <=", fecha, "fecha");
            }
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