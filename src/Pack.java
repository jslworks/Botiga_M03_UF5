

import java.util.ArrayList;
import java.util.Objects;

public final class Pack extends Product {

    private ArrayList<Integer> list = new ArrayList<>();
    private double percentageDiscount = 0.0;

    public Pack(ArrayList<Integer> list, double percentatgeDiscount, int idproduct, String name, double price) {
        super(idproduct, name, price);
        this.list = list;
        this.percentageDiscount = percentatgeDiscount;
    }

    public double getPercetatgeDescompte() {
        return percentageDiscount;
    }

    public void setPercetatgeDescompte(double percetatgeDescompte) {
        this.percentageDiscount = percetatgeDescompte;
    }

    //afegir un producte a la llista o eliminar un producte de la llista
    public void removeProduct(int p) {
        this.list.remove(p);
    }

    public void addProduct(int i) {
        this.list.add(i);
    }

    @Override
    public String toString() {
        String products = super.toString();
        return products;

    }

    //Metode equals del id del pack
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.list);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pack) {
            Pack temp = (Pack) obj;
            return temp.getName().equals(obj);
        } else {
            return false;
        }
    }

}
