package com.discount3a.structures;

import com.discount3a.modelo.Producto;
import java.util.ArrayList;
import java.util.List;

/**
 * VECTOR / ARREGLO: inventario de productos implementado como un arreglo
 * dinámico propio (crece duplicando su capacidad), tal como se enseña
 * en estructuras de datos, en vez de delegar directamente en ArrayList.
 */
public class InventarioVector {
    private Producto[] datos;
    private int cantidad;

    public InventarioVector() {
        datos = new Producto[4];
        cantidad = 0;
    }

    /** Agrega un producto al final del arreglo, creciendo si es necesario. */
    public void agregar(Producto producto) {
        if (cantidad == datos.length) {
            crecer();
        }
        datos[cantidad++] = producto;
    }

    private void crecer() {
        Producto[] nuevo = new Producto[datos.length * 2];
        System.arraycopy(datos, 0, nuevo, 0, datos.length);
        datos = nuevo;
    }

    /** Búsqueda lineal por código de producto. */
    public Producto buscarPorCodigo(String codigo) {
        for (int i = 0; i < cantidad; i++) {
            if (datos[i].getCodigo().equalsIgnoreCase(codigo)) {
                return datos[i];
            }
        }
        return null;
    }

    /** Devuelve una lista (copia) con todos los productos del arreglo. */
    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            lista.add(datos[i]);
        }
        return lista;
    }

    public int tamanio() { return cantidad; }
}
