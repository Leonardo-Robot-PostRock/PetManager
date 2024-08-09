package ar.com.petmanager.domain;

public class Cat extends Pet {
	private String race;

	public Cat(String name, int age, double weight, String health, String race) {
		super(name, age, weight, health,race);
	}

}
