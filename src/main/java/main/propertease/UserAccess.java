package main.propertease;

public class UserAccess {
    public static void setUser(User user) {
        UserAccess.user = user;
    }

    public static User getUser() {
        return user;
    }

    private static User user;
}
