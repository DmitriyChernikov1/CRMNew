import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.restassured.http.Headers;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(BaseTest.class)
public class authTest {



    String str = "Fjt4wNG3KTP6oZ*%#}UIg#i?";
    private String[] readCredentialsFromFile(String filePath) {
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));

            String login = null;
            String password = null;

            String[] lines = fileContent.split("\n");
            for (String line : lines) {
                if (line.startsWith("login=")) {
                    login = line.substring(6).trim();
                } else if (line.startsWith("password=")) {
                    password = line.substring(9).trim();
                }
            }

            if (login == null || password == null) {
                throw new RuntimeException("Не найдены login или password в файле");
            }

            return new String[]{login, password};

        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла: " + filePath, e);
        }
    }
    @Test
    @Description("Авторизация")
    @DisplayName("Вход по валидным данным")
    public void authorization(){
        String[] credentials = readCredentialsFromFile("src/test/java/txtFiles/credentials.txt");
        Map<String, String> auth = new HashMap<>();
        auth.put("login", credentials[0]);
        auth.put("password", credentials[1]);
        Response responseToken = RestAssured
                .given()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(auth)
                .post("http://172.20.207.16:8083/users/auth/login")
                .andReturn();
        int statuscode =responseToken.getStatusCode();
        responseToken.prettyPrint();
        String resp = responseToken.jsonPath().getString("message");
        System.out.println(resp);

        assertEquals(200, statuscode);





        Headers responseHeaders = responseToken.getHeaders();
        System.out.println(responseHeaders);

    }
    @Test
    @Description("Авторизация")
    @DisplayName("Вход под c неверным паролем")
    public void wrongPassword(){
        Map<String, String> auth = new HashMap<>();
        auth.put("login", "DChernikov@sbercity.ru");
        auth.put("password", "1rpJcOI3gVm1MRyLn");
        Response responseToken = RestAssured
                .given()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(auth)
                .post("http://172.20.207.16:8083/users/auth/login")
                .andReturn();
        int statuscode =responseToken.getStatusCode();
        JsonPath jsonPath = responseToken.jsonPath();
        assertEquals("Incorrect login or password", jsonPath.getString("message"));

        assertEquals(401, statuscode);


    }
    @Test
    @Description("Авторизация")
    @DisplayName("Вход под c неверным логином")
    public void wrongLogin(){
        Map<String, String> auth = new HashMap<>();
        auth.put("login", "1DChernikov@sbercity.ru");
        auth.put("password", "G$h8pY}%ci~ZD%H1");
        Response responseToken = RestAssured
                .given()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(auth)
                .post("http://172.20.207.16:8083/users/auth/login")
                .andReturn();
        int statusCode = responseToken.getStatusCode();
        String errorMessage = responseToken.jsonPath().getString("message");

        assertEquals(401, statusCode);
        assertEquals("Incorrect login or password", errorMessage);
    }
    @Test
    @Description("Авторизация")
    @DisplayName("Вход с просроченным паролем")
    public void expiredPassword(){
        Map<String, String> auth = new HashMap<>();
        auth.put("login", "1dimon.ag6@gmail.com");
        auth.put("password", "string");
        Response responseAuth = RestAssured
                .given()
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("accept", "application/json")
                .header("Accept-Language" , "ru-Ru")
                .body(auth)
                .post("http://172.20.207.16:8083/users/auth/login")
                .andReturn();
        int statuscode =responseAuth.getStatusCode();
        String message = responseAuth.jsonPath().getString("message");
        String test = "Учетная запись пользователя 1dimon.ag6@gmail.com удалена и не может быть использована для входа. Обратитесь к администратору.";

assertEquals(test, message);
        assertEquals(401, statuscode);
        System.out.println(statuscode);

    }
    @Test
    @Description("Авторизация")
    @DisplayName("Превышен лимит ошибок")
    public void tooMuchAttempts(){
        Map<String, String> auth = new HashMap<>();
        auth.put("login", "DCh222ernikov@sbercity.ru");
        auth.put("password", "string");
        Response responseAuth = RestAssured
                .given()
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("accept", "application/json")
                .body(auth)
                .post("http://172.20.207.16:8083/users/auth/login")
                .andReturn();
        int statuscode =responseAuth.getStatusCode();
        String message = responseAuth.jsonPath().getString("message");
        assertEquals("Account has been blocked and cannot be used to sign in. Contact the administrator. Reason: Пользователь ввел 10 раз неверный пароль", message);
        assertEquals(401, statuscode);

    }
    @Test
    @Description("Выход из системы")
    @DisplayName("Успешный выход из системы")
    public void successfulLogout() {
        authTokenTest authService = new authTokenTest();
        String accessToken = authService.getAccessToken();
        Response logout = RestAssured
                .given()
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("accept", "application/json")
                .header("Accept-Language", "ru-RU")
                .header("Authorization", "Bearer " + accessToken)
                .get("http://172.20.207.16:8083/users/auth/logout")
                .andReturn();
        int statuscode = logout.getStatusCode();
        assertEquals(statuscode, 200);
    }

}
