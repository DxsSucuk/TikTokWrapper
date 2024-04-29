import de.presti.wrapper.tiktok.TikTokWrapper;
import de.presti.wrapper.tiktok.exceptions.MissingDataInfoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WrapperTests {

    @Test
    void failingUserLookup() {
        Assertions.assertThrows(MissingDataInfoException.class, () -> TikTokWrapper.getUser("watgawgawhgawhz26125623q6t"));;
    }

    @Test
    void successfulUserLookup() {
        Assertions.assertDoesNotThrow(() -> TikTokWrapper.getUser("ree6bot"));
    }

    @Test
    void successfulUserLookupWithId() {
        Assertions.assertDoesNotThrow(() -> TikTokWrapper.getUser(7130536971978392581L));
    }

    @Test
    void successfulUserLookupWithVideos() {
        Assertions.assertDoesNotThrow(() -> TikTokWrapper.getUser("ree6bot", true));
    }

    @Test
    void successfulUserLookupWithIdAndVideos() {
        Assertions.assertDoesNotThrow(() -> TikTokWrapper.getUser(7130536971978392581L, true));
    }

    @Test
    void successfulVideoLookup() {
        Assertions.assertDoesNotThrow(() -> TikTokWrapper.getVideo("7152957515582426373"));
    }

    @Test
    void successfulVideoCatalogLookup() {
        Assertions.assertDoesNotThrow(() -> TikTokWrapper.getVideos(7130536971978392581L));
    }

}
