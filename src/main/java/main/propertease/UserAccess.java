package main.propertease;

/**
 * Class to store and access the current logged-in user
 * @see User
 */
public class UserAccess {
    /**
     * Set the current logged-in user
     * @param user The user to set as the current logged-in user
     */
    public static void setUser(User user) {
        UserAccess.user = user;
    }

    /**
     * Get the current logged-in user
     * @return the current logged-in user
     */
    public static User getUser() {
        return user;
    }

    private static User user;
}
