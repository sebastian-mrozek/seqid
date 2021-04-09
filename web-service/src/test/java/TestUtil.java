import io.ebean.test.Json;
import org.assertj.core.api.Assertions;

public class TestUtil {

    public static void assertResponse(Response<String> actualResponse, int expectedCode, String expectedResource) {
        String expectedContent = Json.readResource("/" + expectedResource);
        Assertions.assertThat(actualResponse.code).isEqualTo(expectedCode);
        Json.assertContains(actualResponse.body, expectedContent);
    }
}
