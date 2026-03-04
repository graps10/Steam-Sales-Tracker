package com.tracker.steamsales.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SteamApiService {

    private final RestTemplate restTemplate;

    public SteamApiService() {

        this.restTemplate = new RestTemplate();
    }

    public SteamGameData fetchGameData(String appId) {
        String url = "https://store.steampowered.com/api/appdetails?appids=" + appId + "&cc=UA";

        try {
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            if (response != null && response.has(appId)) {
                JsonNode gameNode = response.get(appId);
                boolean success = gameNode.get("success").asBoolean();

                if (success && gameNode.has("data")) {
                    JsonNode dataNode = gameNode.get("data");
                    String name = dataNode.get("name").asText();

                    if (dataNode.has("price_overview")) {
                        JsonNode priceNode = dataNode.get("price_overview");
                        
                        double initialPrice = priceNode.get("initial").asDouble() / 100;
                        double finalPrice = priceNode.get("final").asDouble() / 100;
                        int discountPercent = priceNode.get("discount_percent").asInt();
                        String headerImage = dataNode.has("header_image") ? dataNode.get("header_image").asText() : "";

                        return new SteamGameData(name, initialPrice, finalPrice, discountPercent, headerImage);
                    } else {
                        String headerImage = dataNode.has("header_image") ? dataNode.get("header_image").asText() : "";
                        return new SteamGameData(name, 0.0, 0.0, 0, headerImage);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Parsing Error for game " + appId + ": " + e.getMessage());
        }
        
        return null;
    }

    public static class SteamGameData {
        public String name;
        public double initialPrice;
        public double finalPrice;
        public int discountPercent;
        public String headerImage;

        public SteamGameData(String name, double initialPrice, double finalPrice, int discountPercent, String headerImage) {
            this.name = name;
            this.initialPrice = initialPrice;
            this.finalPrice = finalPrice;
            this.discountPercent = discountPercent;
            this.headerImage = headerImage;
        }
    }

    public record GameSearchResult(String appId, String name) {}

    public java.util.List<GameSearchResult> searchGames(String query) {
        String url = "https://store.steampowered.com/api/storesearch/?term=" + query + "&l=english&cc=UA";
        java.util.List<GameSearchResult> results = new java.util.ArrayList<>();

        try {
            JsonNode response = restTemplate.getForObject(url, JsonNode.class);

            if (response != null && response.has("items")) {
                for (JsonNode item : response.get("items")) {
                    results.add(new GameSearchResult(
                            item.get("id").asText(),
                            item.get("name").asText()
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("Searching Error in Steam API: " + e.getMessage());
        }

        return results;
    }
}