package dao;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Locale.Category;

import bo.Pack;
import bo.Product;
import tools.Menu;
import tools.Persistable;
import tools.ProductNameComparator;
import tools.ProductPriceComparator;
import tools.ProductStockComparator;

public class ProductDAO<T> implements Persistable<T> {

    private static TreeMap<Integer, Product> mapaProductos = new TreeMap<>();
    static Menu menu = new Menu();

    Locale locale = Locale.getDefault(Category.FORMAT);

    public String formateoEntero(int numero) {
        return NumberFormat.getNumberInstance(locale).format(numero);
    }

    public String formateoPrecio(Double numero) {
        return NumberFormat.getCurrencyInstance(locale).format(numero);
    }

    public String formateoPorcentaje(Double numero) {
        return NumberFormat.getPercentInstance(locale).format(numero);
    }

    // AGREGAR
    public void agregarProducto_pack(String type) {
        int id;
        String nombre;

        // Obtener datos
        System.out.print(
                menu.getRb().getString("input_id"));
        id = new Scanner(System.in).nextInt();
        if (this.search(id) == null) {
            System.out.print(
                    menu.getRb().getString("input_name"));
            nombre = new Scanner(System.in).nextLine();
            System.out.print(
                    menu.getRb().getString("input_price"));
            String tmp = new Scanner(System.in).nextLine();
            tmp = tmp.contains(",") ? tmp.replace(",", ".") : tmp;
            double precio = Double.parseDouble(tmp);
            System.out.print(
                    menu.getRb().getString("input_Stock"));
            int stock = new Scanner(System.in).nextInt();

            switch (type) {
                case "producto":
                    agregarProducto(id, nombre, precio, stock);
                    break;
                case "pack":
                    agregarPack(id, nombre, precio, stock);
                    break;
                default:
                    break;
            }
        } else {
            menu.alerta(
                    menu.getRb().getString("alert_productExists"), "");
        }
    }

    private void agregarProducto(int idproduct, String nombre, double precio, int stock) {
        ArrayList<LocalDate> fechas = pedirFecha("newProduct");

        // Agregando producto
        Product product = new Product(idproduct, nombre, precio, stock, fechas.get(0), fechas.get(1));
        this.save(product);
    }

    private void agregarPack(int idpack, String nombre, double precio, int stock) {
        // Datos especificos Pack
        System.out.print(
                menu.getRb().getString("input_discount"));
        String tmp = new Scanner(System.in).nextLine();
        tmp = tmp.contains(",") ? tmp.replace(",", ".") : tmp;
        double descuento = Double.parseDouble(tmp);

        // Generando pack
        Pack pack = new Pack(idpack, nombre, precio, stock, descuento);
        do {
            menu.titulo(
                    menu.getRb().getString("info_add_ok"));
            Product prod = buscarProducto_pack("producto");

            boolean res = pack.addProduct(prod);
            if (res) {
                menu.sistema(
                        menu.getRb().getString("addPackTitle"));
            } else {
                menu.alerta(
                        menu.getRb().getString("alert_repeatedProduct"), "");
            }
            System.out.print(
                    menu.getRb().getString("input_addMore"));
        } while (new Scanner(System.in).next().equalsIgnoreCase("S"));
        // Comprobar si otros pack tienen los mismos productos
        if (!packsRepetidos(pack)) {
            this.save(pack);
            System.out.println(pack);
        } else {
            menu.alerta(
                    menu.getRb().getString("alert_repeatedProductsInPack"), "");
        }
    }

