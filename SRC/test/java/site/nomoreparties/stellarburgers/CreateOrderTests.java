package site.nomoreparties.stellarburgers;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import site.nomoreparties.stellarburgers.model.CreateUserResponse;
import site.nomoreparties.stellarburgers.model.Order;
import site.nomoreparties.stellarburgers.model.User;
import site.nomoreparties.stellarburgers.steps.OrderSteps;
import site.nomoreparties.stellarburgers.steps.UserSteps;


import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static site.nomoreparties.stellarburgers.generators.IngredientGenerator.randomIngredientIdByType;
import static site.nomoreparties.stellarburgers.generators.userGenerator.faker;
import static site.nomoreparties.stellarburgers.generators.userGenerator.randomUser;


public class CreateOrderTests {
    private String token;
    private OrderSteps orderSteps = new OrderSteps();
    private UserSteps userSteps = new UserSteps();
    private User user;

    @BeforeEach
    public void setUp() {
        user = randomUser();
        Response responseRegister = userSteps.sendPostRequestAuthRegister(user);
        responseRegister.then()
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true));
        token = responseRegister.as(CreateUserResponse.class).getAccessToken();
    }

    @ParameterizedTest
    @MethodSource("validIngredientListData")
    @DisplayName("Создание заказа. Можно создать заказ из существующих ингредиентов авторизованным пользователем")
    public void createOrderWithValidIngredientsByAuthorizedUserTest(List<String> ingredientIds) {
        Order order = new Order();
        order.setIngredients(ingredientIds);


        Response responseCreateOrder = orderSteps.sendPostRequestOrdersAuthorizedUser(token, order);


        responseCreateOrder.then()
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("name", notNullValue())
                .and()
                .assertThat().body("order.number", notNullValue())
                .and()
                .assertThat().body("order.owner", notNullValue());
    }

    static Stream<Arguments> validIngredientListData() {
        return Stream.of(
                Arguments.of(List.of(randomIngredientIdByType("bun"), randomIngredientIdByType("main"), randomIngredientIdByType("sauce"))),
                Arguments.of(List.of(randomIngredientIdByType("bun"), randomIngredientIdByType("bun"))),
                Arguments.of(List.of(randomIngredientIdByType("sauce")))
        );
    }

    @Test
    @DisplayName("Создание заказа. Можно создать заказ из существующих ингредиентов неавторизованным пользователем")
    public void createOrderWithValidIngredientsByUnauthorizedUserTest() {
        Order order = new Order();
        order.setIngredients(List.of(randomIngredientIdByType("bun")));


        Response responseCreateOrder = orderSteps.sendPostRequestOrdersUnauthorizedUser(order);


        responseCreateOrder.then()
                .statusCode(200)
                .and()
                .assertThat().body("success", equalTo(true))
                .and()
                .assertThat().body("name", notNullValue())
                .and()
                .assertThat().body("order.number", notNullValue())
                .and()
                .assertThat().body("order.owner", nullValue());
    }

    @Test
    @DisplayName("Создание заказа. Нельзя создать заказ без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        Order order = new Order();

        Response responseCreateOrder = orderSteps.sendPostRequestOrdersUnauthorizedUser(order);

        responseCreateOrder.then()
                .statusCode(400)
                .and()
                .assertThat().body("success", equalTo(false))
                .and()
                .assertThat().body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа. Нельзя создать заказ с несуществующим ингредиентом")
    public void createOrderWithIncorrectIngredientTest() {
        Order order = new Order();
        order.setIngredients(List.of(
                faker.regexify("[a-zA-Z0-9]{10}"),
                randomIngredientIdByType("main")
        ));

        Response responseCreateOrder = orderSteps.sendPostRequestOrdersUnauthorizedUser(order);
        responseCreateOrder.then().statusCode(500);
    }

    @AfterEach
    public void tearDown() {
        if (token != null) {
            userSteps.sendDeleteRequestAuthUser(token);
        }
    }
}
