package Main.java.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Класс для улучшения работы с кешем
 *
 * @param <K>
 * @param <V>
 */
public class MyCacheExtendsLinckedHashMap<K, V> extends LinkedHashMap {
    private final int capacity;
    private long accessСounter = 0;
    private long topCounter = 0;

    public MyCacheExtendsLinckedHashMap(int capacity) {
        /**
         *  устанавливаем начальную емкость на единицу больше, чем требуемый размер кеша.
         *  Установка коэффициента нагрузки в 1,1 гарантирует, что механизм переопределения базового класса HashMap
         *  не будет запущен.
         */
        super(capacity + 1, 1.1f, true);
        this.capacity = capacity;
    }

    public Object get(Object key) {
        accessСounter++;
        if (containsKey(key)) {
            topCounter++;
        }
        Object value = super.get(key);
        return value;
    }

    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > capacity;
    }

    public long getAccessCount() {
        return accessСounter;
    }

    public long getHitCount() {
        return topCounter;
    }
}

