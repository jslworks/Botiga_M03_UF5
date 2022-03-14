import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.logging.*;

import bo.Address;
import bo.Client;
import bo.Presencia;
import bo.Product;
import bo.Supplier;
import dao.DAO;
import dao.PresenciaDAO;
import dao.ProductDAO;
import err.StockInsuficientException;
import tools.Menu;

public class Controller {

	Menu menu = new Menu();

	// Logger
	static Logger logger = Logger.getLogger("Log Botiga");

	// DAO
	private ProductDAO<Product> prodDAO = new ProductDAO<Product>();
	private DAO prov = new DAO();
	private DAO clie = new DAO();
	private PresenciaDAO<Presencia> presenciaDAO = new PresenciaDAO<>();

	// Ejecutar
	public void run() {

		// Cargamos los datos desde fichero
		prodDAO.abrirFichero();
		menu.mostrar("productos", prodDAO);

		try {
			// Logger
			FileHandler fh = new FileHandler("log.txt", true);
			fh.setFormatter(new SimpleFormatter());
			logger.addHandler(fh);
			logger.setLevel(Level.ALL);
			fh.close();

			// Variables
			// Scanner keyboard = new Scanner(System.in);
			int option, option2;
			String inputResponse = "", nombreFichero = "";
			boolean ok = false;

			int seleccion;

			String name;

			int idproduct, stock;
			double price;
			Product producto = new Product(1, "Patata", 1, 5);

			do {
				option = menu.show("inicial");
				switch (option) {
					case 1: // PRODUCTOS
						do {
							option2 = menu.show("productos");
							switch (option2) {
								case 1: // Agregar nuevo
									menu.titulo(
											menu.getRb().getString("addNewProductTitle"));
									System.out.println(
											menu.getRb().getString("info_productOption"));
									System.out.println(
											menu.getRb().getString("info_packOption"));
									seleccion = new Scanner(System.in).nextInt();
									switch (seleccion) {
										case 1:
											menu.titulo(
													menu.getRb().getString("addProductTitle"));
											prodDAO.agregarProducto_pack("producto");
											break;
										case 2:
											menu.titulo(
													menu.getRb().getString("addPackTitle"));
											prodDAO.agregarProducto_pack("pack");
											break;
										default:
											break;
									}
									break;
								case 2: // Buscar
									menu.titulo(
											menu.getRb().getString("searchProductTitle"));
									System.out.println(
											menu.getRb().getString("info_productOption"));
									System.out.println(
											menu.getRb().getString("info_packOption"));
									seleccion = new Scanner(System.in).nextInt();
									switch (seleccion) {
										case 1:
											prodDAO.buscarProducto_pack("producto");
											break;
										case 2:
											prodDAO.buscarProducto_pack("pack");
											break;
										default:
											break;
									}
									break;
								case 3: // Modificar
									menu.titulo(
											menu.getRb().getString("modifyProductTitle"));
									// Obtener datos
									System.out.print(
											menu.getRb().getString("input_id"));
									idproduct = new Scanner(System.in).nextInt();
									if (prodDAO.search(idproduct) != null) {
										menu.titulo(
												menu.getRb().getString("modifyTitle"));
										System.out.print(
												menu.getRb().getString("input_name"));
										name = new Scanner(System.in).nextLine();

										System.out.print(
												menu.getRb().getString("input_price"));
										if (inputResponse.contains(",")) {
											inputResponse.replace(",", ".");
											price = Double.parseDouble(inputResponse);
										} else {
											price = new Scanner(System.in).nextDouble();
										}
										System.out.print(
												menu.getRb().getString("input_Stock"));
										stock = new Scanner(System.in).nextInt();

										// Modificando
										prodDAO.modifyProduct(idproduct, name, price, stock);
										Product products = (Product) prodDAO.search(idproduct);
										// Mostrar resultados
										System.out.println(products.toString());
									} else {
										menu.alerta(
												menu.getRb().getString("alert_productNotExist"), "");
									}
									break;
								case 4: // Eliminar
									menu.titulo(
											menu.getRb().getString("deleteProductTitle"));
									System.out.print(
											menu.getRb().getString("input_id"));
									idproduct = new Scanner(System.in).nextInt();
									prodDAO.delete(idproduct);
									break;
								case 5: // Mostrar todos
									seleccion = menu.show(
											menu.getRb().getString("showProductTitle"));
									switch (seleccion) {
										case 1:
											menu.mostrar("productos", prodDAO);
											break;
										case 2:
											prodDAO.mostrarOrdenadoPor("nombre");
											break;
										case 3:
											prodDAO.mostrarOrdenadoPor("precio");
											break;
										case 4:
											prodDAO.mostrarOrdenadoPor("stock");
											break;
										default:
											break;
									}
									break;
								case 6: // Agregar stock
									menu.titulo(
											menu.getRb().getString("addStockTitle"));
									// Selecciona metodo manual o automatico
									System.out.println(
											menu.getRb().getString("input_autoLoad"));
									inputResponse = new Scanner(System.in).nextLine();
									if (inputResponse.equalsIgnoreCase("S")) {
										System.out.println(
												menu.getRb().getString("input_fileName"));
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
										} catch (IOException ioe) {
											menu.alerta(
													menu.getRb().getString("alert_fileError"), ioe);
										}
									} else {
										menu.titulo(
												menu.getRb().getString("addStockTitle2"));
										System.out.print(
												menu.getRb().getString("input_id"));
										idproduct = new Scanner(System.in).nextInt();
										producto = prodDAO.search(idproduct);
										if (producto == null) {
											menu.alerta(
													menu.getRb().getString("alert_productNotExist"), "");
										} else {
											System.out.println(menu.getRb().getString("info_addProductStock") +
													"(" + idproduct + ") " + producto.getNombre());
											int addStock = new Scanner(System.in).nextInt();
											producto.putStock(addStock);

											prodDAO.modifyProduct(producto);
										}
									}
									break;
								case 7: // Quitar stock
									menu.titulo(
											menu.getRb().getString("removeStockTitle"));
									// Selecciona metodo manual o automatico
									System.out.println(
											menu.getRb().getString("input_autoLoad"));
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
										} catch (IOException ioe) {
											menu.alerta(menu.getRb().getString("alert_fileError"), ioe);
										}
									} else {
										System.out.print(
												menu.getRb().getString("input_id"));
										idproduct = new Scanner(System.in).nextInt();
										producto = prodDAO.search(idproduct);
										if (producto == null) {
											menu.alerta(
													menu.getRb().getString("alert_productNotExist"), producto);
										} else {
											int takeStock = -1;
											do {
												System.out
														.println(menu.getRb().getString("info_removeProductStock") + "("
																+ producto.getId() + ") "
																+ producto.getNombre());
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
												menu.alerta(
														menu.getRb().getString("alert_fileError"), producto);
											}
										}
									}
									break;
								case 8: // Mantenimiento de productos
									menu.titulo(
											menu.getRb().getString("productoOrderTitle"));
									System.out.print(
											menu.getRb().getString("input_fileName"));
									nombreFichero = new Scanner(System.in).nextLine();
									DataOutputStream dos = new DataOutputStream(
											new BufferedOutputStream(new FileOutputStream(nombreFichero)));
									do {
										// Obtener datos
										System.out.print(
												menu.getRb().getString("input_id"));
										idproduct = new Scanner(System.in).nextInt();
										producto = prodDAO.search(idproduct);
										if (producto != null) {
											System.out.print(
													menu.getRb().getString("input_amount"));
											stock = new Scanner(System.in).nextInt();
											// keyboard.nextLine();
											dos.writeInt(idproduct);
											dos.writeInt(stock);
										} else {
											menu.alerta(
													menu.getRb().getString("alert_productNotExist"), "");
										}
										System.out.println(
												menu.getRb().getString("input_continue"));
										inputResponse = new Scanner(System.in).nextLine();
									} while (inputResponse.equalsIgnoreCase("S"));
									dos.close();
									break;
								case 9: // Catálogo
									menu.titulo(
											menu.getRb().getString("showCatalogueTitle"));
									// Pedir fecha o pulsar tecla si es dia de hoy
									LocalDate fecha = prodDAO.pedirFecha("fromDate").get(0);
									// Mostrar productos descatalogados
									prodDAO.mostrarDescatalogados(fecha);
									break;
								case 0:
									break;
								default:
									menu.alerta(
											menu.getRb().getString("alert_wrongOption"), "");
									break;
							}
							if (option2 != 0)
								menu.pulsaParaContinuar();
						} while (option2 != 0);
						break;
					case 2: // CLIENTES
						this.clientes();
						break;
					case 3: // PROVEEDORES
						this.proveedores();
						break;
					case 4:
						this.empleados();
						break;
					case 0:
						break;
				}
			} while (option != 0);
		} catch (RuntimeException ex) {
			logger.log(Level.SEVERE, menu.TEXT_RED + "Problema greu", ex);
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		} catch (StockInsuficientException ex) {
			ex.printStackTrace();
		} finally {
			ProductDAO.guardarFichero();
			menu.sistema(
					menu.getRb().getString("info_save_ok"));
			menu.mostrar("productos", prodDAO);
		}
	}

	////////////////////////////////////////////////////////////////////////
	// EMPLEADOS, CLIENTES Y PROVEEDORES
	//////////
	public void empleados() {
		int option = menu.show("empleados");
		if (option != 0) {
			System.out.print(
					menu.getRb().getString("input_id"));
			int idEmpleado = new Scanner(System.in).nextInt();
			switch (option) {
				case 1: // Fichar entrada
					menu.sistema(
							menu.getRb().getString("employeeEntryTitle"));
					presenciaDAO.ficharEntrada(idEmpleado);
					break;
				case 2: // Fichar salida
					menu.sistema(
							menu.getRb().getString("employeeExitTitle"));
					presenciaDAO.ficharSalida(idEmpleado);
					break;
				case 3: // Consultar
					menu.sistema(
							menu.getRb().getString("employeeConsultTitle"));
					presenciaDAO.consultaDia(idEmpleado);
					break;
				default:
					break;
			}
		}

	}

	public void clientes() throws IOException {
		int option2, idperson;
		String dni, name, surnames, locality, province, zipCode, direction;
		do {
			option2 = menu.show("clientes");
			switch (option2) {
				case 1: // Agregar cliente
					menu.titulo("CREAR CLIENTE");
					// Obtener datos
					System.out.print("ID: ");
					idperson = new Scanner(System.in).nextInt();
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
					menu.titulo("BUSCAR CLIENTE");
					System.out.print("ID: ");
					idperson = new Scanner(System.in).nextInt();
					if (clie.search(idperson) != null) {
						Client client = (Client) clie.search(idperson);
						System.out.println(client.toString());
					} else {
						menu.alerta("El cliente no existe", "");
					}
					break;
				case 3: // Modificar cliente
					menu.titulo("MODIFICAR CLIENTE");
					// Obtener datos
					System.out.print("ID : ");
					idperson = new Scanner(System.in).nextInt();
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
						menu.alerta("El cliente no existe", "");
					}
					break;
				case 4: // Eliminar cliente
					menu.titulo("ELIMINAR CLIENTE");
					System.out.print("ID: ");
					idperson = new Scanner(System.in).nextInt();
					clie.delete(idperson);
					break;
				case 5: // Mostrar todos los clientes
					menu.titulo("TODOS CLIENTES");
					menu.mostrar("clientes", clie);
					break;
				case 0:
					break;
			}
			if (option2 != 0)
				menu.pulsaParaContinuar();
		} while (option2 != 0);
	}

	public void proveedores() throws IOException {
		int option2, idperson;
		String dni, name, surnames, locality, province, zipCode, direction;
		do {
			option2 = menu.show("proveedores");
			switch (option2) {
				case 1: // Agregar proveedor
					menu.titulo("PROVEEDOR");
					// Obtener datos
					System.out.print("ID: ");
					idperson = new Scanner(System.in).nextInt();
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
					menu.titulo("BUSCAR PROVEEDOR");
					System.out.print("ID: ");
					idperson = new Scanner(System.in).nextInt();
					if (prov.search(idperson) != null) {
						Supplier proveidor = (Supplier) prov.search(idperson);
						System.out.println(proveidor.toString());
					} else {
						menu.alerta("El proveedor no existeix", "");
					}
					break;
				case 3: // Añadir proveedor
					menu.titulo("MODIFICAR PROVEEDOR");
					// Obtener datos
					System.out.print("ID : ");
					idperson = new Scanner(System.in).nextInt();
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
						menu.alerta("El proveedor no existeix", "");
					}
					break;
				case 4: // Eliminar proveedor
					menu.titulo("ELIMINAR PROVEEDOR");
					System.out.print("ID: ");
					idperson = new Scanner(System.in).nextInt();
					prov.delete(idperson);
					break;
				case 5: // Mostrar todos los proveedores
					menu.titulo("TODOS PROVEEDORES");
					menu.mostrar("proveedores", prov);
					break;
				case 0:
					break;
			}
			if (option2 != 0)
				menu.pulsaParaContinuar();
		} while (option2 != 0);
	}

}
