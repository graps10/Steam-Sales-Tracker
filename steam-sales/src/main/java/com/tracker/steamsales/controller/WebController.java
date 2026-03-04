package com.tracker.steamsales.controller;

import com.tracker.steamsales.model.Game;
import com.tracker.steamsales.model.GamePrice;
import com.tracker.steamsales.repository.GamePriceRepository;
import com.tracker.steamsales.repository.GameRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WebController {

    private final GameRepository gameRepository;
    private final GamePriceRepository gamePriceRepository;

    public WebController(GameRepository gameRepository, GamePriceRepository gamePriceRepository) {
        this.gameRepository = gameRepository;
        this.gamePriceRepository = gamePriceRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Game> games = gameRepository.findAll();
        List<GameViewDto> gameDtos = new ArrayList<>();

        for (Game game : games) {
            GamePrice price = gamePriceRepository.findTopByGameOrderByCheckDateDesc(game);
            
            if (price != null) {
                gameDtos.add(new GameViewDto(
                        game.getSteamAppId(),
                        game.getTitle(),
                        price.getFinalPrice(),
                        price.getInitialPrice(),
                        price.getDiscountPercent()
                ));
            }
        }

        model.addAttribute("games", gameDtos);
        return "index";
    }

    public static class GameViewDto {
        private String steamAppId;
        private String title;
        private Double finalPrice;
        private Double initialPrice;
        private Integer discountPercent;

        public GameViewDto(String steamAppId, String title, Double finalPrice, Double initialPrice, Integer discountPercent) {
            this.steamAppId = steamAppId;
            this.title = title;
            this.finalPrice = finalPrice;
            this.initialPrice = initialPrice;
            this.discountPercent = discountPercent;
        }

        public String getSteamAppId() { return steamAppId; }
        public String getTitle() { return title; }
        public Double getFinalPrice() { return finalPrice; }
        public Double getInitialPrice() { return initialPrice; }
        public Integer getDiscountPercent() { return discountPercent; }
    }
}