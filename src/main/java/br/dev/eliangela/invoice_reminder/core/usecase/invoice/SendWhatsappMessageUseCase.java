package br.dev.eliangela.invoice_reminder.core.usecase.invoice;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.dev.eliangela.invoice_reminder.config.PropertiesFileConfiguration;
import br.dev.eliangela.invoice_reminder.core.model.repository.person.ClientRepository;
import br.dev.eliangela.invoice_reminder.core.model.schema.person.ClientSchema;
import br.dev.eliangela.invoice_reminder.core.model.schema.whatsapp.TextMessageSchema;
import br.dev.eliangela.invoice_reminder.core.usecase.UseCase;
import br.dev.eliangela.invoice_reminder.web.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Service
@RequiredArgsConstructor
public class SendWhatsappMessageUseCase
        extends UseCase<SendWhatsappMessageUseCase.InputValues, SendWhatsappMessageUseCase.OutputValues> {

    private final PropertiesFileConfiguration config;
    private final ClientRepository clientRepository;

    @Override
    public OutputValues execute(InputValues input) {
        ClientSchema client = clientRepository.findById(input.getClientId())
                .orElseThrow(() -> new NotFoundException("Cliente " + input.getClientId() + " não encontrado."));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + config.getWhatsappToken());

        TextMessageSchema textMessage = new TextMessageSchema(client.getPhoneNumber(), input.getMessage());

        HttpEntity<TextMessageSchema> entity = new HttpEntity<>(textMessage, headers);

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.exchange(config.getWhatsappUrl(), HttpMethod.POST, entity,
                String.class).getBody();

        return new OutputValues();
    }

    @Value
    public static class InputValues implements UseCase.InputValues {
        private final Integer clientId;
        private final String message;
    }

    @Value
    public static class OutputValues implements UseCase.OutputValues {

    }

}
