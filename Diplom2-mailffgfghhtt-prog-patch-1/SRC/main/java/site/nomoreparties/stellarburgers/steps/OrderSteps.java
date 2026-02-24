package site.nomoreparties.stellarburgers.steps;

import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.clients.ApiClientOrder;
import site.nomoreparties.stellarburgers.model.Order;

public class OrderSteps {
    private final ApiClientOrder apiClientOrder = new ApiClientOrder();


    /**
     * Отправка POST-запроса на создание нового заказа (требуется авторизация)
     * @param token токен авторизации пользователя (в формате "Bearer <token>")
     * @param order объект заказа с перечнем ingredientIds
     * @return Response объект ответа API
     */
    public Response sendPostRequestCreateOrder(String token, Order order) {
        return apiClientOrder.createOrder(token, order);
    }

    /**
     * Отправка GET-запроса на получение списка заказов текущего пользователя (требуется авторизация)
     * @param token токен авторизации пользователя
     * @return Response объект ответа API (содержит список заказов)
     */
    public Response sendGetRequestUserOrders(String token) {
        return apiClientOrder.getUserOrders(token);
    }

    /**
     * Отправка GET-запроса на получение общего списка заказов (публичные данные, без авторизации)
     * @return Response объект ответа API (содержит общий список последних заказов)
     */
    public Response sendGetRequestPublicOrders() {
        return apiClientOrder.getPublicOrders();
    }

    /**
     * Отправка GET-запроса на получение деталей конкретного заказа по номеру (без авторизации)
     * @param orderNumber номер заказа (целое число)
     * @return Response объект ответа API с деталями заказа
     */
    public Response sendGetRequestOrderByNumber(int orderNumber) {
        return apiClientOrder.getOrderByNumber(orderNumber);
    }

    /**
     * Отправка DELETE-запроса на отмену заказа (требуется авторизация и принадлежность заказа пользователю)
     * @param token токен авторизации пользователя
     * @param orderId идентификатор заказа, который нужно отменить
     * @return Response объект ответа API
     */
    public Response sendDeleteRequestCancelOrder(String token, String orderId) {
        return apiClientOrder.cancelOrder(token, orderId);
    }
    // В классе OrderSteps
    public Response sendPostRequestOrdersAuthorizedUser(String token, Order order) {
        return apiClientOrder.createOrder(token, order);
    }

    public Response sendPostRequestOrdersUnauthorizedUser(Order order) {
        // Для неавторизованных запросов токен не передаётся
        return apiClientOrder.createOrder("", order);
    }

    public void sendPostRequestOrdersAuthorizedUserSpecifiedNumberOfTimes(int count, String token, Order order) {
        for (int i = 0; i < count; i++) {
            sendPostRequestCreateOrder(token, order);
        }
    }

    public Response sendGetRequestOrdersAuthorizedUser(String token) {
        return apiClientOrder.getUserOrders(token);
    }

    public Response sendGetRequestOrdersUnauthorizedUser() {
        return apiClientOrder.getPublicOrders();
    }

}
