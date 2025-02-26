package br.dev.eliangela.invoice_reminder.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;



@Configuration
@OpenAPIDefinition
public class DocsConfiguration {

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme())
                        .addSchemas("GeoJsonCrsPropertiesSchema", new Schema<Map<String, String>>()
                                .addProperty("name", new StringSchema().example("urn:ogc:def:crs:EPSG::4674")))
                        .addSchemas("TagsSchema", new Schema<Map<String, String>>()
                                .addProperty("safra", new StringSchema().example("20242025"))
                                .addProperty("matricula", new StringSchema().example("99999"))))
                .info(new Info().title("API Sustainability")
                        .description("Gerenciamento de GeoJSON para sustentabilidade")
                        .version("1.0")
                        .contact(new Contact().name("Eliangela Menezes Palharini Paes")
                                .email("emenezes@coamo.com.br"))
                        .license(new License().name("Equipe ASTEC - Coamo Agroindustrial Cooperativa")));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

}
