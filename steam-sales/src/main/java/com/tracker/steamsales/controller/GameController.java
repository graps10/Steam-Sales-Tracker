package com.tracker.steamsales.controller;

import com.tracker.steamsales.dtos.SteamGameData;
import com.tracker.steamsales.dtos.GameSearchResult;
import com.tracker.steamsales.model.Game;
import com.tracker.steamsales.model.GamePrice;
import com.tracker.steamsales.repository.GamePriceRepository;
import com.tracker.steamsales.repository.GameRepository;
import com.tracker.steamsales.service.SteamApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameRepository gameRepository;
    private final GamePriceRepository gamePriceRepository;
    private final SteamApiService steamApiService;

    public GameController(GameRepository gameRepository, GamePriceRepository gamePriceRepository, SteamApiService steamApiService) {
        this.gameRepository = gameRepository;
        this.gamePriceRepository = gamePriceRepository;
        this.steamApiService = steamApiService;
    }

    @GetMapping
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @PostMapping("/{appId}")
    public ResponseEntity<String> addGame(@PathVariable String appId) {
        if (gameRepository.existsBySteamAppId(appId)) {
            return ResponseEntity.badRequest().body("The game with the ID " + appId + " is already being tracked.");
        }

        SteamGameData data = steamApiService.fetchGameData(appId);
        if (data == null) {
            return ResponseEntity.badRequest().body("Failed to retrieve data from Steam for ID " + appId);
        }

        Game game = new Game(data.name, appId, data.headerImage, data.reviewSummary);
        gameRepository.save(game);

        GamePrice price = new GamePrice(game, data.finalPrice, data.initialPrice, data.discountPercent);
        gamePriceRepository.save(price);

        return ResponseEntity.ok("Game '" + data.name + "' successfully added to the tracker!");
    }

    @PutMapping("/{appId}/refresh-price")
    public ResponseEntity<String> refreshPrice(@PathVariable String appId) {
        Game game = gameRepository.findBySteamAppId(appId);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        SteamGameData data = steamApiService.fetchGameData(appId);
        if (data != null) {
            GamePrice newPrice = new GamePrice(game, data.finalPrice, data.initialPrice, data.discountPercent);
            gamePriceRepository.save(newPrice);
            return ResponseEntity.ok("The price for '" + data.name + "' has been updated!");
        }
        return ResponseEntity.internalServerError().body("Error connecting to Steam.");
    }

    @DeleteMapping("/{appId}")
    public ResponseEntity<String> deleteGame(@PathVariable String appId) {
        Game game = gameRepository.findBySteamAppId(appId);
        if (game != null) {
            gamePriceRepository.deleteByGame(game);
            gameRepository.delete(game);
            
            return ResponseEntity.ok("Game '" + game.getTitle() + "' has been removed from the database.");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<GameSearchResult>> searchGames(@RequestParam String q) {
        if (q == null || q.trim().isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(steamApiService.searchGames(q));
    }
}