package com.adnanumar.projects.airBnbApp.service;

import com.adnanumar.projects.airBnbApp.entity.Booking;
import com.stripe.exception.StripeException;

public interface CheckoutService {

    String getCheckoutSession(Booking booking, String successUrl, String failureUrl);

}
