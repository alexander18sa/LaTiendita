package com.discount3a.vista;

import com.discount3a.controlador.InventarioController;
import com.discount3a.modelo.Producto;
import com.discount3a.structures.NodoArbol;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Panel que muestra el árbol de categorías y permite insertar nuevas.
 */
public class PanelArbol extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(PanelArbol.class);

    private final InventarioController controlador;
    private final Runnable guardar;
    private final JTree jTree;

    private final JTextField txtPadre = new JTextField(12);
    private final JTextField txtNueva = new JTextField(12);

    public PanelArbol(InventarioController controlador, Runnable guardar) {
        this.controlador = controlador;
        this.guardar = guardar;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(Paleta.BLANCO);

        add(UIHelper.titulo("Árbol de categorías de productos"), BorderLayout.NORTH);

        jTree = new JTree();
        jTree.setFont(Paleta.FUENTE_NORMAL);
        add(new JScrollPane(jTree), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(Paleta.BLANCO);
        bottom.add(UIHelper.etiqueta("Categoría padre (vacío = raíz):"));
        bottom.add(txtPadre);
        bottom.add(UIHelper.etiqueta("Nueva categoría:"));
        bottom.add(txtNueva);
        JButton btnInsertar = UIHelper.botonAccion("Insertar categoría");
        btnInsertar.addActionListener(e -> insertarCategoria());
        bottom.add(btnInsertar);
        JButton btnRefrescar = UIHelper.botonAccion("Refrescar");
        btnRefrescar.addActionListener(e -> refrescar());
        bottom.add(btnRefrescar);
        add(bottom, BorderLayout.SOUTH);

        refrescar();
    }

    private void insertarCategoria() {
        try {
            String padre = txtPadre.getText().trim();
            String nueva = txtNueva.getText().trim();
            if (StringUtils.isBlank(nueva)) {
                JOptionPane.showMessageDialog(this, "Ingrese el nombre de la nueva categoría.");
                return;
            }
            if (Validador.tieneCaracteresInvalidos(nueva) || Validador.tieneCaracteresInvalidos(padre)) {
                JOptionPane.showMessageDialog(this, Validador.MENSAJE_CARACTERES);
                return;
            }
            boolean ok = controlador.insertarCategoria(padre, nueva);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "No se pudo insertar (padre inexistente o categoría duplicada).");
            } else {
                txtNueva.setText("");
                if (guardar != null) guardar.run();
            }
            refrescar();
        } catch (Exception ex) {
            logger.error("Error inesperado al insertar la categoría", ex);
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error inesperado al insertar la categoría:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refrescar() {
        DefaultMutableTreeNode raizSwing = construirNodoSwing(controlador.getRaizArbol());
        jTree.setModel(new DefaultTreeModel(raizSwing));
        for (int i = 0; i < jTree.getRowCount(); i++) {
            jTree.expandRow(i);
        }
    }

    private DefaultMutableTreeNode construirNodoSwing(NodoArbol nodo) {
        DefaultMutableTreeNode swingNodo = new DefaultMutableTreeNode(nodo.getNombreCategoria());
        for (Producto p : nodo.getProductos()) {
            swingNodo.add(new DefaultMutableTreeNode(p.toString()));
        }
        for (NodoArbol hijo : nodo.getHijos()) {
            swingNodo.add(construirNodoSwing(hijo));
        }
        return swingNodo;
    }
}
