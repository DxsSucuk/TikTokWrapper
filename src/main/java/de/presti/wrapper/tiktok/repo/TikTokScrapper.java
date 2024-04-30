package de.presti.wrapper.tiktok.repo;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.presti.wrapper.tiktok.entities.TikTokUser;
import de.presti.wrapper.tiktok.entities.TikTokVideo;
import de.presti.wrapper.tiktok.exceptions.MissingDataInfoException;
import de.presti.wrapper.tiktok.utility.RequestUtility;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

/**
 * The Scrapper to retrieve information from TikTok.
 */
public class TikTokScrapper {

    /**
     * The ID of the element which contains all the information.
     */
    static String elementId = "__UNIVERSAL_DATA_FOR_REHYDRATION__";

    /**
     * The ID of the element which contains the list of videos.
     */
    static String videoListElementId = "user-post-item-list";

    /**
     * The Base Url of the TikTok Website.
     */
    static String baseUrl = "https://www.tiktok.com/";

    /**
     * Retrieve information about a User.
     *
     * @param name        The name of the user.
     * @param parseVideos If the videos of the user should be loaded into the object (takes a lot of time if the users have a lot of them).
     * @return The User.
     * @throws IOException If the connection to the website fails.
     */
    public static TikTokUser getUser(String name, boolean parseVideos) throws IOException {
        if (!name.startsWith("@")) {
            name = "@" + name;
        }

        return TikTokUser.fromScraper(parseElement(name), parseVideos);
    }

    /**
     * Retrieve information about a User and load their videos.
     *
     * @param name The name of the user.
     * @return The User.
     * @throws IOException If the connection to the website fails.
     */
    public static TikTokUser getUser(String name) throws IOException {
        return getUser(name, true);
    }

    /**
     * Retrieve information about a User.
     *
     * @param id          The ID of the user.
     * @param parseVideos If the videos of the user should be loaded into the object (takes a lot of time, if the users has a lot of them).
     * @return The User.
     * @throws IOException If the connection to the website fails.
     */
    public static TikTokUser getUser(long id, boolean parseVideos) throws IOException {
        return TikTokUser.fromScraper(parseElement("share/user/" + id), parseVideos);
    }

    /**
     * Retrieve information about a User and load their videos.
     *
     * @param id The ID of the user.
     * @return The User.
     * @throws IOException If the connection to the website fails.
     */
    public static TikTokUser getUser(long id) throws IOException {
        return getUser(id, true);
    }

    public static List<TikTokVideo> getVideos(String name) throws IOException {
        if (!name.startsWith("@")) {
            name = "@" + name;
        }

        return parseVideos(name);
    }

    public static List<TikTokVideo> getVideos(long id) throws IOException {
        return parseVideos("share/user/" + id);
    }

    private static List<TikTokVideo> parseVideos(String path) throws IOException {
        Connection connection = Jsoup.connect(baseUrl + path + "?lang=en");
        Document document = connection.get();
        Element data = document.getElementsByAttributeValue("data-e2e", videoListElementId).first();

        if (data == null) {
            throw new MissingDataInfoException("Page does not contain the " + videoListElementId + " element! Maybe updated their website? Open a Issue if this continues!");
        }

        return null;
    }

    /**
     * Retrieve information about a Video.
     *
     * @param user The name of the user.
     * @param id   The ID of the video.
     * @return The Video.
     * @throws IOException If the connection to the website fails.
     */
    public static TikTokVideo getVideo(String user, String id) throws IOException {
        if (!user.startsWith("@")) {
            user = "@" + user;
        }

        return TikTokVideo.fromScraper(parseElement(user + "/video/" + id));
    }

    /**
     * Retrieve information about a Video.
     *
     * @param id The ID of the video.
     * @return The Video.
     * @throws IOException If the connection to the website fails.
     */
    public static TikTokVideo getVideo(String id) throws IOException {
        return TikTokVideo.fromScraper(parseElement("share/video/" + id));
    }

    /**
     * Extract results from the Website into a JsonObject usable.
     *
     * @param path The path related to the wanted Object.
     * @return The JsonObject.
     */
    private static JsonObject parseElement(String path) {
        RequestUtility.Request request = RequestUtility.Request.builder()
                .url(baseUrl + path + "?lang=en")
                .GET()
                .build();

        Document document = RequestUtility.request(request);

        if (document == null) {
            throw new MissingDataInfoException("Couldn't retrieve the data from the website!");
        }

        Element data = document.getElementById(elementId);

        if (data == null) {
            throw new MissingDataInfoException("Page does not contain the " + elementId + " element! Maybe updated their website? Open a Issue if this continues!");
        }

        JsonObject jsonObject = JsonParser.parseString(data.data()).getAsJsonObject();

        if (jsonObject.has("__DEFAULT_SCOPE__")) {
            jsonObject = jsonObject.getAsJsonObject("__DEFAULT_SCOPE__");

            // Remove unnecessary data
            jsonObject.remove("webapp.app-context");
            jsonObject.remove("webapp.biz-context");
            jsonObject.remove("webapp.i18n-translation");
            jsonObject.remove("webapp.a-b");
            jsonObject.remove("seo.abtest");
        }

        return jsonObject;
    }
}
