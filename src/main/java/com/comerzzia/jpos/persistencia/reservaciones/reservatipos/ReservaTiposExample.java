package com.comerzzia.jpos.persistencia.reservaciones.reservatipos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReservaTiposExample {
    public static final String ORDER_BY_COD_TIPO = "COD_TIPO";

    public static final String ORDER_BY_COD_TIPO_DESC = "COD_TIPO DESC";

    public static final String ORDER_BY_DES_TIPO = "DES_TIPO";

    public static final String ORDER_BY_DES_TIPO_DESC = "DES_TIPO DESC";

    public static final String ORDER_BY_ABONO_INICIAL = "ABONO_INICIAL";

    public static final String ORDER_BY_ABONO_INICIAL_DESC = "ABONO_INICIAL DESC";

    public static final String ORDER_BY_PORCENTAJE_ABONO_INICIAL = "PORCENTAJE_ABONO_INICIAL";

    public static final String ORDER_BY_PORCENTAJE_ABONO_INICIAL_DESC = "PORCENTAJE_ABONO_INICIAL DESC";

    public static final String ORDER_BY_ARTICULO_RESERVADO = "ARTICULO_RESERVADO";

    public static final String ORDER_BY_ARTICULO_RESERVADO_DESC = "ARTICULO_RESERVADO DESC";

    public static final String ORDER_BY_LISTA_INVITADOS = "LISTA_INVITADOS";

    public static final String ORDER_BY_LISTA_INVITADOS_DESC = "LISTA_INVITADOS DESC";

    public static final String ORDER_BY_PERMITE_COMPRA = "PERMITE_COMPRA";

    public static final String ORDER_BY_PERMITE_COMPRA_DESC = "PERMITE_COMPRA DESC";

    public static final String ORDER_BY_LIQUIDACION = "LIQUIDACION";

    public static final String ORDER_BY_LIQUIDACION_DESC = "LIQUIDACION DESC";

    public static final String ORDER_BY_PERMITE_RESERVAR_PROMOCIONADOS = "PERMITE_RESERVAR_PROMOCIONADOS";

    public static final String ORDER_BY_PERMITE_RESERVAR_PROMOCIONADOS_DESC = "PERMITE_RESERVAR_PROMOCIONADOS DESC";

    public static final String ORDER_BY_ABONOS_MAYORES_A_TOTAL = "ABONOS_MAYORES_A_TOTAL";

    public static final String ORDER_BY_ABONOS_MAYORES_A_TOTAL_DESC = "ABONOS_MAYORES_A_TOTAL DESC";

    public static final String ORDER_BY_PLAZO_RESERVACION = "PLAZO_RESERVACION";

    public static final String ORDER_BY_PLAZO_RESERVACION_DESC = "PLAZO_RESERVACION DESC";

    public static final String ORDER_BY_PERMITE_ELIMINAR_ARTICULOS = "PERMITE_ELIMINAR_ARTICULOS";

    public static final String ORDER_BY_PERMITE_ELIMINAR_ARTICULOS_DESC = "PERMITE_ELIMINAR_ARTICULOS DESC";

    public static final String ORDER_BY_PERMITE_LIQUIDACION_PARCIAL = "PERMITE_LIQUIDACION_PARCIAL";

    public static final String ORDER_BY_PERMITE_LIQUIDACION_PARCIAL_DESC = "PERMITE_LIQUIDACION_PARCIAL DESC";

    public static final String ORDER_BY_PERMITE_ABONOS_PROPIETARIO = "PERMITE_ABONOS_PROPIETARIO";

    public static final String ORDER_BY_PERMITE_ABONOS_PROPIETARIO_DESC = "PERMITE_ABONOS_PROPIETARIO DESC";

    public static final String ORDER_BY_PERMITE_ABONOS_INVITADOS = "PERMITE_ABONOS_INVITADOS";

    public static final String ORDER_BY_PERMITE_ABONOS_INVITADOS_DESC = "PERMITE_ABONOS_INVITADOS DESC";

    public static final String ORDER_BY_PERMITE_ABONOS_PARCIALES = "PERMITE_ABONOS_PARCIALES";

    public static final String ORDER_BY_PERMITE_ABONOS_PARCIALES_DESC = "PERMITE_ABONOS_PARCIALES DESC";

    public static final String ORDER_BY_PERMITE_ALTA_POS = "PERMITE_ALTA_POS";

    public static final String ORDER_BY_PERMITE_ALTA_POS_DESC = "PERMITE_ALTA_POS DESC";

    public static final String ORDER_BY_PERMITE_DATOS_ADICIONALES = "PERMITE_DATOS_ADICIONALES";

    public static final String ORDER_BY_PERMITE_DATOS_ADICIONALES_DESC = "PERMITE_DATOS_ADICIONALES DESC";

    public static final String ORDER_BY_PERMITE_FACTURAR_INVITADO = "PERMITE_FACTURAR_INVITADO";

    public static final String ORDER_BY_PERMITE_FACTURAR_INVITADO_DESC = "PERMITE_FACTURAR_INVITADO DESC";

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ReservaTiposExample() {
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
        protected List<Criterion> abonoInicialCriteria;

        protected List<Criterion> articuloReservadoCriteria;

        protected List<Criterion> listaInvitadosCriteria;

        protected List<Criterion> permiteCompraCriteria;

        protected List<Criterion> liquidacionCriteria;

        protected List<Criterion> permiteReservarPromocionadosCriteria;

        protected List<Criterion> abonosMayoresATotalCriteria;

        protected List<Criterion> permiteEliminarArticulosCriteria;

        protected List<Criterion> permiteLiquidacionParcialCriteria;

        protected List<Criterion> permiteAbonosPropietarioCriteria;

        protected List<Criterion> permiteAbonosInvitadosCriteria;

        protected List<Criterion> permiteAbonosParcialesCriteria;

        protected List<Criterion> permiteAltaPosCriteria;

        protected List<Criterion> permiteDatosAdicionalesCriteria;

        protected List<Criterion> permiteFacturarInvitadoCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
            abonoInicialCriteria = new ArrayList<Criterion>();
            articuloReservadoCriteria = new ArrayList<Criterion>();
            listaInvitadosCriteria = new ArrayList<Criterion>();
            permiteCompraCriteria = new ArrayList<Criterion>();
            liquidacionCriteria = new ArrayList<Criterion>();
            permiteReservarPromocionadosCriteria = new ArrayList<Criterion>();
            abonosMayoresATotalCriteria = new ArrayList<Criterion>();
            permiteEliminarArticulosCriteria = new ArrayList<Criterion>();
            permiteLiquidacionParcialCriteria = new ArrayList<Criterion>();
            permiteAbonosPropietarioCriteria = new ArrayList<Criterion>();
            permiteAbonosInvitadosCriteria = new ArrayList<Criterion>();
            permiteAbonosParcialesCriteria = new ArrayList<Criterion>();
            permiteAltaPosCriteria = new ArrayList<Criterion>();
            permiteDatosAdicionalesCriteria = new ArrayList<Criterion>();
            permiteFacturarInvitadoCriteria = new ArrayList<Criterion>();
        }

        public List<Criterion> getAbonoInicialCriteria() {
            return abonoInicialCriteria;
        }

        protected void addAbonoInicialCriterion(String condition, Object value, String property) {
            if (value != null) {
                abonoInicialCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addAbonoInicialCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                abonoInicialCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getArticuloReservadoCriteria() {
            return articuloReservadoCriteria;
        }

        protected void addArticuloReservadoCriterion(String condition, Object value, String property) {
            if (value != null) {
                articuloReservadoCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addArticuloReservadoCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                articuloReservadoCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getListaInvitadosCriteria() {
            return listaInvitadosCriteria;
        }

        protected void addListaInvitadosCriterion(String condition, Object value, String property) {
            if (value != null) {
                listaInvitadosCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addListaInvitadosCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                listaInvitadosCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getPermiteCompraCriteria() {
            return permiteCompraCriteria;
        }

        protected void addPermiteCompraCriterion(String condition, Object value, String property) {
            if (value != null) {
                permiteCompraCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addPermiteCompraCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                permiteCompraCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getLiquidacionCriteria() {
            return liquidacionCriteria;
        }

        protected void addLiquidacionCriterion(String condition, Object value, String property) {
            if (value != null) {
                liquidacionCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addLiquidacionCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                liquidacionCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getPermiteReservarPromocionadosCriteria() {
            return permiteReservarPromocionadosCriteria;
        }

        protected void addPermiteReservarPromocionadosCriterion(String condition, Object value, String property) {
            if (value != null) {
                permiteReservarPromocionadosCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addPermiteReservarPromocionadosCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                permiteReservarPromocionadosCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getAbonosMayoresATotalCriteria() {
            return abonosMayoresATotalCriteria;
        }

        protected void addAbonosMayoresATotalCriterion(String condition, Object value, String property) {
            if (value != null) {
                abonosMayoresATotalCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addAbonosMayoresATotalCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                abonosMayoresATotalCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getPermiteEliminarArticulosCriteria() {
            return permiteEliminarArticulosCriteria;
        }

        protected void addPermiteEliminarArticulosCriterion(String condition, Object value, String property) {
            if (value != null) {
                permiteEliminarArticulosCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addPermiteEliminarArticulosCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                permiteEliminarArticulosCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getPermiteLiquidacionParcialCriteria() {
            return permiteLiquidacionParcialCriteria;
        }

        protected void addPermiteLiquidacionParcialCriterion(String condition, Object value, String property) {
            if (value != null) {
                permiteLiquidacionParcialCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addPermiteLiquidacionParcialCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                permiteLiquidacionParcialCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getPermiteAbonosPropietarioCriteria() {
            return permiteAbonosPropietarioCriteria;
        }

        protected void addPermiteAbonosPropietarioCriterion(String condition, Object value, String property) {
            if (value != null) {
                permiteAbonosPropietarioCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addPermiteAbonosPropietarioCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                permiteAbonosPropietarioCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getPermiteAbonosInvitadosCriteria() {
            return permiteAbonosInvitadosCriteria;
        }

        protected void addPermiteAbonosInvitadosCriterion(String condition, Object value, String property) {
            if (value != null) {
                permiteAbonosInvitadosCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addPermiteAbonosInvitadosCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                permiteAbonosInvitadosCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getPermiteAbonosParcialesCriteria() {
            return permiteAbonosParcialesCriteria;
        }

        protected void addPermiteAbonosParcialesCriterion(String condition, Object value, String property) {
            if (value != null) {
                permiteAbonosParcialesCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addPermiteAbonosParcialesCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                permiteAbonosParcialesCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getPermiteAltaPosCriteria() {
            return permiteAltaPosCriteria;
        }

        protected void addPermiteAltaPosCriterion(String condition, Object value, String property) {
            if (value != null) {
                permiteAltaPosCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addPermiteAltaPosCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                permiteAltaPosCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getPermiteDatosAdicionalesCriteria() {
            return permiteDatosAdicionalesCriteria;
        }

        protected void addPermiteDatosAdicionalesCriterion(String condition, Object value, String property) {
            if (value != null) {
                permiteDatosAdicionalesCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addPermiteDatosAdicionalesCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                permiteDatosAdicionalesCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public List<Criterion> getPermiteFacturarInvitadoCriteria() {
            return permiteFacturarInvitadoCriteria;
        }

        protected void addPermiteFacturarInvitadoCriterion(String condition, Object value, String property) {
            if (value != null) {
                permiteFacturarInvitadoCriteria.add(new Criterion(condition, value, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        protected void addPermiteFacturarInvitadoCriterion(String condition, Boolean value1, Boolean value2, String property) {
            if (value1 != null && value2 != null) {
                permiteFacturarInvitadoCriteria.add(new Criterion(condition, value1, value2, "es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler"));
                allCriteria = null;
            }
        }

        public boolean isValid() {
            return criteria.size() > 0
                || abonoInicialCriteria.size() > 0
                || articuloReservadoCriteria.size() > 0
                || listaInvitadosCriteria.size() > 0
                || permiteCompraCriteria.size() > 0
                || liquidacionCriteria.size() > 0
                || permiteReservarPromocionadosCriteria.size() > 0
                || abonosMayoresATotalCriteria.size() > 0
                || permiteEliminarArticulosCriteria.size() > 0
                || permiteLiquidacionParcialCriteria.size() > 0
                || permiteAbonosPropietarioCriteria.size() > 0
                || permiteAbonosInvitadosCriteria.size() > 0
                || permiteAbonosParcialesCriteria.size() > 0
                || permiteAltaPosCriteria.size() > 0
                || permiteDatosAdicionalesCriteria.size() > 0
                || permiteFacturarInvitadoCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<Criterion>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(abonoInicialCriteria);
                allCriteria.addAll(articuloReservadoCriteria);
                allCriteria.addAll(listaInvitadosCriteria);
                allCriteria.addAll(permiteCompraCriteria);
                allCriteria.addAll(liquidacionCriteria);
                allCriteria.addAll(permiteReservarPromocionadosCriteria);
                allCriteria.addAll(abonosMayoresATotalCriteria);
                allCriteria.addAll(permiteEliminarArticulosCriteria);
                allCriteria.addAll(permiteLiquidacionParcialCriteria);
                allCriteria.addAll(permiteAbonosPropietarioCriteria);
                allCriteria.addAll(permiteAbonosInvitadosCriteria);
                allCriteria.addAll(permiteAbonosParcialesCriteria);
                allCriteria.addAll(permiteAltaPosCriteria);
                allCriteria.addAll(permiteDatosAdicionalesCriteria);
                allCriteria.addAll(permiteFacturarInvitadoCriteria);
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

        public Criteria andDesTipoIsNull() {
            addCriterion("DES_TIPO is null");
            return (Criteria) this;
        }

        public Criteria andDesTipoIsNotNull() {
            addCriterion("DES_TIPO is not null");
            return (Criteria) this;
        }

        public Criteria andDesTipoEqualTo(String value) {
            addCriterion("DES_TIPO =", value, "desTipo");
            return (Criteria) this;
        }

        public Criteria andDesTipoNotEqualTo(String value) {
            addCriterion("DES_TIPO <>", value, "desTipo");
            return (Criteria) this;
        }

        public Criteria andDesTipoGreaterThan(String value) {
            addCriterion("DES_TIPO >", value, "desTipo");
            return (Criteria) this;
        }

        public Criteria andDesTipoGreaterThanOrEqualTo(String value) {
            addCriterion("DES_TIPO >=", value, "desTipo");
            return (Criteria) this;
        }

        public Criteria andDesTipoLessThan(String value) {
            addCriterion("DES_TIPO <", value, "desTipo");
            return (Criteria) this;
        }

        public Criteria andDesTipoLessThanOrEqualTo(String value) {
            addCriterion("DES_TIPO <=", value, "desTipo");
            return (Criteria) this;
        }

        public Criteria andDesTipoLike(String value) {
            addCriterion("DES_TIPO like", value, "desTipo");
            return (Criteria) this;
        }

        public Criteria andDesTipoNotLike(String value) {
            addCriterion("DES_TIPO not like", value, "desTipo");
            return (Criteria) this;
        }

        public Criteria andDesTipoIn(List<String> values) {
            addCriterion("DES_TIPO in", values, "desTipo");
            return (Criteria) this;
        }

        public Criteria andDesTipoNotIn(List<String> values) {
            addCriterion("DES_TIPO not in", values, "desTipo");
            return (Criteria) this;
        }

        public Criteria andDesTipoBetween(String value1, String value2) {
            addCriterion("DES_TIPO between", value1, value2, "desTipo");
            return (Criteria) this;
        }

        public Criteria andDesTipoNotBetween(String value1, String value2) {
            addCriterion("DES_TIPO not between", value1, value2, "desTipo");
            return (Criteria) this;
        }

        public Criteria andAbonoInicialIsNull() {
            addCriterion("ABONO_INICIAL is null");
            return (Criteria) this;
        }

        public Criteria andAbonoInicialIsNotNull() {
            addCriterion("ABONO_INICIAL is not null");
            return (Criteria) this;
        }

        public Criteria andAbonoInicialEqualTo(Boolean value) {
            addAbonoInicialCriterion("ABONO_INICIAL =", value, "abonoInicial");
            return (Criteria) this;
        }

        public Criteria andAbonoInicialNotEqualTo(Boolean value) {
            addAbonoInicialCriterion("ABONO_INICIAL <>", value, "abonoInicial");
            return (Criteria) this;
        }

        public Criteria andAbonoInicialGreaterThan(Boolean value) {
            addAbonoInicialCriterion("ABONO_INICIAL >", value, "abonoInicial");
            return (Criteria) this;
        }

        public Criteria andAbonoInicialGreaterThanOrEqualTo(Boolean value) {
            addAbonoInicialCriterion("ABONO_INICIAL >=", value, "abonoInicial");
            return (Criteria) this;
        }

        public Criteria andAbonoInicialLessThan(Boolean value) {
            addAbonoInicialCriterion("ABONO_INICIAL <", value, "abonoInicial");
            return (Criteria) this;
        }

        public Criteria andAbonoInicialLessThanOrEqualTo(Boolean value) {
            addAbonoInicialCriterion("ABONO_INICIAL <=", value, "abonoInicial");
            return (Criteria) this;
        }

        public Criteria andAbonoInicialLike(Boolean value) {
            addAbonoInicialCriterion("ABONO_INICIAL like", value, "abonoInicial");
            return (Criteria) this;
        }

        public Criteria andAbonoInicialNotLike(Boolean value) {
            addAbonoInicialCriterion("ABONO_INICIAL not like", value, "abonoInicial");
            return (Criteria) this;
        }

        public Criteria andAbonoInicialIn(List<Boolean> values) {
            addAbonoInicialCriterion("ABONO_INICIAL in", values, "abonoInicial");
            return (Criteria) this;
        }

        public Criteria andAbonoInicialNotIn(List<Boolean> values) {
            addAbonoInicialCriterion("ABONO_INICIAL not in", values, "abonoInicial");
            return (Criteria) this;
        }

        public Criteria andAbonoInicialBetween(Boolean value1, Boolean value2) {
            addAbonoInicialCriterion("ABONO_INICIAL between", value1, value2, "abonoInicial");
            return (Criteria) this;
        }

        public Criteria andAbonoInicialNotBetween(Boolean value1, Boolean value2) {
            addAbonoInicialCriterion("ABONO_INICIAL not between", value1, value2, "abonoInicial");
            return (Criteria) this;
        }

        public Criteria andPorcentajeAbonoInicialIsNull() {
            addCriterion("PORCENTAJE_ABONO_INICIAL is null");
            return (Criteria) this;
        }

        public Criteria andPorcentajeAbonoInicialIsNotNull() {
            addCriterion("PORCENTAJE_ABONO_INICIAL is not null");
            return (Criteria) this;
        }

        public Criteria andPorcentajeAbonoInicialEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_ABONO_INICIAL =", value, "porcentajeAbonoInicial");
            return (Criteria) this;
        }

        public Criteria andPorcentajeAbonoInicialNotEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_ABONO_INICIAL <>", value, "porcentajeAbonoInicial");
            return (Criteria) this;
        }

        public Criteria andPorcentajeAbonoInicialGreaterThan(BigDecimal value) {
            addCriterion("PORCENTAJE_ABONO_INICIAL >", value, "porcentajeAbonoInicial");
            return (Criteria) this;
        }

        public Criteria andPorcentajeAbonoInicialGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_ABONO_INICIAL >=", value, "porcentajeAbonoInicial");
            return (Criteria) this;
        }

        public Criteria andPorcentajeAbonoInicialLessThan(BigDecimal value) {
            addCriterion("PORCENTAJE_ABONO_INICIAL <", value, "porcentajeAbonoInicial");
            return (Criteria) this;
        }

        public Criteria andPorcentajeAbonoInicialLessThanOrEqualTo(BigDecimal value) {
            addCriterion("PORCENTAJE_ABONO_INICIAL <=", value, "porcentajeAbonoInicial");
            return (Criteria) this;
        }

        public Criteria andPorcentajeAbonoInicialIn(List<BigDecimal> values) {
            addCriterion("PORCENTAJE_ABONO_INICIAL in", values, "porcentajeAbonoInicial");
            return (Criteria) this;
        }

        public Criteria andPorcentajeAbonoInicialNotIn(List<BigDecimal> values) {
            addCriterion("PORCENTAJE_ABONO_INICIAL not in", values, "porcentajeAbonoInicial");
            return (Criteria) this;
        }

        public Criteria andPorcentajeAbonoInicialBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PORCENTAJE_ABONO_INICIAL between", value1, value2, "porcentajeAbonoInicial");
            return (Criteria) this;
        }

        public Criteria andPorcentajeAbonoInicialNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PORCENTAJE_ABONO_INICIAL not between", value1, value2, "porcentajeAbonoInicial");
            return (Criteria) this;
        }

        public Criteria andArticuloReservadoIsNull() {
            addCriterion("ARTICULO_RESERVADO is null");
            return (Criteria) this;
        }

        public Criteria andArticuloReservadoIsNotNull() {
            addCriterion("ARTICULO_RESERVADO is not null");
            return (Criteria) this;
        }

        public Criteria andArticuloReservadoEqualTo(Boolean value) {
            addArticuloReservadoCriterion("ARTICULO_RESERVADO =", value, "articuloReservado");
            return (Criteria) this;
        }

        public Criteria andArticuloReservadoNotEqualTo(Boolean value) {
            addArticuloReservadoCriterion("ARTICULO_RESERVADO <>", value, "articuloReservado");
            return (Criteria) this;
        }

        public Criteria andArticuloReservadoGreaterThan(Boolean value) {
            addArticuloReservadoCriterion("ARTICULO_RESERVADO >", value, "articuloReservado");
            return (Criteria) this;
        }

        public Criteria andArticuloReservadoGreaterThanOrEqualTo(Boolean value) {
            addArticuloReservadoCriterion("ARTICULO_RESERVADO >=", value, "articuloReservado");
            return (Criteria) this;
        }

        public Criteria andArticuloReservadoLessThan(Boolean value) {
            addArticuloReservadoCriterion("ARTICULO_RESERVADO <", value, "articuloReservado");
            return (Criteria) this;
        }

        public Criteria andArticuloReservadoLessThanOrEqualTo(Boolean value) {
            addArticuloReservadoCriterion("ARTICULO_RESERVADO <=", value, "articuloReservado");
            return (Criteria) this;
        }

        public Criteria andArticuloReservadoLike(Boolean value) {
            addArticuloReservadoCriterion("ARTICULO_RESERVADO like", value, "articuloReservado");
            return (Criteria) this;
        }

        public Criteria andArticuloReservadoNotLike(Boolean value) {
            addArticuloReservadoCriterion("ARTICULO_RESERVADO not like", value, "articuloReservado");
            return (Criteria) this;
        }

        public Criteria andArticuloReservadoIn(List<Boolean> values) {
            addArticuloReservadoCriterion("ARTICULO_RESERVADO in", values, "articuloReservado");
            return (Criteria) this;
        }

        public Criteria andArticuloReservadoNotIn(List<Boolean> values) {
            addArticuloReservadoCriterion("ARTICULO_RESERVADO not in", values, "articuloReservado");
            return (Criteria) this;
        }

        public Criteria andArticuloReservadoBetween(Boolean value1, Boolean value2) {
            addArticuloReservadoCriterion("ARTICULO_RESERVADO between", value1, value2, "articuloReservado");
            return (Criteria) this;
        }

        public Criteria andArticuloReservadoNotBetween(Boolean value1, Boolean value2) {
            addArticuloReservadoCriterion("ARTICULO_RESERVADO not between", value1, value2, "articuloReservado");
            return (Criteria) this;
        }

        public Criteria andListaInvitadosIsNull() {
            addCriterion("LISTA_INVITADOS is null");
            return (Criteria) this;
        }

        public Criteria andListaInvitadosIsNotNull() {
            addCriterion("LISTA_INVITADOS is not null");
            return (Criteria) this;
        }

        public Criteria andListaInvitadosEqualTo(Boolean value) {
            addListaInvitadosCriterion("LISTA_INVITADOS =", value, "listaInvitados");
            return (Criteria) this;
        }

        public Criteria andListaInvitadosNotEqualTo(Boolean value) {
            addListaInvitadosCriterion("LISTA_INVITADOS <>", value, "listaInvitados");
            return (Criteria) this;
        }

        public Criteria andListaInvitadosGreaterThan(Boolean value) {
            addListaInvitadosCriterion("LISTA_INVITADOS >", value, "listaInvitados");
            return (Criteria) this;
        }

        public Criteria andListaInvitadosGreaterThanOrEqualTo(Boolean value) {
            addListaInvitadosCriterion("LISTA_INVITADOS >=", value, "listaInvitados");
            return (Criteria) this;
        }

        public Criteria andListaInvitadosLessThan(Boolean value) {
            addListaInvitadosCriterion("LISTA_INVITADOS <", value, "listaInvitados");
            return (Criteria) this;
        }

        public Criteria andListaInvitadosLessThanOrEqualTo(Boolean value) {
            addListaInvitadosCriterion("LISTA_INVITADOS <=", value, "listaInvitados");
            return (Criteria) this;
        }

        public Criteria andListaInvitadosLike(Boolean value) {
            addListaInvitadosCriterion("LISTA_INVITADOS like", value, "listaInvitados");
            return (Criteria) this;
        }

        public Criteria andListaInvitadosNotLike(Boolean value) {
            addListaInvitadosCriterion("LISTA_INVITADOS not like", value, "listaInvitados");
            return (Criteria) this;
        }

        public Criteria andListaInvitadosIn(List<Boolean> values) {
            addListaInvitadosCriterion("LISTA_INVITADOS in", values, "listaInvitados");
            return (Criteria) this;
        }

        public Criteria andListaInvitadosNotIn(List<Boolean> values) {
            addListaInvitadosCriterion("LISTA_INVITADOS not in", values, "listaInvitados");
            return (Criteria) this;
        }

        public Criteria andListaInvitadosBetween(Boolean value1, Boolean value2) {
            addListaInvitadosCriterion("LISTA_INVITADOS between", value1, value2, "listaInvitados");
            return (Criteria) this;
        }

        public Criteria andListaInvitadosNotBetween(Boolean value1, Boolean value2) {
            addListaInvitadosCriterion("LISTA_INVITADOS not between", value1, value2, "listaInvitados");
            return (Criteria) this;
        }

        public Criteria andPermiteCompraIsNull() {
            addCriterion("PERMITE_COMPRA is null");
            return (Criteria) this;
        }

        public Criteria andPermiteCompraIsNotNull() {
            addCriterion("PERMITE_COMPRA is not null");
            return (Criteria) this;
        }

        public Criteria andPermiteCompraEqualTo(Boolean value) {
            addPermiteCompraCriterion("PERMITE_COMPRA =", value, "permiteCompra");
            return (Criteria) this;
        }

        public Criteria andPermiteCompraNotEqualTo(Boolean value) {
            addPermiteCompraCriterion("PERMITE_COMPRA <>", value, "permiteCompra");
            return (Criteria) this;
        }

        public Criteria andPermiteCompraGreaterThan(Boolean value) {
            addPermiteCompraCriterion("PERMITE_COMPRA >", value, "permiteCompra");
            return (Criteria) this;
        }

        public Criteria andPermiteCompraGreaterThanOrEqualTo(Boolean value) {
            addPermiteCompraCriterion("PERMITE_COMPRA >=", value, "permiteCompra");
            return (Criteria) this;
        }

        public Criteria andPermiteCompraLessThan(Boolean value) {
            addPermiteCompraCriterion("PERMITE_COMPRA <", value, "permiteCompra");
            return (Criteria) this;
        }

        public Criteria andPermiteCompraLessThanOrEqualTo(Boolean value) {
            addPermiteCompraCriterion("PERMITE_COMPRA <=", value, "permiteCompra");
            return (Criteria) this;
        }

        public Criteria andPermiteCompraLike(Boolean value) {
            addPermiteCompraCriterion("PERMITE_COMPRA like", value, "permiteCompra");
            return (Criteria) this;
        }

        public Criteria andPermiteCompraNotLike(Boolean value) {
            addPermiteCompraCriterion("PERMITE_COMPRA not like", value, "permiteCompra");
            return (Criteria) this;
        }

        public Criteria andPermiteCompraIn(List<Boolean> values) {
            addPermiteCompraCriterion("PERMITE_COMPRA in", values, "permiteCompra");
            return (Criteria) this;
        }

        public Criteria andPermiteCompraNotIn(List<Boolean> values) {
            addPermiteCompraCriterion("PERMITE_COMPRA not in", values, "permiteCompra");
            return (Criteria) this;
        }

        public Criteria andPermiteCompraBetween(Boolean value1, Boolean value2) {
            addPermiteCompraCriterion("PERMITE_COMPRA between", value1, value2, "permiteCompra");
            return (Criteria) this;
        }

        public Criteria andPermiteCompraNotBetween(Boolean value1, Boolean value2) {
            addPermiteCompraCriterion("PERMITE_COMPRA not between", value1, value2, "permiteCompra");
            return (Criteria) this;
        }

        public Criteria andLiquidacionIsNull() {
            addCriterion("LIQUIDACION is null");
            return (Criteria) this;
        }

        public Criteria andLiquidacionIsNotNull() {
            addCriterion("LIQUIDACION is not null");
            return (Criteria) this;
        }

        public Criteria andLiquidacionEqualTo(Boolean value) {
            addLiquidacionCriterion("LIQUIDACION =", value, "liquidacion");
            return (Criteria) this;
        }

        public Criteria andLiquidacionNotEqualTo(Boolean value) {
            addLiquidacionCriterion("LIQUIDACION <>", value, "liquidacion");
            return (Criteria) this;
        }

        public Criteria andLiquidacionGreaterThan(Boolean value) {
            addLiquidacionCriterion("LIQUIDACION >", value, "liquidacion");
            return (Criteria) this;
        }

        public Criteria andLiquidacionGreaterThanOrEqualTo(Boolean value) {
            addLiquidacionCriterion("LIQUIDACION >=", value, "liquidacion");
            return (Criteria) this;
        }

        public Criteria andLiquidacionLessThan(Boolean value) {
            addLiquidacionCriterion("LIQUIDACION <", value, "liquidacion");
            return (Criteria) this;
        }

        public Criteria andLiquidacionLessThanOrEqualTo(Boolean value) {
            addLiquidacionCriterion("LIQUIDACION <=", value, "liquidacion");
            return (Criteria) this;
        }

        public Criteria andLiquidacionLike(Boolean value) {
            addLiquidacionCriterion("LIQUIDACION like", value, "liquidacion");
            return (Criteria) this;
        }

        public Criteria andLiquidacionNotLike(Boolean value) {
            addLiquidacionCriterion("LIQUIDACION not like", value, "liquidacion");
            return (Criteria) this;
        }

        public Criteria andLiquidacionIn(List<Boolean> values) {
            addLiquidacionCriterion("LIQUIDACION in", values, "liquidacion");
            return (Criteria) this;
        }

        public Criteria andLiquidacionNotIn(List<Boolean> values) {
            addLiquidacionCriterion("LIQUIDACION not in", values, "liquidacion");
            return (Criteria) this;
        }

        public Criteria andLiquidacionBetween(Boolean value1, Boolean value2) {
            addLiquidacionCriterion("LIQUIDACION between", value1, value2, "liquidacion");
            return (Criteria) this;
        }

        public Criteria andLiquidacionNotBetween(Boolean value1, Boolean value2) {
            addLiquidacionCriterion("LIQUIDACION not between", value1, value2, "liquidacion");
            return (Criteria) this;
        }

        public Criteria andPermiteReservarPromocionadosIsNull() {
            addCriterion("PERMITE_RESERVAR_PROMOCIONADOS is null");
            return (Criteria) this;
        }

        public Criteria andPermiteReservarPromocionadosIsNotNull() {
            addCriterion("PERMITE_RESERVAR_PROMOCIONADOS is not null");
            return (Criteria) this;
        }

        public Criteria andPermiteReservarPromocionadosEqualTo(Boolean value) {
            addPermiteReservarPromocionadosCriterion("PERMITE_RESERVAR_PROMOCIONADOS =", value, "permiteReservarPromocionados");
            return (Criteria) this;
        }

        public Criteria andPermiteReservarPromocionadosNotEqualTo(Boolean value) {
            addPermiteReservarPromocionadosCriterion("PERMITE_RESERVAR_PROMOCIONADOS <>", value, "permiteReservarPromocionados");
            return (Criteria) this;
        }

        public Criteria andPermiteReservarPromocionadosGreaterThan(Boolean value) {
            addPermiteReservarPromocionadosCriterion("PERMITE_RESERVAR_PROMOCIONADOS >", value, "permiteReservarPromocionados");
            return (Criteria) this;
        }

        public Criteria andPermiteReservarPromocionadosGreaterThanOrEqualTo(Boolean value) {
            addPermiteReservarPromocionadosCriterion("PERMITE_RESERVAR_PROMOCIONADOS >=", value, "permiteReservarPromocionados");
            return (Criteria) this;
        }

        public Criteria andPermiteReservarPromocionadosLessThan(Boolean value) {
            addPermiteReservarPromocionadosCriterion("PERMITE_RESERVAR_PROMOCIONADOS <", value, "permiteReservarPromocionados");
            return (Criteria) this;
        }

        public Criteria andPermiteReservarPromocionadosLessThanOrEqualTo(Boolean value) {
            addPermiteReservarPromocionadosCriterion("PERMITE_RESERVAR_PROMOCIONADOS <=", value, "permiteReservarPromocionados");
            return (Criteria) this;
        }

        public Criteria andPermiteReservarPromocionadosLike(Boolean value) {
            addPermiteReservarPromocionadosCriterion("PERMITE_RESERVAR_PROMOCIONADOS like", value, "permiteReservarPromocionados");
            return (Criteria) this;
        }

        public Criteria andPermiteReservarPromocionadosNotLike(Boolean value) {
            addPermiteReservarPromocionadosCriterion("PERMITE_RESERVAR_PROMOCIONADOS not like", value, "permiteReservarPromocionados");
            return (Criteria) this;
        }

        public Criteria andPermiteReservarPromocionadosIn(List<Boolean> values) {
            addPermiteReservarPromocionadosCriterion("PERMITE_RESERVAR_PROMOCIONADOS in", values, "permiteReservarPromocionados");
            return (Criteria) this;
        }

        public Criteria andPermiteReservarPromocionadosNotIn(List<Boolean> values) {
            addPermiteReservarPromocionadosCriterion("PERMITE_RESERVAR_PROMOCIONADOS not in", values, "permiteReservarPromocionados");
            return (Criteria) this;
        }

        public Criteria andPermiteReservarPromocionadosBetween(Boolean value1, Boolean value2) {
            addPermiteReservarPromocionadosCriterion("PERMITE_RESERVAR_PROMOCIONADOS between", value1, value2, "permiteReservarPromocionados");
            return (Criteria) this;
        }

        public Criteria andPermiteReservarPromocionadosNotBetween(Boolean value1, Boolean value2) {
            addPermiteReservarPromocionadosCriterion("PERMITE_RESERVAR_PROMOCIONADOS not between", value1, value2, "permiteReservarPromocionados");
            return (Criteria) this;
        }

        public Criteria andAbonosMayoresATotalIsNull() {
            addCriterion("ABONOS_MAYORES_A_TOTAL is null");
            return (Criteria) this;
        }

        public Criteria andAbonosMayoresATotalIsNotNull() {
            addCriterion("ABONOS_MAYORES_A_TOTAL is not null");
            return (Criteria) this;
        }

        public Criteria andAbonosMayoresATotalEqualTo(Boolean value) {
            addAbonosMayoresATotalCriterion("ABONOS_MAYORES_A_TOTAL =", value, "abonosMayoresATotal");
            return (Criteria) this;
        }

        public Criteria andAbonosMayoresATotalNotEqualTo(Boolean value) {
            addAbonosMayoresATotalCriterion("ABONOS_MAYORES_A_TOTAL <>", value, "abonosMayoresATotal");
            return (Criteria) this;
        }

        public Criteria andAbonosMayoresATotalGreaterThan(Boolean value) {
            addAbonosMayoresATotalCriterion("ABONOS_MAYORES_A_TOTAL >", value, "abonosMayoresATotal");
            return (Criteria) this;
        }

        public Criteria andAbonosMayoresATotalGreaterThanOrEqualTo(Boolean value) {
            addAbonosMayoresATotalCriterion("ABONOS_MAYORES_A_TOTAL >=", value, "abonosMayoresATotal");
            return (Criteria) this;
        }

        public Criteria andAbonosMayoresATotalLessThan(Boolean value) {
            addAbonosMayoresATotalCriterion("ABONOS_MAYORES_A_TOTAL <", value, "abonosMayoresATotal");
            return (Criteria) this;
        }

        public Criteria andAbonosMayoresATotalLessThanOrEqualTo(Boolean value) {
            addAbonosMayoresATotalCriterion("ABONOS_MAYORES_A_TOTAL <=", value, "abonosMayoresATotal");
            return (Criteria) this;
        }

        public Criteria andAbonosMayoresATotalLike(Boolean value) {
            addAbonosMayoresATotalCriterion("ABONOS_MAYORES_A_TOTAL like", value, "abonosMayoresATotal");
            return (Criteria) this;
        }

        public Criteria andAbonosMayoresATotalNotLike(Boolean value) {
            addAbonosMayoresATotalCriterion("ABONOS_MAYORES_A_TOTAL not like", value, "abonosMayoresATotal");
            return (Criteria) this;
        }

        public Criteria andAbonosMayoresATotalIn(List<Boolean> values) {
            addAbonosMayoresATotalCriterion("ABONOS_MAYORES_A_TOTAL in", values, "abonosMayoresATotal");
            return (Criteria) this;
        }

        public Criteria andAbonosMayoresATotalNotIn(List<Boolean> values) {
            addAbonosMayoresATotalCriterion("ABONOS_MAYORES_A_TOTAL not in", values, "abonosMayoresATotal");
            return (Criteria) this;
        }

        public Criteria andAbonosMayoresATotalBetween(Boolean value1, Boolean value2) {
            addAbonosMayoresATotalCriterion("ABONOS_MAYORES_A_TOTAL between", value1, value2, "abonosMayoresATotal");
            return (Criteria) this;
        }

        public Criteria andAbonosMayoresATotalNotBetween(Boolean value1, Boolean value2) {
            addAbonosMayoresATotalCriterion("ABONOS_MAYORES_A_TOTAL not between", value1, value2, "abonosMayoresATotal");
            return (Criteria) this;
        }

        public Criteria andPlazoReservacionIsNull() {
            addCriterion("PLAZO_RESERVACION is null");
            return (Criteria) this;
        }

        public Criteria andPlazoReservacionIsNotNull() {
            addCriterion("PLAZO_RESERVACION is not null");
            return (Criteria) this;
        }

        public Criteria andPlazoReservacionEqualTo(BigDecimal value) {
            addCriterion("PLAZO_RESERVACION =", value, "plazoReservacion");
            return (Criteria) this;
        }

        public Criteria andPlazoReservacionNotEqualTo(BigDecimal value) {
            addCriterion("PLAZO_RESERVACION <>", value, "plazoReservacion");
            return (Criteria) this;
        }

        public Criteria andPlazoReservacionGreaterThan(BigDecimal value) {
            addCriterion("PLAZO_RESERVACION >", value, "plazoReservacion");
            return (Criteria) this;
        }

        public Criteria andPlazoReservacionGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("PLAZO_RESERVACION >=", value, "plazoReservacion");
            return (Criteria) this;
        }

        public Criteria andPlazoReservacionLessThan(BigDecimal value) {
            addCriterion("PLAZO_RESERVACION <", value, "plazoReservacion");
            return (Criteria) this;
        }

        public Criteria andPlazoReservacionLessThanOrEqualTo(BigDecimal value) {
            addCriterion("PLAZO_RESERVACION <=", value, "plazoReservacion");
            return (Criteria) this;
        }

        public Criteria andPlazoReservacionIn(List<BigDecimal> values) {
            addCriterion("PLAZO_RESERVACION in", values, "plazoReservacion");
            return (Criteria) this;
        }

        public Criteria andPlazoReservacionNotIn(List<BigDecimal> values) {
            addCriterion("PLAZO_RESERVACION not in", values, "plazoReservacion");
            return (Criteria) this;
        }

        public Criteria andPlazoReservacionBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PLAZO_RESERVACION between", value1, value2, "plazoReservacion");
            return (Criteria) this;
        }

        public Criteria andPlazoReservacionNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PLAZO_RESERVACION not between", value1, value2, "plazoReservacion");
            return (Criteria) this;
        }

        public Criteria andPermiteEliminarArticulosIsNull() {
            addCriterion("PERMITE_ELIMINAR_ARTICULOS is null");
            return (Criteria) this;
        }

        public Criteria andPermiteEliminarArticulosIsNotNull() {
            addCriterion("PERMITE_ELIMINAR_ARTICULOS is not null");
            return (Criteria) this;
        }

        public Criteria andPermiteEliminarArticulosEqualTo(Boolean value) {
            addPermiteEliminarArticulosCriterion("PERMITE_ELIMINAR_ARTICULOS =", value, "permiteEliminarArticulos");
            return (Criteria) this;
        }

        public Criteria andPermiteEliminarArticulosNotEqualTo(Boolean value) {
            addPermiteEliminarArticulosCriterion("PERMITE_ELIMINAR_ARTICULOS <>", value, "permiteEliminarArticulos");
            return (Criteria) this;
        }

        public Criteria andPermiteEliminarArticulosGreaterThan(Boolean value) {
            addPermiteEliminarArticulosCriterion("PERMITE_ELIMINAR_ARTICULOS >", value, "permiteEliminarArticulos");
            return (Criteria) this;
        }

        public Criteria andPermiteEliminarArticulosGreaterThanOrEqualTo(Boolean value) {
            addPermiteEliminarArticulosCriterion("PERMITE_ELIMINAR_ARTICULOS >=", value, "permiteEliminarArticulos");
            return (Criteria) this;
        }

        public Criteria andPermiteEliminarArticulosLessThan(Boolean value) {
            addPermiteEliminarArticulosCriterion("PERMITE_ELIMINAR_ARTICULOS <", value, "permiteEliminarArticulos");
            return (Criteria) this;
        }

        public Criteria andPermiteEliminarArticulosLessThanOrEqualTo(Boolean value) {
            addPermiteEliminarArticulosCriterion("PERMITE_ELIMINAR_ARTICULOS <=", value, "permiteEliminarArticulos");
            return (Criteria) this;
        }

        public Criteria andPermiteEliminarArticulosLike(Boolean value) {
            addPermiteEliminarArticulosCriterion("PERMITE_ELIMINAR_ARTICULOS like", value, "permiteEliminarArticulos");
            return (Criteria) this;
        }

        public Criteria andPermiteEliminarArticulosNotLike(Boolean value) {
            addPermiteEliminarArticulosCriterion("PERMITE_ELIMINAR_ARTICULOS not like", value, "permiteEliminarArticulos");
            return (Criteria) this;
        }

        public Criteria andPermiteEliminarArticulosIn(List<Boolean> values) {
            addPermiteEliminarArticulosCriterion("PERMITE_ELIMINAR_ARTICULOS in", values, "permiteEliminarArticulos");
            return (Criteria) this;
        }

        public Criteria andPermiteEliminarArticulosNotIn(List<Boolean> values) {
            addPermiteEliminarArticulosCriterion("PERMITE_ELIMINAR_ARTICULOS not in", values, "permiteEliminarArticulos");
            return (Criteria) this;
        }

        public Criteria andPermiteEliminarArticulosBetween(Boolean value1, Boolean value2) {
            addPermiteEliminarArticulosCriterion("PERMITE_ELIMINAR_ARTICULOS between", value1, value2, "permiteEliminarArticulos");
            return (Criteria) this;
        }

        public Criteria andPermiteEliminarArticulosNotBetween(Boolean value1, Boolean value2) {
            addPermiteEliminarArticulosCriterion("PERMITE_ELIMINAR_ARTICULOS not between", value1, value2, "permiteEliminarArticulos");
            return (Criteria) this;
        }

        public Criteria andPermiteLiquidacionParcialIsNull() {
            addCriterion("PERMITE_LIQUIDACION_PARCIAL is null");
            return (Criteria) this;
        }

        public Criteria andPermiteLiquidacionParcialIsNotNull() {
            addCriterion("PERMITE_LIQUIDACION_PARCIAL is not null");
            return (Criteria) this;
        }

        public Criteria andPermiteLiquidacionParcialEqualTo(Boolean value) {
            addPermiteLiquidacionParcialCriterion("PERMITE_LIQUIDACION_PARCIAL =", value, "permiteLiquidacionParcial");
            return (Criteria) this;
        }

        public Criteria andPermiteLiquidacionParcialNotEqualTo(Boolean value) {
            addPermiteLiquidacionParcialCriterion("PERMITE_LIQUIDACION_PARCIAL <>", value, "permiteLiquidacionParcial");
            return (Criteria) this;
        }

        public Criteria andPermiteLiquidacionParcialGreaterThan(Boolean value) {
            addPermiteLiquidacionParcialCriterion("PERMITE_LIQUIDACION_PARCIAL >", value, "permiteLiquidacionParcial");
            return (Criteria) this;
        }

        public Criteria andPermiteLiquidacionParcialGreaterThanOrEqualTo(Boolean value) {
            addPermiteLiquidacionParcialCriterion("PERMITE_LIQUIDACION_PARCIAL >=", value, "permiteLiquidacionParcial");
            return (Criteria) this;
        }

        public Criteria andPermiteLiquidacionParcialLessThan(Boolean value) {
            addPermiteLiquidacionParcialCriterion("PERMITE_LIQUIDACION_PARCIAL <", value, "permiteLiquidacionParcial");
            return (Criteria) this;
        }

        public Criteria andPermiteLiquidacionParcialLessThanOrEqualTo(Boolean value) {
            addPermiteLiquidacionParcialCriterion("PERMITE_LIQUIDACION_PARCIAL <=", value, "permiteLiquidacionParcial");
            return (Criteria) this;
        }

        public Criteria andPermiteLiquidacionParcialLike(Boolean value) {
            addPermiteLiquidacionParcialCriterion("PERMITE_LIQUIDACION_PARCIAL like", value, "permiteLiquidacionParcial");
            return (Criteria) this;
        }

        public Criteria andPermiteLiquidacionParcialNotLike(Boolean value) {
            addPermiteLiquidacionParcialCriterion("PERMITE_LIQUIDACION_PARCIAL not like", value, "permiteLiquidacionParcial");
            return (Criteria) this;
        }

        public Criteria andPermiteLiquidacionParcialIn(List<Boolean> values) {
            addPermiteLiquidacionParcialCriterion("PERMITE_LIQUIDACION_PARCIAL in", values, "permiteLiquidacionParcial");
            return (Criteria) this;
        }

        public Criteria andPermiteLiquidacionParcialNotIn(List<Boolean> values) {
            addPermiteLiquidacionParcialCriterion("PERMITE_LIQUIDACION_PARCIAL not in", values, "permiteLiquidacionParcial");
            return (Criteria) this;
        }

        public Criteria andPermiteLiquidacionParcialBetween(Boolean value1, Boolean value2) {
            addPermiteLiquidacionParcialCriterion("PERMITE_LIQUIDACION_PARCIAL between", value1, value2, "permiteLiquidacionParcial");
            return (Criteria) this;
        }

        public Criteria andPermiteLiquidacionParcialNotBetween(Boolean value1, Boolean value2) {
            addPermiteLiquidacionParcialCriterion("PERMITE_LIQUIDACION_PARCIAL not between", value1, value2, "permiteLiquidacionParcial");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosPropietarioIsNull() {
            addCriterion("PERMITE_ABONOS_PROPIETARIO is null");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosPropietarioIsNotNull() {
            addCriterion("PERMITE_ABONOS_PROPIETARIO is not null");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosPropietarioEqualTo(Boolean value) {
            addPermiteAbonosPropietarioCriterion("PERMITE_ABONOS_PROPIETARIO =", value, "permiteAbonosPropietario");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosPropietarioNotEqualTo(Boolean value) {
            addPermiteAbonosPropietarioCriterion("PERMITE_ABONOS_PROPIETARIO <>", value, "permiteAbonosPropietario");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosPropietarioGreaterThan(Boolean value) {
            addPermiteAbonosPropietarioCriterion("PERMITE_ABONOS_PROPIETARIO >", value, "permiteAbonosPropietario");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosPropietarioGreaterThanOrEqualTo(Boolean value) {
            addPermiteAbonosPropietarioCriterion("PERMITE_ABONOS_PROPIETARIO >=", value, "permiteAbonosPropietario");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosPropietarioLessThan(Boolean value) {
            addPermiteAbonosPropietarioCriterion("PERMITE_ABONOS_PROPIETARIO <", value, "permiteAbonosPropietario");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosPropietarioLessThanOrEqualTo(Boolean value) {
            addPermiteAbonosPropietarioCriterion("PERMITE_ABONOS_PROPIETARIO <=", value, "permiteAbonosPropietario");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosPropietarioLike(Boolean value) {
            addPermiteAbonosPropietarioCriterion("PERMITE_ABONOS_PROPIETARIO like", value, "permiteAbonosPropietario");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosPropietarioNotLike(Boolean value) {
            addPermiteAbonosPropietarioCriterion("PERMITE_ABONOS_PROPIETARIO not like", value, "permiteAbonosPropietario");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosPropietarioIn(List<Boolean> values) {
            addPermiteAbonosPropietarioCriterion("PERMITE_ABONOS_PROPIETARIO in", values, "permiteAbonosPropietario");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosPropietarioNotIn(List<Boolean> values) {
            addPermiteAbonosPropietarioCriterion("PERMITE_ABONOS_PROPIETARIO not in", values, "permiteAbonosPropietario");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosPropietarioBetween(Boolean value1, Boolean value2) {
            addPermiteAbonosPropietarioCriterion("PERMITE_ABONOS_PROPIETARIO between", value1, value2, "permiteAbonosPropietario");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosPropietarioNotBetween(Boolean value1, Boolean value2) {
            addPermiteAbonosPropietarioCriterion("PERMITE_ABONOS_PROPIETARIO not between", value1, value2, "permiteAbonosPropietario");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosInvitadosIsNull() {
            addCriterion("PERMITE_ABONOS_INVITADOS is null");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosInvitadosIsNotNull() {
            addCriterion("PERMITE_ABONOS_INVITADOS is not null");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosInvitadosEqualTo(Boolean value) {
            addPermiteAbonosInvitadosCriterion("PERMITE_ABONOS_INVITADOS =", value, "permiteAbonosInvitados");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosInvitadosNotEqualTo(Boolean value) {
            addPermiteAbonosInvitadosCriterion("PERMITE_ABONOS_INVITADOS <>", value, "permiteAbonosInvitados");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosInvitadosGreaterThan(Boolean value) {
            addPermiteAbonosInvitadosCriterion("PERMITE_ABONOS_INVITADOS >", value, "permiteAbonosInvitados");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosInvitadosGreaterThanOrEqualTo(Boolean value) {
            addPermiteAbonosInvitadosCriterion("PERMITE_ABONOS_INVITADOS >=", value, "permiteAbonosInvitados");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosInvitadosLessThan(Boolean value) {
            addPermiteAbonosInvitadosCriterion("PERMITE_ABONOS_INVITADOS <", value, "permiteAbonosInvitados");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosInvitadosLessThanOrEqualTo(Boolean value) {
            addPermiteAbonosInvitadosCriterion("PERMITE_ABONOS_INVITADOS <=", value, "permiteAbonosInvitados");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosInvitadosLike(Boolean value) {
            addPermiteAbonosInvitadosCriterion("PERMITE_ABONOS_INVITADOS like", value, "permiteAbonosInvitados");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosInvitadosNotLike(Boolean value) {
            addPermiteAbonosInvitadosCriterion("PERMITE_ABONOS_INVITADOS not like", value, "permiteAbonosInvitados");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosInvitadosIn(List<Boolean> values) {
            addPermiteAbonosInvitadosCriterion("PERMITE_ABONOS_INVITADOS in", values, "permiteAbonosInvitados");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosInvitadosNotIn(List<Boolean> values) {
            addPermiteAbonosInvitadosCriterion("PERMITE_ABONOS_INVITADOS not in", values, "permiteAbonosInvitados");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosInvitadosBetween(Boolean value1, Boolean value2) {
            addPermiteAbonosInvitadosCriterion("PERMITE_ABONOS_INVITADOS between", value1, value2, "permiteAbonosInvitados");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosInvitadosNotBetween(Boolean value1, Boolean value2) {
            addPermiteAbonosInvitadosCriterion("PERMITE_ABONOS_INVITADOS not between", value1, value2, "permiteAbonosInvitados");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosParcialesIsNull() {
            addCriterion("PERMITE_ABONOS_PARCIALES is null");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosParcialesIsNotNull() {
            addCriterion("PERMITE_ABONOS_PARCIALES is not null");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosParcialesEqualTo(Boolean value) {
            addPermiteAbonosParcialesCriterion("PERMITE_ABONOS_PARCIALES =", value, "permiteAbonosParciales");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosParcialesNotEqualTo(Boolean value) {
            addPermiteAbonosParcialesCriterion("PERMITE_ABONOS_PARCIALES <>", value, "permiteAbonosParciales");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosParcialesGreaterThan(Boolean value) {
            addPermiteAbonosParcialesCriterion("PERMITE_ABONOS_PARCIALES >", value, "permiteAbonosParciales");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosParcialesGreaterThanOrEqualTo(Boolean value) {
            addPermiteAbonosParcialesCriterion("PERMITE_ABONOS_PARCIALES >=", value, "permiteAbonosParciales");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosParcialesLessThan(Boolean value) {
            addPermiteAbonosParcialesCriterion("PERMITE_ABONOS_PARCIALES <", value, "permiteAbonosParciales");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosParcialesLessThanOrEqualTo(Boolean value) {
            addPermiteAbonosParcialesCriterion("PERMITE_ABONOS_PARCIALES <=", value, "permiteAbonosParciales");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosParcialesLike(Boolean value) {
            addPermiteAbonosParcialesCriterion("PERMITE_ABONOS_PARCIALES like", value, "permiteAbonosParciales");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosParcialesNotLike(Boolean value) {
            addPermiteAbonosParcialesCriterion("PERMITE_ABONOS_PARCIALES not like", value, "permiteAbonosParciales");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosParcialesIn(List<Boolean> values) {
            addPermiteAbonosParcialesCriterion("PERMITE_ABONOS_PARCIALES in", values, "permiteAbonosParciales");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosParcialesNotIn(List<Boolean> values) {
            addPermiteAbonosParcialesCriterion("PERMITE_ABONOS_PARCIALES not in", values, "permiteAbonosParciales");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosParcialesBetween(Boolean value1, Boolean value2) {
            addPermiteAbonosParcialesCriterion("PERMITE_ABONOS_PARCIALES between", value1, value2, "permiteAbonosParciales");
            return (Criteria) this;
        }

        public Criteria andPermiteAbonosParcialesNotBetween(Boolean value1, Boolean value2) {
            addPermiteAbonosParcialesCriterion("PERMITE_ABONOS_PARCIALES not between", value1, value2, "permiteAbonosParciales");
            return (Criteria) this;
        }

        public Criteria andPermiteAltaPosIsNull() {
            addCriterion("PERMITE_ALTA_POS is null");
            return (Criteria) this;
        }

        public Criteria andPermiteAltaPosIsNotNull() {
            addCriterion("PERMITE_ALTA_POS is not null");
            return (Criteria) this;
        }

        public Criteria andPermiteAltaPosEqualTo(Boolean value) {
            addPermiteAltaPosCriterion("PERMITE_ALTA_POS =", value, "permiteAltaPos");
            return (Criteria) this;
        }

        public Criteria andPermiteAltaPosNotEqualTo(Boolean value) {
            addPermiteAltaPosCriterion("PERMITE_ALTA_POS <>", value, "permiteAltaPos");
            return (Criteria) this;
        }

        public Criteria andPermiteAltaPosGreaterThan(Boolean value) {
            addPermiteAltaPosCriterion("PERMITE_ALTA_POS >", value, "permiteAltaPos");
            return (Criteria) this;
        }

        public Criteria andPermiteAltaPosGreaterThanOrEqualTo(Boolean value) {
            addPermiteAltaPosCriterion("PERMITE_ALTA_POS >=", value, "permiteAltaPos");
            return (Criteria) this;
        }

        public Criteria andPermiteAltaPosLessThan(Boolean value) {
            addPermiteAltaPosCriterion("PERMITE_ALTA_POS <", value, "permiteAltaPos");
            return (Criteria) this;
        }

        public Criteria andPermiteAltaPosLessThanOrEqualTo(Boolean value) {
            addPermiteAltaPosCriterion("PERMITE_ALTA_POS <=", value, "permiteAltaPos");
            return (Criteria) this;
        }

        public Criteria andPermiteAltaPosLike(Boolean value) {
            addPermiteAltaPosCriterion("PERMITE_ALTA_POS like", value, "permiteAltaPos");
            return (Criteria) this;
        }

        public Criteria andPermiteAltaPosNotLike(Boolean value) {
            addPermiteAltaPosCriterion("PERMITE_ALTA_POS not like", value, "permiteAltaPos");
            return (Criteria) this;
        }

        public Criteria andPermiteAltaPosIn(List<Boolean> values) {
            addPermiteAltaPosCriterion("PERMITE_ALTA_POS in", values, "permiteAltaPos");
            return (Criteria) this;
        }

        public Criteria andPermiteAltaPosNotIn(List<Boolean> values) {
            addPermiteAltaPosCriterion("PERMITE_ALTA_POS not in", values, "permiteAltaPos");
            return (Criteria) this;
        }

        public Criteria andPermiteAltaPosBetween(Boolean value1, Boolean value2) {
            addPermiteAltaPosCriterion("PERMITE_ALTA_POS between", value1, value2, "permiteAltaPos");
            return (Criteria) this;
        }

        public Criteria andPermiteAltaPosNotBetween(Boolean value1, Boolean value2) {
            addPermiteAltaPosCriterion("PERMITE_ALTA_POS not between", value1, value2, "permiteAltaPos");
            return (Criteria) this;
        }

        public Criteria andPermiteDatosAdicionalesIsNull() {
            addCriterion("PERMITE_DATOS_ADICIONALES is null");
            return (Criteria) this;
        }

        public Criteria andPermiteDatosAdicionalesIsNotNull() {
            addCriterion("PERMITE_DATOS_ADICIONALES is not null");
            return (Criteria) this;
        }

        public Criteria andPermiteDatosAdicionalesEqualTo(Boolean value) {
            addPermiteDatosAdicionalesCriterion("PERMITE_DATOS_ADICIONALES =", value, "permiteDatosAdicionales");
            return (Criteria) this;
        }

        public Criteria andPermiteDatosAdicionalesNotEqualTo(Boolean value) {
            addPermiteDatosAdicionalesCriterion("PERMITE_DATOS_ADICIONALES <>", value, "permiteDatosAdicionales");
            return (Criteria) this;
        }

        public Criteria andPermiteDatosAdicionalesGreaterThan(Boolean value) {
            addPermiteDatosAdicionalesCriterion("PERMITE_DATOS_ADICIONALES >", value, "permiteDatosAdicionales");
            return (Criteria) this;
        }

        public Criteria andPermiteDatosAdicionalesGreaterThanOrEqualTo(Boolean value) {
            addPermiteDatosAdicionalesCriterion("PERMITE_DATOS_ADICIONALES >=", value, "permiteDatosAdicionales");
            return (Criteria) this;
        }

        public Criteria andPermiteDatosAdicionalesLessThan(Boolean value) {
            addPermiteDatosAdicionalesCriterion("PERMITE_DATOS_ADICIONALES <", value, "permiteDatosAdicionales");
            return (Criteria) this;
        }

        public Criteria andPermiteDatosAdicionalesLessThanOrEqualTo(Boolean value) {
            addPermiteDatosAdicionalesCriterion("PERMITE_DATOS_ADICIONALES <=", value, "permiteDatosAdicionales");
            return (Criteria) this;
        }

        public Criteria andPermiteDatosAdicionalesLike(Boolean value) {
            addPermiteDatosAdicionalesCriterion("PERMITE_DATOS_ADICIONALES like", value, "permiteDatosAdicionales");
            return (Criteria) this;
        }

        public Criteria andPermiteDatosAdicionalesNotLike(Boolean value) {
            addPermiteDatosAdicionalesCriterion("PERMITE_DATOS_ADICIONALES not like", value, "permiteDatosAdicionales");
            return (Criteria) this;
        }

        public Criteria andPermiteDatosAdicionalesIn(List<Boolean> values) {
            addPermiteDatosAdicionalesCriterion("PERMITE_DATOS_ADICIONALES in", values, "permiteDatosAdicionales");
            return (Criteria) this;
        }

        public Criteria andPermiteDatosAdicionalesNotIn(List<Boolean> values) {
            addPermiteDatosAdicionalesCriterion("PERMITE_DATOS_ADICIONALES not in", values, "permiteDatosAdicionales");
            return (Criteria) this;
        }

        public Criteria andPermiteDatosAdicionalesBetween(Boolean value1, Boolean value2) {
            addPermiteDatosAdicionalesCriterion("PERMITE_DATOS_ADICIONALES between", value1, value2, "permiteDatosAdicionales");
            return (Criteria) this;
        }

        public Criteria andPermiteDatosAdicionalesNotBetween(Boolean value1, Boolean value2) {
            addPermiteDatosAdicionalesCriterion("PERMITE_DATOS_ADICIONALES not between", value1, value2, "permiteDatosAdicionales");
            return (Criteria) this;
        }

        public Criteria andPermiteFacturarInvitadoIsNull() {
            addCriterion("PERMITE_FACTURAR_INVITADO is null");
            return (Criteria) this;
        }

        public Criteria andPermiteFacturarInvitadoIsNotNull() {
            addCriterion("PERMITE_FACTURAR_INVITADO is not null");
            return (Criteria) this;
        }

        public Criteria andPermiteFacturarInvitadoEqualTo(Boolean value) {
            addPermiteFacturarInvitadoCriterion("PERMITE_FACTURAR_INVITADO =", value, "permiteFacturarInvitado");
            return (Criteria) this;
        }

        public Criteria andPermiteFacturarInvitadoNotEqualTo(Boolean value) {
            addPermiteFacturarInvitadoCriterion("PERMITE_FACTURAR_INVITADO <>", value, "permiteFacturarInvitado");
            return (Criteria) this;
        }

        public Criteria andPermiteFacturarInvitadoGreaterThan(Boolean value) {
            addPermiteFacturarInvitadoCriterion("PERMITE_FACTURAR_INVITADO >", value, "permiteFacturarInvitado");
            return (Criteria) this;
        }

        public Criteria andPermiteFacturarInvitadoGreaterThanOrEqualTo(Boolean value) {
            addPermiteFacturarInvitadoCriterion("PERMITE_FACTURAR_INVITADO >=", value, "permiteFacturarInvitado");
            return (Criteria) this;
        }

        public Criteria andPermiteFacturarInvitadoLessThan(Boolean value) {
            addPermiteFacturarInvitadoCriterion("PERMITE_FACTURAR_INVITADO <", value, "permiteFacturarInvitado");
            return (Criteria) this;
        }

        public Criteria andPermiteFacturarInvitadoLessThanOrEqualTo(Boolean value) {
            addPermiteFacturarInvitadoCriterion("PERMITE_FACTURAR_INVITADO <=", value, "permiteFacturarInvitado");
            return (Criteria) this;
        }

        public Criteria andPermiteFacturarInvitadoLike(Boolean value) {
            addPermiteFacturarInvitadoCriterion("PERMITE_FACTURAR_INVITADO like", value, "permiteFacturarInvitado");
            return (Criteria) this;
        }

        public Criteria andPermiteFacturarInvitadoNotLike(Boolean value) {
            addPermiteFacturarInvitadoCriterion("PERMITE_FACTURAR_INVITADO not like", value, "permiteFacturarInvitado");
            return (Criteria) this;
        }

        public Criteria andPermiteFacturarInvitadoIn(List<Boolean> values) {
            addPermiteFacturarInvitadoCriterion("PERMITE_FACTURAR_INVITADO in", values, "permiteFacturarInvitado");
            return (Criteria) this;
        }

        public Criteria andPermiteFacturarInvitadoNotIn(List<Boolean> values) {
            addPermiteFacturarInvitadoCriterion("PERMITE_FACTURAR_INVITADO not in", values, "permiteFacturarInvitado");
            return (Criteria) this;
        }

        public Criteria andPermiteFacturarInvitadoBetween(Boolean value1, Boolean value2) {
            addPermiteFacturarInvitadoCriterion("PERMITE_FACTURAR_INVITADO between", value1, value2, "permiteFacturarInvitado");
            return (Criteria) this;
        }

        public Criteria andPermiteFacturarInvitadoNotBetween(Boolean value1, Boolean value2) {
            addPermiteFacturarInvitadoCriterion("PERMITE_FACTURAR_INVITADO not between", value1, value2, "permiteFacturarInvitado");
            return (Criteria) this;
        }

        public Criteria andCodTipoLikeInsensitive(String value) {
            addCriterion("upper(COD_TIPO) like", value.toUpperCase(), "codTipo");
            return (Criteria) this;
        }

        public Criteria andDesTipoLikeInsensitive(String value) {
            addCriterion("upper(DES_TIPO) like", value.toUpperCase(), "desTipo");
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