package com.syakeapps.tcr.service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.JDBC;

import com.syakeapps.tcr.application.Constraints;
import com.syakeapps.tcr.dao.TwitchTokenDao;
import com.syakeapps.tcr.entity.TwitchToken;
import com.syakeapps.tcr.util.JsonUtils;
import com.syakeapps.tcr.util.UriBuilder;

/**
 * Service for handling Twitch authentication.
 * This service manages the OAuth token retrieval and validation process.
 */
public class TwitchAuthService {
    private static final Logger LOG = LoggerFactory.getLogger(TwitchAuthService.class);

    private ConfigService conf = new ConfigService();
    private TwitchTokenDao dao;
    private TwitchAuthStateHolder authState = new TwitchAuthStateHolder();

    /**
     * Authenticates with Twitch by loading the OAuth token from the database,
     * validating it, and refreshing it if necessary.
     *
     * @throws Exception if an error occurs during authentication
     */
    public void authenticate() throws Exception {
        dao = new TwitchTokenDao(JDBC.PREFIX + conf.get(Constraints.DATABASE_FILEPATH));

        // Load the OAuth token from the database
        String token = dao.loadToken();
        if (token == null) {
            throw new RuntimeException("No OAuth token found in database.");
        }
        LOG.debug("Loaded OAuth token from database.");

        // Validate the OAuth token from the database
        if (token != null) {
            LOG.debug("Validating OAuth token from database.");

            URI uri = UriBuilder.create(conf.get(Constraints.TWITCH_OAUTH_ENDPOINT))
                    .path("validate")
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                LOG.debug("OAuth token is valid.");
            } else {
                LOG.debug("Failed to validate OAuth token. {}", response.body());
                // If the token is invalid, we need to reset it
                token = null;
            }
        }

        // If the token is null or invalid, refresh it
        if (token == null) {
            LOG.debug("Trying to refresh OAuth token.");

            String refreshToken = dao.loadRefreshToken();
            if (refreshToken == null) {
                throw new RuntimeException("No refresh token found in database.");
            }

            URI uri = UriBuilder.create(conf.get(Constraints.TWITCH_OAUTH_ENDPOINT))
                    .path("token")
                    .build();

            StringBuilder body = new StringBuilder();
            body.append("client_id=").append(conf.get(Constraints.TWITCH_CLIENT_ID));
            body.append("&");
            body.append("client_secret=").append(conf.get(Constraints.TWITCH_CLIENT_SECRET));
            body.append("&");
            body.append("grant_type=refresh_token");
            body.append("&");
            body.append("refresh_token=").append(URLEncoder.encode(refreshToken, "UTF-8"));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                LOG.debug("New OAuth token obtained successfully.");

                TwitchToken dto = JsonUtils.fromJson(response.body(), TwitchToken.class);
                // Save the new token and refresh token to the database
                dao.saveToken(dto);
                token = dto.getAccessToken();
            } else {
                LOG.error("Failed to obtain new OAuth token. {}", response.body());
                throw new RuntimeException("Failed to obtain new OAuth token.");
            }
        }

        // Set the token in the auth state holder
        authState.setToken(token);
    }
}
