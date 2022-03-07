package bo;

import java.io.Serializable;
import java.time.LocalDate;

import tools.Identificable;

public class Presencia implements Identificable, Serializable {

    private int idEmpleado;
    private LocalDate fecha;
    private LocalDate entrada;
    private LocalDate salida;

    public Presencia(int id, LocalDate fecha) {
        this.idEmpleado = id;
        this.fecha = fecha;
    }

    @Override
    public int getId() {
        return this.idEmpleado;
    }

    public void setId(int id) {
        this.idEmpleado = id;
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
