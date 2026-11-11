package modelos;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Nodo {

    private String nombre;
    private double latitud;
    private double longitud;
    private int tiempoLlegada;
    private String descripcion;

    private Nodo siguiente;

    public Nodo() {
        this.siguiente = null;
    }

    public Nodo(String nombre, double latitud, double longitud,
                int tiempoLlegada, String descripcion) {

        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.tiempoLlegada = tiempoLlegada;
        this.descripcion = descripcion;
        this.siguiente = null;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getTiempoLlegada() {
        return tiempoLlegada;
    }

    public void setTiempoLlegada(int tiempoLlegada) {
        this.tiempoLlegada = tiempoLlegada;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @JsonIgnore
    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }

    @Override
    public String toString() {
        return nombre + " (" + tiempoLlegada + " min)";
    }
}