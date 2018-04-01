package Main.java.cache;

import Main.java.comparator.ComparatorCacheMap;
import Main.java.interfaces.MoveyObject;
import Main.java.interfaces.FirstCache;

import java.io.*;
import java.util.*;

/**
 * Класс для хранения объектов на жестком диске.
 *
 * @param <K>
 * @param <V>
 */
public class HardCache<K, V extends Serializable> implements FirstCache<K, V>, MoveyObject<K> {
    Map<K, String> linkedHashMap;
    Map<K, Integer> accrualsMap;

    public HardCache(int capacity) {
        linkedHashMap = new MyCacheExtendsLinckedHashMap<K, String>(capacity);
        accrualsMap = new TreeMap<K, Integer>();

        File tempFolder = new File("dir\\");
        if (!tempFolder.exists()) {
            tempFolder.mkdirs();
        }
    }

    /**
     * Реализация базового контракта кеширвоания для объектов
     */

    @Override
    public void cache(K key, V value) throws IOException {
        String path;
        path = "dir\\" + UUID.randomUUID().toString();

        accrualsMap.put(key, 1);
        linkedHashMap.put(key, path);

        FileOutputStream fileStr = new FileOutputStream(path);
        ObjectOutputStream objectStr = new ObjectOutputStream(fileStr);

        objectStr.writeObject(value);
        objectStr.flush();
        fileStr.flush();
        objectStr.close();
        fileStr.close();
    }

    @Override
    public V getObj(K key) throws IOException, ClassNotFoundException {
        if (linkedHashMap.containsKey(key)) {
            //получаем по ключу адрес файла на жестком диске из LinkedHashMap
            String pathToObject = linkedHashMap.get(key);

            try (FileInputStream fileStr = new FileInputStream(pathToObject);
                 ObjectInputStream objectStr = new ObjectInputStream(fileStr)) {

                V valueDeseriazibale = (V) objectStr.readObject();

                int cointMemorySize = accrualsMap.get(key);
                accrualsMap.put(key, ++cointMemorySize);

                return valueDeseriazibale;
            } catch (ClassNotFoundException ex) {
                return null;
            } catch (IOException ex) {
                return null;
            }
        }
        return null;
    }

    @Override
    public void deleteObj(K key) {
        if (linkedHashMap.containsKey(key)) {
            File deletingFile = new File(linkedHashMap.remove(key));
            accrualsMap.remove(key);
            deletingFile.delete();
        }
    }

    @Override
    public void clearCache() {
        for (K key : linkedHashMap.keySet()) {
            File deletingFile = new File(linkedHashMap.get(key));
            deletingFile.delete();
        }
        linkedHashMap.clear();
        accrualsMap.clear();
    }

    @Override
    public V removeObj(K key) throws IOException, ClassNotFoundException {
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

    @Override
    public int getCointCallingObjeck(K key) {
        if (linkedHashMap.containsKey(key)) {
            return accrualsMap.get(key);
        }
        return 0;
    }
}