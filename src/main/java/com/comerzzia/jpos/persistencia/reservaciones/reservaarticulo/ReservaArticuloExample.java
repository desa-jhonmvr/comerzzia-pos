package com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo;

import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReservaArticuloExample {
    public static final String ORDER_BY_UID_RESERVACION = "UID_RESERVACION";

    public static final String ORDER_BY_UID_RESERVACION_DESC = "UID_RESERVACION DESC";

    public static final String ORDER_BY_ID_LINEA = "ID_LINEA";

    public static final String ORDER_BY_ID_LINEA_DESC = "ID_LINEA DESC";

    public static final String ORDER_BY_CODART = "CODART";

    public static final String ORDER_BY_CODART_DESC = "CODART DESC";

    public static final String ORDER_BY_DESART = "DESART";

    public static final String ORDER_BY_DESART_DESC = "DESART DESC";

    public static final String ORDER_BY_CANTIDAD = "CANTIDAD";

    public static final String ORDER_BY_CANTIDAD_DESC = "CANTIDAD DESC";

    public static final String ORDER_BY_PRECIO = "PRECIO";

    public static final String ORDER_BY_PRECIO_DESC = "PRECIO DESC";

    public static final String ORDER_BY_PRECIO_TOTAL = "PRECIO_TOTAL";

    public static final String ORDER_BY_PRECIO_TOTAL_DESC = "PRECIO_TOTAL DESC";

    public static final String ORDER_BY_COMPRADO = "COMPRADO";

    public static final String ORDER_BY_COMPRADO_DESC = "COMPRADO DESC";

    public static final String ORDER_BY_CODBARRAS = "CODBARRAS";

    public static final String ORDER_BY_CODBARRAS_DESC = "CODBARRAS DESC";

    public static final String ORDER_BY_ID_INVITADO = "ID_INVITADO";

    public static final String ORDER_BY_ID_INVITADO_DESC = "ID_INVITADO DESC";

    public static final String ORDER_BY_COMPRADO_CON_ABONO = "COMPRADO_CON_ABONO";

    public static final String ORDER_BY_COMPRADO_CON_ABONO_DESC = "COMPRADO_CON_ABONO DESC";

    public static final String ORDER_BY_PROCESADO = "PROCESADO";

    public static final String ORDER_BY_PROCESADO_DESC = "PROCESADO DESC";

    public static final String ORDER_BY_FECHA_COMPRA = "FECHA_COMPRA";

    public static final String ORDER_BY_FECHA_COMPRA_DESC = "FECHA_COMPRA DESC";

    public static final String ORDER_BY_PROCESADO_TIENDA = "PROCESADO_TIENDA";

    public static final String ORDER_BY_PROCESADO_TIENDA_DESC = "PROCESADO_TIENDA DESC";

    public static final String ORDER_BY_PRECIO_TOTAL_SIN_DTO = "PRECIO_TOTAL_SIN_DTO";

    public static final String ORDER_BY_PRECIO_TOTAL_SIN_DTO_DESC = "PRECIO_TOTAL_SIN_DTO DESC";

    public static final String ORDER_BY_PRECIO_TOTAL_CON_DTO = "PRECIO_TOTAL_CON_DTO";

    public static final String ORDER_BY_PRECIO_TOTAL_CON_DTO_DESC = "PRECIO_TOTAL_CON_DTO DESC";
    
    public static final String ORDER_BY_UID_TICKET = "UID_TICKET";
    
    public static final String ORDER_BY_UID_TICKET_DESC = "UID_TICKET DESC";

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ReservaArticuloExample() {
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
        protected List<Criterion> compradoCriteria;

        protected List<Criterion> compradoConAbonoCriteria;

        protected List<Criterion> procesadoCriteria;

        protected List<Criterion> fechaCompraCriteria;

        protected List<Criterion> procesadoTiendaCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
            compradoCriteria = new ArrayList<Criterion>();
            compradoConAbonoCriteria = new ArrayList<Criterion>();
            procesadoCriteria = new ArrayList<Criterion>();
            fechaCompraCriteria = new ArrayList<Criterion>();
            procesadoTiendaCriteria = new ArrayList<Criterion>();
        }

        public List<Criterion> getCompradoCriteria() {
            return compradoCriteria;
        }

        protected void addCompradoCriterion(String condition, Object value, String property) {
            if (value != null) {
                compradoCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addCompradoCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                compradoCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getCompradoConAbonoCriteria() {
            return compradoConAbonoCriteria;
        }

        protected void addCompradoConAbonoCriterion(String condition, Object value, String property) {
            if (value != null) {
                compradoConAbonoCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addCompradoConAbonoCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                compradoConAbonoCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
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

        public List<Criterion> getFechaCompraCriteria() {
            return fechaCompraCriteria;
        }

        protected void addFechaCompraCriterion(String condition, Object value, String property) {
            if (value != null) {
                fechaCompraCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addFechaCompraCriterion(String condition, Fecha value1, Fecha value2, String property) {
            if (value1 != null && value2 != null) {
                fechaCompraCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
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

        public boolean isValid() {
            return criteria.size() > 0
                || compradoCriteria.size() > 0
                || compradoConAbonoCriteria.size() > 0
                || procesadoCriteria.size() > 0
                || fechaCompraCriteria.size() > 0
                || procesadoTiendaCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<Criterion>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(compradoCriteria);
                allCriteria.addAll(compradoConAbonoCriteria);
                allCriteria.addAll(procesadoCriteria);
                allCriteria.addAll(fechaCompraCriteria);
                allCriteria.addAll(procesadoTiendaCriteria);
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

        public Criteria andIdLineaIsNull() {
            addCriterion("ID_LINEA is null");
            return (Criteria) this;
        }

        public Criteria andIdLineaIsNotNull() {
            addCriterion("ID_LINEA is not null");
            return (Criteria) this;
        }

        public Criteria andIdLineaEqualTo(Long value) {
            addCriterion("ID_LINEA =", value, "idLinea");
            return (Criteria) this;
        }

        public Criteria andIdLineaNotEqualTo(Long value) {
            addCriterion("ID_LINEA <>", value, "idLinea");
            return (Criteria) this;
        }

        public Criteria andIdLineaGreaterThan(Long value) {
            addCriterion("ID_LINEA >", value, "idLinea");
            return (Criteria) this;
        }

        public Criteria andIdLineaGreaterThanOrEqualTo(Long value) {
            addCriterion("ID_LINEA >=", value, "idLinea");
            return (Criteria) this;
        }

        public Criteria andIdLineaLessThan(Long value) {
            addCriterion("ID_LINEA <", value, "idLinea");
            return (Criteria) this;
        }

        public Criteria andIdLineaLessThanOrEqualTo(Long value) {
            addCriterion("ID_LINEA <=", value, "idLinea");
            return (Criteria) this;
        }

        public Criteria andIdLineaIn(List<Long> values) {
            addCriterion("ID_LINEA in", values, "idLinea");
            return (Criteria) this;
        }

        public Criteria andIdLineaNotIn(List<Long> values) {
            addCriterion("ID_LINEA not in", values, "idLinea");
            return (Criteria) this;
        }

        public Criteria andIdLineaBetween(Long value1, Long value2) {
            addCriterion("ID_LINEA between", value1, value2, "idLinea");
            return (Criteria) this;
        }

        public Criteria andIdLineaNotBetween(Long value1, Long value2) {
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

        public Criteria andDesartIsNull() {
            addCriterion("DESART is null");
            return (Criteria) this;
        }

        public Criteria andDesartIsNotNull() {
            addCriterion("DESART is not null");
            return (Criteria) this;
        }

        public Criteria andDesartEqualTo(String value) {
            addCriterion("DESART =", value, "desart");
            return (Criteria) this;
        }

        public Criteria andDesartNotEqualTo(String value) {
            addCriterion("DESART <>", value, "desart");
            return (Criteria) this;
        }

        public Criteria andDesartGreaterThan(String value) {
            addCriterion("DESART >", value, "desart");
            return (Criteria) this;
        }

        public Criteria andDesartGreaterThanOrEqualTo(String value) {
            addCriterion("DESART >=", value, "desart");
            return (Criteria) this;
        }

        public Criteria andDesartLessThan(String value) {
            addCriterion("DESART <", value, "desart");
            return (Criteria) this;
        }

        public Criteria andDesartLessThanOrEqualTo(String value) {
            addCriterion("DESART <=", value, "desart");
            return (Criteria) this;
        }

        public Criteria andDesartLike(String value) {
            addCriterion("DESART like", value, "desart");
            return (Criteria) this;
        }

        public Criteria andDesartNotLike(String value) {
            addCriterion("DESART not like", value, "desart");
            return (Criteria) this;
        }

        public Criteria andDesartIn(List<String> values) {
            addCriterion("DESART in", values, "desart");
            return (Criteria) this;
        }

        public Criteria andDesartNotIn(List<String> values) {
            addCriterion("DESART not in", values, "desart");
            return (Criteria) this;
        }

        public Criteria andDesartBetween(String value1, String value2) {
            addCriterion("DESART between", value1, value2, "desart");
            return (Criteria) this;
        }

        public Criteria andDesartNotBetween(String value1, String value2) {
            addCriterion("DESART not between", value1, value2, "desart");
            return (Criteria) this;
        }

        public Criteria andCantidadIsNull() {
            addCriterion("CANTIDAD is null");
            return (Criteria) this;
        }

        public Criteria andCantidadIsNotNull() {
            addCriterion("CANTIDAD is not null");
            return (Criteria) this;
        }

        public Criteria andCantidadEqualTo(Long value) {
            addCriterion("CANTIDAD =", value, "cantidad");
            return (Criteria) this;
        }

        public Criteria andCantidadNotEqualTo(Long value) {
            addCriterion("CANTIDAD <>", value, "cantidad");
            return (Criteria) this;
        }

        public Criteria andCantidadGreaterThan(Long value) {
            addCriterion("CANTIDAD >", value, "cantidad");
            return (Criteria) this;
        }

        public Criteria andCantidadGreaterThanOrEqualTo(Long value) {
            addCriterion("CANTIDAD >=", value, "cantidad");
            return (Criteria) this;
        }

        public Criteria andCantidadLessThan(Long value) {
            addCriterion("CANTIDAD <", value, "cantidad");
            return (Criteria) this;
        }

        public Criteria andCantidadLessThanOrEqualTo(Long value) {
            addCriterion("CANTIDAD <=", value, "cantidad");
            return (Criteria) this;
        }

        public Criteria andCantidadIn(List<Long> values) {
            addCriterion("CANTIDAD in", values, "cantidad");
            return (Criteria) this;
        }

        public Criteria andCantidadNotIn(List<Long> values) {
            addCriterion("CANTIDAD not in", values, "cantidad");
            return (Criteria) this;
        }

        public Criteria andCantidadBetween(Long value1, Long value2) {
            addCriterion("CANTIDAD between", value1, value2, "cantidad");
            return (Criteria) this;
        }

        public Criteria andCantidadNotBetween(Long value1, Long value2) {
            addCriterion("CANTIDAD not between", value1, value2, "cantidad");
            return (Criteria) this;
        }

        public Criteria andPrecioIsNull() {
            addCriterion("PRECIO is null");
            return (Criteria) this;
        }

        public Criteria andPrecioIsNotNull() {
            addCriterion("PRECIO is not null");
            return (Criteria) this;
        }

        public Criteria andPrecioEqualTo(BigDecimal value) {
            addCriterion("PRECIO =", value, "precio");
            return (Criteria) this;
        }

        public Criteria andPrecioNotEqualTo(BigDecimal value) {
            addCriterion("PRECIO <>", value, "precio");
            return (Criteria) this;
        }

        public Criteria andPrecioGreaterThan(BigDecimal value) {
            addCriterion("PRECIO >", value, "precio");
            return (Criteria) this;
        }

        public Criteria andPrecioGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("PRECIO >=", value, "precio");
            return (Criteria) this;
        }

        public Criteria andPrecioLessThan(BigDecimal value) {
            addCriterion("PRECIO <", value, "precio");
            return (Criteria) this;
        }

        public Criteria andPrecioLessThanOrEqualTo(BigDecimal value) {
            addCriterion("PRECIO <=", value, "precio");
            return (Criteria) this;
        }

        public Criteria andPrecioIn(List<BigDecimal> values) {
            addCriterion("PRECIO in", values, "precio");
            return (Criteria) this;
        }

        public Criteria andPrecioNotIn(List<BigDecimal> values) {
            addCriterion("PRECIO not in", values, "precio");
            return (Criteria) this;
        }

        public Criteria andPrecioBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PRECIO between", value1, value2, "precio");
            return (Criteria) this;
        }

        public Criteria andPrecioNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PRECIO not between", value1, value2, "precio");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalIsNull() {
            addCriterion("PRECIO_TOTAL is null");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalIsNotNull() {
            addCriterion("PRECIO_TOTAL is not null");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalEqualTo(BigDecimal value) {
            addCriterion("PRECIO_TOTAL =", value, "precioTotal");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalNotEqualTo(BigDecimal value) {
            addCriterion("PRECIO_TOTAL <>", value, "precioTotal");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalGreaterThan(BigDecimal value) {
            addCriterion("PRECIO_TOTAL >", value, "precioTotal");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("PRECIO_TOTAL >=", value, "precioTotal");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalLessThan(BigDecimal value) {
            addCriterion("PRECIO_TOTAL <", value, "precioTotal");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalLessThanOrEqualTo(BigDecimal value) {
            addCriterion("PRECIO_TOTAL <=", value, "precioTotal");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalIn(List<BigDecimal> values) {
            addCriterion("PRECIO_TOTAL in", values, "precioTotal");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalNotIn(List<BigDecimal> values) {
            addCriterion("PRECIO_TOTAL not in", values, "precioTotal");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PRECIO_TOTAL between", value1, value2, "precioTotal");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PRECIO_TOTAL not between", value1, value2, "precioTotal");
            return (Criteria) this;
        }

        public Criteria andCompradoIsNull() {
            addCriterion("COMPRADO is null");
            return (Criteria) this;
        }

        public Criteria andCompradoIsNotNull() {
            addCriterion("COMPRADO is not null");
            return (Criteria) this;
        }

        public Criteria andCompradoEqualTo(Boolean value) {
            addCompradoCriterion("COMPRADO =", value, "comprado");
            return (Criteria) this;
        }

        public Criteria andCompradoNotEqualTo(Boolean value) {
            addCompradoCriterion("COMPRADO <>", value, "comprado");
            return (Criteria) this;
        }

        public Criteria andCompradoGreaterThan(Boolean value) {
            addCompradoCriterion("COMPRADO >", value, "comprado");
            return (Criteria) this;
        }

        public Criteria andCompradoGreaterThanOrEqualTo(Boolean value) {
            addCompradoCriterion("COMPRADO >=", value, "comprado");
            return (Criteria) this;
        }

        public Criteria andCompradoLessThan(Boolean value) {
            addCompradoCriterion("COMPRADO <", value, "comprado");
            return (Criteria) this;
        }

        public Criteria andCompradoLessThanOrEqualTo(Boolean value) {
            addCompradoCriterion("COMPRADO <=", value, "comprado");
            return (Criteria) this;
        }

        public Criteria andCompradoLike(Boolean value) {
            addCompradoCriterion("COMPRADO like", value, "comprado");
            return (Criteria) this;
        }

        public Criteria andCompradoNotLike(Boolean value) {
            addCompradoCriterion("COMPRADO not like", value, "comprado");
            return (Criteria) this;
        }

        public Criteria andCompradoIn(List<Boolean> values) {
            addCompradoCriterion("COMPRADO in", values, "comprado");
            return (Criteria) this;
        }

        public Criteria andCompradoNotIn(List<Boolean> values) {
            addCompradoCriterion("COMPRADO not in", values, "comprado");
            return (Criteria) this;
        }

        public Criteria andCompradoBetween(Boolean value1, Boolean value2) {
            addCompradoCriterion("COMPRADO between", value1, value2, "comprado");
            return (Criteria) this;
        }

        public Criteria andCompradoNotBetween(Boolean value1, Boolean value2) {
            addCompradoCriterion("COMPRADO not between", value1, value2, "comprado");
            return (Criteria) this;
        }

        public Criteria andCodbarrasIsNull() {
            addCriterion("CODBARRAS is null");
            return (Criteria) this;
        }

        public Criteria andCodbarrasIsNotNull() {
            addCriterion("CODBARRAS is not null");
            return (Criteria) this;
        }

        public Criteria andCodbarrasEqualTo(String value) {
            addCriterion("CODBARRAS =", value, "codbarras");
            return (Criteria) this;
        }

        public Criteria andCodbarrasNotEqualTo(String value) {
            addCriterion("CODBARRAS <>", value, "codbarras");
            return (Criteria) this;
        }

        public Criteria andCodbarrasGreaterThan(String value) {
            addCriterion("CODBARRAS >", value, "codbarras");
            return (Criteria) this;
        }

        public Criteria andCodbarrasGreaterThanOrEqualTo(String value) {
            addCriterion("CODBARRAS >=", value, "codbarras");
            return (Criteria) this;
        }

        public Criteria andCodbarrasLessThan(String value) {
            addCriterion("CODBARRAS <", value, "codbarras");
            return (Criteria) this;
        }

        public Criteria andCodbarrasLessThanOrEqualTo(String value) {
            addCriterion("CODBARRAS <=", value, "codbarras");
            return (Criteria) this;
        }

        public Criteria andCodbarrasLike(String value) {
            addCriterion("CODBARRAS like", value, "codbarras");
            return (Criteria) this;
        }

        public Criteria andCodbarrasNotLike(String value) {
            addCriterion("CODBARRAS not like", value, "codbarras");
            return (Criteria) this;
        }

        public Criteria andCodbarrasIn(List<String> values) {
            addCriterion("CODBARRAS in", values, "codbarras");
            return (Criteria) this;
        }

        public Criteria andCodbarrasNotIn(List<String> values) {
            addCriterion("CODBARRAS not in", values, "codbarras");
            return (Criteria) this;
        }

        public Criteria andCodbarrasBetween(String value1, String value2) {
            addCriterion("CODBARRAS between", value1, value2, "codbarras");
            return (Criteria) this;
        }

        public Criteria andCodbarrasNotBetween(String value1, String value2) {
            addCriterion("CODBARRAS not between", value1, value2, "codbarras");
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

        public Criteria andCompradoConAbonoIsNull() {
            addCriterion("COMPRADO_CON_ABONO is null");
            return (Criteria) this;
        }

        public Criteria andCompradoConAbonoIsNotNull() {
            addCriterion("COMPRADO_CON_ABONO is not null");
            return (Criteria) this;
        }

        public Criteria andCompradoConAbonoEqualTo(Boolean value) {
            addCompradoConAbonoCriterion("COMPRADO_CON_ABONO =", value, "compradoConAbono");
            return (Criteria) this;
        }

        public Criteria andCompradoConAbonoNotEqualTo(Boolean value) {
            addCompradoConAbonoCriterion("COMPRADO_CON_ABONO <>", value, "compradoConAbono");
            return (Criteria) this;
        }

        public Criteria andCompradoConAbonoGreaterThan(Boolean value) {
            addCompradoConAbonoCriterion("COMPRADO_CON_ABONO >", value, "compradoConAbono");
            return (Criteria) this;
        }

        public Criteria andCompradoConAbonoGreaterThanOrEqualTo(Boolean value) {
            addCompradoConAbonoCriterion("COMPRADO_CON_ABONO >=", value, "compradoConAbono");
            return (Criteria) this;
        }

        public Criteria andCompradoConAbonoLessThan(Boolean value) {
            addCompradoConAbonoCriterion("COMPRADO_CON_ABONO <", value, "compradoConAbono");
            return (Criteria) this;
        }

        public Criteria andCompradoConAbonoLessThanOrEqualTo(Boolean value) {
            addCompradoConAbonoCriterion("COMPRADO_CON_ABONO <=", value, "compradoConAbono");
            return (Criteria) this;
        }

        public Criteria andCompradoConAbonoLike(Boolean value) {
            addCompradoConAbonoCriterion("COMPRADO_CON_ABONO like", value, "compradoConAbono");
            return (Criteria) this;
        }

        public Criteria andCompradoConAbonoNotLike(Boolean value) {
            addCompradoConAbonoCriterion("COMPRADO_CON_ABONO not like", value, "compradoConAbono");
            return (Criteria) this;
        }

        public Criteria andCompradoConAbonoIn(List<Boolean> values) {
            addCompradoConAbonoCriterion("COMPRADO_CON_ABONO in", values, "compradoConAbono");
            return (Criteria) this;
        }

        public Criteria andCompradoConAbonoNotIn(List<Boolean> values) {
            addCompradoConAbonoCriterion("COMPRADO_CON_ABONO not in", values, "compradoConAbono");
            return (Criteria) this;
        }

        public Criteria andCompradoConAbonoBetween(Boolean value1, Boolean value2) {
            addCompradoConAbonoCriterion("COMPRADO_CON_ABONO between", value1, value2, "compradoConAbono");
            return (Criteria) this;
        }

        public Criteria andCompradoConAbonoNotBetween(Boolean value1, Boolean value2) {
            addCompradoConAbonoCriterion("COMPRADO_CON_ABONO not between", value1, value2, "compradoConAbono");
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

        public Criteria andFechaCompraIsNull() {
            addCriterion("FECHA_COMPRA is null");
            return (Criteria) this;
        }

        public Criteria andFechaCompraIsNotNull() {
            addCriterion("FECHA_COMPRA is not null");
            return (Criteria) this;
        }

        public Criteria andFechaCompraEqualTo(Fecha value) {
            addFechaCompraCriterion("FECHA_COMPRA =", value, "fechaCompra");
            return (Criteria) this;
        }

        public Criteria andFechaCompraNotEqualTo(Fecha value) {
            addFechaCompraCriterion("FECHA_COMPRA <>", value, "fechaCompra");
            return (Criteria) this;
        }

        public Criteria andFechaCompraGreaterThan(Fecha value) {
            addFechaCompraCriterion("FECHA_COMPRA >", value, "fechaCompra");
            return (Criteria) this;
        }

        public Criteria andFechaCompraGreaterThanOrEqualTo(Fecha value) {
            addFechaCompraCriterion("FECHA_COMPRA >=", value, "fechaCompra");
            return (Criteria) this;
        }

        public Criteria andFechaCompraLessThan(Fecha value) {
            addFechaCompraCriterion("FECHA_COMPRA <", value, "fechaCompra");
            return (Criteria) this;
        }

        public Criteria andFechaCompraLessThanOrEqualTo(Fecha value) {
            addFechaCompraCriterion("FECHA_COMPRA <=", value, "fechaCompra");
            return (Criteria) this;
        }

        public Criteria andFechaCompraIn(List<Fecha> values) {
            addFechaCompraCriterion("FECHA_COMPRA in", values, "fechaCompra");
            return (Criteria) this;
        }

        public Criteria andFechaCompraNotIn(List<Fecha> values) {
            addFechaCompraCriterion("FECHA_COMPRA not in", values, "fechaCompra");
            return (Criteria) this;
        }

        public Criteria andFechaCompraBetween(Fecha value1, Fecha value2) {
            addFechaCompraCriterion("FECHA_COMPRA between", value1, value2, "fechaCompra");
            return (Criteria) this;
        }

        public Criteria andFechaCompraNotBetween(Fecha value1, Fecha value2) {
            addFechaCompraCriterion("FECHA_COMPRA not between", value1, value2, "fechaCompra");
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

        public Criteria andPrecioTotalSinDtoIsNull() {
            addCriterion("PRECIO_TOTAL_SIN_DTO is null");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalSinDtoIsNotNull() {
            addCriterion("PRECIO_TOTAL_SIN_DTO is not null");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalSinDtoEqualTo(BigDecimal value) {
            addCriterion("PRECIO_TOTAL_SIN_DTO =", value, "precioTotalSinDto");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalSinDtoNotEqualTo(BigDecimal value) {
            addCriterion("PRECIO_TOTAL_SIN_DTO <>", value, "precioTotalSinDto");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalSinDtoGreaterThan(BigDecimal value) {
            addCriterion("PRECIO_TOTAL_SIN_DTO >", value, "precioTotalSinDto");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalSinDtoGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("PRECIO_TOTAL_SIN_DTO >=", value, "precioTotalSinDto");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalSinDtoLessThan(BigDecimal value) {
            addCriterion("PRECIO_TOTAL_SIN_DTO <", value, "precioTotalSinDto");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalSinDtoLessThanOrEqualTo(BigDecimal value) {
            addCriterion("PRECIO_TOTAL_SIN_DTO <=", value, "precioTotalSinDto");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalSinDtoIn(List<BigDecimal> values) {
            addCriterion("PRECIO_TOTAL_SIN_DTO in", values, "precioTotalSinDto");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalSinDtoNotIn(List<BigDecimal> values) {
            addCriterion("PRECIO_TOTAL_SIN_DTO not in", values, "precioTotalSinDto");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalSinDtoBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PRECIO_TOTAL_SIN_DTO between", value1, value2, "precioTotalSinDto");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalSinDtoNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PRECIO_TOTAL_SIN_DTO not between", value1, value2, "precioTotalSinDto");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalConDtoIsNull() {
            addCriterion("PRECIO_TOTAL_CON_DTO is null");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalConDtoIsNotNull() {
            addCriterion("PRECIO_TOTAL_CON_DTO is not null");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalConDtoEqualTo(BigDecimal value) {
            addCriterion("PRECIO_TOTAL_CON_DTO =", value, "precioTotalConDto");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalConDtoNotEqualTo(BigDecimal value) {
            addCriterion("PRECIO_TOTAL_CON_DTO <>", value, "precioTotalConDto");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalConDtoGreaterThan(BigDecimal value) {
            addCriterion("PRECIO_TOTAL_CON_DTO >", value, "precioTotalConDto");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalConDtoGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("PRECIO_TOTAL_CON_DTO >=", value, "precioTotalConDto");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalConDtoLessThan(BigDecimal value) {
            addCriterion("PRECIO_TOTAL_CON_DTO <", value, "precioTotalConDto");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalConDtoLessThanOrEqualTo(BigDecimal value) {
            addCriterion("PRECIO_TOTAL_CON_DTO <=", value, "precioTotalConDto");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalConDtoIn(List<BigDecimal> values) {
            addCriterion("PRECIO_TOTAL_CON_DTO in", values, "precioTotalConDto");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalConDtoNotIn(List<BigDecimal> values) {
            addCriterion("PRECIO_TOTAL_CON_DTO not in", values, "precioTotalConDto");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalConDtoBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PRECIO_TOTAL_CON_DTO between", value1, value2, "precioTotalConDto");
            return (Criteria) this;
        }

        public Criteria andPrecioTotalConDtoNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PRECIO_TOTAL_CON_DTO not between", value1, value2, "precioTotalConDto");
            return (Criteria) this;
        }

        public Criteria andUidReservacionLikeInsensitive(String value) {
            addCriterion("upper(UID_RESERVACION) like", value.toUpperCase(), "uidReservacion");
            return (Criteria) this;
        }

        public Criteria andCodartLikeInsensitive(String value) {
            addCriterion("upper(CODART) like", value.toUpperCase(), "codart");
            return (Criteria) this;
        }

        public Criteria andDesartLikeInsensitive(String value) {
            addCriterion("upper(DESART) like", value.toUpperCase(), "desart");
            return (Criteria) this;
        }

        public Criteria andCodbarrasLikeInsensitive(String value) {
            addCriterion("upper(CODBARRAS) like", value.toUpperCase(), "codbarras");
            return (Criteria) this;
        }
        
        public Criteria andUidTicketEqual(String value) {
            addCriterion("UID_TICKET =", value, "uidTicket");
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