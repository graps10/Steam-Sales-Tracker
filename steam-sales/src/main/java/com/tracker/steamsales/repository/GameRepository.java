package com.tracker.steamsales.repository;

import com.tracker.steamsales.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> 
{
    boolean existsBySteamAppId(String steamAppId);

    Game findBySteamAppId(String steamAppId);
}