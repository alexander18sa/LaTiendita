package com.discount3a.modelo;

import com.google.common.base.Preconditions;

/**
 * Línea de una venta: un producto y su cantidad.
 */
public class ItemVenta {
    private final Producto producto;
    private final int cantidad;

    /**
     * Crea un ítem validando que el producto exista y la cantidad sea positiva.
     */
    public ItemVenta(Producto producto, int cantidad) {
        Preconditions.checkNotNull(producto, "El producto no puede ser nulo");
        Preconditions.checkArgument(cantidad > 0, "La cantidad debe ser mayor a cero");
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double getSubtotal() { return producto.getPrecio() * cantidad; }

    @Override
    public String toString() {
        return String.format("%s x%d = S/ %.2f", producto.getNombre(), cantidad, getSubtotal());
    }
}
