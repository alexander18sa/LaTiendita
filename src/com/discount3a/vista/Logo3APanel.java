package com.discount3a.vista;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Panel que muestra la imagen del logotipo del minimarket.
 */
public class Logo3APanel extends JPanel {

    private Image imagenLogo;

    public Logo3APanel(int tamanioFuente) {
        setOpaque(false);

        setPreferredSize(new Dimension(tamanioFuente * 4, tamanioFuente + 15));
        
        try {

            imagenLogo = new ImageIcon(getClass().getResource("/images/market.png")).getImage();
        } catch (Exception e) {
            imagenLogo = null; 
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenLogo != null) {

            g.drawImage(imagenLogo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}