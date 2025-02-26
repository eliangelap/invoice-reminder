package br.dev.eliangela.invoice_reminder.core.model.repository.invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.eliangela.invoice_reminder.core.model.schema.invoice.InvoiceSchema;

public interface InvoiceRepository extends JpaRepository<InvoiceSchema, Integer> {

    Optional<List<InvoiceSchema>> findByClientid(Integer clientid);

    Optional<List<InvoiceSchema>> findByDuedateBefore(LocalDate today);

    Optional<List<InvoiceSchema>> findByDuedateBetween(LocalDate today, LocalDate futureDate);

    Optional<List<InvoiceSchema>> findByStatus(Integer status);

    Optional<List<InvoiceSchema>> findByStatusAndDuedate(Integer status, LocalDate dueDate);

    Optional<List<InvoiceSchema>> findByDuedateBeforeAndStatus(LocalDate today, Integer status);

    Optional<List<InvoiceSchema>> findByDuedateBetweenAndStatus(LocalDate today, LocalDate futureDate, Integer status);

}
