
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.util.logging.*;

import err.StockInsuficientException;

public class Controller {

	static Logger logger = Logger.getLogger("MyLog");

	private ProductDAO<Product> prodDAO = new ProductDAO<Product>();
	private DAO prov = new DAO();
	private DAO clie = new DAO();

	public void run() {

		prodDAO.abrirFichero();

		try {

			// Logger
			FileHandler fh = new FileHandler("log.txt", true);
			fh.setFormatter(new SimpleFormatter());
			logger.addHandler(fh);
			logger.setLevel(Level.ALL);
			fh.close();

			// Variables
			Scanner keyboard = new Scanner(System.in);
			int idperson;
			String dni, name, surnames;
			String locality, province, zipCode, direction;

			int idproduct, stock;
			double price, percentatgeDiscount;
			Product producto = new Product(1, "papa", 1, 5);

			int option, option2, option3;
			String inputResponse = "";

			boolean ok = false;

			do {
				System.out.println("Que vols fer?");
				System.out.println("0.Sortir");
				System.out.println("1.productos");
				System.out.println("2.clientes");
				System.out.println("3.Proveedores");

				option = keyboard.nextInt();
				keyboard.nextLine();

				switch (option) {
					case 1: // PRODUCTOS
						do {
							System.out.println("Que vols fer?");
							System.out.println("0.Sortir");
							System.out.println("1.Agregar producto/pack");
							System.out.println("2.Buscar producto/pack");
							System.out.println("3.Modificar producto");
							System.out.println("4.Eliminar producto/pack");
							System.out.println("5.Mostrar tots els productos/packs");
							System.out.println("6.Agregar stock");
							System.out.println("7.Quitar stock");

							option2 = keyboard.nextInt();
							keyboard.nextLine();

							switch (option2) {
								case 1: // Agregar nuevo
									System.out.println("1.Agregar un producto");
									System.out.println("2.Agregar un pack");
									option3 = keyboard.nextInt();

									if (option3 == 1) {
										System.out.println("ID del producto:");
										idproduct = keyboard.nextInt();
										keyboard.nextLine();

										System.out.println("Nom del producto:");
										name = keyboard.nextLine();

										System.out.println("Preu del producto:");
										inputResponse = keyboard.nextLine();
										if (inputResponse.contains(","))
											inputResponse.replace(",", ".");
										price = Double.parseDouble(inputResponse);
										keyboard.nextLine();

										System.out.println("Stock del producto:");
										stock = keyboard.nextInt();

										Product p = new Product(idproduct, name, price, stock);

										prodDAO.save(p);
										System.out.println("producto afegit");
									}

									if (option3 == 2) {
										System.out.println("Percentatge de descompte del pack:");
										inputResponse = keyboard.nextLine();
										if (inputResponse.contains(",")) {
											inputResponse.replace(",", ".");
											percentatgeDiscount = Double.parseDouble(inputResponse);
										} else {
											percentatgeDiscount = keyboard.nextDouble();
										}

										System.out.println("ID del producto:");
										idproduct = keyboard.nextInt();
										keyboard.nextLine();

										System.out.println("Nom del producto:");
										name = keyboard.nextLine();

										System.out.println("Preu del pack:");
										inputResponse = keyboard.nextLine();
										if (inputResponse.contains(",")) {
											inputResponse.replace(",", ".");
											price = Double.parseDouble(inputResponse);
										} else {
											price = keyboard.nextDouble();
										}

										ArrayList<Integer> packList = new ArrayList<>();
										Pack p = new Pack(packList, percentatgeDiscount, idproduct, name, price);
										prodDAO.save(p);
										System.out.println("Pack afegit");
									}
									break;
								case 2: // Buscar
									System.out.println("1.Buscar un producto");
									System.out.println("2.Buscar un pack");
									option3 = keyboard.nextInt();
									if (option3 == 1) {
										System.out.println("Id del producto:");
										idproduct = keyboard.nextInt();
										if (prodDAO.search(idproduct) != null) {
											Product productos = (Product) prodDAO.search(idproduct);
											System.out.println(productos.toString());
										} else {
											System.out.println("El producto no existeix");
										}
									}

									if (option3 == 2) {
										System.out.println("Id del pack:");
										idproduct = keyboard.nextInt();
										if (prodDAO.search(idproduct) != null) {
											Pack pck = (Pack) prodDAO.search(idproduct);
											System.out.println(pck.toString());
										} else {
											System.out.println("El pack no existeix");
										}
									}

									break;
								case 3: // Modificar
									System.out.println("ID del producto que vols modificar:");
									idproduct = keyboard.nextInt();
									keyboard.nextLine();
									if (prodDAO.search(idproduct) != null) {
										System.out.println("Nom del producto:");
										name = keyboard.nextLine();

										System.out.println("Preu del producto:");
										if (inputResponse.contains(",")) {
											inputResponse.replace(",", ".");
											price = Double.parseDouble(inputResponse);
										} else {
											price = keyboard.nextDouble();
										}

										System.out.println("Stock del producto:");
										stock = keyboard.nextInt();

										// Product p = new Product(idproduct, name, price, stock);
										// prod.modify(p);
										prodDAO.modifyProduct(idproduct, name, price, stock);

										Product products = (Product) prodDAO.search(idproduct);
										System.out.println(products.toString());
									} else {
										System.out.println("El producto no existeix");
									}
									break;
								case 4: // Eliminar
									System.out.println("ID del producto/pack que vols eliminar:");
									idproduct = keyboard.nextInt();
									keyboard.nextLine();
									prodDAO.delete(idproduct);

									break;
								case 5: // Mostrar todos
									Persistable p = prodDAO;
									print(p);
									break;
								case 6: // Agregar stock
									// Selecciona m�todo manual o autom�tico
									System.out.println("Cargar autom�ticamente? (S/n)");
									inputResponse = keyboard.nextLine();

									if (inputResponse.equalsIgnoreCase("S")) {
										try (DataInputStream dis = new DataInputStream(
												new BufferedInputStream(new FileInputStream("comanda_rebuda.txt")))) {

											while (dis.available() > 0) {
												idproduct = Integer.parseInt(dis.readUTF());
												stock = dis.readInt();
												producto = prodDAO.searchProduct(idproduct);
												if (prodDAO.searchProduct(idproduct) != null) {
													producto.putStock(stock);
												}
											}

										} catch (IOException e) {
											System.out.println("Error amb el fitxer: " + e);
										}
									} else {
										System.out.println("Id del producto:");
										idproduct = keyboard.nextInt();
										producto = prodDAO.searchProduct(idproduct);
										if (producto == null) {
											System.out.println("El producto no existeix");
										} else {
											System.out.println("A�adir stock al producto (" + producto.getId() + ") "
													+ producto.getName());
											int addStock = keyboard.nextInt();
											producto.putStock(addStock);

											prodDAO.modifyProduct(producto);

											System.out.println(producto);
										}
									}
									break;
								case 7: // Quitar stock
									// Selecciona metodo manual o automatico
									System.out.println("Cargar autom�ticamente? (S/n)");
									inputResponse = keyboard.nextLine();

									if (inputResponse.equalsIgnoreCase("S")) {
										try (DataInputStream dis = new DataInputStream(
												new BufferedInputStream(new FileInputStream("comanda_rebuda.txt")))) {

											while (dis.available() > 0) {
												idproduct = Integer.parseInt(dis.readUTF());
												stock = dis.readInt();
												producto = prodDAO.searchProduct(idproduct);
												if (prodDAO.searchProduct(idproduct) != null) {
													producto.takeStock(stock);
												}
											}

										} catch (IOException e) {
											System.out.println("Error amb el fitxer: " + e);
										}
									} else {
										System.out.println("Id del producto:");
										idproduct = keyboard.nextInt();
										producto = prodDAO.searchProduct(idproduct);
										if (producto == null) {
											System.out.println("El producto no existeix");
										} else {
											int takeStock = -1;
											do {
												System.out
														.println("Quitar stock al producto (" + producto.getId() + ") "
																+ producto.getName());
												takeStock = keyboard.nextInt();
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
												System.out.println(producto);
											} else {
												System.out.println("No se ha modificado el producto " + producto);
											}

										}
									}
									break;
								default:
									System.out.println("Opci�n incorrecta");
									break;
							}
						} while (option2 != 0);
						break;

					case 2: // CLIENTES
						do {
							System.out.println("Que vols fer?");
							System.out.println("0.Sortir");
							System.out.println("1.Agregar cliente");
							System.out.println("2.Buscar cliente");
							System.out.println("3.Modificar cliente");
							System.out.println("4.Esborrar cliente");
							System.out.println("5.Mostrar tots els clientes");
							option2 = keyboard.nextInt();
							keyboard.nextLine();
							switch (option2) {
								case 1:
									System.out.println("ID del cliente:");
									idperson = keyboard.nextInt();
									keyboard.nextLine();

									System.out.println("DNI del cliente:");
									dni = keyboard.nextLine();

									System.out.println("Nom del cliente:");
									name = keyboard.nextLine();

									System.out.println("Cognom del cliente:");
									surnames = keyboard.nextLine();

									System.out.println("Localitat del cliente");
									locality = keyboard.nextLine();

									System.out.println("Provincia del cliente");
									province = keyboard.nextLine();
									System.out.println("Codi postal del cliente");
									zipCode = keyboard.nextLine();

									System.out.println("Direcci� del cliente");
									direction = keyboard.nextLine();

									Address a = new Address(locality, province, zipCode, direction);
									Client cl = new Client(idperson, dni, name, surnames, a);

									clie.save(cl);
									System.out.println("cliente afegit");
									break;
								case 2:
									System.out.println("Id del cliente:");
									idperson = keyboard.nextInt();
									if (clie.search(idperson) != null) {
										Client client = (Client) clie.search(idperson);
										System.out.println(client.toString());
									} else {
										System.out.println("El cliente no existeix");
									}
									break;
								case 3:
									System.out.println("ID del cliente que vols modificar:");
									idperson = keyboard.nextInt();
									keyboard.nextLine();
									if (clie.search(idperson) != null) {
										System.out.println("DNI del cliente:");
										dni = keyboard.nextLine();

										System.out.println("Nom del cliente:");
										name = keyboard.nextLine();

										System.out.println("Cognom del cliente:");
										surnames = keyboard.nextLine();

										System.out.println("Localitat del cliente");
										locality = keyboard.nextLine();

										System.out.println("Provincia del cliente");
										province = keyboard.nextLine();
										System.out.println("Codi postal del cliente");
										zipCode = keyboard.nextLine();

										System.out.println("Direccion del cliente");
										direction = keyboard.nextLine();

										Address ad = new Address(locality, province, zipCode, direction);

										Client c = new Client(idperson, dni, name, surnames, ad);
										clie.modify(c);

									} else {
										System.out.println("El cliente no existeix");
									}
									break;
								case 4:
									System.out.println("ID del clientee que vols eliminar:");
									idperson = keyboard.nextInt();
									clie.delete(idperson);
									break;
								case 5:
									Persistable p = clie;
									print(p);
									break;
							}
						} while (option2 != 0);
						break;
					case 3: // PROVEEDORES
						do {
							System.out.println("Que vols fer?");
							System.out.println("0.Sortir");
							System.out.println("1.Agregar proveidor");
							System.out.println("2.Buscar proveidor");
							System.out.println("3.Modificar proveidor");
							System.out.println("4.Esborrar proveidor");
							System.out.println("5.Mostrar tots els proveidors");
							option2 = keyboard.nextInt();
							keyboard.nextLine();
							switch (option2) {
								case 1:
									System.out.println("ID del proveidor:");
									idperson = keyboard.nextInt();
									keyboard.nextLine();

									System.out.println("DNI del proveidor:");
									dni = keyboard.nextLine();

									System.out.println("Nom del proveidor:");
									name = keyboard.nextLine();

									System.out.println("Cognom del proveidor:");
									surnames = keyboard.nextLine();

									System.out.println("Localitat del cliente");
									locality = keyboard.nextLine();

									System.out.println("Provincia del cliente");
									province = keyboard.nextLine();
									System.out.println("Codi postal del cliente");
									zipCode = keyboard.nextLine();

									System.out.println("Direccion del cliente");
									direction = keyboard.nextLine();

									Address a = new Address(locality, province, zipCode, direction);

									Supplier s = new Supplier(idperson, dni, name, surnames, a);

									prov.save(s);
									System.out.println("Proveidor afegit");
									break;
								case 2:
									System.out.println("Id del proveidor:");
									idperson = keyboard.nextInt();
									if (prov.search(idperson) != null) {
										Supplier proveidor = (Supplier) prov.search(idperson);
										System.out.println(proveidor.toString());
									} else {
										System.out.println("El proveidor no existeix");
									}
									break;
								case 3:
									System.out.println("ID del proveidor que vols modificar:");
									idperson = keyboard.nextInt();
									if (clie.search(idperson) != null) {
										System.out.println("DNI del proveidor:");
										dni = keyboard.nextLine();

										System.out.println("Nom del proveidor:");
										name = keyboard.nextLine();

										System.out.println("Cognom del proveidor:");
										surnames = keyboard.nextLine();

										System.out.println("Localitat del proveidor");
										locality = keyboard.nextLine();

										System.out.println("Provincia del proveidor");
										province = keyboard.nextLine();
										System.out.println("Codi postal del proveidor");
										zipCode = keyboard.nextLine();

										System.out.println("Direcci� del proveidor");
										direction = keyboard.nextLine();

										Address ad = new Address(locality, province, zipCode, direction);
										Supplier su = new Supplier(idperson, dni, name, surnames, ad);
										prov.modify(su);

										Supplier proveidor = (Supplier) prov.search(idperson);
										System.out.println(proveidor.toString());
									} else {
										System.out.println("El proveidor no existeix");
									}
									break;
								case 4:
									System.out.println("ID del proveidor que vols eliminar:");
									idperson = keyboard.nextInt();
									prov.delete(idperson);
									break;
								case 5:
									Persistable p = prov;
									print(p);
									break;
							}

						} while (option2 != 0);
						break;
				}
			} while (option != 0);

			keyboard.close();
		} catch (RuntimeException ex) {
			logger.log(Level.SEVERE, "Problema greu", ex);
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		} catch (StockInsuficientException ex) {
			ex.printStackTrace();
		} finally {
			ProductDAO.guardarFichero();
		}
	}

	public static void print(Persistable obj) {
		System.out.println(obj.getMap().toString());
	}
}
