package net.ab0oo.aprs;

public class ObjectPacket extends InformationField {
	private String objectName;
	private boolean live = true;
	private Position position;

	/** parse an APRS object message
	 * @return new ObjectPacket instance with the parsed data
	 */
	public ObjectPacket(byte[] msgBody) throws Exception {
		this.objectName = new String(msgBody, 1, 9).trim();
		this.live = (msgBody[10] == '*');
		int cursor = 18;
		this.position = PositionParser.parseUncompressed(msgBody, 18);
		cursor += 19;
		comment = new String(msgBody, cursor, msgBody.length - cursor, "UTF-8").trim();
	}

	public ObjectPacket( String objectName, boolean live, Position position, String comment) {
		this.objectName = objectName;
		this.live = live;
		this.position = position;
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
	 * @param extension the extension to set
	 */
	public void setExtension(DataExtension extension) {
		this.extension = extension;
	}

	public String toString() {
		return ")"+this.objectName+( live ? "!":"_")+position.toString()+comment;
	}
}
