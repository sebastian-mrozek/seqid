import io.ebean.test.Json;
import io.javalin.plugin.json.JavalinJson;
import io.sequenceserver.web.Application;
import io.sequenceservice.api.NumericSequence;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ApplicationTest {

    static final int PORT = 12345;
    static Application app;
    static TestClient testClient = new TestClient("http://localhost:" + PORT + "/sequence/");

    @BeforeAll
    public static void setup() {
        app = new Application();
        app.start(PORT);
    }

    @Test
    public void testCreateAndGet() {
        Response<String> response = testClient.postResource("new-sequence-1-def.json");
        TestUtil.assertResponse(response, 201, "new-sequence-1.json");
        String id = JavalinJson.fromJson(response.body, NumericSequence.class).getId();

        response = testClient.get(id);
        TestUtil.assertResponse(response, 200, "new-sequence-1.json");

        response = testClient.get("test1/my-sequence");
        TestUtil.assertResponse(response, 200, "new-sequence-1.json");
    }

    @Test
    public void testNext() {
        Response<String> response = testClient.postResource("new-sequence-2-def.json");
        String id = JavalinJson.fromJson(response.body, NumericSequence.class).getId();

        response = testClient.get(id + "/next");
        Assertions.assertThat(response.code).isEqualTo(200);
        Assertions.assertThat(response.body).isEqualTo("2");

        response = testClient.get("test2/your-sequence/next");
        Assertions.assertThat(response.code).isEqualTo(200);
        Assertions.assertThat(response.body).isEqualTo("3");
    }

    @Test
    public void testReset() {
        Response<String> response = testClient.postResource("new-sequence-3-def.json");
        String id = JavalinJson.fromJson(response.body, NumericSequence.class).getId();

        testClient.get(id + "/next");
        testClient.get(id + "/next");
        testClient.get(id + "/next");

        response = testClient.patch(id);
        TestUtil.assertResponse(response, 200,"new-sequence-3.json");
        Assertions.assertThat(response.code).isEqualTo(200);

        response = testClient.patchResource(id, "new-sequence-3-custom-def.json");
        TestUtil.assertResponse(response, 200,"new-sequence-3-custom.json");
        Assertions.assertThat(response.code).isEqualTo(200);
    }

    @Test
    public void testDelete() {
        Response<String> response = testClient.postResource("new-sequence-4-def.json");
        String id = JavalinJson.fromJson(response.body, NumericSequence.class).getId();

        response = testClient.delete(id);
        Assertions.assertThat(response.code).isEqualTo(204);

        response = testClient.get(id);
        Assertions.assertThat(response.code).isEqualTo(404);
    }

    @Test
    public void testCreateDuplicate() {

    }

    @Test
    public void testNotFound() {

    }

    @AfterAll
    public static void shutdown() {
        app.stop();
    }
}
