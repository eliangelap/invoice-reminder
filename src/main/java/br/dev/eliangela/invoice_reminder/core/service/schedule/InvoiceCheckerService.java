package br.dev.eliangela.invoice_reminder.core.service.schedule;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.dev.eliangela.invoice_reminder.core.model.repository.invoices.InvoiceRepository;
import br.dev.eliangela.invoice_reminder.core.model.schema.invoices.InvoiceSchema;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceCheckerService {

    private final InvoiceRepository invoiceRepository;
    
    @Async
    @Scheduled(cron = "0 * * * * *") //cron = "0 0 10,16 * * *"
    public void doCheckOverdueInvoice() {
        List<InvoiceSchema> invoices = invoiceRepository.findByDuedateBefore(LocalDate.now()).orElse(null);

        for (InvoiceSchema invoice : invoices) {
            System.out.println("Vencida: " + invoice.getDuedate() + " " + invoice.getClientid());
        }
    }

    @Async
    @Scheduled(cron = "0 * * * * *") //cron = "0 0 10,16 * * *"
    public void doCheckInvoiceDueSoon() {
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(10);

        List<InvoiceSchema> invoices = invoiceRepository.findByDuedateBetween(today, futureDate).orElse(null);

        for (InvoiceSchema invoice : invoices) {
            System.out.println("A vencer: " + invoice.getDuedate() + " " + invoice.getClientid());
        }
    }

}
