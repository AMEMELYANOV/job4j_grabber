package ru.job4j.gc;

import static com.carrotsearch.sizeof.RamUsageEstimator.sizeOf;

public class GCUser {
    private static final long KB = 1000;
    private static final long MB = KB * KB;
    private static final Runtime ENVIRONMENT = Runtime.getRuntime();

    public static void info() {
        final long freeMemory = ENVIRONMENT.freeMemory();
        final long totalMemory = ENVIRONMENT.totalMemory();
        final long maxMemory = ENVIRONMENT.maxMemory();
        System.out.println("=== Environment state ===");
        System.out.printf("Free: %d%n", freeMemory / MB);
        System.out.printf("Total: %d%n", totalMemory / MB);
        System.out.printf("Max: %d%n", maxMemory / MB);
    }
    public static void main(String[] args) {
        System.out.println(sizeOf(new Empty()));
        System.out.println(sizeOf(new User()));


        User user1 = new User("Name of user1", "Role of user1");
        User user2 = new User("Name of user2", "Role of user2");
        info();
        for (int i = 0; i < 10000; i++) {
            new User("Name of user" + i, "Role of user" + i);
        }
//        System.gc();
        info();
    }
}
