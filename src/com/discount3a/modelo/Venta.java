package com.discount3a.modelo;

import com.google.common.base.Preconditions;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Venta finalizada en caja: cliente, ítems y total.
 */
public class Venta {
    private static int contadorGlobal = 1;
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final int id;
    private final String cliente;
    private final List<ItemVenta> items;
    private final LocalDateTime fecha;

    /**
     * Crea una venta nueva con la fecha actual.
     */
    public Venta(String cliente, List<ItemVenta> items) {
        Preconditions.checkNotNull(cliente, "El cliente no puede ser nulo");
        Preconditions.checkNotNull(items, "La lista de ítems no puede ser nula");
        Preconditions.checkArgument(!items.isEmpty(), "La venta debe tener al menos un ítem");
        this.id = contadorGlobal++;
        this.cliente = cliente;
        this.items = new ArrayList<>(items);
        this.fecha = LocalDateTime.now();
    }

    /**
     * Recrea una venta guardada en disco con su id y fecha originales.
     */
    public Venta(int id, String cliente, List<ItemVenta> items, LocalDateTime fecha) {
        Preconditions.checkArgument(id > 0, "El id de la venta debe ser positivo");
        Preconditions.checkNotNull(cliente, "El cliente no puede ser nulo");
        Preconditions.checkNotNull(items, "La lista de ítems no puede ser nula");
        Preconditions.checkArgument(!items.isEmpty(), "La venta debe tener al menos un ítem");
        Preconditions.checkNotNull(fecha, "La fecha no puede ser nula");
        this.id = id;
        this.cliente = cliente;
        this.items = new ArrayList<>(items);
        this.fecha = fecha;
        if (id >= contadorGlobal) {
            contadorGlobal = id + 1;
        }
    }

    public int getId() { return id; }
    public String getCliente() { return cliente; }
    public List<ItemVenta> getItems() { return items; }
    public LocalDateTime getFecha() { return fecha; }

    public String getFechaTexto() {
        return fecha.format(FORMATO);
    }

    /**
     * Suma los subtotales de todos los ítems.
     */
    public double getTotal() {
        double total = 0;
        for (ItemVenta it : items) {
            total += it.getSubtotal();
        }
        return total;
    }

    public String resumen() {
        return "Venta #" + id
                + " | Cliente: " + cliente
                + " | Fecha: " + getFechaTexto()
                + " | Total: S/ " + String.format("%.2f", getTotal());
    }

    @Override
    public String toString() {
        return resumen();
    }
}
