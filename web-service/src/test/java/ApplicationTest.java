import io.sequenceserver.web.Application;
import kong.unirest.HttpResponse;
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
    public void testNext() {
        String id = testClient.createAndAssertSequence("new-sequence-1");

        HttpResponse<String> response = testClient.get(id + "/next");
        Assertions.assertThat(response.getBody()).isEqualTo("10");

        response = testClient.get(id + "/next");
        Assertions.assertThat(response.getBody()).isEqualTo("11");
    }

    @Test
    public void testReset() {

    }

    @Test
    public void testDelete() {

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
