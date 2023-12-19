package com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact;

import java.util.ArrayList;
import java.util.List;

public class FacturacionTicketExample {
    public static final String ORDER_BY_UID_RESERVACION = "UID_RESERVACION";

    public static final String ORDER_BY_UID_RESERVACION_DESC = "UID_RESERVACION DESC";

    public static final String ORDER_BY_TIPO_DOCUMENTO = "TIPO_DOCUMENTO";

    public static final String ORDER_BY_TIPO_DOCUMENTO_DESC = "TIPO_DOCUMENTO DESC";

    public static final String ORDER_BY_DOCUMENTO = "DOCUMENTO";

    public static final String ORDER_BY_DOCUMENTO_DESC = "DOCUMENTO DESC";

    public static final String ORDER_BY_NOMBRE = "NOMBRE";

    public static final String ORDER_BY_NOMBRE_DESC = "NOMBRE DESC";

    public static final String ORDER_BY_APELLIDOS = "APELLIDOS";

    public static final String ORDER_BY_APELLIDOS_DESC = "APELLIDOS DESC";

    public static final String ORDER_BY_DIRECCION = "DIRECCION";

    public static final String ORDER_BY_DIRECCION_DESC = "DIRECCION DESC";

    public static final String ORDER_BY_PROVINCIA = "PROVINCIA";

    public static final String ORDER_BY_PROVINCIA_DESC = "PROVINCIA DESC";

    public static final String ORDER_BY_TELEFONO = "TELEFONO";

