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

import java.io.Serializable;

/**
 * @author johng
 *
 */
public class RangeExtension extends DataExtension implements Serializable {
	private static final long serialVersionUID = 1L;
	private int range;
	
	/**
	 * @param range range in miles
	 * build a new Range extension, which tells other stations the range in miles of this station
	 * n.b. the spec does not indicate if this is transmission range, receiver coverage, or
	 * bidrectional.  Use at your own risk.
	 */
	public RangeExtension( int range ) {
		this.setRange(range);
	}

	/**
	 * @param range the range to set
	 */
	public void setRange(int range) {
		this.range = range;
	}

	/**
	 * @return the range
	 */
	public int getRange() {
		return range;
	}
	
	/**
	 * @return Enum indicating the data type extention
	*/
	@Override
	public APRSExtensions getType() {
		return APRSExtensions.T_RADIORANGE;
	}

	/** 
	 * @return String
	 */
	@Override
	public String toSAEString() {
		return "Range of "+range+" miles";
	}

}
