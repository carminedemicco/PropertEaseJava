package main.propertease;

public class User {
    public User(String firstName, String lastName, String username, int privileges) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.privileges = privileges;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public int getPrivileges() {
        return privileges;
    }

    private final String firstName;
    private final String lastName;
    private final String username;
    private final int privileges;
}