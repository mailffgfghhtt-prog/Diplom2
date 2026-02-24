package site.nomoreparties.stellarburgers.model;
import lombok.*;
@Getter
public class CreateUserResponse {
    private String accessToken;
    private String refreshToken;
    private String success;
    private User user;
}