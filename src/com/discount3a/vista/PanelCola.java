package com.discount3a.vista;

import com.discount3a.controlador.ClienteController;
import com.discount3a.modelo.Cliente;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.function.Consumer;
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
 * Panel de la fila de espera de clientes en caja.
 */
public class PanelCola extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(PanelCola.class);

    private final ClienteController controlador;
    private final Runnable guardar;
    private final DefaultListModel<String> modeloLista = new DefaultListModel<>();
    private final JTextField txtNombre = new JTextField(15);

    private Consumer<Cliente> alAtenderCliente;

    public PanelCola(ClienteController controlador, Runnable guardar) {
        this.controlador = controlador;
        this.guardar = guardar;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(Paleta.BLANCO);

        add(UIHelper.titulo("Fila de espera en caja"), BorderLayout.NORTH);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(Paleta.BLANCO);
        top.add(UIHelper.etiqueta("Nombre del cliente:"));
        top.add(txtNombre);
        JButton btnEncolar = UIHelper.botonAccion("Encolar cliente");
        btnEncolar.addActionListener(e -> encolar());
        top.add(btnEncolar);
        JButton btnAtender = UIHelper.botonAccion("Atender siguiente");
        btnAtender.addActionListener(e -> atenderSiguiente());
        top.add(btnAtender);
        add(top, BorderLayout.SOUTH);

        JList<String> lista = new JList<>(modeloLista);
        lista.setFont(Paleta.FUENTE_NORMAL);
        add(new JScrollPane(lista), BorderLayout.CENTER);

        refrescar();
    }

    /**
     * Define qué hacer con el cliente atendido (MainFrame lo envía a Registrar venta).
     */
    public void setAlAtenderCliente(Consumer<Cliente> callback) {
        this.alAtenderCliente = callback;
    }

    private void encolar() {
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
            controlador.encolar(nombre);
            txtNombre.setText("");
            refrescar();
            if (guardar != null) guardar.run();
        } catch (Exception ex) {
            logger.error("Error al encolar al cliente", ex);
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error al encolar al cliente:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atenderSiguiente() {
        try {
            Cliente c = controlador.atenderSiguiente();
            if (c == null) {
                JOptionPane.showMessageDialog(this, "No hay clientes en espera.");
                refrescar();
                return;
            }
            refrescar();
            if (guardar != null) guardar.run();
            if (alAtenderCliente != null) {
                alAtenderCliente.accept(c);
            } else {
                JOptionPane.showMessageDialog(this, "Atendiendo a: " + c);
            }
        } catch (Exception ex) {
            logger.error("Error al atender al cliente", ex);
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error al atender al cliente:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refrescar() {
        modeloLista.clear();
        int pos = 1;
        for (Cliente c : controlador.listarCola()) {
            modeloLista.addElement(pos + ". " + c);
            pos++;
        }
    }
}
