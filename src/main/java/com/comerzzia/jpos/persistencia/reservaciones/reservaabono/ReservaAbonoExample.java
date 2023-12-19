package com.comerzzia.jpos.persistencia.reservaciones.reservaabono;

import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReservaAbonoExample {
    public static final String ORDER_BY_UID_RESERVACION = "UID_RESERVACION";

    public static final String ORDER_BY_UID_RESERVACION_DESC = "UID_RESERVACION DESC";

    public static final String ORDER_BY_ID_ABONO = "ID_ABONO";

    public static final String ORDER_BY_ID_ABONO_DESC = "ID_ABONO DESC";

    public static final String ORDER_BY_CANTIDAD_ABONO = "CANTIDAD_ABONO";

    public static final String ORDER_BY_CANTIDAD_ABONO_DESC = "CANTIDAD_ABONO DESC";

    public static final String ORDER_BY_ID_INVITADO = "ID_INVITADO";

    public static final String ORDER_BY_ID_INVITADO_DESC = "ID_INVITADO DESC";

    public static final String ORDER_BY_PROCESADO = "PROCESADO";

    public static final String ORDER_BY_PROCESADO_DESC = "PROCESADO DESC";

    public static final String ORDER_BY_CODCAJA = "CODCAJA";

    public static final String ORDER_BY_CODCAJA_DESC = "CODCAJA DESC";

    public static final String ORDER_BY_FECHA_ABONO = "FECHA_ABONO";

    public static final String ORDER_BY_FECHA_ABONO_DESC = "FECHA_ABONO DESC";

    public static final String ORDER_BY_PROCESADO_TIENDA = "PROCESADO_TIENDA";

    public static final String ORDER_BY_PROCESADO_TIENDA_DESC = "PROCESADO_TIENDA DESC";

    public static final String ORDER_BY_CANT_ABONO_SIN_DTO = "CANT_ABONO_SIN_DTO";

    public static final String ORDER_BY_CANT_ABONO_SIN_DTO_DESC = "CANT_ABONO_SIN_DTO DESC";

    public static final String ORDER_BY_TIPO_ABONO = "TIPO_ABONO";

    public static final String ORDER_BY_TIPO_ABONO_DESC = "TIPO_ABONO DESC";

    public static final String ORDER_BY_CAJERO = "CAJERO";

    public static final String ORDER_BY_CAJERO_DESC = "CAJERO DESC";

    public static final String ORDER_BY_ANULADO = "ANULADO";

    public static final String ORDER_BY_ANULADO_DESC = "ANULADO DESC";

    public static final String ORDER_BY_PAGOS = "PAGOS";

    public static final String ORDER_BY_PAGOS_DESC = "PAGOS DESC";

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ReservaAbonoExample() {
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

        protected List<Criterion> fechaAbonoCriteria;

        protected List<Criterion> procesadoTiendaCriteria;

        protected List<Criterion> anuladoCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
            procesadoCriteria = new ArrayList<Criterion>();
            fechaAbonoCriteria = new ArrayList<Criterion>();
            procesadoTiendaCriteria = new ArrayList<Criterion>();
            anuladoCriteria = new ArrayList<Criterion>();
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

        public List<Criterion> getFechaAbonoCriteria() {
            return fechaAbonoCriteria;
        }

        protected void addFechaAbonoCriterion(String condition, Object value, String property) {
            if (value != null) {
                fechaAbonoCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addFechaAbonoCriterion(String condition, Fecha value1, Fecha value2, String property) {
            if (value1 != null && value2 != null) {
                fechaAbonoCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getProcesadoTiendaCriteria() {
            return procesadoTiendaCriteria;
        }

        protected void addProcesadoTiendaCriterion(String condition, Object value, String property) {
            if (value != null) {
                procesadoTiendaCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addProcesadoTiendaCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                procesadoTiendaCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getAnuladoCriteria() {
            return anuladoCriteria;
        }

        protected void addAnuladoCriterion(String condition, Object value, String property) {
            if (value != null) {
                anuladoCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addAnuladoCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                anuladoCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public boolean isValid() {
            return criteria.size() > 0
                || procesadoCriteria.size() > 0
                || fechaAbonoCriteria.size() > 0
                || procesadoTiendaCriteria.size() > 0
                || anuladoCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<Criterion>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(procesadoCriteria);
                allCriteria.addAll(fechaAbonoCriteria);
                allCriteria.addAll(procesadoTiendaCriteria);
                allCriteria.addAll(anuladoCriteria);
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

        public Criteria andUidReservacionIsNull() {
            addCriterion("UID_RESERVACION is null");
            return (Criteria) this;
        }

        public Criteria andUidReservacionIsNotNull() {
            addCriterion("UID_RESERVACION is not null");
            return (Criteria) this;
        }

        public Criteria andUidReservacionEqualTo(String value) {
            addCriterion("UID_RESERVACION =", value, "uidReservacion");
            return (Criteria) this;
        }

        public Criteria andUidReservacionNotEqualTo(String value) {
            addCriterion("UID_RESERVACION <>", value, "uidReservacion");
            return (Criteria) this;
        }

        public Criteria andUidReservacionGreaterThan(String value) {
            addCriterion("UID_RESERVACION >", value, "uidReservacion");
            return (Criteria) this;
        }

        public Criteria andUidReservacionGreaterThanOrEqualTo(String value) {
            addCriterion("UID_RESERVACION >=", value, "uidReservacion");
            return (Criteria) this;
        }

        public Criteria andUidReservacionLessThan(String value) {
            addCriterion("UID_RESERVACION <", value, "uidReservacion");
            return (Criteria) this;
        }

        public Criteria andUidReservacionLessThanOrEqualTo(String value) {
            addCriterion("UID_RESERVACION <=", value, "uidReservacion");
            return (Criteria) this;
        }

        public Criteria andUidReservacionLike(String value) {
            addCriterion("UID_RESERVACION like", value, "uidReservacion");
            return (Criteria) this;
        }

        public Criteria andUidReservacionNotLike(String value) {
            addCriterion("UID_RESERVACION not like", value, "uidReservacion");
            return (Criteria) this;
        }

        public Criteria andUidReservacionIn(List<String> values) {
            addCriterion("UID_RESERVACION in", values, "uidReservacion");
            return (Criteria) this;
        }

        public Criteria andUidReservacionNotIn(List<String> values) {
            addCriterion("UID_RESERVACION not in", values, "uidReservacion");
            return (Criteria) this;
        }

        public Criteria andUidReservacionBetween(String value1, String value2) {
            addCriterion("UID_RESERVACION between", value1, value2, "uidReservacion");
            return (Criteria) this;
        }

        public Criteria andUidReservacionNotBetween(String value1, String value2) {
            addCriterion("UID_RESERVACION not between", value1, value2, "uidReservacion");
            return (Criteria) this;
        }

        public Criteria andIdAbonoIsNull() {
            addCriterion("ID_ABONO is null");
            return (Criteria) this;
        }

        public Criteria andIdAbonoIsNotNull() {
            addCriterion("ID_ABONO is not null");
            return (Criteria) this;
        }

        public Criteria andIdAbonoEqualTo(Long value) {
            addCriterion("ID_ABONO =", value, "idAbono");
            return (Criteria) this;
        }

        public Criteria andIdAbonoNotEqualTo(Long value) {
            addCriterion("ID_ABONO <>", value, "idAbono");
            return (Criteria) this;
        }

        public Criteria andIdAbonoGreaterThan(Long value) {
            addCriterion("ID_ABONO >", value, "idAbono");
            return (Criteria) this;
        }

        public Criteria andIdAbonoGreaterThanOrEqualTo(Long value) {
            addCriterion("ID_ABONO >=", value, "idAbono");
            return (Criteria) this;
        }

        public Criteria andIdAbonoLessThan(Long value) {
            addCriterion("ID_ABONO <", value, "idAbono");
            return (Criteria) this;
        }

        public Criteria andIdAbonoLessThanOrEqualTo(Long value) {
            addCriterion("ID_ABONO <=", value, "idAbono");
            return (Criteria) this;
        }

        public Criteria andIdAbonoIn(List<Long> values) {
            addCriterion("ID_ABONO in", values, "idAbono");
            return (Criteria) this;
        }

        public Criteria andIdAbonoNotIn(List<Long> values) {
            addCriterion("ID_ABONO not in", values, "idAbono");
            return (Criteria) this;
        }

        public Criteria andIdAbonoBetween(Long value1, Long value2) {
            addCriterion("ID_ABONO between", value1, value2, "idAbono");
            return (Criteria) this;
        }

        public Criteria andIdAbonoNotBetween(Long value1, Long value2) {
            addCriterion("ID_ABONO not between", value1, value2, "idAbono");
            return (Criteria) this;
        }

        public Criteria andCantidadAbonoIsNull() {
            addCriterion("CANTIDAD_ABONO is null");
            return (Criteria) this;
        }

        public Criteria andCantidadAbonoIsNotNull() {
            addCriterion("CANTIDAD_ABONO is not null");
            return (Criteria) this;
        }

        public Criteria andCantidadAbonoEqualTo(BigDecimal value) {
            addCriterion("CANTIDAD_ABONO =", value, "cantidadAbono");
            return (Criteria) this;
        }

        public Criteria andCantidadAbonoNotEqualTo(BigDecimal value) {
            addCriterion("CANTIDAD_ABONO <>", value, "cantidadAbono");
            return (Criteria) this;
        }

        public Criteria andCantidadAbonoGreaterThan(BigDecimal value) {
            addCriterion("CANTIDAD_ABONO >", value, "cantidadAbono");
            return (Criteria) this;
        }

        public Criteria andCantidadAbonoGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("CANTIDAD_ABONO >=", value, "cantidadAbono");
            return (Criteria) this;
        }

        public Criteria andCantidadAbonoLessThan(BigDecimal value) {
            addCriterion("CANTIDAD_ABONO <", value, "cantidadAbono");
            return (Criteria) this;
        }

        public Criteria andCantidadAbonoLessThanOrEqualTo(BigDecimal value) {
            addCriterion("CANTIDAD_ABONO <=", value, "cantidadAbono");
            return (Criteria) this;
        }

        public Criteria andCantidadAbonoIn(List<BigDecimal> values) {
            addCriterion("CANTIDAD_ABONO in", values, "cantidadAbono");
            return (Criteria) this;
        }

        public Criteria andCantidadAbonoNotIn(List<BigDecimal> values) {
            addCriterion("CANTIDAD_ABONO not in", values, "cantidadAbono");
            return (Criteria) this;
        }

        public Criteria andCantidadAbonoBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("CANTIDAD_ABONO between", value1, value2, "cantidadAbono");
            return (Criteria) this;
        }

        public Criteria andCantidadAbonoNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("CANTIDAD_ABONO not between", value1, value2, "cantidadAbono");
            return (Criteria) this;
        }

        public Criteria andIdInvitadoIsNull() {
            addCriterion("ID_INVITADO is null");
            return (Criteria) this;
        }

        public Criteria andIdInvitadoIsNotNull() {
            addCriterion("ID_INVITADO is not null");
            return (Criteria) this;
        }

        public Criteria andIdInvitadoEqualTo(Long value) {
            addCriterion("ID_INVITADO =", value, "idInvitado");
            return (Criteria) this;
        }

        public Criteria andIdInvitadoNotEqualTo(Long value) {
            addCriterion("ID_INVITADO <>", value, "idInvitado");
            return (Criteria) this;
        }

        public Criteria andIdInvitadoGreaterThan(Long value) {
            addCriterion("ID_INVITADO >", value, "idInvitado");
            return (Criteria) this;
        }

        public Criteria andIdInvitadoGreaterThanOrEqualTo(Long value) {
            addCriterion("ID_INVITADO >=", value, "idInvitado");
            return (Criteria) this;
        }

        public Criteria andIdInvitadoLessThan(Long value) {
            addCriterion("ID_INVITADO <", value, "idInvitado");
            return (Criteria) this;
        }

        public Criteria andIdInvitadoLessThanOrEqualTo(Long value) {
            addCriterion("ID_INVITADO <=", value, "idInvitado");
            return (Criteria) this;
        }

        public Criteria andIdInvitadoIn(List<Long> values) {
            addCriterion("ID_INVITADO in", values, "idInvitado");
            return (Criteria) this;
        }

        public Criteria andIdInvitadoNotIn(List<Long> values) {
            addCriterion("ID_INVITADO not in", values, "idInvitado");
            return (Criteria) this;
        }

        public Criteria andIdInvitadoBetween(Long value1, Long value2) {
            addCriterion("ID_INVITADO between", value1, value2, "idInvitado");
            return (Criteria) this;
        }

        public Criteria andIdInvitadoNotBetween(Long value1, Long value2) {
            addCriterion("ID_INVITADO not between", value1, value2, "idInvitado");
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

        public Criteria andFechaAbonoIsNull() {
            addCriterion("FECHA_ABONO is null");
            return (Criteria) this;
        }

        public Criteria andFechaAbonoIsNotNull() {
            addCriterion("FECHA_ABONO is not null");
            return (Criteria) this;
        }

        public Criteria andFechaAbonoEqualTo(Fecha value) {
            addFechaAbonoCriterion("FECHA_ABONO =", value, "fechaAbono");
            return (Criteria) this;
        }

        public Criteria andFechaAbonoNotEqualTo(Fecha value) {
            addFechaAbonoCriterion("FECHA_ABONO <>", value, "fechaAbono");
            return (Criteria) this;
        }

        public Criteria andFechaAbonoGreaterThan(Fecha value) {
            addFechaAbonoCriterion("FECHA_ABONO >", value, "fechaAbono");
            return (Criteria) this;
        }

        public Criteria andFechaAbonoGreaterThanOrEqualTo(Fecha value) {
            addFechaAbonoCriterion("FECHA_ABONO >=", value, "fechaAbono");
            return (Criteria) this;
        }

        public Criteria andFechaAbonoLessThan(Fecha value) {
            addFechaAbonoCriterion("FECHA_ABONO <", value, "fechaAbono");
            return (Criteria) this;
        }

        public Criteria andFechaAbonoLessThanOrEqualTo(Fecha value) {
            addFechaAbonoCriterion("FECHA_ABONO <=", value, "fechaAbono");
            return (Criteria) this;
        }

        public Criteria andFechaAbonoIn(List<Fecha> values) {
            addFechaAbonoCriterion("FECHA_ABONO in", values, "fechaAbono");
            return (Criteria) this;
        }

        public Criteria andFechaAbonoNotIn(List<Fecha> values) {
            addFechaAbonoCriterion("FECHA_ABONO not in", values, "fechaAbono");
            return (Criteria) this;
        }

        public Criteria andFechaAbonoBetween(Fecha value1, Fecha value2) {
            addFechaAbonoCriterion("FECHA_ABONO between", value1, value2, "fechaAbono");
            return (Criteria) this;
        }

        public Criteria andFechaAbonoNotBetween(Fecha value1, Fecha value2) {
            addFechaAbonoCriterion("FECHA_ABONO not between", value1, value2, "fechaAbono");
            return (Criteria) this;
        }

        public Criteria andProcesadoTiendaIsNull() {
            addCriterion("PROCESADO_TIENDA is null");
            return (Criteria) this;
        }

        public Criteria andProcesadoTiendaIsNotNull() {
            addCriterion("PROCESADO_TIENDA is not null");
            return (Criteria) this;
        }

        public Criteria andProcesadoTiendaEqualTo(Boolean value) {
            addProcesadoTiendaCriterion("PROCESADO_TIENDA =", value, "procesadoTienda");
            return (Criteria) this;
        }

        public Criteria andProcesadoTiendaNotEqualTo(Boolean value) {
            addProcesadoTiendaCriterion("PROCESADO_TIENDA <>", value, "procesadoTienda");
            return (Criteria) this;
        }

        public Criteria andProcesadoTiendaGreaterThan(Boolean value) {
            addProcesadoTiendaCriterion("PROCESADO_TIENDA >", value, "procesadoTienda");
            return (Criteria) this;
        }

        public Criteria andProcesadoTiendaGreaterThanOrEqualTo(Boolean value) {
            addProcesadoTiendaCriterion("PROCESADO_TIENDA >=", value, "procesadoTienda");
            return (Criteria) this;
        }

        public Criteria andProcesadoTiendaLessThan(Boolean value) {
            addProcesadoTiendaCriterion("PROCESADO_TIENDA <", value, "procesadoTienda");
            return (Criteria) this;
        }

        public Criteria andProcesadoTiendaLessThanOrEqualTo(Boolean value) {
            addProcesadoTiendaCriterion("PROCESADO_TIENDA <=", value, "procesadoTienda");
            return (Criteria) this;
        }

        public Criteria andProcesadoTiendaLike(Boolean value) {
            addProcesadoTiendaCriterion("PROCESADO_TIENDA like", value, "procesadoTienda");
            return (Criteria) this;
        }

        public Criteria andProcesadoTiendaNotLike(Boolean value) {
            addProcesadoTiendaCriterion("PROCESADO_TIENDA not like", value, "procesadoTienda");
            return (Criteria) this;
        }

        public Criteria andProcesadoTiendaIn(List<Boolean> values) {
            addProcesadoTiendaCriterion("PROCESADO_TIENDA in", values, "procesadoTienda");
            return (Criteria) this;
        }

        public Criteria andProcesadoTiendaNotIn(List<Boolean> values) {
            addProcesadoTiendaCriterion("PROCESADO_TIENDA not in", values, "procesadoTienda");
            return (Criteria) this;
        }

        public Criteria andProcesadoTiendaBetween(Boolean value1, Boolean value2) {
            addProcesadoTiendaCriterion("PROCESADO_TIENDA between", value1, value2, "procesadoTienda");
            return (Criteria) this;
        }

        public Criteria andProcesadoTiendaNotBetween(Boolean value1, Boolean value2) {
            addProcesadoTiendaCriterion("PROCESADO_TIENDA not between", value1, value2, "procesadoTienda");
            return (Criteria) this;
        }

        public Criteria andCantAbonoSinDtoIsNull() {
            addCriterion("CANT_ABONO_SIN_DTO is null");
            return (Criteria) this;
        }

        public Criteria andCantAbonoSinDtoIsNotNull() {
            addCriterion("CANT_ABONO_SIN_DTO is not null");
            return (Criteria) this;
        }

        public Criteria andCantAbonoSinDtoEqualTo(BigDecimal value) {
            addCriterion("CANT_ABONO_SIN_DTO =", value, "cantAbonoSinDto");
            return (Criteria) this;
        }

        public Criteria andCantAbonoSinDtoNotEqualTo(BigDecimal value) {
            addCriterion("CANT_ABONO_SIN_DTO <>", value, "cantAbonoSinDto");
            return (Criteria) this;
        }

        public Criteria andCantAbonoSinDtoGreaterThan(BigDecimal value) {
            addCriterion("CANT_ABONO_SIN_DTO >", value, "cantAbonoSinDto");
            return (Criteria) this;
        }

        public Criteria andCantAbonoSinDtoGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("CANT_ABONO_SIN_DTO >=", value, "cantAbonoSinDto");
            return (Criteria) this;
        }

        public Criteria andCantAbonoSinDtoLessThan(BigDecimal value) {
            addCriterion("CANT_ABONO_SIN_DTO <", value, "cantAbonoSinDto");
            return (Criteria) this;
        }

        public Criteria andCantAbonoSinDtoLessThanOrEqualTo(BigDecimal value) {
            addCriterion("CANT_ABONO_SIN_DTO <=", value, "cantAbonoSinDto");
            return (Criteria) this;
        }

        public Criteria andCantAbonoSinDtoIn(List<BigDecimal> values) {
            addCriterion("CANT_ABONO_SIN_DTO in", values, "cantAbonoSinDto");
            return (Criteria) this;
        }

        public Criteria andCantAbonoSinDtoNotIn(List<BigDecimal> values) {
            addCriterion("CANT_ABONO_SIN_DTO not in", values, "cantAbonoSinDto");
            return (Criteria) this;
        }

        public Criteria andCantAbonoSinDtoBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("CANT_ABONO_SIN_DTO between", value1, value2, "cantAbonoSinDto");
            return (Criteria) this;
        }

        public Criteria andCantAbonoSinDtoNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("CANT_ABONO_SIN_DTO not between", value1, value2, "cantAbonoSinDto");
            return (Criteria) this;
        }

        public Criteria andTipoAbonoIsNull() {
            addCriterion("TIPO_ABONO is null");
            return (Criteria) this;
        }

        public Criteria andTipoAbonoIsNotNull() {
            addCriterion("TIPO_ABONO is not null");
            return (Criteria) this;
        }

        public Criteria andTipoAbonoEqualTo(String value) {
            addCriterion("TIPO_ABONO =", value, "tipoAbono");
            return (Criteria) this;
        }

        public Criteria andTipoAbonoNotEqualTo(String value) {
            addCriterion("TIPO_ABONO <>", value, "tipoAbono");
            return (Criteria) this;
        }

        public Criteria andTipoAbonoGreaterThan(String value) {
            addCriterion("TIPO_ABONO >", value, "tipoAbono");
            return (Criteria) this;
        }

        public Criteria andTipoAbonoGreaterThanOrEqualTo(String value) {
            addCriterion("TIPO_ABONO >=", value, "tipoAbono");
            return (Criteria) this;
        }

        public Criteria andTipoAbonoLessThan(String value) {
            addCriterion("TIPO_ABONO <", value, "tipoAbono");
            return (Criteria) this;
        }

        public Criteria andTipoAbonoLessThanOrEqualTo(String value) {
            addCriterion("TIPO_ABONO <=", value, "tipoAbono");
            return (Criteria) this;
        }

        public Criteria andTipoAbonoLike(String value) {
            addCriterion("TIPO_ABONO like", value, "tipoAbono");
            return (Criteria) this;
        }

        public Criteria andTipoAbonoNotLike(String value) {
            addCriterion("TIPO_ABONO not like", value, "tipoAbono");
            return (Criteria) this;
        }

        public Criteria andTipoAbonoIn(List<String> values) {
            addCriterion("TIPO_ABONO in", values, "tipoAbono");
            return (Criteria) this;
        }

        public Criteria andTipoAbonoNotIn(List<String> values) {
            addCriterion("TIPO_ABONO not in", values, "tipoAbono");
            return (Criteria) this;
        }

        public Criteria andTipoAbonoBetween(String value1, String value2) {
            addCriterion("TIPO_ABONO between", value1, value2, "tipoAbono");
            return (Criteria) this;
        }

        public Criteria andTipoAbonoNotBetween(String value1, String value2) {
            addCriterion("TIPO_ABONO not between", value1, value2, "tipoAbono");
            return (Criteria) this;
        }

        public Criteria andCajeroIsNull() {
            addCriterion("CAJERO is null");
            return (Criteria) this;
        }

        public Criteria andCajeroIsNotNull() {
            addCriterion("CAJERO is not null");
            return (Criteria) this;
        }

        public Criteria andCajeroEqualTo(String value) {
            addCriterion("CAJERO =", value, "cajero");
            return (Criteria) this;
        }

        public Criteria andCajeroNotEqualTo(String value) {
            addCriterion("CAJERO <>", value, "cajero");
            return (Criteria) this;
        }

        public Criteria andCajeroGreaterThan(String value) {
            addCriterion("CAJERO >", value, "cajero");
            return (Criteria) this;
        }

        public Criteria andCajeroGreaterThanOrEqualTo(String value) {
            addCriterion("CAJERO >=", value, "cajero");
            return (Criteria) this;
        }

        public Criteria andCajeroLessThan(String value) {
            addCriterion("CAJERO <", value, "cajero");
            return (Criteria) this;
        }

        public Criteria andCajeroLessThanOrEqualTo(String value) {
            addCriterion("CAJERO <=", value, "cajero");
            return (Criteria) this;
        }

        public Criteria andCajeroLike(String value) {
            addCriterion("CAJERO like", value, "cajero");
            return (Criteria) this;
        }

        public Criteria andCajeroNotLike(String value) {
            addCriterion("CAJERO not like", value, "cajero");
            return (Criteria) this;
        }

        public Criteria andCajeroIn(List<String> values) {
            addCriterion("CAJERO in", values, "cajero");
            return (Criteria) this;
        }

        public Criteria andCajeroNotIn(List<String> values) {
            addCriterion("CAJERO not in", values, "cajero");
            return (Criteria) this;
        }

        public Criteria andCajeroBetween(String value1, String value2) {
            addCriterion("CAJERO between", value1, value2, "cajero");
            return (Criteria) this;
        }

        public Criteria andCajeroNotBetween(String value1, String value2) {
            addCriterion("CAJERO not between", value1, value2, "cajero");
            return (Criteria) this;
        }

        public Criteria andAnuladoIsNull() {
            addCriterion("ANULADO is null");
            return (Criteria) this;
        }

        public Criteria andAnuladoIsNotNull() {
            addCriterion("ANULADO is not null");
            return (Criteria) this;
        }

        public Criteria andAnuladoEqualTo(Boolean value) {
            addAnuladoCriterion("ANULADO =", value, "anulado");
            return (Criteria) this;
        }

        public Criteria andAnuladoNotEqualTo(Boolean value) {
            addAnuladoCriterion("ANULADO <>", value, "anulado");
            return (Criteria) this;
        }

        public Criteria andAnuladoGreaterThan(Boolean value) {
            addAnuladoCriterion("ANULADO >", value, "anulado");
            return (Criteria) this;
        }

        public Criteria andAnuladoGreaterThanOrEqualTo(Boolean value) {
            addAnuladoCriterion("ANULADO >=", value, "anulado");
            return (Criteria) this;
        }

        public Criteria andAnuladoLessThan(Boolean value) {
            addAnuladoCriterion("ANULADO <", value, "anulado");
            return (Criteria) this;
        }

        public Criteria andAnuladoLessThanOrEqualTo(Boolean value) {
            addAnuladoCriterion("ANULADO <=", value, "anulado");
            return (Criteria) this;
        }

        public Criteria andAnuladoLike(Boolean value) {
            addAnuladoCriterion("ANULADO like", value, "anulado");
            return (Criteria) this;
        }

        public Criteria andAnuladoNotLike(Boolean value) {
            addAnuladoCriterion("ANULADO not like", value, "anulado");
            return (Criteria) this;
        }

        public Criteria andAnuladoIn(List<Boolean> values) {
            addAnuladoCriterion("ANULADO in", values, "anulado");
            return (Criteria) this;
        }

        public Criteria andAnuladoNotIn(List<Boolean> values) {
            addAnuladoCriterion("ANULADO not in", values, "anulado");
            return (Criteria) this;
        }

        public Criteria andAnuladoBetween(Boolean value1, Boolean value2) {
            addAnuladoCriterion("ANULADO between", value1, value2, "anulado");
            return (Criteria) this;
        }

        public Criteria andAnuladoNotBetween(Boolean value1, Boolean value2) {
            addAnuladoCriterion("ANULADO not between", value1, value2, "anulado");
            return (Criteria) this;
        }

        public Criteria andUidReservacionLikeInsensitive(String value) {
            addCriterion("upper(UID_RESERVACION) like", value.toUpperCase(), "uidReservacion");
            return (Criteria) this;
        }

        public Criteria andCodcajaLikeInsensitive(String value) {
            addCriterion("upper(CODCAJA) like", value.toUpperCase(), "codcaja");
            return (Criteria) this;
        }

        public Criteria andTipoAbonoLikeInsensitive(String value) {
            addCriterion("upper(TIPO_ABONO) like", value.toUpperCase(), "tipoAbono");
            return (Criteria) this;
        }

        public Criteria andCajeroLikeInsensitive(String value) {
            addCriterion("upper(CAJERO) like", value.toUpperCase(), "cajero");
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