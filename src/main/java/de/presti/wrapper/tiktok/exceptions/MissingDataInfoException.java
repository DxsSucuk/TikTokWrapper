package de.presti.wrapper.tiktok.exceptions;

import lombok.NoArgsConstructor;

/**
 * Thrown when the data couldn't be extracted from the Website.
 */
@NoArgsConstructor
public class MissingDataInfoException extends IllegalStateException {

    /**
     * Constructor to create the Exception with a message.
     * @param message The message of the Exception.
     */
    public MissingDataInfoException(String message) {
        super(message);
    }

}
