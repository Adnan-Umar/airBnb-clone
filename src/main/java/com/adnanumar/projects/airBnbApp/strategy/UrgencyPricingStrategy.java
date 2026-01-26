package com.adnanumar.projects.airBnbApp.strategy;

import com.adnanumar.projects.airBnbApp.entity.Inventory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UrgencyPricingStrategy implements PricingStrategy {

    final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        LocalDate today = LocalDate.now();
        if (!inventory.getDate().isBefore(today) && inventory.getDate().isBefore(today.plusDays(7))) {
            price = price.multiply(BigDecimal.valueOf(1.15));
        }
        return price;
    }

}
