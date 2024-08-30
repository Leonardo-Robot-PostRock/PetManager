package ar.com.petmanager.service;

import ar.com.petmanager.domain.Donor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DonorServiceImpl implements DonorService {
    List<Donor> availableDonors;

    public DonorServiceImpl() {
        this.availableDonors = new ArrayList<Donor>();
    }

    @Override
    public void add(Donor donor) {
        this.availableDonors.add(donor);
    }

    @Override
    public void deleteById(int dni) {
        Donor donorToDelete = getById(dni);
        if (donorToDelete != null) {
            this.availableDonors.remove(donorToDelete);
        }
    }

    @Override
    public Donor getById(int dni) {
        return this.availableDonors.stream().filter(donor -> donor.getDni() == dni).findFirst().orElse(null);
    }

    @Override
    public List<Donor> getAll() {
        return new ArrayList<>(availableDonors);
    }

    @Override
    public void update(Donor donor) {
        Optional<Donor> optionalDonor = availableDonors.stream().filter(existingDonor -> donor.getDni() == existingDonor.getDni()).findFirst();

        if (optionalDonor.isPresent()) {
            Donor foundDonor = optionalDonor.get();

            foundDonor.setDni(donor.getDni());
            foundDonor.setName(donor.getName());
            foundDonor.setSurname(donor.getSurname());
            foundDonor.setPhone(donor.getPhone());
            foundDonor.setAddress(donor.getAddress());
        }
    }
}
