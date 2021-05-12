package ru.job4j.gc.ref;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CachedTxtFiles {
    private String path;
    private final Cache<String, String> cache = new Cache<>();

    public CachedTxtFiles(String path) {
        this.path = path;
    }

    private final Function<String, String> load = s -> {
        List<String> text = new ArrayList<>();
        try (BufferedReader read = new BufferedReader(new FileReader(String.format("%s%s", path, s)))) {
            read.lines().forEach(text::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.stream().collect(Collectors.joining(System.lineSeparator()));
    };

    public String get(String key) {
        return cache.get(key, load);
    }

    public static void main(String[] args) {
        CachedTxtFiles cacheTxt = new CachedTxtFiles("./txt/");
        System.out.println(cacheTxt.get("Address.txt"));
        System.out.println(cacheTxt.get("Names.txt"));
    }
}
