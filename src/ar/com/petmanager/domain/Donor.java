package ar.com.petmanager.domain;

public class Donor extends Person {

	public Donor(int dni, String name, String surname, int phone, String street, String city) {
		super(dni, name, surname, phone, street, city);
	}

	@Override
	public int getDni() {
		return super.getDni();
	}

	@Override
	public void setDni(int dni) {
		super.setDni(dni);
	}

	@Override
	public String getName() {
		return super.getName();
	}

	@Override
	public void setName(String name) {
		super.setName(name);
	}

	@Override
	public String getSurname() {
		return super.getSurname();
	}

	@Override
	public void setSurname(String surname) {
		super.setSurname(surname);
	}

	@Override
	public int getPhone() {
		return super.getPhone();
	}

	@Override
	public void setPhone(int phone) {
		super.setPhone(phone);
	}

	@Override
	public Address getAddress() {
		return super.getAddress();
	}

	@Override
	public void setAddress(Address address) {
		super.setAddress(address);
	}
}
