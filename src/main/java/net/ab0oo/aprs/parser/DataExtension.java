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
/**
 * <p>Abstract DataExtension class.</p>
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
 * @version $Id: $Id
 */
public abstract class DataExtension {
	/**
	 * <p>toSAEString.</p>
	 *
	 * @return a {@link java.lang.String} object
	 */
	public abstract String toSAEString();
	/**
	 * <p>getType.</p>
	 *
	 * @return a {@link net.ab0oo.aprs.parser.APRSExtensions} object
	 */
	public abstract APRSExtensions getType();

	/**
	 * <p>parseUncompressedExtension.</p>
	 *
	 * @param msgBody an array of {@link byte} objects
	 * @param cursor a int
	 * @return a {@link net.ab0oo.aprs.parser.DataExtension} object
	 * @throws java.lang.Exception if any.
	 */
	public static DataExtension parseUncompressedExtension(byte[] msgBody, int cursor) throws Exception {
        DataExtension de = null;
        // since the symbol code is position (cursor + 18), we start looking for
        // extensions at position 19
        if (msgBody.length <= 18 + cursor) {
            return null;
        }
        if ((char) msgBody[19 + cursor] == 'P' && (char) msgBody[20 + cursor] == 'H'
                && (char) msgBody[21 + cursor] == 'G') {
            PHGExtension phg = new PHGExtension();
            try {
                phg.setPower(Integer.parseInt(new String(msgBody, 22 + cursor, 1)));
                phg.setHeight(Integer.parseInt(new String(msgBody, 23 + cursor, 1)));
                phg.setGain(Integer.parseInt(new String(msgBody, 24 + cursor, 1)));
                phg.setDirectivity(Integer.parseInt(new String(msgBody, 25 + cursor, 1)));
                de = phg;
            } catch (NumberFormatException nfe) {
                de = null;
            }
        } else if ((char) msgBody[22 + cursor] == '/' && (char) msgBody[18 + cursor] != '_') {
            CourseAndSpeedExtension cse = new CourseAndSpeedExtension();

            String courseString = new String(msgBody, cursor + 19, 3);
            String speedString = new String(msgBody, cursor + 23, 3);
            int course = 0;
            int speed = 0;
            try {
                course = Integer.parseInt(courseString);
                speed = Integer.parseInt(speedString);
            } catch (NumberFormatException nfe) {
                course = 0;
                speed = 0;
                // System.err.println("Unable to parse course "+courseString+" or speed "+
                // speedString+" into a valid course/speed for CS Extension from "+new String(msgBody));
            }
            cse.setCourse(course);
            cse.setSpeed(speed);
            de = cse;
        }
        return de;
    }

	/**
	 * <p>parseMICeExtension.</p>
	 *
	 * @param msgBody an array of {@link byte} objects
	 * @param destinationField a {@link java.lang.String} object
	 * @return a {@link net.ab0oo.aprs.parser.CourseAndSpeedExtension} object
	 * @throws java.lang.Exception if any.
	 */
	public static CourseAndSpeedExtension parseMICeExtension(byte msgBody[], String destinationField) throws Exception {
        CourseAndSpeedExtension cse = new CourseAndSpeedExtension();
        int sp = msgBody[1 + 3] - 28;
        int dc = msgBody[1 + 4] - 28;
        int se = msgBody[1 + 5] - 28;
        // Decoded according to Chap 10, p 52 of APRS Spec 1.0
        int speed = sp * 10;
        int q = (int) (dc / 10);
        speed += q;
        int r = (int) (dc % 10) * 100;
        int course = r + se;
        if (course >= 400)
            course -= 400;
        if (speed >= 800)
            speed -= 800;
        cse.setSpeed(speed);
        cse.setCourse(course);
        return cse;
    }

	/**
	 * <p>parseCompressedExtension.</p>
	 *
	 * @param msgBody an array of {@link byte} objects
	 * @param cursor a int
	 * @return a {@link net.ab0oo.aprs.parser.DataExtension} object
	 * @throws java.lang.Exception if any.
	 */
	public static DataExtension parseCompressedExtension(byte[] msgBody, int cursor) throws Exception {
        DataExtension de = null;
        if (msgBody[cursor + 9] == '_') {
            // this is a weather report packet, and thus has no extension
            return null;
        }
        int t = (char) msgBody[cursor + 12] - 33;
        int nmeaSource = (t & 0x18) >> 3;
        if (nmeaSource == 2) {
            // this message came from a GPGGA sentance, and therefore has altitude
            return null;
        }
        if ((char) msgBody[cursor + 11] == ' ') {
            // another special case, where cs is ignored
            // we see this in Compressed Position packets with no cs
            return null;
        }
        int c = (char) msgBody[cursor + 11] - 33;

        if (c < 90) {
            // this is a compressed course/speed value
            int s = (char) msgBody[cursor + 11] - 33;
            CourseAndSpeedExtension cse = new CourseAndSpeedExtension();
            cse.setCourse(c * 4);
            cse.setSpeed((int) Math.round(Math.pow(1.08, s) - 1));
            de = cse;
        } else if (c == (char) ('{')) {
            int s = (char) msgBody[cursor + 11] - 33;
            s = (int) Math.round(2 * Math.pow(1.08, s));
            RangeExtension re = new RangeExtension(s);
            de = re;
        }
        return de;
    }

}
