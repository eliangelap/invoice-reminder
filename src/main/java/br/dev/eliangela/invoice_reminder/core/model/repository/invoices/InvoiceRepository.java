package br.dev.eliangela.invoice_reminder.core.model.repository.invoices;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.eliangela.invoice_reminder.core.model.schema.invoices.InvoiceSchema;

public interface InvoiceRepository  extends JpaRepository<InvoiceSchema, Integer> {
    
    Optional<List<InvoiceSchema>> findByClientid(Integer clientid);

    Optional<List<InvoiceSchema>> findByDuedateBefore(LocalDate today);

    Optional<List<InvoiceSchema>> findByDuedateBetween(LocalDate today, LocalDate futureDate);

}
