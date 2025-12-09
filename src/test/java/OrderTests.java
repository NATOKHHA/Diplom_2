import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Order;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.OrderTestSteps;
import steps.UserTestSteps;
import utils.RandomDataGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OrderTests {
    private OrderTestSteps orderSteps = new OrderTestSteps();
    private UserTestSteps userSteps = new UserTestSteps();
    private String token;
    private User user; // Добавляем поле для хранения пользователя

    private final List<String> validIngredients = Arrays.asList(
        "61c0c5a71d1f82001bdaaa6d",
        "61c0c5a71d1f82001bdaaa6f"
    );
    private final List<String> invalidIngredients = Collections.singletonList("invalidIngredient");

    @Before
    public void setUp() {
        // Создаем пользователя и получаем токен перед каждым тестом
        user = RandomDataGenerator.createRandomUser();
        Response createResponse = userSteps.createUser(user);
        token = createResponse.path("accessToken");
    }

    @After
    public void tearDown() {
        if (token != null) {
            userSteps.deleteUser(token);
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Проверка создания заказа авторизованным пользователем")
    public void testCreateOrderWithAuth() {
        Order order = new Order(validIngredients);
        Response orderResponse = orderSteps.createOrder(order, token);
        orderSteps.verifyOrderCreatedSuccessfully(orderResponse);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка создания заказа без авторизации")
    public void testCreateOrderWithoutAuth() {
        Order order = new Order(validIngredients);
        Response orderResponse = orderSteps.createOrder(order, null);
        orderSteps.verifyOrderCreatedSuccessfully(orderResponse);
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Проверка создания заказа с валидными ингредиентами")
    public void testCreateOrderWithIngredients() {
        Order order = new Order(validIngredients);
        Response orderResponse = orderSteps.createOrder(order, null);
        orderSteps.verifyOrderCreatedSuccessfully(orderResponse);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка ошибки при создании заказа без ингредиентов")
    public void testCreateOrderWithoutIngredients() {
        Order order = new Order(Collections.emptyList());
        Response orderResponse = orderSteps.createOrder(order, null);
        orderSteps.verifyEmptyIngredientsError(orderResponse);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверка ошибки при создании заказа с невалидными ингредиентами")
    public void testCreateOrderWithInvalidIngredients() {
        Order order = new Order(invalidIngredients);
        Response orderResponse = orderSteps.createOrder(order, null);
        orderSteps.verifyInvalidIngredientsError(orderResponse);
    }
}