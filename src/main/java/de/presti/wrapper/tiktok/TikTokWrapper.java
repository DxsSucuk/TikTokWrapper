package de.presti.wrapper.tiktok;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.presti.wrapper.tiktok.entities.TikTokUser;
import de.presti.wrapper.tiktok.entities.TikTokVideo;
import de.presti.wrapper.tiktok.exceptions.MissingDataInfoException;
import lombok.Getter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * The Wrapper to retrieve information from TikTok.
 */
@Getter
public class TikTokWrapper {

    /**
     * The ID of the element which contains all the information.
     */
    static String elementId = "SIGI_STATE";

    /**
     * The Base Url of the TikTok Website.
     */
    static String baseUrl = "https://www.tiktok.com/";

    /**
     * Retrieve information about a User.
     * @param name The name of the user.
     * @param parseVideos If the videos of the user should be loaded into the object (takes a lot of time, if the users has a lot of them).
     * @return The User.
     * @throws IOException If the connection to the website fails.
     */
    public static TikTokUser getUser(String name, boolean parseVideos) throws IOException {
        if (!name.startsWith("@")) {
            name = "@" + name;
        }

        return new TikTokUser(parseElement(name), parseVideos);
    }

    /**
     * Retrieve information about a User and load their videos.
     * @param name The name of the user.
     * @return The User.
     * @throws IOException If the connection to the website fails.
     */
    public static TikTokUser getUser(String name) throws IOException {
        return getUser(name, true);
    }

    /**
     * Retrieve information about a User.
     * @param id The ID of the user.
     * @param parseVideos If the videos of the user should be loaded into the object (takes a lot of time, if the users has a lot of them).
     * @return The User.
     * @throws IOException If the connection to the website fails.
     */
    public static TikTokUser getUser(long id, boolean parseVideos) throws IOException {
        return new TikTokUser(parseElement("share/user/" + id), parseVideos);
    }

    /**
     * Retrieve information about a User and load their videos.
     * @param id The ID of the user.
     * @return The User.
     * @throws IOException If the connection to the website fails.
     */
    public static TikTokUser getUser(long id) throws IOException {
        return getUser(id, true);
    }

    /**
     * Retrieve information about a Video.
     * @param user The name of the user.
     * @param id The ID of the video.
     * @return The Video.
     * @throws IOException If the connection to the website fails.
     */
    public static TikTokVideo getVideo(String user, String id) throws IOException {
        if (!user.startsWith("@")) {
            user = "@" + user;
        }

        return new TikTokVideo(parseElement(user + "/video/" + id));
    }

    /**
     * Retrieve information about a Video.
     * @param id The ID of the video.
     * @return The Video.
     * @throws IOException If the connection to the website fails.
     */
    public static TikTokVideo getVideo(String id) throws IOException {
        return new TikTokVideo(parseElement("share/video/" + id));
    }

    /**
     * Extract results from the Website into a JsonObject usable.
     * @param path The path related to the wanted Object.
     * @return The JsonObject.
     * @throws IOException If the connection to the website fails.
     */
    private static JsonObject parseElement(String path) throws IOException {
        Connection connection = Jsoup.connect(baseUrl + path + "?lang=en");
        Document document = connection.get();
        Element data = document.getElementById(elementId);

        if (data == null) {
            throw new MissingDataInfoException("Page does not contain the " + elementId + " element! Maybe updated their website? Open a Issue if this continues!");
        }

        return JsonParser.parseString(data.data()).getAsJsonObject();
    }
}
