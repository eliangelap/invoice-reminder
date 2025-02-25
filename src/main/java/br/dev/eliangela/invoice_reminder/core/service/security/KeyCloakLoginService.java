package br.dev.eliangela.invoice_reminder.core.service.security;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import br.dev.eliangela.invoice_reminder.config.PropertiesFileConfiguration;
import br.dev.eliangela.invoice_reminder.core.model.schema.security.KeycloakUserResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KeyCloakLoginService {

    private final PropertiesFileConfiguration propertiesFileConfiguration;

    public KeycloakUserResponse login() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", propertiesFileConfiguration.getKeycloakClientId());
        body.add("grant_type", propertiesFileConfiguration.getKeycloakGrantType());
        body.add("username", propertiesFileConfiguration.getKeycloakUsername());
        body.add("password", propertiesFileConfiguration.getKeycloakPassword());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(propertiesFileConfiguration.getKeycloakUrl(), HttpMethod.POST, entity,
                KeycloakUserResponse.class).getBody();
    }

}
