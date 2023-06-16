package app;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import app.controller.DocumentoController;

public class App {
  public static void main(String[] args) throws Exception {
    // http://localhost:8081

    String host = "localhost";
    int port = 8081; // > 1024 (443 HTTPS)

    InetSocketAddress addr = new InetSocketAddress(host, port);
    int backlog = 0;

    HttpServer server = HttpServer.create(addr, backlog);

    FrontController frontController = 
        new FrontController(server);

    frontController.register(DocumentoController.class);

    System.out.println("Servidor ouvindo em " + 
        host + ":" + port);

    server.start();
  }
}


/*
 * class OiHttpHandler implements HttpHandler {
 * 
 * @Override
 * public void handle(HttpExchange x) throws IOException {
 * 
 * String resposta =
 * "<html><head><title>Oi</title></head><body>Oi, <strong>tudo bem?</strong></body></html>"
 * ;
 * 
 * x.getResponseHeaders().add("Content-Type", "text/html");
 * x.getResponseHeaders().add("Curso-IFRS", "TADS");
 * x.sendResponseHeaders(200, resposta.length());
 * 
 * x.getResponseBody().write(resposta.getBytes());
 * 
 * }
 * 
 * }
 * 
 * class TesteHttpHandler implements HttpHandler {
 * 
 * @Override
 * public void handle(HttpExchange x) throws IOException {
 * // O que chega na requisição: Método/Verbo, ex: GET, POST, DELETE, PUT,
 * PATCH, OPTION,...
 * // URI, caminho, localhost:8081/teste
 * // Cabeçalhos da requisição, ex: User-Agent: Mozilla, Gecko,...
 * String requestMethod = x.getRequestMethod();
 * String requestURI = x.getRequestURI().toString();
 * String requestHeaders = x.getRequestHeaders().entrySet().toString();
 * 
 * System.out.println(requestMethod);
 * System.out.println(requestURI);
 * System.out.println(requestHeaders);
 * 
 * x.sendResponseHeaders(200, 1);
 * x.getResponseBody().write(new byte[]{0});
 * }
 * 
 * }
 * 
 * class TchauHttpHandler implements HttpHandler {
 * 
 * @Override
 * public void handle(HttpExchange x) throws IOException {
 * 
 * // URI uri = x.getRequestURI();
 * // System.out.println("Host:  " + uri.getHost());
 * // System.out.println("Path:  " + uri.getPath());
 * // System.out.println("Query: " + uri.getQuery());
 * 
 * String query = x.getRequestURI().getQuery(); // nome=Victor (group1: Victor)
 * 
 * String regex = "nome=([^&]+)";
 * 
 * System.out.println("regex: " + regex);
 * 
 * Pattern pattern = Pattern.compile(regex);
 * 
 * Matcher matcher = pattern.matcher(query);
 * 
 * String nome = "desconhecido";
 * 
 * if (matcher.matches()) {
 * if (matcher.groupCount() > 0) {
 * nome = matcher.group(1);
 * }
 * }
 * 
 * //String nome = "Marcio";
 * 
 * System.out.println(nome);
 * 
 * String resposta =
 * "<html><head><title>Tchau</title></head><body>Tchau, <strong>" + nome +
 * "!</strong></body></html>";
 * 
 * x.getResponseHeaders().add("Content-Type", "text/html");
 * x.getResponseHeaders().add("Curso-IFRS", "TADS");
 * x.sendResponseHeaders(200, resposta.length());
 * 
 * try (OutputStream out = x.getResponseBody()) {
 * out.write(resposta.getBytes());
 * }
 * }
 * }
 * 
 */