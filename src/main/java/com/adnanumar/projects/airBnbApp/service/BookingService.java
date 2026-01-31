package com.adnanumar.projects.airBnbApp.service;

import com.adnanumar.projects.airBnbApp.dto.BookingDto;
import com.adnanumar.projects.airBnbApp.dto.BookingRequestDto;
import com.adnanumar.projects.airBnbApp.dto.GuestDto;
import com.adnanumar.projects.airBnbApp.dto.HotelReportDto;
import com.stripe.model.Event;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    BookingDto initialiseBooking(BookingRequestDto bookingRequest);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestList);

    String initiatePayments(Long bookingId);

    void capturePayment(Event event);

    void cancelBooking(Long bookingId);

    String getBookingStatus(Long bookingId);

    List<BookingDto> getAllBookingsByHotelId(Long hotelId);

    HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate);

    List<BookingDto> getMyBookings();

}
