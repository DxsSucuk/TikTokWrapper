package de.presti.wrapper.tiktok.entities;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a TikTok User.
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * The ID of the User.
     */
    String id;

    /**
     * The Display Name of the User.
     */
    String displayName;

    /**
     * The Name of the User.
     */
    String name;

    /**
     * The Followers of the User.
     */
    long followers;

    /**
     * The Following of the User.
     */
    long following;

    /**
     * The Likes of the User.
     */
    long likes;

    /**
     * The Bio of the User.
     */
    String bio;

    /**
     * The Avatar of the User.
     */
    Thumbnails avatar;

    /**
     * Indicator if the User is verified.
     */
    boolean verified;

    /**
     * The Region of the User.
     */
    String region;

    /**
     * Indicator if the User is private.
     */
    boolean isPrivate;

    /**
     * The Second User ID of the User.
     */
    String secUID;

    /**
     * The Posts of the User.
     */
    List<Video> posts = new ArrayList<>();

    /**
     * Constructor for the User.
     * @param jsonObject The JsonObject to parse the User from.
     */
    public User(JsonObject jsonObject) {
        this(jsonObject, true);
    }

    /**
     * Constructor for the User.
     * @param jsonObject The JsonObject to parse the User from.
     * @param parseVideos If the Videos should also be parsed/loaded into the Object.
     */
    public User(JsonObject jsonObject, boolean parseVideos) {
        super();

        if (jsonObject.has("UserModule")) {
            JsonObject userModule = jsonObject.getAsJsonObject("UserModule");
            if (userModule.has("users")) {
                JsonObject user = userModule.getAsJsonObject("users");

                if (user.size() == 1) {
                    user = user.getAsJsonObject(user.keySet().iterator().next());
                    id = user.has("id") ? user.getAsJsonPrimitive("id").getAsString() : "";
                    displayName = user.has("nickname") ? user.getAsJsonPrimitive("nickname").getAsString() : "";
                    name = user.has("uniqueId") ? user.getAsJsonPrimitive("uniqueId").getAsString() : "";
                    bio = user.has("signature") ? user.getAsJsonPrimitive("signature").getAsString() : "";
                    avatar = user.has("avatarLarger") ?
                            new Thumbnails(user.getAsJsonPrimitive("avatarLarger").getAsString(),
                                    user.getAsJsonPrimitive("avatarMedium").getAsString(),
                                    user.getAsJsonPrimitive("avatarThumb").getAsString()) :
                            null;
                    verified = user.has("verified") && user.getAsJsonPrimitive("verified").getAsBoolean();
                    region = user.has("region") ? user.getAsJsonPrimitive("region").getAsString() : "";
                    isPrivate = user.has("privateAccount") && user.getAsJsonPrimitive("privateAccount").getAsBoolean();
                    secUID = user.has("secUid") ? user.getAsJsonPrimitive("secUid").getAsString() : "";
                }
            }

            if (userModule.has("stats")) {
                JsonObject stats = userModule.getAsJsonObject("stats");

                if (stats.size() == 1) {
                    stats = stats.getAsJsonObject(stats.keySet().iterator().next());
                    followers = stats.has("followerCount") ? stats.getAsJsonPrimitive("followerCount").getAsLong() : 0;
                    following = stats.has("followingCount") ? stats.getAsJsonPrimitive("followingCount").getAsLong() : 0;
                    likes = stats.has("heartCount") ? stats.getAsJsonPrimitive("heartCount").getAsLong() : 0;
                }
            }
        }

        if (jsonObject.has("ItemModule") && parseVideos) {
            JsonObject itemModule = jsonObject.getAsJsonObject("ItemModule");

            if (itemModule.isEmpty()) return;

            for (String key : itemModule.keySet()) {
                posts.add(new Video(itemModule.getAsJsonObject(key)));
            }
        }
    }
}
