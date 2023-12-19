package com.comerzzia.jpos.persistencia.letras;

import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LetraExample {
    public static final String ORDER_BY_UID_LETRA = "UID_LETRA";

    public static final String ORDER_BY_UID_LETRA_DESC = "UID_LETRA DESC";

    public static final String ORDER_BY_UID_TICKET = "UID_TICKET";

    public static final String ORDER_BY_UID_TICKET_DESC = "UID_TICKET DESC";

    public static final String ORDER_BY_CODCLI = "CODCLI";

    public static final String ORDER_BY_CODCLI_DESC = "CODCLI DESC";

    public static final String ORDER_BY_CODALM = "CODALM";

    public static final String ORDER_BY_CODALM_DESC = "CODALM DESC";

    public static final String ORDER_BY_CODCAJA = "CODCAJA";

    public static final String ORDER_BY_CODCAJA_DESC = "CODCAJA DESC";

    public static final String ORDER_BY_TOTAL = "TOTAL";

    public static final String ORDER_BY_TOTAL_DESC = "TOTAL DESC";

    public static final String ORDER_BY_PLAZO = "PLAZO";

    public static final String ORDER_BY_PLAZO_DESC = "PLAZO DESC";

    public static final String ORDER_BY_INTERESES = "INTERESES";

    public static final String ORDER_BY_INTERESES_DESC = "INTERESES DESC";

    public static final String ORDER_BY_FECHA = "FECHA";

    public static final String ORDER_BY_FECHA_DESC = "FECHA DESC";

    public static final String ORDER_BY_PROCESADO = "PROCESADO";

    public static final String ORDER_BY_PROCESADO_DESC = "PROCESADO DESC";

    public static final String ORDER_BY_ESTADO = "ESTADO";

    public static final String ORDER_BY_ESTADO_DESC = "ESTADO DESC";

    public static final String ORDER_BY_IDENTIFICADOR = "IDENTIFICADOR";

    public static final String ORDER_BY_IDENTIFICADOR_DESC = "IDENTIFICADOR DESC";

    public static final String ORDER_BY_ID_TICKET = "ID_TICKET";

    public static final String ORDER_BY_ID_TICKET_DESC = "ID_TICKET DESC";

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public LetraExample() {
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

        protected List<Criterion> procesadoCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
            fechaCriteria = new ArrayList<Criterion>();
            procesadoCriteria = new ArrayList<Criterion>();
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
                || fechaCriteria.size() > 0
                || procesadoCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<Criterion>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(fechaCriteria);
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

        public Criteria andCodCajaIsNull() {
            addCriterion("CODCAJA is null");
            return (Criteria) this;
        }

        public Criteria andCodCajaIsNotNull() {
            addCriterion("CODCAJA is not null");
            return (Criteria) this;
        }

        public Criteria andCodCajaEqualTo(String value) {
            addCriterion("CODCAJA =", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaNotEqualTo(String value) {
            addCriterion("CODCAJA <>", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaGreaterThan(String value) {
            addCriterion("CODCAJA >", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaGreaterThanOrEqualTo(String value) {
            addCriterion("CODCAJA >=", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaLessThan(String value) {
            addCriterion("CODCAJA <", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaLessThanOrEqualTo(String value) {
            addCriterion("CODCAJA <=", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaLike(String value) {
            addCriterion("CODCAJA like", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaNotLike(String value) {
            addCriterion("CODCAJA not like", value, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaIn(List<String> values) {
            addCriterion("CODCAJA in", values, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaNotIn(List<String> values) {
            addCriterion("CODCAJA not in", values, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaBetween(String value1, String value2) {
            addCriterion("CODCAJA between", value1, value2, "codCaja");
            return (Criteria) this;
        }

        public Criteria andCodCajaNotBetween(String value1, String value2) {
            addCriterion("CODCAJA not between", value1, value2, "codCaja");
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

        public Criteria andPlazoIsNull() {
            addCriterion("PLAZO is null");
            return (Criteria) this;
        }

        public Criteria andPlazoIsNotNull() {
            addCriterion("PLAZO is not null");
            return (Criteria) this;
        }

        public Criteria andPlazoEqualTo(Short value) {
            addCriterion("PLAZO =", value, "plazo");
            return (Criteria) this;
        }

        public Criteria andPlazoNotEqualTo(Short value) {
            addCriterion("PLAZO <>", value, "plazo");
            return (Criteria) this;
        }

        public Criteria andPlazoGreaterThan(Short value) {
            addCriterion("PLAZO >", value, "plazo");
            return (Criteria) this;
        }

        public Criteria andPlazoGreaterThanOrEqualTo(Short value) {
            addCriterion("PLAZO >=", value, "plazo");
            return (Criteria) this;
        }

        public Criteria andPlazoLessThan(Short value) {
            addCriterion("PLAZO <", value, "plazo");
            return (Criteria) this;
        }

        public Criteria andPlazoLessThanOrEqualTo(Short value) {
            addCriterion("PLAZO <=", value, "plazo");
            return (Criteria) this;
        }

        public Criteria andPlazoIn(List<Short> values) {
            addCriterion("PLAZO in", values, "plazo");
            return (Criteria) this;
        }

        public Criteria andPlazoNotIn(List<Short> values) {
            addCriterion("PLAZO not in", values, "plazo");
            return (Criteria) this;
        }

        public Criteria andPlazoBetween(Short value1, Short value2) {
            addCriterion("PLAZO between", value1, value2, "plazo");
            return (Criteria) this;
        }

        public Criteria andPlazoNotBetween(Short value1, Short value2) {
            addCriterion("PLAZO not between", value1, value2, "plazo");
            return (Criteria) this;
        }

        public Criteria andInteresesIsNull() {
            addCriterion("INTERESES is null");
            return (Criteria) this;
        }

        public Criteria andInteresesIsNotNull() {
            addCriterion("INTERESES is not null");
            return (Criteria) this;
        }

        public Criteria andInteresesEqualTo(BigDecimal value) {
            addCriterion("INTERESES =", value, "intereses");
            return (Criteria) this;
        }

        public Criteria andInteresesNotEqualTo(BigDecimal value) {
            addCriterion("INTERESES <>", value, "intereses");
            return (Criteria) this;
        }

        public Criteria andInteresesGreaterThan(BigDecimal value) {
            addCriterion("INTERESES >", value, "intereses");
            return (Criteria) this;
        }

        public Criteria andInteresesGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("INTERESES >=", value, "intereses");
            return (Criteria) this;
        }

        public Criteria andInteresesLessThan(BigDecimal value) {
            addCriterion("INTERESES <", value, "intereses");
            return (Criteria) this;
        }

        public Criteria andInteresesLessThanOrEqualTo(BigDecimal value) {
            addCriterion("INTERESES <=", value, "intereses");
            return (Criteria) this;
        }

        public Criteria andInteresesIn(List<BigDecimal> values) {
            addCriterion("INTERESES in", values, "intereses");
            return (Criteria) this;
        }

        public Criteria andInteresesNotIn(List<BigDecimal> values) {
            addCriterion("INTERESES not in", values, "intereses");
            return (Criteria) this;
        }

        public Criteria andInteresesBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("INTERESES between", value1, value2, "intereses");
            return (Criteria) this;
        }

        public Criteria andInteresesNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("INTERESES not between", value1, value2, "intereses");
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

        public Criteria andIdentificadorIsNull() {
            addCriterion("IDENTIFICADOR is null");
            return (Criteria) this;
        }

        public Criteria andIdentificadorIsNotNull() {
            addCriterion("IDENTIFICADOR is not null");
            return (Criteria) this;
        }

        public Criteria andIdentificadorEqualTo(Integer value) {
            addCriterion("IDENTIFICADOR =", value, "identificador");
            return (Criteria) this;
        }

        public Criteria andIdentificadorNotEqualTo(Integer value) {
            addCriterion("IDENTIFICADOR <>", value, "identificador");
            return (Criteria) this;
        }

        public Criteria andIdentificadorGreaterThan(Integer value) {
            addCriterion("IDENTIFICADOR >", value, "identificador");
            return (Criteria) this;
        }

        public Criteria andIdentificadorGreaterThanOrEqualTo(Integer value) {
            addCriterion("IDENTIFICADOR >=", value, "identificador");
            return (Criteria) this;
        }

        public Criteria andIdentificadorLessThan(Integer value) {
            addCriterion("IDENTIFICADOR <", value, "identificador");
            return (Criteria) this;
        }

        public Criteria andIdentificadorLessThanOrEqualTo(Integer value) {
            addCriterion("IDENTIFICADOR <=", value, "identificador");
            return (Criteria) this;
        }

        public Criteria andIdentificadorIn(List<Integer> values) {
            addCriterion("IDENTIFICADOR in", values, "identificador");
            return (Criteria) this;
        }

        public Criteria andIdentificadorNotIn(List<Integer> values) {
            addCriterion("IDENTIFICADOR not in", values, "identificador");
            return (Criteria) this;
        }

        public Criteria andIdentificadorBetween(Integer value1, Integer value2) {
            addCriterion("IDENTIFICADOR between", value1, value2, "identificador");
            return (Criteria) this;
        }

        public Criteria andIdentificadorNotBetween(Integer value1, Integer value2) {
            addCriterion("IDENTIFICADOR not between", value1, value2, "identificador");
            return (Criteria) this;
        }

        public Criteria andUidLetraLikeInsensitive(String value) {
            addCriterion("upper(UID_LETRA) like", value.toUpperCase(), "uidLetra");
            return (Criteria) this;
        }

        public Criteria andUidTicketLikeInsensitive(String value) {
            addCriterion("upper(UID_TICKET) like", value.toUpperCase(), "uidTicket");
            return (Criteria) this;
        }

        public Criteria andCodClienteLikeInsensitive(String value) {
            addCriterion("upper(CODCLI) like", value.toUpperCase(), "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodAlmacenLikeInsensitive(String value) {
            addCriterion("upper(CODALM) like", value.toUpperCase(), "codAlmacen");
            return (Criteria) this;
        }

        public Criteria andCodCajaLikeInsensitive(String value) {
            addCriterion("upper(CODCAJA) like", value.toUpperCase(), "codCaja");
            return (Criteria) this;
        }

        public Criteria andEstadoLikeInsensitive(String value) {
            addCriterion("upper(ESTADO) like", value.toUpperCase(), "estado");
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