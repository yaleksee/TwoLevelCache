package Main.java.cache;

import Main.java.comparator.ComparatorCacheMap;
import Main.java.interfaces.FirstCache;
import Main.java.interfaces.MoveyObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * класс кеша в ОП
 *
 * @param <K>
 * @param <V>
 */
public class FirstLevelCache<K, V> implements FirstCache<K, V>, MoveyObject<K> {
    private Map<K, V> linkedHashMap;
    private Map<K, Integer> accrualsMap;

    public FirstLevelCache(int capacity) {
        linkedHashMap = new MyCacheExtendsLinckedHashMap<K, V>(capacity);
        accrualsMap = new TreeMap<K, Integer>();
    }

    /**
     * Реализация базового контракта кеширвоания для объектов
     *
     * @param key
     * @param value
     */
    @Override
    public void cache(K key, V value) {
        accrualsMap.put(key, 1);
        linkedHashMap.put(key, value);
    }


    @Override
    public V getObj(K key) {
        if (linkedHashMap.containsKey(key)) {
            int frequency = accrualsMap.get(key);
            accrualsMap.put(key, ++frequency); // фиксация новой записи
            return linkedHashMap.get(key);
        }
        return null;
    }


    @Override
    public void deleteObj(K key) {
        if (linkedHashMap.containsKey(key)) {
            linkedHashMap.remove(key);
            accrualsMap.remove(key);
        }
    }


    @Override
    public void clearCache() {
        linkedHashMap.clear();
        accrualsMap.clear();
    }


    @Override
    public V removeObj(K key) {
        if (linkedHashMap.containsKey(key)) {
            V result = this.getObj(key);
            this.deleteObj(key);
            return result;
        }
        return null;
    }


    @Override
    public boolean checkKey(K key) {
        return linkedHashMap.containsKey(key);
    }


    @Override
    public int size() {
        return linkedHashMap.size();
    }

    /**
     * Реализация контракта на перемещение данных между верхним и нижним кэшем
     */

    /**
     * Возвращает представление карты в виде множества отсортированных ключей.
     *
     * @return
     */
    @Override
    public Set<K> getUsedKey() {
        ComparatorCacheMap comp = new ComparatorCacheMap(accrualsMap);
        Map<K, Integer> sort = new TreeMap(comp);
        sort.putAll(accrualsMap);
        return sort.keySet();
    }

    /**
     * Возвращает частоту ипользования ключа
     *
     * @param key
     * @return
     */
    @Override
    public int getCointCallingObjeck(K key) {
        if (linkedHashMap.containsKey(key)) {
            return accrualsMap.get(key);
        }
        return 0;
    }
}
