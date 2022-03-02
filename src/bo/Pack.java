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
        Pack pack = (Pack) obj;
        // Comprobar si el objeto recibido es el mismo en el que que estas
        if(this == pack){
            return true;
        }
        // De toda la lista, filtramos solo los packs. Si no lo es, entonces no es igual
        if (!(obj instanceof Pack)) {
            return false;
        }

        // Comprobar que no tenga elementos en la lista
        if(productos == null || productos.isEmpty()){
            // Comprobar el pack que me han pasado
            if(pack.productos != null){
                return false;
            }
        }else if(!(pack.productos.equals(this.productos))){
            return false;
        }

        return true;
    }

}
