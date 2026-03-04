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
        String url = "https://store.steampowered.com/api/appdetails?appids=" + appId;

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

                        return new SteamGameData(name, initialPrice, finalPrice, discountPercent);
                    } else {
                        return new SteamGameData(name, 0.0, 0.0, 0);
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

        public SteamGameData(String name, double initialPrice, double finalPrice, int discountPercent) {
            this.name = name;
            this.initialPrice = initialPrice;
            this.finalPrice = finalPrice;
            this.discountPercent = discountPercent;
        }
    }
}