    public static final String ORDER_BY_TELEFONO_DESC = "TELEFONO DESC";

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public FacturacionTicketExample() {
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

        public Criteria andTipoDocumentoIsNull() {
            addCriterion("TIPO_DOCUMENTO is null");
            return (Criteria) this;
        }

        public Criteria andTipoDocumentoIsNotNull() {
            addCriterion("TIPO_DOCUMENTO is not null");
            return (Criteria) this;
        }

        public Criteria andTipoDocumentoEqualTo(String value) {
            addCriterion("TIPO_DOCUMENTO =", value, "tipoDocumento");
            return (Criteria) this;
        }

        public Criteria andTipoDocumentoNotEqualTo(String value) {
            addCriterion("TIPO_DOCUMENTO <>", value, "tipoDocumento");
            return (Criteria) this;
        }

        public Criteria andTipoDocumentoGreaterThan(String value) {
            addCriterion("TIPO_DOCUMENTO >", value, "tipoDocumento");
            return (Criteria) this;
        }

        public Criteria andTipoDocumentoGreaterThanOrEqualTo(String value) {
            addCriterion("TIPO_DOCUMENTO >=", value, "tipoDocumento");
            return (Criteria) this;
        }

        public Criteria andTipoDocumentoLessThan(String value) {
            addCriterion("TIPO_DOCUMENTO <", value, "tipoDocumento");
            return (Criteria) this;
        }

        public Criteria andTipoDocumentoLessThanOrEqualTo(String value) {
            addCriterion("TIPO_DOCUMENTO <=", value, "tipoDocumento");
            return (Criteria) this;
        }

        public Criteria andTipoDocumentoLike(String value) {
            addCriterion("TIPO_DOCUMENTO like", value, "tipoDocumento");
            return (Criteria) this;
        }

        public Criteria andTipoDocumentoNotLike(String value) {
            addCriterion("TIPO_DOCUMENTO not like", value, "tipoDocumento");
            return (Criteria) this;
        }

        public Criteria andTipoDocumentoIn(List<String> values) {
            addCriterion("TIPO_DOCUMENTO in", values, "tipoDocumento");
            return (Criteria) this;
        }

        public Criteria andTipoDocumentoNotIn(List<String> values) {
            addCriterion("TIPO_DOCUMENTO not in", values, "tipoDocumento");
            return (Criteria) this;
        }

        public Criteria andTipoDocumentoBetween(String value1, String value2) {
            addCriterion("TIPO_DOCUMENTO between", value1, value2, "tipoDocumento");
            return (Criteria) this;
        }

        public Criteria andTipoDocumentoNotBetween(String value1, String value2) {
            addCriterion("TIPO_DOCUMENTO not between", value1, value2, "tipoDocumento");
            return (Criteria) this;
        }

        public Criteria andDocumentoIsNull() {
            addCriterion("DOCUMENTO is null");
            return (Criteria) this;
        }

        public Criteria andDocumentoIsNotNull() {
            addCriterion("DOCUMENTO is not null");
            return (Criteria) this;
        }

        public Criteria andDocumentoEqualTo(String value) {
            addCriterion("DOCUMENTO =", value, "documento");
            return (Criteria) this;
        }

        public Criteria andDocumentoNotEqualTo(String value) {
            addCriterion("DOCUMENTO <>", value, "documento");
            return (Criteria) this;
        }

        public Criteria andDocumentoGreaterThan(String value) {
            addCriterion("DOCUMENTO >", value, "documento");
            return (Criteria) this;
        }

        public Criteria andDocumentoGreaterThanOrEqualTo(String value) {
            addCriterion("DOCUMENTO >=", value, "documento");
            return (Criteria) this;
        }

        public Criteria andDocumentoLessThan(String value) {
            addCriterion("DOCUMENTO <", value, "documento");
            return (Criteria) this;
        }

        public Criteria andDocumentoLessThanOrEqualTo(String value) {
            addCriterion("DOCUMENTO <=", value, "documento");
            return (Criteria) this;
        }

        public Criteria andDocumentoLike(String value) {
            addCriterion("DOCUMENTO like", value, "documento");
            return (Criteria) this;
        }

        public Criteria andDocumentoNotLike(String value) {
            addCriterion("DOCUMENTO not like", value, "documento");
            return (Criteria) this;
        }

        public Criteria andDocumentoIn(List<String> values) {
            addCriterion("DOCUMENTO in", values, "documento");
            return (Criteria) this;
        }

        public Criteria andDocumentoNotIn(List<String> values) {
            addCriterion("DOCUMENTO not in", values, "documento");
            return (Criteria) this;
        }

        public Criteria andDocumentoBetween(String value1, String value2) {
            addCriterion("DOCUMENTO between", value1, value2, "documento");
            return (Criteria) this;
        }

        public Criteria andDocumentoNotBetween(String value1, String value2) {
            addCriterion("DOCUMENTO not between", value1, value2, "documento");
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

        public Criteria andApellidosIsNull() {
            addCriterion("APELLIDOS is null");
            return (Criteria) this;
        }

        public Criteria andApellidosIsNotNull() {
            addCriterion("APELLIDOS is not null");
            return (Criteria) this;
        }

        public Criteria andApellidosEqualTo(String value) {
            addCriterion("APELLIDOS =", value, "apellidos");
            return (Criteria) this;
        }

        public Criteria andApellidosNotEqualTo(String value) {
            addCriterion("APELLIDOS <>", value, "apellidos");
            return (Criteria) this;
        }

        public Criteria andApellidosGreaterThan(String value) {
            addCriterion("APELLIDOS >", value, "apellidos");
            return (Criteria) this;
        }

        public Criteria andApellidosGreaterThanOrEqualTo(String value) {
            addCriterion("APELLIDOS >=", value, "apellidos");
            return (Criteria) this;
        }

        public Criteria andApellidosLessThan(String value) {
            addCriterion("APELLIDOS <", value, "apellidos");
            return (Criteria) this;
        }

        public Criteria andApellidosLessThanOrEqualTo(String value) {
            addCriterion("APELLIDOS <=", value, "apellidos");
            return (Criteria) this;
        }

        public Criteria andApellidosLike(String value) {
            addCriterion("APELLIDOS like", value, "apellidos");
            return (Criteria) this;
        }

        public Criteria andApellidosNotLike(String value) {
            addCriterion("APELLIDOS not like", value, "apellidos");
            return (Criteria) this;
        }

        public Criteria andApellidosIn(List<String> values) {
            addCriterion("APELLIDOS in", values, "apellidos");
            return (Criteria) this;
        }

        public Criteria andApellidosNotIn(List<String> values) {
            addCriterion("APELLIDOS not in", values, "apellidos");
            return (Criteria) this;
        }

        public Criteria andApellidosBetween(String value1, String value2) {
            addCriterion("APELLIDOS between", value1, value2, "apellidos");
            return (Criteria) this;
        }

        public Criteria andApellidosNotBetween(String value1, String value2) {
            addCriterion("APELLIDOS not between", value1, value2, "apellidos");
            return (Criteria) this;
        }

        public Criteria andDireccionIsNull() {
            addCriterion("DIRECCION is null");
            return (Criteria) this;
        }

        public Criteria andDireccionIsNotNull() {
            addCriterion("DIRECCION is not null");
            return (Criteria) this;
        }

        public Criteria andDireccionEqualTo(String value) {
            addCriterion("DIRECCION =", value, "direccion");
            return (Criteria) this;
        }

        public Criteria andDireccionNotEqualTo(String value) {
            addCriterion("DIRECCION <>", value, "direccion");
            return (Criteria) this;
        }

        public Criteria andDireccionGreaterThan(String value) {
            addCriterion("DIRECCION >", value, "direccion");
            return (Criteria) this;
        }

        public Criteria andDireccionGreaterThanOrEqualTo(String value) {
            addCriterion("DIRECCION >=", value, "direccion");
            return (Criteria) this;
        }

        public Criteria andDireccionLessThan(String value) {
            addCriterion("DIRECCION <", value, "direccion");
            return (Criteria) this;
        }

        public Criteria andDireccionLessThanOrEqualTo(String value) {
            addCriterion("DIRECCION <=", value, "direccion");
            return (Criteria) this;
        }

        public Criteria andDireccionLike(String value) {
            addCriterion("DIRECCION like", value, "direccion");
            return (Criteria) this;
        }

        public Criteria andDireccionNotLike(String value) {
            addCriterion("DIRECCION not like", value, "direccion");
            return (Criteria) this;
        }

        public Criteria andDireccionIn(List<String> values) {
            addCriterion("DIRECCION in", values, "direccion");
            return (Criteria) this;
        }

        public Criteria andDireccionNotIn(List<String> values) {
            addCriterion("DIRECCION not in", values, "direccion");
            return (Criteria) this;
        }

        public Criteria andDireccionBetween(String value1, String value2) {
            addCriterion("DIRECCION between", value1, value2, "direccion");
            return (Criteria) this;
        }

        public Criteria andDireccionNotBetween(String value1, String value2) {
            addCriterion("DIRECCION not between", value1, value2, "direccion");
            return (Criteria) this;
        }

        public Criteria andProvinciaIsNull() {
            addCriterion("PROVINCIA is null");
            return (Criteria) this;
        }

        public Criteria andProvinciaIsNotNull() {
            addCriterion("PROVINCIA is not null");
            return (Criteria) this;
        }

        public Criteria andProvinciaEqualTo(String value) {
            addCriterion("PROVINCIA =", value, "provincia");
            return (Criteria) this;
        }

        public Criteria andProvinciaNotEqualTo(String value) {
            addCriterion("PROVINCIA <>", value, "provincia");
            return (Criteria) this;
        }

        public Criteria andProvinciaGreaterThan(String value) {
            addCriterion("PROVINCIA >", value, "provincia");
            return (Criteria) this;
        }

        public Criteria andProvinciaGreaterThanOrEqualTo(String value) {
            addCriterion("PROVINCIA >=", value, "provincia");
            return (Criteria) this;
        }

        public Criteria andProvinciaLessThan(String value) {
            addCriterion("PROVINCIA <", value, "provincia");
            return (Criteria) this;
        }

        public Criteria andProvinciaLessThanOrEqualTo(String value) {
            addCriterion("PROVINCIA <=", value, "provincia");
            return (Criteria) this;
        }

        public Criteria andProvinciaLike(String value) {
            addCriterion("PROVINCIA like", value, "provincia");
            return (Criteria) this;
        }

        public Criteria andProvinciaNotLike(String value) {
            addCriterion("PROVINCIA not like", value, "provincia");
            return (Criteria) this;
        }

        public Criteria andProvinciaIn(List<String> values) {
            addCriterion("PROVINCIA in", values, "provincia");
            return (Criteria) this;
        }

        public Criteria andProvinciaNotIn(List<String> values) {
            addCriterion("PROVINCIA not in", values, "provincia");
            return (Criteria) this;
        }

        public Criteria andProvinciaBetween(String value1, String value2) {
            addCriterion("PROVINCIA between", value1, value2, "provincia");
            return (Criteria) this;
        }

        public Criteria andProvinciaNotBetween(String value1, String value2) {
            addCriterion("PROVINCIA not between", value1, value2, "provincia");
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

        public Criteria andUidReservacionLikeInsensitive(String value) {
            addCriterion("upper(UID_RESERVACION) like", value.toUpperCase(), "uidReservacion");
            return (Criteria) this;
        }

        public Criteria andTipoDocumentoLikeInsensitive(String value) {
            addCriterion("upper(TIPO_DOCUMENTO) like", value.toUpperCase(), "tipoDocumento");
            return (Criteria) this;
        }

        public Criteria andDocumentoLikeInsensitive(String value) {
            addCriterion("upper(DOCUMENTO) like", value.toUpperCase(), "documento");
            return (Criteria) this;
        }

        public Criteria andNombreLikeInsensitive(String value) {
            addCriterion("upper(NOMBRE) like", value.toUpperCase(), "nombre");
            return (Criteria) this;
        }

        public Criteria andApellidosLikeInsensitive(String value) {
            addCriterion("upper(APELLIDOS) like", value.toUpperCase(), "apellidos");
            return (Criteria) this;
        }

        public Criteria andDireccionLikeInsensitive(String value) {
            addCriterion("upper(DIRECCION) like", value.toUpperCase(), "direccion");
            return (Criteria) this;
        }

        public Criteria andProvinciaLikeInsensitive(String value) {
            addCriterion("upper(PROVINCIA) like", value.toUpperCase(), "provincia");
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