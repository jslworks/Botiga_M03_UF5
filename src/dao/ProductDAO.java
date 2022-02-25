package dao;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import bo.Pack;
import bo.Persistable;
import bo.Product;

public class ProductDAO<T> implements Persistable<T> {

    private static HashMap<Integer, Product> productes = new HashMap<>();

    public void agregarProducto_pack(String type){
        int idproduct;
        String nombre, tmp;
		double precio;

        // Obtener datos
        System.out.print("ID: ");
        idproduct = new Scanner(System.in).nextInt();
        
        System.out.print("Nombre: ");
        nombre = new Scanner(System.in).nextLine();
        System.out.print("Precio: ");
        tmp = new Scanner(System.in).nextLine();
        tmp = tmp.contains(",") ? tmp.replace(",", ".") : tmp;
        precio = Double.parseDouble(tmp);

        switch (type) {
            case "producto":
                agregarProducto(idproduct, nombre, precio);
                break;
            case "pack":
                agregarPack(idproduct, nombre, precio);
                break;
            default:
                break;
        }
    }

    private void agregarProducto(int idproduct, String nombre, double precio){
        // Datos especificos Producto
        System.out.print("Stock: ");
        int stock = new Scanner(System.in).nextInt();
        // Agregando producto
        Product p = new Product(idproduct, nombre, precio, stock);
        this.save(p);
    }

    private void agregarPack(int idproduct, String nombre, double precio){
        // Datos especificos Pack
        System.out.println("% descuento: ");
        String tmp = new Scanner(System.in).nextLine();
        tmp = tmp.contains(",") ? tmp.replace(",", ".") : tmp;
        double descuento = Double.parseDouble(tmp);

        // Generando pack
        ArrayList<Integer> packList = new ArrayList<>();
        Pack p = new Pack(packList, descuento, idproduct, nombre, precio);
        this.save(p);
    }

    
    public void buscarProducto_pack(String type){
        int idprod_Pack;

        System.out.print("ID: ");
        idprod_Pack = new Scanner(System.in).nextInt();
        Object search = this.search(idprod_Pack);
        
        if (search != null) {
            System.out.println(search); // Imprimira producto o pack
        } else {
            System.out.println("\u001B[31m" + "No existe este " + type + "\u001B[0m");
        }
    }
        
    // Metodos

    public void modifyProduct(Product producto) {
        Product prod = (Product) productes.get(producto.getId());
        prod.setName(producto.getName());
        prod.setPrice(producto.getPrice());
        prod.setStock(producto.getStock());
        System.out.println(producto);
    }

    public ArrayList<String> printProduct() {
        ArrayList<String> llistaprod = new ArrayList<String>();
        llistaprod.add(productes.toString());
        return llistaprod;
    }

    public static void guardarFichero() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("productes.dat"))) {
            oos.writeObject(productes);
        } catch (IOException e) {
            System.out.println("\u001B[31m" + "Error al guardar el archivo: " + e + "\u001B[0m");
        } finally {
            System.out.println("\u001B[32m" + "productes.dat guardado correctamente" + "\u001B[0m");
        }
    }

    public static void abrirFichero() {
        try{
            File file = new File("productes.dat");
            file.createNewFile();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            productes = (HashMap<Integer, Product>) ois.readObject();
        } catch (EOFException eofe) {
        } catch (IOException ioe) {
            System.out.println("\u001B[31m" + ioe + "\u001B[0m");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("\u001B[31m" + "La classe no existe: " + cnfe + "\u001B[0m");
        } finally {
            System.out.println("\u001B[32m" + "productes.dat cargado correctamente" + "\u001B[0m");
        }

    }

    // Implementacio de la Interficie amb variable generica
    @Override
    public void save(Object obj) {
        if (obj != null && obj instanceof Product) {
            Product id = (Product) obj;
            productes.put(id.getIdProduct(), (Product) obj);
            System.out.println("\u001B[32m" + "Guardado correctamente" + "\u001B[0m");
        }
    }

    @Override
    public HashMap<Integer, T> getMap() {
        return (HashMap<Integer, T>) productes;
    }

    @Override
    public void delete(int id) {
        if (productes.containsKey(id)) {
            productes.remove(id);
            System.out.println("\u001B[32m" + "Eliminado correctamente" + "\u001B[0m");
        }
    }

    @Override
    public T search(int id) {
        return productes.containsKey(id) ? (T) (Product) productes.get(id) : null;
    }

    // Adaptar y dejar de usar
    public void modifyProduct(int i, String n, double p, int s) {
        Product prod = (Product) productes.get(i);
        prod.setName(n);
        prod.setPrice(p);
        prod.setStock(s);
    }
}
