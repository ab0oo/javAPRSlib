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
 *
 *  This code lifted blatantly and directly from the JAVA FAP translation done
 *  by Matti Aarnio at http://repo.ham.fi/websvn/java-aprs-fap/
 */

package net.ab0oo.aprs.parser;

/**
 * <p>APRSTypes class.</p>
 *
 * @author john
 * @version $Id: $Id
 */
public enum APRSTypes {
	/**
	 * Unspecified messages
	 */
	T_UNSPECIFIED,
	/**
	 * Timestamp, Spec Ch 6
	 */
	T_TIMESTAMP,
	/**
	 * Position, Spec Ch 6, includes SYMBOL
	 */
	T_POSITION,
	/**
	 * Weather report, Spec Ch 12
	 */
	T_WX,
	/**
	 * Third party packets, Spec Ch 17
	 */
	T_THIRDPARTY,
	/**
	 * Query packets, Spec Ch 15
	 */
	T_QUERY,
	/**
	 * Object report, Spec Ch 11
	 */
	T_OBJECT,
	/**
	 * Item report, Spec Ch 11
	 */
	T_ITEM,
	/**
	 * Normal APRS Message
	 */
	T_NORMAL,
	/**
	 * Object kill command, Spec Ch 11
	 */
	T_KILL,
	/**
	 * Station status message, Spec Ch 16
	 */
	T_STATUS,
	/**
	 * Station capabilities request/response, Spec Ch 15
	 */
	T_STATCAPA,
	/**
	 * Telemetry message, Spec Ch 13
	 */
	T_TELEMETRY,
	/**
	 * User defined format, Spec Ch 18
	 */
	T_USERDEF,
	/**
	 * Station-to-station message, Spec Ch 14
	 */
	T_MESSAGE,
	/**
	 * Specific to FAP, ask Matti.  :)
	 */
	T_NWS; // Used on fap.getSubtype()

	;
}
