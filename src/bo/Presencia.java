package bo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Presencia implements Serializable {

    private String id;
    private int idEmpleado;
    private LocalDate fecha;
    private LocalTime entrada;
    private LocalTime salida;

    public Presencia(int idEmpleado, LocalDate fecha) {
        this.idEmpleado = idEmpleado;
        this.fecha = fecha;
        this.id = Integer.toString(idEmpleado) + "_" + fecha.toString();
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

    public LocalTime getEntrada() {
        return entrada;
    }

    public void setEntrada(LocalTime entrada) {
        this.entrada = entrada;
    }

    public LocalTime getSalida() {
        return salida;
    }

    public void setSalida(LocalTime salida) {
        this.salida = salida;
    }

    @Override
    public String toString() {
        return "Empleado " + idEmpleado + " [" + fecha + "], entrada= " + entrada + " / salida= " + salida;
    }

}
