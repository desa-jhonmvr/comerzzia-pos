package com.comerzzia.jpos.persistencia.credito.abonos;

import es.mpsistemas.util.fechas.Fecha;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AbonoCreditoExample {
    public static final String ORDER_BY_UID_CREDITO_PAGO = "UID_CREDITO_PAGO";

    public static final String ORDER_BY_UID_CREDITO_PAGO_DESC = "UID_CREDITO_PAGO DESC";

    public static final String ORDER_BY_CODALM = "CODALM";

    public static final String ORDER_BY_CODALM_DESC = "CODALM DESC";

    public static final String ORDER_BY_CODCAJA = "CODCAJA";

    public static final String ORDER_BY_CODCAJA_DESC = "CODCAJA DESC";

    public static final String ORDER_BY_IDENTIFICADOR = "IDENTIFICADOR";

    public static final String ORDER_BY_IDENTIFICADOR_DESC = "IDENTIFICADOR DESC";

    public static final String ORDER_BY_NUM_CREDITO = "NUM_CREDITO";

    public static final String ORDER_BY_NUM_CREDITO_DESC = "NUM_CREDITO DESC";

    public static final String ORDER_BY_CODCLI = "CODCLI";

    public static final String ORDER_BY_CODCLI_DESC = "CODCLI DESC";

    public static final String ORDER_BY_COD_VENDEDOR = "COD_VENDEDOR";

    public static final String ORDER_BY_COD_VENDEDOR_DESC = "COD_VENDEDOR DESC";

    public static final String ORDER_BY_TOTAL_SIN_DTO = "TOTAL_SIN_DTO";

    public static final String ORDER_BY_TOTAL_SIN_DTO_DESC = "TOTAL_SIN_DTO DESC";

    public static final String ORDER_BY_TOTAL_CON_DTO = "TOTAL_CON_DTO";

    public static final String ORDER_BY_TOTAL_CON_DTO_DESC = "TOTAL_CON_DTO DESC";

    public static final String ORDER_BY_OBSERVACIONES = "OBSERVACIONES";

    public static final String ORDER_BY_OBSERVACIONES_DESC = "OBSERVACIONES DESC";

    public static final String ORDER_BY_PROCESADO = "PROCESADO";

    public static final String ORDER_BY_PROCESADO_DESC = "PROCESADO DESC";

    public static final String ORDER_BY_FECHA = "FECHA";

    public static final String ORDER_BY_FECHA_DESC = "FECHA DESC";

    public static final String ORDER_BY_NUMERO_TARJETA = "NUMERO_TARJETA";

    public static final String ORDER_BY_NUMERO_TARJETA_DESC = "NUMERO_TARJETA DESC";

    public static final String ORDER_BY_PAGOS = "PAGOS";

    public static final String ORDER_BY_PAGOS_DESC = "PAGOS DESC";

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AbonoCreditoExample() {
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

        protected List<Criterion> fechaCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
            procesadoCriteria = new ArrayList<Criterion>();
            fechaCriteria = new ArrayList<Criterion>();
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

        public boolean isValid() {
            return criteria.size() > 0
                || procesadoCriteria.size() > 0
                || fechaCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<Criterion>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(procesadoCriteria);
                allCriteria.addAll(fechaCriteria);
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

        public Criteria andUidCreditoPagoIsNull() {
            addCriterion("UID_CREDITO_PAGO is null");
            return (Criteria) this;
        }

        public Criteria andUidCreditoPagoIsNotNull() {
            addCriterion("UID_CREDITO_PAGO is not null");
            return (Criteria) this;
        }

        public Criteria andUidCreditoPagoEqualTo(String value) {
            addCriterion("UID_CREDITO_PAGO =", value, "uidCreditoPago");
            return (Criteria) this;
        }

        public Criteria andUidCreditoPagoNotEqualTo(String value) {
            addCriterion("UID_CREDITO_PAGO <>", value, "uidCreditoPago");
            return (Criteria) this;
        }

        public Criteria andUidCreditoPagoGreaterThan(String value) {
            addCriterion("UID_CREDITO_PAGO >", value, "uidCreditoPago");
            return (Criteria) this;
        }

        public Criteria andUidCreditoPagoGreaterThanOrEqualTo(String value) {
            addCriterion("UID_CREDITO_PAGO >=", value, "uidCreditoPago");
            return (Criteria) this;
        }

        public Criteria andUidCreditoPagoLessThan(String value) {
            addCriterion("UID_CREDITO_PAGO <", value, "uidCreditoPago");
            return (Criteria) this;
        }

        public Criteria andUidCreditoPagoLessThanOrEqualTo(String value) {
            addCriterion("UID_CREDITO_PAGO <=", value, "uidCreditoPago");
            return (Criteria) this;
        }

        public Criteria andUidCreditoPagoLike(String value) {
            addCriterion("UID_CREDITO_PAGO like", value, "uidCreditoPago");
            return (Criteria) this;
        }

        public Criteria andUidCreditoPagoNotLike(String value) {
            addCriterion("UID_CREDITO_PAGO not like", value, "uidCreditoPago");
            return (Criteria) this;
        }

        public Criteria andUidCreditoPagoIn(List<String> values) {
            addCriterion("UID_CREDITO_PAGO in", values, "uidCreditoPago");
            return (Criteria) this;
        }

        public Criteria andUidCreditoPagoNotIn(List<String> values) {
            addCriterion("UID_CREDITO_PAGO not in", values, "uidCreditoPago");
            return (Criteria) this;
        }

        public Criteria andUidCreditoPagoBetween(String value1, String value2) {
            addCriterion("UID_CREDITO_PAGO between", value1, value2, "uidCreditoPago");
            return (Criteria) this;
        }

        public Criteria andUidCreditoPagoNotBetween(String value1, String value2) {
            addCriterion("UID_CREDITO_PAGO not between", value1, value2, "uidCreditoPago");
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

        public Criteria andNumCreditoIsNull() {
            addCriterion("NUM_CREDITO is null");
            return (Criteria) this;
        }

        public Criteria andNumCreditoIsNotNull() {
            addCriterion("NUM_CREDITO is not null");
            return (Criteria) this;
        }

        public Criteria andNumCreditoEqualTo(Integer value) {
            addCriterion("NUM_CREDITO =", value, "numCredito");
            return (Criteria) this;
        }

        public Criteria andNumCreditoNotEqualTo(Integer value) {
            addCriterion("NUM_CREDITO <>", value, "numCredito");
            return (Criteria) this;
        }

        public Criteria andNumCreditoGreaterThan(Integer value) {
            addCriterion("NUM_CREDITO >", value, "numCredito");
            return (Criteria) this;
        }

        public Criteria andNumCreditoGreaterThanOrEqualTo(Integer value) {
            addCriterion("NUM_CREDITO >=", value, "numCredito");
            return (Criteria) this;
        }

        public Criteria andNumCreditoLessThan(Integer value) {
            addCriterion("NUM_CREDITO <", value, "numCredito");
            return (Criteria) this;
        }

        public Criteria andNumCreditoLessThanOrEqualTo(Integer value) {
            addCriterion("NUM_CREDITO <=", value, "numCredito");
            return (Criteria) this;
        }

        public Criteria andNumCreditoIn(List<Integer> values) {
            addCriterion("NUM_CREDITO in", values, "numCredito");
            return (Criteria) this;
        }

        public Criteria andNumCreditoNotIn(List<Integer> values) {
            addCriterion("NUM_CREDITO not in", values, "numCredito");
            return (Criteria) this;
        }

        public Criteria andNumCreditoBetween(Integer value1, Integer value2) {
            addCriterion("NUM_CREDITO between", value1, value2, "numCredito");
            return (Criteria) this;
        }

        public Criteria andNumCreditoNotBetween(Integer value1, Integer value2) {
            addCriterion("NUM_CREDITO not between", value1, value2, "numCredito");
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

        public Criteria andCodVendedorIsNull() {
            addCriterion("COD_VENDEDOR is null");
            return (Criteria) this;
        }

        public Criteria andCodVendedorIsNotNull() {
            addCriterion("COD_VENDEDOR is not null");
            return (Criteria) this;
        }

        public Criteria andCodVendedorEqualTo(String value) {
            addCriterion("COD_VENDEDOR =", value, "codVendedor");
            return (Criteria) this;
        }

        public Criteria andCodVendedorNotEqualTo(String value) {
            addCriterion("COD_VENDEDOR <>", value, "codVendedor");
            return (Criteria) this;
        }

        public Criteria andCodVendedorGreaterThan(String value) {
            addCriterion("COD_VENDEDOR >", value, "codVendedor");
            return (Criteria) this;
        }

        public Criteria andCodVendedorGreaterThanOrEqualTo(String value) {
            addCriterion("COD_VENDEDOR >=", value, "codVendedor");
            return (Criteria) this;
        }

        public Criteria andCodVendedorLessThan(String value) {
            addCriterion("COD_VENDEDOR <", value, "codVendedor");
            return (Criteria) this;
        }

        public Criteria andCodVendedorLessThanOrEqualTo(String value) {
            addCriterion("COD_VENDEDOR <=", value, "codVendedor");
            return (Criteria) this;
        }

        public Criteria andCodVendedorLike(String value) {
            addCriterion("COD_VENDEDOR like", value, "codVendedor");
            return (Criteria) this;
        }

        public Criteria andCodVendedorNotLike(String value) {
            addCriterion("COD_VENDEDOR not like", value, "codVendedor");
            return (Criteria) this;
        }

        public Criteria andCodVendedorIn(List<String> values) {
            addCriterion("COD_VENDEDOR in", values, "codVendedor");
            return (Criteria) this;
        }

        public Criteria andCodVendedorNotIn(List<String> values) {
            addCriterion("COD_VENDEDOR not in", values, "codVendedor");
            return (Criteria) this;
        }

        public Criteria andCodVendedorBetween(String value1, String value2) {
            addCriterion("COD_VENDEDOR between", value1, value2, "codVendedor");
            return (Criteria) this;
        }

        public Criteria andCodVendedorNotBetween(String value1, String value2) {
            addCriterion("COD_VENDEDOR not between", value1, value2, "codVendedor");
            return (Criteria) this;
        }

        public Criteria andTotalSinDtoIsNull() {
            addCriterion("TOTAL_SIN_DTO is null");
            return (Criteria) this;
        }

        public Criteria andTotalSinDtoIsNotNull() {
            addCriterion("TOTAL_SIN_DTO is not null");
            return (Criteria) this;
        }

        public Criteria andTotalSinDtoEqualTo(BigDecimal value) {
            addCriterion("TOTAL_SIN_DTO =", value, "totalSinDto");
            return (Criteria) this;
        }

        public Criteria andTotalSinDtoNotEqualTo(BigDecimal value) {
            addCriterion("TOTAL_SIN_DTO <>", value, "totalSinDto");
            return (Criteria) this;
        }

        public Criteria andTotalSinDtoGreaterThan(BigDecimal value) {
            addCriterion("TOTAL_SIN_DTO >", value, "totalSinDto");
            return (Criteria) this;
        }

        public Criteria andTotalSinDtoGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("TOTAL_SIN_DTO >=", value, "totalSinDto");
            return (Criteria) this;
        }

        public Criteria andTotalSinDtoLessThan(BigDecimal value) {
            addCriterion("TOTAL_SIN_DTO <", value, "totalSinDto");
            return (Criteria) this;
        }

        public Criteria andTotalSinDtoLessThanOrEqualTo(BigDecimal value) {
            addCriterion("TOTAL_SIN_DTO <=", value, "totalSinDto");
            return (Criteria) this;
        }

        public Criteria andTotalSinDtoIn(List<BigDecimal> values) {
            addCriterion("TOTAL_SIN_DTO in", values, "totalSinDto");
            return (Criteria) this;
        }

        public Criteria andTotalSinDtoNotIn(List<BigDecimal> values) {
            addCriterion("TOTAL_SIN_DTO not in", values, "totalSinDto");
            return (Criteria) this;
        }

        public Criteria andTotalSinDtoBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("TOTAL_SIN_DTO between", value1, value2, "totalSinDto");
            return (Criteria) this;
        }

        public Criteria andTotalSinDtoNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("TOTAL_SIN_DTO not between", value1, value2, "totalSinDto");
            return (Criteria) this;
        }

        public Criteria andTotalConDtoIsNull() {
            addCriterion("TOTAL_CON_DTO is null");
            return (Criteria) this;
        }

        public Criteria andTotalConDtoIsNotNull() {
            addCriterion("TOTAL_CON_DTO is not null");
            return (Criteria) this;
        }

        public Criteria andTotalConDtoEqualTo(BigDecimal value) {
            addCriterion("TOTAL_CON_DTO =", value, "totalConDto");
            return (Criteria) this;
        }

        public Criteria andTotalConDtoNotEqualTo(BigDecimal value) {
            addCriterion("TOTAL_CON_DTO <>", value, "totalConDto");
            return (Criteria) this;
        }

        public Criteria andTotalConDtoGreaterThan(BigDecimal value) {
            addCriterion("TOTAL_CON_DTO >", value, "totalConDto");
            return (Criteria) this;
        }

        public Criteria andTotalConDtoGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("TOTAL_CON_DTO >=", value, "totalConDto");
            return (Criteria) this;
        }

        public Criteria andTotalConDtoLessThan(BigDecimal value) {
            addCriterion("TOTAL_CON_DTO <", value, "totalConDto");
            return (Criteria) this;
        }

        public Criteria andTotalConDtoLessThanOrEqualTo(BigDecimal value) {
            addCriterion("TOTAL_CON_DTO <=", value, "totalConDto");
            return (Criteria) this;
        }

        public Criteria andTotalConDtoIn(List<BigDecimal> values) {
            addCriterion("TOTAL_CON_DTO in", values, "totalConDto");
            return (Criteria) this;
        }

        public Criteria andTotalConDtoNotIn(List<BigDecimal> values) {
            addCriterion("TOTAL_CON_DTO not in", values, "totalConDto");
            return (Criteria) this;
        }

        public Criteria andTotalConDtoBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("TOTAL_CON_DTO between", value1, value2, "totalConDto");
            return (Criteria) this;
        }

        public Criteria andTotalConDtoNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("TOTAL_CON_DTO not between", value1, value2, "totalConDto");
            return (Criteria) this;
        }

        public Criteria andObservacionesIsNull() {
            addCriterion("OBSERVACIONES is null");
            return (Criteria) this;
        }

        public Criteria andObservacionesIsNotNull() {
            addCriterion("OBSERVACIONES is not null");
            return (Criteria) this;
        }

        public Criteria andObservacionesEqualTo(String value) {
            addCriterion("OBSERVACIONES =", value, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesNotEqualTo(String value) {
            addCriterion("OBSERVACIONES <>", value, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesGreaterThan(String value) {
            addCriterion("OBSERVACIONES >", value, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesGreaterThanOrEqualTo(String value) {
            addCriterion("OBSERVACIONES >=", value, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesLessThan(String value) {
            addCriterion("OBSERVACIONES <", value, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesLessThanOrEqualTo(String value) {
            addCriterion("OBSERVACIONES <=", value, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesLike(String value) {
            addCriterion("OBSERVACIONES like", value, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesNotLike(String value) {
            addCriterion("OBSERVACIONES not like", value, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesIn(List<String> values) {
            addCriterion("OBSERVACIONES in", values, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesNotIn(List<String> values) {
            addCriterion("OBSERVACIONES not in", values, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesBetween(String value1, String value2) {
            addCriterion("OBSERVACIONES between", value1, value2, "observaciones");
            return (Criteria) this;
        }

        public Criteria andObservacionesNotBetween(String value1, String value2) {
            addCriterion("OBSERVACIONES not between", value1, value2, "observaciones");
            return (Criteria) this;
        }

        public Criteria andAnuladoEqualTo(Boolean value) {
            addProcesadoCriterion("ANULADO =", value, "anulado");
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

        public Criteria andNumeroTarjetaIsNull() {
            addCriterion("NUMERO_TARJETA is null");
            return (Criteria) this;
        }

        public Criteria andNumeroTarjetaIsNotNull() {
            addCriterion("NUMERO_TARJETA is not null");
            return (Criteria) this;
        }

        public Criteria andNumeroTarjetaEqualTo(String value) {
            addCriterion("NUMERO_TARJETA =", value, "numeroTarjeta");
            return (Criteria) this;
        }

        public Criteria andNumeroTarjetaNotEqualTo(String value) {
            addCriterion("NUMERO_TARJETA <>", value, "numeroTarjeta");
            return (Criteria) this;
        }

        public Criteria andNumeroTarjetaGreaterThan(String value) {
            addCriterion("NUMERO_TARJETA >", value, "numeroTarjeta");
            return (Criteria) this;
        }

        public Criteria andNumeroTarjetaGreaterThanOrEqualTo(String value) {
            addCriterion("NUMERO_TARJETA >=", value, "numeroTarjeta");
            return (Criteria) this;
        }

        public Criteria andNumeroTarjetaLessThan(String value) {
            addCriterion("NUMERO_TARJETA <", value, "numeroTarjeta");
            return (Criteria) this;
        }

        public Criteria andNumeroTarjetaLessThanOrEqualTo(String value) {
            addCriterion("NUMERO_TARJETA <=", value, "numeroTarjeta");
            return (Criteria) this;
        }

        public Criteria andNumeroTarjetaLike(String value) {
            addCriterion("NUMERO_TARJETA like", value, "numeroTarjeta");
            return (Criteria) this;
        }

        public Criteria andNumeroTarjetaNotLike(String value) {
            addCriterion("NUMERO_TARJETA not like", value, "numeroTarjeta");
            return (Criteria) this;
        }

        public Criteria andNumeroTarjetaIn(List<String> values) {
            addCriterion("NUMERO_TARJETA in", values, "numeroTarjeta");
            return (Criteria) this;
        }

        public Criteria andNumeroTarjetaNotIn(List<String> values) {
            addCriterion("NUMERO_TARJETA not in", values, "numeroTarjeta");
            return (Criteria) this;
        }

        public Criteria andNumeroTarjetaBetween(String value1, String value2) {
            addCriterion("NUMERO_TARJETA between", value1, value2, "numeroTarjeta");
            return (Criteria) this;
        }

        public Criteria andNumeroTarjetaNotBetween(String value1, String value2) {
            addCriterion("NUMERO_TARJETA not between", value1, value2, "numeroTarjeta");
            return (Criteria) this;
        }

        public Criteria andUidCreditoPagoLikeInsensitive(String value) {
            addCriterion("upper(UID_CREDITO_PAGO) like", value.toUpperCase(), "uidCreditoPago");
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

        public Criteria andCodClienteLikeInsensitive(String value) {
            addCriterion("upper(CODCLI) like", value.toUpperCase(), "codCliente");
            return (Criteria) this;
        }

        public Criteria andCodVendedorLikeInsensitive(String value) {
            addCriterion("upper(COD_VENDEDOR) like", value.toUpperCase(), "codVendedor");
            return (Criteria) this;
        }

        public Criteria andObservacionesLikeInsensitive(String value) {
            addCriterion("upper(OBSERVACIONES) like", value.toUpperCase(), "observaciones");
            return (Criteria) this;
        }

        public Criteria andNumeroTarjetaLikeInsensitive(String value) {
            addCriterion("upper(NUMERO_TARJETA) like", value.toUpperCase(), "numeroTarjeta");
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