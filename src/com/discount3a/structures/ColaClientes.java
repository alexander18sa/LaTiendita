package com.discount3a.structures;

import com.discount3a.modelo.Cliente;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * COLA (FIFO): fila de espera de clientes para ser atendidos en caja.
 * El primer cliente en llegar es el primero en ser atendido.
 */
public class ColaClientes {
    private final Queue<Cliente> cola = new LinkedList<>();

    /** Encola un cliente al final de la fila. */
    public void encolar(Cliente cliente) {
        cola.offer(cliente);
    }

    /** Atiende (retira) al cliente que está al frente de la fila. */
    public Cliente atenderSiguiente() {
        return cola.poll();
    }

    /** Devuelve una copia de la fila actual, sin modificarla. */
    public List<Cliente> verCola() {
        return new LinkedList<>(cola);
    }

    public boolean estaVacia() { return cola.isEmpty(); }
    public int tamanio() { return cola.size(); }
}
