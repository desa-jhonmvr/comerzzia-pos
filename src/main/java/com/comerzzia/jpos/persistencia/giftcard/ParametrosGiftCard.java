package com.comerzzia.jpos.persistencia.giftcard;

import java.math.BigDecimal;

public class ParametrosGiftCard {

	private Long porcentaje;
	private BigDecimal monto;

	public Long getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Long porcentaje) {
		this.porcentaje = porcentaje;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

}
