package com.fnisi.cmwizard;

import java.util.*;

public class ManagedObject {
    private String name;
    private String type;
    private Map<String, String> properties;

    public ManagedObject(String name, String type) {
        this.name = name;
        this.type = type;
        this.properties = new TreeMap<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addProperty(String pname, String pval){
        properties.put(pname, pval);
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public List<String> getPropertyNames() {
        List<String> ret = new ArrayList<>(properties.keySet());
        Collections.sort(ret);
        return ret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
