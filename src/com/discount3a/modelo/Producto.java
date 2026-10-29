package com.discount3a.modelo;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

/**
 * Producto del catálogo de La Tiendita.
 */
public class Producto {
    private String codigo;
    private String nombre;
    private String categoria;
    private double precio;
    private int stock;

    /**
     * Crea un producto validando todos sus datos.
     */
    public Producto(String codigo, String nombre, String categoria, double precio, int stock) {
        Preconditions.checkArgument(StringUtils.isNotBlank(codigo), "El código no puede estar vacío");
        Preconditions.checkArgument(StringUtils.isNotBlank(nombre), "El nombre no puede estar vacío");
        Preconditions.checkArgument(StringUtils.isNotBlank(categoria), "La categoría no puede estar vacía");
        Preconditions.checkArgument(precio >= 0, "El precio no puede ser negativo");
        Preconditions.checkArgument(stock >= 0, "El stock no puede ser negativo");
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }

    public void setStock(int stock) {
        Preconditions.checkArgument(stock >= 0, "El stock no puede ser negativo");
        this.stock = stock;
    }

    /**
     * Resta unidades al stock. La cantidad no puede superar el stock actual.
     */
    public void reducirStock(int cantidad) {
        Preconditions.checkArgument(cantidad > 0 && cantidad <= stock, "Cantidad inválida o stock insuficiente");
        this.stock -= cantidad;
    }

    /**
     * Suma unidades al stock.
     */
    public void aumentarStock(int cantidad) {
        Preconditions.checkArgument(cantidad > 0, "La cantidad debe ser mayor a cero");
        this.stock += cantidad;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s - S/ %.2f - Stock: %d",
                codigo, nombre, categoria, precio, stock);
    }
}
