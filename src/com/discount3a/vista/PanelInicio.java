package com.discount3a.vista;

import com.discount3a.controlador.ClienteController;
import com.discount3a.controlador.InventarioController;
import com.discount3a.controlador.VentaController;
import com.discount3a.modelo.Producto;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/** Pantalla de inicio con el resumen de la tienda. */
public class PanelInicio extends JPanel {
    private final InventarioController inventarioController;
    private final ClienteController clienteController;
    private final VentaController ventaController;
    private final Runnable navegar;
    private final JLabel productos = dato();
    private final JLabel espera = dato();
    private final JLabel frecuentes = dato();
    private final JLabel ingresos = dato();
    private final JPanel bajoStock = new JPanel(new GridLayout(0, 1, 0, 5));

    public PanelInicio(InventarioController inventarioController, ClienteController clienteController,
                       VentaController ventaController, java.util.function.Consumer<String> navegacion) {
        this.inventarioController = inventarioController;
        this.clienteController = clienteController;
        this.ventaController = ventaController;
        this.navegar = () -> navegacion.accept("VENTAS");
        setLayout(new BorderLayout(16, 16)); setBorder(new EmptyBorder(20, 22, 20, 22)); setBackground(Paleta.FONDO);
        add(construirCentro(), BorderLayout.CENTER);
        refrescar();
    }

    private JPanel construirCentro() {
        JPanel centro = new JPanel(new BorderLayout(16, 16)); centro.setOpaque(false);
        JPanel bienvenida = new JPanel(new BorderLayout()); bienvenida.setOpaque(false);
        JPanel textos = new JPanel(); textos.setOpaque(false); textos.setLayout(new javax.swing.BoxLayout(textos, javax.swing.BoxLayout.Y_AXIS));
        JLabel h = UIHelper.titulo("Resumen de la tienda");
        JLabel p = UIHelper.etiquetaSuave("Consulta el estado actual y accede rápidamente a las operaciones frecuentes.");
        textos.add(h); textos.add(javax.swing.Box.createVerticalStrut(5)); textos.add(p);
        bienvenida.add(textos, BorderLayout.WEST);
        JButton vender = UIHelper.botonAccion("＋ Registrar venta", Paleta.PRINCIPAL);
        vender.addActionListener(e -> navegar.run()); bienvenida.add(vender, BorderLayout.EAST);
        centro.add(bienvenida, BorderLayout.NORTH);

        JPanel tarjetas = new JPanel(new GridLayout(1, 4, 12, 0)); tarjetas.setOpaque(false);
        tarjetas.add(tarjeta("Productos", productos, Paleta.PRINCIPAL));
        tarjetas.add(tarjeta("Clientes en cola", espera, Paleta.ACENTO));
        tarjetas.add(tarjeta("Clientes frecuentes", frecuentes, Color.decode("#7C3AED")));
        tarjetas.add(tarjeta("Ingresos totales", ingresos, Paleta.EXITO));

        JPanel inferior = new JPanel(new GridLayout(1, 2, 12, 0)); inferior.setOpaque(false);
        JPanel stock = UIHelper.tarjeta(); stock.setLayout(new BorderLayout(8, 8));
        JLabel sh = UIHelper.subtitulo("Productos con stock bajo"); stock.add(sh, BorderLayout.NORTH);
        bajoStock.setOpaque(false); stock.add(bajoStock, BorderLayout.CENTER);
        JPanel info = UIHelper.tarjeta(); info.setLayout(new BorderLayout(8, 8));
        info.add(UIHelper.subtitulo("Operaciones principales"), BorderLayout.NORTH);
        JPanel lista = new JPanel(new GridLayout(4, 1, 0, 7)); lista.setOpaque(false);
        lista.add(UIHelper.etiqueta("• Registrar productos y controlar stock"));
        lista.add(UIHelper.etiqueta("• Atender la cola y registrar ventas"));
        lista.add(UIHelper.etiqueta("• Organizar categorías y estantes"));
        lista.add(UIHelper.etiqueta("• Consultar historial y reporte general"));
        info.add(lista, BorderLayout.CENTER);
        inferior.add(stock); inferior.add(info);

        JPanel wrap = new JPanel(new BorderLayout(0, 14)); wrap.setOpaque(false); wrap.add(tarjetas, BorderLayout.NORTH); wrap.add(inferior, BorderLayout.CENTER);
        centro.add(wrap, BorderLayout.CENTER); return centro;
    }

    private JPanel tarjeta(String titulo, JLabel valor, Color color) {
        JPanel p = UIHelper.tarjeta(); p.setLayout(new BorderLayout(5, 5));
        JLabel t = UIHelper.etiqueta(titulo); p.add(t, BorderLayout.NORTH);
        valor.setForeground(color); p.add(valor, BorderLayout.CENTER); return p;
    }

    private static JLabel dato() { JLabel l = new JLabel("0"); l.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 24)); return l; }

    public void refrescar() {
        productos.setText(String.valueOf(inventarioController.cantidadProductos()));
        espera.setText(String.valueOf(clienteController.tamanioCola()));
        frecuentes.setText(String.valueOf(clienteController.tamanioFrecuentes()));
        ingresos.setText(String.format("S/ %.2f", ventaController.calcularIngresos()));
        bajoStock.removeAll();
        List<Producto> lista = inventarioController.listarProductos(); int mostrados = 0;
        for (Producto p : lista) {
            if (p.getStock() <= 5 && mostrados < 6) {
                JLabel row = new JLabel("• " + p.getNombre() + "   —   " + p.getStock() + " u.");
                row.setFont(Paleta.FUENTE_NORMAL); row.setForeground(p.getStock() == 0 ? Paleta.PELIGRO : Paleta.ADVERTENCIA);
                bajoStock.add(row); mostrados++;
            }
        }
        if (mostrados == 0) bajoStock.add(UIHelper.etiqueta("No hay productos con stock bajo."));
        bajoStock.revalidate(); bajoStock.repaint();
    }
}
