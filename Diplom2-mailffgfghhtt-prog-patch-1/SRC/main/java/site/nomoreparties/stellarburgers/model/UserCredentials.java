package site.nomoreparties.stellarburgers.model;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserCredentials {
    private String email;
    private String password;


    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Статический метод для конвертации User → UserCredentials
    public static UserCredentials fromUser(User user) {
        return new UserCredentials(user.getEmail(), user.getPassword());
    }
}
