package ar.com.petmanager.service;

import java.util.List;

import ar.com.petmanager.domain.*;

public interface AdoptableService {
	void adopPet(Pet pet);

	List<Pet> listAvailablePets();
}
