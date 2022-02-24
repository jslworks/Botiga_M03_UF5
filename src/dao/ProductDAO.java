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

    // Funcionalidades para menú
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
        if (tmp.contains(","))
            tmp.replace(",", ".");
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
        if (tmp.contains(","))
            tmp.replace(",", ".");
        double descuento = Double.parseDouble(tmp);

        // Generando pack
        ArrayList<Integer> packList = new ArrayList<>();
        Pack p = new Pack(packList, descuento, idproduct, nombre, precio);
        this.save(p);
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
            System.out.println("Error al guardarr l'arxiu: " + e);
        } finally {
            System.out.println("productes.dat guardado correctamente");
        }
    }

    public static void abrirFichero() {
        File file = new File("productes.dat");
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println(" " + e);
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            productes = (HashMap<Integer, Product>) ois.readObject();
        } catch (EOFException eofe) {
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            System.out.println("La classe no existeix: " + cnfe);
        } finally {
            System.out.println("productes.dat cargado correctamente");
        }

    }

    // Implementacio de la Interficie amb variable generica
    @Override
    public void save(Object obj) {
        if (obj != null && obj instanceof Product) {
            Product id = (Product) obj;
            productes.put(id.getIdProduct(), (Product) obj);
            System.out.println("Guardado correctamente");
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
            System.out.println("Eliminado correctamente");
        }
    }

    @Override
    public T search(int id) {
        if (productes.containsKey(id)) {
            return (T) (Product) productes.get(id);
        } else {
            return null;
        }
    }

    // Adaptar y dejar de usar
    public void modifyProduct(int i, String n, double p, int s) {
        Product prod = (Product) productes.get(i);
        prod.setName(n);
        prod.setPrice(p);
        prod.setStock(s);
    }
}
