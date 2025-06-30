package org.example.Etapa1Tarefas;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import org.example.Etapa1.Auxiliares;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ListaDeTarefas {
    public static void EndPoints(Javalin app){
        List<Tarefa> tarefas = new ArrayList<>();

        // Etapa1: Desenvolvimento REST com Javalin

        // Item 5: Com base no caso de uso definido pelo professor, implemente um endpoint POST que receba um JSON com os atributos necessários (ex: nome, email, idade) e armazene os dados em uma estrutura em memória (como uma lista). Retorne status 201 ao criar com sucesso.
        app.post("/tarefas", ctx -> {
            var body = ctx.bodyAsClass(Tarefa.class);

            if (body.titulo == null || body.titulo.isBlank()) {
                ctx.status(HttpStatus.BAD_REQUEST).result("Campo 'titulo' é obrigatório.");
                return;
            }

            Tarefa novaTarefa = new Tarefa(body.titulo, body.descricao);
            tarefas.add(novaTarefa);

            ctx.status(HttpStatus.CREATED).json(novaTarefa);
        });


        // Item 6A: Retorne todos os registros armazenados até o momento (ex: /usuarios, /tarefas, etc.).
        app.get("/tarefas", ctx -> ctx.json(tarefas));


        // Item 6B: Busque um item pelo identificador principal (ex: email, id ou nome) e retorne os dados em JSON. Caso não exista, retorne status 404.
        app.get("/tarefas/{id}", ctx -> {
            String idStr = ctx.pathParam("id");
            UUID id;

            try {
                id = UUID.fromString(idStr);
            } catch (IllegalArgumentException e) {
                ctx.status(HttpStatus.NOT_FOUND).result("ID inválido");
                return;
            }

            var tarefa = tarefas.stream()
                    .filter(t -> t.id.equals(id))
                    .findFirst()
                    .orElse(null);

            ctx.json(tarefa);
            });
    }
}
