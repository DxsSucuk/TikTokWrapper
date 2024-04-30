package de.presti.wrapper.tiktok.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.presti.wrapper.tiktok.exceptions.MissingDataInfoException;
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
public class TikTokUser {

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
    TikTokThumbnail avatar;

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
    List<TikTokVideo> posts = new ArrayList<>();

    public static TikTokUser fromScraper(JsonObject jsonObject, boolean parseVideos) {
        if (jsonObject == null) throw new MissingDataInfoException("Received null JsonObject for User!");

        TikTokUser tiktokUser = new TikTokUser();

        if (jsonObject.has("webapp.user-detail")) {
            JsonObject userDetail = jsonObject.getAsJsonObject("webapp.user-detail");
            if (userDetail.has("statusCode") && userDetail.getAsJsonPrimitive("statusCode").getAsInt() != 0) {
                throw new MissingDataInfoException("User not found");
            }

            if (userDetail.has("userInfo")) {
                JsonObject userInfo = userDetail.getAsJsonObject("userInfo");

                if (userInfo.has("user")) {
                    JsonObject user = userInfo.getAsJsonObject("user");
                    tiktokUser.id = user.has("id") ? user.getAsJsonPrimitive("id").getAsString() : "";
                    tiktokUser.displayName = user.has("nickname") ? user.getAsJsonPrimitive("nickname").getAsString() : "";
                    tiktokUser.name = user.has("uniqueId") ? user.getAsJsonPrimitive("uniqueId").getAsString() : "";
                    tiktokUser.bio = user.has("signature") ? user.getAsJsonPrimitive("signature").getAsString() : "";
                    tiktokUser.avatar = user.has("avatarLarger") ?
                            new TikTokThumbnail(user.getAsJsonPrimitive("avatarLarger").getAsString(),
                                    user.getAsJsonPrimitive("avatarMedium").getAsString(),
                                    user.getAsJsonPrimitive("avatarThumb").getAsString()) :
                            null;
                    tiktokUser.verified = user.has("verified") && user.getAsJsonPrimitive("verified").getAsBoolean();
                    tiktokUser.region = user.has("region") ? user.getAsJsonPrimitive("region").getAsString() : "";
                    tiktokUser.isPrivate = user.has("privateAccount") && user.getAsJsonPrimitive("privateAccount").getAsBoolean();
                    tiktokUser.secUID = user.has("secUid") ? user.getAsJsonPrimitive("secUid").getAsString() : "";
                }

                if (userInfo.has("stats")) {
                    JsonObject stats = userInfo.getAsJsonObject("stats");

                    tiktokUser.followers = stats.has("followerCount") ? stats.getAsJsonPrimitive("followerCount").getAsLong() : 0;
                    tiktokUser.following = stats.has("followingCount") ? stats.getAsJsonPrimitive("followingCount").getAsLong() : 0;
                    tiktokUser.likes = stats.has("heartCount") ? stats.getAsJsonPrimitive("heartCount").getAsLong() : 0;
                }


                if (userInfo.has("itemList") && parseVideos) {
                    JsonArray itemModule = userInfo.getAsJsonArray("itemList");

                    if (!itemModule.isEmpty()) {
                        for (JsonElement key : itemModule) {
                            tiktokUser.posts.add(TikTokVideo.fromScraper(key.getAsJsonObject()));
                        }
                    }
                }
            } else {
                throw new MissingDataInfoException("User not found");
            }
        } else {
            throw new MissingDataInfoException("User not found");
        }

        return tiktokUser;
    }
}
