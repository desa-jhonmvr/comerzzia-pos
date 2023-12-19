package com.comerzzia.jpos.util.mybatis;

import com.comerzzia.jpos.persistencia.bonoEfectivo.BonoEfectivoMapper;
import com.comerzzia.jpos.persistencia.cotizaciones.CotizacionMapper;
import com.comerzzia.jpos.persistencia.credito.abonos.AbonoCreditoMapper;
import com.comerzzia.jpos.persistencia.letras.LetraMapper;
import com.comerzzia.jpos.persistencia.letras.detalles.LetraCuotaMapper;
import com.comerzzia.jpos.persistencia.listapda.SesionPdaMapper;
import com.comerzzia.jpos.persistencia.listapda.detalle.DetalleSesionPdaMapper;
import com.comerzzia.jpos.persistencia.print.documentos.DocumentosMapper;
import com.comerzzia.jpos.persistencia.print.documentos.impresos.DocumentosImpresosMapper;
import com.comerzzia.jpos.persistencia.promociones.clientes.PromocionClienteMapper;
import com.comerzzia.jpos.persistencia.promociones.configuracion.billeton.ConfiguracionBilletonMapper;
import com.comerzzia.jpos.persistencia.promociones.configuracion.billeton.detalles.ConfiguracionBilletonDetalleMapper;
import com.comerzzia.jpos.persistencia.puntos.acumulacion.AcumulacionMapper;
import com.comerzzia.jpos.persistencia.puntos.consumo.ConsumoMapper;
import com.comerzzia.jpos.persistencia.reservaciones.reserva.ReservaMapper;
import com.comerzzia.jpos.persistencia.reservaciones.reservaabono.ReservaAbonoMapper;
import com.comerzzia.jpos.persistencia.reservaciones.reservaarticulo.ReservaArticuloMapper;
import com.comerzzia.jpos.persistencia.reservaciones.reservadatosfact.FacturacionTicketMapper;
import com.comerzzia.jpos.persistencia.reservaciones.reservainvitados.ReservaInvitadoMapper;
import com.comerzzia.jpos.persistencia.reservaciones.reservatipos.ReservaTiposMapper;
import com.comerzzia.jpos.persistencia.sukupon.SukuponMapper;
import com.comerzzia.jpos.servicios.login.Sesion;
import java.sql.Connection;


import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import es.mpsistemas.util.log.Logger;

import es.mpsistemas.util.mybatis.typehandlers.BooleanTypeHandler;
import es.mpsistemas.util.mybatis.typehandlers.FechaHoraTypeHandler;
import es.mpsistemas.util.mybatis.typehandlers.FechaTypeHandler;
import java.util.Properties;
import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;

public class SessionFactory {

    private static Logger log = Logger.getMLogger(SessionFactory.class);
    private static SqlSessionFactory sqlSessionFactory = null;

    private static void addMappers(Configuration configuration) {
        configuration.addMapper(LetraMapper.class);
        configuration.addMapper(LetraCuotaMapper.class);
        configuration.addMapper(AbonoCreditoMapper.class);
        configuration.addMapper(ConsumoMapper.class);
        configuration.addMapper(AcumulacionMapper.class);
        configuration.addMapper(PromocionClienteMapper.class);
        configuration.addMapper(ConfiguracionBilletonMapper.class);
        configuration.addMapper(ConfiguracionBilletonDetalleMapper.class);
        configuration.addMapper(DocumentosMapper.class);
        configuration.addMapper(DocumentosImpresosMapper.class);
        configuration.addMapper(CotizacionMapper.class);
        configuration.addMapper(ReservaMapper.class);
        configuration.addMapper(ReservaAbonoMapper.class);
        configuration.addMapper(ReservaArticuloMapper.class);
        configuration.addMapper(FacturacionTicketMapper.class);
        configuration.addMapper(ReservaInvitadoMapper.class);
        configuration.addMapper(ReservaTiposMapper.class);
        configuration.addMapper(SesionPdaMapper.class);
        configuration.addMapper(DetalleSesionPdaMapper.class);
        configuration.addMapper(BonoEfectivoMapper.class);
        configuration.addMapper(SukuponMapper.class);
    }

    private static void createSessionFactory() {
        try {
            PooledDataSourceFactory dataSourceFactory = new PooledDataSourceFactory();
            Properties prop = new Properties();
            prop.put("driver", Sesion.datosDatabase.getDriver());
            prop.put("url", Sesion.datosDatabase.getUrlConexion());
            prop.put("username", Sesion.datosDatabase.getUsuario());
            prop.put("password", Sesion.datosDatabase.getPassword());
            dataSourceFactory.setProperties(prop);
            TransactionFactory transactionFactory = new JdbcTransactionFactory();
            Environment environment = new Environment("development", transactionFactory, dataSourceFactory.getDataSource());
            Configuration configuration = new Configuration(environment);
            addMappers(configuration);
            configuration.getTypeHandlerRegistry().register(BooleanTypeHandler.class);
            configuration.getTypeHandlerRegistry().register(FechaHoraTypeHandler.class);
            configuration.getTypeHandlerRegistry().register(FechaTypeHandler.class);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        }
        catch (Exception e) {
            log.error("createSessionFactory() - Error creando session factory: " + e.getMessage(), e);
        }
    }

    private static SqlSessionFactory getSessionFactory() {
        if (sqlSessionFactory == null) {
            createSessionFactory();
        }
        return sqlSessionFactory;
    }

    public static SqlSession openSession() {
        return getSessionFactory().openSession();
    }

    public static SqlSession openSession(Connection conn) {
        return getSessionFactory().openSession(conn);
    }
}
