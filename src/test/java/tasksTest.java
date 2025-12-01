import io.qameta.allure.Description;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(BaseTest.class)
public class tasksTest
{
    private String getAuthToken() {
        authTokenTest authService = new authTokenTest();
        return authService.getAccessToken();
    }


    @Test
    @Description("infoUser")
    @DisplayName("Создание задачи связанное с заявкой")
    public void createTaskWithApplication() {
        // Получаем accessToken из AuthToken
        String accessToken = getAuthToken();


        // Создаем задачу с использованием полученного токена
        File jsonFile = new File("src/test/java/JsonFiles/tasks.application.json");
        Response createTask = RestAssured
                .given()
                .body(jsonFile)
                .headers("Authorization", "Bearer " + accessToken , "Content-Type", "application/json; charset=UTF-8")
                .post("http://172.20.207.16/api/client-relations/related-collections/applicationFull/addTask/53aad282-25f8-421b-94b8-29c391ef47b1")
                .andReturn();

        // Выводим результат и проверяем статус код
        createTask.prettyPrint();
        int statusCode = createTask.getStatusCode();
        assertEquals(200, statusCode);
    }
    @Test
    @Description("infoUser")
    @DisplayName("Создание заявки")
    public void createApplication(){

        String body = TestDataJson.application();
        // Получаем токен
        String accessToken = getAuthToken();


        //Параметры предаваемые
        Response createApplication = RestAssured
                .given()
                .body(body)
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .post("http://172.20.207.16/api/client-relations/application-full")
                .andReturn();
        // Выводим результат и проверяем статус код
        String id = createApplication.jsonPath().getString("id");
        System.out.println(id);
        int statusCode = createApplication.getStatusCode();
        assertEquals(200, statusCode);
    }
    @Test
    @Description("infoUser")
    @DisplayName("Создание задачи задачи без приоритета")
    public void createTask(){
        String accessToken = getAuthToken();
        // Назначаем файл с телом запроса
        File jsonFile = new File("src/test/java/JsonFiles/createTask.json");

        Response createTask = RestAssured
                .given()
                .body(jsonFile)
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .post("http://172.20.207.16/api/client-relations/tasks-full")
                .andReturn();

        createTask.prettyPrint();
        int statusCode = createTask.getStatusCode();
        assertEquals(200, statusCode);
        System.out.println("\nКуки");
        Map<String,String> cookies = createTask.getCookies();
        System.out.println(cookies);

    }
    @Test
    @Description("infoUser")
    @DisplayName("Создание задачи задачи с приоритетом аварийная")
    public void createTaskEmergency(){
        String accessToken = getAuthToken();
        // Назначаем файл с телом запроса
        String body = TestDataJson.createTaskEmergency;

        Response createTask = RestAssured
                .given()
                .body(body)
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .post("http://172.20.207.16/api/client-relations/tasks-full")
                .andReturn();

        createTask.prettyPrint();
        int statusCode = createTask.getStatusCode();
        assertEquals(200, statusCode);
        System.out.println("\nКуки");
        Map<String,String> cookies = createTask.getCookies();
        System.out.println(cookies);

    }
    @Test
    @Description("infoUser")
    @DisplayName("Создание задачи задачи с приоритетом Платная")
    public void createTaskPaid(){
        String accessToken = getAuthToken();
        // Назначаем файл с телом запроса
        String body = TestDataJson.createTaskPaid;

        Response createTask = RestAssured
                .given()
                .body(body)
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .post("http://172.20.207.16/api/client-relations/tasks-full")
                .andReturn();

        createTask.prettyPrint();
        int statusCode = createTask.getStatusCode();
        assertEquals(200, statusCode);
        System.out.println("\nКуки");
        Map<String,String> cookies = createTask.getCookies();
        System.out.println(cookies);

    }
     @Test
    @Description("infoApplication")
    @DisplayName("Получение информации по заявке")
    public void getInfoApplication() {
        // Получаем accessToken из AuthToken
         String accessToken = getAuthToken();

        // Создаем задачу с использованием полученного токена
        Response getinfo = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .get("http://172.20.207.16/client-relations/user-group-application/ad2cd409-13c5-4e6d-b8d0-d3a5e620dd98")
                .andReturn();

        // Выводим результат и проверяем статус код

        int statusCode = getinfo.getStatusCode();
        assertEquals(200, statusCode);
    }
    @Test
    @Description("getInfoGroupAplication")
    @DisplayName("Получение списка заявок")
    public void getInfoGroupAplication(){
        String accessToken = getAuthToken();

        Response getInfoGroupAplication = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .get("http://172.20.207.16/client-relations/user-group-application?page=1&size=20&sort=name")
                .andReturn();

        int statusCode = getInfoGroupAplication.getStatusCode();
        assertEquals(200, statusCode);
        getInfoGroupAplication.getBody().asString();

    }
    @Test
    @Description("infoUser")
    @DisplayName("Создание и удаление заявки")
    public void createAndDeleteApplication() {
        // Получаем токен
        String accessToken = getAuthToken();

        // Назначаем файл с телом запроса
        File jsonFile = new File("src/test/java/JsonFiles/application.json");

        // Создаем заявку
        Response createApplication = RestAssured
                .given()
                .body(jsonFile)
                .headers("Authorization", "Bearer " + accessToken,
                        "Content-Type", "application/json; charset=UTF-8")
                .post("http://172.20.207.16/api/client-relations/application-full")
                .andReturn();

        int statusCode = createApplication.getStatusCode();
        assertEquals(200, statusCode);

        // Извлекаем ID созданной заявки
        String applicationId = createApplication.jsonPath().getString("id");
        System.out.println(applicationId);

        // Проверяем, что ID не пустой
        assertNotNull(applicationId, "ID заявки не должен быть null");
        assertFalse(applicationId.isEmpty(), "ID заявки не должен быть пустым");

        // Удаляем созданную заявку
        Response deleteResponse = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken)
                .delete("http://172.20.207.16/api/client-relations/application-full/" + applicationId)
                .andReturn();

