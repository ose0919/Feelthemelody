// Loginmanager.java
import java.util.HashMap;

public class Loginmanager {
    private HashMap<String, User> users;

    public Loginmanager(HashMap<String, User> users) {
        this.users = users;
    }

    public boolean login(String id, String pw) {
        User user = users.get(id);
        return user != null && user.password.equals(pw);
    }
}