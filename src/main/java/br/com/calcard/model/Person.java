package br.com.calcard.model;

import java.math.BigDecimal;

public class Person {
	private Long id;
	private int idAccount;
	private int idPerson;
	private String name;
	private String cpf;
	private BigDecimal limite;
	private BigDecimal limiteSaque;
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getIdAccount() {
		return idAccount;
	}

	public void setIdAccount(int idAccount) {
		this.idAccount = idAccount;
	}

	public int getIdPerson() {
		return idPerson;
	}

	public void setIdPerson(int idPerson) {
		this.idPerson = idPerson;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public BigDecimal getLimite() {
		return limite;
	}

	public void setLimite(BigDecimal limite) {
		this.limite = limite;
	}

	public BigDecimal getLimiteSaque() {
		return limiteSaque;
	}

	public void setLimiteSaque(BigDecimal limiteSaque) {
		this.limiteSaque = limiteSaque;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status){
		this.status = status;
	}

}
