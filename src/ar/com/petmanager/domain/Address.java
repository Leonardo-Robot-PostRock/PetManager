package ar.com.petmanager.domain;

public class Address {
	private String street;
	private String city;
	
	public Address(String street, String city) {
		this.street = street;
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Address{");
		sb.append("street='").append(street).append('\'');
		sb.append(", city='").append(city).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
