package ss.martin.telegram.bot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Update model.
 * @author alex
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Update(
    Long update_id,
    Message message
) {}
