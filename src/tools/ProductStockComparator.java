package tools;

import java.util.Comparator;

import bo.Product;

public class ProductStockComparator implements Comparator<Product>{

    @Override
    public int compare(Product o1, Product o2) {
        return ( o1.getStock() - o2.getStock() );
    }
}