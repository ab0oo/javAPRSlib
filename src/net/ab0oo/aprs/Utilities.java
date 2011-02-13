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

/**
 * @author johng
 * 
 */
public class Utilities {
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage:  AprsPass <callsign>");
			System.exit(1);
		}
	}

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
	
	public static int ktsToMph(int knots) {
		return (int)Math.round(knots * 1.15077945);
	}
}
