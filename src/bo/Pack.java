package bo;


import java.util.Objects;
import java.util.TreeSet;

public final class Pack extends Product {

    private TreeSet<Product> productos;
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

    public boolean removeProduct(Product elProducto) {
        return this.productos.remove(elProducto);
    }

    public boolean addProduct(Product elProducto) {
        return this.productos.add(elProducto);
    }

    @Override
    public String toString() {
        return "(" + super.getId() + ") " + getNombre() + " "+ productos + " = " + getPrecio() + " EUR ; Stock = " + getStock() + " <Pack>";
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
        boolean checkedID = false;
        boolean checkedList = false;
        if (obj instanceof Pack) {
            Pack pack = (Pack) obj;
            checkedID = (getId() == pack.getId());
            checkedList = (productos.equals(pack.productos));
        }
        return (checkedID && checkedList);
    }

}
