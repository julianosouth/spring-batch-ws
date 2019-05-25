package br.com.calcard.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import br.com.calcard.model.Person;

public class PersonRowMapper implements RowMapper<Person> {

	@Override
	public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
		Person person = new Person();

		person.setIdAccount(rs.getInt("id_account"));
		person.setIdPerson(rs.getInt("id_person"));
		person.setName(rs.getString("name"));
		person.setLimite(rs.getBigDecimal("limite_visa"));
		person.setLimiteSaque(rs.getBigDecimal("limite_saque_rapido"));
		person.setStatus("ELIGIBLE");
		return person;
	}
 
}
