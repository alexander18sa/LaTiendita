package com.discount3a.modelo;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

/**
 * Cliente que llega a la tienda.
 */
public class Cliente {
    private static int contadorGlobal = 1;

    private final int numero;
    private final String nombre;

    /**
     * Crea un cliente nuevo con el siguiente número disponible.
     */
    public Cliente(String nombre) {
        Preconditions.checkArgument(StringUtils.isNotBlank(nombre), "El nombre del cliente no puede estar vacío");
        this.numero = contadorGlobal++;
        this.nombre = nombre.trim();
    }

    /**
     * Recrea un cliente guardado en disco con su número original.
     */
    public Cliente(int numero, String nombre) {
        Preconditions.checkArgument(numero > 0, "El número de cliente debe ser positivo");
        Preconditions.checkArgument(StringUtils.isNotBlank(nombre), "El nombre del cliente no puede estar vacío");
        this.numero = numero;
        this.nombre = nombre.trim();
        if (numero >= contadorGlobal) {
            contadorGlobal = numero + 1;
        }
    }

    public int getNumero() { return numero; }
    public String getNombre() { return nombre; }

    @Override
    public String toString() {
        return "Cliente #" + numero + " - " + nombre;
    }
}
