package ss.martin.telegram.bot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * User.
 * @author alex
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record User(
    Long id,
    String username
) {}
