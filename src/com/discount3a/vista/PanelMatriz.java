package com.discount3a.vista;

import com.discount3a.controlador.InventarioController;
import com.discount3a.structures.MatrizEstantes;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Panel con la distribución de la tienda: pasillos por niveles de estante.
 */
public class PanelMatriz extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(PanelMatriz.class);

    private final InventarioController controlador;
    private final Runnable guardar;
    private final DefaultTableModel modelo;

    private final JTextField txtPasillo = new JTextField(3);
    private final JTextField txtNivel = new JTextField(3);
    private final JTextField txtCodigo = new JTextField(8);

    public PanelMatriz(InventarioController controlador, Runnable guardar) {
        this.controlador = controlador;
        this.guardar = guardar;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(Paleta.BLANCO);

        add(UIHelper.titulo("Distribución de la tienda (pasillo x nivel)"), BorderLayout.NORTH);

        Object[] columnas = new Object[MatrizEstantes.NIVELES + 1];
        columnas[0] = "Pasillo \\ Nivel";
        for (int j = 0; j < MatrizEstantes.NIVELES; j++) {
            columnas[j + 1] = "Nivel " + j;
        }

        modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = new JTable(modelo);
        UIHelper.estilizarTabla(tabla);
        add(UIHelper.conScroll(tabla), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(Paleta.BLANCO);
        bottom.add(UIHelper.etiqueta("Pasillo (0-" + (MatrizEstantes.PASILLOS - 1) + "):"));
        bottom.add(txtPasillo);
        bottom.add(UIHelper.etiqueta("Nivel (0-" + (MatrizEstantes.NIVELES - 1) + "):"));
        bottom.add(txtNivel);
        bottom.add(UIHelper.etiqueta("Código producto (vacío = vaciar):"));
        bottom.add(txtCodigo);
        JButton btnAsignar = UIHelper.botonAccion("Asignar");
        btnAsignar.addActionListener(e -> asignar());
        bottom.add(btnAsignar);
        add(bottom, BorderLayout.SOUTH);

        refrescar();
    }

    private void asignar() {
        try {
            if (StringUtils.isBlank(txtPasillo.getText()) || StringUtils.isBlank(txtNivel.getText())) {
                JOptionPane.showMessageDialog(this, "Ingrese el pasillo y el nivel.");
                return;
            }
            int pasillo = Integer.parseInt(txtPasillo.getText().trim());
            int nivel = Integer.parseInt(txtNivel.getText().trim());
            String codigo = txtCodigo.getText().trim();

            if (Validador.tieneCaracteresInvalidos(codigo)) {
                JOptionPane.showMessageDialog(this, Validador.MENSAJE_CARACTERES);
                return;
            }
            boolean ok;
            try {
                ok = controlador.asignarUbicacion(pasillo, nivel, codigo);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Producto inexistente",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Posición fuera de rango.");
                return;
            }
            txtCodigo.setText("");
            refrescar();
            if (guardar != null) guardar.run();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Pasillo y nivel deben ser números.");
        } catch (Exception ex) {
            logger.error("Error inesperado al asignar la posición", ex);
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error inesperado al asignar la posición:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refrescar() {
        modelo.setRowCount(0);
        String[][] datos = controlador.getMatriz();
        for (int i = 0; i < MatrizEstantes.PASILLOS; i++) {
            Object[] fila = new Object[MatrizEstantes.NIVELES + 1];
            fila[0] = "Pasillo " + i;
            for (int j = 0; j < MatrizEstantes.NIVELES; j++) {
                fila[j + 1] = datos[i][j] == null ? "-" : datos[i][j];
            }
            modelo.addRow(fila);
        }
    }
}