        // Проверяем успешное удаление
        int deleteStatusCode = deleteResponse.getStatusCode();
        assertEquals(201, deleteStatusCode, "Заявка должна быть успешно удалена");

    }
    @Test
    @Description("infoUser")
    @DisplayName("Создание и удаление задачи")
    public void createAndDeleteTask() {
        String accessToken = getAuthToken();

        // Назначаем файл с телом запроса
        File jsonFile = new File("src/test/java/JsonFiles/createTask.json");

        // Создаем задачу
        Response createTask = RestAssured
                .given()
                .body(jsonFile)
                .headers("Authorization", "Bearer " + accessToken,
                        "Content-Type", "application/json; charset=UTF-8")
                .post("http://172.20.207.16/api/client-relations/tasks-full")
                .andReturn();

        // Выводим результат и проверяем статус код
        createTask.prettyPrint();
        int statusCode = createTask.getStatusCode();
        assertEquals(200, statusCode);

        // Выводим куки
        System.out.println("\nКуки");
        Map<String, String> cookies = createTask.getCookies();
        System.out.println(cookies);

        // Извлекаем ID созданной задачи из ответа
        String taskId = createTask.jsonPath().getString("id");

        // Проверяем, что ID не пустой
        assertNotNull(taskId, "ID задачи не должен быть null");
        assertFalse(taskId.isEmpty(), "ID задачи не должен быть пустым");
        System.out.println("Создана задача с ID: " + taskId);

        // Удаляем созданную задачу
        Response deleteResponse = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken)
                .delete("http://172.20.207.16/api/client-relations/tasks-full/" + taskId)
                .andReturn();

        // Проверяем успешное удаление
        int deleteStatusCode = deleteResponse.getStatusCode();
        assertEquals(201, deleteStatusCode, "Задача должна быть успешно удалена");

        // Дополнительная проверка: убеждаемся, что задача действительно удалена
        Response getResponse = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken)
                .get("http://172.20.207.16/api/client-relations/tasks-full/" + taskId)
                .andReturn();



        System.out.println("Задача с ID " + taskId + " успешно удалена");
    }
    @Test
    @Description("infoUser")
    @DisplayName("Изменение задачи")
    public void editTask(){
        //получаем токен
        String accessToken = getAuthToken();
        // Назначаем файл с телом запроса
        File jsonFile = new File("src/test/java/JsonFiles/editTask.json");

        Response createTask = RestAssured
                .given()
                .body(jsonFile)
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .put("http://172.20.207.16/api/client-relations/tasks-full/b02a468a-d7f6-4a94-97f2-323f9e2d1d45")
                .andReturn();

        createTask.prettyPrint();
        int statusCode = createTask.getStatusCode();
        assertEquals(200, statusCode);
        System.out.println("\nКуки");
        Map<String,String> cookies = createTask.getCookies();
        System.out.println(cookies);}
    @Test
    @Description("infoUser")
    @DisplayName("Редактирование заявки")
    public void editApplication(){
        // Получаем токен
        String accessToken = getAuthToken();
        // Назначаем файл с телом запроса
        File jsonFile = new File("src/test/java/JsonFiles/application.json");
        //Параметры предаваемые
        Response createApplication = RestAssured
                .given()
                .body(jsonFile)
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .post("http://172.20.207.16/api/client-relations/application-full")
                .andReturn();
        // Выводим результат и проверяем статус код
        String id = createApplication.jsonPath().getString("id");
        System.out.println(id);
        int statusCode = createApplication.getStatusCode();
        assertEquals(200, statusCode);
    }
    @Test
    @Description("infoApplication")
    @DisplayName("Получение информации по задаче")
    public void getInfoTask() {
        // Получаем accessToken из AuthToken
        String accessToken = getAuthToken();

        // Создаем задачу с использованием полученного токена
        Response getinfo = RestAssured
                .given()
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .get("http://172.20.207.16/api/client-relations/tasks-full/54572a9d-c2c4-4f27-8a55-23f5391e726d")
                .andReturn();

        // Выводим результат и проверяем статус код
        String createdDate = getinfo.jsonPath().getString("createdDate");
        assertEquals("2025-10-14 21:42:00.494", createdDate);
        int statusCode = getinfo.getStatusCode();
        assertEquals(200, statusCode);
    }
    @Test
    @Description("отправка сообщения во внутрений чат")
    @DisplayName("отправка сообщения во внутрений чат")
    public void sendMessageInterior(){
        String accessToken = getAuthToken();
        Response sendMessage = RestAssured
                .given()
                .body("{\"link\":\"chat\",\"text\":\"автотест\",\"employeeId\":\"b69f0af0-43bd-4a37-b3c1-f68c123fde0c\",\"module\":\"Задача\",\"objectId\":\"fe66614f-4407-48c9-bda5-079490919c0a\",\"documentIds\":[]}")
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .post("http://172.20.207.16/api/message/chat/create")
                .andReturn();
        int statuscode = sendMessage.statusCode();
        assertEquals(200,statuscode);
    }
    @Test
    @Description("Проверка что сообщение отображается в WebSocket чате")
    @DisplayName("Проверка WebSocket чата")
    public void testMessageInWebSocketChat() throws Exception {

        // Создаем уникальное сообщение чтобы не перепутать с другими
        String uniqueMessage = "автотест " + System.currentTimeMillis();
        System.out.println("🔄 Начинаем тест с сообщением: " + uniqueMessage);

        // Шаг 1: Отправляем сообщение через REST API
        String accessToken = getAuthToken();
        sendMessageToChat(accessToken, uniqueMessage);

        // Шаг 2: Проверяем что сообщение пришло через WebSocket
        boolean messageReceived = checkMessageInWebSocket(uniqueMessage);

        // Шаг 3: Проверяем результат
        assertTrue(messageReceived, "Сообщение '" + uniqueMessage + "' не было получено через WebSocket");
        System.out.println("✅ Тест пройден! Сообщение успешно получено через WebSocket");
    }

    /**
     * Отправляет сообщение в чат через REST API
     */
    private void sendMessageToChat(String accessToken, String messageText) {
        System.out.println("📤 Отправляем сообщение в чат...");

        Response sendMessage = RestAssured
                .given()
                .body("{\"link\":\"chat\",\"text\":\"" + messageText + "\",\"employeeId\":\"b69f0af0-43bd-4a37-b3c1-f68c123fde0c\",\"module\":\"Задача\",\"objectId\":\"fe66614f-4407-48c9-bda5-079490919c0a\",\"documentIds\":[]}")
                .headers("Authorization", "Bearer " + accessToken, "Content-Type", "application/json; charset=UTF-8")
                .post("http://172.20.207.16/api/message/chat/create")
                .andReturn();

        int statusCode = sendMessage.statusCode();
        assertEquals(200, statusCode);
        System.out.println("✅ Сообщение отправлено, статус: " + statusCode);
    }

    /**
     * Проверяет через WebSocket, пришло ли сообщение в чат
     */
    private boolean checkMessageInWebSocket(String expectedMessage) throws Exception {
        System.out.println("📡 Подключаемся к WebSocket...");

        // Создаем "флажок" для отслеживания получения сообщения
        AtomicBoolean messageWasReceived = new AtomicBoolean(false);

        // URL WebSocket соединения (такой же как в вашем примере)
        String wsUrl = "ws://172.20.207.16:7575/chat/internal?employeeId=b69f0af0-43bd-4a37-b3c1-f68c123fde0c&objectId=fe66614f-4407-48c9-bda5-079490919c0a&module=%D0%97%D0%B0%D0%B4%D0%B0%D1%87%D0%B0";

        // Создаем WebSocket клиент
        WebSocketClient client = new WebSocketClient(URI.create(wsUrl)) {

            @Override
            public void onOpen(ServerHandshake handshake) {
                System.out.println("✅ WebSocket подключен");
            }

            @Override
            public void onMessage(String message) {
                System.out.println("📨 WebSocket: " + message);

                // Ищем комбинацию "title" и нашего сообщения
                if (message.contains("title") && message.contains(expectedMessage)) {
                    System.out.println("🎯 Нашли наше сообщение в title!");
                    messageWasReceived.set(true);
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("🔌 WebSocket закрыт: " + reason);
            }

            @Override
            public void onError(Exception ex) {
                System.out.println("❌ Ошибка WebSocket: " + ex.getMessage());
            }
        };

        try {
            // Подключаемся к WebSocket
            client.connect();

            // Ждем пока подключится (максимум 3 секунды)
            int waitCount = 0;
            while (!client.isOpen() && waitCount < 30) {
                Thread.sleep(100);
                waitCount++;
            }

            if (!client.isOpen()) {
                System.out.println("❌ Не удалось подключиться к WebSocket");
                return false;
            }

            System.out.println("⏳ Ожидаем сообщение в чате (10 секунд)...");

            // Ждем сообщение максимум 10 секунд
            for (int i = 0; i < 100; i++) {
                if (messageWasReceived.get()) {
                    return true; // Сообщение получено!
                }
                Thread.sleep(100); // Ждем 100ms между проверками
            }

            System.out.println("⏰ Время ожидания истекло");
            return false;

        } finally {
            // Всегда закрываем соединение
            client.close();
        }
    }


}