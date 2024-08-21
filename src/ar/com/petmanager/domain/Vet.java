package ar.com.petmanager.domain;

public class Vet  {
	private String name;
	private int phone;
	private Address address;
	
	public Vet(String name, int phone, String street, String city ) {
		this.name = name;
		this.phone = phone;
		this.address = new Address(street, city);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
