package de.presti.wrapper.tiktok.utility;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility used to work with HTTP Requests.
 */
@Slf4j
public class RequestUtility {

    /**
     * User-Agent for all the Requests.
     */
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

    /**
     * Send a Request.
     *
     * @param request the Request.
     * @return an {@link Document}.
     */
    public static Document request(Request request) {
        Connection connection = Jsoup.connect(request.getUrl())
                .ignoreContentType(true)
                .userAgent(USER_AGENT);

        if (!request.getHeaders().isEmpty()) {
            for (String[] header : request.getHeaders()) {
                if (header.length == 2) {
                    connection.header(header[0], header[1]);
                }
            }
        }

        if (request.bearerAuth != null) {
            connection.header("Authorization", "bearer " + request.getBearerAuth());
        }

        Document document;
        try {

            if (request.getMethod() == Method.GET) {
                document = connection.get();
            } else {
                document = connection.requestBody(request.getBody()).post();
            }

            return document;
        } catch (Exception ex) {
            log.error("Couldn't send a Request!", ex);
        }

        return null;
    }

    /**
     * Send a Request.
     *
     * @param request the Request.
     * @return an {@link JsonElement}.
     */
    public static JsonElement requestJson(Request request) {
        JsonElement jsonObject = new JsonObject();


        if (request.getHeaders().isEmpty()) {
            request.getHeaders().add(new String[]{"Content-Type", "application/json"});
        }

        Document doc = request(request);

        if (doc == null) {
            jsonObject.getAsJsonObject().addProperty("success", false);
            return jsonObject;
        }

        String content = doc.body().text();

        try {
            JsonStreamParser jsonStreamParser = new JsonStreamParser(content);

            if (jsonStreamParser.hasNext()) {
                jsonObject = jsonStreamParser.next();
            } else {
                jsonObject.getAsJsonObject().addProperty("success", false);
            }

            return jsonObject;
        } catch (Exception ex) {
            log.error("Couldn't send a Request!", ex);
            log.error("Content: {}", content);
        }

        return jsonObject;
    }

    /**
     * Utility class for Requests.
     */
    @Getter
    public static class Request {

        /**
         * The URL for the Request.
         */
        protected String url;

        /**
         * The Bearer Auth Token for the Request.
         */
        protected String bearerAuth;

        /**
         * The Request Method.
         */
        protected Method method = Method.GET;

        /**
         * The Body Publisher used for PUT and POST Requests.
         */
        protected String body;

        /**
         * Custom Headers.
         */
        protected List<String[]> headers = new ArrayList<>();

        /**
         * Create a new Request builder.
         *
         * @return a new Request builder.
         */
        public static RequestBuilder builder() {
            return new RequestBuilder();
        }

        /**
         * Get the URL as URI.
         *
         * @return the URL as {@link URI}
         */
        public URI getUri() {
            return URI.create(getUrl());
        }

        /**
         * Builder class for a Request class.
         */
        public static class RequestBuilder {

            /**
             * The URL for the Request.
             */
            protected String url;

            /**
             * The Bearer Auth Token for the Request.
             */
            protected String bearerAuth;

            /**
             * The Request Method.
             */
            protected Method method = Method.GET;

            /**
             * The Body Publisher used for PUT and POST Requests.
             */
            protected String body;

            /**
             * Custom Headers.
             */
            protected List<String[]> headers = new ArrayList<>();

            /**
             * Change the Url of the Request.
             *
             * @param url the new Url.
             * @return the Request.
             */
            public RequestBuilder url(String url) {
                this.url = url;
                return this;
            }

            /**
             * Change the Bearer Auth Token.
             *
             * @param bearerAuth the new Auth Token.
             * @return the Request.
             */
            public RequestBuilder bearerAuth(String bearerAuth) {
                this.bearerAuth = bearerAuth;
                return this;
            }

            /**
             * Change the Request method.
             *
             * @param method the new Method.
             * @return the Request.
             */
            public RequestBuilder method(Method method) {
                this.method = method;
                return this;
            }

            /**
             * Change the Request method to GET.
             *
             * @return the RequestBuilder.
             */
            public RequestBuilder GET() {
                this.method = Method.GET;
                return this;
            }

            /**
             * Change the Request method to POST.
             *
             * @return the RequestBuilder.
             */
            public RequestBuilder POST() {
                this.method = Method.POST;
                return this;
            }

            /**
             * Change the Body publisher used.
             *
             * @param body the Body used for PUT and POST Requests.
             * @return the Request.
             */
            public RequestBuilder body(String body) {
                this.body = body;
                return this;
            }

            /**
             * Change the Headers.
             *
             * @param header the new Header.
             * @return the Request.
             */
            public RequestBuilder header(String[] header) {
                this.headers.add(header);
                return this;
            }

            /**
             * Build the Request.
             *
             * @return the Request.
             */
            public Request build() {
                Request request = new Request();
                request.url = this.url;
                request.method = this.method;
                request.body = this.body;
                request.headers = this.headers;
                request.bearerAuth = this.bearerAuth;
                return request;
            }
        }
    }

    /**
     * Supported Methods.
     */
    public enum Method {
        /**
         * The GET Method.
         */
        GET,

        /**
         * The POST-Method.
         */
        POST
    }

}