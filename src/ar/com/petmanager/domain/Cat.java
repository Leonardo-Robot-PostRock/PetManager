package ar.com.petmanager.domain;

import java.util.List;

public class Cat extends Pet {

    public Cat(String name) {
        super(name);
    }

    public Cat(String name, int age, double weight, String race) {
        super(name, age, weight, race);
    }

    public Cat(String name, int age, double weight, String race, boolean isSick, String description) {
        super(name, age, weight, race, isSick, description);
    }

    @Override
    public long getId() {
        return super.getId();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public int getAge() {
        return super.getAge();
    }

    @Override
    public void setAge(int age) {
        super.setAge(age);
    }

    @Override
    public double getWeight() {
        return super.getWeight();
    }

    @Override
    public void setWeight(double weight) {
        super.setWeight(weight);
    }

    @Override
    public String getRace() {
        return super.getRace();
    }

    @Override
    public void setRace(String race) {
        super.setRace(race);
    }

    @Override
    public boolean isSick() {
        return super.isSick();
    }

    @Override
    public void setSick(boolean isSick) {
        super.setSick(isSick);
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description);
    }

    @Override
    public List<Owner> getOwners() {
        return super.getOwners();
    }

    @Override
    public void addOwner(Owner owner) {
        super.addOwner(owner);
    }
}
