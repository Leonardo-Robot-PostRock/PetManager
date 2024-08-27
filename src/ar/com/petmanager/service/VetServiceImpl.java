package ar.com.petmanager.service;

import ar.com.petmanager.domain.Owner;
import ar.com.petmanager.domain.Vet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VetServiceImpl implements VetService {
    List<Vet> availableVets;
    private OwnerService ownerService;

    public VetServiceImpl(OwnerService ownerService) {
        this.availableVets = new ArrayList<Vet>();
        this.ownerService = ownerService;
    }

    @Override
    public void add(Vet vet) {
        this.availableVets.add(vet);
    }

    @Override
    public void delete(Vet vet) {
        this.availableVets.remove(vet);

        List<Owner> owners = this.ownerService.getAll();

        for (Owner owner : owners) {
            if (owner.getPreferredVet() != null && owner.getPreferredVet().equals(vet)) {
                owner.removePreferredVet();
            }
        }

    }

    @Override
    public Vet getById(int id) {
        return this.availableVets.stream().filter(vet -> vet.getIdVet() == id).findFirst().orElse(null);
    }

    @Override
    public List<Vet> getAll() {
        return new ArrayList<Vet>(availableVets);
    }

    @Override
    public void update(Vet vet) {
        Optional<Vet> optionalVet = this.availableVets.stream().filter(existingVet -> vet.getIdVet() == existingVet.getIdVet()).findFirst();

        if (optionalVet.isPresent()) {
            Vet foundVet = optionalVet.get();

            foundVet.setName(vet.getName());
            foundVet.setPhone(vet.getPhone());
            foundVet.setAddress(vet.getAddress());
        }
    }
}
