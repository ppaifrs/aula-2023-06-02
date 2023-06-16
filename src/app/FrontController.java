package app;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import app.controller.DocumentoController;
import app.http.Request;
import app.http.Response;
import app.metadata.Controller;
import app.metadata.Path;

public class FrontController implements HttpHandler {

  private static class Pair {

    Class<?> controller;
    Map<String, Method> metodos;

  }

  private final HttpServer server;
  // documentos => DocumentosController
  private Map<String, Pair> routing = new HashMap<>();


  public FrontController(HttpServer server) {
    this.server = server;
    this.server.createContext("/", this);
  }

  @Override // TODO: roteamento
  public void handle(HttpExchange exchange) throws IOException {
    //   /teste => ["", "teste"]
    //   /documentos/listar => ["", "documentos", "listar"]
    String path = exchange.getRequestURI().getPath()
      .substring(1);
    List<String> split = Arrays.asList(path.split("/"));

    if (split.size() < 2) {
      exchange.sendResponseHeaders(404, 0);
      try (OutputStream out = exchange.getResponseBody()) {
        out.write(new byte[] {});
      }
    } else {
      String rota = split.get(0); // documentos
      String metodo = split.get(1); // listar

      Pair pair = routing.get(rota);
      if (pair == null) {
        exchange.sendResponseHeaders(404, 0);
      } else {
        Method m = pair.metodos.get(metodo); // listar?
        if (m == null) {
          exchange.sendResponseHeaders(404, 0);
        } else {

          try {
            Object objeto = pair.controller
                  .getDeclaredConstructor()
                  .newInstance(); // new DocumentoController
            Request req = new Request();
            Response resp = (Response) m.invoke(objeto, req);
            String body = resp.getBody();
            int status = resp.getStatus();
            exchange.sendResponseHeaders(status, body.length());
            exchange.getResponseBody().write(body.getBytes());
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
  }

  public void register(Class<?> clazz) {

    if (clazz.isAnnotationPresent(Controller.class)) {

      String rota = clazz.getAnnotation(Controller.class).value();
      
      Map<String, Method> metodos = new HashMap<>();
      
      for (Method m : clazz.getMethods()) {
        if (m.isAnnotationPresent(Path.class)) {
          metodos.put(
            m.getDeclaredAnnotation(Path.class).value(),
            m); // "listar" => Método lista()
        }
      }

      Pair pair = new Pair();
      pair.controller = clazz;
      pair.metodos = metodos;

      // "documentos"=>DocumentoController
      //       // "listar" => método lista()
      this.routing.put(rota, pair);
    }
  }
}
