package com.discount3a.vista;

import com.discount3a.controlador.VentaController;
import com.discount3a.modelo.ItemVenta;
import com.discount3a.modelo.Venta;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Panel que muestra el historial de ventas y permite deshacer la última.
 */
public class PanelHistorial extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(PanelHistorial.class);

    private final VentaController controlador;
    private final JTextArea area;
    private Runnable alDeshacer;
    private Runnable guardar;

    public PanelHistorial(VentaController controlador) {
        this.controlador = controlador;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(Paleta.BLANCO);

        add(UIHelper.titulo("Historial de ventas"), BorderLayout.NORTH);

        area = UIHelper.areaTexto();
        add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(Paleta.BLANCO);
        JButton btnRefrescar = UIHelper.botonAccion("Refrescar");
        btnRefrescar.addActionListener(e -> refrescar());
        JButton btnDeshacer = UIHelper.botonAccion("Deshacer última venta");
        btnDeshacer.addActionListener(e -> deshacer());
        bottom.add(btnRefrescar);
        bottom.add(btnDeshacer);
        add(bottom, BorderLayout.SOUTH);

        refrescar();
    }

    public void setAlDeshacer(Runnable callback) { this.alDeshacer = callback; }

    public void setGuardar(Runnable guardar) { this.guardar = guardar; }

    private void deshacer() {
        try {
            Venta v = controlador.deshacerUltimaVenta();
            if (v == null) {
                JOptionPane.showMessageDialog(this, "No hay ventas para deshacer.");
                return;
            }
            JOptionPane.showMessageDialog(this, "Se deshizo: " + v.resumen());
            refrescar();
            if (alDeshacer != null) alDeshacer.run();
            if (guardar != null) guardar.run();
        } catch (Exception ex) {
            logger.error("Error inesperado al deshacer la venta", ex);
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error inesperado al deshacer la venta:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refrescar() {
        StringBuilder sb = new StringBuilder();
        for (Venta v : controlador.listarHistorial()) {
            sb.append(v.resumen()).append("\n");
            for (ItemVenta item : v.getItems()) {
                sb.append("     - ").append(item).append("\n");
            }
        }
        if (sb.length() == 0) sb.append("Aún no hay ventas registradas.");
        area.setText(sb.toString());
    }
}
