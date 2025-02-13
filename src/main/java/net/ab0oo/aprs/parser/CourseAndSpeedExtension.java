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
 * <p>CourseAndSpeedExtension class.</p>
 *
 * @author johng
 * @version $Id: $Id
 */
public class CourseAndSpeedExtension extends DataExtension implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * course over ground, in degrees 0-359
	 */
	private int course;
	/**
	 * speed over ground, indicated in miles per hour
	 */
	private int speed;
	/**
	 * <p>Getter for the field <code>course</code>.</p>
	 *
	 * @return the course in degrees true
	 */
	public int getCourse() {
		return course;
	}
	/**
	 * <p>Setter for the field <code>course</code>.</p>
	 *
	 * @param course the course to set in degrees true
	 */
	public void setCourse(int course) {
		this.course = course;
	}
	/**
	 * <p>Getter for the field <code>speed</code>.</p>
	 *
	 * @return the speed in knots
	 */
	public int getSpeed() {
		return speed;
	}
	/**
	 * <p>Setter for the field <code>speed</code>.</p>
	 *
	 * @param speed the speed to set in knots
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "Moving "+speed+" kts @ "+course+" deg";
	}
	
	/** {@inheritDoc} */
	@Override
	public String toSAEString() {
		return "Moving "+Utilities.ktsToMph(speed)+" mph @ "+course+" deg";
	}

	/** {@inheritDoc} */
	@Override
	public APRSExtensions getType() {
		return APRSExtensions.T_COURSESPEED;
	}
}
