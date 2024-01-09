import com.comerzzia.jpos.servicios.credito.tabla.amortizacion.TablaAmortizacionService;

import java.math.BigDecimal;

class TablaAmortizacionServiceTest {

    public static void main(String[] args) {
        TablaAmortizacionService.init(BigDecimal.valueOf(Double.valueOf("1426.95")),
                BigDecimal.valueOf(Double.valueOf("36")),
                BigDecimal.valueOf(Double.valueOf("15.5")),null
        );

        System.out.println("Valores2>>>>>>>>>>>");
        System.out.println(TablaAmortizacionService.imprimir());

    }
}