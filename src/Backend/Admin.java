package Backend;

public class Admin extends User {
    public Admin(String userName, int userId, String email, String passwordHash) {
        super(userName, userId, email, passwordHash, "admin");

    }

    public Admin(String userName, String email, String passwordHash) {
        super(userName, email, passwordHash, "admin");

    }
}