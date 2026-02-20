package site.nomoreparties.stellarburgers.clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.model.User;
import site.nomoreparties.stellarburgers.model.UserCredentials;
import static io.restassured.RestAssured.given;


public class ApiClientUser {
    private static final String API_CREATE_USER = "/api/auth/register";
    private static final String API_LOGIN_USER = "/api/auth/login";
    private static final String API_USER_INFO = "/api/auth/user";
    private static final String API_USER_PASSWORD_RESET_REQUEST = "/api/password-reset";


    public ApiClientUser() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru";
    }

    /**
     * Создание нового пользователя
     * @param user объект пользователя с заполненными полями (email, password, name)
     * @return Response объект ответа API
     */
    public Response createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(API_CREATE_USER);
    }

    /**
     * Удаление пользователя (требуется авторизация)
     * @param token токен авторизации пользователя (в формате "Bearer <token>")
     * @return Response объект ответа API
     */
    public Response deleteUser(String token) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", token)
                .when()
                .delete(API_USER_INFO);
    }

    /**
     * Авторизация пользователя (получение токенов)
     * @param userCreds объект с credentials (email и password)
     * @return Response объект ответа API
     */
    public Response loginUser(UserCredentials userCreds) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(userCreds)
                .when()
                .post(API_LOGIN_USER);
    }

    /**
     * Изменение данных авторизованного пользователя
     * @param token токен авторизации пользователя
     * @param user обновлённый объект пользователя (можно менять email и/или name)
     * @return Response объект ответа API
     */
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

    // В классе ApiClientOrder
    public Response getIngredientList() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/ingredients");
    }




    /**
     * Запрос на сброс пароля (не требует авторизации)
     * @param token токен авторизации (для этого метода может быть пустым или недействительным)
     * @param userCreds объект с email для сброса пароля
     * @return Response объект ответа API
     */
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
