package com.hms.controller;

import com.hms.entities.Bookings;
import com.hms.entities.Property;
import com.hms.entities.Rooms;
import com.hms.repository.BookingsRepository;
import com.hms.repository.PropertyRepository;
import com.hms.repository.RoomsRepository;
import com.hms.service.PDFService;
import com.hms.service.S3Service;
import com.hms.service.SmsService;
import com.hms.service.WhatsAppService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
    private PDFService pdfService;
    private PropertyRepository propertyRepository;
    private BookingsRepository bookingsRepository;
    private RoomsRepository roomsRepository;
    private SmsService smsService;
    private WhatsAppService whatsAppService;
    private S3Service s3Service;


    public BookingController(PDFService pdfService, PropertyRepository propertyRepository, BookingsRepository bookingsRepository, RoomsRepository roomsRepository, SmsService smsService, WhatsAppService whatsAppService, S3Service s3Service) {
        this.pdfService = pdfService;
        this.propertyRepository = propertyRepository;
        this.bookingsRepository = bookingsRepository;
        this.roomsRepository = roomsRepository;
        this.smsService = smsService;
        this.whatsAppService = whatsAppService;
        this.s3Service = s3Service;
    }

@PostMapping("/create-booking")
public ResponseEntity<?> createBookingPdf(
        @RequestParam long propertyId,
        @RequestParam String type,
        @RequestBody Bookings bookings
) {
    try {
        Optional<Property> propertyOptional = propertyRepository.findById(propertyId);
        if (!propertyOptional.isPresent()) {
            return new ResponseEntity<>("Property not found", HttpStatus.NOT_FOUND);
        }

        Property property = propertyOptional.get();
        List<Rooms> rooms = roomsRepository.findByTypeAndProperty(type, propertyId, bookings.getFromDate(), bookings.getToDate());

        for (Rooms room : rooms) {
            if (room.getCount() == 0) {
                return new ResponseEntity<>("No Room available: " + room.getDate(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }

        for (Rooms room : rooms) {
            double totalPrice = room.getPerNightPrice() * (double) (rooms.size() - 1);
        }
        // Save booking and update room availability
        Bookings savedBooking = bookingsRepository.save(bookings);
        if (savedBooking != null) {
            for (Rooms room : rooms) {
                room.setCount(room.getCount() - 1);
                roomsRepository.save(room);
            }
        }

        String message = "Your booking is confirmed. Order ID: " + savedBooking.getId() + ". Thank you for choosing us! Your technical partner Ankush";


        // Send SMS
        String phoneNumber = "+916200767396";
        //smsService.sendSms(phoneNumber, message);

        // Send WhatsApp message
        // Generate booking PDF and upload to S3 (or any cloud storage)
        String pdfPath = "E:\\HMSBooking\\confirmation-order" + savedBooking.getId() + ".pdf";
        pdfService.generateBookingPdf(pdfPath, property);

        // Upload PDF to S3 and get public URL
        //String pdfUrl = s3Service.uploadFile(new File(pdfPath)); // Implement this method in your S3 service
        // Send WhatsApp message with PDF
        // whatsAppService.sendWhatsAppMessageWithPdf(phoneNumber, message, pdfUrl);

        return new ResponseEntity<>("Booking created and notifications sent", HttpStatus.OK);
    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>("Failed to create booking: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

}