package ar.com.petmanager.domain;

public class Vet {
	private final long idVet;
	private String name;
	private int phone;
	private Address address;
	private static int countVet;
	
	private Vet() {
		this.idVet = ++Vet.countVet;
	}

	public Vet(String name, int phone, String street, String city) {
		this();
		this.name = name;
		this.phone = phone;
		this.address = new Address(street, city);
	}

	public long getIdVet() {
		return idVet;
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
