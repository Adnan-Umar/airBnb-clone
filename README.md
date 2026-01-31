# AirBnbApp (Backend)

Spring Boot backend for an Airbnb-style booking platform. This service exposes REST APIs for authentication, hotel management, inventory, bookings, and Stripe payments.

## Tech Stack

- Java 21
- Spring Boot 4.0.1
- Spring Web MVC
- Spring Data JPA
- Spring Security + JWT
- PostgreSQL
- Stripe Java SDK
- Springdoc OpenAPI
- ModelMapper

## Project Structure (High-Level)

- Controllers: REST endpoints (auth, hotels, bookings, inventory, users, webhooks)
- Services: business logic
- Repositories: data access
- DTOs: request/response models
- Entities: JPA models

## Base URL

- Context path: /api/v1
- Example: http://localhost:8080/api/v1

## Configuration

Update values in [src/main/resources/application.yaml](src/main/resources/application.yaml) or provide environment variables:

Required environment variables:
- DB_USERNAME
- DB_PASSWORD
- STRIPE_SECRET_KEY
- STRIPE_WEBHOOK_SECRET_KEY

Additional settings already defined:
- PostgreSQL URL: jdbc:postgresql://localhost:5432/airBnbDB
- JWT secret key: jwt.secretKey

## Running the Application

Using Maven wrapper:

- Windows:
  mvnw.cmd spring-boot:run

- macOS/Linux:
  ./mvnw spring-boot:run

The API will be available at:
http://localhost:8080/api/v1

## API Overview

### Auth

- POST /auth/signup
- POST /auth/login
- POST /auth/refresh

### Hotels (Browse)

- GET /hotels/search
- GET /hotels/{hotelId}/info

### Bookings

- POST /bookings/init
- POST /bookings/{bookingId}/addGuests
- POST /bookings/{bookingId}/payments
- POST /bookings/{bookingId}/cancel
- POST /bookings/{bookingId}/status

### Admin: Hotels

- POST /admin/hotels
- GET /admin/hotels
- GET /admin/hotels/{hotelId}
- PUT /admin/hotels/{hotelId}
- DELETE /admin/hotels/{hotelId}
- PATCH /admin/hotels/{hotelId}/activate
- GET /admin/hotels/{hotelId}/bookings
- GET /admin/hotels/{hotelId}/reports?startDate=YYYY-MM-DD&endDate=YYYY-MM-DD

### Admin: Rooms

- POST /admin/hotels/{hotelId}/rooms
- GET /admin/hotels/{hotelId}/rooms
- GET /admin/hotels/{hotelId}/rooms/{roomId}
- PUT /admin/hotels/{hotelId}/rooms/{roomId}
- DELETE /admin/hotels/{hotelId}/rooms/{roomId}

### Admin: Inventory

- GET /admin/inventory/rooms/{roomId}
- PATCH /admin/inventory/rooms/{roomId}

### Users

- GET /users/profile
- PATCH /users/profile
- GET /users/my-bookings

### Stripe Webhook

- POST /webhook/payment

## OpenAPI / Swagger

If enabled by Springdoc, you can view API documentation at:
- http://localhost:8080/api/v1/swagger-ui/index.html
- http://localhost:8080/api/v1/v3/api-docs

## Notes

- This is a backend-only service. Frontend is not included.
- Authentication uses JWT access tokens and refresh tokens stored in HttpOnly cookies.
- Stripe webhook requires the configured endpoint secret and signature header.

## License

MIT License

Copyright (c) 2026

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
