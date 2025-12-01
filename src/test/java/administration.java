import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class administration {
    authTokenTest authService = new authTokenTest();
    String accessToken = authService.getAccessToken();

    @Test
    @Description("putUsers")
    @DisplayName("Изменение пользователя")
    public void putUser() {

        String body = TestDataJson.bodyForUser;
        Response putUser = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .body(body)
                .post("http://172.20.207.16/api/users/user_employee/01bb66cc-6456-41ce-9fb7-ad38c25fea43")
                .andReturn();

        int statusCode = putUser.getStatusCode();
        assertEquals(200, statusCode);
        putUser.getBody().asString();
    }
}
