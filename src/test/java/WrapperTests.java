import de.presti.wrapper.tiktok.TikTokWrapper;
import de.presti.wrapper.tiktok.entities.User;

import java.io.IOException;

public class WrapperTests {

    public static void main(String[] args) throws IOException {
        User user = TikTokWrapper.getUser("ree6bot");
        User shareUser = TikTokWrapper.getUser(Long.parseLong(user.getId()));
    }

}
