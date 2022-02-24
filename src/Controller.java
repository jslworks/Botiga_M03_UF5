
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.logging.*;

import bo.Address;
import bo.Client;
import bo.Pack;
import bo.Persistable;
import bo.Product;
import bo.Supplier;
import dao.DAO;
import dao.ProductDAO;
import err.StockInsuficientException;

public class Controller {

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

	// Logger
	static Logger logger = Logger.getLogger("Log Botiga");

	// DAO
	private ProductDAO<Product> prodDAO = new ProductDAO<Product>();
	private DAO prov = new DAO();
	private DAO clie = new DAO();

	// Ejecutar
	public void run() {

		// Cargamos los datos desde fichero
		prodDAO.abrirFichero();
		System.out.println(TEXT_GREEN + "Resultado carga");
		mostrarProductos(prodDAO);
		System.out.println(TEXT_RESET);

		try {
			// Logger
			FileHandler fh = new FileHandler("log.txt", true);
			fh.setFormatter(new SimpleFormatter());
			logger.addHandler(fh);
			logger.setLevel(Level.ALL);
			fh.close();

			// Variables
			// Scanner keyboard = new Scanner(System.in);
			int option, option2, option3;
			String inputResponse = "", nombreFichero = "";
			boolean ok = false;

			int idperson;
			String dni, name, surnames;
			String locality, province, zipCode, direction;

			int idproduct, stock;
			double price;
			Product producto = new Product(1, "Patata", 1, 5);

			do {
				option = menu("inicial");
				switch (option) {
					case 1: // PRODUCTOS
						do {
							option2 = menu("productos");
							switch (option2) {
								case 1: // Agregar nuevo
									System.out.println("1.Agregar un producto");
									System.out.println("2.Agregar un pack");
									int seleccion = new Scanner(System.in).nextInt();
									String type = seleccion == 1 ? "producto" : "pack";

									prodDAO.agregarProducto_pack(type);
									break;
								case 2: // Buscar
									System.out.println("1.Buscar un producto");
									System.out.println("2.Buscar un pack");
									option3 = new Scanner(System.in).nextInt();
									if (option3 == 1) {
										System.out.print("ID producto: ");
										idproduct = new Scanner(System.in).nextInt();
										if (prodDAO.search(idproduct) != null) {
											Product productos = (Product) prodDAO.search(idproduct);
											System.out.println(productos.toString());
										} else {
											System.out.println("El producto no existe");
										}
									}
									if (option3 == 2) {
										System.out.print("ID pack: ");
										idproduct = new Scanner(System.in).nextInt();
										if (prodDAO.search(idproduct) != null) {
											Pack pck = (Pack) prodDAO.search(idproduct);
											System.out.println(pck.toString());
										} else {
											System.out.println("El pack no existe");
										}
									}
									break;
								case 3: // Modificar
									System.out.println("MODIFICAR PRODUCTO");
									// Obtener datos
									System.out.print("ID: ");
									idproduct = new Scanner(System.in).nextInt();
									// keyboard.nextLine();
									if (prodDAO.search(idproduct) != null) {
										System.out.print("Nombre:");
										name = new Scanner(System.in).nextLine();

										System.out.print("Precio: ");
										if (inputResponse.contains(",")) {
											inputResponse.replace(",", ".");
											price = Double.parseDouble(inputResponse);
										} else {
											price = new Scanner(System.in).nextDouble();
										}
										System.out.print("Stock: ");
										stock = new Scanner(System.in).nextInt();

										// Modificando
										prodDAO.modifyProduct(idproduct, name, price, stock);
										Product products = (Product) prodDAO.search(idproduct);
										// Mostrar resultados
										System.out.println(products.toString());
									} else {
										System.out.println("El producto no existe");
									}
									break;
								case 4: // Eliminar
									System.out.println("ELIMINAR PRODUCTO O PACK: ");
									System.out.println("ID :");
									idproduct = new Scanner(System.in).nextInt();
									// keyboard.nextLine();
									prodDAO.delete(idproduct);
									break;
								case 5: // Mostrar todos
									mostrar("productos", prodDAO);
									break;
								case 6: // Agregar stock
									// Selecciona metodo manual o automatico
									System.out.println("Cargar automaticamente? (S/n)");
									inputResponse = new Scanner(System.in).nextLine();
									if (inputResponse.equalsIgnoreCase("S")) {
										System.out.print("Nombre fichero: ");
										nombreFichero = new Scanner(System.in).nextLine();
										try (DataInputStream dis = new DataInputStream(
												new BufferedInputStream(new FileInputStream(nombreFichero)))) {

											while (dis.available() > 0) {
												// idproduct = Integer.parseInt(dis.readUTF());
												idproduct = dis.readInt();
												stock = dis.readInt();
												producto = prodDAO.search(idproduct);
												if (producto != null) {
													producto.putStock(stock);
												}
											}
										} catch (IOException e) {
											System.out.println("Error con el fichero " + e);
										}
									} else {
										System.out.println("STOCK PRODUCTO");
										System.out.print("ID: ");
										idproduct = new Scanner(System.in).nextInt();
										producto = prodDAO.search(idproduct);
										if (producto != null) {
											System.out.println("El producto no existe");
										} else {
											System.out.println("Agregar stock al producto (" + producto.getId() + ") "
													+ producto.getName());
											int addStock = new Scanner(System.in).nextInt();
											producto.putStock(addStock);

											prodDAO.modifyProduct(producto);
										}
									}
									break;
								case 7: // Quitar stock
									// Selecciona metodo manual o automatico
									System.out.println("Cargar automaticamente? (S/n)");
									inputResponse = new Scanner(System.in).nextLine();

									if (inputResponse.equalsIgnoreCase("S")) {
										try (DataInputStream dis = new DataInputStream(
												new BufferedInputStream(new FileInputStream("comanda_rebuda.txt")))) {

											while (dis.available() > 0) {
												idproduct = Integer.parseInt(dis.readUTF());
												stock = dis.readInt();
												producto = prodDAO.search(idproduct);
												if (producto != null) {
													producto.takeStock(stock);
												}
											}
										} catch (IOException e) {
											System.out.println("Error con el fichero " + e);
										}
									} else {
										System.out.println("Id del producto:");
										idproduct = new Scanner(System.in).nextInt();
										producto = prodDAO.search(idproduct);
										if (producto == null) {
											System.out.println("El producto no existeix");
										} else {
											int takeStock = -1;
											do {
												System.out
														.println("Quitar stock al producto (" + producto.getId() + ") "
																+ producto.getName());
												takeStock = new Scanner(System.in).nextInt();
												try {
													producto.takeStock(takeStock);
													ok = true;
												} catch (StockInsuficientException e) {
													ok = false;
													e.printStackTrace();
												}
											} while (!ok);

											if (ok) {
												prodDAO.modifyProduct(producto);
											} else {
												System.out.println("No se ha modificado el producto " + producto);
											}
										}
									}
									break;
								case 8: // Mantenimiento de productos
									System.out.println("COMANDA PRODUCTO");
									System.out.print("Nombre fichero: ");
									nombreFichero = new Scanner(System.in).nextLine();
									DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(nombreFichero)));
									do{
										// Obtener datos
										System.out.print("ID producto: ");
										idproduct = new Scanner(System.in).nextInt();
										producto = prodDAO.search(idproduct);
										if (producto != null) {
											System.out.print("Cantidad: ");
											stock = new Scanner(System.in).nextInt();
											// keyboard.nextLine();
											dos.writeInt(idproduct);
											dos.writeInt(stock);
										} else {
											System.out.println("El producto no existe");
										}
										System.out.println("¿Continuar? (S/n)");
										inputResponse = new Scanner(System.in).nextLine();
									}while(inputResponse.equalsIgnoreCase("S"));
									dos.close();
									break;
								case 0:
									break;
								default:
									System.out.println("Opcion incorrecta");
									break;
							}
							if (option2 != 0)
								pulsaParaContinuar();
						} while (option2 != 0);
						break;

					case 2: // CLIENTES
						do {
							option2 = menu("clientes");
							switch (option2) {
								case 1: // Agregar cliente
									System.out.println("CLIENTE");
									// Obtener datos
									System.out.print("ID: ");
									idperson = new Scanner(System.in).nextInt();
									// keyboard.nextLine();
									System.out.print("DNI: ");
									dni = new Scanner(System.in).nextLine();
									System.out.print("Nombre: ");
									name = new Scanner(System.in).nextLine();
									System.out.print("Apellidos: ");
									surnames = new Scanner(System.in).nextLine();
									System.out.print("Localidad: ");
									locality = new Scanner(System.in).nextLine();
									System.out.print("Provincia: ");
									province = new Scanner(System.in).nextLine();
									System.out.print("CP: ");
									zipCode = new Scanner(System.in).nextLine();
									System.out.print("Direccion: ");
									direction = new Scanner(System.in).nextLine();
									// Generar cliente
									Address a = new Address(locality, province, zipCode, direction);
									Client cl = new Client(idperson, dni, name, surnames, a);
									clie.save(cl);
									break;
								case 2: // Buscar cliente
									System.out.print("ID del cliente: ");
									idperson = new Scanner(System.in).nextInt();
									if (clie.search(idperson) != null) {
										Client client = (Client) clie.search(idperson);
										System.out.println(client.toString());
									} else {
										System.out.println("El cliente no existeix");
									}
									break;
								case 3: // Modificar cliente
									System.out.println("MODIFICAR CLIENTE");
									// Obtener datos
									System.out.print("ID : ");
									idperson = new Scanner(System.in).nextInt();
									// keyboard.nextLine();
									if (clie.search(idperson) != null) {
										System.out.print("DNI: ");
										dni = new Scanner(System.in).nextLine();
										System.out.print("Nombre: ");
										name = new Scanner(System.in).nextLine();
										System.out.print("Apellidos: ");
										surnames = new Scanner(System.in).nextLine();
										System.out.print("Localidad: ");
										locality = new Scanner(System.in).nextLine();
										System.out.print("Provincia: ");
										province = new Scanner(System.in).nextLine();
										System.out.print("CP: ");
										zipCode = new Scanner(System.in).nextLine();
										System.out.print("Direccion: ");
										direction = new Scanner(System.in).nextLine();
										// Modificando
										Address ad = new Address(locality, province, zipCode, direction);
										Client c = new Client(idperson, dni, name, surnames, ad);
										clie.modify(c);
									} else {
										System.out.println("El cliente no existeix");
									}
									break;
								case 4: // Eliminar cliente
									System.out.print("ID del cliente: ");
									idperson = new Scanner(System.in).nextInt();
									clie.delete(idperson);
									break;
								case 5: // Mostrar todos los clientess
									mostrar("clientes", clie);
									break;
								case 0:
									break;
							}
							if (option2 != 0)
								pulsaParaContinuar();
						} while (option2 != 0);
						break;
					case 3: // PROVEEDORES
						do {
							option2 = menu("proveedores");
							switch (option2) {
								case 1: // Agregar proveedor
									System.out.println("PROVEEDOR");
									// Obtener datos
									System.out.print("ID: ");
									idperson = new Scanner(System.in).nextInt();
									// keyboard.nextLine();
									System.out.print("DNI: ");
									dni = new Scanner(System.in).nextLine();
									System.out.print("Nombre: ");
									name = new Scanner(System.in).nextLine();
									System.out.print("Apellidos: ");
									surnames = new Scanner(System.in).nextLine();
									System.out.print("Localidad: ");
									locality = new Scanner(System.in).nextLine();
									System.out.print("Provincia: ");
									province = new Scanner(System.in).nextLine();
									System.out.print("CP: ");
									zipCode = new Scanner(System.in).nextLine();
									System.out.print("Direccion: ");
									direction = new Scanner(System.in).nextLine();
									// Generar proveedor
									Address a = new Address(locality, province, zipCode, direction);
									Supplier s = new Supplier(idperson, dni, name, surnames, a);
									prov.save(s);
									break;
								case 2: // Buscar proveedor
									System.out.print("Id del proveedor: ");
									idperson = new Scanner(System.in).nextInt();
									if (prov.search(idperson) != null) {
										Supplier proveidor = (Supplier) prov.search(idperson);
										System.out.println(proveidor.toString());
									} else {
										System.out.println("El proveidor no existe");
									}
									break;
								case 3: // Añadir proveedor
									System.out.println("MODIFICAR PROVEEDOR");
									// Obtener datos
									System.out.print("ID : ");
									idperson = new Scanner(System.in).nextInt();
									// keyboard.nextLine();
									if (clie.search(idperson) != null) {
										System.out.print("DNI: ");
										dni = new Scanner(System.in).nextLine();
										System.out.print("Nombre: ");
										name = new Scanner(System.in).nextLine();
										System.out.print("Apellidos: ");
										surnames = new Scanner(System.in).nextLine();
										System.out.print("Localidad: ");
										locality = new Scanner(System.in).nextLine();
										System.out.print("Provincia: ");
										province = new Scanner(System.in).nextLine();
										System.out.print("CP: ");
										zipCode = new Scanner(System.in).nextLine();
										System.out.print("Direccion: ");
										direction = new Scanner(System.in).nextLine();
										// Modificando
										Address ad = new Address(locality, province, zipCode, direction);
										Supplier su = new Supplier(idperson, dni, name, surnames, ad);
										prov.modify(su);

										Supplier proveidor = (Supplier) prov.search(idperson);
										System.out.println(proveidor.toString());
									} else {
										System.out.println("El proveidor no existe");
									}
									break;
								case 4: // Eliminar proveedor
									System.out.print("ID del proveedor:");
									idperson = new Scanner(System.in).nextInt();
									prov.delete(idperson);
									break;
								case 5: // Mostrar todos los proveedores
									mostrar("proveedores", prov);
									break;
								case 0:
									break;
							}
							if (option2 != 0)
								pulsaParaContinuar();
						} while (option2 != 0);
						break;
					case 0:
						break;
				}
			} while (option != 0);
			// keyboard.close();
		} catch (RuntimeException ex) {
			logger.log(Level.SEVERE, "Problema greu", ex);
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		} catch (StockInsuficientException ex) {
			ex.printStackTrace();
		} finally {
			ProductDAO.guardarFichero();
			System.out.println(TEXT_GREEN + "Resultado guardado");
			mostrarProductos(prodDAO);
			System.out.println(TEXT_RESET);
		}
	}

	// Vistas
	public static int menu(String type) {
		Scanner sc = new Scanner(System.in);
		int seleccion;
		switch (type) {
			case "inicial":
				System.out.println("BOTIGA");
				System.out.println("+----+");
				System.out.println("1. Productos");
				System.out.println("2. Clientes");
				System.out.println("3. Proveedores");
				break;
			case "productos":
				System.out.println("PRODUCTOS/PACKS < BOTIGA");
				System.out.println("+----------------+");
				System.out.println("1. Agregar");
				System.out.println("1. Buscar");
				System.out.println("3. Modificar producto");
				System.out.println("4. Eliminar");
				System.out.println("5. Mostrar todo");
				System.out.println("6. Agregar stock");
				System.out.println("7. Quitar stock");
				System.out.println("8. Mantenimiento de producto");
				break;
			case "clientes":
				System.out.println("CLIENTES < BOTIGA");
				System.out.println("+----------------+");
				System.out.println("1. Agregar");
				System.out.println("2. Buscar");
				System.out.println("3. Modificar");
				System.out.println("4. Eliminar");
				System.out.println("5. Mostrar todo");
				break;
			case "proveedores":
				System.out.println("PROVEEDORES < BOTIGA");
				System.out.println("+----------------+");
				System.out.println("1. Agregar");
				System.out.println("2. Buscar");
				System.out.println("3. Modificar");
				System.out.println("4. Eliminar");
				System.out.println("5. Mostrar todo");
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

	// Funcionalidades
	private static void pulsaParaContinuar() throws IOException {
		System.out.println("Pulsa para continuar...");
		System.in.read();
	}

	public static void mostrarProductos(ProductDAO<Product> prodDAO) {
		Persistable p = prodDAO;
		System.out.println(p.getMap().toString());
	}

	public static void mostrarClientes(DAO clieDAO) {
		Persistable p = clieDAO;
		System.out.println(p.getMap().toString());
	}

	public static void mostrarProveedores(DAO provDAO) {
		Persistable p = provDAO;
		System.out.println(p.getMap().toString());
	}

	public static void mostrar(String type, Object dao){
		Persistable p = null;
		switch (type) {
			case "productos":
				p = (ProductDAO<Product>) dao;
				break;
			case "clientes":
			case "proveedores":
				p = (DAO) dao;
				break;
		}
		System.out.println(p.getMap().toString());
	}
}
