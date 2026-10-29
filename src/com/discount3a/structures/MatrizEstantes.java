package com.discount3a.structures;

/**
 * MATRIZ: representa la distribución física de la tienda 3A en
 * pasillos (filas) x niveles de estante (columnas). Cada celda guarda
 * el código del producto ubicado en ese espacio (o null si está vacío).
 */
public class MatrizEstantes {
    public static final int PASILLOS = 4;
    public static final int NIVELES = 5;

    private final String[][] matriz;

    public MatrizEstantes() {
        matriz = new String[PASILLOS][NIVELES];
    }

    /** Asigna el código de un producto a la celda [pasillo][nivel]. */
    public boolean asignar(int pasillo, int nivel, String codigoProducto) {
        if (!esValido(pasillo, nivel)) return false;
        matriz[pasillo][nivel] = codigoProducto;
        return true;
    }

    public String obtener(int pasillo, int nivel) {
        if (!esValido(pasillo, nivel)) return null;
        return matriz[pasillo][nivel];
    }

    public boolean esValido(int pasillo, int nivel) {
        return pasillo >= 0 && pasillo < PASILLOS && nivel >= 0 && nivel < NIVELES;
    }

    /** Devuelve la matriz completa (referencia interna) para mostrarla. */
    public String[][] getMatriz() {
        return matriz;
    }
}
