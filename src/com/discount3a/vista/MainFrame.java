package com.discount3a.vista;

import com.discount3a.controlador.ClienteController;
import com.discount3a.controlador.InventarioController;
import com.discount3a.controlador.ReporteController;
import com.discount3a.controlador.VentaController;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Ventana principal: navegación lateral y módulos de la tienda. */
public class MainFrame extends JFrame {

    private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);

    private final InventarioController inventarioController;
    private final VentaController ventaController;
    private final ClienteController clienteController;
    private final ReporteController reporteController;
    private final Runnable guardar = this::guardarEstado;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelContenido = new JPanel(cardLayout);
    private final JPanel menuLateral = new JPanel();
    private final JLabel lblModulo = new JLabel("Inicio");

    private PanelInventario panelInventario;
    private PanelVentas panelVentas;
    private PanelHistorial panelHistorial;
    private PanelInicio panelInicio;

    public MainFrame(InventarioController inventarioController, VentaController ventaController,
                     ClienteController clienteController, ReporteController reporteController) {
        super("La Tiendita — Sistema de Gestión Administrativa");
        this.inventarioController = inventarioController;
        this.ventaController = ventaController;
        this.clienteController = clienteController;
        this.reporteController = reporteController;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 720);
        setMinimumSize(new Dimension(980, 620));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Paleta.FONDO);

        cargarEstadoInicial();
        add(construirMenuLateral(), BorderLayout.WEST);
        add(construirZonaPrincipal(), BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { guardarEstado(); }
        });
    }

    private JPanel construirZonaPrincipal() {
        JPanel zona = new JPanel(new BorderLayout());
        zona.setBackground(Paleta.FONDO);
        zona.add(construirEncabezado(), BorderLayout.NORTH);
        zona.add(construirContenido(), BorderLayout.CENTER);
        zona.add(construirBarraEstado(), BorderLayout.SOUTH);
        return zona;
    }

    private JPanel construirEncabezado() {
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Paleta.BLANCO);
        encabezado.setBorder(new EmptyBorder(16, 24, 14, 24));

        JPanel izquierda = new JPanel();
        izquierda.setOpaque(false);
        izquierda.setLayout(new BoxLayout(izquierda, BoxLayout.Y_AXIS));
        lblModulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblModulo.setForeground(Paleta.TEXTO);
        izquierda.add(lblModulo);
        JLabel subt = new JLabel("Gestión rápida de ventas e inventario");
        subt.setFont(Paleta.FUENTE_NORMAL);
        subt.setForeground(Paleta.GRIS_MEDIO);
        izquierda.add(Box.createVerticalStrut(3));
        izquierda.add(subt);
        encabezado.add(izquierda, BorderLayout.WEST);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        derecha.setOpaque(false);
        JLabel estado = new JLabel("●  Sistema local");
        estado.setForeground(Paleta.EXITO);
        estado.setFont(Paleta.FUENTE_BOTON);
        derecha.add(estado);
        encabezado.add(derecha, BorderLayout.EAST);
        return encabezado;
    }

    private JPanel construirMenuLateral() {
        menuLateral.setLayout(new BoxLayout(menuLateral, BoxLayout.Y_AXIS));
        menuLateral.setBackground(Paleta.PRINCIPAL_OSCURO);
        menuLateral.setBorder(new EmptyBorder(18, 14, 16, 14));
        menuLateral.setPreferredSize(new Dimension(245, 0));

        JLabel marca = new JLabel("🛒  LA TIENDITA");
        marca.setForeground(Color.WHITE);
        marca.setFont(new Font("SansSerif", Font.BOLD, 20));
        marca.setBorder(new EmptyBorder(4, 6, 22, 6));
        menuLateral.add(marca);

        JLabel seccion = new JLabel("MENÚ PRINCIPAL");
        seccion.setForeground(new Color(180, 198, 220));
        seccion.setFont(new Font("SansSerif", Font.BOLD, 10));
        seccion.setBorder(new EmptyBorder(0, 7, 9, 0));
        menuLateral.add(seccion);

        agregarBotonMenu("⌂   Inicio", "INICIO");
        agregarBotonMenu("▣   Registrar venta", "VENTAS");
        agregarBotonMenu("▤   Inventario", "INVENTARIO");
        agregarBotonMenu("♙   Cola de caja", "COLA");
        agregarBotonMenu("★   Clientes frecuentes", "CLIENTES");
        agregarBotonMenu("▦   Categorías", "ARBOL");
        agregarBotonMenu("▥   Distribución de estantes", "MATRIZ");
        agregarBotonMenu("↶   Historial de ventas", "HISTORIAL");
        agregarBotonMenu("▥   Reporte general", "REPORTE");
        menuLateral.add(Box.createVerticalGlue());

        JPanel usuario = new JPanel(new BorderLayout(8, 0));
        usuario.setOpaque(false);
        usuario.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(55, 77, 104)));
        usuario.setMaximumSize(new Dimension(230, 65));
        JLabel avatar = new JLabel("  LV  ");
        avatar.setOpaque(true);
        avatar.setBackground(Paleta.PRINCIPAL);
        avatar.setForeground(Color.WHITE);
        avatar.setFont(Paleta.FUENTE_BOTON);
        usuario.add(avatar, BorderLayout.WEST);
        JLabel nombre = new JLabel("Sistema local");
        nombre.setForeground(Color.WHITE);
        nombre.setFont(Paleta.FUENTE_NORMAL);
        usuario.add(nombre, BorderLayout.CENTER);
        menuLateral.add(usuario);
        return menuLateral;
    }

    private void agregarBotonMenu(String texto, String card) {
        JButton boton = UIHelper.botonMenu(texto);
        boton.setForeground(new Color(224, 232, 244));
        boton.setBackground(Paleta.PRINCIPAL_OSCURO);
        boton.setBorder(new EmptyBorder(11, 10, 11, 10));
        boton.addActionListener(e -> mostrarModulo(card, texto));
        menuLateral.add(boton);
        menuLateral.add(Box.createVerticalStrut(3));
    }

    private void mostrarModulo(String card, String texto) {
        cardLayout.show(panelContenido, card);
        lblModulo.setText(texto.replaceAll("^[^ ]+\\s+", ""));
        if ("HISTORIAL".equals(card) && panelHistorial != null) panelHistorial.refrescar();
        if ("INVENTARIO".equals(card) && panelInventario != null) panelInventario.refrescarCategorias();
        if ("INICIO".equals(card) && panelInicio != null) panelInicio.refrescar();
    }

    private JPanel construirContenido() {
        panelInicio = new PanelInicio(inventarioController, clienteController, ventaController,
                card -> mostrarModulo(card, ""));
        panelInventario = new PanelInventario(inventarioController, guardar);
        panelVentas = new PanelVentas(inventarioController, ventaController, clienteController);
        panelHistorial = new PanelHistorial(ventaController);
        PanelCola panelCola = new PanelCola(clienteController, guardar);
        PanelMatriz panelMatriz = new PanelMatriz(inventarioController, guardar);
        PanelClientesFrecuentes panelClientes = new PanelClientesFrecuentes(clienteController, guardar);
        PanelArbol panelArbol = new PanelArbol(inventarioController, guardar);
        PanelReporte panelReporte = new PanelReporte(reporteController);

        panelVentas.setGuardar(guardar);
        panelHistorial.setGuardar(guardar);
        panelVentas.setAlFinalizarVenta(() -> {
            panelInventario.refrescarTabla();
            panelReporte.generar();
            panelInicio.refrescar();
        });
        panelHistorial.setAlDeshacer(() -> {
            panelInventario.refrescarTabla();
            panelReporte.generar();
            panelInicio.refrescar();
        });
        panelCola.setAlAtenderCliente(cliente -> {
            panelVentas.iniciarConCliente(cliente);
            cardLayout.show(panelContenido, "VENTAS");
            lblModulo.setText("Registrar venta");
        });

        panelContenido.setBackground(Paleta.FONDO);
        panelContenido.add(panelInicio, "INICIO");
        panelContenido.add(panelInventario, "INVENTARIO");
        panelContenido.add(panelCola, "COLA");
        panelContenido.add(panelVentas, "VENTAS");
        panelContenido.add(panelHistorial, "HISTORIAL");
        panelContenido.add(panelMatriz, "MATRIZ");
        panelContenido.add(panelClientes, "CLIENTES");
        panelContenido.add(panelArbol, "ARBOL");
        panelContenido.add(panelReporte, "REPORTE");
        cardLayout.show(panelContenido, "INICIO");
        return panelContenido;
    }

    private JPanel construirBarraEstado() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        barra.setBackground(Paleta.FONDO);
        barra.setBorder(new EmptyBorder(3, 10, 3, 10));
        JLabel lbl = new JLabel("✓ Guardado automático activado");
        lbl.setFont(Paleta.FUENTE_PEQUENA);
        lbl.setForeground(Paleta.GRIS_MEDIO);
        barra.add(lbl);
        return barra;
    }

    private void cargarEstadoInicial() {
        try {
            inventarioController.cargar();
            clienteController.cargar();
            ventaController.cargar();
        } catch (Exception ex) {
            logger.error("No se pudieron cargar los datos guardados", ex);
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los datos guardados.\nEl sistema iniciará con información en blanco.\n\nDetalle: " + ex.getMessage(),
                    "Aviso de carga", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void guardarEstado() {
        try {
            inventarioController.guardar();
            clienteController.guardar();
            ventaController.guardar();
            logger.info("Datos guardados correctamente");
        } catch (Exception ex) {
            logger.error("Error al guardar los datos", ex);
        }
    }
}
