package vistas;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

public class Marcador extends DefaultWaypoint {

    private String nombre;

    public Marcador(GeoPosition coordenada, String nombre) {

        super(coordenada);

        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}