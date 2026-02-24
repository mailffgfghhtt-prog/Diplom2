package site.nomoreparties.stellarburgers.clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.model.User;
import site.nomoreparties.stellarburgers.model.UserCredentials;
import static io.restassured.RestAssured.given;
import io.qameta.allure.Step;

public class ApiClientUser {
    private static final String API_CREATE_USER = "/api/auth/register";
    private static final String API_LOGIN_USER = "/api/auth/login";
    private static final String API_USER_INFO = "/api/auth/user";
    private static final String API_USER_PASSWORD_RESET_REQUEST = "/api/password-reset";

    public ApiClientUser() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru";
    }

    @Step("Создание нового пользователя: {user.email}")
    public Response createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(API_CREATE_USER);
    }

    @Step("Удаление пользователя с токеном: {token}")
    public Response deleteUser(String token) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", token)
                .when()
                .delete(API_USER_INFO);
    }

    @Step("Авторизация пользователя: {userCreds.email}")
    public Response loginUser(UserCredentials userCreds) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(userCreds)
                .when()
                .post(API_LOGIN_USER);
    }

    @Step("Изменение данных авторизованного пользователя (токен: {token})")
    public Response changeAuthorizedUserParam(String token, User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", token)
                .and()
                .body(user)
                .when()
                .patch(API_USER_INFO);
    }

    @Step("Получение списка ингредиентов")
    public Response getIngredientList() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/ingredients");
    }

    @Step("Запрос на сброс пароля для email: {userCreds.email}")
    public Response resetAuthorizedUserPassword(String token, UserCredentials userCreds) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", token)
                .and()
                .body(userCreds)
                .when()
                .post(API_USER_PASSWORD_RESET_REQUEST);
    }
}
