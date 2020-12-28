package com.fnisi.cmwizard;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class CmXmlReader implements Iterable<> {
    private File xmlFile;
    private Document doc;
    private Set<String> moClasses;
    private Map<String, Set<String>> moProperties;
    private SortedMap<String, List<ManagedObject>> managedObjects;
    private SortedMap<String, Integer> numberOfManagedObjects;

    public CmXmlReader(File file) throws ParserConfigurationException, IOException, SAXException {
        this.xmlFile = file;
        this.moClasses = new HashSet<>();
        this.moProperties = new HashMap<>();
        this.managedObjects = new SortedMap<>();
        this.numberOfManagedObjects = new HashMap<>();

        // read and parse the XML file
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        // Since we do not have the DTD file, we should ignore the reference to the
        // document type definition file. In order to do that provide a new entity
        // resolver to replace the default one which looks for the DTD file
        documentBuilder.setEntityResolver((publicId, systemId) -> {
            if (systemId.contains("raml20.dtd")) {
                return new InputSource(new StringReader(""));
            } else {
                return null;
            }
        });
        doc = documentBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        // traverse through all the nodes. we are interested in managedObject nodes
        NodeList managedObjectNodeList = doc.getElementsByTagName("managedObject");

        for (int index = 0; index < managedObjectNodeList.getLength(); index++) {
            Node managedObjectNode = managedObjectNodeList.item(index);

            Node managedObjectNodeClass = managedObjectNode.getAttributes().getNamedItem("class");
            if (managedObjectNodeClass != null && managedObjectNodeClass.getNodeType() == Node.ATTRIBUTE_NODE) {
                String managedObjectClassName = managedObjectNodeClass.getNodeValue();
                moClasses.add(managedObjectClassName);

                // set the name afterwards when the "name" property is read from
                // the reference XML file
                ManagedObject mo = new ManagedObject("", managedObjectClassName);

                // iterate through all the "p" and "list" nodes
                // and add each property to the current ManagedObject
                NodeList childNodes = managedObjectNode.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    Node childNode = childNodes.item(i);

                    // we are only interested in "p" or "list" elements
                    String childNodeName = childNode.getNodeName();
                    if (childNodeName.compareTo("p") == 0 || childNodeName.compareTo("list") == 0) {
                        String pname = childNode.getAttributes().getNamedItem("name").getNodeValue();
                        String pvalue = childNode.getFirstChild().getNodeValue();

                        // this maintains the list of all possible properties for each
                        // managed object class
                        addPropertyForMoClass(managedObjectClassName, pname);

                        // we deferred defining the name for the managed object
                        if (pname.compareTo("name") == 0){
                            mo.setName(pvalue);
                        }
                        mo.addProperty(pname, pvalue);
                    }
                }
                // add this managed object to the map
                // managed object class -> list of managed objects of type class
                addManagedObject(mo);
            }
        }

    }

    // returns an alphabetically sorted list of managedObjects names
    public List<String> getMoClasses() {
        List<String> ret = new ArrayList<>(moClasses);
        Collections.sort(ret);
        return ret;
    }

    public Map<String, List<ManagedObject>> getManagedObjects() {
        return managedObjects;
    }

    // returns a list of property names that the node "moName"
    // contains. If the XML document does not have a managed object
    // with moName, an empty list is returned
    public List<String> getPropertiesOf(String moName) {
        Set<String> ret = moProperties.get(moName);
        if (ret == null)
            return new ArrayList<>();

        List<String> retList = new ArrayList<>(ret);
        Collections.sort(retList);
        return retList;
    }

    // returns the total number of managed objects found in the
    // whole XML tree
    public int getTotalNumberOfManagedObjects() {
        int n = 0;
        for (Map.Entry<String, Integer> entry : numberOfManagedObjects.entrySet()) {
            n += entry.getValue();
        }
        return n;
    }

    // returns the number of managed objects only for the
    // managed object class given by the type parameter
    public int getNumberOfManagedObjectsFor(String type) {
        if (numberOfManagedObjects.containsKey(type)) {
            return numberOfManagedObjects.get(type);
        }
        return -1;
    }

    // adds an item into the relevant moObject's list of properties
    // creates a new entry if the key does not exist
    private void addPropertyForMoClass(String moClass, String property){
        if (moProperties.containsKey(moClass)) {
            moProperties.get(moClass).add(property);
        } else {
            Set<String> properties = new HashSet<>();
            properties.add(property);
            moProperties.put(moClass, properties);
        }
    }

    private void addManagedObject(ManagedObject managedObject){
        String type = managedObject.getType();

        if (!managedObjects.containsKey(type)) {
            managedObjects.put(type, new ArrayList<>());
            numberOfManagedObjects.put(type, 0);
        }
        managedObjects.get(type).add(managedObject);
        numberOfManagedObjects.computeIfPresent(type, (key, value) -> value+1);
    }

    @Override
    public Iterator<CmXmlReader> iterator() {
        return new CmXmlReaderIterator<CmXmlReader>(this);
    }
}

class CmXmlReaderIterator<CmXmlReader> implements Iterator<CmXmlReader> {
    private final CmXmlReader reader;

    public CmXmlReaderIterator(CmXmlReader reader){
        this.reader = reader;
    }
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public CmXmlReader next() {
        return null;
    }
}
