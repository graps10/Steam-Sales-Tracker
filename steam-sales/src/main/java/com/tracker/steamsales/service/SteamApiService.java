package com.tracker.steamsales.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.tracker.steamsales.dtos.GameSearchResult;
import com.tracker.steamsales.dtos.SteamGameData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class SteamApiService {

    private final RestTemplate restTemplate;

    public SteamApiService() {

        this.restTemplate = new RestTemplate();
    }

    public SteamGameData fetchGameData(String appId) {
        String apiUrl = "https://store.steampowered.com/api/appdetails?appids=" + appId + "&cc=UA";
        String reviewSummary = "No reviews";

        try {
            Document doc = Jsoup.connect("https://store.steampowered.com/app/" + appId)
                    .cookie("birthtime", "283993201")
                    .cookie("lastagecheckage", "1-January-1978")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .get();

            Element reviewElement = doc.selectFirst(".game_review_summary");
            if (reviewElement != null) {
                reviewSummary = reviewElement.text();
            }
        } catch (Exception e) {
            System.out.println("HTML Parsing Error: " + e.getMessage());
        }

        try {
            JsonNode response = restTemplate.getForObject(apiUrl, JsonNode.class);

            if (response != null && response.has(appId)) {
                JsonNode gameNode = response.get(appId);
                boolean success = gameNode.get("success").asBoolean();

                if (success && gameNode.has("data")) {
                    JsonNode dataNode = gameNode.get("data");
                    String name = dataNode.get("name").asText();
                    String headerImage = dataNode.has("header_image") ? dataNode.get("header_image").asText() : "";

                    if (dataNode.has("price_overview")) {
                        JsonNode priceNode = dataNode.get("price_overview");
                        double initialPrice = priceNode.get("initial").asDouble() / 100;
                        double finalPrice = priceNode.get("final").asDouble() / 100;
                        int discountPercent = priceNode.get("discount_percent").asInt();

                        return new SteamGameData(name, initialPrice, finalPrice, discountPercent, headerImage, reviewSummary);
                    } else {
                        return new SteamGameData(name, 0.0, 0.0, 0, headerImage, reviewSummary);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("JSON Parsing Error for game " + appId + ": " + e.getMessage());
        }
        
        return null;
    }

    public List<GameSearchResult> searchGames(String query) {
        String url = "https://store.steampowered.com/api/storesearch/?term=" + query + "&l=english&cc=UA";
        List<GameSearchResult> results = new ArrayList<>();

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