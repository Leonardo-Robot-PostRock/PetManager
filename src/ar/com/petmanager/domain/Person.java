package ar.com.petmanager.domain;

public class Person {
	private String dni;
	private String name;
	private String surname;
	private int phone;
	private Address address;

	public Person(String dni, String name, String surname, int phone, String street, String city) {
		this.dni = dni;
		this.name = name;
		this.surname = surname;
		this.phone = phone;
		this.address = new Address(street, city);
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
