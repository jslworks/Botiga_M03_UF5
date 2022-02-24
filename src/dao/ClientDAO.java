package dao;

import java.util.HashMap;

import bo.Address;
import bo.Client;
import bo.Persistable;

public class ClientDAO<T> implements Persistable<T> {

    private HashMap<Integer, T> client = new HashMap<Integer, T>();

    public boolean addClient(Integer idperson, String dni, String name, String surnames, Address address) {
        if (client.containsKey(idperson)) {
            return false;
        } else {
            Client clie = new Client(idperson, dni, name, surnames, address);
            client.put(idperson, (T) clie);
            return true;
        }
    }

    public Client searchClient(int idperson) {
        if (client.containsKey(idperson)) {
            return (Client) client.get(idperson);
        } else {
            return null;
        }

    }

    public void modifyClient(int idperson, String dni, String name, String surnames) {
        Client clie = (Client) client.get(idperson);
        clie.setDni(dni);
        clie.setName(name);
        clie.setSurnames(surnames);
    }

    public void deleteClient(int idperson) {
        client.remove(idperson);
    }

    //Implementacio de la Interficie amb variable generica
    @Override
    public void save(Object obj) {
        if (obj != null && obj instanceof Client) {
            Client id = (Client) obj;
            client.put(id.getId(), (T) obj);
        } 
    }

    public HashMap<Integer, T> getMap() {
        return client;
    }

    @Override
    public void delete(int id) {
        if (client.containsKey(id)) {
            client.remove(id);
        }
    }

    @Override
    public T search(int id) {
        if (client.containsKey(id)) {
            return (T) (Client) client.get(id);
        } else {
            return null;
        }
    }

}
