package ar.com.petmanager.service;

import ar.com.petmanager.data.DataAccess;
import ar.com.petmanager.domain.Owner;
import ar.com.petmanager.domain.Vet;

import java.util.ArrayList;
import java.util.List;

public class VetServiceImpl implements VetService {

    private final DataAccess dataAccess;
    private final List<Vet> cache;         // fallback in-memory cuando no hay DB
    private final OwnerService ownerService;

    public VetServiceImpl(OwnerService ownerService, DataAccess dataAccess) {
        this.ownerService = ownerService;
        this.dataAccess = dataAccess;
        this.cache = new ArrayList<>();
    }

    @Override
    public void add(Vet vet) {
        if (dataAccess != null) {
            dataAccess.saveVet(vet);
        } else {
            cache.add(vet);
        }
    }

    @Override
    public void deleteById(int id) {
        if (dataAccess != null) {
            dataAccess.deleteVet(id);
        } else {
            Vet vet = getById(id);
            if (vet != null) {
                cache.remove(vet);
                for (Owner owner : ownerService.getAll()) {
                    if (owner.getPreferredVet() != null && owner.getPreferredVet().getIdVet() == id) {
                        owner.removePreferredVet();
                    }
                }
            }
        }
    }

    @Override
    public Vet getById(int id) {
        if (dataAccess != null) {
            return dataAccess.getVet(id);
        }
        return cache.stream().filter(v -> v.getIdVet() == id).findFirst().orElse(null);
    }

    @Override
    public List<Vet> getAll() {
        if (dataAccess != null) {
            return dataAccess.getAllVets();
        }
        return new ArrayList<>(cache);
    }

    @Override
    public void update(Vet vet) {
        if (dataAccess != null) {
            dataAccess.updateVet(vet);
        } else {
            cache.stream()
                    .filter(v -> v.getIdVet() == vet.getIdVet())
                    .findFirst()
                    .ifPresent(found -> {
                        found.setName(vet.getName());
                        found.setPhone(vet.getPhone());
                        found.setAddress(vet.getAddress());
                    });
        }
    }
}
