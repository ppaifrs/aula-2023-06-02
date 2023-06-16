package app.controller;

import app.http.Request;
import app.http.Response;
import app.metadata.Controller;
import app.metadata.Path;

// /documentos/listar

@Controller("documentos") // metadado
public class DocumentoController {

  @Path("listar")
  public Response lista(Request request) {
    // TODO: request.getParam("ordem"); // ?ordem=nome
    return Response.of(200, "{a: 1, b: 2}");
  }
  
}
