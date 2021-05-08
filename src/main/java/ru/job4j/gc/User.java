package ru.job4j.gc;

public class User {
    private String name;
    private String role;

    public User() {

    }

    public User(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.printf("Removed %s %s%n", name, role);
    }
}
