package ru.job4j.gc.ref;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Cache<T, V> {
    private final Map<T, SoftReference<V>> dataMap = new HashMap<>();

    public V get(T key, Function<T, V> function) {
        dataMap.putIfAbsent(key, new SoftReference<>(function.apply(key)));
        return dataMap.get(key).get();
    }
}
