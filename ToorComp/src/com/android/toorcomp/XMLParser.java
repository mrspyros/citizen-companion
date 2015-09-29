package com.android.toorcomp;


/**
 * @author MrSpyros
 *
 *  This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   any later version.

 *  This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *   
 * Parses xml and returns an arrayList of POIS mPois<Poi_Struct>
 * 
 * or DB and Program Version
 *
 */


public class XMLParser extends DefaultHandler {
 
    private List<Poi_Struct> mPois;
    private String mTempXmlItem;
    private Poi_Struct mTempPoiStruct;
    private String mProgramVersion;
    private String mDBVersion;
 
    public XMLParser() {
       mPois = new ArrayList<Poi_Struct>();
    }
 
    public List<Poi_Struct> getPois() {
        return mPois;
    }
 
    // Event Handlers
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        // reset
        mTempXmlItem = "";
        
        // ---- In case we handle pois.xml
        
        if (qName.equalsIgnoreCase("pois")) {
            // create a new instance of employee
            mTempPoiStruct = new Poi_Struct();
        }
   
    }
 
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        mTempXmlItem = new String(ch, start, length);
    }
 
    
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
       
    	
    	// ------   In case we handle pois.xml
    	
    	if (qName.equalsIgnoreCase("pois")) {
            // add it to the list
            mPois.add(mTempPoiStruct);
        } else if (qName.equalsIgnoreCase("type")) {
            mTempPoiStruct.setType((mTempXmlItem));
        } else if (qName.equalsIgnoreCase("lon")) {
            mTempPoiStruct.setLon(Double.parseDouble(mTempXmlItem));
        } else if (qName.equalsIgnoreCase("latt")) {
            mTempPoiStruct.setLat(Double.parseDouble(mTempXmlItem));
        } else if (qName.equalsIgnoreCase("desc")) {
            mTempPoiStruct.setDesc(mTempXmlItem);
        } else if (qName.equalsIgnoreCase("link")) {
            mTempPoiStruct.setLink(mTempXmlItem);
        } else if (qName.equalsIgnoreCase("name")) {
            mTempPoiStruct.setName(mTempXmlItem);
        }
        
    	/**
    	 *  TODO  
    	 */
    	
    	// ------   In case we handle Version.xml
    	
    	if (qName.equalsIgnoreCase("programversion")){
    		setProgramVersion(mTempXmlItem);
    	}
    	
    	if (qName.equalsIgnoreCase("databaseversion")){
    		setDBVersion(mTempXmlItem);
    	}
    	
    }

	public String getProgramVersion() {
		return mProgramVersion;
	}

	public void setProgramVersion(String programVersion) {
		mProgramVersion = programVersion;
	}

	public String getDBVersion() {
		return mDBVersion;
	}

	public void setDBVersion(String dBVersion) {
		mDBVersion = dBVersion;
	}
}