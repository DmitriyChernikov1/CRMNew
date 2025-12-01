import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(BaseTest.class)
public class calendarTest {
    authTokenTest authService = new authTokenTest();
    String accessToken = authService.getAccessToken();
    @Test
    @Description("get Event")
    @DisplayName("Получение ивентов")
    public void  GetEvent() {
        Response responseGetEvent = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .get("http://172.20.207.16/api/calendar/input/event")
                .andReturn();
        int StatusCode = responseGetEvent.getStatusCode();
        assertEquals(200, StatusCode);
    }
    @Test
    @Description("Get Type Events with custom pagination parameters")
    @DisplayName("Получение типов событий")
    public void getTypeEventsWithCustomPagination() {
        Response response = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .queryParam("page", 1)
                .queryParam("size", 10)
                .queryParam("sort", "name,desc")
                .get("http://172.20.207.16/api/calendar/type-event")
                .andReturn();

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode, "Статус код должен быть 200");
    }
    @Test
    @Description("Get Kind Events with default pagination")
    @DisplayName("Получение видов событий с пагинацией по умолчанию")
    public void getKindEventsWithDefaultPagination() {
        Response response = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .get("http://172.20.207.16/api/calendar/kind-event")
                .andReturn();

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode, "Статус код должен быть 200");

        // Проверяем структуру ответа
        String responseBody = response.getBody().asString();
        assertNotNull(responseBody, "Тело ответа не должно быть null");
    }
    @Test
    @Description("Get Kind Events with custom pagination parameters")
    @DisplayName("Получение видов событий с кастомными параметрами пагинации")
    public void getKindEventsWithCustomPagination() {
        Response response = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .queryParam("page", 1)
                .queryParam("size", 10)
                .queryParam("sort", "name,desc")
                .get("http://172.20.207.16/api/calendar/kind-event")
                .andReturn();

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode, "Статус код должен быть 200");
    }
    @Test
    @Description("Get Kind Events with sorting by name ascending")
    @DisplayName("Получение видов событий с сортировкой по имени по возрастанию")
    public void getKindEventsWithSortingAsc() {
        Response response = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .queryParam("sort", "name")
                .get("http://172.20.207.16/api/calendar/kind-event")
                .andReturn();

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode, "Статус код должен быть 200");
    }
    @Test
    @Description("Get Kind Events with sorting by name descending")
    @DisplayName("Получение видов событий с сортировкой по имени по убыванию")
    public void getKindEventsWithSortingDesc() {
        Response response = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .queryParam("sort", "name,desc")
                .get("http://172.20.207.16/api/calendar/kind-event")
                .andReturn();

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode, "Статус код должен быть 200");
    }
    @Test
    @Description("Get Kind Events without authentication")
    @DisplayName("Получение видов событий без авторизации")
    public void getKindEventsWithoutAuth() {
        Response response = RestAssured
                .given()
                .headers("Content-Type", "application/json; charset=UTF-8")
                .get("http://172.20.207.16/api/calendar/kind-event")
                .andReturn();

        int statusCode = response.getStatusCode();
        // Ожидаем 401 Unauthorized или 403 Forbidden в зависимости от конфигурации безопасности
        assertEquals(401, statusCode, "Статус код должен быть 401 без авторизации");
    }
    @Test
    @Description("Get tire fitting events with default pagination")
    @DisplayName("Получение ивентов шиномонтажа с пагинацией по умолчанию")
    public void getTireFittingEventsWithDefaultPagination() {
        String body = TestDataJson.jsonTime();

        Response response = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .body(body)
                .post("http://172.20.207.16/api/calendar/event/tire-fitting")
                .andReturn();

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode, "Статус код должен быть 200");


        String responseBody = response.getBody().asString();
        assertNotNull(responseBody, "Тело ответа не должно быть null");

    }
    @Test
    @Description("Get tire fitting events with custom pagination")
    @DisplayName("Получение ивентов шиномонтажа с кастомной пагинацией")
    public void getTireFittingEventsWithCustomPagination() {
        String body = TestDataJson.jsonTime();

        Response response = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .queryParam("page", 1)
                .queryParam("size", 10)
                .queryParam("sort", "name,desc")
                .body(body)
                .post("http://172.20.207.16/api/calendar/event/tire-fitting")
                .andReturn();

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode, "Статус код должен быть 200");
    }
    @Test
    @Description("Get tire fitting events without authentication")
    @DisplayName("Получение ивентов шиномонтажа без авторизации")
    public void getTireFittingEventsWithoutAuth() {
        String body = TestDataJson.jsonTime();

        Response response = RestAssured
                .given()
                .headers("Content-Type", "application/json; charset=UTF-8")
                .body(body)
                .post("http://172.20.207.16/api/calendar/event/tire-fitting")
                .andReturn();

        int statusCode = response.getStatusCode();
        assertEquals(401, statusCode, "Статус код должен быть 401 без авторизации");
    }
    @Test
    @Description("Get Kind Events and validate response structure")
    @DisplayName("Получение видов событий с проверкой структуры ответа")
    public void getKindEventsAndValidateStructure() {
        Response response = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .get("http://172.20.207.16/api/calendar/kind-event")
                .andReturn();

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode, "Статус код должен быть 200");

        // Проверяем основные поля пагинированного ответа
        response.then().assertThat()
                .body("content", org.hamcrest.Matchers.notNullValue())
                .body("totalPages", org.hamcrest.Matchers.greaterThanOrEqualTo(0))
                .body("totalElements", org.hamcrest.Matchers.greaterThanOrEqualTo(0))
                .body("size", org.hamcrest.Matchers.equalTo(20)) // default size
                .body("number", org.hamcrest.Matchers.equalTo(0)); // default page
    }
    @Test
    @Description("get filter")
    @DisplayName("Получение списка календаря")
    public void  getFilterForCalendar(){
        File jsonFile = new File("src/test/java/JsonFiles/GetFiltersCalendar.json");
        Response responseGetFilter = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .body(jsonFile)
                .post("http://172.20.207.16/api/calendar/event/filtering-event-map")
                .andReturn();
        responseGetFilter.prettyPrint();

        int statusCode = responseGetFilter.getStatusCode();
        assertEquals(200, statusCode);

    }
     @Test
    @Description("creating a simple task")
    @DisplayName("создание простой задачи")
    public void  createSimpleEvent(){

            File jsonBody = new File("src/test/java/JsonFiles/createEvent.json");

            Response responseCreateEvent = RestAssured
                    .given()
                    .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                    .body(jsonBody)
                    .post("http://172.20.207.16/api/calendar/event")
                    .andReturn();
            responseCreateEvent.prettyPrint();

            int statusCode = responseCreateEvent.getStatusCode();
            assertEquals(200, statusCode);
            String eventId = responseCreateEvent.jsonPath().getString("id");
            String employeeId = "b69f0af0-43bd-4a37-b3c1-f68c123fde0c"; // employeeId

            String taskRequestBody = String.format(
                    "{" +
                            "\"eventId\": \"%s\"," +
                            "\"employees\": [{" +
                            "\"employeeId\": \"%s\"," +
                            "\"isMain\": true," +
                            "\"partyStatus\": \"Ожидает решения\"" +
                            "}]" +
                            "}",
                    eventId, employeeId
            );

            Response responseCreateTask = RestAssured
                    .given()
                    .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                    .body(taskRequestBody)
                    .post("http://172.20.207.16/api/calendar/party-employee/create")
                    .andReturn();
            responseCreateTask.prettyPrint();
            int sstatusCode = responseCreateTask.getStatusCode();
            assertEquals(200, sstatusCode);


    }
    @Test
    @Description("get calendar params")
    @DisplayName("Получение  параметров календаря")
    public void  getCalendarParams(){

        Response responseGetParams = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .get("http://172.20.207.16/api/calendar/input/event")
                .andReturn();
        String typeEvents = responseGetParams.jsonPath().getString("typeEventDtos");
        assertNotNull(typeEvents, "поле не должно быть null");
        String kind = responseGetParams.jsonPath().getString("kindEventDtos");
        assertNotNull(kind, "Поле 'kindEventDtos' не должно быть null");
        int statusCode = responseGetParams.getStatusCode();
        assertEquals(200, statusCode);

    }
    @Test
    @Description("get filter")
    @DisplayName("Получение списка графика дежурств")
    public void  getFilterDutySchedule(){

        File jsonFile = new File("src/test/java/JsonFiles/dutySchedule.json");
        Response responseGetFilter = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .body(jsonFile)
                .post("http://172.20.207.16/api/calendar/event/filtering-event-map")
                .andReturn();
        responseGetFilter.prettyPrint();

        int statusCode = responseGetFilter.getStatusCode();
        assertEquals(200, statusCode);

    }
    @Test
    @Description("create schedule")
    @DisplayName("Создание графика дежурств")
    public void createSchedule(){

        File jsonFile = new File("src/test/java/JsonFiles/createSchedule.json");
        Response responseCreateSchedule = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .body(jsonFile)
                .post("http://172.20.207.16/api/calendar/event/generate-schedule")
                .andReturn();


        int statusCode = responseCreateSchedule.getStatusCode();
        assertEquals(200, statusCode);
    }
    @Test
    @Description("delete schedule")
    @DisplayName("Удаление графика дежурств")
    public void deleteSchedule(){

        File jsonFile = new File("src/test/java/JsonFiles/deleteSchedule.json");
        Response responseDeleteSchedule = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .body(jsonFile)
                .post("http://172.20.207.16/api/calendar/event/delete-schedule")
                .andReturn();


        int statusCode = responseDeleteSchedule.getStatusCode();
        assertEquals(201, statusCode);
    }

}
