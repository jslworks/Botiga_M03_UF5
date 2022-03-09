package dao;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;

import bo.Presencia;
import tools.Persistable;

public class PresenciaDAO<T> implements Persistable<T> {

    private static TreeMap<String, Presencia> mapaPresencia = new TreeMap<>();

    public void ficharEntrada(int idEmpleado) {
        // System.out.println("ID: ");
        // int idEmpleado = new Scanner(System.in).nextInt();
        LocalDate fechaActual = java.time.LocalDate.now();
        if (this.search(idEmpleado) == null) {
            Presencia presencia = new Presencia(idEmpleado, fechaActual);
            presencia.setEntrada(LocalTime.now());
            this.save(presencia);
            sistema("Entrada correcta " + (presencia.getEntrada()).toString());
        } else {
            alerta("No se puede volver fichar la entrada", "");
        }
    }

    public void ficharSalida(int idEmpleado) {
        // System.out.print("ID: ");
        // int idEmpleado = new Scanner(System.in).nextInt();
        LocalDate fechaActual = java.time.LocalDate.now();
        String idPresencia = Integer.toString(idEmpleado) + "_" + fechaActual.toString();
        Presencia presencia = (Presencia) this.search(idPresencia);

        if (presencia != null && presencia.getSalida() == null) {
            presencia.setSalida(LocalTime.now());
            this.save(presencia);
            sistema("Salida correcta " + (presencia.getSalida()).toString());
        } else {
            alerta("No se puede fichar la salida", "");
        }
    }

    public void consultaDia(int idEmpleado) {
        sistema("Formato de fecha dd/MM/yyyy (p.e. 02/02/2020)");
        System.out.print("Fecha: ");
        LocalDate dateInput = LocalDate.parse(
                new Scanner(System.in).nextLine(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String idPresencia = Integer.toString(idEmpleado) + "_" + dateInput.toString();

        Presencia presencia = (Presencia) this.search(idPresencia);
        if (presencia != null) {
            System.out.println(presencia);
        } else {
            alerta("No existe", "");
        }
    }

    // AGREGAR
    @Override
    public void save(Object obj) {
        if (obj != null && obj instanceof Presencia) {
            Presencia presencia = (Presencia) obj;
            mapaPresencia.put(presencia.getId(), (Presencia) obj);
            // sistema("Guardado correctamente");
        }
    }

    // BUSCAR
    @Override
    public T search(String id) {
        return mapaPresencia.containsKey(id) ? (T) (Presencia) mapaPresencia.get(id) : null;
    }

    @Override
    public T search(int id) {
        ArrayList<Presencia> listaPresencia = new ArrayList<>(mapaPresencia.values());
        Iterator<Presencia> it = listaPresencia.iterator();
        Presencia presencia;
        while (it.hasNext()) {
            presencia = it.next();
            if (presencia.getIdEmpleado() == id) {
                return (T) (Presencia) presencia;
            }
        }
        return null;
    }

    // ELIMINAR
    @Override
    public void delete(String id) {
        if (mapaPresencia.containsKey(id)) {
            mapaPresencia.remove(id);
            sistema("Eliminado correctamente");
        } else {
            alerta("No se ha podido eliminar el id ", id);
        }
    }

    @Override
    public void delete(int id) {
        // TODO Auto-generated method stub
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

}