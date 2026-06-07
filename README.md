<div align="center">

# 🏨 AirBnb Clone
### Spring Boot Backend

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Stripe](https://img.shields.io/badge/Stripe-626CD9?style=for-the-badge&logo=stripe&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=jsonwebtokens&logoColor=white)

A full-featured hotel booking REST API built with Spring Boot 4, modelled after Airbnb/OYO-style platforms.  
Covers hotel & room management, dynamic pricing, real-time inventory tracking, Stripe-powered payments, JWT auth, and role-based access control.

</div>

---

## 📑 Table of Contents

- [⚙️ Tech Stack](#%EF%B8%8F-tech-stack)
- [🏗️ Architecture Overview](#%EF%B8%8F-architecture-overview)
- [📁 Project Structure](#-project-structure)
- [🗄️ Data Model](#%EF%B8%8F-data-model)
- [💰 Dynamic Pricing Engine](#-dynamic-pricing-engine)
- [🔐 Authentication & Authorization](#-authentication--authorization)
- [🌐 API Reference](#-api-reference)
  - [Auth](#-auth--apiv1auth)
  - [Hotel Browse (Public)](#-hotel-browse-public--apiv1hotels)
  - [Hotel Admin](#-hotel-admin--apiv1adminhotels)
  - [Room Admin](#-room-admin--apiv1adminhotelshotelidrooms)
  - [Inventory Admin](#-inventory-admin--apiv1admininventory)
  - [Bookings](#-bookings--apiv1bookings)
  - [User Profile](#-user-profile--apiv1users)
  - [Stripe Webhook](#-stripe-webhook--apiv1webhookpayment)
- [🔄 Booking Lifecycle](#-booking-lifecycle)
- [⏰ Scheduled Jobs](#-scheduled-jobs)
- [🚨 Error Handling](#-error-handling)
- [🔧 Configuration](#-configuration)
- [🚀 Running Locally](#-running-locally)
- [🐳 Docker](#-docker)

---

## ⚙️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.1 |
| Web | Spring MVC (spring-boot-starter-webmvc) |
| Persistence | Spring Data JPA + Hibernate |
| Database | PostgreSQL |
| Security | Spring Security + JWT (jjwt 0.13.0) |
| Payments | Stripe Java SDK 31.2.0 |
| API Docs | SpringDoc OpenAPI 3.0.1 (Swagger UI) |
| Mapping | ModelMapper 3.2.6 |
| Boilerplate | Lombok |
| Build | Maven 3 |
| Container | Docker (multi-stage, eclipse-temurin 21 JRE) |

---

## 🏗️ Architecture Overview

The application follows a classic layered architecture with clean separation between REST, business logic, and data access. All requests pass through a JWT security filter before reaching any controller.

```mermaid
flowchart TD
    Client(["👤 Client / Browser"])

    subgraph Security["🔐 Security Layer"]
        JwtFilter["JwtAuthFilter\n(OncePerRequestFilter)"]
        WSC["WebSecurityConfig\n(RBAC Rules)"]
    end

    subgraph Controllers["🌐 REST Controllers  —  /api/v1"]
        AC["AuthController\n/auth"]
        HBC["HotelBrowseController\n/hotels"]
        HC["HotelController\n/admin/hotels"]
        RAC["RoomAdminController\n/admin/hotels/{id}/rooms"]
        IC["InventoryController\n/admin/inventory"]
        BBC["HotelBookingController\n/bookings"]
        UC["UserController\n/users"]
        WC["WebhookController\n/webhook"]
    end

    subgraph Services["⚙️ Service Layer"]
        AS["AuthService"]
        HS["HotelService"]
        BS["BookingService"]
        IS["InventoryService"]
        RS["RoomService"]
        CS["CheckoutService"]
        US["UserService"]
        PS["PricingService\n(Strategy Pattern)"]
        PUS["PricingUpdateService\n(Scheduler)"]
    end

    subgraph Repositories["🗃️ Repository Layer  —  Spring Data JPA"]
        HR["HotelRepository"]
        BR["BookingRepository"]
        IR["InventoryRepository"]
        RR["RoomRepository"]
        UR["UserRepository"]
        GR["GuestRepository"]
        HMPR["HotelMinPriceRepository"]
    end

    subgraph CrossCutting["🔀 Cross-Cutting"]
        GEH["GlobalExceptionHandler\n(@RestControllerAdvice)"]
        GRH["GlobalResponseHandler\n(Response Wrapper)"]
    end

    DB[("🐘 PostgreSQL")]
    Stripe(["💳 Stripe API"])

    Client -->|"HTTP Request"| JwtFilter
    JwtFilter --> WSC
    WSC --> Controllers

    AC --> AS
    HBC --> HS & IS
    HC --> HS & BS
    RAC --> RS
    IC --> IS
    BBC --> BS
    UC --> US & BS
    WC --> BS

    BS --> CS
    BS --> PS
    CS -->|"Create Session"| Stripe
    Stripe -->|"Webhook Event"| WC

    Services --> Repositories
    PS --> PUS
    Repositories --> DB

    Controllers -.->|"Error"| GEH
    Controllers -.->|"Success"| GRH

    style Security fill:#fff3cd,stroke:#ffc107
    style Controllers fill:#cfe2ff,stroke:#0d6efd
    style Services fill:#d1e7dd,stroke:#198754
    style Repositories fill:#f8d7da,stroke:#dc3545
    style CrossCutting fill:#e2e3e5,stroke:#6c757d
```

---

## 📁 Project Structure

```
src/main/java/com/adnanumar/projects/airBnbApp/
│
├── AirBnbAppApplication.java          # Entry point
│
├── advices/                           # Cross-cutting response/exception handling
│   ├── ApiError.java                  # Error payload (status, message, subErrors)
│   ├── ApiResponse.java               # Unified response wrapper with timestamp
│   ├── GlobalExceptionHandler.java    # @RestControllerAdvice — maps exceptions to HTTP codes
│   └── GlobalResponseHandler.java     # Wraps all successful responses in ApiResponse<T>
│
├── config/
│   ├── MapperConfig.java              # ModelMapper Spring bean
│   ├── OpenApiConfig.java             # Swagger / OpenAPI configuration
│   └── StripeConfig.java              # Stripe SDK initializer (reads secret key)
│
├── controller/
│   ├── AuthController.java            # /auth — signup, login, token refresh
│   ├── HotelBrowseController.java     # /hotels — public search & hotel info
│   ├── HotelController.java           # /admin/hotels — CRUD + reports (HOTEL_MANAGER)
│   ├── RoomAdminController.java       # /admin/hotels/{id}/rooms — room CRUD
│   ├── InventoryController.java       # /admin/inventory — view & update inventory
│   ├── HotelBookingController.java    # /bookings — full booking flow
│   ├── UserController.java            # /users — profile, my bookings
│   └── WebhookController.java         # /webhook/payment — Stripe event listener
│
├── dto/                               # Data Transfer Objects (request & response)
│   ├── BookingDto / BookingRequestDto
│   ├── GuestDto
│   ├── HotelDto / HotelInfoDto / HotelPriceDto / HotelReportDto / HotelSearchRequestDto
│   ├── InventoryDto / UpdateInventoryRequestDto
│   ├── LoginDto / LoginResponseDto / SignUpRequestDto
│   ├── ProfileUpdateRequestDto / UserDto
│   └── RoomDto
│
├── entity/                            # JPA entities
│   ├── Booking.java
│   ├── Guest.java
│   ├── Hotel.java
│   ├── HotelContactInfo.java          # @Embeddable — embedded in Hotel
│   ├── HotelMinPrice.java             # Cheapest room price per hotel per day (search index)
│   ├── Inventory.java                 # Per-room per-day availability record
│   ├── Room.java
│   ├── User.java                      # Implements UserDetails
│   └── enums/
│       ├── BookingStatus.java         # RESERVED → GUEST_ADDED → PAYMENTS_PENDING → CONFIRMED / CANCELLED / EXPIRED
│       ├── Gender.java
│       ├── PaymentStatus.java
│       └── Role.java                  # GUEST, HOTEL_MANAGER
│
├── exception/
│   ├── PaymentValidationException.java
│   ├── ResourceNotFoundException.java
│   └── UnAuthorisedException.java
│
├── repository/                        # Spring Data JPA interfaces
│   ├── BookingRepository.java
│   ├── GuestRepository.java
│   ├── HotelMinPriceRepository.java   # Contains paginated hotel search query
│   ├── HotelRepository.java
│   ├── InventoryRepository.java       # Custom locking queries for concurrent booking
│   ├── RoomRepository.java
│   └── UserRepository.java
│
├── security/
│   ├── AuthService.java               # signUp, login, refreshToken logic
│   ├── JwtAuthFilter.java             # OncePerRequestFilter — validates Bearer token
│   ├── JwtService.java                # Token generation & parsing (HMAC-SHA)
│   └── WebSecurityConfig.java         # SecurityFilterChain, RBAC, BCrypt
│
├── service/                           # Service interfaces
│   ├── BookingService.java
│   ├── CheckoutService.java
│   ├── HotelService.java
│   ├── InventoryService.java
│   ├── RoomService.java
│   └── UserService.java
│
├── service/impl/                      # Service implementations
│   ├── BookingServiceImpl.java        # Core booking orchestration + Stripe refunds
│   ├── CheckoutServiceImpl.java       # Creates Stripe Checkout Session
│   ├── HotelServiceImpl.java          # Hotel CRUD, activation, inventory init
│   ├── InventoryServiceImpl.java      # Search, locking, update
│   ├── PricingUpdateService.java      # Hourly scheduler — updates prices in bulk
│   ├── RoomServiceImpl.java           # Room CRUD + inventory lifecycle
│   └── UserServiceImpl.java           # Profile management
│
├── strategy/                          # Decorator-based dynamic pricing
│   ├── PricingStrategy.java           # Interface: calculatePrice(Inventory)
│   ├── BasePricingStrategy.java       # Returns room.basePrice
│   ├── SurgePricingStrategy.java      # × inventory.surgeFactor
│   ├── OccupancyPricingStrategy.java  # ×1.2 when occupancy > 80%
│   ├── UrgencyPricingStrategy.java    # ×1.15 when check-in is within 7 days
│   ├── HolidayPricingStrategy.java    # ×1.25 on holidays
│   └── PricingService.java            # Chains all strategies; calculates total for a stay
│
└── util/
    └── AppUtils.java                  # getCurrentUser() from SecurityContextHolder
```

---

## 🗄️ Data Model

### Entity Relationship Diagram

```mermaid
erDiagram
    USER {
        bigint id PK
        string email UK
        string password
        string name
        date dateOfBirth
        string gender
    }

    USER_ROLES {
        bigint user_id FK
        string role
    }

    HOTEL {
        bigint id PK
        string name
        string city
        text[] photos
        text[] amenities
        boolean active
        string address
        string phoneNumber
        string email
        string location
        bigint owner_id FK
    }

    ROOM {
        bigint id PK
        bigint hotel_id FK
        string type
        decimal basePrice
        text[] photos
        text[] amenities
        int totalCount
        int capacity
    }

    INVENTORY {
        bigint id PK
        bigint hotel_id FK
        bigint room_id FK
        date date
        int totalCount
        int bookedCount
        int reservedCount
        decimal surgeFactor
        decimal price
        string city
        boolean closed
    }

    HOTEL_MIN_PRICE {
        bigint id PK
        bigint hotel_id FK
        date date
        decimal price
    }

    BOOKING {
        bigint id PK
        bigint hotel_id FK
        bigint room_id FK
        bigint user_id FK
        int roomsCount
        date checkInDate
        date checkOutDate
        string bookingStatus
        decimal amount
        string paymentSessionId UK
    }

    GUEST {
        bigint id PK
        bigint user_id FK
        string name
        string gender
        int age
    }

    BOOKING_GUEST {
        bigint booking_id FK
        bigint guest_id FK
    }

    USER ||--o{ USER_ROLES : "has"
    USER ||--o{ HOTEL : "owns"
    USER ||--o{ BOOKING : "makes"
    USER ||--o{ GUEST : "manages"
    HOTEL ||--o{ ROOM : "contains"
    HOTEL ||--o{ INVENTORY : "tracks"
    HOTEL ||--o{ HOTEL_MIN_PRICE : "indexes"
    HOTEL ||--o{ BOOKING : "has"
    ROOM ||--o{ INVENTORY : "per day"
    ROOM ||--o{ BOOKING : "booked in"
    BOOKING ||--o{ BOOKING_GUEST : "includes"
    GUEST ||--o{ BOOKING_GUEST : "listed in"
```

### 🔑 Unique Constraints

- `Inventory`: `(hotel_id, room_id, date)` — one record per room per day
- `Booking.paymentSessionId`: unique Stripe session reference
- `User.email`: unique login identifier

---

## 💰 Dynamic Pricing Engine

Pricing uses the **Decorator pattern**. Each strategy wraps the previous one and adds a multiplier on top. `PricingService` assembles the full chain at runtime.

### Strategy Chain Diagram

```mermaid
flowchart LR
    INV["📦 Inventory\n(room + date)"]

    subgraph Chain["Decorator Chain  —  outermost applied last"]
        B["BasePricingStrategy\n─────────────────\nreturns room.basePrice"]
        S["SurgePricingStrategy\n─────────────────\n× surgeFactor\n(set by admin)"]
        O["OccupancyPricingStrategy\n─────────────────\n× 1.2\nif booked > 80%"]
        U["UrgencyPricingStrategy\n─────────────────\n× 1.15\nif check-in ≤ 7 days"]
        H["HolidayPricingStrategy\n─────────────────\n× 1.25\non holidays"]
    end

    FINAL["💵 Final\nDynamic Price"]

    INV --> B --> S --> O --> U --> H --> FINAL

    style B fill:#e8f5e9,stroke:#4caf50
    style S fill:#fff9c4,stroke:#ffeb3b
    style O fill:#fff3e0,stroke:#ff9800
    style U fill:#fce4ec,stroke:#e91e63
    style H fill:#f3e5f5,stroke:#9c27b0
    style FINAL fill:#e3f2fd,stroke:#2196f3
```

### Worst-Case Multiplier Example

> Base price ₹1,000 × surgeFactor 1.5 × occupancy 1.2 × urgency 1.15 × holiday 1.25 = **₹2,587.50**

### Pricing Update Flow

```mermaid
sequenceDiagram
    participant Cron as ⏰ Cron  (every hour)
    participant PUS as PricingUpdateService
    participant IR as InventoryRepository
    participant HMPR as HotelMinPriceRepository
    participant PS as PricingService

    Cron->>PUS: updatePrice()
    loop Batch of 100 hotels
        PUS->>IR: findByHotelAndDateBetween(today, +1 year)
        IR-->>PUS: List<Inventory>
        loop Each Inventory record
            PUS->>PS: calculateDynamicPricing(inventory)
            PS-->>PUS: new price
            PUS->>PUS: inventory.setPrice(newPrice)
        end
        PUS->>IR: saveAll(inventoryList)
        PUS->>PUS: groupByDate → find min price per day
        PUS->>HMPR: saveAll(hotelMinPrices)
    end
```

---

## 🔐 Authentication & Authorization

### 🎟️ JWT Token Strategy

| Token | Expiry | Storage |
|---|---|---|
| Access Token | 1 hour | Response body (`LoginResponseDto`) |
| Refresh Token | 6 months | HttpOnly cookie (`refreshToken`) |

Tokens are signed with HMAC-SHA using the key from `jwt.secretKey`.

### Login & Token Refresh Flow

```mermaid
sequenceDiagram
    actor User
    participant AC as AuthController
    participant AS as AuthService
    participant AM as AuthenticationManager
    participant JS as JwtService
    participant DB as UserRepository

    User->>AC: POST /auth/login {email, password}
    AC->>AS: login(loginDto)
    AS->>AM: authenticate(UsernamePasswordToken)
    AM->>DB: loadUserByUsername(email)
    DB-->>AM: User entity
    AM-->>AS: Authentication (success)
    AS->>JS: generateAccessToken(user)
    JS-->>AS: accessToken (1h)
    AS->>JS: generateRefreshToken(user)
    JS-->>AS: refreshToken (6 months)
    AS-->>AC: [accessToken, refreshToken]
    AC-->>User: body: {accessToken}\ncookie: refreshToken (HttpOnly)

    Note over User,DB: ─── Token Refresh ───
    User->>AC: POST /auth/refresh (cookie)
    AC->>JS: getUserIdFromToken(refreshToken)
    JS-->>AC: userId
    AC->>DB: findById(userId)
    DB-->>AC: User entity
    AC->>JS: generateAccessToken(user)
    JS-->>AC: new accessToken
    AC-->>User: body: {accessToken}
```

### JWT Request Filter Flow

```mermaid
flowchart TD
    REQ["Incoming HTTP Request"] --> F["JwtAuthFilter"]
    F --> CHK{"Authorization header\npresent & starts\nwith 'Bearer '?"}
    CHK -- No --> PASS["Pass through\n(unauthenticated)"]
    CHK -- Yes --> EXT["Extract JWT token"]
    EXT --> PARSE["JwtService.getUserIdFromToken()"]
    PARSE --> VALID{"Token\nvalid?"}
    VALID -- No --> ERR["401 Unauthorized\n(JwtException)"]
    VALID -- Yes --> LOAD["UserRepository.findById(userId)"]
    LOAD --> SET["Set Authentication\nin SecurityContextHolder"]
    SET --> RBAC["WebSecurityConfig\nRBAC check"]
    RBAC --> ALLOW{"Role\nmatches?"}
    ALLOW -- Yes --> CTRL["Controller"]
    ALLOW -- No --> DENY["403 Forbidden"]

    style ERR fill:#f8d7da,stroke:#dc3545
    style DENY fill:#f8d7da,stroke:#dc3545
    style CTRL fill:#d1e7dd,stroke:#198754
```

### 👥 Roles & Access

```mermaid
flowchart LR
    subgraph Public["🌍 Public  (no token required)"]
        P1["GET /hotels/search"]
        P2["GET /hotels/{id}/info"]
        P3["POST /auth/signup"]
        P4["POST /auth/login"]
        P5["POST /auth/refresh"]
    end

    subgraph Guest["🧳 GUEST role  (any authenticated user)"]
        G1["POST /bookings/**"]
        G2["GET /users/profile"]
        G3["PATCH /users/profile"]
        G4["GET /users/my-bookings"]
    end

    subgraph Manager["🏨 HOTEL_MANAGER role  (granted on first hotel creation)"]
        M1["POST /admin/hotels"]
        M2["GET /admin/hotels/**"]
        M3["PUT /admin/hotels/**"]
        M4["DELETE /admin/hotels/**"]
        M5["PATCH /admin/hotels/{id}/activate"]
        M6["POST /admin/hotels/{id}/rooms"]
        M7["PATCH /admin/inventory/**"]
        M8["GET /admin/hotels/{id}/reports"]
    end

    GUEST_ROLE(["👤 GUEST"])
    MANAGER_ROLE(["🏨 HOTEL_MANAGER"])

    GUEST_ROLE --> Public
    GUEST_ROLE --> Guest
    MANAGER_ROLE --> Public
    MANAGER_ROLE --> Guest
    MANAGER_ROLE --> Manager
```

---

## 🌐 API Reference

> **Base path**: `/api/v1`  
> All responses are wrapped in `ApiResponse<T>` with a `timestamp` field.

### Full API Map

```mermaid
flowchart LR
    BASE(["🌐 /api/v1"])

    BASE --> AUTH["🔑 /auth"]
    AUTH --> A1["POST  /signup"]
    AUTH --> A2["POST  /login"]
    AUTH --> A3["POST  /refresh"]

    BASE --> HOTELS["🔍 /hotels"]
    HOTELS --> H1["GET  /search"]
    HOTELS --> H2["GET  /{hotelId}/info"]

    BASE --> ADMINH["🏨 /admin/hotels"]
    ADMINH --> AH1["POST  /"]
    ADMINH --> AH2["GET  /"]
    ADMINH --> AH3["GET  /{hotelId}"]
    ADMINH --> AH4["PUT  /{hotelId}"]
    ADMINH --> AH5["DELETE  /{hotelId}"]
    ADMINH --> AH6["PATCH  /{hotelId}/activate"]
    ADMINH --> AH7["GET  /{hotelId}/bookings"]
    ADMINH --> AH8["GET  /{hotelId}/reports"]

    BASE --> ROOMS["🛏️ /admin/hotels/{hotelId}/rooms"]
    ROOMS --> R1["POST  /"]
    ROOMS --> R2["GET  /"]
    ROOMS --> R3["GET  /{roomId}"]
    ROOMS --> R4["PUT  /{roomId}"]
    ROOMS --> R5["DELETE  /{roomId}"]

    BASE --> INV["📦 /admin/inventory"]
    INV --> I1["GET  /rooms/{roomId}"]
    INV --> I2["PATCH  /rooms/{roomId}"]

    BASE --> BOOK["📋 /bookings"]
    BOOK --> B1["POST  /init"]
    BOOK --> B2["POST  /{bookingId}/addGuests"]
    BOOK --> B3["POST  /{bookingId}/payments"]
    BOOK --> B4["POST  /{bookingId}/cancel"]
    BOOK --> B5["POST  /{bookingId}/status"]

    BASE --> USERS["👤 /users"]
    USERS --> U1["GET  /profile"]
    USERS --> U2["PATCH  /profile"]
    USERS --> U3["GET  /my-bookings"]

    BASE --> WH["🪝 /webhook"]
    WH --> W1["POST  /payment"]

    style AUTH fill:#cfe2ff,stroke:#0d6efd
    style HOTELS fill:#d1e7dd,stroke:#198754
    style ADMINH fill:#fff3cd,stroke:#ffc107
    style ROOMS fill:#fff3cd,stroke:#ffc107
    style INV fill:#fff3cd,stroke:#ffc107
    style BOOK fill:#f8d7da,stroke:#dc3545
    style USERS fill:#e2e3e5,stroke:#6c757d
    style WH fill:#f3e5f5,stroke:#9c27b0
```

---

### 🔑 Auth — `/api/v1/auth`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/auth/signup` | Public | Register a new user. Returns `UserDto`. |
| POST | `/auth/login` | Public | Login. Returns access token in body; refresh token set as HttpOnly cookie. |
| POST | `/auth/refresh` | Public (cookie) | Exchange refresh token cookie for a new access token. |

**Signup request body**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "secret123"
}
```

**Login response**
```json
{
  "accessToken": "<jwt>"
}
```

---

### 🔍 Hotel Browse (Public) — `/api/v1/hotels`

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/hotels/search` | Public | Paginated hotel search by city and date range. |
| GET | `/hotels/{hotelId}/info` | Public | Full hotel details with all rooms. |

**Search request body**
```json
{
  "city": "Mumbai",
  "startDate": "2025-12-20",
  "endDate": "2025-12-25",
  "roomsCount": 1,
  "page": 0,
  "size": 10
}
```

---

### 🏨 Hotel Admin — `/api/v1/admin/hotels`

> Requires `HOTEL_MANAGER` role. Hotel managers can only operate on their own hotels.

| Method | Endpoint | Description |
|---|---|---|
| POST | `/admin/hotels` | Create a hotel (sets creator as HOTEL_MANAGER). |
| GET | `/admin/hotels` | List all hotels owned by the current user. |
| GET | `/admin/hotels/{hotelId}` | Get hotel details. |
| PUT | `/admin/hotels/{hotelId}` | Update hotel. |
| DELETE | `/admin/hotels/{hotelId}` | Delete hotel and all its rooms/inventories. |
| PATCH | `/admin/hotels/{hotelId}/activate` | Activate hotel — initializes room inventory for 1 year. |
| GET | `/admin/hotels/{hotelId}/bookings` | List all bookings for the hotel. |
| GET | `/admin/hotels/{hotelId}/reports?startDate=&endDate=` | Revenue report (defaults to last 30 days). |

---

### 🛏️ Room Admin — `/api/v1/admin/hotels/{hotelId}/rooms`

> Requires `HOTEL_MANAGER` role.

| Method | Endpoint | Description |
|---|---|---|
| POST | `/admin/hotels/{hotelId}/rooms` | Create a room inside the hotel. |
| GET | `/admin/hotels/{hotelId}/rooms` | List all rooms in the hotel. |
| GET | `/admin/hotels/{hotelId}/rooms/{roomId}` | Get a specific room. |
| PUT | `/admin/hotels/{hotelId}/rooms/{roomId}` | Update room details. |
| DELETE | `/admin/hotels/{hotelId}/rooms/{roomId}` | Delete room and its inventory records. |

---

### 📦 Inventory Admin — `/api/v1/admin/inventory`

> Requires `HOTEL_MANAGER` role. Only the hotel owner can manage its room inventory.

| Method | Endpoint | Description |
|---|---|---|
| GET | `/admin/inventory/rooms/{roomId}` | Get full inventory list for a room (all dates). |
| PATCH | `/admin/inventory/rooms/{roomId}` | Update inventory for a date range — set `closed` flag and/or `surgeFactor`. |

**Update inventory request body**
```json
{
  "startDate": "2025-12-24",
  "endDate": "2025-12-26",
  "closed": false,
  "surgeFactor": 1.5
}
```

---

### 📋 Bookings — `/api/v1/bookings`

> Requires authentication. Users can only interact with their own bookings.

| Method | Endpoint | Description |
|---|---|---|
| POST | `/bookings/init` | Initialize a booking — reserves inventory and calculates price. |
| POST | `/bookings/{bookingId}/addGuests` | Add guests to a reserved booking. |
| POST | `/bookings/{bookingId}/payments` | Create a Stripe Checkout Session. Returns `sessionUrl`. |
| POST | `/bookings/{bookingId}/cancel` | Cancel a confirmed booking. Triggers Stripe refund. |
| POST | `/bookings/{bookingId}/status` | Get current booking status. |

**Init booking request body**
```json
{
  "hotelId": 1,
  "roomId": 2,
  "checkInDate": "2025-12-20",
  "checkOutDate": "2025-12-25",
  "roomsCount": 1
}
```

> ⚠️ **Note**: Bookings expire after **10 minutes** if payment is not completed.

---

### 👤 User Profile — `/api/v1/users`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/users/profile` | Get the logged-in user's profile. |
| PATCH | `/users/profile` | Update profile details. |
| GET | `/users/my-bookings` | List all bookings made by the logged-in user. |

---

### 🪝 Stripe Webhook — `/api/v1/webhook/payment`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/webhook/payment` | Receives Stripe events. Handles `checkout.session.completed` to confirm booking. |

The webhook validates the `Stripe-Signature` header against `stripe.webhook.secret` before processing.

---

## 🔄 Booking Lifecycle

### State Machine

```mermaid
stateDiagram-v2
    direction LR

    [*] --> RESERVED : POST /bookings/init\n(inventory.reservedCount++)

    RESERVED --> GUEST_ADDED : POST /bookings/{id}/addGuests\n(guests saved & linked)

    GUEST_ADDED --> PAYMENTS_PENDING : POST /bookings/{id}/payments\n(Stripe Session created)

    PAYMENTS_PENDING --> CONFIRMED : Stripe Webhook\ncheckout.session.completed\n(inventory.bookedCount++\nreservedCount--)

    CONFIRMED --> CANCELLED : POST /bookings/{id}/cancel\n(inventory.bookedCount--\nStripe refund issued)

    RESERVED --> EXPIRED : No payment\nwithin 10 minutes
    GUEST_ADDED --> EXPIRED : No payment\nwithin 10 minutes
    PAYMENTS_PENDING --> EXPIRED : No payment\nwithin 10 minutes

    CONFIRMED --> [*]
    CANCELLED --> [*]
    EXPIRED --> [*]
```

### Full Booking Sequence Diagram

```mermaid
sequenceDiagram
    actor Guest as 👤 Guest
    participant BC as HotelBookingController
    participant BS as BookingService
    participant IR as InventoryRepository
    participant PS as PricingService
    participant CS as CheckoutService
    participant Stripe as 💳 Stripe
    participant WC as WebhookController

    Note over Guest,WC: ─── Step 1: Initialize Booking ───
    Guest->>BC: POST /bookings/init
    BC->>BS: initialiseBooking(request)
    BS->>IR: findAndLockAvailableInventory(roomId, dates, count)
    IR-->>BS: List<Inventory> (pessimistic lock)
    BS->>PS: calculateTotalPrice(inventoryList)
    PS-->>BS: totalAmount
    BS->>IR: initBooking() — reservedCount++
    BS-->>Guest: BookingDto {status: RESERVED}

    Note over Guest,WC: ─── Step 2: Add Guests ───
    Guest->>BC: POST /bookings/{id}/addGuests
    BC->>BS: addGuests(bookingId, guestList)
    BS->>BS: validate ownership & expiry
    BS-->>Guest: BookingDto {status: GUEST_ADDED}

    Note over Guest,WC: ─── Step 3: Initiate Payment ───
    Guest->>BC: POST /bookings/{id}/payments
    BC->>BS: initiatePayments(bookingId)
    BS->>CS: getCheckoutSession(booking, successUrl, failureUrl)
    CS->>Stripe: Customer.create()
    CS->>Stripe: Session.create(lineItems, mode=PAYMENT)
    Stripe-->>CS: Session {id, url}
    CS-->>BS: sessionUrl
    BS->>BS: booking.setPaymentSessionId()\nbooking.status = PAYMENTS_PENDING
    BS-->>Guest: {sessionUrl}

    Note over Guest,WC: ─── Step 4: Stripe Webhook ───
    Guest->>Stripe: Complete payment on Stripe page
    Stripe->>WC: POST /webhook/payment\n(checkout.session.completed)
    WC->>BS: capturePayment(event)
    BS->>IR: findAndLockReservedInventory()
    BS->>IR: confirmBooking() — bookedCount++, reservedCount--
    BS->>BS: booking.status = CONFIRMED

    Note over Guest,WC: ─── Step 5 (optional): Cancel ───
    Guest->>BC: POST /bookings/{id}/cancel
    BC->>BS: cancelBooking(bookingId)
    BS->>IR: cancelBooking() — bookedCount--
    BS->>Stripe: Refund.create(paymentIntentId)
    Stripe-->>BS: Refund confirmed
    BS->>BS: booking.status = CANCELLED
    BS-->>Guest: 204 No Content
```

---

## ⏰ Scheduled Jobs

```mermaid
flowchart TD
    CRON(["⏰ Cron Trigger\nevery hour"])

    CRON --> FETCH["Fetch hotels\nin batches of 100\n(paginated)"]
    FETCH --> LOOP{"More\nhotels?"}

    LOOP -- Yes --> HOTEL["Process hotel"]
    LOOP -- No --> DONE(["✅ Done"])

    HOTEL --> INV["Fetch all Inventory\nrecords for hotel\n(today → +1 year)"]
    INV --> PRICE["For each Inventory:\nrecalculate dynamic price\nvia PricingService chain"]
    PRICE --> SAVEINV["inventoryRepository\n.saveAll()"]
    SAVEINV --> GROUP["Group by date\nfind min price per day"]
    GROUP --> SAVEMIN["hotelMinPriceRepository\n.saveAll()\n(upsert)"]
    SAVEMIN --> LOOP

    style CRON fill:#fff3cd,stroke:#ffc107
    style DONE fill:#d1e7dd,stroke:#198754
```

| Job | Schedule | What it does |
|---|---|---|
| `PricingUpdateService.updatePrice()` | Every hour (`0 0 * * * *`) | Processes all hotels in batches of 100. Recomputes dynamic prices for all inventory records up to 1 year ahead, then rebuilds the `HotelMinPrice` search index. |

---

## 🚨 Error Handling

All errors return a consistent `ApiResponse<T>` envelope:

```json
{
  "timeStamp": "2025-12-01T10:30:00",
  "data": null,
  "error": {
    "status": "NOT_FOUND",
    "message": "Hotel not found with ID : 99",
    "subErrors": []
  }
}
```

### Exception Mapping

```mermaid
flowchart LR
    EX["Exception thrown\nin any layer"]

    EX --> GEH["@RestControllerAdvice\nGlobalExceptionHandler"]

    GEH --> RNF["ResourceNotFoundException\n→ 404 Not Found"]
    GEH --> AUTH["AuthenticationException\n→ 401 Unauthorized"]
    GEH --> JWT["JwtException\n→ 401 Unauthorized"]
    GEH --> ACCESS["AccessDeniedException\n→ 403 Forbidden"]
    GEH --> GENERIC["Exception (fallback)\n→ 500 Internal Server Error"]

    RNF & AUTH & JWT & ACCESS & GENERIC --> WRAP["Wrap in\nApiResponse<ApiError>"]
    WRAP --> CLIENT["JSON Response\nto Client"]

    style RNF fill:#fff3cd,stroke:#ffc107
    style AUTH fill:#f8d7da,stroke:#dc3545
    style JWT fill:#f8d7da,stroke:#dc3545
    style ACCESS fill:#fce4ec,stroke:#e91e63
    style GENERIC fill:#f8d7da,stroke:#dc3545
    style CLIENT fill:#d1e7dd,stroke:#198754
```

| Exception | HTTP Status |
|---|---|
| `ResourceNotFoundException` | 404 Not Found |
| `AuthenticationException` | 401 Unauthorized |
| `JwtException` | 401 Unauthorized |
| `AccessDeniedException` | 403 Forbidden |
| `Exception` (fallback) | 500 Internal Server Error |

---

## 🔧 Configuration

All sensitive values are injected via environment variables.

```yaml
# src/main/resources/application.yaml

spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

server:
  servlet:
    context-path: /api/v1

jwt:
  secretKey: <your-secret-key>   # min 256-bit recommended

frontend:
  url: http://localhost:8080     # Stripe redirect after payment

stripe:
  secret:
    key: ${STRIPE_SECRET_KEY}
  webhook:
    secret: ${STRIPE_WEBHOOK_SECRET_KEY}
```

### 📋 Required Environment Variables

| Variable | Description |
|---|---|
| `DB_URL` | PostgreSQL JDBC URL e.g. `jdbc:postgresql://localhost:5432/airbnb` |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `STRIPE_SECRET_KEY` | Stripe secret key (`sk_test_...`) |
| `STRIPE_WEBHOOK_SECRET_KEY` | Stripe webhook signing secret (`whsec_...`) |

---

## 🚀 Running Locally

### ✅ Prerequisites

- Java 21
- Maven 3.8+
- PostgreSQL (running, with a database created)
- Stripe account (test mode keys)

### 🪜 Steps

**1. Clone the repository**
```bash
git clone <repo-url>
cd airBnbApp
```

**2. Set environment variables**
```bash
# Windows
set DB_URL=jdbc:postgresql://localhost:5432/airbnb
set DB_USERNAME=postgres
set DB_PASSWORD=your_password
set STRIPE_SECRET_KEY=sk_test_...
set STRIPE_WEBHOOK_SECRET_KEY=whsec_...

# Linux / macOS
export DB_URL=jdbc:postgresql://localhost:5432/airbnb
export DB_USERNAME=postgres
export DB_PASSWORD=your_password
export STRIPE_SECRET_KEY=sk_test_...
export STRIPE_WEBHOOK_SECRET_KEY=whsec_...
```

**3. Run the application**
```bash
./mvnw spring-boot:run
```

**4. Access Swagger UI**
```
http://localhost:8080/api/v1/swagger-ui/index.html
```

**5. Set up Stripe webhook (for local testing)**
```bash
stripe listen --forward-to localhost:8080/api/v1/webhook/payment
```

---

## 🐳 Docker

A multi-stage Dockerfile is included. The build stage uses the full Maven + JDK 21 image; the runtime stage uses a slim JRE-only image.

### Docker Build Pipeline

```mermaid
flowchart LR
    subgraph Build["🔨 Stage 1 — Build  (maven:3.9.6-eclipse-temurin-21)"]
        B1["COPY pom.xml"] --> B2["mvn dependency:go-offline"]
        B2 --> B3["COPY src/"]
        B3 --> B4["mvn clean package -DskipTests"]
        B4 --> B5["target/*.jar"]
    end

    subgraph Run["🚀 Stage 2 — Run  (eclipse-temurin:21-jre)"]
        R1["COPY --from=build\napp.jar"]
        R1 --> R2["EXPOSE 8080"]
        R2 --> R3["ENTRYPOINT\njava -jar app.jar"]
    end

    B5 -->|"copy artifact only\n(no Maven / src)"| R1

    style Build fill:#e8f5e9,stroke:#4caf50
    style Run fill:#e3f2fd,stroke:#2196f3
```

### 🔨 Build and Run

```bash
# Build the image
docker build -t airbnb-app .

# Run the container
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/airbnb \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=your_password \
  -e STRIPE_SECRET_KEY=sk_test_... \
  -e STRIPE_WEBHOOK_SECRET_KEY=whsec_... \
  airbnb-app
```

> 💡 If your PostgreSQL is running on the host machine, use `host.docker.internal` (Docker Desktop) or the host's IP address as the DB host.

---

<div align="center">

Made with ❤️ by **Adnan Umar** &nbsp;·&nbsp; `com.adnanumar.projects`

</div>
