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

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
/**
 * <p>APRSPacket class.</p>
 *
 * @author johng
 *  This class represents a complete APRS AX.25 packet, as found in a TNC2-style string:
 *  SOURCE&gt;DESTIN,VIA,VIA:payload
 * @version $Id: $Id
 */
public class APRSPacket implements Serializable {
    private static final long serialVersionUID = 1L;
	/**
	 * an internal-use timestamp indicating when this packet object was instantiated
	 */
	private Date receivedTimestamp = null;
	/**
	 * The original TNC2-format string that we're attempting to parse
	 */
	private String originalString;
	/**
	 * from the TNC2 format: the originating station of this message
	 * Generally used to indicate the type and version of device that
	 * originated th packet.  Can also be used to encode position in
	 * Mic-E packets
	 */
	private String sourceCall;
	/**
	 * from the TNC2 format:  the destination of this APRS packet.
	 */
    private String destinationCall;
	/**
	 * from the TNC2 format:  the list of digipeaters that have handled this message
	 */
    private ArrayList<Digipeater> digipeaters;
	/**
	 * the data type identifier, per Chap 5 of the APRS Spec
	 */
    private char dti;
	/**
	 * The base APRSInformation object, which is everything after the ":"
	 * character trailing all digipeaters in the TNC2 packet format
	 */
    private InformationField aprsInformation;
	/**
	 * hard saying.  Might be the comment from the packet?  I hate commenting my code...
	 */
	private String comment;

	static final String REGEX_PATH_ALIASES = "^(WIDE|TRACE|RELAY)\\d*$";
    
    /**
     * <p>Constructor for APRSPacket.</p>
     *
     * @param source a {@link java.lang.String} object
     * @param destination a {@link java.lang.String} object
     * @param digipeaters a {@link java.util.ArrayList} object
     * @param body an array of {@link byte} objects
     */
    public APRSPacket( String source, String destination, ArrayList<Digipeater> digipeaters, byte[] body) {
		receivedTimestamp = new Date(System.currentTimeMillis());
        this.sourceCall=source.toUpperCase();
        this.destinationCall=destination.toUpperCase();
        if ( digipeaters == null ) {
        	Digipeater aprsIs = new Digipeater("TCPIP*");
        	this.digipeaters = new ArrayList<Digipeater>();
        	this.digipeaters.add(aprsIs);
        } else {
        	this.digipeaters = digipeaters;
        }
		this.dti = (char)body[0];
        this.aprsInformation = new InformationField(body);
    }
    
    
	/**
	 * <p>getBaseCall.</p>
	 *
	 * @param callsign the initiating callsign of this APRS Packet
	 * @return String
	 */
	public static final String getBaseCall(String callsign) {
    	int sepIdx = callsign.indexOf('-');
    	if ( sepIdx > -1 ) {
    		return callsign.substring(0,sepIdx);
    	} else {
    		return callsign;
    	}
    }
    
    
	/**
	 * <p>getSsid.</p>
	 *
	 * @param callsign the complete callsign, including SSID, you want to extract the SSID from
	 * @return String
	 */
	public static final String getSsid(String callsign) {
    	int sepIdx = callsign.indexOf('-');
    	if ( sepIdx > -1 ) {
    		return callsign.substring(sepIdx+1);
    	} else {
    		return "0";
    	}
    }
    
    /**
     * <p>getIgate.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getIgate() {
    	for ( int i=0; i<digipeaters.size(); i++) {
    		Digipeater d = digipeaters.get(i);
    		// I'm not sure I'm treating these correctly (poor understanding of the
    		// Q-constructs on my part).  For now, I'm saying that call sign AFTER a 
    		// q-construct is the I-gate.
    		if ( d.getCallsign().equalsIgnoreCase("qar") && i<digipeaters.size()-1 ) {
    			return digipeaters.get(i+1).toString();
    		}
    		if ( d.getCallsign().equalsIgnoreCase("qas") && i<digipeaters.size()-1 ) {
    			return digipeaters.get(i+1).toString();
    		}
    		if ( d.getCallsign().equalsIgnoreCase("qac") && i<digipeaters.size()-1 ) {
    			return digipeaters.get(i+1).toString();
    		}
    		if ( d.getCallsign().equalsIgnoreCase("qao") && i<digipeaters.size()-1 ) {
    			return digipeaters.get(i+1).toString();
    		}
    	}
    	return "";
    }

    /**
     * <p>Getter for the field <code>sourceCall</code>.</p>
     *
     * @return the source
     */
    public String getSourceCall() {
        return sourceCall;
    }

    /**
     * <p>Getter for the field <code>destinationCall</code>.</p>
     *
     * @return the destination
     */
    public String getDestinationCall() {
        return destinationCall;
    }

