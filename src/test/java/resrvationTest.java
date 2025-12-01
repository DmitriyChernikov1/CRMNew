import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(BaseTest.class)
public class resrvationTest {
    // Получаем авторизационный токен
    authTokenTest authService = new authTokenTest();
    String accessToken = authService.getAccessToken();
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static Map<String, Object> createReservationRequestBody() {
        return createReservationRequestBody(
                "f239537b-c3bf-4ded-8b10-ef1dc8ff55ac",
                9,  // start hour
                23  // end hour
        );
    }

    public static Map<String, Object> createReservationRequestBody(
            String objectUkId,
            int startHour,
            int endHour
    ) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("startPlanDate", getFormattedDateTime(startHour));
        requestBody.put("stopPlanDate", getFormattedDateTime(endHour));
        requestBody.put("objectUkIds", Collections.singletonList(objectUkId));
        return requestBody;
    }

    private static String getFormattedDateTime(int hour) {
        return LocalDateTime.now()
                .withHour(hour)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .format(DATE_FORMATTER);
    }
    @Test
    @Description("Бронирование")
    @DisplayName("создание бронирование авто")
    public void resrvationed(){
        //Получаем авторизационный токен
        authTokenTest authService = new authTokenTest();
        String accessToken = authService.getAccessToken();
// Получаем Дату в формате строки
        String startDate = TimeGenerated.generateTimeStart();
        String endDate = TimeGenerated.generateTimeEnd();

        String requestBody = String.format(
                "{\n" +
                        "\"destination\": \"Автотест\",\n" +
                        "\"startDate\": \"%s\",\n" +
                        "\"endDate\": \"%s\"\n" +
                        "}", startDate, endDate);

        Response createReservation = RestAssured
                .given()
                .body(requestBody)
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .post("http://172.20.207.16/api/client-relations/tasks-full/booking-rented-car")
                .andReturn();
        int statuscode = createReservation.getStatusCode();
        assertEquals(201, statuscode);
        createReservation.prettyPrint();

    }
    @Test
    @Description("Бронирование")
    @DisplayName("Получение списка записей")
    public void GetReservation() {
        authTokenTest authService = new authTokenTest();
        String accessToken = authService.getAccessToken();
        String date = TimeGenerated.planeDate();

        Response getReservations = RestAssured
                .given()
                .body(date)
                .header("accept", "*/*")
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json; charset=UTF-8")
                .queryParam("page", 0)  // Добавляем query-параметр page
                .queryParam("size", 20)  // Добавляем query-параметр size
                .post("http://172.20.207.16:5555/calendar/event/rented-cars");

        getReservations.prettyPrint();
        int statuscode = getReservations.getStatusCode();
        assertEquals(200, statuscode);
    }

    @Test
    @Description("Бронирование")
    @DisplayName("создание бронирование авто Форд")
    public void resrvationFord(){

// Получаем Дату в формате строки
        String startDate = TimeGenerated.generateTimeStart();
        String endDate = TimeGenerated.generateTimeEnd();
        //Создаем body
        String requestBody = String.format(
                "{\n" +
                        "\"destination\": \"Автотест\",\n" +
                        "\"startDate\": \"%s\",\n" +
                        "\"endDate\": \"%s\",\n" +
                        "\"objectUkId\": \"b71a6e71-da3a-46e7-a86f-0e0f0f8ccf67\"\n" +
                        "}", startDate, endDate);


        Response createReservation = RestAssured
                .given()
                .log().all()  // Логирование запроса
                .body(requestBody)
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .post("http://172.20.207.16/api/client-relations/tasks-full/booking-rented-car-ford")
                .andReturn()
                .then()
                .log().all()  // Логирование ответа
                .extract()
                .response();
        int statuscode = createReservation.getStatusCode();
        assertEquals(201, statuscode);
        System.out.println(requestBody);
    }
    @Test
    @Description("Бронирование")
    @DisplayName("Получение списка записей Форд")
    public void GetReservationFord() {


        String requestBody = "{\"startPlanDate\":\"" + TimeGenerated.getTodayStartTime() + "\","
                + "\"stopPlanDate\":\"" + TimeGenerated.getTodayEndTime() + "\","
                + "\"objectUkIds\":[\"b71a6e71-da3a-46e7-a86f-0e0f0f8ccf67\"]}";

        // Логирование запроса и отправка запроса
        Response getReservations = RestAssured
                .given()
                .log().all()  // Логирование запроса (метод, URL, заголовки, тело)
                .body(requestBody)
                .header("accept", "*/*")
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json; charset=UTF-8")
                .post("http://172.20.207.16/api/calendar/event/rented-cars-ford")
                .then()
                .log().all()  // Логирование ответа (статус код, заголовки, тело)
                .extract()
                .response();

        // Вывод тела ответа в консоль
        getReservations.prettyPrint();

        // Проверка статус-кода
        int statuscode = getReservations.getStatusCode();
        assertEquals(200, statuscode);
    }
    @Test
    @Description("Бронирование")
    @DisplayName("создание бронирование авто Vats")
    public void resrvationVats(){
        //Получаем авторизационный токен
        authTokenTest authService = new authTokenTest();
        String accessToken = authService.getAccessToken();
// Получаем Дату в формате строки
        String startDate = TimeGenerated.generateTimeStart();
        String endDate = TimeGenerated.generateTimeEnd();
        //Создаем body
        String requestBody = String.format(
                "{\n" +
                        "\"destination\": \"Автотест\",\n" +
                        "\"startDate\": \"%s\",\n" +
                        "\"endDate\": \"%s\",\n" +
                        "\"objectUkId\": \"f239537b-c3bf-4ded-8b10-ef1dc8ff55ac\"\n" +
                        "}", startDate, endDate);


        Response createReservation = RestAssured
                .given()
                .log().all()  // Логирование запроса
                .body(requestBody)
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .post("http://172.20.207.16/api/client-relations/tasks-full/booking-rented-car-hav")
                .andReturn()
                .then()
                .log().all()  // Логирование ответа
                .extract()
                .response();
        int statuscode = createReservation.getStatusCode();
        assertEquals(201, statuscode);

    }
    @Test
    @Description("Бронирование")
    @DisplayName("Получение списка записей Ватс")
    public void GetReservationVats() {
        authTokenTest authService = new authTokenTest();
        String accessToken = authService.getAccessToken();
        Map<String, Object> requestBody = resrvationTest.createReservationRequestBody();


        Response getReservations = RestAssured
                .given()
                .body(requestBody)
                .header("accept", "*/*")
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json; charset=UTF-8")
                .queryParam("page", 0)
                .queryParam("size", 20)
                .post("http://172.20.207.16/api/calendar/event/rented-cars-hav");

        getReservations.prettyPrint();
        int statuscode = getReservations.getStatusCode();
        assertEquals(200, statuscode);
    }
    @Test
    @Description("Бронирование")
    @DisplayName("просмтор бронирования")
    public void GetReservationNiva(){


    }


}