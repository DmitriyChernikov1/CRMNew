import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class authTokenTest {

    // Метод для получения accessToken
    public String getAccessToken() {
        // Чтение учетных данных из файла
        String[] credentials = readCredentialsFromFile("src/test/java/txtFiles/credentials.txt");
        if (credentials == null || credentials.length != 2) {
            throw new RuntimeException("Не удалось прочитать логин и пароль из файла");
        }

        Map<String, String> auth = new HashMap<>();
        auth.put("login", credentials[0]);
        auth.put("password", credentials[1]);

        Response responseToken = RestAssured
                .given()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(auth)
                .post("http://172.20.207.16:8083/users/auth/login")
                .andReturn();

        // Проверка статуса ответа
        int statusCode = responseToken.getStatusCode();
        if (statusCode != 200) {
            throw new RuntimeException("Ошибка при получении токена. Код статуса: " + statusCode);
        }

        // Возвращаем accessToken
        return responseToken.jsonPath().getString("accessToken");
    }

    // Метод для чтения учетных данных из файла
    private String[] readCredentialsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String login = null;
            String password = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("login=")) {
                    login = line.substring(6).trim();
                } else if (line.startsWith("password=")) {
                    password = line.substring(9).trim();
                }
            }

            if (login != null && password != null) {
                return new String[]{login, password};
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
        return null;
    }
}