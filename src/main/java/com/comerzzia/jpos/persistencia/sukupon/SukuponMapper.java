package com.comerzzia.jpos.persistencia.sukupon;

import java.util.List;

public interface SukuponMapper {

    List<SukuponBean> selectFromByExample(SukuponExample example);
    
}
