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
 * <p>PHGExtension class.</p>
 *
 * @author john
 * @version $Id: $Id
 */
public class PHGExtension extends DataExtension implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final int[] powerCodes = {0,1,4,9,16,25,36,49,64,81};
	private static final int[] heightCodes = {10,20,40,80,160,320,640,1280,2560,5120};
	private static final int[] gainCodes = {0,1,2,3,4,5,6,7,8,9};
	private static final int[] directivityCodes = {0,45,90,135,180,225,270,315,360,0};
	
	/**
	 * Index in to the Power Codes table, indicating the output power of the originating station
	 */
	private int power;
	/**
	 * Index into the Height Codes Table, indicating the Height Above Average Terrain of the
	 * originating station
	 */
	private int height;
	/**
	 * Index into the Gain Codes table, indicating the antenna gain of the originating station
	 */
	private int gain;
	/**
	 * Index into the Directivity Table, indicating the primary lobe of the transmitting antenna
	 */
	private int directivity;

	/**
	 * <p>Getter for the field <code>power</code>.</p>
	 *
	 * @return the power
	 */
	public int getPower() {
		return power;
	}
	/**
	 * <p>Setter for the field <code>power</code>.</p>
	 *
	 * @param power the power to set
	 */
	public void setPower(int power) {
		this.power = powerCodes[power];
	}
	/**
	 * <p>Getter for the field <code>height</code>.</p>
	 *
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * <p>Setter for the field <code>height</code>.</p>
	 *
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = heightCodes[height];
	}
	/**
	 * <p>Getter for the field <code>gain</code>.</p>
	 *
	 * @return the gain
	 */
	public int getGain() {
		return gain;
	}
	/**
	 * <p>Setter for the field <code>gain</code>.</p>
	 *
	 * @param gain the gain to set
	 */
	public void setGain(int gain) {
		this.gain = gainCodes[gain];
	}
	/**
	 * <p>Getter for the field <code>directivity</code>.</p>
	 *
	 * @return the directivity
	 */
	public int getDirectivity() {
		return directivity;
	}
	/**
	 * <p>Setter for the field <code>directivity</code>.</p>
	 *
	 * @param directivity the directivity to set
	 */
	public void setDirectivity(int directivity) {
		this.directivity = directivityCodes[directivity];
	}
	
	/** {@inheritDoc} */
	@Override
	public APRSExtensions getType() {
		return APRSExtensions.T_PHG;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toSAEString() {
		return power+" watts at "+height+" ft HAAT with "+gain+" dBi gain directed at "+directivity+" degress";
	}
}
