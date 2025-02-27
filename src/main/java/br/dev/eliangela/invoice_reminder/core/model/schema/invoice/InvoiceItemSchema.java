package br.dev.eliangela.invoice_reminder.core.model.schema.invoice;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tblitemable")
@Getter
public class InvoiceItemSchema {

    @Id
    private Integer id;

    private Integer relId;
    private String relType;

    @Lob
    private String description;

    @Lob
    private String longDescription;

    @Column(name = "qty")
    private BigDecimal quantity;

    @Column(name = "rate")
    private BigDecimal price;

    private String unit;
    private Integer itemOrder;

}
