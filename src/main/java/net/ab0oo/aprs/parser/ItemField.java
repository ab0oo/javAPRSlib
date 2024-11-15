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

public class ItemField extends APRSData {
	private static final long serialVersionUID = 1L;
	private boolean live = true;
	private String objectName;

	/**
	 * @param msgBody byte array of the on-air message
	 * @throws Exception if it is unable to parse the item field from the msg
	 * 
	 * parse an APRS item message
	 */
	public ItemField(byte[] msgBody) throws Exception {
		this.rawBytes = msgBody;
		String body = new String(msgBody);
		int name_length = body.indexOf("!") - 1;
		if (name_length < 1 || name_length > 9) {
			name_length = body.indexOf("_");
			if (name_length < 1 || name_length > 9) {
				setHasFault(true);
				setFaultReason("Invalid ITEM packet, missing '!' or '_'.");
				throw new Exception("Invalid ITEM packet, missing '!' or '_'.");
			}
			this.live = false;
		} else
			this.live = true;
		this.objectName = new String(msgBody, 1, name_length).trim();
		int cursor = name_length + 2;
		comment = new String(msgBody, cursor, msgBody.length - cursor, "UTF-8").trim();
		super.setLastCursorPosition(cursor);
	}

	
	/** 
	 * @return String
	 */
	@Override
	public String toString() {
		if (rawBytes != null)
			return new String(rawBytes);
		return ")" + this.objectName + (live ? "!" : "_") + comment;
	}

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

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof ItemField)) {
			return false;
		}
		ItemField itemField = (ItemField) o;
		return live == itemField.live && Objects.equals(objectName, itemField.objectName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(live, objectName);
	}

}
