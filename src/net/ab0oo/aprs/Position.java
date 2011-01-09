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
 */
package net.ab0oo.aprs;

import java.util.Date;
/**
 * 
 * @author johng
 * This class represents a Position as specified by the APRS specification.  This includes
 * a symbol table and actual symbol, and a possible timestamp.
 *
 */
public class Position {
	private double latitude = 0d, longitude = 0d;
	private int altitude = -1;
	private int positionAmbiguity;
	private Date timestamp;
	private char symbolTable, symbolCode;

	public Position() {}
	
	public Position(double lat, double lon, int posAmb, char st, char sc) {
		this.latitude = Math.round(lat * 100000) * 0.00001D;
		this.longitude = Math.round(lon * 100000) * 0.00001D;
		this.positionAmbiguity = posAmb;
		this.symbolTable = st;
		this.symbolCode = sc;
		this.timestamp = new Date();
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the altitude
	 */
	public int getAltitude() {
		return altitude;
	}

	/**
	 * @param altitude the altitude to set
	 */
	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	/**
	 * @return the positionAmbiguity
	 */
	public int getPositionAmbiguity() {
		return positionAmbiguity;
	}

	/**
	 * @param positionAmbiguity the positionAmbiguity to set
	 */
	public void setPositionAmbiguity(int positionAmbiguity) {
		this.positionAmbiguity = positionAmbiguity;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the symbolTable
	 */
	public char getSymbolTable() {
		return symbolTable;
	}

	/**
	 * @param symbolTable the symbolTable to set
	 */
	public void setSymbolTable(char symbolTable) {
		this.symbolTable = symbolTable;
	}

	/**
	 * @return the symbolCode
	 */
	public char getSymbolCode() {
		return symbolCode;
	}

	/**
	 * @param symbolCode the symbolCode to set
	 */
	public void setSymbolCode(char symbolCode) {
		this.symbolCode = symbolCode;
	}
	
	public static String getDMS(double dd, boolean latitude) {
			// define variables local to this method
			double dfFrac;			// fraction after decimal
			double dfSec;			// fraction converted to seconds

			// Get degrees by chopping off at the decimal
			Double dfDegree = Math.floor( dd );
			// correction required since floor() is not the same as int()
			if ( dfDegree < 0 )
				dfDegree = dfDegree + 1;
			// Get fraction after the decimal
			dfFrac = Math.abs( dd - dfDegree );
			// Convert this fraction to seconds (without minutes)
			dfSec = dfFrac * 3600;
			// Determine number of whole minutes in the fraction
			Double dfMinute = Math.floor( dfSec / 60 );
			// Put the remainder in seconds
			Double dfSecond = dfSec - dfMinute * 60;
			// Fix round off errors
			if ( Math.rint( dfSecond ) == 60 ) {
				dfMinute = dfMinute + 1;
				dfSecond = 0d;
			}
			if ( Math.rint( dfMinute ) == 60 ) {
				if ( dfDegree < 0 )
					dfDegree = dfDegree - 1;
				else // ( dfDegree => 0 )
					dfDegree = dfDegree + 1;

				dfMinute = 0d;
			}
			dfDegree = Math.abs(dfDegree);
			if ( latitude ) {
				return String.format("%02.0f%02.0f.%02.0f%s", dfDegree,dfMinute,dfSecond, ( dd < 0 ? "S" : "N"));
			} else {
				return String.format("%03.0f%02.0f.%02.0f%s", dfDegree,dfMinute,dfSecond, ( dd < 0 ? "W" : "E"));
			}
	}
	
	public String toString() {
		return getDMS(latitude,true)+symbolTable+getDMS(longitude,false)+symbolCode;
	}
	
	public String toCompressedString() {
		long latbase = Math.round(380926 * (90-this.latitude));
		long latchar1 = latbase / (91*91*91)+33;
		latbase = latbase % (91*91*91);
		long latchar2 = latbase / (91*91)+33;
		latbase = latbase % (91*91);
		int latchar3 = (int)(latbase / 91)+33;
		int latchar4 = (int)(latbase % 91)+33;
		long lonbase = Math.round(190463 * (180+this.longitude));
		long lonchar1 = lonbase / (91*91*91)+33;
		lonbase %= (91*91*91);
		long lonchar2 = lonbase / (91*91)+33;
		lonbase = lonbase % (91*91);
		int lonchar3 = (int)(lonbase / 91)+33;
		int lonchar4 = (int)(lonbase % 91)+33;
		
		return ""+symbolTable+(char)latchar1+(char)latchar2+(char)latchar3+(char)latchar4+
				""+(char)lonchar1+(char)lonchar2+(char)lonchar3+(char)lonchar4+symbolCode+" sT";
	}
	
	public static void main(String[] args) {
		Position pos = new Position();
		pos.setLatitude(34.12558);
		pos.setLongitude(-84.13697);
		pos.setSymbolCode('o');
		pos.setSymbolTable('/');
		System.out.println("latitude is "+pos.getLatitude());
		System.out.println("Position string is "+pos.toString());
		System.out.println("Compressed string is "+pos.toCompressedString());
	}

}