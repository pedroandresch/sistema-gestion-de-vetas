package com.sistema.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JsonRepository {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Guarda cualquier lista de objetos en su archivo JSON
    public static <T> boolean guardar(String rutaArchivo, List<T> lista) {
        File archivo = new File(rutaArchivo);
        File carpeta = archivo.getParentFile();
        if (carpeta != null && !carpeta.exists()) {
            carpeta.mkdirs();
        }

        try (FileWriter escritor = new FileWriter(archivo, StandardCharsets.UTF_8)) {
            gson.toJson(lista, escritor);
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar archivo " + rutaArchivo + ": " + e.getMessage());
            return false;
        }
    }

    // Carga una lista de objetos desde su archivo JSON
    public static <T> List<T> cargar(String rutaArchivo, Type tipoLista) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists() || archivo.length() == 0) {
            return new ArrayList<>();
        }

        try (FileReader lector = new FileReader(archivo, StandardCharsets.UTF_8)) {
            List<T> lista = gson.fromJson(lector, tipoLista);
            if (lista != null) {
                return lista;
            }
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error al cargar archivo " + rutaArchivo + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
