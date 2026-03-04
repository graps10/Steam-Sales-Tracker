package com.tracker.steamsales.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_prices")
public class GamePrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many to One
    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    private Double finalPrice;
    private Double initialPrice;
    private Integer discountPercent;
    private LocalDateTime checkDate;

    public GamePrice() {
        this.checkDate = LocalDateTime.now();
    }

    public GamePrice(Game game, Double finalPrice, Double initialPrice, Integer discountPercent) {
        this.game = game;
        this.finalPrice = finalPrice;
        this.initialPrice = initialPrice;
        this.discountPercent = discountPercent;
        this.checkDate = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }

    public Double getFinalPrice() { return finalPrice; }
    public void setFinalPrice(Double finalPrice) { this.finalPrice = finalPrice; }

    public Double getInitialPrice() { return initialPrice; }
    public void setInitialPrice(Double initialPrice) { this.initialPrice = initialPrice; }

    public Integer getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(Integer discountPercent) { this.discountPercent = discountPercent; }

    public LocalDateTime getCheckDate() { return checkDate; }
    public void setCheckDate(LocalDateTime checkDate) { this.checkDate = checkDate; }
}