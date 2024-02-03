package main.propertease;

public class User {
    public User(String name, int privileges) {
        this.name = name;
        this.privileges = privileges;
    }

    public String getName() {
        return name;
    }

    public int getPrivileges() {
        return privileges;
    }

    String name;
    int privileges;
}