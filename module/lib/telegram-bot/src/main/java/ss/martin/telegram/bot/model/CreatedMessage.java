package ss.martin.telegram.bot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created message
 * @author alex
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreatedMessage(
    @JsonProperty("message_id")
    Long messageId
) {}
