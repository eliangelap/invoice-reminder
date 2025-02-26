package br.dev.eliangela.invoice_reminder.core.model.repository.person;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.eliangela.invoice_reminder.core.model.schema.person.ClientSchema;

public interface ClientRepository  extends JpaRepository<ClientSchema, Integer> {

}
