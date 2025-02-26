package br.dev.eliangela.invoice_reminder.core.model.repository.invoice;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.eliangela.invoice_reminder.core.model.schema.invoice.InvoiceItemSchema;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItemSchema, Integer> {

    Optional<List<InvoiceItemSchema>> findByRelIdAndRelType(Integer invoiceId, String relType);

}
