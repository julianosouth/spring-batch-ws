package br.com.calcard.processor;

import org.springframework.batch.item.ItemProcessor;

import br.com.calcard.model.Person;

import java.math.BigDecimal;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(final Person person) throws Exception {

        final String name = person.getName();
        final int idConta = person.getIdAccount();
        final int idPerson = person.getIdPerson();
        final String status = person.getStatus();
        final BigDecimal limite = person.getLimite();
        final BigDecimal limiteSaque = person.getLimiteSaque() == null ? new BigDecimal(0): person.getLimiteSaque();
        final String cpf = person.getCpf();

        final Person transformedPerson = new Person();

        transformedPerson.setName(name.toUpperCase());
        transformedPerson.setIdAccount(idConta);
        transformedPerson.setLimiteSaque(limiteSaque);
        transformedPerson.setLimite(limite);
        transformedPerson.setCpf(cpf);
        transformedPerson.setIdPerson(idPerson);
        transformedPerson.setStatus(status);

        return transformedPerson;
    }
}
