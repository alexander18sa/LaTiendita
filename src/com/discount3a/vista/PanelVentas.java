package com.discount3a.vista;

import com.discount3a.controlador.ClienteController;
import com.discount3a.controlador.InventarioController;
import com.discount3a.controlador.VentaController;
import com.discount3a.modelo.Cliente;
import com.discount3a.modelo.ItemVenta;
import com.discount3a.modelo.Producto;
import com.discount3a.modelo.Venta;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
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
 * Panel para armar y finalizar una venta en caja.
 */
public class PanelVentas extends JPanel {

    private static final Logger logger = LoggerFactory.getLogger(PanelVentas.class);

    private final InventarioController inventarioController;
    private final VentaController ventaController;
    private final ClienteController clienteController;
    private final DefaultTableModel modeloCarrito;
    private final List<ItemVenta> carrito = new ArrayList<>();

    private final JTextField txtCliente = new JTextField(15);
    private final JTextField txtCodigo = new JTextField(8);
    private final JTextField txtCantidad = new JTextField(5);
    private final JLabel lblClienteActual = new JLabel(" ");

    private Cliente clienteDeCola;
    private String clienteBloqueado;

    private Runnable alFinalizarVenta;
    private Runnable guardar;

    public PanelVentas(InventarioController inventarioController, VentaController ventaController,
                       ClienteController clienteController) {
        this.inventarioController = inventarioController;
        this.ventaController = ventaController;
        this.clienteController = clienteController;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(Paleta.BLANCO);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(Paleta.BLANCO);
        top.add(UIHelper.etiqueta("Cliente:"));
        top.add(txtCliente);
        JButton btnAtenderCola = UIHelper.botonAccion("Atender siguiente (cola)");
        btnAtenderCola.addActionListener(e -> atenderSiguienteDeCola());
        top.add(btnAtenderCola);
        top.add(UIHelper.etiqueta("Código producto:"));
        top.add(txtCodigo);
        top.add(UIHelper.etiqueta("Cantidad:"));
        top.add(txtCantidad);
        JButton btnAgregar = UIHelper.botonAccion("Agregar al carrito");
        btnAgregar.addActionListener(e -> agregarAlCarrito());
        top.add(btnAgregar);
        add(top, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(6, 6));
        centro.setBackground(Paleta.BLANCO);
        lblClienteActual.setFont(Paleta.FUENTE_SUBTITULO);
        lblClienteActual.setForeground(Paleta.PRINCIPAL);
        centro.add(lblClienteActual, BorderLayout.NORTH);

        modeloCarrito = new DefaultTableModel(new Object[]{"Producto", "Cantidad", "Subtotal"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = new JTable(modeloCarrito);
        UIHelper.estilizarTabla(tabla);
        centro.add(UIHelper.conScroll(tabla), BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        JButton btnCancelar = UIHelper.botonAccion("Cancelar venta actual");
        btnCancelar.addActionListener(e -> cancelarVentaActual());
        JButton btnFinalizar = UIHelper.botonAccion("Finalizar venta");
        btnFinalizar.addActionListener(e -> finalizarVenta());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(Paleta.BLANCO);
        bottom.add(btnCancelar);
        bottom.add(btnFinalizar);
        add(bottom, BorderLayout.SOUTH);

        actualizarEtiquetaCliente();
    }

    public void setAlFinalizarVenta(Runnable callback) {
        this.alFinalizarVenta = callback;
    }

    public void setGuardar(Runnable guardar) {
        this.guardar = guardar;
    }

    /**
     * Inicia una venta vinculada a un cliente de la cola.
     */
    public void iniciarConCliente(Cliente cliente) {
        if (cliente == null) return;
        if (!carrito.isEmpty()) {
            int resp = JOptionPane.showConfirmDialog(this,
                    "Hay una venta en curso para \"" + clienteBloqueado + "\" con productos sin finalizar.\n"
                    + "¿Descartarla y empezar con \"" + cliente.getNombre() + "\"?",
                    "Venta en curso", JOptionPane.YES_NO_OPTION);
            if (resp != JOptionPane.YES_OPTION) return;
        }
        limpiarCarrito();
        clienteDeCola = cliente;
        clienteBloqueado = cliente.getNombre();
        txtCliente.setText(cliente.getNombre());
        txtCliente.setEditable(false);
        actualizarEtiquetaCliente();
    }

    private void atenderSiguienteDeCola() {
        try {
            Cliente c = clienteController.atenderSiguiente();
            if (c == null) {
                JOptionPane.showMessageDialog(this, "No hay clientes en espera en la cola.");
                return;
            }
            if (guardar != null) guardar.run();
            iniciarConCliente(c);
        } catch (Exception ex) {
            logger.error("Error al atender al siguiente cliente", ex);
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error al atender al siguiente cliente:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarAlCarrito() {
        try {
            String codigo = txtCodigo.getText().trim();
            if (StringUtils.isBlank(codigo)) {
                JOptionPane.showMessageDialog(this, "Ingrese el código del producto.");
                return;
            }
            if (Validador.tieneCaracteresInvalidos(codigo)) {
                JOptionPane.showMessageDialog(this, Validador.MENSAJE_CARACTERES);
                return;
            }
            int cantidad;
            try {
                cantidad = Integer.parseInt(txtCantidad.getText().trim());
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser numérica.");
                return;
            }
            Producto p = inventarioController.buscarProducto(codigo);
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Producto no encontrado.");
                return;
            }
            int yaEnCarrito = 0;
            for (ItemVenta it : carrito) {
                if (it.getProducto() == p) {
                    yaEnCarrito += it.getCantidad();
                }
            }
            if (cantidad <= 0 || cantidad + yaEnCarrito > p.getStock()) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida o stock insuficiente.");
                return;
            }

            if (carrito.isEmpty() && clienteBloqueado == null) {
                String nombre = txtCliente.getText().trim();
                if (Validador.tieneCaracteresInvalidos(nombre)) {
                    JOptionPane.showMessageDialog(this, Validador.MENSAJE_CARACTERES);
                    return;
                }
                clienteBloqueado = StringUtils.isBlank(nombre) ? "Cliente general" : nombre;
                txtCliente.setText(clienteBloqueado);
                txtCliente.setEditable(false);
                actualizarEtiquetaCliente();
            }

            ItemVenta item = new ItemVenta(p, cantidad);
            carrito.add(item);
            modeloCarrito.addRow(new Object[]{p.getNombre(), cantidad,
                    String.format("S/ %.2f", item.getSubtotal())});
            txtCodigo.setText("");
            txtCantidad.setText("");
        } catch (Exception ex) {
            logger.error("Error inesperado al agregar el producto al carrito", ex);
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error inesperado al agregar el producto:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelarVentaActual() {
        if (carrito.isEmpty() && clienteBloqueado == null) return;
        int resp = JOptionPane.showConfirmDialog(this,
                "¿Cancelar la venta actual" + (clienteBloqueado != null ? " de \"" + clienteBloqueado + "\"" : "")
                + "? Se perderán los productos del carrito (el stock no se ve afectado).",
                "Cancelar venta", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) return;
        limpiarCarrito();
    }

    private void finalizarVenta() {
        try {
            if (carrito.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El carrito está vacío.");
                return;
            }
            String cliente = (clienteBloqueado != null) ? clienteBloqueado : "Cliente general";
            Venta venta = ventaController.registrarVenta(cliente, carrito);

            JOptionPane.showMessageDialog(this, "Venta registrada.\n" + venta.resumen());

            limpiarCarrito();
            if (alFinalizarVenta != null) alFinalizarVenta.run();
            if (guardar != null) guardar.run();
        } catch (Exception ex) {
            logger.error("Error inesperado al finalizar la venta", ex);
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error inesperado al finalizar la venta:\n" + ex.getMessage()
                    + "\nLa venta NO fue registrada; verifique el carrito.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCarrito() {
        carrito.clear();
        modeloCarrito.setRowCount(0);
        txtCliente.setText("");
        txtCliente.setEditable(true);
        clienteBloqueado = null;
        clienteDeCola = null;
        actualizarEtiquetaCliente();
    }

    private void actualizarEtiquetaCliente() {
        if (clienteBloqueado != null) {
            lblClienteActual.setText("Atendiendo a: " + clienteBloqueado
                    + (clienteDeCola != null ? "  (cliente #" + clienteDeCola.getNumero() + " de la cola)" : "")
                    + " — carrito bloqueado a este cliente hasta finalizar o cancelar.");
        } else {
            lblClienteActual.setText("Ingrese un cliente (o use \"Atender siguiente\") y agregue productos.");
        }
    }
}
