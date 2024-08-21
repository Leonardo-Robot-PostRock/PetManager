package ar.com.petmanager.domain;

import java.util.ArrayList;
import java.util.List;

public class Owner extends Person {
	private Vet preferredVet;
	private List<Pet> pets = new ArrayList<>();

	public Owner(String dni, String name, String surname, int phone, String city, String address) {
		super(dni, name, surname, phone, city, address);
	}

	public Vet getPreferredVet() {
		return preferredVet;
	}

	public void setPreferredVet(Vet preferredVet) {
		this.preferredVet = preferredVet;
	}

	public void addPet(Pet pet) {
		pets.add(pet);
	}

	public List<Pet> getPets() {
		return pets;
	}

}
