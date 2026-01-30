package com.adnanumar.projects.airBnbApp.controller;

import com.adnanumar.projects.airBnbApp.service.BookingService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class WebhookController {

    final BookingService bookingService;

    @Value("${stripe.webhook.secret}")
    String endpointSecret;

    @PostMapping("/payment")
    public ResponseEntity<Void> capturePayment(@RequestBody String payload, @RequestHeader("Stripe-Signature") String signHeader) {
        try {
            Event event = Webhook.constructEvent(payload, signHeader, endpointSecret);
            bookingService.capturePayment(event);
            return ResponseEntity.noContent().build();
        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }
    }

}
