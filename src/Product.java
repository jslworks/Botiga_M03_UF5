
import java.util.Objects;
import err.StockInsuficientException;

public class Product implements Identificable{

    private Integer idProduct;
    private String name;
    private int price;
    private int stock;

    @Override
    public int getId() {
        return this.idProduct;
    }   
    //Getters dels productes
    public Integer getIdProduct() {
        return idProduct;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    //Setters del producte
    public void setIdproduct(int i) {
        this.idProduct = i;
    }

    public void setName(String n) {
        this.name = n;
    }

    public void setPrice(int p) {
        this.price = p;
    }

    public void setStock(int s) {
        this.stock = s;
    }
    
    // Transacciones stock
    public void putStock(int cantidad) {
    	this.setStock(this.getStock() + cantidad);
    }

    public void takeStock(int cantidad) throws StockInsuficientException{
    	if (this.getStock() > cantidad) {
        	this.setStock(this.getStock() - cantidad);
		} else {
			throw new StockInsuficientException("No hay suficiente stock de " + this.getName()); 
		}
    }
    
    //El constructors del produte
    public Product(int idproduct, String nom, int price, int stock) {
        this.idProduct = idproduct;
        this.name = nom;
        this.price = price;
        this.stock = stock;
    }

    public Product(int idproduct, String nom, int price) {
        this.idProduct = idproduct;
        this.name = nom;
        this.price = price;

    }
    //El metode toString
    @Override
    public String toString() {
        return "Producte{" + "idproduct=" + idProduct + ", nom=" + name + ", price=" + price + ", stock=" + stock + '}';
    }

    //Metode equals del name
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product) {
            Product temp = (Product) obj;
            return temp.getName().equals(obj);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.name);
        return hash;
    }

    

}
