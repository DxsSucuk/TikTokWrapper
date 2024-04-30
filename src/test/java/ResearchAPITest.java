import de.presti.wrapper.tiktok.repo.TikTokResearchAPI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ResearchAPITest {

    private static TikTokResearchAPI api;

    @BeforeAll
    public static void setup() {
        api = new TikTokResearchAPI("awd96yorysodw206", "g8B7QAFqVKwxYGa3ejvleBoNhrr0wQqi", "client_credentials");
    }

    @Test
    public void getUserInfo() {
        Assertions.assertDoesNotThrow(() -> api.getUser("ree6bot"));
    }

}
