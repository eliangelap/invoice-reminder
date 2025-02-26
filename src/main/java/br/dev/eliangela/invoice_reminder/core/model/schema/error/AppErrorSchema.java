package br.dev.eliangela.invoice_reminder.core.model.schema.error;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class AppErrorSchema {

    private Date timestamp;
    private Integer status;
    private String error;
    private String className;
    private List<String> messages;
    private String path;

}
