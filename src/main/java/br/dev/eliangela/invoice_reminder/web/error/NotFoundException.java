package br.dev.eliangela.invoice_reminder.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotFoundException() {
        this("Não encontrado");
    }

    public NotFoundException(String message) {
        super(message);
    }

}
