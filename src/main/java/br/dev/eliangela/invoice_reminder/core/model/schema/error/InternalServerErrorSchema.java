package br.dev.eliangela.invoice_reminder.core.model.schema.error;

import java.util.Date;

import lombok.Data;

@Data
public class InternalServerErrorSchema {

    private Date timestamp;
    private Integer status;
    private String error;
    private String className;
    private String message;
    private String path;
    private String stacktrace;

}
