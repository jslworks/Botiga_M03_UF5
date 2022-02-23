

import java.util.HashMap;

public class DAO<T extends Identificable> implements Persistable<T> {
    
    private HashMap<Integer, T> hmDAO = new HashMap<Integer, T>();
    
    public T get(int id){
        return this.hmDAO.get(id);
    }
    
    @Override
    public void save(T t){
        this.hmDAO.put(t.getId(), t);
    }
    
    @Override
    public HashMap<Integer, T> getMap() {
        return hmDAO;
    }

     @Override
    public void delete(int id) {
        hmDAO.remove(id);
    }

    public T search(int id) {
        if (hmDAO.containsKey(id)) {
            return hmDAO.get(id);
        } else {
            return null;
        }
    }
    
    public void modify(T obj) {
        hmDAO.replace(obj.getId(), obj);
    }

}
