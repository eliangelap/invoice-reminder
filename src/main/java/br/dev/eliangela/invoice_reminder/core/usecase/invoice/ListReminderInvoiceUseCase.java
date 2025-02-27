package br.dev.eliangela.invoice_reminder.core.usecase.invoice;

import static br.dev.eliangela.invoice_reminder.core.model.schema.invoice.InvoiceStatusEnum.ACTIVE;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.dev.eliangela.invoice_reminder.config.PropertiesFileConfiguration;
import br.dev.eliangela.invoice_reminder.core.model.repository.invoice.InvoiceRepository;
import br.dev.eliangela.invoice_reminder.core.model.schema.invoice.InvoiceSchema;
import br.dev.eliangela.invoice_reminder.core.usecase.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Service
@RequiredArgsConstructor
public class ListReminderInvoiceUseCase
        extends UseCase<ListReminderInvoiceUseCase.InputValues, ListReminderInvoiceUseCase.OutputValues> {

    private final InvoiceRepository invoiceRepository;
    private final PropertiesFileConfiguration config;

    @Override
    public OutputValues execute(InputValues input) {

        LocalDate today = LocalDate.now();
        List<InvoiceSchema> invoicesDueList = new ArrayList<>();

        for (int day : config.getDaysSendReminderBefore()) {
            LocalDate futureDate = today.plusDays(day);

            List<InvoiceSchema> invoicesList = invoiceRepository
                    .findByStatusAndDuedate(ACTIVE.getStatus(), futureDate).orElse(null);

            invoicesDueList.addAll(invoicesList);
        }

        return new OutputValues(invoicesDueList);
    }

    @Value
    public static class InputValues implements UseCase.InputValues {
        
    }

    @Value
    public static class OutputValues implements UseCase.OutputValues {
        private final List<InvoiceSchema> invoicesDueList;
    }

}