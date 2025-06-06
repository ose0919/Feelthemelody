// User.java
public class User {
    String id, password, name, birth, gender;

    public User(String id, String password, String name, String birth, String gender) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.birth = birth;
        this.gender = gender;
    }

    // 새롭게 추가된 메서드
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // 비밀번호 필드가 public이지만, Loginmanager에서 직접 접근하므로 getter는 추가하지 않습니다.
    // 실제 애플리케이션에서는 password 필드를 private으로 하고, 인증 메서드를 사용하는 것이 보안상 더 좋습니다.
}
