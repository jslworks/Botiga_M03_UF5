package bo;

import java.util.HashMap;

public interface Persistable<T> {

    public void save(T obj);

    public void delete(int id);

    public T search(int id);

    public HashMap<Integer, T> getMap();
}
