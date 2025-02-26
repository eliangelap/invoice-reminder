package br.dev.eliangela.invoice_reminder.core.model.schema.whatsapp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TextMessageSchema {
    
    private final String number;
    private final String body;
    private Boolean saveOnTicket = true;
    private Boolean linkPreview = true;

}