    private static boolean packsRepetidos(Pack newPack) {
        for (Product producto : mapaProductos.values()) {
            if (producto instanceof Pack) {
                Pack pack = (Pack) producto;
                if (newPack.equals(pack)) {
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
            menu.sistema(
                    menu.getRb().getString("info_save_ok"));
        }
    }

    // BUSCAR
    public Product buscarProducto_pack(String type) {
        int idprod_Pack;

        System.out.print(
                menu.getRb().getString("input_id"));
        idprod_Pack = new Scanner(System.in).nextInt();
        Product search = (Product) this.search(idprod_Pack);

        if (search != null) {
            System.out.println(search); // Imprimira producto o pack
        } else {
            menu.alerta(
                    menu.getRb().getString("alert_notFound"), type);
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
            menu.sistema(
                    menu.getRb().getString("info_delete_ok"));
        }
    }

    // MOSTRAR
    public void mostrarOrdenadoPor(String type) {
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

    public void printProducts(ArrayList<Product> lista) {
        System.out.println(
                menu.getRb().getString("showProdTitle"));
        menu.sistema(
                menu.getRb().getString("showProdHeaders"));
        ArrayList<Pack> listaPacks = new ArrayList<>();

        for (Product product : lista) {
            if (product instanceof Pack) {
                listaPacks.add((Pack) product);
            } else {
                System.out.println(
                        product.getId() + "\t" +
                        product.getPrecio() + "\t" +
                        product.getStock() + "\t" +
                        product.getNombre() + "\t" +
                        product.getFechaInicial() + " > " +
                        product.getFechaFinal());
            }
        }
        System.out.println();
        if (!listaPacks.isEmpty()) {
            System.out.println(
                    menu.getRb().getString("showPackTitle"));
            menu.sistema(
                    menu.getRb().getString("showPackHeaders"));
            for (Pack pack : listaPacks) {
                System.out.println(
                        pack.getId() + "\t" +
                                formateoPrecio(pack.getPrecio()) + "\t" +
                                formateoEntero(pack.getStock()) + " u\t" +
                                formateoPorcentaje(pack.getDescuento()) + "\t\t" +
                                pack.getNombre() + "\t" +
                                pack.getProductos());
            }
        }
        System.out.println();
    }

    public void mostrarDescatalogados(LocalDate fecha) {
        for (Product product : mapaProductos.values()) {
            if (product.getFechaFinal().isBefore(fecha)) {
                int fecha_dias = fecha.getDayOfYear();
                int product_final_dias = product.getFechaFinal().getDayOfYear();
                int tiempoDescatalogado = fecha_dias - product_final_dias;

                String text = "El producto (" + product.getId() + ") " + product.getNombre() +
                        " lleva " + menu.TEXT_RED + tiempoDescatalogado;
                text += (tiempoDescatalogado == 1) ? " dia descatalogado" : " dias descatalogado";
                text += menu.TEXT_RESET;

                System.out.println(text);
            }
        }
    }

    // FICHEROS
    public static void guardarFichero() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("productes.dat"))) {
            oos.writeObject(mapaProductos);
        } catch (IOException e) {
            menu.alerta(
                    menu.getRb().getString("alert_saveFile"), e);
        } finally {
            menu.sistema(
                    menu.getRb().getString("info_savedat_ok"));
        }
    }

    public void abrirFichero() {
        try {
            File file = new File("productes.dat");
            file.createNewFile();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            mapaProductos = (TreeMap<Integer, Product>) ois.readObject();
        } catch (EOFException eofe) {
        } catch (IOException ioe) {
            menu.alerta("", ioe);
        } catch (ClassNotFoundException cnfe) {
            menu.alerta(
                    menu.getRb().getString("alert_classNotExists"), cnfe);
        } finally {
            menu.sistema(
                    menu.getRb().getString("info_loaddat_ok"));
        }

    }

    // Set/get data
    @Override
    public TreeMap<Integer, T> getMap() {
        return (TreeMap<Integer, T>) mapaProductos;
    }

    public ArrayList<LocalDate> pedirFecha(String type) {
        int n_fechas = 1;
        ArrayList<String> textFechas = new ArrayList<>();
        if (type.equals("newProduct")) {
            n_fechas = 2; // Inicial y final
            textFechas.add(
                    menu.getRb().getString("input_initialDate"));
            textFechas.add(
                    menu.getRb().getString("input_finalDate"));
        } else if (type.equals("fromDate")) {
            n_fechas = 1;
            textFechas.add(
                    menu.getRb().getString("input_date"));
        }

        ArrayList<LocalDate> fechas = new ArrayList<>(n_fechas);
        menu.sistema(
                menu.getRb().getString("info_dateFormat"));
        menu.sistema(
                menu.getRb().getString("info_dateFormatAdvise"));
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (int i = 0; i < n_fechas; i++) {
            System.out.print(textFechas.get(i));
            String userInput = new Scanner(System.in).nextLine();
            fechas.add(userInput.isBlank() ? java.time.LocalDate.now() : LocalDate.parse(userInput, dateFormat));
        }

        return fechas;
    }

    @Override
    public TreeMap<String, T> getSMap() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub

    }

    @Override
    public T search(String id) {
        // TODO Auto-generated method stub
        return null;
    }
}
