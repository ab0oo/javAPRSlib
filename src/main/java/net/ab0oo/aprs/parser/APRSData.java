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

/**
 * This abstract class is the base for all of the basic building blocks of APRS on air packets
 * it includes:
 * - Items
 * - Messages
 * - Objects
 * - Positions
 * - Timestamps
 * - Weather reports
 *
 * There is an additional BadPacket class used for indicating parser failures
 *
 * @author john
 * @version $Id: $Id
 */
public abstract class APRSData implements java.io.Serializable, java.lang.Comparable<APRSData> {
    private static final long serialVersionUID = 1L;
    /**
     * A single APRS message can contain one or more pieces of APRSData
     * The data type represented by this object is kept here
     */
    protected APRSTypes type;
    /**
     * private counter, indicating the farthest right into the message string we've parsed.
    */
    private int lastCursorPosition = 0;
    /**
     * original byte representation of this message
     */
    protected byte[] rawBytes;
    /**
     * flag indicating whether the station that created this APRS message can receive
     * APRS messages
     */
    protected boolean canMessage = false;
    /**
     * flag to indicate a particular component failed during parsing.  This
     * does not necessarily mean the entire message is useless
     */
    private boolean hasFault;
    /**
     * string indicating why parsing has failed.
     */
	private String faultReason = "";
    /**
     * all characters after the set parsable APRSData fields are "comments"
     */
    protected String comment;
    /**
     * used primarily for setting up unit tests
     */
    public APRSData() {}

    /**
     * <p>Constructor for APRSData.</p>
     *
     * @param msgBody an array of {@link byte} objects
     */
    public APRSData(byte[] msgBody) {
        rawBytes = new byte[msgBody.length];
        System.arraycopy(msgBody, 0, rawBytes, 0, msgBody.length);
    }

    
    /**
     * <p>Getter for the field <code>lastCursorPosition</code>.</p>
     *
     * @return int last cursor position
     * The cursor is used as various components are parsed.  It indicates the farthest point into a message
     * (from left to right) that has been analyzed.  It can be assumed that everything to the left of the cursor
     * has been parsed.
     */
    public int getLastCursorPosition() {
        return lastCursorPosition;
    }

    
    /**
     * <p>Setter for the field <code>lastCursorPosition</code>.</p>
     *
     * @param cp last cursor position
     * This sets the deepest character into a message that has been analyzed and parsed.  Any additional
     * parsing of this message body will start from this character position
     */
    public void setLastCursorPosition(int cp) {
        this.lastCursorPosition = cp;
    }


    /** {@inheritDoc} */
    @Override
    public abstract String toString();

    /**
     * <p>Setter for the field <code>type</code>.</p>
     *
     * @param type a {@link net.ab0oo.aprs.parser.APRSTypes} object
     */
    public void setType( APRSTypes type ) {
        this.type = type;
    }

    /**
     * <p>hasFault.</p>
     *
     * @return boolean if this packet has faults during decoding
     */
    public final boolean hasFault() {
        return this.hasFault;
    }

    /**
     * <p>Setter for the field <code>hasFault</code>.</p>
     *
     * @param _faulted boolean
     */
    public final void setHasFault(boolean _faulted) {
        this.hasFault = _faulted;
    }

	/**
	 * <p>Setter for the field <code>faultReason</code>.</p>
	 *
	 * @param reason Set the reason this packet failed to parse
	 * This string is set to give the upstream components some clue as to why this packet failed to parse
	 */
	public final void setFaultReason(String reason) {
		this.faultReason = reason;
	}

	/**
	 * <p>Getter for the field <code>faultReason</code>.</p>
	 *
	 * @return reason the reason this packet failed to parse
	 * Used by upstream components to tell the user why a packet failed to parse
	 */
	public final String getFaultReason() {
		return faultReason;
	}
   
    /**
     * <p>Getter for the field <code>type</code>.</p>
     *
     * @return APRSTypes
     */
    public APRSTypes getType() {
        return this.type;
    }

    /**
     * <p>Getter for the field <code>rawBytes</code>.</p>
     *
     * @return byte[] the raw bytes handed to this object
     * All messages start as rawBytes, and the raw bytes of the message are passed around
     * for good measure.
     */
    public byte[] getRawBytes() {
        return rawBytes;
    }

    /**
     * <p>Setter for the field <code>rawBytes</code>.</p>
     *
     * @param rawBytes set the raw bytes of the packet body
     * set by the inbound message
     */
    public void setRawBytes(byte[] rawBytes) {
        this.rawBytes = rawBytes;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(APRSData o) {
        if (this.hashCode() > o.hashCode()) {
            return 1;
        }
        if (this.hashCode() == o.hashCode()) {
            return 0;
        }
        return -1;
    }
}