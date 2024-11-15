/*
 * javAPRSlib - https://github.com/ab0oo/javAPRSlib
 *
 * Copyright (C) 2011, 2024 John Gorkos, AB0OO
 *
 * javAPRSlib is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * javAPRSlib is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
package net.ab0oo.aprs.parser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author johng
 *  This class represents the "payload" of a TNC2 style AX25 packet, stripped of the source call,
 *  destination call, and digi VIAs.  Note this class is abstract:  only subclasses of it may be 
 *  instantiated.  Per the APRS spec, these classes include Position, Direction Finding, Objects
 *  and Items, Weather, Telemetry, Messages, Bulletins, Annoucements, Queries, Responses, Statuses,
 *  and User-defined Others.
 */
public class InformationField implements Serializable {
	private static final long serialVersionUID = 1L;
    private final long createTimestamp = System.currentTimeMillis();
	private char dataTypeIdentifier;
    protected byte[] rawBytes;
    protected boolean canMessage = false;
    Map<APRSTypes,APRSData> dataFields;
    DataExtension extension = null;
	protected String comment = "";

    public InformationField() {
    }
    
    public InformationField( byte[] rawBytes ) {
        if ( rawBytes.length < 1 ) {
            System.err.println("Parse error:  zero length information field");
        }
        this.rawBytes = rawBytes;
        this.dataTypeIdentifier = (char)rawBytes[0];
        this.dataFields = new HashMap<>();
        switch ( dataTypeIdentifier ) {
       	case '@' :
        	case '=' :
        	case '\'':
        	case ':' : this.canMessage = true;
            default: this.canMessage = false;
        }
    }
    
    
    /** 
     * @return char Data Type Indicator
     *
     * Fetches the Data Type indicator (the first character of an AX25 information field)
     */
    public char getDataTypeIdentifier() {
        return dataTypeIdentifier;
    }

    
    /** 
     * @param dti Data Type Indicator
     *
     * Sets the Data Type Indicator for a constructed packet
     */
    public void setDataTypeIdentifier(char dti) {
        this.dataTypeIdentifier = dti;
    }

    /**
     * @return the rawBytes
     *
     * Returns a byte array of the current Information Field
     */
    public byte[] getRawBytes() {
	    if (rawBytes != null)
		    return rawBytes;
	    else
		    return toString().getBytes();
    }
    
    
    /** 
     * @param start start index
     * @param end end index
     * @return byte[] a new byte[] with the requested slice
     *
     * Returns a slice of bytes from the current Information Field
     */
    public byte[] getBytes(int start, int end) {
        byte[] returnArray = new byte[end-start];
        System.arraycopy(getRawBytes(), start, returnArray, 0, end-start);
        return returnArray;
    }
    
	/**
	 * @return the comment string which was embedded in the packet
     *
     * Returns the comment, which comes after all fixed, parsable data
	 */
    public String getComment() {
        return comment;
    }
    
    
    /** 
     * @return String String representation of this message component
     *
     * Returns a pretty-printed string version of the Info Field
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Raw Bytes:\t"+new String(rawBytes)+"\n");
        sb.append("Data Type Identifier: "+dataTypeIdentifier+"\n");
        sb.append("Create Timestamp:\t"+ (new java.util.Date(createTimestamp).toString() )+"\n");
        sb.append("Comment:  "+this.comment+"\n");
        for ( APRSData df : dataFields.values() ) {
            sb.append("Class "+df.getClass().getName()+"\n");
            sb.append(df.toString());
        }

        return sb.toString();
    }
	/**
	 * @return boolean  True is packet has faults that should not be on-air
     *
     * Faulted packets are unable to be parsed for various reasons
	 */
	public boolean hasFault() {
        boolean faultFound = false;
        for ( APRSData data : dataFields.values() )  {
            faultFound = faultFound | data.hasFault();
        }
		return faultFound;
	}

	/**
	 * @return DataExtension Each Information Field (i.e. on-air packet) can contain one data extension,
     * the list of which can be found in the Spec, Ch 7.
     *
     * Returns the Data Extension from this Information Field
     */
	public final DataExtension getExtension() {
		return extension;
	}

    /**
     * @param _extension Set the data extension for this packet
     *
     * Sets the data extension for constructed Information Fields
     */
    public final void setDataExtension( DataExtension _extension ) {
        this.extension = _extension;
    }
    
    /** 
     * @return long epoch timestamp of creation time
     *
     * Returns the epoch timestamp of the creation time of this Info Field
     */
    public final long getCreateTimestamp() {
        return this.createTimestamp;
    }

	
    /** 
     * @return Map a Mapping of APRSTypes to APRSData
     */
    public Map<APRSTypes,APRSData> getAprsData() {
		return this.dataFields;
	}

    
    /** 
     * @param t APRSTypes type to fetch from the InformationField envelope
     * @return APRSData any data that matches the type requested, null if the
     * InformationField does not contain the requested type.
     *
     * Returns the specified APRSData given the APRSType
     */
    public APRSData getAprsData(APRSTypes t) {
        if ( dataFields.containsKey(t)) {
            return dataFields.get(t);
        }
        return null;
    }

	
    /** 
     * @param type APRSTypes enum to indicate the type of data being added
     * @param data the data to be added to the InformationField envelope
     *
     * Adds a new AprsData object to a constructed field
     */
    public void addAprsData(APRSTypes type, APRSData data) {
		dataFields.put(type, data);
	}

    
    /** 
     * @param t the APRSTypes to check for
     * @return boolean true if this object contains the given data type
     *
     * Used to determine if this Info Field contains a data of a specific APRSType
     */
    public boolean containsType(APRSTypes t) {
        if ( dataFields.containsKey(t) ) return true;
        return false;
    }

    
    /** 
     * @return Set of APRSTypes
     *
     * Returns the set of APRSTypes in this packet
     */
    public Set<APRSTypes> getTypes() {
        return dataFields.keySet();
    }

}
