package site.nomoreparties.stellarburgers;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import site.nomoreparties.stellarburgers.generators.userGenerator;
import site.nomoreparties.stellarburgers.model.User;
import site.nomoreparties.stellarburgers.model.UserCredentials;
import site.nomoreparties.stellarburgers.steps.UserSteps;
import static org.junit.jupiter.api.Assertions.*;


public class UserLoginTests {

    private UserSteps userSteps;
    private User user;

    @BeforeEach
    public void setUp() {
        userSteps = new UserSteps();
        user = userGenerator.randomUser(); // Предполагаем, что генератор доступен
    }

    @Test
    public void loginWithValidCredentialsShouldReturnSuccess() {
        // 1. Регистрация пользователя
        Response createResponse = userSteps.sendPostRequestAuthRegister(user);
        assertEquals(200, createResponse.statusCode());

        // Получаем токен из ответа (предполагаем структуру ответа)
        String accessToken = createResponse.jsonPath().getString("accessToken");


        // 2. Авторизация с корректными данными
        UserCredentials userCreds = UserCredentials.fromUser(user);
        Response loginResponse = userSteps.sendPostRequestAuthLogin(userCreds);


        assertEquals(200, loginResponse.statusCode());
        assertTrue(loginResponse.jsonPath().getBoolean("success"));
        assertNotNull(loginResponse.jsonPath().getString("accessToken"));
    }

    @Test
    public void loginWithWrongEmailShouldFail() {
        // Регистрируем пользователя
        Response createResponse = userSteps.sendPostRequestAuthRegister(user);
        assertEquals(200, createResponse.statusCode());

        // Пытаемся авторизоваться с неверным email
        Response loginResponse = userSteps.sendPostRequestAuthLoginWrongEmail(user);


        assertEquals(401, loginResponse.statusCode()); // Или другой ожидаемый статус
        assertFalse(loginResponse.jsonPath().getBoolean("success"));
        assertEquals("email or password are incorrect",
                loginResponse.jsonPath().getString("message"));
    }

    @Test
    public void loginWithWrongPasswordShouldFail() {
        // Регистрируем пользователя
        Response createResponse = userSteps.sendPostRequestAuthRegister(user);
        assertEquals(200, createResponse.statusCode());

        // Пытаемся авторизоваться с неверным паролем
        Response loginResponse = userSteps.sendPostRequestAuthLoginWrongPassword(user);


        assertEquals(401, loginResponse.statusCode());
        assertFalse(loginResponse.jsonPath().getBoolean("success"));
        assertEquals("email or password are incorrect",
                loginResponse.jsonPath().getString("message"));
    }

    @Test
    public void loginWithoutEmailShouldFail() {
        User invalidUser = new User()
                .setPassword(user.getPassword())
                .setName(user.getName());


        UserCredentials invalidCreds = new UserCredentials("", invalidUser.getPassword());
        Response loginResponse = userSteps.sendPostRequestAuthLogin(invalidCreds);

        assertEquals(401, loginResponse.statusCode());
        assertFalse(loginResponse.jsonPath().getBoolean("success"));
        assertEquals("email or password are incorrect",
                loginResponse.jsonPath().getString("message"));
    }

    @Test
    public void loginWithoutPasswordShouldFail() {
        User invalidUser = new User()
                .setEmail(user.getEmail())
                .setName(user.getName());

        UserCredentials invalidCreds = new UserCredentials(invalidUser.getEmail(), "");
        Response loginResponse = userSteps.sendPostRequestAuthLogin(invalidCreds);

        assertEquals(401, loginResponse.statusCode());
        assertFalse(loginResponse.jsonPath().getBoolean("success"));
        assertEquals("email or password are incorrect",
                loginResponse.jsonPath().getString("message"));
    }
}
