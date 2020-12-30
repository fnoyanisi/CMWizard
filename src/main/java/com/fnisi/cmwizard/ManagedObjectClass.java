package com.fnisi.cmwizard;

import java.util.*;

public class ManagedObjectClass extends ArrayList<ManagedObject> implements Comparable<ManagedObjectClass> {
    private final String name;
    private Set<String> properties;

    public ManagedObjectClass(String name) {
        this.name = name;
        this.properties = new TreeSet<>();
    }

    public void addManagedObject(ManagedObject managedObject) {
        add(managedObject);
    }

    public String getName() {
        return name;
    }

    public void addProperty(String property) {
        properties.add(property);
    }

    public boolean containsProperty(String property){
        return properties.contains(property);
    }

    public ManagedObject getManagedObject(int index) {
        return this.get(index);
    }

    public Set<String> getProperties(){
        return properties;
    }

    public void setProperties(Set<String> properties) {
        this.properties = properties;
    }

    @Override
    public int compareTo(ManagedObjectClass o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManagedObjectClass that = (ManagedObjectClass) o;
        return name.equals(that.name) && properties.equals(that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, properties);
    }

}