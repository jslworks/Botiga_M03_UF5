package tools;

import java.util.TreeMap;

public interface Persistable<T> {

    public void save(T obj);

    public void delete(int id);
    public void delete(String id);

    public T search(int id);
    public T search(String id);

    public TreeMap<Integer, T> getMap();
    public TreeMap<String, T> getSMap();
}
