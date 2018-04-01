package Main.java.interfaces;
import java.io.IOException;

/**
 * базовый контракт для любого кэша
 */
public interface FirstCache<K, V> {
    void cache(K key, V value) throws IOException, ClassNotFoundException;

    V getObj(K key) throws IOException, ClassNotFoundException;

    V removeObj(K key) throws IOException, ClassNotFoundException;

    void deleteObj(K key);

    void clearCache();

    boolean checkKey(K key);

    int size();
}
