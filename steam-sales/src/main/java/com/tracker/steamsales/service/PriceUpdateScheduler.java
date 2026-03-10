package com.tracker.steamsales.service;

import com.tracker.steamsales.dtos.SteamGameData;
import com.tracker.steamsales.model.Game;
import com.tracker.steamsales.model.GamePrice;
import com.tracker.steamsales.repository.GamePriceRepository;
import com.tracker.steamsales.repository.GameRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceUpdateScheduler {

    private final GameRepository gameRepository;
    private final GamePriceRepository gamePriceRepository;
    private final SteamApiService steamApiService;

    public PriceUpdateScheduler(GameRepository gameRepository, GamePriceRepository gamePriceRepository, SteamApiService steamApiService) {
        this.gameRepository = gameRepository;
        this.gamePriceRepository = gamePriceRepository;
        this.steamApiService = steamApiService;
    }

    @Scheduled(fixedRate = 10800000)
    public void updateAllPricesAutomatically() {
        System.out.println("Background task: Starting to update prices from Steam...");
        
        List<Game> games = gameRepository.findAll();
        
        for (Game game : games) {
            SteamGameData data = steamApiService.fetchGameData(game.getSteamAppId());
            
            if (data != null) {
                GamePrice newPrice = new GamePrice(game, data.finalPrice, data.initialPrice, data.discountPercent);
                gamePriceRepository.save(newPrice);
                System.out.println("Updated: " + game.getTitle());
            }
        }
        
        System.out.println("Background task completed.");
    }
}