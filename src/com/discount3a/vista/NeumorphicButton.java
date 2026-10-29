package com.discount3a.vista;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;
import javax.swing.SwingConstants;

/**
 * Botón pintado a mano (no delega en el Look&amp;Feel del sistema) que simula
 * neumorfismo: una superficie "suave" que parece sobresalir del fondo gracias
 * a una sombra clara (arriba-izquierda) y una sombra oscura (abajo-derecha).
 * Al presionarlo, el efecto se invierte para dar sensación de "hundido".
 *
 * Se pinta 100% en paintComponent para evitar el problema típico de Swing en
 * el que setBackground(...) de un JButton es ignorado por el Look&amp;Feel
 * nativo (Windows/Mac), dejando el botón gris pese a fijar un color.
 */
public class NeumorphicButton extends JButton {

    /** Radio de las esquinas redondeadas. */
    private static final int ARC = 18;
    /** Cuántos "anillos" se dibujan para simular el desenfoque de la sombra. */
    private static final int PASOS_SOMBRA = 6;
    /** Separación máxima (px) de la sombra respecto al cuerpo del botón. */
    private static final int OFFSET_SOMBRA = 5;

    private boolean presionado = false;
    private boolean sobreBoton = false;

    public NeumorphicButton(String texto) {
        super(texto);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setForeground(Paleta.NEGRO);
        setFont(Paleta.FUENTE_BOTON);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setHorizontalAlignment(SwingConstants.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                presionado = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                presionado = false;
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                sobreBoton = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                sobreBoton = false;
                presionado = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int margen = OFFSET_SOMBRA + PASOS_SOMBRA;


        Color sombraOscura = new Color(0, 0, 0, 0);
        Color sombraClara = Paleta.NEGRO;


        int direccion = presionado ? -1 : 1;

        for (int i = PASOS_SOMBRA; i >= 1; i--) {
            float alpha = 0.16f * (i / (float) PASOS_SOMBRA);
            int off = (int) (OFFSET_SOMBRA * (i / (float) PASOS_SOMBRA));


            g2.setColor(new Color(20, 20, 25, (int) (alpha * 140)));
            g2.fill(new RoundRectangle2D.Float(
                    margen + off * direccion, margen + off * direccion,
                    w - 2 * margen, h - 2 * margen, ARC, ARC));


            g2.setColor(new Color(255, 255, 255, (int) (alpha * 170)));
            g2.fill(new RoundRectangle2D.Float(
                    margen - off * direccion, margen - off * direccion,
                    w - 2 * margen, h - 2 * margen, ARC, ARC));
        }



        Color base = Paleta.PRINCIPAL;
        if (presionado) {
            base = base.darker();
        } else if (sobreBoton) {
            base = aclarar(base, 0.10f);
        }

        RoundRectangle2D cuerpo = new RoundRectangle2D.Float(
                margen, margen, w - 2 * margen, h - 2 * margen, ARC, ARC);
        g2.setColor(base);
        g2.fill(cuerpo);


        g2.setColor(new Color(255, 255, 255, 90));
        g2.setStroke(new java.awt.BasicStroke(1.2f));
        g2.draw(cuerpo);

        g2.dispose();


        super.paintComponent(g);
    }

    private static Color aclarar(Color c, float factor) {
        int r = Math.min(255, (int) (c.getRed() + (255 - c.getRed()) * factor));
        int g = Math.min(255, (int) (c.getGreen() + (255 - c.getGreen()) * factor));
        int b = Math.min(255, (int) (c.getBlue() + (255 - c.getBlue()) * factor));
        return new Color(r, g, b);
    }
}
