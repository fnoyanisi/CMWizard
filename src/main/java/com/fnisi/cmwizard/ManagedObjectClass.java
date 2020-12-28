package com.fnisi.cmwizard;

import java.util.*;

public class ManagedObjectClass implements Comparable<ManagedObjectClass>, Iterable<ManagedObjectClass> {
    private List<ManagedObject> managedObjects;
    private final String name;
    private Set<String> properties;

    public ManagedObjectClass(String name) {
        this.name = name;
        this.managedObjects = new ArrayList<>();
        this.properties = new TreeSet<>();
    }

    public List<ManagedObject> getManagedObjects() {
        return managedObjects;
    }

    public void addManagedObject(ManagedObject managedObject) {
        managedObjects.add(managedObject);
    }

    public String getName() {
        return name;
    }

    public void addProperty(String property) {
        properties.add(property);
    }

    public boolean hasProperty(String property){
        return properties.contains(property);
    }

    public Set<String> getProperties(){
        return properties;
    }

    @Override
    public int compareTo(ManagedObjectClass o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public Iterator<ManagedObjectClass> iterator() {
        return new ManagedObjectClassIterator(this);
    }
}

class ManagedObjectClassIterator implements Iterator<ManagedObjectClass> {
    private final ManagedObjectClass moc;

    public ManagedObjectClassIterator(ManagedObjectClass moc){
        this.moc = moc;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public ManagedObjectClass next() {
        return null;
    }

    @Override
    public void remove() {

    }
}