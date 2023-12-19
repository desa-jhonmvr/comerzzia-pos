/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.comerzzia.jpos.dto.ventas.paginaweb.placeToPay;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Gabriel Simbania
 */
public class Recurring  implements Serializable{
    
    private static final long serialVersionUID = 1963847005909198483L;
    
    private String periodicity;
    private Integer interval;
    private Date nextPayment;
    private Integer maxPeriods;
    private Date dueDate;
    private Integer notificationUrl;

    public Recurring() {
    }

    public String getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(String periodicity) {
        this.periodicity = periodicity;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Date getNextPayment() {
        return nextPayment;
    }

    public void setNextPayment(Date nextPayment) {
        this.nextPayment = nextPayment;
    }

    public Integer getMaxPeriods() {
        return maxPeriods;
    }

    public void setMaxPeriods(Integer maxPeriods) {
        this.maxPeriods = maxPeriods;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getNotificationUrl() {
        return notificationUrl;
    }

    public void setNotificationUrl(Integer notificationUrl) {
        this.notificationUrl = notificationUrl;
    }
    
    
    
}
