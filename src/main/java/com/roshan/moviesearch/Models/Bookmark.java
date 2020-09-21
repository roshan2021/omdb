package com.roshan.moviesearch.Models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity 
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String omdbId;

    private Integer ownerId;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOmdbId() {
        return this.omdbId;
    }

    public void setOmdbId(String omdbId) {
        this.omdbId = omdbId;
    }

    public Integer getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", omdbId='" + getOmdbId() + "'" + ", ownerId='" + getOwnerId() + "'"
                + "}";
    }

}