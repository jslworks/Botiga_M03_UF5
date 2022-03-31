package model.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeMap;

import model.bo.Persona;
import model.bo.Product;
/**
 * Clase per gestionar la persistència de les dades de les persones
 * @author manuel
 *
 */
public class ProductDAO {

	private TreeMap<Integer, Product> mapaProductos = new TreeMap<>();
	
	public boolean save(Product producto){
		mapaProductos.put(producto.getId(), producto);
		return true;
	}

	public boolean delete(Integer id){

		if (mapaProductos.containsKey(id)){
			mapaProductos.remove(id);
			return true;
		}

		return false;
	}

	public Product find(Integer id){

		if (id == null || id == 0){
			return null;
		}

		return mapaProductos.get(id);
	}

	public void saveAll(){

		try (ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream("personas.dat"))) {
			oo.writeObject(mapaProductos);
		} catch (IOException e) {
			System.out.println("Error escribiendo fichero");
		}

	}

	@SuppressWarnings("unchecked")
	public void openAll(){

		File file = new File("productos.dat");
		if (file.exists()) {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
				mapaProductos = (TreeMap<Integer, Product>) ois.readObject();
			} catch (Exception e) {
				System.out.println("Error leyendo fichero");
			}
		}
	}

	public void showAll(){

		System.out.println("-------------------");
		System.out.println("Todos los productos");
		System.out.println("-------------------");
		
		for (Product producto : mapaProductos.values()) {
		    System.out.println(producto);
		    System.out.println("-------------------");
		}
	}
}

