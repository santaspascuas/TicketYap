package com.trabajofinal.ticketyap.modelos;

import java.time.Instant;
import java.util.UUID;

public class RefreshToken {

        private UUID id;
    private String token;
    private Integer user_id;
    private Instant created_at;
    private Instant expires_at;
    private Boolean revoked;


    public RefreshToken(UUID id, String token, Integer user_id, Instant created_at, Instant expires_at,
            Boolean revoked) {
        this.id = id;
        this.token = token;
        this.user_id = user_id;
        this.created_at = created_at;
        this.expires_at = expires_at;
        this.revoked = revoked;
    }

    //Usar otro constructor sin id para crear nuevos tokens. // Una manera de poder crear sin tener que pasarle el id o
    
    public RefreshToken(String token, Integer userId, Instant expiresAt) {
    this.id = UUID.randomUUID();
    this.token = token;
    this.user_id = userId;
    this.created_at = Instant.now();
    this.expires_at = expiresAt;
    this.revoked = false;
}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Instant getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Instant created_at) {
        this.created_at = created_at;
    }

    public Instant getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(Instant expires_at) {
        this.expires_at = expires_at;
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }















}
