package com.isscrm.isscrm_backend.controller;

import com.isscrm.isscrm_backend.model.Invoice;
import com.isscrm.isscrm_backend.repository.InvoiceRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final InvoiceRepository invoiceRepository;

    public FinanceController(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    // 📊 Tüm faturaları getir
    @GetMapping("/invoices")
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    // 📆 Tarih aralığına göre fatura getir
    @GetMapping("/invoices/range")
    public List<Invoice> getInvoicesByDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return invoiceRepository.findAll().stream()
                .filter(inv -> inv.getIssueDate() != null &&
                        !inv.getIssueDate().toLocalDate().isBefore(startDate) &&
                        !inv.getIssueDate().toLocalDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    // 💰 Toplam gelir hesabı
    @GetMapping("/total")
    public double getTotalRevenue() {
        return invoiceRepository.findAll().stream()
                .mapToDouble(Invoice::getAmount)
                .sum();
    }

    // 🧾 PDF çıktısı oluştur
    @GetMapping("/invoices/pdf")
    public ResponseEntity<byte[]> generateInvoiceReport() throws DocumentException {
        List<Invoice> invoices = invoiceRepository.findAll();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        PdfWriter.getInstance(document, baos);
        document.open();

        document.add(new Paragraph("Invoice Report"));
        document.add(new Paragraph("Generated on: " + LocalDate.now()));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);

        table.addCell(new PdfPCell(new Paragraph("Invoice No")));
        table.addCell(new PdfPCell(new Paragraph("Customer Name")));
        table.addCell(new PdfPCell(new Paragraph("Amount")));
        table.addCell(new PdfPCell(new Paragraph("Status")));

        for (Invoice inv : invoices) {
            table.addCell(inv.getInvoiceNo());
            table.addCell(inv.getCustomerName());
            table.addCell(String.valueOf(inv.getAmount()));
            table.addCell(inv.getStatus());
        }

        document.add(table);
        document.close();

        byte[] pdfBytes = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "invoice_report.pdf");
        headers.setContentLength(pdfBytes.length);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    // 🧾 JSON export (isteğe bağlı)
    @GetMapping("/invoices/export")
    public ResponseEntity<String> exportJson() {
        List<Invoice> invoices = invoiceRepository.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (Invoice inv : invoices) {
            sb.append(String.format("  {\"invoiceNo\": \"%s\", \"amount\": %.2f},\n", inv.getInvoiceNo(), inv.getAmount()));
        }
        sb.append("]");
        byte[] jsonBytes = sb.toString().getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDispositionFormData("attachment", "invoices.json");

        return new ResponseEntity<>(new String(jsonBytes, StandardCharsets.UTF_8), headers, HttpStatus.OK);
    }
}
