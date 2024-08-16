package ar.com.petmanager.domain;

public class Pet {
	private String name;
	private int age;
	private double weight;
	private String health;
	private String race;
	private Owner owner;

	public Pet(String name, int age, double weight, String health, String race) {
		this.name = name;
		this.age = age;
		this.weight = weight;
		this.health = health;
		this.race = race;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getHealth() {
		return health;
	}

	public void setHealth(String health) {
		this.health = health;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

}
