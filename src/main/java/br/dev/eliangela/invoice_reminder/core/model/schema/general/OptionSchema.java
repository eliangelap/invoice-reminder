package br.dev.eliangela.invoice_reminder.core.model.schema.general;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "tbloptions")
public class OptionSchema {
    
    @Id
    private Integer id;

    private String name;

    @Lob
    private String value;

    private Boolean autoload;
    
}
