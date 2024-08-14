package ar.com.petmanager.service;

import java.util.ArrayList;
import java.util.List;
import ar.com.petmanager.domain.*;

public class AdoptableServiceImpl implements AdoptableService {
	private List<Pet> availablePets;

	public AdoptableServiceImpl() {
		this.availablePets = new ArrayList<>();
	}

	@Override
	public void adopPet(Pet pet) {
		availablePets.remove(pet);
	}

	@Override
	public List<Pet> listAvailablePets() {
		return new ArrayList<>(availablePets);
	}
}
