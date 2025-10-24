package de.stella.agora_web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    // Endpoint base
    private String apiEndpoint;

    // JWT
    private String jwtIssuer;
    private String jwtAudience;
    private String accessTokenPrivate;
    private String accessTokenPublic;

    // Refresh token
    private Boolean refreshTokenEnabled;
    private Long refreshTokenExpiration;
    private String refreshTokenPrivate;
    private String refreshTokenPublic;

    // Kafka
    private Boolean kafkaEnabled;

    // Email admin
    private String adminEmail;

    // Rate limiting
    private Boolean rateLimitingEnabled;
    private Integer rateLimitingRequestsPerMinute;

    // Getters and setters
    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    public String getJwtIssuer() {
        return jwtIssuer;
    }

    public void setJwtIssuer(String jwtIssuer) {
        this.jwtIssuer = jwtIssuer;
    }

    public String getJwtAudience() {
        return jwtAudience;
    }

    public void setJwtAudience(String jwtAudience) {
        this.jwtAudience = jwtAudience;
    }

    public String getAccessTokenPrivate() {
        return accessTokenPrivate;
    }

    public void setAccessTokenPrivate(String accessTokenPrivate) {
        this.accessTokenPrivate = accessTokenPrivate;
    }

    public String getAccessTokenPublic() {
        return accessTokenPublic;
    }

    public void setAccessTokenPublic(String accessTokenPublic) {
        this.accessTokenPublic = accessTokenPublic;
    }

    public Boolean getRefreshTokenEnabled() {
        return refreshTokenEnabled;
    }

    public void setRefreshTokenEnabled(Boolean refreshTokenEnabled) {
        this.refreshTokenEnabled = refreshTokenEnabled;
    }

    public Long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    public void setRefreshTokenExpiration(Long refreshTokenExpiration) {
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String getRefreshTokenPrivate() {
        return refreshTokenPrivate;
    }

    public void setRefreshTokenPrivate(String refreshTokenPrivate) {
        this.refreshTokenPrivate = refreshTokenPrivate;
    }

    public String getRefreshTokenPublic() {
        return refreshTokenPublic;
    }

    public void setRefreshTokenPublic(String refreshTokenPublic) {
        this.refreshTokenPublic = refreshTokenPublic;
    }

    public Boolean getKafkaEnabled() {
        return kafkaEnabled;
    }

    public void setKafkaEnabled(Boolean kafkaEnabled) {
        this.kafkaEnabled = kafkaEnabled;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public Boolean getRateLimitingEnabled() {
        return rateLimitingEnabled;
    }

    public void setRateLimitingEnabled(Boolean rateLimitingEnabled) {
        this.rateLimitingEnabled = rateLimitingEnabled;
    }

    public Integer getRateLimitingRequestsPerMinute() {
        return rateLimitingRequestsPerMinute;
    }

    public void setRateLimitingRequestsPerMinute(Integer rateLimitingRequestsPerMinute) {
        this.rateLimitingRequestsPerMinute = rateLimitingRequestsPerMinute;
    }
}
