import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.AuthTestSteps;
import steps.UserTestSteps;
import utils.RandomDataGenerator;

public class AuthTests {
    private AuthTestSteps authSteps;
    private UserTestSteps userSteps;
    private User user;
    private String token;

    @Before
    public void setUp() {
        authSteps = new AuthTestSteps();
        userSteps = new UserTestSteps();
        user = RandomDataGenerator.createRandomUser();
        Response response = userSteps.createUser(user);
        token = response.path("accessToken");
    }

    @After
    public void tearDown() {
        if (token != null) {
            userSteps.deleteUser(token);
        }
    }

    @Test
    @DisplayName("Вход под существующим пользователем")
    @Description("Проверка успешной авторизации с валидными данными")
    public void testLoginWithValidCredentials() {
        Response response = authSteps.loginUser(user);
        authSteps.verifySuccessfulLogin(response);
    }

    @Test
    @DisplayName("Вход с неверным логином")
    @Description("Проверка ошибки при авторизации с неверным логином")
    public void testLoginWithInvalidEmail() {
        User invalidUser = new User("invalid@gmail.com", user.getPassword(), null);
        Response response = authSteps.loginUser(invalidUser);
        authSteps.verifyLoginWithInvalidCredentials(response);
    }

    @Test
    @DisplayName("Вход с неверным паролем")
    @Description("Проверка ошибки при авторизации с неверным паролем")
    public void testLoginWithInvalidPassword() {
        User invalidUser = new User(user.getEmail(), "wrongpassword", null);
        Response response = authSteps.loginUser(invalidUser);
        authSteps.verifyLoginWithInvalidCredentials(response);
    }
}