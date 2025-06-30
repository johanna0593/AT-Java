package org.example.Etapa1;

import io.javalin.Javalin;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Endpoints {
    public static Javalin Start(Javalin app){

        // Etapa1:  Desenvolvimento REST com Javalin -

        // Item 1:Configure um projeto com Gradle e adicione a dependência do Javalin. Crie uma aplicação básica que inicia na porta 7000 e exponha o endpoint /hello, retornando "Hello, Javalin!".
        app.get("/hello", ctx -> ctx.result("Hello, Javalin!"));


        // Item 2: Implemente um endpoint GET chamado /status que retorna um JSON contendo status: ok e timestamp com a hora atual no formato ISO-8601.
        app.get("/status", ctx ->
                ctx.json(new Auxiliares.Status("ok", LocalDateTime.now().toString()))
        );


        // Item 3: Implemente um endpoint POST chamado /echo, que recebe um JSON com a chave mensagem e retorna o mesmo conteúdo como resposta.
        app.post("/echo", ctx -> {
            Auxiliares.Mensagem item = ctx.bodyAsClass(Auxiliares.Mensagem.class);
            ctx.json(item);
        });


        // Item 4: Implemente um endpoint GET com path parameter /saudacao/{nome} que retorna:
        //{ "mensagem": "Olá, <nome>!" }
        app.get("/saudacao/{nome}", ctx -> {
            ctx.json(new Auxiliares.Saudacao("Olá, " + ctx.pathParam("nome") + "!"));
        });


        return app;
    }
}
