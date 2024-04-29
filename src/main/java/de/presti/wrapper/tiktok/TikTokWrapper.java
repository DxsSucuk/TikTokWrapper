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
import java.util.List;

/**
 * The Wrapper to retrieve information from TikTok.
 */
@Getter
public class TikTokWrapper {

    public static TikTokUser getUser(String name) {
        return null;
    }

    public static TikTokUser getUser(long id) {
        return null;
    }

    public static TikTokUser getUser(String name, boolean parseVideos) {
        return null;
    }

    public static TikTokUser getUser(long id, boolean parseVideos) {
        return null;
    }

    public static TikTokVideo getVideo(String id) {
        return null;
    }


    public static TikTokVideo getVideos(long id) {
        return null;
    }

}
