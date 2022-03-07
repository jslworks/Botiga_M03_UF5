package bo;

import java.io.Serializable;
import java.time.LocalDate;

public class Presencia implements Serializable {

    private String id;
    private int idEmpleado;
    private LocalDate fecha;
    private LocalDate entrada;
    private LocalDate salida;

    public Presencia(int idEmpleado, LocalDate fecha) {
        this.idEmpleado = idEmpleado;
        this.fecha = fecha;
        this.id = Integer.toString(idEmpleado) + "_" + fecha.toString();
        System.out.println(this.id);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalDate getEntrada() {
        return entrada;
    }

    public void setEntrada(LocalDate entrada) {
        this.entrada = entrada;
    }

    public LocalDate getSalida() {
        return salida;
    }

    public void setSalida(LocalDate salida) {
        this.salida = salida;
    }

}
