package net.ab0oo.aprs.parser;

import java.util.Objects;

public class ObjectField extends APRSData {
	private static final long serialVersionUID = 1L;
	protected String objectName;
	protected boolean live = true;

	protected ObjectField() {
	}

	/**
	 * parse an APRS object message
	 * 
	 * @return new ObjectPacket instance with the parsed data
	 */
	public ObjectField(byte[] msgBody) throws Exception {
		this.objectName = new String(msgBody, 1, 9).trim();
		this.live = (msgBody[10] == '*');
		this.setLastCursorPosition(10);
	}

	public ObjectField(String objectName, boolean live, Position position, String comment) {
		this.objectName = objectName;
		this.live = live;
		this.comment = comment;
	}

	/**
	 * @return the objectName
	 */
	public String getObjectName() {
		return objectName;
	}

	/**
	 * @param objectName the objectName to set
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	/**
	 * @return the live
	 */
	public boolean isLive() {
		return live;
	}

	/**
	 * @param live the live to set
	 */
	public void setLive(boolean live) {
		this.live = live;
	}

	@Override
	public String toString() {
		if (rawBytes != null)
			return new String(rawBytes);
		return String.format(";%-9s%c%s%s", this.objectName, live ? '*' : '_', comment);
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
	public boolean hasFault() {
		return this.hasFault;
	}

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

	@Override
	public int hashCode() {
		return Objects.hash(objectName, live);
	}

}