    /**
     * <p>Getter for the field <code>digipeaters</code>.</p>
     *
     * @return the digipeaters
     */
    public ArrayList<Digipeater> getDigipeaters() {
        return digipeaters;
    }
    
    /**
     * <p>Setter for the field <code>digipeaters</code>.</p>
     *
     * @param newDigis a {@link java.util.ArrayList} object
     */
    public void setDigipeaters(ArrayList<Digipeater> newDigis) {
	    digipeaters = newDigis;
    }

    /**
     * <p>getLastUsedDigi.</p>
     *
     * @return the last digipeater in the path marked as used (with '*') or null.
     */
    public String getLastUsedDigi() {
        for (int i=digipeaters.size()-1; i>=0; i--) {
            Digipeater d = digipeaters.get(i);
	    String call = d.getCallsign();
            if (d.isUsed() && !call.matches(REGEX_PATH_ALIASES))
                return call;
        }
        return null;
    }

    /**
     * <p>getDigiString.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getDigiString() {
        StringBuilder sb = new StringBuilder();
		boolean first=true;
        for ( Digipeater digi : digipeaters ) {
			if ( first ) {
            	sb.append(digi.toString());
				first=false;
			} else 
				sb.append(","+digi.toString());
        }
        return sb.toString();
    }

    /**
     * <p>Getter for the field <code>dti</code>.</p>
     *
     * @return the dti
     */
    public char getDti() {
        return dti;
    }

    /**
     * <p>Getter for the field <code>aprsInformation</code>.</p>
     *
     * @return the aprsInformation
     */
    public InformationField getAprsInformation() {
        return aprsInformation;
    }
    /**
     * <p>isAprs.</p>
     *
     * @return a boolean
     */
    public boolean isAprs() {
    	return true;
    }

	/**
	 * <p>Getter for the field <code>originalString</code>.</p>
	 *
	 * @return the originalString
	 */
	public final String getOriginalString() {
		return originalString;
	}

	/**
	 * <p>Setter for the field <code>originalString</code>.</p>
	 *
	 * @param originalString the originalString to set
	 */
	public final void setOriginalString(String originalString) {
		this.originalString = originalString;
	}

		/**
		 * <p>setInfoField.</p>
		 *
		 * @param infoField a {@link net.ab0oo.aprs.parser.InformationField} object
		 */
		public void setInfoField(InformationField infoField) {
		this.aprsInformation = infoField;
	}

	/**
	 * <p>hasFault.</p>
	 *
	 * @return the hasFault
	 */
	public boolean hasFault() {
		boolean fault = false;
		for ( APRSData d : this.getAprsInformation().getAprsData().values() ) {
			fault = fault | d.hasFault();
		}
		return fault;
	}

	/**
	 * <p>getFaultReason.</p>
	 *
	 * @return reason the reason this packet failed to parse
	 */
	public final String getFaultReason() {
		String faultReason = "";
		for ( APRSData d : this.getAprsInformation().getAprsData().values() ) {
			faultReason = faultReason+=d.getFaultReason();
		}
		return faultReason;
	}

    /**
     * <p>Getter for the field <code>comment</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getComment() {
		return comment;
	}


	/**
	 * <p>Setter for the field <code>comment</code>.</p>
	 *
	 * @param comment a {@link java.lang.String} object
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("-------------------------------\n");
		sb.append(sourceCall+">"+destinationCall+"\n");
		sb.append("Via Digis: "+getDigiString()+"\n");
		sb.append(aprsInformation.toString());
		return sb.toString();
	}

	/**
	 * <p>toAX25Frame.</p>
	 *
	 * @return an array of {@link byte} objects
	 * @throws java.lang.IllegalArgumentException if any.
	 */
	public byte[] toAX25Frame() throws IllegalArgumentException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// write AX.25 header
		// dest
		byte[] dest = new Digipeater(destinationCall + "*").toAX25();
		baos.write(dest, 0, dest.length);
		// src
		byte[] src = new Digipeater(sourceCall).toAX25();
		// last byte of last address is |=1
		if (digipeaters.size() == 0)
			src[6] |= 1;
		baos.write(src, 0, src.length);
		// digipeater list
		for (int i = 0; i < digipeaters.size(); i++) {
			byte[] d = digipeaters.get(i).toAX25();
			// last byte of last digi is |=1
			if (i == digipeaters.size() - 1)
				d[6] |= 1;
			baos.write(d, 0, 7);
		}
		// control: UI-frame, poll-bit set
		baos.write(0x03);
		// pid: 0xF0 - no layer 3 protocol
		baos.write(0xF0);
		// write content
		byte[] content = aprsInformation.getRawBytes();
		baos.write(content, 0, content.length);
		return baos.toByteArray();
	}

	/**
	 * <p>getRecevedTimestamp.</p>
	 *
	 * @return a {@link java.util.Date} object
	 */
	public Date getRecevedTimestamp() {
		return this.receivedTimestamp;
	}
}

