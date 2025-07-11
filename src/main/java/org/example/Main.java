package org.example;

import io.javalin.Javalin;
import org.example.Etapa1.Endpoints;
import org.example.Etapa1Tarefas.ListaDeTarefas;
import org.example.Etapa3.Etapa3;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Para testar descomente a etapa desejada
        // Obs. Descomente apenas uma por vez

        // Do item 1 até o item 4
        //Etapa1();

        // Do item 5 até o item 6
        //Etapa1Tarefas();

        Etapa3(args);
    }

    private static void Etapa1 () {
        // Inicia o servidor Etapa1
        Javalin app = Javalin.create().start(7000);
        Endpoints.Start(app);
    }

    private static void Etapa1Tarefas () {
        // Inicia o servidor Etapa1 Sistema de tarefas
        Javalin app = Javalin.create(
                javalinConfig -> javalinConfig.http.defaultContentType = "application/json; charset=utf-8"
        ).start(7000);

        ListaDeTarefas.EndPoints(app);
    }

    private static void Etapa3(String[] args) {
        Javalin app = Javalin.create().start(7000);
        ListaDeTarefas.EndPoints(app);
    }
}