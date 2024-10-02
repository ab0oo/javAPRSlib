package net.ab0oo.aprs.parser;

import java.util.Objects;

public class PositionField extends APRSData {
	private static final long serialVersionUID = 1L;
	private Position position = new Position(0, 0);
	private String positionSource;
	private boolean compressedFormat;
	DataExtension extension = null;

	public PositionField(byte[] msgBody, String destinationField) throws Exception {
		System.err.println("Executing alternate constructor");
		new PositionField(msgBody, destinationField, 0);
	}

	public PositionField(byte[] msgBody, String destinationField, int cursor) throws Exception {
		super(msgBody);
		positionSource = "Unknown";
		char packetType = (char) msgBody[0];
		this.setHasFault(false);
		try {
			switch (packetType) {
				case '\'':
				case '`': // Possibly MICe
					// (char)packet.length >= 9 ?
					this.type = APRSTypes.T_POSITION;
					this.position = PositionParser.parseMICe(msgBody, destinationField);
					this.extension = DataExtension.parseMICeExtension(msgBody, destinationField);
					this.positionSource = "MICe";
					cursor = 10;
					if (cursor < msgBody.length
							&& (msgBody[cursor] == '>' || msgBody[cursor] == ']' || msgBody[cursor] == '`'))
						cursor++;
					if (cursor < msgBody.length && msgBody[cursor] == '"')
						cursor += 4;
					break;
				case '!':
					// "$ULT..." -- Ultimeter 2000 weather instrument
					if (msgBody[1] == 'U' && msgBody[2] == 'L' && msgBody[3] == 'T') {
						this.type = APRSTypes.T_WX;
						break;
					}
				case '=':
				case '/':
				case '@':
					if (msgBody.length < 10) { // Too short!
						this.setHasFault(true);
						this.setFaultReason("Position packet too short");
					} else {

						// Normal or compressed location packet, with or without
						// timestamp, with or without messaging capability
						// ! and / have messaging, / and @ have a prepended timestamp

						this.type = APRSTypes.T_POSITION;
						char posChar = (char) msgBody[cursor];
						if (validSymTableCompressed(posChar)) { /* [\/\\A-Za-j] */
							// compressed position packet
							this.position = PositionParser.parseCompressed(msgBody, cursor);
							this.extension = DataExtension.parseCompressedExtension(msgBody, cursor);
							this.positionSource = "Compressed";
							cursor += 13;
						} else if ('0' <= posChar && posChar <= '9') {
							// normal uncompressed position
							try {
								this.position = PositionParser.parseUncompressed(msgBody, cursor);
							} catch (Exception ex) {
								this.comment = ex.getMessage();
								System.err.println(ex);
								this.setFaultReason("Failed to parse uncompressed position");
								this.setHasFault(true);
							}
							try {
								this.extension = DataExtension.parseUncompressedExtension(msgBody, cursor);
							} catch (ArrayIndexOutOfBoundsException oobex) {
								this.extension = null;
							}
							this.positionSource = "Uncompressed";
							cursor += 17;
						} else {
							this.positionSource = "Who knows...";
							this.setHasFault(true);
							this.setFaultReason("No one really knows...");
						}
						break;
					}
				case '$':
					if (msgBody.length > 10) {
						this.type = APRSTypes.T_POSITION;
						this.position = PositionParser.parseNMEA(msgBody);
						this.positionSource = "NMEA";
					} else {
						this.setHasFault(true);
						this.setFaultReason("Unable to parse NMEA position");
					}
					break;

			}
			if (null != position && position.getSymbolCode() == '_') {
				// pass
			} else {
				if (cursor > 0 && cursor < msgBody.length) {
					comment = new String(msgBody, cursor, msgBody.length - cursor, "UTF-8");
				}
			}
			this.setLastCursorPosition(cursor);
			compressedFormat = false;
		} catch (Exception ex) {
			this.setHasFault(true);
			this.setFaultReason(" Invalid position format");
			this.comment = this.comment + " INVALID position format.";
			System.err.println(ex);
		}
	}

	public PositionField(Position position, String comment) {
		this.position = position;
		this.type = APRSTypes.T_POSITION;
		// this.comment = comment;
		compressedFormat = false;
	}

	public PositionField(Position position, String comment, boolean msgCapable) {
		this(position, comment);
		// canMessage = msgCapable;
	}

	
	/** 
	 * @param val tells the encoder to compress this packet out output
	 */
	public void setCompressedFormat(boolean val) {
		compressedFormat = val;
	}

	
	/** 
	 * @return boolean returns true if this packet is compressed
	 */
	public boolean getCompressedFormat() {
		return compressedFormat;
	}

	/** 
	 * @return boolean returns true if this packet has a valid symbol
	 */
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
	 * '\\') return true; if ('A' <= c && c <= 'Z') return true; if ('0' <= c && c
	 * <= '9') return true; return false; }
	 * 
	 * public String toString() { return "Latitude:  " + position.getLatitude() +
	 * ", longitude: " + position.getLongitude(); }
	 */

	/**
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * @return DataExtension returns any data extension found in this packet
	 */
	public DataExtension getExtension() {
		return extension;
	}

	/**
	 * @param e data extension to add to this position
	 */
	public void setExtension( DataExtension e) {
		this.extension = e;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("---POSITION---\n");
		sb.append("Position Source\t" + this.positionSource + "\n");
		sb.append("Is Compressed:\t" + this.compressedFormat + "\n");
		sb.append(this.position.toString());
		sb.append("Comment:  " + this.comment + "\n");
		return sb.toString();
	}

	/**
	 * @return the positionSource
	 */
	public String getPositionSource() {
		return positionSource;
	}

	public void setPositionSource(String positionSource) {
		this.positionSource = positionSource;
	}

	public boolean isCompressedFormat() {
		return this.compressedFormat;
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
		if (!(o instanceof PositionField)) {
			return false;
		}
		PositionField positionField = (PositionField) o;
		return Objects.equals(position, positionField.position)
				&& Objects.equals(positionSource, positionField.positionSource)
				&& compressedFormat == positionField.compressedFormat
				&& Objects.equals(extension, positionField.extension);
	}

	@Override
	public int hashCode() {
		return Objects.hash(position, positionSource, compressedFormat, extension);
	}

}
