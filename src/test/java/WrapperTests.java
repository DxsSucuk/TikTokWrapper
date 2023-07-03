import de.presti.wrapper.tiktok.TikTokWrapper;
import de.presti.wrapper.tiktok.entities.TikTokUser;

import java.io.IOException;

public class WrapperTests {

    public static void main(String[] args) throws IOException {
        TikTokUser user = TikTokWrapper.getUser("ree6bot");
        TikTokUser shareUser = TikTokWrapper.getUser(Long.parseLong(user.getId()));
    }

}
