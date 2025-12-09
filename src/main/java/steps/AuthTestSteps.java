package steps;

import api.ApiClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.User;
import static org.apache.http.HttpStatus.*;

import static org.hamcrest.Matchers.equalTo;

public class AuthTestSteps {
    private final ApiClient apiClient = new ApiClient();

    @Step("Авторизовать пользователя")
    public Response loginUser(User user) {
        return apiClient.loginUser(user);
    }

    @Step("Проверить успешную авторизацию")
    public void verifySuccessfulLogin(Response response) {
        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Step("Проверить ошибку авторизации с неверными данными")
    public void verifyLoginWithInvalidCredentials(Response response) {
        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
    }
}