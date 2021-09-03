import io.ebean.test.Json;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class TestClient {

    private final String apiBaseUrl;

    public TestClient(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    public Response<String> postResource(String resourceName) {
        String content = Json.readResource("/" + resourceName);
        HttpResponse<String> httpResponse = Unirest.post(apiBaseUrl).body(content).asString();
        return new Response<>(httpResponse.getStatus(), httpResponse.getBody());
    }

    public Response<String> get(String path) {
        HttpResponse<String> httpResponse = Unirest.get(apiBaseUrl + path).asString();
        return new Response<>(httpResponse.getStatus(), httpResponse.getBody());
    }

    public Response<String> patch(String path) {
        HttpResponse<String> httpResponse = Unirest.patch(apiBaseUrl + path).asString();
        return new Response<>(httpResponse.getStatus(), httpResponse.getBody());
    }

    public Response<String> patchResource(String path, String resourceName) {
        String content = Json.readResource("/" + resourceName);
        HttpResponse<String> httpResponse = Unirest.patch(apiBaseUrl + path).body(content).asString();
        return new Response<>(httpResponse.getStatus(), httpResponse.getBody());
    }

    public Response<String> delete(String path) {
        HttpResponse<String> httpResponse = Unirest.delete(apiBaseUrl + path).asString();
        return new Response<>(httpResponse.getStatus(), httpResponse.getBody());
    }

    public Response<String> get() {
        return get("");
    }
}
