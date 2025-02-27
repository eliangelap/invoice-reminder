package br.dev.eliangela.invoice_reminder.core.service.schedule;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.dev.eliangela.invoice_reminder.core.model.schema.invoice.InvoiceSchema;
import br.dev.eliangela.invoice_reminder.core.usecase.invoice.CreateReminderTextUseCase;
import br.dev.eliangela.invoice_reminder.core.usecase.invoice.ListOverdueInvoiceUseCase;
import br.dev.eliangela.invoice_reminder.core.usecase.invoice.ListReminderInvoiceUseCase;
import br.dev.eliangela.invoice_reminder.core.usecase.invoice.SendWhatsappMessageUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceCheckerService {

    private final SendWhatsappMessageUseCase sendWhatsappMessageUseCase;
    private final CreateReminderTextUseCase createReminderTextUseCase;
    private final ListOverdueInvoiceUseCase listOverdueInvoiceUseCase;
    private final ListReminderInvoiceUseCase listReminderInvoiceUseCase;

    @Async
    @Scheduled(cron = "0 * * * * *")
    public void doCheckInvoices() {

        System.out.println("Checando faturas...");

        List<InvoiceSchema> overdueInvoices = listOverdueInvoiceUseCase.execute(null).getOverdueInvoicesList();
        System.out.println("Notificando faturas vencidas..." + overdueInvoices.size() + " faturas.");
        callSendMessageUseCase(overdueInvoices);

        List<InvoiceSchema> invoices = listReminderInvoiceUseCase.execute(null).getInvoicesDueList();
        System.out.println("Notificando faturas a vencer... " + invoices.size() + " faturas.");
        callSendMessageUseCase(invoices);
    }

    private void callSendMessageUseCase(List<InvoiceSchema> invoices) {
        for (InvoiceSchema invoice : invoices) {
            if (invoice.getCancelOverdueReminders() == 1) {
                System.out.println("Fatura marcada para não notificar: " + invoice.getFormattedNumber());
                continue;
            }

            String message = createReminderTextUseCase
                    .execute(new CreateReminderTextUseCase.InputValues(invoice.getId())).getMessage();

            System.out.println("Enviando notificação para cliente: " + invoice.getClientid() + " - Mensagem: " + message);

            sendWhatsappMessageUseCase
                    .execute(new SendWhatsappMessageUseCase.InputValues(invoice.getClientid(), message));
        }
    }

}
