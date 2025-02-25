package br.dev.eliangela.invoice_reminder.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class PropertiesFileConfiguration {

    @Value("#{${security.white-list}}")
    private List<String> securityWhiteList;

    public String getKeycloakClientId() {
        return "";
    }

    public String getKeycloakGrantType() {
        return "";
    }

    public String getKeycloakUsername() {
        return "";
    }

    public String getKeycloakPassword() {
        return "";
    }

    public URI getKeycloakUrl() {
        try {
            return new URI("");
        } catch (URISyntaxException e) {
            return null;
        }
    }

}
