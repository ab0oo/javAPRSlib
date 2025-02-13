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

/**
 * <p>ItemField class.</p>
 *
 * @author john
 * @version $Id: $Id
 */
public class ItemField extends APRSData {
	private static final long serialVersionUID = 1L;
	/**
	 * flag indicating if this Item is "live" or has been killed
	 */
	private boolean live = true;
	/**
	 * String indicating the name of this item, as assigned by the originator
	 */
	private String itemName;

	/**
	 * <p>Constructor for ItemField.</p>
	 *
	 * @param msgBody byte array of the on-air message
	 * @throws java.lang.Exception if it is unable to parse the item field from the msg
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
		this.itemName = new String(msgBody, 1, name_length).trim();
		int cursor = name_length + 2;
		comment = new String(msgBody, cursor, msgBody.length - cursor, "UTF-8").trim();
		super.setLastCursorPosition(cursor);
	}

	/**
	 * <p>Getter for the field <code>itemName</code>.</p>
	 *
	 * @return name of the item in this ItemField
	 * This is the getter for the Item Name
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * <p>Setter for the field <code>itemName</code>.</p>
	 *
	 * @param itemName the item name to set
	 * This is the setter for the item name for this ItemField
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		if (rawBytes != null)
			return new String(rawBytes);
		return ")" + this.itemName + (live ? "!" : "_") + comment;
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

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof ItemField)) {
			return false;
		}
		ItemField itemField = (ItemField) o;
		return live == itemField.live && Objects.equals(itemName, itemField.itemName);
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return Objects.hash(live, itemName);
	}

}
