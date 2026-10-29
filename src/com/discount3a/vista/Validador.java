package com.discount3a.vista;

import org.apache.commons.lang3.StringUtils;

/**
 * Validaciones de texto para los campos de las ventanas.
 */
public final class Validador {

    public static final String MENSAJE_CARACTERES = "No use el carácter | ni saltos de línea o tabulaciones.";

    private Validador() { }

    /**
     * Indica si el texto tiene caracteres que romperían los archivos txt.
     */
    public static boolean tieneCaracteresInvalidos(String texto) {
        return StringUtils.containsAny(texto, '|', '\n', '\r', '\t');
    }
}
