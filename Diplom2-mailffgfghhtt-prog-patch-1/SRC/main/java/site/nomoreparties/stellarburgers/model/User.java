package site.nomoreparties.stellarburgers.model;
import lombok.*;
@Getter
public class User {
    private String email;
    private String password;
    private String name;
    public User setEmail(String email) {
        this.email = email;
        return this;
    }
    public User setPassword(String password) {
        this.password = password;
        return this;
    }
    public User setName(String name) {
        this.name = name;
        return this;
    }
}