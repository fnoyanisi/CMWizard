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

public class CmXmlReader {
    private File xmlFile;
    private Document doc;
    private Set<ManagedObjectClass> managedObjectClasses;

    public CmXmlReader(File file) throws ParserConfigurationException, IOException, SAXException {
        this.xmlFile = file;
        this.managedObjectClasses = new TreeSet<>();

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
                ManagedObjectClass managedObjectClass = new ManagedObjectClass(managedObjectClassName);

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
                        managedObjectClass.addProperty(pname);

                        // we deferred defining the name for the managed object
                        if (pname.compareTo("name") == 0){
                            mo.setName(pvalue);
                        }
                        mo.addProperty(pname, pvalue);
                    }
                }
                // add this managed object to the map
                // managed object class -> list of managed objects of type class
                managedObjectClass.addManagedObject(mo);
            }
        }

    }

    // returns an alphabetically sorted list of managedObjects names
    public List<String> getMoClasses() {
        List<String> moClasses = new ArrayList<>();
        for (ManagedObjectClass moc: managedObjectClasses) {
            moClasses.add(moc.getName());
        }
        Collections.sort(moClasses);
        return moClasses;
    }

    public List<ManagedObject> getManagedObjects() {
        List<ManagedObject> mo = new ArrayList<>();
        for (ManagedObjectClass moc : managedObjectClasses){
            mo.addAll(moc.getManagedObjects());
        }
        return mo;
    }

    public List<ManagedObject> getManagedObjectsOf(String mocName){
        for (ManagedObjectClass moc : managedObjectClasses) {
            if (moc.getName().compareTo(mocName) == 0) {
                return moc.getManagedObjects();
            }
        }
        return null;
    }

    // returns a list of property names that the node "moName"
    // contains. If the XML document does not have a managed object
    // with moName, an empty list is returned
    public List<String> getPropertiesOf(String mocName) {
        for (ManagedObjectClass moc: managedObjectClasses) {
            if (moc.getName().compareTo(mocName) == 0){
                List<String> ret = new ArrayList<>(moc.getProperties());
                Collections.sort(ret);
                return ret;
            }
        }
        return null;
    }

    // returns the total number of managed objects found in the
    // whole XML tree
    public int getTotalNumberOfManagedObjects() {
        int n = 0;
        for (ManagedObjectClass moc : managedObjectClasses) {
            n += moc.getManagedObjects().size();
        }
        return n;
    }

    // returns the number of managed objects only for the
    // managed object class given by the type parameter
    public int getNumberOfManagedObjectsFor(String mocName) {
        for (ManagedObjectClass moc : managedObjectClasses){
            if (moc.getName().compareTo(mocName) == 0){
                return moc.getManagedObjects().size();
            }
        }
        return -1;
    }
}
