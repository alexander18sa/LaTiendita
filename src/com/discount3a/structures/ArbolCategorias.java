package com.discount3a.structures;

import com.discount3a.modelo.Producto;
import java.util.ArrayList;
import java.util.List;

/**
 * ÁRBOL: organiza las categorías y subcategorías de productos de la
 * tienda 3A a partir de un nodo raíz ("Categorías 3A").
 */
public class ArbolCategorias {
    private final NodoArbol raiz;

    public ArbolCategorias() {
        raiz = new NodoArbol("Categorías 3A");
    }

    public NodoArbol getRaiz() { return raiz; }

    /**
     * Busca una categoría por nombre recorriendo el árbol en profundidad (DFS).
     * Si nombrePadre es null, la categoría nueva se agrega bajo la raíz.
     */
    public NodoArbol buscarCategoria(String nombre) {
        return buscarRecursivo(raiz, nombre);
    }

    private NodoArbol buscarRecursivo(NodoArbol actual, String nombre) {
        if (actual.getNombreCategoria().equalsIgnoreCase(nombre)) return actual;
        for (NodoArbol hijo : actual.getHijos()) {
            NodoArbol encontrado = buscarRecursivo(hijo, nombre);
            if (encontrado != null) return encontrado;
        }
        return null;
    }

    /** Indica si ya existe una categoría con ese nombre en el árbol. */
    public boolean existeCategoria(String nombre) {
        return nombre != null && !nombre.isEmpty() && buscarCategoria(nombre) != null;
    }

    /** Inserta una nueva categoría bajo la categoría padre indicada (o bajo la raíz). */
    public boolean insertarCategoria(String nombrePadre, String nombreNueva) {
        NodoArbol padre = (nombrePadre == null || nombrePadre.isEmpty())
                ? raiz : buscarCategoria(nombrePadre);
        if (padre == null) return false;
        if (buscarCategoria(nombreNueva) != null) return false;
        padre.agregarHijo(new NodoArbol(nombreNueva));
        return true;
    }

    /** Agrega un producto a la categoría indicada (creándola bajo la raíz si no existe). */
    public boolean agregarProductoACategoria(String nombreCategoria, Producto producto) {
        NodoArbol nodo = buscarCategoria(nombreCategoria);
        if (nodo == null) {
            nodo = new NodoArbol(nombreCategoria);
            raiz.agregarHijo(nodo);
        }
        nodo.agregarProducto(producto);
        return true;
    }

    /**
     * Lista (DFS, preorden) los nombres de todas las categorías creadas,
     * sin incluir la raíz. Se usa para poblar el combo de categorías del
     * módulo de Inventario, evitando que se puedan registrar productos con
     * categorías que no existen en el árbol.
     */
    public List<String> listarTodasCategorias() {
        List<String> nombres = new ArrayList<>();
        for (NodoArbol hijo : raiz.getHijos()) {
            listarRecursivo(hijo, nombres);
        }
        return nombres;
    }

    private void listarRecursivo(NodoArbol nodo, List<String> nombres) {
        nombres.add(nodo.getNombreCategoria());
        for (NodoArbol hijo : nodo.getHijos()) {
            listarRecursivo(hijo, nombres);
        }
    }

    /**
     * Lista todas las relaciones padre-hijo del árbol en orden preorden
     * (el padre siempre aparece antes que sus propios hijos). Cada elemento
     * es {padre, hijo}; padre = "" cuando el hijo cuelga directamente de la
     * raíz. Se usa para persistir y reconstruir la estructura del árbol.
     */
    public List<String[]> listarRelaciones() {
        List<String[]> relaciones = new ArrayList<>();
        listarRelacionesRecursivo(raiz, relaciones);
        return relaciones;
    }

    private void listarRelacionesRecursivo(NodoArbol nodo, List<String[]> relaciones) {
        for (NodoArbol hijo : nodo.getHijos()) {
            String padreNombre = (nodo == raiz) ? "" : nodo.getNombreCategoria();
            relaciones.add(new String[]{padreNombre, hijo.getNombreCategoria()});
            listarRelacionesRecursivo(hijo, relaciones);
        }
    }
}
