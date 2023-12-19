package com.comerzzia.jpos.persistencia.bonoEfectivo;

import java.util.List;

public interface BonoEfectivoMapper {

    List<BonoEfectivoBean> selectFromByExample(BonoEfectivoExample example);
}
