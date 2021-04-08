import io.ebean.test.Json;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class TestClient {

    private final String apiBaseUrl;

    public TestClient(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    public static void assertResponseEqualToResource(HttpResponse<String> response, String expectedResource) {
        String expectedContent = Json.readResource("/" + expectedResource);
        Json.assertContains(response.getBody(), expectedContent);
    }


    public HttpResponse<String> postResource(String resourceName) {
        String content = Json.readResource("/" + resourceName);
        return Unirest.post(apiBaseUrl).body(content).asString();
    }

    public HttpResponse<String> get(String path) {
        return Unirest.get(apiBaseUrl + path).asString();
    }

    public String createAndAssertSequence(String testCaseName) {
        HttpResponse<String> response = postResource(testCaseName + "-def.json");
        TestClient.assertResponseEqualToResource(response, testCaseName + ".json");
        return Json.readNode(response.getBody()).get("id").asText();
    }

}
