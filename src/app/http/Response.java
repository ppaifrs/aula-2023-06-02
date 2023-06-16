package app.http;

public class Response { // Value Object

  public static Response of(int status, String body) {
    return new Response(status, body);
  }

  private final String body;
  private final int status;

  private Response(int status, String body) {
    this.body = body;
    this.status = status;
  }

  public String getBody() {
    return body;
  }

  public int getStatus() {
    return status;
  }
}
