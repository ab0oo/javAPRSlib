/*
 * AVRS - http://avrs.sourceforge.net/
 *
 * Copyright (C) 2011 John Gorkos, AB0OO
 *
 * AVRS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * AVRS is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AVRS; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */
package net.ab0oo.aprs;

import java.util.ArrayList;
/**
 * 
 * @author johng
 *  This class represents a complete APRS AX.25 packet, as found in a TNC2-style string:
 *  SOURCE>DESTIN,VIA,VIA:payload
 */
public class APRSPacket {
    private String sourceCall;
    private String destinationCall;
    private ArrayList<Digipeater> digipeaters;
    private char dti;
    private InformationField aprsInformation;
    protected boolean hasFault;
    private APRSTypes type;
    
    public APRSPacket( String source, String destination, ArrayList<Digipeater> digipeaters, InformationField info) {
        this.sourceCall=source;
        this.destinationCall=destination;
        if ( digipeaters == null ) {
        	Digipeater aprsIs = new Digipeater("TCPIP*");
        	this.digipeaters = new ArrayList<Digipeater>();
        	this.digipeaters.add(aprsIs);
        } else {
        	this.digipeaters = digipeaters;
        }
        this.aprsInformation = info;
        if ( info != null ) {
            this.dti = aprsInformation.getDateTypeIdentifier();
        } else {
            this.dti= (char)' ';
        }
    }

    /**
     * @return the source
     */
    public String getSourceCall() {
        return sourceCall;
    }

    /**
     * @return the destination
     */
    public String getDestinationCall() {
        return destinationCall;
    }

    /**
     * @return the digipeaters
     */
    public ArrayList<Digipeater> getDigipeaters() {
        return digipeaters;
    }
    
    public String getDigiString() {
        StringBuilder sb = new StringBuilder();
        for ( Digipeater digi : digipeaters ) {
            sb.append(","+digi.toString());
        }
        return sb.toString();
    }

    /**
     * @return the dti
     */
    public char getDti() {
        return dti;
    }

    /**
     * @return the aprsInformation
     */
    public InformationField getAprsInformation() {
        return aprsInformation;
    }
    public boolean isAprs() {
    	return true;
    }

	/**
	 * @return the hasFault
	 */
	public boolean hasFault() {
		return hasFault;
	}

	/**
	 * @param hasFault the hasFault to set
	 */
	public void setHasFault(boolean hasFault) {
		this.hasFault = hasFault;
	}

	public APRSTypes getType() {
		return type;
	}

	public void setType(APRSTypes type) {
		this.type = type;
	}

	public String toString() {
		return sourceCall+">"+destinationCall+getDigiString()+":"+aprsInformation.toString()+"\n";
	}
}

