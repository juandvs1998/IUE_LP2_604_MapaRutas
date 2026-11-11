package modelos;

import java.util.ArrayList;
import java.util.List;

public class ListaParadas {

    private Nodo cabeza;
    private int tamaño;

    public ListaParadas() {
        cabeza = null;
        tamaño = 0;
    }

    // Agregar parada al final
    public void agregar(Nodo nuevo) {

        nuevo.setSiguiente(null);

        if (cabeza == null) {
            cabeza = nuevo;
        } else {

            Nodo actual = cabeza;

            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }

            actual.setSiguiente(nuevo);
        }

        tamaño++;
    }

    // Eliminar parada
    public boolean eliminar(Nodo objetivo) {

        if (cabeza == null || objetivo == null) {
            return false;
        }

        if (cabeza == objetivo) {
            cabeza = cabeza.getSiguiente();
            tamaño--;
            return true;
        }

        Nodo actual = cabeza;

        while (actual.getSiguiente() != null) {

            if (actual.getSiguiente() == objetivo) {

                actual.setSiguiente(
                    actual.getSiguiente().getSiguiente()
                );

                tamaño--;
                return true;
            }

            actual = actual.getSiguiente();
        }

        return false;
    }

    // Obtener cabeza
    public Nodo getCabeza() {
        return cabeza;
    }

    // Obtener tamaño
    public int getTamaño() {
        return tamaño;
    }

    // Vaciar lista
    public void limpiar() {
        cabeza = null;
        tamaño = 0;
    }

    // Convertir lista ligada a ArrayList
    public List<Nodo> convertirALista() {

        List<Nodo> lista = new ArrayList<>();

        Nodo actual = cabeza;

        while (actual != null) {
            lista.add(actual);
            actual = actual.getSiguiente();
        }

        return lista;
    }

    // Cargar datos desde una lista
    public void cargarLista(List<Nodo> nodos) {

        limpiar();

        if (nodos == null) {
            return;
        }

        for (Nodo nodo : nodos) {
            nodo.setSiguiente(null);
            agregar(nodo);
        }
    }
}