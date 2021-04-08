import io.ebean.test.Json;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.assertj.core.api.Assertions;

public class TestClient {

    private final String apiBaseUrl;

    public TestClient(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    public static void assertResponseEqualToResource(Response<String> response, String expectedResource) {
        String expectedContent = Json.readResource("/" + expectedResource);
        Assertions.assertThat(response.getCode()).isEqualTo(200);
        Json.assertContains(response.getBody(), expectedContent);
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

    public String createAndAssertSequence(String testCaseName) {
        Response<String> response = postResource(testCaseName + "-def.json");
        TestClient.assertResponseEqualToResource(response, testCaseName + ".json");
        return Json.readNode(response.getBody()).get("id").asText();
    }

}
