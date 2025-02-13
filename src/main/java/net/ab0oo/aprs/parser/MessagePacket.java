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
 * <p>MessagePacket class.</p>
 *
 * @author john
 * @version $Id: $Id
 */
public class MessagePacket extends APRSData {
	private static final long serialVersionUID = 1L;
    /**
     * The body of the message, stripped of all meta data
     */
    private String messageBody;
    /**
     * the self-assigned message number of the originating station
     */
    private String messageNumber;
    /**
     * the intended recipient of the message, NOT the TNC2 "destination"
     */
    private String targetCallsign ="";
    /**
     * flag indicating that this is an acknowledgement of a message sent by
     * another station
     */
    private boolean isAck = false;
    /**
     * flag set to indicate that this message was rejected by the recipient for
     * some reason
     */
    private boolean isRej = false;
    
    /**
     * <p>Constructor for MessagePacket.</p>
     *
     * @param bodyBytes an array of {@link byte} objects
     * @param destCall a {@link java.lang.String} object
     */
    public MessagePacket( byte[] bodyBytes, String destCall ) {
        super(bodyBytes);
        String message = new String(bodyBytes);
        if ( message.length() < 2) {
            this.setHasFault(true);
            return;
        }
        int msgSpc = message.indexOf(':', 2);
        if ( msgSpc < 1 ) {
        	this.targetCallsign = "UNKNOWN";
        } else {
        	targetCallsign = message.substring(1,msgSpc).trim().toUpperCase();
        }
        int msgNumberIdx = message.lastIndexOf('{');
        this.messageNumber="";
        if ( msgNumberIdx > -1 ) {
            this.messageNumber = message.substring(msgNumberIdx+1);
            messageBody = message.substring(11,msgNumberIdx);
        } else {
            messageBody = message.substring(11);
        }
        String lcMsg = messageBody.toLowerCase();
        if ( lcMsg.startsWith("ack") ) {
        	isAck = true;
        	this.messageNumber = messageBody.substring(3,messageBody.length());
		this.messageBody = messageBody.substring(0, 3);
        }
        if ( lcMsg.startsWith("rej") ) {
        	isRej = true;
        	this.messageNumber = messageBody.substring(3,messageBody.length());
		this.messageBody = messageBody.substring(0, 3);
        }
    }
    
    /**
     * <p>Constructor for MessagePacket.</p>
     *
     * @param targetCallsign a {@link java.lang.String} object
     * @param messageBody a {@link java.lang.String} object
     * @param messageNumber a {@link java.lang.String} object
     */
    public MessagePacket(String targetCallsign, String messageBody, String messageNumber) {
    	this.messageBody = messageBody;
    	this.targetCallsign = targetCallsign;
    	this.messageNumber = messageNumber;
    	if ( messageBody.equals("ack") ) isAck = true;
    	if ( messageBody.equals("rej") ) isRej = true;
    }
    
    /**
     * <p>Getter for the field <code>messageBody</code>.</p>
     *
     * @return the messageBody
     */
    public String getMessageBody() {
        return this.messageBody;
    }

    /**
     * <p>Setter for the field <code>messageBody</code>.</p>
     *
     * @param messageBody the messageBody to set
     */
    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    /**
     * <p>Getter for the field <code>messageNumber</code>.</p>
     *
     * @return the messageNumber
     */
    public String getMessageNumber() {
        return messageNumber;
    }

    /**
     * <p>Setter for the field <code>messageNumber</code>.</p>
     *
     * @param messageNumber the messageNumber to set
     */
    public void setMessageNumber(String messageNumber) {
        this.messageNumber = messageNumber;
    }

    /**
     * <p>Getter for the field <code>targetCallsign</code>.</p>
     *
     * @return the targetCallsign
     */
    public String getTargetCallsign() {
        return targetCallsign;
    }

    /**
     * <p>Setter for the field <code>targetCallsign</code>.</p>
     *
     * @param targetCallsign the targetCallsign to set
     */
    public void setTargetCallsign(String targetCallsign) {
        this.targetCallsign = targetCallsign;
    }

	/**
	 * <p>isAck.</p>
	 *
	 * @return the isAck
	 */
	public boolean isAck() {
		return isAck;
	}

	/**
	 * <p>setAck.</p>
	 *
	 * @param isAck the isAck to set
	 */
	public void setAck(boolean isAck) {
		this.isAck = isAck;
	}

	/**
	 * <p>isRej.</p>
	 *
	 * @return the isRej
	 */
	public boolean isRej() {
		return isRej;
	}

	/**
	 * <p>setRej.</p>
	 *
	 * @param isRej the isRej to set
	 */
	public void setRej(boolean isRej) {
		this.isRej = isRej;
	}

	
	/** {@inheritDoc} */
    @Override
	public String toString() {
		if (rawBytes != null)
			return new String(rawBytes);
		if ( this.messageBody.equals("ack") || this.messageBody.equals("rej")) {
			return String.format(":%-9s:%s%s", this.targetCallsign, this.messageBody, this.messageNumber);
		} else if (messageNumber.length() > 0) {
			return String.format(":%-9s:%s{%s", this.targetCallsign, this.messageBody, this.messageNumber);
		} else {
			return String.format(":%-9s:%s", this.targetCallsign, this.messageBody);
		}
	}
}
