package com.sistema.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestor genérico de persistencia en formato JSON utilizando Google Gson.
 * Proporciona escritura con formato legible (pretty printing) y carga segura.
 */
public class JsonRepository {

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /**
     * Guarda una lista de objetos en el archivo especificado.
     */
    public static <T> boolean guardar(String rutaArchivo, List<T> lista) {
        File file = new File(rutaArchivo);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(lista, writer);
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar archivo " + rutaArchivo + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Carga una lista de objetos desde el archivo especificado.
     */
    public static <T> List<T> cargar(String rutaArchivo, Type tipoLista) {
        File file = new File(rutaArchivo);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            List<T> lista = gson.fromJson(reader, tipoLista);
            return lista != null ? lista : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error al cargar archivo " + rutaArchivo + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
