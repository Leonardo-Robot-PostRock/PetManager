package ar.com.petmanager.service;

import ar.com.petmanager.data.DataAccess;
import ar.com.petmanager.domain.Donor;

import java.util.ArrayList;
import java.util.List;

public class DonorServiceImpl implements DonorService {

    private final DataAccess dataAccess;
    private final List<Donor> cache;    // fallback in-memory cuando no hay DB

    public DonorServiceImpl(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
        this.cache = new ArrayList<>();
    }

    @Override
    public void add(Donor donor) {
        if (dataAccess != null) {
            dataAccess.saveDonor(donor);
        } else {
            cache.add(donor);
        }
    }

    @Override
    public void deleteById(int dni) {
        if (dataAccess != null) {
            dataAccess.deleteDonor(dni);
        } else {
            Donor donor = getById(dni);
            if (donor != null) cache.remove(donor);
        }
    }

    @Override
    public Donor getById(int dni) {
        if (dataAccess != null) {
            return dataAccess.getDonor(dni);
        }
        return cache.stream().filter(d -> d.getDni() == dni).findFirst().orElse(null);
    }

    @Override
    public List<Donor> getAll() {
        if (dataAccess != null) {
            return dataAccess.getAllDonors();
        }
        return new ArrayList<>(cache);
    }

    @Override
    public void update(Donor donor) {
        if (dataAccess != null) {
            dataAccess.updateDonor(donor);
        } else {
            cache.stream()
                    .filter(d -> d.getDni() == donor.getDni())
                    .findFirst()
                    .ifPresent(found -> {
                        found.setName(donor.getName());
                        found.setSurname(donor.getSurname());
                        found.setPhone(donor.getPhone());
                        found.setAddress(donor.getAddress());
                    });
        }
    }
}
