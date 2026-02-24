package site.nomoreparties.stellarburgers;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import site.nomoreparties.stellarburgers.model.CreateUserResponse;
import site.nomoreparties.stellarburgers.model.User;
import site.nomoreparties.stellarburgers.steps.UserSteps;

import static org.hamcrest.Matchers.*;
import static site.nomoreparties.stellarburgers.generators.UserGenerator.*;


public class CreateUserTests {
    private String token;
    private UserSteps userSteps = new UserSteps();

    @BeforeEach
    public void setUp() {
        // Подготовка общих данных перед каждым тестом (если требуется)
    }

    @Test
    @DisplayName("Создание пользователя: успешный регистр с корректными данными")
    public void createUserWithAllValidFields() {
        User user = randomUser();

        Response response = userSteps.sendPostRequestAuthRegister(user);

        response.then()
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("accessToken", notNullValue())
                .and()
                .assertThat().body("refreshToken", notNullValue())
                .and()
                .assertThat().body("user.email", equalTo(user.getEmail()))
                .and()
                .assertThat().body("user.name", equalTo(user.getName()));

        token = response.as(CreateUserResponse.class).getAccessToken();
    }

    @Test
    @DisplayName("Создание пользователя: нельзя зарегистрировать пользователя с уже существующим email")
    public void createUserWithDuplicateEmail() {
        // Создаем первого пользователя
        User user1 = randomUser();
        Response response1 = userSteps.sendPostRequestAuthRegister(user1);
        response1.then().statusCode(200);

        // Пытаемся создать второго пользователя с тем же email
        User user2 = new User()
                .setEmail(user1.getEmail())
                .setName(faker.name().fullName())
                .setPassword(faker.internet().password(9, 10));

        Response response2 = userSteps.sendPostRequestAuthRegister(user2);

        response2.then()
                .statusCode(403)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("User already exists"));

        token = response1.as(CreateUserResponse.class).getAccessToken();
    }

    @Test
    @DisplayName("Создание пользователя: нельзя создать без email")
    public void createUserWithoutEmail() {
        User user = randomUserWithoutEmail();

        Response response = userSteps.sendPostRequestAuthRegister(user);

        response.then()
                .statusCode(403)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя: нельзя создать без пароля")
    public void createUserWithoutPassword() {
        User user = randomUserWithoutPassword();

        Response response = userSteps.sendPostRequestAuthRegister(user);

        response.then()
                .statusCode(403)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя: нельзя создать без имени")
    public void createUserWithoutName() {
        User user = randomUserWithoutName();

        Response response = userSteps.sendPostRequestAuthRegister(user);

        response.then()
                .statusCode(403)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }


    @Test
    @DisplayName("Создание пользователя: можно создать с минимально допустимым паролем (6 символов)")
    public void createUserWithMinPasswordLength() {
        User user = new User()
                .setEmail(faker.internet().safeEmailAddress())
                .setName(faker.name().fullName())
                .setPassword("123456"); // 6 символов

        Response response = userSteps.sendPostRequestAuthRegister(user);

        response.then()
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));

        token = response.as(CreateUserResponse.class).getAccessToken();
    }

    @AfterEach
    public void tearDown() {
        if (token != null) {
            userSteps.sendDeleteRequestAuthUser(token);
            token = null;
        }
    }
}
