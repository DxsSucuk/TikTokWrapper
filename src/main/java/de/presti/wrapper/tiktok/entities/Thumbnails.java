package de.presti.wrapper.tiktok.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents the Thumbnails of a TikTok Object.
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Thumbnails {

    /**
     * The Url of the small Thumbnail.
     */
    String smallUrl;

    /**
     * The Url of the medium Thumbnail.
     */
    String mediumUrl;

    /**
     * The Url of the large Thumbnail.
     */
    String largeUrl;
}
