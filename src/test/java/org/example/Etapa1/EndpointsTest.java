package org.example.Etapa1;

import io.javalin.Javalin;
import io.javalin.testtools.*;
import okhttp3.RequestBody;
import org.example.Etapa1Tarefas.ListaDeTarefas;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.*;
import static org.junit.jupiter.api.Assertions.*;


public class EndpointsTest {
    private static final String URL_GERAL ="http://localhost:7000";

    private Javalin createApp() {
        Javalin app = Javalin.create(config -> {
            //config.http.defaultContentType = "text/plain; charset=utf-8";
            config.http.defaultContentType = "application/json; charset=utf-8";
        }).start(7000);

        Endpoints.Start(app);

        return app;
    }


    @Test
    @DisplayName("Etapa 2 - Item1")
    public void testHelloEndpointReturnsHelloJavalin() {
        JavalinTest.test(createApp(), (server, client) -> {
            var response = client.get("/hello");
            assertEquals(200, response.code());
            assertEquals("Hello, Javalin!", response.body().string());
        });
    }


    @Test
    @DisplayName("Etapa2 - item 2")
    public void testCreateItemReturnsStatus201() throws URISyntaxException, IOException, InterruptedException {
        Javalin app = createApp();
        ListaDeTarefas.EndPoints(app);

        String jsonTarefa = """
            {
              "titulo": "Estudar Javalin",
              "descricao": "Ler a documentação e fazer exemplos",
              "concluida": false
            }
            """;

        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_GERAL + "/tarefas"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonTarefa))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }


    @Test
    @DisplayName("Etapa2 - item 3")
    public void testGetItemByIdReturnsCorrectItem() throws URISyntaxException, IOException, InterruptedException {
        Javalin app = createApp();
        ListaDeTarefas.EndPoints(app);

        String json = """
            {
                "titulo": "Minha tarefa teste",
                "descricao": "Descrição da tarefa"
            }
            """;

        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_GERAL + "/tarefas"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        String responseBody = response.body();
        String id = responseBody.replaceAll(".*\"id\"\\s*:\\s*\"([^\"]+)\".*", "$1");

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(new URI(URL_GERAL  + "/tarefas/" + id))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getResponse.statusCode(), "Status do GET deve ser 200");
        String bodyGet = getResponse.body();

        assertTrue(bodyGet.contains("Minha tarefa teste"), "Resposta deve conter o título");
        assertTrue(bodyGet.contains("Descrição da tarefa"), "Resposta deve conter a descrição");
    }


    @Test
    @DisplayName("Etapa2 - item 4")
    public void testListItemsReturnsNonEmptyArray() {
        JavalinTest.test(createApp(), (server, client) -> {
            // Cria um item
            client.post("/tarefa", RequestBody.create("Tarefa na Lista", null));

            // Lista os itens
            var listResp = client.get("/tarefas");
            assertEquals(200, listResp.code());
            String responseBody = listResp.body().string();
            assertTrue(responseBody.contains("Item na Lista"));
            assertTrue(responseBody.startsWith("["));
        });
    }
}