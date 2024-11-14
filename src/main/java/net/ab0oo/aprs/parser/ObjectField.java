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

import java.util.Objects;

public class ObjectField extends APRSData {
	private static final long serialVersionUID = 1L;
	protected String objectName;
	protected boolean live = true;
	protected TimeField timestamp;
	protected PositionField position;

	protected ObjectField() {
	}

	/**
	 * @param msgBody byte array of on air message
	 * parse an APRS object message
	 * 
	 * builds an ObjectField instance with the parsed data
	 */
	public ObjectField(byte[] msgBody) throws Exception {
		// first, we get the object name
		this.objectName = new String(msgBody, 1, 9).trim();
		this.live = (msgBody[10] == '*');
		// then we get the timestamp
		this.timestamp = new TimeField(msgBody, 10);
		this.position = new PositionField(msgBody, "FOO", 17);
		this.setLastCursorPosition(36);
	}

	/**
	 * 
	 * @param objectName
	 * @param live
	 * @param position
	 * @param comment
	 * 
	 * build an ObjectField with the parsed data
	 */
	public ObjectField(String objectName, boolean live, Position position, String comment) {
		this.objectName = objectName;
		this.live = live;
		this.comment = comment;
	}

	/**
	 * @return the objectName
	 *
	 * Returns the name of the Object defined in this field
	 */
	public String getObjectName() {
		return objectName;
	}

	/**
	 * @param objectName the objectName to set
	 *
	 * Sets the object name in a constructed object
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	/**
	 * @return true if this object is marked as Live by the owner
	 *
	 * Live-ness state of the object, as sent by the originator
	 */
	public boolean isLive() {
		return live;
	}

	/**
	 * @param live marks whether the object is live
	 *
	 * When constructing an object, this flag indicates the object is live in the network
	 */
	public void setLive(boolean live) {
		this.live = live;
	}


	/**
	 * @return TimeField
	 *
	 * Fetches the APRS Time Field from the Object
	 */
	public TimeField getTimestamp() {
		return timestamp;
	}


	/**
	 * @param timestamp
	 *
	 * Sets the APRS-formatted Time Field for this generated packet
	 */
	public void setTimestamp(TimeField timestamp) {
		this.timestamp = timestamp;
	}


	/**
	 * @return PositionField
	 *
	 * Returns the APRS position of this object (includes symbol table/symbol)
	 */
	public PositionField getPosition() {
		return position;
	}


	/**
	 * @param position
	 *
	 * Sets the position for this object (includes symbol table/symbol)
	 */
	public void setPosition(PositionField position) {
		this.position = position;
	}
	
	/** 
	 * @return String
	 *
	 * Returns a pretty-printed string of this Object
	 */
	@Override
	public String toString() {
		if (rawBytes != null)
			return new String(rawBytes);
		return String.format(";%-9s%c%s", this.objectName, live ? '*' : '_', comment);
	}

	
	/** 
	 * @param o
	 * @return int
	 *
	 * Allows two Object fields to be compared.
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
	
	/** 
	 * @param o
	 * @return boolean
	 *
	 * Returns true if the object passed in is the same as this object
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof ObjectField)) {
			return false;
		}
		ObjectField objectField = (ObjectField) o;
		return Objects.equals(objectName, objectField.objectName) && live == objectField.live;
	}

	
	/** 
	 * @return int
	 *
	 * Calculates a hash for this particular object
	 */
	@Override
	public int hashCode() {
		return Objects.hash(objectName, live);
	}

}
