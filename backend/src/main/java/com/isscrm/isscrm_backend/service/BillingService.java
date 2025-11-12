package com.isscrm.isscrm_backend.service;

import com.isscrm.isscrm_backend.model.Billing;
import com.isscrm.isscrm_backend.model.Customer;
import com.isscrm.isscrm_backend.repository.BillingRepository;
import com.isscrm.isscrm_backend.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillingRepository billingRepository;
    private final CustomerRepository customerRepository;
    private final MikroTikSyncLogService mikroTikSyncLogService;

    /** Tüm faturaları getirir */
    public List<Billing> getAllBillings() {
        return billingRepository.findAll();
    }

    /** Tüm müşteriler için aylık fatura oluşturur */
    public List<Billing> generateMonthlyBills() {
        List<Billing> generated = new ArrayList<>();
        try {
            List<Customer> customers = customerRepository.findAll();
            LocalDateTime now = LocalDateTime.now();

            for (Customer c : customers) {
                Billing b = new Billing();
                b.setCustomer(c);
                b.setDealer(c.getDealer());
                b.setAmount(c.getTariff() != null ? c.getTariff().getPrice() : 0.0);
                b.setBillingDate(now);
                b.setStatus("PENDING");
                billingRepository.save(b);
                generated.add(b);
            }

            mikroTikSyncLogService.logSuccess(
                    "BillingService", null, "GENERATE_MONTHLY_BILLS",
                    "Generated " + generated.size() + " monthly bills successfully");
        } catch (Exception e) {
            mikroTikSyncLogService.logError(
                    "BillingService", null, "GENERATE_MONTHLY_BILLS",
                    "Error generating bills: " + e.getMessage());
            throw e;
        }
        return generated;
    }

    /** Belirli bir müşteri için fatura oluşturur */
    public Billing generateBilling(Long customerId) {
        Customer c = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));

        Billing b = new Billing();
        b.setCustomer(c);
        b.setDealer(c.getDealer());
        b.setAmount(c.getTariff() != null ? c.getTariff().getPrice() : 0.0);
        b.setBillingDate(LocalDateTime.now());
        b.setStatus("PENDING");
        return billingRepository.save(b);
    }

    /** Faturayı ödenmiş yapar */
    public Billing payBilling(Long billingId) {
        Billing b = billingRepository.findById(billingId)
                .orElseThrow(() -> new RuntimeException("Billing not found: " + billingId));
        b.setStatus("PAID");
        return billingRepository.save(b);
    }

    /** Bayiye göre faturaları getirir */
    public List<Billing> getBillingsByDealer(Long dealerId) {
        return billingRepository.findByDealerId(dealerId);
    }

    /** Müşteriye göre faturaları getirir */
    public List<Billing> getBillingsByCustomer(Long customerId) {
        return billingRepository.findByCustomerId(customerId);
    }

    /** Ödenmemiş faturaları getirir */
    public List<Billing> getUnpaidBillings() {
        return billingRepository.findByStatus("PENDING");
    }

    /** Aylık fatura özetini döner */
    public String getMonthlyBillingSummary() {
        long total = billingRepository.count();
        long unpaid = billingRepository.findByStatus("PENDING").size();
        return "Total bills: " + total + ", Unpaid: " + unpaid;
    }

    /** Tüm faturaları CSV olarak dışa aktarır */
    public String exportBillingsToCsv() {
        List<Billing> billings = billingRepository.findAll();
        String filePath = "billings_export.csv";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("ID,Customer,Dealer,Amount,Status,BillingDate\n");
            for (Billing b : billings) {
                String customerName = (b.getCustomer() != null && b.getCustomer().getFullName() != null)
                        ? b.getCustomer().getFullName()
                        : "";
                String dealerName = (b.getDealer() != null && b.getDealer().getDealerName() != null)
                        ? b.getDealer().getDealerName()
                        : "";

                writer.write(String.format("%d,%s,%s,%.2f,%s,%s\n",
                        b.getId(),
                        customerName.replace(",", " "),
                        dealerName.replace(",", " "),
                        b.getAmount(),
                        b.getStatus(),
                        b.getBillingDate() != null ? b.getBillingDate().toString() : ""));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error exporting CSV: " + e.getMessage(), e);
        }

        return filePath;
    }
}
