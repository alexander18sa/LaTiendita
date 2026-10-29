package com.discount3a;

import com.discount3a.controlador.ClienteController;
import com.discount3a.controlador.InventarioController;
import com.discount3a.controlador.ReporteController;
import com.discount3a.controlador.VentaController;
import com.discount3a.dao.CategoriaDAOImpl;
import com.discount3a.dao.ClienteDAOImpl;
import com.discount3a.dao.ProductoDAOImpl;
import com.discount3a.dao.ReporteExcelDAO;
import com.discount3a.dao.UbicacionDAOImpl;
import com.discount3a.dao.VentaDAOImpl;
import com.discount3a.vista.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Punto de entrada de La Tiendita: crea los DAO y los controladores y abre la ventana principal.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.error("No se pudo aplicar el aspecto del sistema, se usa el de Swing", e);
        }

        InventarioController inventarioController = new InventarioController(
                new ProductoDAOImpl(), new CategoriaDAOImpl(), new UbicacionDAOImpl());
        VentaController ventaController = new VentaController(inventarioController, new VentaDAOImpl());
        ClienteController clienteController = new ClienteController(
                new ClienteDAOImpl("cola.txt"), new ClienteDAOImpl("clientes_frecuentes.txt"));
        ReporteController reporteController = new ReporteController(
                inventarioController, ventaController, clienteController, new ReporteExcelDAO());

        logger.info("Iniciando La Tiendita");
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(inventarioController, ventaController,
                    clienteController, reporteController);
            frame.setVisible(true);
        });
    }
}
