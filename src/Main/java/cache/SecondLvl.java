package Main.java.cache;

import Main.java.interfaces.FrecquencyCache;
import Main.java.interfaces.FirstCache;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

/**
 * класс двухуровневого кэша
 *
 * @param <K>
 * @param <V>
 */

public class SecondLvl<K, V extends Serializable> extends Object implements FrecquencyCache<K, V> {
    FirstLevelCache<K, V> firstLvl;
    HardCache<K, V> hardCache;

    int maxfirstLvl;
    int numberReq;
    int numberReqCallVoidRecache;

    /**
     * Задание настроек работы 2 уровневого кеша
     *
     * @param maxfirstLvl              - ерхний предел записей в кэше оперативной памяти.
     *                                 При превышении вызывается алгоритм рекэширования.
     * @param numberReqCallVoidRecache - количество запросов, необходимое для рекэширования.
     * @param numberReq                - количество запросов к кэшу после последнего рекэширования.
     */

    public SecondLvl(int maxfirstLvl, int numberReqCallVoidRecache, int numberReq) {
        maxfirstLvl = maxfirstLvl;
        numberReqCallVoidRecache = numberReqCallVoidRecache;
        numberReq = numberReq;
        firstLvl = new FirstLevelCache<>(maxfirstLvl);
        hardCache = new HardCache<>(maxfirstLvl);
    }


    @Override
    public void cache(K key, V value) throws IOException, ClassNotFoundException {
        firstLvl.cache(key, value);
    }

    @Override
    public V getObj(K key) throws IOException, ClassNotFoundException {
        checkContainKey(firstLvl, key);
        checkContainKey(hardCache, key);
        return null;
    }

    public V checkContainKey(FirstCache firstCache, K key) throws IOException, ClassNotFoundException {
        if (firstCache.checkKey(key)) {
            numberReq++;
            if (numberReq > numberReqCallVoidRecache) {
                this.recache();
                numberReq = 0;
            }
            if (firstCache instanceof HardCache) {
                return hardCache.getObj(key);
            } else {
                return firstLvl.getObj(key);
            }
        }
        return null;
    }


    @Override
    public void deleteObj(K key) {
        if (firstLvl.checkKey(key)) {
            firstLvl.deleteObj(key);
        }
        if (hardCache.checkKey(key)) {
            hardCache.deleteObj(key);
        }
    }


    @Override
    public void clearCache() {
        hardCache.clearCache();
        firstLvl.clearCache();
    }


    @Override
    public V removeObj(K key) throws IOException, ClassNotFoundException {
        if (firstLvl.checkKey(key)) {
            return firstLvl.removeObj(key);
        }
        if (hardCache.checkKey(key)) {
            return hardCache.removeObj(key);
        }
        return null;
    }


    @Override
    public boolean checkKey(K key) {
        if (firstLvl.checkKey(key)) {
            return true;
        }
        if (hardCache.checkKey(key)) {
            return true;
        }
        return false;
    }


    @Override
    public int size() {
        return firstLvl.size() + hardCache.size();
    }

    /**
     * Метод для определния пропускной способности между двумя уровнями
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Override
    public void recache() throws IOException, ClassNotFoundException {
        Set<K> embedOP = new TreeSet<K>(firstLvl.getUsedKey());
        int cointIteration = 0;

        for (K key : embedOP) {
            cointIteration += firstLvl.getCointCallingObjeck(key);
        }
        cointIteration /= embedOP.size();

        for (K key : embedOP) {
            if (firstLvl.getCointCallingObjeck(key) <= cointIteration) {
                hardCache.cache(key, firstLvl.removeObj(key));
            }
        }

        Set<K> embed = new TreeSet<K>(hardCache.getUsedKey());
        for (K key : embed) {
            try {
                if (hardCache.getCointCallingObjeck(key) > cointIteration) {
                    firstLvl.cache(key, hardCache.removeObj(key));
                }
            } catch (IOException ex) {
                hardCache.deleteObj(key);
                continue;
            } catch (ClassNotFoundException ex) {
                continue;
            }
        }
    }

    @Override
    public Set<K> getUsedKey() {
        Set<K> set = new TreeSet<K>(firstLvl.getUsedKey());
        set.addAll(hardCache.getUsedKey());
        return set;
    }

    @Override
    public int getCointCallingObjeck(K key) {
        if (firstLvl.checkKey(key)) {
            return firstLvl.getCointCallingObjeck(key);
        }
        if (hardCache.checkKey(key)) {
            return hardCache.getCointCallingObjeck(key);
        }
        return 0;
    }
}
