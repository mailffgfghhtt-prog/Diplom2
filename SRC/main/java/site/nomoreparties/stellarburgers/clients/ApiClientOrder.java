package site.nomoreparties.stellarburgers.clients;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.model.Order;


import static io.restassured.RestAssured.given;

public class ApiClientOrder {
    private static final String API_ORDERS = "/api/orders";
    private static final String API_ORDERS_HISTORY = "/api/orders/user";


    public ApiClientOrder() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru";
    }

    /**
     * Создание нового заказа (требуется авторизация)
     * @param token токен авторизации пользователя (в формате "Bearer <token>")
     * @param order объект заказа с перечнем ingredientIds
     * @return Response объект ответа API
     */
    public Response createOrder(String token, Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", token)
                .and()
                .body(order)
                .when()
                .post(API_ORDERS);
    }

    /**
     * Получение списка заказов текущего пользователя (требуется авторизация)
     * @param token токен авторизации пользователя
     * @return Response объект ответа API (содержит список заказов)
     */
    public Response getUserOrders(String token) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", token)
                .when()
                .get(API_ORDERS_HISTORY);
    }


    /**
     * Получение общего списка заказов (не требует авторизации, публичные данные)
     * @return Response объект ответа API (содержит общий список последних заказов)
     */
    public Response getPublicOrders() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(API_ORDERS);
    }

    /**
     * Получение деталей конкретного заказа по номеру (не требует авторизации)
     * @param orderNumber номер заказа (целое число)
     * @return Response объект ответа API с деталями заказа
     */
    public Response getOrderByNumber(int orderNumber) {
        return given()
                .header("Content-type", "application/json")
                .queryParam("number", orderNumber)
                .when()
                .get(API_ORDERS);
    }

    /**
     * Отмена заказа (требуется авторизация и принадлежность заказа пользователю)
     * @param token токен авторизации пользователя
     * @param orderId идентификатор заказа, который нужно отменить
     * @return Response объект ответа API
     */
    public Response cancelOrder(String token, String orderId) {
        Order order = new Order();
        order.setOrderId(orderId);

        return given()
                .header("Content-type", "application/json")
                .and()
                .header("authorization", token)
                .and()
                .body(order)
                .when()
                .delete(API_ORDERS);
    }
}
