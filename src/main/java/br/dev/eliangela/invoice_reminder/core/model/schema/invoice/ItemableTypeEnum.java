package br.dev.eliangela.invoice_reminder.core.model.schema.invoice;

public enum ItemableTypeEnum {
    
    INVOICE("invoice");

    ItemableTypeEnum(String type) {
        this.type = type;
    }

    private String type;

    public String getType() {
        return type;
    }
}
