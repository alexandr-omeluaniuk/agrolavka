package ss.agrolavka.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;

public record IKassaAuthResponse(
    @JsonProperty("access_token")
    String accessToken,
    @JsonProperty("expires_in")
    Long expiresIn,
    String scope,
    @JsonProperty("token_type")
    String tokenType,
    Long ttl
) {
}
