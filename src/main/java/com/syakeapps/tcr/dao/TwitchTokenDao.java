package com.syakeapps.tcr.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.syakeapps.tcr.entity.TwitchToken;

public class TwitchTokenDao extends DaoBase {
    public TwitchTokenDao(String dbUrl) {
        super(dbUrl);
    }

    public void saveToken(TwitchToken dto) throws Exception {
        try (Connection conn = DriverManager.getConnection(dbUrl);
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO twitch_token (access_token, refresh_token, scope, token_type, expires_in) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setString(1, dto.getAccessToken());
            stmt.setString(2, dto.getRefreshToken());
            stmt.setObject(3, dto.getScope());
            stmt.setString(4, dto.getTokenType());
            stmt.setInt(5, dto.getExpiresIn());
            stmt.executeUpdate();
        }
    }

    public String loadToken() throws Exception {
        try (Connection conn = DriverManager.getConnection(dbUrl);
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT access_token FROM twitch_token ORDER BY id DESC LIMIT 1")) {

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("access_token");
                }
            }
        }

        return null;
    }

    public String loadRefreshToken() throws Exception {
        try (Connection conn = DriverManager.getConnection(dbUrl);
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT refresh_token FROM twitch_token ORDER BY id DESC LIMIT 1")) {

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("refresh_token");
                }
            }
        }

        return null;
    }
}
