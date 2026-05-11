package com.trabajofinal.ticketyap.modelos;

public class GoogleUserData {

    private String email;
    private String name;
    private String picture;
    private String googleId;

    public GoogleUserData(String email, String googleId, String name, String picture) {
        this.email = email;
        this.googleId = googleId;
        this.name = name;
        this.picture = picture;
    }

    public GoogleUserData(){}

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
    public String getGoogleId() {
        return googleId;
    }
    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GoogleUserData{");
        sb.append("email=").append(email);
        sb.append(", name=").append(name);
        sb.append(", picture=").append(picture);
        sb.append(", googleId=").append(googleId);
        sb.append('}');
        return sb.toString();
    }





}
