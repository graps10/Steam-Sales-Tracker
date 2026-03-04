# 🎮 Steam Sales Tracker

A full-stack web application built with Spring Boot that allows users to track game prices and discounts on Steam. This project features a live search autocomplete, automated background price updates, and a sleek, Steam-inspired user interface.

## ✨ Features

* **Live Game Search:** Search for any game on Steam using the official Steam Storefront API with real-time autocomplete.
* **Price Tracking:** Automatically fetches and stores the current price, original price, and discount percentage.
* **Automated Background Updates:** A built-in Spring Scheduler updates the prices of all tracked games every 3 hours to ensure data is always fresh.
* **Persistent Database:** Uses an H2 relational database configured to save data locally, ensuring your tracked games persist between server restarts.
* **Beautiful UI/UX:** A responsive CSS-grid layout featuring game posters, discount badges, and smooth hover effects, rendered server-side with Thymeleaf.

## 🛠️ Tech Stack

* **Backend:** Java 17, Spring Boot 3.x
* **Database:** H2 Database (File-based mode), Spring Data JPA, Hibernate
* **Frontend:** HTML5, CSS3, Vanilla JavaScript, Thymeleaf (Template Engine)
* **Data Processing:** Jackson (JSON parsing), Spring `RestTemplate` for external API calls

## 🚀 Getting Started

### Prerequisites
Make sure you have **Java 17** (or higher) installed on your machine. You do not need to install Maven manually, as the project includes the Maven Wrapper.