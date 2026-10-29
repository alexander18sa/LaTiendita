package com.discount3a.structures;

import com.discount3a.modelo.Cliente;
import java.util.ArrayList;
import java.util.List;

/**
 * LISTA ENLAZADA (simple, implementada a mano): clientes frecuentes de la
 * tienda 3A. Cada nodo apunta al siguiente, sin usar List/LinkedList de Java.
 */
public class ListaClientesFrecuentes {

    private static class Nodo {
        Cliente cliente;
        Nodo siguiente;
        Nodo(Cliente cliente) { this.cliente = cliente; }
    }

    private Nodo cabeza;
    private int tamanio;

    /** Agrega un cliente frecuente al final de la lista. */
    public void agregar(Cliente cliente) {
        Nodo nuevo = new Nodo(cliente);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != null) actual = actual.siguiente;
            actual.siguiente = nuevo;
        }
        tamanio++;
    }

    /** Elimina un cliente frecuente por su número. */
    public boolean eliminar(int numero) {
        Nodo actual = cabeza, anterior = null;
        while (actual != null) {
            if (actual.cliente.getNumero() == numero) {
                if (anterior == null) cabeza = actual.siguiente;
                else anterior.siguiente = actual.siguiente;
                tamanio--;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    /** Busca un cliente frecuente por su número. */
    public Cliente buscar(int numero) {
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual.cliente.getNumero() == numero) return actual.cliente;
            actual = actual.siguiente;
        }
        return null;
    }

    /** Recorre la lista enlazada y devuelve los clientes en orden. */
    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        Nodo actual = cabeza;
        while (actual != null) {
            lista.add(actual.cliente);
            actual = actual.siguiente;
        }
        return lista;
    }

    public int tamanio() { return tamanio; }
}
