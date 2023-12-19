package com.comerzzia.jpos.persistencia.reservaciones.reservainvitados;

import java.util.ArrayList;
import java.util.List;

public class ReservaInvitadoExample {
    public static final String ORDER_BY_UID_RESERVACION = "UID_RESERVACION";

    public static final String ORDER_BY_UID_RESERVACION_DESC = "UID_RESERVACION DESC";

    public static final String ORDER_BY_ID_INVITADO = "ID_INVITADO";

    public static final String ORDER_BY_ID_INVITADO_DESC = "ID_INVITADO DESC";

    public static final String ORDER_BY_NOMBRE = "NOMBRE";

    public static final String ORDER_BY_NOMBRE_DESC = "NOMBRE DESC";

    public static final String ORDER_BY_APELLIDO = "APELLIDO";

    public static final String ORDER_BY_APELLIDO_DESC = "APELLIDO DESC";

    public static final String ORDER_BY_EMAIL = "EMAIL";

    public static final String ORDER_BY_EMAIL_DESC = "EMAIL DESC";

    public static final String ORDER_BY_TELEFONO = "TELEFONO";

    public static final String ORDER_BY_TELEFONO_DESC = "TELEFONO DESC";

    public static final String ORDER_BY_PROCESADO = "PROCESADO";

    public static final String ORDER_BY_PROCESADO_DESC = "PROCESADO DESC";

    public static final String ORDER_BY_PROCESADO_TIENDA = "PROCESADO_TIENDA";

