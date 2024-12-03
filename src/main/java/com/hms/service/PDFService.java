package com.hms.service;

import com.hms.entities.Property;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class PDFService {

    public void generateBookingPdf(String filePath, Property properties) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Font Settings
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            Font tableBodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, BaseColor.GRAY);

            // Header: Hotel Name
            Paragraph header = new Paragraph("Hotel Paradise Inn", titleFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            // Sub-header: Address and Date
            Paragraph address = new Paragraph("1234 Paradise Lane, Dream City, Country", subtitleFont);
            address.setAlignment(Element.ALIGN_CENTER);
            document.add(address);

            // Current Date and Time
            String currentDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
            Paragraph dateTime = new Paragraph(currentDate, subtitleFont);
            dateTime.setAlignment(Element.ALIGN_RIGHT);
            document.add(dateTime);

            // Add a Line Break
            document.add(Chunk.NEWLINE);

            // Booking Table
            PdfPTable table = new PdfPTable(6); // 6 columns
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setWidths(new float[]{1, 2, 2, 2, 2, 1}); // Column Widths

            // Table Header
            PdfPCell headerCell;
            String[] headers = {"Booking ID", "Customer Name", "Room Type", "From Date", "To Date", "Price"};
            for (String columnHeader : headers) {
                headerCell = new PdfPCell(new Phrase(columnHeader, tableHeaderFont));
                headerCell.setBackgroundColor(BaseColor.DARK_GRAY);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(headerCell);
            }

            // Table Body: Populate Booking Details
            DecimalFormat df = new DecimalFormat("#.00");

           // for (Property property : properties) {
                double priceWithGST = 4548* 1.18; // Assuming GST = 18%

                table.addCell("1"); // Booking ID
                table.addCell("John Doe"); // Customer Name
                table.addCell("Deluxe Room"); // Room Type
                table.addCell("01-12-2024"); // From Date
                table.addCell("05-12-2024"); // To Date
                table.addCell("₹ " + df.format(priceWithGST)); // Price with GST
           // }

            // Add Table to Document
            document.add(table);

            // Total Price (Example: Summing all prices)
//            double totalPrice = properties.stream()
//                    .mapToDouble(property -> 8545 * 1.18)
//                    .sum();

            Paragraph totalPriceParagraph = new Paragraph("Total Price: ₹ " + df.format(85274), subtitleFont);
            totalPriceParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalPriceParagraph);

            // Footer: Thank You Message
            Paragraph footer = new Paragraph("Thank you for choosing Hotel Paradise Inn! We look forward to serving you again.", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(20f);
            document.add(footer);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
