/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.util.fechas;

import es.mpsistemas.util.fechas.Fecha;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class Fechas {

    public static final String DATE_DATA_FORMAT = "dd/MM/yyyy";
    
    public static int diferenciaDias(Fecha fecha1, Fecha fecha2) {
        Long ms = fecha1.getDate().getTime() - fecha2.getDate().getTime();
        return Math.abs(new Long(ms / 1000 / 60 / 60 / 24).intValue());
    }

    public static double diferenciaSemanas(Fecha f1, Fecha f2) {
        int diferencia = diferenciaDias(f1, f2);
        return Math.round(diferencia / 7.0);
    }

    public static java.sql.Timestamp toSqlTimestamp(java.util.Date fecha) {
        if (fecha == null) {
            return null;
        }

        return new Timestamp(fecha.getTime());
    }

    /**
     * Suma la cantidad de minutos a la fecha pasada
     *
     * @param fecha a sumar
     * @param cantidad de minutos a sumar
     * @return
     */
    public static Date sumaMinutos(Date fecha, int cantidad) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        c.add(Calendar.MINUTE, cantidad);
        fecha = c.getTime();

        return fecha;
    }

    /**
     * @author Gabriel Simbania
     * @param fecha
     * @param formato
     * @return
     */
    public static String dateToString(Date fecha, String formato) {

        SimpleDateFormat format = new SimpleDateFormat(formato);
        return format.format(fecha);
    }

    /**
     * @author Gabriel Simbania
     * @param fecha
     * @param formato
     * @return
     */
    public static Date stringToDate(String fecha, String formato) {

        SimpleDateFormat format = new SimpleDateFormat(formato);
        Date date;
        try {
            date = format.parse(fecha);
        } catch (ParseException ex) {
            date = null;
        }
        return date;
    }

    /**
     *
     * @return
     */
    public static Timestamp actualDateTime() {
        return new Timestamp(new Date().getTime());
    }

    /**
     * <p>
     * <b>author: </b> Gabriel Simbania</p>
     * <p>
     * Obtiene la el dia, mes o anio dependidendo que se envie por en
     * calendarType  </p>
     *
     *
     *
     * @param date
     * @param calendarType Calendar.DAY_OF_WEEK, Calendar.YEAR, etc
     * @return
     */
    public static int getDateByCalendar(Date date, int calendarType) {

        Calendar fecha = Calendar.getInstance();
        fecha.setTime(date);
        return fecha.get(calendarType);

    }
}