    public static final String ORDER_BY_PROCESADO_TIENDA_DESC = "PROCESADO_TIENDA DESC";

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ReservaInvitadoExample() {
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

        protected List<Criterion> procesadoTiendaCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
            procesadoCriteria = new ArrayList<Criterion>();
            procesadoTiendaCriteria = new ArrayList<Criterion>();
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
                || procesadoCriteria.size() > 0
                || procesadoTiendaCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<Criterion>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(procesadoCriteria);
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

        public Criteria andNombreIsNull() {
            addCriterion("NOMBRE is null");
            return (Criteria) this;
        }

        public Criteria andNombreIsNotNull() {
            addCriterion("NOMBRE is not null");
            return (Criteria) this;
        }

        public Criteria andNombreEqualTo(String value) {
            addCriterion("NOMBRE =", value, "nombre");
            return (Criteria) this;
        }

        public Criteria andNombreNotEqualTo(String value) {
            addCriterion("NOMBRE <>", value, "nombre");
            return (Criteria) this;
        }

        public Criteria andNombreGreaterThan(String value) {
            addCriterion("NOMBRE >", value, "nombre");
            return (Criteria) this;
        }

        public Criteria andNombreGreaterThanOrEqualTo(String value) {
            addCriterion("NOMBRE >=", value, "nombre");
            return (Criteria) this;
        }

        public Criteria andNombreLessThan(String value) {
            addCriterion("NOMBRE <", value, "nombre");
            return (Criteria) this;
        }

        public Criteria andNombreLessThanOrEqualTo(String value) {
            addCriterion("NOMBRE <=", value, "nombre");
            return (Criteria) this;
        }

        public Criteria andNombreLike(String value) {
            addCriterion("NOMBRE like", value, "nombre");
            return (Criteria) this;
        }

        public Criteria andNombreNotLike(String value) {
            addCriterion("NOMBRE not like", value, "nombre");
            return (Criteria) this;
        }

        public Criteria andNombreIn(List<String> values) {
            addCriterion("NOMBRE in", values, "nombre");
            return (Criteria) this;
        }

        public Criteria andNombreNotIn(List<String> values) {
            addCriterion("NOMBRE not in", values, "nombre");
            return (Criteria) this;
        }

        public Criteria andNombreBetween(String value1, String value2) {
            addCriterion("NOMBRE between", value1, value2, "nombre");
            return (Criteria) this;
        }

        public Criteria andNombreNotBetween(String value1, String value2) {
            addCriterion("NOMBRE not between", value1, value2, "nombre");
            return (Criteria) this;
        }

        public Criteria andApellidoIsNull() {
            addCriterion("APELLIDO is null");
            return (Criteria) this;
        }

        public Criteria andApellidoIsNotNull() {
            addCriterion("APELLIDO is not null");
            return (Criteria) this;
        }

        public Criteria andApellidoEqualTo(String value) {
            addCriterion("APELLIDO =", value, "apellido");
            return (Criteria) this;
        }

        public Criteria andApellidoNotEqualTo(String value) {
            addCriterion("APELLIDO <>", value, "apellido");
            return (Criteria) this;
        }

        public Criteria andApellidoGreaterThan(String value) {
            addCriterion("APELLIDO >", value, "apellido");
            return (Criteria) this;
        }

        public Criteria andApellidoGreaterThanOrEqualTo(String value) {
            addCriterion("APELLIDO >=", value, "apellido");
            return (Criteria) this;
        }

        public Criteria andApellidoLessThan(String value) {
            addCriterion("APELLIDO <", value, "apellido");
            return (Criteria) this;
        }

        public Criteria andApellidoLessThanOrEqualTo(String value) {
            addCriterion("APELLIDO <=", value, "apellido");
            return (Criteria) this;
        }

        public Criteria andApellidoLike(String value) {
            addCriterion("APELLIDO like", value, "apellido");
            return (Criteria) this;
        }

        public Criteria andApellidoNotLike(String value) {
            addCriterion("APELLIDO not like", value, "apellido");
            return (Criteria) this;
        }

        public Criteria andApellidoIn(List<String> values) {
            addCriterion("APELLIDO in", values, "apellido");
            return (Criteria) this;
        }

        public Criteria andApellidoNotIn(List<String> values) {
            addCriterion("APELLIDO not in", values, "apellido");
            return (Criteria) this;
        }

        public Criteria andApellidoBetween(String value1, String value2) {
            addCriterion("APELLIDO between", value1, value2, "apellido");
            return (Criteria) this;
        }

        public Criteria andApellidoNotBetween(String value1, String value2) {
            addCriterion("APELLIDO not between", value1, value2, "apellido");
            return (Criteria) this;
        }

        public Criteria andEmailIsNull() {
            addCriterion("EMAIL is null");
            return (Criteria) this;
        }

        public Criteria andEmailIsNotNull() {
            addCriterion("EMAIL is not null");
            return (Criteria) this;
        }

        public Criteria andEmailEqualTo(String value) {
            addCriterion("EMAIL =", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotEqualTo(String value) {
            addCriterion("EMAIL <>", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThan(String value) {
            addCriterion("EMAIL >", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThanOrEqualTo(String value) {
            addCriterion("EMAIL >=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThan(String value) {
            addCriterion("EMAIL <", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThanOrEqualTo(String value) {
            addCriterion("EMAIL <=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLike(String value) {
            addCriterion("EMAIL like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotLike(String value) {
            addCriterion("EMAIL not like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailIn(List<String> values) {
            addCriterion("EMAIL in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotIn(List<String> values) {
            addCriterion("EMAIL not in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailBetween(String value1, String value2) {
            addCriterion("EMAIL between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotBetween(String value1, String value2) {
            addCriterion("EMAIL not between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andTelefonoIsNull() {
            addCriterion("TELEFONO is null");
            return (Criteria) this;
        }

        public Criteria andTelefonoIsNotNull() {
            addCriterion("TELEFONO is not null");
            return (Criteria) this;
        }

        public Criteria andTelefonoEqualTo(String value) {
            addCriterion("TELEFONO =", value, "telefono");
            return (Criteria) this;
        }

        public Criteria andTelefonoNotEqualTo(String value) {
            addCriterion("TELEFONO <>", value, "telefono");
            return (Criteria) this;
        }

        public Criteria andTelefonoGreaterThan(String value) {
            addCriterion("TELEFONO >", value, "telefono");
            return (Criteria) this;
        }

        public Criteria andTelefonoGreaterThanOrEqualTo(String value) {
            addCriterion("TELEFONO >=", value, "telefono");
            return (Criteria) this;
        }

        public Criteria andTelefonoLessThan(String value) {
            addCriterion("TELEFONO <", value, "telefono");
            return (Criteria) this;
        }

        public Criteria andTelefonoLessThanOrEqualTo(String value) {
            addCriterion("TELEFONO <=", value, "telefono");
            return (Criteria) this;
        }

        public Criteria andTelefonoLike(String value) {
            addCriterion("TELEFONO like", value, "telefono");
            return (Criteria) this;
        }

        public Criteria andTelefonoNotLike(String value) {
            addCriterion("TELEFONO not like", value, "telefono");
            return (Criteria) this;
        }

        public Criteria andTelefonoIn(List<String> values) {
            addCriterion("TELEFONO in", values, "telefono");
            return (Criteria) this;
        }

        public Criteria andTelefonoNotIn(List<String> values) {
            addCriterion("TELEFONO not in", values, "telefono");
            return (Criteria) this;
        }

        public Criteria andTelefonoBetween(String value1, String value2) {
            addCriterion("TELEFONO between", value1, value2, "telefono");
            return (Criteria) this;
        }

        public Criteria andTelefonoNotBetween(String value1, String value2) {
            addCriterion("TELEFONO not between", value1, value2, "telefono");
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

        public Criteria andUidReservacionLikeInsensitive(String value) {
            addCriterion("upper(UID_RESERVACION) like", value.toUpperCase(), "uidReservacion");
            return (Criteria) this;
        }

        public Criteria andNombreLikeInsensitive(String value) {
            addCriterion("upper(NOMBRE) like", value.toUpperCase(), "nombre");
            return (Criteria) this;
        }

        public Criteria andApellidoLikeInsensitive(String value) {
            addCriterion("upper(APELLIDO) like", value.toUpperCase(), "apellido");
            return (Criteria) this;
        }

        public Criteria andEmailLikeInsensitive(String value) {
            addCriterion("upper(EMAIL) like", value.toUpperCase(), "email");
            return (Criteria) this;
        }

        public Criteria andTelefonoLikeInsensitive(String value) {
            addCriterion("upper(TELEFONO) like", value.toUpperCase(), "telefono");
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