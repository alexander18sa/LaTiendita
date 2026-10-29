package com.discount3a.vista;

import com.discount3a.controlador.InventarioController;
import com.discount3a.modelo.Producto;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
 * Panel para registrar productos y consultar el inventario.
 */
public class PanelInventario extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(PanelInventario.class);

    private final InventarioController controlador;
    private final DefaultTableModel modeloTabla;
    private final Runnable guardar;

    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JComboBox<String> cmbCategoria = new JComboBox<>();
    private final JTextField txtPrecio = new JTextField();
    private final JTextField txtStock = new JTextField();
    private final JTextField txtBuscar = new JTextField();

    public PanelInventario(InventarioController controlador, Runnable guardar) {
        this.controlador = controlador;
        this.guardar = guardar;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(Paleta.BLANCO);

        add(UIHelper.titulo("Inventario de productos"), BorderLayout.NORTH);
        add(construirFormulario(), BorderLayout.WEST);

        modeloTabla = new DefaultTableModel(
                new Object[]{"Código", "Nombre", "Categoría", "Precio", "Stock"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = new JTable(modeloTabla);
        UIHelper.estilizarTabla(tabla);
        add(UIHelper.conScroll(tabla), BorderLayout.CENTER);

        refrescarTabla();
        refrescarCategorias();
    }

    private JPanel construirFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 4, 6));
        panel.setBackground(Paleta.BLANCO);
        panel.setBorder(BorderFactory.createTitledBorder("Nuevo producto"));
        panel.setPreferredSize(new Dimension(260, 420));

        panel.add(UIHelper.etiqueta("Código:"));
        panel.add(txtCodigo);
        panel.add(UIHelper.etiqueta("Nombre:"));
        panel.add(txtNombre);
        panel.add(UIHelper.etiqueta("Categoría (del árbol):"));
        panel.add(cmbCategoria);
        panel.add(UIHelper.etiqueta("Precio (S/):"));
        panel.add(txtPrecio);
        panel.add(UIHelper.etiqueta("Stock inicial:"));
        panel.add(txtStock);

        JButton btnAgregar = UIHelper.botonAccion("Agregar al inventario");
        btnAgregar.addActionListener(e -> agregarProducto());
        panel.add(btnAgregar);

        panel.add(UIHelper.etiqueta("Buscar por código:"));
        panel.add(txtBuscar);
        JButton btnBuscar = UIHelper.botonAccion("Buscar");
        btnBuscar.addActionListener(e -> buscarProducto());
        panel.add(btnBuscar);

        return panel;
    }

    /**
     * Vuelve a cargar en el combo las categorías que existen en el árbol.
     */
    public void refrescarCategorias() {
        String seleccionActual = (String) cmbCategoria.getSelectedItem();
        cmbCategoria.removeAllItems();
        List<String> categorias = controlador.listarCategorias();
        for (String c : categorias) {
            cmbCategoria.addItem(c);
        }
        if (seleccionActual != null && categorias.contains(seleccionActual)) {
            cmbCategoria.setSelectedItem(seleccionActual);
        }
    }

    private void agregarProducto() {
        try {
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            String categoria = (String) cmbCategoria.getSelectedItem();

            if (categoria == null) {
                JOptionPane.showMessageDialog(this,
                        "Todavía no hay categorías creadas.\nVe al módulo \"Categorías\" y crea al menos una antes de registrar productos.",
                        "Sin categorías", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (StringUtils.isBlank(codigo) || StringUtils.isBlank(nombre)
                    || StringUtils.isBlank(txtPrecio.getText()) || StringUtils.isBlank(txtStock.getText())) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.");
                return;
            }
            if (Validador.tieneCaracteresInvalidos(codigo) || Validador.tieneCaracteresInvalidos(nombre)) {
                JOptionPane.showMessageDialog(this, Validador.MENSAJE_CARACTERES);
                return;
            }

            double precio;
            int stock;
            try {
                precio = Double.parseDouble(txtPrecio.getText().trim());
                stock = Integer.parseInt(txtStock.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Precio y stock deben ser numéricos.");
                return;
            }
            if (precio < 0 || stock < 0) {
                JOptionPane.showMessageDialog(this, "Precio y stock no pueden ser negativos.");
                return;
            }

            try {
                controlador.agregarProducto(codigo, nombre, categoria, precio, stock);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
                refrescarCategorias();
                return;
            }

            refrescarTabla();
            txtCodigo.setText("");
            txtNombre.setText("");
            txtPrecio.setText("");
            txtStock.setText("");
            if (guardar != null) guardar.run();
        } catch (Exception ex) {
            logger.error("Error inesperado al registrar el producto", ex);
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error inesperado al registrar el producto:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarProducto() {
        try {
            String codigo = txtBuscar.getText().trim();
            if (StringUtils.isBlank(codigo)) {
                JOptionPane.showMessageDialog(this, "Ingrese el código a buscar.");
                return;
            }
            Producto p = controlador.buscarProducto(codigo);
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Producto no encontrado.");
            } else {
                JOptionPane.showMessageDialog(this, p.toString(), "Producto encontrado",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            logger.error("Error al buscar el producto", ex);
            JOptionPane.showMessageDialog(this, "Ocurrió un error al buscar el producto.");
        }
    }

    public void refrescarTabla() {
        modeloTabla.setRowCount(0);
        for (Producto p : controlador.listarProductos()) {
            modeloTabla.addRow(new Object[]{
                    p.getCodigo(), p.getNombre(), p.getCategoria(),
                    String.format("%.2f", p.getPrecio()), p.getStock()
            });
        }
    }
}
