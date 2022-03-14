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

	private Locale localeDisplay;
	private static ResourceBundle rb;

	public Menu() {
		this.localeDisplay = Locale.getDefault(Category.DISPLAY);
		this.rb = ResourceBundle.getBundle("i18n/Texts", localeDisplay); // Fetch data from this property
		System.out.println(localeDisplay);
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
	public int show(String type) {
		Scanner sc = new Scanner(System.in);
		int seleccion;
		switch (type) {
			case "inicial":
				titulo(
						getRb().getString("botigaTitle"));
				System.out.println("+----+");
				System.out.println(
						getRb().getString("ppal_productOption"));
				System.out.println(
						getRb().getString("ppal_clientOption"));
				System.out.println(
						getRb().getString("ppal_providerOption"));
				System.out.println(
						getRb().getString("ppal_employeeOption"));
				break;
			case "productos":
				titulo(
						getRb().getString("prodTitle"));
				System.out.println("+----------------+");
				System.out.println(
						getRb().getString("comm_add_option"));
				System.out.println(
						getRb().getString("comm_search_option"));
				System.out.println(
						getRb().getString("comm_modify_option"));
				System.out.println(
						getRb().getString("comm_delete_option"));
				System.out.println(
						getRb().getString("comm_show_option"));
				System.out.println(
						getRb().getString("prod_AddStock_option"));
				System.out.println(
						getRb().getString("prod_DelStock_option"));
				System.out.println(
						getRb().getString("prod_ManageProd_option"));
				System.out.println(
						getRb().getString("prod_ShowDiscontinued_option"));
				break;
			case "clientes":
				titulo(
						getRb().getString("clientsTitle"));
				System.out.println("+----------------+");
				System.out.println(
						getRb().getString("comm_add_option"));
				System.out.println(
						getRb().getString("comm_search_option"));
				System.out.println(
						getRb().getString("comm_modify_option"));
				System.out.println(
						getRb().getString("comm_delete_option"));
				System.out.println(
						getRb().getString("comm_show_option"));
				break;
			case "proveedores":
				titulo(
						getRb().getString("providersTitle") );
				System.out.println("+----------------+");
				System.out.println(
						getRb().getString("comm_add_option"));
				System.out.println(
						getRb().getString("comm_search_option"));
				System.out.println(
						getRb().getString("comm_modify_option"));
				System.out.println(
						getRb().getString("comm_delete_option"));
				System.out.println(
						getRb().getString("comm_show_option"));
				break;
			case "empleados":
				titulo(
						getRb().getString("employeeTitle"));
				System.out.println("+----------------+");
				System.out.println(
						getRb().getString("empl_entry_option"));
				System.out.println(
						getRb().getString("empl_exit_option"));
				System.out.println(
						getRb().getString("empl_search_option"));
				break;
			case "MOSTRAR PRODUCTOS":
				titulo(
						getRb().getString("showProductTitle"));
				System.out.println(
						getRb().getString("showProd_ID_option"));
				System.out.println(
						getRb().getString("showProd_Name_option"));
				System.out.println(
						getRb().getString("showProd_ID_option"));
				System.out.println(
						getRb().getString("showProd_ID_option"));
				break;
			default:
				break;
		}
		System.out.println(
				getRb().getString("comm_exit"));
		System.out.println("+------------------------------------------------+");
		System.out.print(
				getRb().getString("input_select"));
		seleccion = sc.nextInt();
		sc.nextLine();
		System.out.println("");
		return seleccion;
	}

	public void mostrar(String type, Object dao) {
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

	public void titulo(String texto) {
		System.out.println(TEXT_PURPLE + texto + TEXT_RESET);
	}

	public void alerta(String texto, Object obj) {
		System.out.println(TEXT_RED + texto + obj + TEXT_RESET);
	}

	public void sistema(String texto) {
		System.out.println(TEXT_GREEN + texto + TEXT_RESET);
	}

	public void pulsaParaContinuar() throws IOException {
		System.out.println(TEXT_CYAN + getRb().getString("input_pressContinue") + TEXT_RESET);
		System.in.read();
	}

	// Definición colores para prints
	public final String TEXT_RESET = "\u001B[0m";
	public static final String TEXT_BLACK = "\u001B[30m";
	public final String TEXT_RED = "\u001B[31m";
	public static final String TEXT_GREEN = "\u001B[32m";
	public static final String TEXT_YELLOW = "\u001B[33m";
	public static final String TEXT_BLUE = "\u001B[34m";
	public static final String TEXT_PURPLE = "\u001B[35m";
	public static final String TEXT_CYAN = "\u001B[36m";
	public static final String TEXT_WHITE = "\u001B[37m";
}