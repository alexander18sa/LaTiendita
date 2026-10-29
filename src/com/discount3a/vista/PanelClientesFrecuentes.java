package com.discount3a.vista;

import com.discount3a.controlador.ClienteController;
import com.discount3a.modelo.Cliente;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Panel de clientes frecuentes.
 */
public class PanelClientesFrecuentes extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(PanelClientesFrecuentes.class);

    private final ClienteController controlador;
    private final Runnable guardar;
    private final DefaultListModel<String> modeloLista = new DefaultListModel<>();
    private final JTextField txtNombre = new JTextField(15);
    private final JTextField txtNumeroEliminar = new JTextField(5);

    public PanelClientesFrecuentes(ClienteController controlador, Runnable guardar) {
        this.controlador = controlador;
        this.guardar = guardar;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(Paleta.BLANCO);

        add(UIHelper.titulo("Clientes frecuentes"), BorderLayout.NORTH);

        JList<String> jList = new JList<>(modeloLista);
        jList.setFont(Paleta.FUENTE_NORMAL);
        add(new JScrollPane(jList), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(Paleta.BLANCO);
        bottom.add(UIHelper.etiqueta("Nombre:"));
        bottom.add(txtNombre);
        JButton btnAgregar = UIHelper.botonAccion("Agregar");
        btnAgregar.addActionListener(e -> agregar());
        bottom.add(btnAgregar);

        bottom.add(UIHelper.etiqueta("N° a eliminar:"));
        bottom.add(txtNumeroEliminar);
        JButton btnEliminar = UIHelper.botonAccion("Eliminar");
        btnEliminar.addActionListener(e -> eliminar());
        bottom.add(btnEliminar);

        add(bottom, BorderLayout.SOUTH);

        refrescar();
    }

    private void agregar() {
        try {
            String nombre = txtNombre.getText().trim();
            if (StringUtils.isBlank(nombre)) {
                JOptionPane.showMessageDialog(this, "Ingrese el nombre del cliente.");
                return;
            }
            if (Validador.tieneCaracteresInvalidos(nombre)) {
                JOptionPane.showMessageDialog(this, Validador.MENSAJE_CARACTERES);
                return;
            }
            controlador.agregarFrecuente(nombre);
            txtNombre.setText("");
            refrescar();
            if (guardar != null) guardar.run();
        } catch (Exception ex) {
            logger.error("Error al agregar el cliente frecuente", ex);
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error al agregar el cliente frecuente:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        try {
            if (StringUtils.isBlank(txtNumeroEliminar.getText())) {
                JOptionPane.showMessageDialog(this, "Ingrese el número del cliente a eliminar.");
                return;
            }
            int numero = Integer.parseInt(txtNumeroEliminar.getText().trim());
            if (!controlador.eliminarFrecuente(numero)) {
                JOptionPane.showMessageDialog(this, "No se encontró ese cliente.");
            } else {
                if (guardar != null) guardar.run();
            }
            txtNumeroEliminar.setText("");
            refrescar();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido.");
        } catch (Exception ex) {
            logger.error("Error al eliminar el cliente frecuente", ex);
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error al eliminar el cliente:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refrescar() {
        modeloLista.clear();
        for (Cliente c : controlador.listarFrecuentes()) {
            modeloLista.addElement(c.toString());
        }
    }
}
