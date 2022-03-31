package tools;

import java.util.Comparator;

import model.bo.Product;

public class ProductNameComparator implements Comparator<Product> {

    @Override
    public int compare(Product o1, Product o2) {
        return o1.getNombre().compareTo(o2.getNombre());
    }
}