package tools;

import java.util.Comparator;

import bo.Product;

public class ProductPriceComparator implements Comparator<Product> {

    @Override
    public int compare(Product o1, Product o2) {
        return Double.compare(o1.getPrecio(), o2.getPrecio());
    }
}