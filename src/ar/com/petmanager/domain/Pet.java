package ar.com.petmanager.domain;

import java.util.List;

public abstract class Pet {
    private long id;
    private String name;
    private String age;
    private List<Owner> owners;
    private double weight;
    private String race;
    private boolean isSick;
    private String description;
    private PetStatus status;
    private static int countPet;

    private Pet() {
        this.id = ++Pet.countPet;
        this.status = PetStatus.ACTIVA;
    }

    public Pet(String name) {
        this();
        this.name = name;
    }

    public Pet(String name, String age, double weight, String race) {
        this();
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.race = race;
    }

    public Pet(String name, String age, double weight, String race, boolean isSick, String description) {
        this();
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.race = race;
        this.isSick = isSick;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public boolean isSick() {
        return isSick;
    }

    public void setSick(boolean isSick) {
        this.isSick = isSick;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PetStatus getStatus() {
        return status;
    }

    public void setStatus(PetStatus status) {
        this.status = status;
    }

    public List<Owner> getOwners() {
        return owners;
    }

    public void addOwner(Owner owner) {
        if (owner != null && !owners.contains(owner)) {
            this.owners.add(owner);
            owner.adoptPet(this);
        }
    }
}
