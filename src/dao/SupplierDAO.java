package dao;

import java.util.ArrayList;
import java.util.TreeMap;

import bo.Address;
import bo.Product;
import bo.Supplier;
import tools.Persistable;

public class SupplierDAO<T> implements Persistable<T> {

    private TreeMap<Integer, T> supplier = new TreeMap<Integer, T>();

    public boolean addSupplier(Integer idperson, String dni, String name, String surnames, Address address) {
        if (supplier.containsKey(idperson)) {
            return false;
        } else {
            Supplier prov = new Supplier(idperson, dni, name, surnames, address);
            supplier.put(idperson, (T) prov);
            return true;
        }
    }

    public Supplier searchSupplier(int idperson) {
        if (supplier.containsKey(idperson)) {
            return (Supplier) supplier.get(idperson);
        } else {
            return null;
        }

    }

    public void modifySupplier(int idperson, String dni, String name, String surnames) {
        Supplier supp = (Supplier) supplier.get(idperson);
        supp.setDni(dni);
        supp.setName(name);
        supp.setSurnames(surnames);
    }

    public void deleteSupplier(int idperson) {
        supplier.remove(idperson);
    }

    public ArrayList<String> printSupplier() {
        ArrayList<String> llistasupp = new ArrayList<String>();
        llistasupp.add(supplier.toString());
        return llistasupp;
    }

    @Override
    public void save(Object obj) {
        if (obj != null && obj instanceof Product) {
            Supplier id = (Supplier) obj;
            supplier.put(id.getId(), (T) obj);
        }
    }

    // Implementacio de la Interficie amb variable generica
    @Override
    public TreeMap<Integer, T> getMap() {
        return supplier;
    }

    @Override
    public void delete(int id) {
        if (supplier.containsKey(id)) {
            supplier.remove(id);
        }
    }

    @Override
    public T search(int id) {
        if (supplier.containsKey(id)) {
            return (T) (Supplier) supplier.get(id);
        } else {
            return null;
        }
    }

}
