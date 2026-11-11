package controladores;

import modelos.Nodo;
import modelos.ListaParadas;

import org.jxmapviewer.viewer.GeoPosition;

import vistas.MapaRutaVista;
import vistas.Marcador;

import servicios.ArchivoServicio;

import com.fasterxml.jackson.core.type.TypeReference;

import javax.swing.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapaRutaControlador {

    private final MapaRutaVista vista;

    private final ListaParadas listaParadas;

    private String nombrePendiente;
    private int tiempoPendiente;
    private String descripcionPendiente;

    public MapaRutaControlador(MapaRutaVista vista) {

        this.vista = vista;

        this.listaParadas = new ListaParadas();

        // Agregar
        vista.setAgregarParada(
                e -> prepararAgregarParada()
        );

        // Modificar
        vista.setModificarParada(
                e -> modificarParada()
        );

        // Eliminar
        vista.setEliminarParada(
                e -> eliminarParada()
        );

        // Guardar
        vista.setGuardarRuta(
                e -> guardarRuta()
        );

        // Cargar
        vista.setCargarRuta(
                e -> cargarRuta()
        );

        // Distancia
        vista.setCalcularDistancia(
                e -> calcularDistancia()
        );

        // Seleccionar parada
        vista.setAlSeleccionarNodo(
                () -> alSeleccionarNodo()
        );

        // Clic en mapa
        vista.setAlHacerClickMapa(
                posicion -> agregarParadaEnMapa(posicion)
        );
    }

    // =====================================
    // AGREGAR PARADA
    // =====================================

    private void prepararAgregarParada() {

        nombrePendiente = JOptionPane.showInputDialog(
                vista,
                "Nombre de la parada:"
        );

        if (nombrePendiente == null ||
                nombrePendiente.trim().isEmpty()) {

            nombrePendiente = null;
            return;
        }

        String tiempo = JOptionPane.showInputDialog(
                vista,
                "Tiempo estimado de llegada (minutos):"
        );

        if (tiempo == null) {
            nombrePendiente = null;
            return;
        }

        try {

            tiempoPendiente = Integer.parseInt(tiempo);

            if (tiempoPendiente < 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Ingresa un tiempo válido."
            );

            nombrePendiente = null;
            return;
        }

        descripcionPendiente = JOptionPane.showInputDialog(
                vista,
                "Descripción de la parada:"
        );

        if (descripcionPendiente == null) {
            descripcionPendiente = "";
        }

        JOptionPane.showMessageDialog(
                vista,
                "Ahora haz clic en el mapa para ubicar la parada."
        );
    }

    private void agregarParadaEnMapa(GeoPosition posicion) {

        if (nombrePendiente == null) {
            return;
        }

        Nodo nuevo = new Nodo(
                nombrePendiente,
                posicion.getLatitude(),
                posicion.getLongitude(),
                tiempoPendiente,
                descripcionPendiente
        );

        listaParadas.agregar(nuevo);

        actualizarInterfaz();

        nombrePendiente = null;

        JOptionPane.showMessageDialog(
                vista,
                "Parada agregada correctamente."
        );
    }

    // =====================================
    // MODIFICAR PARADA
    // =====================================

    private void modificarParada() {

        Nodo seleccionado = vista.getNodoSeleccionado();

        if (seleccionado == null) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Selecciona una parada primero."
            );

            return;
        }

        String nombre = JOptionPane.showInputDialog(
                vista,
                "Nuevo nombre:",
                seleccionado.getNombre()
        );

        if (nombre == null || nombre.trim().isEmpty()) {
            return;
        }

        String tiempo = JOptionPane.showInputDialog(
                vista,
                "Nuevo tiempo estimado:",
                seleccionado.getTiempoLlegada()
        );

        if (tiempo == null) {
            return;
        }

        try {

            int nuevoTiempo = Integer.parseInt(tiempo);

            if (nuevoTiempo < 0) {
                throw new NumberFormatException();
            }

            String descripcion = JOptionPane.showInputDialog(
                    vista,
                    "Nueva descripción:",
                    seleccionado.getDescripcion()
            );

            if (descripcion == null) {
                return;
            }

            seleccionado.setNombre(nombre);
            seleccionado.setTiempoLlegada(nuevoTiempo);
            seleccionado.setDescripcion(descripcion);

            actualizarInterfaz();

            JOptionPane.showMessageDialog(
                    vista,
                    "Parada modificada correctamente."
            );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    vista,
                    "El tiempo debe ser un número válido."
            );
        }
    }

    // =====================================
    // ELIMINAR PARADA
    // =====================================

    private void eliminarParada() {

        Nodo seleccionado = vista.getNodoSeleccionado();

        if (seleccionado == null) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Selecciona una parada primero."
            );

            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                vista,
                "¿Deseas eliminar la parada "
                        + seleccionado.getNombre() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta == JOptionPane.YES_OPTION) {

            listaParadas.eliminar(seleccionado);

            actualizarInterfaz();

            JOptionPane.showMessageDialog(
                    vista,
                    "Parada eliminada correctamente."
            );
        }
    }

    // =====================================
    // ACTUALIZAR INTERFAZ
    // =====================================

    private void actualizarInterfaz() {

        actualizarArbol();

        actualizarMarcadores();
    }

    private void actualizarArbol() {

        vista.setNodosArbol(
                listaParadas.convertirALista()
        );
    }

    private void actualizarMarcadores() {

        Set<Marcador> marcadores = new HashSet<>();

        Nodo actual = listaParadas.getCabeza();

        while (actual != null) {

            marcadores.add(
                    new Marcador(
                            new GeoPosition(
                                    actual.getLatitud(),
                                    actual.getLongitud()
                            ),
                            actual.getNombre()
                    )
            );

            actual = actual.getSiguiente();
        }

        vista.mostrarMarcadores(marcadores);
    }

    // =====================================
    // SELECCIONAR PARADA
    // =====================================

    private void alSeleccionarNodo() {

        Nodo seleccionado = vista.getNodoSeleccionado();

        if (seleccionado != null) {

            vista.setUbicacionMapa(
                    seleccionado.getLatitud(),
                    seleccionado.getLongitud()
            );
        }
    }

    // =====================================
    // GUARDAR JSON
    // =====================================

    private void guardarRuta() {

        if (listaParadas.getCabeza() == null) {

            JOptionPane.showMessageDialog(
                    vista,
                    "No hay paradas para guardar."
            );

            return;
        }

        String ruta = ArchivoServicio.elegirArchivo(true);

        if (ruta.isEmpty()) {
            return;
        }

        if (!ruta.toLowerCase().endsWith(".json")) {
            ruta += ".json";
        }

        boolean guardado = ArchivoServicio.guardarJson(
                ruta,
                listaParadas.convertirALista()
        );

        if (guardado) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Ruta guardada correctamente."
            );
        }
    }

    // =====================================
    // CARGAR JSON
    // =====================================

    private void cargarRuta() {

        String ruta = ArchivoServicio.elegirArchivo(false);

        if (ruta.isEmpty()) {
            return;
        }

        List<Nodo> nodos = ArchivoServicio.leerJson(
                ruta,
                new TypeReference<List<Nodo>>() {}
        );

        if (nodos != null) {

            listaParadas.cargarLista(nodos);

            actualizarInterfaz();

            JOptionPane.showMessageDialog(
                    vista,
                    "Ruta cargada correctamente.\n"
                            + "Total de paradas: "
                            + listaParadas.getTamaño()
            );
        }
    }

    // =====================================
    // CALCULAR DISTANCIA
    // =====================================

    private void calcularDistancia() {

        if (listaParadas.getTamaño() < 2) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Necesitas al menos dos paradas."
            );

            return;
        }

        double distanciaTotal = 0;

        Nodo actual = listaParadas.getCabeza();

        while (actual != null &&
                actual.getSiguiente() != null) {

            Nodo siguiente = actual.getSiguiente();

            distanciaTotal += calcularDistanciaEntre(
                    actual.getLatitud(),
                    actual.getLongitud(),
                    siguiente.getLatitud(),
                    siguiente.getLongitud()
            );

            actual = siguiente;
        }

        JOptionPane.showMessageDialog(
                vista,
                String.format(
                        "Distancia aproximada de la ruta:\n%.2f km",
                        distanciaTotal
                ),
                "Distancia total",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Fórmula de Haversine

    private double calcularDistanciaEntre(
            double lat1,
            double lon1,
            double lat2,
            double lon2) {

        final double RADIO_TIERRA = 6371.0;

        double dLat = Math.toRadians(lat2 - lat1);

        double dLon = Math.toRadians(lon2 - lon1);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2)
                +
                Math.cos(Math.toRadians(lat1))
                *
                Math.cos(Math.toRadians(lat2))
                *
                Math.sin(dLon / 2)
                *
                Math.sin(dLon / 2);

        double c = 2 * Math.atan2(
                Math.sqrt(a),
                Math.sqrt(1 - a)
        );

        return RADIO_TIERRA * c;
    }
}



