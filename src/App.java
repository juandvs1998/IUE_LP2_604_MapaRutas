import controladores.MapaRutaControlador;
import vistas.MapaRutaVista;

import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            MapaRutaVista vista = new MapaRutaVista();

            MapaRutaControlador controlador =
                    new MapaRutaControlador(vista);

            vista.setVisible(true);

        });
    }
}