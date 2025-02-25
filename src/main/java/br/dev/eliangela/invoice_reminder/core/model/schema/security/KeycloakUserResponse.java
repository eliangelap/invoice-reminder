package br.dev.eliangela.invoice_reminder.core.model.schema.security;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class KeycloakUserResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("refresh_expires_in")
    private Long refreshExpiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("not-before-policy")
    private Integer notBeforePolicy;

    @JsonProperty("session_state")
    private String sessionState;

    @JsonProperty("scope")
    private String scope;

    private ZonedDateTime expirationDateTime;

    public void setExpiresIn(Long expiresIn) {
        this.expirationDateTime = ZonedDateTime.now().plusSeconds(expiresIn);
        this.expiresIn = expiresIn;
    }

    public boolean isCredentialsExpired() {
        return this.expirationDateTime.compareTo(ZonedDateTime.now()) < 0;
    }

    public boolean isEnabled() {
        return true;
    }

}
