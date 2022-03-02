package tools;

import java.util.Comparator;

import bo.Product;

public class ProductPriceComparator implements Comparator<Product>{

    @Override
    public int compare(Product o1, Product o2) {
        // System.out.println("--------------");
        // System.out.println(o1.getPrecio());
        // System.out.println(o2.getPrecio());
        // System.out.println(Double.compare(o1.getPrecio(), o2.getPrecio()));
        // System.out.println("--------------");
        return Double.compare(o1.getPrecio(), o2.getPrecio());
    }
}