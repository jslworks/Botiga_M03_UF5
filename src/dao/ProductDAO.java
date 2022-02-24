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

import bo.Pack;
import bo.Persistable;
import bo.Product;

public class ProductDAO<T> implements Persistable<T> {

    private static HashMap<Integer, Product> productes = new HashMap<>();

    public boolean addProduct(int i, String n, int p, int s) {
        if (productes.containsKey(i)) {
            return false;
        } else {
            Product prod = new Product(i, n, p, s);
            productes.put(i, prod);
            return true;

        }
    }

    public void addPack(ArrayList<Integer> l, int d, Integer i, String n, int p) {
        Pack pack = new Pack(l, d, i, n, p);
        productes.put(i, pack);
    }

    public Product searchProduct(int i) {
        if (productes.containsKey(i)) {
            return (Product) productes.get(i);
        } else {
            return null;
        }

    }

    public Pack searchPack(int i) {
        if (productes.containsKey(i)) {
            return (Pack) productes.get(i);
        } else {
            return null;
        }

    }

    public void modifyProduct(int i, String n, double p, int s) {
        Product prod = (Product) productes.get(i);
        prod.setName(n);
        prod.setPrice(p);
        prod.setStock(s);
    }

    public void modifyProduct(Product producto) {
        Product prod = (Product) productes.get(producto.getId());
        prod.setName(producto.getName());
        prod.setPrice(producto.getPrice());
        prod.setStock(producto.getStock());
        System.out.println(producto);
    }

    public void deleteP(int i) {
        productes.remove(i);
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

}
