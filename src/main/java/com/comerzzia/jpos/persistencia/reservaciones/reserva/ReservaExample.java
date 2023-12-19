package com.comerzzia.jpos.persistencia.reservaciones.reserva;

import com.comerzzia.jpos.entity.db.Cliente;
import com.comerzzia.jpos.persistencia.reservaciones.reservatipos.ReservaTiposBean;
import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReservaExample {
    public static final String ORDER_BY_UID_RESERVACION = "UID_RESERVACION";

    public static final String ORDER_BY_UID_RESERVACION_DESC = "UID_RESERVACION DESC";

    public static final String ORDER_BY_CODCLI = "CODCLI";
    
    public static final String ORDER_BY_TIPODOCU = "TIPODOCU";

    public static final String ORDER_BY_CODCLI_DESC = "CODCLI DESC";

    public static final String ORDER_BY_COD_TIPO = "COD_TIPO";

    public static final String ORDER_BY_COD_TIPO_DESC = "COD_TIPO DESC";

    public static final String ORDER_BY_CADUCIDAD = "CADUCIDAD";

    public static final String ORDER_BY_CADUCIDAD_DESC = "CADUCIDAD DESC";

    public static final String ORDER_BY_FECHA_ALTA = "FECHA_ALTA";

    public static final String ORDER_BY_FECHA_ALTA_DESC = "FECHA_ALTA DESC";

    public static final String ORDER_BY_LIQUIDADO = "LIQUIDADO";

    public static final String ORDER_BY_LIQUIDADO_DESC = "LIQUIDADO DESC";

    public static final String ORDER_BY_CODALM = "CODALM";

    public static final String ORDER_BY_CODALM_DESC = "CODALM DESC";

    public static final String ORDER_BY_CANCELADO = "CANCELADO";

    public static final String ORDER_BY_CANCELADO_DESC = "CANCELADO DESC";

    public static final String ORDER_BY_PROCESADO = "PROCESADO";

    public static final String ORDER_BY_PROCESADO_DESC = "PROCESADO DESC";

    public static final String ORDER_BY_CODCAJA = "CODCAJA";

    public static final String ORDER_BY_CODCAJA_DESC = "CODCAJA DESC";

    public static final String ORDER_BY_ID_USUARIO = "ID_USUARIO";

    public static final String ORDER_BY_ID_USUARIO_DESC = "ID_USUARIO DESC";

    public static final String ORDER_BY_CUOTA_INICIAL = "CUOTA_INICIAL";

    public static final String ORDER_BY_CUOTA_INICIAL_DESC = "CUOTA_INICIAL DESC";

    public static final String ORDER_BY_FECHA_LIQUIDACION = "FECHA_LIQUIDACION";

    public static final String ORDER_BY_FECHA_LIQUIDACION_DESC = "FECHA_LIQUIDACION DESC";

    public static final String ORDER_BY_CODVENDEDOR = "CODVENDEDOR";

    public static final String ORDER_BY_CODVENDEDOR_DESC = "CODVENDEDOR DESC";

    public static final String ORDER_BY_PROCESADO_TIENDA = "PROCESADO_TIENDA";

    public static final String ORDER_BY_PROCESADO_TIENDA_DESC = "PROCESADO_TIENDA DESC";

    public static final String ORDER_BY_COD_RESERVACION = "COD_RESERVACION";

    public static final String ORDER_BY_COD_RESERVACION_DESC = "COD_RESERVACION DESC";

    public static final String ORDER_BY_NOMBRE_ORGANIZADORA = "NOMBRE_ORGANIZADORA";

    public static final String ORDER_BY_NOMBRE_ORGANIZADORA_DESC = "NOMBRE_ORGANIZADORA DESC";

    public static final String ORDER_BY_APELLIDOS_ORGANIZADORA = "APELLIDOS_ORGANIZADORA";

    public static final String ORDER_BY_APELLIDOS_ORGANIZADORA_DESC = "APELLIDOS_ORGANIZADORA DESC";

    public static final String ORDER_BY_TELEFONO_ORGANIZADORA = "TELEFONO_ORGANIZADORA";

    public static final String ORDER_BY_TELEFONO_ORGANIZADORA_DESC = "TELEFONO_ORGANIZADORA DESC";

    public static final String ORDER_BY_DIRECCION_EVENTO = "DIRECCION_EVENTO";

    public static final String ORDER_BY_DIRECCION_EVENTO_DESC = "DIRECCION_EVENTO DESC";

    public static final String ORDER_BY_FECHA_HORA_EVENTO = "FECHA_HORA_EVENTO";

    public static final String ORDER_BY_FECHA_HORA_EVENTO_DESC = "FECHA_HORA_EVENTO DESC";

    public static final String ORDER_BY_RESERVACION_FACT = "RESERVACION_FACT";

    public static final String ORDER_BY_RESERVACION_FACT_DESC = "RESERVACION_FACT DESC";

    public static final String ORDER_BY_NOMBRE_ORG = "NOMBRE_ORG";

    public static final String ORDER_BY_NOMBRE_ORG_DESC = "NOMBRE_ORG DESC";

    public static final String ORDER_BY_APELLIDOS_ORG = "APELLIDOS_ORG";

    public static final String ORDER_BY_APELLIDOS_ORG_DESC = "APELLIDOS_ORG DESC";

    public static final String ORDER_BY_CODCLIENTE = "CODCLIENTE";

    public static final String ORDER_BY_CODCLIENTE_DESC = "CODCLIENTE DESC";

    public static final String ORDER_BY_NOMBRECLIENTE = "NOMBRECLIENTE";

    public static final String ORDER_BY_NOMBRECLIENTE_DESC = "NOMBRECLIENTE DESC";

    public static final String ORDER_BY_APELLIDOSCLIENTE = "APELLIDOSCLIENTE";

    public static final String ORDER_BY_APELLIDOSCLIENTE_DESC = "APELLIDOSCLIENTE DESC";

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ReservaExample() {
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
        protected List<Criterion> caducidadCriteria;

        protected List<Criterion> fechaAltaCriteria;

        protected List<Criterion> liquidadoCriteria;
        
        protected List<Criterion> bonoCriteria;

        protected List<Criterion> canceladoCriteria;

        protected List<Criterion> procesadoCriteria;

        protected List<Criterion> fechaLiquidacionCriteria;

        protected List<Criterion> procesadoTiendaCriteria;

        protected List<Criterion> fechaHoraEventoCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
            caducidadCriteria = new ArrayList<Criterion>();
            fechaAltaCriteria = new ArrayList<Criterion>();
            liquidadoCriteria = new ArrayList<Criterion>();
            bonoCriteria = new ArrayList<Criterion>();
            canceladoCriteria = new ArrayList<Criterion>();
            procesadoCriteria = new ArrayList<Criterion>();
            fechaLiquidacionCriteria = new ArrayList<Criterion>();
            procesadoTiendaCriteria = new ArrayList<Criterion>();
            fechaHoraEventoCriteria = new ArrayList<Criterion>();
        }

        public List<Criterion> getCaducidadCriteria() {
            return caducidadCriteria;
        }

        protected void addCaducidadCriterion(String condition, Object value, String property) {
            if (value != null) {
                caducidadCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addCaducidadCriterion(String condition, Fecha value1, Fecha value2, String property) {
            if (value1 != null && value2 != null) {
                caducidadCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
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

        public List<Criterion> getLiquidadoCriteria() {
            return liquidadoCriteria;
        }
        
        public List<Criterion> getBonoCriteria() {
            return bonoCriteria;
        }        

        protected void addLiquidadoCriterion(String condition, Object value, String property) {
            if (value != null) {
                liquidadoCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addLiquidadoCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                liquidadoCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }
        
        protected void addBonoCriterion(String condition, Object value, String property) {
            if (value != null) {
                bonoCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addBonoCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                bonoCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getCanceladoCriteria() {
            return canceladoCriteria;
        }

        protected void addCanceladoCriterion(String condition, Object value, String property) {
            if (value != null) {
                canceladoCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addCanceladoCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                canceladoCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
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

        public List<Criterion> getFechaLiquidacionCriteria() {
            return fechaLiquidacionCriteria;
        }

        protected void addFechaLiquidacionCriterion(String condition, Object value, String property) {
            if (value != null) {
                fechaLiquidacionCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addFechaLiquidacionCriterion(String condition, Fecha value1, Fecha value2, String property) {
            if (value1 != null && value2 != null) {
                fechaLiquidacionCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
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

        public List<Criterion> getFechaHoraEventoCriteria() {
            return fechaHoraEventoCriteria;
        }

        protected void addFechaHoraEventoCriterion(String condition, Object value, String property) {
            if (value != null) {
                fechaHoraEventoCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addFechaHoraEventoCriterion(String condition, Fecha value1, Fecha value2, String property) {
            if (value1 != null && value2 != null) {
                fechaHoraEventoCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler"));
                allCriteria = null;
            }
        }

        public boolean isValid() {
            return criteria.size() > 0
                || caducidadCriteria.size() > 0
                || fechaAltaCriteria.size() > 0
                || liquidadoCriteria.size() > 0
                || canceladoCriteria.size() > 0
                || procesadoCriteria.size() > 0
                || fechaLiquidacionCriteria.size() > 0
                || procesadoTiendaCriteria.size() > 0
                || fechaHoraEventoCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<Criterion>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(caducidadCriteria);
                allCriteria.addAll(fechaAltaCriteria);
                allCriteria.addAll(liquidadoCriteria);
                allCriteria.addAll(canceladoCriteria);
                allCriteria.addAll(procesadoCriteria);
                allCriteria.addAll(fechaLiquidacionCriteria);
                allCriteria.addAll(procesadoTiendaCriteria);
                allCriteria.addAll(fechaHoraEventoCriteria);
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

        public Criteria andTipoDocuIsNull() {
            addCriterion("TIPODOCU is null");
            return (Criteria) this;
        }

        public Criteria andTipoDocuIsNotNull() {
            addCriterion("TIPODOCU is not null");
            return (Criteria) this;
        }

        public Criteria andTipoDocuEqualTo(String value) {
            addCriterion("TIPODOCU =", value, "tipodocu");
            return (Criteria) this;
        }

        public Criteria andTipoDocuNotEqualTo(String value) {
            addCriterion("TIPODOCU <>", value, "tipodocu");
            return (Criteria) this;
        }

        public Criteria andTipoDocuGreaterThan(String value) {
            addCriterion("TIPODOCU >", value, "tipodocu");
            return (Criteria) this;
        }

        public Criteria andTipoDocuGreaterThanOrEqualTo(String value) {
            addCriterion("TIPODOCU >=", value, "tipodocu");
            return (Criteria) this;
        }

        public Criteria andTipoDocuLessThan(String value) {
            addCriterion("TIPODOCU <", value, "tipodocu");
            return (Criteria) this;
        }

        public Criteria andTipoDocuLessThanOrEqualTo(String value) {
            addCriterion("TIPODOCU <=", value, "tipodocu");
            return (Criteria) this;
        }

        public Criteria andTipoDocuLike(String value) {
            addCriterion("TIPODOCU like", value, "tipodocu");
            return (Criteria) this;
        }

        public Criteria andTipoDocuNotLike(String value) {
            addCriterion("TIPODOCU not like", value, "tipodocu");
            return (Criteria) this;
        }

        public Criteria andTipoDocuIn(List<String> values) {
            addCriterion("TIPODOCU in", values, "tipodocu");
            return (Criteria) this;
        }

        public Criteria andTipoDocuNotIn(List<String> values) {
            addCriterion("TIPODOCU not in", values, "tipodocu");
            return (Criteria) this;
        }

        public Criteria andTipoDocuBetween(String value1, String value2) {
            addCriterion("TIPODOCU between", value1, value2, "tipodocu");
            return (Criteria) this;
        }

        public Criteria andTipoDocuNotBetween(String value1, String value2) {
            addCriterion("TIPODOCU not between", value1, value2, "tipodocu");
            return (Criteria) this;
        }
        
        public Criteria andCodTipoIsNull() {
            addCriterion("COD_TIPO is null");
            return (Criteria) this;
        }

        public Criteria andCodTipoIsNotNull() {
            addCriterion("COD_TIPO is not null");
            return (Criteria) this;
        }

        public Criteria andCodTipoEqualTo(String value) {
            addCriterion("COD_TIPO =", value, "codTipo");
            return (Criteria) this;
        }

        public Criteria andCodTipoNotEqualTo(String value) {
            addCriterion("COD_TIPO <>", value, "codTipo");
            return (Criteria) this;
        }

        public Criteria andCodTipoGreaterThan(String value) {
            addCriterion("COD_TIPO >", value, "codTipo");
            return (Criteria) this;
        }

        public Criteria andCodTipoGreaterThanOrEqualTo(String value) {
            addCriterion("COD_TIPO >=", value, "codTipo");
            return (Criteria) this;
        }

        public Criteria andCodTipoLessThan(String value) {
            addCriterion("COD_TIPO <", value, "codTipo");
            return (Criteria) this;
        }

        public Criteria andCodTipoLessThanOrEqualTo(String value) {
            addCriterion("COD_TIPO <=", value, "codTipo");
            return (Criteria) this;
        }

        public Criteria andCodTipoLike(String value) {
            addCriterion("COD_TIPO like", value, "codTipo");
            return (Criteria) this;
        }

        public Criteria andCodTipoNotLike(String value) {
            addCriterion("COD_TIPO not like", value, "codTipo");
            return (Criteria) this;
        }

        public Criteria andCodTipoIn(List<String> values) {
            addCriterion("COD_TIPO in", values, "codTipo");
            return (Criteria) this;
        }

        public Criteria andCodTipoNotIn(List<String> values) {
            addCriterion("COD_TIPO not in", values, "codTipo");
            return (Criteria) this;
        }

        public Criteria andCodTipoBetween(String value1, String value2) {
            addCriterion("COD_TIPO between", value1, value2, "codTipo");
            return (Criteria) this;
        }

        public Criteria andCodTipoNotBetween(String value1, String value2) {
            addCriterion("COD_TIPO not between", value1, value2, "codTipo");
            return (Criteria) this;
        }

        public Criteria andCaducidadIsNull() {
            addCriterion("CADUCIDAD is null");
            return (Criteria) this;
        }

        public Criteria andCaducidadIsNotNull() {
            addCriterion("CADUCIDAD is not null");
            return (Criteria) this;
        }

        public Criteria andCaducidadEqualTo(Fecha value) {
            addCaducidadCriterion("CADUCIDAD =", value, "caducidad");
            return (Criteria) this;
        }

        public Criteria andCaducidadNotEqualTo(Fecha value) {
            addCaducidadCriterion("CADUCIDAD <>", value, "caducidad");
            return (Criteria) this;
        }

        public Criteria andCaducidadGreaterThan(Fecha value) {
            addCaducidadCriterion("CADUCIDAD >", value, "caducidad");
            return (Criteria) this;
        }

        public Criteria andCaducidadGreaterThanOrEqualTo(Fecha value) {
            addCaducidadCriterion("CADUCIDAD >=", value, "caducidad");
            return (Criteria) this;
        }

        public Criteria andCaducidadLessThan(Fecha value) {
            addCaducidadCriterion("CADUCIDAD <", value, "caducidad");
            return (Criteria) this;
        }

        public Criteria andCaducidadLessThanOrEqualTo(Fecha value) {
            addCaducidadCriterion("CADUCIDAD <=", value, "caducidad");
            return (Criteria) this;
        }

        public Criteria andCaducidadIn(List<Fecha> values) {
            addCaducidadCriterion("CADUCIDAD in", values, "caducidad");
            return (Criteria) this;
        }

        public Criteria andCaducidadNotIn(List<Fecha> values) {
            addCaducidadCriterion("CADUCIDAD not in", values, "caducidad");
            return (Criteria) this;
        }

        public Criteria andCaducidadBetween(Fecha value1, Fecha value2) {
            addCaducidadCriterion("CADUCIDAD between", value1, value2, "caducidad");
            return (Criteria) this;
        }

        public Criteria andCaducidadNotBetween(Fecha value1, Fecha value2) {
            addCaducidadCriterion("CADUCIDAD not between", value1, value2, "caducidad");
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

        public Criteria andLiquidadoIsNull() {
            addCriterion("LIQUIDADO is null");
            return (Criteria) this;
        }

        public Criteria andLiquidadoIsNotNull() {
            addCriterion("LIQUIDADO is not null");
            return (Criteria) this;
        }

        public Criteria andLiquidadoEqualTo(Boolean value) {
            addLiquidadoCriterion("LIQUIDADO =", value, "liquidado");
            return (Criteria) this;
        }

        public Criteria andLiquidadoNotEqualTo(Boolean value) {
            addLiquidadoCriterion("LIQUIDADO <>", value, "liquidado");
            return (Criteria) this;
        }

        public Criteria andLiquidadoGreaterThan(Boolean value) {
            addLiquidadoCriterion("LIQUIDADO >", value, "liquidado");
            return (Criteria) this;
        }

        public Criteria andLiquidadoGreaterThanOrEqualTo(Boolean value) {
            addLiquidadoCriterion("LIQUIDADO >=", value, "liquidado");
            return (Criteria) this;
        }

        public Criteria andLiquidadoLessThan(Boolean value) {
            addLiquidadoCriterion("LIQUIDADO <", value, "liquidado");
            return (Criteria) this;
        }

        public Criteria andLiquidadoLessThanOrEqualTo(Boolean value) {
            addLiquidadoCriterion("LIQUIDADO <=", value, "liquidado");
            return (Criteria) this;
        }

        public Criteria andLiquidadoLike(Boolean value) {
            addLiquidadoCriterion("LIQUIDADO like", value, "liquidado");
            return (Criteria) this;
        }

        public Criteria andLiquidadoNotLike(Boolean value) {
            addLiquidadoCriterion("LIQUIDADO not like", value, "liquidado");
            return (Criteria) this;
        }

        public Criteria andLiquidadoIn(List<Boolean> values) {
            addLiquidadoCriterion("LIQUIDADO in", values, "liquidado");
            return (Criteria) this;
        }

        public Criteria andLiquidadoNotIn(List<Boolean> values) {
            addLiquidadoCriterion("LIQUIDADO not in", values, "liquidado");
            return (Criteria) this;
        }

        public Criteria andLiquidadoBetween(Boolean value1, Boolean value2) {
            addLiquidadoCriterion("LIQUIDADO between", value1, value2, "liquidado");
            return (Criteria) this;
        }

        public Criteria andLiquidadoNotBetween(Boolean value1, Boolean value2) {
            addLiquidadoCriterion("LIQUIDADO not between", value1, value2, "liquidado");
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

        public Criteria andCanceladoIsNull() {
            addCriterion("CANCELADO is null");
            return (Criteria) this;
        }

        public Criteria andCanceladoIsNotNull() {
            addCriterion("CANCELADO is not null");
            return (Criteria) this;
        }

        public Criteria andCanceladoEqualTo(Boolean value) {
            addCanceladoCriterion("CANCELADO =", value, "cancelado");
            return (Criteria) this;
        }

        public Criteria andCanceladoNotEqualTo(Boolean value) {
            addCanceladoCriterion("CANCELADO <>", value, "cancelado");
            return (Criteria) this;
        }

        public Criteria andCanceladoGreaterThan(Boolean value) {
            addCanceladoCriterion("CANCELADO >", value, "cancelado");
            return (Criteria) this;
        }

        public Criteria andCanceladoGreaterThanOrEqualTo(Boolean value) {
            addCanceladoCriterion("CANCELADO >=", value, "cancelado");
            return (Criteria) this;
        }

        public Criteria andCanceladoLessThan(Boolean value) {
            addCanceladoCriterion("CANCELADO <", value, "cancelado");
            return (Criteria) this;
        }

        public Criteria andCanceladoLessThanOrEqualTo(Boolean value) {
            addCanceladoCriterion("CANCELADO <=", value, "cancelado");
            return (Criteria) this;
        }

        public Criteria andCanceladoLike(Boolean value) {
            addCanceladoCriterion("CANCELADO like", value, "cancelado");
            return (Criteria) this;
        }

        public Criteria andCanceladoNotLike(Boolean value) {
            addCanceladoCriterion("CANCELADO not like", value, "cancelado");
            return (Criteria) this;
        }

        public Criteria andCanceladoIn(List<Boolean> values) {
            addCanceladoCriterion("CANCELADO in", values, "cancelado");
            return (Criteria) this;
        }

        public Criteria andCanceladoNotIn(List<Boolean> values) {
            addCanceladoCriterion("CANCELADO not in", values, "cancelado");
            return (Criteria) this;
        }

        public Criteria andCanceladoBetween(Boolean value1, Boolean value2) {
            addCanceladoCriterion("CANCELADO between", value1, value2, "cancelado");
            return (Criteria) this;
        }

        public Criteria andCanceladoNotBetween(Boolean value1, Boolean value2) {
            addCanceladoCriterion("CANCELADO not between", value1, value2, "cancelado");
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

        public Criteria andIdUsuarioIsNull() {
            addCriterion("ID_USUARIO is null");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioIsNotNull() {
            addCriterion("ID_USUARIO is not null");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioEqualTo(BigDecimal value) {
            addCriterion("ID_USUARIO =", value, "idUsuario");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioNotEqualTo(BigDecimal value) {
            addCriterion("ID_USUARIO <>", value, "idUsuario");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioGreaterThan(BigDecimal value) {
            addCriterion("ID_USUARIO >", value, "idUsuario");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ID_USUARIO >=", value, "idUsuario");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioLessThan(BigDecimal value) {
            addCriterion("ID_USUARIO <", value, "idUsuario");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ID_USUARIO <=", value, "idUsuario");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioIn(List<BigDecimal> values) {
            addCriterion("ID_USUARIO in", values, "idUsuario");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioNotIn(List<BigDecimal> values) {
            addCriterion("ID_USUARIO not in", values, "idUsuario");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ID_USUARIO between", value1, value2, "idUsuario");
            return (Criteria) this;
        }

        public Criteria andIdUsuarioNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ID_USUARIO not between", value1, value2, "idUsuario");
            return (Criteria) this;
        }

        public Criteria andCuotaInicialIsNull() {
            addCriterion("CUOTA_INICIAL is null");
            return (Criteria) this;
        }

        public Criteria andCuotaInicialIsNotNull() {
            addCriterion("CUOTA_INICIAL is not null");
            return (Criteria) this;
        }

        public Criteria andCuotaInicialEqualTo(BigDecimal value) {
            addCriterion("CUOTA_INICIAL =", value, "cuotaInicial");
            return (Criteria) this;
        }

        public Criteria andCuotaInicialNotEqualTo(BigDecimal value) {
            addCriterion("CUOTA_INICIAL <>", value, "cuotaInicial");
            return (Criteria) this;
        }

        public Criteria andCuotaInicialGreaterThan(BigDecimal value) {
            addCriterion("CUOTA_INICIAL >", value, "cuotaInicial");
            return (Criteria) this;
        }

        public Criteria andCuotaInicialGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("CUOTA_INICIAL >=", value, "cuotaInicial");
            return (Criteria) this;
        }

        public Criteria andCuotaInicialLessThan(BigDecimal value) {
            addCriterion("CUOTA_INICIAL <", value, "cuotaInicial");
            return (Criteria) this;
        }

        public Criteria andCuotaInicialLessThanOrEqualTo(BigDecimal value) {
            addCriterion("CUOTA_INICIAL <=", value, "cuotaInicial");
            return (Criteria) this;
        }

        public Criteria andCuotaInicialIn(List<BigDecimal> values) {
            addCriterion("CUOTA_INICIAL in", values, "cuotaInicial");
            return (Criteria) this;
        }

        public Criteria andCuotaInicialNotIn(List<BigDecimal> values) {
            addCriterion("CUOTA_INICIAL not in", values, "cuotaInicial");
            return (Criteria) this;
        }

        public Criteria andCuotaInicialBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("CUOTA_INICIAL between", value1, value2, "cuotaInicial");
            return (Criteria) this;
        }

        public Criteria andCuotaInicialNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("CUOTA_INICIAL not between", value1, value2, "cuotaInicial");
            return (Criteria) this;
        }

        public Criteria andFechaLiquidacionIsNull() {
            addCriterion("FECHA_LIQUIDACION is null");
            return (Criteria) this;
        }

        public Criteria andFechaLiquidacionIsNotNull() {
            addCriterion("FECHA_LIQUIDACION is not null");
            return (Criteria) this;
        }

        public Criteria andFechaLiquidacionEqualTo(Fecha value) {
            addFechaLiquidacionCriterion("FECHA_LIQUIDACION =", value, "fechaLiquidacion");
            return (Criteria) this;
        }

        public Criteria andFechaLiquidacionNotEqualTo(Fecha value) {
            addFechaLiquidacionCriterion("FECHA_LIQUIDACION <>", value, "fechaLiquidacion");
            return (Criteria) this;
        }

        public Criteria andFechaLiquidacionGreaterThan(Fecha value) {
            addFechaLiquidacionCriterion("FECHA_LIQUIDACION >", value, "fechaLiquidacion");
            return (Criteria) this;
        }

        public Criteria andFechaLiquidacionGreaterThanOrEqualTo(Fecha value) {
            addFechaLiquidacionCriterion("FECHA_LIQUIDACION >=", value, "fechaLiquidacion");
            return (Criteria) this;
        }

        public Criteria andFechaLiquidacionLessThan(Fecha value) {
            addFechaLiquidacionCriterion("FECHA_LIQUIDACION <", value, "fechaLiquidacion");
            return (Criteria) this;
        }

        public Criteria andFechaLiquidacionLessThanOrEqualTo(Fecha value) {
            addFechaLiquidacionCriterion("FECHA_LIQUIDACION <=", value, "fechaLiquidacion");
            return (Criteria) this;
        }

        public Criteria andFechaLiquidacionIn(List<Fecha> values) {
            addFechaLiquidacionCriterion("FECHA_LIQUIDACION in", values, "fechaLiquidacion");
            return (Criteria) this;
        }

        public Criteria andFechaLiquidacionNotIn(List<Fecha> values) {
            addFechaLiquidacionCriterion("FECHA_LIQUIDACION not in", values, "fechaLiquidacion");
            return (Criteria) this;
        }

        public Criteria andFechaLiquidacionBetween(Fecha value1, Fecha value2) {
            addFechaLiquidacionCriterion("FECHA_LIQUIDACION between", value1, value2, "fechaLiquidacion");
            return (Criteria) this;
        }

        public Criteria andFechaLiquidacionNotBetween(Fecha value1, Fecha value2) {
            addFechaLiquidacionCriterion("FECHA_LIQUIDACION not between", value1, value2, "fechaLiquidacion");
            return (Criteria) this;
        }

        public Criteria andCodvendedorIsNull() {
            addCriterion("CODVENDEDOR is null");
            return (Criteria) this;
        }

        public Criteria andCodvendedorIsNotNull() {
            addCriterion("CODVENDEDOR is not null");
            return (Criteria) this;
        }

        public Criteria andCodvendedorEqualTo(String value) {
            addCriterion("CODVENDEDOR =", value, "codvendedor");
            return (Criteria) this;
        }

        public Criteria andCodvendedorNotEqualTo(String value) {
            addCriterion("CODVENDEDOR <>", value, "codvendedor");
            return (Criteria) this;
        }

        public Criteria andCodvendedorGreaterThan(String value) {
            addCriterion("CODVENDEDOR >", value, "codvendedor");
            return (Criteria) this;
        }

        public Criteria andCodvendedorGreaterThanOrEqualTo(String value) {
            addCriterion("CODVENDEDOR >=", value, "codvendedor");
            return (Criteria) this;
        }

        public Criteria andCodvendedorLessThan(String value) {
            addCriterion("CODVENDEDOR <", value, "codvendedor");
            return (Criteria) this;
        }

        public Criteria andCodvendedorLessThanOrEqualTo(String value) {
            addCriterion("CODVENDEDOR <=", value, "codvendedor");
            return (Criteria) this;
        }

        public Criteria andCodvendedorLike(String value) {
            addCriterion("CODVENDEDOR like", value, "codvendedor");
            return (Criteria) this;
        }

        public Criteria andCodvendedorNotLike(String value) {
            addCriterion("CODVENDEDOR not like", value, "codvendedor");
            return (Criteria) this;
        }

        public Criteria andCodvendedorIn(List<String> values) {
            addCriterion("CODVENDEDOR in", values, "codvendedor");
            return (Criteria) this;
        }

        public Criteria andCodvendedorNotIn(List<String> values) {
            addCriterion("CODVENDEDOR not in", values, "codvendedor");
            return (Criteria) this;
        }

        public Criteria andCodvendedorBetween(String value1, String value2) {
            addCriterion("CODVENDEDOR between", value1, value2, "codvendedor");
            return (Criteria) this;
        }

        public Criteria andCodvendedorNotBetween(String value1, String value2) {
            addCriterion("CODVENDEDOR not between", value1, value2, "codvendedor");
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

        public Criteria andCodReservacionIsNull() {
            addCriterion("COD_RESERVACION is null");
            return (Criteria) this;
        }

        public Criteria andCodReservacionIsNotNull() {
            addCriterion("COD_RESERVACION is not null");
            return (Criteria) this;
        }

        public Criteria andCodReservacionEqualTo(BigDecimal value) {
            addCriterion("COD_RESERVACION =", value, "codReservacion");
            return (Criteria) this;
        }

        public Criteria andCodReservacionNotEqualTo(BigDecimal value) {
            addCriterion("COD_RESERVACION <>", value, "codReservacion");
            return (Criteria) this;
        }

        public Criteria andCodReservacionGreaterThan(BigDecimal value) {
            addCriterion("COD_RESERVACION >", value, "codReservacion");
            return (Criteria) this;
        }

        public Criteria andCodReservacionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("COD_RESERVACION >=", value, "codReservacion");
            return (Criteria) this;
        }

        public Criteria andCodReservacionLessThan(BigDecimal value) {
            addCriterion("COD_RESERVACION <", value, "codReservacion");
            return (Criteria) this;
        }

        public Criteria andCodReservacionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("COD_RESERVACION <=", value, "codReservacion");
            return (Criteria) this;
        }

        public Criteria andCodReservacionIn(List<BigDecimal> values) {
            addCriterion("COD_RESERVACION in", values, "codReservacion");
            return (Criteria) this;
        }

        public Criteria andCodReservacionNotIn(List<BigDecimal> values) {
            addCriterion("COD_RESERVACION not in", values, "codReservacion");
            return (Criteria) this;
        }

        public Criteria andCodReservacionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("COD_RESERVACION between", value1, value2, "codReservacion");
            return (Criteria) this;
        }

        public Criteria andCodReservacionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("COD_RESERVACION not between", value1, value2, "codReservacion");
            return (Criteria) this;
        }

        public Criteria andNombreOrganizadoraIsNull() {
            addCriterion("NOMBRE_ORGANIZADORA is null");
            return (Criteria) this;
        }

        public Criteria andNombreOrganizadoraIsNotNull() {
            addCriterion("NOMBRE_ORGANIZADORA is not null");
            return (Criteria) this;
        }

        public Criteria andNombreOrganizadoraEqualTo(String value) {
            addCriterion("NOMBRE_ORGANIZADORA =", value, "nombreOrganizadora");
            return (Criteria) this;
        }

        public Criteria andNombreOrganizadoraNotEqualTo(String value) {
            addCriterion("NOMBRE_ORGANIZADORA <>", value, "nombreOrganizadora");
            return (Criteria) this;
        }

        public Criteria andNombreOrganizadoraGreaterThan(String value) {
            addCriterion("NOMBRE_ORGANIZADORA >", value, "nombreOrganizadora");
            return (Criteria) this;
        }

        public Criteria andNombreOrganizadoraGreaterThanOrEqualTo(String value) {
            addCriterion("NOMBRE_ORGANIZADORA >=", value, "nombreOrganizadora");
            return (Criteria) this;
        }

        public Criteria andNombreOrganizadoraLessThan(String value) {
            addCriterion("NOMBRE_ORGANIZADORA <", value, "nombreOrganizadora");
            return (Criteria) this;
        }

        public Criteria andNombreOrganizadoraLessThanOrEqualTo(String value) {
            addCriterion("NOMBRE_ORGANIZADORA <=", value, "nombreOrganizadora");
            return (Criteria) this;
        }

        public Criteria andNombreOrganizadoraLike(String value) {
            addCriterion("NOMBRE_ORGANIZADORA like", value, "nombreOrganizadora");
            return (Criteria) this;
        }

        public Criteria andNombreOrganizadoraNotLike(String value) {
            addCriterion("NOMBRE_ORGANIZADORA not like", value, "nombreOrganizadora");
            return (Criteria) this;
        }

        public Criteria andNombreOrganizadoraIn(List<String> values) {
            addCriterion("NOMBRE_ORGANIZADORA in", values, "nombreOrganizadora");
            return (Criteria) this;
        }

        public Criteria andNombreOrganizadoraNotIn(List<String> values) {
            addCriterion("NOMBRE_ORGANIZADORA not in", values, "nombreOrganizadora");
            return (Criteria) this;
        }

        public Criteria andNombreOrganizadoraBetween(String value1, String value2) {
            addCriterion("NOMBRE_ORGANIZADORA between", value1, value2, "nombreOrganizadora");
            return (Criteria) this;
        }

        public Criteria andNombreOrganizadoraNotBetween(String value1, String value2) {
            addCriterion("NOMBRE_ORGANIZADORA not between", value1, value2, "nombreOrganizadora");
            return (Criteria) this;
        }

        public Criteria andApellidosOrganizadoraIsNull() {
            addCriterion("APELLIDOS_ORGANIZADORA is null");
            return (Criteria) this;
        }

        public Criteria andApellidosOrganizadoraIsNotNull() {
            addCriterion("APELLIDOS_ORGANIZADORA is not null");
            return (Criteria) this;
        }

        public Criteria andApellidosOrganizadoraEqualTo(String value) {
            addCriterion("APELLIDOS_ORGANIZADORA =", value, "apellidosOrganizadora");
            return (Criteria) this;
        }

        public Criteria andApellidosOrganizadoraNotEqualTo(String value) {
            addCriterion("APELLIDOS_ORGANIZADORA <>", value, "apellidosOrganizadora");
            return (Criteria) this;
        }

        public Criteria andApellidosOrganizadoraGreaterThan(String value) {
            addCriterion("APELLIDOS_ORGANIZADORA >", value, "apellidosOrganizadora");
            return (Criteria) this;
        }

        public Criteria andApellidosOrganizadoraGreaterThanOrEqualTo(String value) {
            addCriterion("APELLIDOS_ORGANIZADORA >=", value, "apellidosOrganizadora");
            return (Criteria) this;
        }

        public Criteria andApellidosOrganizadoraLessThan(String value) {
            addCriterion("APELLIDOS_ORGANIZADORA <", value, "apellidosOrganizadora");
            return (Criteria) this;
        }

        public Criteria andApellidosOrganizadoraLessThanOrEqualTo(String value) {
            addCriterion("APELLIDOS_ORGANIZADORA <=", value, "apellidosOrganizadora");
            return (Criteria) this;
        }

        public Criteria andApellidosOrganizadoraLike(String value) {
            addCriterion("APELLIDOS_ORGANIZADORA like", value, "apellidosOrganizadora");
            return (Criteria) this;
        }

        public Criteria andApellidosOrganizadoraNotLike(String value) {
            addCriterion("APELLIDOS_ORGANIZADORA not like", value, "apellidosOrganizadora");
            return (Criteria) this;
        }

        public Criteria andApellidosOrganizadoraIn(List<String> values) {
            addCriterion("APELLIDOS_ORGANIZADORA in", values, "apellidosOrganizadora");
            return (Criteria) this;
        }

        public Criteria andApellidosOrganizadoraNotIn(List<String> values) {
            addCriterion("APELLIDOS_ORGANIZADORA not in", values, "apellidosOrganizadora");
            return (Criteria) this;
        }

        public Criteria andApellidosOrganizadoraBetween(String value1, String value2) {
            addCriterion("APELLIDOS_ORGANIZADORA between", value1, value2, "apellidosOrganizadora");
            return (Criteria) this;
        }

        public Criteria andApellidosOrganizadoraNotBetween(String value1, String value2) {
            addCriterion("APELLIDOS_ORGANIZADORA not between", value1, value2, "apellidosOrganizadora");
            return (Criteria) this;
        }

        public Criteria andTelefonoOrganizadoraIsNull() {
            addCriterion("TELEFONO_ORGANIZADORA is null");
            return (Criteria) this;
        }

        public Criteria andTelefonoOrganizadoraIsNotNull() {
            addCriterion("TELEFONO_ORGANIZADORA is not null");
            return (Criteria) this;
        }

        public Criteria andTelefonoOrganizadoraEqualTo(String value) {
            addCriterion("TELEFONO_ORGANIZADORA =", value, "telefonoOrganizadora");
            return (Criteria) this;
        }

        public Criteria andTelefonoOrganizadoraNotEqualTo(String value) {
            addCriterion("TELEFONO_ORGANIZADORA <>", value, "telefonoOrganizadora");
            return (Criteria) this;
        }

        public Criteria andTelefonoOrganizadoraGreaterThan(String value) {
            addCriterion("TELEFONO_ORGANIZADORA >", value, "telefonoOrganizadora");
            return (Criteria) this;
        }

        public Criteria andTelefonoOrganizadoraGreaterThanOrEqualTo(String value) {
            addCriterion("TELEFONO_ORGANIZADORA >=", value, "telefonoOrganizadora");
            return (Criteria) this;
        }

        public Criteria andTelefonoOrganizadoraLessThan(String value) {
            addCriterion("TELEFONO_ORGANIZADORA <", value, "telefonoOrganizadora");
            return (Criteria) this;
        }

        public Criteria andTelefonoOrganizadoraLessThanOrEqualTo(String value) {
            addCriterion("TELEFONO_ORGANIZADORA <=", value, "telefonoOrganizadora");
            return (Criteria) this;
        }

        public Criteria andTelefonoOrganizadoraLike(String value) {
            addCriterion("TELEFONO_ORGANIZADORA like", value, "telefonoOrganizadora");
            return (Criteria) this;
        }

        public Criteria andTelefonoOrganizadoraNotLike(String value) {
            addCriterion("TELEFONO_ORGANIZADORA not like", value, "telefonoOrganizadora");
            return (Criteria) this;
        }

        public Criteria andTelefonoOrganizadoraIn(List<String> values) {
            addCriterion("TELEFONO_ORGANIZADORA in", values, "telefonoOrganizadora");
            return (Criteria) this;
        }

        public Criteria andTelefonoOrganizadoraNotIn(List<String> values) {
            addCriterion("TELEFONO_ORGANIZADORA not in", values, "telefonoOrganizadora");
            return (Criteria) this;
        }

        public Criteria andTelefonoOrganizadoraBetween(String value1, String value2) {
            addCriterion("TELEFONO_ORGANIZADORA between", value1, value2, "telefonoOrganizadora");
            return (Criteria) this;
        }

        public Criteria andTelefonoOrganizadoraNotBetween(String value1, String value2) {
            addCriterion("TELEFONO_ORGANIZADORA not between", value1, value2, "telefonoOrganizadora");
            return (Criteria) this;
        }

        public Criteria andDireccionEventoIsNull() {
            addCriterion("DIRECCION_EVENTO is null");
            return (Criteria) this;
        }

        public Criteria andDireccionEventoIsNotNull() {
            addCriterion("DIRECCION_EVENTO is not null");
            return (Criteria) this;
        }

        public Criteria andDireccionEventoEqualTo(String value) {
            addCriterion("DIRECCION_EVENTO =", value, "direccionEvento");
            return (Criteria) this;
        }

        public Criteria andDireccionEventoNotEqualTo(String value) {
            addCriterion("DIRECCION_EVENTO <>", value, "direccionEvento");
            return (Criteria) this;
        }

        public Criteria andDireccionEventoGreaterThan(String value) {
            addCriterion("DIRECCION_EVENTO >", value, "direccionEvento");
            return (Criteria) this;
        }

        public Criteria andDireccionEventoGreaterThanOrEqualTo(String value) {
            addCriterion("DIRECCION_EVENTO >=", value, "direccionEvento");
            return (Criteria) this;
        }

        public Criteria andDireccionEventoLessThan(String value) {
            addCriterion("DIRECCION_EVENTO <", value, "direccionEvento");
            return (Criteria) this;
        }

        public Criteria andDireccionEventoLessThanOrEqualTo(String value) {
            addCriterion("DIRECCION_EVENTO <=", value, "direccionEvento");
            return (Criteria) this;
        }

        public Criteria andDireccionEventoLike(String value) {
            addCriterion("DIRECCION_EVENTO like", value, "direccionEvento");
            return (Criteria) this;
        }

        public Criteria andDireccionEventoNotLike(String value) {
            addCriterion("DIRECCION_EVENTO not like", value, "direccionEvento");
            return (Criteria) this;
        }

        public Criteria andDireccionEventoIn(List<String> values) {
            addCriterion("DIRECCION_EVENTO in", values, "direccionEvento");
            return (Criteria) this;
        }

        public Criteria andDireccionEventoNotIn(List<String> values) {
            addCriterion("DIRECCION_EVENTO not in", values, "direccionEvento");
            return (Criteria) this;
        }

        public Criteria andDireccionEventoBetween(String value1, String value2) {
            addCriterion("DIRECCION_EVENTO between", value1, value2, "direccionEvento");
            return (Criteria) this;
        }

        public Criteria andDireccionEventoNotBetween(String value1, String value2) {
            addCriterion("DIRECCION_EVENTO not between", value1, value2, "direccionEvento");
            return (Criteria) this;
        }

        public Criteria andFechaHoraEventoIsNull() {
            addCriterion("FECHA_HORA_EVENTO is null");
            return (Criteria) this;
        }

        public Criteria andFechaHoraEventoIsNotNull() {
            addCriterion("FECHA_HORA_EVENTO is not null");
            return (Criteria) this;
        }

        public Criteria andFechaHoraEventoEqualTo(Fecha value) {
            addFechaHoraEventoCriterion("FECHA_HORA_EVENTO =", value, "fechaHoraEvento");
            return (Criteria) this;
        }

        public Criteria andFechaHoraEventoNotEqualTo(Fecha value) {
            addFechaHoraEventoCriterion("FECHA_HORA_EVENTO <>", value, "fechaHoraEvento");
            return (Criteria) this;
        }

        public Criteria andFechaHoraEventoGreaterThan(Fecha value) {
            addFechaHoraEventoCriterion("FECHA_HORA_EVENTO >", value, "fechaHoraEvento");
            return (Criteria) this;
        }

        public Criteria andFechaHoraEventoGreaterThanOrEqualTo(Fecha value) {
            addFechaHoraEventoCriterion("FECHA_HORA_EVENTO >=", value, "fechaHoraEvento");
            return (Criteria) this;
        }

        public Criteria andFechaHoraEventoLessThan(Fecha value) {
            addFechaHoraEventoCriterion("FECHA_HORA_EVENTO <", value, "fechaHoraEvento");
            return (Criteria) this;
        }

        public Criteria andFechaHoraEventoLessThanOrEqualTo(Fecha value) {
            addFechaHoraEventoCriterion("FECHA_HORA_EVENTO <=", value, "fechaHoraEvento");
            return (Criteria) this;
        }

        public Criteria andFechaHoraEventoIn(List<Fecha> values) {
            addFechaHoraEventoCriterion("FECHA_HORA_EVENTO in", values, "fechaHoraEvento");
            return (Criteria) this;
        }

        public Criteria andFechaHoraEventoNotIn(List<Fecha> values) {
            addFechaHoraEventoCriterion("FECHA_HORA_EVENTO not in", values, "fechaHoraEvento");
            return (Criteria) this;
        }

        public Criteria andFechaHoraEventoBetween(Fecha value1, Fecha value2) {
            addFechaHoraEventoCriterion("FECHA_HORA_EVENTO between", value1, value2, "fechaHoraEvento");
            return (Criteria) this;
        }

        public Criteria andFechaHoraEventoNotBetween(Fecha value1, Fecha value2) {
            addFechaHoraEventoCriterion("FECHA_HORA_EVENTO not between", value1, value2, "fechaHoraEvento");
            return (Criteria) this;
        }

        public Criteria andUidReservacionLikeInsensitive(String value) {
            addCriterion("upper(UID_RESERVACION) like", value.toUpperCase(), "uidReservacion");
            return (Criteria) this;
        }

        public Criteria andCodcliLikeInsensitive(String value) {
            addCriterion("upper(CODCLI) like", value.toUpperCase(), "codcli");
            return (Criteria) this;
        }

        public Criteria andCodTipoLikeInsensitive(String value) {
            addCriterion("upper(COD_TIPO) like", value.toUpperCase(), "codTipo");
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

        public Criteria andCodvendedorLikeInsensitive(String value) {
            addCriterion("upper(CODVENDEDOR) like", value.toUpperCase(), "codvendedor");
            return (Criteria) this;
        }

        public Criteria andNombreOrganizadoraLikeInsensitive(String value) {
            addCriterion("upper(NOMBRE_ORGANIZADORA) like", value.toUpperCase(), "nombreOrganizadora");
            return (Criteria) this;
        }

        public Criteria andApellidosOrganizadoraLikeInsensitive(String value) {
            addCriterion("upper(APELLIDOS_ORGANIZADORA) like", value.toUpperCase(), "apellidosOrganizadora");
            return (Criteria) this;
        }

        public Criteria andTelefonoOrganizadoraLikeInsensitive(String value) {
            addCriterion("upper(TELEFONO_ORGANIZADORA) like", value.toUpperCase(), "telefonoOrganizadora");
            return (Criteria) this;
        }

        public Criteria andDireccionEventoLikeInsensitive(String value) {
            addCriterion("upper(DIRECCION_EVENTO) like", value.toUpperCase(), "direccionEvento");
            return (Criteria) this;
        }
        
        public Criteria andCodcliEqualToWithoutNull(Cliente cliente) {
            if(cliente!= null && cliente.getCodcli()!= null && !cliente.getCodcli().trim().isEmpty()){
               andCodcliEqualTo(cliente.getCodcli());
            }
            return (Criteria) this;
        }
        
        public Criteria andCodTipoEqualToWithoutNull(ReservaTiposBean reservaTipos) {
            if (reservaTipos != null && !reservaTipos.getCodTipo().equals("TODOS")) {
                andCodTipoEqualTo(reservaTipos.getCodTipo());
            }
            return (Criteria) this;
        }
        
        public Criteria andCodAlmEqualToWithoutNull(String value) {
            if(value!=null && !value.trim().isEmpty()){
                andCodalmEqualTo(value);
            }
            return (Criteria) this;
        }
        
        public Criteria andEstadoEqualTo(String estado) {
            if (estado != null && !estado.trim().isEmpty()) {
                if (estado.equals("Abierta")) {
                    andLiquidadoEqualTo(false);
                    andCanceladoEqualTo(false);
                    andCaducidadGreaterThan(new Fecha());
                }
                else if (estado.equals("Anulada")) {
                    andCanceladoEqualTo(true);
                }
                else if (estado.equals("Liquidada")) {
                    andLiquidadoEqualTo(true);
                }
                else if (estado.equals("Caducada")) {
                    andLiquidadoEqualTo(false);
                    andCanceladoEqualTo(false);  
                    andCaducidadLessThanOrEqualTo(new Fecha());
                }
            }
            return (Criteria) this;
        }
        
        public Criteria andNombreLikeToWithouthNull(String nombre){
            if(nombre!=null && !nombre.trim().isEmpty()){
                addCriterion("upper(NOMBRECLIENTE) like", "%"+nombre.toUpperCase()+"%", "nombrecliente");
            }
            return (Criteria) this;
        }

        public Criteria andApellidosLikeToWithouthNull(String apellidos){
            if(apellidos!=null && !apellidos.trim().isEmpty()){
                addCriterion("upper(APELLIDOSCLIENTE) like", "%"+apellidos.toUpperCase()+"%", "apellidoscliente");
            }
            return (Criteria) this;
        }

        public Criteria andNombreOrgLikeToWithouthNull(String nombreOrg){
            if(nombreOrg!=null && !nombreOrg.trim().isEmpty()){
                andNombreOrganizadoraLike("%"+nombreOrg+"%");
            }
            return (Criteria) this;
        }

        public Criteria andApellidosOrgLikeToWithouthNull(String apellidosOrg){
            if(apellidosOrg!=null && !apellidosOrg.trim().isEmpty()){
                andApellidosOrganizadoraLike("%"+apellidosOrg+"%");
            }
            return (Criteria) this;
        }        
        
        public Criteria andFechaAltaBetweenWithoutNull(Fecha fecha1, Fecha fecha2) {
            if(fecha1.getDate() != null && fecha2.getDate() != null){
                andFechaAltaBetween(fecha1, fecha2);
            } else if (fecha1.getDate() != null) {
                andFechaAltaGreaterThanOrEqualTo(fecha1);
            } else if (fecha2.getDate() != null) {
                andFechaAltaLessThanOrEqualTo(fecha2);
            }
            return (Criteria) this;
        }
        
        public Criteria andBonoIsNull() {
            addCriterion("BONO is null");
            return (Criteria) this;
        }

        public Criteria andBonoIsNotNull() {
            addCriterion("BONO is not null");
            return (Criteria) this;
        }

        public Criteria andBonoEqualTo(Boolean value) {
            addBonoCriterion("BONO =", value, "bono");
            return (Criteria) this;
        }

        public Criteria andBonoNotEqualTo(Boolean value) {
            addBonoCriterion("BONO <>", value, "bono");
            return (Criteria) this;
        }

        public Criteria andBonoGreaterThan(Boolean value) {
            addBonoCriterion("BONO >", value, "bono");
            return (Criteria) this;
        }

        public Criteria andBonoGreaterThanOrEqualTo(Boolean value) {
            addBonoCriterion("BONO >=", value, "bono");
            return (Criteria) this;
        }

        public Criteria andBonoLessThan(Boolean value) {
            addBonoCriterion("BONO <", value, "bono");
            return (Criteria) this;
        }

        public Criteria andBonoLessThanOrEqualTo(Boolean value) {
            addBonoCriterion("BONO <=", value, "bono");
            return (Criteria) this;
        }

        public Criteria andBonoLike(Boolean value) {
            addBonoCriterion("BONO like", value, "bono");
            return (Criteria) this;
        }

        public Criteria andBonoNotLike(Boolean value) {
            addBonoCriterion("BONO not like", value, "bono");
            return (Criteria) this;
        }

        public Criteria andBonoIn(List<Boolean> values) {
            addBonoCriterion("BONO in", values, "bono");
            return (Criteria) this;
        }

        public Criteria andBonoNotIn(List<Boolean> values) {
            addBonoCriterion("BONO not in", values, "bono");
            return (Criteria) this;
        }

        public Criteria andBonoBetween(Boolean value1, Boolean value2) {
            addBonoCriterion("BONO between", value1, value2, "bono");
            return (Criteria) this;
        }

        public Criteria andBonoNotBetween(Boolean value1, Boolean value2) {
            addBonoCriterion("BONO not between", value1, value2, "bono");
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