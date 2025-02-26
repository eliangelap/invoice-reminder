package br.dev.eliangela.invoice_reminder.core.model.schema.invoice;

public enum InvoiceStatusEnum {
    
    ACTIVE(1), PAID(2), CANCELED(5);

    private Integer status;

    InvoiceStatusEnum(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

}
