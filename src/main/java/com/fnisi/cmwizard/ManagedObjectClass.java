package com.fnisi.cmwizard;

import java.util.*;

public class ManagedObjectClass implements Comparable<ManagedObjectClass>, Iterable<ManagedObject> {
    private List<ManagedObject> managedObjects;
    private final String name;
    private Set<String> properties;
    private int nextIndex;          // array index to be used for the next ManagedObject in the list

    public ManagedObjectClass(String name) {
        this.name = name;
        this.managedObjects = new ArrayList<>();
        this.properties = new TreeSet<>();
        this.nextIndex = 0;
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

    public ManagedObject getManagedObject(int index) {
        if (nextIndex > 0 && index >= 0 && index < managedObjects.size()) {
            return managedObjects.get(index);
        }
        return null;
    }

    public Set<String> getProperties(){
        return properties;
    }

    public int getNextIndex() {
        return nextIndex;
    }

    @Override
    public int compareTo(ManagedObjectClass o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public Iterator<ManagedObject> iterator() {
        return new ManagedObjectClassIterator(this);
    }
}

class ManagedObjectClassIterator implements Iterator<ManagedObject> {
    private final ManagedObjectClass moc;

    public ManagedObjectClassIterator(ManagedObjectClass moc){
        this.moc = moc;
    }

    @Override
    public boolean hasNext() {
        return moc.getManagedObjects().size() > moc.getNextIndex();
    }

    @Override
    public ManagedObject next() {
        return moc.getManagedObject(moc.getNextIndex());
    }
}