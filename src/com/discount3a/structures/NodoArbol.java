package com.discount3a.structures;

import com.discount3a.modelo.Producto;
import java.util.ArrayList;
import java.util.List;

/**
 * Nodo de un ÁRBOL general n-ario usado para organizar categorías de
 * productos de la tienda 3A (ej. Alimentos -> Lácteos, Bebidas ...).
 */
public class NodoArbol {
    private final String nombreCategoria;
    private final List<NodoArbol> hijos = new ArrayList<>();
    private final List<Producto> productos = new ArrayList<>();

    public NodoArbol(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getNombreCategoria() { return nombreCategoria; }
    public List<NodoArbol> getHijos() { return hijos; }
    public List<Producto> getProductos() { return productos; }

    public void agregarHijo(NodoArbol hijo) { hijos.add(hijo); }
    public void agregarProducto(Producto producto) { productos.add(producto); }

    @Override
    public String toString() { return nombreCategoria; }
}
