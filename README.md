# 🎭 Theater Seat Reservation System

This project provides a persistent class model for managing theater seat reservations.

## 📦 Project Overview

The system allows users to reserve seats for various performances in different rooms of a theater. It supports different ticket types and tracks the state of each reservation.

### Key Features

- Multiple **ticket types** (e.g., regular, student, discounted)
- Support for **multiple rooms**
- **Performances** can be shown multiple times and in different rooms
- **Reservations** can either be **confirmed** or **expired**
- **No data is deleted** — full history is preserved
- Queries are implemented using **HQL (Hibernate Query Language)**
- Only essential data is exposed to the frontend via **DTOs (Data Transfer Objects)**
- Supports **pagination** in returned lists

## 📚 Contents

The project includes:

- Persistent class model for all entities:
    - `User`
    - `TicketType`
    - `Room`
    - `Play`
    - `Performance`
    - `Reservation`
    - `Seat`
    - `ReservationSeat`
- Sample HQL queries for:
    - Listing performances by room ID
    - Listing performances by performance ID
    - Listing performances by title
    - Listing attendees for a performance
    - Listing user's performances by user ID or login
    - Counting booked seats for a room at a specific time
    - Counting the number of rooms used for a given play
    - Counting tickets bought by a user within a date range

## 🛠 Technologies

- Java
- Hibernate / JPA
- HQL
- Spring Framework for service and controller layers
