package ar.com.petmanager.domain;

public class Dog extends Pet{

	public Dog(String name) {
		super(name);
	}
	
	public Dog(String name, int age, double weight, String race) {
		super(name, age, weight, race);
	}
	
	public Dog(String name, int age, double weight, String race, boolean isSick, String description) {
		super(name, age, weight, race, isSick, description);
	}
}
