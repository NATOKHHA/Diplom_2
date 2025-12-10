package api;

import io.restassured.response.Response;
import models.Order;
import models.User;

import static io.restassured.RestAssured.given;

public class ApiClient {

    public Response createUser(User user) {
        return given()
                .spec(ApiConfig.getDefaultRequestSpec())
                .body(user)
                .post(Endpoints.AUTH_REGISTER);
    }

    public Response loginUser(User user) {
        return given()
                .spec(ApiConfig.getDefaultRequestSpec())
                .body(user)
                .post(Endpoints.AUTH_LOGIN);
    }

    public Response deleteUser(String token) {
        return given()
                .spec(ApiConfig.getRequestSpecWithAuth(token))
                .delete(Endpoints.AUTH_USER);
    }

    public Response createOrder(Order order, String token) {
        if (token != null) {
            return given()
                    .spec(ApiConfig.getRequestSpecWithAuth(token))
                    .body(order)
                    .post(Endpoints.ORDERS);
        } else {
            return given()
                    .spec(ApiConfig.getDefaultRequestSpec())
                    .body(order)
                    .post(Endpoints.ORDERS);
        }
    }
}