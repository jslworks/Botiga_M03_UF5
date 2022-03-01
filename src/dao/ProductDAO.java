package dao;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

import bo.Pack;
import bo.Product;
import tools.Persistable;

public class ProductDAO<T> implements Persistable<T> {

    private static TreeMap<Integer, Product> mapaProductos = new TreeMap<>();

    // AGREGAR
    public void agregarProducto_pack(String type){
        int id;
        String nombre;

        // Obtener datos
        System.out.print("ID: ");
        id = new Scanner(System.in).nextInt();
        if(this.search(id) == null){
            System.out.print("Nombre: ");
            nombre = new Scanner(System.in).nextLine();
            System.out.print("Precio: ");
            String tmp = new Scanner(System.in).nextLine();
            tmp = tmp.contains(",") ? tmp.replace(",", ".") : tmp;
            double precio = Double.parseDouble(tmp);
            System.out.print("Stock: ");
            int stock = new Scanner(System.in).nextInt();

            System.out.println("Formato de fecha dd/MM/yyyy (p.e. 02/02/2020)");
            System.out.print("Fecha inicial : ");
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String userInput = new Scanner(System.in).nextLine();
            LocalDate fechaInicial = LocalDate.parse(userInput, dateFormat);
            System.out.print("Fecha inicial: ");
            userInput = new Scanner(System.in).nextLine();
            LocalDate fechaFinal = LocalDate.parse(userInput, dateFormat);

            switch (type) {
                case "producto":
                    agregarProducto(id, nombre, precio, stock, fechaInicial, fechaFinal);
                    break;
                case "pack":
                    agregarPack(id, nombre, precio, stock);
                    break;
                default:
                    break;
            }
        }else{
            System.out.println("\u001B[31m" + "No se ha creado. Ya existe un producto o pack con ese id" + "\u001B[0m");
        }        
    }

    private void agregarProducto(int idproduct, String nombre, double precio, int stock, LocalDate fechaInicial, LocalDate fechaFinal){
        // Agregando producto
        Product product = new Product(idproduct, nombre, precio, stock, fechaInicial, fechaFinal);
        this.save(product);
    }

    private void agregarPack(int idpack, String nombre, double precio, int stock){
        // Datos especificos Pack
        System.out.print("% descuento: ");
        String tmp = new Scanner(System.in).nextLine();
        tmp = tmp.contains(",") ? tmp.replace(",", ".") : tmp;
        double descuento = Double.parseDouble(tmp);

        // Generando pack
        Pack pack = new Pack(idpack, nombre, precio, stock, descuento);
        do {
            System.out.println("\u001B[35m" + "AGREGAR PRODUCTOS < PACK" + "\u001B[0m");
            Product prod = buscarProducto_pack("producto");

            boolean res = pack.addProduct(prod);
            if(res){
                System.out.println("\u001B[33m" + "Añadido correctamente" + "\u001B[0m");
            }else{
                System.out.println("\u001B[31m" + "No se puede repetir producto" + "\u001B[0m");
            }
            System.out.print("¿Agregar mas productos? (S/n) ");
        } while (new Scanner(System.in).next().equalsIgnoreCase("S"));
        System.out.println(pack);
        // Comprobar si otros pack tienen los mismos productos
        if(!packsRepetidos(pack)){
            this.save(pack);
        }else{
            System.out.println("\u001B[31m" + "Ya existe un pack con éstos productos" + "\u001B[0m");
        }
    }

    private static boolean packsRepetidos(Pack newPack){
        for (Product producto : mapaProductos.values()) {
            if (producto instanceof Pack) {
                Pack pack = (Pack) producto;
                if(newPack.equals(pack)){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void save(Object obj) {
        if (obj != null && obj instanceof Product) {
            Product id = (Product) obj;
            mapaProductos.put(id.getIdProduct(), (Product) obj);
            System.out.println("\u001B[32m" + "Guardado correctamente" + "\u001B[0m");
        }
    }

    // BUSCAR
    public Product buscarProducto_pack(String type){
        int idprod_Pack;

        System.out.print("ID: ");
        idprod_Pack = new Scanner(System.in).nextInt();
        Product search = (Product) this.search(idprod_Pack);
        
        if (search != null) {
            System.out.println(search); // Imprimira producto o pack
        } else {
            System.out.println("\u001B[31m" + "No existe este " + type + "\u001B[0m");
        }
        return search;
    }

    @Override
    public T search(int id) {
        return mapaProductos.containsKey(id) ? (T) (Product) mapaProductos.get(id) : null;
    }
        
    // MODIFICAR

    public void modifyProduct(Product producto) {
        Product prod = (Product) mapaProductos.get(producto.getId());
        prod.setNombre(producto.getNombre());
        prod.setPrecio(producto.getPrecio());
        prod.setStock(producto.getStock());
        System.out.println(producto);
    }

        // Adaptar y dejar de usar
        public void modifyProduct(int i, String n, double p, int s) {
            Product prod = (Product) mapaProductos.get(i);
            prod.setNombre(n);
            prod.setPrecio(p);
            prod.setStock(s);
        }

    // ELIMINAR
    @Override
    public void delete(int id) {
        if (mapaProductos.containsKey(id)) {
            mapaProductos.remove(id);
            System.out.println("\u001B[32m" + "Eliminado correctamente" + "\u001B[0m");
        }
    }

    // MOSTRAR
    public TreeSet<String> printProduct() {
        TreeSet<String> llistaprod = new TreeSet<String>();
        llistaprod.add(mapaProductos.toString());
        return llistaprod;
    }

    public ArrayList<Product> mostrarOrdenadoPor(String type){
        ArrayList<Product> listaOrdenada = new ArrayList<>(mapaProductos.size());
        switch (type) {
            case "nombre":
                
                break;
            case "precio":
                
                break;
            case "stock":
                
                break;
            default:
                break;
        }
        return listaOrdenada;
    }
    
    // FICHEROS
    public static void guardarFichero() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("productes.dat"))) {
            oos.writeObject(mapaProductos);
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
            mapaProductos = (TreeMap<Integer, Product>) ois.readObject();
        } catch (EOFException eofe) {
        } catch (IOException ioe) {
            System.out.println("\u001B[31m" + ioe + "\u001B[0m");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("\u001B[31m" + "La classe no existe: " + cnfe + "\u001B[0m");
        } finally {
            System.out.println("\u001B[32m" + "productes.dat cargado correctamente" + "\u001B[0m");
        }

    }

    // Set/get data
    @Override
    public TreeMap<Integer, T> getMap() {
        return (TreeMap<Integer, T>) mapaProductos;
    }

}
