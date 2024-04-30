package de.presti.wrapper.tiktok.entities;

import com.google.gson.JsonObject;
import de.presti.wrapper.tiktok.exceptions.MissingDataInfoException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents a TikTok Video.
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TikTokVideo {

    /**
     * The ID of the Video.
     */
    String id;

    /**
     * The Description of the Video.
     */
    String description;

    /**
     * The Author of the Video.
     */
    String author;

    /**
     * The Creation Time of the Video.
     */
    long creationTime;

    /**
     * Unsure what this is, but it's always false.
     */
    boolean original;

    /**
     * If the Video is officially posted by TikTok or distributed.
     */
    boolean official;

    /**
     * If the Video is a secret.
     */
    boolean secret;

    /**
     * If the Video is for friends only.
     */
    boolean forFriend;

    /**
     * If the Video is private.
     */
    boolean isPrivate;

    /**
     * If the Video can be used for Duets.
     */
    boolean canDuet;

    /**
     * If the Video can be used for Stitches.
     */
    boolean canStitch;

    /**
     * If the Video can be shared.
     */
    boolean canShare;

    /**
     * The Location of the Video.
     */
    String location;

    /**
     * The amount of Views of the Video.
     */
    long playCount;

    /**
     * The amount of Likes of the Video.
     */
    long likeCount;

    /**
     * The amount of Shares of the Video.
     */
    long shareCount;

    /**
     * The amount of Comments of the Video.
     */
    long commentCount;

    /**
     * The duration of the Video.
     */
    long duration;

    /**
     * The resolution/ratio of the Video.
     */
    String resolution;

    /**
     * The covers of the Video.
     */
    TikTokThumbnail cover;

    public static TikTokVideo fromScraper(JsonObject item) {
        if (item == null) {
            throw new MissingDataInfoException("Received null JsonObject for Video!");
        }

        TikTokVideo tikTokVideo = new TikTokVideo();

        if (item.has("webapp.video-detail")) {
            item = item.getAsJsonObject("webapp.video-detail");
            if (item.has("statusCode") && item.getAsJsonPrimitive("statusCode").getAsInt() != 0) {
                throw new MissingDataInfoException("Video not found");
            }

            if (item.has("itemInfo")) {
                item = item.getAsJsonObject("itemInfo");

                if (item.has("itemStruct")) {
                    item = item.getAsJsonObject("itemStruct");

                    tikTokVideo.parseFromStructure(item);
                }
            } else {
                throw new MissingDataInfoException("Video not found");
            }
        }
        return tikTokVideo;
    }

    private void parseFromStructure(JsonObject item) {
        id = item.has("id") ? item.getAsJsonPrimitive("id").getAsString() : "";

        description = item.has("desc") ? item.getAsJsonPrimitive("desc").getAsString() : "";
        if (item.has("author")) {
            author = item.getAsJsonObject("author").has("uniqueId") ? item.getAsJsonObject("author").getAsJsonPrimitive("uniqueId").getAsString() : "";
        }

        creationTime = item.has("createTime") ? item.getAsJsonPrimitive("createTime").getAsLong() : 0;

        original = item.has("originalItem") && item.getAsJsonPrimitive("originalItem").getAsBoolean();
        official = item.has("officialItem") && item.getAsJsonPrimitive("officialItem").getAsBoolean();
        secret = item.has("secret") && item.getAsJsonPrimitive("secret").getAsBoolean();
        forFriend = item.has("forFriend") && item.getAsJsonPrimitive("forFriend").getAsBoolean();
        isPrivate = item.has("privateItem") && item.getAsJsonPrimitive("privateItem").getAsBoolean();
        canDuet = item.has("duetEnabled") && item.getAsJsonPrimitive("duetEnabled").getAsBoolean();
        canStitch = item.has("stitchEnabled") && item.getAsJsonPrimitive("stitchEnabled").getAsBoolean();
        canShare = item.has("shareEnabled") && item.getAsJsonPrimitive("shareEnabled").getAsBoolean();
        location = item.has("locationCreated") ? item.getAsJsonPrimitive("locationCreated").getAsString() : "";

        if (item.has("stats")) {
            JsonObject itemStats = item.getAsJsonObject("stats");
            playCount = itemStats.has("playCount") ? itemStats.getAsJsonPrimitive("playCount").getAsLong() : 0;
            shareCount = itemStats.has("shareCount") ? itemStats.getAsJsonPrimitive("shareCount").getAsLong() : 0;
            commentCount = itemStats.has("commentCount") ? itemStats.getAsJsonPrimitive("commentCount").getAsLong() : 0;
            likeCount = itemStats.has("diggCount") ? itemStats.getAsJsonPrimitive("diggCount").getAsLong() : 0;
        }

        if (item.has("video")) {
            JsonObject itemVideo = item.getAsJsonObject("video");
            duration = itemVideo.has("duration") ? itemVideo.getAsJsonPrimitive("duration").getAsLong() : 0;
            resolution = itemVideo.has("ratio") ? itemVideo.getAsJsonPrimitive("ratio").getAsString() : "";

            if (itemVideo.has("cover")) {
                cover = new TikTokThumbnail(itemVideo.getAsJsonPrimitive("cover").getAsString(),
                        itemVideo.getAsJsonPrimitive("originCover").getAsString(),
                        itemVideo.getAsJsonPrimitive("dynamicCover").getAsString());
            }
        }
    }
}
