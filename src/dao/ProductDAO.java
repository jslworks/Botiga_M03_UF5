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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.TreeMap;

import bo.Pack;
import bo.Product;
import tools.Persistable;
import tools.ProductNameComparator;
import tools.ProductPriceComparator;
import tools.ProductStockComparator;

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

            sistema("Formato de fecha dd/MM/yyyy (p.e. 02/02/2020)");
            System.out.print("Fecha inicial : ");
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String userInput = new Scanner(System.in).nextLine();
            userInput = userInput.isBlank() ? "02/02/2020" : userInput;
            LocalDate fechaInicial = LocalDate.parse(userInput, dateFormat);
            System.out.print("Fecha final: ");
            userInput = new Scanner(System.in).nextLine();
            userInput = userInput.isBlank() ? "02/02/2020" : userInput;
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
            alerta("No se ha creado. Ya existe un producto o pack con ese id", "");
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
            titulo("AGREGAR PRODUCTOS < PACK");
            Product prod = buscarProducto_pack("producto");

            boolean res = pack.addProduct(prod);
            if(res){
                sistema("Añadido correctamente");
            }else{
                alerta("No se puede repetir producto", "");
            }
            System.out.print("¿Agregar mas productos? (S/n) ");
        } while (new Scanner(System.in).next().equalsIgnoreCase("S"));
        System.out.println(pack);
        // Comprobar si otros pack tienen los mismos productos
        if(!packsRepetidos(pack)){
            this.save(pack);
        }else{
            alerta("Ya existe un pack con éstos productos", "");
        }
    }

    private static boolean packsRepetidos(Pack newPack){
        for (Product producto : mapaProductos.values()) {
            if (producto instanceof Pack) {
                Pack pack = (Pack) producto;
                if(newPack.equals(pack)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void save(Object obj) {
        if (obj != null && obj instanceof Product) {
            Product id = (Product) obj;
            mapaProductos.put(id.getIdProduct(), (Product) obj);
            sistema("Guardado correctamente");
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
            alerta("No existe este ", type);
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
            sistema("Eliminado correctamente");
        }
    }

    // MOSTRAR
    public void mostrarOrdenadoPor(String type){
        ArrayList<Product> listaOrdenada = new ArrayList<>(mapaProductos.values());
        switch (type) {
            case "nombre":
                Collections.sort(listaOrdenada, new ProductNameComparator());
                break;
            case "precio":
                Collections.sort(listaOrdenada, new ProductPriceComparator());
                break;
            case "stock":
                Collections.sort(listaOrdenada, new ProductStockComparator());
            break;
            default:
                break;
        }
        
        printProducts(listaOrdenada);
    }
    
    public void printProducts(ArrayList<Product> lista){
        System.out.println("\t-- PRODUCTOS --");
        System.out.println("\u001b[32m" + "ID\tPrecio\tStock\tNombre\t" + "\u001b[0m");
        ArrayList<Pack> listaPacks = new ArrayList<>();
        for (Product product : lista) {
            if(product instanceof Pack){
                listaPacks.add((Pack) product);
            }else{
                System.out.println(
                    product.getId() + "\t" + 
                    product.getPrecio() + "\t" + 
                    product.getStock() + "\t" + 
                    product.getNombre() 
                );
            }
        }
        System.out.println();
        if(!listaPacks.isEmpty()){
            System.out.println("\t-- PACKS -- ");
            System.out.println("\u001b[32m" + "ID\tPrecio\tStock\tDescuento\tNombre\tLista Productos" + "\u001b[0m");
            for (Pack pack : listaPacks) {
                System.out.println(
                    pack.getId() + "\t" + 
                    pack.getPrecio() + "\t" + 
                    pack.getStock() + " u\t" + 
                    pack.getDescuento() + " %\t\t" + 
                    pack.getNombre() + "\t" +
                    pack.getProductos()
                );
            }
        }
        System.out.println();
    }

    // FICHEROS
    public static void guardarFichero() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("productes.dat"))) {
            oos.writeObject(mapaProductos);
        } catch (IOException e) {
            alerta("Error al guardar el archivo: ", e);
        } finally {
            sistema("productes.dat guardado correctamente");
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
            alerta("", ioe);
        } catch (ClassNotFoundException cnfe) {
            alerta("La classe no existe: ", cnfe);
        } finally {
            sistema("productes.dat cargado correctamente");
        }

    }

    // Set/get data
    @Override
    public TreeMap<Integer, T> getMap() {
        return (TreeMap<Integer, T>) mapaProductos;
    }

	////////////////////////////////////////////////////////////////////////
	// VISTA
	//////////

	private static void titulo(String texto){
		System.out.println(TEXT_PURPLE + texto + TEXT_RESET);
	}

	private static void alerta(String texto, Object obj){
		System.out.println(TEXT_RED + texto + obj + TEXT_RESET);
	}

	private static void sistema(String texto){
		System.out.println(TEXT_GREEN + texto + TEXT_RESET);
	}

	private static void pulsaParaContinuar() throws IOException {
		System.out.println(TEXT_CYAN +"Pulsa para continuar..." + TEXT_RESET);
		System.in.read();
	}

	// Definición colores para prints
	public static final String TEXT_RESET = "\u001B[0m";
	public static final String TEXT_BLACK = "\u001B[30m";
	public static final String TEXT_RED = "\u001B[31m";
	public static final String TEXT_GREEN = "\u001B[32m";
	public static final String TEXT_YELLOW = "\u001B[33m";
	public static final String TEXT_BLUE = "\u001B[34m";
	public static final String TEXT_PURPLE = "\u001B[35m";
	public static final String TEXT_CYAN = "\u001B[36m";
	public static final String TEXT_WHITE = "\u001B[37m";
}
