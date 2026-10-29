package com.discount3a.structures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.discount3a.modelo.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas del inventario y del stock de los productos.
 */
class InventarioTest {

    private InventarioVector inventario;

    @BeforeEach
    void preparar() {
        inventario = new InventarioVector();
    }

    @Test
    void agregaProductosYLosBuscaPorCodigo() {
        inventario.agregar(new Producto("P001", "Arroz 1kg", "Abarrotes", 4.50, 20));
        inventario.agregar(new Producto("P002", "Leche 400g", "Lácteos", 3.20, 10));

        assertEquals(2, inventario.tamanio());
        assertNotNull(inventario.buscarPorCodigo("p002"));
        assertNull(inventario.buscarPorCodigo("P999"));
    }

    @Test
    void creceMasAllaDeLaCapacidadInicial() {
        for (int i = 1; i <= 10; i++) {
            inventario.agregar(new Producto("P" + i, "Producto " + i, "General", 1.0, 5));
        }

        assertEquals(10, inventario.tamanio());
    }

    @Test
    void reduceYAumentaElStock() {
        Producto arroz = new Producto("P001", "Arroz 1kg", "Abarrotes", 4.50, 20);

        arroz.reducirStock(5);
        assertEquals(15, arroz.getStock());

        arroz.aumentarStock(2);
        assertEquals(17, arroz.getStock());
    }

    @Test
    void noPermiteReducirMasDelStockDisponible() {
        Producto arroz = new Producto("P001", "Arroz 1kg", "Abarrotes", 4.50, 3);

        assertThrows(IllegalArgumentException.class, () -> arroz.reducirStock(4));
    }
}
