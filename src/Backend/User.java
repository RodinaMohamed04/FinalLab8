package Backend;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicInteger;

public class User {

    protected String userName;
    protected static final AtomicInteger nextID = new AtomicInteger(100);
    protected int userId;
    protected String email;
    protected String passwordHash;
    protected String role;

    public User(String userName, String email, String passwordHash, String role) {
        this.userName = userName;
        this.userId = nextID.getAndIncrement();
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;

    }

    public User(String userName, int userId, String email, String passwordHash, String role) {
        this.userName = userName;
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;

    }

    public static String hashy(String password) throws NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance("SHA-256");
        byte[] hashbytes = m.digest(password.getBytes());
        String s = "";
        for (byte hashbyte : hashbytes) {
            s += String.format("%02x", hashbyte);

        }
        return s;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPassword(String password) {
        try {
            this.passwordHash = hashy(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setCounter(int newValue) {
        nextID.set(newValue);
    }

    public String getRole() {
        return role;
    }

}
 