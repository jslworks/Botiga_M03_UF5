package dao;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.TreeMap;

import bo.Pack;
import bo.Product;
import bo.Presencia;
import tools.Persistable;
import tools.ProductNameComparator;
import tools.ProductPriceComparator;
import tools.ProductStockComparator;

public class PresenciaDAO<T> implements Persistable<T> {

    private static TreeMap<String, Presencia> mapaPresencia = new TreeMap<>();

    // AGREGAR
    @Override
    public void save(Object obj) {
        if (obj != null && obj instanceof Presencia) {
            Presencia presencia = (Presencia) obj;
            mapaPresencia.put(presencia.getId(), (Presencia) obj);
            sistema("Guardado correctamente");
        }
    }

    // BUSCAR
    @Override
    public T search(int id) {
        return mapaPresencia.containsKey(id) ? (T) (Presencia) mapaPresencia.get(id) : null;
    }

    // ELIMINAR
    @Override
    public void delete(String id) {
        if (mapaPresencia.containsKey(id)) {
            mapaPresencia.remove(id);
            sistema("Eliminado correctamente");
        }else{
            alerta("No se ha podido eliminar el id ", id);
        }
    }

    // Set/get data
    @Override
    public TreeMap<String, T> getSMap() {
        return (TreeMap<String, T>) mapaPresencia;
    }

    @Override
    public TreeMap<Integer, T> getMap() {
        return null;
    }
    
    // GETTERS & SETTERS

    // MOSTRAR

    // MODIFICAR

    ////////////////////////////////////////////////////////////////////////
    // VISTA
    //////////
    private static void titulo(String texto) {
        System.out.println(TEXT_PURPLE + texto + TEXT_RESET);
    }

    private static void alerta(String texto, Object obj) {
        System.out.println(TEXT_RED + texto + obj + TEXT_RESET);
    }

    private static void sistema(String texto) {
        System.out.println(TEXT_GREEN + texto + TEXT_RESET);
    }

    private static void pulsaParaContinuar() throws IOException {
        System.out.println(TEXT_CYAN + "Pulsa para continuar..." + TEXT_RESET);
        System.in.read();
    }

    // Definición colores para prints
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_BLACK = "\u001B[30m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_GREEN = "\u001B[32m";
    public static final String TEXT_YELLOW = "\u001B[33m";
    public static final String TEXT_BLUE = "\u001B[34m";
    public static final String TEXT_PURPLE = "\u001B[35m";
    public static final String TEXT_CYAN = "\u001B[36m";
    public static final String TEXT_WHITE = "\u001B[37m";

    @Override
    public void delete(int id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public T search(String id) {
        // TODO Auto-generated method stub
        return null;
    }
    
}