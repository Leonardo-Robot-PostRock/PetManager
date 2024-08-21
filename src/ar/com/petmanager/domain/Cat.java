package ar.com.petmanager.domain;

public class Cat extends Pet {
	
	public Cat(String name) {
		super(name);
	}

	public Cat(String name, int age, double weight, String race) {
		super(name, age, weight, race);
	}

	public Cat(String name, int age, double weight, String race, boolean isSick, String description) {
		super(name, age, weight, race, isSick, description);
	}

}
