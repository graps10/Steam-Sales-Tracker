package com.tracker.steamsales.repository;

import com.tracker.steamsales.model.Game;
import com.tracker.steamsales.model.GamePrice;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GamePriceRepository extends JpaRepository<GamePrice, Long> { 

    GamePrice findTopByGameOrderByCheckDateDesc(Game game);

    @Transactional
    void deleteByGame(Game game);
}