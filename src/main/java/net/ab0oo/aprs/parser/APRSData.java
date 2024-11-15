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

public abstract class APRSData implements java.io.Serializable, java.lang.Comparable<APRSData> {
    private static final long serialVersionUID = 1L;
    protected APRSTypes type;
    private int lastCursorPosition = 0;
    protected byte[] rawBytes;
    protected boolean canMessage = false;
    private boolean hasFault;
	private String faultReason = "";
    protected String comment;

    public APRSData() {}

    public APRSData(byte[] msgBody) {
        rawBytes = new byte[msgBody.length];
        System.arraycopy(msgBody, 0, rawBytes, 0, msgBody.length);
    }

    
    /** 
     * @return int
     */
    public int getLastCursorPosition() {
        return lastCursorPosition;
    }

    
    /** 
     * @param cp
     */
    public void setLastCursorPosition(int cp) {
        this.lastCursorPosition = cp;
    }


    /**
     * @return String representation of this object:
     */
    @Override
    public abstract String toString();

    /** 
     * @param type
     */
    public void setType( APRSTypes type ) {
        this.type = type;
    }

    /**
     * 
     * @return boolean if this packet has faults during decoding
     */
    public final boolean hasFault() {
        return this.hasFault;
    }

    /**
     * 
     * @param _faulted boolean
     */
    public final void setHasFault(boolean _faulted) {
        this.hasFault = _faulted;
    }

    /**
	 * @param reason Set the reason this packet failed to parse
	 */
	public final void setFaultReason(String reason) {
		this.faultReason = reason;
	}

	/**
	 * @return reason the reason this packet failed to parse
	 */
	public final String getFaultReason() {
		return faultReason;
	}
   
    /** 
     * @return APRSTypes
     */
    public APRSTypes getType() {
        return this.type;
    }

    /** 
     * @return byte[] the raw bytes handed to this object
     */
    public byte[] getRawBytes() {
        return rawBytes;
    }

    /**
     * @param rawBytes set the raw bytes of the packet body
     */
    public void setRawBytes(byte[] rawBytes) {
        this.rawBytes = rawBytes;
    }

    /** 
     * @param o
     * @return int
     */
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