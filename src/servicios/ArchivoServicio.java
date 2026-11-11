package servicios;

import javax.swing.*;
import java.io.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ArchivoServicio {

    private static final ObjectMapper objectMapper =
            new ObjectMapper();

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    // Seleccionar archivo
    public static String elegirArchivo(boolean guardar) {

        JFileChooser fc = new JFileChooser();

        int opcion;

        if (guardar) {
            opcion = fc.showSaveDialog(null);
        } else {
            opcion = fc.showOpenDialog(null);
        }

        if (opcion == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile().getAbsolutePath();
        }

        return "";
    }

    // Guardar JSON
    public static boolean guardarJson(String ruta, Object objeto) {

        try {

            objectMapper.writeValue(new File(ruta), objeto);

            return true;

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error al guardar JSON: " + e.getMessage()
            );

            return false;
        }
    }

    // Leer JSON
    public static <T> T leerJson(
            String ruta,
            TypeReference<T> tipo) {

        try {

            return objectMapper.readValue(
                    new File(ruta),
                    tipo
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Error al cargar JSON: " + e.getMessage()
            );

            return null;
        }
    }
}
