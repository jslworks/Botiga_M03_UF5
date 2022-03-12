package dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;

import bo.Presencia;
import tools.Menu;
import tools.Persistable;

public class PresenciaDAO<T> implements Persistable<T> {

	Menu menu = new Menu();

    private static TreeMap<String, Presencia> mapaPresencia = new TreeMap<>();

    public void ficharEntrada(int idEmpleado) {
        // System.out.println("ID: ");
        // int idEmpleado = new Scanner(System.in).nextInt();
        LocalDate fechaActual = java.time.LocalDate.now();
        if (this.search(idEmpleado) == null) {
            Presencia presencia = new Presencia(idEmpleado, fechaActual);
            presencia.setEntrada(LocalTime.now());
            this.save(presencia);
            menu.sistema(
                menu.getRb().getString("info_entryWork_ok") + (presencia.getEntrada()).toString() );
        } else {
            menu.alerta(
                menu.getRb().getString("alert_entryWork"), "");
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
            menu.sistema(
                menu.getRb().getString("info_exitWork_ok") + (presencia.getEntrada()).toString() );
        } else {
            menu.alerta(
                menu.getRb().getString("alert_exitWork"), "");
        }
    }

    public void consultaDia(int idEmpleado) {
        menu.sistema(
            menu.getRb().getString("info_dateFormat") );
        System.out.print(
            menu.getRb().getString("input_date") );
        LocalDate dateInput = LocalDate.parse(
                new Scanner(System.in).nextLine(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String idPresencia = Integer.toString(idEmpleado) + "_" + dateInput.toString();

        Presencia presencia = (Presencia) this.search(idPresencia);
        if (presencia != null) {
            System.out.println(presencia);
        } else {
            menu.alerta(
                menu.getRb().getString("alert_notExists"), "");
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
            menu.sistema(
                menu.getRb().getString("info_delete_ok") );
        } else {
            menu.alerta(
                menu.getRb().getString("alert_deleteID"), id);
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

}