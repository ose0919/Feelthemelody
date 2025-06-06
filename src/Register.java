// Register.java
import java.util.HashMap;

public class Register {
    private HashMap<String, User> users;
    public Register(HashMap<String, User> users) {
        this.users = users;
    }
    public String registerUser(String id, String pw, String name, String birth, String gender) {
        if (id.isEmpty() || pw.isEmpty() || name.isEmpty() || birth.isEmpty()) return "모든 항목을 입력해주세요.";
        if (pw.length() < 6) return "비밀번호는 최소 6자 이상이어야 합니다.";
        if (users.containsKey(id)) return "이미 존재하는 아이디입니다.";
        users.put(id, new User(id, pw, name, birth, gender));
        return "회원가입 성공";
    }
}