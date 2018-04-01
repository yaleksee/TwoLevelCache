package Main.java.comparator;

import java.util.Comparator;
import java.util.Map;

/**
 * Версия компаратора не удаляет из сета объекты с одинаковой частотой
 */
public class ComparatorCacheMap implements Comparator {
    Map cacheMap;

    public ComparatorCacheMap(Map map) {
        this.cacheMap = map;
    }

    @Override
    public int compare(Object o1, Object o2) {
        int obj_1 = (Integer) cacheMap.get(o1);
        int obj_2 = (Integer) cacheMap.get(o2);
        if (obj_1 < obj_2) {
            return 1;
        } else if (obj_1 == obj_2) {
            return 1;
        } else {
            return -1;
        }
    }
}
