import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Order;
import pojo.User;
import steps.OrderSteps;

import java.util.List;

public class OrderTestSteps {
    private final UserApi userApi = new UserApi();
    private final OrderApi orderApi = new OrderApi();
    private final OrderSteps orderChecks = new OrderSteps();
    private String accessToken;


    private List<String> getValidIngredients() {
        return orderApi.getAvailableIngredients().extract().path("data._id");
    }


    @Before
    public void setUp() {
        Faker faker = new Faker();
        //mail, password, name
        User user = new User(faker.internet().emailAddress(), faker.internet().password(6, 6), faker.name().firstName());
        Response response = userApi.createUser(user);
        accessToken = userApi.getAccessToken(response);
    }


    //Создание заказа:
    //без ингредиентов;
    //с неверным хешем ингредиентов.

    //1 Создание заказа: //с авторизацией;
    @Test
    @DisplayName("Создание заказа с авторизацией и валидными ингредиентами")
    @Description("Проверка на успешное создание заказа с авторизацией и валидными ингредиентами")
    public void orderCanBeCreatedWithAuthAndValidIngredientsTest() {
        List<String> ingredients = getValidIngredients();
        Order order = Order.validOrder(ingredients);
        ValidatableResponse response = orderApi.createOrderWithAuth(order, accessToken);
        orderChecks.checkOrderCreatedSuccessfully(response);
    }


    //2 Создание заказа: //без авторизации;
    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка на создание заказа без авторизации")
    public void orderCanBeCreatedWithoutAuthTest() {
        List<String> ingredients = getValidIngredients();
        Order order = Order.validOrder(ingredients);
        ValidatableResponse response = orderApi.createOrderWithoutAuth(order);
        orderChecks.checkOrderCreatedSuccessfully(response);
    }


    //Создание заказа: //без ингредиентов;
    @Test
    @DisplayName("Создание заказа с авторизацией без ингредиентов")
    @Description("Проверка на невозможность создания заказа без ингредиентов")
    public void orderCannotBeCreatedWithoutIngredientsTest() {
        Order order = Order.emptyOrder();
        ValidatableResponse response = orderApi.createOrderWithAuth(order, accessToken);
        orderChecks.checkOrderCreationWithoutIngredientsFails(response);
    }


    //Создание заказа: //    с неверным хешем ингредиентов.
    @Test
    @DisplayName("Создание заказа с невалидными ингредиентами")
    @Description("Проверка на невозможность создания заказа с невалидными ингредиентами")
    public void orderCannotBeCreatedWithInvalidIngredientsTest() {
        Order order = Order.invalidOrder();
        ValidatableResponse response = orderApi.createOrderWithAuth(order, accessToken);
        orderChecks.checkOrderCreationWithInvalidIngredientsFails(response);
    }


    //Получение списка заказов: //с авторизацией;
    @Test
    @DisplayName("Получение списка заказов с автризацией")
    @Description("Проверка на успешное получение списка заказов с автризацией")
    public void orderCanBeListedWithAuthTest() {
        List<String> ingredients = getValidIngredients();
        Order order = Order.validOrder(ingredients);
        // Создаем заказ
        orderApi.createOrderWithAuth(order, accessToken);
        // Смотрим список заказов пользователя
        ValidatableResponse response = orderApi.getOrders(accessToken);
        orderChecks.checkGetOrdersWithAuth(response);
    }


    //Получение списка заказов: //без авторизации;
    @Test
    @DisplayName("Получение списка заказов без автризации")
    @Description("Проверка на успешное получение списка заказов без автризации")
    public void orderCanNotBeListedWithAuthTest() {
        List<String> ingredients = getValidIngredients();
        Order order = Order.validOrder(ingredients);
        // Создаем заказ
        orderApi.createOrderWithAuth(order, accessToken);
        // Смотрим список заказов без автризации пользователя
        ValidatableResponse response = orderApi.getOrders(null);
        orderChecks.checkGetOrdersWithoutAuth(response);
    }


    //Удаляем созданного пользователя
    @After
    public void cleanUp() {
        if (accessToken != null) {
            userApi.deleteUser(accessToken);
        }
    }
}
