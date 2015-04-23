package com.android.toorcomp;


import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

  
public class XMLParser extends DefaultHandler {
 
    private List<Poi_Struct> Pois;
    private String tempVal;
    private Poi_Struct tempEmp;
 
    public XMLParser() {
       Pois = new ArrayList<Poi_Struct>();
    }
 
    public List<Poi_Struct> getPois() {
        return Pois;
    }
 
    // Event Handlers
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        // reset
        tempVal = "";
        if (qName.equalsIgnoreCase("pois")) {
            // create a new instance of employee
            tempEmp = new Poi_Struct();
        }
    }
 
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        tempVal = new String(ch, start, length);
    }
 
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equalsIgnoreCase("pois")) {
            // add it to the list
            Pois.add(tempEmp);
        } else if (qName.equalsIgnoreCase("type")) {
            tempEmp.setType((tempVal));
        } else if (qName.equalsIgnoreCase("lon")) {
            tempEmp.setLon(Double.parseDouble(tempVal));
        } else if (qName.equalsIgnoreCase("latt")) {
            tempEmp.setLat(Double.parseDouble(tempVal));
        } else if (qName.equalsIgnoreCase("desc")) {
            tempEmp.setDesc(tempVal);
        } else if (qName.equalsIgnoreCase("link")) {
            tempEmp.setLink(tempVal);
        } else if (qName.equalsIgnoreCase("name")) {
            tempEmp.setName(tempVal);
        }
        
    }
}