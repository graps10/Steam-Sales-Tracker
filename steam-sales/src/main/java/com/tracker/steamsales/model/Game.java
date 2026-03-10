package com.tracker.steamsales.model;

import jakarta.persistence.*;

@Entity
@Table(name = "games")
public class Game 
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    @Column(name = "steam_app_id", unique = true)
    private String steamAppId;

    @Column(name = "header_image")
    private String headerImage;
    private String reviewSummary;

    public Game() {
    }

    public Game(String title, String steamAppId, String headerImage, String reviewSummary) {
     this.title = title;
     this.steamAppId = steamAppId;
     this.headerImage = headerImage;
     this.reviewSummary = reviewSummary;
 }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSteamAppId() { return steamAppId; }
    public void setSteamAppId(String steamAppId) { this.steamAppId = steamAppId; }

    public String getHeaderImage() { return headerImage; }
    public void setHeaderImage(String headerImage) { this.headerImage = headerImage; }

    public String getReviewSummary() { return reviewSummary; }
    public void setReviewSummary(String reviewSummary) { this.reviewSummary = reviewSummary; }
}