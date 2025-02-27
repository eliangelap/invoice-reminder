package br.dev.eliangela.invoice_reminder.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class EnvironmentConfiguration {

    private Environment environment;

    public EnvironmentConfiguration(Environment environment) {
        this.environment = environment;
    }

    public boolean isProduction() {
        String[] activeProfiles = environment.getActiveProfiles();

        if (activeProfiles.length == 0) {
            return false;
        }

        for (String profile : activeProfiles) {
            if (profile.startsWith("prod")) {
                return true;
            }
        }

        return false;
    }

    public boolean isDevelopment() {
        return !isProduction();
    }

}
