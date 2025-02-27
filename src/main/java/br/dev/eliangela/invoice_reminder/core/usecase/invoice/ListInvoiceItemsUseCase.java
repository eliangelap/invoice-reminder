package br.dev.eliangela.invoice_reminder.core.usecase.invoice;

import static br.dev.eliangela.invoice_reminder.core.model.schema.invoice.ItemableTypeEnum.INVOICE;

import java.util.List;

import org.springframework.stereotype.Service;

import br.dev.eliangela.invoice_reminder.core.model.repository.invoice.InvoiceItemRepository;
import br.dev.eliangela.invoice_reminder.core.model.schema.invoice.InvoiceItemSchema;
import br.dev.eliangela.invoice_reminder.core.usecase.UseCase;
import br.dev.eliangela.invoice_reminder.web.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Service
@RequiredArgsConstructor
public class ListInvoiceItemsUseCase
        extends UseCase<ListInvoiceItemsUseCase.InputValues, ListInvoiceItemsUseCase.OutputValues> {

    private final InvoiceItemRepository invoiceItemRepository;

    @Override
    public OutputValues execute(InputValues input) {
        Integer invoiceId = input.getInvoiceId();
        List<InvoiceItemSchema> invoiceItemList = invoiceItemRepository
                .findByRelIdAndRelType(invoiceId, INVOICE.getType())
                .orElseThrow(() -> new NotFoundException("Fatura " + invoiceId + " sem itens."));

        int count = 0;
        StringBuilder textMessage = new StringBuilder("");

        for (InvoiceItemSchema invoiceItem : invoiceItemList) {
            textMessage.append("        " + (++count) + ". " + invoiceItem.getDescription() + "\n");
        }

        return new OutputValues(textMessage.toString());
    }

    @Value
    public static class InputValues implements UseCase.InputValues {
        private final Integer invoiceId;
    }

    @Value
    public static class OutputValues implements UseCase.OutputValues {
        private final String message;
    }

}