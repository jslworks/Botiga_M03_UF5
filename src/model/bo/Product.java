package model.bo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import err.StockInsuficientException;
import tools.Identificable;

public class Product implements Identificable, Serializable, Comparable<Product> {

    private static final long serialVersionUID = 14L;

    private Integer id;
    private String nombre;
    private double precio;
    private int stock;
    private LocalDate fechaInicial;
    private LocalDate fechaFinal;

    // Transacciones stock
    public void putStock(int cantidad) {
        this.setStock(this.getStock() + cantidad);
    }

    public void takeStock(int cantidad) throws StockInsuficientException {
        if (this.getStock() > cantidad) {
            this.setStock(this.getStock() - cantidad);
        } else {
            throw new StockInsuficientException("No hay suficiente stock de " + this.getNombre());
        }
    }

    // Constructores
    public Product(int idproduct, String nom, double price, int stock, LocalDate fechaInicial, LocalDate fechaFinal) {
        this.id = idproduct;
        this.nombre = nom;
        this.precio = price;
        this.stock = stock;
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
    }

    public Product(int idproduct, String nom, double price, int stock) {
        this.id = idproduct;
        this.nombre = nom;
        this.precio = price;
        this.stock = stock;
    }

    public Product(int idproduct, String nom, double price) {
        this.id = idproduct;
        this.nombre = nom;
        this.precio = price;

    }

    @Override
    public String toString() {
        return "(" + id + ") " + nombre + ": " + precio + " EUR, " + stock + " unidad/es // Desde " + fechaInicial
                + " hasta " + fechaFinal + " <Producto>";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product) {
            Product temp = (Product) obj;
            return temp.getNombre().equals(obj);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    @Override
    public int compareTo(Product prod) {
        Integer thisID = getId();
        Integer otherID = prod.getId();
        return thisID.compareTo(otherID);
    }

    // Getters & Setters
    @Override
    public int getId() {
        return this.id;
    }

    // Getters dels productes
    public Integer getIdProduct() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    // Setters del producte
    public void setIdproduct(int i) {
        this.id = i;
    }

    public void setNombre(String n) {
        this.nombre = n;
    }

    public void setPrecio(double p) {
        this.precio = p;
    }

    public void setStock(int s) {
        this.stock = s;
    }

    public LocalDate getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(LocalDate fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public LocalDate getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(LocalDate fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

}
