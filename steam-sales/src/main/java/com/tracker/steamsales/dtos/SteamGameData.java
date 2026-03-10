package com.tracker.steamsales.dtos;

public class SteamGameData {
    public String name;
    public double initialPrice;
    public double finalPrice;
    public int discountPercent;
    public String headerImage;
    public String reviewSummary;

    public SteamGameData(String name, double initialPrice, double finalPrice, int discountPercent, String headerImage, String reviewSummary) {
        this.name = name;
        this.initialPrice = initialPrice;
        this.finalPrice = finalPrice;
        this.discountPercent = discountPercent;
        this.headerImage = headerImage;
        this.reviewSummary = reviewSummary;
    }
}