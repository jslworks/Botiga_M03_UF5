package tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Locale.Category;

import bo.Product;
import dao.DAO;
import dao.ProductDAO;

/**
 * InnerInternacionalizacion
 */
public class Menu {

    Locale localeDisplay;
    ResourceBundle rb;

    public Menu(){
       this.localeDisplay = Locale.getDefault(Category.DISPLAY); 
       this.rb = ResourceBundle.getBundle("i18n/Texts", localeDisplay); // Fetch data from this property
    }

    public Locale getLocaleDisplay() {
        return localeDisplay;
    }

    public void setLocaleDisplay(Locale localeDisplay) {
        this.localeDisplay = localeDisplay;
    }

    public ResourceBundle getRb() {
        return rb;
    }

    public void setRb(ResourceBundle rb) {
        this.rb = rb;
    }

	////////////////////////////////////////////////////////////////////////
	// VISTA
	//////////    
    public static int show(String type) {
		Scanner sc = new Scanner(System.in);
		int seleccion;
		switch (type) {
			case "inicial":
				titulo("BOTIGA");
				System.out.println("+----+");
				System.out.println("1. Productos");
				System.out.println("2. Clientes");
				System.out.println("3. Proveedores");
				System.out.println("4. Empleados");
				break;
			case "productos":
				titulo("PRODUCTOS/PACKS < BOTIGA");
				System.out.println("+----------------+");
				System.out.println("1. Agregar");
				System.out.println("2. Buscar");
				System.out.println("3. Modificar producto");
				System.out.println("4. Eliminar");
				System.out.println("5. Mostrar productos");
				System.out.println("6. Agregar stock");
				System.out.println("7. Quitar stock");
				System.out.println("8. Mantenimiento de producto");
				System.out.println("9. Mostrar descatalogados");
				break;
			case "clientes":
				titulo("CLIENTES < BOTIGA");
				System.out.println("+----------------+");
				System.out.println("1. Agregar");
				System.out.println("2. Buscar");
				System.out.println("3. Modificar");
				System.out.println("4. Eliminar");
				System.out.println("5. Mostrar todo");
				break;
			case "proveedores":
				titulo("PROVEEDORES < BOTIGA");
				System.out.println("+----------------+");
				System.out.println("1. Agregar");
				System.out.println("2. Buscar");
				System.out.println("3. Modificar");
				System.out.println("4. Eliminar");
				System.out.println("5. Mostrar todo");
				break;
			case "empleados":
				titulo("EMPLEADOS < BOTIGA");
				System.out.println("+----------------+");
				System.out.println("1. Fichar entrada");
				System.out.println("2. Fichar salida");
				System.out.println("3. Consultar");
				break;
			case "MOSTRAR PRODUCTOS":
				titulo("MOSTRAR PRODUCTOS");
				System.out.println("1. ID");
				System.out.println("2. Nombre");
				System.out.println("3. Precio");
				System.out.println("4. Stock");
				break;
			default:
				break;
		}
		System.out.println("0. Salir");
		System.out.println("+------------------------------------------------+");
		System.out.print("Selección: ");
		seleccion = sc.nextInt();
		sc.nextLine();
		System.out.println("");
		return seleccion;
	}

	public static void mostrar(String type, Object dao) {
		Persistable p = null;
		switch (type) {
			case "productos":
				p = (ProductDAO<Product>) dao;
				((ProductDAO<Product>) dao).printProducts(new ArrayList<>(p.getMap().values()));
				break;
			case "clientes":
				p = (DAO) dao;
				// Falta definir
				break;
			case "proveedores":
				p = (DAO) dao;
				// Falta definir
				break;
		}
	}

	public static void titulo(String texto) {
		System.out.println(TEXT_PURPLE + texto + TEXT_RESET);
	}

	public static void alerta(String texto, Object obj) {
		System.out.println(TEXT_RED + texto + obj + TEXT_RESET);
	}

	public static void sistema(String texto) {
		System.out.println(TEXT_GREEN + texto + TEXT_RESET);
	}

	public static void pulsaParaContinuar() throws IOException {
		System.out.println(TEXT_CYAN + "Pulsa para continuar..." + TEXT_RESET);
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