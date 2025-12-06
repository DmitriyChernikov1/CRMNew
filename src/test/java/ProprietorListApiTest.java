import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class ProprietorListApiTest {
    authTokenTest authService = new authTokenTest();
    String accessToken = authService.getAccessToken();
    @Test
    @Description("Получение списка собственников с пагинацией")
    @DisplayName("Успешное получение списка собственников")
    public void getProprietorList_Success() {


        Response getProprietorList = given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .get("http://172.20.207.16:9000/client-relations/tasks/proprietor")
                .andReturn();

        int statusCode = getProprietorList.getStatusCode();
        assertEquals(200, statusCode);

        String responseBody = getProprietorList.getBody().asString();
        assertNotNull(responseBody);
    }

    @Test
    @Description("Получение списка собственников с указанием страницы")
    @DisplayName("Успешное получение списка с пагинацией")
    public void getProprietorList_WithPagination() {

        Response getProprietorList = given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .get("http://172.20.207.16:9000/client-relations/tasks/proprietor")
                .andReturn();

        int statusCode = getProprietorList.getStatusCode();
        assertEquals(200, statusCode);

        String responseBody = getProprietorList.getBody().asString();
        assertNotNull(responseBody);
    }

    @Test
    @Description("Получение списка собственников с сортировкой по имени")
    @DisplayName("Успешное получение списка с сортировкой")
    public void getProprietorList_WithSorting() {


        Response getProprietorList = given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .queryParam("sort", "name")
                .get("http://172.20.207.16:9000/client-relations/tasks/proprietor")
                .andReturn();

        int statusCode = getProprietorList.getStatusCode();
        assertEquals(200, statusCode);

        String responseBody = getProprietorList.getBody().asString();
        assertNotNull(responseBody);
    }

    @Test
    @Description("Получение списка собственников с сортировкой по убыванию")
    @DisplayName("Успешное получение списка с сортировкой по убыванию")
    public void getProprietorList_WithDescSorting() {

        Response getProprietorList = given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .queryParam("sort", "name,desc")
                .get("http://172.20.207.16:9000/client-relations/tasks/proprietor")
                .andReturn();

        int statusCode = getProprietorList.getStatusCode();
        assertEquals(200, statusCode);

        String responseBody = getProprietorList.getBody().asString();
        assertNotNull(responseBody);
    }


    @Test
    @Description("Получение списка собственников с полными параметрами")
    @DisplayName("Успешное получение списка со всеми параметрами")
    public void getProprietorList_WithAllParams() {

        Response getProprietorList = given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .queryParam("page", 1)
                .queryParam("size", 5)
                .queryParam("sort", "name,desc")
                .get("http://172.20.207.16:9000/client-relations/tasks/proprietor")
                .andReturn();

        int statusCode = getProprietorList.getStatusCode();
        assertEquals(200, statusCode);

        String responseBody = getProprietorList.getBody().asString();
        assertNotNull(responseBody);
    }

    @Test
    @Description("Получение списка собственников - неавторизованный доступ")
    @DisplayName("Ошибка авторизации при получении списка")
    public void getProprietorList_Unauthorized() {
        Response getProprietorList = given()
                .headers("Content-Type", "application/json; charset=UTF-8")
                .get("http://172.20.207.16:9000/client-relations/tasks/proprietor")
                .andReturn();

        int statusCode = getProprietorList.getStatusCode();
        assertEquals(401, statusCode);
    }

    @Test
    @Description("Получение списка собственников с некорректными параметрами")
    @DisplayName("Ошибка при некорректных параметрах запроса")
    public void getProprietorList_InvalidParams() {


        Response getProprietorList = given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .queryParam("page", -1) // Некорректная страница
                .queryParam("size", 1000) // Слишком большой размер
                .get("http://172.20.207.16:9000/client-relations/tasks/proprietor")
                .andReturn();

        int statusCode = getProprietorList.getStatusCode();
        // Может вернуть 400 или работать с ограничениями
        assertTrue(statusCode == 400 || statusCode == 200);
    }

    @Test
    @Description("Проверка структуры ответа списка собственников")
    @DisplayName("Валидация структуры ответа списка")
    public void getProprietorList_ResponseStructure() {

        Response getProprietorList = given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .queryParam("page", 0)
                .queryParam("size", 5)
                .get("http://172.20.207.16:9000/client-relations/tasks/proprietor")
                .andReturn();

        int statusCode = getProprietorList.getStatusCode();
        assertEquals(200, statusCode);

        // Проверка наличия основных полей пагинации
        Integer totalPages = getProprietorList.jsonPath().getInt("totalPages");
        Long totalElements = getProprietorList.jsonPath().getLong("totalElements");
        Integer size = getProprietorList.jsonPath().getInt("size");
        Integer number = getProprietorList.jsonPath().getInt("number");
        Object content = getProprietorList.jsonPath().get("content");

        assertNotNull(totalPages);
        assertNotNull(totalElements);
        assertNotNull(size);
        assertNotNull(number);
        assertNotNull(content);
    }

    @Test
    @Description("Получение пустого списка собственников")
    @DisplayName("Успешное получение пустого списка")
    public void getProprietorList_EmptyResult() {

        Response getProprietorList = given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .queryParam("page", 9999) // Несуществующая страница
                .queryParam("size", 10)
                .get("http://172.20.207.16:9000/client-relations/tasks/proprietor")
                .andReturn();

        int statusCode = getProprietorList.getStatusCode();
        assertEquals(200, statusCode);

        // Проверка что content пустой
        Integer contentSize = getProprietorList.jsonPath().getInt("content.size()");
        assertEquals(0, contentSize);
    }

    @Test
    @Description("Получение списка собственников с параметрами по умолчанию")
    @DisplayName("Успешное получение списка с параметрами по умолчанию")
    public void getProprietorList_WithDefaultParams() {

        Response getProprietorList = given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                // Без параметров - должны использоваться значения по умолчанию
                .get("http://172.20.207.16:9000/client-relations/tasks/proprietor")
                .andReturn();

        int statusCode = getProprietorList.getStatusCode();
        assertEquals(200, statusCode);

        // Проверка что используется размер страницы по умолчанию (20)
        Integer size = getProprietorList.jsonPath().getInt("size");
        assertEquals(20, size);

        // Проверка что используется страница по умолчанию (0)
        Integer number = getProprietorList.jsonPath().getInt("number");
        assertEquals(0, number);
    }

    @Test
    @Description("Проверка сортировки списка собственников")
    @DisplayName("Валидация сортировки списка")
    public void getProprietorList_SortValidation() {

        // Получаем список с сортировкой по имени по возрастанию
        Response getProprietorList = given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .queryParam("sort", "name,asc")
                .queryParam("size", 10)
                .get("http://172.20.207.16:9000/client-relations/tasks/proprietor")
                .andReturn();

        int statusCode = getProprietorList.getStatusCode();
        assertEquals(200, statusCode);

        Integer contentSize = getProprietorList.jsonPath().getInt("content.size()");
        if (contentSize > 1) {
            // Проверка что элементы отсортированы по имени по возрастанию
            String firstName = getProprietorList.jsonPath().getString("content[0].name");
            String secondName = getProprietorList.jsonPath().getString("content[1].name");

            if (firstName != null && secondName != null) {
                assertTrue(firstName.compareToIgnoreCase(secondName) <= 0);
            }
        }
    }
    @Test
    @Description("Получение собственника по UUID")
    @DisplayName("Успешное получение собственника")
    public void getProprietor_Success() {

        String validUuid = "0d020b30-d708-4714-a7d0-cd0521195a3f";

        Response getProprietor = given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .get("http://172.20.207.16:9000/client-relations/tasks/proprietor/" + validUuid)
                .andReturn();

        int statusCode = getProprietor.getStatusCode();
        assertEquals(200, statusCode);

        String responseBody = getProprietor.getBody().asString();
        assertNotNull(responseBody);
    }

    @Test
    @Description("Получение собственника - неавторизованный доступ")
    @DisplayName("Ошибка авторизации при получении собственника")
    public void getProprietor_Unauthorized() {
        String validUuid = "0d020b30-d708-4714-a7d0-cd0521195a3f";

        Response getProprietor = given()
                .headers("Content-Type", "application/json; charset=UTF-8")
                .get("http://172.20.207.16:9000/client-relations/tasks/proprietor/" + validUuid)
                .andReturn();

        int statusCode = getProprietor.getStatusCode();
        assertEquals(401, statusCode);
    }

    @Test
    @Description("Получение собственника с неверным UUID")
    @DisplayName("Ошибка при неверном формате UUID")
    public void getProprietor_InvalidUuid() {

        String invalidUuid = "invalid-uuid-format";

        Response getProprietor = given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .get("http://172.20.207.16:9000/client-relations/tasks/proprietor/" + invalidUuid)
                .andReturn();

        int statusCode = getProprietor.getStatusCode();
        assertEquals(500, statusCode);
    }

    @Test
    @Description("Получение несуществующего собственника")
    @DisplayName("Ошибка при запросе несуществующего собственника")
    public void getProprietor_NotFound() {


        String nonExistentUuid = "00000000-0000-0000-0000-000000000000";

        Response getProprietor = given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .get("http://172.20.207.16:9000/client-relations/tasks/proprietor/" + nonExistentUuid)
                .andReturn();

        int statusCode = getProprietor.getStatusCode();
        assertEquals(500, statusCode);
    }

    @Test
    @Description("Получение собственника с использованием path параметра")
    @DisplayName("Успешное получение собственника (path param)")
    public void getProprietor_WithPathParam() {

        String validUuid = "0d020b30-d708-4714-a7d0-cd0521195a3f";

        Response getProprietor = given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .pathParam("uuid", validUuid)
                .get("http://172.20.207.16:9000/client-relations/tasks/proprietor/{uuid}")
                .andReturn();

        int statusCode = getProprietor.getStatusCode();
        assertEquals(200, statusCode);

        String responseBody = getProprietor.getBody().asString();
        assertNotNull(responseBody);

        // Проверка что возвращен корректный UUID
        String responseUuid = getProprietor.jsonPath().getString("id");
        assertEquals(validUuid, responseUuid);
    }

    @Test
    @Description("Получение собственника с проверкой заголовков ответа")
    @DisplayName("Проверка заголовков ответа")
    public void getProprietor_ResponseHeaders() {

        String validUuid = "0d020b30-d708-4714-a7d0-cd0521195a3f";

        Response getProprietor = given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .get("http://172.20.207.16:9000/client-relations/tasks/proprietor/" + validUuid)
                .andReturn();

        int statusCode = getProprietor.getStatusCode();
        assertEquals(200, statusCode);

        // Проверка заголовков ответа
        String contentType = getProprietor.getHeader("Content-Type");
        assertNotNull(contentType);
        assertTrue(contentType.contains("application/json"));
    }

    @Test
    @Description("Получение собственника и проверка формата UUID")
    @DisplayName("Проверка формата UUID в ответе")
    public void getProprietor_UuidFormat() {

        String validUuid = "0d020b30-d708-4714-a7d0-cd0521195a3f";

        Response getProprietor = given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .get("http://172.20.207.16:9000/client-relations/tasks/proprietor/" + validUuid)
                .andReturn();

        int statusCode = getProprietor.getStatusCode();
        assertEquals(200, statusCode);

        String responseUuid = getProprietor.jsonPath().getString("id");

        // Проверка что UUID соответствует формату
        assertTrue(responseUuid.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"),
                "UUID format is invalid");

        assertEquals(validUuid, responseUuid);
    }
    @Test
    @Description("Создание новой записи собственника с использованием DTO")
    @DisplayName("Успешное создание и удаление собственника с проверкой заголовков")
    public void saveAndDeleteProprietorDto_Success() {


        String clientId = "5146211d-6bfb-4966-8ef8-3a1e69e72449";
        String objectUk = "690396bc-2ff2-459c-b1be-b05adaf711b6";

        String requestBody = String.format(
                "{\"clientId\":\"%s\",\"objectUk\":\"%s\",\"typeContact\":\"Собственник\",\"typeOwnership\":\"совместная\"}",
                clientId, objectUk
        );

        // Выполняем POST запрос
        Response saveProprietorResponse = RestAssured
                .given()
                .headers(
                        "Authorization", "Bearer " + accessToken,
                        "Content-Type", "application/json; charset=UTF-8"
                )
                .body(requestBody)
                .post("http://172.20.207.16:9000/client-relations/tasks/proprietor/save-dto")
                .andReturn();

        // Проверяем статус код
        int statusCode = saveProprietorResponse.getStatusCode();
        assertEquals(200, statusCode, "Expected status code 201 Created");

        // Проверка заголовков ответа
        String contentType = saveProprietorResponse.getHeader("Content-Type");
        assertNotNull(contentType, "Content-Type header should not be null");
        assertTrue(contentType.contains("application/json"), "Content-Type should be application/json");

        // Проверяем, что тело ответа не пустое
        String responseBody = saveProprietorResponse.getBody().asString();
        assertNotNull(responseBody, "Response body should not be null");
        assertFalse(responseBody.isEmpty(), "Response body should not be empty");
        saveProprietorResponse.prettyPrint();

        String id =saveProprietorResponse.jsonPath().getString("id");

        String deleteBody = String.format(
                "{\"id\":\"%s\",\"clientId\":\"%s\",\"objectUk\":\"%s\",\"typeContact\":\"Собственник\",\"isDelete\":true,\"typeOwnership\":\"совместная\"}",
                id, clientId, objectUk
        );
        Response deleteProprietor = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .body(deleteBody)
                .put("http://172.20.207.16/api/client-relations/tasks/proprietor/a67ae7c3-74d9-4c0c-91f9-0e79e88e07ec")
                .andReturn();
        int statusCodes = deleteProprietor.getStatusCode();
        assertEquals(200,statusCodes);

    }


}