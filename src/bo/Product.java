package bo;

import java.io.Serializable;
import java.util.Objects;
import err.StockInsuficientException;

public class Product implements Identificable, Serializable{

    private static final long serialVersionUID = 14L;

    private Integer id;
    private String nombre;
    private double precio;
    private int stock;

    @Override
    public int getId() {
        return this.id;
    }   
    //Getters dels productes
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

    //Setters del producte
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
    
    // Transacciones stock
    public void putStock(int cantidad) {
    	this.setStock(this.getStock() + cantidad);
    }

    public void takeStock(int cantidad) throws StockInsuficientException{
    	if (this.getStock() > cantidad) {
        	this.setStock(this.getStock() - cantidad);
		} else {
			throw new StockInsuficientException("No hay suficiente stock de " + this.getNombre()); 
		}
    }
    
    //El constructors del produte
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
    //El metode toString
    @Override
    public String toString() {
        return "Producte{" + "idproduct=" + id + ", nom=" + nombre + ", price=" + precio + ", stock=" + stock + '}';
    }

    //Metode equals del name
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

    

}
