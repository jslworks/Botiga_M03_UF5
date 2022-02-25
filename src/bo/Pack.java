package bo;


import java.util.Objects;
import java.util.TreeSet;

public final class Pack extends Product {

    private TreeSet<Integer> productos;
    private double descuento;

    public Pack(int id, String nombre, double precio, int stock, double descuento) {
        super(id, nombre, precio, stock);
        this.descuento = descuento;
        this.productos = new TreeSet<>();
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    //afegir un producte a la llista o eliminar un producte de la llista
    public boolean removeProduct(int p) {
        return this.productos.remove(p);
    }

    public boolean addProduct(int i) {
        return this.productos.add(i);
    }

    @Override
    public String toString() {
        return "<Pack>{" + getNombre() + "="+ productos + " " + getPrecio() + " EUR ; Stock = " + getStock() + "}";
    }

    //Metode equals del id del pack
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.productos);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pack) {
            Pack temp = (Pack) obj;
            return temp.getNombre().equals(obj);
        } else {
            return false;
        }
    }

}
