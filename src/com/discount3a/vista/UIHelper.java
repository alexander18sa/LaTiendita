package com.discount3a.vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/** Componentes reutilizables para mantener una UI consistente. */
public final class UIHelper {
    private UIHelper() {}

    public static JButton botonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(Paleta.FUENTE_BOTON);
        boton.setForeground(Paleta.GRIS_TEXTO);
        boton.setBackground(Paleta.BLANCO);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(11, 14, 11, 14));
        boton.setAlignmentX(JButton.LEFT_ALIGNMENT);
        boton.setMaximumSize(new Dimension(235, 44));
        boton.setPreferredSize(new Dimension(235, 44));
        boton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return boton;
    }

    public static JButton botonAccion(String texto) {
        return botonAccion(texto, Paleta.PRINCIPAL);
    }

    public static JButton botonAccion(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(Paleta.FUENTE_BOTON);
        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(9, 16, 9, 16));
        boton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return boton;
    }

    public static JLabel titulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(Paleta.FUENTE_TITULO);
        label.setForeground(Paleta.TEXTO);
        return label;
    }

    public static JLabel subtitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(Paleta.FUENTE_SUBTITULO);
        label.setForeground(Paleta.TEXTO);
        return label;
    }

    public static JLabel etiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(Paleta.FUENTE_NORMAL);
        label.setForeground(Paleta.GRIS_TEXTO);
        return label;
    }

    public static JLabel etiquetaSuave(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(Paleta.FUENTE_PEQUENA);
        label.setForeground(Paleta.GRIS_MEDIO);
        return label;
    }

    public static void estilizarCampo(JTextField campo) {
        campo.setFont(Paleta.FUENTE_NORMAL);
        campo.setForeground(Paleta.TEXTO);
        campo.setBackground(Paleta.BLANCO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE),
                new EmptyBorder(7, 9, 7, 9)));
        campo.setMargin(new Insets(0, 0, 0, 0));
    }

    public static void estilizarTabla(JTable tabla) {
        tabla.setRowHeight(32);
        tabla.setFont(Paleta.FUENTE_NORMAL);
        tabla.setForeground(Paleta.GRIS_TEXTO);
        tabla.setBackground(Paleta.BLANCO);
        tabla.setGridColor(Paleta.GRIS_CLARO);
        tabla.setSelectionBackground(Paleta.PRINCIPAL_SUAVE);
        tabla.setSelectionForeground(Paleta.TEXTO);
        tabla.setShowVerticalLines(false);
        tabla.getTableHeader().setBackground(Paleta.PRINCIPAL_OSCURO);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(Paleta.FUENTE_BOTON);
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 34));
    }

    public static JScrollPane conScroll(JTable tabla) {
        JScrollPane sp = new JScrollPane(tabla);
        sp.setBorder(BorderFactory.createLineBorder(Paleta.BORDE));
        sp.getViewport().setBackground(Paleta.BLANCO);
        return sp;
    }

    public static JTextArea areaTexto() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(Paleta.FUENTE_NORMAL);
        area.setBackground(Paleta.BLANCO);
        area.setForeground(Paleta.GRIS_TEXTO);
        area.setBorder(new EmptyBorder(12, 12, 12, 12));
        return area;
    }

    public static javax.swing.JPanel tarjeta() {
        javax.swing.JPanel p = new javax.swing.JPanel();
        p.setBackground(Paleta.BLANCO);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Paleta.BORDE),
                new EmptyBorder(14, 16, 14, 16)));
        return p;
    }
}
