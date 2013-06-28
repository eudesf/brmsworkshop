package com.redhat.brmsworkshop.entities;

public class CustomerPOJO {

	private Integer id;
	private Integer age;
	private String cpf;
	private Boolean married;
	private Integer monthlyIncome;
	private Integer credit;
	private Boolean approved;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public Boolean getMarried() {
		return married;
	}
	public void setMarried(Boolean married) {
		this.married = married;
	}
	public Integer getMonthlyIncome() {
		return monthlyIncome;
	}
	public void setMonthlyIncome(Integer monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}
	public Boolean getApproved() {
		return approved;
	}
	public void setApproved(Boolean approved) {
		this.approved = approved;
	}
	public Integer getCredit() {
		return credit;
	}
	public void setCredit(Integer credit) {
		this.credit = credit;
	}
	
	
}
