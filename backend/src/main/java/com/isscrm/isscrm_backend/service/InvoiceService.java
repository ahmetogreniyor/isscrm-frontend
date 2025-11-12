package com.isscrm.isscrm_backend.service;

import com.isscrm.isscrm_backend.model.Invoice;
import com.isscrm.isscrm_backend.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * 📋 Tüm faturaları listeler
     */
    public List<Invoice> getAll() {
        return invoiceRepository.findAll();
    }

    /**
     * 🔍 ID’ye göre tek bir faturayı getirir
     */
    public Optional<Invoice> getById(Long id) {
        return invoiceRepository.findById(id);
    }

    /**
     * 💾 Yeni bir fatura oluşturur
     */
    public Invoice createInvoice(String invoiceNo, String customerName, Double amount, String status, String xmlContent) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNo(invoiceNo);
        invoice.setCustomerName(customerName);
        invoice.setAmount(amount);
        invoice.setStatus(status);
        invoice.setIssueDate(LocalDateTime.now());
        invoice.setXmlContent(xmlContent);

        return invoiceRepository.save(invoice);
    }

    /**
     * 🧾 Fatura kaydını günceller
     */
    public Invoice updateInvoice(Long id, Invoice updatedInvoice) {
        return invoiceRepository.findById(id).map(inv -> {
            inv.setInvoiceNo(updatedInvoice.getInvoiceNo());
            inv.setCustomerName(updatedInvoice.getCustomerName());
            inv.setAmount(updatedInvoice.getAmount());
            inv.setStatus(updatedInvoice.getStatus());
            inv.setXmlContent(updatedInvoice.getXmlContent());
            return invoiceRepository.save(inv);
        }).orElseThrow(() -> new RuntimeException("Invoice not found with ID: " + id));
    }

    /**
     * ❌ Fatura silme işlemi
     */
    public void deleteInvoice(Long id) {
        if (invoiceRepository.existsById(id)) {
            invoiceRepository.deleteById(id);
        } else {
            throw new RuntimeException("Invoice not found with ID: " + id);
        }
    }

    /**
     * 📅 Tarih aralığına göre faturaları getirir
     */
    public List<Invoice> getInvoicesBetweenDates(LocalDateTime start, LocalDateTime end) {
        return invoiceRepository.findInvoicesBetweenDates(start, end);
    }

    /**
     * 💰 Duruma göre faturaları getirir
     */
    public List<Invoice> getInvoicesByStatus(String status) {
        return invoiceRepository.findByStatus(status);
    }

    /**
     * 🧾 Tek fatura numarasına göre getirir
     */
    public Invoice getByInvoiceNo(String invoiceNo) {
        return invoiceRepository.findByInvoiceNo(invoiceNo);
    }
}
