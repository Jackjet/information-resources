
package d1.project.tangshan.operation.manage.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SSOToken {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("error")
    private String error;
    @JsonProperty("error_description")
    private String errorDescription;
    @JsonProperty("expires_in")
    private Long expiresIn;
    @JsonProperty("not-before-policy")
    private Long notBeforePolicy;
    @JsonProperty("refresh_expires_in")
    private Long refreshExpiresIn;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("scope")
    private String scope;
    @JsonProperty("session_state")
    private String sessionState;
    @JsonProperty("token_type")
    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Long getNotBeforePolicy() {
        return notBeforePolicy;
    }

    public void setNotBeforePolicy(Long notBeforePolicy) {
        this.notBeforePolicy = notBeforePolicy;
    }

    public Long getRefreshExpiresIn() {
        return refreshExpiresIn;
    }

    public void setRefreshExpiresIn(Long refreshExpiresIn) {
        this.refreshExpiresIn = refreshExpiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSessionState() {
        return sessionState;
    }

    public void setSessionState(String sessionState) {
        this.sessionState = sessionState;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

}
