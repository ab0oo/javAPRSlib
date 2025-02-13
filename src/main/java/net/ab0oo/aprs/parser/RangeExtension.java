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
 * <p>RangeExtension class.</p>
 *
 * @author johng
 * @version $Id: $Id
 */
public class RangeExtension extends DataExtension implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * computed radio range, in miles, from the originating station
	 */
	private int range;
	
	/**
	 * <p>Constructor for RangeExtension.</p>
	 *
	 * @param range range in miles
	 * build a new Range extension, which tells other stations the range in miles of this station
	 * n.b. the spec does not indicate if this is transmission range, receiver coverage, or
	 * bidrectional.  Use at your own risk.
	 */
	public RangeExtension( int range ) {
		this.setRange(range);
	}

	/**
	 * <p>Setter for the field <code>range</code>.</p>
	 *
	 * @param range the range to set
	 */
	public void setRange(int range) {
		this.range = range;
	}

	/**
	 * <p>Getter for the field <code>range</code>.</p>
	 *
	 * @return the range
	 */
	public int getRange() {
		return range;
	}
	
	/** {@inheritDoc} */
	@Override
	public APRSExtensions getType() {
		return APRSExtensions.T_RADIORANGE;
	}

	/** {@inheritDoc} */
	@Override
	public String toSAEString() {
		return "Range of "+range+" miles";
	}

}
