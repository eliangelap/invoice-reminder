package br.dev.eliangela.invoice_reminder.core.model.repository.general;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.eliangela.invoice_reminder.core.model.schema.general.OptionSchema;

import java.util.List;
import java.util.Optional;


public interface OptionRepository  extends JpaRepository<OptionSchema, Integer> {

    Optional<List<OptionSchema>> findByName(String name);

}
