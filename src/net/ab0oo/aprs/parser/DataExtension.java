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
package net.ab0oo.aprs.parser;
/**
 * 
 * @author johng
 * Abstract class that encapsulates the possible Data Extensions to APRS packets.  These include
 * Course and Speed
 * Power, Effective Antenna Heigh/Gain/Directivity
 * Pre-calculated Radio Range
 * Omni DF Signal Strength
 * Storm Data
 * Bearing and Number/Range/Quality
 * Area Objects
 * Wind Direction and Speed
 */
public abstract class DataExtension {
	public abstract String toSAEString();
}
