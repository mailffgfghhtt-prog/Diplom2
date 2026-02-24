package site.nomoreparties.stellarburgers.steps;

import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.clients.ApiClientUser;
import site.nomoreparties.stellarburgers.model.User;
import site.nomoreparties.stellarburgers.model.UserCredentials;

public class UserSteps {
    private final ApiClientUser apiClientUser = new ApiClientUser();

    /**
     * Отправка POST-запроса на регистрацию нового пользователя
     * @param user объект пользователя с заполненными полями (email, password, name)
     * @return Response объект ответа API
     */
    public Response sendPostRequestAuthRegister(User user) {
        return apiClientUser.createUser(user);
    }

    /**
     * Отправка POST-запроса на авторизацию пользователя
     * @param userCreds объект с credentials (email и password)
     * @return Response объект ответа API
     */
    public Response sendPostRequestAuthLogin(UserCredentials userCreds) {
        return apiClientUser.loginUser(userCreds);
    }

    /**
     * Отправка DELETE-запроса на удаление пользователя (требуется авторизация)
     * @param token токен авторизации пользователя (в формате "Bearer <token>")
     * @return Response объект ответа API
     */
    public Response sendDeleteRequestAuthUser(String token) {
        return apiClientUser.deleteUser(token);
    }

    /**
     * Отправка PATCH-запроса на изменение данных авторизованного пользователя
     * @param token токен авторизации пользователя
     * @param user обновлённый объект пользователя (можно менять email и/или name)
     * @return Response объект ответа API
     */
    public Response sendPatchRequestAuthUserChangeEmailAuthorized(String token, String newEmail) {
        User user = new User();
        user.setEmail(newEmail);
        return apiClientUser.changeAuthorizedUserParam(token, user);
    }

    public Response sendPatchRequestAuthUserChangeNameAuthorized(String token, String newName) {
        User user = new User();
        user.setName(newName);
        return apiClientUser.changeAuthorizedUserParam(token, user);
    }

    // В классе UserSteps
    public Response sendPostRequestAuthLoginWrongEmail(User user) {
        UserCredentials wrongCreds = new UserCredentials("wrong_" + user.getEmail(), user.getPassword());
        return sendPostRequestAuthLogin(wrongCreds);
    }


    public Response sendPostRequestAuthLoginWrongPassword(User user) {
        UserCredentials wrongCreds = new UserCredentials(user.getEmail(), "wrong_" + user.getPassword());
        return sendPostRequestAuthLogin(wrongCreds);
    }


    /**
     * Отправка PATCH-запроса на изменение email неавторизованного пользователя (ожидаемый провал)
     * @param newEmail новый email
     * @return Response объект ответа API
     */
    public Response sendPatchRequestAuthUserChangeEmailUnauthorized(String newEmail) {
        User user = new User();
        user.setEmail(newEmail);
        // Передаем пустой токен — имитируем неавторизованный запрос
        return apiClientUser.changeAuthorizedUserParam("", user);
    }

    /**
     * Отправка PATCH-запроса на изменение имени неавторизованного пользователя (ожидаемый провал)
     * @param newName новое имя
     * @return Response объект ответа API
     */
    public Response sendPatchRequestAuthUserChangeNameUnauthorized(String newName) {
        User user = new User();
        user.setName(newName);
        // Передаем пустой токен — имитируем неавторизованный запрос
        return apiClientUser.changeAuthorizedUserParam("", user);
    }

    /**
     * Отправка PATCH-запроса с некорректным токеном
     * @param invalidToken некорректный токен (например, expired)
     * @param newEmail новый email для попытки обновления
     * @return Response объект ответа API
     */
    public Response sendPatchRequestAuthUserWithInvalidToken(String invalidToken, String newEmail) {
        User user = new User();
        user.setEmail(newEmail);
        return apiClientUser.changeAuthorizedUserParam(invalidToken, user);
    }

    /**
     * Отправка POST-запроса на сброс пароля (не требует авторизации)
     * @param token токен (может быть пустым или недействительным)
     * @param userCreds объект с email для сброса пароля
     * @return Response объект ответа API
     */
    public Response sendPostRequestPasswordReset(String token, UserCredentials userCreds) {
        return apiClientUser.resetAuthorizedUserPassword(token, userCreds);
    }
}
