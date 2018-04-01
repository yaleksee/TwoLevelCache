package Main.java.interfaces;

import java.util.Set;

public interface MoveyObject<K> {
    Set<K> getUsedKey();

    int getCointCallingObjeck(K key);
}