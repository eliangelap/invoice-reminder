package br.dev.eliangela.invoice_reminder.core.usecase.invoice;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import br.dev.eliangela.invoice_reminder.config.PropertiesFileConfiguration;
import br.dev.eliangela.invoice_reminder.core.model.repository.general.OptionRepository;
import br.dev.eliangela.invoice_reminder.core.model.repository.invoice.InvoiceRepository;
import br.dev.eliangela.invoice_reminder.core.model.repository.person.ClientRepository;
import br.dev.eliangela.invoice_reminder.core.model.schema.general.OptionSchema;
import br.dev.eliangela.invoice_reminder.core.model.schema.invoice.InvoiceSchema;
import br.dev.eliangela.invoice_reminder.core.model.schema.person.ClientSchema;
import br.dev.eliangela.invoice_reminder.core.usecase.UseCase;
import br.dev.eliangela.invoice_reminder.util.CurrencyUtil;
import br.dev.eliangela.invoice_reminder.util.DateTimeUtil;
import br.dev.eliangela.invoice_reminder.web.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Service
@RequiredArgsConstructor
public class CreateReminderTextUseCase
        extends UseCase<CreateReminderTextUseCase.InputValues, CreateReminderTextUseCase.OutputValues> {

    private static final String TEXT_TEMPLATE = """
            Olá, {clientName}! Tudo bem?

            Estamos passando para lembrar que a fatura relacionada aos serviços prestados {additionalMessage}.
            Agradecemos por dar uma olhadinha nisso.

            Detalhes da Fatura:
            - Número da Fatura: {invoiceNumber}
            - Data de Vencimento: {invoiceDuedate}
            - Valor Total: {invoiceTotal}
            - Serviços Prestados:
            {serviceDescription}

            Para acessar a fatura completa e fazer o pagamento, é só clicar no link: {invoiceLink}

            Obrigado pela atenção e pela colaboração! Se precisar de alguma coisa, estamos à disposição. 😊

            Atenciosamente,
            {companyName}
            """;

    private final InvoiceRepository invoiceRepository;
    private final ClientRepository clientRepository;
    private final PropertiesFileConfiguration config;
    private final ListInvoiceItemsUseCase getInvoiceItemsUseCase;
    private final OptionRepository optionRepository;

    @Override
    public OutputValues execute(InputValues input) {
        Integer invoiceId = input.getInvoiceId();
        InvoiceSchema invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new NotFoundException("Fatura " + invoiceId + " não encontrada."));

        Integer clientId = invoice.getClientid();
        ClientSchema client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NotFoundException("Cliente " + clientId + " não encontrado."));

        LocalDate dueDate = invoice.getDuedate();
        String additionalMessage = getAdditionalMessage(dueDate);

        String clientName = client.getCompany();
        String invoiceFormattedNumber = invoice.getFormattedNumber();
        String formattedInvoiceDuedate = DateTimeUtil.getFormattedDate(dueDate);
        String invoiceTotal = CurrencyUtil.getFormattedCurrency(invoice.getTotal());

        List<OptionSchema> option = optionRepository.findByName("companyname").orElse(null);
        String companyName = option != null ? option.get(0).getValue() : "";

        String serviceDescription = getInvoiceItemsUseCase
                .execute(new ListInvoiceItemsUseCase.InputValues(invoiceId))
                .getMessage();

        String invoiceLink = config.getInvoiceBaseUrl()
                .replace("{invoiceId}", String.valueOf(invoiceId))
                .replace("{invoiceHash}", invoice.getHash());

        String textMessage = TEXT_TEMPLATE.replace("{clientName}", clientName)
                .replace("{invoiceNumber}", invoiceFormattedNumber)
                .replace("{invoiceDuedate}", formattedInvoiceDuedate)
                .replace("{invoiceTotal}", invoiceTotal)
                .replace("{invoiceLink}", invoiceLink)
                .replace("{serviceDescription}", serviceDescription)
                .replace("{companyName}", companyName)
                .replace("{additionalMessage}", additionalMessage);

        return new OutputValues(textMessage);
    }

    private String getAdditionalMessage(LocalDate dueDate) {

        return dueDate.isBefore(LocalDate.now())
                ? "venceu recentemente"
                : "irá vencer em breve";
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