package steps;

import api.ApiClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.User;
import static org.apache.http.HttpStatus.*;

import static org.hamcrest.Matchers.equalTo;

public class UserTestSteps {
    private final ApiClient apiClient = new ApiClient();

    @Step("Создать нового пользователя")
    public Response createUser(User user) {
        return apiClient.createUser(user);
    }

    @Step("Удалить пользователя")
    public Response deleteUser(String token) {
        return apiClient.deleteUser(token);
    }

    @Step("Проверить успешное создание пользователя")
    public void verifyUserCreatedSuccessfully(Response response) {
        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Step("Проверить ошибку при создании дубликата пользователя")
    public void verifyDuplicateUserError(Response response) {
        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("User already exists"));
    }

    @Step("Проверить ошибку при отсутствии обязательного поля")
    public void verifyMissingFieldError(Response response) {
        response.then()
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }
}