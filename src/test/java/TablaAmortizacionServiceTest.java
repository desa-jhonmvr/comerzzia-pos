import com.comerzzia.jpos.servicios.credito.tabla.amortizacion.TablaAmortizacionService;

import java.math.BigDecimal;

class TablaAmortizacionServiceTest {

    public static void main(String[] args) {
        TablaAmortizacionService.init(
                BigDecimal.valueOf(Double.valueOf("68.84")),
                BigDecimal.valueOf(Double.valueOf("3")),
                BigDecimal.valueOf(Double.valueOf("0"))
        );

        System.out.println("Valores2>>>>>>>>>>>");
        System.out.println(TablaAmortizacionService.imprimir());

    }
}