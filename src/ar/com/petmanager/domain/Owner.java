package ar.com.petmanager.domain;

public class Owner {
	private String dni;
	private String name;
	private Address address;

	public Owner(String dni, String name, String street, String city) {
		this.dni = dni;
		this.name = name;
		Address address = new Address(street, city);
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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
