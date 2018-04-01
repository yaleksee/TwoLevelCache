package Main.java.interfaces;

import java.io.IOException;

/**
 * Контракт многоуровневого кеша
 *
 * @param <K>
 * @param <V>
 */

public interface FrecquencyCache<K, V> extends FirstCache<K, V>, MoveyObject<K> {
    void recache() throws IOException, ClassNotFoundException;
}