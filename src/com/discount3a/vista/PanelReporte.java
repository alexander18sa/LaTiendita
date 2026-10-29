package com.discount3a.vista;

import com.discount3a.controlador.ReporteController;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Panel del reporte general con opción de exportar a Excel.
 */
public class PanelReporte extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(PanelReporte.class);

    private final ReporteController controlador;
    private final JTextArea area;

    public PanelReporte(ReporteController controlador) {
        this.controlador = controlador;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(Paleta.BLANCO);

        add(UIHelper.titulo("Reporte general de La Tiendita"), BorderLayout.NORTH);

        area = UIHelper.areaTexto();
        add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(Paleta.BLANCO);
        JButton btnGenerar = UIHelper.botonAccion("Generar reporte");
        btnGenerar.addActionListener(e -> generar());
        bottom.add(btnGenerar);
        JButton btnExportar = UIHelper.botonAccion("Exportar a Excel");
        btnExportar.addActionListener(e -> exportar());
        bottom.add(btnExportar);
        add(bottom, BorderLayout.SOUTH);

        generar();
    }

    public void generar() {
        area.setText(controlador.generarTexto());
    }

    private void exportar() {
        JFileChooser selector = new JFileChooser();
        selector.setSelectedFile(new File("reporte_general.xlsx"));
        if (selector.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File destino = selector.getSelectedFile();
        if (!destino.getName().toLowerCase().endsWith(".xlsx")) {
            destino = new File(destino.getParentFile(), destino.getName() + ".xlsx");
        }
        try {
            controlador.exportarExcel(destino);
            JOptionPane.showMessageDialog(this, "Reporte exportado correctamente:\n" + destino.getAbsolutePath());
        } catch (IOException ex) {
            logger.error("No se pudo exportar el reporte a Excel", ex);
            JOptionPane.showMessageDialog(this,
                    "No se pudo exportar el reporte:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
