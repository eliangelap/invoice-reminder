package br.dev.eliangela.invoice_reminder.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class PropertiesFileConfiguration {

    @Value("#{${security.white-list}}")
    private List<String> securityWhiteList;

    @Value("#{${billing.days-send-reminder-before}}")
    private List<Integer> daysSendReminderBefore;

    @Value("#{${billing.days-send-reminder-after}}")
    private List<Integer> daysSendReminderAfter;

    @Value("#{${messages.whatsapp.url}}")
    private String whatsappUrl;

    @Value("#{${messages.whatsapp.token}}")
    private String whatsappToken;

    @Value("#{${invoice.base-url}}")
    private String invoiceBaseUrl;

}
