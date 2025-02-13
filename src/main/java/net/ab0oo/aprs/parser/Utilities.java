/*
 * javAPRSlib - https://github.com/ab0oo/javAPRSlib
 *
 * Copyright (C) 2011, 2024 John Gorkos, AB0OO
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
package net.ab0oo.aprs.parser;

/**
 * <p>Utilities class.</p>
 *
 * @author johng
 * @version $Id: $Id
 */
public class Utilities {
	
	/**
	 * <p>main.</p>
	 *
	 * @param args an array of {@link java.lang.String} objects
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage:  AprsPass <callsign>");
			System.exit(1);
		}
	}

	
	/**
	 * <p>doHash.</p>
	 *
	 * @param callSign a {@link java.lang.String} object
	 * @return int
	 */
	public static int doHash(String callSign) {
		short kKey = 0x73e2; // Straight from Steme Dimse himself
		if (callSign.indexOf('-') > 0) {
			callSign = callSign.substring(0, callSign.indexOf('-'));
		}
		callSign = callSign.toUpperCase();
		short i = 0;
		int hash = kKey;
		int len = callSign.length();
		while (i < len) {
			hash ^= callSign.charAt(i) << 8;
			if (i + 1 < len) {
				hash ^= callSign.charAt(i + 1);
			}
			i += 2;
		}
		int code = hash & 0x7FFF;
		return code;
	}
	
	
	/**
	 * <p>ktsToMph.</p>
	 *
	 * @param knots a int
	 * @return int
	 */
	public static int ktsToMph(int knots) {
		return (int)Math.round(knots * 1.15077945);
	}
	
	
	/**
	 * <p>kntsToKmh.</p>
	 *
	 * @param knots a int
	 * @return int
	 */
	public static int kntsToKmh(int knots) {
		return (int)Math.round(knots*1.852);
	}
	
	
	/**
	 * <p>metersToMiles.</p>
	 *
	 * @param meters a double
	 * @return double
	 */
	public static double metersToMiles(double meters) {
		return meters * 0.000621371192;
	}
	
	
	/**
	 * <p>metersToKilometers.</p>
	 *
	 * @param meters a double
	 * @return double
	 */
	public static double metersToKilometers(double meters) {
		return meters / 1000.0;
	}
	
	
	/**
	 * <p>degressToCardinal.</p>
	 *
	 * @param degrees a double
	 * @return String
	 */
	public static String degressToCardinal(double degrees) {
		if ( degrees >= 11.25 && degrees < 33.75 ) return "NNE";
		if ( degrees >= 33.75 && degrees < 56.25 ) return "NE";
		if ( degrees >= 56.25 && degrees < 78.25 ) return "ENE";
		if ( degrees >= 78.25 && degrees < 101.25 ) return "E";
		if ( degrees >= 101.25 && degrees < 123.75 ) return "ESE";
		if ( degrees >= 123.75 && degrees < 146.25 ) return "SE";
		if ( degrees >= 146.25 && degrees < 168.75 ) return "SSE";
		if ( degrees >= 168.75 && degrees < 191.25 ) return "S";
		if ( degrees >= 191.25 && degrees < 213.75 ) return "SSW";
		if ( degrees >= 213.75 && degrees < 236.25 ) return "SW";
		if ( degrees >= 236.25 && degrees < 258.75 ) return "WSW";
		if ( degrees >= 258.75 && degrees < 281.25 ) return "W";
		if ( degrees >= 281.25 && degrees < 303.75 ) return "WNW";
		if ( degrees >= 303.75 && degrees < 326.25 ) return "NW";
		if ( degrees >= 326.25 && degrees < 348.75 ) return "NNW";
		return "N";
	}
}
