package com.discount3a.structures;

import com.discount3a.modelo.Venta;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * PILA (LIFO): historial de ventas de caja. La última venta registrada
 * es la primera que se puede consultar o deshacer.
 */
public class PilaHistorialVentas {
    private final Deque<Venta> pila = new ArrayDeque<>();

    /** Apila (registra) una nueva venta en el historial. */
    public void registrarVenta(Venta venta) {
        pila.push(venta);
    }

    /** Desapila (deshace) la última venta registrada. */
    public Venta deshacerUltimaVenta() {
        return pila.isEmpty() ? null : pila.pop();
    }

    /** Consulta la última venta sin retirarla de la pila. */
    public Venta verUltima() {
        return pila.peek();
    }

    /** Devuelve el historial ordenado de la más reciente a la más antigua. */
    public List<Venta> verHistorial() {
        return new ArrayList<>(pila);
    }

    public boolean estaVacia() { return pila.isEmpty(); }
    public int tamanio() { return pila.size(); }
}
