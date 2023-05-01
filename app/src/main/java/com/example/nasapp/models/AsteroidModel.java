package com.example.nasapp.models;

public class AsteroidModel {
    private long id;
    private String name;
    private boolean hazardousAsteroid;
    private String neoReferenceId;
    private String nasaJplUrl;
    private boolean isSentryObject;
    private String absoluteMagnitude;
    private long userId;
    private String closeApproachDate;

    public AsteroidModel(long id, String name, boolean hazardousAsteroid, String neoReferenceId, String nasaJplUrl, boolean isSentryObject, String absoluteMagnitude, long userId) {
        this.id = id;
        this.name = name;
        this.hazardousAsteroid = hazardousAsteroid;
        this.neoReferenceId = neoReferenceId;
        this.nasaJplUrl = nasaJplUrl;
        this.isSentryObject = isSentryObject;
        this.absoluteMagnitude = absoluteMagnitude;
        this.userId = userId;

    }

    public AsteroidModel() {
    }

    public AsteroidModel(long id, String name, boolean hazardousAsteroid, String neoReferenceId, String nasaJplUrl, boolean isSentryObject, String absoluteMagnitude) {
        this.id = id;
        this.name = name;
        this.hazardousAsteroid = hazardousAsteroid;
        this.neoReferenceId = neoReferenceId;
        this.nasaJplUrl = nasaJplUrl;
        this.isSentryObject = isSentryObject;
        this.absoluteMagnitude = absoluteMagnitude;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHazardousAsteroid() {
        return hazardousAsteroid;
    }

    public void setHazardousAsteroid(boolean hazardousAsteroid) {
        this.hazardousAsteroid = hazardousAsteroid;
    }

    public String getNeoReferenceId() {
        return neoReferenceId;
    }

    public void setNeoReferenceId(String neoReferenceId) {
        this.neoReferenceId = neoReferenceId;
    }

    public String getNasaJplUrl() {
        return nasaJplUrl;
    }

    public void setNasaJplUrl(String nasaJplUrl) {
        this.nasaJplUrl = nasaJplUrl;
    }

    public boolean isSentryObject() {
        return isSentryObject;
    }

    public void setSentryObject(boolean sentryObject) {
        isSentryObject = sentryObject;
    }

    public String getAbsoluteMagnitude() {
        return absoluteMagnitude;
    }

    public void setAbsoluteMagnitude(String absoluteMagnitude) {
        this.absoluteMagnitude = absoluteMagnitude;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
