package com.isscrm.isscrm_backend.controller;

import com.isscrm.isscrm_backend.model.Invoice;
import com.isscrm.isscrm_backend.service.InvoiceService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService service;

    public InvoiceController(InvoiceService service) {
        this.service = service;
    }

    /**
     * 📋 Tüm faturaları listeler
     */
    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        return ResponseEntity.ok(service.getAll());
    }

    /**
     * 🔍 ID’ye göre fatura getirir
     */
    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * ➕ Yeni fatura oluşturur
     */
    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) {
        Invoice created = service.createInvoice(
                invoice.getInvoiceNo(),
                invoice.getCustomerName(),
                invoice.getAmount(),
                invoice.getStatus(),
                invoice.getXmlContent()
        );
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * ✏️ Fatura güncelleme
     */
    @PutMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable Long id, @RequestBody Invoice invoice) {
        try {
            Invoice updated = service.updateInvoice(id, invoice);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * ❌ Fatura silme
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        try {
            service.deleteInvoice(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 📄 Tek bir faturayı PDF olarak indir (örnek amaçlı sahte PDF üretimi)
     */
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable Long id) {
        return service.getById(id)
                .map(invoice -> {
                    String pdfContent = "Invoice Report\n\n" +
                            "Invoice No: " + invoice.getInvoiceNo() + "\n" +
                            "Customer: " + invoice.getCustomerName() + "\n" +
                            "Amount: $" + invoice.getAmount() + "\n" +
                            "Status: " + invoice.getStatus();

                    byte[] pdfBytes = pdfContent.getBytes(StandardCharsets.UTF_8);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDisposition(
                            ContentDisposition.builder("attachment")
                                    .filename("Invoice_" + invoice.getInvoiceNo() + ".pdf", StandardCharsets.UTF_8)
                                    .build()
                    );

                    return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
