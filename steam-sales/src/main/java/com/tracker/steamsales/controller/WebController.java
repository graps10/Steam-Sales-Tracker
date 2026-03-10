package com.tracker.steamsales.controller;

import com.tracker.steamsales.dtos.GameViewDto;
import com.tracker.steamsales.model.Game;
import com.tracker.steamsales.model.GamePrice;
import com.tracker.steamsales.repository.GamePriceRepository;
import com.tracker.steamsales.repository.GameRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        List<Game> games = gameRepository.findAllByOrderByIdDesc();
        
        List<GameViewDto> gameDtos = games.stream()
                .map(game -> {
                    GamePrice price = gamePriceRepository.findTopByGameOrderByCheckDateDesc(game);
                    if (price != null) {
                        return new GameViewDto(
                                game.getSteamAppId(),
                                game.getTitle(),
                                price.getFinalPrice(),
                                price.getInitialPrice(),
                                price.getDiscountPercent(),
                                game.getHeaderImage(),
                                game.getReviewSummary()
                        );
                    }
                    return null;
                })
                .filter(java.util.Objects::nonNull)
                .toList();

        model.addAttribute("games", gameDtos);
        return "index";
    }
}