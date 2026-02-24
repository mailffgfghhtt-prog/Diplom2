package site.nomoreparties.stellarburgers;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import site.nomoreparties.stellarburgers.generators.UserGenerator;
import site.nomoreparties.stellarburgers.model.User;
import site.nomoreparties.stellarburgers.model.UserCredentials;
import site.nomoreparties.stellarburgers.steps.UserSteps;
import static org.junit.jupiter.api.Assertions.*;

public class UserLoginTests {

    private UserSteps userSteps;
    private User user;
    private String accessToken;

    @BeforeEach
    void setUp() {
        userSteps = new UserSteps();
        user = UserGenerator.randomUser();

        // Регистрация пользователя перед каждым тестом
        Response createResponse = userSteps.sendPostRequestAuthRegister(user);
        assertEquals(200, createResponse.statusCode());

        // Сохраняем токен для последующего удаления пользователя
        accessToken = createResponse.jsonPath().getString("accessToken");
    }

    @AfterEach
    void tearDown() {
        if (accessToken != null) {
            // Удаление пользователя после каждого теста
            Response deleteResponse = userSteps.sendDeleteRequestAuthUser(accessToken);
            // Можно добавить проверку статуса ответа, если требуется
            // assertEquals(202, deleteResponse.statusCode());
        }
    }

    @Test
    void loginWithValidCredentialsShouldReturnSuccess() {
        // Авторизация с корректными данными
        UserCredentials userCreds = UserCredentials.fromUser(user);
        Response loginResponse = userSteps.sendPostRequestAuthLogin(userCreds);

        assertEquals(200, loginResponse.statusCode());
        assertTrue(loginResponse.jsonPath().getBoolean("success"));
        assertNotNull(loginResponse.jsonPath().getString("accessToken"));
    }

    @Test
    void loginWithWrongEmailShouldFail() {
        // Пытаемся авторизоваться с неверным email
        Response loginResponse = userSteps.sendPostRequestAuthLoginWrongEmail(user);

        assertEquals(401, loginResponse.statusCode());
        assertFalse(loginResponse.jsonPath().getBoolean("success"));
        assertEquals("email or password are incorrect",
                loginResponse.jsonPath().getString("message"));
    }

    @Test
    void loginWithWrongPasswordShouldFail() {
        // Пытаемся авторизоваться с неверным паролем
        Response loginResponse = userSteps.sendPostRequestAuthLoginWrongPassword(user);

        assertEquals(401, loginResponse.statusCode());
        assertFalse(loginResponse.jsonPath().getBoolean("success"));
        assertEquals("email or password are incorrect",
                loginResponse.jsonPath().getString("message"));
    }

    @Test
    void loginWithoutEmailShouldFail() {
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
    void loginWithoutPasswordShouldFail() {
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
