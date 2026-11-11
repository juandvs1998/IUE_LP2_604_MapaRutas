package vistas;

import modelos.Nodo;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class MapaRutaVista extends JFrame {

    private JButton btnAgregar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnGuardar;
    private JButton btnCargar;
    private JButton btnDistancia;

    private JTree arbolRutas;

    private DefaultMutableTreeNode nodoRaiz;

    private JXMapViewer visorMapa;

    public MapaRutaVista() {

        setSize(1200, 750);

        setTitle("Gestión de Rutas Urbanas");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        // Barra superior

        JToolBar barra = new JToolBar();

        btnAgregar = new JButton("Agregar Parada");
        btnModificar = new JButton("Modificar Parada");
        btnEliminar = new JButton("Eliminar Parada");
        btnGuardar = new JButton("Guardar Ruta JSON");
        btnCargar = new JButton("Cargar Ruta JSON");
        btnDistancia = new JButton("Calcular Distancia");

        barra.add(btnAgregar);
        barra.add(btnModificar);
        barra.add(btnEliminar);
        barra.add(btnGuardar);
        barra.add(btnCargar);
        barra.add(btnDistancia);

        // Mapa

        visorMapa = new JXMapViewer();

        TileFactoryInfo info = new TileFactoryInfo(
                1, 15, 17, 256,
                true, true,
                "https://tile.openstreetmap.org",
                "x", "y", "z") {

            @Override
            public String getTileUrl(int x, int y, int zoom) {

                return "https://tile.openstreetmap.org/"
                        + (17 - zoom) + "/"
                        + x + "/"
                        + y + ".png";
            }
        };

        org.jxmapviewer.viewer.DefaultTileFactory tileFactory =
                new org.jxmapviewer.viewer.DefaultTileFactory(info);

        visorMapa.setTileFactory(tileFactory);

        visorMapa.setZoom(17);

        visorMapa.setAddressLocation(
                new GeoPosition(
                        6.24440601432547,
                        -75.5734461516244
                )
        );

        // Árbol de paradas

        nodoRaiz = new DefaultMutableTreeNode("Ruta Turística");

        arbolRutas = new JTree(nodoRaiz);

        JScrollPane scrollArbol = new JScrollPane(arbolRutas);

        // Distribución

        JSplitPane panelDividido = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                scrollArbol,
                visorMapa
        );

        panelDividido.setDividerLocation(280);

        add(barra, BorderLayout.NORTH);

        add(panelDividido, BorderLayout.CENTER);

        // Permitir mover el mapa

        javax.swing.event.MouseInputListener movimiento =
                new org.jxmapviewer.input.PanMouseInputListener(
                        visorMapa
                );

        visorMapa.addMouseListener(movimiento);

        visorMapa.addMouseMotionListener(movimiento);

    }

    // Obtener parada seleccionada

    public Nodo getNodoSeleccionado() {

        DefaultMutableTreeNode seleccionado =
                (DefaultMutableTreeNode)
                        arbolRutas.getLastSelectedPathComponent();

        if (seleccionado != null) {

            Object objeto = seleccionado.getUserObject();

            if (objeto instanceof Nodo) {
                return (Nodo) objeto;
            }
        }

        return null;
    }

    // Actualizar árbol

    public void setNodosArbol(List<Nodo> nodos) {

        nodoRaiz.removeAllChildren();

        for (Nodo nodo : nodos) {

            DefaultMutableTreeNode nuevo =
                    new DefaultMutableTreeNode(nodo);

            nodoRaiz.add(nuevo);
        }

        ((javax.swing.tree.DefaultTreeModel)
                arbolRutas.getModel()).reload();

    }

    // Centrar mapa

    public void setUbicacionMapa(
            double latitud,
            double longitud) {

        GeoPosition posicion =
                new GeoPosition(latitud, longitud);

        visorMapa.setAddressLocation(posicion);

        visorMapa.repaint();
    }

    // Detectar clic en el mapa

    public void setAlHacerClickMapa(
            Consumer<GeoPosition> evento) {

        visorMapa.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                GeoPosition posicion =
                        visorMapa.convertPointToGeoPosition(
                                e.getPoint()
                        );

                evento.accept(posicion);
            }
        });
    }

    // Mostrar marcadores

    public void mostrarMarcadores(Set<Marcador> marcadores) {

        WaypointPainter<Marcador> trazador =
                new WaypointPainter<>();

        trazador.setWaypoints(marcadores);

        visorMapa.setOverlayPainter(trazador);

        visorMapa.repaint();
    }

    // Eventos de botones

    public void setAgregarParada(ActionListener evento) {
        btnAgregar.addActionListener(evento);
    }

    public void setModificarParada(ActionListener evento) {
        btnModificar.addActionListener(evento);
    }

    public void setEliminarParada(ActionListener evento) {
        btnEliminar.addActionListener(evento);
    }

    public void setGuardarRuta(ActionListener evento) {
        btnGuardar.addActionListener(evento);
    }

    public void setCargarRuta(ActionListener evento) {
        btnCargar.addActionListener(evento);
    }

    public void setCalcularDistancia(ActionListener evento) {
        btnDistancia.addActionListener(evento);
    }

    // Evento de selección

    public void setAlSeleccionarNodo(Runnable evento) {

        arbolRutas.addTreeSelectionListener(e -> {

            if (e.isAddedPath()) {
                evento.run();
            }
        });
    }
}