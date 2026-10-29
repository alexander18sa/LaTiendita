package com.discount3a.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de la clase Venta.
 */
class VentaTest {

    private Producto arroz;
    private Producto leche;

    @BeforeEach
    void preparar() {
        arroz = new Producto("P001", "Arroz 1kg", "Abarrotes", 4.50, 20);
        leche = new Producto("P002", "Leche 400g", "Lácteos", 3.20, 10);
    }

    @Test
    void calculaElTotalDeLaVenta() {
        List<ItemVenta> items = new ArrayList<>();
        items.add(new ItemVenta(arroz, 2));
        items.add(new ItemVenta(leche, 3));

        Venta venta = new Venta("Ana", items);

        assertEquals(18.60, venta.getTotal(), 0.001);
    }

    @Test
    void noPermiteVentaSinItems() {
        assertThrows(IllegalArgumentException.class, () -> new Venta("Ana", new ArrayList<>()));
    }

    @Test
    void noPermiteCantidadCero() {
        assertThrows(IllegalArgumentException.class, () -> new ItemVenta(arroz, 0));
    }
}
