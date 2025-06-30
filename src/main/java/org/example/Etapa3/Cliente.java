package org.example.Etapa3;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Cliente {

    private static final Gson conversor = new Gson();

    public static String[] enviarGet(String endereco) throws IOException, URISyntaxException {
        HttpURLConnection conexao = configurarConexao(endereco, "GET");

        int codigoResposta = conexao.getResponseCode();
        String mensagemResposta = conexao.getResponseMessage();
        String conteudo = null;

        if (codigoResposta != HttpURLConnection.HTTP_NOT_FOUND) {
            conteudo = lerConteudo(conexao);
        }

        return new String[]{conteudo, String.valueOf(codigoResposta), mensagemResposta};
    }

    public static String[] enviarPost(String endereco, Object objeto) throws IOException, URISyntaxException {
        HttpURLConnection conexao = configurarConexao(endereco, "POST");

        String json = conversor.toJson(objeto);
        try (OutputStream output = conexao.getOutputStream()) {
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            output.write(bytes);
        }

        int status = conexao.getResponseCode();
        String msg = conexao.getResponseMessage();

        if (status != 200 && status != 201) {
            if (status >= 500 && status < 600) {
                return enviarPost(endereco, objeto); // reenvia se erro 5xx
            }
            throw new RuntimeException("Erro ao executar POST. Código: " + status);
        }

        String retorno = lerConteudo(conexao);
        return new String[]{retorno, String.valueOf(status), msg};
    }

    public static String[] enviarOptions(String endereco) throws IOException, URISyntaxException {
        HttpURLConnection conexao = configurarConexao(endereco, "OPTIONS");

        int status = conexao.getResponseCode();
        String msg = conexao.getResponseMessage();
        String metodosPermitidos = conexao.getHeaderField("Allow");

        conexao.disconnect();

        return new String[]{metodosPermitidos, String.valueOf(status), msg};
    }

    private static HttpURLConnection configurarConexao(String endereco, String metodo) throws IOException, URISyntaxException {
        URL url = new URI(endereco).toURL();
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

        conexao.setRequestMethod(metodo);
        conexao.setRequestProperty("Accept", "application/json");

        if (metodo.equalsIgnoreCase("POST") || metodo.equalsIgnoreCase("PUT")) {
            conexao.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conexao.setDoOutput(true);
        }

        return conexao;
    }

    private static String lerConteudo(HttpURLConnection conexao) throws IOException {
        try (BufferedReader leitor = new BufferedReader(
                new InputStreamReader(conexao.getInputStream(), StandardCharsets.UTF_8))) {

            StringBuilder resultado = new StringBuilder();
            String linha;

            while ((linha = leitor.readLine()) != null) {
                resultado.append(linha).append("\n");
            }

            return resultado.toString();
        } finally {
            conexao.disconnect();
        }
    }
}
