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

 *  Please note that significant portions of this code were taken from the JAVA FAP
 *  conversion by Matti Aarnio at http://repo.ham.fi/websvn/java-aprs-fap/
 */
package net.ab0oo.aprs;


public class PositionPacket extends InformationField {
	private Position position;

	public PositionPacket(byte[] msgBody, String destinationField)
			throws Exception {
		super(msgBody);
		char packetType = (char) msgBody[0];
		int cursor = 0;
		switch (packetType) {
		case '\'' :
		case '`': // Possibly MICe
			// (char)packet.length >= 9 ?
			type = APRSTypes.T_POSITION;
			position = PositionParser.parseMICe(msgBody, destinationField);
			cursor = 10;
			if (cursor < msgBody.length && (msgBody[cursor] == '>' || msgBody[cursor] == ']' || msgBody[cursor] == '`'))
				cursor++;
			if (cursor < msgBody.length && msgBody[cursor] == '"')
				cursor += 4;
			break;
		case '!':
			if (msgBody[1] == 'U' && // "$ULT..." -- Ultimeter 2000 weather
					// instrument
					msgBody[2] == 'L' && msgBody[3] == 'T') {
				type = APRSTypes.T_WX;
				break;
			}
		case '=':
		case '/':
		case '@':
			if (msgBody.length < 10) { // Too short!
				hasFault = true;
			} else {

				// Normal or compressed location packet, with or without
				// timestamp, with or without messaging capability
				//
				// ! and / have messaging, / and @ have a prepended
				// timestamp

				type = APRSTypes.T_POSITION;
				cursor = 1;

				if (packetType == '/' || packetType == '@') {
					// With a prepended timestamp, jump over it.
					cursor += 7;
				}
				char posChar = (char) msgBody[cursor];
				if (validSymTableCompressed(posChar)) { /* [\/\\A-Za-j] */
					// compressed position packet
					position = PositionParser.parseCompressed(msgBody, cursor);
					cursor += 12;
				} else if ('0' <= posChar && posChar <= '9') {
					// normal uncompressed position
					position = PositionParser.parseUncompressed(msgBody);
					cursor += 19;
				} else {
					hasFault = true;
				}
				break;
			}
		case '$':
			if (msgBody.length > 10) {
				type = APRSTypes.T_POSITION;
				position = PositionParser.parseNMEA(msgBody);
			} else {
				hasFault = true;
			}
			break;

		}
		if (cursor > 0 && cursor < msgBody.length) {
			comment = new String(msgBody, cursor, msgBody.length - cursor, "UTF-8").trim();
		}
	}
	public PositionPacket(Position position, String comment) {
		this.position = position;
		this.type = APRSTypes.T_POSITION;
		this.comment = comment;
	}

	private boolean validSymTableCompressed(char c) {
		if (c == '/' || c == '\\')
			return true;
		if ('A' <= c && c <= 'Z')
			return true;
		if ('a' <= c && c <= 'j')
			return true;
		return false;
	}

	/*
	 * private boolean validSymTableUncompressed(char c) { if (c == '/' || c ==
	 * '\\') return true; if ('A' <= c && c <= 'Z') return true; if ('0' <= c &&
	 * c <= '9') return true; return false; }
	 * 
	 * public String toString() { return "Latitude:  " + position.getLatitude()
	 * + ", longitude: " + position.getLongitude(); }
	 */

	/**
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	public String toString() {
		return "!" + position + comment;
	}
}
