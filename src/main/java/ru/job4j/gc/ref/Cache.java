package ru.job4j.gc.ref;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Cache<T, V> {
    private final Map<T, SoftReference<V>> dataMap = new HashMap<>();

    public V get(T key, Function<T, V> function) {
        V value = null;
        if (!dataMap.containsKey(key)) {
            value = function.apply(key);
            dataMap.put(key, new SoftReference<>(value));
        } else {
            value = dataMap.get(key).get();
            if (value == null) {
                value = function.apply(key);
                dataMap.put(key, new SoftReference<>(value));
            }
        }
        return value;
    }
}
