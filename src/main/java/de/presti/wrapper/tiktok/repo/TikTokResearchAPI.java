package de.presti.wrapper.tiktok.repo;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.presti.wrapper.tiktok.entities.TikTokUser;
import de.presti.wrapper.tiktok.entities.TikTokVideo;
import de.presti.wrapper.tiktok.exceptions.MissingDataInfoException;
import de.presti.wrapper.tiktok.utility.RequestUtility;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * The API to retrieve information from TikTok.
 */
@NoArgsConstructor
public class TikTokResearchAPI {

    private final String BASE_URL = "https://open.tiktokapis.com/v2/research";
    private final String OAUTH_URL = "https://open.tiktokapis.com/v2/oauth/token/";

    @Setter
    private String client_key;

    @Setter
    private String client_secret;

    @Setter
    private String grant_type;

    @Getter
    private String access_token;

    @Getter
    private long expires;

    public TikTokResearchAPI(String client_key, String client_secret, String grant_type) {
        this.client_key = client_key;
        this.client_secret = client_secret;
        this.grant_type = grant_type;
    }

    private void checkAccessToken() throws UnsupportedEncodingException {
        if (expires < System.currentTimeMillis()) {
            requestAccessToken();
        }
    }

    public void requestAccessToken() throws UnsupportedEncodingException {
        RequestUtility.Request request = RequestUtility.Request.builder()
                .url(OAUTH_URL)
                .body("client_key=" + URLEncoder.encode(client_key, StandardCharsets.UTF_8.name()) + "&client_secret=" + URLEncoder.encode(client_secret, StandardCharsets.UTF_8.name()) + "&grant_type=" + URLEncoder.encode(grant_type, StandardCharsets.UTF_8.name()))
                .header(new String[]{"Content-Type", "application/x-www-form-urlencoded"})
                .POST()
                .build();
        JsonElement jsonElement = RequestUtility.requestJson(request);
        if (!jsonElement.isJsonNull()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("error") || (jsonObject.has("success") && !jsonObject.getAsJsonPrimitive("success").getAsBoolean())) {
                throw new MissingDataInfoException("Couldn't get the Access Token!");
            }

            access_token = jsonObject.getAsJsonPrimitive("access_token").getAsString();
            expires = System.currentTimeMillis() + Duration.ofSeconds(jsonObject.getAsJsonPrimitive("expires_in").getAsInt()).toMillis();
        }
    }

    public TikTokUser getUser(String username) throws UnsupportedEncodingException {
        checkAccessToken();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("username", username);

        RequestUtility.Request request = RequestUtility.Request.builder()
                .url(BASE_URL + "/user/info/?fields=display_name,bio_description,avatar_url,is_verified,follower_count,following_count,likes_count,video_count")
                .bearerAuth(access_token)
                .header(new String[]{"Content-Type", "text/plain"})
                .body(requestObject.toString())
                .POST()
                .build();

        JsonElement jsonElement = RequestUtility.requestJson(request);

        if (!jsonElement.isJsonNull()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("error") || (jsonObject.has("success") && !jsonObject.getAsJsonPrimitive("success").getAsBoolean())) {
                throw new MissingDataInfoException("Couldn't get the User!");
            }

            return null;
        }
        return null;
    }

    public List<TikTokVideo> getVideos(String username) throws UnsupportedEncodingException {
        checkAccessToken();

        JsonObject requestObject = getUserQueryObject(username);

        RequestUtility.Request request = RequestUtility.Request.builder()
                .url(BASE_URL + "/video/query/?fields=display_name,bio_description,avatar_url,is_verified,follower_count,following_count,likes_count,video_count")
                .bearerAuth(access_token)
                .body(requestObject.getAsString())
                .POST()
                .build();

        JsonElement jsonElement = RequestUtility.requestJson(request);

        if (!jsonElement.isJsonNull()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("error") || (jsonObject.has("success") && !jsonObject.getAsJsonPrimitive("success").getAsBoolean())) {
                throw new MissingDataInfoException("Couldn't get the Videos!");
            }

            return Collections.emptyList();
        }
        return Collections.emptyList();
    }

    private static JsonObject getUserQueryObject(String username) {
        JsonObject requestObject = new JsonObject();
        JsonObject queryObject = new JsonObject();
        JsonArray andObject = new JsonArray();
        JsonObject operationObject = new JsonObject();

        JsonArray fieldValues = new JsonArray();
        fieldValues.add(username);

        operationObject.addProperty("operation", "EQ");
        operationObject.addProperty("field_name", "username");
        operationObject.add("field_values", fieldValues);

        andObject.add(operationObject);
        queryObject.add("and", andObject);

        requestObject.addProperty("query", username);
        return requestObject;
    }

}
