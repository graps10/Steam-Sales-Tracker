package com.tracker.steamsales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SteamSalesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SteamSalesApplication.class, args);
	}

}
