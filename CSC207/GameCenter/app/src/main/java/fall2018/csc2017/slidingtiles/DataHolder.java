package fall2018.csc2017.slidingtiles;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class DataHolder {
    /**
     * DataHolder saves current user in system
     */
    private static final DataHolder holder = new DataHolder();

    /**
     * data holding data memory
     */
    Map<String, WeakReference<Object>> data = new HashMap<String, WeakReference<Object>>();

    /**
     * get holder object
     * @return object holding data
     */
    public static DataHolder getInstance() {
        return holder;
    }

    /**
     * save data
     * @param id data reference
     * @param object data object
     */
    public void save(String id, Object object) {
        data.put(id, new WeakReference<Object>(object));
    }

    /**
     * retrieve data from data object
     * @param id data reference
     * @return memory
     */
    public Object retrieve(String id) {
        WeakReference<Object> objectWeakReference = data.get(id);
        return objectWeakReference.get();
    }
}