package com.tracker.steamsales.dtos;

public class GameViewDto {
    private String steamAppId;
    private String title;
    private Double finalPrice;
    private Double initialPrice;
    private Integer discountPercent;
    private String headerImage;
    private String reviewSummary;

    public GameViewDto(String steamAppId, String title, Double finalPrice, Double initialPrice, Integer discountPercent, String headerImage, String reviewSummary) {
        this.steamAppId = steamAppId;
        this.title = title;
        this.finalPrice = finalPrice != null ? finalPrice : 0.0;
        this.initialPrice = initialPrice != null ? initialPrice : 0.0;
        this.discountPercent = discountPercent != null ? discountPercent : 0;
        this.headerImage = headerImage;
        this.reviewSummary = reviewSummary;
    }

    public String getSteamAppId() { return steamAppId; }
    public String getTitle() { return title; }
    public String getHeaderImage() { return headerImage; }
    public String getReviewSummary() { return reviewSummary != null ? reviewSummary : "No reviews"; }
    
    public String getFinalPriceText() { return this.finalPrice == 0.0 ? "Free" : this.finalPrice + " ₴"; }
    public String getInitialPriceText() { return this.initialPrice + " ₴"; }
    public String getDiscountText() { return this.discountPercent > 0 ? "-" + this.discountPercent + "%" : "-"; }
    public boolean isDiscounted() { return this.discountPercent > 0; }
}