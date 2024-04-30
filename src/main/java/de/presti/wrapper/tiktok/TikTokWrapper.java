package de.presti.wrapper.tiktok;

import de.presti.wrapper.tiktok.entities.TikTokUser;
import de.presti.wrapper.tiktok.entities.TikTokVideo;
import de.presti.wrapper.tiktok.repo.TikTokResearchAPI;
import de.presti.wrapper.tiktok.repo.TikTokScrapper;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * The Wrapper to retrieve information from TikTok.
 */
@Getter
public class TikTokWrapper {

    @Setter
    protected static TikTokResearchAPI tikTokResearchAPI;

    /**
     * Retrieve information about a User.
     * @param name The name of the user.
     * @return The User.
     * @throws IOException If the connection to the website fails.
     */
    public static TikTokUser getUser(String name) throws IOException {
        if (tikTokResearchAPI == null)
            return TikTokScrapper.getUser(name);

        return tikTokResearchAPI.getUser(name);
    }

    /**
     * Retrieve information about a User.
     * @param id The ID of the user.
     * @return The User.
     * @throws IOException If the connection to the website fails.
     */
    public static TikTokUser getUser(long id) throws IOException {
        if (tikTokResearchAPI == null)
            return TikTokScrapper.getUser(id);

        return tikTokResearchAPI.getUser(String.valueOf(id));
    }

    /**
     * Retrieve information about a User.
     * @param name The name of the user.
     * @param parseVideos If the videos of the user should be loaded into the object (takes a lot of time if the users have a lot of them).
     * @return The User.
     * @throws IOException If the connection to the website fails.
     */
    public static TikTokUser getUser(String name, boolean parseVideos) throws IOException {
        if (tikTokResearchAPI == null)
            return TikTokScrapper.getUser(name, parseVideos);

        return tikTokResearchAPI.getUser(name);
    }

    /**
     * Retrieve information about a User.
     * @param id The ID of the user.
     * @param parseVideos If the videos of the user should be loaded into the object (takes a lot of time, if the users has a lot of them).
     * @return The User.
     * @throws IOException If the connection to the website fails.
     */
    public static TikTokUser getUser(long id, boolean parseVideos) throws IOException {
        if (tikTokResearchAPI == null)
            return TikTokScrapper.getUser(id, parseVideos);

        return tikTokResearchAPI.getUser(String.valueOf(id));
    }

    /**
     * Retrieve information about a Video.
     * @param id The ID of the Video.
     * @return The Video.
     * @throws IOException If the connection to the website fails.
     */
    public static TikTokVideo getVideo(String id) throws IOException {
        if (tikTokResearchAPI == null)
            return TikTokScrapper.getVideo(id);

        return null;
    }

    /**
     * Retrieve information about a Video.
     * @param id The ID of the Video.
     * @return The Video.
     * @throws IOException If the connection to the website fails.
     */
    public static TikTokVideo getVideos(long id) throws IOException {
        if (tikTokResearchAPI == null)
            return TikTokScrapper.getVideo(String.valueOf(id));

        return null;
    }

}
